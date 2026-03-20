package androidx.core.graphics;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: a  reason: collision with root package name */
    private static final ThreadLocal<androidx.core.util.d<Rect, Rect>> f4712a = new ThreadLocal<>();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static boolean a(Paint paint, String str) {
            return paint.hasGlyph(str);
        }
    }

    public static boolean a(Paint paint, String str) {
        if (Build.VERSION.SDK_INT >= 23) {
            return a.a(paint, str);
        }
        int length = str.length();
        if (length == 1 && Character.isWhitespace(str.charAt(0))) {
            return true;
        }
        float measureText = paint.measureText("\udfffd");
        float measureText2 = paint.measureText("m");
        float measureText3 = paint.measureText(str);
        float f5 = 0.0f;
        if (measureText3 == 0.0f) {
            return false;
        }
        if (str.codePointCount(0, str.length()) > 1) {
            if (measureText3 > measureText2 * 2.0f) {
                return false;
            }
            int i8 = 0;
            while (i8 < length) {
                int charCount = Character.charCount(str.codePointAt(i8)) + i8;
                f5 += paint.measureText(str, i8, charCount);
                i8 = charCount;
            }
            if (measureText3 >= f5) {
                return false;
            }
        }
        if (measureText3 != measureText) {
            return true;
        }
        androidx.core.util.d<Rect, Rect> b9 = b();
        paint.getTextBounds("\udfffd", 0, 2, b9.f4889a);
        paint.getTextBounds(str, 0, length, b9.f4890b);
        return !b9.f4889a.equals(b9.f4890b);
    }

    private static androidx.core.util.d<Rect, Rect> b() {
        ThreadLocal<androidx.core.util.d<Rect, Rect>> threadLocal = f4712a;
        androidx.core.util.d<Rect, Rect> dVar = threadLocal.get();
        if (dVar == null) {
            androidx.core.util.d<Rect, Rect> dVar2 = new androidx.core.util.d<>(new Rect(), new Rect());
            threadLocal.set(dVar2);
            return dVar2;
        }
        dVar.f4889a.setEmpty();
        dVar.f4890b.setEmpty();
        return dVar;
    }
}
