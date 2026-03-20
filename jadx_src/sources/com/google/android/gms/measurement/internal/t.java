package com.google.android.gms.measurement.internal;

import android.os.Handler;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class t {

    /* renamed from: d  reason: collision with root package name */
    private static volatile Handler f16988d;

    /* renamed from: a  reason: collision with root package name */
    private final f7 f16989a;

    /* renamed from: b  reason: collision with root package name */
    private final Runnable f16990b;

    /* renamed from: c  reason: collision with root package name */
    private volatile long f16991c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t(f7 f7Var) {
        n6.j.l(f7Var);
        this.f16989a = f7Var;
        this.f16990b = new s(this, f7Var);
    }

    private final Handler f() {
        Handler handler;
        if (f16988d != null) {
            return f16988d;
        }
        synchronized (t.class) {
            if (f16988d == null) {
                f16988d = new com.google.android.gms.internal.measurement.b2(this.f16989a.zza().getMainLooper());
            }
            handler = f16988d;
        }
        return handler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a() {
        this.f16991c = 0L;
        f().removeCallbacks(this.f16990b);
    }

    public final void b(long j8) {
        a();
        if (j8 >= 0) {
            this.f16991c = this.f16989a.zzb().a();
            if (f().postDelayed(this.f16990b, j8)) {
                return;
            }
            this.f16989a.i().E().b("Failed to schedule delayed post. time", Long.valueOf(j8));
        }
    }

    public abstract void d();

    public final boolean e() {
        return this.f16991c != 0;
    }
}
