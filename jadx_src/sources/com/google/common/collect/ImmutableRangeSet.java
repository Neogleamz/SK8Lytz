package com.google.common.collect;

import java.io.Serializable;
import java.lang.Comparable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ImmutableRangeSet<C extends Comparable> extends j<C> implements Serializable {

    /* renamed from: b  reason: collision with root package name */
    private static final ImmutableRangeSet<Comparable<?>> f19008b = new ImmutableRangeSet<>(ImmutableList.E());

    /* renamed from: c  reason: collision with root package name */
    private static final ImmutableRangeSet<Comparable<?>> f19009c = new ImmutableRangeSet<>(ImmutableList.F(Range.a()));

    /* renamed from: a  reason: collision with root package name */
    private final transient ImmutableList<Range<C>> f19010a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a<C extends Comparable> implements Serializable {

        /* renamed from: a  reason: collision with root package name */
        private final ImmutableList<Range<C>> f19011a;

        a(ImmutableList<Range<C>> immutableList) {
            this.f19011a = immutableList;
        }

        Object readResolve() {
            return this.f19011a.isEmpty() ? ImmutableRangeSet.d() : this.f19011a.equals(ImmutableList.F(Range.a())) ? ImmutableRangeSet.b() : new ImmutableRangeSet(this.f19011a);
        }
    }

    ImmutableRangeSet(ImmutableList<Range<C>> immutableList) {
        this.f19010a = immutableList;
    }

    static <C extends Comparable> ImmutableRangeSet<C> b() {
        return f19009c;
    }

    public static <C extends Comparable> ImmutableRangeSet<C> d() {
        return f19008b;
    }

    @Override // com.google.common.collect.c2
    /* renamed from: c */
    public ImmutableSet<Range<C>> a() {
        return this.f19010a.isEmpty() ? ImmutableSet.H() : new i2(this.f19010a, Range.i());
    }

    @Override // com.google.common.collect.j
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    Object writeReplace() {
        return new a(this.f19010a);
    }
}
