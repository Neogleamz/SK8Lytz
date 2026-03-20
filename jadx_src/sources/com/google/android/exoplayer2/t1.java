package com.google.android.exoplayer2;

import android.util.Pair;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.source.x;
import com.google.android.exoplayer2.t1;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t1 {

    /* renamed from: a  reason: collision with root package name */
    private final j4.t1 f10855a;

    /* renamed from: e  reason: collision with root package name */
    private final d f10859e;

    /* renamed from: h  reason: collision with root package name */
    private final j4.a f10862h;

    /* renamed from: i  reason: collision with root package name */
    private final b6.l f10863i;

    /* renamed from: k  reason: collision with root package name */
    private boolean f10865k;

    /* renamed from: l  reason: collision with root package name */
    private a6.y f10866l;

    /* renamed from: j  reason: collision with root package name */
    private com.google.android.exoplayer2.source.x f10864j = new x.a(0);

    /* renamed from: c  reason: collision with root package name */
    private final IdentityHashMap<com.google.android.exoplayer2.source.j, c> f10857c = new IdentityHashMap<>();

    /* renamed from: d  reason: collision with root package name */
    private final Map<Object, c> f10858d = new HashMap();

    /* renamed from: b  reason: collision with root package name */
    private final List<c> f10856b = new ArrayList();

    /* renamed from: f  reason: collision with root package name */
    private final HashMap<c, b> f10860f = new HashMap<>();

    /* renamed from: g  reason: collision with root package name */
    private final Set<c> f10861g = new HashSet();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class a implements com.google.android.exoplayer2.source.l, com.google.android.exoplayer2.drm.h {

        /* renamed from: a  reason: collision with root package name */
        private final c f10867a;

        public a(c cVar) {
            this.f10867a = cVar;
        }

        private Pair<Integer, k.b> E(int i8, k.b bVar) {
            k.b bVar2 = null;
            if (bVar != null) {
                k.b n8 = t1.n(this.f10867a, bVar);
                if (n8 == null) {
                    return null;
                }
                bVar2 = n8;
            }
            return Pair.create(Integer.valueOf(t1.r(this.f10867a, i8)), bVar2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void F(Pair pair, h5.i iVar) {
            t1.this.f10862h.M(((Integer) pair.first).intValue(), (k.b) pair.second, iVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void G(Pair pair) {
            t1.this.f10862h.g0(((Integer) pair.first).intValue(), (k.b) pair.second);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void H(Pair pair) {
            t1.this.f10862h.b0(((Integer) pair.first).intValue(), (k.b) pair.second);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void K(Pair pair) {
            t1.this.f10862h.o0(((Integer) pair.first).intValue(), (k.b) pair.second);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void L(Pair pair, int i8) {
            t1.this.f10862h.k0(((Integer) pair.first).intValue(), (k.b) pair.second, i8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void N(Pair pair, Exception exc) {
            t1.this.f10862h.J(((Integer) pair.first).intValue(), (k.b) pair.second, exc);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void P(Pair pair) {
            t1.this.f10862h.l0(((Integer) pair.first).intValue(), (k.b) pair.second);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void Q(Pair pair, h5.h hVar, h5.i iVar) {
            t1.this.f10862h.c0(((Integer) pair.first).intValue(), (k.b) pair.second, hVar, iVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void T(Pair pair, h5.h hVar, h5.i iVar) {
            t1.this.f10862h.O(((Integer) pair.first).intValue(), (k.b) pair.second, hVar, iVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void U(Pair pair, h5.h hVar, h5.i iVar, IOException iOException, boolean z4) {
            t1.this.f10862h.i0(((Integer) pair.first).intValue(), (k.b) pair.second, hVar, iVar, iOException, z4);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void V(Pair pair, h5.h hVar, h5.i iVar) {
            t1.this.f10862h.R(((Integer) pair.first).intValue(), (k.b) pair.second, hVar, iVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void Y(Pair pair, h5.i iVar) {
            t1.this.f10862h.I(((Integer) pair.first).intValue(), (k.b) b6.a.e((k.b) pair.second), iVar);
        }

        @Override // com.google.android.exoplayer2.source.l
        public void I(int i8, k.b bVar, final h5.i iVar) {
            final Pair<Integer, k.b> E = E(i8, bVar);
            if (E != null) {
                t1.this.f10863i.b(new Runnable() { // from class: com.google.android.exoplayer2.i1
                    @Override // java.lang.Runnable
                    public final void run() {
                        t1.a.this.Y(E, iVar);
                    }
                });
            }
        }

        @Override // com.google.android.exoplayer2.drm.h
        public void J(int i8, k.b bVar, final Exception exc) {
            final Pair<Integer, k.b> E = E(i8, bVar);
            if (E != null) {
                t1.this.f10863i.b(new Runnable() { // from class: com.google.android.exoplayer2.j1
                    @Override // java.lang.Runnable
                    public final void run() {
                        t1.a.this.N(E, exc);
                    }
                });
            }
        }

        @Override // com.google.android.exoplayer2.source.l
        public void M(int i8, k.b bVar, final h5.i iVar) {
            final Pair<Integer, k.b> E = E(i8, bVar);
            if (E != null) {
                t1.this.f10863i.b(new Runnable() { // from class: com.google.android.exoplayer2.s1
                    @Override // java.lang.Runnable
                    public final void run() {
                        t1.a.this.F(E, iVar);
                    }
                });
            }
        }

        @Override // com.google.android.exoplayer2.source.l
        public void O(int i8, k.b bVar, final h5.h hVar, final h5.i iVar) {
            final Pair<Integer, k.b> E = E(i8, bVar);
            if (E != null) {
                t1.this.f10863i.b(new Runnable() { // from class: com.google.android.exoplayer2.p1
                    @Override // java.lang.Runnable
                    public final void run() {
                        t1.a.this.T(E, hVar, iVar);
                    }
                });
            }
        }

        @Override // com.google.android.exoplayer2.source.l
        public void R(int i8, k.b bVar, final h5.h hVar, final h5.i iVar) {
            final Pair<Integer, k.b> E = E(i8, bVar);
            if (E != null) {
                t1.this.f10863i.b(new Runnable() { // from class: com.google.android.exoplayer2.o1
                    @Override // java.lang.Runnable
                    public final void run() {
                        t1.a.this.V(E, hVar, iVar);
                    }
                });
            }
        }

        @Override // com.google.android.exoplayer2.drm.h
        public void b0(int i8, k.b bVar) {
            final Pair<Integer, k.b> E = E(i8, bVar);
            if (E != null) {
                t1.this.f10863i.b(new Runnable() { // from class: com.google.android.exoplayer2.l1
                    @Override // java.lang.Runnable
                    public final void run() {
                        t1.a.this.H(E);
                    }
                });
            }
        }

        @Override // com.google.android.exoplayer2.source.l
        public void c0(int i8, k.b bVar, final h5.h hVar, final h5.i iVar) {
            final Pair<Integer, k.b> E = E(i8, bVar);
            if (E != null) {
                t1.this.f10863i.b(new Runnable() { // from class: com.google.android.exoplayer2.q1
                    @Override // java.lang.Runnable
                    public final void run() {
                        t1.a.this.Q(E, hVar, iVar);
                    }
                });
            }
        }

        @Override // com.google.android.exoplayer2.drm.h
        public void g0(int i8, k.b bVar) {
            final Pair<Integer, k.b> E = E(i8, bVar);
            if (E != null) {
                t1.this.f10863i.b(new Runnable() { // from class: com.google.android.exoplayer2.k1
                    @Override // java.lang.Runnable
                    public final void run() {
                        t1.a.this.G(E);
                    }
                });
            }
        }

        @Override // com.google.android.exoplayer2.source.l
        public void i0(int i8, k.b bVar, final h5.h hVar, final h5.i iVar, final IOException iOException, final boolean z4) {
            final Pair<Integer, k.b> E = E(i8, bVar);
            if (E != null) {
                t1.this.f10863i.b(new Runnable() { // from class: com.google.android.exoplayer2.r1
                    @Override // java.lang.Runnable
                    public final void run() {
                        t1.a.this.U(E, hVar, iVar, iOException, z4);
                    }
                });
            }
        }

        @Override // com.google.android.exoplayer2.drm.h
        public void k0(int i8, k.b bVar, final int i9) {
            final Pair<Integer, k.b> E = E(i8, bVar);
            if (E != null) {
                t1.this.f10863i.b(new Runnable() { // from class: com.google.android.exoplayer2.n1
                    @Override // java.lang.Runnable
                    public final void run() {
                        t1.a.this.L(E, i9);
                    }
                });
            }
        }

        @Override // com.google.android.exoplayer2.drm.h
        public void l0(int i8, k.b bVar) {
            final Pair<Integer, k.b> E = E(i8, bVar);
            if (E != null) {
                t1.this.f10863i.b(new Runnable() { // from class: com.google.android.exoplayer2.h1
                    @Override // java.lang.Runnable
                    public final void run() {
                        t1.a.this.P(E);
                    }
                });
            }
        }

        @Override // com.google.android.exoplayer2.drm.h
        public void o0(int i8, k.b bVar) {
            final Pair<Integer, k.b> E = E(i8, bVar);
            if (E != null) {
                t1.this.f10863i.b(new Runnable() { // from class: com.google.android.exoplayer2.m1
                    @Override // java.lang.Runnable
                    public final void run() {
                        t1.a.this.K(E);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final com.google.android.exoplayer2.source.k f10869a;

        /* renamed from: b  reason: collision with root package name */
        public final k.c f10870b;

        /* renamed from: c  reason: collision with root package name */
        public final a f10871c;

        public b(com.google.android.exoplayer2.source.k kVar, k.c cVar, a aVar) {
            this.f10869a = kVar;
            this.f10870b = cVar;
            this.f10871c = aVar;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c implements f1 {

        /* renamed from: a  reason: collision with root package name */
        public final com.google.android.exoplayer2.source.i f10872a;

        /* renamed from: d  reason: collision with root package name */
        public int f10875d;

        /* renamed from: e  reason: collision with root package name */
        public boolean f10876e;

        /* renamed from: c  reason: collision with root package name */
        public final List<k.b> f10874c = new ArrayList();

        /* renamed from: b  reason: collision with root package name */
        public final Object f10873b = new Object();

        public c(com.google.android.exoplayer2.source.k kVar, boolean z4) {
            this.f10872a = new com.google.android.exoplayer2.source.i(kVar, z4);
        }

        @Override // com.google.android.exoplayer2.f1
        public Object a() {
            return this.f10873b;
        }

        @Override // com.google.android.exoplayer2.f1
        public h2 b() {
            return this.f10872a.c0();
        }

        public void c(int i8) {
            this.f10875d = i8;
            this.f10876e = false;
            this.f10874c.clear();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        void d();
    }

    public t1(d dVar, j4.a aVar, b6.l lVar, j4.t1 t1Var) {
        this.f10855a = t1Var;
        this.f10859e = dVar;
        this.f10862h = aVar;
        this.f10863i = lVar;
    }

    private void B(int i8, int i9) {
        for (int i10 = i9 - 1; i10 >= i8; i10--) {
            c remove = this.f10856b.remove(i10);
            this.f10858d.remove(remove.f10873b);
            g(i10, -remove.f10872a.c0().t());
            remove.f10876e = true;
            if (this.f10865k) {
                u(remove);
            }
        }
    }

    private void g(int i8, int i9) {
        while (i8 < this.f10856b.size()) {
            this.f10856b.get(i8).f10875d += i9;
            i8++;
        }
    }

    private void j(c cVar) {
        b bVar = this.f10860f.get(cVar);
        if (bVar != null) {
            bVar.f10869a.g(bVar.f10870b);
        }
    }

    private void k() {
        Iterator<c> it = this.f10861g.iterator();
        while (it.hasNext()) {
            c next = it.next();
            if (next.f10874c.isEmpty()) {
                j(next);
                it.remove();
            }
        }
    }

    private void l(c cVar) {
        this.f10861g.add(cVar);
        b bVar = this.f10860f.get(cVar);
        if (bVar != null) {
            bVar.f10869a.r(bVar.f10870b);
        }
    }

    private static Object m(Object obj) {
        return com.google.android.exoplayer2.a.z(obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static k.b n(c cVar, k.b bVar) {
        for (int i8 = 0; i8 < cVar.f10874c.size(); i8++) {
            if (cVar.f10874c.get(i8).f20289d == bVar.f20289d) {
                return bVar.c(p(cVar, bVar.f20286a));
            }
        }
        return null;
    }

    private static Object o(Object obj) {
        return com.google.android.exoplayer2.a.A(obj);
    }

    private static Object p(c cVar, Object obj) {
        return com.google.android.exoplayer2.a.C(cVar.f10873b, obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int r(c cVar, int i8) {
        return i8 + cVar.f10875d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void t(com.google.android.exoplayer2.source.k kVar, h2 h2Var) {
        this.f10859e.d();
    }

    private void u(c cVar) {
        if (cVar.f10876e && cVar.f10874c.isEmpty()) {
            b bVar = (b) b6.a.e(this.f10860f.remove(cVar));
            bVar.f10869a.c(bVar.f10870b);
            bVar.f10869a.f(bVar.f10871c);
            bVar.f10869a.m(bVar.f10871c);
            this.f10861g.remove(cVar);
        }
    }

    private void x(c cVar) {
        com.google.android.exoplayer2.source.i iVar = cVar.f10872a;
        k.c cVar2 = new k.c() { // from class: com.google.android.exoplayer2.g1
            @Override // com.google.android.exoplayer2.source.k.c
            public final void a(com.google.android.exoplayer2.source.k kVar, h2 h2Var) {
                t1.this.t(kVar, h2Var);
            }
        };
        a aVar = new a(cVar);
        this.f10860f.put(cVar, new b(iVar, cVar2, aVar));
        iVar.e(b6.l0.y(), aVar);
        iVar.l(b6.l0.y(), aVar);
        iVar.a(cVar2, this.f10866l, this.f10855a);
    }

    public h2 A(int i8, int i9, com.google.android.exoplayer2.source.x xVar) {
        b6.a.a(i8 >= 0 && i8 <= i9 && i9 <= q());
        this.f10864j = xVar;
        B(i8, i9);
        return i();
    }

    public h2 C(List<c> list, com.google.android.exoplayer2.source.x xVar) {
        B(0, this.f10856b.size());
        return f(this.f10856b.size(), list, xVar);
    }

    public h2 D(com.google.android.exoplayer2.source.x xVar) {
        int q = q();
        if (xVar.b() != q) {
            xVar = xVar.i().g(0, q);
        }
        this.f10864j = xVar;
        return i();
    }

    public h2 f(int i8, List<c> list, com.google.android.exoplayer2.source.x xVar) {
        int i9;
        if (!list.isEmpty()) {
            this.f10864j = xVar;
            for (int i10 = i8; i10 < list.size() + i8; i10++) {
                c cVar = list.get(i10 - i8);
                if (i10 > 0) {
                    c cVar2 = this.f10856b.get(i10 - 1);
                    i9 = cVar2.f10875d + cVar2.f10872a.c0().t();
                } else {
                    i9 = 0;
                }
                cVar.c(i9);
                g(i10, cVar.f10872a.c0().t());
                this.f10856b.add(i10, cVar);
                this.f10858d.put(cVar.f10873b, cVar);
                if (this.f10865k) {
                    x(cVar);
                    if (this.f10857c.isEmpty()) {
                        this.f10861g.add(cVar);
                    } else {
                        j(cVar);
                    }
                }
            }
        }
        return i();
    }

    public com.google.android.exoplayer2.source.j h(k.b bVar, a6.b bVar2, long j8) {
        Object o5 = o(bVar.f20286a);
        k.b c9 = bVar.c(m(bVar.f20286a));
        c cVar = (c) b6.a.e(this.f10858d.get(o5));
        l(cVar);
        cVar.f10874c.add(c9);
        com.google.android.exoplayer2.source.h b9 = cVar.f10872a.b(c9, bVar2, j8);
        this.f10857c.put(b9, cVar);
        k();
        return b9;
    }

    public h2 i() {
        if (this.f10856b.isEmpty()) {
            return h2.f9745a;
        }
        int i8 = 0;
        for (int i9 = 0; i9 < this.f10856b.size(); i9++) {
            c cVar = this.f10856b.get(i9);
            cVar.f10875d = i8;
            i8 += cVar.f10872a.c0().t();
        }
        return new a2(this.f10856b, this.f10864j);
    }

    public int q() {
        return this.f10856b.size();
    }

    public boolean s() {
        return this.f10865k;
    }

    public h2 v(int i8, int i9, int i10, com.google.android.exoplayer2.source.x xVar) {
        b6.a.a(i8 >= 0 && i8 <= i9 && i9 <= q() && i10 >= 0);
        this.f10864j = xVar;
        if (i8 == i9 || i8 == i10) {
            return i();
        }
        int min = Math.min(i8, i10);
        int max = Math.max(((i9 - i8) + i10) - 1, i9 - 1);
        int i11 = this.f10856b.get(min).f10875d;
        b6.l0.B0(this.f10856b, i8, i9, i10);
        while (min <= max) {
            c cVar = this.f10856b.get(min);
            cVar.f10875d = i11;
            i11 += cVar.f10872a.c0().t();
            min++;
        }
        return i();
    }

    public void w(a6.y yVar) {
        b6.a.f(!this.f10865k);
        this.f10866l = yVar;
        for (int i8 = 0; i8 < this.f10856b.size(); i8++) {
            c cVar = this.f10856b.get(i8);
            x(cVar);
            this.f10861g.add(cVar);
        }
        this.f10865k = true;
    }

    public void y() {
        for (b bVar : this.f10860f.values()) {
            try {
                bVar.f10869a.c(bVar.f10870b);
            } catch (RuntimeException e8) {
                b6.p.d("MediaSourceList", "Failed to release child source.", e8);
            }
            bVar.f10869a.f(bVar.f10871c);
            bVar.f10869a.m(bVar.f10871c);
        }
        this.f10860f.clear();
        this.f10861g.clear();
        this.f10865k = false;
    }

    public void z(com.google.android.exoplayer2.source.j jVar) {
        c cVar = (c) b6.a.e(this.f10857c.remove(jVar));
        cVar.f10872a.p(jVar);
        cVar.f10874c.remove(((com.google.android.exoplayer2.source.h) jVar).f10439a);
        if (!this.f10857c.isEmpty()) {
            k();
        }
        u(cVar);
    }
}
