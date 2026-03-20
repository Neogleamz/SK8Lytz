package com.google.common.collect;

import com.google.common.collect.z2;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class j2<R, C, V> extends ImmutableTable<R, C, V> {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class b extends e1<z2.a<R, C, V>> {
        private b() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.e1
        /* renamed from: Q */
        public z2.a<R, C, V> get(int i8) {
            return j2.this.A(i8);
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            if (obj instanceof z2.a) {
                z2.a aVar = (z2.a) obj;
                Object h8 = j2.this.h(aVar.b(), aVar.a());
                return h8 != null && h8.equals(aVar.getValue());
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean n() {
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return j2.this.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class c extends ImmutableList<V> {
        private c() {
        }

        @Override // java.util.List
        public V get(int i8) {
            return (V) j2.this.B(i8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean n() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return j2.this.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <R, C, V> j2<R, C, V> z(ImmutableList<z2.a<R, C, V>> immutableList, ImmutableSet<R> immutableSet, ImmutableSet<C> immutableSet2) {
        return ((long) immutableList.size()) > (((long) immutableSet.size()) * ((long) immutableSet2.size())) / 2 ? new d0(immutableList, immutableSet, immutableSet2) : new w2(immutableList, immutableSet, immutableSet2);
    }

    abstract z2.a<R, C, V> A(int i8);

    abstract V B(int i8);

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.o
    /* renamed from: q */
    public final ImmutableSet<z2.a<R, C, V>> f() {
        return i() ? ImmutableSet.H() : new b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.o
    /* renamed from: s */
    public final ImmutableCollection<V> g() {
        return i() ? ImmutableList.E() : new c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void y(R r4, C c9, V v8, V v9) {
        com.google.common.base.l.k(v8 == null, "Duplicate key: (row=%s, column=%s), values: [%s, %s].", r4, c9, v9, v8);
    }
}
