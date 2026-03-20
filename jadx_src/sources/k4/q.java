package k4;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q {

    /* renamed from: a  reason: collision with root package name */
    public final int f21009a;

    /* renamed from: b  reason: collision with root package name */
    public final float f21010b;

    public q(int i8, float f5) {
        this.f21009a = i8;
        this.f21010b = f5;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || q.class != obj.getClass()) {
            return false;
        }
        q qVar = (q) obj;
        return this.f21009a == qVar.f21009a && Float.compare(qVar.f21010b, this.f21010b) == 0;
    }

    public int hashCode() {
        return ((527 + this.f21009a) * 31) + Float.floatToIntBits(this.f21010b);
    }
}
