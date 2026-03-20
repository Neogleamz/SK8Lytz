package k5;

import l5.i;
import n4.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g implements e {

    /* renamed from: a  reason: collision with root package name */
    private final c f21032a;

    /* renamed from: b  reason: collision with root package name */
    private final long f21033b;

    public g(c cVar, long j8) {
        this.f21032a = cVar;
        this.f21033b = j8;
    }

    @Override // k5.e
    public long a(long j8) {
        return this.f21032a.f22056e[(int) j8] - this.f21033b;
    }

    @Override // k5.e
    public long b(long j8, long j9) {
        return this.f21032a.f22055d[(int) j8];
    }

    @Override // k5.e
    public long c(long j8, long j9) {
        return 0L;
    }

    @Override // k5.e
    public long d(long j8, long j9) {
        return -9223372036854775807L;
    }

    @Override // k5.e
    public i e(long j8) {
        c cVar = this.f21032a;
        int i8 = (int) j8;
        return new i(null, cVar.f22054c[i8], cVar.f22053b[i8]);
    }

    @Override // k5.e
    public long f(long j8, long j9) {
        return this.f21032a.b(j8 + this.f21033b);
    }

    @Override // k5.e
    public boolean g() {
        return true;
    }

    @Override // k5.e
    public long h() {
        return 0L;
    }

    @Override // k5.e
    public long i(long j8) {
        return this.f21032a.f22052a;
    }

    @Override // k5.e
    public long j(long j8, long j9) {
        return this.f21032a.f22052a;
    }
}
