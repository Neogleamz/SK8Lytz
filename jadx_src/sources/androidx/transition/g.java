package androidx.transition;

import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class g implements e {

    /* renamed from: b  reason: collision with root package name */
    private static Class<?> f7555b;

    /* renamed from: c  reason: collision with root package name */
    private static boolean f7556c;

    /* renamed from: d  reason: collision with root package name */
    private static Method f7557d;

    /* renamed from: e  reason: collision with root package name */
    private static boolean f7558e;

    /* renamed from: f  reason: collision with root package name */
    private static Method f7559f;

    /* renamed from: g  reason: collision with root package name */
    private static boolean f7560g;

    /* renamed from: a  reason: collision with root package name */
    private final View f7561a;

    private g(View view) {
        this.f7561a = view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static e b(View view, ViewGroup viewGroup, Matrix matrix) {
        c();
        Method method = f7557d;
        if (method != null) {
            try {
                return new g((View) method.invoke(null, view, viewGroup, matrix));
            } catch (IllegalAccessException unused) {
            } catch (InvocationTargetException e8) {
                throw new RuntimeException(e8.getCause());
            }
        }
        return null;
    }

    private static void c() {
        if (f7558e) {
            return;
        }
        try {
            d();
            Method declaredMethod = f7555b.getDeclaredMethod("addGhost", View.class, ViewGroup.class, Matrix.class);
            f7557d = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (NoSuchMethodException e8) {
            Log.i("GhostViewApi21", "Failed to retrieve addGhost method", e8);
        }
        f7558e = true;
    }

    private static void d() {
        if (f7556c) {
            return;
        }
        try {
            f7555b = Class.forName("android.view.GhostView");
        } catch (ClassNotFoundException e8) {
            Log.i("GhostViewApi21", "Failed to retrieve GhostView class", e8);
        }
        f7556c = true;
    }

    private static void e() {
        if (f7560g) {
            return;
        }
        try {
            d();
            Method declaredMethod = f7555b.getDeclaredMethod("removeGhost", View.class);
            f7559f = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (NoSuchMethodException e8) {
            Log.i("GhostViewApi21", "Failed to retrieve removeGhost method", e8);
        }
        f7560g = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void f(View view) {
        e();
        Method method = f7559f;
        if (method != null) {
            try {
                method.invoke(null, view);
            } catch (IllegalAccessException unused) {
            } catch (InvocationTargetException e8) {
                throw new RuntimeException(e8.getCause());
            }
        }
    }

    @Override // androidx.transition.e
    public void a(ViewGroup viewGroup, View view) {
    }

    @Override // androidx.transition.e
    public void setVisibility(int i8) {
        this.f7561a.setVisibility(i8);
    }
}
