package v;

import y.t0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {

    /* renamed from: a  reason: collision with root package name */
    private final boolean f23116a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f23117b;

    public b(t0 t0Var) {
        this.f23116a = t0Var.a(u.s.class);
        this.f23117b = u.l.a(u.k.class) != null;
    }

    public int a(int i8) {
        if ((this.f23116a || this.f23117b) && i8 == 2) {
            return 1;
        }
        return i8;
    }
}
