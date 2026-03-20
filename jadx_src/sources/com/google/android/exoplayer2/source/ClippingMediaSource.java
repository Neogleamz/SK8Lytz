package com.google.android.exoplayer2.source;

import b6.l0;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import java.io.IOException;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ClippingMediaSource extends a0 {
    private IllegalClippingException A;
    private long B;
    private long C;

    /* renamed from: n  reason: collision with root package name */
    private final long f10240n;

    /* renamed from: p  reason: collision with root package name */
    private final long f10241p;
    private final boolean q;

    /* renamed from: t  reason: collision with root package name */
    private final boolean f10242t;

    /* renamed from: w  reason: collision with root package name */
    private final boolean f10243w;

    /* renamed from: x  reason: collision with root package name */
    private final ArrayList<b> f10244x;

    /* renamed from: y  reason: collision with root package name */
    private final h2.d f10245y;

    /* renamed from: z  reason: collision with root package name */
    private a f10246z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class IllegalClippingException extends IOException {

        /* renamed from: a  reason: collision with root package name */
        public final int f10247a;

        public IllegalClippingException(int i8) {
            super("Illegal clipping: " + a(i8));
            this.f10247a = i8;
        }

        private static String a(int i8) {
            return i8 != 0 ? i8 != 1 ? i8 != 2 ? "unknown" : "start exceeds end" : "not seekable to start" : "invalid period count";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends f {

        /* renamed from: g  reason: collision with root package name */
        private final long f10248g;

        /* renamed from: h  reason: collision with root package name */
        private final long f10249h;

        /* renamed from: j  reason: collision with root package name */
        private final long f10250j;

        /* renamed from: k  reason: collision with root package name */
        private final boolean f10251k;

        public a(h2 h2Var, long j8, long j9) {
            super(h2Var);
            boolean z4 = false;
            if (h2Var.m() != 1) {
                throw new IllegalClippingException(0);
            }
            h2.d r4 = h2Var.r(0, new h2.d());
            long max = Math.max(0L, j8);
            if (!r4.f9781m && max != 0 && !r4.f9777h) {
                throw new IllegalClippingException(1);
            }
            long max2 = j9 == Long.MIN_VALUE ? r4.f9783p : Math.max(0L, j9);
            long j10 = r4.f9783p;
            if (j10 != -9223372036854775807L) {
                max2 = max2 > j10 ? j10 : max2;
                if (max > max2) {
                    throw new IllegalClippingException(2);
                }
            }
            this.f10248g = max;
            this.f10249h = max2;
            int i8 = (max2 > (-9223372036854775807L) ? 1 : (max2 == (-9223372036854775807L) ? 0 : -1));
            this.f10250j = i8 == 0 ? -9223372036854775807L : max2 - max;
            if (r4.f9778j && (i8 == 0 || (j10 != -9223372036854775807L && max2 == j10))) {
                z4 = true;
            }
            this.f10251k = z4;
        }

        @Override // com.google.android.exoplayer2.source.f, com.google.android.exoplayer2.h2
        public h2.b k(int i8, h2.b bVar, boolean z4) {
            this.f10433f.k(0, bVar, z4);
            long q = bVar.q() - this.f10248g;
            long j8 = this.f10250j;
            return bVar.u(bVar.f9756a, bVar.f9757b, 0, j8 == -9223372036854775807L ? -9223372036854775807L : j8 - q, q);
        }

        @Override // com.google.android.exoplayer2.source.f, com.google.android.exoplayer2.h2
        public h2.d s(int i8, h2.d dVar, long j8) {
            this.f10433f.s(0, dVar, 0L);
            long j9 = dVar.f9785w;
            long j10 = this.f10248g;
            dVar.f9785w = j9 + j10;
            dVar.f9783p = this.f10250j;
            dVar.f9778j = this.f10251k;
            long j11 = dVar.f9782n;
            if (j11 != -9223372036854775807L) {
                long max = Math.max(j11, j10);
                dVar.f9782n = max;
                long j12 = this.f10249h;
                if (j12 != -9223372036854775807L) {
                    max = Math.min(max, j12);
                }
                dVar.f9782n = max;
                dVar.f9782n = max - this.f10248g;
            }
            long a12 = l0.a1(this.f10248g);
            long j13 = dVar.f9774e;
            if (j13 != -9223372036854775807L) {
                dVar.f9774e = j13 + a12;
            }
            long j14 = dVar.f9775f;
            if (j14 != -9223372036854775807L) {
                dVar.f9775f = j14 + a12;
            }
            return dVar;
        }
    }

    public ClippingMediaSource(k kVar, long j8, long j9) {
        this(kVar, j8, j9, true, false, false);
    }

    public ClippingMediaSource(k kVar, long j8, long j9, boolean z4, boolean z8, boolean z9) {
        super((k) b6.a.e(kVar));
        b6.a.a(j8 >= 0);
        this.f10240n = j8;
        this.f10241p = j9;
        this.q = z4;
        this.f10242t = z8;
        this.f10243w = z9;
        this.f10244x = new ArrayList<>();
        this.f10245y = new h2.d();
    }

    private void Z(h2 h2Var) {
        long j8;
        long j9;
        h2Var.r(0, this.f10245y);
        long g8 = this.f10245y.g();
        if (this.f10246z == null || this.f10244x.isEmpty() || this.f10242t) {
            long j10 = this.f10240n;
            long j11 = this.f10241p;
            if (this.f10243w) {
                long e8 = this.f10245y.e();
                j10 += e8;
                j11 += e8;
            }
            this.B = g8 + j10;
            this.C = this.f10241p != Long.MIN_VALUE ? g8 + j11 : Long.MIN_VALUE;
            int size = this.f10244x.size();
            for (int i8 = 0; i8 < size; i8++) {
                this.f10244x.get(i8).w(this.B, this.C);
            }
            j8 = j10;
            j9 = j11;
        } else {
            long j12 = this.B - g8;
            j9 = this.f10241p != Long.MIN_VALUE ? this.C - g8 : Long.MIN_VALUE;
            j8 = j12;
        }
        try {
            a aVar = new a(h2Var, j8, j9);
            this.f10246z = aVar;
            D(aVar);
        } catch (IllegalClippingException e9) {
            this.A = e9;
            for (int i9 = 0; i9 < this.f10244x.size(); i9++) {
                this.f10244x.get(i9).t(this.A);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.c, com.google.android.exoplayer2.source.a
    public void E() {
        super.E();
        this.A = null;
        this.f10246z = null;
    }

    @Override // com.google.android.exoplayer2.source.a0
    protected void V(h2 h2Var) {
        if (this.A != null) {
            return;
        }
        Z(h2Var);
    }

    @Override // com.google.android.exoplayer2.source.k
    public j b(k.b bVar, a6.b bVar2, long j8) {
        b bVar3 = new b(this.f10261l.b(bVar, bVar2, j8), this.q, this.B, this.C);
        this.f10244x.add(bVar3);
        return bVar3;
    }

    @Override // com.google.android.exoplayer2.source.c, com.google.android.exoplayer2.source.k
    public void n() {
        IllegalClippingException illegalClippingException = this.A;
        if (illegalClippingException != null) {
            throw illegalClippingException;
        }
        super.n();
    }

    @Override // com.google.android.exoplayer2.source.k
    public void p(j jVar) {
        b6.a.f(this.f10244x.remove(jVar));
        this.f10261l.p(((b) jVar).f10262a);
        if (!this.f10244x.isEmpty() || this.f10242t) {
            return;
        }
        Z(((a) b6.a.e(this.f10246z)).f10433f);
    }
}
