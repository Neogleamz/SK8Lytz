package com.google.android.exoplayer2;

import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import com.google.common.collect.ImmutableList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e1 {

    /* renamed from: a  reason: collision with root package name */
    private final h2.b f9642a = new h2.b();

    /* renamed from: b  reason: collision with root package name */
    private final h2.d f9643b = new h2.d();

    /* renamed from: c  reason: collision with root package name */
    private final j4.a f9644c;

    /* renamed from: d  reason: collision with root package name */
    private final b6.l f9645d;

    /* renamed from: e  reason: collision with root package name */
    private long f9646e;

    /* renamed from: f  reason: collision with root package name */
    private int f9647f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f9648g;

    /* renamed from: h  reason: collision with root package name */
    private b1 f9649h;

    /* renamed from: i  reason: collision with root package name */
    private b1 f9650i;

    /* renamed from: j  reason: collision with root package name */
    private b1 f9651j;

    /* renamed from: k  reason: collision with root package name */
    private int f9652k;

    /* renamed from: l  reason: collision with root package name */
    private Object f9653l;

    /* renamed from: m  reason: collision with root package name */
    private long f9654m;

    public e1(j4.a aVar, b6.l lVar) {
        this.f9644c = aVar;
        this.f9645d = lVar;
    }

    private static k.b A(h2 h2Var, Object obj, long j8, long j9, h2.d dVar, h2.b bVar) {
        h2Var.l(obj, bVar);
        h2Var.r(bVar.f9758c, dVar);
        int f5 = h2Var.f(obj);
        Object obj2 = obj;
        while (bVar.f9759d == 0 && bVar.f() > 0 && bVar.t(bVar.r()) && bVar.h(0L) == -1) {
            int i8 = f5 + 1;
            if (f5 >= dVar.f9784t) {
                break;
            }
            h2Var.k(i8, bVar, true);
            obj2 = b6.a.e(bVar.f9757b);
            f5 = i8;
        }
        h2Var.l(obj2, bVar);
        int h8 = bVar.h(j8);
        return h8 == -1 ? new k.b(obj2, j9, bVar.g(j8)) : new k.b(obj2, h8, bVar.n(h8), j9);
    }

    private long C(h2 h2Var, Object obj) {
        int f5;
        int i8 = h2Var.l(obj, this.f9642a).f9758c;
        Object obj2 = this.f9653l;
        if (obj2 == null || (f5 = h2Var.f(obj2)) == -1 || h2Var.j(f5, this.f9642a).f9758c != i8) {
            b1 b1Var = this.f9649h;
            while (true) {
                if (b1Var == null) {
                    b1Var = this.f9649h;
                    while (b1Var != null) {
                        int f8 = h2Var.f(b1Var.f9460b);
                        if (f8 == -1 || h2Var.j(f8, this.f9642a).f9758c != i8) {
                            b1Var = b1Var.j();
                        }
                    }
                    long j8 = this.f9646e;
                    this.f9646e = 1 + j8;
                    if (this.f9649h == null) {
                        this.f9653l = obj;
                        this.f9654m = j8;
                    }
                    return j8;
                } else if (b1Var.f9460b.equals(obj)) {
                    break;
                } else {
                    b1Var = b1Var.j();
                }
            }
            return b1Var.f9464f.f9480a.f20289d;
        }
        return this.f9654m;
    }

    private boolean E(h2 h2Var) {
        b1 b1Var = this.f9649h;
        if (b1Var == null) {
            return true;
        }
        int f5 = h2Var.f(b1Var.f9460b);
        while (true) {
            f5 = h2Var.h(f5, this.f9642a, this.f9643b, this.f9647f, this.f9648g);
            while (b1Var.j() != null && !b1Var.f9464f.f9486g) {
                b1Var = b1Var.j();
            }
            b1 j8 = b1Var.j();
            if (f5 == -1 || j8 == null || h2Var.f(j8.f9460b) != f5) {
                break;
            }
            b1Var = j8;
        }
        boolean z4 = z(b1Var);
        b1Var.f9464f = r(h2Var, b1Var.f9464f);
        return !z4;
    }

    private boolean d(long j8, long j9) {
        return j8 == -9223372036854775807L || j8 == j9;
    }

    private boolean e(c1 c1Var, c1 c1Var2) {
        return c1Var.f9481b == c1Var2.f9481b && c1Var.f9480a.equals(c1Var2.f9480a);
    }

    private c1 h(w1 w1Var) {
        return k(w1Var.f11241a, w1Var.f11242b, w1Var.f11243c, w1Var.f11257r);
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x00cc, code lost:
        if (r0.t(r0.r()) != false) goto L26;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.google.android.exoplayer2.c1 i(com.google.android.exoplayer2.h2 r20, com.google.android.exoplayer2.b1 r21, long r22) {
        /*
            Method dump skipped, instructions count: 450
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.e1.i(com.google.android.exoplayer2.h2, com.google.android.exoplayer2.b1, long):com.google.android.exoplayer2.c1");
    }

    private c1 k(h2 h2Var, k.b bVar, long j8, long j9) {
        h2Var.l(bVar.f20286a, this.f9642a);
        boolean b9 = bVar.b();
        Object obj = bVar.f20286a;
        return b9 ? l(h2Var, obj, bVar.f20287b, bVar.f20288c, j8, bVar.f20289d) : m(h2Var, obj, j9, j8, bVar.f20289d);
    }

    private c1 l(h2 h2Var, Object obj, int i8, int i9, long j8, long j9) {
        k.b bVar = new k.b(obj, i8, i9, j9);
        long e8 = h2Var.l(bVar.f20286a, this.f9642a).e(bVar.f20287b, bVar.f20288c);
        long j10 = i9 == this.f9642a.n(i8) ? this.f9642a.j() : 0L;
        return new c1(bVar, (e8 == -9223372036854775807L || j10 < e8) ? j10 : Math.max(0L, e8 - 1), j8, -9223372036854775807L, e8, this.f9642a.t(bVar.f20287b), false, false, false);
    }

    private c1 m(h2 h2Var, Object obj, long j8, long j9, long j10) {
        boolean z4;
        long j11;
        long j12;
        long j13;
        long j14 = j8;
        h2Var.l(obj, this.f9642a);
        int g8 = this.f9642a.g(j14);
        int i8 = 1;
        h2.b bVar = this.f9642a;
        if (g8 == -1) {
            if (bVar.f() > 0) {
                h2.b bVar2 = this.f9642a;
                if (bVar2.t(bVar2.r())) {
                    z4 = true;
                }
            }
            z4 = false;
        } else {
            if (bVar.t(g8)) {
                long i9 = this.f9642a.i(g8);
                h2.b bVar3 = this.f9642a;
                if (i9 == bVar3.f9759d && bVar3.s(g8)) {
                    z4 = true;
                    g8 = -1;
                }
            }
            z4 = false;
        }
        k.b bVar4 = new k.b(obj, j10, g8);
        boolean s8 = s(bVar4);
        boolean u8 = u(h2Var, bVar4);
        boolean t8 = t(h2Var, bVar4, s8);
        boolean z8 = g8 != -1 && this.f9642a.t(g8);
        if (g8 != -1) {
            j12 = this.f9642a.i(g8);
        } else if (!z4) {
            j11 = -9223372036854775807L;
            j13 = (j11 != -9223372036854775807L || j11 == Long.MIN_VALUE) ? this.f9642a.f9759d : j11;
            if (j13 != -9223372036854775807L && j14 >= j13) {
                if (!t8 && z4) {
                    i8 = 0;
                }
                j14 = Math.max(0L, j13 - i8);
            }
            return new c1(bVar4, j14, j9, j11, j13, z8, s8, u8, t8);
        } else {
            j12 = this.f9642a.f9759d;
        }
        j11 = j12;
        if (j11 != -9223372036854775807L) {
        }
        if (j13 != -9223372036854775807L) {
            if (!t8) {
                i8 = 0;
            }
            j14 = Math.max(0L, j13 - i8);
        }
        return new c1(bVar4, j14, j9, j11, j13, z8, s8, u8, t8);
    }

    private long n(h2 h2Var, Object obj, int i8) {
        h2Var.l(obj, this.f9642a);
        long i9 = this.f9642a.i(i8);
        return i9 == Long.MIN_VALUE ? this.f9642a.f9759d : i9 + this.f9642a.l(i8);
    }

    private boolean s(k.b bVar) {
        return !bVar.b() && bVar.f20290e == -1;
    }

    private boolean t(h2 h2Var, k.b bVar, boolean z4) {
        int f5 = h2Var.f(bVar.f20286a);
        return !h2Var.r(h2Var.j(f5, this.f9642a).f9758c, this.f9643b).f9778j && h2Var.v(f5, this.f9642a, this.f9643b, this.f9647f, this.f9648g) && z4;
    }

    private boolean u(h2 h2Var, k.b bVar) {
        if (s(bVar)) {
            return h2Var.r(h2Var.l(bVar.f20286a, this.f9642a).f9758c, this.f9643b).f9784t == h2Var.f(bVar.f20286a);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void w(ImmutableList.a aVar, k.b bVar) {
        this.f9644c.X(aVar.k(), bVar);
    }

    private void x() {
        final ImmutableList.a u8 = ImmutableList.u();
        for (b1 b1Var = this.f9649h; b1Var != null; b1Var = b1Var.j()) {
            u8.a(b1Var.f9464f.f9480a);
        }
        b1 b1Var2 = this.f9650i;
        final k.b bVar = b1Var2 == null ? null : b1Var2.f9464f.f9480a;
        this.f9645d.b(new Runnable() { // from class: com.google.android.exoplayer2.d1
            @Override // java.lang.Runnable
            public final void run() {
                e1.this.w(u8, bVar);
            }
        });
    }

    public k.b B(h2 h2Var, Object obj, long j8) {
        long C = C(h2Var, obj);
        h2Var.l(obj, this.f9642a);
        h2Var.r(this.f9642a.f9758c, this.f9643b);
        boolean z4 = false;
        for (int f5 = h2Var.f(obj); f5 >= this.f9643b.q; f5--) {
            h2Var.k(f5, this.f9642a, true);
            boolean z8 = this.f9642a.f() > 0;
            z4 |= z8;
            h2.b bVar = this.f9642a;
            if (bVar.h(bVar.f9759d) != -1) {
                obj = b6.a.e(this.f9642a.f9757b);
            }
            if (z4 && (!z8 || this.f9642a.f9759d != 0)) {
                break;
            }
        }
        return A(h2Var, obj, j8, C, this.f9643b, this.f9642a);
    }

    public boolean D() {
        b1 b1Var = this.f9651j;
        return b1Var == null || (!b1Var.f9464f.f9488i && b1Var.q() && this.f9651j.f9464f.f9484e != -9223372036854775807L && this.f9652k < 100);
    }

    public boolean F(h2 h2Var, long j8, long j9) {
        c1 c1Var;
        b1 b1Var = this.f9649h;
        b1 b1Var2 = null;
        while (b1Var != null) {
            c1 c1Var2 = b1Var.f9464f;
            if (b1Var2 != null) {
                c1 i8 = i(h2Var, b1Var2, j8);
                if (i8 != null && e(c1Var2, i8)) {
                    c1Var = i8;
                }
                return !z(b1Var2);
            }
            c1Var = r(h2Var, c1Var2);
            b1Var.f9464f = c1Var.a(c1Var2.f9482c);
            if (!d(c1Var2.f9484e, c1Var.f9484e)) {
                b1Var.A();
                long j10 = c1Var.f9484e;
                return (z(b1Var) || (b1Var == this.f9650i && !b1Var.f9464f.f9485f && ((j9 > Long.MIN_VALUE ? 1 : (j9 == Long.MIN_VALUE ? 0 : -1)) == 0 || (j9 > ((j10 > (-9223372036854775807L) ? 1 : (j10 == (-9223372036854775807L) ? 0 : -1)) == 0 ? Long.MAX_VALUE : b1Var.z(j10)) ? 1 : (j9 == ((j10 > (-9223372036854775807L) ? 1 : (j10 == (-9223372036854775807L) ? 0 : -1)) == 0 ? Long.MAX_VALUE : b1Var.z(j10)) ? 0 : -1)) >= 0))) ? false : true;
            }
            b1Var2 = b1Var;
            b1Var = b1Var.j();
        }
        return true;
    }

    public boolean G(h2 h2Var, int i8) {
        this.f9647f = i8;
        return E(h2Var);
    }

    public boolean H(h2 h2Var, boolean z4) {
        this.f9648g = z4;
        return E(h2Var);
    }

    public b1 b() {
        b1 b1Var = this.f9649h;
        if (b1Var == null) {
            return null;
        }
        if (b1Var == this.f9650i) {
            this.f9650i = b1Var.j();
        }
        this.f9649h.t();
        int i8 = this.f9652k - 1;
        this.f9652k = i8;
        if (i8 == 0) {
            this.f9651j = null;
            b1 b1Var2 = this.f9649h;
            this.f9653l = b1Var2.f9460b;
            this.f9654m = b1Var2.f9464f.f9480a.f20289d;
        }
        this.f9649h = this.f9649h.j();
        x();
        return this.f9649h;
    }

    public b1 c() {
        b1 b1Var = this.f9650i;
        b6.a.f((b1Var == null || b1Var.j() == null) ? false : true);
        this.f9650i = this.f9650i.j();
        x();
        return this.f9650i;
    }

    public void f() {
        if (this.f9652k == 0) {
            return;
        }
        b1 b1Var = (b1) b6.a.h(this.f9649h);
        this.f9653l = b1Var.f9460b;
        this.f9654m = b1Var.f9464f.f9480a.f20289d;
        while (b1Var != null) {
            b1Var.t();
            b1Var = b1Var.j();
        }
        this.f9649h = null;
        this.f9651j = null;
        this.f9650i = null;
        this.f9652k = 0;
        x();
    }

    public b1 g(i4.f0[] f0VarArr, z5.a0 a0Var, a6.b bVar, t1 t1Var, c1 c1Var, z5.b0 b0Var) {
        b1 b1Var = this.f9651j;
        b1 b1Var2 = new b1(f0VarArr, b1Var == null ? 1000000000000L : (b1Var.l() + this.f9651j.f9464f.f9484e) - c1Var.f9481b, a0Var, bVar, t1Var, c1Var, b0Var);
        b1 b1Var3 = this.f9651j;
        if (b1Var3 != null) {
            b1Var3.w(b1Var2);
        } else {
            this.f9649h = b1Var2;
            this.f9650i = b1Var2;
        }
        this.f9653l = null;
        this.f9651j = b1Var2;
        this.f9652k++;
        x();
        return b1Var2;
    }

    public b1 j() {
        return this.f9651j;
    }

    public c1 o(long j8, w1 w1Var) {
        b1 b1Var = this.f9651j;
        return b1Var == null ? h(w1Var) : i(w1Var.f11241a, b1Var, j8);
    }

    public b1 p() {
        return this.f9649h;
    }

    public b1 q() {
        return this.f9650i;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x006c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.google.android.exoplayer2.c1 r(com.google.android.exoplayer2.h2 r19, com.google.android.exoplayer2.c1 r20) {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            r2 = r20
            com.google.android.exoplayer2.source.k$b r3 = r2.f9480a
            boolean r12 = r0.s(r3)
            boolean r13 = r0.u(r1, r3)
            boolean r14 = r0.t(r1, r3, r12)
            com.google.android.exoplayer2.source.k$b r4 = r2.f9480a
            java.lang.Object r4 = r4.f20286a
            com.google.android.exoplayer2.h2$b r5 = r0.f9642a
            r1.l(r4, r5)
            boolean r1 = r3.b()
            r4 = -1
            r5 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
            if (r1 != 0) goto L35
            int r1 = r3.f20290e
            if (r1 != r4) goto L2e
            goto L35
        L2e:
            com.google.android.exoplayer2.h2$b r7 = r0.f9642a
            long r7 = r7.i(r1)
            goto L36
        L35:
            r7 = r5
        L36:
            boolean r1 = r3.b()
            if (r1 == 0) goto L48
            com.google.android.exoplayer2.h2$b r1 = r0.f9642a
            int r5 = r3.f20287b
            int r6 = r3.f20288c
            long r5 = r1.e(r5, r6)
        L46:
            r9 = r5
            goto L5c
        L48:
            int r1 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r1 == 0) goto L55
            r5 = -9223372036854775808
            int r1 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r1 != 0) goto L53
            goto L55
        L53:
            r9 = r7
            goto L5c
        L55:
            com.google.android.exoplayer2.h2$b r1 = r0.f9642a
            long r5 = r1.m()
            goto L46
        L5c:
            boolean r1 = r3.b()
            if (r1 == 0) goto L6c
            com.google.android.exoplayer2.h2$b r1 = r0.f9642a
            int r4 = r3.f20287b
            boolean r1 = r1.t(r4)
        L6a:
            r11 = r1
            goto L7c
        L6c:
            int r1 = r3.f20290e
            if (r1 == r4) goto L7a
            com.google.android.exoplayer2.h2$b r4 = r0.f9642a
            boolean r1 = r4.t(r1)
            if (r1 == 0) goto L7a
            r1 = 1
            goto L6a
        L7a:
            r1 = 0
            goto L6a
        L7c:
            com.google.android.exoplayer2.c1 r15 = new com.google.android.exoplayer2.c1
            long r4 = r2.f9481b
            long r1 = r2.f9482c
            r16 = r1
            r1 = r15
            r2 = r3
            r3 = r4
            r5 = r16
            r1.<init>(r2, r3, r5, r7, r9, r11, r12, r13, r14)
            return r15
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.e1.r(com.google.android.exoplayer2.h2, com.google.android.exoplayer2.c1):com.google.android.exoplayer2.c1");
    }

    public boolean v(com.google.android.exoplayer2.source.j jVar) {
        b1 b1Var = this.f9651j;
        return b1Var != null && b1Var.f9459a == jVar;
    }

    public void y(long j8) {
        b1 b1Var = this.f9651j;
        if (b1Var != null) {
            b1Var.s(j8);
        }
    }

    public boolean z(b1 b1Var) {
        boolean z4 = false;
        b6.a.f(b1Var != null);
        if (b1Var.equals(this.f9651j)) {
            return false;
        }
        this.f9651j = b1Var;
        while (b1Var.j() != null) {
            b1Var = b1Var.j();
            if (b1Var == this.f9650i) {
                this.f9650i = this.f9649h;
                z4 = true;
            }
            b1Var.t();
            this.f9652k--;
        }
        this.f9651j.w(null);
        x();
        return z4;
    }
}
