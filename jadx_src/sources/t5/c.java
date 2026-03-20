package t5;

import android.text.Spannable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {
    public static void a(Spannable spannable, Object obj, int i8, int i9, int i10) {
        Object[] spans;
        for (Object obj2 : spannable.getSpans(i8, i9, obj.getClass())) {
            if (spannable.getSpanStart(obj2) == i8 && spannable.getSpanEnd(obj2) == i9 && spannable.getSpanFlags(obj2) == i10) {
                spannable.removeSpan(obj2);
            }
        }
        spannable.setSpan(obj, i8, i9, i10);
    }
}
