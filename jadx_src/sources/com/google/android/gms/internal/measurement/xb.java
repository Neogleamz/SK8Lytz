package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class xb extends AbstractList<String> implements o9, RandomAccess {

    /* renamed from: a  reason: collision with root package name */
    private final o9 f12671a;

    public xb(o9 o9Var) {
        this.f12671a = o9Var;
    }

    @Override // com.google.android.gms.internal.measurement.o9
    public final o9 b() {
        return this;
    }

    @Override // com.google.android.gms.internal.measurement.o9
    public final List<?> d() {
        return this.f12671a.d();
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i8) {
        return (String) this.f12671a.get(i8);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public final Iterator<String> iterator() {
        return new ac(this);
    }

    @Override // com.google.android.gms.internal.measurement.o9
    public final Object j(int i8) {
        return this.f12671a.j(i8);
    }

    @Override // java.util.AbstractList, java.util.List
    public final ListIterator<String> listIterator(int i8) {
        return new wb(this, i8);
    }

    @Override // com.google.android.gms.internal.measurement.o9
    public final void r0(zzij zzijVar) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f12671a.size();
    }
}
