package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b {

    /* renamed from: c  reason: collision with root package name */
    static b f5848c = new b();

    /* renamed from: a  reason: collision with root package name */
    private final Map<Class<?>, a> f5849a = new HashMap();

    /* renamed from: b  reason: collision with root package name */
    private final Map<Class<?>, Boolean> f5850b = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        final Map<Lifecycle.Event, List<C0058b>> f5851a = new HashMap();

        /* renamed from: b  reason: collision with root package name */
        final Map<C0058b, Lifecycle.Event> f5852b;

        a(Map<C0058b, Lifecycle.Event> map) {
            this.f5852b = map;
            for (Map.Entry<C0058b, Lifecycle.Event> entry : map.entrySet()) {
                Lifecycle.Event value = entry.getValue();
                List<C0058b> list = this.f5851a.get(value);
                if (list == null) {
                    list = new ArrayList<>();
                    this.f5851a.put(value, list);
                }
                list.add(entry.getKey());
            }
        }

        private static void b(List<C0058b> list, j jVar, Lifecycle.Event event, Object obj) {
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    list.get(size).a(jVar, event, obj);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void a(j jVar, Lifecycle.Event event, Object obj) {
            b(this.f5851a.get(event), jVar, event, obj);
            b(this.f5851a.get(Lifecycle.Event.ON_ANY), jVar, event, obj);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    /* renamed from: androidx.lifecycle.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0058b {

        /* renamed from: a  reason: collision with root package name */
        final int f5853a;

        /* renamed from: b  reason: collision with root package name */
        final Method f5854b;

        C0058b(int i8, Method method) {
            this.f5853a = i8;
            this.f5854b = method;
            method.setAccessible(true);
        }

        void a(j jVar, Lifecycle.Event event, Object obj) {
            try {
                int i8 = this.f5853a;
                if (i8 == 0) {
                    this.f5854b.invoke(obj, new Object[0]);
                } else if (i8 == 1) {
                    this.f5854b.invoke(obj, jVar);
                } else if (i8 != 2) {
                } else {
                    this.f5854b.invoke(obj, jVar, event);
                }
            } catch (IllegalAccessException e8) {
                throw new RuntimeException(e8);
            } catch (InvocationTargetException e9) {
                throw new RuntimeException("Failed to call observer method", e9.getCause());
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof C0058b) {
                C0058b c0058b = (C0058b) obj;
                return this.f5853a == c0058b.f5853a && this.f5854b.getName().equals(c0058b.f5854b.getName());
            }
            return false;
        }

        public int hashCode() {
            return (this.f5853a * 31) + this.f5854b.getName().hashCode();
        }
    }

    b() {
    }

    private a a(Class<?> cls, Method[] methodArr) {
        int i8;
        a c9;
        Class<? super Object> superclass = cls.getSuperclass();
        HashMap hashMap = new HashMap();
        if (superclass != null && (c9 = c(superclass)) != null) {
            hashMap.putAll(c9.f5852b);
        }
        for (Class<?> cls2 : cls.getInterfaces()) {
            for (Map.Entry<C0058b, Lifecycle.Event> entry : c(cls2).f5852b.entrySet()) {
                e(hashMap, entry.getKey(), entry.getValue(), cls);
            }
        }
        if (methodArr == null) {
            methodArr = b(cls);
        }
        boolean z4 = false;
        for (Method method : methodArr) {
            r rVar = (r) method.getAnnotation(r.class);
            if (rVar != null) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length <= 0) {
                    i8 = 0;
                } else if (!j.class.isAssignableFrom(parameterTypes[0])) {
                    throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
                } else {
                    i8 = 1;
                }
                Lifecycle.Event value = rVar.value();
                if (parameterTypes.length > 1) {
                    if (!Lifecycle.Event.class.isAssignableFrom(parameterTypes[1])) {
                        throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
                    }
                    if (value != Lifecycle.Event.ON_ANY) {
                        throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
                    }
                    i8 = 2;
                }
                if (parameterTypes.length > 2) {
                    throw new IllegalArgumentException("cannot have more than 2 params");
                }
                e(hashMap, new C0058b(i8, method), value, cls);
                z4 = true;
            }
        }
        a aVar = new a(hashMap);
        this.f5849a.put(cls, aVar);
        this.f5850b.put(cls, Boolean.valueOf(z4));
        return aVar;
    }

    private Method[] b(Class<?> cls) {
        try {
            return cls.getDeclaredMethods();
        } catch (NoClassDefFoundError e8) {
            throw new IllegalArgumentException("The observer class has some methods that use newer APIs which are not available in the current OS version. Lifecycles cannot access even other methods so you should make sure that your observer classes only access framework classes that are available in your min API level OR use lifecycle:compiler annotation processor.", e8);
        }
    }

    private void e(Map<C0058b, Lifecycle.Event> map, C0058b c0058b, Lifecycle.Event event, Class<?> cls) {
        Lifecycle.Event event2 = map.get(c0058b);
        if (event2 == null || event == event2) {
            if (event2 == null) {
                map.put(c0058b, event);
                return;
            }
            return;
        }
        Method method = c0058b.f5854b;
        throw new IllegalArgumentException("Method " + method.getName() + " in " + cls.getName() + " already declared with different @OnLifecycleEvent value: previous value " + event2 + ", new value " + event);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a c(Class<?> cls) {
        a aVar = this.f5849a.get(cls);
        return aVar != null ? aVar : a(cls, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d(Class<?> cls) {
        Boolean bool = this.f5850b.get(cls);
        if (bool != null) {
            return bool.booleanValue();
        }
        Method[] b9 = b(cls);
        for (Method method : b9) {
            if (((r) method.getAnnotation(r.class)) != null) {
                a(cls, b9);
                return true;
            }
        }
        this.f5850b.put(cls, Boolean.FALSE);
        return false;
    }
}
