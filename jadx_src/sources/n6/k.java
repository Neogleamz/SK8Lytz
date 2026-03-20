package n6;

import com.google.android.gms.common.internal.RootTelemetryConfiguration;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k {

    /* renamed from: b  reason: collision with root package name */
    private static k f22188b;

    /* renamed from: c  reason: collision with root package name */
    private static final RootTelemetryConfiguration f22189c = new RootTelemetryConfiguration(0, false, false, 0, 0);

    /* renamed from: a  reason: collision with root package name */
    private RootTelemetryConfiguration f22190a;

    private k() {
    }

    public static synchronized k b() {
        k kVar;
        synchronized (k.class) {
            if (f22188b == null) {
                f22188b = new k();
            }
            kVar = f22188b;
        }
        return kVar;
    }

    public RootTelemetryConfiguration a() {
        return this.f22190a;
    }

    public final synchronized void c(RootTelemetryConfiguration rootTelemetryConfiguration) {
        if (rootTelemetryConfiguration == null) {
            this.f22190a = f22189c;
            return;
        }
        RootTelemetryConfiguration rootTelemetryConfiguration2 = this.f22190a;
        if (rootTelemetryConfiguration2 == null || rootTelemetryConfiguration2.E0() < rootTelemetryConfiguration.E0()) {
            this.f22190a = rootTelemetryConfiguration;
        }
    }
}
