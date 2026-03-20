package androidx.core.view;

import android.graphics.Rect;
import android.os.Build;
import android.view.Gravity;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static void a(int i8, int i9, int i10, Rect rect, int i11, int i12, Rect rect2, int i13) {
            Gravity.apply(i8, i9, i10, rect, i11, i12, rect2, i13);
        }

        static void b(int i8, int i9, int i10, Rect rect, Rect rect2, int i11) {
            Gravity.apply(i8, i9, i10, rect, rect2, i11);
        }

        static void c(int i8, Rect rect, Rect rect2, int i9) {
            Gravity.applyDisplay(i8, rect, rect2, i9);
        }
    }

    public static void a(int i8, int i9, int i10, Rect rect, Rect rect2, int i11) {
        if (Build.VERSION.SDK_INT >= 17) {
            a.b(i8, i9, i10, rect, rect2, i11);
        } else {
            Gravity.apply(i8, i9, i10, rect, rect2);
        }
    }

    public static int b(int i8, int i9) {
        return Build.VERSION.SDK_INT >= 17 ? Gravity.getAbsoluteGravity(i8, i9) : i8 & (-8388609);
    }
}
