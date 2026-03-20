package v;

import android.util.Size;
import androidx.camera.core.impl.SurfaceConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j {

    /* renamed from: a  reason: collision with root package name */
    private final u.o f23126a;

    public j() {
        this((u.o) u.l.a(u.o.class));
    }

    j(u.o oVar) {
        this.f23126a = oVar;
    }

    public Size a(Size size) {
        Size a9;
        u.o oVar = this.f23126a;
        if (oVar == null || (a9 = oVar.a(SurfaceConfig.ConfigType.PRIV)) == null) {
            return size;
        }
        return a9.getWidth() * a9.getHeight() > size.getWidth() * size.getHeight() ? a9 : size;
    }
}
