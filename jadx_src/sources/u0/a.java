package u0;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import androidx.core.util.h;
@SuppressLint({"PrivateConstructorForUtilityClass"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    private static final String[] f22955a = new String[0];

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: u0.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0209a {
        static void a(EditorInfo editorInfo, CharSequence charSequence, int i8) {
            editorInfo.setInitialSurroundingSubText(charSequence, i8);
        }
    }

    public static String[] a(EditorInfo editorInfo) {
        if (Build.VERSION.SDK_INT >= 25) {
            String[] strArr = editorInfo.contentMimeTypes;
            return strArr != null ? strArr : f22955a;
        }
        Bundle bundle = editorInfo.extras;
        if (bundle == null) {
            return f22955a;
        }
        String[] stringArray = bundle.getStringArray("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
        if (stringArray == null) {
            stringArray = editorInfo.extras.getStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
        }
        return stringArray != null ? stringArray : f22955a;
    }

    private static boolean b(CharSequence charSequence, int i8, int i9) {
        if (i9 != 0) {
            if (i9 != 1) {
                return false;
            }
            return Character.isHighSurrogate(charSequence.charAt(i8));
        }
        return Character.isLowSurrogate(charSequence.charAt(i8));
    }

    private static boolean c(int i8) {
        int i9 = i8 & 4095;
        return i9 == 129 || i9 == 225 || i9 == 18;
    }

    public static void d(EditorInfo editorInfo, String[] strArr) {
        if (Build.VERSION.SDK_INT >= 25) {
            editorInfo.contentMimeTypes = strArr;
            return;
        }
        if (editorInfo.extras == null) {
            editorInfo.extras = new Bundle();
        }
        editorInfo.extras.putStringArray("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", strArr);
        editorInfo.extras.putStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", strArr);
    }

    public static void e(EditorInfo editorInfo, CharSequence charSequence, int i8) {
        h.h(charSequence);
        if (Build.VERSION.SDK_INT >= 30) {
            C0209a.a(editorInfo, charSequence, i8);
            return;
        }
        int i9 = editorInfo.initialSelStart;
        int i10 = editorInfo.initialSelEnd;
        int i11 = i9 > i10 ? i10 - i8 : i9 - i8;
        int i12 = i9 > i10 ? i9 - i8 : i10 - i8;
        int length = charSequence.length();
        if (i8 < 0 || i11 < 0 || i12 > length) {
            g(editorInfo, null, 0, 0);
        } else if (c(editorInfo.inputType)) {
            g(editorInfo, null, 0, 0);
        } else if (length <= 2048) {
            g(editorInfo, charSequence, i11, i12);
        } else {
            h(editorInfo, charSequence, i11, i12);
        }
    }

    public static void f(EditorInfo editorInfo, CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 30) {
            C0209a.a(editorInfo, charSequence, 0);
        } else {
            e(editorInfo, charSequence, 0);
        }
    }

    private static void g(EditorInfo editorInfo, CharSequence charSequence, int i8, int i9) {
        if (editorInfo.extras == null) {
            editorInfo.extras = new Bundle();
        }
        editorInfo.extras.putCharSequence("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SURROUNDING_TEXT", charSequence != null ? new SpannableStringBuilder(charSequence) : null);
        editorInfo.extras.putInt("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_HEAD", i8);
        editorInfo.extras.putInt("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_END", i9);
    }

    private static void h(EditorInfo editorInfo, CharSequence charSequence, int i8, int i9) {
        int i10 = i9 - i8;
        int i11 = i10 > 1024 ? 0 : i10;
        int i12 = 2048 - i11;
        int min = Math.min(charSequence.length() - i9, i12 - Math.min(i8, (int) (i12 * 0.8d)));
        int min2 = Math.min(i8, i12 - min);
        int i13 = i8 - min2;
        if (b(charSequence, i13, 0)) {
            i13++;
            min2--;
        }
        if (b(charSequence, (i9 + min) - 1, 1)) {
            min--;
        }
        CharSequence concat = i11 != i10 ? TextUtils.concat(charSequence.subSequence(i13, i13 + min2), charSequence.subSequence(i9, min + i9)) : charSequence.subSequence(i13, min2 + i11 + min + i13);
        int i14 = min2 + 0;
        g(editorInfo, concat, i14, i11 + i14);
    }
}
