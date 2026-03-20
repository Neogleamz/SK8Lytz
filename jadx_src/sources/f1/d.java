package f1;

import f1.a;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d extends a {
    public d() {
        this(null, 1, null);
    }

    public d(a aVar) {
        p.e(aVar, "initialExtras");
        b().putAll(aVar.b());
    }

    public /* synthetic */ d(a aVar, int i8, i iVar) {
        this((i8 & 1) != 0 ? a.C0169a.f19836b : aVar);
    }

    @Override // f1.a
    public <T> T a(a.b<T> bVar) {
        p.e(bVar, "key");
        return (T) b().get(bVar);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <T> void c(a.b<T> bVar, T t8) {
        p.e(bVar, "key");
        b().put(bVar, t8);
    }
}
