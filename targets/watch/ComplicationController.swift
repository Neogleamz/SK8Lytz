import ClockKit
import SwiftUI

/**
 * ComplicationController — Provides watchOS complications for SK8Lytz.
 *
 * Supported families:
 *   • Graphic Circular  — speed gauge ring (0–30 mph) with numeric center
 *   • Modular Small     — speed number + "mph" label stack
 *   • Graphic Corner    — inline status text showing speed when active
 *
 * Data source: WatchConnectivityManager.shared (live speed + session status)
 * Refresh: whenever WatchConnectivityManager receives a speed update (real-time during session),
 *          and on every system-granted background timeline refresh when idle.
 */
class ComplicationController: NSObject, CLKComplicationDataSource {

    // MARK: - Brand colors
    private let electricCyan = UIColor(red: 0, green: 240/255, blue: 1, alpha: 1)

    // MARK: - Descriptor

    func getComplicationDescriptors(handler: @escaping ([CLKComplicationDescriptor]) -> Void) {
        let descriptors = [
            CLKComplicationDescriptor(
                identifier: "sk8lytz_speed",
                displayName: "SK8Lytz Speed",
                supportedFamilies: [
                    .graphicCircular,
                    .modularSmall,
                    .graphicCorner
                ]
            )
        ]
        handler(descriptors)
    }

    // MARK: - Timeline Configuration

    func getTimelineEndDate(for complication: CLKComplication,
                            withHandler handler: @escaping (Date?) -> Void) {
        // We update in real-time; no fixed end date
        handler(nil)
    }

    func getPrivacyBehavior(for complication: CLKComplication,
                            withHandler handler: @escaping (CLKComplicationPrivacyBehavior) -> Void) {
        handler(.showOnLockScreen)
    }

    // MARK: - Current Timeline Entry

    func getCurrentTimelineEntry(for complication: CLKComplication,
                                 withHandler handler: @escaping (CLKComplicationTimelineEntry?) -> Void) {
        let manager = WatchConnectivityManager.shared
        let speed    = manager.currentSpeed
        let isActive = manager.isSessionActive

        if let template = makeTemplate(for: complication.family, speed: speed, isActive: isActive) {
            handler(CLKComplicationTimelineEntry(date: Date(), complicationTemplate: template))
        } else {
            handler(nil)
        }
    }

    // MARK: - Placeholder / Sample

    func getLocalizableSampleTemplate(for complication: CLKComplication,
                                      withHandler handler: @escaping (CLKComplicationTemplate?) -> Void) {
        // Show a realistic preview — 12.5 mph active session
        handler(makeTemplate(for: complication.family, speed: 12.5, isActive: true))
    }

    // MARK: - Template Factory

    private func makeTemplate(for family: CLKComplicationFamily,
                              speed: Double,
                              isActive: Bool) -> CLKComplicationTemplate? {
        switch family {

        case .graphicCircular:
            // Ring gauge: 0 → 30 mph = 0 → 100%, electric cyan ring, speed number center
            let fillFraction = Float(min(speed / 30.0, 1.0))
            let gaugeProvider = CLKSimpleGaugeProvider(
                style: .ring,
                gaugeColor: electricCyan,
                fillFraction: fillFraction
            )
            let centerText = CLKSimpleTextProvider(
                text: isActive ? String(format: "%.0f", speed) : "--"
            )
            centerText.tintColor = electricCyan
            return CLKComplicationTemplateGraphicCircularClosedGaugeText(
                gaugeProvider: gaugeProvider,
                centerTextProvider: centerText
            )

        case .modularSmall:
            // Two-line stack: speed on top, unit / brand on bottom
            let line1 = CLKSimpleTextProvider(
                text: isActive ? String(format: "%.1f", speed) : "--"
            )
            line1.tintColor = electricCyan
            let line2 = CLKSimpleTextProvider(text: isActive ? "mph" : "SK8")
            return CLKComplicationTemplateModularSmallStackText(
                line1TextProvider: line1,
                line2TextProvider: line2
            )

        case .graphicCorner:
            // Corner inline: "12.5 mph" when active, "SK8Lytz" when idle
            let statusText = CLKSimpleTextProvider(
                text: isActive
                    ? String(format: "%.1f mph", speed)
                    : "SK8Lytz"
            )
            statusText.tintColor = isActive ? electricCyan : .gray
            // graphicCorner requires an image provider; use a simple gauge as the corner curve
            let gaugeProvider = CLKSimpleGaugeProvider(
                style: .fill,
                gaugeColor: isActive ? electricCyan : .gray,
                fillFraction: isActive ? Float(min(speed / 30.0, 1.0)) : 0
            )
            return CLKComplicationTemplateGraphicCornerGaugeText(
                gaugeProvider: gaugeProvider,
                leadingTextProvider: nil,
                trailingTextProvider: nil,
                outerTextProvider: statusText
            )

        default:
            return nil
        }
    }
}
