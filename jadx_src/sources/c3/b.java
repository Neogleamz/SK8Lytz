package c3;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {

    /* renamed from: a  reason: collision with root package name */
    private static final char[] f8295a = "0123456789ABCDEF".toCharArray();

    public static String a(byte[] bArr, int i8) {
        if (bArr != null) {
            StringBuilder sb = new StringBuilder();
            for (int i9 = 0; i9 < i8; i9++) {
                char[] cArr = f8295a;
                sb.append(cArr[(bArr[i9] & 255) >> 4]);
                sb.append(cArr[bArr[i9] & 15]);
            }
            return sb.toString().trim().toUpperCase(Locale.US);
        }
        return BuildConfig.FLAVOR;
    }

    public static byte[] b(String str) {
        String replaceAll = str.replaceAll(" ", BuildConfig.FLAVOR);
        int length = replaceAll.length();
        if (length % 2 != 0) {
            length++;
            replaceAll = "0" + replaceAll;
        }
        byte[] bArr = new byte[length / 2];
        for (int i8 = 0; i8 < length; i8 += 2) {
            bArr[i8 / 2] = (byte) ((Character.digit(replaceAll.charAt(i8), 16) << 4) + Character.digit(replaceAll.charAt(i8 + 1), 16));
        }
        return bArr;
    }
}
