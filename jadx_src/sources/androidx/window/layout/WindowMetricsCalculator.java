package androidx.window.layout;

import android.app.Activity;
import androidx.window.core.ExperimentalWindowApi;
import kotlin.jvm.internal.p;
import mj.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface WindowMetricsCalculator {
    public static final Companion Companion = Companion.$$INSTANCE;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();
        private static l<? super WindowMetricsCalculator, ? extends WindowMetricsCalculator> decorator = new l<WindowMetricsCalculator, WindowMetricsCalculator>() { // from class: androidx.window.layout.WindowMetricsCalculator$Companion$decorator$1
            public final WindowMetricsCalculator invoke(WindowMetricsCalculator windowMetricsCalculator) {
                p.e(windowMetricsCalculator, "it");
                return windowMetricsCalculator;
            }
        };

        private Companion() {
        }

        public final WindowMetricsCalculator getOrCreate() {
            return (WindowMetricsCalculator) decorator.invoke(WindowMetricsCalculatorCompat.INSTANCE);
        }

        @ExperimentalWindowApi
        public final void overrideDecorator(WindowMetricsCalculatorDecorator windowMetricsCalculatorDecorator) {
            p.e(windowMetricsCalculatorDecorator, "overridingDecorator");
            decorator = new WindowMetricsCalculator$Companion$overrideDecorator$1(windowMetricsCalculatorDecorator);
        }

        @ExperimentalWindowApi
        public final void reset() {
            decorator = new l<WindowMetricsCalculator, WindowMetricsCalculator>() { // from class: androidx.window.layout.WindowMetricsCalculator$Companion$reset$1
                public final WindowMetricsCalculator invoke(WindowMetricsCalculator windowMetricsCalculator) {
                    p.e(windowMetricsCalculator, "it");
                    return windowMetricsCalculator;
                }
            };
        }
    }

    static WindowMetricsCalculator getOrCreate() {
        return Companion.getOrCreate();
    }

    @ExperimentalWindowApi
    static void overrideDecorator(WindowMetricsCalculatorDecorator windowMetricsCalculatorDecorator) {
        Companion.overrideDecorator(windowMetricsCalculatorDecorator);
    }

    @ExperimentalWindowApi
    static void reset() {
        Companion.reset();
    }

    WindowMetrics computeCurrentWindowMetrics(Activity activity);

    WindowMetrics computeMaximumWindowMetrics(Activity activity);
}
