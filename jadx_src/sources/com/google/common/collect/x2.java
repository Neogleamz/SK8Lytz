package com.google.common.collect;

import com.google.common.collect.m1;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class x2<R, C, V> extends y2<R, C, V> {
    private static final long serialVersionUID = 0;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends y2<R, C, V>.d implements SortedMap<R, Map<C, V>> {
        private b() {
            super();
        }

        @Override // java.util.SortedMap
        public Comparator<? super R> comparator() {
            return x2.this.t().comparator();
        }

        @Override // java.util.SortedMap
        public R firstKey() {
            return (R) x2.this.t().firstKey();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.m1.k
        /* renamed from: h */
        public SortedSet<R> b() {
            return new m1.i(this);
        }

        @Override // java.util.SortedMap
        public SortedMap<R, Map<C, V>> headMap(R r4) {
            com.google.common.base.l.n(r4);
            return new x2(x2.this.t().headMap(r4), x2.this.f19518d).b();
        }

        @Override // com.google.common.collect.m1.k, java.util.AbstractMap, java.util.Map
        /* renamed from: i */
        public SortedSet<R> keySet() {
            return (SortedSet) super.keySet();
        }

        @Override // java.util.SortedMap
        public R lastKey() {
            return (R) x2.this.t().lastKey();
        }

        @Override // java.util.SortedMap
        public SortedMap<R, Map<C, V>> subMap(R r4, R r8) {
            com.google.common.base.l.n(r4);
            com.google.common.base.l.n(r8);
            return new x2(x2.this.t().subMap(r4, r8), x2.this.f19518d).b();
        }

        @Override // java.util.SortedMap
        public SortedMap<R, Map<C, V>> tailMap(R r4) {
            com.google.common.base.l.n(r4);
            return new x2(x2.this.t().tailMap(r4), x2.this.f19518d).b();
        }
    }

    x2(SortedMap<R, Map<C, V>> sortedMap, com.google.common.base.r<? extends Map<C, V>> rVar) {
        super(sortedMap, rVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SortedMap<R, Map<C, V>> t() {
        return (SortedMap) this.f19517c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.y2
    /* renamed from: r */
    public SortedMap<R, Map<C, V>> m() {
        return new b();
    }

    @Override // com.google.common.collect.y2, com.google.common.collect.z2
    /* renamed from: s */
    public SortedMap<R, Map<C, V>> b() {
        return (SortedMap) super.b();
    }
}
