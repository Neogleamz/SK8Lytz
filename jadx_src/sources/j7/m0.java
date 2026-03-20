package j7;

import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class m0 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ l0 f20828a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ Callable f20829b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m0(l0 l0Var, Callable callable) {
        this.f20828a = l0Var;
        this.f20829b = callable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            this.f20828a.t(this.f20829b.call());
        } catch (Exception e8) {
            this.f20828a.s(e8);
        } catch (Throwable th) {
            this.f20828a.s(new RuntimeException(th));
        }
    }
}
