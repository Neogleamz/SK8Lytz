package com.google.android.gms.internal.measurement;

import java.util.Iterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n implements Iterator<r> {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ Iterator f12358a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(Iterator it) {
        this.f12358a = it;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f12358a.hasNext();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ r next() {
        return new t((String) this.f12358a.next());
    }
}
