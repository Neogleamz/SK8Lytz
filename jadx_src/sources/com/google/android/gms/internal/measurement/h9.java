package com.google.android.gms.internal.measurement;

import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h9<K> implements Map.Entry<K, Object> {

    /* renamed from: a  reason: collision with root package name */
    private Map.Entry<K, i9> f12228a;

    private h9(Map.Entry<K, i9> entry) {
        this.f12228a = entry;
    }

    public final i9 a() {
        return this.f12228a.getValue();
    }

    @Override // java.util.Map.Entry
    public final K getKey() {
        return this.f12228a.getKey();
    }

    @Override // java.util.Map.Entry
    public final Object getValue() {
        if (this.f12228a.getValue() == null) {
            return null;
        }
        return i9.e();
    }

    @Override // java.util.Map.Entry
    public final Object setValue(Object obj) {
        if (obj instanceof ia) {
            return this.f12228a.getValue().a((ia) obj);
        }
        throw new IllegalArgumentException("LazyField now only used for MessageSet, and the value of MessageSet must be an instance of MessageLite");
    }
}
