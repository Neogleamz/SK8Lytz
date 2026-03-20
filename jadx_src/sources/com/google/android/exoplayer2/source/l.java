package com.google.android.exoplayer2.source;

import android.os.Handler;
import b6.l0;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.w0;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface l {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        public final int f10617a;

        /* renamed from: b  reason: collision with root package name */
        public final k.b f10618b;

        /* renamed from: c  reason: collision with root package name */
        private final CopyOnWriteArrayList<C0113a> f10619c;

        /* renamed from: d  reason: collision with root package name */
        private final long f10620d;

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: com.google.android.exoplayer2.source.l$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class C0113a {

            /* renamed from: a  reason: collision with root package name */
            public Handler f10621a;

            /* renamed from: b  reason: collision with root package name */
            public l f10622b;

            public C0113a(Handler handler, l lVar) {
                this.f10621a = handler;
                this.f10622b = lVar;
            }
        }

        public a() {
            this(new CopyOnWriteArrayList(), 0, null, 0L);
        }

        private a(CopyOnWriteArrayList<C0113a> copyOnWriteArrayList, int i8, k.b bVar, long j8) {
            this.f10619c = copyOnWriteArrayList;
            this.f10617a = i8;
            this.f10618b = bVar;
            this.f10620d = j8;
        }

        private long h(long j8) {
            long a12 = l0.a1(j8);
            if (a12 == -9223372036854775807L) {
                return -9223372036854775807L;
            }
            return this.f10620d + a12;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void k(l lVar, h5.i iVar) {
            lVar.M(this.f10617a, this.f10618b, iVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void l(l lVar, h5.h hVar, h5.i iVar) {
            lVar.c0(this.f10617a, this.f10618b, hVar, iVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void m(l lVar, h5.h hVar, h5.i iVar) {
            lVar.O(this.f10617a, this.f10618b, hVar, iVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void n(l lVar, h5.h hVar, h5.i iVar, IOException iOException, boolean z4) {
            lVar.i0(this.f10617a, this.f10618b, hVar, iVar, iOException, z4);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void o(l lVar, h5.h hVar, h5.i iVar) {
            lVar.R(this.f10617a, this.f10618b, hVar, iVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void p(l lVar, k.b bVar, h5.i iVar) {
            lVar.I(this.f10617a, bVar, iVar);
        }

        public void A(h5.h hVar, int i8, int i9, w0 w0Var, int i10, Object obj, long j8, long j9) {
            B(hVar, new h5.i(i8, i9, w0Var, i10, obj, h(j8), h(j9)));
        }

        public void B(h5.h hVar, h5.i iVar) {
            Iterator<C0113a> it = this.f10619c.iterator();
            while (it.hasNext()) {
                C0113a next = it.next();
                l0.L0(next.f10621a, new h5.m(this, next.f10622b, hVar, iVar));
            }
        }

        public void C(l lVar) {
            Iterator<C0113a> it = this.f10619c.iterator();
            while (it.hasNext()) {
                C0113a next = it.next();
                if (next.f10622b == lVar) {
                    this.f10619c.remove(next);
                }
            }
        }

        public void D(int i8, long j8, long j9) {
            E(new h5.i(1, i8, null, 3, null, h(j8), h(j9)));
        }

        public void E(h5.i iVar) {
            k.b bVar = (k.b) b6.a.e(this.f10618b);
            Iterator<C0113a> it = this.f10619c.iterator();
            while (it.hasNext()) {
                C0113a next = it.next();
                l0.L0(next.f10621a, new h5.k(this, next.f10622b, bVar, iVar));
            }
        }

        public a F(int i8, k.b bVar, long j8) {
            return new a(this.f10619c, i8, bVar, j8);
        }

        public void g(Handler handler, l lVar) {
            b6.a.e(handler);
            b6.a.e(lVar);
            this.f10619c.add(new C0113a(handler, lVar));
        }

        public void i(int i8, w0 w0Var, int i9, Object obj, long j8) {
            j(new h5.i(1, i8, w0Var, i9, obj, h(j8), -9223372036854775807L));
        }

        public void j(h5.i iVar) {
            Iterator<C0113a> it = this.f10619c.iterator();
            while (it.hasNext()) {
                C0113a next = it.next();
                l0.L0(next.f10621a, new h5.p(this, next.f10622b, iVar));
            }
        }

        public void q(h5.h hVar, int i8) {
            r(hVar, i8, -1, null, 0, null, -9223372036854775807L, -9223372036854775807L);
        }

        public void r(h5.h hVar, int i8, int i9, w0 w0Var, int i10, Object obj, long j8, long j9) {
            s(hVar, new h5.i(i8, i9, w0Var, i10, obj, h(j8), h(j9)));
        }

        public void s(h5.h hVar, h5.i iVar) {
            Iterator<C0113a> it = this.f10619c.iterator();
            while (it.hasNext()) {
                C0113a next = it.next();
                l0.L0(next.f10621a, new h5.n(this, next.f10622b, hVar, iVar));
            }
        }

        public void t(h5.h hVar, int i8) {
            u(hVar, i8, -1, null, 0, null, -9223372036854775807L, -9223372036854775807L);
        }

        public void u(h5.h hVar, int i8, int i9, w0 w0Var, int i10, Object obj, long j8, long j9) {
            v(hVar, new h5.i(i8, i9, w0Var, i10, obj, h(j8), h(j9)));
        }

        public void v(h5.h hVar, h5.i iVar) {
            Iterator<C0113a> it = this.f10619c.iterator();
            while (it.hasNext()) {
                C0113a next = it.next();
                l0.L0(next.f10621a, new h5.l(this, next.f10622b, hVar, iVar));
            }
        }

        public void w(h5.h hVar, int i8, int i9, w0 w0Var, int i10, Object obj, long j8, long j9, IOException iOException, boolean z4) {
            y(hVar, new h5.i(i8, i9, w0Var, i10, obj, h(j8), h(j9)), iOException, z4);
        }

        public void x(h5.h hVar, int i8, IOException iOException, boolean z4) {
            w(hVar, i8, -1, null, 0, null, -9223372036854775807L, -9223372036854775807L, iOException, z4);
        }

        public void y(h5.h hVar, h5.i iVar, IOException iOException, boolean z4) {
            Iterator<C0113a> it = this.f10619c.iterator();
            while (it.hasNext()) {
                C0113a next = it.next();
                l0.L0(next.f10621a, new h5.o(this, next.f10622b, hVar, iVar, iOException, z4));
            }
        }

        public void z(h5.h hVar, int i8) {
            A(hVar, i8, -1, null, 0, null, -9223372036854775807L, -9223372036854775807L);
        }
    }

    default void I(int i8, k.b bVar, h5.i iVar) {
    }

    default void M(int i8, k.b bVar, h5.i iVar) {
    }

    default void O(int i8, k.b bVar, h5.h hVar, h5.i iVar) {
    }

    default void R(int i8, k.b bVar, h5.h hVar, h5.i iVar) {
    }

    default void c0(int i8, k.b bVar, h5.h hVar, h5.i iVar) {
    }

    default void i0(int i8, k.b bVar, h5.h hVar, h5.i iVar, IOException iOException, boolean z4) {
    }
}
