package androidx.transition;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class l0 {

    /* renamed from: b  reason: collision with root package name */
    private static Method f7579b;

    /* renamed from: c  reason: collision with root package name */
    private static boolean f7580c;

    /* renamed from: d  reason: collision with root package name */
    private static Field f7581d;

    /* renamed from: e  reason: collision with root package name */
    private static boolean f7582e;

    /* renamed from: a  reason: collision with root package name */
    private float[] f7583a;

    @SuppressLint({"PrivateApi"})
    private void b() {
        if (f7580c) {
            return;
        }
        try {
            Class cls = Integer.TYPE;
            Method declaredMethod = View.class.getDeclaredMethod("setFrame", cls, cls, cls, cls);
            f7579b = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (NoSuchMethodException e8) {
            Log.i("ViewUtilsBase", "Failed to retrieve setFrame method", e8);
        }
        f7580c = true;
    }

    public void a(View view) {
        if (view.getVisibility() == 0) {
            view.setTag(x1.b.f23763d, null);
        }
    }

    public float c(View view) {
        Float f5 = (Float) view.getTag(x1.b.f23763d);
        float alpha = view.getAlpha();
        return f5 != null ? alpha / f5.floatValue() : alpha;
    }

    public void d(View view) {
        int i8 = x1.b.f23763d;
        if (view.getTag(i8) == null) {
            view.setTag(i8, Float.valueOf(view.getAlpha()));
        }
    }

    public void e(View view, Matrix matrix) {
        if (matrix == null || matrix.isIdentity()) {
            view.setPivotX(view.getWidth() / 2);
            view.setPivotY(view.getHeight() / 2);
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
            view.setRotation(0.0f);
            return;
        }
        float[] fArr = this.f7583a;
        if (fArr == null) {
            fArr = new float[9];
            this.f7583a = fArr;
        }
        matrix.getValues(fArr);
        float f5 = fArr[3];
        float sqrt = ((float) Math.sqrt(1.0f - (f5 * f5))) * (fArr[0] < 0.0f ? -1 : 1);
        float degrees = (float) Math.toDegrees(Math.atan2(f5, sqrt));
        float f8 = fArr[0] / sqrt;
        float f9 = fArr[4] / sqrt;
        float f10 = fArr[2];
        float f11 = fArr[5];
        view.setPivotX(0.0f);
        view.setPivotY(0.0f);
        view.setTranslationX(f10);
        view.setTranslationY(f11);
        view.setRotation(degrees);
        view.setScaleX(f8);
        view.setScaleY(f9);
    }

    public void f(View view, int i8, int i9, int i10, int i11) {
        b();
        Method method = f7579b;
        if (method != null) {
            try {
                method.invoke(view, Integer.valueOf(i8), Integer.valueOf(i9), Integer.valueOf(i10), Integer.valueOf(i11));
            } catch (IllegalAccessException unused) {
            } catch (InvocationTargetException e8) {
                throw new RuntimeException(e8.getCause());
            }
        }
    }

    public void g(View view, float f5) {
        Float f8 = (Float) view.getTag(x1.b.f23763d);
        if (f8 != null) {
            view.setAlpha(f8.floatValue() * f5);
        } else {
            view.setAlpha(f5);
        }
    }

    public void h(View view, int i8) {
        if (!f7582e) {
            try {
                Field declaredField = View.class.getDeclaredField("mViewFlags");
                f7581d = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException unused) {
                Log.i("ViewUtilsBase", "fetchViewFlagsField: ");
            }
            f7582e = true;
        }
        Field field = f7581d;
        if (field != null) {
            try {
                f7581d.setInt(view, i8 | (field.getInt(view) & (-13)));
            } catch (IllegalAccessException unused2) {
            }
        }
    }

    public void i(View view, Matrix matrix) {
        ViewParent parent = view.getParent();
        if (parent instanceof View) {
            View view2 = (View) parent;
            i(view2, matrix);
            matrix.preTranslate(-view2.getScrollX(), -view2.getScrollY());
        }
        matrix.preTranslate(view.getLeft(), view.getTop());
        Matrix matrix2 = view.getMatrix();
        if (matrix2.isIdentity()) {
            return;
        }
        matrix.preConcat(matrix2);
    }

    public void j(View view, Matrix matrix) {
        ViewParent parent = view.getParent();
        if (parent instanceof View) {
            View view2 = (View) parent;
            j(view2, matrix);
            matrix.postTranslate(view2.getScrollX(), view2.getScrollY());
        }
        matrix.postTranslate(-view.getLeft(), -view.getTop());
        Matrix matrix2 = view.getMatrix();
        if (matrix2.isIdentity()) {
            return;
        }
        Matrix matrix3 = new Matrix();
        if (matrix2.invert(matrix3)) {
            matrix.postConcat(matrix3);
        }
    }
}
