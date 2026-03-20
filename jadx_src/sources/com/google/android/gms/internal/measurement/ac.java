package com.google.android.gms.internal.measurement;

import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class ac implements Iterator<String> {

    /* renamed from: a  reason: collision with root package name */
    private Iterator<String> f12077a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ xb f12078b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ac(xb xbVar) {
        o9 o9Var;
        this.f12078b = xbVar;
        o9Var = xbVar.f12671a;
        this.f12077a = o9Var.iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f12077a.hasNext();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ String next() {
        return this.f12077a.next();
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
