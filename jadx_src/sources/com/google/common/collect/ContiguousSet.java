package com.google.common.collect;

import java.lang.Comparable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ContiguousSet<C extends Comparable> extends ImmutableSortedSet<C> {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableSortedSet
    /* renamed from: A0 */
    public abstract ContiguousSet<C> i0(C c9, boolean z4, C c10, boolean z8);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet, java.util.SortedSet
    /* renamed from: B0 */
    public ContiguousSet<C> tailSet(C c9) {
        return l0((Comparable) com.google.common.base.l.n(c9), true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    /* renamed from: C0 */
    public ContiguousSet<C> tailSet(C c9, boolean z4) {
        return l0((Comparable) com.google.common.base.l.n(c9), z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableSortedSet
    /* renamed from: D0 */
    public abstract ContiguousSet<C> l0(C c9, boolean z4);

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<C> T() {
        return new f0(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet, java.util.SortedSet
    /* renamed from: s0 */
    public ContiguousSet<C> headSet(C c9) {
        return e0((Comparable) com.google.common.base.l.n(c9), false);
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        return w0().toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    /* renamed from: u0 */
    public ContiguousSet<C> headSet(C c9, boolean z4) {
        return e0((Comparable) com.google.common.base.l.n(c9), z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableSortedSet
    /* renamed from: v0 */
    public abstract ContiguousSet<C> e0(C c9, boolean z4);

    public abstract Range<C> w0();

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet, java.util.SortedSet
    /* renamed from: x0 */
    public ContiguousSet<C> subSet(C c9, C c10) {
        com.google.common.base.l.n(c9);
        com.google.common.base.l.n(c10);
        com.google.common.base.l.d(comparator().compare(c9, c10) <= 0);
        return i0(c9, true, c10, false);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    /* renamed from: z0 */
    public ContiguousSet<C> subSet(C c9, boolean z4, C c10, boolean z8) {
        com.google.common.base.l.n(c9);
        com.google.common.base.l.n(c10);
        com.google.common.base.l.d(comparator().compare(c9, c10) <= 0);
        return i0(c9, z4, c10, z8);
    }
}
