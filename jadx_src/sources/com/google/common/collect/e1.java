package com.google.common.collect;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class e1<E> extends ImmutableSet<E> {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends ImmutableList<E> {
        a() {
        }

        @Override // java.util.List
        public E get(int i8) {
            return (E) e1.this.get(i8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean n() {
            return e1.this.n();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return e1.this.size();
        }
    }

    @Override // com.google.common.collect.ImmutableSet
    ImmutableList<E> F() {
        return new a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int g(Object[] objArr, int i8) {
        return e().g(objArr, i8);
    }

    abstract E get(int i8);

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: p */
    public d3<E> iterator() {
        return e().iterator();
    }
}
