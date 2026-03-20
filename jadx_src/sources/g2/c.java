package g2;

import com.daimajia.numberprogressbar.BuildConfig;
import java.security.MessageDigest;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {

    /* renamed from: a  reason: collision with root package name */
    private static char[] f20185a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static int a(byte[] bArr) {
        return ((bArr[1] << 8) & 65280) | (bArr[0] & 255);
    }

    public static byte[] b(int i8) {
        return new byte[]{(byte) ((i8 >> 8) & 255), (byte) (i8 & 255)};
    }

    public static byte[] c(int i8) {
        return new byte[]{(byte) (i8 & 255), (byte) ((i8 >> 8) & 255)};
    }

    public static String d(byte[] bArr) {
        StringBuilder sb = new StringBuilder(BuildConfig.FLAVOR);
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b9 : bArr) {
            String hexString = Integer.toHexString(b9 & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString + " ");
        }
        return sb.toString();
    }

    public static String e(byte[] bArr) {
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

    private static byte f(char c9) {
        return (byte) "0123456789ABCDEF".indexOf(c9);
    }

    public static String g(byte[] bArr) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bArr);
            byte[] digest = messageDigest.digest();
            char[] cArr = new char[digest.length * 2];
            int i8 = 0;
            for (byte b9 : digest) {
                int i9 = i8 + 1;
                char[] cArr2 = f20185a;
                cArr[i8] = cArr2[(b9 >>> 4) & 15];
                i8 = i9 + 1;
                cArr[i9] = cArr2[b9 & 15];
            }
            return new String(cArr);
        } catch (Exception e8) {
            e8.printStackTrace();
            return null;
        }
    }

    public static byte[] h(String str) {
        if (str == null || str.equals(BuildConfig.FLAVOR)) {
            return null;
        }
        String replace = str.toUpperCase().replace(" ", BuildConfig.FLAVOR);
        int length = replace.length() / 2;
        char[] charArray = replace.toCharArray();
        byte[] bArr = new byte[length];
        for (int i8 = 0; i8 < length; i8++) {
            int i9 = i8 * 2;
            bArr[i8] = (byte) (f(charArray[i9 + 1]) | (f(charArray[i9]) << 4));
        }
        return bArr;
    }
}
