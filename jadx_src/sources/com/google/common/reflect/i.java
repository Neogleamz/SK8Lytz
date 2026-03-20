package com.google.common.reflect;

import com.google.common.base.k;
import com.google.common.base.l;
import com.google.common.base.n;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.d3;
import com.google.common.collect.f1;
import com.google.common.reflect.i;
import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i {

    /* renamed from: a  reason: collision with root package name */
    private static final com.google.common.base.h f19631a = com.google.common.base.h.g(", ").i("null");

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {

        /* renamed from: a  reason: collision with root package name */
        public static final a f19632a = new C0160a("OWNED_BY_ENCLOSING_CLASS", 0);

        /* renamed from: b  reason: collision with root package name */
        public static final a f19633b = new c("LOCAL_CLASS_HAS_NO_OWNER", 1);

        /* renamed from: d  reason: collision with root package name */
        private static final /* synthetic */ a[] f19635d = c();

        /* renamed from: c  reason: collision with root package name */
        static final a f19634c = f();

        /* renamed from: com.google.common.reflect.i$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum C0160a extends a {
            C0160a(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.reflect.i.a
            Class<?> h(Class<?> cls) {
                return cls.getEnclosingClass();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class b<T> {
            b() {
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum c extends a {
            c(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.reflect.i.a
            Class<?> h(Class<?> cls) {
                if (cls.isLocalClass()) {
                    return null;
                }
                return cls.getEnclosingClass();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class d extends b<String> {
            d() {
            }
        }

        private a(String str, int i8) {
        }

        /* synthetic */ a(String str, int i8, com.google.common.reflect.h hVar) {
            this(str, i8);
        }

        private static /* synthetic */ a[] c() {
            return new a[]{f19632a, f19633b};
        }

        private static a f() {
            a[] values;
            new d();
            ParameterizedType parameterizedType = (ParameterizedType) d.class.getGenericSuperclass();
            Objects.requireNonNull(parameterizedType);
            ParameterizedType parameterizedType2 = parameterizedType;
            for (a aVar : values()) {
                if (aVar.h(b.class) == parameterizedType2.getOwnerType()) {
                    return aVar;
                }
            }
            throw new AssertionError();
        }

        public static a valueOf(String str) {
            return (a) Enum.valueOf(a.class, str);
        }

        public static a[] values() {
            return (a[]) f19635d.clone();
        }

        abstract Class<?> h(Class<?> cls);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements GenericArrayType, Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private final Type f19636a;

        b(Type type) {
            this.f19636a = c.f19641e.k(type);
        }

        public boolean equals(Object obj) {
            if (obj instanceof GenericArrayType) {
                return k.a(getGenericComponentType(), ((GenericArrayType) obj).getGenericComponentType());
            }
            return false;
        }

        @Override // java.lang.reflect.GenericArrayType
        public Type getGenericComponentType() {
            return this.f19636a;
        }

        public int hashCode() {
            return this.f19636a.hashCode();
        }

        public String toString() {
            return String.valueOf(i.p(this.f19636a)).concat("[]");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class c {

        /* renamed from: a  reason: collision with root package name */
        public static final c f19637a;

        /* renamed from: b  reason: collision with root package name */
        public static final c f19638b;

        /* renamed from: c  reason: collision with root package name */
        public static final c f19639c;

        /* renamed from: d  reason: collision with root package name */
        public static final c f19640d;

        /* renamed from: e  reason: collision with root package name */
        static final c f19641e;

        /* renamed from: f  reason: collision with root package name */
        private static final /* synthetic */ c[] f19642f;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum a extends c {
            a(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.reflect.i.c
            Type k(Type type) {
                l.n(type);
                if (type instanceof Class) {
                    Class cls = (Class) type;
                    return cls.isArray() ? new b(cls.getComponentType()) : type;
                }
                return type;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.reflect.i.c
            /* renamed from: o */
            public GenericArrayType h(Type type) {
                return new b(type);
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum b extends c {
            b(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.reflect.i.c
            Type h(Type type) {
                return type instanceof Class ? i.g((Class) type) : new b(type);
            }

            @Override // com.google.common.reflect.i.c
            Type k(Type type) {
                return (Type) l.n(type);
            }
        }

        /* renamed from: com.google.common.reflect.i$c$c  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum C0161c extends c {
            C0161c(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.reflect.i.c
            Type h(Type type) {
                return c.f19638b.h(type);
            }

            @Override // com.google.common.reflect.i.c
            String i(Type type) {
                try {
                    return (String) Type.class.getMethod("getTypeName", new Class[0]).invoke(type, new Object[0]);
                } catch (IllegalAccessException e8) {
                    throw new RuntimeException(e8);
                } catch (NoSuchMethodException unused) {
                    throw new AssertionError("Type.getTypeName should be available in Java 8");
                } catch (InvocationTargetException e9) {
                    throw new RuntimeException(e9);
                }
            }

            @Override // com.google.common.reflect.i.c
            Type k(Type type) {
                return c.f19638b.k(type);
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum d extends c {
            d(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.reflect.i.c
            boolean f() {
                return false;
            }

            @Override // com.google.common.reflect.i.c
            Type h(Type type) {
                return c.f19639c.h(type);
            }

            @Override // com.google.common.reflect.i.c
            String i(Type type) {
                return c.f19639c.i(type);
            }

            @Override // com.google.common.reflect.i.c
            Type k(Type type) {
                return c.f19639c.k(type);
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class e extends com.google.common.reflect.c<Map.Entry<String, int[][]>> {
            e() {
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class f extends com.google.common.reflect.c<int[]> {
            f() {
            }
        }

        static {
            a aVar = new a("JAVA6", 0);
            f19637a = aVar;
            b bVar = new b("JAVA7", 1);
            f19638b = bVar;
            C0161c c0161c = new C0161c("JAVA8", 2);
            f19639c = c0161c;
            d dVar = new d("JAVA9", 3);
            f19640d = dVar;
            f19642f = c();
            if (AnnotatedElement.class.isAssignableFrom(TypeVariable.class)) {
                if (new e().a().toString().contains("java.util.Map.java.util.Map")) {
                    f19641e = c0161c;
                } else {
                    f19641e = dVar;
                }
            } else if (new f().a() instanceof Class) {
                f19641e = bVar;
            } else {
                f19641e = aVar;
            }
        }

        private c(String str, int i8) {
        }

        /* synthetic */ c(String str, int i8, com.google.common.reflect.h hVar) {
            this(str, i8);
        }

        private static /* synthetic */ c[] c() {
            return new c[]{f19637a, f19638b, f19639c, f19640d};
        }

        public static c valueOf(String str) {
            return (c) Enum.valueOf(c.class, str);
        }

        public static c[] values() {
            return (c[]) f19642f.clone();
        }

        boolean f() {
            return true;
        }

        abstract Type h(Type type);

        /* JADX INFO: Access modifiers changed from: package-private */
        public String i(Type type) {
            return i.p(type);
        }

        final ImmutableList<Type> j(Type[] typeArr) {
            ImmutableList.a u8 = ImmutableList.u();
            for (Type type : typeArr) {
                u8.a(k(type));
            }
            return u8.k();
        }

        abstract Type k(Type type);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class d<X> {

        /* renamed from: a  reason: collision with root package name */
        static final boolean f19643a = !d.class.getTypeParameters()[0].equals(i.i(d.class, "X", new Type[0]));

        d() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e implements ParameterizedType, Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private final Type f19644a;

        /* renamed from: b  reason: collision with root package name */
        private final ImmutableList<Type> f19645b;

        /* renamed from: c  reason: collision with root package name */
        private final Class<?> f19646c;

        e(Type type, Class<?> cls, Type[] typeArr) {
            l.n(cls);
            l.d(typeArr.length == cls.getTypeParameters().length);
            i.e(typeArr, "type parameter");
            this.f19644a = type;
            this.f19646c = cls;
            this.f19645b = c.f19641e.j(typeArr);
        }

        public boolean equals(Object obj) {
            if (obj instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) obj;
                return getRawType().equals(parameterizedType.getRawType()) && k.a(getOwnerType(), parameterizedType.getOwnerType()) && Arrays.equals(getActualTypeArguments(), parameterizedType.getActualTypeArguments());
            }
            return false;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type[] getActualTypeArguments() {
            return i.o(this.f19645b);
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getOwnerType() {
            return this.f19644a;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getRawType() {
            return this.f19646c;
        }

        public int hashCode() {
            Type type = this.f19644a;
            return ((type == null ? 0 : type.hashCode()) ^ this.f19645b.hashCode()) ^ this.f19646c.hashCode();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.f19644a != null) {
                c cVar = c.f19641e;
                if (cVar.f()) {
                    sb.append(cVar.i(this.f19644a));
                    sb.append('.');
                }
            }
            sb.append(this.f19646c.getName());
            sb.append('<');
            com.google.common.base.h hVar = i.f19631a;
            ImmutableList<Type> immutableList = this.f19645b;
            final c cVar2 = c.f19641e;
            Objects.requireNonNull(cVar2);
            sb.append(hVar.d(f1.o(immutableList, new com.google.common.base.g() { // from class: com.google.common.reflect.j
                @Override // com.google.common.base.g
                public final Object apply(Object obj) {
                    return i.c.this.i((Type) obj);
                }
            })));
            sb.append('>');
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class f<D extends GenericDeclaration> {

        /* renamed from: a  reason: collision with root package name */
        private final D f19647a;

        /* renamed from: b  reason: collision with root package name */
        private final String f19648b;

        /* renamed from: c  reason: collision with root package name */
        private final ImmutableList<Type> f19649c;

        f(D d8, String str, Type[] typeArr) {
            i.e(typeArr, "bound for type variable");
            this.f19647a = (D) l.n(d8);
            this.f19648b = (String) l.n(str);
            this.f19649c = ImmutableList.y(typeArr);
        }

        public D a() {
            return this.f19647a;
        }

        public String b() {
            return this.f19648b;
        }

        public boolean equals(Object obj) {
            if (!d.f19643a) {
                if (obj instanceof TypeVariable) {
                    TypeVariable typeVariable = (TypeVariable) obj;
                    return this.f19648b.equals(typeVariable.getName()) && this.f19647a.equals(typeVariable.getGenericDeclaration());
                }
                return false;
            } else if (obj != null && Proxy.isProxyClass(obj.getClass()) && (Proxy.getInvocationHandler(obj) instanceof g)) {
                f fVar = ((g) Proxy.getInvocationHandler(obj)).f19651a;
                return this.f19648b.equals(fVar.b()) && this.f19647a.equals(fVar.a()) && this.f19649c.equals(fVar.f19649c);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return this.f19647a.hashCode() ^ this.f19648b.hashCode();
        }

        public String toString() {
            return this.f19648b;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g implements InvocationHandler {

        /* renamed from: b  reason: collision with root package name */
        private static final ImmutableMap<String, Method> f19650b;

        /* renamed from: a  reason: collision with root package name */
        private final f<?> f19651a;

        static {
            Method[] methods;
            ImmutableMap.b a9 = ImmutableMap.a();
            for (Method method : f.class.getMethods()) {
                if (method.getDeclaringClass().equals(f.class)) {
                    try {
                        method.setAccessible(true);
                    } catch (AccessControlException unused) {
                    }
                    a9.g(method.getName(), method);
                }
            }
            f19650b = a9.c();
        }

        g(f<?> fVar) {
            this.f19651a = fVar;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) {
            String name = method.getName();
            Method method2 = f19650b.get(name);
            if (method2 != null) {
                try {
                    return method2.invoke(this.f19651a, objArr);
                } catch (InvocationTargetException e8) {
                    throw e8.getCause();
                }
            }
            throw new UnsupportedOperationException(name);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class h implements WildcardType, Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private final ImmutableList<Type> f19652a;

        /* renamed from: b  reason: collision with root package name */
        private final ImmutableList<Type> f19653b;

        /* JADX INFO: Access modifiers changed from: package-private */
        public h(Type[] typeArr, Type[] typeArr2) {
            i.e(typeArr, "lower bound for wildcard");
            i.e(typeArr2, "upper bound for wildcard");
            c cVar = c.f19641e;
            this.f19652a = cVar.j(typeArr);
            this.f19653b = cVar.j(typeArr2);
        }

        public boolean equals(Object obj) {
            if (obj instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) obj;
                return this.f19652a.equals(Arrays.asList(wildcardType.getLowerBounds())) && this.f19653b.equals(Arrays.asList(wildcardType.getUpperBounds()));
            }
            return false;
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getLowerBounds() {
            return i.o(this.f19652a);
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getUpperBounds() {
            return i.o(this.f19653b);
        }

        public int hashCode() {
            return this.f19652a.hashCode() ^ this.f19653b.hashCode();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("?");
            d3<Type> it = this.f19652a.iterator();
            while (it.hasNext()) {
                sb.append(" super ");
                sb.append(c.f19641e.i(it.next()));
            }
            for (Type type : i.f(this.f19653b)) {
                sb.append(" extends ");
                sb.append(c.f19641e.i(type));
            }
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void e(Type[] typeArr, String str) {
        Class cls;
        for (Type type : typeArr) {
            if (type instanceof Class) {
                l.j(!cls.isPrimitive(), "Primitive type '%s' used as %s", (Class) type, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Iterable<Type> f(Iterable<Type> iterable) {
        return f1.d(iterable, n.f(n.d(Object.class)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Class<?> g(Class<?> cls) {
        return Array.newInstance(cls, 0).getClass();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Type h(Type type) {
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            Type[] lowerBounds = wildcardType.getLowerBounds();
            l.e(lowerBounds.length <= 1, "Wildcard cannot have more than one lower bounds.");
            if (lowerBounds.length == 1) {
                return n(h(lowerBounds[0]));
            }
            Type[] upperBounds = wildcardType.getUpperBounds();
            l.e(upperBounds.length == 1, "Wildcard should have only one upper bound.");
            return m(h(upperBounds[0]));
        }
        return c.f19641e.h(type);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <D extends GenericDeclaration> TypeVariable<D> i(D d8, String str, Type... typeArr) {
        if (typeArr.length == 0) {
            typeArr = new Type[]{Object.class};
        }
        return l(d8, str, typeArr);
    }

    static ParameterizedType j(Class<?> cls, Type... typeArr) {
        return new e(a.f19634c.h(cls), cls, typeArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ParameterizedType k(Type type, Class<?> cls, Type... typeArr) {
        if (type == null) {
            return j(cls, typeArr);
        }
        l.n(typeArr);
        l.i(cls.getEnclosingClass() != null, "Owner type for unenclosed %s", cls);
        return new e(type, cls, typeArr);
    }

    private static <D extends GenericDeclaration> TypeVariable<D> l(D d8, String str, Type[] typeArr) {
        return (TypeVariable) com.google.common.reflect.b.a(TypeVariable.class, new g(new f(d8, str, typeArr)));
    }

    static WildcardType m(Type type) {
        return new h(new Type[0], new Type[]{type});
    }

    static WildcardType n(Type type) {
        return new h(new Type[]{type}, new Type[]{Object.class});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Type[] o(Collection<Type> collection) {
        return (Type[]) collection.toArray(new Type[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String p(Type type) {
        return type instanceof Class ? ((Class) type).getName() : type.toString();
    }
}
