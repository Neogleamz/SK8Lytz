package v;

import android.hardware.camera2.CaptureRequest;
import android.util.Range;
import r.a;
import y.t0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    private final Range<Integer> f23115a;

    public a(t0 t0Var) {
        u.a aVar = (u.a) t0Var.b(u.a.class);
        this.f23115a = aVar == null ? null : aVar.b();
    }

    public void a(a.C0201a c0201a) {
        Range<Integer> range = this.f23115a;
        if (range != null) {
            c0201a.e(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, range);
        }
    }
}
