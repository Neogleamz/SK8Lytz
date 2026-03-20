package u4;

import b6.l0;
import b6.p;
import b6.z;
import k4.u;
import n4.a0;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h implements g {

    /* renamed from: a  reason: collision with root package name */
    private final long[] f23018a;

    /* renamed from: b  reason: collision with root package name */
    private final long[] f23019b;

    /* renamed from: c  reason: collision with root package name */
    private final long f23020c;

    /* renamed from: d  reason: collision with root package name */
    private final long f23021d;

    private h(long[] jArr, long[] jArr2, long j8, long j9) {
        this.f23018a = jArr;
        this.f23019b = jArr2;
        this.f23020c = j8;
        this.f23021d = j9;
    }

    public static h b(long j8, long j9, u.a aVar, z zVar) {
        int H;
        zVar.V(10);
        int q = zVar.q();
        if (q <= 0) {
            return null;
        }
        int i8 = aVar.f21024d;
        long O0 = l0.O0(q, 1000000 * (i8 >= 32000 ? 1152 : 576), i8);
        int N = zVar.N();
        int N2 = zVar.N();
        int N3 = zVar.N();
        zVar.V(2);
        long j10 = j9 + aVar.f21023c;
        long[] jArr = new long[N];
        long[] jArr2 = new long[N];
        int i9 = 0;
        long j11 = j9;
        while (i9 < N) {
            int i10 = N2;
            long j12 = j10;
            jArr[i9] = (i9 * O0) / N;
            jArr2[i9] = Math.max(j11, j12);
            if (N3 == 1) {
                H = zVar.H();
            } else if (N3 == 2) {
                H = zVar.N();
            } else if (N3 == 3) {
                H = zVar.K();
            } else if (N3 != 4) {
                return null;
            } else {
                H = zVar.L();
            }
            j11 += H * i10;
            i9++;
            jArr = jArr;
            N2 = i10;
            j10 = j12;
        }
        long[] jArr3 = jArr;
        if (j8 != -1 && j8 != j11) {
            p.i("VbriSeeker", "VBRI data size mismatch: " + j8 + ", " + j11);
        }
        return new h(jArr3, jArr2, O0, j11);
    }

    @Override // u4.g
    public long a(long j8) {
        return this.f23018a[l0.i(this.f23019b, j8, true, true)];
    }

    @Override // n4.z
    public long d() {
        return this.f23020c;
    }

    @Override // u4.g
    public long f() {
        return this.f23021d;
    }

    @Override // n4.z
    public boolean h() {
        return true;
    }

    @Override // n4.z
    public z.a i(long j8) {
        int i8 = l0.i(this.f23018a, j8, true, true);
        a0 a0Var = new a0(this.f23018a[i8], this.f23019b[i8]);
        if (a0Var.f22046a >= j8 || i8 == this.f23018a.length - 1) {
            return new z.a(a0Var);
        }
        int i9 = i8 + 1;
        return new z.a(a0Var, new a0(this.f23018a[i9], this.f23019b[i9]));
    }
}
