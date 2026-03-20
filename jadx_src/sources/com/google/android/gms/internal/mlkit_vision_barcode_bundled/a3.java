package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a3 implements Map.Entry {

    /* renamed from: a  reason: collision with root package name */
    private final Map.Entry f14705a;

    public final c3 a() {
        return (c3) this.f14705a.getValue();
    }

    @Override // java.util.Map.Entry
    public final Object getKey() {
        return this.f14705a.getKey();
    }

    @Override // java.util.Map.Entry
    public final Object getValue() {
        if (((c3) this.f14705a.getValue()) == null) {
            return null;
        }
        throw null;
    }

    @Override // java.util.Map.Entry
    public final Object setValue(Object obj) {
        if (obj instanceof x3) {
            return ((c3) this.f14705a.getValue()).c((x3) obj);
        }
        throw new IllegalArgumentException("LazyField now only used for MessageSet, and the value of MessageSet must be an instance of MessageLite");
    }
}
