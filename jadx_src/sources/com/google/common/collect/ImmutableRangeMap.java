package com.google.common.collect;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ImmutableRangeMap<K extends Comparable<?>, V> implements b2<K, V>, Serializable {

    /* renamed from: c  reason: collision with root package name */
    private static final ImmutableRangeMap<Comparable<?>, Object> f19003c = new ImmutableRangeMap<>(ImmutableList.E(), ImmutableList.E());
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    private final transient ImmutableList<Range<K>> f19004a;

    /* renamed from: b  reason: collision with root package name */
    private final transient ImmutableList<V> f19005b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a<K extends Comparable<?>, V> {

        /* renamed from: a  reason: collision with root package name */
        private final List<Map.Entry<Range<K>, V>> f19006a = j1.h();

        public ImmutableRangeMap<K, V> a() {
            Collections.sort(this.f19006a, Range.i().d());
            ImmutableList.a aVar = new ImmutableList.a(this.f19006a.size());
            ImmutableList.a aVar2 = new ImmutableList.a(this.f19006a.size());
            for (int i8 = 0; i8 < this.f19006a.size(); i8++) {
                Range<K> key = this.f19006a.get(i8).getKey();
                if (i8 > 0) {
                    Range<K> key2 = this.f19006a.get(i8 - 1).getKey();
                    if (key.g(key2) && !key.f(key2).h()) {
                        String valueOf = String.valueOf(key2);
                        String valueOf2 = String.valueOf(key);
                        StringBuilder sb = new StringBuilder(valueOf.length() + 47 + valueOf2.length());
                        sb.append("Overlapping ranges: range ");
                        sb.append(valueOf);
                        sb.append(" overlaps with entry ");
                        sb.append(valueOf2);
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
                aVar.a(key);
                aVar2.a(this.f19006a.get(i8).getValue());
            }
            return new ImmutableRangeMap<>(aVar.k(), aVar2.k());
        }

        public a<K, V> b(Range<K> range, V v8) {
            com.google.common.base.l.n(range);
            com.google.common.base.l.n(v8);
            com.google.common.base.l.i(!range.h(), "Range must not be empty, but was %s", range);
            this.f19006a.add(m1.f(range, v8));
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class b<K extends Comparable<?>, V> implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private final ImmutableMap<Range<K>, V> f19007a;

        b(ImmutableMap<Range<K>, V> immutableMap) {
            this.f19007a = immutableMap;
        }

        Object a() {
            a aVar = new a();
            d3<Map.Entry<Range<K>, V>> it = this.f19007a.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Range<K>, V> next = it.next();
                aVar.b(next.getKey(), next.getValue());
            }
            return aVar.a();
        }

        Object readResolve() {
            return this.f19007a.isEmpty() ? ImmutableRangeMap.c() : a();
        }
    }

    ImmutableRangeMap(ImmutableList<Range<K>> immutableList, ImmutableList<V> immutableList2) {
        this.f19004a = immutableList;
        this.f19005b = immutableList2;
    }

    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> c() {
        return (ImmutableRangeMap<K, V>) f19003c;
    }

    @Override // com.google.common.collect.b2
    /* renamed from: b */
    public ImmutableMap<Range<K>, V> a() {
        return this.f19004a.isEmpty() ? ImmutableMap.n() : new ImmutableSortedMap(new i2(this.f19004a, Range.i()), this.f19005b);
    }

    public boolean equals(Object obj) {
        if (obj instanceof b2) {
            return a().equals(((b2) obj).a());
        }
        return false;
    }

    public int hashCode() {
        return a().hashCode();
    }

    public String toString() {
        return a().toString();
    }

    Object writeReplace() {
        return new b(a());
    }
}
