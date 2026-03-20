package u6;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {
    public static boolean a(int[] iArr, int i8) {
        if (iArr != null) {
            for (int i9 : iArr) {
                if (i9 == i8) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> boolean b(T[] tArr, T t8) {
        int length = tArr != null ? tArr.length : 0;
        int i8 = 0;
        while (true) {
            if (i8 >= length) {
                break;
            } else if (!n6.i.a(tArr[i8], t8)) {
                i8++;
            } else if (i8 >= 0) {
                return true;
            }
        }
        return false;
    }

    public static void c(StringBuilder sb, double[] dArr) {
        int length = dArr.length;
        for (int i8 = 0; i8 < length; i8++) {
            if (i8 != 0) {
                sb.append(",");
            }
            sb.append(dArr[i8]);
        }
    }

    public static void d(StringBuilder sb, float[] fArr) {
        int length = fArr.length;
        for (int i8 = 0; i8 < length; i8++) {
            if (i8 != 0) {
                sb.append(",");
            }
            sb.append(fArr[i8]);
        }
    }

    public static void e(StringBuilder sb, int[] iArr) {
        int length = iArr.length;
        for (int i8 = 0; i8 < length; i8++) {
            if (i8 != 0) {
                sb.append(",");
            }
            sb.append(iArr[i8]);
        }
    }

    public static void f(StringBuilder sb, long[] jArr) {
        int length = jArr.length;
        for (int i8 = 0; i8 < length; i8++) {
            if (i8 != 0) {
                sb.append(",");
            }
            sb.append(jArr[i8]);
        }
    }

    public static <T> void g(StringBuilder sb, T[] tArr) {
        int length = tArr.length;
        for (int i8 = 0; i8 < length; i8++) {
            if (i8 != 0) {
                sb.append(",");
            }
            sb.append(tArr[i8]);
        }
    }

    public static void h(StringBuilder sb, boolean[] zArr) {
        int length = zArr.length;
        for (int i8 = 0; i8 < length; i8++) {
            if (i8 != 0) {
                sb.append(",");
            }
            sb.append(zArr[i8]);
        }
    }

    public static void i(StringBuilder sb, String[] strArr) {
        int length = strArr.length;
        for (int i8 = 0; i8 < length; i8++) {
            if (i8 != 0) {
                sb.append(",");
            }
            sb.append("\"");
            sb.append(strArr[i8]);
            sb.append("\"");
        }
    }
}
