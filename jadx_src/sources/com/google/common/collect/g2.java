package com.google.common.collect;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g2<E> extends ImmutableSet<E> {

    /* renamed from: h  reason: collision with root package name */
    private static final Object[] f19301h;

    /* renamed from: j  reason: collision with root package name */
    static final g2<Object> f19302j;

    /* renamed from: c  reason: collision with root package name */
    final transient Object[] f19303c;

    /* renamed from: d  reason: collision with root package name */
    private final transient int f19304d;

    /* renamed from: e  reason: collision with root package name */
    final transient Object[] f19305e;

    /* renamed from: f  reason: collision with root package name */
    private final transient int f19306f;

    /* renamed from: g  reason: collision with root package name */
    private final transient int f19307g;

    static {
        Object[] objArr = new Object[0];
        f19301h = objArr;
        f19302j = new g2<>(objArr, 0, objArr, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g2(Object[] objArr, int i8, Object[] objArr2, int i9, int i10) {
        this.f19303c = objArr;
        this.f19304d = i8;
        this.f19305e = objArr2;
        this.f19306f = i9;
        this.f19307g = i10;
    }

    @Override // com.google.common.collect.ImmutableSet
    ImmutableList<E> F() {
        return ImmutableList.t(this.f19303c, this.f19307g);
    }

    @Override // com.google.common.collect.ImmutableSet
    boolean G() {
        return true;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        Object[] objArr = this.f19305e;
        if (obj == null || objArr.length == 0) {
            return false;
        }
        int d8 = v0.d(obj);
        while (true) {
            int i8 = d8 & this.f19306f;
            Object obj2 = objArr[i8];
            if (obj2 == null) {
                return false;
            }
            if (obj2.equals(obj)) {
                return true;
            }
            d8 = i8 + 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int g(Object[] objArr, int i8) {
        System.arraycopy(this.f19303c, 0, objArr, i8, this.f19307g);
        return i8 + this.f19307g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public Object[] h() {
        return this.f19303c;
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public int hashCode() {
        return this.f19304d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int i() {
        return this.f19307g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int k() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public boolean n() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: p */
    public d3<E> iterator() {
        return e().iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.f19307g;
    }
}
