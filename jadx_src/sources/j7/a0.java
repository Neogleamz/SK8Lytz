package j7;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a0 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ j f20798a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ b0 f20799b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a0(b0 b0Var, j jVar) {
        this.f20799b = b0Var;
        this.f20798a = jVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        f fVar;
        f fVar2;
        obj = this.f20799b.f20802b;
        synchronized (obj) {
            b0 b0Var = this.f20799b;
            fVar = b0Var.f20803c;
            if (fVar != null) {
                fVar2 = b0Var.f20803c;
                fVar2.b((Exception) n6.j.l(this.f20798a.k()));
            }
        }
    }
}
