import Foundation
import WatchConnectivity
import Combine

class WatchConnectivityManager: NSObject, ObservableObject, WCSessionDelegate {
    static let shared = WatchConnectivityManager()
    
    @Published var isSessionActive: Bool = false
    @Published var currentSpeed: Double = 0.0
    @Published var activeCalories: Int = 0
    
    private var session: WCSession?
    
    private override init() {
        super.init()
        if WCSession.isSupported() {
            session = WCSession.default
            session?.delegate = self
            session?.activate()
        }
    }
    
    func session(_ session: WCSession, activationDidCompleteWith activationState: WCSessionActivationState, error: Error?) {
        if let error = error {
            print("WCSession activation failed with error: \(error.localizedDescription)")
            return
        }
        print("WCSession activated with state: \(activationState.rawValue)")
    }
    
    func session(_ session: WCSession, didReceiveApplicationContext applicationContext: [String : Any]) {
        DispatchQueue.main.async {
            self.handlePayload(payload: applicationContext)
        }
    }
    
    func session(_ session: WCSession, didReceiveMessage message: [String : Any]) {
        DispatchQueue.main.async {
            self.handlePayload(payload: message)
        }
    }
    
    private func handlePayload(payload: [String: Any]) {
        if let status = payload["status"] as? String {
            self.isSessionActive = (status == "ACTIVE")
        }
        if let speed = payload["speed"] as? Double {
            self.currentSpeed = speed
        }
        if let calories = payload["calories"] as? Int {
            self.activeCalories = calories
        }
    }
    
    func sendStartSession() {
        self.isSessionActive = true
        sendMessage(["command": "START_SESSION"])
    }
    
    func sendStopSession() {
        self.isSessionActive = false
        sendMessage(["command": "STOP_SESSION"])
    }
    
    private func sendMessage(_ message: [String: Any]) {
        guard let session = session, session.isReachable else {
            // Fallback to updating application context if not reachable in real-time
            do {
                try self.session?.updateApplicationContext(message)
            } catch {
                print("Failed to update application context: \(error)")
            }
            return
        }
        session.sendMessage(message, replyHandler: nil) { error in
            print("Failed to send message: \(error.localizedDescription)")
        }
    }
}
