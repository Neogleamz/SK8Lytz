package x4;

import b6.l0;
import b6.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class y {

    /* renamed from: c  reason: collision with root package name */
    private boolean f24144c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f24145d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f24146e;

    /* renamed from: a  reason: collision with root package name */
    private final b6.h0 f24142a = new b6.h0(0);

    /* renamed from: f  reason: collision with root package name */
    private long f24147f = -9223372036854775807L;

    /* renamed from: g  reason: collision with root package name */
    private long f24148g = -9223372036854775807L;

    /* renamed from: h  reason: collision with root package name */
    private long f24149h = -9223372036854775807L;

    /* renamed from: b  reason: collision with root package name */
    private final z f24143b = new z();

    private static boolean a(byte[] bArr) {
        return (bArr[0] & 196) == 68 && (bArr[2] & 4) == 4 && (bArr[4] & 4) == 4 && (bArr[5] & 1) == 1 && (bArr[8] & 3) == 3;
    }

    private int b(n4.l lVar) {
        this.f24143b.R(l0.f8068f);
        this.f24144c = true;
        lVar.h();
        return 0;
    }

    private int f(byte[] bArr, int i8) {
        return (bArr[i8 + 3] & 255) | ((bArr[i8] & 255) << 24) | ((bArr[i8 + 1] & 255) << 16) | ((bArr[i8 + 2] & 255) << 8);
    }

    private int h(n4.l lVar, n4.y yVar) {
        int min = (int) Math.min(20000L, lVar.b());
        long j8 = 0;
        if (lVar.getPosition() != j8) {
            yVar.f22152a = j8;
            return 1;
        }
        this.f24143b.Q(min);
        lVar.h();
        lVar.k(this.f24143b.e(), 0, min);
        this.f24147f = i(this.f24143b);
        this.f24145d = true;
        return 0;
    }

    private long i(z zVar) {
        int g8 = zVar.g();
        for (int f5 = zVar.f(); f5 < g8 - 3; f5++) {
            if (f(zVar.e(), f5) == 442) {
                zVar.U(f5 + 4);
                long l8 = l(zVar);
                if (l8 != -9223372036854775807L) {
                    return l8;
                }
            }
        }
        return -9223372036854775807L;
    }

    private int j(n4.l lVar, n4.y yVar) {
        long b9 = lVar.b();
        int min = (int) Math.min(20000L, b9);
        long j8 = b9 - min;
        if (lVar.getPosition() != j8) {
            yVar.f22152a = j8;
            return 1;
        }
        this.f24143b.Q(min);
        lVar.h();
        lVar.k(this.f24143b.e(), 0, min);
        this.f24148g = k(this.f24143b);
        this.f24146e = true;
        return 0;
    }

    private long k(z zVar) {
        int f5 = zVar.f();
        for (int g8 = zVar.g() - 4; g8 >= f5; g8--) {
            if (f(zVar.e(), g8) == 442) {
                zVar.U(g8 + 4);
                long l8 = l(zVar);
                if (l8 != -9223372036854775807L) {
                    return l8;
                }
            }
        }
        return -9223372036854775807L;
    }

    public static long l(z zVar) {
        int f5 = zVar.f();
        if (zVar.a() < 9) {
            return -9223372036854775807L;
        }
        byte[] bArr = new byte[9];
        zVar.l(bArr, 0, 9);
        zVar.U(f5);
        if (a(bArr)) {
            return m(bArr);
        }
        return -9223372036854775807L;
    }

    private static long m(byte[] bArr) {
        return (((bArr[0] & 56) >> 3) << 30) | ((bArr[0] & 3) << 28) | ((bArr[1] & 255) << 20) | (((bArr[2] & 248) >> 3) << 15) | ((bArr[2] & 3) << 13) | ((bArr[3] & 255) << 5) | ((bArr[4] & 248) >> 3);
    }

    public long c() {
        return this.f24149h;
    }

    public b6.h0 d() {
        return this.f24142a;
    }

    public boolean e() {
        return this.f24144c;
    }

    public int g(n4.l lVar, n4.y yVar) {
        if (this.f24146e) {
            if (this.f24148g == -9223372036854775807L) {
                return b(lVar);
            }
            if (this.f24145d) {
                long j8 = this.f24147f;
                if (j8 == -9223372036854775807L) {
                    return b(lVar);
                }
                long b9 = this.f24142a.b(this.f24148g) - this.f24142a.b(j8);
                this.f24149h = b9;
                if (b9 < 0) {
                    b6.p.i("PsDurationReader", "Invalid duration: " + this.f24149h + ". Using TIME_UNSET instead.");
                    this.f24149h = -9223372036854775807L;
                }
                return b(lVar);
            }
            return h(lVar, yVar);
        }
        return j(lVar, yVar);
    }
}
