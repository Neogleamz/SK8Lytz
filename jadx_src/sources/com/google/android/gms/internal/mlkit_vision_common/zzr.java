package com.google.android.gms.internal.mlkit_vision_common;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class zzr implements Map, Serializable {

    /* renamed from: a  reason: collision with root package name */
    private transient zzs f16276a;

    /* renamed from: b  reason: collision with root package name */
    private transient zzs f16277b;

    /* renamed from: c  reason: collision with root package name */
    private transient zzl f16278c;

    public static zzr c(Object obj, Object obj2) {
        h7.a("optional-module-barcode", "com.google.android.gms.vision.barcode");
        return oc.i(1, new Object[]{"optional-module-barcode", "com.google.android.gms.vision.barcode"}, null);
    }

    abstract zzl a();

    @Override // java.util.Map
    /* renamed from: b */
    public final zzl values() {
        zzl zzlVar = this.f16278c;
        if (zzlVar == null) {
            zzl a9 = a();
            this.f16278c = a9;
            return a9;
        }
        return zzlVar;
    }

    @Override // java.util.Map
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public final boolean containsKey(Object obj) {
        return get(obj) != null;
    }

    @Override // java.util.Map
    public final boolean containsValue(Object obj) {
        return values().contains(obj);
    }

    abstract zzs d();

    @Override // java.util.Map
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Map) {
            return entrySet().equals(((Map) obj).entrySet());
        }
        return false;
    }

    abstract zzs f();

    @Override // java.util.Map
    public abstract Object get(Object obj);

    @Override // java.util.Map
    public final Object getOrDefault(Object obj, Object obj2) {
        Object obj3 = get(obj);
        return obj3 != null ? obj3 : obj2;
    }

    @Override // java.util.Map
    /* renamed from: h */
    public final zzs entrySet() {
        zzs zzsVar = this.f16276a;
        if (zzsVar == null) {
            zzs d8 = d();
            this.f16276a = d8;
            return d8;
        }
        return zzsVar;
    }

    @Override // java.util.Map
    public final int hashCode() {
        return b.a(entrySet());
    }

    @Override // java.util.Map
    public final boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.Map
    public final /* bridge */ /* synthetic */ Set keySet() {
        zzs zzsVar = this.f16277b;
        if (zzsVar == null) {
            zzs f5 = f();
            this.f16277b = f5;
            return f5;
        }
        return zzsVar;
    }

    @Override // java.util.Map
    @Deprecated
    public final Object put(Object obj, Object obj2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @Deprecated
    public final void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @Deprecated
    public final Object remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    public final String toString() {
        int size = size();
        if (size < 0) {
            throw new IllegalArgumentException("size cannot be negative but was: " + size);
        }
        StringBuilder sb = new StringBuilder((int) Math.min(size * 8, 1073741824L));
        sb.append('{');
        boolean z4 = true;
        for (Map.Entry entry : entrySet()) {
            if (!z4) {
                sb.append(", ");
            }
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
            z4 = false;
        }
        sb.append('}');
        return sb.toString();
    }
}
