package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzfi extends LinkedHashMap {

    /* renamed from: b  reason: collision with root package name */
    private static final zzfi f15033b;

    /* renamed from: a  reason: collision with root package name */
    private boolean f15034a;

    static {
        zzfi zzfiVar = new zzfi();
        f15033b = zzfiVar;
        zzfiVar.f15034a = false;
    }

    private zzfi() {
        this.f15034a = true;
    }

    private zzfi(Map map) {
        super(map);
        this.f15034a = true;
    }

    public static zzfi b() {
        return f15033b;
    }

    private static int k(Object obj) {
        if (!(obj instanceof byte[])) {
            if (obj instanceof r2) {
                throw new UnsupportedOperationException();
            }
            return obj.hashCode();
        }
        byte[] bArr = (byte[]) obj;
        byte[] bArr2 = y2.f14888d;
        int length = bArr.length;
        int b9 = y2.b(length, bArr, 0, length);
        if (b9 == 0) {
            return 1;
        }
        return b9;
    }

    private final void l() {
        if (!this.f15034a) {
            throw new UnsupportedOperationException();
        }
    }

    public final zzfi c() {
        return isEmpty() ? new zzfi() : new zzfi(this);
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final void clear() {
        l();
        super.clear();
    }

    public final void d() {
        this.f15034a = false;
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final Set entrySet() {
        return isEmpty() ? Collections.emptySet() : super.entrySet();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean equals(Object obj) {
        boolean equals;
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (this == map) {
                return true;
            }
            if (size() != map.size()) {
                return false;
            }
            Iterator it = entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (!map.containsKey(entry.getKey())) {
                    return false;
                }
                Object value = entry.getValue();
                Object obj2 = map.get(entry.getKey());
                if ((value instanceof byte[]) && (obj2 instanceof byte[])) {
                    equals = Arrays.equals((byte[]) value, (byte[]) obj2);
                    continue;
                } else {
                    equals = value.equals(obj2);
                    continue;
                }
                if (!equals) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public final void f(zzfi zzfiVar) {
        l();
        if (zzfiVar.isEmpty()) {
            return;
        }
        putAll(zzfiVar);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final int hashCode() {
        Iterator it = entrySet().iterator();
        int i8 = 0;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            i8 += k(entry.getValue()) ^ k(entry.getKey());
        }
        return i8;
    }

    public final boolean j() {
        return this.f15034a;
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final Object put(Object obj, Object obj2) {
        l();
        byte[] bArr = y2.f14888d;
        Objects.requireNonNull(obj);
        Objects.requireNonNull(obj2);
        return super.put(obj, obj2);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final void putAll(Map map) {
        l();
        for (Object obj : map.keySet()) {
            byte[] bArr = y2.f14888d;
            Objects.requireNonNull(obj);
            Objects.requireNonNull(map.get(obj));
        }
        super.putAll(map);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final Object remove(Object obj) {
        l();
        return super.remove(obj);
    }
}
