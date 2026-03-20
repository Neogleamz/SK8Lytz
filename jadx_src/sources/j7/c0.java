package j7;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c0 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ j f20804a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ d0 f20805b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c0(d0 d0Var, j jVar) {
        this.f20805b = d0Var;
        this.f20804a = jVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        g gVar;
        g gVar2;
        obj = this.f20805b.f20807b;
        synchronized (obj) {
            d0 d0Var = this.f20805b;
            gVar = d0Var.f20808c;
            if (gVar != null) {
                gVar2 = d0Var.f20808c;
                gVar2.c(this.f20804a.l());
            }
        }
    }
}
