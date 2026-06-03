import Foundation
import WatchConnectivity

/// Manages bidirectional WCSession communication between the watch app and the iOS host.
/// Acts as the single source of truth for session state and telemetry pushed from the phone.
class WatchConnectivityManager: NSObject, ObservableObject, WCSessionDelegate {
    static let shared = WatchConnectivityManager()

    @Published var isSessionActive: Bool = false
    @Published var currentSpeed: Double = 0.0
    @Published var activeCalories: Int = 0

    private override init() {
        super.init()
        guard WCSession.isSupported() else { return }
        WCSession.default.delegate = self
        WCSession.default.activate()
    }

    // MARK: - Public Commands (watch → phone)

    func sendStartSession() {
        isSessionActive = true
        send(["command": "START_SESSION"])
    }

    func sendStopSession() {
        isSessionActive = false
        send(["command": "STOP_SESSION"])
    }

    // MARK: - WCSessionDelegate — required on watchOS

    func session(_ session: WCSession,
                 activationDidCompleteWith activationState: WCSessionActivationState,
                 error: Error?) {
        if let error = error {
            print("[WCSession] Activation failed: \(error.localizedDescription)")
        } else {
            print("[WCSession] Activated — state: \(activationState.rawValue)")
        }
    }

    /// Required on watchOS — called when the paired iPhone becomes temporarily unavailable.
    func sessionDidBecomeInactive(_ session: WCSession) {
        print("[WCSession] Session became inactive")
    }

    /// Required on watchOS — called after sessionDidBecomeInactive.
    /// Re-activate to support Watch handoff scenarios.
    func sessionDidDeactivate(_ session: WCSession) {
        print("[WCSession] Session deactivated — reactivating")
        WCSession.default.activate()
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
            // Phone not reachable in real-time — queue via application context
            do {
                try activeSession.updateApplicationContext(message)
            } catch {
                print("[WCSession] updateApplicationContext failed: \(error)")
            }
        }
    }
}
