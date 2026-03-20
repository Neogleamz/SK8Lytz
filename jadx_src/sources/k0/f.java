package k0;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class f<K, V> {

    /* renamed from: a  reason: collision with root package name */
    f<K, V>.b f20882a;

    /* renamed from: b  reason: collision with root package name */
    f<K, V>.c f20883b;

    /* renamed from: c  reason: collision with root package name */
    f<K, V>.e f20884c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class a<T> implements Iterator<T> {

        /* renamed from: a  reason: collision with root package name */
        final int f20885a;

        /* renamed from: b  reason: collision with root package name */
        int f20886b;

        /* renamed from: c  reason: collision with root package name */
        int f20887c;

        /* renamed from: d  reason: collision with root package name */
        boolean f20888d = false;

        a(int i8) {
            this.f20885a = i8;
            this.f20886b = f.this.d();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f20887c < this.f20886b;
        }

        @Override // java.util.Iterator
        public T next() {
            if (hasNext()) {
                T t8 = (T) f.this.b(this.f20887c, this.f20885a);
                this.f20887c++;
                this.f20888d = true;
                return t8;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            if (!this.f20888d) {
                throw new IllegalStateException();
            }
            int i8 = this.f20887c - 1;
            this.f20887c = i8;
            this.f20886b--;
            this.f20888d = false;
            f.this.h(i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class b implements Set<Map.Entry<K, V>> {
        b() {
        }

        @Override // java.util.Set, java.util.Collection
        public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
            int d8 = f.this.d();
            for (Map.Entry<K, V> entry : collection) {
                f.this.g(entry.getKey(), entry.getValue());
            }
            return d8 != f.this.d();
        }

        @Override // java.util.Set, java.util.Collection
        public void clear() {
            f.this.a();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean contains(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                int e8 = f.this.e(entry.getKey());
                if (e8 < 0) {
                    return false;
                }
                return k0.c.c(f.this.b(e8, 1), entry.getValue());
            }
            return false;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.Set, java.util.Collection
        /* renamed from: e */
        public boolean add(Map.Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean equals(Object obj) {
            return f.k(this, obj);
        }

        @Override // java.util.Set, java.util.Collection
        public int hashCode() {
            int i8 = 0;
            for (int d8 = f.this.d() - 1; d8 >= 0; d8--) {
                Object b9 = f.this.b(d8, 0);
                Object b10 = f.this.b(d8, 1);
                i8 += (b9 == null ? 0 : b9.hashCode()) ^ (b10 == null ? 0 : b10.hashCode());
            }
            return i8;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean isEmpty() {
            return f.this.d() == 0;
        }

        @Override // java.util.Set, java.util.Collection, java.lang.Iterable
        public Iterator<Map.Entry<K, V>> iterator() {
            return new d();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public int size() {
            return f.this.d();
        }

        @Override // java.util.Set, java.util.Collection
        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class c implements Set<K> {
        c() {
        }

        @Override // java.util.Set, java.util.Collection
        public boolean add(K k8) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean addAll(Collection<? extends K> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public void clear() {
            f.this.a();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean contains(Object obj) {
            return f.this.e(obj) >= 0;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            return f.j(f.this.c(), collection);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean equals(Object obj) {
            return f.k(this, obj);
        }

        @Override // java.util.Set, java.util.Collection
        public int hashCode() {
            int i8 = 0;
            for (int d8 = f.this.d() - 1; d8 >= 0; d8--) {
                Object b9 = f.this.b(d8, 0);
                i8 += b9 == null ? 0 : b9.hashCode();
            }
            return i8;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean isEmpty() {
            return f.this.d() == 0;
        }

        @Override // java.util.Set, java.util.Collection, java.lang.Iterable
        public Iterator<K> iterator() {
            return new a(0);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean remove(Object obj) {
            int e8 = f.this.e(obj);
            if (e8 >= 0) {
                f.this.h(e8);
                return true;
            }
            return false;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            return f.o(f.this.c(), collection);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            return f.p(f.this.c(), collection);
        }

        @Override // java.util.Set, java.util.Collection
        public int size() {
            return f.this.d();
        }

        @Override // java.util.Set, java.util.Collection
        public Object[] toArray() {
            return f.this.q(0);
        }

        @Override // java.util.Set, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) f.this.r(tArr, 0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class d implements Iterator<Map.Entry<K, V>>, Map.Entry<K, V> {

        /* renamed from: a  reason: collision with root package name */
        int f20892a;

        /* renamed from: c  reason: collision with root package name */
        boolean f20894c = false;

        /* renamed from: b  reason: collision with root package name */
        int f20893b = -1;

        d() {
            this.f20892a = f.this.d() - 1;
        }

        @Override // java.util.Iterator
        /* renamed from: a */
        public Map.Entry<K, V> next() {
            if (hasNext()) {
                this.f20893b++;
                this.f20894c = true;
                return this;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (this.f20894c) {
                if (obj instanceof Map.Entry) {
                    Map.Entry entry = (Map.Entry) obj;
                    return k0.c.c(entry.getKey(), f.this.b(this.f20893b, 0)) && k0.c.c(entry.getValue(), f.this.b(this.f20893b, 1));
                }
                return false;
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            if (this.f20894c) {
                return (K) f.this.b(this.f20893b, 0);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            if (this.f20894c) {
                return (V) f.this.b(this.f20893b, 1);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f20893b < this.f20892a;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            if (this.f20894c) {
                Object b9 = f.this.b(this.f20893b, 0);
                Object b10 = f.this.b(this.f20893b, 1);
                return (b9 == null ? 0 : b9.hashCode()) ^ (b10 != null ? b10.hashCode() : 0);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Iterator
        public void remove() {
            if (!this.f20894c) {
                throw new IllegalStateException();
            }
            f.this.h(this.f20893b);
            this.f20893b--;
            this.f20892a--;
            this.f20894c = false;
        }

        @Override // java.util.Map.Entry
        public V setValue(V v8) {
            if (this.f20894c) {
                return (V) f.this.i(this.f20893b, v8);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class e implements Collection<V> {
        e() {
        }

        @Override // java.util.Collection
        public boolean add(V v8) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public void clear() {
            f.this.a();
        }

        @Override // java.util.Collection
        public boolean contains(Object obj) {
            return f.this.f(obj) >= 0;
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            return f.this.d() == 0;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return new a(1);
        }

        @Override // java.util.Collection
        public boolean remove(Object obj) {
            int f5 = f.this.f(obj);
            if (f5 >= 0) {
                f.this.h(f5);
                return true;
            }
            return false;
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            int d8 = f.this.d();
            int i8 = 0;
            boolean z4 = false;
            while (i8 < d8) {
                if (collection.contains(f.this.b(i8, 1))) {
                    f.this.h(i8);
                    i8--;
                    d8--;
                    z4 = true;
                }
                i8++;
            }
            return z4;
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            int d8 = f.this.d();
            int i8 = 0;
            boolean z4 = false;
            while (i8 < d8) {
                if (!collection.contains(f.this.b(i8, 1))) {
                    f.this.h(i8);
                    i8--;
                    d8--;
                    z4 = true;
                }
                i8++;
            }
            return z4;
        }

        @Override // java.util.Collection
        public int size() {
            return f.this.d();
        }

        @Override // java.util.Collection
        public Object[] toArray() {
            return f.this.q(1);
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) f.this.r(tArr, 1);
        }
    }

    public static <K, V> boolean j(Map<K, V> map, Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (!map.containsKey(it.next())) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean k(Set<T> set, Object obj) {
        if (set == obj) {
            return true;
        }
        if (obj instanceof Set) {
            Set set2 = (Set) obj;
            try {
                if (set.size() == set2.size()) {
                    if (set.containsAll(set2)) {
                        return true;
                    }
                }
                return false;
            } catch (ClassCastException | NullPointerException unused) {
            }
        }
        return false;
    }

    public static <K, V> boolean o(Map<K, V> map, Collection<?> collection) {
        int size = map.size();
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            map.remove(it.next());
        }
        return size != map.size();
    }

    public static <K, V> boolean p(Map<K, V> map, Collection<?> collection) {
        int size = map.size();
        Iterator<K> it = map.keySet().iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
            }
        }
        return size != map.size();
    }

    protected abstract void a();

    protected abstract Object b(int i8, int i9);

    protected abstract Map<K, V> c();

    protected abstract int d();

    protected abstract int e(Object obj);

    protected abstract int f(Object obj);

    protected abstract void g(K k8, V v8);

    protected abstract void h(int i8);

    protected abstract V i(int i8, V v8);

    public Set<Map.Entry<K, V>> l() {
        if (this.f20882a == null) {
            this.f20882a = new b();
        }
        return this.f20882a;
    }

    public Set<K> m() {
        if (this.f20883b == null) {
            this.f20883b = new c();
        }
        return this.f20883b;
    }

    public Collection<V> n() {
        if (this.f20884c == null) {
            this.f20884c = new e();
        }
        return this.f20884c;
    }

    public Object[] q(int i8) {
        int d8 = d();
        Object[] objArr = new Object[d8];
        for (int i9 = 0; i9 < d8; i9++) {
            objArr[i9] = b(i9, i8);
        }
        return objArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T[] r(T[] tArr, int i8) {
        int d8 = d();
        if (tArr.length < d8) {
            tArr = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), d8));
        }
        for (int i9 = 0; i9 < d8; i9++) {
            tArr[i9] = b(i9, i8);
        }
        if (tArr.length > d8) {
            tArr[d8] = null;
        }
        return tArr;
    }
}
