package x;

import androidx.camera.core.l1;
import java.util.Objects;
import x.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g extends z.b {

    /* renamed from: a  reason: collision with root package name */
    private final a0 f23715a;

    /* renamed from: b  reason: collision with root package name */
    private final l1 f23716b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(a0 a0Var, l1 l1Var) {
        Objects.requireNonNull(a0Var, "Null processingRequest");
        this.f23715a = a0Var;
        Objects.requireNonNull(l1Var, "Null imageProxy");
        this.f23716b = l1Var;
    }

    @Override // x.z.b
    l1 a() {
        return this.f23716b;
    }

    @Override // x.z.b
    a0 b() {
        return this.f23715a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof z.b) {
            z.b bVar = (z.b) obj;
            return this.f23715a.equals(bVar.b()) && this.f23716b.equals(bVar.a());
        }
        return false;
    }

    public int hashCode() {
        return ((this.f23715a.hashCode() ^ 1000003) * 1000003) ^ this.f23716b.hashCode();
    }

    public String toString() {
        return "InputPacket{processingRequest=" + this.f23715a + ", imageProxy=" + this.f23716b + "}";
    }
}
