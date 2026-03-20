package androidx.core.util;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i {

    /* renamed from: a  reason: collision with root package name */
    private final float f4894a;

    /* renamed from: b  reason: collision with root package name */
    private final float f4895b;

    public i(float f5, float f8) {
        this.f4894a = h.c(f5, "width");
        this.f4895b = h.c(f8, "height");
    }

    public float a() {
        return this.f4895b;
    }

    public float b() {
        return this.f4894a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof i) {
            i iVar = (i) obj;
            return iVar.f4894a == this.f4894a && iVar.f4895b == this.f4895b;
        }
        return false;
    }

    public int hashCode() {
        return Float.floatToIntBits(this.f4894a) ^ Float.floatToIntBits(this.f4895b);
    }

    public String toString() {
        return this.f4894a + "x" + this.f4895b;
    }
}
