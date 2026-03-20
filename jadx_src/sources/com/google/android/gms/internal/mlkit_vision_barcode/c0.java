package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c0 extends l1 {

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ i0 f13319b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public c0(i0 i0Var, Map map) {
        super(map);
        this.f13319b = i0Var;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final void clear() {
        h1.a(iterator());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean containsAll(Collection collection) {
        return this.f13651a.keySet().containsAll(collection);
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
    public final boolean equals(Object obj) {
        return this == obj || this.f13651a.keySet().equals(obj);
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
    public final int hashCode() {
        return this.f13651a.keySet().hashCode();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.l1, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator iterator() {
        return new b0(this, this.f13651a.entrySet().iterator());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean remove(Object obj) {
        int i8;
        Collection collection = (Collection) this.f13651a.remove(obj);
        if (collection != null) {
            int size = collection.size();
            collection.clear();
            i0 i0Var = this.f13319b;
            i8 = i0Var.f13546d;
            i0Var.f13546d = i8 - size;
            return size > 0;
        }
        return false;
    }
}
