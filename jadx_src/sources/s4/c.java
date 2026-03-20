package s4;

import n4.l;
import n4.u;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c extends u {

    /* renamed from: b  reason: collision with root package name */
    private final long f22789b;

    public c(l lVar, long j8) {
        super(lVar);
        b6.a.a(lVar.getPosition() >= j8);
        this.f22789b = j8;
    }

    @Override // n4.u, n4.l
    public long b() {
        return super.b() - this.f22789b;
    }

    @Override // n4.u, n4.l
    public long e() {
        return super.e() - this.f22789b;
    }

    @Override // n4.u, n4.l
    public long getPosition() {
        return super.getPosition() - this.f22789b;
    }
}
