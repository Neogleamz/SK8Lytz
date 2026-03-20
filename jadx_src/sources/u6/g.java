package u6;

import android.os.SystemClock;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g implements d {

    /* renamed from: a  reason: collision with root package name */
    private static final g f23068a = new g();

    private g() {
    }

    public static d d() {
        return f23068a;
    }

    @Override // u6.d
    public final long a() {
        return System.currentTimeMillis();
    }

    @Override // u6.d
    public final long b() {
        return SystemClock.elapsedRealtime();
    }

    @Override // u6.d
    public final long c() {
        return System.nanoTime();
    }
}
