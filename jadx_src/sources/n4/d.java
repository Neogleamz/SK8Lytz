package n4;

import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d implements z {

    /* renamed from: a  reason: collision with root package name */
    private final long f22065a;

    /* renamed from: b  reason: collision with root package name */
    private final long f22066b;

    /* renamed from: c  reason: collision with root package name */
    private final int f22067c;

    /* renamed from: d  reason: collision with root package name */
    private final long f22068d;

    /* renamed from: e  reason: collision with root package name */
    private final int f22069e;

    /* renamed from: f  reason: collision with root package name */
    private final long f22070f;

    /* renamed from: g  reason: collision with root package name */
    private final boolean f22071g;

    public d(long j8, long j9, int i8, int i9, boolean z4) {
        long e8;
        this.f22065a = j8;
        this.f22066b = j9;
        this.f22067c = i9 == -1 ? 1 : i9;
        this.f22069e = i8;
        this.f22071g = z4;
        if (j8 == -1) {
            this.f22068d = -1L;
            e8 = -9223372036854775807L;
        } else {
            this.f22068d = j8 - j9;
            e8 = e(j8, j9, i8);
        }
        this.f22070f = e8;
    }

    private long b(long j8) {
        int i8 = this.f22067c;
        long j9 = (((j8 * this.f22069e) / 8000000) / i8) * i8;
        long j10 = this.f22068d;
        if (j10 != -1) {
            j9 = Math.min(j9, j10 - i8);
        }
        return this.f22066b + Math.max(j9, 0L);
    }

    private static long e(long j8, long j9, int i8) {
        return ((Math.max(0L, j8 - j9) * 8) * 1000000) / i8;
    }

    public long c(long j8) {
        return e(j8, this.f22066b, this.f22069e);
    }

    @Override // n4.z
    public long d() {
        return this.f22070f;
    }

    @Override // n4.z
    public boolean h() {
        return this.f22068d != -1 || this.f22071g;
    }

    @Override // n4.z
    public z.a i(long j8) {
        if (this.f22068d != -1 || this.f22071g) {
            long b9 = b(j8);
            long c9 = c(b9);
            a0 a0Var = new a0(c9, b9);
            if (this.f22068d != -1 && c9 < j8) {
                int i8 = this.f22067c;
                if (i8 + b9 < this.f22065a) {
                    long j9 = b9 + i8;
                    return new z.a(a0Var, new a0(c(j9), j9));
                }
            }
            return new z.a(a0Var);
        }
        return new z.a(new a0(0L, this.f22066b));
    }
}
