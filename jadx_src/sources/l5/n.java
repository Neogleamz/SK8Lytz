package l5;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n {

    /* renamed from: a  reason: collision with root package name */
    private final String[] f21720a;

    /* renamed from: b  reason: collision with root package name */
    private final int[] f21721b;

    /* renamed from: c  reason: collision with root package name */
    private final String[] f21722c;

    /* renamed from: d  reason: collision with root package name */
    private final int f21723d;

    private n(String[] strArr, int[] iArr, String[] strArr2, int i8) {
        this.f21720a = strArr;
        this.f21721b = iArr;
        this.f21722c = strArr2;
        this.f21723d = i8;
    }

    public static n b(String str) {
        String[] strArr = new String[5];
        int[] iArr = new int[4];
        String[] strArr2 = new String[4];
        return new n(strArr, iArr, strArr2, c(str, strArr, iArr, strArr2));
    }

    private static int c(String str, String[] strArr, int[] iArr, String[] strArr2) {
        String str2;
        strArr[0] = BuildConfig.FLAVOR;
        int i8 = 0;
        int i9 = 0;
        while (i8 < str.length()) {
            int indexOf = str.indexOf("$", i8);
            char c9 = 65535;
            if (indexOf == -1) {
                strArr[i9] = strArr[i9] + str.substring(i8);
                i8 = str.length();
            } else if (indexOf != i8) {
                strArr[i9] = strArr[i9] + str.substring(i8, indexOf);
                i8 = indexOf;
            } else if (str.startsWith("$$", i8)) {
                strArr[i9] = strArr[i9] + "$";
                i8 += 2;
            } else {
                int i10 = i8 + 1;
                int indexOf2 = str.indexOf("$", i10);
                String substring = str.substring(i10, indexOf2);
                if (substring.equals("RepresentationID")) {
                    iArr[i9] = 1;
                } else {
                    int indexOf3 = substring.indexOf("%0");
                    if (indexOf3 != -1) {
                        str2 = substring.substring(indexOf3);
                        if (!str2.endsWith("d") && !str2.endsWith("x") && !str2.endsWith("X")) {
                            str2 = str2 + "d";
                        }
                        substring = substring.substring(0, indexOf3);
                    } else {
                        str2 = "%01d";
                    }
                    substring.hashCode();
                    switch (substring.hashCode()) {
                        case -1950496919:
                            if (substring.equals("Number")) {
                                c9 = 0;
                                break;
                            }
                            break;
                        case 2606829:
                            if (substring.equals("Time")) {
                                c9 = 1;
                                break;
                            }
                            break;
                        case 38199441:
                            if (substring.equals("Bandwidth")) {
                                c9 = 2;
                                break;
                            }
                            break;
                    }
                    switch (c9) {
                        case 0:
                            iArr[i9] = 2;
                            break;
                        case 1:
                            iArr[i9] = 4;
                            break;
                        case 2:
                            iArr[i9] = 3;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid template: " + str);
                    }
                    strArr2[i9] = str2;
                }
                i9++;
                strArr[i9] = BuildConfig.FLAVOR;
                i8 = indexOf2 + 1;
            }
        }
        return i9;
    }

    public String a(String str, long j8, int i8, long j9) {
        String format;
        StringBuilder sb = new StringBuilder();
        int i9 = 0;
        while (true) {
            int i10 = this.f21723d;
            if (i9 >= i10) {
                sb.append(this.f21720a[i10]);
                return sb.toString();
            }
            sb.append(this.f21720a[i9]);
            int[] iArr = this.f21721b;
            if (iArr[i9] == 1) {
                sb.append(str);
            } else {
                if (iArr[i9] == 2) {
                    format = String.format(Locale.US, this.f21722c[i9], Long.valueOf(j8));
                } else if (iArr[i9] == 3) {
                    format = String.format(Locale.US, this.f21722c[i9], Integer.valueOf(i8));
                } else if (iArr[i9] == 4) {
                    format = String.format(Locale.US, this.f21722c[i9], Long.valueOf(j9));
                }
                sb.append(format);
            }
            i9++;
        }
    }
}
