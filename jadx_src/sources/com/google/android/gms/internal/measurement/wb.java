package com.google.android.gms.internal.measurement;

import java.util.ListIterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class wb implements ListIterator<String> {

    /* renamed from: a  reason: collision with root package name */
    private ListIterator<String> f12639a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ int f12640b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ xb f12641c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public wb(xb xbVar, int i8) {
        o9 o9Var;
        this.f12640b = i8;
        this.f12641c = xbVar;
        o9Var = xbVar.f12671a;
        this.f12639a = o9Var.listIterator(i8);
    }

    @Override // java.util.ListIterator
    public final /* synthetic */ void add(String str) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public final boolean hasNext() {
        return this.f12639a.hasNext();
    }

    @Override // java.util.ListIterator
    public final boolean hasPrevious() {
        return this.f12639a.hasPrevious();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public final /* synthetic */ Object next() {
        return this.f12639a.next();
    }

    @Override // java.util.ListIterator
    public final int nextIndex() {
        return this.f12639a.nextIndex();
    }

    @Override // java.util.ListIterator
    public final /* synthetic */ String previous() {
        return this.f12639a.previous();
    }

    @Override // java.util.ListIterator
    public final int previousIndex() {
        return this.f12639a.previousIndex();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public final /* synthetic */ void set(String str) {
        throw new UnsupportedOperationException();
    }
}
