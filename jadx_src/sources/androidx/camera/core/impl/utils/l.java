package androidx.camera.core.impl.utils;

import android.opengl.Matrix;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l {

    /* renamed from: a  reason: collision with root package name */
    private static final float[] f2657a = new float[16];

    private static void a(float[] fArr, float f5, float f8) {
        Matrix.translateM(fArr, 0, -f5, -f8, 0.0f);
    }

    private static void b(float[] fArr, float f5, float f8) {
        Matrix.translateM(fArr, 0, f5, f8, 0.0f);
    }

    public static void c(float[] fArr, float f5, float f8, float f9) {
        b(fArr, f8, f9);
        Matrix.rotateM(fArr, 0, f5, 0.0f, 0.0f, 1.0f);
        a(fArr, f8, f9);
    }
}
