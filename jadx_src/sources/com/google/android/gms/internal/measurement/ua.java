package com.google.android.gms.internal.measurement;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ua {

    /* renamed from: c  reason: collision with root package name */
    private static final ua f12557c = new ua();

    /* renamed from: b  reason: collision with root package name */
    private final ConcurrentMap<Class<?>, xa<?>> f12559b = new ConcurrentHashMap();

    /* renamed from: a  reason: collision with root package name */
    private final wa f12558a = new w9();

    private ua() {
    }

    public static ua a() {
        return f12557c;
    }

    public final <T> xa<T> b(Class<T> cls) {
        a9.f(cls, "messageType");
        xa<T> xaVar = (xa<T>) this.f12559b.get(cls);
        if (xaVar == null) {
            xa<T> a9 = this.f12558a.a(cls);
            a9.f(cls, "messageType");
            a9.f(a9, "schema");
            xa<T> xaVar2 = (xa<T>) this.f12559b.putIfAbsent(cls, a9);
            return xaVar2 != null ? xaVar2 : a9;
        }
        return xaVar;
    }

    public final <T> xa<T> c(T t8) {
        return b(t8.getClass());
    }
}
