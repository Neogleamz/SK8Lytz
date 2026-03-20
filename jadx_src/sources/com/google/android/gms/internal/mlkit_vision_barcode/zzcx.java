package com.google.android.gms.internal.mlkit_vision_barcode;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class zzcx implements Map, Serializable {

    /* renamed from: a  reason: collision with root package name */
    private transient zzcy f14312a;

    /* renamed from: b  reason: collision with root package name */
    private transient zzcy f14313b;

    /* renamed from: c  reason: collision with root package name */
    private transient zzcq f14314c;

    public static zzcx c(Object obj, Object obj2) {
        n0.b("optional-module-barcode", "com.google.android.gms.vision.barcode");
        return x1.i(1, new Object[]{"optional-module-barcode", "com.google.android.gms.vision.barcode"}, null);
    }

    abstract zzcq a();

    @Override // java.util.Map
    /* renamed from: b */
    public final zzcq values() {
        zzcq zzcqVar = this.f14314c;
        if (zzcqVar == null) {
            zzcq a9 = a();
            this.f14314c = a9;
            return a9;
        }
        return zzcqVar;
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

    abstract zzcy d();

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

    abstract zzcy f();

    @Override // java.util.Map
    public abstract Object get(Object obj);

    @Override // java.util.Map
    public final Object getOrDefault(Object obj, Object obj2) {
        Object obj3 = get(obj);
        return obj3 != null ? obj3 : obj2;
    }

    @Override // java.util.Map
    /* renamed from: h */
    public final zzcy entrySet() {
        zzcy zzcyVar = this.f14312a;
        if (zzcyVar == null) {
            zzcy d8 = d();
            this.f14312a = d8;
            return d8;
        }
        return zzcyVar;
    }

    @Override // java.util.Map
    public final int hashCode() {
        return z1.a(entrySet());
    }

    @Override // java.util.Map
    public final boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.Map
    public final /* bridge */ /* synthetic */ Set keySet() {
        zzcy zzcyVar = this.f14313b;
        if (zzcyVar == null) {
            zzcy f5 = f();
            this.f14313b = f5;
            return f5;
        }
        return zzcyVar;
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
        n0.a(size, "size");
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
