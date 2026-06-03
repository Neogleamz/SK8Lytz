import Foundation
import HealthKit

/// Manages the active HKWorkoutSession on the watch, keeping the app alive and
/// collecting live heart rate and calorie data during a skating session.
class HealthManager: NSObject, ObservableObject, HKWorkoutSessionDelegate, HKLiveWorkoutBuilderDelegate {
    static let shared = HealthManager()

    private let healthStore = HKHealthStore()
    private var session: HKWorkoutSession?
    private var builder: HKLiveWorkoutBuilder?

    @Published var currentHeartRate: Double = 0.0
    @Published var activeCalories: Double = 0.0

    private override init() {
        super.init()
    }

    // MARK: - Authorization

    func requestAuthorization() {
        let typesToShare: Set = [
            HKQuantityType.workoutType()
        ]

        // Guard against unavailable types rather than force-unwrapping
        var typesToRead: Set<HKQuantityType> = []
        if let heartRate = HKQuantityType.quantityType(forIdentifier: .heartRate) {
            typesToRead.insert(heartRate)
        }
        if let calories = HKQuantityType.quantityType(forIdentifier: .activeEnergyBurned) {
            typesToRead.insert(calories)
        }

        guard !typesToRead.isEmpty else {
            print("[HealthManager] HealthKit types unavailable on this device")
            return
        }

        healthStore.requestAuthorization(toShare: typesToShare, read: typesToRead) { success, error in
            if !success {
                print("[HealthManager] Authorization failed: \(String(describing: error))")
            }
        }
    }

    // MARK: - Workout Lifecycle

    func startWorkout() {
        let configuration = HKWorkoutConfiguration()
        configuration.activityType = .skatingSports
        configuration.locationType = .outdoor

        do {
            session = try HKWorkoutSession(healthStore: healthStore, configuration: configuration)
            builder = session?.associatedWorkoutBuilder()
        } catch {
            print("[HealthManager] Failed to create workout session: \(error)")
            return
        }

        session?.delegate = self
        builder?.delegate = self
        builder?.dataSource = HKLiveWorkoutDataSource(
            healthStore: healthStore,
            workoutConfiguration: configuration
        )

        let startDate = Date()
        session?.startActivity(with: startDate)
        builder?.beginCollection(withStart: startDate) { success, error in
            if !success {
                print("[HealthManager] beginCollection failed: \(String(describing: error))")
            }
        }
    }

    func stopWorkout() {
        session?.end()
        builder?.endCollection(withEnd: Date()) { [weak self] _, _ in
            self?.builder?.finishWorkout { _, error in
                if let error = error {
                    print("[HealthManager] finishWorkout failed: \(error)")
                }
                DispatchQueue.main.async {
                    self?.session = nil
                    self?.builder = nil
                }
            }
        }
    }

    // MARK: - HKWorkoutSessionDelegate

    func workoutSession(_ workoutSession: HKWorkoutSession,
                        didChangeTo toState: HKWorkoutSessionState,
                        from fromState: HKWorkoutSessionState,
                        date: Date) {
        // If the session ends externally (e.g. watch face dismissed, system timeout),
        // reset local state so the UI correctly returns to the idle screen.
        if toState == .ended {
            DispatchQueue.main.async { [weak self] in
                self?.session = nil
                self?.builder = nil
                // Note: isSessionActive lives on WatchConnectivityManager — it will
                // be reset by the phone via WCSession when it processes the stop command,
                // or we can reset it here directly via the shared instance.
                WatchConnectivityManager.shared.isSessionActive = false
            }
        }
    }

    func workoutSession(_ workoutSession: HKWorkoutSession, didFailWithError error: Error) {
        print("[HealthManager] Session failed: \(error)")
    }

    // MARK: - HKLiveWorkoutBuilderDelegate

    func workoutBuilder(_ workoutBuilder: HKLiveWorkoutBuilder,
                        didCollectDataOf collectedTypes: Set<HKSampleType>) {
        for type in collectedTypes {
            guard let quantityType = type as? HKQuantityType,
                  let statistics = workoutBuilder.statistics(for: quantityType) else { continue }

            DispatchQueue.main.async { [weak self] in
                switch quantityType {
                case HKQuantityType.quantityType(forIdentifier: .heartRate):
                    let unit = HKUnit.count().unitDivided(by: .minute())
                    self?.currentHeartRate = statistics.mostRecentQuantity()?.doubleValue(for: unit) ?? 0.0

                case HKQuantityType.quantityType(forIdentifier: .activeEnergyBurned):
                    let unit = HKUnit.kilocalorie()
                    self?.activeCalories = statistics.sumQuantity()?.doubleValue(for: unit) ?? 0.0

                default:
                    break
                }
            }
        }
    }

    func workoutBuilderDidCollectEvent(_ workoutBuilder: HKLiveWorkoutBuilder) {
        // Reserved for future event handling (e.g. lap markers)
    }
}
