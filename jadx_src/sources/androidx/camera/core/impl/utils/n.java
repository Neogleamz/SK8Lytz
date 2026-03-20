package androidx.camera.core.impl.utils;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Size;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class n {

    /* renamed from: a  reason: collision with root package name */
    public static final RectF f2658a = new RectF(-1.0f, -1.0f, 1.0f, 1.0f);

    public static Matrix a(Rect rect) {
        return b(new RectF(rect));
    }

    public static Matrix b(RectF rectF) {
        Matrix matrix = new Matrix();
        matrix.setRectToRect(f2658a, rectF, Matrix.ScaleToFit.FILL);
        return matrix;
    }

    public static Matrix c(RectF rectF, RectF rectF2, int i8) {
        return d(rectF, rectF2, i8, false);
    }

    public static Matrix d(RectF rectF, RectF rectF2, int i8, boolean z4) {
        Matrix matrix = new Matrix();
        matrix.setRectToRect(rectF, f2658a, Matrix.ScaleToFit.FILL);
        matrix.postRotate(i8);
        if (z4) {
            matrix.postScale(-1.0f, 1.0f);
        }
        matrix.postConcat(b(rectF2));
        return matrix;
    }

    public static boolean e(Rect rect, Size size) {
        return (rect.left == 0 && rect.top == 0 && rect.width() == size.getWidth() && rect.height() == size.getHeight()) ? false : true;
    }

    public static boolean f(int i8) {
        if (i8 == 90 || i8 == 270) {
            return true;
        }
        if (i8 == 0 || i8 == 180) {
            return false;
        }
        throw new IllegalArgumentException("Invalid rotation degrees: " + i8);
    }

    public static boolean g(Size size, boolean z4, Size size2, boolean z8) {
        float width;
        float width2;
        float width3;
        float f5;
        if (z4) {
            width = size.getWidth() / size.getHeight();
            width2 = width;
        } else {
            width = (size.getWidth() + 1.0f) / (size.getHeight() - 1.0f);
            width2 = (size.getWidth() - 1.0f) / (size.getHeight() + 1.0f);
        }
        if (z8) {
            width3 = size2.getWidth() / size2.getHeight();
            f5 = width3;
        } else {
            float width4 = (size2.getWidth() + 1.0f) / (size2.getHeight() - 1.0f);
            width3 = (size2.getWidth() - 1.0f) / (size2.getHeight() + 1.0f);
            f5 = width4;
        }
        return width >= width3 && f5 >= width2;
    }

    public static Size h(Rect rect) {
        return new Size(rect.width(), rect.height());
    }

    public static Size i(Size size) {
        return new Size(size.getHeight(), size.getWidth());
    }

    public static Size j(Size size, int i8) {
        boolean z4 = i8 % 90 == 0;
        androidx.core.util.h.b(z4, "Invalid rotation degrees: " + i8);
        return f(p(i8)) ? i(size) : size;
    }

    public static Rect k(Size size) {
        return l(size, 0, 0);
    }

    public static Rect l(Size size, int i8, int i9) {
        return new Rect(i8, i9, size.getWidth() + i8, size.getHeight() + i9);
    }

    public static RectF m(Size size) {
        return n(size, 0, 0);
    }

    public static RectF n(Size size, int i8, int i9) {
        return new RectF(i8, i9, i8 + size.getWidth(), i9 + size.getHeight());
    }

    public static Matrix o(Matrix matrix, Rect rect) {
        Matrix matrix2 = new Matrix(matrix);
        matrix2.postTranslate(-rect.left, -rect.top);
        return matrix2;
    }

    public static int p(int i8) {
        return ((i8 % 360) + 360) % 360;
    }
}
