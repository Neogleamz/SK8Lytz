package s7;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {
    public static float a(float f5, float f8, float f9, float f10) {
        return (float) Math.hypot(f9 - f5, f10 - f8);
    }

    public static float b(float f5, float f8, float f9, float f10, float f11, float f12) {
        return e(a(f5, f8, f9, f10), a(f5, f8, f11, f10), a(f5, f8, f11, f12), a(f5, f8, f9, f12));
    }

    public static boolean c(float f5, float f8, float f9) {
        return f5 + f9 >= f8;
    }

    public static float d(float f5, float f8, float f9) {
        return ((1.0f - f9) * f5) + (f9 * f8);
    }

    private static float e(float f5, float f8, float f9, float f10) {
        return (f5 <= f8 || f5 <= f9 || f5 <= f10) ? (f8 <= f9 || f8 <= f10) ? f9 > f10 ? f9 : f10 : f8 : f5;
    }
}
