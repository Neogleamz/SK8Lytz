package androidx.core.app;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: a  reason: collision with root package name */
    protected static final Class<?> f4454a;

    /* renamed from: b  reason: collision with root package name */
    protected static final Field f4455b;

    /* renamed from: c  reason: collision with root package name */
    protected static final Field f4456c;

    /* renamed from: d  reason: collision with root package name */
    protected static final Method f4457d;

    /* renamed from: e  reason: collision with root package name */
    protected static final Method f4458e;

    /* renamed from: f  reason: collision with root package name */
    protected static final Method f4459f;

    /* renamed from: g  reason: collision with root package name */
    private static final Handler f4460g = new Handler(Looper.getMainLooper());

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ C0029d f4461a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Object f4462b;

        a(C0029d c0029d, Object obj) {
            this.f4461a = c0029d;
            this.f4462b = obj;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f4461a.f4467a = this.f4462b;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Application f4463a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ C0029d f4464b;

        b(Application application, C0029d c0029d) {
            this.f4463a = application;
            this.f4464b = c0029d;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f4463a.unregisterActivityLifecycleCallbacks(this.f4464b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Object f4465a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Object f4466b;

        c(Object obj, Object obj2) {
            this.f4465a = obj;
            this.f4466b = obj2;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Method method = d.f4457d;
                if (method != null) {
                    method.invoke(this.f4465a, this.f4466b, Boolean.FALSE, "AppCompat recreation");
                } else {
                    d.f4458e.invoke(this.f4465a, this.f4466b, Boolean.FALSE);
                }
            } catch (RuntimeException e8) {
                if (e8.getClass() == RuntimeException.class && e8.getMessage() != null && e8.getMessage().startsWith("Unable to stop")) {
                    throw e8;
                }
            } catch (Throwable th) {
                Log.e("ActivityRecreator", "Exception while invoking performStopActivity", th);
            }
        }
    }

    /* renamed from: androidx.core.app.d$d  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class C0029d implements Application.ActivityLifecycleCallbacks {

        /* renamed from: a  reason: collision with root package name */
        Object f4467a;

        /* renamed from: b  reason: collision with root package name */
        private Activity f4468b;

        /* renamed from: c  reason: collision with root package name */
        private final int f4469c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f4470d = false;

        /* renamed from: e  reason: collision with root package name */
        private boolean f4471e = false;

        /* renamed from: f  reason: collision with root package name */
        private boolean f4472f = false;

        C0029d(Activity activity) {
            this.f4468b = activity;
            this.f4469c = activity.hashCode();
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity activity) {
            if (this.f4468b == activity) {
                this.f4468b = null;
                this.f4471e = true;
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
            if (!this.f4471e || this.f4472f || this.f4470d || !d.h(this.f4467a, this.f4469c, activity)) {
                return;
            }
            this.f4472f = true;
            this.f4467a = null;
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity activity) {
            if (this.f4468b == activity) {
                this.f4470d = true;
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
        }
    }

    static {
        Class<?> a9 = a();
        f4454a = a9;
        f4455b = b();
        f4456c = f();
        f4457d = d(a9);
        f4458e = c(a9);
        f4459f = e(a9);
    }

    private static Class<?> a() {
        try {
            return Class.forName("android.app.ActivityThread");
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Field b() {
        try {
            Field declaredField = Activity.class.getDeclaredField("mMainThread");
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Method c(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        try {
            Method declaredMethod = cls.getDeclaredMethod("performStopActivity", IBinder.class, Boolean.TYPE);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Method d(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        try {
            Method declaredMethod = cls.getDeclaredMethod("performStopActivity", IBinder.class, Boolean.TYPE, String.class);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Method e(Class<?> cls) {
        if (g() && cls != null) {
            try {
                Class<?> cls2 = Boolean.TYPE;
                Method declaredMethod = cls.getDeclaredMethod("requestRelaunchActivity", IBinder.class, List.class, List.class, Integer.TYPE, cls2, Configuration.class, Configuration.class, cls2, cls2);
                declaredMethod.setAccessible(true);
                return declaredMethod;
            } catch (Throwable unused) {
            }
        }
        return null;
    }

    private static Field f() {
        try {
            Field declaredField = Activity.class.getDeclaredField("mToken");
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Throwable unused) {
            return null;
        }
    }

    private static boolean g() {
        int i8 = Build.VERSION.SDK_INT;
        return i8 == 26 || i8 == 27;
    }

    protected static boolean h(Object obj, int i8, Activity activity) {
        try {
            Object obj2 = f4456c.get(activity);
            if (obj2 == obj && activity.hashCode() == i8) {
                f4460g.postAtFrontOfQueue(new c(f4455b.get(activity), obj2));
                return true;
            }
            return false;
        } catch (Throwable th) {
            Log.e("ActivityRecreator", "Exception while fetching field values", th);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean i(Activity activity) {
        Object obj;
        if (Build.VERSION.SDK_INT >= 28) {
            activity.recreate();
            return true;
        } else if (g() && f4459f == null) {
            return false;
        } else {
            if (f4458e == null && f4457d == null) {
                return false;
            }
            try {
                Object obj2 = f4456c.get(activity);
                if (obj2 == null || (obj = f4455b.get(activity)) == null) {
                    return false;
                }
                Application application = activity.getApplication();
                C0029d c0029d = new C0029d(activity);
                application.registerActivityLifecycleCallbacks(c0029d);
                Handler handler = f4460g;
                handler.post(new a(c0029d, obj2));
                if (g()) {
                    Method method = f4459f;
                    Boolean bool = Boolean.FALSE;
                    method.invoke(obj, obj2, null, null, 0, bool, null, null, bool, bool);
                } else {
                    activity.recreate();
                }
                handler.post(new b(application, c0029d));
                return true;
            } catch (Throwable unused) {
                return false;
            }
        }
    }
}
