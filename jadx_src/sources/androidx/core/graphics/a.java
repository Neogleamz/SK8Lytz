package androidx.core.graphics;

import android.graphics.Bitmap;
import android.os.Build;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: androidx.core.graphics.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class C0034a {
        static int a(Bitmap bitmap) {
            return bitmap.getAllocationByteCount();
        }
    }

    public static int a(Bitmap bitmap) {
        return Build.VERSION.SDK_INT >= 19 ? C0034a.a(bitmap) : bitmap.getByteCount();
    }
}
