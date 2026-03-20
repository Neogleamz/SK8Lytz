package com.google.common.collect;

import com.google.common.collect.a3;
import com.google.common.collect.m1;
import com.google.common.collect.z2;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ArrayTable<R, C, V> extends o<R, C, V> implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: c  reason: collision with root package name */
    private final ImmutableList<R> f18866c;

    /* renamed from: d  reason: collision with root package name */
    private final ImmutableList<C> f18867d;

    /* renamed from: e  reason: collision with root package name */
    private final ImmutableMap<R, Integer> f18868e;

    /* renamed from: f  reason: collision with root package name */
    private final ImmutableMap<C, Integer> f18869f;

    /* renamed from: g  reason: collision with root package name */
    private final V[][] f18870g;

    /* renamed from: h  reason: collision with root package name */
    private transient ArrayTable<R, C, V>.f f18871h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends com.google.common.collect.b<z2.a<R, C, V>> {
        a(int i8) {
            super(i8);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.b
        /* renamed from: b */
        public z2.a<R, C, V> a(int i8) {
            return ArrayTable.this.s(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends a3.b<R, C, V> {

        /* renamed from: a  reason: collision with root package name */
        final int f18873a;

        /* renamed from: b  reason: collision with root package name */
        final int f18874b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ int f18875c;

        b(int i8) {
            this.f18875c = i8;
            this.f18873a = i8 / ArrayTable.this.f18867d.size();
            this.f18874b = i8 % ArrayTable.this.f18867d.size();
        }

        @Override // com.google.common.collect.z2.a
        public C a() {
            return (C) ArrayTable.this.f18867d.get(this.f18874b);
        }

        @Override // com.google.common.collect.z2.a
        public R b() {
            return (R) ArrayTable.this.f18866c.get(this.f18873a);
        }

        @Override // com.google.common.collect.z2.a
        public V getValue() {
            return (V) ArrayTable.this.r(this.f18873a, this.f18874b);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends com.google.common.collect.b<V> {
        c(int i8) {
            super(i8);
        }

        @Override // com.google.common.collect.b
        protected V a(int i8) {
            return (V) ArrayTable.this.t(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class d<K, V> extends m1.g<K, V> {

        /* renamed from: a  reason: collision with root package name */
        private final ImmutableMap<K, Integer> f18878a;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a extends g<K, V> {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ int f18879a;

            a(int i8) {
                this.f18879a = i8;
            }

            @Override // com.google.common.collect.g, java.util.Map.Entry
            public K getKey() {
                return (K) d.this.c(this.f18879a);
            }

            @Override // com.google.common.collect.g, java.util.Map.Entry
            public V getValue() {
                return (V) d.this.f(this.f18879a);
            }

            @Override // com.google.common.collect.g, java.util.Map.Entry
            public V setValue(V v8) {
                return (V) d.this.h(this.f18879a, v8);
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class b extends com.google.common.collect.b<Map.Entry<K, V>> {
            b(int i8) {
                super(i8);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.b
            /* renamed from: b */
            public Map.Entry<K, V> a(int i8) {
                return d.this.b(i8);
            }
        }

        private d(ImmutableMap<K, Integer> immutableMap) {
            this.f18878a = immutableMap;
        }

        /* synthetic */ d(ImmutableMap immutableMap, a aVar) {
            this(immutableMap);
        }

        @Override // com.google.common.collect.m1.g
        Iterator<Map.Entry<K, V>> a() {
            return new b(size());
        }

        Map.Entry<K, V> b(int i8) {
            com.google.common.base.l.l(i8, size());
            return new a(i8);
        }

        K c(int i8) {
            return this.f18878a.keySet().e().get(i8);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object obj) {
            return this.f18878a.containsKey(obj);
        }

        abstract String d();

        abstract V f(int i8);

        @Override // java.util.AbstractMap, java.util.Map
        public V get(Object obj) {
            Integer num = this.f18878a.get(obj);
            if (num == null) {
                return null;
            }
            return f(num.intValue());
        }

        abstract V h(int i8, V v8);

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            return this.f18878a.isEmpty();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return this.f18878a.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V put(K k8, V v8) {
            Integer num = this.f18878a.get(k8);
            if (num != null) {
                return h(num.intValue(), v8);
            }
            String d8 = d();
            String valueOf = String.valueOf(k8);
            String valueOf2 = String.valueOf(this.f18878a.keySet());
            StringBuilder sb = new StringBuilder(String.valueOf(d8).length() + 9 + valueOf.length() + valueOf2.length());
            sb.append(d8);
            sb.append(" ");
            sb.append(valueOf);
            sb.append(" not in ");
            sb.append(valueOf2);
            throw new IllegalArgumentException(sb.toString());
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.f18878a.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e extends d<C, V> {

        /* renamed from: b  reason: collision with root package name */
        final int f18882b;

        e(int i8) {
            super(ArrayTable.this.f18869f, null);
            this.f18882b = i8;
        }

        @Override // com.google.common.collect.ArrayTable.d
        String d() {
            return "Column";
        }

        @Override // com.google.common.collect.ArrayTable.d
        V f(int i8) {
            return (V) ArrayTable.this.r(this.f18882b, i8);
        }

        @Override // com.google.common.collect.ArrayTable.d
        V h(int i8, V v8) {
            return (V) ArrayTable.this.u(this.f18882b, i8, v8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class f extends d<R, Map<C, V>> {
        private f() {
            super(ArrayTable.this.f18868e, null);
        }

        /* synthetic */ f(ArrayTable arrayTable, a aVar) {
            this();
        }

        @Override // com.google.common.collect.ArrayTable.d
        String d() {
            return "Row";
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ArrayTable.d
        /* renamed from: i */
        public Map<C, V> f(int i8) {
            return new e(i8);
        }

        @Override // com.google.common.collect.ArrayTable.d, java.util.AbstractMap, java.util.Map
        /* renamed from: j */
        public Map<C, V> put(R r4, Map<C, V> map) {
            throw new UnsupportedOperationException();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ArrayTable.d
        /* renamed from: k */
        public Map<C, V> h(int i8, Map<C, V> map) {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public z2.a<R, C, V> s(int i8) {
        return new b(i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public V t(int i8) {
        return r(i8 / this.f18867d.size(), i8 % this.f18867d.size());
    }

    @Override // com.google.common.collect.o, com.google.common.collect.z2
    public Set<z2.a<R, C, V>> a() {
        return super.a();
    }

    @Override // com.google.common.collect.z2
    public Map<R, Map<C, V>> b() {
        ArrayTable<R, C, V>.f fVar = this.f18871h;
        if (fVar == null) {
            ArrayTable<R, C, V>.f fVar2 = new f(this, null);
            this.f18871h = fVar2;
            return fVar2;
        }
        return fVar;
    }

    @Override // com.google.common.collect.o
    Iterator<z2.a<R, C, V>> c() {
        return new a(size());
    }

    @Override // com.google.common.collect.o
    @Deprecated
    public void d() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.o
    public boolean e(Object obj) {
        V[][] vArr;
        for (V[] vArr2 : this.f18870g) {
            for (V v8 : vArr2) {
                if (com.google.common.base.k.a(obj, v8)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.google.common.collect.o
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.google.common.collect.o
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.o
    Iterator<V> k() {
        return new c(size());
    }

    public V r(int i8, int i9) {
        com.google.common.base.l.l(i8, this.f18866c.size());
        com.google.common.base.l.l(i9, this.f18867d.size());
        return this.f18870g[i8][i9];
    }

    @Override // com.google.common.collect.z2
    public int size() {
        return this.f18866c.size() * this.f18867d.size();
    }

    @Override // com.google.common.collect.o
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public V u(int i8, int i9, V v8) {
        com.google.common.base.l.l(i8, this.f18866c.size());
        com.google.common.base.l.l(i9, this.f18867d.size());
        V[][] vArr = this.f18870g;
        V v9 = vArr[i8][i9];
        vArr[i8][i9] = v8;
        return v9;
    }
}
