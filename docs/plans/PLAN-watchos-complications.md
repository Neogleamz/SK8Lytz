# Implementation Plan: watchOS Complications

## Goal
Add watchOS complications for quick-launch and live speed display on the watch face. Strava and Nike Run Club both ship complications — this is expected by Apple Watch users.

## Proposed Changes

### watchOS — New ComplicationController

#### [NEW] [ComplicationController.swift](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/ComplicationController.swift)

Create a new file implementing `CLKComplicationDataSource`:

```swift
import ClockKit
import SwiftUI

class ComplicationController: NSObject, CLKComplicationDataSource {

    // MARK: - Brand Colors
    private let electricCyan = UIColor(red: 0, green: 240/255, blue: 1, alpha: 1)
    private let neonMagenta = UIColor(red: 1, green: 0, blue: 212/255, alpha: 1)

    // MARK: - Supported Families
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
    func getTimelineEndDate(for complication: CLKComplication, withHandler handler: @escaping (Date?) -> Void) {
        // We provide updates indefinitely
        handler(nil)
    }

    func getPrivacyBehavior(for complication: CLKComplication, withHandler handler: @escaping (CLKComplicationPrivacyBehavior) -> Void) {
        handler(.showOnLockScreen)
    }

    // MARK: - Current Timeline Entry
    func getCurrentTimelineEntry(for complication: CLKComplication, withHandler handler: @escaping (CLKComplicationTimelineEntry?) -> Void) {
        let manager = WatchConnectivityManager.shared
        let speed = manager.currentSpeed
        let isActive = manager.isSessionActive

        let template = makeTemplate(for: complication.family, speed: speed, isActive: isActive)
        if let template = template {
            let entry = CLKComplicationTimelineEntry(date: Date(), complicationTemplate: template)
            handler(entry)
        } else {
            handler(nil)
        }
    }

    // MARK: - Placeholder
    func getLocalizableSampleTemplate(for complication: CLKComplication, withHandler handler: @escaping (CLKComplicationTemplate?) -> Void) {
        handler(makeTemplate(for: complication.family, speed: 12.5, isActive: true))
    }

    // MARK: - Template Factory
    private func makeTemplate(for family: CLKComplicationFamily, speed: Double, isActive: Bool) -> CLKComplicationTemplate? {
        switch family {
        case .graphicCircular:
            // Circular gauge showing speed 0-30 mph
            let gaugeProvider = CLKSimpleGaugeProvider(
                style: .ring,
                gaugeColor: electricCyan,
                fillFraction: Float(min(speed / 30.0, 1.0))
            )
            let speedText = CLKSimpleTextProvider(text: String(format: "%.0f", speed))
            speedText.tintColor = electricCyan
            let template = CLKComplicationTemplateGraphicCircularClosedGaugeText(
                gaugeProvider: gaugeProvider,
                centerTextProvider: speedText
            )
            return template

        case .modularSmall:
            // Simple speed number with "mph" label
            let line1 = CLKSimpleTextProvider(text: isActive ? String(format: "%.1f", speed) : "--")
            line1.tintColor = electricCyan
            let line2 = CLKSimpleTextProvider(text: isActive ? "mph" : "SK8")
            let template = CLKComplicationTemplateModularSmallStackText(
                line1TextProvider: line1,
                line2TextProvider: line2
            )
            return template

        case .graphicCorner:
            // Corner text showing session status
            let text = CLKSimpleTextProvider(
                text: isActive ? "SKATING \(String(format: "%.1f", speed)) mph" : "SK8Lytz"
            )
            text.tintColor = isActive ? electricCyan : .gray
            let template = CLKComplicationTemplateGraphicCornerTextView(
                textProvider: text,
                label: {
                    // SwiftUI label view
                    Text(isActive ? "🛼" : "")
                        .font(.caption2)
                }
            )
            return template

        default:
            return nil
        }
    }
}
```

#### [MODIFY] [WatchConnectivityManager.swift](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/WatchConnectivityManager.swift)

Add complication reload trigger when speed updates arrive. In `handlePayload()`, after updating `currentSpeed`:

```swift
// Reload complications with fresh speed data
if #available(watchOS 7.0, *) {
    let server = CLKComplicationServer.sharedInstance()
    for complication in server.activeComplications ?? [] {
        server.reloadTimeline(for: complication)
    }
}
```

Add import at top:
```swift
import ClockKit
```

#### [MODIFY] [expo-target.config.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/expo-target.config.js)

Add complication configuration to the infoPlist section:

```js
CLKComplicationPrincipalClass: '$(PRODUCT_MODULE_NAME).ComplicationController',
CLKComplicationSupportedFamilies: [
  'CLKComplicationFamilyGraphicCircular',
  'CLKComplicationFamilyModularSmall',
  'CLKComplicationFamilyGraphicCorner',
],
```

## Verification Plan

### Automated Tests
- `npm run verify` — ensure no TS regressions

### Manual Verification
- Build watchOS target in Xcode
- Long-press watch face → Edit → Add complication → SK8Lytz should appear
- Verify speed gauge updates when session is active
- Verify idle state shows "SK8" placeholder
