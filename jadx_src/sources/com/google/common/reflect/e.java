package com.google.common.reflect;

import com.google.common.base.k;
import com.google.common.base.l;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.m1;
import com.google.common.reflect.i;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: a  reason: collision with root package name */
    private final b f19624a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a extends g {

        /* renamed from: b  reason: collision with root package name */
        private final Map<c, Type> f19625b = m1.k();

        private a() {
        }

        static ImmutableMap<c, Type> g(Type type) {
            l.n(type);
            a aVar = new a();
            aVar.a(type);
            return ImmutableMap.c(aVar.f19625b);
        }

        private void h(c cVar, Type type) {
            if (this.f19625b.containsKey(cVar)) {
                return;
            }
            Type type2 = type;
            while (type2 != null) {
                if (cVar.a(type2)) {
                    while (type != null) {
                        type = this.f19625b.remove(c.c(type));
                    }
                    return;
                }
                type2 = this.f19625b.get(c.c(type2));
            }
            this.f19625b.put(cVar, type);
        }

        @Override // com.google.common.reflect.g
        void b(Class<?> cls) {
            a(cls.getGenericSuperclass());
            a(cls.getGenericInterfaces());
        }

        @Override // com.google.common.reflect.g
        void d(ParameterizedType parameterizedType) {
            Class cls = (Class) parameterizedType.getRawType();
            TypeVariable[] typeParameters = cls.getTypeParameters();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            l.s(typeParameters.length == actualTypeArguments.length);
            for (int i8 = 0; i8 < typeParameters.length; i8++) {
                h(new c(typeParameters[i8]), actualTypeArguments[i8]);
            }
            a(cls);
            a(parameterizedType.getOwnerType());
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
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        private final ImmutableMap<c, Type> f19626a;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a extends b {

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ TypeVariable f19627b;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ b f19628c;

            a(b bVar, TypeVariable typeVariable, b bVar2) {
                this.f19627b = typeVariable;
                this.f19628c = bVar2;
            }

            @Override // com.google.common.reflect.e.b
            public Type b(TypeVariable<?> typeVariable, b bVar) {
                return typeVariable.getGenericDeclaration().equals(this.f19627b.getGenericDeclaration()) ? typeVariable : this.f19628c.b(typeVariable, bVar);
            }
        }

        b() {
            this.f19626a = ImmutableMap.n();
        }

        private b(ImmutableMap<c, Type> immutableMap) {
            this.f19626a = immutableMap;
        }

        final Type a(TypeVariable<?> typeVariable) {
            return b(typeVariable, new a(this, typeVariable, this));
        }

        /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.reflect.GenericDeclaration] */
        Type b(TypeVariable<?> typeVariable, b bVar) {
            Type type = this.f19626a.get(new c(typeVariable));
            if (type == null) {
                Type[] bounds = typeVariable.getBounds();
                if (bounds.length == 0) {
                    return typeVariable;
                }
                Type[] f5 = new e(bVar, null).f(bounds);
                return (i.d.f19643a && Arrays.equals(bounds, f5)) ? typeVariable : i.i(typeVariable.getGenericDeclaration(), typeVariable.getName(), f5);
            }
            return new e(bVar, null).e(type);
        }

        final b c(Map<c, ? extends Type> map) {
            ImmutableMap.b a9 = ImmutableMap.a();
            a9.j(this.f19626a);
            for (Map.Entry<c, ? extends Type> entry : map.entrySet()) {
                c key = entry.getKey();
                Type value = entry.getValue();
                l.i(!key.a(value), "Type variable %s bound to itself", key);
                a9.g(key, value);
            }
            return new b(a9.d());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        private final TypeVariable<?> f19629a;

        c(TypeVariable<?> typeVariable) {
            this.f19629a = (TypeVariable) l.n(typeVariable);
        }

        private boolean b(TypeVariable<?> typeVariable) {
            return this.f19629a.getGenericDeclaration().equals(typeVariable.getGenericDeclaration()) && this.f19629a.getName().equals(typeVariable.getName());
        }

        static c c(Type type) {
            if (type instanceof TypeVariable) {
                return new c((TypeVariable) type);
            }
            return null;
        }

        boolean a(Type type) {
            if (type instanceof TypeVariable) {
                return b((TypeVariable) type);
            }
            return false;
        }

        public boolean equals(Object obj) {
            if (obj instanceof c) {
                return b(((c) obj).f19629a);
            }
            return false;
        }

        public int hashCode() {
            return k.b(this.f19629a.getGenericDeclaration(), this.f19629a.getName());
        }

        public String toString() {
            return this.f19629a.toString();
        }
    }

    public e() {
        this.f19624a = new b();
    }

    private e(b bVar) {
        this.f19624a = bVar;
    }

    /* synthetic */ e(b bVar, d dVar) {
        this(bVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static e b(Type type) {
        return new e().h(a.g(type));
    }

    private Type c(GenericArrayType genericArrayType) {
        return i.h(e(genericArrayType.getGenericComponentType()));
    }

    private ParameterizedType d(ParameterizedType parameterizedType) {
        Type ownerType = parameterizedType.getOwnerType();
        return i.k(ownerType == null ? null : e(ownerType), (Class) e(parameterizedType.getRawType()), f(parameterizedType.getActualTypeArguments()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Type[] f(Type[] typeArr) {
        Type[] typeArr2 = new Type[typeArr.length];
        for (int i8 = 0; i8 < typeArr.length; i8++) {
            typeArr2[i8] = e(typeArr[i8]);
        }
        return typeArr2;
    }

    private WildcardType g(WildcardType wildcardType) {
        return new i.h(f(wildcardType.getLowerBounds()), f(wildcardType.getUpperBounds()));
    }

    public Type e(Type type) {
        l.n(type);
        return type instanceof TypeVariable ? this.f19624a.a((TypeVariable) type) : type instanceof ParameterizedType ? d((ParameterizedType) type) : type instanceof GenericArrayType ? c((GenericArrayType) type) : type instanceof WildcardType ? g((WildcardType) type) : type;
    }

    e h(Map<c, ? extends Type> map) {
        return new e(this.f19624a.c(map));
    }
}
