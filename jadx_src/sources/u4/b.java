package u4;

import b6.l0;
import b6.q;
import n4.a0;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b implements g {

    /* renamed from: a  reason: collision with root package name */
    private final long f22990a;

    /* renamed from: b  reason: collision with root package name */
    private final q f22991b;

    /* renamed from: c  reason: collision with root package name */
    private final q f22992c;

    /* renamed from: d  reason: collision with root package name */
    private long f22993d;

    public b(long j8, long j9, long j10) {
        this.f22993d = j8;
        this.f22990a = j10;
        q qVar = new q();
        this.f22991b = qVar;
        q qVar2 = new q();
        this.f22992c = qVar2;
        qVar.a(0L);
        qVar2.a(j9);
    }

    @Override // u4.g
    public long a(long j8) {
        return this.f22991b.b(l0.f(this.f22992c, j8, true, true));
    }

    public boolean b(long j8) {
        q qVar = this.f22991b;
        return j8 - qVar.b(qVar.c() - 1) < 100000;
    }

    public void c(long j8, long j9) {
        if (b(j8)) {
            return;
        }
        this.f22991b.a(j8);
        this.f22992c.a(j9);
    }

    @Override // n4.z
    public long d() {
        return this.f22993d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(long j8) {
        this.f22993d = j8;
    }

    @Override // u4.g
    public long f() {
        return this.f22990a;
    }

    @Override // n4.z
    public boolean h() {
        return true;
    }

    @Override // n4.z
    public z.a i(long j8) {
        int f5 = l0.f(this.f22991b, j8, true, true);
        a0 a0Var = new a0(this.f22991b.b(f5), this.f22992c.b(f5));
        if (a0Var.f22046a == j8 || f5 == this.f22991b.c() - 1) {
            return new z.a(a0Var);
        }
        int i8 = f5 + 1;
        return new z.a(a0Var, new a0(this.f22991b.b(i8), this.f22992c.b(i8)));
    }
}
