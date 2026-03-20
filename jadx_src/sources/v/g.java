package v;

import androidx.camera.camera2.internal.n2;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import y.t0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g {

    /* renamed from: a  reason: collision with root package name */
    private final u.h f23122a;

    @FunctionalInterface
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(n2 n2Var);
    }

    public g(t0 t0Var) {
        this.f23122a = (u.h) t0Var.b(u.h.class);
    }

    private void a(Set<n2> set) {
        for (n2 n2Var : set) {
            n2Var.c().p(n2Var);
        }
    }

    private void b(Set<n2> set) {
        for (n2 n2Var : set) {
            n2Var.c().q(n2Var);
        }
    }

    public void c(n2 n2Var, List<n2> list, List<n2> list2, a aVar) {
        n2 next;
        n2 next2;
        if (d()) {
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            Iterator<n2> it = list.iterator();
            while (it.hasNext() && (next2 = it.next()) != n2Var) {
                linkedHashSet.add(next2);
            }
            b(linkedHashSet);
        }
        aVar.a(n2Var);
        if (d()) {
            LinkedHashSet linkedHashSet2 = new LinkedHashSet();
            Iterator<n2> it2 = list2.iterator();
            while (it2.hasNext() && (next = it2.next()) != n2Var) {
                linkedHashSet2.add(next);
            }
            a(linkedHashSet2);
        }
    }

    public boolean d() {
        return this.f23122a != null;
    }
}
