package com.google.common.reflect;

import com.google.common.base.l;
import com.google.common.base.m;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.i0;
import com.google.common.collect.m1;
import com.google.common.collect.r0;
import com.google.common.collect.y1;
import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class TypeToken<T> extends com.google.common.reflect.c<T> implements Serializable {
    private static final long serialVersionUID = 3637540370352322684L;

    /* renamed from: a  reason: collision with root package name */
    private final Type f19611a;

    /* renamed from: b  reason: collision with root package name */
    private transient e f19612b;

    /* renamed from: c  reason: collision with root package name */
    private transient e f19613c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class TypeSet extends r0<TypeToken<? super T>> implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private transient ImmutableSet<TypeToken<? super T>> f19614a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ TypeToken f19615b;

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.r0, com.google.common.collect.j0
        /* renamed from: v */
        public Set<TypeToken<? super T>> i() {
            ImmutableSet<TypeToken<? super T>> immutableSet = this.f19614a;
            if (immutableSet == null) {
                ImmutableSet<TypeToken<? super T>> i8 = i0.g(c.f19617a.c(this.f19615b)).e(d.f19621a).i();
                this.f19614a = i8;
                return i8;
            }
            return immutableSet;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends g {

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ ImmutableSet.a f19616b;

        a(TypeToken typeToken, ImmutableSet.a aVar) {
            this.f19616b = aVar;
        }

        @Override // com.google.common.reflect.g
        void b(Class<?> cls) {
            this.f19616b.a(cls);
        }

        @Override // com.google.common.reflect.g
        void c(GenericArrayType genericArrayType) {
            this.f19616b.a(i.g(TypeToken.j(genericArrayType.getGenericComponentType()).h()));
        }

        @Override // com.google.common.reflect.g
        void d(ParameterizedType parameterizedType) {
            this.f19616b.a((Class) parameterizedType.getRawType());
        }

        @Override // com.google.common.reflect.g
        void e(TypeVariable<?> typeVariable) {
            a(typeVariable.getBounds());
        }

        @Override // com.google.common.reflect.g
        void f(WildcardType wildcardType) {
            a(wildcardType.getUpperBounds());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b<T> extends TypeToken<T> {
        private static final long serialVersionUID = 0;

        b(Type type) {
            super(type, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class c<K> {

        /* renamed from: a  reason: collision with root package name */
        static final c<TypeToken<?>> f19617a = new a();

        /* renamed from: b  reason: collision with root package name */
        static final c<Class<?>> f19618b = new b();

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends c<TypeToken<?>> {
            a() {
                super(null);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.reflect.TypeToken.c
            /* renamed from: h */
            public Iterable<? extends TypeToken<?>> d(TypeToken<?> typeToken) {
                return typeToken.f();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.reflect.TypeToken.c
            /* renamed from: i */
            public Class<?> e(TypeToken<?> typeToken) {
                return typeToken.h();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.reflect.TypeToken.c
            /* renamed from: j */
            public TypeToken<?> f(TypeToken<?> typeToken) {
                return typeToken.g();
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class b extends c<Class<?>> {
            b() {
                super(null);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.reflect.TypeToken.c
            /* renamed from: h */
            public Iterable<? extends Class<?>> d(Class<?> cls) {
                return Arrays.asList(cls.getInterfaces());
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.reflect.TypeToken.c
            /* renamed from: i */
            public Class<?> e(Class<?> cls) {
                return cls;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.reflect.TypeToken.c
            /* renamed from: j */
            public Class<?> f(Class<?> cls) {
                return cls.getSuperclass();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.google.common.reflect.TypeToken$c$c  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class C0158c extends y1<K> {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ Comparator f19619a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ Map f19620b;

            C0158c(Comparator comparator, Map map) {
                this.f19619a = comparator;
                this.f19620b = map;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.y1, java.util.Comparator
            public int compare(K k8, K k9) {
                Comparator comparator = this.f19619a;
                Object obj = this.f19620b.get(k8);
                Objects.requireNonNull(obj);
                Object obj2 = this.f19620b.get(k9);
                Objects.requireNonNull(obj2);
                return comparator.compare(obj, obj2);
            }
        }

        private c() {
        }

        /* synthetic */ c(f fVar) {
            this();
        }

        /* JADX WARN: Multi-variable type inference failed */
        private int a(K k8, Map<? super K, Integer> map) {
            Integer num = map.get(k8);
            if (num != null) {
                return num.intValue();
            }
            boolean isInterface = e(k8).isInterface();
            int i8 = isInterface;
            for (K k9 : d(k8)) {
                i8 = Math.max(i8, a(k9, map));
            }
            K f5 = f(k8);
            int i9 = i8;
            if (f5 != null) {
                i9 = Math.max(i8, a(f5, map));
            }
            int i10 = i9 + 1;
            map.put(k8, Integer.valueOf(i10));
            return i10;
        }

        private static <K, V> ImmutableList<K> g(Map<K, V> map, Comparator<? super V> comparator) {
            return (ImmutableList<K>) new C0158c(comparator, map).b(map.keySet());
        }

        ImmutableList<K> b(Iterable<? extends K> iterable) {
            HashMap k8 = m1.k();
            for (K k9 : iterable) {
                a(k9, k8);
            }
            return g(k8, y1.c().f());
        }

        final ImmutableList<K> c(K k8) {
            return b(ImmutableList.F(k8));
        }

        abstract Iterable<? extends K> d(K k8);

        abstract Class<?> e(K k8);

        abstract K f(K k8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class d implements m<TypeToken<?>> {

        /* renamed from: a  reason: collision with root package name */
        public static final d f19621a = new a("IGNORE_TYPE_VARIABLE_OR_WILDCARD", 0);

        /* renamed from: b  reason: collision with root package name */
        public static final d f19622b = new b("INTERFACE_ONLY", 1);

        /* renamed from: c  reason: collision with root package name */
        private static final /* synthetic */ d[] f19623c = c();

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum a extends d {
            a(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.base.m
            /* renamed from: f */
            public boolean apply(TypeToken<?> typeToken) {
                return ((((TypeToken) typeToken).f19611a instanceof TypeVariable) || (((TypeToken) typeToken).f19611a instanceof WildcardType)) ? false : true;
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum b extends d {
            b(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.base.m
            /* renamed from: f */
            public boolean apply(TypeToken<?> typeToken) {
                return typeToken.h().isInterface();
            }
        }

        private d(String str, int i8) {
        }

        /* synthetic */ d(String str, int i8, f fVar) {
            this(str, i8);
        }

        private static /* synthetic */ d[] c() {
            return new d[]{f19621a, f19622b};
        }

        public static d valueOf(String str) {
            return (d) Enum.valueOf(d.class, str);
        }

        public static d[] values() {
            return (d[]) f19623c.clone();
        }
    }

    protected TypeToken() {
        Type a9 = a();
        this.f19611a = a9;
        l.v(!(a9 instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", a9);
    }

    private TypeToken(Type type) {
        this.f19611a = (Type) l.n(type);
    }

    /* synthetic */ TypeToken(Type type, f fVar) {
        this(type);
    }

    private TypeToken<? super T> c(Type type) {
        TypeToken<? super T> typeToken = (TypeToken<? super T>) j(type);
        if (typeToken.h().isInterface()) {
            return null;
        }
        return typeToken;
    }

    private ImmutableList<TypeToken<? super T>> d(Type[] typeArr) {
        ImmutableList.a u8 = ImmutableList.u();
        for (Type type : typeArr) {
            TypeToken<?> j8 = j(type);
            if (j8.h().isInterface()) {
                u8.a(j8);
            }
        }
        return u8.k();
    }

    private e e() {
        e eVar = this.f19613c;
        if (eVar == null) {
            e b9 = e.b(this.f19611a);
            this.f19613c = b9;
            return b9;
        }
        return eVar;
    }

    private ImmutableSet<Class<? super T>> i() {
        ImmutableSet.a u8 = ImmutableSet.u();
        new a(this, u8).a(this.f19611a);
        return u8.l();
    }

    public static TypeToken<?> j(Type type) {
        return new b(type);
    }

    private TypeToken<?> k(Type type) {
        TypeToken<?> j8 = j(e().e(type));
        j8.f19613c = this.f19613c;
        j8.f19612b = this.f19612b;
        return j8;
    }

    public boolean equals(Object obj) {
        if (obj instanceof TypeToken) {
            return this.f19611a.equals(((TypeToken) obj).f19611a);
        }
        return false;
    }

    final ImmutableList<TypeToken<? super T>> f() {
        Type type = this.f19611a;
        if (type instanceof TypeVariable) {
            return d(((TypeVariable) type).getBounds());
        }
        if (type instanceof WildcardType) {
            return d(((WildcardType) type).getUpperBounds());
        }
        ImmutableList.a u8 = ImmutableList.u();
        for (Type type2 : h().getGenericInterfaces()) {
            u8.a(k(type2));
        }
        return u8.k();
    }

    final TypeToken<? super T> g() {
        Type type;
        Type type2 = this.f19611a;
        if (type2 instanceof TypeVariable) {
            type = ((TypeVariable) type2).getBounds()[0];
        } else if (!(type2 instanceof WildcardType)) {
            Type genericSuperclass = h().getGenericSuperclass();
            if (genericSuperclass == null) {
                return null;
            }
            return (TypeToken<? super T>) k(genericSuperclass);
        } else {
            type = ((WildcardType) type2).getUpperBounds()[0];
        }
        return c(type);
    }

    public final Class<? super T> h() {
        return i().iterator().next();
    }

    public int hashCode() {
        return this.f19611a.hashCode();
    }

    public String toString() {
        return i.p(this.f19611a);
    }

    protected Object writeReplace() {
        return j(new e().e(this.f19611a));
    }
}
