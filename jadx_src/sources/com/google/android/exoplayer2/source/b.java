package com.google.android.exoplayer2.source;

import b6.l0;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.source.ClippingMediaSource;
import com.google.android.exoplayer2.source.j;
import com.google.android.exoplayer2.w0;
import i4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b implements j, j.a {

    /* renamed from: a  reason: collision with root package name */
    public final j f10262a;

    /* renamed from: b  reason: collision with root package name */
    private j.a f10263b;

    /* renamed from: c  reason: collision with root package name */
    private a[] f10264c = new a[0];

    /* renamed from: d  reason: collision with root package name */
    private long f10265d;

    /* renamed from: e  reason: collision with root package name */
    long f10266e;

    /* renamed from: f  reason: collision with root package name */
    long f10267f;

    /* renamed from: g  reason: collision with root package name */
    private ClippingMediaSource.IllegalClippingException f10268g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private final class a implements h5.r {

        /* renamed from: a  reason: collision with root package name */
        public final h5.r f10269a;

        /* renamed from: b  reason: collision with root package name */
        private boolean f10270b;

        public a(h5.r rVar) {
            this.f10269a = rVar;
        }

        @Override // h5.r
        public void a() {
            this.f10269a.a();
        }

        public void b() {
            this.f10270b = false;
        }

        @Override // h5.r
        public boolean e() {
            return !b.this.m() && this.f10269a.e();
        }

        @Override // h5.r
        public int m(long j8) {
            if (b.this.m()) {
                return -3;
            }
            return this.f10269a.m(j8);
        }

        @Override // h5.r
        public int o(i4.s sVar, DecoderInputBuffer decoderInputBuffer, int i8) {
            if (b.this.m()) {
                return -3;
            }
            if (this.f10270b) {
                decoderInputBuffer.x(4);
                return -4;
            }
            int o5 = this.f10269a.o(sVar, decoderInputBuffer, i8);
            if (o5 == -5) {
                w0 w0Var = (w0) b6.a.e(sVar.f20512b);
                int i9 = w0Var.K;
                if (i9 != 0 || w0Var.L != 0) {
                    b bVar = b.this;
                    if (bVar.f10266e != 0) {
                        i9 = 0;
                    }
                    sVar.f20512b = w0Var.b().P(i9).Q(bVar.f10267f == Long.MIN_VALUE ? w0Var.L : 0).G();
                }
                return -5;
            }
            b bVar2 = b.this;
            long j8 = bVar2.f10267f;
            if (j8 == Long.MIN_VALUE || ((o5 != -4 || decoderInputBuffer.f9514e < j8) && !(o5 == -3 && bVar2.g() == Long.MIN_VALUE && !decoderInputBuffer.f9513d))) {
                return o5;
            }
            decoderInputBuffer.k();
            decoderInputBuffer.x(4);
            this.f10270b = true;
            return -4;
        }
    }

    public b(j jVar, boolean z4, long j8, long j9) {
        this.f10262a = jVar;
        this.f10265d = z4 ? j8 : -9223372036854775807L;
        this.f10266e = j8;
        this.f10267f = j9;
    }

    private i0 j(long j8, i0 i0Var) {
        long r4 = l0.r(i0Var.f20507a, 0L, j8 - this.f10266e);
        long j9 = i0Var.f20508b;
        long j10 = this.f10267f;
        long r8 = l0.r(j9, 0L, j10 == Long.MIN_VALUE ? Long.MAX_VALUE : j10 - j8);
        return (r4 == i0Var.f20507a && r8 == i0Var.f20508b) ? i0Var : new i0(r4, r8);
    }

    private static boolean v(long j8, z5.r[] rVarArr) {
        if (j8 != 0) {
            for (z5.r rVar : rVarArr) {
                if (rVar != null) {
                    w0 n8 = rVar.n();
                    if (!b6.t.a(n8.f11207m, n8.f11204j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public long b() {
        long b9 = this.f10262a.b();
        if (b9 != Long.MIN_VALUE) {
            long j8 = this.f10267f;
            if (j8 == Long.MIN_VALUE || b9 < j8) {
                return b9;
            }
        }
        return Long.MIN_VALUE;
    }

    @Override // com.google.android.exoplayer2.source.j
    public long c(long j8, i0 i0Var) {
        long j9 = this.f10266e;
        if (j8 == j9) {
            return j9;
        }
        return this.f10262a.c(j8, j(j8, i0Var));
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public boolean d(long j8) {
        return this.f10262a.d(j8);
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public boolean f() {
        return this.f10262a.f();
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public long g() {
        long g8 = this.f10262a.g();
        if (g8 != Long.MIN_VALUE) {
            long j8 = this.f10267f;
            if (j8 == Long.MIN_VALUE || g8 < j8) {
                return g8;
            }
        }
        return Long.MIN_VALUE;
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public void h(long j8) {
        this.f10262a.h(j8);
    }

    @Override // com.google.android.exoplayer2.source.j.a
    public void k(j jVar) {
        if (this.f10268g != null) {
            return;
        }
        ((j.a) b6.a.e(this.f10263b)).k(this);
    }

    @Override // com.google.android.exoplayer2.source.j
    public void l() {
        ClippingMediaSource.IllegalClippingException illegalClippingException = this.f10268g;
        if (illegalClippingException != null) {
            throw illegalClippingException;
        }
        this.f10262a.l();
    }

    boolean m() {
        return this.f10265d != -9223372036854775807L;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0032, code lost:
        if (r0 > r6) goto L18;
     */
    @Override // com.google.android.exoplayer2.source.j
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public long n(long r6) {
        /*
            r5 = this;
            r0 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
            r5.f10265d = r0
            com.google.android.exoplayer2.source.b$a[] r0 = r5.f10264c
            int r1 = r0.length
            r2 = 0
            r3 = r2
        Lc:
            if (r3 >= r1) goto L18
            r4 = r0[r3]
            if (r4 == 0) goto L15
            r4.b()
        L15:
            int r3 = r3 + 1
            goto Lc
        L18:
            com.google.android.exoplayer2.source.j r0 = r5.f10262a
            long r0 = r0.n(r6)
            int r6 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r6 == 0) goto L34
            long r6 = r5.f10266e
            int r6 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r6 < 0) goto L35
            long r6 = r5.f10267f
            r3 = -9223372036854775808
            int r3 = (r6 > r3 ? 1 : (r6 == r3 ? 0 : -1))
            if (r3 == 0) goto L34
            int r6 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r6 > 0) goto L35
        L34:
            r2 = 1
        L35:
            b6.a.f(r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.b.n(long):long");
    }

    @Override // com.google.android.exoplayer2.source.w.a
    /* renamed from: o */
    public void e(j jVar) {
        ((j.a) b6.a.e(this.f10263b)).e(this);
    }

    @Override // com.google.android.exoplayer2.source.j
    public long p() {
        if (m()) {
            long j8 = this.f10265d;
            this.f10265d = -9223372036854775807L;
            long p8 = p();
            return p8 != -9223372036854775807L ? p8 : j8;
        }
        long p9 = this.f10262a.p();
        if (p9 == -9223372036854775807L) {
            return -9223372036854775807L;
        }
        boolean z4 = true;
        b6.a.f(p9 >= this.f10266e);
        long j9 = this.f10267f;
        if (j9 != Long.MIN_VALUE && p9 > j9) {
            z4 = false;
        }
        b6.a.f(z4);
        return p9;
    }

    @Override // com.google.android.exoplayer2.source.j
    public void q(j.a aVar, long j8) {
        this.f10263b = aVar;
        this.f10262a.q(this, j8);
    }

    @Override // com.google.android.exoplayer2.source.j
    public h5.w r() {
        return this.f10262a.r();
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0062, code lost:
        if (r2 > r4) goto L26;
     */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x006e  */
    @Override // com.google.android.exoplayer2.source.j
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public long s(z5.r[] r13, boolean[] r14, h5.r[] r15, boolean[] r16, long r17) {
        /*
            r12 = this;
            r0 = r12
            r1 = r15
            int r2 = r1.length
            com.google.android.exoplayer2.source.b$a[] r2 = new com.google.android.exoplayer2.source.b.a[r2]
            r0.f10264c = r2
            int r2 = r1.length
            h5.r[] r9 = new h5.r[r2]
            r10 = 0
            r2 = r10
        Lc:
            int r3 = r1.length
            r11 = 0
            if (r2 >= r3) goto L25
            com.google.android.exoplayer2.source.b$a[] r3 = r0.f10264c
            r4 = r1[r2]
            com.google.android.exoplayer2.source.b$a r4 = (com.google.android.exoplayer2.source.b.a) r4
            r3[r2] = r4
            r4 = r3[r2]
            if (r4 == 0) goto L20
            r3 = r3[r2]
            h5.r r11 = r3.f10269a
        L20:
            r9[r2] = r11
            int r2 = r2 + 1
            goto Lc
        L25:
            com.google.android.exoplayer2.source.j r2 = r0.f10262a
            r3 = r13
            r4 = r14
            r5 = r9
            r6 = r16
            r7 = r17
            long r2 = r2.s(r3, r4, r5, r6, r7)
            boolean r4 = r12.m()
            if (r4 == 0) goto L47
            long r4 = r0.f10266e
            int r6 = (r17 > r4 ? 1 : (r17 == r4 ? 0 : -1))
            if (r6 != 0) goto L47
            r6 = r13
            boolean r4 = v(r4, r13)
            if (r4 == 0) goto L47
            r4 = r2
            goto L4c
        L47:
            r4 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
        L4c:
            r0.f10265d = r4
            int r4 = (r2 > r17 ? 1 : (r2 == r17 ? 0 : -1))
            if (r4 == 0) goto L67
            long r4 = r0.f10266e
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 < 0) goto L65
            long r4 = r0.f10267f
            r6 = -9223372036854775808
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 == 0) goto L67
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 > 0) goto L65
            goto L67
        L65:
            r4 = r10
            goto L68
        L67:
            r4 = 1
        L68:
            b6.a.f(r4)
        L6b:
            int r4 = r1.length
            if (r10 >= r4) goto L97
            r4 = r9[r10]
            if (r4 != 0) goto L77
            com.google.android.exoplayer2.source.b$a[] r4 = r0.f10264c
            r4[r10] = r11
            goto L8e
        L77:
            com.google.android.exoplayer2.source.b$a[] r4 = r0.f10264c
            r5 = r4[r10]
            if (r5 == 0) goto L85
            r5 = r4[r10]
            h5.r r5 = r5.f10269a
            r6 = r9[r10]
            if (r5 == r6) goto L8e
        L85:
            com.google.android.exoplayer2.source.b$a r5 = new com.google.android.exoplayer2.source.b$a
            r6 = r9[r10]
            r5.<init>(r6)
            r4[r10] = r5
        L8e:
            com.google.android.exoplayer2.source.b$a[] r4 = r0.f10264c
            r4 = r4[r10]
            r1[r10] = r4
            int r10 = r10 + 1
            goto L6b
        L97:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.b.s(z5.r[], boolean[], h5.r[], boolean[], long):long");
    }

    public void t(ClippingMediaSource.IllegalClippingException illegalClippingException) {
        this.f10268g = illegalClippingException;
    }

    @Override // com.google.android.exoplayer2.source.j
    public void u(long j8, boolean z4) {
        this.f10262a.u(j8, z4);
    }

    public void w(long j8, long j9) {
        this.f10266e = j8;
        this.f10267f = j9;
    }
}
