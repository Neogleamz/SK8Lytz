package androidx.transition;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class a0 {

    /* renamed from: a  reason: collision with root package name */
    private static boolean f7512a = true;

    /* renamed from: b  reason: collision with root package name */
    private static Method f7513b;

    /* renamed from: c  reason: collision with root package name */
    private static boolean f7514c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(ViewGroup viewGroup, int i8) {
        if (Build.VERSION.SDK_INT >= 29) {
            return viewGroup.getChildDrawingOrder(i8);
        }
        if (!f7514c) {
            try {
                Class cls = Integer.TYPE;
                Method declaredMethod = ViewGroup.class.getDeclaredMethod("getChildDrawingOrder", cls, cls);
                f7513b = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (NoSuchMethodException unused) {
            }
            f7514c = true;
        }
        Method method = f7513b;
        if (method != null) {
            try {
                return ((Integer) method.invoke(viewGroup, Integer.valueOf(viewGroup.getChildCount()), Integer.valueOf(i8))).intValue();
            } catch (IllegalAccessException | InvocationTargetException unused2) {
            }
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static z b(ViewGroup viewGroup) {
        return Build.VERSION.SDK_INT >= 18 ? new y(viewGroup) : x.g(viewGroup);
    }

    @SuppressLint({"NewApi"})
    private static void c(ViewGroup viewGroup, boolean z4) {
        if (f7512a) {
            try {
                viewGroup.suppressLayout(z4);
            } catch (NoSuchMethodError unused) {
                f7512a = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void d(ViewGroup viewGroup, boolean z4) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 29) {
            viewGroup.suppressLayout(z4);
        } else if (i8 >= 18) {
            c(viewGroup, z4);
        } else {
            b0.b(viewGroup, z4);
        }
    }
}
