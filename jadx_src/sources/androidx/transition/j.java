package androidx.transition;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;
import java.lang.reflect.Field;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class j {

    /* renamed from: a  reason: collision with root package name */
    private static boolean f7574a = true;

    /* renamed from: b  reason: collision with root package name */
    private static Field f7575b;

    /* renamed from: c  reason: collision with root package name */
    private static boolean f7576c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(ImageView imageView, Matrix matrix) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 29) {
            imageView.animateTransform(matrix);
            return;
        }
        if (matrix == null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable == null) {
                return;
            }
            drawable.setBounds(0, 0, (imageView.getWidth() - imageView.getPaddingLeft()) - imageView.getPaddingRight(), (imageView.getHeight() - imageView.getPaddingTop()) - imageView.getPaddingBottom());
        } else if (i8 >= 21) {
            c(imageView, matrix);
            return;
        } else {
            Drawable drawable2 = imageView.getDrawable();
            if (drawable2 == null) {
                return;
            }
            drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
            Matrix matrix2 = null;
            b();
            Field field = f7575b;
            if (field != null) {
                try {
                    Matrix matrix3 = (Matrix) field.get(imageView);
                    if (matrix3 == null) {
                        try {
                            matrix2 = new Matrix();
                            f7575b.set(imageView, matrix2);
                        } catch (IllegalAccessException unused) {
                        }
                    }
                    matrix2 = matrix3;
                } catch (IllegalAccessException unused2) {
                }
            }
            if (matrix2 != null) {
                matrix2.set(matrix);
            }
        }
        imageView.invalidate();
    }

    private static void b() {
        if (f7576c) {
            return;
        }
        try {
            Field declaredField = ImageView.class.getDeclaredField("mDrawMatrix");
            f7575b = declaredField;
            declaredField.setAccessible(true);
        } catch (NoSuchFieldException unused) {
        }
        f7576c = true;
    }

    @SuppressLint({"NewApi"})
    private static void c(ImageView imageView, Matrix matrix) {
        if (f7574a) {
            try {
                imageView.animateTransform(matrix);
            } catch (NoSuchMethodError unused) {
                f7574a = false;
            }
        }
    }
}
