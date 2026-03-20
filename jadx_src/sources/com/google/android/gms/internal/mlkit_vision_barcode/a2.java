package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Iterator;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class a2 implements Iterator {

    /* renamed from: a  reason: collision with root package name */
    final Iterator f13245a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a2(Iterator it) {
        Objects.requireNonNull(it);
        this.f13245a = it;
    }

    abstract Object a(Object obj);

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f13245a.hasNext();
    }

    @Override // java.util.Iterator
    public final Object next() {
        return a(this.f13245a.next());
    }

    @Override // java.util.Iterator
    public final void remove() {
        this.f13245a.remove();
    }
}
