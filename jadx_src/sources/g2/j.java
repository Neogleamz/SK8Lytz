package g2;

import android.text.TextUtils;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j {
    public static String a(String str, long j8) {
        if (j8 <= 0) {
            j8 = 0;
        }
        int i8 = (int) (j8 / 1000);
        int i9 = i8 % 60;
        int i10 = (i8 / 60) % 60;
        int i11 = i8 / 3600;
        if ("%02d:%02d".equals(str)) {
            return String.format(str, Integer.valueOf(i10), Integer.valueOf(i9));
        }
        if ("%02d:%02d:%02d".equals(str)) {
            return String.format(str, Integer.valueOf(i11), Integer.valueOf(i10), Integer.valueOf(i9));
        }
        if (TextUtils.isEmpty(str)) {
            str = "%02d:%02d:%02d";
        }
        return String.format(str, Integer.valueOf(i11), Integer.valueOf(i10), Integer.valueOf(i9));
    }

    public static String b(long j8) {
        return a("%02d:%02d", j8);
    }

    public static String c(long j8) {
        return a("%02d:%02d:%02d", j8);
    }

    public static String d(long j8) {
        return ((long) ((int) (j8 / 1000))) >= 3600 ? c(j8) : b(j8);
    }
}
