package com.google.common.collect;

import com.google.common.collect.ImmutableCollection;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class HashBiMap<K, V> extends AbstractMap<K, V> implements r<K, V>, Serializable {

    /* renamed from: a  reason: collision with root package name */
    transient K[] f18915a;

    /* renamed from: b  reason: collision with root package name */
    transient V[] f18916b;

    /* renamed from: c  reason: collision with root package name */
    transient int f18917c;

    /* renamed from: d  reason: collision with root package name */
    transient int f18918d;

    /* renamed from: e  reason: collision with root package name */
    private transient int[] f18919e;

    /* renamed from: f  reason: collision with root package name */
    private transient int[] f18920f;

    /* renamed from: g  reason: collision with root package name */
    private transient int[] f18921g;

    /* renamed from: h  reason: collision with root package name */
    private transient int[] f18922h;

    /* renamed from: j  reason: collision with root package name */
    private transient int f18923j;

    /* renamed from: k  reason: collision with root package name */
    private transient int f18924k;

    /* renamed from: l  reason: collision with root package name */
    private transient int[] f18925l;

    /* renamed from: m  reason: collision with root package name */
    private transient int[] f18926m;

    /* renamed from: n  reason: collision with root package name */
    private transient Set<K> f18927n;

    /* renamed from: p  reason: collision with root package name */
    private transient Set<V> f18928p;
    private transient Set<Map.Entry<K, V>> q;

    /* renamed from: t  reason: collision with root package name */
    private transient r<V, K> f18929t;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class a extends com.google.common.collect.g<K, V> {

        /* renamed from: a  reason: collision with root package name */
        final K f18930a;

        /* renamed from: b  reason: collision with root package name */
        int f18931b;

        a(int i8) {
            this.f18930a = (K) u1.a(HashBiMap.this.f18915a[i8]);
            this.f18931b = i8;
        }

        void a() {
            int i8 = this.f18931b;
            if (i8 != -1) {
                HashBiMap hashBiMap = HashBiMap.this;
                if (i8 <= hashBiMap.f18917c && com.google.common.base.k.a(hashBiMap.f18915a[i8], this.f18930a)) {
                    return;
                }
            }
            this.f18931b = HashBiMap.this.o(this.f18930a);
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public K getKey() {
            return this.f18930a;
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public V getValue() {
            a();
            int i8 = this.f18931b;
            return i8 == -1 ? (V) u1.b() : (V) u1.a(HashBiMap.this.f18916b[i8]);
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public V setValue(V v8) {
            a();
            int i8 = this.f18931b;
            if (i8 == -1) {
                HashBiMap.this.put(this.f18930a, v8);
                return (V) u1.b();
            }
            V v9 = (V) u1.a(HashBiMap.this.f18916b[i8]);
            if (com.google.common.base.k.a(v9, v8)) {
                return v8;
            }
            HashBiMap.this.G(this.f18931b, v8, false);
            return v9;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b<K, V> extends com.google.common.collect.g<V, K> {

        /* renamed from: a  reason: collision with root package name */
        final HashBiMap<K, V> f18933a;

        /* renamed from: b  reason: collision with root package name */
        final V f18934b;

        /* renamed from: c  reason: collision with root package name */
        int f18935c;

        b(HashBiMap<K, V> hashBiMap, int i8) {
            this.f18933a = hashBiMap;
            this.f18934b = (V) u1.a(hashBiMap.f18916b[i8]);
            this.f18935c = i8;
        }

        private void a() {
            int i8 = this.f18935c;
            if (i8 != -1) {
                HashBiMap<K, V> hashBiMap = this.f18933a;
                if (i8 <= hashBiMap.f18917c && com.google.common.base.k.a(this.f18934b, hashBiMap.f18916b[i8])) {
                    return;
                }
            }
            this.f18935c = this.f18933a.q(this.f18934b);
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public V getKey() {
            return this.f18934b;
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public K getValue() {
            a();
            int i8 = this.f18935c;
            return i8 == -1 ? (K) u1.b() : (K) u1.a(this.f18933a.f18915a[i8]);
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public K setValue(K k8) {
            a();
            int i8 = this.f18935c;
            if (i8 == -1) {
                this.f18933a.y(this.f18934b, k8, false);
                return (K) u1.b();
            }
            K k9 = (K) u1.a(this.f18933a.f18915a[i8]);
            if (com.google.common.base.k.a(k9, k8)) {
                return k8;
            }
            this.f18933a.F(this.f18935c, k8, false);
            return k9;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class c extends h<K, V, Map.Entry<K, V>> {
        c() {
            super(HashBiMap.this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                Object value = entry.getValue();
                int o5 = HashBiMap.this.o(key);
                return o5 != -1 && com.google.common.base.k.a(value, HashBiMap.this.f18916b[o5]);
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.HashBiMap.h
        /* renamed from: g */
        public Map.Entry<K, V> e(int i8) {
            return new a(i8);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                Object value = entry.getValue();
                int d8 = v0.d(key);
                int p8 = HashBiMap.this.p(key, d8);
                if (p8 == -1 || !com.google.common.base.k.a(value, HashBiMap.this.f18916b[p8])) {
                    return false;
                }
                HashBiMap.this.B(p8, d8);
                return true;
            }
            return false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class d<K, V> extends AbstractMap<V, K> implements r<V, K>, Serializable {

        /* renamed from: a  reason: collision with root package name */
        private final HashBiMap<K, V> f18937a;

        /* renamed from: b  reason: collision with root package name */
        private transient Set<Map.Entry<V, K>> f18938b;

        d(HashBiMap<K, V> hashBiMap) {
            this.f18937a = hashBiMap;
        }

        private void readObject(ObjectInputStream objectInputStream) {
            objectInputStream.defaultReadObject();
            ((HashBiMap) this.f18937a).f18929t = this;
        }

        @Override // java.util.AbstractMap, java.util.Map
        /* renamed from: a */
        public Set<K> values() {
            return this.f18937a.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            this.f18937a.clear();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object obj) {
            return this.f18937a.containsValue(obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(Object obj) {
            return this.f18937a.containsKey(obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<V, K>> entrySet() {
            Set<Map.Entry<V, K>> set = this.f18938b;
            if (set == null) {
                e eVar = new e(this.f18937a);
                this.f18938b = eVar;
                return eVar;
            }
            return set;
        }

        @Override // com.google.common.collect.r
        public r<K, V> g() {
            return this.f18937a;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public K get(Object obj) {
            return this.f18937a.s(obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<V> keySet() {
            return this.f18937a.values();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public K put(V v8, K k8) {
            return this.f18937a.y(v8, k8, false);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public K remove(Object obj) {
            return this.f18937a.E(obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.f18937a.f18917c;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class e<K, V> extends h<K, V, Map.Entry<V, K>> {
        e(HashBiMap<K, V> hashBiMap) {
            super(hashBiMap);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                Object value = entry.getValue();
                int q = this.f18941a.q(key);
                return q != -1 && com.google.common.base.k.a(this.f18941a.f18915a[q], value);
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.HashBiMap.h
        /* renamed from: g */
        public Map.Entry<V, K> e(int i8) {
            return new b(this.f18941a, i8);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                Object value = entry.getValue();
                int d8 = v0.d(key);
                int r4 = this.f18941a.r(key, d8);
                if (r4 == -1 || !com.google.common.base.k.a(this.f18941a.f18915a[r4], value)) {
                    return false;
                }
                this.f18941a.D(r4, d8);
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class f extends h<K, V, K> {
        f() {
            super(HashBiMap.this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return HashBiMap.this.containsKey(obj);
        }

        @Override // com.google.common.collect.HashBiMap.h
        K e(int i8) {
            return (K) u1.a(HashBiMap.this.f18915a[i8]);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            int d8 = v0.d(obj);
            int p8 = HashBiMap.this.p(obj, d8);
            if (p8 != -1) {
                HashBiMap.this.B(p8, d8);
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class g extends h<K, V, V> {
        g() {
            super(HashBiMap.this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return HashBiMap.this.containsValue(obj);
        }

        @Override // com.google.common.collect.HashBiMap.h
        V e(int i8) {
            return (V) u1.a(HashBiMap.this.f18916b[i8]);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            int d8 = v0.d(obj);
            int r4 = HashBiMap.this.r(obj, d8);
            if (r4 != -1) {
                HashBiMap.this.D(r4, d8);
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class h<K, V, T> extends AbstractSet<T> {

        /* renamed from: a  reason: collision with root package name */
        final HashBiMap<K, V> f18941a;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Iterator<T> {

            /* renamed from: a  reason: collision with root package name */
            private int f18942a;

            /* renamed from: b  reason: collision with root package name */
            private int f18943b = -1;

            /* renamed from: c  reason: collision with root package name */
            private int f18944c;

            /* renamed from: d  reason: collision with root package name */
            private int f18945d;

            a() {
                this.f18942a = ((HashBiMap) h.this.f18941a).f18923j;
                HashBiMap<K, V> hashBiMap = h.this.f18941a;
                this.f18944c = hashBiMap.f18918d;
                this.f18945d = hashBiMap.f18917c;
            }

            private void a() {
                if (h.this.f18941a.f18918d != this.f18944c) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                a();
                return this.f18942a != -2 && this.f18945d > 0;
            }

            @Override // java.util.Iterator
            public T next() {
                if (hasNext()) {
                    T t8 = (T) h.this.e(this.f18942a);
                    this.f18943b = this.f18942a;
                    this.f18942a = ((HashBiMap) h.this.f18941a).f18926m[this.f18942a];
                    this.f18945d--;
                    return t8;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                a();
                t.d(this.f18943b != -1);
                h.this.f18941a.z(this.f18943b);
                int i8 = this.f18942a;
                HashBiMap<K, V> hashBiMap = h.this.f18941a;
                if (i8 == hashBiMap.f18917c) {
                    this.f18942a = this.f18943b;
                }
                this.f18943b = -1;
                this.f18944c = hashBiMap.f18918d;
            }
        }

        h(HashBiMap<K, V> hashBiMap) {
            this.f18941a = hashBiMap;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.f18941a.clear();
        }

        abstract T e(int i8);

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<T> iterator() {
            return new a();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f18941a.f18917c;
        }
    }

    private void A(int i8, int i9, int i10) {
        com.google.common.base.l.d(i8 != -1);
        j(i8, i9);
        k(i8, i10);
        H(this.f18925l[i8], this.f18926m[i8]);
        w(this.f18917c - 1, i8);
        K[] kArr = this.f18915a;
        int i11 = this.f18917c;
        kArr[i11 - 1] = null;
        this.f18916b[i11 - 1] = null;
        this.f18917c = i11 - 1;
        this.f18918d++;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F(int i8, K k8, boolean z4) {
        com.google.common.base.l.d(i8 != -1);
        int d8 = v0.d(k8);
        int p8 = p(k8, d8);
        int i9 = this.f18924k;
        int i10 = -2;
        if (p8 != -1) {
            if (!z4) {
                String valueOf = String.valueOf(k8);
                StringBuilder sb = new StringBuilder(valueOf.length() + 28);
                sb.append("Key already present in map: ");
                sb.append(valueOf);
                throw new IllegalArgumentException(sb.toString());
            }
            i9 = this.f18925l[p8];
            i10 = this.f18926m[p8];
            B(p8, d8);
            if (i8 == this.f18917c) {
                i8 = p8;
            }
        }
        if (i9 == i8) {
            i9 = this.f18925l[i8];
        } else if (i9 == this.f18917c) {
            i9 = p8;
        }
        if (i10 == i8) {
            p8 = this.f18926m[i8];
        } else if (i10 != this.f18917c) {
            p8 = i10;
        }
        H(this.f18925l[i8], this.f18926m[i8]);
        j(i8, v0.d(this.f18915a[i8]));
        this.f18915a[i8] = k8;
        u(i8, v0.d(k8));
        H(i9, i8);
        H(i8, p8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void G(int i8, V v8, boolean z4) {
        com.google.common.base.l.d(i8 != -1);
        int d8 = v0.d(v8);
        int r4 = r(v8, d8);
        if (r4 != -1) {
            if (!z4) {
                String valueOf = String.valueOf(v8);
                StringBuilder sb = new StringBuilder(valueOf.length() + 30);
                sb.append("Value already present in map: ");
                sb.append(valueOf);
                throw new IllegalArgumentException(sb.toString());
            }
            D(r4, d8);
            if (i8 == this.f18917c) {
                i8 = r4;
            }
        }
        k(i8, v0.d(this.f18916b[i8]));
        this.f18916b[i8] = v8;
        v(i8, d8);
    }

    private void H(int i8, int i9) {
        if (i8 == -2) {
            this.f18923j = i9;
        } else {
            this.f18926m[i8] = i9;
        }
        if (i9 == -2) {
            this.f18924k = i8;
        } else {
            this.f18925l[i9] = i8;
        }
    }

    private int h(int i8) {
        return i8 & (this.f18919e.length - 1);
    }

    private static int[] i(int i8) {
        int[] iArr = new int[i8];
        Arrays.fill(iArr, -1);
        return iArr;
    }

    private void j(int i8, int i9) {
        com.google.common.base.l.d(i8 != -1);
        int h8 = h(i9);
        int[] iArr = this.f18919e;
        if (iArr[h8] == i8) {
            int[] iArr2 = this.f18921g;
            iArr[h8] = iArr2[i8];
            iArr2[i8] = -1;
            return;
        }
        int i10 = iArr[h8];
        int i11 = this.f18921g[i10];
        while (true) {
            int i12 = i11;
            int i13 = i10;
            i10 = i12;
            if (i10 == -1) {
                String valueOf = String.valueOf(this.f18915a[i8]);
                StringBuilder sb = new StringBuilder(valueOf.length() + 32);
                sb.append("Expected to find entry with key ");
                sb.append(valueOf);
                throw new AssertionError(sb.toString());
            } else if (i10 == i8) {
                int[] iArr3 = this.f18921g;
                iArr3[i13] = iArr3[i8];
                iArr3[i8] = -1;
                return;
            } else {
                i11 = this.f18921g[i10];
            }
        }
    }

    private void k(int i8, int i9) {
        com.google.common.base.l.d(i8 != -1);
        int h8 = h(i9);
        int[] iArr = this.f18920f;
        if (iArr[h8] == i8) {
            int[] iArr2 = this.f18922h;
            iArr[h8] = iArr2[i8];
            iArr2[i8] = -1;
            return;
        }
        int i10 = iArr[h8];
        int i11 = this.f18922h[i10];
        while (true) {
            int i12 = i11;
            int i13 = i10;
            i10 = i12;
            if (i10 == -1) {
                String valueOf = String.valueOf(this.f18916b[i8]);
                StringBuilder sb = new StringBuilder(valueOf.length() + 34);
                sb.append("Expected to find entry with value ");
                sb.append(valueOf);
                throw new AssertionError(sb.toString());
            } else if (i10 == i8) {
                int[] iArr3 = this.f18922h;
                iArr3[i13] = iArr3[i8];
                iArr3[i8] = -1;
                return;
            } else {
                i11 = this.f18922h[i10];
            }
        }
    }

    private void l(int i8) {
        int[] iArr = this.f18921g;
        if (iArr.length < i8) {
            int c9 = ImmutableCollection.b.c(iArr.length, i8);
            this.f18915a = (K[]) Arrays.copyOf(this.f18915a, c9);
            this.f18916b = (V[]) Arrays.copyOf(this.f18916b, c9);
            this.f18921g = m(this.f18921g, c9);
            this.f18922h = m(this.f18922h, c9);
            this.f18925l = m(this.f18925l, c9);
            this.f18926m = m(this.f18926m, c9);
        }
        if (this.f18919e.length < i8) {
            int a9 = v0.a(i8, 1.0d);
            this.f18919e = i(a9);
            this.f18920f = i(a9);
            for (int i9 = 0; i9 < this.f18917c; i9++) {
                int h8 = h(v0.d(this.f18915a[i9]));
                int[] iArr2 = this.f18921g;
                int[] iArr3 = this.f18919e;
                iArr2[i9] = iArr3[h8];
                iArr3[h8] = i9;
                int h9 = h(v0.d(this.f18916b[i9]));
                int[] iArr4 = this.f18922h;
                int[] iArr5 = this.f18920f;
                iArr4[i9] = iArr5[h9];
                iArr5[h9] = i9;
            }
        }
    }

    private static int[] m(int[] iArr, int i8) {
        int length = iArr.length;
        int[] copyOf = Arrays.copyOf(iArr, i8);
        Arrays.fill(copyOf, length, i8, -1);
        return copyOf;
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        int h8 = m2.h(objectInputStream);
        t(16);
        m2.c(this, objectInputStream, h8);
    }

    private void u(int i8, int i9) {
        com.google.common.base.l.d(i8 != -1);
        int h8 = h(i9);
        int[] iArr = this.f18921g;
        int[] iArr2 = this.f18919e;
        iArr[i8] = iArr2[h8];
        iArr2[h8] = i8;
    }

    private void v(int i8, int i9) {
        com.google.common.base.l.d(i8 != -1);
        int h8 = h(i9);
        int[] iArr = this.f18922h;
        int[] iArr2 = this.f18920f;
        iArr[i8] = iArr2[h8];
        iArr2[h8] = i8;
    }

    private void w(int i8, int i9) {
        int i10;
        int i11;
        if (i8 == i9) {
            return;
        }
        int i12 = this.f18925l[i8];
        int i13 = this.f18926m[i8];
        H(i12, i9);
        H(i9, i13);
        K[] kArr = this.f18915a;
        K k8 = kArr[i8];
        V[] vArr = this.f18916b;
        V v8 = vArr[i8];
        kArr[i9] = k8;
        vArr[i9] = v8;
        int h8 = h(v0.d(k8));
        int[] iArr = this.f18919e;
        if (iArr[h8] == i8) {
            iArr[h8] = i9;
        } else {
            int i14 = iArr[h8];
            int i15 = this.f18921g[i14];
            while (true) {
                int i16 = i15;
                i10 = i14;
                i14 = i16;
                if (i14 == i8) {
                    break;
                }
                i15 = this.f18921g[i14];
            }
            this.f18921g[i10] = i9;
        }
        int[] iArr2 = this.f18921g;
        iArr2[i9] = iArr2[i8];
        iArr2[i8] = -1;
        int h9 = h(v0.d(v8));
        int[] iArr3 = this.f18920f;
        if (iArr3[h9] == i8) {
            iArr3[h9] = i9;
        } else {
            int i17 = iArr3[h9];
            int i18 = this.f18922h[i17];
            while (true) {
                int i19 = i18;
                i11 = i17;
                i17 = i19;
                if (i17 == i8) {
                    break;
                }
                i18 = this.f18922h[i17];
            }
            this.f18922h[i11] = i9;
        }
        int[] iArr4 = this.f18922h;
        iArr4[i9] = iArr4[i8];
        iArr4[i8] = -1;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        m2.i(this, objectOutputStream);
    }

    void B(int i8, int i9) {
        A(i8, i9, v0.d(this.f18916b[i8]));
    }

    void D(int i8, int i9) {
        A(i8, v0.d(this.f18915a[i8]), i9);
    }

    K E(Object obj) {
        int d8 = v0.d(obj);
        int r4 = r(obj, d8);
        if (r4 == -1) {
            return null;
        }
        K k8 = this.f18915a[r4];
        D(r4, d8);
        return k8;
    }

    @Override // java.util.AbstractMap, java.util.Map
    /* renamed from: I */
    public Set<V> values() {
        Set<V> set = this.f18928p;
        if (set == null) {
            g gVar = new g();
            this.f18928p = gVar;
            return gVar;
        }
        return set;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        Arrays.fill(this.f18915a, 0, this.f18917c, (Object) null);
        Arrays.fill(this.f18916b, 0, this.f18917c, (Object) null);
        Arrays.fill(this.f18919e, -1);
        Arrays.fill(this.f18920f, -1);
        Arrays.fill(this.f18921g, 0, this.f18917c, -1);
        Arrays.fill(this.f18922h, 0, this.f18917c, -1);
        Arrays.fill(this.f18925l, 0, this.f18917c, -1);
        Arrays.fill(this.f18926m, 0, this.f18917c, -1);
        this.f18917c = 0;
        this.f18923j = -2;
        this.f18924k = -2;
        this.f18918d++;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return o(obj) != -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        return q(obj) != -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.q;
        if (set == null) {
            c cVar = new c();
            this.q = cVar;
            return cVar;
        }
        return set;
    }

    @Override // com.google.common.collect.r
    public r<V, K> g() {
        r<V, K> rVar = this.f18929t;
        if (rVar == null) {
            d dVar = new d(this);
            this.f18929t = dVar;
            return dVar;
        }
        return rVar;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        int o5 = o(obj);
        if (o5 == -1) {
            return null;
        }
        return this.f18916b[o5];
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> set = this.f18927n;
        if (set == null) {
            f fVar = new f();
            this.f18927n = fVar;
            return fVar;
        }
        return set;
    }

    int n(Object obj, int i8, int[] iArr, int[] iArr2, Object[] objArr) {
        int i9 = iArr[h(i8)];
        while (i9 != -1) {
            if (com.google.common.base.k.a(objArr[i9], obj)) {
                return i9;
            }
            i9 = iArr2[i9];
        }
        return -1;
    }

    int o(Object obj) {
        return p(obj, v0.d(obj));
    }

    int p(Object obj, int i8) {
        return n(obj, i8, this.f18919e, this.f18921g, this.f18915a);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k8, V v8) {
        return x(k8, v8, false);
    }

    int q(Object obj) {
        return r(obj, v0.d(obj));
    }

    int r(Object obj, int i8) {
        return n(obj, i8, this.f18920f, this.f18922h, this.f18916b);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        int d8 = v0.d(obj);
        int p8 = p(obj, d8);
        if (p8 == -1) {
            return null;
        }
        V v8 = this.f18916b[p8];
        B(p8, d8);
        return v8;
    }

    K s(Object obj) {
        int q = q(obj);
        if (q == -1) {
            return null;
        }
        return this.f18915a[q];
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.f18917c;
    }

    void t(int i8) {
        t.b(i8, "expectedSize");
        int a9 = v0.a(i8, 1.0d);
        this.f18917c = 0;
        this.f18915a = (K[]) new Object[i8];
        this.f18916b = (V[]) new Object[i8];
        this.f18919e = i(a9);
        this.f18920f = i(a9);
        this.f18921g = i(i8);
        this.f18922h = i(i8);
        this.f18923j = -2;
        this.f18924k = -2;
        this.f18925l = i(i8);
        this.f18926m = i(i8);
    }

    V x(K k8, V v8, boolean z4) {
        int d8 = v0.d(k8);
        int p8 = p(k8, d8);
        if (p8 != -1) {
            V v9 = this.f18916b[p8];
            if (com.google.common.base.k.a(v9, v8)) {
                return v8;
            }
            G(p8, v8, z4);
            return v9;
        }
        int d9 = v0.d(v8);
        int r4 = r(v8, d9);
        if (!z4) {
            com.google.common.base.l.i(r4 == -1, "Value already present: %s", v8);
        } else if (r4 != -1) {
            D(r4, d9);
        }
        l(this.f18917c + 1);
        K[] kArr = this.f18915a;
        int i8 = this.f18917c;
        kArr[i8] = k8;
        this.f18916b[i8] = v8;
        u(i8, d8);
        v(this.f18917c, d9);
        H(this.f18924k, this.f18917c);
        H(this.f18917c, -2);
        this.f18917c++;
        this.f18918d++;
        return null;
    }

    K y(V v8, K k8, boolean z4) {
        int d8 = v0.d(v8);
        int r4 = r(v8, d8);
        if (r4 != -1) {
            K k9 = this.f18915a[r4];
            if (com.google.common.base.k.a(k9, k8)) {
                return k8;
            }
            F(r4, k8, z4);
            return k9;
        }
        int i8 = this.f18924k;
        int d9 = v0.d(k8);
        int p8 = p(k8, d9);
        if (!z4) {
            com.google.common.base.l.i(p8 == -1, "Key already present: %s", k8);
        } else if (p8 != -1) {
            i8 = this.f18925l[p8];
            B(p8, d9);
        }
        l(this.f18917c + 1);
        K[] kArr = this.f18915a;
        int i9 = this.f18917c;
        kArr[i9] = k8;
        this.f18916b[i9] = v8;
        u(i9, d9);
        v(this.f18917c, d8);
        int i10 = i8 == -2 ? this.f18923j : this.f18926m[i8];
        H(i8, this.f18917c);
        H(this.f18917c, i10);
        this.f18917c++;
        this.f18918d++;
        return null;
    }

    void z(int i8) {
        B(i8, v0.d(this.f18915a[i8]));
    }
}
