package b6;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e0 implements d {
    @Override // b6.d
    public long b() {
        return SystemClock.elapsedRealtime();
    }

    @Override // b6.d
    public long c() {
        return SystemClock.uptimeMillis();
    }

    @Override // b6.d
    public l d(Looper looper, Handler.Callback callback) {
        return new f0(new Handler(looper, callback));
    }

    @Override // b6.d
    public void e() {
    }
}
