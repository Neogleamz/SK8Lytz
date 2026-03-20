package b0;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Rational;
import android.util.Size;
import androidx.camera.core.a3;
import androidx.camera.core.internal.utils.ImageUtil;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k {
    public static Map<a3, Rect> a(Rect rect, boolean z4, Rational rational, int i8, int i9, int i10, Map<a3, Size> map) {
        androidx.core.util.h.b(rect.width() > 0 && rect.height() > 0, "Cannot compute viewport crop rects zero sized sensor rect.");
        RectF rectF = new RectF(rect);
        HashMap hashMap = new HashMap();
        RectF rectF2 = new RectF(rect);
        for (Map.Entry<a3, Size> entry : map.entrySet()) {
            Matrix matrix = new Matrix();
            RectF rectF3 = new RectF(0.0f, 0.0f, entry.getValue().getWidth(), entry.getValue().getHeight());
            matrix.setRectToRect(rectF3, rectF, Matrix.ScaleToFit.CENTER);
            hashMap.put(entry.getKey(), matrix);
            RectF rectF4 = new RectF();
            matrix.mapRect(rectF4, rectF3);
            rectF2.intersect(rectF4);
        }
        RectF g8 = g(rectF2, ImageUtil.c(i8, rational), i9, z4, i10, i8);
        HashMap hashMap2 = new HashMap();
        RectF rectF5 = new RectF();
        Matrix matrix2 = new Matrix();
        for (Map.Entry entry2 : hashMap.entrySet()) {
            ((Matrix) entry2.getValue()).invert(matrix2);
            matrix2.mapRect(rectF5, g8);
            Rect rect2 = new Rect();
            rectF5.round(rect2);
            hashMap2.put((a3) entry2.getKey(), rect2);
        }
        return hashMap2;
    }

    private static RectF b(boolean z4, int i8, RectF rectF, RectF rectF2) {
        boolean z8 = true;
        boolean z9 = i8 == 0 && !z4;
        boolean z10 = i8 == 90 && z4;
        if (z9 || z10) {
            return rectF2;
        }
        boolean z11 = i8 == 0 && z4;
        boolean z12 = i8 == 270 && !z4;
        if (z11 || z12) {
            return c(rectF2, rectF.centerX());
        }
        boolean z13 = i8 == 90 && !z4;
        boolean z14 = i8 == 180 && z4;
        if (z13 || z14) {
            return d(rectF2, rectF.centerY());
        }
        boolean z15 = i8 == 180 && !z4;
        if (i8 != 270 || !z4) {
            z8 = false;
        }
        if (z15 || z8) {
            return c(d(rectF2, rectF.centerY()), rectF.centerX());
        }
        throw new IllegalArgumentException("Invalid argument: mirrored " + z4 + " rotation " + i8);
    }

    private static RectF c(RectF rectF, float f5) {
        return new RectF(e(rectF.right, f5), rectF.top, e(rectF.left, f5), rectF.bottom);
    }

    private static RectF d(RectF rectF, float f5) {
        return new RectF(rectF.left, f(rectF.bottom, f5), rectF.right, f(rectF.top, f5));
    }

    private static float e(float f5, float f8) {
        return (f8 + f8) - f5;
    }

    private static float f(float f5, float f8) {
        return (f8 + f8) - f5;
    }

    @SuppressLint({"SwitchIntDef"})
    public static RectF g(RectF rectF, Rational rational, int i8, boolean z4, int i9, int i10) {
        Matrix.ScaleToFit scaleToFit;
        if (i8 == 3) {
            return rectF;
        }
        Matrix matrix = new Matrix();
        RectF rectF2 = new RectF(0.0f, 0.0f, rational.getNumerator(), rational.getDenominator());
        if (i8 == 0) {
            scaleToFit = Matrix.ScaleToFit.START;
        } else if (i8 == 1) {
            scaleToFit = Matrix.ScaleToFit.CENTER;
        } else if (i8 != 2) {
            throw new IllegalStateException("Unexpected scale type: " + i8);
        } else {
            scaleToFit = Matrix.ScaleToFit.END;
        }
        matrix.setRectToRect(rectF2, rectF, scaleToFit);
        RectF rectF3 = new RectF();
        matrix.mapRect(rectF3, rectF2);
        return b(h(z4, i9), i10, rectF, rectF3);
    }

    private static boolean h(boolean z4, int i8) {
        return z4 ^ (i8 == 1);
    }
}
