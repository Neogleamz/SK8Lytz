package com.google.common.hash;

import com.google.common.base.k;
import com.google.common.base.l;
import com.google.common.base.m;
import java.io.Serializable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class BloomFilter<T> implements m<T>, Serializable {

    /* renamed from: a  reason: collision with root package name */
    private final com.google.common.hash.a f19548a;

    /* renamed from: b  reason: collision with root package name */
    private final int f19549b;

    /* renamed from: c  reason: collision with root package name */
    private final Funnel<? super T> f19550c;

    /* renamed from: d  reason: collision with root package name */
    private final c f19551d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class b<T> implements Serializable {
        private static final long serialVersionUID = 1;

        /* renamed from: a  reason: collision with root package name */
        final long[] f19552a;

        /* renamed from: b  reason: collision with root package name */
        final int f19553b;

        /* renamed from: c  reason: collision with root package name */
        final Funnel<? super T> f19554c;

        /* renamed from: d  reason: collision with root package name */
        final c f19555d;

        b(BloomFilter<T> bloomFilter) {
            this.f19552a = com.google.common.hash.a.a(((BloomFilter) bloomFilter).f19548a.f19556a);
            this.f19553b = ((BloomFilter) bloomFilter).f19549b;
            this.f19554c = ((BloomFilter) bloomFilter).f19550c;
            this.f19555d = ((BloomFilter) bloomFilter).f19551d;
        }

        Object readResolve() {
            return new BloomFilter(new com.google.common.hash.a(this.f19552a), this.f19553b, this.f19554c, this.f19555d);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c extends Serializable {
        <T> boolean C(T t8, Funnel<? super T> funnel, int i8, com.google.common.hash.a aVar);
    }

    private BloomFilter(com.google.common.hash.a aVar, int i8, Funnel<? super T> funnel, c cVar) {
        l.f(i8 > 0, "numHashFunctions (%s) must be > 0", i8);
        l.f(i8 <= 255, "numHashFunctions (%s) must be <= 255", i8);
        this.f19548a = (com.google.common.hash.a) l.n(aVar);
        this.f19549b = i8;
        this.f19550c = (Funnel) l.n(funnel);
        this.f19551d = (c) l.n(cVar);
    }

    private Object writeReplace() {
        return new b(this);
    }

    @Override // com.google.common.base.m
    @Deprecated
    public boolean apply(T t8) {
        return e(t8);
    }

    public boolean e(T t8) {
        return this.f19551d.C(t8, this.f19550c, this.f19549b, this.f19548a);
    }

    @Override // com.google.common.base.m
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof BloomFilter) {
            BloomFilter bloomFilter = (BloomFilter) obj;
            return this.f19549b == bloomFilter.f19549b && this.f19550c.equals(bloomFilter.f19550c) && this.f19548a.equals(bloomFilter.f19548a) && this.f19551d.equals(bloomFilter.f19551d);
        }
        return false;
    }

    public int hashCode() {
        return k.b(Integer.valueOf(this.f19549b), this.f19550c, this.f19551d, this.f19548a);
    }
}
