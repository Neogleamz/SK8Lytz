package h5;

import android.net.Uri;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.z0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s extends h2 {

    /* renamed from: y  reason: collision with root package name */
    private static final Object f20291y = new Object();

    /* renamed from: z  reason: collision with root package name */
    private static final z0 f20292z = new z0.c().c("SinglePeriodTimeline").f(Uri.EMPTY).a();

    /* renamed from: f  reason: collision with root package name */
    private final long f20293f;

    /* renamed from: g  reason: collision with root package name */
    private final long f20294g;

    /* renamed from: h  reason: collision with root package name */
    private final long f20295h;

    /* renamed from: j  reason: collision with root package name */
    private final long f20296j;

    /* renamed from: k  reason: collision with root package name */
    private final long f20297k;

    /* renamed from: l  reason: collision with root package name */
    private final long f20298l;

    /* renamed from: m  reason: collision with root package name */
    private final long f20299m;

    /* renamed from: n  reason: collision with root package name */
    private final boolean f20300n;

    /* renamed from: p  reason: collision with root package name */
    private final boolean f20301p;
    private final boolean q;

    /* renamed from: t  reason: collision with root package name */
    private final Object f20302t;

    /* renamed from: w  reason: collision with root package name */
    private final z0 f20303w;

    /* renamed from: x  reason: collision with root package name */
    private final z0.g f20304x;

    public s(long j8, long j9, long j10, long j11, long j12, long j13, long j14, boolean z4, boolean z8, boolean z9, Object obj, z0 z0Var, z0.g gVar) {
        this.f20293f = j8;
        this.f20294g = j9;
        this.f20295h = j10;
        this.f20296j = j11;
        this.f20297k = j12;
        this.f20298l = j13;
        this.f20299m = j14;
        this.f20300n = z4;
        this.f20301p = z8;
        this.q = z9;
        this.f20302t = obj;
        this.f20303w = (z0) b6.a.e(z0Var);
        this.f20304x = gVar;
    }

    public s(long j8, long j9, long j10, long j11, boolean z4, boolean z8, boolean z9, Object obj, z0 z0Var) {
        this(-9223372036854775807L, -9223372036854775807L, -9223372036854775807L, j8, j9, j10, j11, z4, z8, false, obj, z0Var, z9 ? z0Var.f11306d : null);
    }

    public s(long j8, boolean z4, boolean z8, boolean z9, Object obj, z0 z0Var) {
        this(j8, j8, 0L, 0L, z4, z8, z9, obj, z0Var);
    }

    @Override // com.google.android.exoplayer2.h2
    public int f(Object obj) {
        return f20291y.equals(obj) ? 0 : -1;
    }

    @Override // com.google.android.exoplayer2.h2
    public h2.b k(int i8, h2.b bVar, boolean z4) {
        b6.a.c(i8, 0, 1);
        return bVar.u(null, z4 ? f20291y : null, 0, this.f20296j, -this.f20298l);
    }

    @Override // com.google.android.exoplayer2.h2
    public int m() {
        return 1;
    }

    @Override // com.google.android.exoplayer2.h2
    public Object q(int i8) {
        b6.a.c(i8, 0, 1);
        return f20291y;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x002b, code lost:
        if (r1 > r5) goto L9;
     */
    @Override // com.google.android.exoplayer2.h2
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.google.android.exoplayer2.h2.d s(int r25, com.google.android.exoplayer2.h2.d r26, long r27) {
        /*
            r24 = this;
            r0 = r24
            r1 = 0
            r2 = 1
            r3 = r25
            b6.a.c(r3, r1, r2)
            long r1 = r0.f20299m
            boolean r14 = r0.f20301p
            r3 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
            if (r14 == 0) goto L2e
            boolean r5 = r0.q
            if (r5 != 0) goto L2e
            r5 = 0
            int r5 = (r27 > r5 ? 1 : (r27 == r5 ? 0 : -1))
            if (r5 == 0) goto L2e
            long r5 = r0.f20297k
            int r7 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r7 != 0) goto L27
        L24:
            r16 = r3
            goto L30
        L27:
            long r1 = r1 + r27
            int r5 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r5 <= 0) goto L2e
            goto L24
        L2e:
            r16 = r1
        L30:
            java.lang.Object r4 = com.google.android.exoplayer2.h2.d.f9767x
            com.google.android.exoplayer2.z0 r5 = r0.f20303w
            java.lang.Object r6 = r0.f20302t
            long r7 = r0.f20293f
            long r9 = r0.f20294g
            long r11 = r0.f20295h
            boolean r13 = r0.f20300n
            com.google.android.exoplayer2.z0$g r15 = r0.f20304x
            long r1 = r0.f20297k
            r18 = r1
            r20 = 0
            r21 = 0
            long r1 = r0.f20298l
            r22 = r1
            r3 = r26
            com.google.android.exoplayer2.h2$d r1 = r3.i(r4, r5, r6, r7, r9, r11, r13, r14, r15, r16, r18, r20, r21, r22)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: h5.s.s(int, com.google.android.exoplayer2.h2$d, long):com.google.android.exoplayer2.h2$d");
    }

    @Override // com.google.android.exoplayer2.h2
    public int t() {
        return 1;
    }
}
