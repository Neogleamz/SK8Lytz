package v4;

import android.util.Pair;
import android.util.SparseArray;
import b6.h0;
import b6.l0;
import b6.u;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import n4.b0;
import n4.v;
import n4.y;
import v4.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g implements n4.k {
    public static final n4.p I = f.b;
    private static final byte[] J = {-94, 57, 79, 82, 90, -101, 79, 20, -94, 68, 108, 66, 124, 100, -115, -12};
    private static final w0 K = new w0.b().g0("application/x-emsg").G();
    private int A;
    private int B;
    private int C;
    private boolean D;
    private n4.m E;
    private b0[] F;
    private b0[] G;
    private boolean H;

    /* renamed from: a  reason: collision with root package name */
    private final int f23217a;

    /* renamed from: b  reason: collision with root package name */
    private final o f23218b;

    /* renamed from: c  reason: collision with root package name */
    private final List<w0> f23219c;

    /* renamed from: d  reason: collision with root package name */
    private final SparseArray<b> f23220d;

    /* renamed from: e  reason: collision with root package name */
    private final z f23221e;

    /* renamed from: f  reason: collision with root package name */
    private final z f23222f;

    /* renamed from: g  reason: collision with root package name */
    private final z f23223g;

    /* renamed from: h  reason: collision with root package name */
    private final byte[] f23224h;

    /* renamed from: i  reason: collision with root package name */
    private final z f23225i;

    /* renamed from: j  reason: collision with root package name */
    private final h0 f23226j;

    /* renamed from: k  reason: collision with root package name */
    private final c5.b f23227k;

    /* renamed from: l  reason: collision with root package name */
    private final z f23228l;

    /* renamed from: m  reason: collision with root package name */
    private final ArrayDeque<a.C0216a> f23229m;

    /* renamed from: n  reason: collision with root package name */
    private final ArrayDeque<a> f23230n;

    /* renamed from: o  reason: collision with root package name */
    private final b0 f23231o;

    /* renamed from: p  reason: collision with root package name */
    private int f23232p;
    private int q;

    /* renamed from: r  reason: collision with root package name */
    private long f23233r;

    /* renamed from: s  reason: collision with root package name */
    private int f23234s;

    /* renamed from: t  reason: collision with root package name */
    private z f23235t;

    /* renamed from: u  reason: collision with root package name */
    private long f23236u;

    /* renamed from: v  reason: collision with root package name */
    private int f23237v;

    /* renamed from: w  reason: collision with root package name */
    private long f23238w;

    /* renamed from: x  reason: collision with root package name */
    private long f23239x;

    /* renamed from: y  reason: collision with root package name */
    private long f23240y;

    /* renamed from: z  reason: collision with root package name */
    private b f23241z;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final long f23242a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f23243b;

        /* renamed from: c  reason: collision with root package name */
        public final int f23244c;

        public a(long j8, boolean z4, int i8) {
            this.f23242a = j8;
            this.f23243b = z4;
            this.f23244c = i8;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final b0 f23245a;

        /* renamed from: d  reason: collision with root package name */
        public r f23248d;

        /* renamed from: e  reason: collision with root package name */
        public c f23249e;

        /* renamed from: f  reason: collision with root package name */
        public int f23250f;

        /* renamed from: g  reason: collision with root package name */
        public int f23251g;

        /* renamed from: h  reason: collision with root package name */
        public int f23252h;

        /* renamed from: i  reason: collision with root package name */
        public int f23253i;

        /* renamed from: l  reason: collision with root package name */
        private boolean f23256l;

        /* renamed from: b  reason: collision with root package name */
        public final q f23246b = new q();

        /* renamed from: c  reason: collision with root package name */
        public final z f23247c = new z();

        /* renamed from: j  reason: collision with root package name */
        private final z f23254j = new z(1);

        /* renamed from: k  reason: collision with root package name */
        private final z f23255k = new z();

        public b(b0 b0Var, r rVar, c cVar) {
            this.f23245a = b0Var;
            this.f23248d = rVar;
            this.f23249e = cVar;
            j(rVar, cVar);
        }

        public int c() {
            int i8 = !this.f23256l ? this.f23248d.f23338g[this.f23250f] : this.f23246b.f23325k[this.f23250f] ? 1 : 0;
            return g() != null ? i8 | 1073741824 : i8;
        }

        public long d() {
            return !this.f23256l ? this.f23248d.f23334c[this.f23250f] : this.f23246b.f23321g[this.f23252h];
        }

        public long e() {
            return !this.f23256l ? this.f23248d.f23337f[this.f23250f] : this.f23246b.c(this.f23250f);
        }

        public int f() {
            return !this.f23256l ? this.f23248d.f23335d[this.f23250f] : this.f23246b.f23323i[this.f23250f];
        }

        public p g() {
            if (this.f23256l) {
                int i8 = ((c) l0.j(this.f23246b.f23315a)).f23207a;
                p pVar = this.f23246b.f23328n;
                if (pVar == null) {
                    pVar = this.f23248d.f23332a.a(i8);
                }
                if (pVar == null || !pVar.f23310a) {
                    return null;
                }
                return pVar;
            }
            return null;
        }

        public boolean h() {
            this.f23250f++;
            if (this.f23256l) {
                int i8 = this.f23251g + 1;
                this.f23251g = i8;
                int[] iArr = this.f23246b.f23322h;
                int i9 = this.f23252h;
                if (i8 == iArr[i9]) {
                    this.f23252h = i9 + 1;
                    this.f23251g = 0;
                    return false;
                }
                return true;
            }
            return false;
        }

        public int i(int i8, int i9) {
            z zVar;
            p g8 = g();
            if (g8 == null) {
                return 0;
            }
            int i10 = g8.f23313d;
            if (i10 != 0) {
                zVar = this.f23246b.f23329o;
            } else {
                byte[] bArr = (byte[]) l0.j(g8.f23314e);
                this.f23255k.S(bArr, bArr.length);
                z zVar2 = this.f23255k;
                i10 = bArr.length;
                zVar = zVar2;
            }
            boolean g9 = this.f23246b.g(this.f23250f);
            boolean z4 = g9 || i9 != 0;
            this.f23254j.e()[0] = (byte) ((z4 ? RecognitionOptions.ITF : 0) | i10);
            this.f23254j.U(0);
            this.f23245a.e(this.f23254j, 1, 1);
            this.f23245a.e(zVar, i10, 1);
            if (z4) {
                if (!g9) {
                    this.f23247c.Q(8);
                    byte[] e8 = this.f23247c.e();
                    e8[0] = 0;
                    e8[1] = 1;
                    e8[2] = (byte) ((i9 >> 8) & 255);
                    e8[3] = (byte) (i9 & 255);
                    e8[4] = (byte) ((i8 >> 24) & 255);
                    e8[5] = (byte) ((i8 >> 16) & 255);
                    e8[6] = (byte) ((i8 >> 8) & 255);
                    e8[7] = (byte) (i8 & 255);
                    this.f23245a.e(this.f23247c, 8, 1);
                    return i10 + 1 + 8;
                }
                z zVar3 = this.f23246b.f23329o;
                int N = zVar3.N();
                zVar3.V(-2);
                int i11 = (N * 6) + 2;
                if (i9 != 0) {
                    this.f23247c.Q(i11);
                    byte[] e9 = this.f23247c.e();
                    zVar3.l(e9, 0, i11);
                    int i12 = (((e9[2] & 255) << 8) | (e9[3] & 255)) + i9;
                    e9[2] = (byte) ((i12 >> 8) & 255);
                    e9[3] = (byte) (i12 & 255);
                    zVar3 = this.f23247c;
                }
                this.f23245a.e(zVar3, i11, 1);
                return i10 + 1 + i11;
            }
            return i10 + 1;
        }

        public void j(r rVar, c cVar) {
            this.f23248d = rVar;
            this.f23249e = cVar;
            this.f23245a.f(rVar.f23332a.f23304f);
            k();
        }

        public void k() {
            this.f23246b.f();
            this.f23250f = 0;
            this.f23252h = 0;
            this.f23251g = 0;
            this.f23253i = 0;
            this.f23256l = false;
        }

        public void l(long j8) {
            int i8 = this.f23250f;
            while (true) {
                q qVar = this.f23246b;
                if (i8 >= qVar.f23320f || qVar.c(i8) > j8) {
                    return;
                }
                if (this.f23246b.f23325k[i8]) {
                    this.f23253i = i8;
                }
                i8++;
            }
        }

        public void m() {
            p g8 = g();
            if (g8 == null) {
                return;
            }
            z zVar = this.f23246b.f23329o;
            int i8 = g8.f23313d;
            if (i8 != 0) {
                zVar.V(i8);
            }
            if (this.f23246b.g(this.f23250f)) {
                zVar.V(zVar.N() * 6);
            }
        }

        public void n(DrmInitData drmInitData) {
            p a9 = this.f23248d.f23332a.a(((c) l0.j(this.f23246b.f23315a)).f23207a);
            this.f23245a.f(this.f23248d.f23332a.f23304f.b().O(drmInitData.c(a9 != null ? a9.f23311b : null)).G());
        }
    }

    public g() {
        this(0);
    }

    public g(int i8) {
        this(i8, null);
    }

    public g(int i8, h0 h0Var) {
        this(i8, h0Var, null, Collections.emptyList());
    }

    public g(int i8, h0 h0Var, o oVar) {
        this(i8, h0Var, oVar, Collections.emptyList());
    }

    public g(int i8, h0 h0Var, o oVar, List<w0> list) {
        this(i8, h0Var, oVar, list, null);
    }

    public g(int i8, h0 h0Var, o oVar, List<w0> list, b0 b0Var) {
        this.f23217a = i8;
        this.f23226j = h0Var;
        this.f23218b = oVar;
        this.f23219c = Collections.unmodifiableList(list);
        this.f23231o = b0Var;
        this.f23227k = new c5.b();
        this.f23228l = new z(16);
        this.f23221e = new z(u.f8109a);
        this.f23222f = new z(5);
        this.f23223g = new z();
        byte[] bArr = new byte[16];
        this.f23224h = bArr;
        this.f23225i = new z(bArr);
        this.f23229m = new ArrayDeque<>();
        this.f23230n = new ArrayDeque<>();
        this.f23220d = new SparseArray<>();
        this.f23239x = -9223372036854775807L;
        this.f23238w = -9223372036854775807L;
        this.f23240y = -9223372036854775807L;
        this.E = n4.m.S;
        this.F = new b0[0];
        this.G = new b0[0];
    }

    private static void A(z zVar, q qVar) {
        z(zVar, 0, qVar);
    }

    private static Pair<Long, n4.c> B(z zVar, long j8) {
        long M;
        long M2;
        int[] iArr;
        zVar.U(8);
        int c9 = v4.a.c(zVar.q());
        zVar.V(4);
        long J2 = zVar.J();
        if (c9 == 0) {
            M = zVar.J();
            M2 = zVar.J();
        } else {
            M = zVar.M();
            M2 = zVar.M();
        }
        long j9 = M;
        long j10 = j8 + M2;
        long O0 = l0.O0(j9, 1000000L, J2);
        zVar.V(2);
        int N = zVar.N();
        int[] iArr2 = new int[N];
        long[] jArr = new long[N];
        long[] jArr2 = new long[N];
        long[] jArr3 = new long[N];
        long j11 = O0;
        int i8 = 0;
        long j12 = j9;
        while (i8 < N) {
            int q = zVar.q();
            if ((q & Integer.MIN_VALUE) != 0) {
                throw ParserException.a("Unhandled indirect reference", null);
            }
            long J3 = zVar.J();
            iArr2[i8] = q & Integer.MAX_VALUE;
            jArr[i8] = j10;
            jArr3[i8] = j11;
            long j13 = j12 + J3;
            long[] jArr4 = jArr2;
            long[] jArr5 = jArr3;
            int i9 = N;
            long O02 = l0.O0(j13, 1000000L, J2);
            jArr4[i8] = O02 - jArr5[i8];
            zVar.V(4);
            j10 += iArr[i8];
            i8++;
            iArr2 = iArr2;
            jArr3 = jArr5;
            jArr2 = jArr4;
            jArr = jArr;
            N = i9;
            j12 = j13;
            j11 = O02;
        }
        return Pair.create(Long.valueOf(O0), new n4.c(iArr2, jArr, jArr2, jArr3));
    }

    private static long C(z zVar) {
        zVar.U(8);
        return v4.a.c(zVar.q()) == 1 ? zVar.M() : zVar.J();
    }

    private static b D(z zVar, SparseArray<b> sparseArray, boolean z4) {
        zVar.U(8);
        int b9 = v4.a.b(zVar.q());
        b valueAt = z4 ? sparseArray.valueAt(0) : sparseArray.get(zVar.q());
        if (valueAt == null) {
            return null;
        }
        if ((b9 & 1) != 0) {
            long M = zVar.M();
            q qVar = valueAt.f23246b;
            qVar.f23317c = M;
            qVar.f23318d = M;
        }
        c cVar = valueAt.f23249e;
        valueAt.f23246b.f23315a = new c((b9 & 2) != 0 ? zVar.q() - 1 : cVar.f23207a, (b9 & 8) != 0 ? zVar.q() : cVar.f23208b, (b9 & 16) != 0 ? zVar.q() : cVar.f23209c, (b9 & 32) != 0 ? zVar.q() : cVar.f23210d);
        return valueAt;
    }

    private static void E(a.C0216a c0216a, SparseArray<b> sparseArray, boolean z4, int i8, byte[] bArr) {
        b D = D(((a.b) b6.a.e(c0216a.g(1952868452))).f23177b, sparseArray, z4);
        if (D == null) {
            return;
        }
        q qVar = D.f23246b;
        long j8 = qVar.q;
        boolean z8 = qVar.f23331r;
        D.k();
        D.f23256l = true;
        a.b g8 = c0216a.g(1952867444);
        if (g8 == null || (i8 & 2) != 0) {
            qVar.q = j8;
            qVar.f23331r = z8;
        } else {
            qVar.q = C(g8.f23177b);
            qVar.f23331r = true;
        }
        H(c0216a, D, i8);
        p a9 = D.f23248d.f23332a.a(((c) b6.a.e(qVar.f23315a)).f23207a);
        a.b g9 = c0216a.g(1935763834);
        if (g9 != null) {
            x((p) b6.a.e(a9), g9.f23177b, qVar);
        }
        a.b g10 = c0216a.g(1935763823);
        if (g10 != null) {
            w(g10.f23177b, qVar);
        }
        a.b g11 = c0216a.g(1936027235);
        if (g11 != null) {
            A(g11.f23177b, qVar);
        }
        y(c0216a, a9 != null ? a9.f23311b : null, qVar);
        int size = c0216a.f23175c.size();
        for (int i9 = 0; i9 < size; i9++) {
            a.b bVar = c0216a.f23175c.get(i9);
            if (bVar.f23173a == 1970628964) {
                I(bVar.f23177b, qVar, bArr);
            }
        }
    }

    private static Pair<Integer, c> F(z zVar) {
        zVar.U(12);
        return Pair.create(Integer.valueOf(zVar.q()), new c(zVar.q() - 1, zVar.q(), zVar.q(), zVar.q()));
    }

    private static int G(b bVar, int i8, int i9, z zVar, int i10) {
        boolean z4;
        int i11;
        boolean z8;
        int i12;
        boolean z9;
        boolean z10;
        boolean z11;
        int i13;
        b bVar2 = bVar;
        zVar.U(8);
        int b9 = v4.a.b(zVar.q());
        o oVar = bVar2.f23248d.f23332a;
        q qVar = bVar2.f23246b;
        c cVar = (c) l0.j(qVar.f23315a);
        qVar.f23322h[i8] = zVar.L();
        long[] jArr = qVar.f23321g;
        jArr[i8] = qVar.f23317c;
        if ((b9 & 1) != 0) {
            jArr[i8] = jArr[i8] + zVar.q();
        }
        boolean z12 = (b9 & 4) != 0;
        int i14 = cVar.f23210d;
        if (z12) {
            i14 = zVar.q();
        }
        boolean z13 = (b9 & RecognitionOptions.QR_CODE) != 0;
        boolean z14 = (b9 & RecognitionOptions.UPC_A) != 0;
        boolean z15 = (b9 & RecognitionOptions.UPC_E) != 0;
        boolean z16 = (b9 & RecognitionOptions.PDF417) != 0;
        long j8 = l(oVar) ? ((long[]) l0.j(oVar.f23307i))[0] : 0L;
        int[] iArr = qVar.f23323i;
        long[] jArr2 = qVar.f23324j;
        boolean[] zArr = qVar.f23325k;
        int i15 = i14;
        boolean z17 = oVar.f23300b == 2 && (i9 & 1) != 0;
        int i16 = i10 + qVar.f23322h[i8];
        boolean z18 = z17;
        long j9 = oVar.f23301c;
        long j10 = qVar.q;
        int i17 = i10;
        while (i17 < i16) {
            int d8 = d(z13 ? zVar.q() : cVar.f23208b);
            if (z14) {
                i11 = zVar.q();
                z4 = z13;
            } else {
                z4 = z13;
                i11 = cVar.f23209c;
            }
            int d9 = d(i11);
            if (z15) {
                z8 = z12;
                i12 = zVar.q();
            } else if (i17 == 0 && z12) {
                z8 = z12;
                i12 = i15;
            } else {
                z8 = z12;
                i12 = cVar.f23210d;
            }
            if (z16) {
                z9 = z16;
                z10 = z14;
                z11 = z15;
                i13 = zVar.q();
            } else {
                z9 = z16;
                z10 = z14;
                z11 = z15;
                i13 = 0;
            }
            jArr2[i17] = l0.O0((i13 + j10) - j8, 1000000L, j9);
            if (!qVar.f23331r) {
                jArr2[i17] = jArr2[i17] + bVar2.f23248d.f23339h;
            }
            iArr[i17] = d9;
            zArr[i17] = ((i12 >> 16) & 1) == 0 && (!z18 || i17 == 0);
            j10 += d8;
            i17++;
            bVar2 = bVar;
            z13 = z4;
            z12 = z8;
            z16 = z9;
            z14 = z10;
            z15 = z11;
        }
        qVar.q = j10;
        return i16;
    }

    private static void H(a.C0216a c0216a, b bVar, int i8) {
        List<a.b> list = c0216a.f23175c;
        int size = list.size();
        int i9 = 0;
        int i10 = 0;
        for (int i11 = 0; i11 < size; i11++) {
            a.b bVar2 = list.get(i11);
            if (bVar2.f23173a == 1953658222) {
                z zVar = bVar2.f23177b;
                zVar.U(12);
                int L = zVar.L();
                if (L > 0) {
                    i10 += L;
                    i9++;
                }
            }
        }
        bVar.f23252h = 0;
        bVar.f23251g = 0;
        bVar.f23250f = 0;
        bVar.f23246b.e(i9, i10);
        int i12 = 0;
        int i13 = 0;
        for (int i14 = 0; i14 < size; i14++) {
            a.b bVar3 = list.get(i14);
            if (bVar3.f23173a == 1953658222) {
                i13 = G(bVar, i12, i8, bVar3.f23177b, i13);
                i12++;
            }
        }
    }

    private static void I(z zVar, q qVar, byte[] bArr) {
        zVar.U(8);
        zVar.l(bArr, 0, 16);
        if (Arrays.equals(bArr, J)) {
            z(zVar, 16, qVar);
        }
    }

    private void J(long j8) {
        while (!this.f23229m.isEmpty() && this.f23229m.peek().f23174b == j8) {
            o(this.f23229m.pop());
        }
        f();
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0154  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean K(n4.l r12) {
        /*
            Method dump skipped, instructions count: 347
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: v4.g.K(n4.l):boolean");
    }

    private void L(n4.l lVar) {
        int i8 = ((int) this.f23233r) - this.f23234s;
        z zVar = this.f23235t;
        if (zVar != null) {
            lVar.readFully(zVar.e(), 8, i8);
            q(new a.b(this.q, zVar), lVar.getPosition());
        } else {
            lVar.i(i8);
        }
        J(lVar.getPosition());
    }

    private void M(n4.l lVar) {
        int size = this.f23220d.size();
        long j8 = Long.MAX_VALUE;
        b bVar = null;
        for (int i8 = 0; i8 < size; i8++) {
            q qVar = this.f23220d.valueAt(i8).f23246b;
            if (qVar.f23330p) {
                long j9 = qVar.f23318d;
                if (j9 < j8) {
                    bVar = this.f23220d.valueAt(i8);
                    j8 = j9;
                }
            }
        }
        if (bVar == null) {
            this.f23232p = 3;
            return;
        }
        int position = (int) (j8 - lVar.getPosition());
        if (position < 0) {
            throw ParserException.a("Offset to encryption data was negative.", null);
        }
        lVar.i(position);
        bVar.f23246b.b(lVar);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean N(n4.l lVar) {
        int c9;
        int i8;
        b bVar = this.f23241z;
        Throwable th = null;
        if (bVar == null) {
            bVar = j(this.f23220d);
            if (bVar == null) {
                int position = (int) (this.f23236u - lVar.getPosition());
                if (position >= 0) {
                    lVar.i(position);
                    f();
                    return false;
                }
                throw ParserException.a("Offset to end of mdat was negative.", null);
            }
            int d8 = (int) (bVar.d() - lVar.getPosition());
            if (d8 < 0) {
                b6.p.i("FragmentedMp4Extractor", "Ignoring negative offset to sample data.");
                d8 = 0;
            }
            lVar.i(d8);
            this.f23241z = bVar;
        }
        int i9 = 4;
        int i10 = 1;
        if (this.f23232p == 3) {
            int f5 = bVar.f();
            this.A = f5;
            if (bVar.f23250f < bVar.f23253i) {
                lVar.i(f5);
                bVar.m();
                if (!bVar.h()) {
                    this.f23241z = null;
                }
                this.f23232p = 3;
                return true;
            }
            if (bVar.f23248d.f23332a.f23305g == 1) {
                this.A = f5 - 8;
                lVar.i(8);
            }
            if ("audio/ac4".equals(bVar.f23248d.f23332a.f23304f.f11207m)) {
                this.B = bVar.i(this.A, 7);
                k4.c.a(this.A, this.f23225i);
                bVar.f23245a.b(this.f23225i, 7);
                i8 = this.B + 7;
            } else {
                i8 = bVar.i(this.A, 0);
            }
            this.B = i8;
            this.A += this.B;
            this.f23232p = 4;
            this.C = 0;
        }
        o oVar = bVar.f23248d.f23332a;
        b0 b0Var = bVar.f23245a;
        long e8 = bVar.e();
        h0 h0Var = this.f23226j;
        if (h0Var != null) {
            e8 = h0Var.a(e8);
        }
        long j8 = e8;
        if (oVar.f23308j == 0) {
            while (true) {
                int i11 = this.B;
                int i12 = this.A;
                if (i11 >= i12) {
                    break;
                }
                this.B += b0Var.c(lVar, i12 - i11, false);
            }
        } else {
            byte[] e9 = this.f23222f.e();
            e9[0] = 0;
            e9[1] = 0;
            e9[2] = 0;
            int i13 = oVar.f23308j;
            int i14 = i13 + 1;
            int i15 = 4 - i13;
            while (this.B < this.A) {
                int i16 = this.C;
                if (i16 == 0) {
                    lVar.readFully(e9, i15, i14);
                    this.f23222f.U(0);
                    int q = this.f23222f.q();
                    if (q < i10) {
                        throw ParserException.a("Invalid NAL length", th);
                    }
                    this.C = q - 1;
                    this.f23221e.U(0);
                    b0Var.b(this.f23221e, i9);
                    b0Var.b(this.f23222f, i10);
                    this.D = (this.G.length <= 0 || !u.g(oVar.f23304f.f11207m, e9[i9])) ? 0 : i10;
                    this.B += 5;
                    this.A += i15;
                } else {
                    if (this.D) {
                        this.f23223g.Q(i16);
                        lVar.readFully(this.f23223g.e(), 0, this.C);
                        b0Var.b(this.f23223g, this.C);
                        c9 = this.C;
                        int q8 = u.q(this.f23223g.e(), this.f23223g.g());
                        this.f23223g.U("video/hevc".equals(oVar.f23304f.f11207m) ? 1 : 0);
                        this.f23223g.T(q8);
                        n4.b.a(j8, this.f23223g, this.G);
                    } else {
                        c9 = b0Var.c(lVar, i16, false);
                    }
                    this.B += c9;
                    this.C -= c9;
                    th = null;
                    i9 = 4;
                    i10 = 1;
                }
            }
        }
        int c10 = bVar.c();
        p g8 = bVar.g();
        b0Var.d(j8, c10, this.A, 0, g8 != null ? g8.f23312c : null);
        t(j8);
        if (!bVar.h()) {
            this.f23241z = null;
        }
        this.f23232p = 3;
        return true;
    }

    private static boolean O(int i8) {
        return i8 == 1836019574 || i8 == 1953653099 || i8 == 1835297121 || i8 == 1835626086 || i8 == 1937007212 || i8 == 1836019558 || i8 == 1953653094 || i8 == 1836475768 || i8 == 1701082227;
    }

    private static boolean P(int i8) {
        return i8 == 1751411826 || i8 == 1835296868 || i8 == 1836476516 || i8 == 1936286840 || i8 == 1937011556 || i8 == 1937011827 || i8 == 1668576371 || i8 == 1937011555 || i8 == 1937011578 || i8 == 1937013298 || i8 == 1937007471 || i8 == 1668232756 || i8 == 1937011571 || i8 == 1952867444 || i8 == 1952868452 || i8 == 1953196132 || i8 == 1953654136 || i8 == 1953658222 || i8 == 1886614376 || i8 == 1935763834 || i8 == 1935763823 || i8 == 1936027235 || i8 == 1970628964 || i8 == 1935828848 || i8 == 1936158820 || i8 == 1701606260 || i8 == 1835362404 || i8 == 1701671783;
    }

    private static int d(int i8) {
        if (i8 >= 0) {
            return i8;
        }
        throw ParserException.a("Unexpected negative value: " + i8, null);
    }

    private void f() {
        this.f23232p = 0;
        this.f23234s = 0;
    }

    private c h(SparseArray<c> sparseArray, int i8) {
        return (c) (sparseArray.size() == 1 ? sparseArray.valueAt(0) : b6.a.e(sparseArray.get(i8)));
    }

    private static DrmInitData i(List<a.b> list) {
        int size = list.size();
        ArrayList arrayList = null;
        for (int i8 = 0; i8 < size; i8++) {
            a.b bVar = list.get(i8);
            if (bVar.f23173a == 1886614376) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                byte[] e8 = bVar.f23177b.e();
                UUID f5 = l.f(e8);
                if (f5 == null) {
                    b6.p.i("FragmentedMp4Extractor", "Skipped pssh atom (failed to extract uuid)");
                } else {
                    arrayList.add(new DrmInitData.SchemeData(f5, "video/mp4", e8));
                }
            }
        }
        if (arrayList == null) {
            return null;
        }
        return new DrmInitData(arrayList);
    }

    private static b j(SparseArray<b> sparseArray) {
        int size = sparseArray.size();
        b bVar = null;
        long j8 = Long.MAX_VALUE;
        for (int i8 = 0; i8 < size; i8++) {
            b valueAt = sparseArray.valueAt(i8);
            if ((valueAt.f23256l || valueAt.f23250f != valueAt.f23248d.f23333b) && (!valueAt.f23256l || valueAt.f23252h != valueAt.f23246b.f23319e)) {
                long d8 = valueAt.d();
                if (d8 < j8) {
                    bVar = valueAt;
                    j8 = d8;
                }
            }
        }
        return bVar;
    }

    private void k() {
        int i8;
        b0[] b0VarArr = new b0[2];
        this.F = b0VarArr;
        b0 b0Var = this.f23231o;
        int i9 = 0;
        if (b0Var != null) {
            b0VarArr[0] = b0Var;
            i8 = 1;
        } else {
            i8 = 0;
        }
        int i10 = 100;
        if ((this.f23217a & 4) != 0) {
            b0VarArr[i8] = this.E.e(100, 5);
            i8++;
            i10 = 101;
        }
        b0[] b0VarArr2 = (b0[]) l0.H0(this.F, i8);
        this.F = b0VarArr2;
        for (b0 b0Var2 : b0VarArr2) {
            b0Var2.f(K);
        }
        this.G = new b0[this.f23219c.size()];
        while (i9 < this.G.length) {
            b0 e8 = this.E.e(i10, 3);
            e8.f(this.f23219c.get(i9));
            this.G[i9] = e8;
            i9++;
            i10++;
        }
    }

    private static boolean l(o oVar) {
        long[] jArr;
        long[] jArr2 = oVar.f23306h;
        if (jArr2 == null || jArr2.length != 1 || (jArr = oVar.f23307i) == null) {
            return false;
        }
        return jArr2[0] == 0 || l0.O0(jArr2[0] + jArr[0], 1000000L, oVar.f23302d) >= oVar.f23303e;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ n4.k[] m() {
        return new n4.k[]{new g()};
    }

    private void o(a.C0216a c0216a) {
        int i8 = c0216a.f23173a;
        if (i8 == 1836019574) {
            s(c0216a);
        } else if (i8 == 1836019558) {
            r(c0216a);
        } else if (this.f23229m.isEmpty()) {
        } else {
            this.f23229m.peek().d(c0216a);
        }
    }

    private void p(z zVar) {
        long O0;
        String str;
        long O02;
        String str2;
        long J2;
        long j8;
        b0[] b0VarArr;
        if (this.F.length == 0) {
            return;
        }
        zVar.U(8);
        int c9 = v4.a.c(zVar.q());
        if (c9 == 0) {
            String str3 = (String) b6.a.e(zVar.B());
            String str4 = (String) b6.a.e(zVar.B());
            long J3 = zVar.J();
            O0 = l0.O0(zVar.J(), 1000000L, J3);
            long j9 = this.f23240y;
            long j10 = j9 != -9223372036854775807L ? j9 + O0 : -9223372036854775807L;
            str = str3;
            O02 = l0.O0(zVar.J(), 1000L, J3);
            str2 = str4;
            J2 = zVar.J();
            j8 = j10;
        } else if (c9 != 1) {
            b6.p.i("FragmentedMp4Extractor", "Skipping unsupported emsg version: " + c9);
            return;
        } else {
            long J4 = zVar.J();
            j8 = l0.O0(zVar.M(), 1000000L, J4);
            long O03 = l0.O0(zVar.J(), 1000L, J4);
            long J5 = zVar.J();
            str = (String) b6.a.e(zVar.B());
            O02 = O03;
            J2 = J5;
            str2 = (String) b6.a.e(zVar.B());
            O0 = -9223372036854775807L;
        }
        byte[] bArr = new byte[zVar.a()];
        zVar.l(bArr, 0, zVar.a());
        z zVar2 = new z(this.f23227k.a(new EventMessage(str, str2, O02, J2, bArr)));
        int a9 = zVar2.a();
        for (b0 b0Var : this.F) {
            zVar2.U(0);
            b0Var.b(zVar2, a9);
        }
        if (j8 == -9223372036854775807L) {
            this.f23230n.addLast(new a(O0, true, a9));
        } else if (this.f23230n.isEmpty()) {
            h0 h0Var = this.f23226j;
            if (h0Var != null) {
                j8 = h0Var.a(j8);
            }
            for (b0 b0Var2 : this.F) {
                b0Var2.d(j8, 1, a9, 0, null);
            }
            return;
        } else {
            this.f23230n.addLast(new a(j8, false, a9));
        }
        this.f23237v += a9;
    }

    private void q(a.b bVar, long j8) {
        if (!this.f23229m.isEmpty()) {
            this.f23229m.peek().e(bVar);
            return;
        }
        int i8 = bVar.f23173a;
        if (i8 != 1936286840) {
            if (i8 == 1701671783) {
                p(bVar.f23177b);
                return;
            }
            return;
        }
        Pair<Long, n4.c> B = B(bVar.f23177b, j8);
        this.f23240y = ((Long) B.first).longValue();
        this.E.m((n4.z) B.second);
        this.H = true;
    }

    private void r(a.C0216a c0216a) {
        v(c0216a, this.f23220d, this.f23218b != null, this.f23217a, this.f23224h);
        DrmInitData i8 = i(c0216a.f23175c);
        if (i8 != null) {
            int size = this.f23220d.size();
            for (int i9 = 0; i9 < size; i9++) {
                this.f23220d.valueAt(i9).n(i8);
            }
        }
        if (this.f23238w != -9223372036854775807L) {
            int size2 = this.f23220d.size();
            for (int i10 = 0; i10 < size2; i10++) {
                this.f23220d.valueAt(i10).l(this.f23238w);
            }
            this.f23238w = -9223372036854775807L;
        }
    }

    private void s(a.C0216a c0216a) {
        int i8 = 0;
        b6.a.g(this.f23218b == null, "Unexpected moov box.");
        DrmInitData i9 = i(c0216a.f23175c);
        a.C0216a c0216a2 = (a.C0216a) b6.a.e(c0216a.f(1836475768));
        SparseArray<c> sparseArray = new SparseArray<>();
        int size = c0216a2.f23175c.size();
        long j8 = -9223372036854775807L;
        for (int i10 = 0; i10 < size; i10++) {
            a.b bVar = c0216a2.f23175c.get(i10);
            int i11 = bVar.f23173a;
            if (i11 == 1953654136) {
                Pair<Integer, c> F = F(bVar.f23177b);
                sparseArray.put(((Integer) F.first).intValue(), (c) F.second);
            } else if (i11 == 1835362404) {
                j8 = u(bVar.f23177b);
            }
        }
        List<r> A = v4.b.A(c0216a, new v(), j8, i9, (this.f23217a & 16) != 0, false, new e(this));
        int size2 = A.size();
        if (this.f23220d.size() != 0) {
            b6.a.f(this.f23220d.size() == size2);
            while (i8 < size2) {
                r rVar = A.get(i8);
                o oVar = rVar.f23332a;
                this.f23220d.get(oVar.f23299a).j(rVar, h(sparseArray, oVar.f23299a));
                i8++;
            }
            return;
        }
        while (i8 < size2) {
            r rVar2 = A.get(i8);
            o oVar2 = rVar2.f23332a;
            this.f23220d.put(oVar2.f23299a, new b(this.E.e(i8, oVar2.f23300b), rVar2, h(sparseArray, oVar2.f23299a)));
            this.f23239x = Math.max(this.f23239x, oVar2.f23303e);
            i8++;
        }
        this.E.o();
    }

    private void t(long j8) {
        while (!this.f23230n.isEmpty()) {
            a removeFirst = this.f23230n.removeFirst();
            this.f23237v -= removeFirst.f23244c;
            long j9 = removeFirst.f23242a;
            if (removeFirst.f23243b) {
                j9 += j8;
            }
            h0 h0Var = this.f23226j;
            if (h0Var != null) {
                j9 = h0Var.a(j9);
            }
            for (b0 b0Var : this.F) {
                b0Var.d(j9, 1, removeFirst.f23244c, this.f23237v, null);
            }
        }
    }

    private static long u(z zVar) {
        zVar.U(8);
        return v4.a.c(zVar.q()) == 0 ? zVar.J() : zVar.M();
    }

    private static void v(a.C0216a c0216a, SparseArray<b> sparseArray, boolean z4, int i8, byte[] bArr) {
        int size = c0216a.f23176d.size();
        for (int i9 = 0; i9 < size; i9++) {
            a.C0216a c0216a2 = c0216a.f23176d.get(i9);
            if (c0216a2.f23173a == 1953653094) {
                E(c0216a2, sparseArray, z4, i8, bArr);
            }
        }
    }

    private static void w(z zVar, q qVar) {
        zVar.U(8);
        int q = zVar.q();
        if ((v4.a.b(q) & 1) == 1) {
            zVar.V(8);
        }
        int L = zVar.L();
        if (L == 1) {
            qVar.f23318d += v4.a.c(q) == 0 ? zVar.J() : zVar.M();
            return;
        }
        throw ParserException.a("Unexpected saio entry count: " + L, null);
    }

    private static void x(p pVar, z zVar, q qVar) {
        int i8;
        int i9 = pVar.f23313d;
        zVar.U(8);
        if ((v4.a.b(zVar.q()) & 1) == 1) {
            zVar.V(8);
        }
        int H = zVar.H();
        int L = zVar.L();
        if (L > qVar.f23320f) {
            throw ParserException.a("Saiz sample count " + L + " is greater than fragment sample count" + qVar.f23320f, null);
        }
        if (H == 0) {
            boolean[] zArr = qVar.f23327m;
            i8 = 0;
            for (int i10 = 0; i10 < L; i10++) {
                int H2 = zVar.H();
                i8 += H2;
                zArr[i10] = H2 > i9;
            }
        } else {
            i8 = (H * L) + 0;
            Arrays.fill(qVar.f23327m, 0, L, H > i9);
        }
        Arrays.fill(qVar.f23327m, L, qVar.f23320f, false);
        if (i8 > 0) {
            qVar.d(i8);
        }
    }

    private static void y(a.C0216a c0216a, String str, q qVar) {
        byte[] bArr = null;
        z zVar = null;
        z zVar2 = null;
        for (int i8 = 0; i8 < c0216a.f23175c.size(); i8++) {
            a.b bVar = c0216a.f23175c.get(i8);
            z zVar3 = bVar.f23177b;
            int i9 = bVar.f23173a;
            if (i9 == 1935828848) {
                zVar3.U(12);
                if (zVar3.q() == 1936025959) {
                    zVar = zVar3;
                }
            } else if (i9 == 1936158820) {
                zVar3.U(12);
                if (zVar3.q() == 1936025959) {
                    zVar2 = zVar3;
                }
            }
        }
        if (zVar == null || zVar2 == null) {
            return;
        }
        zVar.U(8);
        int c9 = v4.a.c(zVar.q());
        zVar.V(4);
        if (c9 == 1) {
            zVar.V(4);
        }
        if (zVar.q() != 1) {
            throw ParserException.d("Entry count in sbgp != 1 (unsupported).");
        }
        zVar2.U(8);
        int c10 = v4.a.c(zVar2.q());
        zVar2.V(4);
        if (c10 == 1) {
            if (zVar2.J() == 0) {
                throw ParserException.d("Variable length description in sgpd found (unsupported)");
            }
        } else if (c10 >= 2) {
            zVar2.V(4);
        }
        if (zVar2.J() != 1) {
            throw ParserException.d("Entry count in sgpd != 1 (unsupported).");
        }
        zVar2.V(1);
        int H = zVar2.H();
        int i10 = (H & 240) >> 4;
        int i11 = H & 15;
        boolean z4 = zVar2.H() == 1;
        if (z4) {
            int H2 = zVar2.H();
            byte[] bArr2 = new byte[16];
            zVar2.l(bArr2, 0, 16);
            if (H2 == 0) {
                int H3 = zVar2.H();
                bArr = new byte[H3];
                zVar2.l(bArr, 0, H3);
            }
            qVar.f23326l = true;
            qVar.f23328n = new p(z4, str, H2, bArr2, i10, i11, bArr);
        }
    }

    private static void z(z zVar, int i8, q qVar) {
        zVar.U(i8 + 8);
        int b9 = v4.a.b(zVar.q());
        if ((b9 & 1) != 0) {
            throw ParserException.d("Overriding TrackEncryptionBox parameters is unsupported.");
        }
        boolean z4 = (b9 & 2) != 0;
        int L = zVar.L();
        if (L == 0) {
            Arrays.fill(qVar.f23327m, 0, qVar.f23320f, false);
        } else if (L == qVar.f23320f) {
            Arrays.fill(qVar.f23327m, 0, L, z4);
            qVar.d(zVar.a());
            qVar.a(zVar);
        } else {
            throw ParserException.a("Senc sample count " + L + " is different from fragment sample count" + qVar.f23320f, null);
        }
    }

    @Override // n4.k
    public void b(n4.m mVar) {
        this.E = mVar;
        f();
        k();
        o oVar = this.f23218b;
        if (oVar != null) {
            this.f23220d.put(0, new b(mVar.e(0, oVar.f23300b), new r(this.f23218b, new long[0], new int[0], 0, new long[0], new int[0], 0L), new c(0, 0, 0, 0)));
            this.E.o();
        }
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        int size = this.f23220d.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.f23220d.valueAt(i8).k();
        }
        this.f23230n.clear();
        this.f23237v = 0;
        this.f23238w = j9;
        this.f23229m.clear();
        f();
    }

    @Override // n4.k
    public int e(n4.l lVar, y yVar) {
        while (true) {
            int i8 = this.f23232p;
            if (i8 != 0) {
                if (i8 == 1) {
                    L(lVar);
                } else if (i8 == 2) {
                    M(lVar);
                } else if (N(lVar)) {
                    return 0;
                }
            } else if (!K(lVar)) {
                return -1;
            }
        }
    }

    @Override // n4.k
    public boolean g(n4.l lVar) {
        return n.b(lVar);
    }

    protected o n(o oVar) {
        return oVar;
    }

    @Override // n4.k
    public void release() {
    }
}
