package com.google.common.collect;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class v<K, V> extends AbstractMap<K, V> implements Serializable {

    /* renamed from: k  reason: collision with root package name */
    private static final Object f19455k = new Object();

    /* renamed from: a  reason: collision with root package name */
    private transient Object f19456a;

    /* renamed from: b  reason: collision with root package name */
    transient int[] f19457b;

    /* renamed from: c  reason: collision with root package name */
    transient Object[] f19458c;

    /* renamed from: d  reason: collision with root package name */
    transient Object[] f19459d;

    /* renamed from: e  reason: collision with root package name */
    private transient int f19460e;

    /* renamed from: f  reason: collision with root package name */
    private transient int f19461f;

    /* renamed from: g  reason: collision with root package name */
    private transient Set<K> f19462g;

    /* renamed from: h  reason: collision with root package name */
    private transient Set<Map.Entry<K, V>> f19463h;

    /* renamed from: j  reason: collision with root package name */
    private transient Collection<V> f19464j;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends v<K, V>.e<K> {
        a() {
            super(v.this, null);
        }

        @Override // com.google.common.collect.v.e
        K b(int i8) {
            return (K) v.this.K(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends v<K, V>.e<Map.Entry<K, V>> {
        b() {
            super(v.this, null);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.v.e
        /* renamed from: d */
        public Map.Entry<K, V> b(int i8) {
            return new g(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends v<K, V>.e<V> {
        c() {
            super(v.this, null);
        }

        @Override // com.google.common.collect.v.e
        V b(int i8) {
            return (V) v.this.a0(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends AbstractSet<Map.Entry<K, V>> {
        d() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            v.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            Map<K, V> z4 = v.this.z();
            if (z4 != null) {
                return z4.entrySet().contains(obj);
            }
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                int H = v.this.H(entry.getKey());
                return H != -1 && com.google.common.base.k.a(v.this.a0(H), entry.getValue());
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return v.this.B();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Map<K, V> z4 = v.this.z();
            if (z4 != null) {
                return z4.entrySet().remove(obj);
            }
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                if (v.this.N()) {
                    return false;
                }
                int F = v.this.F();
                int f5 = x.f(entry.getKey(), entry.getValue(), F, v.this.R(), v.this.P(), v.this.Q(), v.this.S());
                if (f5 == -1) {
                    return false;
                }
                v.this.M(f5, F);
                v.f(v.this);
                v.this.G();
                return true;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return v.this.size();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private abstract class e<T> implements Iterator<T> {

        /* renamed from: a  reason: collision with root package name */
        int f19469a;

        /* renamed from: b  reason: collision with root package name */
        int f19470b;

        /* renamed from: c  reason: collision with root package name */
        int f19471c;

        private e() {
            this.f19469a = v.this.f19460e;
            this.f19470b = v.this.D();
            this.f19471c = -1;
        }

        /* synthetic */ e(v vVar, a aVar) {
            this();
        }

        private void a() {
            if (v.this.f19460e != this.f19469a) {
                throw new ConcurrentModificationException();
            }
        }

        abstract T b(int i8);

        void c() {
            this.f19469a += 32;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f19470b >= 0;
        }

        @Override // java.util.Iterator
        public T next() {
            a();
            if (hasNext()) {
                int i8 = this.f19470b;
                this.f19471c = i8;
                T b9 = b(i8);
                this.f19470b = v.this.E(this.f19470b);
                return b9;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            a();
            t.d(this.f19471c >= 0);
            c();
            v vVar = v.this;
            vVar.remove(vVar.K(this.f19471c));
            this.f19470b = v.this.q(this.f19470b, this.f19471c);
            this.f19471c = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f extends AbstractSet<K> {
        f() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            v.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return v.this.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return v.this.L();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Map<K, V> z4 = v.this.z();
            return z4 != null ? z4.keySet().remove(obj) : v.this.O(obj) != v.f19455k;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return v.this.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class g extends com.google.common.collect.g<K, V> {

        /* renamed from: a  reason: collision with root package name */
        private final K f19474a;

        /* renamed from: b  reason: collision with root package name */
        private int f19475b;

        g(int i8) {
            this.f19474a = (K) v.this.K(i8);
            this.f19475b = i8;
        }

        private void a() {
            int i8 = this.f19475b;
            if (i8 == -1 || i8 >= v.this.size() || !com.google.common.base.k.a(this.f19474a, v.this.K(this.f19475b))) {
                this.f19475b = v.this.H(this.f19474a);
            }
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public K getKey() {
            return this.f19474a;
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public V getValue() {
            Map<K, V> z4 = v.this.z();
            if (z4 != null) {
                return (V) u1.a(z4.get(this.f19474a));
            }
            a();
            int i8 = this.f19475b;
            return i8 == -1 ? (V) u1.b() : (V) v.this.a0(i8);
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public V setValue(V v8) {
            Map<K, V> z4 = v.this.z();
            if (z4 != null) {
                return (V) u1.a(z4.put(this.f19474a, v8));
            }
            a();
            int i8 = this.f19475b;
            if (i8 == -1) {
                v.this.put(this.f19474a, v8);
                return (V) u1.b();
            }
            V v9 = (V) v.this.a0(i8);
            v.this.Z(this.f19475b, v8);
            return v9;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h extends AbstractCollection<V> {
        h() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            v.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return v.this.b0();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return v.this.size();
        }
    }

    v() {
        I(3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public v(int i8) {
        I(i8);
    }

    private int A(int i8) {
        return P()[i8];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int F() {
        return (1 << (this.f19460e & 31)) - 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int H(Object obj) {
        if (N()) {
            return -1;
        }
        int d8 = v0.d(obj);
        int F = F();
        int h8 = x.h(R(), d8 & F);
        if (h8 == 0) {
            return -1;
        }
        int b9 = x.b(d8, F);
        do {
            int i8 = h8 - 1;
            int A = A(i8);
            if (x.b(A, F) == b9 && com.google.common.base.k.a(obj, K(i8))) {
                return i8;
            }
            h8 = x.c(A, F);
        } while (h8 != 0);
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public K K(int i8) {
        return (K) Q()[i8];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object O(Object obj) {
        if (N()) {
            return f19455k;
        }
        int F = F();
        int f5 = x.f(obj, null, F, R(), P(), Q(), null);
        if (f5 == -1) {
            return f19455k;
        }
        V a02 = a0(f5);
        M(f5, F);
        this.f19461f--;
        G();
        return a02;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] P() {
        int[] iArr = this.f19457b;
        Objects.requireNonNull(iArr);
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object[] Q() {
        Object[] objArr = this.f19458c;
        Objects.requireNonNull(objArr);
        return objArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object R() {
        Object obj = this.f19456a;
        Objects.requireNonNull(obj);
        return obj;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object[] S() {
        Object[] objArr = this.f19459d;
        Objects.requireNonNull(objArr);
        return objArr;
    }

    private void U(int i8) {
        int min;
        int length = P().length;
        if (i8 <= length || (min = Math.min(1073741823, (Math.max(1, length >>> 1) + length) | 1)) == length) {
            return;
        }
        T(min);
    }

    private int V(int i8, int i9, int i10, int i11) {
        Object a9 = x.a(i9);
        int i12 = i9 - 1;
        if (i11 != 0) {
            x.i(a9, i10 & i12, i11 + 1);
        }
        Object R = R();
        int[] P = P();
        for (int i13 = 0; i13 <= i8; i13++) {
            int h8 = x.h(R, i13);
            while (h8 != 0) {
                int i14 = h8 - 1;
                int i15 = P[i14];
                int b9 = x.b(i15, i8) | i13;
                int i16 = b9 & i12;
                int h9 = x.h(a9, i16);
                x.i(a9, i16, h8);
                P[i14] = x.d(b9, h9, i12);
                h8 = x.c(i15, i8);
            }
        }
        this.f19456a = a9;
        X(i12);
        return i12;
    }

    private void W(int i8, int i9) {
        P()[i8] = i9;
    }

    private void X(int i8) {
        this.f19460e = x.d(this.f19460e, 32 - Integer.numberOfLeadingZeros(i8), 31);
    }

    private void Y(int i8, K k8) {
        Q()[i8] = k8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void Z(int i8, V v8) {
        S()[i8] = v8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public V a0(int i8) {
        return (V) S()[i8];
    }

    static /* synthetic */ int f(v vVar) {
        int i8 = vVar.f19461f;
        vVar.f19461f = i8 - 1;
        return i8;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        if (readInt < 0) {
            StringBuilder sb = new StringBuilder(25);
            sb.append("Invalid size: ");
            sb.append(readInt);
            throw new InvalidObjectException(sb.toString());
        }
        I(readInt);
        for (int i8 = 0; i8 < readInt; i8++) {
            put(objectInputStream.readObject(), objectInputStream.readObject());
        }
    }

    public static <K, V> v<K, V> t() {
        return new v<>();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(size());
        Iterator<Map.Entry<K, V>> B = B();
        while (B.hasNext()) {
            Map.Entry<K, V> next = B.next();
            objectOutputStream.writeObject(next.getKey());
            objectOutputStream.writeObject(next.getValue());
        }
    }

    public static <K, V> v<K, V> y(int i8) {
        return new v<>(i8);
    }

    Iterator<Map.Entry<K, V>> B() {
        Map<K, V> z4 = z();
        return z4 != null ? z4.entrySet().iterator() : new b();
    }

    int D() {
        return isEmpty() ? -1 : 0;
    }

    int E(int i8) {
        int i9 = i8 + 1;
        if (i9 < this.f19461f) {
            return i9;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void G() {
        this.f19460e += 32;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void I(int i8) {
        com.google.common.base.l.e(i8 >= 0, "Expected size must be >= 0");
        this.f19460e = com.google.common.primitives.g.f(i8, 1, 1073741823);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J(int i8, K k8, V v8, int i9, int i10) {
        W(i8, x.d(i9, 0, i10));
        Y(i8, k8);
        Z(i8, v8);
    }

    Iterator<K> L() {
        Map<K, V> z4 = z();
        return z4 != null ? z4.keySet().iterator() : new a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void M(int i8, int i9) {
        Object R = R();
        int[] P = P();
        Object[] Q = Q();
        Object[] S = S();
        int size = size() - 1;
        if (i8 >= size) {
            Q[i8] = null;
            S[i8] = null;
            P[i8] = 0;
            return;
        }
        Object obj = Q[size];
        Q[i8] = obj;
        S[i8] = S[size];
        Q[size] = null;
        S[size] = null;
        P[i8] = P[size];
        P[size] = 0;
        int d8 = v0.d(obj) & i9;
        int h8 = x.h(R, d8);
        int i10 = size + 1;
        if (h8 == i10) {
            x.i(R, d8, i8 + 1);
            return;
        }
        while (true) {
            int i11 = h8 - 1;
            int i12 = P[i11];
            int c9 = x.c(i12, i9);
            if (c9 == i10) {
                P[i11] = x.d(i12, i8 + 1, i9);
                return;
            }
            h8 = c9;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean N() {
        return this.f19456a == null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void T(int i8) {
        this.f19457b = Arrays.copyOf(P(), i8);
        this.f19458c = Arrays.copyOf(Q(), i8);
        this.f19459d = Arrays.copyOf(S(), i8);
    }

    Iterator<V> b0() {
        Map<K, V> z4 = z();
        return z4 != null ? z4.values().iterator() : new c();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        if (N()) {
            return;
        }
        G();
        Map<K, V> z4 = z();
        if (z4 != null) {
            this.f19460e = com.google.common.primitives.g.f(size(), 3, 1073741823);
            z4.clear();
            this.f19456a = null;
        } else {
            Arrays.fill(Q(), 0, this.f19461f, (Object) null);
            Arrays.fill(S(), 0, this.f19461f, (Object) null);
            x.g(R());
            Arrays.fill(P(), 0, this.f19461f, 0);
        }
        this.f19461f = 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        Map<K, V> z4 = z();
        return z4 != null ? z4.containsKey(obj) : H(obj) != -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        Map<K, V> z4 = z();
        if (z4 != null) {
            return z4.containsValue(obj);
        }
        for (int i8 = 0; i8 < this.f19461f; i8++) {
            if (com.google.common.base.k.a(obj, a0(i8))) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.f19463h;
        if (set == null) {
            Set<Map.Entry<K, V>> u8 = u();
            this.f19463h = u8;
            return u8;
        }
        return set;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        Map<K, V> z4 = z();
        if (z4 != null) {
            return z4.get(obj);
        }
        int H = H(obj);
        if (H == -1) {
            return null;
        }
        p(H);
        return a0(H);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> set = this.f19462g;
        if (set == null) {
            Set<K> w8 = w();
            this.f19462g = w8;
            return w8;
        }
        return set;
    }

    void p(int i8) {
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k8, V v8) {
        int i8;
        if (N()) {
            r();
        }
        Map<K, V> z4 = z();
        if (z4 != null) {
            return z4.put(k8, v8);
        }
        int[] P = P();
        Object[] Q = Q();
        Object[] S = S();
        int i9 = this.f19461f;
        int i10 = i9 + 1;
        int d8 = v0.d(k8);
        int F = F();
        int i11 = d8 & F;
        int h8 = x.h(R(), i11);
        if (h8 == 0) {
            if (i10 <= F) {
                x.i(R(), i11, i10);
                i8 = F;
            }
            i8 = V(F, x.e(F), d8, i9);
        } else {
            int b9 = x.b(d8, F);
            int i12 = 0;
            while (true) {
                int i13 = h8 - 1;
                int i14 = P[i13];
                if (x.b(i14, F) == b9 && com.google.common.base.k.a(k8, Q[i13])) {
                    V v9 = (V) S[i13];
                    S[i13] = v8;
                    p(i13);
                    return v9;
                }
                int c9 = x.c(i14, F);
                i12++;
                if (c9 != 0) {
                    h8 = c9;
                } else if (i12 >= 9) {
                    return s().put(k8, v8);
                } else {
                    if (i10 <= F) {
                        P[i13] = x.d(i14, i10, F);
                    }
                }
            }
        }
        U(i10);
        J(i9, k8, v8, d8, i8);
        this.f19461f = i10;
        G();
        return null;
    }

    int q(int i8, int i9) {
        return i8 - 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int r() {
        com.google.common.base.l.t(N(), "Arrays already allocated");
        int i8 = this.f19460e;
        int j8 = x.j(i8);
        this.f19456a = x.a(j8);
        X(j8 - 1);
        this.f19457b = new int[i8];
        this.f19458c = new Object[i8];
        this.f19459d = new Object[i8];
        return i8;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        Map<K, V> z4 = z();
        if (z4 != null) {
            return z4.remove(obj);
        }
        V v8 = (V) O(obj);
        if (v8 == f19455k) {
            return null;
        }
        return v8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<K, V> s() {
        Map<K, V> v8 = v(F() + 1);
        int D = D();
        while (D >= 0) {
            v8.put(K(D), a0(D));
            D = E(D);
        }
        this.f19456a = v8;
        this.f19457b = null;
        this.f19458c = null;
        this.f19459d = null;
        G();
        return v8;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        Map<K, V> z4 = z();
        return z4 != null ? z4.size() : this.f19461f;
    }

    Set<Map.Entry<K, V>> u() {
        return new d();
    }

    Map<K, V> v(int i8) {
        return new LinkedHashMap(i8, 1.0f);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Collection<V> collection = this.f19464j;
        if (collection == null) {
            Collection<V> x8 = x();
            this.f19464j = x8;
            return x8;
        }
        return collection;
    }

    Set<K> w() {
        return new f();
    }

    Collection<V> x() {
        return new h();
    }

    Map<K, V> z() {
        Object obj = this.f19456a;
        if (obj instanceof Map) {
            return (Map) obj;
        }
        return null;
    }
}
