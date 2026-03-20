package v;

import android.hardware.camera2.CaptureRequest;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.f;
import java.util.List;
import r.a;
import u.e0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class r {

    /* renamed from: a  reason: collision with root package name */
    private final boolean f23135a;

    public r() {
        this.f23135a = u.l.a(e0.class) != null;
    }

    public androidx.camera.core.impl.f a(androidx.camera.core.impl.f fVar) {
        f.a aVar = new f.a();
        aVar.p(fVar.g());
        for (DeferrableSurface deferrableSurface : fVar.e()) {
            aVar.f(deferrableSurface);
        }
        aVar.e(fVar.d());
        a.C0201a c0201a = new a.C0201a();
        c0201a.e(CaptureRequest.FLASH_MODE, 0);
        aVar.e(c0201a.c());
        return aVar.h();
    }

    public boolean b(List<CaptureRequest> list, boolean z4) {
        if (this.f23135a && z4) {
            for (CaptureRequest captureRequest : list) {
                Integer num = (Integer) captureRequest.get(CaptureRequest.FLASH_MODE);
                if (num != null && num.intValue() == 2) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
