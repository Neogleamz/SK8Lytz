package w3;

import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class m implements Executor {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f23513a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        private final Runnable f23514a;

        a(Runnable runnable) {
            this.f23514a = runnable;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.f23514a.run();
            } catch (Exception e8) {
                a4.a.d("Executor", "Background execution failure.", e8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public m(Executor executor) {
        this.f23513a = executor;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        this.f23513a.execute(new a(runnable));
    }
}
