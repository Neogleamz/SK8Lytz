package androidx.camera.core.impl;

import androidx.camera.core.impl.SurfaceConfig;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c extends SurfaceConfig {

    /* renamed from: a  reason: collision with root package name */
    private final SurfaceConfig.ConfigType f2540a;

    /* renamed from: b  reason: collision with root package name */
    private final SurfaceConfig.ConfigSize f2541b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(SurfaceConfig.ConfigType configType, SurfaceConfig.ConfigSize configSize) {
        Objects.requireNonNull(configType, "Null configType");
        this.f2540a = configType;
        Objects.requireNonNull(configSize, "Null configSize");
        this.f2541b = configSize;
    }

    @Override // androidx.camera.core.impl.SurfaceConfig
    public SurfaceConfig.ConfigSize b() {
        return this.f2541b;
    }

    @Override // androidx.camera.core.impl.SurfaceConfig
    public SurfaceConfig.ConfigType c() {
        return this.f2540a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof SurfaceConfig) {
            SurfaceConfig surfaceConfig = (SurfaceConfig) obj;
            return this.f2540a.equals(surfaceConfig.c()) && this.f2541b.equals(surfaceConfig.b());
        }
        return false;
    }

    public int hashCode() {
        return ((this.f2540a.hashCode() ^ 1000003) * 1000003) ^ this.f2541b.hashCode();
    }

    public String toString() {
        return "SurfaceConfig{configType=" + this.f2540a + ", configSize=" + this.f2541b + "}";
    }
}
