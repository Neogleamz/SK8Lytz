package j7;

import com.google.android.gms.tasks.RuntimeExecutionException;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class u implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ j f20838a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ v f20839b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public u(v vVar, j jVar) {
        this.f20839b = vVar;
        this.f20838a = jVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        l0 l0Var;
        l0 l0Var2;
        l0 l0Var3;
        c cVar;
        try {
            cVar = this.f20839b.f20841b;
            j jVar = (j) cVar.a(this.f20838a);
            if (jVar == null) {
                this.f20839b.b(new NullPointerException("Continuation returned null"));
                return;
            }
            v vVar = this.f20839b;
            Executor executor = l.f20821b;
            jVar.g(executor, vVar);
            jVar.e(executor, this.f20839b);
            jVar.a(executor, this.f20839b);
        } catch (RuntimeExecutionException e8) {
            if (e8.getCause() instanceof Exception) {
                l0Var3 = this.f20839b.f20842c;
                l0Var3.s((Exception) e8.getCause());
                return;
            }
            l0Var2 = this.f20839b.f20842c;
            l0Var2.s(e8);
        } catch (Exception e9) {
            l0Var = this.f20839b.f20842c;
            l0Var.s(e9);
        }
    }
}
