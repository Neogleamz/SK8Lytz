package androidx.core.graphics;

import android.graphics.Color;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    private static final ThreadLocal<double[]> f4706a = new ThreadLocal<>();

    public static int a(float[] fArr) {
        int round;
        int round2;
        int round3;
        float f5 = fArr[0];
        float f8 = fArr[1];
        float f9 = fArr[2];
        float abs = (1.0f - Math.abs((f9 * 2.0f) - 1.0f)) * f8;
        float f10 = f9 - (0.5f * abs);
        float abs2 = (1.0f - Math.abs(((f5 / 60.0f) % 2.0f) - 1.0f)) * abs;
        switch (((int) f5) / 60) {
            case 0:
                round = Math.round((abs + f10) * 255.0f);
                round2 = Math.round((abs2 + f10) * 255.0f);
                round3 = Math.round(f10 * 255.0f);
                break;
            case 1:
                round = Math.round((abs2 + f10) * 255.0f);
                round2 = Math.round((abs + f10) * 255.0f);
                round3 = Math.round(f10 * 255.0f);
                break;
            case 2:
                round = Math.round(f10 * 255.0f);
                round2 = Math.round((abs + f10) * 255.0f);
                round3 = Math.round((abs2 + f10) * 255.0f);
                break;
            case 3:
                round = Math.round(f10 * 255.0f);
                round2 = Math.round((abs2 + f10) * 255.0f);
                round3 = Math.round((abs + f10) * 255.0f);
                break;
            case 4:
                round = Math.round((abs2 + f10) * 255.0f);
                round2 = Math.round(f10 * 255.0f);
                round3 = Math.round((abs + f10) * 255.0f);
                break;
            case 5:
            case 6:
                round = Math.round((abs + f10) * 255.0f);
                round2 = Math.round(f10 * 255.0f);
                round3 = Math.round((abs2 + f10) * 255.0f);
                break;
            default:
                round3 = 0;
                round = 0;
                round2 = 0;
                break;
        }
        return Color.rgb(n(round, 0, 255), n(round2, 0, 255), n(round3, 0, 255));
    }

    public static void b(int i8, int i9, int i10, float[] fArr) {
        float f5;
        float abs;
        float f8 = i8 / 255.0f;
        float f9 = i9 / 255.0f;
        float f10 = i10 / 255.0f;
        float max = Math.max(f8, Math.max(f9, f10));
        float min = Math.min(f8, Math.min(f9, f10));
        float f11 = max - min;
        float f12 = (max + min) / 2.0f;
        if (max == min) {
            f5 = 0.0f;
            abs = 0.0f;
        } else {
            f5 = max == f8 ? ((f9 - f10) / f11) % 6.0f : max == f9 ? ((f10 - f8) / f11) + 2.0f : 4.0f + ((f8 - f9) / f11);
            abs = f11 / (1.0f - Math.abs((2.0f * f12) - 1.0f));
        }
        float f13 = (f5 * 60.0f) % 360.0f;
        if (f13 < 0.0f) {
            f13 += 360.0f;
        }
        fArr[0] = m(f13, 0.0f, 360.0f);
        fArr[1] = m(abs, 0.0f, 1.0f);
        fArr[2] = m(f12, 0.0f, 1.0f);
    }

    public static void c(int i8, int i9, int i10, double[] dArr) {
        if (dArr.length != 3) {
            throw new IllegalArgumentException("outXyz must have a length of 3.");
        }
        double d8 = i8 / 255.0d;
        double pow = d8 < 0.04045d ? d8 / 12.92d : Math.pow((d8 + 0.055d) / 1.055d, 2.4d);
        double d9 = i9 / 255.0d;
        double pow2 = d9 < 0.04045d ? d9 / 12.92d : Math.pow((d9 + 0.055d) / 1.055d, 2.4d);
        double d10 = i10 / 255.0d;
        double pow3 = d10 < 0.04045d ? d10 / 12.92d : Math.pow((d10 + 0.055d) / 1.055d, 2.4d);
        dArr[0] = ((0.4124d * pow) + (0.3576d * pow2) + (0.1805d * pow3)) * 100.0d;
        dArr[1] = ((0.2126d * pow) + (0.7152d * pow2) + (0.0722d * pow3)) * 100.0d;
        dArr[2] = ((pow * 0.0193d) + (pow2 * 0.1192d) + (pow3 * 0.9505d)) * 100.0d;
    }

    public static int d(double d8, double d9, double d10) {
        double d11 = (((3.2406d * d8) + ((-1.5372d) * d9)) + ((-0.4986d) * d10)) / 100.0d;
        double d12 = ((((-0.9689d) * d8) + (1.8758d * d9)) + (0.0415d * d10)) / 100.0d;
        double d13 = (((0.0557d * d8) + ((-0.204d) * d9)) + (1.057d * d10)) / 100.0d;
        return Color.rgb(n((int) Math.round((d11 > 0.0031308d ? (Math.pow(d11, 0.4166666666666667d) * 1.055d) - 0.055d : d11 * 12.92d) * 255.0d), 0, 255), n((int) Math.round((d12 > 0.0031308d ? (Math.pow(d12, 0.4166666666666667d) * 1.055d) - 0.055d : d12 * 12.92d) * 255.0d), 0, 255), n((int) Math.round((d13 > 0.0031308d ? (Math.pow(d13, 0.4166666666666667d) * 1.055d) - 0.055d : d13 * 12.92d) * 255.0d), 0, 255));
    }

    public static double e(int i8, int i9) {
        if (Color.alpha(i9) != 255) {
            throw new IllegalArgumentException("background can not be translucent: #" + Integer.toHexString(i9));
        }
        if (Color.alpha(i8) < 255) {
            i8 = k(i8, i9);
        }
        double f5 = f(i8) + 0.05d;
        double f8 = f(i9) + 0.05d;
        return Math.max(f5, f8) / Math.min(f5, f8);
    }

    public static double f(int i8) {
        double[] o5 = o();
        i(i8, o5);
        return o5[1] / 100.0d;
    }

    public static int g(int i8, int i9, float f5) {
        int i10 = 255;
        if (Color.alpha(i9) != 255) {
            throw new IllegalArgumentException("background can not be translucent: #" + Integer.toHexString(i9));
        }
        double d8 = f5;
        if (e(p(i8, 255), i9) < d8) {
            return -1;
        }
        int i11 = 0;
        for (int i12 = 0; i12 <= 10 && i10 - i11 > 1; i12++) {
            int i13 = (i11 + i10) / 2;
            if (e(p(i8, i13), i9) < d8) {
                i11 = i13;
            } else {
                i10 = i13;
            }
        }
        return i10;
    }

    public static void h(int i8, float[] fArr) {
        b(Color.red(i8), Color.green(i8), Color.blue(i8), fArr);
    }

    public static void i(int i8, double[] dArr) {
        c(Color.red(i8), Color.green(i8), Color.blue(i8), dArr);
    }

    private static int j(int i8, int i9) {
        return 255 - (((255 - i9) * (255 - i8)) / 255);
    }

    public static int k(int i8, int i9) {
        int alpha = Color.alpha(i9);
        int alpha2 = Color.alpha(i8);
        int j8 = j(alpha2, alpha);
        return Color.argb(j8, l(Color.red(i8), alpha2, Color.red(i9), alpha, j8), l(Color.green(i8), alpha2, Color.green(i9), alpha, j8), l(Color.blue(i8), alpha2, Color.blue(i9), alpha, j8));
    }

    private static int l(int i8, int i9, int i10, int i11, int i12) {
        if (i12 == 0) {
            return 0;
        }
        return (((i8 * 255) * i9) + ((i10 * i11) * (255 - i9))) / (i12 * 255);
    }

    private static float m(float f5, float f8, float f9) {
        return f5 < f8 ? f8 : Math.min(f5, f9);
    }

    private static int n(int i8, int i9, int i10) {
        return i8 < i9 ? i9 : Math.min(i8, i10);
    }

    private static double[] o() {
        ThreadLocal<double[]> threadLocal = f4706a;
        double[] dArr = threadLocal.get();
        if (dArr == null) {
            double[] dArr2 = new double[3];
            threadLocal.set(dArr2);
            return dArr2;
        }
        return dArr;
    }

    public static int p(int i8, int i9) {
        if (i9 < 0 || i9 > 255) {
            throw new IllegalArgumentException("alpha must be between 0 and 255.");
        }
        return (i8 & 16777215) | (i9 << 24);
    }
}
