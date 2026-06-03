import Foundation
import ExpoModulesCore
import WatchConnectivity

/**
 * Sk8lytzWatchBridgeModule — iOS side of the watch bridge.
 *
 * Uses WCSession to push state and metrics to the paired Apple Watch.
 * - syncSessionState → WCSession.updateApplicationContext (queued, survives unreachable watch)
 * - sendMetricUpdate  → WCSession.sendMessage if reachable, else updateApplicationContext
 * - Inbound commands (START/STOP from watch) → emitted as 'onWatchCommandReceived' JS events
 */
public class Sk8lytzWatchBridgeModule: Module, WCSessionDelegate {

  private var session: WCSession?

  public func definition() -> ModuleDefinition {
    Name("Sk8lytzWatchBridge")

    Events("onWatchCommandReceived", "onWatchHealthUpdate")

    OnCreate {
      if WCSession.isSupported() {
        self.session = WCSession.default
        self.session?.delegate = self
        self.session?.activate()
      }
    }

    AsyncFunction("syncSessionState") { (state: [String: Any]) throws in
      guard let session = self.session, session.isPaired else { return }
      do {
        try session.updateApplicationContext(state)
      } catch {
        throw Exception(name: "WCSessionError", description: "updateApplicationContext failed: \(error.localizedDescription)")
      }
    }

    AsyncFunction("sendMetricUpdate") { (metrics: [String: Any]) throws in
      guard let session = self.session, session.isPaired else { return }
      if session.isReachable {
        session.sendMessage(metrics, replyHandler: nil) { error in
          // Fallback to context on send failure — non-throwing by design
          try? session.updateApplicationContext(metrics)
        }
      } else {
        try? session.updateApplicationContext(metrics)
      }
    }

    AsyncFunction("isWatchReachable") { () -> Bool in
      return self.session?.isReachable ?? false
    }
  }

  // MARK: - WCSessionDelegate

  public func session(_ session: WCSession,
                      activationDidCompleteWith activationState: WCSessionActivationState,
                      error: Error?) {
    if let error = error {
      print("[WatchBridge] WCSession activation failed: \(error.localizedDescription)")
    }
  }

  public func sessionDidBecomeInactive(_ session: WCSession) {}

  public func sessionDidDeactivate(_ session: WCSession) {
    WCSession.default.activate()
  }

  /// Receives commands and health updates sent from the Apple Watch
  public func session(_ session: WCSession, didReceiveMessage message: [String: Any]) {
    if let command = message["command"] as? String {
      sendEvent("onWatchCommandReceived", ["command": command])
    }
    if message["healthUpdate"] as? Bool == true {
      let hr = message["heartRate"] as? Int ?? 0
      let cal = message["calories"] as? Int ?? 0
      sendEvent("onWatchHealthUpdate", ["heartRate": hr, "calories": cal])
    }
  }

  /// Also handle commands/health that arrive via applicationContext (watch not reachable in real-time)
  public func session(_ session: WCSession,
                      didReceiveApplicationContext applicationContext: [String: Any]) {
    if let command = applicationContext["command"] as? String {
      sendEvent("onWatchCommandReceived", ["command": command])
    }
    if applicationContext["healthUpdate"] as? Bool == true {
      let hr = applicationContext["heartRate"] as? Int ?? 0
      let cal = applicationContext["calories"] as? Int ?? 0
      sendEvent("onWatchHealthUpdate", ["heartRate": hr, "calories": cal])
    }
  }
}
