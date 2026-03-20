package v;

import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.p1;
import java.util.List;
import u.d0;
import u.z;
import y.t0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h {

    /* renamed from: a  reason: collision with root package name */
    private final boolean f23123a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f23124b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f23125c;

    public h(t0 t0Var, t0 t0Var2) {
        this.f23123a = t0Var2.a(d0.class);
        this.f23124b = t0Var.a(z.class);
        this.f23125c = t0Var.a(u.j.class);
    }

    public void a(List<DeferrableSurface> list) {
        if (!b() || list == null) {
            return;
        }
        for (DeferrableSurface deferrableSurface : list) {
            deferrableSurface.c();
        }
        p1.a("ForceCloseDeferrableSurface", "deferrableSurface closed");
    }

    public boolean b() {
        return this.f23123a || this.f23124b || this.f23125c;
    }
}
