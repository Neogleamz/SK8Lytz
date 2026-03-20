package y4;

import b6.l0;
import n4.a0;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e implements z {

    /* renamed from: a  reason: collision with root package name */
    private final c f24417a;

    /* renamed from: b  reason: collision with root package name */
    private final int f24418b;

    /* renamed from: c  reason: collision with root package name */
    private final long f24419c;

    /* renamed from: d  reason: collision with root package name */
    private final long f24420d;

    /* renamed from: e  reason: collision with root package name */
    private final long f24421e;

    public e(c cVar, int i8, long j8, long j9) {
        this.f24417a = cVar;
        this.f24418b = i8;
        this.f24419c = j8;
        long j10 = (j9 - j8) / cVar.f24412e;
        this.f24420d = j10;
        this.f24421e = b(j10);
    }

    private long b(long j8) {
        return l0.O0(j8 * this.f24418b, 1000000L, this.f24417a.f24410c);
    }

    @Override // n4.z
    public long d() {
        return this.f24421e;
    }

    @Override // n4.z
    public boolean h() {
        return true;
    }

    @Override // n4.z
    public z.a i(long j8) {
        long r4 = l0.r((this.f24417a.f24410c * j8) / (this.f24418b * 1000000), 0L, this.f24420d - 1);
        long j9 = this.f24419c + (this.f24417a.f24412e * r4);
        long b9 = b(r4);
        a0 a0Var = new a0(b9, j9);
        if (b9 >= j8 || r4 == this.f24420d - 1) {
            return new z.a(a0Var);
        }
        long j10 = r4 + 1;
        return new z.a(a0Var, new a0(b(j10), this.f24419c + (this.f24417a.f24412e * j10)));
    }
}
