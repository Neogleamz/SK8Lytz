package com.google.android.exoplayer2.source;

import b6.l0;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.h;
import com.google.android.exoplayer2.drm.i;
import com.google.android.exoplayer2.source.v;
import com.google.android.exoplayer2.w0;
import n4.b0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class v implements b0 {
    private w0 A;
    private w0 B;
    private int C;
    private boolean D;
    private boolean E;
    private long F;
    private boolean G;

    /* renamed from: a  reason: collision with root package name */
    private final t f10803a;

    /* renamed from: d  reason: collision with root package name */
    private final com.google.android.exoplayer2.drm.i f10806d;

    /* renamed from: e  reason: collision with root package name */
    private final h.a f10807e;

    /* renamed from: f  reason: collision with root package name */
    private d f10808f;

    /* renamed from: g  reason: collision with root package name */
    private w0 f10809g;

    /* renamed from: h  reason: collision with root package name */
    private DrmSession f10810h;

    /* renamed from: p  reason: collision with root package name */
    private int f10818p;
    private int q;

    /* renamed from: r  reason: collision with root package name */
    private int f10819r;

    /* renamed from: s  reason: collision with root package name */
    private int f10820s;

    /* renamed from: w  reason: collision with root package name */
    private boolean f10824w;

    /* renamed from: z  reason: collision with root package name */
    private boolean f10827z;

    /* renamed from: b  reason: collision with root package name */
    private final b f10804b = new b();

    /* renamed from: i  reason: collision with root package name */
    private int f10811i = 1000;

    /* renamed from: j  reason: collision with root package name */
    private int[] f10812j = new int[1000];

    /* renamed from: k  reason: collision with root package name */
    private long[] f10813k = new long[1000];

    /* renamed from: n  reason: collision with root package name */
    private long[] f10816n = new long[1000];

    /* renamed from: m  reason: collision with root package name */
    private int[] f10815m = new int[1000];

    /* renamed from: l  reason: collision with root package name */
    private int[] f10814l = new int[1000];

    /* renamed from: o  reason: collision with root package name */
    private b0.a[] f10817o = new b0.a[1000];

    /* renamed from: c  reason: collision with root package name */
    private final z<c> f10805c = new z<>(new b6.h() { // from class: com.google.android.exoplayer2.source.u
        @Override // b6.h
        public final void accept(Object obj) {
            v.L((v.c) obj);
        }
    });

    /* renamed from: t  reason: collision with root package name */
    private long f10821t = Long.MIN_VALUE;

    /* renamed from: u  reason: collision with root package name */
    private long f10822u = Long.MIN_VALUE;

    /* renamed from: v  reason: collision with root package name */
    private long f10823v = Long.MIN_VALUE;

    /* renamed from: y  reason: collision with root package name */
    private boolean f10826y = true;

    /* renamed from: x  reason: collision with root package name */
    private boolean f10825x = true;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public int f10828a;

        /* renamed from: b  reason: collision with root package name */
        public long f10829b;

        /* renamed from: c  reason: collision with root package name */
        public b0.a f10830c;

        b() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        public final w0 f10831a;

        /* renamed from: b  reason: collision with root package name */
        public final i.b f10832b;

        private c(w0 w0Var, i.b bVar) {
            this.f10831a = w0Var;
            this.f10832b = bVar;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        void a(w0 w0Var);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public v(a6.b bVar, com.google.android.exoplayer2.drm.i iVar, h.a aVar) {
        this.f10806d = iVar;
        this.f10807e = aVar;
        this.f10803a = new t(bVar);
    }

    private long B(int i8) {
        long j8 = Long.MIN_VALUE;
        if (i8 == 0) {
            return Long.MIN_VALUE;
        }
        int D = D(i8 - 1);
        for (int i9 = 0; i9 < i8; i9++) {
            j8 = Math.max(j8, this.f10816n[D]);
            if ((this.f10815m[D] & 1) != 0) {
                break;
            }
            D--;
            if (D == -1) {
                D = this.f10811i - 1;
            }
        }
        return j8;
    }

    private int D(int i8) {
        int i9 = this.f10819r + i8;
        int i10 = this.f10811i;
        return i9 < i10 ? i9 : i9 - i10;
    }

    private boolean H() {
        return this.f10820s != this.f10818p;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void L(c cVar) {
        cVar.f10832b.release();
    }

    private boolean M(int i8) {
        DrmSession drmSession = this.f10810h;
        return drmSession == null || drmSession.getState() == 4 || ((this.f10815m[i8] & 1073741824) == 0 && this.f10810h.e());
    }

    private void O(w0 w0Var, i4.s sVar) {
        w0 w0Var2 = this.f10809g;
        boolean z4 = w0Var2 == null;
        DrmInitData drmInitData = z4 ? null : w0Var2.q;
        this.f10809g = w0Var;
        DrmInitData drmInitData2 = w0Var.q;
        com.google.android.exoplayer2.drm.i iVar = this.f10806d;
        sVar.f20512b = iVar != null ? w0Var.c(iVar.c(w0Var)) : w0Var;
        sVar.f20511a = this.f10810h;
        if (this.f10806d == null) {
            return;
        }
        if (z4 || !l0.c(drmInitData, drmInitData2)) {
            DrmSession drmSession = this.f10810h;
            DrmSession d8 = this.f10806d.d(this.f10807e, w0Var);
            this.f10810h = d8;
            sVar.f20511a = d8;
            if (drmSession != null) {
                drmSession.b(this.f10807e);
            }
        }
    }

    private synchronized int P(i4.s sVar, DecoderInputBuffer decoderInputBuffer, boolean z4, boolean z8, b bVar) {
        decoderInputBuffer.f9513d = false;
        if (!H()) {
            if (!z8 && !this.f10824w) {
                w0 w0Var = this.B;
                if (w0Var == null || (!z4 && w0Var == this.f10809g)) {
                    return -3;
                }
                O((w0) b6.a.e(w0Var), sVar);
                return -5;
            }
            decoderInputBuffer.x(4);
            return -4;
        }
        w0 w0Var2 = this.f10805c.e(C()).f10831a;
        if (!z4 && w0Var2 == this.f10809g) {
            int D = D(this.f10820s);
            if (!M(D)) {
                decoderInputBuffer.f9513d = true;
                return -3;
            }
            decoderInputBuffer.x(this.f10815m[D]);
            if (this.f10820s == this.f10818p - 1 && (z8 || this.f10824w)) {
                decoderInputBuffer.j(536870912);
            }
            long j8 = this.f10816n[D];
            decoderInputBuffer.f9514e = j8;
            if (j8 < this.f10821t) {
                decoderInputBuffer.j(Integer.MIN_VALUE);
            }
            bVar.f10828a = this.f10814l[D];
            bVar.f10829b = this.f10813k[D];
            bVar.f10830c = this.f10817o[D];
            return -4;
        }
        O(w0Var2, sVar);
        return -5;
    }

    private void U() {
        DrmSession drmSession = this.f10810h;
        if (drmSession != null) {
            drmSession.b(this.f10807e);
            this.f10810h = null;
            this.f10809g = null;
        }
    }

    private synchronized void X() {
        this.f10820s = 0;
        this.f10803a.o();
    }

    private synchronized boolean c0(w0 w0Var) {
        this.f10826y = false;
        if (l0.c(w0Var, this.B)) {
            return false;
        }
        if (!this.f10805c.g() && this.f10805c.f().f10831a.equals(w0Var)) {
            w0Var = this.f10805c.f().f10831a;
        }
        this.B = w0Var;
        w0 w0Var2 = this.B;
        this.D = b6.t.a(w0Var2.f11207m, w0Var2.f11204j);
        this.E = false;
        return true;
    }

    private synchronized boolean h(long j8) {
        if (this.f10818p == 0) {
            return j8 > this.f10822u;
        } else if (A() >= j8) {
            return false;
        } else {
            t(this.q + j(j8));
            return true;
        }
    }

    private synchronized void i(long j8, int i8, long j9, int i9, b0.a aVar) {
        int i10 = this.f10818p;
        if (i10 > 0) {
            int D = D(i10 - 1);
            b6.a.a(this.f10813k[D] + ((long) this.f10814l[D]) <= j9);
        }
        this.f10824w = (536870912 & i8) != 0;
        this.f10823v = Math.max(this.f10823v, j8);
        int D2 = D(this.f10818p);
        this.f10816n[D2] = j8;
        this.f10813k[D2] = j9;
        this.f10814l[D2] = i9;
        this.f10815m[D2] = i8;
        this.f10817o[D2] = aVar;
        this.f10812j[D2] = this.C;
        if (this.f10805c.g() || !this.f10805c.f().f10831a.equals(this.B)) {
            com.google.android.exoplayer2.drm.i iVar = this.f10806d;
            this.f10805c.a(G(), new c((w0) b6.a.e(this.B), iVar != null ? iVar.e(this.f10807e, this.B) : i.b.f9625a));
        }
        int i11 = this.f10818p + 1;
        this.f10818p = i11;
        int i12 = this.f10811i;
        if (i11 == i12) {
            int i13 = i12 + 1000;
            int[] iArr = new int[i13];
            long[] jArr = new long[i13];
            long[] jArr2 = new long[i13];
            int[] iArr2 = new int[i13];
            int[] iArr3 = new int[i13];
            b0.a[] aVarArr = new b0.a[i13];
            int i14 = this.f10819r;
            int i15 = i12 - i14;
            System.arraycopy(this.f10813k, i14, jArr, 0, i15);
            System.arraycopy(this.f10816n, this.f10819r, jArr2, 0, i15);
            System.arraycopy(this.f10815m, this.f10819r, iArr2, 0, i15);
            System.arraycopy(this.f10814l, this.f10819r, iArr3, 0, i15);
            System.arraycopy(this.f10817o, this.f10819r, aVarArr, 0, i15);
            System.arraycopy(this.f10812j, this.f10819r, iArr, 0, i15);
            int i16 = this.f10819r;
            System.arraycopy(this.f10813k, 0, jArr, i15, i16);
            System.arraycopy(this.f10816n, 0, jArr2, i15, i16);
            System.arraycopy(this.f10815m, 0, iArr2, i15, i16);
            System.arraycopy(this.f10814l, 0, iArr3, i15, i16);
            System.arraycopy(this.f10817o, 0, aVarArr, i15, i16);
            System.arraycopy(this.f10812j, 0, iArr, i15, i16);
            this.f10813k = jArr;
            this.f10816n = jArr2;
            this.f10815m = iArr2;
            this.f10814l = iArr3;
            this.f10817o = aVarArr;
            this.f10812j = iArr;
            this.f10819r = 0;
            this.f10811i = i13;
        }
    }

    private int j(long j8) {
        int i8 = this.f10818p;
        int D = D(i8 - 1);
        while (i8 > this.f10820s && this.f10816n[D] >= j8) {
            i8--;
            D--;
            if (D == -1) {
                D = this.f10811i - 1;
            }
        }
        return i8;
    }

    public static v k(a6.b bVar, com.google.android.exoplayer2.drm.i iVar, h.a aVar) {
        return new v(bVar, (com.google.android.exoplayer2.drm.i) b6.a.e(iVar), (h.a) b6.a.e(aVar));
    }

    public static v l(a6.b bVar) {
        return new v(bVar, null, null);
    }

    private synchronized long m(long j8, boolean z4, boolean z8) {
        int i8;
        int i9 = this.f10818p;
        if (i9 != 0) {
            long[] jArr = this.f10816n;
            int i10 = this.f10819r;
            if (j8 >= jArr[i10]) {
                if (z8 && (i8 = this.f10820s) != i9) {
                    i9 = i8 + 1;
                }
                int v8 = v(i10, i9, j8, z4);
                if (v8 == -1) {
                    return -1L;
                }
                return p(v8);
            }
        }
        return -1L;
    }

    private synchronized long n() {
        int i8 = this.f10818p;
        if (i8 == 0) {
            return -1L;
        }
        return p(i8);
    }

    private long p(int i8) {
        int i9;
        this.f10822u = Math.max(this.f10822u, B(i8));
        this.f10818p -= i8;
        int i10 = this.q + i8;
        this.q = i10;
        int i11 = this.f10819r + i8;
        this.f10819r = i11;
        int i12 = this.f10811i;
        if (i11 >= i12) {
            this.f10819r = i11 - i12;
        }
        int i13 = this.f10820s - i8;
        this.f10820s = i13;
        if (i13 < 0) {
            this.f10820s = 0;
        }
        this.f10805c.d(i10);
        if (this.f10818p == 0) {
            int i14 = this.f10819r;
            if (i14 == 0) {
                i14 = this.f10811i;
            }
            return this.f10813k[i14 - 1] + this.f10814l[i9];
        }
        return this.f10813k[this.f10819r];
    }

    private long t(int i8) {
        int D;
        int G = G() - i8;
        boolean z4 = false;
        b6.a.a(G >= 0 && G <= this.f10818p - this.f10820s);
        int i9 = this.f10818p - G;
        this.f10818p = i9;
        this.f10823v = Math.max(this.f10822u, B(i9));
        if (G == 0 && this.f10824w) {
            z4 = true;
        }
        this.f10824w = z4;
        this.f10805c.c(i8);
        int i10 = this.f10818p;
        if (i10 != 0) {
            return this.f10813k[D(i10 - 1)] + this.f10814l[D];
        }
        return 0L;
    }

    private int v(int i8, int i9, long j8, boolean z4) {
        int i10 = -1;
        for (int i11 = 0; i11 < i9; i11++) {
            long[] jArr = this.f10816n;
            if (jArr[i8] > j8) {
                return i10;
            }
            if (!z4 || (this.f10815m[i8] & 1) != 0) {
                if (jArr[i8] == j8) {
                    return i11;
                }
                i10 = i11;
            }
            i8++;
            if (i8 == this.f10811i) {
                i8 = 0;
            }
        }
        return i10;
    }

    public final synchronized long A() {
        return Math.max(this.f10822u, B(this.f10820s));
    }

    public final int C() {
        return this.q + this.f10820s;
    }

    public final synchronized int E(long j8, boolean z4) {
        int D = D(this.f10820s);
        if (H() && j8 >= this.f10816n[D]) {
            if (j8 > this.f10823v && z4) {
                return this.f10818p - this.f10820s;
            }
            int v8 = v(D, this.f10818p - this.f10820s, j8, true);
            if (v8 == -1) {
                return 0;
            }
            return v8;
        }
        return 0;
    }

    public final synchronized w0 F() {
        return this.f10826y ? null : this.B;
    }

    public final int G() {
        return this.q + this.f10818p;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void I() {
        this.f10827z = true;
    }

    public final synchronized boolean J() {
        return this.f10824w;
    }

    public synchronized boolean K(boolean z4) {
        w0 w0Var;
        boolean z8 = true;
        if (H()) {
            if (this.f10805c.e(C()).f10831a != this.f10809g) {
                return true;
            }
            return M(D(this.f10820s));
        }
        if (!z4 && !this.f10824w && ((w0Var = this.B) == null || w0Var == this.f10809g)) {
            z8 = false;
        }
        return z8;
    }

    public void N() {
        DrmSession drmSession = this.f10810h;
        if (drmSession != null && drmSession.getState() == 1) {
            throw ((DrmSession.DrmSessionException) b6.a.e(this.f10810h.h()));
        }
    }

    public final synchronized int Q() {
        return H() ? this.f10812j[D(this.f10820s)] : this.C;
    }

    public void R() {
        r();
        U();
    }

    public int S(i4.s sVar, DecoderInputBuffer decoderInputBuffer, int i8, boolean z4) {
        int P = P(sVar, decoderInputBuffer, (i8 & 2) != 0, z4, this.f10804b);
        if (P == -4 && !decoderInputBuffer.t()) {
            boolean z8 = (i8 & 1) != 0;
            if ((i8 & 4) == 0) {
                t tVar = this.f10803a;
                b bVar = this.f10804b;
                if (z8) {
                    tVar.f(decoderInputBuffer, bVar);
                } else {
                    tVar.m(decoderInputBuffer, bVar);
                }
            }
            if (!z8) {
                this.f10820s++;
            }
        }
        return P;
    }

    public void T() {
        W(true);
        U();
    }

    public final void V() {
        W(false);
    }

    public void W(boolean z4) {
        this.f10803a.n();
        this.f10818p = 0;
        this.q = 0;
        this.f10819r = 0;
        this.f10820s = 0;
        this.f10825x = true;
        this.f10821t = Long.MIN_VALUE;
        this.f10822u = Long.MIN_VALUE;
        this.f10823v = Long.MIN_VALUE;
        this.f10824w = false;
        this.f10805c.b();
        if (z4) {
            this.A = null;
            this.B = null;
            this.f10826y = true;
        }
    }

    public final synchronized boolean Y(int i8) {
        boolean z4;
        X();
        int i9 = this.q;
        if (i8 >= i9 && i8 <= this.f10818p + i9) {
            this.f10821t = Long.MIN_VALUE;
            this.f10820s = i8 - i9;
            z4 = true;
        }
        z4 = false;
        return z4;
    }

    public final synchronized boolean Z(long j8, boolean z4) {
        X();
        int D = D(this.f10820s);
        if (H() && j8 >= this.f10816n[D] && (j8 <= this.f10823v || z4)) {
            int v8 = v(D, this.f10818p - this.f10820s, j8, true);
            if (v8 == -1) {
                return false;
            }
            this.f10821t = j8;
            this.f10820s += v8;
            return true;
        }
        return false;
    }

    @Override // n4.b0
    public final int a(a6.f fVar, int i8, boolean z4, int i9) {
        return this.f10803a.p(fVar, i8, z4);
    }

    public final void a0(long j8) {
        if (this.F != j8) {
            this.F = j8;
            I();
        }
    }

    public final void b0(long j8) {
        this.f10821t = j8;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0059  */
    @Override // n4.b0
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void d(long r12, int r14, int r15, int r16, n4.b0.a r17) {
        /*
            r11 = this;
            r8 = r11
            boolean r0 = r8.f10827z
            if (r0 == 0) goto L10
            com.google.android.exoplayer2.w0 r0 = r8.A
            java.lang.Object r0 = b6.a.h(r0)
            com.google.android.exoplayer2.w0 r0 = (com.google.android.exoplayer2.w0) r0
            r11.f(r0)
        L10:
            r0 = r14 & 1
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L18
            r3 = r2
            goto L19
        L18:
            r3 = r1
        L19:
            boolean r4 = r8.f10825x
            if (r4 == 0) goto L22
            if (r3 != 0) goto L20
            return
        L20:
            r8.f10825x = r1
        L22:
            long r4 = r8.F
            long r4 = r4 + r12
            boolean r6 = r8.D
            if (r6 == 0) goto L54
            long r6 = r8.f10821t
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 >= 0) goto L30
            return
        L30:
            if (r0 != 0) goto L54
            boolean r0 = r8.E
            if (r0 != 0) goto L50
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r6 = "Overriding unexpected non-sync sample for format: "
            r0.append(r6)
            com.google.android.exoplayer2.w0 r6 = r8.B
            r0.append(r6)
            java.lang.String r0 = r0.toString()
            java.lang.String r6 = "SampleQueue"
            b6.p.i(r6, r0)
            r8.E = r2
        L50:
            r0 = r14 | 1
            r6 = r0
            goto L55
        L54:
            r6 = r14
        L55:
            boolean r0 = r8.G
            if (r0 == 0) goto L66
            if (r3 == 0) goto L65
            boolean r0 = r11.h(r4)
            if (r0 != 0) goto L62
            goto L65
        L62:
            r8.G = r1
            goto L66
        L65:
            return
        L66:
            com.google.android.exoplayer2.source.t r0 = r8.f10803a
            long r0 = r0.e()
            r7 = r15
            long r2 = (long) r7
            long r0 = r0 - r2
            r2 = r16
            long r2 = (long) r2
            long r9 = r0 - r2
            r0 = r11
            r1 = r4
            r3 = r6
            r4 = r9
            r6 = r15
            r7 = r17
            r0.i(r1, r3, r4, r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.v.d(long, int, int, int, n4.b0$a):void");
    }

    public final void d0(d dVar) {
        this.f10808f = dVar;
    }

    @Override // n4.b0
    public final void e(b6.z zVar, int i8, int i9) {
        this.f10803a.q(zVar, i8);
    }

    public final synchronized void e0(int i8) {
        boolean z4;
        if (i8 >= 0) {
            try {
                if (this.f10820s + i8 <= this.f10818p) {
                    z4 = true;
                    b6.a.a(z4);
                    this.f10820s += i8;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        z4 = false;
        b6.a.a(z4);
        this.f10820s += i8;
    }

    @Override // n4.b0
    public final void f(w0 w0Var) {
        w0 w8 = w(w0Var);
        this.f10827z = false;
        this.A = w0Var;
        boolean c02 = c0(w8);
        d dVar = this.f10808f;
        if (dVar == null || !c02) {
            return;
        }
        dVar.a(w8);
    }

    public final void f0(int i8) {
        this.C = i8;
    }

    public final void g0() {
        this.G = true;
    }

    public synchronized long o() {
        int i8 = this.f10820s;
        if (i8 == 0) {
            return -1L;
        }
        return p(i8);
    }

    public final void q(long j8, boolean z4, boolean z8) {
        this.f10803a.b(m(j8, z4, z8));
    }

    public final void r() {
        this.f10803a.b(n());
    }

    public final void s() {
        this.f10803a.b(o());
    }

    public final void u(int i8) {
        this.f10803a.c(t(i8));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public w0 w(w0 w0Var) {
        return (this.F == 0 || w0Var.f11210t == Long.MAX_VALUE) ? w0Var : w0Var.b().k0(w0Var.f11210t + this.F).G();
    }

    public final int x() {
        return this.q;
    }

    public final synchronized long y() {
        return this.f10818p == 0 ? Long.MIN_VALUE : this.f10816n[this.f10819r];
    }

    public final synchronized long z() {
        return this.f10823v;
    }
}
