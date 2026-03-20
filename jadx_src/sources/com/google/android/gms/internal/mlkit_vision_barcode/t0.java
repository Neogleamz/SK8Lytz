package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class t0 implements Iterator {

    /* renamed from: a  reason: collision with root package name */
    int f14022a;

    /* renamed from: b  reason: collision with root package name */
    int f14023b;

    /* renamed from: c  reason: collision with root package name */
    int f14024c;

    /* renamed from: d  reason: collision with root package name */
    final /* synthetic */ x0 f14025d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ t0(x0 x0Var, s0 s0Var) {
        int i8;
        this.f14025d = x0Var;
        i8 = x0Var.f14191e;
        this.f14022a = i8;
        this.f14023b = x0Var.f();
        this.f14024c = -1;
    }

    private final void b() {
        int i8;
        i8 = this.f14025d.f14191e;
        if (i8 != this.f14022a) {
            throw new ConcurrentModificationException();
        }
    }

    abstract Object a(int i8);

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f14023b >= 0;
    }

    @Override // java.util.Iterator
    public final Object next() {
        b();
        if (hasNext()) {
            int i8 = this.f14023b;
            this.f14024c = i8;
            Object a9 = a(i8);
            this.f14023b = this.f14025d.h(this.f14023b);
            return a9;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public final void remove() {
        b();
        u.e(this.f14024c >= 0, "no calls to next() since the last call to remove()");
        this.f14022a += 32;
        x0 x0Var = this.f14025d;
        int i8 = this.f14024c;
        Object[] objArr = x0Var.f14189c;
        objArr.getClass();
        x0Var.remove(objArr[i8]);
        this.f14023b--;
        this.f14024c = -1;
    }
}
