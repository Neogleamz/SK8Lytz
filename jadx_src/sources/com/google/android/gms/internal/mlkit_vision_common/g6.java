package com.google.android.gms.internal.mlkit_vision_common;

import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class g6 extends d {

    /* renamed from: a  reason: collision with root package name */
    private final int f15506a;

    /* renamed from: b  reason: collision with root package name */
    private int f15507b;

    /* JADX INFO: Access modifiers changed from: protected */
    public g6(int i8, int i9) {
        e4.b(i9, i8, "index");
        this.f15506a = i8;
        this.f15507b = i9;
    }

    protected abstract Object a(int i8);

    @Override // java.util.Iterator, java.util.ListIterator
    public final boolean hasNext() {
        return this.f15507b < this.f15506a;
    }

    @Override // java.util.ListIterator
    public final boolean hasPrevious() {
        return this.f15507b > 0;
    }

    @Override // java.util.Iterator, java.util.ListIterator
    public final Object next() {
        if (hasNext()) {
            int i8 = this.f15507b;
            this.f15507b = i8 + 1;
            return a(i8);
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.ListIterator
    public final int nextIndex() {
        return this.f15507b;
    }

    @Override // java.util.ListIterator
    public final Object previous() {
        if (hasPrevious()) {
            int i8 = this.f15507b - 1;
            this.f15507b = i8;
            return a(i8);
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.ListIterator
    public final int previousIndex() {
        return this.f15507b - 1;
    }
}
