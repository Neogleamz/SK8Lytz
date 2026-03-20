package androidx.core.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.PointerIcon;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z {

    /* renamed from: a  reason: collision with root package name */
    private final PointerIcon f5086a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static PointerIcon a(Bitmap bitmap, float f5, float f8) {
            return PointerIcon.create(bitmap, f5, f8);
        }

        static PointerIcon b(Context context, int i8) {
            return PointerIcon.getSystemIcon(context, i8);
        }

        static PointerIcon c(Resources resources, int i8) {
            return PointerIcon.load(resources, i8);
        }
    }

    private z(PointerIcon pointerIcon) {
        this.f5086a = pointerIcon;
    }

    public static z b(Context context, int i8) {
        return Build.VERSION.SDK_INT >= 24 ? new z(a.b(context, i8)) : new z(null);
    }

    public Object a() {
        return this.f5086a;
    }
}
