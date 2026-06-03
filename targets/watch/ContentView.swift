import SwiftUI

struct ContentView: View {
    // Use @ObservedObject (not @StateObject) for singletons we don't own
    @ObservedObject private var watchManager = WatchConnectivityManager.shared
    @ObservedObject private var healthManager = HealthManager.shared

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
        .padding()
    }

    // MARK: - Active Session View
    private var activeSessionView: some View {
        VStack(spacing: 12) {
            Text("ACTIVE SESSION")
                .font(.headline)
                .foregroundColor(.green)

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
                    Text("\(watchManager.activeCalories)")
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
        healthManager.startWorkout()
        watchManager.sendStartSession()
    }

    private func stopSession() {
        healthManager.stopWorkout()
        watchManager.sendStopSession()
    }
}

#Preview {
    ContentView()
}
