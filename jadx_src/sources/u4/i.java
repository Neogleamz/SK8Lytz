package u4;

import b6.l0;
import b6.p;
import b6.z;
import k4.u;
import n4.a0;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class i implements g {

    /* renamed from: a  reason: collision with root package name */
    private final long f23022a;

    /* renamed from: b  reason: collision with root package name */
    private final int f23023b;

    /* renamed from: c  reason: collision with root package name */
    private final long f23024c;

    /* renamed from: d  reason: collision with root package name */
    private final long f23025d;

    /* renamed from: e  reason: collision with root package name */
    private final long f23026e;

    /* renamed from: f  reason: collision with root package name */
    private final long[] f23027f;

    private i(long j8, int i8, long j9) {
        this(j8, i8, j9, -1L, null);
    }

    private i(long j8, int i8, long j9, long j10, long[] jArr) {
        this.f23022a = j8;
        this.f23023b = i8;
        this.f23024c = j9;
        this.f23027f = jArr;
        this.f23025d = j10;
        this.f23026e = j10 != -1 ? j8 + j10 : -1L;
    }

    public static i b(long j8, long j9, u.a aVar, z zVar) {
        int L;
        int i8 = aVar.f21027g;
        int i9 = aVar.f21024d;
        int q = zVar.q();
        if ((q & 1) != 1 || (L = zVar.L()) == 0) {
            return null;
        }
        long O0 = l0.O0(L, i8 * 1000000, i9);
        if ((q & 6) != 6) {
            return new i(j9, aVar.f21023c, O0);
        }
        long J = zVar.J();
        long[] jArr = new long[100];
        for (int i10 = 0; i10 < 100; i10++) {
            jArr[i10] = zVar.H();
        }
        if (j8 != -1) {
            long j10 = j9 + J;
            if (j8 != j10) {
                p.i("XingSeeker", "XING data size mismatch: " + j8 + ", " + j10);
            }
        }
        return new i(j9, aVar.f21023c, O0, J, jArr);
    }

    private long c(int i8) {
        return (this.f23024c * i8) / 100;
    }

    @Override // u4.g
    public long a(long j8) {
        long j9 = j8 - this.f23022a;
        if (!h() || j9 <= this.f23023b) {
            return 0L;
        }
        long[] jArr = (long[]) b6.a.h(this.f23027f);
        double d8 = (j9 * 256.0d) / this.f23025d;
        int i8 = l0.i(jArr, (long) d8, true, true);
        long c9 = c(i8);
        long j10 = jArr[i8];
        int i9 = i8 + 1;
        long c10 = c(i9);
        long j11 = i8 == 99 ? 256L : jArr[i9];
        return c9 + Math.round((j10 == j11 ? 0.0d : (d8 - j10) / (j11 - j10)) * (c10 - c9));
    }

    @Override // n4.z
    public long d() {
        return this.f23024c;
    }

    @Override // u4.g
    public long f() {
        return this.f23026e;
    }

    @Override // n4.z
    public boolean h() {
        return this.f23027f != null;
    }

    @Override // n4.z
    public z.a i(long j8) {
        long[] jArr;
        if (h()) {
            long r4 = l0.r(j8, 0L, this.f23024c);
            double d8 = (r4 * 100.0d) / this.f23024c;
            double d9 = 0.0d;
            if (d8 > 0.0d) {
                if (d8 >= 100.0d) {
                    d9 = 256.0d;
                } else {
                    int i8 = (int) d8;
                    double d10 = ((long[]) b6.a.h(this.f23027f))[i8];
                    d9 = d10 + ((d8 - i8) * ((i8 == 99 ? 256.0d : jArr[i8 + 1]) - d10));
                }
            }
            return new z.a(new a0(r4, this.f23022a + l0.r(Math.round((d9 / 256.0d) * this.f23025d), this.f23023b, this.f23025d - 1)));
        }
        return new z.a(new a0(0L, this.f23022a + this.f23023b));
    }
}
