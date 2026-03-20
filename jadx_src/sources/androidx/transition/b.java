package androidx.transition;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Build;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class b {

    /* renamed from: a  reason: collision with root package name */
    private static Method f7515a;

    /* renamed from: b  reason: collision with root package name */
    private static Method f7516b;

    /* renamed from: c  reason: collision with root package name */
    private static boolean f7517c;

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"SoonBlockedPrivateApi"})
    public static void a(Canvas canvas, boolean z4) {
        Method method;
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 21) {
            return;
        }
        if (i8 >= 29) {
            if (z4) {
                canvas.enableZ();
            } else {
                canvas.disableZ();
            }
        } else if (i8 == 28) {
            throw new IllegalStateException("This method doesn't work on Pie!");
        } else {
            if (!f7517c) {
                try {
                    Method declaredMethod = Canvas.class.getDeclaredMethod("insertReorderBarrier", new Class[0]);
                    f7515a = declaredMethod;
                    declaredMethod.setAccessible(true);
                    Method declaredMethod2 = Canvas.class.getDeclaredMethod("insertInorderBarrier", new Class[0]);
                    f7516b = declaredMethod2;
                    declaredMethod2.setAccessible(true);
                } catch (NoSuchMethodException unused) {
                }
                f7517c = true;
            }
            if (z4) {
                try {
                    Method method2 = f7515a;
                    if (method2 != null) {
                        method2.invoke(canvas, new Object[0]);
                    }
                } catch (IllegalAccessException unused2) {
                    return;
                } catch (InvocationTargetException e8) {
                    throw new RuntimeException(e8.getCause());
                }
            }
            if (z4 || (method = f7516b) == null) {
                return;
            }
            method.invoke(canvas, new Object[0]);
        }
    }
}
