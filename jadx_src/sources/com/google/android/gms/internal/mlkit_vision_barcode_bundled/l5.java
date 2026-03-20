package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.ListIterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class l5 implements ListIterator {

    /* renamed from: a  reason: collision with root package name */
    final ListIterator f14804a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ int f14805b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ n5 f14806c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l5(n5 n5Var, int i8) {
        f3 f3Var;
        this.f14806c = n5Var;
        this.f14805b = i8;
        f3Var = n5Var.f14821a;
        this.f14804a = f3Var.listIterator(i8);
    }

    @Override // java.util.ListIterator
    public final /* synthetic */ void add(Object obj) {
        String str = (String) obj;
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public final boolean hasNext() {
        return this.f14804a.hasNext();
    }

    @Override // java.util.ListIterator
    public final boolean hasPrevious() {
        return this.f14804a.hasPrevious();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public final /* bridge */ /* synthetic */ Object next() {
        return (String) this.f14804a.next();
    }

    @Override // java.util.ListIterator
    public final int nextIndex() {
        return this.f14804a.nextIndex();
    }

    @Override // java.util.ListIterator
    public final /* bridge */ /* synthetic */ Object previous() {
        return (String) this.f14804a.previous();
    }

    @Override // java.util.ListIterator
    public final int previousIndex() {
        return this.f14804a.previousIndex();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public final /* synthetic */ void set(Object obj) {
        String str = (String) obj;
        throw new UnsupportedOperationException();
    }
}
