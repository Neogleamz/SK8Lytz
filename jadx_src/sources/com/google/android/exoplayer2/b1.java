package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.k;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b1 {

    /* renamed from: a  reason: collision with root package name */
    public final com.google.android.exoplayer2.source.j f9459a;

    /* renamed from: b  reason: collision with root package name */
    public final Object f9460b;

    /* renamed from: c  reason: collision with root package name */
    public final h5.r[] f9461c;

    /* renamed from: d  reason: collision with root package name */
    public boolean f9462d;

    /* renamed from: e  reason: collision with root package name */
    public boolean f9463e;

    /* renamed from: f  reason: collision with root package name */
    public c1 f9464f;

    /* renamed from: g  reason: collision with root package name */
    public boolean f9465g;

    /* renamed from: h  reason: collision with root package name */
    private final boolean[] f9466h;

    /* renamed from: i  reason: collision with root package name */
    private final i4.f0[] f9467i;

    /* renamed from: j  reason: collision with root package name */
    private final z5.a0 f9468j;

    /* renamed from: k  reason: collision with root package name */
    private final t1 f9469k;

    /* renamed from: l  reason: collision with root package name */
    private b1 f9470l;

    /* renamed from: m  reason: collision with root package name */
    private h5.w f9471m;

    /* renamed from: n  reason: collision with root package name */
    private z5.b0 f9472n;

    /* renamed from: o  reason: collision with root package name */
    private long f9473o;

    public b1(i4.f0[] f0VarArr, long j8, z5.a0 a0Var, a6.b bVar, t1 t1Var, c1 c1Var, z5.b0 b0Var) {
        this.f9467i = f0VarArr;
        this.f9473o = j8;
        this.f9468j = a0Var;
        this.f9469k = t1Var;
        k.b bVar2 = c1Var.f9480a;
        this.f9460b = bVar2.f20286a;
        this.f9464f = c1Var;
        this.f9471m = h5.w.f20313d;
        this.f9472n = b0Var;
        this.f9461c = new h5.r[f0VarArr.length];
        this.f9466h = new boolean[f0VarArr.length];
        this.f9459a = e(bVar2, t1Var, bVar, c1Var.f9481b, c1Var.f9483d);
    }

    private void c(h5.r[] rVarArr) {
        int i8 = 0;
        while (true) {
            i4.f0[] f0VarArr = this.f9467i;
            if (i8 >= f0VarArr.length) {
                return;
            }
            if (f0VarArr[i8].h() == -2 && this.f9472n.c(i8)) {
                rVarArr[i8] = new h5.g();
            }
            i8++;
        }
    }

    private static com.google.android.exoplayer2.source.j e(k.b bVar, t1 t1Var, a6.b bVar2, long j8, long j9) {
        com.google.android.exoplayer2.source.j h8 = t1Var.h(bVar, bVar2, j8);
        return j9 != -9223372036854775807L ? new com.google.android.exoplayer2.source.b(h8, true, 0L, j9) : h8;
    }

    private void f() {
        if (!r()) {
            return;
        }
        int i8 = 0;
        while (true) {
            z5.b0 b0Var = this.f9472n;
            if (i8 >= b0Var.f24603a) {
                return;
            }
            boolean c9 = b0Var.c(i8);
            z5.r rVar = this.f9472n.f24605c[i8];
            if (c9 && rVar != null) {
                rVar.g();
            }
            i8++;
        }
    }

    private void g(h5.r[] rVarArr) {
        int i8 = 0;
        while (true) {
            i4.f0[] f0VarArr = this.f9467i;
            if (i8 >= f0VarArr.length) {
                return;
            }
            if (f0VarArr[i8].h() == -2) {
                rVarArr[i8] = null;
            }
            i8++;
        }
    }

    private void h() {
        if (!r()) {
            return;
        }
        int i8 = 0;
        while (true) {
            z5.b0 b0Var = this.f9472n;
            if (i8 >= b0Var.f24603a) {
                return;
            }
            boolean c9 = b0Var.c(i8);
            z5.r rVar = this.f9472n.f24605c[i8];
            if (c9 && rVar != null) {
                rVar.i();
            }
            i8++;
        }
    }

    private boolean r() {
        return this.f9470l == null;
    }

    private static void u(t1 t1Var, com.google.android.exoplayer2.source.j jVar) {
        try {
            if (jVar instanceof com.google.android.exoplayer2.source.b) {
                jVar = ((com.google.android.exoplayer2.source.b) jVar).f10262a;
            }
            t1Var.z(jVar);
        } catch (RuntimeException e8) {
            b6.p.d("MediaPeriodHolder", "Period release failed.", e8);
        }
    }

    public void A() {
        com.google.android.exoplayer2.source.j jVar = this.f9459a;
        if (jVar instanceof com.google.android.exoplayer2.source.b) {
            long j8 = this.f9464f.f9483d;
            if (j8 == -9223372036854775807L) {
                j8 = Long.MIN_VALUE;
            }
            ((com.google.android.exoplayer2.source.b) jVar).w(0L, j8);
        }
    }

    public long a(z5.b0 b0Var, long j8, boolean z4) {
        return b(b0Var, j8, z4, new boolean[this.f9467i.length]);
    }

    public long b(z5.b0 b0Var, long j8, boolean z4, boolean[] zArr) {
        int i8 = 0;
        while (true) {
            boolean z8 = true;
            if (i8 >= b0Var.f24603a) {
                break;
            }
            boolean[] zArr2 = this.f9466h;
            if (z4 || !b0Var.b(this.f9472n, i8)) {
                z8 = false;
            }
            zArr2[i8] = z8;
            i8++;
        }
        g(this.f9461c);
        f();
        this.f9472n = b0Var;
        h();
        long s8 = this.f9459a.s(b0Var.f24605c, this.f9466h, this.f9461c, zArr, j8);
        c(this.f9461c);
        this.f9463e = false;
        int i9 = 0;
        while (true) {
            h5.r[] rVarArr = this.f9461c;
            if (i9 >= rVarArr.length) {
                return s8;
            }
            if (rVarArr[i9] != null) {
                b6.a.f(b0Var.c(i9));
                if (this.f9467i[i9].h() != -2) {
                    this.f9463e = true;
                }
            } else {
                b6.a.f(b0Var.f24605c[i9] == null);
            }
            i9++;
        }
    }

    public void d(long j8) {
        b6.a.f(r());
        this.f9459a.d(y(j8));
    }

    public long i() {
        if (this.f9462d) {
            long g8 = this.f9463e ? this.f9459a.g() : Long.MIN_VALUE;
            return g8 == Long.MIN_VALUE ? this.f9464f.f9484e : g8;
        }
        return this.f9464f.f9481b;
    }

    public b1 j() {
        return this.f9470l;
    }

    public long k() {
        if (this.f9462d) {
            return this.f9459a.b();
        }
        return 0L;
    }

    public long l() {
        return this.f9473o;
    }

    public long m() {
        return this.f9464f.f9481b + this.f9473o;
    }

    public h5.w n() {
        return this.f9471m;
    }

    public z5.b0 o() {
        return this.f9472n;
    }

    public void p(float f5, h2 h2Var) {
        this.f9462d = true;
        this.f9471m = this.f9459a.r();
        z5.b0 v8 = v(f5, h2Var);
        c1 c1Var = this.f9464f;
        long j8 = c1Var.f9481b;
        long j9 = c1Var.f9484e;
        if (j9 != -9223372036854775807L && j8 >= j9) {
            j8 = Math.max(0L, j9 - 1);
        }
        long a9 = a(v8, j8, false);
        long j10 = this.f9473o;
        c1 c1Var2 = this.f9464f;
        this.f9473o = j10 + (c1Var2.f9481b - a9);
        this.f9464f = c1Var2.b(a9);
    }

    public boolean q() {
        return this.f9462d && (!this.f9463e || this.f9459a.g() == Long.MIN_VALUE);
    }

    public void s(long j8) {
        b6.a.f(r());
        if (this.f9462d) {
            this.f9459a.h(y(j8));
        }
    }

    public void t() {
        f();
        u(this.f9469k, this.f9459a);
    }

    public z5.b0 v(float f5, h2 h2Var) {
        z5.r[] rVarArr;
        z5.b0 g8 = this.f9468j.g(this.f9467i, n(), this.f9464f.f9480a, h2Var);
        for (z5.r rVar : g8.f24605c) {
            if (rVar != null) {
                rVar.q(f5);
            }
        }
        return g8;
    }

    public void w(b1 b1Var) {
        if (b1Var == this.f9470l) {
            return;
        }
        f();
        this.f9470l = b1Var;
        h();
    }

    public void x(long j8) {
        this.f9473o = j8;
    }

    public long y(long j8) {
        return j8 - l();
    }

    public long z(long j8) {
        return j8 + l();
    }
}
