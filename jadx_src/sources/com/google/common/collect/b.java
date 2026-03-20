package com.google.common.collect;

import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class b<E> extends e3<E> {

    /* renamed from: a  reason: collision with root package name */
    private final int f19172a;

    /* renamed from: b  reason: collision with root package name */
    private int f19173b;

    /* JADX INFO: Access modifiers changed from: protected */
    public b(int i8) {
        this(i8, 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public b(int i8, int i9) {
        com.google.common.base.l.p(i9, i8);
        this.f19172a = i8;
        this.f19173b = i9;
    }

    protected abstract E a(int i8);

    @Override // java.util.Iterator, java.util.ListIterator
    public final boolean hasNext() {
        return this.f19173b < this.f19172a;
    }

    @Override // java.util.ListIterator
    public final boolean hasPrevious() {
        return this.f19173b > 0;
    }

    @Override // java.util.Iterator, java.util.ListIterator
    public final E next() {
        if (hasNext()) {
            int i8 = this.f19173b;
            this.f19173b = i8 + 1;
            return a(i8);
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.ListIterator
    public final int nextIndex() {
        return this.f19173b;
    }

    @Override // java.util.ListIterator
    public final E previous() {
        if (hasPrevious()) {
            int i8 = this.f19173b - 1;
            this.f19173b = i8;
            return a(i8);
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.ListIterator
    public final int previousIndex() {
        return this.f19173b - 1;
    }
}
