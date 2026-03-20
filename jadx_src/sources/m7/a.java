package m7;

import android.graphics.Canvas;
import android.os.Build;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {
    public static int a(Canvas canvas, float f5, float f8, float f9, float f10, int i8) {
        return Build.VERSION.SDK_INT > 21 ? canvas.saveLayerAlpha(f5, f8, f9, f10, i8) : canvas.saveLayerAlpha(f5, f8, f9, f10, i8, 31);
    }
}
