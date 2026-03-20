package com.google.android.exoplayer2.drm;

import android.os.Handler;
import b6.l0;
import com.google.android.exoplayer2.source.k;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface h {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        public final int f9618a;

        /* renamed from: b  reason: collision with root package name */
        public final k.b f9619b;

        /* renamed from: c  reason: collision with root package name */
        private final CopyOnWriteArrayList<C0106a> f9620c;

        /* renamed from: com.google.android.exoplayer2.drm.h$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private static final class C0106a {

            /* renamed from: a  reason: collision with root package name */
            public Handler f9621a;

            /* renamed from: b  reason: collision with root package name */
            public h f9622b;

            public C0106a(Handler handler, h hVar) {
                this.f9621a = handler;
                this.f9622b = hVar;
            }
        }

        public a() {
            this(new CopyOnWriteArrayList(), 0, null);
        }

        private a(CopyOnWriteArrayList<C0106a> copyOnWriteArrayList, int i8, k.b bVar) {
            this.f9620c = copyOnWriteArrayList;
            this.f9618a = i8;
            this.f9619b = bVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void n(h hVar) {
            hVar.g0(this.f9618a, this.f9619b);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void o(h hVar) {
            hVar.b0(this.f9618a, this.f9619b);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void p(h hVar) {
            hVar.o0(this.f9618a, this.f9619b);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void q(h hVar, int i8) {
            hVar.f0(this.f9618a, this.f9619b);
            hVar.k0(this.f9618a, this.f9619b, i8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void r(h hVar, Exception exc) {
            hVar.J(this.f9618a, this.f9619b, exc);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void s(h hVar) {
            hVar.l0(this.f9618a, this.f9619b);
        }

        public void g(Handler handler, h hVar) {
            b6.a.e(handler);
            b6.a.e(hVar);
            this.f9620c.add(new C0106a(handler, hVar));
        }

        public void h() {
            Iterator<C0106a> it = this.f9620c.iterator();
            while (it.hasNext()) {
                C0106a next = it.next();
                l0.L0(next.f9621a, new m4.f(this, next.f9622b));
            }
        }

        public void i() {
            Iterator<C0106a> it = this.f9620c.iterator();
            while (it.hasNext()) {
                C0106a next = it.next();
                l0.L0(next.f9621a, new m4.e(this, next.f9622b));
            }
        }

        public void j() {
            Iterator<C0106a> it = this.f9620c.iterator();
            while (it.hasNext()) {
                C0106a next = it.next();
                l0.L0(next.f9621a, new m4.g(this, next.f9622b));
            }
        }

        public void k(int i8) {
            Iterator<C0106a> it = this.f9620c.iterator();
            while (it.hasNext()) {
                C0106a next = it.next();
                l0.L0(next.f9621a, new m4.h(this, next.f9622b, i8));
            }
        }

        public void l(Exception exc) {
            Iterator<C0106a> it = this.f9620c.iterator();
            while (it.hasNext()) {
                C0106a next = it.next();
                l0.L0(next.f9621a, new m4.i(this, next.f9622b, exc));
            }
        }

        public void m() {
            Iterator<C0106a> it = this.f9620c.iterator();
            while (it.hasNext()) {
                C0106a next = it.next();
                l0.L0(next.f9621a, new m4.d(this, next.f9622b));
            }
        }

        public void t(h hVar) {
            Iterator<C0106a> it = this.f9620c.iterator();
            while (it.hasNext()) {
                C0106a next = it.next();
                if (next.f9622b == hVar) {
                    this.f9620c.remove(next);
                }
            }
        }

        public a u(int i8, k.b bVar) {
            return new a(this.f9620c, i8, bVar);
        }
    }

    default void J(int i8, k.b bVar, Exception exc) {
    }

    default void b0(int i8, k.b bVar) {
    }

    @Deprecated
    default void f0(int i8, k.b bVar) {
    }

    default void g0(int i8, k.b bVar) {
    }

    default void k0(int i8, k.b bVar, int i9) {
    }

    default void l0(int i8, k.b bVar) {
    }

    default void o0(int i8, k.b bVar) {
    }
}
