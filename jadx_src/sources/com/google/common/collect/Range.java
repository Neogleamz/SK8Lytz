package com.google.common.collect;

import java.io.Serializable;
import java.lang.Comparable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class Range<C extends Comparable> extends a2 implements com.google.common.base.m<C> {

    /* renamed from: c  reason: collision with root package name */
    private static final Range<Comparable> f19113c = new Range<>(c0.f(), c0.c());
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    final c0<C> f19114a;

    /* renamed from: b  reason: collision with root package name */
    final c0<C> f19115b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a extends y1<Range<?>> implements Serializable {

        /* renamed from: a  reason: collision with root package name */
        static final y1<Range<?>> f19116a = new a();
        private static final long serialVersionUID = 0;

        private a() {
        }

        @Override // com.google.common.collect.y1, java.util.Comparator
        /* renamed from: g */
        public int compare(Range<?> range, Range<?> range2) {
            return b0.k().f(range.f19114a, range2.f19114a).f(range.f19115b, range2.f19115b).j();
        }
    }

    private Range(c0<C> c0Var, c0<C> c0Var2) {
        this.f19114a = (c0) com.google.common.base.l.n(c0Var);
        this.f19115b = (c0) com.google.common.base.l.n(c0Var2);
        if (c0Var.compareTo(c0Var2) > 0 || c0Var == c0.c() || c0Var2 == c0.f()) {
            String valueOf = String.valueOf(j(c0Var, c0Var2));
            throw new IllegalArgumentException(valueOf.length() != 0 ? "Invalid range: ".concat(valueOf) : new String("Invalid range: "));
        }
    }

    public static <C extends Comparable<?>> Range<C> a() {
        return (Range<C>) f19113c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(Comparable comparable, Comparable comparable2) {
        return comparable.compareTo(comparable2);
    }

    static <C extends Comparable<?>> Range<C> e(c0<C> c0Var, c0<C> c0Var2) {
        return new Range<>(c0Var, c0Var2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <C extends Comparable<?>> y1<Range<C>> i() {
        return (y1<Range<C>>) a.f19116a;
    }

    private static String j(c0<?> c0Var, c0<?> c0Var2) {
        StringBuilder sb = new StringBuilder(16);
        c0Var.i(sb);
        sb.append("..");
        c0Var2.j(sb);
        return sb.toString();
    }

    @Override // com.google.common.base.m
    @Deprecated
    /* renamed from: b */
    public boolean apply(C c9) {
        return d(c9);
    }

    public boolean d(C c9) {
        com.google.common.base.l.n(c9);
        return this.f19114a.k(c9) && !this.f19115b.k(c9);
    }

    @Override // com.google.common.base.m
    public boolean equals(Object obj) {
        if (obj instanceof Range) {
            Range range = (Range) obj;
            return this.f19114a.equals(range.f19114a) && this.f19115b.equals(range.f19115b);
        }
        return false;
    }

    public Range<C> f(Range<C> range) {
        int compareTo = this.f19114a.compareTo(range.f19114a);
        int compareTo2 = this.f19115b.compareTo(range.f19115b);
        if (compareTo < 0 || compareTo2 > 0) {
            if (compareTo > 0 || compareTo2 < 0) {
                c0<C> c0Var = compareTo >= 0 ? this.f19114a : range.f19114a;
                c0<C> c0Var2 = compareTo2 <= 0 ? this.f19115b : range.f19115b;
                com.google.common.base.l.j(c0Var.compareTo(c0Var2) <= 0, "intersection is undefined for disconnected ranges %s and %s", this, range);
                return e(c0Var, c0Var2);
            }
            return range;
        }
        return this;
    }

    public boolean g(Range<C> range) {
        return this.f19114a.compareTo(range.f19115b) <= 0 && range.f19114a.compareTo(this.f19115b) <= 0;
    }

    public boolean h() {
        return this.f19114a.equals(this.f19115b);
    }

    public int hashCode() {
        return (this.f19114a.hashCode() * 31) + this.f19115b.hashCode();
    }

    Object readResolve() {
        return equals(f19113c) ? a() : this;
    }

    public String toString() {
        return j(this.f19114a, this.f19115b);
    }
}
