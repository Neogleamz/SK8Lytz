import SwiftUI

struct ContentView: View {
    // Use @ObservedObject (not @StateObject) for singletons we don't own
    @ObservedObject private var watchManager = WatchConnectivityManager.shared
    @ObservedObject private var healthManager = HealthManager.shared

    /// Live elapsed time derived from the phone-authoritative anchor timestamp.
    /// Computed every second by the timer publisher below.
    @State private var elapsedSeconds: Int = 0
    private let ticker = Timer.publish(every: 1, on: .main, in: .common).autoconnect()

    var body: some View {
        VStack(spacing: 16) {
            if watchManager.isSessionActive {
                activeSessionView
            } else {
                idleView
            }
        }
        .onAppear {
            healthManager.requestAuthorization()
        }
        .onReceive(ticker) { _ in
            // Recompute elapsed from the phone-authoritative anchor every second.
            // Falls back to zero gracefully when sessionStartTime is nil.
            if watchManager.isPaused {
                // If paused, freeze elapsedSeconds at its current value
            } else if let anchor = watchManager.sessionStartTime {
                elapsedSeconds = Int(Date().timeIntervalSince(anchor))
            } else {
                elapsedSeconds = 0
            }
        }
        .padding()
    }

    // MARK: - Active Session View
    private var activeSessionView: some View {
        VStack(spacing: 10) {
            if watchManager.isPaused {
                Text("⏸ PAUSED")
                    .font(.headline)
                    .foregroundColor(.orange)
            } else {
                Text("ACTIVE SESSION")
                    .font(.headline)
                    .foregroundColor(.green)
            }

            // Elapsed duration — anchored to phone-authoritative start time
            Text(formatElapsed(elapsedSeconds))
                .font(.system(size: 22, weight: .medium, design: .monospaced))
                .foregroundColor(.white)

            HStack {
                VStack(spacing: 2) {
                    Text(String(format: "%.1f", watchManager.currentSpeed))
                        .font(.system(size: 28, weight: .bold, design: .rounded))
                    Text("mph")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }

                Spacer()

                VStack(spacing: 2) {
                    Text(healthManager.currentHeartRate > 0
                         ? "\(Int(healthManager.currentHeartRate))"
                         : "--")
                        .font(.system(size: 28, weight: .bold, design: .rounded))
                        .foregroundColor(.red)
                    Text("bpm")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }

                Spacer()

                VStack(spacing: 2) {
                    // Prefer watch-side HealthKit calories; fall back to phone-pushed value
                    Text("\(healthManager.activeCalories > 0 ? Int(healthManager.activeCalories) : watchManager.activeCalories)")
                        .font(.system(size: 28, weight: .bold, design: .rounded))
                        .foregroundColor(.orange)
                    Text("kcal")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
            }
            .padding(.horizontal, 4)

            Button(action: stopSession) {
                Text("Stop")
                    .font(.callout)
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity)
            }
            .tint(.red)
        }
    }

    // MARK: - Idle View
    private var idleView: some View {
        VStack(spacing: 12) {
            // Using SK8Lytz brand mark — no dedicated roller-skating SF Symbol exists
            Image("sk8lytz-logo")
                .resizable()
                .scaledToFit()
                .frame(width: 44, height: 44)
                .padding(.bottom, 4)

            Text("Ready to Skate")
                .font(.headline)

            Button(action: startSession) {
                Text("Start")
                    .font(.callout)
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity)
            }
            .tint(.blue)
        }
    }

    // MARK: - Actions
    private func startSession() {
        // Anchor locally when the session is initiated from the watch itself.
        // When initiated from the phone, the anchor arrives via handlePayload().
        if watchManager.sessionStartTime == nil {
            watchManager.sessionStartTime = Date()
        }
        healthManager.startWorkout()
        watchManager.sendStartSession()
    }

    private func stopSession() {
        // Anchor cleared in WatchConnectivityManager.handlePayload when STOPPED arrives;
        // clear immediately here too for instant UI reset.
        watchManager.sessionStartTime = nil
        elapsedSeconds = 0
        healthManager.stopWorkout()
        watchManager.sendStopSession()
    }

    // MARK: - Helpers

    /// Formats a raw second count into MM:SS (or H:MM:SS past 59:59).
    private func formatElapsed(_ totalSeconds: Int) -> String {
        let h = totalSeconds / 3600
        let m = (totalSeconds % 3600) / 60
        let s = totalSeconds % 60
        if h > 0 {
            return String(format: "%d:%02d:%02d", h, m, s)
        }
        return String(format: "%02d:%02d", m, s)
    }
}

#Preview {
    ContentView()
}
