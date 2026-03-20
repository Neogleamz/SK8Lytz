package androidx.lifecycle;

import android.app.Application;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b0 {

    /* renamed from: a  reason: collision with root package name */
    private static final List<Class<?>> f5855a = kotlin.collections.q.h(new Class[]{Application.class, w.class});

    /* renamed from: b  reason: collision with root package name */
    private static final List<Class<?>> f5856b = kotlin.collections.q.d(w.class);

    public static final <T> Constructor<T> c(Class<T> cls, List<? extends Class<?>> list) {
        kotlin.jvm.internal.p.e(cls, "modelClass");
        kotlin.jvm.internal.p.e(list, "signature");
        Constructor<?>[] constructors = cls.getConstructors();
        kotlin.jvm.internal.p.d(constructors, "modelClass.constructors");
        for (Constructor<?> constructor : constructors) {
            Constructor<T> constructor2 = (Constructor<T>) constructor;
            Class<?>[] parameterTypes = constructor2.getParameterTypes();
            kotlin.jvm.internal.p.d(parameterTypes, "constructor.parameterTypes");
            List G = kotlin.collections.j.G(parameterTypes);
            if (kotlin.jvm.internal.p.a(list, G)) {
                kotlin.jvm.internal.p.c(constructor2, "null cannot be cast to non-null type java.lang.reflect.Constructor<T of androidx.lifecycle.SavedStateViewModelFactoryKt.findMatchingConstructor>");
                return constructor2;
            } else if (list.size() == G.size() && G.containsAll(list)) {
                throw new UnsupportedOperationException("Class " + cls.getSimpleName() + " must have parameters in the proper order: " + list);
            }
        }
        return null;
    }

    public static final <T extends e0> T d(Class<T> cls, Constructor<T> constructor, Object... objArr) {
        kotlin.jvm.internal.p.e(cls, "modelClass");
        kotlin.jvm.internal.p.e(constructor, "constructor");
        kotlin.jvm.internal.p.e(objArr, "params");
        try {
            return constructor.newInstance(Arrays.copyOf(objArr, objArr.length));
        } catch (IllegalAccessException e8) {
            throw new RuntimeException("Failed to access " + cls, e8);
        } catch (InstantiationException e9) {
            throw new RuntimeException("A " + cls + " cannot be instantiated.", e9);
        } catch (InvocationTargetException e10) {
            throw new RuntimeException("An exception happened in constructor of " + cls, e10.getCause());
        }
    }
}
