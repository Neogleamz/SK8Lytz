package androidx.camera.core;

import androidx.camera.core.SurfaceOutput;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j extends SurfaceOutput.a {

    /* renamed from: a  reason: collision with root package name */
    private final int f2686a;

    /* renamed from: b  reason: collision with root package name */
    private final SurfaceOutput f2687b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j(int i8, SurfaceOutput surfaceOutput) {
        this.f2686a = i8;
        Objects.requireNonNull(surfaceOutput, "Null surfaceOutput");
        this.f2687b = surfaceOutput;
    }

    @Override // androidx.camera.core.SurfaceOutput.a
    public int a() {
        return this.f2686a;
    }

    @Override // androidx.camera.core.SurfaceOutput.a
    public SurfaceOutput b() {
        return this.f2687b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof SurfaceOutput.a) {
            SurfaceOutput.a aVar = (SurfaceOutput.a) obj;
            return this.f2686a == aVar.a() && this.f2687b.equals(aVar.b());
        }
        return false;
    }

    public int hashCode() {
        return ((this.f2686a ^ 1000003) * 1000003) ^ this.f2687b.hashCode();
    }

    public String toString() {
        return "Event{eventCode=" + this.f2686a + ", surfaceOutput=" + this.f2687b + "}";
    }
}
