package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e3 extends a1 implements RandomAccess, f3 {

    /* renamed from: c  reason: collision with root package name */
    private static final e3 f14751c;
    @Deprecated

    /* renamed from: d  reason: collision with root package name */
    public static final f3 f14752d;

    /* renamed from: b  reason: collision with root package name */
    private final List f14753b;

    static {
        e3 e3Var = new e3(false);
        f14751c = e3Var;
        f14752d = e3Var;
    }

    public e3() {
        this(10);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public e3(int i8) {
        super(true);
        ArrayList arrayList = new ArrayList(i8);
        this.f14753b = arrayList;
    }

    private e3(ArrayList arrayList) {
        super(true);
        this.f14753b = arrayList;
    }

    private e3(boolean z4) {
        super(false);
        this.f14753b = Collections.emptyList();
    }

    private static String h(Object obj) {
        return obj instanceof String ? (String) obj : obj instanceof zzdb ? ((zzdb) obj).H(y2.f14886b) : y2.d((byte[]) obj);
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ void add(int i8, Object obj) {
        e();
        this.f14753b.add(i8, (String) obj);
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.List
    public final boolean addAll(int i8, Collection collection) {
        e();
        if (collection instanceof f3) {
            collection = ((f3) collection).f();
        }
        boolean addAll = this.f14753b.addAll(i8, collection);
        ((AbstractList) this).modCount++;
        return addAll;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection collection) {
        return addAll(size(), collection);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final void clear() {
        e();
        this.f14753b.clear();
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.f3
    public final f3 d() {
        return a() ? new n5(this) : this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.f3
    public final List f() {
        return Collections.unmodifiableList(this.f14753b);
    }

    @Override // java.util.AbstractList, java.util.List
    /* renamed from: g */
    public final String get(int i8) {
        Object obj = this.f14753b.get(i8);
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof zzdb) {
            zzdb zzdbVar = (zzdb) obj;
            String H = zzdbVar.H(y2.f14886b);
            if (zzdbVar.y()) {
                this.f14753b.set(i8, H);
            }
            return H;
        }
        byte[] bArr = (byte[]) obj;
        String d8 = y2.d(bArr);
        if (x5.g(bArr)) {
            this.f14753b.set(i8, d8);
        }
        return d8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.f3
    public final Object m(int i8) {
        return this.f14753b.get(i8);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object remove(int i8) {
        e();
        Object remove = this.f14753b.remove(i8);
        ((AbstractList) this).modCount++;
        return h(remove);
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object set(int i8, Object obj) {
        e();
        return h(this.f14753b.set(i8, (String) obj));
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f14753b.size();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2
    public final /* bridge */ /* synthetic */ x2 z(int i8) {
        if (i8 >= size()) {
            ArrayList arrayList = new ArrayList(i8);
            arrayList.addAll(this.f14753b);
            return new e3(arrayList);
        }
        throw new IllegalArgumentException();
    }
}
