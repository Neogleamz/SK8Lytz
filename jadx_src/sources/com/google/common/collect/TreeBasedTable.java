package com.google.common.collect;

import com.google.common.collect.m1;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class TreeBasedTable<R, C, V> extends x2<R, C, V> {
    private static final long serialVersionUID = 0;

    /* renamed from: f  reason: collision with root package name */
    private final Comparator<? super C> f19117f;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends y2<R, C, V>.c implements SortedMap<C, V> {

        /* renamed from: d  reason: collision with root package name */
        final C f19118d;

        /* renamed from: e  reason: collision with root package name */
        final C f19119e;

        /* renamed from: f  reason: collision with root package name */
        transient SortedMap<C, V> f19120f;

        a(TreeBasedTable treeBasedTable, R r4) {
            this(r4, null, null);
        }

        a(R r4, C c9, C c10) {
            super(r4);
            this.f19118d = c9;
            this.f19119e = c10;
            com.google.common.base.l.d(c9 == null || c10 == null || h(c9, c10) <= 0);
        }

        @Override // com.google.common.collect.y2.c
        void c() {
            l();
            SortedMap<C, V> sortedMap = this.f19120f;
            if (sortedMap == null || !sortedMap.isEmpty()) {
                return;
            }
            TreeBasedTable.this.f19517c.remove(this.f19524a);
            this.f19120f = null;
            this.f19525b = null;
        }

        @Override // java.util.SortedMap
        public Comparator<? super C> comparator() {
            return TreeBasedTable.this.u();
        }

        @Override // com.google.common.collect.y2.c, java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object obj) {
            return k(obj) && super.containsKey(obj);
        }

        @Override // java.util.SortedMap
        public C firstKey() {
            d();
            Map<C, V> map = this.f19525b;
            if (map != null) {
                return (C) ((SortedMap) map).firstKey();
            }
            throw new NoSuchElementException();
        }

        int h(Object obj, Object obj2) {
            return comparator().compare(obj, obj2);
        }

        @Override // java.util.SortedMap
        public SortedMap<C, V> headMap(C c9) {
            com.google.common.base.l.d(k(com.google.common.base.l.n(c9)));
            return new a(this.f19524a, this.f19118d, c9);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.y2.c
        /* renamed from: i */
        public SortedMap<C, V> b() {
            l();
            SortedMap<C, V> sortedMap = this.f19120f;
            if (sortedMap != null) {
                C c9 = this.f19118d;
                if (c9 != null) {
                    sortedMap = sortedMap.tailMap(c9);
                }
                C c10 = this.f19119e;
                return c10 != null ? sortedMap.headMap(c10) : sortedMap;
            }
            return null;
        }

        @Override // java.util.AbstractMap, java.util.Map, java.util.SortedMap
        /* renamed from: j */
        public SortedSet<C> keySet() {
            return new m1.i(this);
        }

        boolean k(Object obj) {
            C c9;
            C c10;
            return obj != null && ((c9 = this.f19118d) == null || h(c9, obj) <= 0) && ((c10 = this.f19119e) == null || h(c10, obj) > 0);
        }

        void l() {
            SortedMap<C, V> sortedMap = this.f19120f;
            if (sortedMap == null || (sortedMap.isEmpty() && TreeBasedTable.this.f19517c.containsKey(this.f19524a))) {
                this.f19120f = (SortedMap) TreeBasedTable.this.f19517c.get(this.f19524a);
            }
        }

        @Override // java.util.SortedMap
        public C lastKey() {
            d();
            Map<C, V> map = this.f19525b;
            if (map != null) {
                return (C) ((SortedMap) map).lastKey();
            }
            throw new NoSuchElementException();
        }

        @Override // com.google.common.collect.y2.c, java.util.AbstractMap, java.util.Map
        public V put(C c9, V v8) {
            com.google.common.base.l.d(k(com.google.common.base.l.n(c9)));
            return (V) super.put(c9, v8);
        }

        @Override // java.util.SortedMap
        public SortedMap<C, V> subMap(C c9, C c10) {
            com.google.common.base.l.d(k(com.google.common.base.l.n(c9)) && k(com.google.common.base.l.n(c10)));
            return new a(this.f19524a, c9, c10);
        }

        @Override // java.util.SortedMap
        public SortedMap<C, V> tailMap(C c9) {
            com.google.common.base.l.d(k(com.google.common.base.l.n(c9)));
            return new a(this.f19524a, c9, this.f19119e);
        }
    }

    @Override // com.google.common.collect.y2, com.google.common.collect.o, com.google.common.collect.z2
    public /* bridge */ /* synthetic */ Set a() {
        return super.a();
    }

    @Override // com.google.common.collect.y2, com.google.common.collect.o
    public /* bridge */ /* synthetic */ void d() {
        super.d();
    }

    @Override // com.google.common.collect.y2, com.google.common.collect.o
    public /* bridge */ /* synthetic */ boolean e(Object obj) {
        return super.e(obj);
    }

    @Override // com.google.common.collect.o
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.google.common.collect.o
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.y2
    public /* bridge */ /* synthetic */ boolean l(Object obj) {
        return super.l(obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.y2
    public /* bridge */ /* synthetic */ Object o(Object obj, Object obj2, Object obj3) {
        return super.o(obj, obj2, obj3);
    }

    @Override // com.google.common.collect.x2, com.google.common.collect.y2, com.google.common.collect.z2
    /* renamed from: s */
    public SortedMap<R, Map<C, V>> b() {
        return super.b();
    }

    @Override // com.google.common.collect.y2, com.google.common.collect.z2
    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    @Override // com.google.common.collect.o
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Deprecated
    public Comparator<? super C> u() {
        return this.f19117f;
    }

    @Override // com.google.common.collect.y2
    /* renamed from: v */
    public SortedMap<C, V> p(R r4) {
        return new a(this, r4);
    }
}
