package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l9 extends l7<String> implements o9, RandomAccess {

    /* renamed from: c  reason: collision with root package name */
    private static final l9 f12301c;
    @Deprecated

    /* renamed from: d  reason: collision with root package name */
    private static final o9 f12302d;

    /* renamed from: b  reason: collision with root package name */
    private final List<Object> f12303b;

    static {
        l9 l9Var = new l9(false);
        f12301c = l9Var;
        f12302d = l9Var;
    }

    public l9(int i8) {
        this(new ArrayList(i8));
    }

    private l9(ArrayList<Object> arrayList) {
        this.f12303b = arrayList;
    }

    private l9(boolean z4) {
        super(false);
        this.f12303b = Collections.emptyList();
    }

    private static String g(Object obj) {
        return obj instanceof String ? (String) obj : obj instanceof zzij ? ((zzij) obj).A() : a9.h((byte[]) obj);
    }

    @Override // com.google.android.gms.internal.measurement.l7, com.google.android.gms.internal.measurement.g9
    public final /* bridge */ /* synthetic */ boolean a() {
        return super.a();
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ void add(int i8, Object obj) {
        e();
        this.f12303b.add(i8, (String) obj);
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ /* synthetic */ boolean add(Object obj) {
        return super.add(obj);
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.List
    public final boolean addAll(int i8, Collection<? extends String> collection) {
        e();
        if (collection instanceof o9) {
            collection = ((o9) collection).d();
        }
        boolean addAll = this.f12303b.addAll(i8, collection);
        ((AbstractList) this).modCount++;
        return addAll;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection<? extends String> collection) {
        return addAll(size(), collection);
    }

    @Override // com.google.android.gms.internal.measurement.o9
    public final o9 b() {
        return a() ? new xb(this) : this;
    }

    @Override // com.google.android.gms.internal.measurement.g9
    public final /* synthetic */ g9 c(int i8) {
        if (i8 >= size()) {
            ArrayList arrayList = new ArrayList(i8);
            arrayList.addAll(this.f12303b);
            return new l9(arrayList);
        }
        throw new IllegalArgumentException();
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final void clear() {
        e();
        this.f12303b.clear();
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.measurement.o9
    public final List<?> d() {
        return Collections.unmodifiableList(this.f12303b);
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.Collection, java.util.List
    public final /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i8) {
        Object obj = this.f12303b.get(i8);
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof zzij) {
            zzij zzijVar = (zzij) obj;
            String A = zzijVar.A();
            if (zzijVar.D()) {
                this.f12303b.set(i8, A);
            }
            return A;
        }
        byte[] bArr = (byte[]) obj;
        String h8 = a9.h(bArr);
        if (a9.i(bArr)) {
            this.f12303b.set(i8, h8);
        }
        return h8;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.Collection, java.util.List
    public final /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.android.gms.internal.measurement.o9
    public final Object j(int i8) {
        return this.f12303b.get(i8);
    }

    @Override // com.google.android.gms.internal.measurement.o9
    public final void r0(zzij zzijVar) {
        e();
        this.f12303b.add(zzijVar);
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.List
    public final /* synthetic */ Object remove(int i8) {
        e();
        Object remove = this.f12303b.remove(i8);
        ((AbstractList) this).modCount++;
        return g(remove);
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ /* synthetic */ boolean remove(Object obj) {
        return super.remove(obj);
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
        return super.removeAll(collection);
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
        return super.retainAll(collection);
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object set(int i8, Object obj) {
        e();
        return g(this.f12303b.set(i8, (String) obj));
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f12303b.size();
    }
}
