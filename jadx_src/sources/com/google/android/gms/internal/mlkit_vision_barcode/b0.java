package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b0 implements Iterator {

    /* renamed from: a  reason: collision with root package name */
    Map.Entry f13287a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ Iterator f13288b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ c0 f13289c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b0(c0 c0Var, Iterator it) {
        this.f13289c = c0Var;
        this.f13288b = it;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f13288b.hasNext();
    }

    @Override // java.util.Iterator
    public final Object next() {
        Map.Entry entry = (Map.Entry) this.f13288b.next();
        this.f13287a = entry;
        return entry.getKey();
    }

    @Override // java.util.Iterator
    public final void remove() {
        int i8;
        u.e(this.f13287a != null, "no calls to next() since the last call to remove()");
        Collection collection = (Collection) this.f13287a.getValue();
        this.f13288b.remove();
        i0 i0Var = this.f13289c.f13319b;
        i8 = i0Var.f13546d;
        i0Var.f13546d = i8 - collection.size();
        collection.clear();
        this.f13287a = null;
    }
}
