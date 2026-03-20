package b7;

import android.os.Handler;
import android.os.Looper;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f extends Handler {

    /* renamed from: a  reason: collision with root package name */
    private final Looper f8165a;

    public f(Looper looper) {
        super(looper);
        this.f8165a = Looper.getMainLooper();
    }

    public f(Looper looper, Handler.Callback callback) {
        super(looper, callback);
        this.f8165a = Looper.getMainLooper();
    }
}
