package androidx.camera.core;

import android.graphics.Rect;
import android.util.Size;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class j2 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static j2 a(Size size, Rect rect, int i8) {
        return new i(size, rect, i8);
    }

    public abstract Rect b();

    public abstract Size c();

    public abstract int d();
}
