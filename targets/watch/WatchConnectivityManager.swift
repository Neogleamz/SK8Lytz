import Foundation
import WatchConnectivity
import WatchKit
import ClockKit
import os

/// Manages bidirectional WCSession communication between the watch app and the iOS host.
/// Acts as the single source of truth for session state and telemetry pushed from the phone.
class WatchConnectivityManager: NSObject, ObservableObject, WCSessionDelegate {
    static let shared = WatchConnectivityManager()

    @Published var isSessionActive: Bool = false
    @Published var isPaused: Bool = false
    @Published var currentSpeed: Double = 0.0
    @Published var activeCalories: Int = 0
    /// Phone-authoritative session start timestamp — drives elapsed timer on watch.
    /// Nil when no session is active.
    @Published var sessionStartTime: Date?

    // ── Post-session summary state ────────────────────────────────────────
    @Published var showingSummary: Bool = false
    @Published var summaryDurationSec: Int = 0
    @Published var summaryDistanceMiles: Double = 0.0
    @Published var summaryAvgSpeed: Double = 0.0
    @Published var summaryCalories: Int = 0
    @Published var summaryPeakHR: Int = 0
    private var summaryDismissTimer: Timer?

    /// Timer that periodically sends watch-side health data back to the phone
    private var healthRelayTimer: Timer?

    private override init() {
        super.init()
        guard WCSession.isSupported() else { return }
        WCSession.default.delegate = self
        WCSession.default.activate()
    }

    // MARK: - Public Commands (watch → phone)

    @MainActor
    func sendStartSession() {
        isSessionActive = true
        isPaused = false
        WKInterfaceDevice.current().play(.start)
        send(["command": "START_SESSION"])
        startHealthRelay()
    }

    @MainActor
    func sendStopSession() {
        isSessionActive = false
        isPaused = false
        WKInterfaceDevice.current().play(.stop)
        send(["command": "STOP_SESSION"])
        stopHealthRelay()
    }

    // MARK: - WCSessionDelegate

    func session(_ session: WCSession,
                 activationDidCompleteWith activationState: WCSessionActivationState,
                 error: Error?) {
        let logger = Logger(subsystem: "com.neogleamz.sk8lytz", category: "WatchConnectivity")
        if let error = error {
            logger.error("[WCSession] Activation failed: \(error.localizedDescription)")
        } else {
            logger.notice("[WCSession] Activated — state: \(activationState.rawValue)")
        }
    }

    // MARK: - Receive updates from phone (phone → watch)

    func session(_ session: WCSession,
                 didReceiveApplicationContext applicationContext: [String: Any]) {
        DispatchQueue.main.async { self.handlePayload(applicationContext) }
    }

    func session(_ session: WCSession, didReceiveMessage message: [String: Any]) {
        DispatchQueue.main.async { self.handlePayload(message) }
    }

    // MARK: - Private

    @MainActor
    private func handlePayload(_ payload: [String: Any]) {
        if let status = payload["status"] as? String {
            switch status {
            case "ACTIVE":
                // Resume normal session view
                isSessionActive = true
                isPaused = false
                showingSummary = false
                summaryDismissTimer?.invalidate()
                summaryDismissTimer = nil
            case "PAUSED":
                isSessionActive = true
                isPaused = true
            case "SUMMARY":
                // Capture metrics and show the summary card for 10 seconds
                isSessionActive = false
                isPaused = false
                summaryDurationSec    = payload["totalDuration"] as? Int    ?? 0
                summaryDistanceMiles  = payload["distance"]     as? Double  ?? 0.0
                summaryAvgSpeed       = payload["avgSpeed"]     as? Double  ?? 0.0
                summaryCalories       = payload["calories"]     as? Int     ?? 0
                summaryPeakHR         = payload["peakHR"]       as? Int     ?? 0
                sessionStartTime = nil   // Stop elapsed timer immediately
                showingSummary = true
                // Auto-dismiss after 10 seconds (matches phone's setTimeout + STOPPED push)
                summaryDismissTimer?.invalidate()
                summaryDismissTimer = Timer.scheduledTimer(withTimeInterval: 10.0, repeats: false) { [weak self] _ in
                    DispatchQueue.main.async { self?.dismissSummary() }
                }
            default:  // "STOPPED" or unknown
                isSessionActive = false
                isPaused = false
                sessionStartTime = nil
                showingSummary = false
                summaryDismissTimer?.invalidate()
                summaryDismissTimer = nil
            }
        }
        // Parse phone-authoritative start timestamp (ISO-8601 UTC)
        if let startISO = payload["startTime"] as? String, !startISO.isEmpty {
            let formatter = ISO8601DateFormatter()
            if let date = formatter.date(from: startISO) {
                sessionStartTime = date
            }
        }
        // Only update live display vars from ACTIVE/PAUSED payloads —
        // SUMMARY reuses the same field names for final metric values so we guard here
        // to avoid clobbering the live session display state.
        if !showingSummary {
            if let speed = payload["speed"] as? Double {
                currentSpeed = speed
                // Reload complications so the speed gauge reflects the new value immediately
                reloadComplications()
            }
            // Calories may arrive as Int or Double depending on source
            if let calories = payload["calories"] as? Double {
                activeCalories = Int(calories)
            } else if let calories = payload["calories"] as? Int {
                activeCalories = calories
            }
        }
    }

    /// Tells ClockKit to refresh all active SK8Lytz complications with the latest data.
    private func reloadComplications() {
        let server = CLKComplicationServer.sharedInstance()
        guard let active = server.activeComplications, !active.isEmpty else { return }
        for complication in active {
            server.reloadTimeline(for: complication)
        }
    }

    /// Dismisses the summary card and resets summary state.
    @MainActor
    func dismissSummary() {
        showingSummary = false
        summaryDismissTimer?.invalidate()
        summaryDismissTimer = nil
    }

    private func send(_ message: [String: Any]) {
        let activeSession = WCSession.default
        if activeSession.isReachable {
            activeSession.sendMessage(message, replyHandler: nil) { error in
                print("[WCSession] sendMessage failed: \(error.localizedDescription)")
            }
        } else {
            do {
                var newContext = activeSession.applicationContext
                for (key, value) in message {
                    newContext[key] = value
                }
                try activeSession.updateApplicationContext(newContext)
            } catch {
                print("[WCSession] updateApplicationContext failed: \(error)")
            }
        }
    }

    // MARK: - Health Data Relay (watch → phone)

    /// Starts a 5-second timer that periodically relays watch-side health data
    /// (heart rate + calories from HealthKit) back to the phone.
    private func startHealthRelay() {
        stopHealthRelay()
        healthRelayTimer = Timer.scheduledTimer(withTimeInterval: 5.0, repeats: true) { [weak self] _ in
            guard let self = self else { return }
            let hr = HealthManager.shared.currentHeartRate
            let cal = HealthManager.shared.activeCalories
            guard hr > 0 || cal > 0 else { return }
            self.send([
                "healthUpdate": true,
                "heartRate": Int(hr),
                "calories": Int(cal)
            ])
        }
    }

    private func stopHealthRelay() {
        healthRelayTimer?.invalidate()
        healthRelayTimer = nil
    }
}
