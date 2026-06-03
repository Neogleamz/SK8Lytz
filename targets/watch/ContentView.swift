import SwiftUI

struct ContentView: View {
    @StateObject private var watchManager = WatchConnectivityManager.shared
    @StateObject private var healthManager = HealthManager.shared
    
    var body: some View {
        VStack(spacing: 20) {
            if watchManager.isSessionActive {
                VStack {
                    Text("ACTIVE SESSION")
                        .font(.headline)
                        .foregroundColor(.green)
                    
                    HStack {
                        VStack {
                            Text(String(format: "%.1f", watchManager.currentSpeed))
                                .font(.system(size: 32, weight: .bold))
                            Text("mph")
                                .font(.caption)
                        }
                        
                        Spacer()
                        
                        VStack {
                            Text("\(healthManager.currentHeartRate > 0 ? Int(healthManager.currentHeartRate) : 0)")
                                .font(.system(size: 32, weight: .bold))
                                .foregroundColor(.red)
                            Text("bpm")
                                .font(.caption)
                        }
                    }
                    .padding()
                    
                    Button(action: stopSession) {
                        Text("Stop")
                            .font(.title3)
                            .fontWeight(.bold)
                            .frame(maxWidth: .infinity)
                    }
                    .tint(.red)
                }
            } else {
                VStack {
                    Image(systemName: "figure.skating")
                        .font(.system(size: 50))
                        .foregroundColor(.blue)
                        .padding(.bottom, 10)
                    
                    Text("Ready to Skate")
                        .font(.headline)
                    
                    Button(action: startSession) {
                        Text("Start")
                            .font(.title3)
                            .fontWeight(.bold)
                            .frame(maxWidth: .infinity)
                    }
                    .tint(.blue)
                    .padding(.top, 10)
                }
            }
        }
        .onAppear {
            healthManager.requestAuthorization()
        }
        .padding()
    }
    
    private func startSession() {
        healthManager.startWorkout()
        watchManager.sendStartSession()
    }
    
    private func stopSession() {
        healthManager.stopWorkout()
        watchManager.sendStopSession()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
