package androidx.camera.core;

import androidx.camera.core.h0;
import androidx.camera.core.q0;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q0 extends o0 {

    /* renamed from: t  reason: collision with root package name */
    final Executor f2779t;

    /* renamed from: u  reason: collision with root package name */
    private final Object f2780u = new Object();

    /* renamed from: v  reason: collision with root package name */
    l1 f2781v;

    /* renamed from: w  reason: collision with root package name */
    private b f2782w;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements a0.c<Void> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ b f2783a;

        a(b bVar) {
            this.f2783a = bVar;
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r12) {
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            this.f2783a.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends h0 {

        /* renamed from: d  reason: collision with root package name */
        final WeakReference<q0> f2785d;

        b(l1 l1Var, q0 q0Var) {
            super(l1Var);
            this.f2785d = new WeakReference<>(q0Var);
            a(new h0.a() { // from class: androidx.camera.core.r0
                @Override // androidx.camera.core.h0.a
                public final void b(l1 l1Var2) {
                    q0.b.this.h(l1Var2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void h(l1 l1Var) {
            final q0 q0Var = this.f2785d.get();
            if (q0Var != null) {
                q0Var.f2779t.execute(new Runnable() { // from class: androidx.camera.core.s0
                    @Override // java.lang.Runnable
                    public final void run() {
                        q0.this.z();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public q0(Executor executor) {
        this.f2779t = executor;
    }

    @Override // androidx.camera.core.o0
    l1 d(y.g0 g0Var) {
        return g0Var.acquireLatestImage();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.core.o0
    public void g() {
        synchronized (this.f2780u) {
            l1 l1Var = this.f2781v;
            if (l1Var != null) {
                l1Var.close();
                this.f2781v = null;
            }
        }
    }

    @Override // androidx.camera.core.o0
    void o(l1 l1Var) {
        synchronized (this.f2780u) {
            if (!this.f2760s) {
                l1Var.close();
            } else if (this.f2782w == null) {
                b bVar = new b(l1Var, this);
                this.f2782w = bVar;
                a0.f.b(e(bVar), new a(bVar), z.a.a());
            } else {
                if (l1Var.e1().d() <= this.f2782w.e1().d()) {
                    l1Var.close();
                } else {
                    l1 l1Var2 = this.f2781v;
                    if (l1Var2 != null) {
                        l1Var2.close();
                    }
                    this.f2781v = l1Var;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void z() {
        synchronized (this.f2780u) {
            this.f2782w = null;
            l1 l1Var = this.f2781v;
            if (l1Var != null) {
                this.f2781v = null;
                o(l1Var);
            }
        }
    }
}
