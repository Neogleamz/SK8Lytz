package com.google.android.gms.measurement.internal;

import android.os.Process;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c6 extends Thread {

    /* renamed from: a  reason: collision with root package name */
    private final Object f16432a;

    /* renamed from: b  reason: collision with root package name */
    private final BlockingQueue<d6<?>> f16433b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f16434c = false;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ a6 f16435d;

    public c6(a6 a6Var, String str, BlockingQueue<d6<?>> blockingQueue) {
        this.f16435d = a6Var;
        n6.j.l(str);
        n6.j.l(blockingQueue);
        this.f16432a = new Object();
        this.f16433b = blockingQueue;
        setName(str);
    }

    private final void b(InterruptedException interruptedException) {
        z4 J = this.f16435d.i().J();
        String name = getName();
        J.b(name + " was interrupted", interruptedException);
    }

    private final void c() {
        Object obj;
        Semaphore semaphore;
        Object obj2;
        c6 c6Var;
        c6 c6Var2;
        obj = this.f16435d.f16308i;
        synchronized (obj) {
            if (!this.f16434c) {
                semaphore = this.f16435d.f16309j;
                semaphore.release();
                obj2 = this.f16435d.f16308i;
                obj2.notifyAll();
                c6Var = this.f16435d.f16302c;
                if (this == c6Var) {
                    this.f16435d.f16302c = null;
                } else {
                    c6Var2 = this.f16435d.f16303d;
                    if (this == c6Var2) {
                        this.f16435d.f16303d = null;
                    } else {
                        this.f16435d.i().E().a("Current scheduler thread is neither worker nor network");
                    }
                }
                this.f16434c = true;
            }
        }
    }

    public final void a() {
        synchronized (this.f16432a) {
            this.f16432a.notifyAll();
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        Object obj;
        boolean z4;
        Semaphore semaphore;
        boolean z8 = false;
        while (!z8) {
            try {
                semaphore = this.f16435d.f16309j;
                semaphore.acquire();
                z8 = true;
            } catch (InterruptedException e8) {
                b(e8);
            }
        }
        try {
            int threadPriority = Process.getThreadPriority(Process.myTid());
            while (true) {
                d6<?> poll = this.f16433b.poll();
                if (poll != null) {
                    Process.setThreadPriority(poll.f16454b ? threadPriority : 10);
                    poll.run();
                } else {
                    synchronized (this.f16432a) {
                        if (this.f16433b.peek() == null) {
                            z4 = this.f16435d.f16310k;
                            if (!z4) {
                                try {
                                    this.f16432a.wait(30000L);
                                } catch (InterruptedException e9) {
                                    b(e9);
                                }
                            }
                        }
                    }
                    obj = this.f16435d.f16308i;
                    synchronized (obj) {
                        if (this.f16433b.peek() == null) {
                            c();
                            return;
                        }
                    }
                }
            }
        } finally {
            c();
        }
    }
}
