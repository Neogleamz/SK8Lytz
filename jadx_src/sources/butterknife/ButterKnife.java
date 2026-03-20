package butterknife;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ButterKnife {

    /* renamed from: a  reason: collision with root package name */
    private static boolean f8185a = false;

    /* renamed from: b  reason: collision with root package name */
    static final Map<Class<?>, Constructor<? extends Unbinder>> f8186b = new LinkedHashMap();

    private ButterKnife() {
        throw new AssertionError("No instances.");
    }

    public static Unbinder a(Activity activity) {
        return b(activity, activity.getWindow().getDecorView());
    }

    public static Unbinder b(Object obj, View view) {
        Class<?> cls = obj.getClass();
        if (f8185a) {
            Log.d("ButterKnife", "Looking up binding for " + cls.getName());
        }
        Constructor<? extends Unbinder> c9 = c(cls);
        if (c9 == null) {
            return Unbinder.f8203a;
        }
        try {
            return c9.newInstance(obj, view);
        } catch (IllegalAccessException e8) {
            throw new RuntimeException("Unable to invoke " + c9, e8);
        } catch (InstantiationException e9) {
            throw new RuntimeException("Unable to invoke " + c9, e9);
        } catch (InvocationTargetException e10) {
            Throwable cause = e10.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new RuntimeException("Unable to create binding instance.", cause);
        }
    }

    private static Constructor<? extends Unbinder> c(Class<?> cls) {
        Constructor<? extends Unbinder> c9;
        Map<Class<?>, Constructor<? extends Unbinder>> map = f8186b;
        Constructor<? extends Unbinder> constructor = map.get(cls);
        if (constructor != null || map.containsKey(cls)) {
            if (f8185a) {
                Log.d("ButterKnife", "HIT: Cached in binding map.");
            }
            return constructor;
        }
        String name = cls.getName();
        if (name.startsWith("android.") || name.startsWith("java.") || name.startsWith("androidx.")) {
            if (f8185a) {
                Log.d("ButterKnife", "MISS: Reached framework class. Abandoning search.");
                return null;
            }
            return null;
        }
        try {
            ClassLoader classLoader = cls.getClassLoader();
            c9 = classLoader.loadClass(name + "_ViewBinding").getConstructor(cls, View.class);
            if (f8185a) {
                Log.d("ButterKnife", "HIT: Loaded binding class and constructor.");
            }
        } catch (ClassNotFoundException unused) {
            if (f8185a) {
                Log.d("ButterKnife", "Not found. Trying superclass " + cls.getSuperclass().getName());
            }
            c9 = c(cls.getSuperclass());
        } catch (NoSuchMethodException e8) {
            throw new RuntimeException("Unable to find binding constructor for " + name, e8);
        }
        f8186b.put(cls, c9);
        return c9;
    }
}
