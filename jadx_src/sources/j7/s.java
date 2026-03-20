package j7;

import com.google.android.gms.tasks.RuntimeExecutionException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class s implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ j f20833a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ t f20834b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s(t tVar, j jVar) {
        this.f20834b = tVar;
        this.f20833a = jVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        l0 l0Var;
        l0 l0Var2;
        l0 l0Var3;
        c cVar;
        l0 l0Var4;
        l0 l0Var5;
        if (this.f20833a.n()) {
            l0Var5 = this.f20834b.f20837c;
            l0Var5.u();
            return;
        }
        try {
            cVar = this.f20834b.f20836b;
            Object a9 = cVar.a(this.f20833a);
            l0Var4 = this.f20834b.f20837c;
            l0Var4.t(a9);
        } catch (RuntimeExecutionException e8) {
            if (e8.getCause() instanceof Exception) {
                l0Var3 = this.f20834b.f20837c;
                l0Var3.s((Exception) e8.getCause());
                return;
            }
            l0Var2 = this.f20834b.f20837c;
            l0Var2.s(e8);
        } catch (Exception e9) {
            l0Var = this.f20834b.f20837c;
            l0Var.s(e9);
        }
    }
}
