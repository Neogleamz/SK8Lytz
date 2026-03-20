package androidx.core.content.res;

import android.graphics.Color;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b {

    /* renamed from: a  reason: collision with root package name */
    static final float[][] f4640a = {new float[]{0.401288f, 0.650173f, -0.051461f}, new float[]{-0.250268f, 1.204414f, 0.045854f}, new float[]{-0.002079f, 0.048952f, 0.953127f}};

    /* renamed from: b  reason: collision with root package name */
    static final float[][] f4641b = {new float[]{1.8620678f, -1.0112547f, 0.14918678f}, new float[]{0.38752654f, 0.62144744f, -0.00897398f}, new float[]{-0.0158415f, -0.03412294f, 1.0499644f}};

    /* renamed from: c  reason: collision with root package name */
    static final float[] f4642c = {95.047f, 100.0f, 108.883f};

    /* renamed from: d  reason: collision with root package name */
    static final float[][] f4643d = {new float[]{0.41233894f, 0.35762063f, 0.18051042f}, new float[]{0.2126f, 0.7152f, 0.0722f}, new float[]{0.01932141f, 0.11916382f, 0.9503448f}};

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(float f5) {
        if (f5 < 1.0f) {
            return -16777216;
        }
        if (f5 > 99.0f) {
            return -1;
        }
        float f8 = (f5 + 16.0f) / 116.0f;
        float f9 = (f5 > 8.0f ? 1 : (f5 == 8.0f ? 0 : -1)) > 0 ? f8 * f8 * f8 : f5 / 903.2963f;
        float f10 = f8 * f8 * f8;
        boolean z4 = f10 > 0.008856452f;
        float f11 = z4 ? f10 : ((f8 * 116.0f) - 16.0f) / 903.2963f;
        if (!z4) {
            f10 = ((f8 * 116.0f) - 16.0f) / 903.2963f;
        }
        float[] fArr = f4642c;
        return androidx.core.graphics.b.d(f11 * fArr[0], f9 * fArr[1], f10 * fArr[2]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(int i8) {
        return c(g(i8));
    }

    static float c(float f5) {
        float f8 = f5 / 100.0f;
        return f8 <= 0.008856452f ? f8 * 903.2963f : (((float) Math.cbrt(f8)) * 116.0f) - 16.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float d(float f5, float f8, float f9) {
        return f5 + ((f8 - f5) * f9);
    }

    static float e(int i8) {
        float f5 = i8 / 255.0f;
        return (f5 <= 0.04045f ? f5 / 12.92f : (float) Math.pow((f5 + 0.055f) / 1.055f, 2.4000000953674316d)) * 100.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float[] f(int i8) {
        float e8 = e(Color.red(i8));
        float e9 = e(Color.green(i8));
        float e10 = e(Color.blue(i8));
        float[][] fArr = f4643d;
        return new float[]{(fArr[0][0] * e8) + (fArr[0][1] * e9) + (fArr[0][2] * e10), (fArr[1][0] * e8) + (fArr[1][1] * e9) + (fArr[1][2] * e10), (e8 * fArr[2][0]) + (e9 * fArr[2][1]) + (e10 * fArr[2][2])};
    }

    static float g(int i8) {
        float e8 = e(Color.red(i8));
        float e9 = e(Color.green(i8));
        float e10 = e(Color.blue(i8));
        float[][] fArr = f4643d;
        return (e8 * fArr[1][0]) + (e9 * fArr[1][1]) + (e10 * fArr[1][2]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float h(float f5) {
        return (f5 > 8.0f ? (float) Math.pow((f5 + 16.0d) / 116.0d, 3.0d) : f5 / 903.2963f) * 100.0f;
    }
}
