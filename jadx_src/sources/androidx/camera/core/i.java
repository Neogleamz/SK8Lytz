package androidx.camera.core;

import android.graphics.Rect;
import android.util.Size;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class i extends j2 {

    /* renamed from: a  reason: collision with root package name */
    private final Size f2409a;

    /* renamed from: b  reason: collision with root package name */
    private final Rect f2410b;

    /* renamed from: c  reason: collision with root package name */
    private final int f2411c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(Size size, Rect rect, int i8) {
        Objects.requireNonNull(size, "Null resolution");
        this.f2409a = size;
        Objects.requireNonNull(rect, "Null cropRect");
        this.f2410b = rect;
        this.f2411c = i8;
    }

    @Override // androidx.camera.core.j2
    public Rect b() {
        return this.f2410b;
    }

    @Override // androidx.camera.core.j2
    public Size c() {
        return this.f2409a;
    }

    @Override // androidx.camera.core.j2
    public int d() {
        return this.f2411c;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof j2) {
            j2 j2Var = (j2) obj;
            return this.f2409a.equals(j2Var.c()) && this.f2410b.equals(j2Var.b()) && this.f2411c == j2Var.d();
        }
        return false;
    }

    public int hashCode() {
        return ((((this.f2409a.hashCode() ^ 1000003) * 1000003) ^ this.f2410b.hashCode()) * 1000003) ^ this.f2411c;
    }

    public String toString() {
        return "ResolutionInfo{resolution=" + this.f2409a + ", cropRect=" + this.f2410b + ", rotationDegrees=" + this.f2411c + "}";
    }
}
