package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class z implements Iterator {

    /* renamed from: a  reason: collision with root package name */
    final Iterator f14263a;

    /* renamed from: b  reason: collision with root package name */
    Collection f14264b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ a0 f14265c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public z(a0 a0Var) {
        this.f14265c = a0Var;
        this.f14263a = a0Var.f13240c.entrySet().iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f14263a.hasNext();
    }

    @Override // java.util.Iterator
    public final /* bridge */ /* synthetic */ Object next() {
        Map.Entry entry = (Map.Entry) this.f14263a.next();
        this.f14264b = (Collection) entry.getValue();
        a0 a0Var = this.f14265c;
        Object key = entry.getKey();
        return new c1(key, a0Var.f13241d.f(key, (Collection) entry.getValue()));
    }

    @Override // java.util.Iterator
    public final void remove() {
        int i8;
        u.e(this.f14264b != null, "no calls to next() since the last call to remove()");
        this.f14263a.remove();
        i0 i0Var = this.f14265c.f13241d;
        i8 = i0Var.f13546d;
        i0Var.f13546d = i8 - this.f14264b.size();
        this.f14264b.clear();
        this.f14264b = null;
    }
}
