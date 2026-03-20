package y;

import android.util.Size;
import android.view.Surface;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e extends r0 {

    /* renamed from: a  reason: collision with root package name */
    private final Surface f24292a;

    /* renamed from: b  reason: collision with root package name */
    private final Size f24293b;

    /* renamed from: c  reason: collision with root package name */
    private final int f24294c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(Surface surface, Size size, int i8) {
        Objects.requireNonNull(surface, "Null surface");
        this.f24292a = surface;
        Objects.requireNonNull(size, "Null size");
        this.f24293b = size;
        this.f24294c = i8;
    }

    @Override // y.r0
    public int b() {
        return this.f24294c;
    }

    @Override // y.r0
    public Size c() {
        return this.f24293b;
    }

    @Override // y.r0
    public Surface d() {
        return this.f24292a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof r0) {
            r0 r0Var = (r0) obj;
            return this.f24292a.equals(r0Var.d()) && this.f24293b.equals(r0Var.c()) && this.f24294c == r0Var.b();
        }
        return false;
    }

    public int hashCode() {
        return ((((this.f24292a.hashCode() ^ 1000003) * 1000003) ^ this.f24293b.hashCode()) * 1000003) ^ this.f24294c;
    }

    public String toString() {
        return "OutputSurface{surface=" + this.f24292a + ", size=" + this.f24293b + ", imageFormat=" + this.f24294c + "}";
    }
}
