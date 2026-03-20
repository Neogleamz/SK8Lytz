package androidx.room;

import java.util.ArrayDeque;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class u0 implements Executor {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f7222a;

    /* renamed from: b  reason: collision with root package name */
    private final ArrayDeque<Runnable> f7223b = new ArrayDeque<>();

    /* renamed from: c  reason: collision with root package name */
    private Runnable f7224c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Runnable f7225a;

        a(Runnable runnable) {
            this.f7225a = runnable;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.f7225a.run();
            } finally {
                u0.this.a();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public u0(Executor executor) {
        this.f7222a = executor;
    }

    synchronized void a() {
        Runnable poll = this.f7223b.poll();
        this.f7224c = poll;
        if (poll != null) {
            this.f7222a.execute(poll);
        }
    }

    @Override // java.util.concurrent.Executor
    public synchronized void execute(Runnable runnable) {
        this.f7223b.offer(new a(runnable));
        if (this.f7224c == null) {
            a();
        }
    }
}
