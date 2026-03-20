package androidx.camera.core;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface SurfaceOutput {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum GlTransformOptions {
        USE_SURFACE_TEXTURE_TRANSFORM,
        APPLY_CROP_ROTATE_AND_MIRRORING
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {
        public static a c(int i8, SurfaceOutput surfaceOutput) {
            return new j(i8, surfaceOutput);
        }

        public abstract int a();

        public abstract SurfaceOutput b();
    }

    int b();
}
