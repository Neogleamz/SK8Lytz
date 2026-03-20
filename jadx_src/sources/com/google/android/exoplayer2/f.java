package com.google.android.exoplayer2;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class f implements c2, i4.f0 {

    /* renamed from: a  reason: collision with root package name */
    private final int f9694a;

    /* renamed from: c  reason: collision with root package name */
    private i4.g0 f9696c;

    /* renamed from: d  reason: collision with root package name */
    private int f9697d;

    /* renamed from: e  reason: collision with root package name */
    private j4.t1 f9698e;

    /* renamed from: f  reason: collision with root package name */
    private int f9699f;

    /* renamed from: g  reason: collision with root package name */
    private h5.r f9700g;

    /* renamed from: h  reason: collision with root package name */
    private w0[] f9701h;

    /* renamed from: j  reason: collision with root package name */
    private long f9702j;

    /* renamed from: k  reason: collision with root package name */
    private long f9703k;

    /* renamed from: m  reason: collision with root package name */
    private boolean f9705m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f9706n;

    /* renamed from: b  reason: collision with root package name */
    private final i4.s f9695b = new i4.s();

    /* renamed from: l  reason: collision with root package name */
    private long f9704l = Long.MIN_VALUE;

    public f(int i8) {
        this.f9694a = i8;
    }

    private void W(long j8, boolean z4) {
        this.f9705m = false;
        this.f9703k = j8;
        this.f9704l = j8;
        Q(j8, z4);
    }

    @Override // com.google.android.exoplayer2.c2
    public final void A() {
        ((h5.r) b6.a.e(this.f9700g)).a();
    }

    @Override // com.google.android.exoplayer2.c2
    public final long B() {
        return this.f9704l;
    }

    @Override // com.google.android.exoplayer2.c2
    public final void C(long j8) {
        W(j8, false);
    }

    @Override // com.google.android.exoplayer2.c2
    public final boolean D() {
        return this.f9705m;
    }

    @Override // com.google.android.exoplayer2.c2
    public b6.r E() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final ExoPlaybackException G(Throwable th, w0 w0Var, int i8) {
        return H(th, w0Var, false, i8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final ExoPlaybackException H(Throwable th, w0 w0Var, boolean z4, int i8) {
        int i9;
        if (w0Var != null && !this.f9706n) {
            this.f9706n = true;
            try {
                i9 = i4.f0.F(a(w0Var));
            } catch (ExoPlaybackException unused) {
            } finally {
                this.f9706n = false;
            }
            return ExoPlaybackException.f(th, getName(), K(), w0Var, i9, z4, i8);
        }
        i9 = 4;
        return ExoPlaybackException.f(th, getName(), K(), w0Var, i9, z4, i8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final i4.g0 I() {
        return (i4.g0) b6.a.e(this.f9696c);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final i4.s J() {
        this.f9695b.a();
        return this.f9695b;
    }

    protected final int K() {
        return this.f9697d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final j4.t1 L() {
        return (j4.t1) b6.a.e(this.f9698e);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final w0[] M() {
        return (w0[]) b6.a.e(this.f9701h);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean N() {
        return i() ? this.f9705m : ((h5.r) b6.a.e(this.f9700g)).e();
    }

    protected abstract void O();

    protected void P(boolean z4, boolean z8) {
    }

    protected abstract void Q(long j8, boolean z4);

    protected void R() {
    }

    protected void S() {
    }

    protected void T() {
    }

    protected abstract void U(w0[] w0VarArr, long j8, long j9);

    /* JADX INFO: Access modifiers changed from: protected */
    public final int V(i4.s sVar, DecoderInputBuffer decoderInputBuffer, int i8) {
        int o5 = ((h5.r) b6.a.e(this.f9700g)).o(sVar, decoderInputBuffer, i8);
        if (o5 == -4) {
            if (decoderInputBuffer.t()) {
                this.f9704l = Long.MIN_VALUE;
                return this.f9705m ? -4 : -3;
            }
            long j8 = decoderInputBuffer.f9514e + this.f9702j;
            decoderInputBuffer.f9514e = j8;
            this.f9704l = Math.max(this.f9704l, j8);
        } else if (o5 == -5) {
            w0 w0Var = (w0) b6.a.e(sVar.f20512b);
            if (w0Var.f11210t != Long.MAX_VALUE) {
                sVar.f20512b = w0Var.b().k0(w0Var.f11210t + this.f9702j).G();
            }
        }
        return o5;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int X(long j8) {
        return ((h5.r) b6.a.e(this.f9700g)).m(j8 - this.f9702j);
    }

    @Override // com.google.android.exoplayer2.c2
    public final void g() {
        b6.a.f(this.f9699f == 1);
        this.f9695b.a();
        this.f9699f = 0;
        this.f9700g = null;
        this.f9701h = null;
        this.f9705m = false;
        O();
    }

    @Override // com.google.android.exoplayer2.c2
    public final int getState() {
        return this.f9699f;
    }

    @Override // com.google.android.exoplayer2.c2, i4.f0
    public final int h() {
        return this.f9694a;
    }

    @Override // com.google.android.exoplayer2.c2
    public final boolean i() {
        return this.f9704l == Long.MIN_VALUE;
    }

    @Override // com.google.android.exoplayer2.c2
    public final void k(i4.g0 g0Var, w0[] w0VarArr, h5.r rVar, long j8, boolean z4, boolean z8, long j9, long j10) {
        b6.a.f(this.f9699f == 0);
        this.f9696c = g0Var;
        this.f9699f = 1;
        P(z4, z8);
        z(w0VarArr, rVar, j9, j10);
        W(j8, z4);
    }

    @Override // com.google.android.exoplayer2.c2
    public final void l(int i8, j4.t1 t1Var) {
        this.f9697d = i8;
        this.f9698e = t1Var;
    }

    @Override // com.google.android.exoplayer2.c2
    public final void m() {
        this.f9705m = true;
    }

    @Override // com.google.android.exoplayer2.c2
    public final i4.f0 q() {
        return this;
    }

    @Override // com.google.android.exoplayer2.c2
    public final void reset() {
        b6.a.f(this.f9699f == 0);
        this.f9695b.a();
        R();
    }

    @Override // com.google.android.exoplayer2.c2
    public final void start() {
        b6.a.f(this.f9699f == 1);
        this.f9699f = 2;
        S();
    }

    @Override // com.google.android.exoplayer2.c2
    public final void stop() {
        b6.a.f(this.f9699f == 2);
        this.f9699f = 1;
        T();
    }

    @Override // i4.f0
    public int v() {
        return 0;
    }

    @Override // com.google.android.exoplayer2.z1.b
    public void x(int i8, Object obj) {
    }

    @Override // com.google.android.exoplayer2.c2
    public final h5.r y() {
        return this.f9700g;
    }

    @Override // com.google.android.exoplayer2.c2
    public final void z(w0[] w0VarArr, h5.r rVar, long j8, long j9) {
        b6.a.f(!this.f9705m);
        this.f9700g = rVar;
        if (this.f9704l == Long.MIN_VALUE) {
            this.f9704l = j8;
        }
        this.f9701h = w0VarArr;
        this.f9702j = j9;
        U(w0VarArr, j8, j9);
    }
}
