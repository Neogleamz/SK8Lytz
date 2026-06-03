import Foundation
import WatchConnectivity
import WatchKit

/// Manages bidirectional WCSession communication between the watch app and the iOS host.
/// Acts as the single source of truth for session state and telemetry pushed from the phone.
class WatchConnectivityManager: NSObject, ObservableObject, WCSessionDelegate {
    static let shared = WatchConnectivityManager()

    @Published var isSessionActive: Bool = false
    @Published var currentSpeed: Double = 0.0
    @Published var activeCalories: Int = 0

    /// Timer that periodically sends watch-side health data back to the phone
    private var healthRelayTimer: Timer?

    private override init() {
        super.init()
        guard WCSession.isSupported() else { return }
        WCSession.default.delegate = self
        WCSession.default.activate()
    }

    // MARK: - Public Commands (watch → phone)

    func sendStartSession() {
        isSessionActive = true
        WKInterfaceDevice.current().play(.start)
        send(["command": "START_SESSION"])
        startHealthRelay()
    }

    func sendStopSession() {
        isSessionActive = false
        WKInterfaceDevice.current().play(.stop)
        send(["command": "STOP_SESSION"])
        stopHealthRelay()
    }

    // MARK: - WCSessionDelegate

    func session(_ session: WCSession,
                 activationDidCompleteWith activationState: WCSessionActivationState,
                 error: Error?) {
        if let error = error {
            print("[WCSession] Activation failed: \(error.localizedDescription)")
        } else {
            print("[WCSession] Activated — state: \(activationState.rawValue)")
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

    private func handlePayload(_ payload: [String: Any]) {
        if let status = payload["status"] as? String {
            isSessionActive = (status == "ACTIVE")
        }
        if let speed = payload["speed"] as? Double {
            currentSpeed = speed
        }
        // Calories may arrive as Int or Double depending on source (Health Connect sends Double)
        if let calories = payload["calories"] as? Double {
            activeCalories = Int(calories)
        } else if let calories = payload["calories"] as? Int {
            activeCalories = calories
        }
    }

    private func send(_ message: [String: Any]) {
        let activeSession = WCSession.default
        if activeSession.isReachable {
            activeSession.sendMessage(message, replyHandler: nil) { error in
                print("[WCSession] sendMessage failed: \(error.localizedDescription)")
            }
        } else {
            do {
                try activeSession.updateApplicationContext(message)
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
