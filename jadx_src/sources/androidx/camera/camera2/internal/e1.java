package androidx.camera.camera2.internal;

import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.SessionConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e1 implements SessionConfig.d {

    /* renamed from: a  reason: collision with root package name */
    static final e1 f1786a = new e1();

    @Override // androidx.camera.core.impl.SessionConfig.d
    public void a(androidx.camera.core.impl.v<?> vVar, SessionConfig.b bVar) {
        SessionConfig n8 = vVar.n(null);
        Config M = androidx.camera.core.impl.o.M();
        int l8 = SessionConfig.a().l();
        if (n8 != null) {
            l8 = n8.l();
            bVar.a(n8.b());
            bVar.c(n8.i());
            bVar.b(n8.g());
            M = n8.d();
        }
        bVar.q(M);
        r.a aVar = new r.a(vVar);
        bVar.s(aVar.P(l8));
        bVar.e(aVar.Q(i1.b()));
        bVar.j(aVar.T(h1.b()));
        bVar.d(n1.d(aVar.S(k0.c())));
        androidx.camera.core.impl.n P = androidx.camera.core.impl.n.P();
        P.s(r.a.G, aVar.M(r.c.e()));
        P.s(r.a.I, aVar.R(null));
        P.s(r.a.C, Long.valueOf(aVar.U(-1L)));
        bVar.g(P);
        bVar.g(aVar.N());
    }
}
