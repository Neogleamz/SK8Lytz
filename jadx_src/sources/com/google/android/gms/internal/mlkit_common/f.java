package com.google.android.gms.internal.mlkit_common;

import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class f extends x {

    /* renamed from: a  reason: collision with root package name */
    private final int f12970a;

    /* renamed from: b  reason: collision with root package name */
    private int f12971b;

    /* JADX INFO: Access modifiers changed from: protected */
    public f(int i8, int i9) {
        d.b(i9, i8, "index");
        this.f12970a = i8;
        this.f12971b = i9;
    }

    protected abstract Object a(int i8);

    @Override // java.util.Iterator, java.util.ListIterator
    public final boolean hasNext() {
        return this.f12971b < this.f12970a;
    }

    @Override // java.util.ListIterator
    public final boolean hasPrevious() {
        return this.f12971b > 0;
    }

    @Override // java.util.Iterator, java.util.ListIterator
    public final Object next() {
        if (hasNext()) {
            int i8 = this.f12971b;
            this.f12971b = i8 + 1;
            return a(i8);
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.ListIterator
    public final int nextIndex() {
        return this.f12971b;
    }

    @Override // java.util.ListIterator
    public final Object previous() {
        if (hasPrevious()) {
            int i8 = this.f12971b - 1;
            this.f12971b = i8;
            return a(i8);
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.ListIterator
    public final int previousIndex() {
        return this.f12971b - 1;
    }
}
