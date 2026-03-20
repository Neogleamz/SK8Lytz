package v;

import y.t0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l {

    /* renamed from: a  reason: collision with root package name */
    private final boolean f23128a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f23129b = false;

    public l(t0 t0Var) {
        this.f23128a = t0Var.b(u.d.class) != null;
    }

    public void a() {
        this.f23129b = false;
    }

    public void b() {
        this.f23129b = true;
    }

    public boolean c(int i8) {
        return this.f23129b && i8 == 0 && this.f23128a;
    }
}
