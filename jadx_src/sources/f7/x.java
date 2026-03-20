package f7;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x {
    public static String a(String str, String[] strArr, String[] strArr2) {
        n6.j.l(strArr);
        n6.j.l(strArr2);
        int min = Math.min(strArr.length, strArr2.length);
        for (int i8 = 0; i8 < min; i8++) {
            String str2 = strArr[i8];
            if ((str == null && str2 == null) ? true : str == null ? false : str.equals(str2)) {
                return strArr2[i8];
            }
        }
        return null;
    }
}
