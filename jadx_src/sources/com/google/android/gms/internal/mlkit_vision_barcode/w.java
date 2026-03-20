package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class w extends c2 {

    /* renamed from: a  reason: collision with root package name */
    private final int f14142a;

    /* renamed from: b  reason: collision with root package name */
    private int f14143b;

    /* JADX INFO: Access modifiers changed from: protected */
    public w(int i8, int i9) {
        u.b(i9, i8, "index");
        this.f14142a = i8;
        this.f14143b = i9;
    }

    protected abstract Object a(int i8);

    @Override // java.util.Iterator, java.util.ListIterator
    public final boolean hasNext() {
        return this.f14143b < this.f14142a;
    }

    @Override // java.util.ListIterator
    public final boolean hasPrevious() {
        return this.f14143b > 0;
    }

    @Override // java.util.Iterator, java.util.ListIterator
    public final Object next() {
        if (hasNext()) {
            int i8 = this.f14143b;
            this.f14143b = i8 + 1;
            return a(i8);
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.ListIterator
    public final int nextIndex() {
        return this.f14143b;
    }

    @Override // java.util.ListIterator
    public final Object previous() {
        if (hasPrevious()) {
            int i8 = this.f14143b - 1;
            this.f14143b = i8;
            return a(i8);
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.ListIterator
    public final int previousIndex() {
        return this.f14143b - 1;
    }
}
