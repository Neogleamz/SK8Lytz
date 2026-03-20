package x4;

import b6.z;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.Arrays;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n implements m {
    private static final double[] q = {23.976023976023978d, 24.0d, 25.0d, 29.97002997002997d, 30.0d, 50.0d, 59.94005994005994d, 60.0d};

    /* renamed from: a  reason: collision with root package name */
    private String f23960a;

    /* renamed from: b  reason: collision with root package name */
    private n4.b0 f23961b;

    /* renamed from: c  reason: collision with root package name */
    private final k0 f23962c;

    /* renamed from: d  reason: collision with root package name */
    private final z f23963d;

    /* renamed from: e  reason: collision with root package name */
    private final u f23964e;

    /* renamed from: f  reason: collision with root package name */
    private final boolean[] f23965f;

    /* renamed from: g  reason: collision with root package name */
    private final a f23966g;

    /* renamed from: h  reason: collision with root package name */
    private long f23967h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f23968i;

    /* renamed from: j  reason: collision with root package name */
    private boolean f23969j;

    /* renamed from: k  reason: collision with root package name */
    private long f23970k;

    /* renamed from: l  reason: collision with root package name */
    private long f23971l;

    /* renamed from: m  reason: collision with root package name */
    private long f23972m;

    /* renamed from: n  reason: collision with root package name */
    private long f23973n;

    /* renamed from: o  reason: collision with root package name */
    private boolean f23974o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f23975p;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: e  reason: collision with root package name */
        private static final byte[] f23976e = {0, 0, 1};

        /* renamed from: a  reason: collision with root package name */
        private boolean f23977a;

        /* renamed from: b  reason: collision with root package name */
        public int f23978b;

        /* renamed from: c  reason: collision with root package name */
        public int f23979c;

        /* renamed from: d  reason: collision with root package name */
        public byte[] f23980d;

        public a(int i8) {
            this.f23980d = new byte[i8];
        }

        public void a(byte[] bArr, int i8, int i9) {
            if (this.f23977a) {
                int i10 = i9 - i8;
                byte[] bArr2 = this.f23980d;
                int length = bArr2.length;
                int i11 = this.f23978b;
                if (length < i11 + i10) {
                    this.f23980d = Arrays.copyOf(bArr2, (i11 + i10) * 2);
                }
                System.arraycopy(bArr, i8, this.f23980d, this.f23978b, i10);
                this.f23978b += i10;
            }
        }

        public boolean b(int i8, int i9) {
            if (this.f23977a) {
                int i10 = this.f23978b - i9;
                this.f23978b = i10;
                if (this.f23979c != 0 || i8 != 181) {
                    this.f23977a = false;
                    return true;
                }
                this.f23979c = i10;
            } else if (i8 == 179) {
                this.f23977a = true;
            }
            byte[] bArr = f23976e;
            a(bArr, 0, bArr.length);
            return false;
        }

        public void c() {
            this.f23977a = false;
            this.f23978b = 0;
            this.f23979c = 0;
        }
    }

    public n() {
        this(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(k0 k0Var) {
        z zVar;
        this.f23962c = k0Var;
        this.f23965f = new boolean[4];
        this.f23966g = new a(RecognitionOptions.ITF);
        if (k0Var != null) {
            this.f23964e = new u(178, RecognitionOptions.ITF);
            zVar = new z();
        } else {
            zVar = null;
            this.f23964e = null;
        }
        this.f23963d = zVar;
        this.f23971l = -9223372036854775807L;
        this.f23973n = -9223372036854775807L;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0075  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.util.Pair<com.google.android.exoplayer2.w0, java.lang.Long> a(x4.n.a r8, java.lang.String r9) {
        /*
            byte[] r0 = r8.f23980d
            int r1 = r8.f23978b
            byte[] r0 = java.util.Arrays.copyOf(r0, r1)
            r1 = 4
            r2 = r0[r1]
            r2 = r2 & 255(0xff, float:3.57E-43)
            r3 = 5
            r4 = r0[r3]
            r4 = r4 & 255(0xff, float:3.57E-43)
            r5 = 6
            r5 = r0[r5]
            r5 = r5 & 255(0xff, float:3.57E-43)
            int r2 = r2 << r1
            int r6 = r4 >> 4
            r2 = r2 | r6
            r4 = r4 & 15
            int r4 = r4 << 8
            r4 = r4 | r5
            r5 = 7
            r6 = r0[r5]
            r6 = r6 & 240(0xf0, float:3.36E-43)
            int r6 = r6 >> r1
            r7 = 2
            if (r6 == r7) goto L3d
            r7 = 3
            if (r6 == r7) goto L37
            if (r6 == r1) goto L31
            r1 = 1065353216(0x3f800000, float:1.0)
            goto L44
        L31:
            int r1 = r4 * 121
            float r1 = (float) r1
            int r6 = r2 * 100
            goto L42
        L37:
            int r1 = r4 * 16
            float r1 = (float) r1
            int r6 = r2 * 9
            goto L42
        L3d:
            int r1 = r4 * 4
            float r1 = (float) r1
            int r6 = r2 * 3
        L42:
            float r6 = (float) r6
            float r1 = r1 / r6
        L44:
            com.google.android.exoplayer2.w0$b r6 = new com.google.android.exoplayer2.w0$b
            r6.<init>()
            com.google.android.exoplayer2.w0$b r9 = r6.U(r9)
            java.lang.String r6 = "video/mpeg2"
            com.google.android.exoplayer2.w0$b r9 = r9.g0(r6)
            com.google.android.exoplayer2.w0$b r9 = r9.n0(r2)
            com.google.android.exoplayer2.w0$b r9 = r9.S(r4)
            com.google.android.exoplayer2.w0$b r9 = r9.c0(r1)
            java.util.List r1 = java.util.Collections.singletonList(r0)
            com.google.android.exoplayer2.w0$b r9 = r9.V(r1)
            com.google.android.exoplayer2.w0 r9 = r9.G()
            r1 = 0
            r4 = r0[r5]
            r4 = r4 & 15
            int r4 = r4 + (-1)
            if (r4 < 0) goto L9c
            double[] r5 = x4.n.q
            int r6 = r5.length
            if (r4 >= r6) goto L9c
            r1 = r5[r4]
            int r8 = r8.f23979c
            int r8 = r8 + 9
            r4 = r0[r8]
            r4 = r4 & 96
            int r3 = r4 >> 5
            r8 = r0[r8]
            r8 = r8 & 31
            if (r3 == r8) goto L95
            double r3 = (double) r3
            r5 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r3 = r3 + r5
            int r8 = r8 + 1
            double r5 = (double) r8
            double r3 = r3 / r5
            double r1 = r1 * r3
        L95:
            r3 = 4696837146684686336(0x412e848000000000, double:1000000.0)
            double r3 = r3 / r1
            long r1 = (long) r3
        L9c:
            java.lang.Long r8 = java.lang.Long.valueOf(r1)
            android.util.Pair r8 = android.util.Pair.create(r9, r8)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: x4.n.a(x4.n$a, java.lang.String):android.util.Pair");
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0142  */
    @Override // x4.m
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void b(b6.z r21) {
        /*
            Method dump skipped, instructions count: 328
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: x4.n.b(b6.z):void");
    }

    @Override // x4.m
    public void c() {
        b6.u.a(this.f23965f);
        this.f23966g.c();
        u uVar = this.f23964e;
        if (uVar != null) {
            uVar.d();
        }
        this.f23967h = 0L;
        this.f23968i = false;
        this.f23971l = -9223372036854775807L;
        this.f23973n = -9223372036854775807L;
    }

    @Override // x4.m
    public void d(n4.m mVar, i0.d dVar) {
        dVar.a();
        this.f23960a = dVar.b();
        this.f23961b = mVar.e(dVar.c(), 2);
        k0 k0Var = this.f23962c;
        if (k0Var != null) {
            k0Var.b(mVar, dVar);
        }
    }

    @Override // x4.m
    public void e() {
    }

    @Override // x4.m
    public void f(long j8, int i8) {
        this.f23971l = j8;
    }
}
