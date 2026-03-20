package com.google.android.gms.internal.mlkit_common;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class zzau implements Map, Serializable {

    /* renamed from: a  reason: collision with root package name */
    private transient zzav f13032a;

    /* renamed from: b  reason: collision with root package name */
    private transient zzav f13033b;

    /* renamed from: c  reason: collision with root package name */
    private transient zzan f13034c;

    public static zzau c(Object obj, Object obj2) {
        g.a("optional-module-barcode", "com.google.android.gms.vision.barcode");
        return u.i(1, new Object[]{"optional-module-barcode", "com.google.android.gms.vision.barcode"}, null);
    }

    abstract zzan a();

    @Override // java.util.Map
    /* renamed from: b */
    public final zzan values() {
        zzan zzanVar = this.f13034c;
        if (zzanVar == null) {
            zzan a9 = a();
            this.f13034c = a9;
            return a9;
        }
        return zzanVar;
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

    abstract zzav d();

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

    abstract zzav f();

    @Override // java.util.Map
    public abstract Object get(Object obj);

    @Override // java.util.Map
    public final Object getOrDefault(Object obj, Object obj2) {
        Object obj3 = get(obj);
        return obj3 != null ? obj3 : obj2;
    }

    @Override // java.util.Map
    /* renamed from: h */
    public final zzav entrySet() {
        zzav zzavVar = this.f13032a;
        if (zzavVar == null) {
            zzav d8 = d();
            this.f13032a = d8;
            return d8;
        }
        return zzavVar;
    }

    @Override // java.util.Map
    public final int hashCode() {
        return v.a(entrySet());
    }

    @Override // java.util.Map
    public final boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.Map
    public final /* bridge */ /* synthetic */ Set keySet() {
        zzav zzavVar = this.f13033b;
        if (zzavVar == null) {
            zzav f5 = f();
            this.f13033b = f5;
            return f5;
        }
        return zzavVar;
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
