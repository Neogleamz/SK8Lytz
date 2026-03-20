package androidx.camera.core.impl;

import android.util.Size;
import y.z0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class SurfaceConfig {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum ConfigSize {
        VGA(0),
        PREVIEW(1),
        RECORD(2),
        MAXIMUM(3),
        NOT_SUPPORT(4);
        

        /* renamed from: a  reason: collision with root package name */
        final int f2514a;

        ConfigSize(int i8) {
            this.f2514a = i8;
        }

        int c() {
            return this.f2514a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum ConfigType {
        PRIV,
        YUV,
        JPEG,
        RAW
    }

    public static SurfaceConfig a(ConfigType configType, ConfigSize configSize) {
        return new c(configType, configSize);
    }

    public static ConfigType d(int i8) {
        return i8 == 35 ? ConfigType.YUV : i8 == 256 ? ConfigType.JPEG : i8 == 32 ? ConfigType.RAW : ConfigType.PRIV;
    }

    public static SurfaceConfig f(int i8, Size size, z0 z0Var) {
        ConfigType d8 = d(i8);
        ConfigSize configSize = ConfigSize.NOT_SUPPORT;
        int a9 = f0.c.a(size);
        return a(d8, a9 <= f0.c.a(z0Var.b()) ? ConfigSize.VGA : a9 <= f0.c.a(z0Var.c()) ? ConfigSize.PREVIEW : a9 <= f0.c.a(z0Var.d()) ? ConfigSize.RECORD : ConfigSize.MAXIMUM);
    }

    public abstract ConfigSize b();

    public abstract ConfigType c();

    public final boolean e(SurfaceConfig surfaceConfig) {
        return surfaceConfig.b().c() <= b().c() && surfaceConfig.c() == c();
    }
}
