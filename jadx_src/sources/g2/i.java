package g2;

import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.LinkedList;
import java.util.regex.Pattern;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class b {

        /* renamed from: a  reason: collision with root package name */
        private int f20187a;

        /* renamed from: b  reason: collision with root package name */
        private int f20188b;

        private b() {
        }
    }

    public static boolean a(String str) {
        if (str == null || BuildConfig.FLAVOR.equals(str)) {
            return false;
        }
        return Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$").matcher(str).matches();
    }

    public static boolean b(String str) {
        return str.length() >= 4;
    }

    public static boolean c(String str, int i8) {
        return str.length() >= i8;
    }

    public static SpannableStringBuilder d(String str, String str2, String str3, int i8, int i9) {
        int i10;
        char[] charArray = str.toCharArray();
        char[] charArray2 = str3.toCharArray();
        if (charArray.length > charArray2.length) {
            return new SpannableStringBuilder(str3);
        }
        LinkedList linkedList = new LinkedList();
        for (int i11 = 0; i11 < charArray2.length; i11++) {
            for (int i12 = 0; i12 < charArray.length && (i10 = i11 + i12) <= charArray2.length - 1; i12++) {
                if (charArray[i12] == charArray2[i10]) {
                    if (i12 == charArray.length - 1) {
                        b bVar = new b();
                        bVar.f20187a = i11;
                        bVar.f20188b = i10;
                        linkedList.addLast(bVar);
                        Log.i("sfafaf", i11 + " start " + i10 + " end ");
                    }
                }
            }
        }
        if (linkedList.size() > 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append((CharSequence) str3.substring(0, ((b) linkedList.get(0)).f20187a));
            int i13 = 0;
            while (i13 < linkedList.size()) {
                String substring = i13 == linkedList.size() - 1 ? str3.substring(((b) linkedList.get(i13)).f20188b + 1) : str3.substring(((b) linkedList.get(i13)).f20188b + 1, ((b) linkedList.get(i13 + 1)).f20187a);
                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(str2);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(i8);
                AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(i9, true);
                spannableStringBuilder2.setSpan(foregroundColorSpan, 0, spannableStringBuilder2.length(), 17);
                spannableStringBuilder2.setSpan(absoluteSizeSpan, 0, spannableStringBuilder2.length(), 17);
                spannableStringBuilder.append((CharSequence) spannableStringBuilder2);
                spannableStringBuilder.append((CharSequence) substring);
                i13++;
            }
            return spannableStringBuilder;
        }
        return new SpannableStringBuilder(str3);
    }
}
