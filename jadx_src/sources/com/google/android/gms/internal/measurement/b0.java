package com.google.android.gms.internal.measurement;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b0 {

    /* renamed from: a  reason: collision with root package name */
    private final k3 f12085a;

    /* renamed from: b  reason: collision with root package name */
    private g6 f12086b;

    /* renamed from: c  reason: collision with root package name */
    d f12087c;

    /* renamed from: d  reason: collision with root package name */
    private final b f12088d;

    public b0() {
        this(new k3());
    }

    private b0(k3 k3Var) {
        this.f12085a = k3Var;
        this.f12086b = k3Var.f12275b.d();
        this.f12087c = new d();
        this.f12088d = new b();
        k3Var.b("internal.registerCallback", new Callable() { // from class: com.google.android.gms.internal.measurement.a
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return b0.this.e();
            }
        });
        k3Var.b("internal.eventLogger", new Callable() { // from class: com.google.android.gms.internal.measurement.z1
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return new e9(b0.this.f12087c);
            }
        });
    }

    public final d a() {
        return this.f12087c;
    }

    public final void b(i5 i5Var) {
        m mVar;
        try {
            this.f12086b = this.f12085a.f12275b.d();
            if (this.f12085a.a(this.f12086b, (zzgb$zzd[]) i5Var.J().toArray(new zzgb$zzd[0])) instanceof k) {
                throw new IllegalStateException("Program loading failed");
            }
            for (h5 h5Var : i5Var.H().J()) {
                List<zzgb$zzd> J = h5Var.J();
                String I = h5Var.I();
                Iterator<zzgb$zzd> it = J.iterator();
                while (it.hasNext()) {
                    r a9 = this.f12085a.a(this.f12086b, it.next());
                    if (!(a9 instanceof q)) {
                        throw new IllegalArgumentException("Invalid rule definition");
                    }
                    g6 g6Var = this.f12086b;
                    if (g6Var.g(I)) {
                        r c9 = g6Var.c(I);
                        if (!(c9 instanceof m)) {
                            throw new IllegalStateException("Invalid function name: " + I);
                        }
                        mVar = (m) c9;
                    } else {
                        mVar = null;
                    }
                    if (mVar == null) {
                        throw new IllegalStateException("Rule function is undefined: " + I);
                    }
                    mVar.c(this.f12086b, Collections.singletonList(a9));
                }
            }
        } catch (Throwable th) {
            throw new zzc(th);
        }
    }

    public final void c(String str, Callable<? extends m> callable) {
        this.f12085a.b(str, callable);
    }

    public final boolean d(e eVar) {
        try {
            this.f12087c.b(eVar);
            this.f12085a.f12276c.h("runtime.counter", new j(Double.valueOf(0.0d)));
            this.f12088d.b(this.f12086b.d(), this.f12087c);
            if (g()) {
                return true;
            }
            return f();
        } catch (Throwable th) {
            throw new zzc(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ m e() {
        return new lg(this.f12088d);
    }

    public final boolean f() {
        return !this.f12087c.f().isEmpty();
    }

    public final boolean g() {
        return !this.f12087c.d().equals(this.f12087c.a());
    }
}
