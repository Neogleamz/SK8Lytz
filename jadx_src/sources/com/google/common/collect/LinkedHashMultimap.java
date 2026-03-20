package com.google.common.collect;

import com.google.common.collect.p2;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class LinkedHashMultimap<K, V> extends h1<K, V> {
    private static final long serialVersionUID = 1;

    /* renamed from: g  reason: collision with root package name */
    transient int f19050g;

    /* renamed from: h  reason: collision with root package name */
    private transient b<K, V> f19051h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Iterator<Map.Entry<K, V>> {

        /* renamed from: a  reason: collision with root package name */
        b<K, V> f19052a;

        /* renamed from: b  reason: collision with root package name */
        b<K, V> f19053b;

        a() {
            this.f19052a = LinkedHashMultimap.this.f19051h.b();
        }

        @Override // java.util.Iterator
        /* renamed from: a */
        public Map.Entry<K, V> next() {
            if (hasNext()) {
                b<K, V> bVar = this.f19052a;
                this.f19053b = bVar;
                this.f19052a = bVar.b();
                return bVar;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f19052a != LinkedHashMultimap.this.f19051h;
        }

        @Override // java.util.Iterator
        public void remove() {
            com.google.common.base.l.t(this.f19053b != null, "no calls to next() since the last call to remove()");
            LinkedHashMultimap.this.remove(this.f19053b.getKey(), this.f19053b.getValue());
            this.f19053b = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b<K, V> extends w0<K, V> implements d<K, V> {

        /* renamed from: c  reason: collision with root package name */
        final int f19055c;

        /* renamed from: d  reason: collision with root package name */
        b<K, V> f19056d;

        /* renamed from: e  reason: collision with root package name */
        d<K, V> f19057e;

        /* renamed from: f  reason: collision with root package name */
        d<K, V> f19058f;

        /* renamed from: g  reason: collision with root package name */
        b<K, V> f19059g;

        /* renamed from: h  reason: collision with root package name */
        b<K, V> f19060h;

        b(K k8, V v8, int i8, b<K, V> bVar) {
            super(k8, v8);
            this.f19055c = i8;
            this.f19056d = bVar;
        }

        static <K, V> b<K, V> d() {
            return new b<>(null, null, 0, null);
        }

        public b<K, V> a() {
            b<K, V> bVar = this.f19059g;
            Objects.requireNonNull(bVar);
            return bVar;
        }

        public b<K, V> b() {
            b<K, V> bVar = this.f19060h;
            Objects.requireNonNull(bVar);
            return bVar;
        }

        boolean c(Object obj, int i8) {
            return this.f19055c == i8 && com.google.common.base.k.a(getValue(), obj);
        }

        @Override // com.google.common.collect.LinkedHashMultimap.d
        public void e(d<K, V> dVar) {
            this.f19058f = dVar;
        }

        public void f(b<K, V> bVar) {
            this.f19059g = bVar;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.d
        public d<K, V> g() {
            d<K, V> dVar = this.f19057e;
            Objects.requireNonNull(dVar);
            return dVar;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.d
        public d<K, V> h() {
            d<K, V> dVar = this.f19058f;
            Objects.requireNonNull(dVar);
            return dVar;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.d
        public void i(d<K, V> dVar) {
            this.f19057e = dVar;
        }

        public void j(b<K, V> bVar) {
            this.f19060h = bVar;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class c extends p2.d<V> implements d<K, V> {

        /* renamed from: a  reason: collision with root package name */
        private final K f19061a;

        /* renamed from: b  reason: collision with root package name */
        b<K, V>[] f19062b;

        /* renamed from: c  reason: collision with root package name */
        private int f19063c = 0;

        /* renamed from: d  reason: collision with root package name */
        private int f19064d = 0;

        /* renamed from: e  reason: collision with root package name */
        private d<K, V> f19065e = this;

        /* renamed from: f  reason: collision with root package name */
        private d<K, V> f19066f = this;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Iterator<V> {

            /* renamed from: a  reason: collision with root package name */
            d<K, V> f19068a;

            /* renamed from: b  reason: collision with root package name */
            b<K, V> f19069b;

            /* renamed from: c  reason: collision with root package name */
            int f19070c;

            a() {
                this.f19068a = c.this.f19065e;
                this.f19070c = c.this.f19064d;
            }

            private void a() {
                if (c.this.f19064d != this.f19070c) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                a();
                return this.f19068a != c.this;
            }

            @Override // java.util.Iterator
            public V next() {
                if (hasNext()) {
                    b<K, V> bVar = (b) this.f19068a;
                    V value = bVar.getValue();
                    this.f19069b = bVar;
                    this.f19068a = bVar.h();
                    return value;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                a();
                com.google.common.base.l.t(this.f19069b != null, "no calls to next() since the last call to remove()");
                c.this.remove(this.f19069b.getValue());
                this.f19070c = c.this.f19064d;
                this.f19069b = null;
            }
        }

        c(K k8, int i8) {
            this.f19061a = k8;
            this.f19062b = new b[v0.a(i8, 1.0d)];
        }

        private int p() {
            return this.f19062b.length - 1;
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void q() {
            if (v0.b(this.f19063c, this.f19062b.length, 1.0d)) {
                int length = this.f19062b.length * 2;
                b<K, V>[] bVarArr = new b[length];
                this.f19062b = bVarArr;
                int i8 = length - 1;
                for (d dVar = (d<K, V>) this.f19065e; dVar != this; dVar = (d<K, V>) dVar.h()) {
                    b<K, V> bVar = (b) dVar;
                    int i9 = bVar.f19055c & i8;
                    bVar.f19056d = bVarArr[i9];
                    bVarArr[i9] = bVar;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(V v8) {
            int d8 = v0.d(v8);
            int p8 = p() & d8;
            b<K, V> bVar = this.f19062b[p8];
            for (b<K, V> bVar2 = bVar; bVar2 != null; bVar2 = bVar2.f19056d) {
                if (bVar2.c(v8, d8)) {
                    return false;
                }
            }
            b<K, V> bVar3 = new b<>(this.f19061a, v8, d8, bVar);
            LinkedHashMultimap.S(this.f19066f, bVar3);
            LinkedHashMultimap.S(bVar3, this);
            LinkedHashMultimap.R(LinkedHashMultimap.this.f19051h.a(), bVar3);
            LinkedHashMultimap.R(bVar3, LinkedHashMultimap.this.f19051h);
            this.f19062b[p8] = bVar3;
            this.f19063c++;
            this.f19064d++;
            q();
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Arrays.fill(this.f19062b, (Object) null);
            this.f19063c = 0;
            for (d<K, V> dVar = this.f19065e; dVar != this; dVar = dVar.h()) {
                LinkedHashMultimap.P((b) dVar);
            }
            LinkedHashMultimap.S(this, this);
            this.f19064d++;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            int d8 = v0.d(obj);
            for (b<K, V> bVar = this.f19062b[p() & d8]; bVar != null; bVar = bVar.f19056d) {
                if (bVar.c(obj, d8)) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.d
        public void e(d<K, V> dVar) {
            this.f19065e = dVar;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.d
        public d<K, V> g() {
            return this.f19066f;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.d
        public d<K, V> h() {
            return this.f19065e;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.d
        public void i(d<K, V> dVar) {
            this.f19066f = dVar;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<V> iterator() {
            return new a();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            int d8 = v0.d(obj);
            int p8 = p() & d8;
            b<K, V> bVar = null;
            for (b<K, V> bVar2 = this.f19062b[p8]; bVar2 != null; bVar2 = bVar2.f19056d) {
                if (bVar2.c(obj, d8)) {
                    if (bVar == null) {
                        this.f19062b[p8] = bVar2.f19056d;
                    } else {
                        bVar.f19056d = bVar2.f19056d;
                    }
                    LinkedHashMultimap.Q(bVar2);
                    LinkedHashMultimap.P(bVar2);
                    this.f19063c--;
                    this.f19064d++;
                    return true;
                }
                bVar = bVar2;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f19063c;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d<K, V> {
        void e(d<K, V> dVar);

        d<K, V> g();

        d<K, V> h();

        void i(d<K, V> dVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> void P(b<K, V> bVar) {
        R(bVar.a(), bVar.b());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> void Q(d<K, V> dVar) {
        S(dVar.g(), dVar.h());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> void R(b<K, V> bVar, b<K, V> bVar2) {
        bVar.j(bVar2);
        bVar2.f(bVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> void S(d<K, V> dVar, d<K, V> dVar2) {
        dVar.e(dVar2);
        dVar2.i(dVar);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        b<K, V> d8 = b.d();
        this.f19051h = d8;
        R(d8, d8);
        this.f19050g = 2;
        int readInt = objectInputStream.readInt();
        Map e8 = z1.e(12);
        for (int i8 = 0; i8 < readInt; i8++) {
            Object readObject = objectInputStream.readObject();
            e8.put(readObject, u(readObject));
        }
        int readInt2 = objectInputStream.readInt();
        for (int i9 = 0; i9 < readInt2; i9++) {
            Object readObject2 = objectInputStream.readObject();
            Object readObject3 = objectInputStream.readObject();
            Collection collection = (Collection) e8.get(readObject2);
            Objects.requireNonNull(collection);
            collection.add(readObject3);
        }
        A(e8);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(keySet().size());
        for (K k8 : keySet()) {
            objectOutputStream.writeObject(k8);
        }
        objectOutputStream.writeInt(size());
        for (Map.Entry<K, V> entry : i()) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }

    @Override // com.google.common.collect.k, com.google.common.collect.e, com.google.common.collect.h
    /* renamed from: G */
    public Set<Map.Entry<K, V>> i() {
        return super.i();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.k
    public /* bridge */ /* synthetic */ Set H(Object obj) {
        return super.get(obj);
    }

    @Override // com.google.common.collect.k
    public /* bridge */ /* synthetic */ Set I(Object obj) {
        return super.a(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.e
    /* renamed from: O */
    public Set<V> t() {
        return z1.f(this.f19050g);
    }

    @Override // com.google.common.collect.k, com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ Map b() {
        return super.b();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean c(Object obj, Object obj2) {
        return super.c(obj, obj2);
    }

    @Override // com.google.common.collect.e, com.google.common.collect.n1
    public void clear() {
        super.clear();
        b<K, V> bVar = this.f19051h;
        R(bVar, bVar);
    }

    @Override // com.google.common.collect.e, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean containsKey(Object obj) {
        return super.containsKey(obj);
    }

    @Override // com.google.common.collect.h
    public /* bridge */ /* synthetic */ boolean d(Object obj) {
        return super.d(obj);
    }

    @Override // com.google.common.collect.k, com.google.common.collect.h
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.google.common.collect.h
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.google.common.collect.e, com.google.common.collect.h
    Iterator<Map.Entry<K, V>> j() {
        return new a();
    }

    @Override // com.google.common.collect.e, com.google.common.collect.h
    Iterator<V> k() {
        return m1.t(j());
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public Set<K> keySet() {
        return super.keySet();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.k, com.google.common.collect.e, com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean put(Object obj, Object obj2) {
        return super.put(obj, obj2);
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean remove(Object obj, Object obj2) {
        return super.remove(obj, obj2);
    }

    @Override // com.google.common.collect.e, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    @Override // com.google.common.collect.h
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.e
    public Collection<V> u(K k8) {
        return new c(k8, this.f19050g);
    }

    @Override // com.google.common.collect.e, com.google.common.collect.h, com.google.common.collect.n1
    public Collection<V> values() {
        return super.values();
    }
}
