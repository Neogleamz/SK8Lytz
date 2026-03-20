package p4;

import b6.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c implements a {

    /* renamed from: a  reason: collision with root package name */
    public final int f22345a;

    /* renamed from: b  reason: collision with root package name */
    public final int f22346b;

    /* renamed from: c  reason: collision with root package name */
    public final int f22347c;

    /* renamed from: d  reason: collision with root package name */
    public final int f22348d;

    private c(int i8, int i9, int i10, int i11) {
        this.f22345a = i8;
        this.f22346b = i9;
        this.f22347c = i10;
        this.f22348d = i11;
    }

    public static c b(z zVar) {
        int u8 = zVar.u();
        zVar.V(8);
        int u9 = zVar.u();
        int u10 = zVar.u();
        zVar.V(4);
        int u11 = zVar.u();
        zVar.V(12);
        return new c(u8, u9, u10, u11);
    }

    public boolean a() {
        return (this.f22346b & 16) == 16;
    }

    @Override // p4.a
    public int getType() {
        return 1751742049;
    }
}
