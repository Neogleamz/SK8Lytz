package androidx.appcompat.widget;

import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class u0 {

    /* renamed from: a  reason: collision with root package name */
    private static Method f1635a;

    /* renamed from: b  reason: collision with root package name */
    static final boolean f1636b;

    static {
        int i8 = Build.VERSION.SDK_INT;
        f1636b = i8 >= 27;
        if (i8 >= 18) {
            try {
                Method declaredMethod = View.class.getDeclaredMethod("computeFitSystemWindows", Rect.class, Rect.class);
                f1635a = declaredMethod;
                if (declaredMethod.isAccessible()) {
                    return;
                }
                f1635a.setAccessible(true);
            } catch (NoSuchMethodException unused) {
                Log.d("ViewUtils", "Could not find method computeFitSystemWindows. Oh well.");
            }
        }
    }

    public static void a(View view, Rect rect, Rect rect2) {
        Method method = f1635a;
        if (method != null) {
            try {
                method.invoke(view, rect, rect2);
            } catch (Exception e8) {
                Log.d("ViewUtils", "Could not invoke computeFitSystemWindows", e8);
            }
        }
    }

    public static boolean b(View view) {
        return androidx.core.view.c0.E(view) == 1;
    }

    public static void c(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            try {
                Method method = view.getClass().getMethod("makeOptionalFitsSystemWindows", new Class[0]);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(view, new Object[0]);
            } catch (IllegalAccessException e8) {
                e = e8;
                Log.d("ViewUtils", "Could not invoke makeOptionalFitsSystemWindows", e);
            } catch (NoSuchMethodException unused) {
                Log.d("ViewUtils", "Could not find method makeOptionalFitsSystemWindows. Oh well...");
            } catch (InvocationTargetException e9) {
                e = e9;
                Log.d("ViewUtils", "Could not invoke makeOptionalFitsSystemWindows", e);
            }
        }
    }
}
