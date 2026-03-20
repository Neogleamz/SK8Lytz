package androidx.camera.core;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p0 extends o0 {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements a0.c<Void> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ l1 f2771a;

        a(l1 l1Var) {
            this.f2771a = l1Var;
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r12) {
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            this.f2771a.close();
        }
    }

    @Override // androidx.camera.core.o0
    l1 d(y.g0 g0Var) {
        return g0Var.f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.core.o0
    public void g() {
    }

    @Override // androidx.camera.core.o0
    void o(l1 l1Var) {
        a0.f.b(e(l1Var), new a(l1Var), z.a.a());
    }
}
