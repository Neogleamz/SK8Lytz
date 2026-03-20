package com.google.android.gms.measurement.internal;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d6<V> extends FutureTask<V> implements Comparable<d6<V>> {

    /* renamed from: a  reason: collision with root package name */
    private final long f16453a;

    /* renamed from: b  reason: collision with root package name */
    final boolean f16454b;

    /* renamed from: c  reason: collision with root package name */
    private final String f16455c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ a6 f16456d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public d6(a6 a6Var, Runnable runnable, boolean z4, String str) {
        super(com.google.android.gms.internal.measurement.x1.a().b(runnable), null);
        AtomicLong atomicLong;
        this.f16456d = a6Var;
        n6.j.l(str);
        atomicLong = a6.f16301l;
        long andIncrement = atomicLong.getAndIncrement();
        this.f16453a = andIncrement;
        this.f16455c = str;
        this.f16454b = z4;
        if (andIncrement == Long.MAX_VALUE) {
            a6Var.i().E().a("Tasks index overflow");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public d6(a6 a6Var, Callable<V> callable, boolean z4, String str) {
        super(com.google.android.gms.internal.measurement.x1.a().a(callable));
        AtomicLong atomicLong;
        this.f16456d = a6Var;
        n6.j.l(str);
        atomicLong = a6.f16301l;
        long andIncrement = atomicLong.getAndIncrement();
        this.f16453a = andIncrement;
        this.f16455c = str;
        this.f16454b = z4;
        if (andIncrement == Long.MAX_VALUE) {
            a6Var.i().E().a("Tasks index overflow");
        }
    }

    @Override // java.lang.Comparable
    public final /* synthetic */ int compareTo(Object obj) {
        d6 d6Var = (d6) obj;
        boolean z4 = this.f16454b;
        if (z4 != d6Var.f16454b) {
            return z4 ? -1 : 1;
        }
        long j8 = this.f16453a;
        long j9 = d6Var.f16453a;
        if (j8 < j9) {
            return -1;
        }
        if (j8 > j9) {
            return 1;
        }
        this.f16456d.i().G().b("Two tasks share the same index. index", Long.valueOf(this.f16453a));
        return 0;
    }

    @Override // java.util.concurrent.FutureTask
    protected final void setException(Throwable th) {
        this.f16456d.i().E().b(this.f16455c, th);
        super.setException(th);
    }
}
