package com.dexterous.flutterlocalnotifications;

import androidx.annotation.Keep;
import b9.l;
import com.google.gson.JsonParseException;
import com.google.gson.e;
import com.google.gson.k;
import com.google.gson.m;
import com.google.gson.o;
import com.google.gson.s;
import com.google.gson.t;
import e9.b;
import java.util.LinkedHashMap;
import java.util.Map;
@Keep
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class RuntimeTypeAdapterFactory<T> implements t {
    private final Class<?> baseType;
    private final Map<String, Class<?>> labelToSubtype = new LinkedHashMap();
    private final Map<Class<?>, String> subtypeToLabel = new LinkedHashMap();
    private final String typeFieldName;

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a<R> extends s<R> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Map f8860a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Map f8861b;

        a(Map map, Map map2) {
            this.f8860a = map;
            this.f8861b = map2;
        }

        public R c(e9.a aVar) {
            k a9 = l.a(aVar);
            k C = a9.h().C(RuntimeTypeAdapterFactory.this.typeFieldName);
            if (C == null) {
                throw new JsonParseException("cannot deserialize " + RuntimeTypeAdapterFactory.this.baseType + " because it does not define a field named " + RuntimeTypeAdapterFactory.this.typeFieldName);
            }
            String k8 = C.k();
            s sVar = (s) this.f8860a.get(k8);
            if (sVar != null) {
                return (R) sVar.a(a9);
            }
            throw new JsonParseException("cannot deserialize " + RuntimeTypeAdapterFactory.this.baseType + " subtype named " + k8 + "; did you forget to register a subtype?");
        }

        public void e(b bVar, R r4) {
            Class<?> cls = r4.getClass();
            String str = (String) RuntimeTypeAdapterFactory.this.subtypeToLabel.get(cls);
            s sVar = (s) this.f8861b.get(cls);
            if (sVar == null) {
                throw new JsonParseException("cannot serialize " + cls.getName() + "; did you forget to register a subtype?");
            }
            m h8 = sVar.d(r4).h();
            if (h8.A(RuntimeTypeAdapterFactory.this.typeFieldName)) {
                throw new JsonParseException("cannot serialize " + cls.getName() + " because it already defines a field named " + RuntimeTypeAdapterFactory.this.typeFieldName);
            }
            m mVar = new m();
            mVar.u(RuntimeTypeAdapterFactory.this.typeFieldName, new o(str));
            for (Map.Entry entry : h8.entrySet()) {
                mVar.u((String) entry.getKey(), (k) entry.getValue());
            }
            l.b(mVar, bVar);
        }
    }

    private RuntimeTypeAdapterFactory(Class<?> cls, String str) {
        if (str == null || cls == null) {
            throw null;
        }
        this.baseType = cls;
        this.typeFieldName = str;
    }

    public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> cls) {
        return new RuntimeTypeAdapterFactory<>(cls, "type");
    }

    public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> cls, String str) {
        return new RuntimeTypeAdapterFactory<>(cls, str);
    }

    public <R> s<R> create(e eVar, com.google.gson.reflect.a<R> aVar) {
        if (aVar.getRawType() != this.baseType) {
            return null;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        for (Map.Entry<String, Class<?>> entry : this.labelToSubtype.entrySet()) {
            s p8 = eVar.p(this, com.google.gson.reflect.a.get(entry.getValue()));
            linkedHashMap.put(entry.getKey(), p8);
            linkedHashMap2.put(entry.getValue(), p8);
        }
        return new a(linkedHashMap, linkedHashMap2).b();
    }

    public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> cls) {
        return registerSubtype(cls, cls.getSimpleName());
    }

    public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> cls, String str) {
        if (cls == null || str == null) {
            throw null;
        }
        if (this.subtypeToLabel.containsKey(cls) || this.labelToSubtype.containsKey(str)) {
            throw new IllegalArgumentException("types and labels must be unique");
        }
        this.labelToSubtype.put(str, cls);
        this.subtypeToLabel.put(cls, str);
        return this;
    }
}
