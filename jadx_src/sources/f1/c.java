package f1;

import androidx.lifecycle.e0;
import androidx.lifecycle.f0;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.jvm.internal.p;
import mj.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: a  reason: collision with root package name */
    private final List<f<?>> f19838a = new ArrayList();

    public final <T extends e0> void a(tj.c<T> cVar, l<? super a, ? extends T> lVar) {
        p.e(cVar, "clazz");
        p.e(lVar, "initializer");
        this.f19838a.add(new f<>(lj.a.a(cVar), lVar));
    }

    public final f0.b b() {
        f[] fVarArr = (f[]) this.f19838a.toArray(new f[0]);
        return new b((f[]) Arrays.copyOf(fVarArr, fVarArr.length));
    }
}
