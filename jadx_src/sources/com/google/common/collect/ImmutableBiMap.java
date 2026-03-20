package com.google.common.collect;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ImmutableBiMap<K, V> extends ImmutableMap<K, V> implements r<K, V> {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a<K, V> extends ImmutableMap.b<K, V> {
        a(int i8) {
            super(i8);
        }

        @Override // com.google.common.collect.ImmutableMap.b
        /* renamed from: l */
        public ImmutableBiMap<K, V> a() {
            return d();
        }

        @Override // com.google.common.collect.ImmutableMap.b
        @Deprecated
        /* renamed from: m */
        public ImmutableBiMap<K, V> c() {
            throw new UnsupportedOperationException("Not supported for bimaps");
        }

        @Override // com.google.common.collect.ImmutableMap.b
        /* renamed from: n */
        public ImmutableBiMap<K, V> d() {
            int i8 = this.f18968c;
            if (i8 == 0) {
                return ImmutableBiMap.s();
            }
            if (this.f18966a != null) {
                if (this.f18969d) {
                    this.f18967b = Arrays.copyOf(this.f18967b, i8 * 2);
                }
                ImmutableMap.b.k(this.f18967b, this.f18968c, this.f18966a);
            }
            this.f18969d = true;
            return new d2(this.f18967b, this.f18968c);
        }

        @Override // com.google.common.collect.ImmutableMap.b
        /* renamed from: o */
        public a<K, V> g(K k8, V v8) {
            super.g(k8, v8);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.b
        /* renamed from: p */
        public a<K, V> h(Map.Entry<? extends K, ? extends V> entry) {
            super.h(entry);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.b
        /* renamed from: q */
        public a<K, V> i(Iterable<? extends Map.Entry<? extends K, ? extends V>> iterable) {
            super.i(iterable);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.b
        /* renamed from: r */
        public a<K, V> j(Map<? extends K, ? extends V> map) {
            super.j(map);
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class b<K, V> extends ImmutableMap.d<K, V> {
        private static final long serialVersionUID = 0;

        b(ImmutableBiMap<K, V> immutableBiMap) {
            super(immutableBiMap);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableMap.d
        /* renamed from: c */
        public a<K, V> b(int i8) {
            return new a<>(i8);
        }
    }

    public static <K, V> ImmutableBiMap<K, V> s() {
        return d2.f19209k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableMap
    /* renamed from: q */
    public final ImmutableSet<V> h() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.r
    /* renamed from: r */
    public abstract ImmutableBiMap<V, K> g();

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    /* renamed from: t */
    public ImmutableSet<V> values() {
        return g().keySet();
    }

    @Override // com.google.common.collect.ImmutableMap
    Object writeReplace() {
        return new b(this);
    }
}
