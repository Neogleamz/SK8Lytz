package v;

import android.util.Size;
import androidx.camera.core.impl.SurfaceConfig;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class n {

    /* renamed from: a  reason: collision with root package name */
    private final u.o f23130a;

    public n() {
        this((u.o) u.l.a(u.o.class));
    }

    n(u.o oVar) {
        this.f23130a = oVar;
    }

    public List<Size> a(SurfaceConfig.ConfigType configType, List<Size> list) {
        Size a9;
        u.o oVar = this.f23130a;
        if (oVar == null || (a9 = oVar.a(configType)) == null) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(a9);
        for (Size size : list) {
            if (!size.equals(a9)) {
                arrayList.add(size);
            }
        }
        return arrayList;
    }
}
