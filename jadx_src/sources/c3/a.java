package c3;

import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {
    public static byte a(byte b9, int i8) {
        return (byte) (b9 & (~(1 << i8)));
    }

    public static int b(byte b9, int i8) {
        return ((b9 & (1 << i8)) >> i8) == 1 ? 1 : 0;
    }

    public static int c(int i8, int i9) {
        return ((i8 & (1 << i9)) >> i9) == 1 ? 1 : 0;
    }

    public static byte d(byte b9, int i8) {
        return (byte) (b9 | (1 << i8));
    }

    public static int e(int i8, int i9) {
        return i8 | (1 << i9);
    }

    public static Integer f(Byte b9) {
        return Integer.valueOf(b9.byteValue() < 0 ? b9.byteValue() & 255 : b9.byteValue());
    }

    public static String g(byte[] bArr) {
        StringBuilder sb = new StringBuilder(BuildConfig.FLAVOR);
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b9 : bArr) {
            String hexString = Integer.toHexString(b9 & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }
}
