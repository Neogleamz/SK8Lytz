package androidx.camera.core;

import android.view.Surface;
import androidx.camera.core.z2;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k extends z2.f {

    /* renamed from: a  reason: collision with root package name */
    private final int f2694a;

    /* renamed from: b  reason: collision with root package name */
    private final Surface f2695b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k(int i8, Surface surface) {
        this.f2694a = i8;
        Objects.requireNonNull(surface, "Null surface");
        this.f2695b = surface;
    }

    @Override // androidx.camera.core.z2.f
    public int a() {
        return this.f2694a;
    }

    @Override // androidx.camera.core.z2.f
    public Surface b() {
        return this.f2695b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof z2.f) {
            z2.f fVar = (z2.f) obj;
            return this.f2694a == fVar.a() && this.f2695b.equals(fVar.b());
        }
        return false;
    }

    public int hashCode() {
        return ((this.f2694a ^ 1000003) * 1000003) ^ this.f2695b.hashCode();
    }

    public String toString() {
        return "Result{resultCode=" + this.f2694a + ", surface=" + this.f2695b + "}";
    }
}
