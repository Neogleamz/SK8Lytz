package b6;

import android.net.Uri;
import android.text.TextUtils;
import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j0 {
    private static int[] a(String str) {
        int i8;
        int[] iArr = new int[4];
        if (TextUtils.isEmpty(str)) {
            iArr[0] = -1;
            return iArr;
        }
        int length = str.length();
        int indexOf = str.indexOf(35);
        if (indexOf != -1) {
            length = indexOf;
        }
        int indexOf2 = str.indexOf(63);
        if (indexOf2 == -1 || indexOf2 > length) {
            indexOf2 = length;
        }
        int indexOf3 = str.indexOf(47);
        if (indexOf3 == -1 || indexOf3 > indexOf2) {
            indexOf3 = indexOf2;
        }
        int indexOf4 = str.indexOf(58);
        if (indexOf4 > indexOf3) {
            indexOf4 = -1;
        }
        int i9 = indexOf4 + 2;
        if (i9 < indexOf2 && str.charAt(indexOf4 + 1) == '/' && str.charAt(i9) == '/') {
            i8 = str.indexOf(47, indexOf4 + 3);
            if (i8 == -1 || i8 > indexOf2) {
                i8 = indexOf2;
            }
        } else {
            i8 = indexOf4 + 1;
        }
        iArr[0] = indexOf4;
        iArr[1] = i8;
        iArr[2] = indexOf2;
        iArr[3] = length;
        return iArr;
    }

    public static boolean b(String str) {
        return (str == null || a(str)[0] == -1) ? false : true;
    }

    private static String c(StringBuilder sb, int i8, int i9) {
        int i10;
        int i11;
        if (i8 >= i9) {
            return sb.toString();
        }
        if (sb.charAt(i8) == '/') {
            i8++;
        }
        int i12 = i8;
        int i13 = i12;
        while (i12 <= i9) {
            if (i12 == i9) {
                i10 = i12;
            } else if (sb.charAt(i12) == '/') {
                i10 = i12 + 1;
            } else {
                i12++;
            }
            int i14 = i13 + 1;
            if (i12 == i14 && sb.charAt(i13) == '.') {
                sb.delete(i13, i10);
                i9 -= i10 - i13;
            } else {
                if (i12 == i13 + 2 && sb.charAt(i13) == '.' && sb.charAt(i14) == '.') {
                    i11 = sb.lastIndexOf("/", i13 - 2) + 1;
                    int i15 = i11 > i8 ? i11 : i8;
                    sb.delete(i15, i10);
                    i9 -= i10 - i15;
                } else {
                    i11 = i12 + 1;
                }
                i13 = i11;
            }
            i12 = i13;
        }
        return sb.toString();
    }

    public static String d(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        if (str == null) {
            str = BuildConfig.FLAVOR;
        }
        if (str2 == null) {
            str2 = BuildConfig.FLAVOR;
        }
        int[] a9 = a(str2);
        if (a9[0] != -1) {
            sb.append(str2);
            c(sb, a9[1], a9[2]);
            return sb.toString();
        }
        int[] a10 = a(str);
        if (a9[3] == 0) {
            sb.append((CharSequence) str, 0, a10[3]);
            sb.append(str2);
            return sb.toString();
        } else if (a9[2] == 0) {
            sb.append((CharSequence) str, 0, a10[2]);
            sb.append(str2);
            return sb.toString();
        } else if (a9[1] != 0) {
            int i8 = a10[0] + 1;
            sb.append((CharSequence) str, 0, i8);
            sb.append(str2);
            return c(sb, a9[1] + i8, i8 + a9[2]);
        } else if (str2.charAt(a9[1]) == '/') {
            sb.append((CharSequence) str, 0, a10[1]);
            sb.append(str2);
            return c(sb, a10[1], a10[1] + a9[2]);
        } else if (a10[0] + 2 < a10[1] && a10[1] == a10[2]) {
            sb.append((CharSequence) str, 0, a10[1]);
            sb.append('/');
            sb.append(str2);
            return c(sb, a10[1], a10[1] + a9[2] + 1);
        } else {
            int lastIndexOf = str.lastIndexOf(47, a10[2] - 1);
            int i9 = lastIndexOf == -1 ? a10[1] : lastIndexOf + 1;
            sb.append((CharSequence) str, 0, i9);
            sb.append(str2);
            return c(sb, a10[1], i9 + a9[2]);
        }
    }

    public static Uri e(String str, String str2) {
        return Uri.parse(d(str, str2));
    }
}
