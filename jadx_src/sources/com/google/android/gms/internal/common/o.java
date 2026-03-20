package com.google.android.gms.internal.common;

import java.util.NoSuchElementException;
import org.jspecify.nullness.NullMarked;
@NullMarked
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class o extends f {

    /* renamed from: a  reason: collision with root package name */
    private final int f12049a;

    /* renamed from: b  reason: collision with root package name */
    private int f12050b;

    /* JADX INFO: Access modifiers changed from: protected */
    public o(int i8, int i9) {
        m.b(i9, i8, "index");
        this.f12049a = i8;
        this.f12050b = i9;
    }

    protected abstract Object a(int i8);

    @Override // java.util.Iterator, java.util.ListIterator
    public final boolean hasNext() {
        return this.f12050b < this.f12049a;
    }

    @Override // java.util.ListIterator
    public final boolean hasPrevious() {
        return this.f12050b > 0;
    }

    @Override // java.util.Iterator, java.util.ListIterator
    public final Object next() {
        if (hasNext()) {
            int i8 = this.f12050b;
            this.f12050b = i8 + 1;
            return a(i8);
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.ListIterator
    public final int nextIndex() {
        return this.f12050b;
    }

    @Override // java.util.ListIterator
    public final Object previous() {
        if (hasPrevious()) {
            int i8 = this.f12050b - 1;
            this.f12050b = i8;
            return a(i8);
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.ListIterator
    public final int previousIndex() {
        return this.f12050b - 1;
    }
}
