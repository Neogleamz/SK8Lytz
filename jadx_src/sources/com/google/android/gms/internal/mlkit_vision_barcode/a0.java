package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a0 extends n1 {

    /* renamed from: c  reason: collision with root package name */
    final transient Map f13240c;

    /* renamed from: d  reason: collision with root package name */
    final /* synthetic */ i0 f13241d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a0(i0 i0Var, Map map) {
        this.f13241d = i0Var;
        this.f13240c = map;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.n1
    protected final Set a() {
        return new y(this);
    }

    @Override // java.util.AbstractMap, java.util.Map
    /* renamed from: b */
    public final Collection get(Object obj) {
        Collection collection = (Collection) o1.a(this.f13240c, obj);
        if (collection == null) {
            return null;
        }
        return this.f13241d.f(obj, collection);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final void clear() {
        Map map;
        Map map2 = this.f13240c;
        i0 i0Var = this.f13241d;
        map = i0Var.f13545c;
        if (map2 == map) {
            i0Var.n();
        } else {
            h1.a(new z(this));
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean containsKey(Object obj) {
        return o1.b(this.f13240c, obj);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean equals(Object obj) {
        return this == obj || this.f13240c.equals(obj);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final int hashCode() {
        return this.f13240c.hashCode();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Set keySet() {
        return this.f13241d.k();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final /* bridge */ /* synthetic */ Object remove(Object obj) {
        int i8;
        Collection collection = (Collection) this.f13240c.remove(obj);
        if (collection == null) {
            return null;
        }
        Collection e8 = this.f13241d.e();
        e8.addAll(collection);
        i0 i0Var = this.f13241d;
        i8 = i0Var.f13546d;
        i0Var.f13546d = i8 - collection.size();
        collection.clear();
        return e8;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final int size() {
        return this.f13240c.size();
    }

    @Override // java.util.AbstractMap
    public final String toString() {
        return this.f13240c.toString();
    }
}
