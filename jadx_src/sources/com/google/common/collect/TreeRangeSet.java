package com.google.common.collect;

import java.io.Serializable;
import java.lang.Comparable;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class TreeRangeSet<C extends Comparable<?>> extends j<C> implements Serializable {

    /* renamed from: a  reason: collision with root package name */
    final NavigableMap<c0<C>, Range<C>> f19149a;

    /* renamed from: b  reason: collision with root package name */
    private transient Set<Range<C>> f19150b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class a extends j0<Range<C>> implements Set<Range<C>> {

        /* renamed from: a  reason: collision with root package name */
        final Collection<Range<C>> f19151a;

        a(TreeRangeSet treeRangeSet, Collection<Range<C>> collection) {
            this.f19151a = collection;
        }

        @Override // java.util.Collection, java.util.Set
        public boolean equals(Object obj) {
            return p2.a(this, obj);
        }

        @Override // java.util.Collection, java.util.Set
        public int hashCode() {
            return p2.d(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.j0, com.google.common.collect.p0
        /* renamed from: i */
        public Collection<Range<C>> h() {
            return this.f19151a;
        }
    }

    @Override // com.google.common.collect.c2
    public Set<Range<C>> a() {
        Set<Range<C>> set = this.f19150b;
        if (set == null) {
            a aVar = new a(this, this.f19149a.values());
            this.f19150b = aVar;
            return aVar;
        }
        return set;
    }

    @Override // com.google.common.collect.j
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }
}
