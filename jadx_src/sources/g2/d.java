package g2;

import android.graphics.Color;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d {
    public static float a(int i8) {
        float[] fArr = new float[3];
        Color.colorToHSV(i8, fArr);
        return fArr[2];
    }

    public static int b(double d8, double d9, double d10) {
        double d11;
        double d12;
        double d13 = 0.0d;
        if (d9 != 0.0d) {
            double d14 = d8 / 60.0d;
            int floor = (int) Math.floor(d14);
            double d15 = d14 - floor;
            d11 = (1.0d - d9) * d10;
            double d16 = (1.0d - (d9 * d15)) * d10;
            d12 = d10 * (1.0d - ((1.0d - d15) * d9));
            if (floor != 0) {
                if (floor == 1) {
                    d13 = d10;
                    d12 = d11;
                    d11 = d16;
                } else if (floor == 2) {
                    d13 = d10;
                } else if (floor == 3) {
                    d12 = d10;
                    d13 = d16;
                } else if (floor == 4) {
                    d13 = d11;
                    d11 = d12;
                    d12 = d10;
                } else if (floor == 5) {
                    d13 = d11;
                    d12 = d16;
                }
                return Color.argb(255, (int) Math.round(d11 * 255.0d), (int) Math.round(d13 * 255.0d), (int) Math.round(d12 * 255.0d));
            }
            d13 = d12;
            d12 = d11;
            d11 = d10;
            return Color.argb(255, (int) Math.round(d11 * 255.0d), (int) Math.round(d13 * 255.0d), (int) Math.round(d12 * 255.0d));
        }
        d13 = d10;
        d12 = d13;
        d11 = d12;
        return Color.argb(255, (int) Math.round(d11 * 255.0d), (int) Math.round(d13 * 255.0d), (int) Math.round(d12 * 255.0d));
    }

    public static float c(int i8, int i9, int i10) {
        float[] fArr = new float[3];
        Color.colorToHSV(Color.rgb(i8, i9, i10), fArr);
        return fArr[2];
    }

    public static int d(int i8, int i9, int i10) {
        Color.colorToHSV(Color.rgb(i8, i9, i10), r0);
        float[] fArr = {0.0f, 0.0f, 1.0f};
        return Color.HSVToColor(fArr);
    }

    private static int e(int i8, int i9, int i10) {
        return i8 < i9 ? i9 : i8 > i10 ? i10 : i8;
    }

    public static float f(float f5, float f8, float f9, float f10, float f11) {
        float f12 = f8 - f5;
        if (f12 == 0.0f) {
            return f9;
        }
        float f13 = ((f10 - f9) * 1.0f * ((f11 - f5) / f12)) + f9;
        if (f13 <= f10) {
            f10 = f13;
        }
        return f10 < f9 ? f9 : f10;
    }

    public static int g(int i8, float f5) {
        Color.colorToHSV(i8, r0);
        float[] fArr = {0.0f, 0.0f, f5};
        return b(fArr[0], fArr[1], fArr[2]);
    }

    public static String h(int i8, float f5) {
        int red = Color.red(i8);
        int green = Color.green(i8);
        int blue = Color.blue(i8);
        float round = Math.round(f5 * 255.0f);
        return ((int) Math.floor((red * 1.0f) / round)) + "-" + ((int) Math.floor((green * 1.0f) / round)) + "-" + ((int) Math.floor((blue * 1.0f) / round));
    }

    public static int i(int i8, int i9, float f5) {
        return Color.argb(255, e((int) j(Color.red(i8), Color.red(i9), f5), 0, 255), e((int) j(Color.green(i8), Color.green(i9), f5), 0, 255), e((int) j(Color.blue(i8), Color.blue(i9), f5), 0, 255));
    }

    private static float j(int i8, int i9, float f5) {
        return (i8 * (1.0f - f5)) + (i9 * f5);
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x002f, code lost:
        if (r3 > 255.0f) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x004a, code lost:
        if (r4 > 255.0f) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0066, code lost:
        if (r4 > 255.0f) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0068, code lost:
        r4 = 255.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x008b, code lost:
        if (r1 > 255.0f) goto L17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int k(int r10) {
        /*
            r0 = 1000(0x3e8, float:1.401E-42)
            if (r10 >= r0) goto L5
            r10 = r0
        L5:
            r0 = 40000(0x9c40, float:5.6052E-41)
            if (r10 <= r0) goto Lb
            r10 = r0
        Lb:
            int r10 = r10 / 100
            r0 = 66
            r1 = 0
            r2 = 1132396544(0x437f0000, float:255.0)
            if (r10 > r0) goto L16
        L14:
            r3 = r2
            goto L32
        L16:
            int r3 = r10 + (-60)
            float r3 = (float) r3
            r4 = 1134877040(0x43a4d970, float:329.69873)
            double r5 = (double) r3
            r7 = -4629404809370271744(0xbfc10cda80000000, double:-0.13320475816726685)
            double r5 = java.lang.Math.pow(r5, r7)
            float r3 = (float) r5
            float r3 = r3 * r4
            int r4 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1))
            if (r4 >= 0) goto L2d
            r3 = r1
        L2d:
            int r4 = (r3 > r2 ? 1 : (r3 == r2 ? 0 : -1))
            if (r4 <= 0) goto L32
            goto L14
        L32:
            if (r10 > r0) goto L4d
            float r4 = (float) r10
            r5 = 1120334093(0x42c6f10d, float:99.4708)
            double r6 = (double) r4
            double r6 = java.lang.Math.log(r6)
            float r4 = (float) r6
            float r4 = r4 * r5
            r5 = 1126243996(0x43211e9c, float:161.11957)
            float r4 = r4 - r5
            int r5 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1))
            if (r5 >= 0) goto L48
            r4 = r1
        L48:
            int r5 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r5 <= 0) goto L69
            goto L68
        L4d:
            int r4 = r10 + (-60)
            float r4 = (float) r4
            r5 = 1133514659(0x43900fa3, float:288.12216)
            double r6 = (double) r4
            r8 = -4633266197844121933(0xbfb354f0efad26b3, double:-0.0755148492)
            double r6 = java.lang.Math.pow(r6, r8)
            float r4 = (float) r6
            float r4 = r4 * r5
            int r5 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1))
            if (r5 >= 0) goto L64
            r4 = r1
        L64:
            int r5 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r5 <= 0) goto L69
        L68:
            r4 = r2
        L69:
            if (r10 < r0) goto L6d
        L6b:
            r1 = r2
            goto L8e
        L6d:
            r0 = 19
            if (r10 > r0) goto L72
            goto L8e
        L72:
            int r10 = r10 + (-10)
            float r10 = (float) r10
            r0 = 1124762762(0x430a848a, float:138.51773)
            double r5 = (double) r10
            double r5 = java.lang.Math.log(r5)
            float r10 = (float) r5
            float r10 = r10 * r0
            r0 = 1134069180(0x439885bc, float:305.0448)
            float r10 = r10 - r0
            int r0 = (r10 > r1 ? 1 : (r10 == r1 ? 0 : -1))
            if (r0 >= 0) goto L88
            goto L89
        L88:
            r1 = r10
        L89:
            int r10 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r10 <= 0) goto L8e
            goto L6b
        L8e:
            int r10 = (int) r3
            int r0 = (int) r4
            int r1 = (int) r1
            int r10 = android.graphics.Color.rgb(r10, r0, r1)
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: g2.d.k(int):int");
    }
}
