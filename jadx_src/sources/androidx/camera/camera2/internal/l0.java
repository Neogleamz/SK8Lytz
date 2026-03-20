package androidx.camera.camera2.internal;

import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.f;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l0 implements f.b {

    /* renamed from: a  reason: collision with root package name */
    static final l0 f1946a = new l0();

    @Override // androidx.camera.core.impl.f.b
    public void a(androidx.camera.core.impl.v<?> vVar, f.a aVar) {
        androidx.camera.core.impl.f t8 = vVar.t(null);
        Config M = androidx.camera.core.impl.o.M();
        int g8 = androidx.camera.core.impl.f.a().g();
        if (t8 != null) {
            g8 = t8.g();
            aVar.a(t8.b());
            M = t8.d();
        }
        aVar.o(M);
        r.a aVar2 = new r.a(vVar);
        aVar.p(aVar2.P(g8));
        aVar.c(n1.d(aVar2.S(k0.c())));
        aVar.e(aVar2.N());
    }
}
