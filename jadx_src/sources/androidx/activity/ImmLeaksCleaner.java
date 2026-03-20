package androidx.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.lifecycle.Lifecycle;
import java.lang.reflect.Field;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class ImmLeaksCleaner implements androidx.lifecycle.h {

    /* renamed from: b  reason: collision with root package name */
    private static int f367b;

    /* renamed from: c  reason: collision with root package name */
    private static Field f368c;

    /* renamed from: d  reason: collision with root package name */
    private static Field f369d;

    /* renamed from: e  reason: collision with root package name */
    private static Field f370e;

    /* renamed from: a  reason: collision with root package name */
    private Activity f371a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmLeaksCleaner(Activity activity) {
        this.f371a = activity;
    }

    @SuppressLint({"SoonBlockedPrivateApi"})
    private static void a() {
        try {
            f367b = 2;
            Field declaredField = InputMethodManager.class.getDeclaredField("mServedView");
            f369d = declaredField;
            declaredField.setAccessible(true);
            Field declaredField2 = InputMethodManager.class.getDeclaredField("mNextServedView");
            f370e = declaredField2;
            declaredField2.setAccessible(true);
            Field declaredField3 = InputMethodManager.class.getDeclaredField("mH");
            f368c = declaredField3;
            declaredField3.setAccessible(true);
            f367b = 1;
        } catch (NoSuchFieldException unused) {
        }
    }

    @Override // androidx.lifecycle.h
    public void c(androidx.lifecycle.j jVar, Lifecycle.Event event) {
        if (event != Lifecycle.Event.ON_DESTROY) {
            return;
        }
        if (f367b == 0) {
            a();
        }
        if (f367b == 1) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.f371a.getSystemService("input_method");
            try {
                Object obj = f368c.get(inputMethodManager);
                if (obj == null) {
                    return;
                }
                synchronized (obj) {
                    try {
                        try {
                            View view = (View) f369d.get(inputMethodManager);
                            if (view == null) {
                                return;
                            }
                            if (view.isAttachedToWindow()) {
                                return;
                            }
                            try {
                                f370e.set(inputMethodManager, null);
                                inputMethodManager.isActive();
                            } catch (IllegalAccessException unused) {
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    } catch (ClassCastException unused2) {
                    } catch (IllegalAccessException unused3) {
                    }
                }
            } catch (IllegalAccessException unused4) {
            }
        }
    }
}
