package com.google.android.exoplayer2;

import android.os.SystemClock;
import com.google.android.exoplayer2.z0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h implements y0 {

    /* renamed from: a  reason: collision with root package name */
    private final float f9717a;

    /* renamed from: b  reason: collision with root package name */
    private final float f9718b;

    /* renamed from: c  reason: collision with root package name */
    private final long f9719c;

    /* renamed from: d  reason: collision with root package name */
    private final float f9720d;

    /* renamed from: e  reason: collision with root package name */
    private final long f9721e;

    /* renamed from: f  reason: collision with root package name */
    private final long f9722f;

    /* renamed from: g  reason: collision with root package name */
    private final float f9723g;

    /* renamed from: h  reason: collision with root package name */
    private long f9724h;

    /* renamed from: i  reason: collision with root package name */
    private long f9725i;

    /* renamed from: j  reason: collision with root package name */
    private long f9726j;

    /* renamed from: k  reason: collision with root package name */
    private long f9727k;

    /* renamed from: l  reason: collision with root package name */
    private long f9728l;

    /* renamed from: m  reason: collision with root package name */
    private long f9729m;

    /* renamed from: n  reason: collision with root package name */
    private float f9730n;

    /* renamed from: o  reason: collision with root package name */
    private float f9731o;

    /* renamed from: p  reason: collision with root package name */
    private float f9732p;
    private long q;

    /* renamed from: r  reason: collision with root package name */
    private long f9733r;

    /* renamed from: s  reason: collision with root package name */
    private long f9734s;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private float f9735a = 0.97f;

        /* renamed from: b  reason: collision with root package name */
        private float f9736b = 1.03f;

        /* renamed from: c  reason: collision with root package name */
        private long f9737c = 1000;

        /* renamed from: d  reason: collision with root package name */
        private float f9738d = 1.0E-7f;

        /* renamed from: e  reason: collision with root package name */
        private long f9739e = b6.l0.C0(20);

        /* renamed from: f  reason: collision with root package name */
        private long f9740f = b6.l0.C0(500);

        /* renamed from: g  reason: collision with root package name */
        private float f9741g = 0.999f;

        public h a() {
            return new h(this.f9735a, this.f9736b, this.f9737c, this.f9738d, this.f9739e, this.f9740f, this.f9741g);
        }

        public b b(float f5) {
            b6.a.a(f5 >= 1.0f);
            this.f9736b = f5;
            return this;
        }

        public b c(float f5) {
            b6.a.a(0.0f < f5 && f5 <= 1.0f);
            this.f9735a = f5;
            return this;
        }

        public b d(long j8) {
            b6.a.a(j8 > 0);
            this.f9739e = b6.l0.C0(j8);
            return this;
        }

        public b e(float f5) {
            b6.a.a(f5 >= 0.0f && f5 < 1.0f);
            this.f9741g = f5;
            return this;
        }

        public b f(long j8) {
            b6.a.a(j8 > 0);
            this.f9737c = j8;
            return this;
        }

        public b g(float f5) {
            b6.a.a(f5 > 0.0f);
            this.f9738d = f5 / 1000000.0f;
            return this;
        }

        public b h(long j8) {
            b6.a.a(j8 >= 0);
            this.f9740f = b6.l0.C0(j8);
            return this;
        }
    }

    private h(float f5, float f8, long j8, float f9, long j9, long j10, float f10) {
        this.f9717a = f5;
        this.f9718b = f8;
        this.f9719c = j8;
        this.f9720d = f9;
        this.f9721e = j9;
        this.f9722f = j10;
        this.f9723g = f10;
        this.f9724h = -9223372036854775807L;
        this.f9725i = -9223372036854775807L;
        this.f9727k = -9223372036854775807L;
        this.f9728l = -9223372036854775807L;
        this.f9731o = f5;
        this.f9730n = f8;
        this.f9732p = 1.0f;
        this.q = -9223372036854775807L;
        this.f9726j = -9223372036854775807L;
        this.f9729m = -9223372036854775807L;
        this.f9733r = -9223372036854775807L;
        this.f9734s = -9223372036854775807L;
    }

    private void f(long j8) {
        long j9 = this.f9733r + (this.f9734s * 3);
        if (this.f9729m > j9) {
            float C0 = (float) b6.l0.C0(this.f9719c);
            this.f9729m = com.google.common.primitives.i.c(j9, this.f9726j, this.f9729m - (((this.f9732p - 1.0f) * C0) + ((this.f9730n - 1.0f) * C0)));
            return;
        }
        long r4 = b6.l0.r(j8 - (Math.max(0.0f, this.f9732p - 1.0f) / this.f9720d), this.f9729m, j9);
        this.f9729m = r4;
        long j10 = this.f9728l;
        if (j10 == -9223372036854775807L || r4 <= j10) {
            return;
        }
        this.f9729m = j10;
    }

    private void g() {
        long j8 = this.f9724h;
        if (j8 != -9223372036854775807L) {
            long j9 = this.f9725i;
            if (j9 != -9223372036854775807L) {
                j8 = j9;
            }
            long j10 = this.f9727k;
            if (j10 != -9223372036854775807L && j8 < j10) {
                j8 = j10;
            }
            long j11 = this.f9728l;
            if (j11 != -9223372036854775807L && j8 > j11) {
                j8 = j11;
            }
        } else {
            j8 = -9223372036854775807L;
        }
        if (this.f9726j == j8) {
            return;
        }
        this.f9726j = j8;
        this.f9729m = j8;
        this.f9733r = -9223372036854775807L;
        this.f9734s = -9223372036854775807L;
        this.q = -9223372036854775807L;
    }

    private static long h(long j8, long j9, float f5) {
        return (((float) j8) * f5) + ((1.0f - f5) * ((float) j9));
    }

    private void i(long j8, long j9) {
        long h8;
        long j10 = j8 - j9;
        long j11 = this.f9733r;
        if (j11 == -9223372036854775807L) {
            this.f9733r = j10;
            h8 = 0;
        } else {
            long max = Math.max(j10, h(j11, j10, this.f9723g));
            this.f9733r = max;
            h8 = h(this.f9734s, Math.abs(j10 - max), this.f9723g);
        }
        this.f9734s = h8;
    }

    @Override // com.google.android.exoplayer2.y0
    public void a(z0.g gVar) {
        this.f9724h = b6.l0.C0(gVar.f11368a);
        this.f9727k = b6.l0.C0(gVar.f11369b);
        this.f9728l = b6.l0.C0(gVar.f11370c);
        float f5 = gVar.f11371d;
        if (f5 == -3.4028235E38f) {
            f5 = this.f9717a;
        }
        this.f9731o = f5;
        float f8 = gVar.f11372e;
        if (f8 == -3.4028235E38f) {
            f8 = this.f9718b;
        }
        this.f9730n = f8;
        if (f5 == 1.0f && f8 == 1.0f) {
            this.f9724h = -9223372036854775807L;
        }
        g();
    }

    @Override // com.google.android.exoplayer2.y0
    public float b(long j8, long j9) {
        if (this.f9724h == -9223372036854775807L) {
            return 1.0f;
        }
        i(j8, j9);
        if (this.q == -9223372036854775807L || SystemClock.elapsedRealtime() - this.q >= this.f9719c) {
            this.q = SystemClock.elapsedRealtime();
            f(j8);
            long j10 = j8 - this.f9729m;
            if (Math.abs(j10) < this.f9721e) {
                this.f9732p = 1.0f;
            } else {
                this.f9732p = b6.l0.p((this.f9720d * ((float) j10)) + 1.0f, this.f9731o, this.f9730n);
            }
            return this.f9732p;
        }
        return this.f9732p;
    }

    @Override // com.google.android.exoplayer2.y0
    public long c() {
        return this.f9729m;
    }

    @Override // com.google.android.exoplayer2.y0
    public void d() {
        long j8 = this.f9729m;
        if (j8 == -9223372036854775807L) {
            return;
        }
        long j9 = j8 + this.f9722f;
        this.f9729m = j9;
        long j10 = this.f9728l;
        if (j10 != -9223372036854775807L && j9 > j10) {
            this.f9729m = j10;
        }
        this.q = -9223372036854775807L;
    }

    @Override // com.google.android.exoplayer2.y0
    public void e(long j8) {
        this.f9725i = j8;
        g();
    }
}
