package y;

import android.util.Size;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f extends z0 {

    /* renamed from: a  reason: collision with root package name */
    private final Size f24295a;

    /* renamed from: b  reason: collision with root package name */
    private final Size f24296b;

    /* renamed from: c  reason: collision with root package name */
    private final Size f24297c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(Size size, Size size2, Size size3) {
        Objects.requireNonNull(size, "Null analysisSize");
        this.f24295a = size;
        Objects.requireNonNull(size2, "Null previewSize");
        this.f24296b = size2;
        Objects.requireNonNull(size3, "Null recordSize");
        this.f24297c = size3;
    }

    @Override // y.z0
    public Size b() {
        return this.f24295a;
    }

    @Override // y.z0
    public Size c() {
        return this.f24296b;
    }

    @Override // y.z0
    public Size d() {
        return this.f24297c;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof z0) {
            z0 z0Var = (z0) obj;
            return this.f24295a.equals(z0Var.b()) && this.f24296b.equals(z0Var.c()) && this.f24297c.equals(z0Var.d());
        }
        return false;
    }

    public int hashCode() {
        return ((((this.f24295a.hashCode() ^ 1000003) * 1000003) ^ this.f24296b.hashCode()) * 1000003) ^ this.f24297c.hashCode();
    }

    public String toString() {
        return "SurfaceSizeDefinition{analysisSize=" + this.f24295a + ", previewSize=" + this.f24296b + ", recordSize=" + this.f24297c + "}";
    }
}
