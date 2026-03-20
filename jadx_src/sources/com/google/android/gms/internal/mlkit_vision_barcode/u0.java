package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class u0 extends AbstractSet {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ x0 f14055a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public u0(x0 x0Var) {
        this.f14055a = x0Var;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final void clear() {
        this.f14055a.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        return this.f14055a.containsKey(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator iterator() {
        x0 x0Var = this.f14055a;
        Map l8 = x0Var.l();
        return l8 != null ? l8.keySet().iterator() : new o0(x0Var);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean remove(Object obj) {
        Object u8;
        Object obj2;
        Map l8 = this.f14055a.l();
        if (l8 != null) {
            return l8.keySet().remove(obj);
        }
        u8 = this.f14055a.u(obj);
        obj2 = x0.f14186k;
        return u8 != obj2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.f14055a.size();
    }
}
