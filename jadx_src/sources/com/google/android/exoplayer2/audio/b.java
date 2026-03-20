package com.google.android.exoplayer2.audio;

import android.os.Handler;
import b6.l0;
import com.google.android.exoplayer2.w0;
import k4.o;
import k4.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface b {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final Handler f9332a;

        /* renamed from: b  reason: collision with root package name */
        private final b f9333b;

        public a(Handler handler, b bVar) {
            this.f9332a = bVar != null ? (Handler) b6.a.e(handler) : null;
            this.f9333b = bVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void A(int i8, long j8, long j9) {
            ((b) l0.j(this.f9333b)).x(i8, j8, j9);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void r(Exception exc) {
            ((b) l0.j(this.f9333b)).t(exc);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void s(Exception exc) {
            ((b) l0.j(this.f9333b)).b(exc);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void t(String str, long j8, long j9) {
            ((b) l0.j(this.f9333b)).g(str, j8, j9);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void u(String str) {
            ((b) l0.j(this.f9333b)).f(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void v(l4.e eVar) {
            eVar.c();
            ((b) l0.j(this.f9333b)).j(eVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void w(l4.e eVar) {
            ((b) l0.j(this.f9333b)).m(eVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void x(w0 w0Var, l4.g gVar) {
            ((b) l0.j(this.f9333b)).G(w0Var);
            ((b) l0.j(this.f9333b)).n(w0Var, gVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void y(long j8) {
            ((b) l0.j(this.f9333b)).s(j8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void z(boolean z4) {
            ((b) l0.j(this.f9333b)).a(z4);
        }

        public void B(long j8) {
            Handler handler = this.f9332a;
            if (handler != null) {
                handler.post(new k4.h(this, j8));
            }
        }

        public void C(boolean z4) {
            Handler handler = this.f9332a;
            if (handler != null) {
                handler.post(new p(this, z4));
            }
        }

        public void D(int i8, long j8, long j9) {
            Handler handler = this.f9332a;
            if (handler != null) {
                handler.post(new k4.g(this, i8, j8, j9));
            }
        }

        public void k(Exception exc) {
            Handler handler = this.f9332a;
            if (handler != null) {
                handler.post(new k4.j(this, exc));
            }
        }

        public void l(Exception exc) {
            Handler handler = this.f9332a;
            if (handler != null) {
                handler.post(new k4.k(this, exc));
            }
        }

        public void m(String str, long j8, long j9) {
            Handler handler = this.f9332a;
            if (handler != null) {
                handler.post(new k4.m(this, str, j8, j9));
            }
        }

        public void n(String str) {
            Handler handler = this.f9332a;
            if (handler != null) {
                handler.post(new k4.l(this, str));
            }
        }

        public void o(l4.e eVar) {
            eVar.c();
            Handler handler = this.f9332a;
            if (handler != null) {
                handler.post(new k4.n(this, eVar));
            }
        }

        public void p(l4.e eVar) {
            Handler handler = this.f9332a;
            if (handler != null) {
                handler.post(new o(this, eVar));
            }
        }

        public void q(w0 w0Var, l4.g gVar) {
            Handler handler = this.f9332a;
            if (handler != null) {
                handler.post(new k4.i(this, w0Var, gVar));
            }
        }
    }

    @Deprecated
    default void G(w0 w0Var) {
    }

    default void a(boolean z4) {
    }

    default void b(Exception exc) {
    }

    default void f(String str) {
    }

    default void g(String str, long j8, long j9) {
    }

    default void j(l4.e eVar) {
    }

    default void m(l4.e eVar) {
    }

    default void n(w0 w0Var, l4.g gVar) {
    }

    default void s(long j8) {
    }

    default void t(Exception exc) {
    }

    default void x(int i8, long j8, long j9) {
    }
}
