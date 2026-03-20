package j4;

import android.os.Looper;
import android.util.SparseArray;
import b6.d;
import b6.l;
import b6.l0;
import b6.o;
import c6.x;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.a1;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.i2;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.w0;
import com.google.android.exoplayer2.x1;
import com.google.android.exoplayer2.y1;
import com.google.android.exoplayer2.z0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.f1;
import h5.h;
import h5.i;
import h5.j;
import j4.b;
import java.io.IOException;
import java.util.List;
import l4.e;
import l4.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o1 implements j4.a {

    /* renamed from: a  reason: collision with root package name */
    private final d f20649a;

    /* renamed from: b  reason: collision with root package name */
    private final h2.b f20650b;

    /* renamed from: c  reason: collision with root package name */
    private final h2.d f20651c;

    /* renamed from: d  reason: collision with root package name */
    private final a f20652d;

    /* renamed from: e  reason: collision with root package name */
    private final SparseArray<b.a> f20653e;

    /* renamed from: f  reason: collision with root package name */
    private o<b> f20654f;

    /* renamed from: g  reason: collision with root package name */
    private y1 f20655g;

    /* renamed from: h  reason: collision with root package name */
    private l f20656h;

    /* renamed from: j  reason: collision with root package name */
    private boolean f20657j;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final h2.b f20658a;

        /* renamed from: b  reason: collision with root package name */
        private ImmutableList<k.b> f20659b = ImmutableList.E();

        /* renamed from: c  reason: collision with root package name */
        private ImmutableMap<k.b, h2> f20660c = ImmutableMap.n();

        /* renamed from: d  reason: collision with root package name */
        private k.b f20661d;

        /* renamed from: e  reason: collision with root package name */
        private k.b f20662e;

        /* renamed from: f  reason: collision with root package name */
        private k.b f20663f;

        public a(h2.b bVar) {
            this.f20658a = bVar;
        }

        private void b(ImmutableMap.b<k.b, h2> bVar, k.b bVar2, h2 h2Var) {
            if (bVar2 == null) {
                return;
            }
            if (h2Var.f(bVar2.f20286a) == -1 && (h2Var = this.f20660c.get(bVar2)) == null) {
                return;
            }
            bVar.g(bVar2, h2Var);
        }

        private static k.b c(y1 y1Var, ImmutableList<k.b> immutableList, k.b bVar, h2.b bVar2) {
            h2 K = y1Var.K();
            int m8 = y1Var.m();
            Object q = K.u() ? null : K.q(m8);
            int g8 = (y1Var.h() || K.u()) ? -1 : K.j(m8, bVar2).g(l0.C0(y1Var.getCurrentPosition()) - bVar2.q());
            for (int i8 = 0; i8 < immutableList.size(); i8++) {
                k.b bVar3 = immutableList.get(i8);
                if (i(bVar3, q, y1Var.h(), y1Var.E(), y1Var.q(), g8)) {
                    return bVar3;
                }
            }
            if (immutableList.isEmpty() && bVar != null) {
                if (i(bVar, q, y1Var.h(), y1Var.E(), y1Var.q(), g8)) {
                    return bVar;
                }
            }
            return null;
        }

        private static boolean i(k.b bVar, Object obj, boolean z4, int i8, int i9, int i10) {
            if (bVar.f20286a.equals(obj)) {
                return (z4 && bVar.f20287b == i8 && bVar.f20288c == i9) || (!z4 && bVar.f20287b == -1 && bVar.f20290e == i10);
            }
            return false;
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0032, code lost:
            if (com.google.common.base.k.a(r3.f20661d, r3.f20663f) == false) goto L10;
         */
        /* JADX WARN: Code restructure failed: missing block: B:17:0x0054, code lost:
            if (r3.f20659b.contains(r3.f20661d) == false) goto L10;
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x0056, code lost:
            b(r0, r3.f20661d, r4);
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void m(com.google.android.exoplayer2.h2 r4) {
            /*
                r3 = this;
                com.google.common.collect.ImmutableMap$b r0 = com.google.common.collect.ImmutableMap.a()
                com.google.common.collect.ImmutableList<com.google.android.exoplayer2.source.k$b> r1 = r3.f20659b
                boolean r1 = r1.isEmpty()
                if (r1 == 0) goto L35
                com.google.android.exoplayer2.source.k$b r1 = r3.f20662e
                r3.b(r0, r1, r4)
                com.google.android.exoplayer2.source.k$b r1 = r3.f20663f
                com.google.android.exoplayer2.source.k$b r2 = r3.f20662e
                boolean r1 = com.google.common.base.k.a(r1, r2)
                if (r1 != 0) goto L20
                com.google.android.exoplayer2.source.k$b r1 = r3.f20663f
                r3.b(r0, r1, r4)
            L20:
                com.google.android.exoplayer2.source.k$b r1 = r3.f20661d
                com.google.android.exoplayer2.source.k$b r2 = r3.f20662e
                boolean r1 = com.google.common.base.k.a(r1, r2)
                if (r1 != 0) goto L5b
                com.google.android.exoplayer2.source.k$b r1 = r3.f20661d
                com.google.android.exoplayer2.source.k$b r2 = r3.f20663f
                boolean r1 = com.google.common.base.k.a(r1, r2)
                if (r1 != 0) goto L5b
                goto L56
            L35:
                r1 = 0
            L36:
                com.google.common.collect.ImmutableList<com.google.android.exoplayer2.source.k$b> r2 = r3.f20659b
                int r2 = r2.size()
                if (r1 >= r2) goto L4c
                com.google.common.collect.ImmutableList<com.google.android.exoplayer2.source.k$b> r2 = r3.f20659b
                java.lang.Object r2 = r2.get(r1)
                com.google.android.exoplayer2.source.k$b r2 = (com.google.android.exoplayer2.source.k.b) r2
                r3.b(r0, r2, r4)
                int r1 = r1 + 1
                goto L36
            L4c:
                com.google.common.collect.ImmutableList<com.google.android.exoplayer2.source.k$b> r1 = r3.f20659b
                com.google.android.exoplayer2.source.k$b r2 = r3.f20661d
                boolean r1 = r1.contains(r2)
                if (r1 != 0) goto L5b
            L56:
                com.google.android.exoplayer2.source.k$b r1 = r3.f20661d
                r3.b(r0, r1, r4)
            L5b:
                com.google.common.collect.ImmutableMap r4 = r0.d()
                r3.f20660c = r4
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: j4.o1.a.m(com.google.android.exoplayer2.h2):void");
        }

        public k.b d() {
            return this.f20661d;
        }

        public k.b e() {
            if (this.f20659b.isEmpty()) {
                return null;
            }
            return (k.b) f1.f(this.f20659b);
        }

        public h2 f(k.b bVar) {
            return this.f20660c.get(bVar);
        }

        public k.b g() {
            return this.f20662e;
        }

        public k.b h() {
            return this.f20663f;
        }

        public void j(y1 y1Var) {
            this.f20661d = c(y1Var, this.f20659b, this.f20662e, this.f20658a);
        }

        public void k(List<k.b> list, k.b bVar, y1 y1Var) {
            this.f20659b = ImmutableList.x(list);
            if (!list.isEmpty()) {
                this.f20662e = list.get(0);
                this.f20663f = (k.b) b6.a.e(bVar);
            }
            if (this.f20661d == null) {
                this.f20661d = c(y1Var, this.f20659b, this.f20662e, this.f20658a);
            }
            m(y1Var.K());
        }

        public void l(y1 y1Var) {
            this.f20661d = c(y1Var, this.f20659b, this.f20662e, this.f20658a);
            m(y1Var.K());
        }
    }

    public o1(d dVar) {
        this.f20649a = (d) b6.a.e(dVar);
        this.f20654f = new o<>(l0.Q(), dVar, i1.a);
        h2.b bVar = new h2.b();
        this.f20650b = bVar;
        this.f20651c = new h2.d();
        this.f20652d = new a(bVar);
        this.f20653e = new SparseArray<>();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void A2(b.a aVar, int i8, y1.e eVar, y1.e eVar2, b bVar) {
        bVar.n(aVar, i8);
        bVar.P(aVar, eVar, eVar2, i8);
    }

    private b.a E1(k.b bVar) {
        b6.a.e(this.f20655g);
        h2 f5 = bVar == null ? null : this.f20652d.f(bVar);
        if (bVar == null || f5 == null) {
            int F = this.f20655g.F();
            h2 K = this.f20655g.K();
            if (!(F < K.t())) {
                K = h2.f9745a;
            }
            return D1(K, F, null);
        }
        return D1(f5, f5.l(bVar.f20286a, this.f20650b).f9758c, bVar);
    }

    private b.a F1() {
        return E1(this.f20652d.e());
    }

    private b.a G1(int i8, k.b bVar) {
        b6.a.e(this.f20655g);
        if (bVar != null) {
            return this.f20652d.f(bVar) != null ? E1(bVar) : D1(h2.f9745a, i8, bVar);
        }
        h2 K = this.f20655g.K();
        if (!(i8 < K.t())) {
            K = h2.f9745a;
        }
        return D1(K, i8, null);
    }

    private b.a H1() {
        return E1(this.f20652d.g());
    }

    private b.a I1() {
        return E1(this.f20652d.h());
    }

    private b.a J1(PlaybackException playbackException) {
        j jVar;
        return (!(playbackException instanceof ExoPlaybackException) || (jVar = ((ExoPlaybackException) playbackException).f9136p) == null) ? C1() : E1(new k.b(jVar));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void K1(b bVar, b6.k kVar) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void L2(b.a aVar, String str, long j8, long j9, b bVar) {
        bVar.N(aVar, str, j8);
        bVar.J(aVar, str, j9, j8);
        bVar.h(aVar, 2, str, j8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void N2(b.a aVar, e eVar, b bVar) {
        bVar.V(aVar, eVar);
        bVar.u0(aVar, 2, eVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void O1(b.a aVar, String str, long j8, long j9, b bVar) {
        bVar.f(aVar, str, j8);
        bVar.E(aVar, str, j9, j8);
        bVar.h(aVar, 1, str, j8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void O2(b.a aVar, e eVar, b bVar) {
        bVar.s(aVar, eVar);
        bVar.b(aVar, 2, eVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void Q1(b.a aVar, e eVar, b bVar) {
        bVar.U(aVar, eVar);
        bVar.u0(aVar, 1, eVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void Q2(b.a aVar, w0 w0Var, g gVar, b bVar) {
        bVar.v0(aVar, w0Var);
        bVar.k0(aVar, w0Var, gVar);
        bVar.C(aVar, 2, w0Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void R1(b.a aVar, e eVar, b bVar) {
        bVar.K(aVar, eVar);
        bVar.b(aVar, 1, eVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void R2(b.a aVar, x xVar, b bVar) {
        bVar.t0(aVar, xVar);
        bVar.k(aVar, xVar.f8461a, xVar.f8462b, xVar.f8463c, xVar.f8464d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void S1(b.a aVar, w0 w0Var, g gVar, b bVar) {
        bVar.y0(aVar, w0Var);
        bVar.j(aVar, w0Var, gVar);
        bVar.C(aVar, 1, w0Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void U2(y1 y1Var, b bVar, b6.k kVar) {
        bVar.d0(y1Var, new b.C0177b(kVar, this.f20653e));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void V2() {
        b.a C1 = C1();
        W2(C1, 1028, new n(C1));
        this.f20654f.j();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void g2(b.a aVar, int i8, b bVar) {
        bVar.e0(aVar);
        bVar.n0(aVar, i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void k2(b.a aVar, boolean z4, b bVar) {
        bVar.a(aVar, z4);
        bVar.G(aVar, z4);
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void A(int i8) {
        b.a C1 = C1();
        W2(C1, 6, new e(C1, i8));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public void B(boolean z4) {
    }

    @Override // com.google.android.exoplayer2.y1.d
    public void C(int i8) {
    }

    protected final b.a C1() {
        return E1(this.f20652d.d());
    }

    @Override // com.google.android.exoplayer2.y1.d
    public void D(i2 i2Var) {
        b.a C1 = C1();
        W2(C1, 2, new a0(C1, i2Var));
    }

    protected final b.a D1(h2 h2Var, int i8, k.b bVar) {
        long u8;
        k.b bVar2 = h2Var.u() ? null : bVar;
        long b9 = this.f20649a.b();
        boolean z4 = true;
        boolean z8 = h2Var.equals(this.f20655g.K()) && i8 == this.f20655g.F();
        long j8 = 0;
        if (bVar2 != null && bVar2.b()) {
            if (!z8 || this.f20655g.E() != bVar2.f20287b || this.f20655g.q() != bVar2.f20288c) {
                z4 = false;
            }
            if (z4) {
                j8 = this.f20655g.getCurrentPosition();
            }
        } else if (z8) {
            u8 = this.f20655g.u();
            return new b.a(b9, h2Var, i8, bVar2, u8, this.f20655g.K(), this.f20655g.F(), this.f20652d.d(), this.f20655g.getCurrentPosition(), this.f20655g.i());
        } else if (!h2Var.u()) {
            j8 = h2Var.r(i8, this.f20651c).d();
        }
        u8 = j8;
        return new b.a(b9, h2Var, i8, bVar2, u8, this.f20655g.K(), this.f20655g.F(), this.f20652d.d(), this.f20655g.getCurrentPosition(), this.f20655g.i());
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void E(boolean z4) {
        b.a C1 = C1();
        W2(C1, 3, new d1(C1, z4));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void F() {
        b.a C1 = C1();
        W2(C1, -1, new j0(C1));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void G(PlaybackException playbackException) {
        b.a J1 = J1(playbackException);
        W2(J1, 10, new v(J1, playbackException));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public void H(y1.b bVar) {
        b.a C1 = C1();
        W2(C1, 13, new z(C1, bVar));
    }

    @Override // com.google.android.exoplayer2.source.l
    public final void I(int i8, k.b bVar, i iVar) {
        b.a G1 = G1(i8, bVar);
        W2(G1, 1005, new i0(G1, iVar));
    }

    @Override // com.google.android.exoplayer2.drm.h
    public final void J(int i8, k.b bVar, Exception exc) {
        b.a G1 = G1(i8, bVar);
        W2(G1, RecognitionOptions.UPC_E, new n0(G1, exc));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void K(h2 h2Var, int i8) {
        this.f20652d.l((y1) b6.a.e(this.f20655g));
        b.a C1 = C1();
        W2(C1, 0, new f(C1, i8));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void L(float f5) {
        b.a I1 = I1();
        W2(I1, 22, new l1(I1, f5));
    }

    @Override // com.google.android.exoplayer2.source.l
    public final void M(int i8, k.b bVar, i iVar) {
        b.a G1 = G1(i8, bVar);
        W2(G1, 1004, new h0(G1, iVar));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void N(int i8) {
        b.a C1 = C1();
        W2(C1, 4, new d(C1, i8));
    }

    @Override // com.google.android.exoplayer2.source.l
    public final void O(int i8, k.b bVar, h hVar, i iVar) {
        b.a G1 = G1(i8, bVar);
        W2(G1, 1001, new d0(G1, hVar, iVar));
    }

    @Override // a6.d.a
    public final void P(int i8, long j8, long j9) {
        b.a F1 = F1();
        W2(F1, 1006, new i(F1, i8, j8, j9));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public void Q(com.google.android.exoplayer2.j jVar) {
        b.a C1 = C1();
        W2(C1, 29, new q(C1, jVar));
    }

    @Override // com.google.android.exoplayer2.source.l
    public final void R(int i8, k.b bVar, h hVar, i iVar) {
        b.a G1 = G1(i8, bVar);
        W2(G1, 1000, new f0(G1, hVar, iVar));
    }

    @Override // j4.a
    public final void S() {
        if (this.f20657j) {
            return;
        }
        b.a C1 = C1();
        this.f20657j = true;
        W2(C1, -1, new k1(C1));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public void T(a1 a1Var) {
        b.a C1 = C1();
        W2(C1, 14, new u(C1, a1Var));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void U(boolean z4) {
        b.a C1 = C1();
        W2(C1, 9, new b1(C1, z4));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public void V(y1 y1Var, y1.c cVar) {
    }

    @Override // j4.a
    public void W(y1 y1Var, Looper looper) {
        b6.a.f(this.f20655g == null || this.f20652d.f20659b.isEmpty());
        this.f20655g = (y1) b6.a.e(y1Var);
        this.f20656h = this.f20649a.d(looper, null);
        this.f20654f = this.f20654f.e(looper, new h1(this, y1Var));
    }

    protected final void W2(b.a aVar, int i8, o.a<b> aVar2) {
        this.f20653e.put(i8, aVar);
        this.f20654f.k(i8, aVar2);
    }

    @Override // j4.a
    public final void X(List<k.b> list, k.b bVar) {
        this.f20652d.k(list, bVar, (y1) b6.a.e(this.f20655g));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public void Y(int i8, boolean z4) {
        b.a C1 = C1();
        W2(C1, 30, new l(C1, i8, z4));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void Z(boolean z4, int i8) {
        b.a C1 = C1();
        W2(C1, -1, new g1(C1, z4, i8));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void a(boolean z4) {
        b.a I1 = I1();
        W2(I1, 23, new c1(I1, z4));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void a0(com.google.android.exoplayer2.audio.a aVar) {
        b.a I1 = I1();
        W2(I1, 20, new b0(I1, aVar));
    }

    @Override // j4.a
    public final void b(Exception exc) {
        b.a I1 = I1();
        W2(I1, 1014, new m0(I1, exc));
    }

    @Override // com.google.android.exoplayer2.drm.h
    public final void b0(int i8, k.b bVar) {
        b.a G1 = G1(i8, bVar);
        W2(G1, 1026, new u0(G1));
    }

    @Override // j4.a
    public final void c(String str) {
        b.a I1 = I1();
        W2(I1, 1019, new p0(I1, str));
    }

    @Override // com.google.android.exoplayer2.source.l
    public final void c0(int i8, k.b bVar, h hVar, i iVar) {
        b.a G1 = G1(i8, bVar);
        W2(G1, 1002, new e0(G1, hVar, iVar));
    }

    @Override // j4.a
    public final void d(String str, long j8, long j9) {
        b.a I1 = I1();
        W2(I1, 1016, new s0(I1, str, j9, j8));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public void d0() {
    }

    @Override // j4.a
    public final void e(e eVar) {
        b.a I1 = I1();
        W2(I1, 1015, new y0(I1, eVar));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void e0(z0 z0Var, int i8) {
        b.a C1 = C1();
        W2(C1, 1, new t(C1, z0Var, i8));
    }

    @Override // j4.a
    public final void f(String str) {
        b.a I1 = I1();
        W2(I1, 1012, new q0(I1, str));
    }

    @Override // j4.a
    public final void g(String str, long j8, long j9) {
        b.a I1 = I1();
        W2(I1, 1008, new r0(I1, str, j9, j8));
    }

    @Override // com.google.android.exoplayer2.drm.h
    public final void g0(int i8, k.b bVar) {
        b.a G1 = G1(i8, bVar);
        W2(G1, 1023, new y(G1));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void h(Metadata metadata) {
        b.a C1 = C1();
        W2(C1, 28, new c0(C1, metadata));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void h0(boolean z4, int i8) {
        b.a C1 = C1();
        W2(C1, 5, new e1(C1, z4, i8));
    }

    @Override // j4.a
    public final void i(int i8, long j8) {
        b.a H1 = H1();
        W2(H1, 1018, new h(H1, i8, j8));
    }

    @Override // com.google.android.exoplayer2.source.l
    public final void i0(int i8, k.b bVar, h hVar, i iVar, IOException iOException, boolean z4) {
        b.a G1 = G1(i8, bVar);
        W2(G1, 1003, new g0(G1, hVar, iVar, iOException, z4));
    }

    @Override // j4.a
    public final void j(e eVar) {
        b.a H1 = H1();
        W2(H1, 1013, new w0(H1, eVar));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void j0(int i8, int i9) {
        b.a I1 = I1();
        W2(I1, 24, new g(I1, i8, i9));
    }

    @Override // j4.a
    public final void k(Object obj, long j8) {
        b.a I1 = I1();
        W2(I1, 26, new o0(I1, obj, j8));
    }

    @Override // com.google.android.exoplayer2.drm.h
    public final void k0(int i8, k.b bVar, int i9) {
        b.a G1 = G1(i8, bVar);
        W2(G1, 1022, new n1(G1, i9));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void l(int i8) {
        b.a C1 = C1();
        W2(C1, 8, new m1(C1, i8));
    }

    @Override // com.google.android.exoplayer2.drm.h
    public final void l0(int i8, k.b bVar) {
        b.a G1 = G1(i8, bVar);
        W2(G1, 1027, new c(G1));
    }

    @Override // j4.a
    public final void m(e eVar) {
        b.a I1 = I1();
        W2(I1, 1007, new x0(I1, eVar));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public void m0(PlaybackException playbackException) {
        b.a J1 = J1(playbackException);
        W2(J1, 10, new w(J1, playbackException));
    }

    @Override // j4.a
    public final void n(w0 w0Var, g gVar) {
        b.a I1 = I1();
        W2(I1, 1009, new s(I1, w0Var, gVar));
    }

    @Override // j4.a
    public void n0(b bVar) {
        b6.a.e(bVar);
        this.f20654f.c(bVar);
    }

    @Override // j4.a
    public final void o(e eVar) {
        b.a H1 = H1();
        W2(H1, 1020, new v0(H1, eVar));
    }

    @Override // com.google.android.exoplayer2.drm.h
    public final void o0(int i8, k.b bVar) {
        b.a G1 = G1(i8, bVar);
        W2(G1, 1025, new f1(G1));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void p(x xVar) {
        b.a I1 = I1();
        W2(I1, 25, new p(I1, xVar));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public void p0(boolean z4) {
        b.a C1 = C1();
        W2(C1, 7, new a1(C1, z4));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public void q(p5.e eVar) {
        b.a C1 = C1();
        W2(C1, 27, new z0(C1, eVar));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public void r(List<p5.b> list) {
        b.a C1 = C1();
        W2(C1, 27, new t0(C1, list));
    }

    @Override // j4.a
    public void release() {
        ((l) b6.a.h(this.f20656h)).b(new j1(this));
    }

    @Override // j4.a
    public final void s(long j8) {
        b.a I1 = I1();
        W2(I1, 1010, new m(I1, j8));
    }

    @Override // j4.a
    public final void t(Exception exc) {
        b.a I1 = I1();
        W2(I1, 1029, new k0(I1, exc));
    }

    @Override // j4.a
    public final void u(w0 w0Var, g gVar) {
        b.a I1 = I1();
        W2(I1, 1017, new r(I1, w0Var, gVar));
    }

    @Override // j4.a
    public final void v(Exception exc) {
        b.a I1 = I1();
        W2(I1, 1030, new l0(I1, exc));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void w(x1 x1Var) {
        b.a C1 = C1();
        W2(C1, 12, new x(C1, x1Var));
    }

    @Override // j4.a
    public final void x(int i8, long j8, long j9) {
        b.a I1 = I1();
        W2(I1, 1011, new j(I1, i8, j8, j9));
    }

    @Override // j4.a
    public final void y(long j8, int i8) {
        b.a H1 = H1();
        W2(H1, 1021, new o(H1, j8, i8));
    }

    @Override // com.google.android.exoplayer2.y1.d
    public final void z(y1.e eVar, y1.e eVar2, int i8) {
        if (i8 == 1) {
            this.f20657j = false;
        }
        this.f20652d.j((y1) b6.a.e(this.f20655g));
        b.a C1 = C1();
        W2(C1, 11, new k(C1, i8, eVar, eVar2));
    }
}
