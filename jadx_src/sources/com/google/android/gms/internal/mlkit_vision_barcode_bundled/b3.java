package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b3 implements Iterator {

    /* renamed from: a  reason: collision with root package name */
    private final Iterator f14730a;

    public b3(Iterator it) {
        this.f14730a = it;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f14730a.hasNext();
    }

    @Override // java.util.Iterator
    public final /* bridge */ /* synthetic */ Object next() {
        Map.Entry entry = (Map.Entry) this.f14730a.next();
        return entry.getValue() instanceof c3 ? new a3(entry, null) : entry;
    }

    @Override // java.util.Iterator
    public final void remove() {
        this.f14730a.remove();
    }
}
