package j7;

import com.google.android.gms.tasks.RuntimeExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e0 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ j f20809a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ f0 f20810b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e0(f0 f0Var, j jVar) {
        this.f20810b = f0Var;
        this.f20809a = jVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        i iVar;
        try {
            iVar = this.f20810b.f20812b;
            j a9 = iVar.a(this.f20809a.l());
            if (a9 == null) {
                this.f20810b.b(new NullPointerException("Continuation returned null"));
                return;
            }
            f0 f0Var = this.f20810b;
            Executor executor = l.f20821b;
            a9.g(executor, f0Var);
            a9.e(executor, this.f20810b);
            a9.a(executor, this.f20810b);
        } catch (RuntimeExecutionException e8) {
            if (e8.getCause() instanceof Exception) {
                this.f20810b.b((Exception) e8.getCause());
            } else {
                this.f20810b.b(e8);
            }
        } catch (CancellationException unused) {
            this.f20810b.a();
        } catch (Exception e9) {
            this.f20810b.b(e9);
        }
    }
}
