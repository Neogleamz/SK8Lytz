package com.google.android.gms.measurement.internal;

import android.content.Context;
import java.lang.Thread;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a6 extends d7 {

    /* renamed from: l  reason: collision with root package name */
    private static final AtomicLong f16301l = new AtomicLong(Long.MIN_VALUE);

    /* renamed from: c  reason: collision with root package name */
    private c6 f16302c;

    /* renamed from: d  reason: collision with root package name */
    private c6 f16303d;

    /* renamed from: e  reason: collision with root package name */
    private final PriorityBlockingQueue<d6<?>> f16304e;

    /* renamed from: f  reason: collision with root package name */
    private final BlockingQueue<d6<?>> f16305f;

    /* renamed from: g  reason: collision with root package name */
    private final Thread.UncaughtExceptionHandler f16306g;

    /* renamed from: h  reason: collision with root package name */
    private final Thread.UncaughtExceptionHandler f16307h;

    /* renamed from: i  reason: collision with root package name */
    private final Object f16308i;

    /* renamed from: j  reason: collision with root package name */
    private final Semaphore f16309j;

    /* renamed from: k  reason: collision with root package name */
    private volatile boolean f16310k;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a6(f6 f6Var) {
        super(f6Var);
        this.f16308i = new Object();
        this.f16309j = new Semaphore(2);
        this.f16304e = new PriorityBlockingQueue<>();
        this.f16305f = new LinkedBlockingQueue();
        this.f16306g = new b6(this, "Thread death: Uncaught exception on worker thread");
        this.f16307h = new b6(this, "Thread death: Uncaught exception on network thread");
    }

    private final void w(d6<?> d6Var) {
        synchronized (this.f16308i) {
            this.f16304e.add(d6Var);
            c6 c6Var = this.f16302c;
            if (c6Var == null) {
                c6 c6Var2 = new c6(this, "Measurement Worker", this.f16304e);
                this.f16302c = c6Var2;
                c6Var2.setUncaughtExceptionHandler(this.f16306g);
                this.f16302c.start();
            } else {
                c6Var.a();
            }
        }
    }

    public final void B(Runnable runnable) {
        n();
        n6.j.l(runnable);
        w(new d6<>(this, runnable, false, "Task exception on worker thread"));
    }

    public final void E(Runnable runnable) {
        n();
        n6.j.l(runnable);
        w(new d6<>(this, runnable, true, "Task exception on worker thread"));
    }

    public final boolean H() {
        return Thread.currentThread() == this.f16302c;
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ e a() {
        return super.a();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ d b() {
        return super.b();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ x c() {
        return super.c();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ s4 e() {
        return super.e();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ h5 f() {
        return super.f();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ sb g() {
        return super.g();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final void h() {
        if (Thread.currentThread() != this.f16303d) {
            throw new IllegalStateException("Call expected from network thread");
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ x4 i() {
        return super.i();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void j() {
        super.j();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final void k() {
        if (Thread.currentThread() != this.f16302c) {
            throw new IllegalStateException("Call expected from worker thread");
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ a6 l() {
        return super.l();
    }

    @Override // com.google.android.gms.measurement.internal.d7
    protected final boolean r() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final <T> T t(AtomicReference<T> atomicReference, long j8, String str, Runnable runnable) {
        synchronized (atomicReference) {
            l().B(runnable);
            try {
                atomicReference.wait(j8);
            } catch (InterruptedException unused) {
                z4 J = i().J();
                J.a("Interrupted waiting for " + str);
                return null;
            }
        }
        T t8 = atomicReference.get();
        if (t8 == null) {
            z4 J2 = i().J();
            J2.a("Timed out waiting for " + str);
        }
        return t8;
    }

    public final <V> Future<V> u(Callable<V> callable) {
        n();
        n6.j.l(callable);
        d6<?> d6Var = new d6<>(this, (Callable<?>) callable, false, "Task exception on worker thread");
        if (Thread.currentThread() == this.f16302c) {
            if (!this.f16304e.isEmpty()) {
                i().J().a("Callable skipped the worker queue.");
            }
            d6Var.run();
        } else {
            w(d6Var);
        }
        return d6Var;
    }

    public final void x(Runnable runnable) {
        n();
        n6.j.l(runnable);
        d6<?> d6Var = new d6<>(this, runnable, false, "Task exception on network thread");
        synchronized (this.f16308i) {
            this.f16305f.add(d6Var);
            c6 c6Var = this.f16303d;
            if (c6Var == null) {
                c6 c6Var2 = new c6(this, "Measurement Network", this.f16305f);
                this.f16303d = c6Var2;
                c6Var2.setUncaughtExceptionHandler(this.f16307h);
                this.f16303d.start();
            } else {
                c6Var.a();
            }
        }
    }

    public final <V> Future<V> z(Callable<V> callable) {
        n();
        n6.j.l(callable);
        d6<?> d6Var = new d6<>(this, (Callable<?>) callable, true, "Task exception on worker thread");
        if (Thread.currentThread() == this.f16302c) {
            d6Var.run();
        } else {
            w(d6Var);
        }
        return d6Var;
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ Context zza() {
        return super.zza();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ u6.d zzb() {
        return super.zzb();
    }
}
