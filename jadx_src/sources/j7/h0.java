package j7;

import java.util.ArrayDeque;
import java.util.Queue;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h0 {

    /* renamed from: a  reason: collision with root package name */
    private final Object f20814a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private Queue f20815b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f20816c;

    public final void a(g0 g0Var) {
        synchronized (this.f20814a) {
            if (this.f20815b == null) {
                this.f20815b = new ArrayDeque();
            }
            this.f20815b.add(g0Var);
        }
    }

    public final void b(j jVar) {
        g0 g0Var;
        synchronized (this.f20814a) {
            if (this.f20815b != null && !this.f20816c) {
                this.f20816c = true;
                while (true) {
                    synchronized (this.f20814a) {
                        g0Var = (g0) this.f20815b.poll();
                        if (g0Var == null) {
                            this.f20816c = false;
                            return;
                        }
                    }
                    g0Var.d(jVar);
                }
            }
        }
    }
}
