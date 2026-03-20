package androidx.core.widget;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import androidx.core.view.c0;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j {

    /* renamed from: a  reason: collision with root package name */
    private static Method f5152a;

    /* renamed from: b  reason: collision with root package name */
    private static boolean f5153b;

    /* renamed from: c  reason: collision with root package name */
    private static Field f5154c;

    /* renamed from: d  reason: collision with root package name */
    private static boolean f5155d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static void a(PopupWindow popupWindow, View view, int i8, int i9, int i10) {
            popupWindow.showAsDropDown(view, i8, i9, i10);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {
        static boolean a(PopupWindow popupWindow) {
            return popupWindow.getOverlapAnchor();
        }

        static int b(PopupWindow popupWindow) {
            return popupWindow.getWindowLayoutType();
        }

        static void c(PopupWindow popupWindow, boolean z4) {
            popupWindow.setOverlapAnchor(z4);
        }

        static void d(PopupWindow popupWindow, int i8) {
            popupWindow.setWindowLayoutType(i8);
        }
    }

    public static void a(PopupWindow popupWindow, boolean z4) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 23) {
            b.c(popupWindow, z4);
        } else if (i8 >= 21) {
            if (!f5155d) {
                try {
                    Field declaredField = PopupWindow.class.getDeclaredField("mOverlapAnchor");
                    f5154c = declaredField;
                    declaredField.setAccessible(true);
                } catch (NoSuchFieldException e8) {
                    Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", e8);
                }
                f5155d = true;
            }
            Field field = f5154c;
            if (field != null) {
                try {
                    field.set(popupWindow, Boolean.valueOf(z4));
                } catch (IllegalAccessException e9) {
                    Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", e9);
                }
            }
        }
    }

    public static void b(PopupWindow popupWindow, int i8) {
        if (Build.VERSION.SDK_INT >= 23) {
            b.d(popupWindow, i8);
            return;
        }
        if (!f5153b) {
            try {
                Method declaredMethod = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", Integer.TYPE);
                f5152a = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (Exception unused) {
            }
            f5153b = true;
        }
        Method method = f5152a;
        if (method != null) {
            try {
                method.invoke(popupWindow, Integer.valueOf(i8));
            } catch (Exception unused2) {
            }
        }
    }

    public static void c(PopupWindow popupWindow, View view, int i8, int i9, int i10) {
        if (Build.VERSION.SDK_INT >= 19) {
            a.a(popupWindow, view, i8, i9, i10);
            return;
        }
        if ((androidx.core.view.f.b(i10, c0.E(view)) & 7) == 5) {
            i8 -= popupWindow.getWidth() - view.getWidth();
        }
        popupWindow.showAsDropDown(view, i8, i9);
    }
}
