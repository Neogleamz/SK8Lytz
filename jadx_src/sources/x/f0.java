package x;

import androidx.camera.core.l1;
import java.util.Objects;
import x.l;
import x.z;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f0 {

    /* renamed from: a  reason: collision with root package name */
    private a0 f23713a;

    /* renamed from: b  reason: collision with root package name */
    private z.a f23714b;

    /* JADX INFO: Access modifiers changed from: private */
    public void c(l1 l1Var) {
        androidx.camera.core.impl.utils.m.a();
        androidx.core.util.h.j(this.f23713a != null);
        Object c9 = l1Var.e1().a().c(this.f23713a.g());
        Objects.requireNonNull(c9);
        androidx.core.util.h.j(((Integer) c9).intValue() == this.f23713a.f().get(0).intValue());
        this.f23714b.a().accept(z.b.c(this.f23713a, l1Var));
        this.f23713a = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(a0 a0Var) {
        androidx.camera.core.impl.utils.m.a();
        androidx.core.util.h.k(a0Var.f().size() == 1, "Cannot handle multi-image capture.");
        androidx.core.util.h.k(this.f23713a == null, "Already has an existing request.");
        this.f23713a = a0Var;
    }

    public void d() {
    }

    public z.a f(l.b bVar) {
        bVar.b().a(new d0(this));
        bVar.c().a(new e0(this));
        z.a c9 = z.a.c(bVar.a());
        this.f23714b = c9;
        return c9;
    }
}
