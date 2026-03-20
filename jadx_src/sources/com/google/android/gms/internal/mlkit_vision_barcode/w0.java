package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class w0 extends AbstractCollection {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ x0 f14144a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w0(x0 x0Var) {
        this.f14144a = x0Var;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final void clear() {
        this.f14144a.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public final Iterator iterator() {
        x0 x0Var = this.f14144a;
        Map l8 = x0Var.l();
        return l8 != null ? l8.values().iterator() : new q0(x0Var);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final int size() {
        return this.f14144a.size();
    }
}
