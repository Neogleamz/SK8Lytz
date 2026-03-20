package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y4 implements Map.Entry, Comparable {

    /* renamed from: a  reason: collision with root package name */
    private final Comparable f14891a;

    /* renamed from: b  reason: collision with root package name */
    private Object f14892b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ e5 f14893c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y4(e5 e5Var, Comparable comparable, Object obj) {
        this.f14893c = e5Var;
        this.f14891a = comparable;
        this.f14892b = obj;
    }

    private static final boolean f(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    public final Comparable c() {
        return this.f14891a;
    }

    @Override // java.lang.Comparable
    public final /* bridge */ /* synthetic */ int compareTo(Object obj) {
        return this.f14891a.compareTo(((y4) obj).f14891a);
    }

    @Override // java.util.Map.Entry
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) obj;
            return f(this.f14891a, entry.getKey()) && f(this.f14892b, entry.getValue());
        }
        return false;
    }

    @Override // java.util.Map.Entry
    public final /* synthetic */ Object getKey() {
        return this.f14891a;
    }

    @Override // java.util.Map.Entry
    public final Object getValue() {
        return this.f14892b;
    }

    @Override // java.util.Map.Entry
    public final int hashCode() {
        Comparable comparable = this.f14891a;
        int hashCode = comparable == null ? 0 : comparable.hashCode();
        Object obj = this.f14892b;
        return hashCode ^ (obj != null ? obj.hashCode() : 0);
    }

    @Override // java.util.Map.Entry
    public final Object setValue(Object obj) {
        this.f14893c.p();
        Object obj2 = this.f14892b;
        this.f14892b = obj;
        return obj2;
    }

    public final String toString() {
        String valueOf = String.valueOf(this.f14891a);
        String valueOf2 = String.valueOf(this.f14892b);
        return valueOf + "=" + valueOf2;
    }
}
