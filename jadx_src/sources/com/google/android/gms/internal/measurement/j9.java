package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class j9<K> implements Iterator<Map.Entry<K, Object>> {

    /* renamed from: a  reason: collision with root package name */
    private Iterator<Map.Entry<K, Object>> f12261a;

    public j9(Iterator<Map.Entry<K, Object>> it) {
        this.f12261a = it;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f12261a.hasNext();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ Object next() {
        Map.Entry<K, Object> next = this.f12261a.next();
        return next.getValue() instanceof i9 ? new h9(next) : next;
    }

    @Override // java.util.Iterator
    public final void remove() {
        this.f12261a.remove();
    }
}
