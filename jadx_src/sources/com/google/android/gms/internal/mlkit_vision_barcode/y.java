package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class y extends k1 {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ a0 f14226a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y(a0 a0Var) {
        this.f14226a = a0Var;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.k1, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        Set entrySet = this.f14226a.f13240c.entrySet();
        Objects.requireNonNull(entrySet);
        try {
            return entrySet.contains(obj);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.k1
    final Map e() {
        return this.f14226a;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator iterator() {
        return new z(this.f14226a);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean remove(Object obj) {
        if (contains(obj)) {
            Map.Entry entry = (Map.Entry) obj;
            entry.getClass();
            i0.m(this.f14226a.f13241d, entry.getKey());
            return true;
        }
        return false;
    }
}
