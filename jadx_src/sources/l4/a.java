package l4;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a {

    /* renamed from: a  reason: collision with root package name */
    private int f21572a;

    public final void j(int i8) {
        this.f21572a = i8 | this.f21572a;
    }

    public void k() {
        this.f21572a = 0;
    }

    public final void o(int i8) {
        this.f21572a = (~i8) & this.f21572a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean q(int i8) {
        return (this.f21572a & i8) == i8;
    }

    public final boolean r() {
        return q(268435456);
    }

    public final boolean s() {
        return q(Integer.MIN_VALUE);
    }

    public final boolean t() {
        return q(4);
    }

    public final boolean u() {
        return q(134217728);
    }

    public final boolean v() {
        return q(1);
    }

    public final boolean w() {
        return q(536870912);
    }

    public final void x(int i8) {
        this.f21572a = i8;
    }
}
