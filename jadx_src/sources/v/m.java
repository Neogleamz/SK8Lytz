package v;

import android.hardware.camera2.CaptureRequest;
import androidx.camera.core.impl.SessionConfig;
import r.a;
import u.a0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m {
    public static void a(SessionConfig.b bVar) {
        if (((a0) u.l.a(a0.class)) == null) {
            return;
        }
        a.C0201a c0201a = new a.C0201a();
        c0201a.e(CaptureRequest.TONEMAP_MODE, 2);
        bVar.g(c0201a.c());
    }
}
