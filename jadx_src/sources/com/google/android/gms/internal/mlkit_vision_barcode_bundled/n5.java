package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n5 extends AbstractList implements RandomAccess, f3 {

    /* renamed from: a  reason: collision with root package name */
    private final f3 f14821a;

    public n5(f3 f3Var) {
        this.f14821a = f3Var;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.f3
    public final f3 d() {
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.f3
    public final List f() {
        return this.f14821a.f();
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object get(int i8) {
        return ((e3) this.f14821a).get(i8);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public final Iterator iterator() {
        return new m5(this);
    }

    @Override // java.util.AbstractList, java.util.List
    public final ListIterator listIterator(int i8) {
        return new l5(this, i8);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.f3
    public final Object m(int i8) {
        return this.f14821a.m(i8);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f14821a.size();
    }
}
