package com.google.android.exoplayer2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class i implements b6.r {

    /* renamed from: a  reason: collision with root package name */
    private final b6.d0 f9786a;

    /* renamed from: b  reason: collision with root package name */
    private final a f9787b;

    /* renamed from: c  reason: collision with root package name */
    private c2 f9788c;

    /* renamed from: d  reason: collision with root package name */
    private b6.r f9789d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f9790e = true;

    /* renamed from: f  reason: collision with root package name */
    private boolean f9791f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void w(x1 x1Var);
    }

    public i(a aVar, b6.d dVar) {
        this.f9787b = aVar;
        this.f9786a = new b6.d0(dVar);
    }

    private boolean g(boolean z4) {
        c2 c2Var = this.f9788c;
        return c2Var == null || c2Var.b() || (!this.f9788c.e() && (z4 || this.f9788c.i()));
    }

    private void k(boolean z4) {
        if (g(z4)) {
            this.f9790e = true;
            if (this.f9791f) {
                this.f9786a.b();
                return;
            }
            return;
        }
        b6.r rVar = (b6.r) b6.a.e(this.f9789d);
        long f5 = rVar.f();
        if (this.f9790e) {
            if (f5 < this.f9786a.f()) {
                this.f9786a.e();
                return;
            }
            this.f9790e = false;
            if (this.f9791f) {
                this.f9786a.b();
            }
        }
        this.f9786a.a(f5);
        x1 c9 = rVar.c();
        if (c9.equals(this.f9786a.c())) {
            return;
        }
        this.f9786a.d(c9);
        this.f9787b.w(c9);
    }

    public void a(c2 c2Var) {
        if (c2Var == this.f9788c) {
            this.f9789d = null;
            this.f9788c = null;
            this.f9790e = true;
        }
    }

    public void b(c2 c2Var) {
        b6.r rVar;
        b6.r E = c2Var.E();
        if (E == null || E == (rVar = this.f9789d)) {
            return;
        }
        if (rVar != null) {
            throw ExoPlaybackException.h(new IllegalStateException("Multiple renderer media clocks enabled."));
        }
        this.f9789d = E;
        this.f9788c = c2Var;
        E.d(this.f9786a.c());
    }

    @Override // b6.r
    public x1 c() {
        b6.r rVar = this.f9789d;
        return rVar != null ? rVar.c() : this.f9786a.c();
    }

    @Override // b6.r
    public void d(x1 x1Var) {
        b6.r rVar = this.f9789d;
        if (rVar != null) {
            rVar.d(x1Var);
            x1Var = this.f9789d.c();
        }
        this.f9786a.d(x1Var);
    }

    public void e(long j8) {
        this.f9786a.a(j8);
    }

    @Override // b6.r
    public long f() {
        return this.f9790e ? this.f9786a.f() : ((b6.r) b6.a.e(this.f9789d)).f();
    }

    public void h() {
        this.f9791f = true;
        this.f9786a.b();
    }

    public void i() {
        this.f9791f = false;
        this.f9786a.e();
    }

    public long j(boolean z4) {
        k(z4);
        return f();
    }
}
