package v;

import android.annotation.SuppressLint;
import android.hardware.camera2.CaptureRequest;
import r.a;
import u.u;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i {
    @SuppressLint({"NewApi"})
    public void a(int i8, a.C0201a c0201a) {
        CaptureRequest.Key key;
        Boolean bool;
        if (((u) u.l.a(u.class)) == null) {
            return;
        }
        if (i8 == 0) {
            key = CaptureRequest.CONTROL_ENABLE_ZSL;
            bool = Boolean.TRUE;
        } else if (i8 != 1) {
            return;
        } else {
            key = CaptureRequest.CONTROL_ENABLE_ZSL;
            bool = Boolean.FALSE;
        }
        c0201a.e(key, bool);
    }
}
