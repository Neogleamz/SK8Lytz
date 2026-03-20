package com.google.common.primitives;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j {

    /* renamed from: a  reason: collision with root package name */
    private static final Map<Class<?>, Class<?>> f19609a;

    /* renamed from: b  reason: collision with root package name */
    private static final Map<Class<?>, Class<?>> f19610b;

    static {
        LinkedHashMap linkedHashMap = new LinkedHashMap(16);
        LinkedHashMap linkedHashMap2 = new LinkedHashMap(16);
        a(linkedHashMap, linkedHashMap2, Boolean.TYPE, Boolean.class);
        a(linkedHashMap, linkedHashMap2, Byte.TYPE, Byte.class);
        a(linkedHashMap, linkedHashMap2, Character.TYPE, Character.class);
        a(linkedHashMap, linkedHashMap2, Double.TYPE, Double.class);
        a(linkedHashMap, linkedHashMap2, Float.TYPE, Float.class);
        a(linkedHashMap, linkedHashMap2, Integer.TYPE, Integer.class);
        a(linkedHashMap, linkedHashMap2, Long.TYPE, Long.class);
        a(linkedHashMap, linkedHashMap2, Short.TYPE, Short.class);
        a(linkedHashMap, linkedHashMap2, Void.TYPE, Void.class);
        f19609a = Collections.unmodifiableMap(linkedHashMap);
        f19610b = Collections.unmodifiableMap(linkedHashMap2);
    }

    private static void a(Map<Class<?>, Class<?>> map, Map<Class<?>, Class<?>> map2, Class<?> cls, Class<?> cls2) {
        map.put(cls, cls2);
        map2.put(cls2, cls);
    }

    public static <T> Class<T> b(Class<T> cls) {
        com.google.common.base.l.n(cls);
        Class<T> cls2 = (Class<T>) f19609a.get(cls);
        return cls2 == null ? cls : cls2;
    }
}
