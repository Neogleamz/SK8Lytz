package z;

import androidx.core.util.h;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g implements Executor {

    /* renamed from: b  reason: collision with root package name */
    private final Executor f24503b;

    /* renamed from: a  reason: collision with root package name */
    final Deque<Runnable> f24502a = new ArrayDeque();

    /* renamed from: c  reason: collision with root package name */
    private final b f24504c = new b();

    /* renamed from: d  reason: collision with root package name */
    c f24505d = c.IDLE;

    /* renamed from: e  reason: collision with root package name */
    long f24506e = 0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Runnable f24507a;

        a(Runnable runnable) {
            this.f24507a = runnable;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f24507a.run();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class b implements Runnable {
        b() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:17:0x0039, code lost:
            if (r1 == false) goto L35;
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x003b, code lost:
            java.lang.Thread.currentThread().interrupt();
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x0042, code lost:
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x0048, code lost:
            r1 = r1 | java.lang.Thread.interrupted();
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0049, code lost:
            r3.run();
         */
        /* JADX WARN: Code restructure failed: missing block: B:25:0x004d, code lost:
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x004e, code lost:
            androidx.camera.core.p1.d("SequentialExecutor", "Exception while executing runnable " + r3, r2);
         */
        /* JADX WARN: Code restructure failed: missing block: B:44:?, code lost:
            return;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void a() {
            /*
                r9 = this;
                r0 = 0
                r1 = r0
            L2:
                z.g r2 = z.g.this     // Catch: java.lang.Throwable -> L68
                java.util.Deque<java.lang.Runnable> r2 = r2.f24502a     // Catch: java.lang.Throwable -> L68
                monitor-enter(r2)     // Catch: java.lang.Throwable -> L68
                if (r0 != 0) goto L26
                z.g r0 = z.g.this     // Catch: java.lang.Throwable -> L65
                z.g$c r3 = r0.f24505d     // Catch: java.lang.Throwable -> L65
                z.g$c r4 = z.g.c.RUNNING     // Catch: java.lang.Throwable -> L65
                if (r3 != r4) goto L1c
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L65
                if (r1 == 0) goto L1b
                java.lang.Thread r0 = java.lang.Thread.currentThread()
                r0.interrupt()
            L1b:
                return
            L1c:
                long r5 = r0.f24506e     // Catch: java.lang.Throwable -> L65
                r7 = 1
                long r5 = r5 + r7
                r0.f24506e = r5     // Catch: java.lang.Throwable -> L65
                r0.f24505d = r4     // Catch: java.lang.Throwable -> L65
                r0 = 1
            L26:
                z.g r3 = z.g.this     // Catch: java.lang.Throwable -> L65
                java.util.Deque<java.lang.Runnable> r3 = r3.f24502a     // Catch: java.lang.Throwable -> L65
                java.lang.Object r3 = r3.poll()     // Catch: java.lang.Throwable -> L65
                java.lang.Runnable r3 = (java.lang.Runnable) r3     // Catch: java.lang.Throwable -> L65
                if (r3 != 0) goto L43
                z.g r0 = z.g.this     // Catch: java.lang.Throwable -> L65
                z.g$c r3 = z.g.c.IDLE     // Catch: java.lang.Throwable -> L65
                r0.f24505d = r3     // Catch: java.lang.Throwable -> L65
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L65
                if (r1 == 0) goto L42
                java.lang.Thread r0 = java.lang.Thread.currentThread()
                r0.interrupt()
            L42:
                return
            L43:
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L65
                boolean r2 = java.lang.Thread.interrupted()     // Catch: java.lang.Throwable -> L68
                r1 = r1 | r2
                r3.run()     // Catch: java.lang.RuntimeException -> L4d java.lang.Throwable -> L68
                goto L2
            L4d:
                r2 = move-exception
                java.lang.String r4 = "SequentialExecutor"
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L68
                r5.<init>()     // Catch: java.lang.Throwable -> L68
                java.lang.String r6 = "Exception while executing runnable "
                r5.append(r6)     // Catch: java.lang.Throwable -> L68
                r5.append(r3)     // Catch: java.lang.Throwable -> L68
                java.lang.String r3 = r5.toString()     // Catch: java.lang.Throwable -> L68
                androidx.camera.core.p1.d(r4, r3, r2)     // Catch: java.lang.Throwable -> L68
                goto L2
            L65:
                r0 = move-exception
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L65
                throw r0     // Catch: java.lang.Throwable -> L68
            L68:
                r0 = move-exception
                if (r1 == 0) goto L72
                java.lang.Thread r1 = java.lang.Thread.currentThread()
                r1.interrupt()
            L72:
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: z.g.b.a():void");
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                a();
            } catch (Error e8) {
                synchronized (g.this.f24502a) {
                    g.this.f24505d = c.IDLE;
                    throw e8;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum c {
        IDLE,
        QUEUING,
        QUEUED,
        RUNNING
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(Executor executor) {
        this.f24503b = (Executor) h.h(executor);
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        c cVar;
        h.h(runnable);
        synchronized (this.f24502a) {
            c cVar2 = this.f24505d;
            if (cVar2 != c.RUNNING && cVar2 != (cVar = c.QUEUED)) {
                long j8 = this.f24506e;
                a aVar = new a(runnable);
                this.f24502a.add(aVar);
                c cVar3 = c.QUEUING;
                this.f24505d = cVar3;
                try {
                    this.f24503b.execute(this.f24504c);
                    if (this.f24505d != cVar3) {
                        return;
                    }
                    synchronized (this.f24502a) {
                        if (this.f24506e == j8 && this.f24505d == cVar3) {
                            this.f24505d = cVar;
                        }
                    }
                    return;
                } catch (Error | RuntimeException e8) {
                    synchronized (this.f24502a) {
                        c cVar4 = this.f24505d;
                        if ((cVar4 != c.IDLE && cVar4 != c.QUEUING) || !this.f24502a.removeLastOccurrence(aVar)) {
                            r0 = false;
                        }
                        if (!(e8 instanceof RejectedExecutionException) || r0) {
                            throw e8;
                        }
                    }
                    return;
                }
            }
            this.f24502a.add(runnable);
        }
    }
}
