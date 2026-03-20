package androidx.lifecycle;

import com.daimajia.numberprogressbar.BuildConfig;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m {

    /* renamed from: a  reason: collision with root package name */
    public static final m f5897a = new m();

    /* renamed from: b  reason: collision with root package name */
    private static final Map<Class<?>, Integer> f5898b = new HashMap();

    /* renamed from: c  reason: collision with root package name */
    private static final Map<Class<?>, List<Constructor<? extends d>>> f5899c = new HashMap();

    private m() {
    }

    private final d a(Constructor<? extends d> constructor, Object obj) {
        try {
            d newInstance = constructor.newInstance(obj);
            kotlin.jvm.internal.p.d(newInstance, "{\n            constructo…tance(`object`)\n        }");
            return newInstance;
        } catch (IllegalAccessException e8) {
            throw new RuntimeException(e8);
        } catch (InstantiationException e9) {
            throw new RuntimeException(e9);
        } catch (InvocationTargetException e10) {
            throw new RuntimeException(e10);
        }
    }

    private final Constructor<? extends d> b(Class<?> cls) {
        try {
            Package r02 = cls.getPackage();
            String canonicalName = cls.getCanonicalName();
            String name = r02 != null ? r02.getName() : BuildConfig.FLAVOR;
            kotlin.jvm.internal.p.d(name, "fullPackage");
            if (!(name.length() == 0)) {
                kotlin.jvm.internal.p.d(canonicalName, "name");
                canonicalName = canonicalName.substring(name.length() + 1);
                kotlin.jvm.internal.p.d(canonicalName, "this as java.lang.String).substring(startIndex)");
            }
            kotlin.jvm.internal.p.d(canonicalName, "if (fullPackage.isEmpty(…g(fullPackage.length + 1)");
            String c9 = c(canonicalName);
            if (!(name.length() == 0)) {
                c9 = name + '.' + c9;
            }
            Class<?> cls2 = Class.forName(c9);
            kotlin.jvm.internal.p.c(cls2, "null cannot be cast to non-null type java.lang.Class<out androidx.lifecycle.GeneratedAdapter>");
            Constructor declaredConstructor = cls2.getDeclaredConstructor(cls);
            if (declaredConstructor.isAccessible()) {
                return declaredConstructor;
            }
            declaredConstructor.setAccessible(true);
            return declaredConstructor;
        } catch (ClassNotFoundException unused) {
            return null;
        } catch (NoSuchMethodException e8) {
            throw new RuntimeException(e8);
        }
    }

    public static final String c(String str) {
        kotlin.jvm.internal.p.e(str, "className");
        return vj.e.q(str, ".", "_", false, 4, (Object) null) + "_LifecycleAdapter";
    }

    private final int d(Class<?> cls) {
        Map<Class<?>, Integer> map = f5898b;
        Integer num = map.get(cls);
        if (num != null) {
            return num.intValue();
        }
        int g8 = g(cls);
        map.put(cls, Integer.valueOf(g8));
        return g8;
    }

    private final boolean e(Class<?> cls) {
        return cls != null && i.class.isAssignableFrom(cls);
    }

    public static final h f(Object obj) {
        kotlin.jvm.internal.p.e(obj, "object");
        boolean z4 = obj instanceof h;
        boolean z8 = obj instanceof DefaultLifecycleObserver;
        if (z4 && z8) {
            return new DefaultLifecycleObserverAdapter((DefaultLifecycleObserver) obj, (h) obj);
        }
        if (z8) {
            return new DefaultLifecycleObserverAdapter((DefaultLifecycleObserver) obj, null);
        }
        if (z4) {
            return (h) obj;
        }
        Class<?> cls = obj.getClass();
        m mVar = f5897a;
        if (mVar.d(cls) == 2) {
            List<Constructor<? extends d>> list = f5899c.get(cls);
            kotlin.jvm.internal.p.b(list);
            List<Constructor<? extends d>> list2 = list;
            if (list2.size() == 1) {
                return new SingleGeneratedAdapterObserver(mVar.a(list2.get(0), obj));
            }
            int size = list2.size();
            d[] dVarArr = new d[size];
            for (int i8 = 0; i8 < size; i8++) {
                dVarArr[i8] = f5897a.a(list2.get(i8), obj);
            }
            return new CompositeGeneratedAdaptersObserver(dVarArr);
        }
        return new ReflectiveGenericLifecycleObserver(obj);
    }

    private final int g(Class<?> cls) {
        if (cls.getCanonicalName() == null) {
            return 1;
        }
        Constructor<? extends d> b9 = b(cls);
        if (b9 != null) {
            f5899c.put(cls, kotlin.collections.q.d(b9));
            return 2;
        } else if (b.f5848c.d(cls)) {
            return 1;
        } else {
            Class<? super Object> superclass = cls.getSuperclass();
            ArrayList arrayList = null;
            if (e(superclass)) {
                kotlin.jvm.internal.p.d(superclass, "superclass");
                if (d(superclass) == 1) {
                    return 1;
                }
                List<Constructor<? extends d>> list = f5899c.get(superclass);
                kotlin.jvm.internal.p.b(list);
                arrayList = new ArrayList(list);
            }
            Class<?>[] interfaces = cls.getInterfaces();
            kotlin.jvm.internal.p.d(interfaces, "klass.interfaces");
            for (Class<?> cls2 : interfaces) {
                if (e(cls2)) {
                    kotlin.jvm.internal.p.d(cls2, "intrface");
                    if (d(cls2) == 1) {
                        return 1;
                    }
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    List<Constructor<? extends d>> list2 = f5899c.get(cls2);
                    kotlin.jvm.internal.p.b(list2);
                    arrayList.addAll(list2);
                }
            }
            if (arrayList != null) {
                f5899c.put(cls, arrayList);
                return 2;
            }
            return 1;
        }
    }
}
