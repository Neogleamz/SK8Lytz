package c6;

import android.os.Handler;
import android.os.SystemClock;
import b6.l0;
import c6.v;
import com.google.android.exoplayer2.w0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface v {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final Handler f8452a;

        /* renamed from: b  reason: collision with root package name */
        private final v f8453b;

        public a(Handler handler, v vVar) {
            this.f8452a = vVar != null ? (Handler) b6.a.e(handler) : null;
            this.f8453b = vVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void q(String str, long j8, long j9) {
            ((v) l0.j(this.f8453b)).d(str, j8, j9);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void r(String str) {
            ((v) l0.j(this.f8453b)).c(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void s(l4.e eVar) {
            eVar.c();
            ((v) l0.j(this.f8453b)).o(eVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void t(int i8, long j8) {
            ((v) l0.j(this.f8453b)).i(i8, j8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void u(l4.e eVar) {
            ((v) l0.j(this.f8453b)).e(eVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void v(w0 w0Var, l4.g gVar) {
            ((v) l0.j(this.f8453b)).F(w0Var);
            ((v) l0.j(this.f8453b)).u(w0Var, gVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void w(Object obj, long j8) {
            ((v) l0.j(this.f8453b)).k(obj, j8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void x(long j8, int i8) {
            ((v) l0.j(this.f8453b)).y(j8, i8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void y(Exception exc) {
            ((v) l0.j(this.f8453b)).v(exc);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void z(x xVar) {
            ((v) l0.j(this.f8453b)).p(xVar);
        }

        public void A(final Object obj) {
            if (this.f8452a != null) {
                final long elapsedRealtime = SystemClock.elapsedRealtime();
                this.f8452a.post(new Runnable() { // from class: c6.q
                    @Override // java.lang.Runnable
                    public final void run() {
                        v.a.this.w(obj, elapsedRealtime);
                    }
                });
            }
        }

        public void B(final long j8, final int i8) {
            Handler handler = this.f8452a;
            if (handler != null) {
                handler.post(new Runnable() { // from class: c6.m
                    @Override // java.lang.Runnable
                    public final void run() {
                        v.a.this.x(j8, i8);
                    }
                });
            }
        }

        public void C(final Exception exc) {
            Handler handler = this.f8452a;
            if (handler != null) {
                handler.post(new Runnable() { // from class: c6.p
                    @Override // java.lang.Runnable
                    public final void run() {
                        v.a.this.y(exc);
                    }
                });
            }
        }

        public void D(final x xVar) {
            Handler handler = this.f8452a;
            if (handler != null) {
                handler.post(new Runnable() { // from class: c6.n
                    @Override // java.lang.Runnable
                    public final void run() {
                        v.a.this.z(xVar);
                    }
                });
            }
        }

        public void k(final String str, final long j8, final long j9) {
            Handler handler = this.f8452a;
            if (handler != null) {
                handler.post(new Runnable() { // from class: c6.s
                    @Override // java.lang.Runnable
                    public final void run() {
                        v.a.this.q(str, j8, j9);
                    }
                });
            }
        }

        public void l(final String str) {
            Handler handler = this.f8452a;
            if (handler != null) {
                handler.post(new Runnable() { // from class: c6.r
                    @Override // java.lang.Runnable
                    public final void run() {
                        v.a.this.r(str);
                    }
                });
            }
        }

        public void m(final l4.e eVar) {
            eVar.c();
            Handler handler = this.f8452a;
            if (handler != null) {
                handler.post(new Runnable() { // from class: c6.t
                    @Override // java.lang.Runnable
                    public final void run() {
                        v.a.this.s(eVar);
                    }
                });
            }
        }

        public void n(final int i8, final long j8) {
            Handler handler = this.f8452a;
            if (handler != null) {
                handler.post(new Runnable() { // from class: c6.l
                    @Override // java.lang.Runnable
                    public final void run() {
                        v.a.this.t(i8, j8);
                    }
                });
            }
        }

        public void o(final l4.e eVar) {
            Handler handler = this.f8452a;
            if (handler != null) {
                handler.post(new Runnable() { // from class: c6.u
                    @Override // java.lang.Runnable
                    public final void run() {
                        v.a.this.u(eVar);
                    }
                });
            }
        }

        public void p(final w0 w0Var, final l4.g gVar) {
            Handler handler = this.f8452a;
            if (handler != null) {
                handler.post(new Runnable() { // from class: c6.o
                    @Override // java.lang.Runnable
                    public final void run() {
                        v.a.this.v(w0Var, gVar);
                    }
                });
            }
        }
    }

    @Deprecated
    default void F(w0 w0Var) {
    }

    default void c(String str) {
    }

    default void d(String str, long j8, long j9) {
    }

    default void e(l4.e eVar) {
    }

    default void i(int i8, long j8) {
    }

    default void k(Object obj, long j8) {
    }

    default void o(l4.e eVar) {
    }

    default void p(x xVar) {
    }

    default void u(w0 w0Var, l4.g gVar) {
    }

    default void v(Exception exc) {
    }

    default void y(long j8, int i8) {
    }
}
