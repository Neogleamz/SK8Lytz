package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class m5 implements Iterator {

    /* renamed from: a  reason: collision with root package name */
    final Iterator f14813a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ n5 f14814b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m5(n5 n5Var) {
        f3 f3Var;
        this.f14814b = n5Var;
        f3Var = n5Var.f14821a;
        this.f14813a = f3Var.iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f14813a.hasNext();
    }

    @Override // java.util.Iterator
    public final /* bridge */ /* synthetic */ Object next() {
        return (String) this.f14813a.next();
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
