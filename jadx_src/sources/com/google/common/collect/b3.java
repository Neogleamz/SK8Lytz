package com.google.common.collect;

import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class b3<F, T> implements Iterator<T> {

    /* renamed from: a  reason: collision with root package name */
    final Iterator<? extends F> f19178a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b3(Iterator<? extends F> it) {
        this.f19178a = (Iterator) com.google.common.base.l.n(it);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract T a(F f5);

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f19178a.hasNext();
    }

    @Override // java.util.Iterator
    public final T next() {
        return a(this.f19178a.next());
    }

    @Override // java.util.Iterator
    public final void remove() {
        this.f19178a.remove();
    }
}
