package com.google.common.reflect;

import com.google.common.collect.p2;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class g {

    /* renamed from: a  reason: collision with root package name */
    private final Set<Type> f19630a = p2.f();

    public final void a(Type... typeArr) {
        for (Type type : typeArr) {
            if (type != null && this.f19630a.add(type)) {
                try {
                    if (type instanceof TypeVariable) {
                        e((TypeVariable) type);
                    } else if (type instanceof WildcardType) {
                        f((WildcardType) type);
                    } else if (type instanceof ParameterizedType) {
                        d((ParameterizedType) type);
                    } else if (type instanceof Class) {
                        b((Class) type);
                    } else if (!(type instanceof GenericArrayType)) {
                        String valueOf = String.valueOf(type);
                        StringBuilder sb = new StringBuilder(valueOf.length() + 14);
                        sb.append("Unknown type: ");
                        sb.append(valueOf);
                        throw new AssertionError(sb.toString());
                    } else {
                        c((GenericArrayType) type);
                    }
                } catch (Throwable th) {
                    this.f19630a.remove(type);
                    throw th;
                }
            }
        }
    }

    abstract void b(Class<?> cls);

    void c(GenericArrayType genericArrayType) {
    }

    abstract void d(ParameterizedType parameterizedType);

    abstract void e(TypeVariable<?> typeVariable);

    abstract void f(WildcardType wildcardType);
}
