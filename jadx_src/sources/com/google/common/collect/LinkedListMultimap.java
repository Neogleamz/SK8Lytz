package com.google.common.collect;

import com.google.common.collect.p2;
import com.google.common.collect.q1;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class LinkedListMultimap<K, V> extends com.google.common.collect.h<K, V> implements i1<K, V>, Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: e  reason: collision with root package name */
    private transient g<K, V> f19072e;

    /* renamed from: f  reason: collision with root package name */
    private transient g<K, V> f19073f;

    /* renamed from: g  reason: collision with root package name */
    private transient Map<K, f<K, V>> f19074g;

    /* renamed from: h  reason: collision with root package name */
    private transient int f19075h;

    /* renamed from: j  reason: collision with root package name */
    private transient int f19076j;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends AbstractSequentialList<V> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Object f19077a;

        a(Object obj) {
            this.f19077a = obj;
        }

        @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public ListIterator<V> listIterator(int i8) {
            return new i(this.f19077a, i8);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            f fVar = (f) LinkedListMultimap.this.f19074g.get(this.f19077a);
            if (fVar == null) {
                return 0;
            }
            return fVar.f19090c;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends AbstractSequentialList<Map.Entry<K, V>> {
        b() {
        }

        @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public ListIterator<Map.Entry<K, V>> listIterator(int i8) {
            return new h(i8);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return LinkedListMultimap.this.f19075h;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends p2.d<K> {
        c() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return LinkedListMultimap.this.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new e(LinkedListMultimap.this, null);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return !LinkedListMultimap.this.a(obj).isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return LinkedListMultimap.this.f19074g.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends AbstractSequentialList<V> {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends c3<Map.Entry<K, V>, V> {

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ h f19082b;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(d dVar, ListIterator listIterator, h hVar) {
                super(listIterator);
                this.f19082b = hVar;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.collect.b3
            /* renamed from: c */
            public V a(Map.Entry<K, V> entry) {
                return entry.getValue();
            }

            @Override // java.util.ListIterator
            public void set(V v8) {
                this.f19082b.f(v8);
            }
        }

        d() {
        }

        @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public ListIterator<V> listIterator(int i8) {
            h hVar = new h(i8);
            return new a(this, hVar, hVar);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return LinkedListMultimap.this.f19075h;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class e implements Iterator<K> {

        /* renamed from: a  reason: collision with root package name */
        final Set<K> f19083a;

        /* renamed from: b  reason: collision with root package name */
        g<K, V> f19084b;

        /* renamed from: c  reason: collision with root package name */
        g<K, V> f19085c;

        /* renamed from: d  reason: collision with root package name */
        int f19086d;

        private e() {
            this.f19083a = p2.g(LinkedListMultimap.this.keySet().size());
            this.f19084b = LinkedListMultimap.this.f19072e;
            this.f19086d = LinkedListMultimap.this.f19076j;
        }

        /* synthetic */ e(LinkedListMultimap linkedListMultimap, a aVar) {
            this();
        }

        private void a() {
            if (LinkedListMultimap.this.f19076j != this.f19086d) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            a();
            return this.f19084b != null;
        }

        @Override // java.util.Iterator
        public K next() {
            g<K, V> gVar;
            a();
            g<K, V> gVar2 = this.f19084b;
            if (gVar2 != null) {
                this.f19085c = gVar2;
                this.f19083a.add(gVar2.f19091a);
                do {
                    gVar = this.f19084b.f19093c;
                    this.f19084b = gVar;
                    if (gVar == null) {
                        break;
                    }
                } while (!this.f19083a.add(gVar.f19091a));
                return this.f19085c.f19091a;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            a();
            com.google.common.base.l.t(this.f19085c != null, "no calls to next() since the last call to remove()");
            LinkedListMultimap.this.A(this.f19085c.f19091a);
            this.f19085c = null;
            this.f19086d = LinkedListMultimap.this.f19076j;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f<K, V> {

        /* renamed from: a  reason: collision with root package name */
        g<K, V> f19088a;

        /* renamed from: b  reason: collision with root package name */
        g<K, V> f19089b;

        /* renamed from: c  reason: collision with root package name */
        int f19090c;

        f(g<K, V> gVar) {
            this.f19088a = gVar;
            this.f19089b = gVar;
            gVar.f19096f = null;
            gVar.f19095e = null;
            this.f19090c = 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g<K, V> extends com.google.common.collect.g<K, V> {

        /* renamed from: a  reason: collision with root package name */
        final K f19091a;

        /* renamed from: b  reason: collision with root package name */
        V f19092b;

        /* renamed from: c  reason: collision with root package name */
        g<K, V> f19093c;

        /* renamed from: d  reason: collision with root package name */
        g<K, V> f19094d;

        /* renamed from: e  reason: collision with root package name */
        g<K, V> f19095e;

        /* renamed from: f  reason: collision with root package name */
        g<K, V> f19096f;

        g(K k8, V v8) {
            this.f19091a = k8;
            this.f19092b = v8;
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public K getKey() {
            return this.f19091a;
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public V getValue() {
            return this.f19092b;
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public V setValue(V v8) {
            V v9 = this.f19092b;
            this.f19092b = v8;
            return v9;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class h implements ListIterator<Map.Entry<K, V>> {

        /* renamed from: a  reason: collision with root package name */
        int f19097a;

        /* renamed from: b  reason: collision with root package name */
        g<K, V> f19098b;

        /* renamed from: c  reason: collision with root package name */
        g<K, V> f19099c;

        /* renamed from: d  reason: collision with root package name */
        g<K, V> f19100d;

        /* renamed from: e  reason: collision with root package name */
        int f19101e;

        h(int i8) {
            this.f19101e = LinkedListMultimap.this.f19076j;
            int size = LinkedListMultimap.this.size();
            com.google.common.base.l.p(i8, size);
            if (i8 < size / 2) {
                this.f19098b = LinkedListMultimap.this.f19072e;
                while (true) {
                    int i9 = i8 - 1;
                    if (i8 <= 0) {
                        break;
                    }
                    next();
                    i8 = i9;
                }
            } else {
                this.f19100d = LinkedListMultimap.this.f19073f;
                this.f19097a = size;
                while (true) {
                    int i10 = i8 + 1;
                    if (i8 >= size) {
                        break;
                    }
                    previous();
                    i8 = i10;
                }
            }
            this.f19099c = null;
        }

        private void b() {
            if (LinkedListMultimap.this.f19076j != this.f19101e) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.ListIterator
        /* renamed from: a */
        public void add(Map.Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        /* renamed from: c */
        public g<K, V> next() {
            b();
            g<K, V> gVar = this.f19098b;
            if (gVar != null) {
                this.f19099c = gVar;
                this.f19100d = gVar;
                this.f19098b = gVar.f19093c;
                this.f19097a++;
                return gVar;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        /* renamed from: d */
        public g<K, V> previous() {
            b();
            g<K, V> gVar = this.f19100d;
            if (gVar != null) {
                this.f19099c = gVar;
                this.f19098b = gVar;
                this.f19100d = gVar.f19094d;
                this.f19097a--;
                return gVar;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        /* renamed from: e */
        public void set(Map.Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }

        void f(V v8) {
            com.google.common.base.l.s(this.f19099c != null);
            this.f19099c.f19092b = v8;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            b();
            return this.f19098b != null;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            b();
            return this.f19100d != null;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.f19097a;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.f19097a - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            b();
            com.google.common.base.l.t(this.f19099c != null, "no calls to next() since the last call to remove()");
            g<K, V> gVar = this.f19099c;
            if (gVar != this.f19098b) {
                this.f19100d = gVar.f19094d;
                this.f19097a--;
            } else {
                this.f19098b = gVar.f19093c;
            }
            LinkedListMultimap.this.B(gVar);
            this.f19099c = null;
            this.f19101e = LinkedListMultimap.this.f19076j;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class i implements ListIterator<V> {

        /* renamed from: a  reason: collision with root package name */
        final K f19103a;

        /* renamed from: b  reason: collision with root package name */
        int f19104b;

        /* renamed from: c  reason: collision with root package name */
        g<K, V> f19105c;

        /* renamed from: d  reason: collision with root package name */
        g<K, V> f19106d;

        /* renamed from: e  reason: collision with root package name */
        g<K, V> f19107e;

        i(K k8) {
            this.f19103a = k8;
            f fVar = (f) LinkedListMultimap.this.f19074g.get(k8);
            this.f19105c = fVar == null ? null : fVar.f19088a;
        }

        public i(K k8, int i8) {
            f fVar = (f) LinkedListMultimap.this.f19074g.get(k8);
            int i9 = fVar == null ? 0 : fVar.f19090c;
            com.google.common.base.l.p(i8, i9);
            if (i8 < i9 / 2) {
                this.f19105c = fVar == null ? null : fVar.f19088a;
                while (true) {
                    int i10 = i8 - 1;
                    if (i8 <= 0) {
                        break;
                    }
                    next();
                    i8 = i10;
                }
            } else {
                this.f19107e = fVar == null ? null : fVar.f19089b;
                this.f19104b = i9;
                while (true) {
                    int i11 = i8 + 1;
                    if (i8 >= i9) {
                        break;
                    }
                    previous();
                    i8 = i11;
                }
            }
            this.f19103a = k8;
            this.f19106d = null;
        }

        @Override // java.util.ListIterator
        public void add(V v8) {
            this.f19107e = LinkedListMultimap.this.t(this.f19103a, v8, this.f19105c);
            this.f19104b++;
            this.f19106d = null;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.f19105c != null;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.f19107e != null;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public V next() {
            g<K, V> gVar = this.f19105c;
            if (gVar != null) {
                this.f19106d = gVar;
                this.f19107e = gVar;
                this.f19105c = gVar.f19095e;
                this.f19104b++;
                return gVar.f19092b;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.f19104b;
        }

        @Override // java.util.ListIterator
        public V previous() {
            g<K, V> gVar = this.f19107e;
            if (gVar != null) {
                this.f19106d = gVar;
                this.f19105c = gVar;
                this.f19107e = gVar.f19096f;
                this.f19104b--;
                return gVar.f19092b;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.f19104b - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            com.google.common.base.l.t(this.f19106d != null, "no calls to next() since the last call to remove()");
            g<K, V> gVar = this.f19106d;
            if (gVar != this.f19105c) {
                this.f19107e = gVar.f19096f;
                this.f19104b--;
            } else {
                this.f19105c = gVar.f19095e;
            }
            LinkedListMultimap.this.B(gVar);
            this.f19106d = null;
        }

        @Override // java.util.ListIterator
        public void set(V v8) {
            com.google.common.base.l.s(this.f19106d != null);
            this.f19106d.f19092b = v8;
        }
    }

    LinkedListMultimap() {
        this(12);
    }

    private LinkedListMultimap(int i8) {
        this.f19074g = z1.c(i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void A(K k8) {
        g1.e(new i(k8));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void B(g<K, V> gVar) {
        g<K, V> gVar2 = gVar.f19094d;
        g<K, V> gVar3 = gVar.f19093c;
        if (gVar2 != null) {
            gVar2.f19093c = gVar3;
        } else {
            this.f19072e = gVar3;
        }
        g<K, V> gVar4 = gVar.f19093c;
        if (gVar4 != null) {
            gVar4.f19094d = gVar2;
        } else {
            this.f19073f = gVar2;
        }
        if (gVar.f19096f == null && gVar.f19095e == null) {
            f<K, V> remove = this.f19074g.remove(gVar.f19091a);
            Objects.requireNonNull(remove);
            remove.f19090c = 0;
            this.f19076j++;
        } else {
            f<K, V> fVar = this.f19074g.get(gVar.f19091a);
            Objects.requireNonNull(fVar);
            fVar.f19090c--;
            g<K, V> gVar5 = gVar.f19096f;
            if (gVar5 == null) {
                g<K, V> gVar6 = gVar.f19095e;
                Objects.requireNonNull(gVar6);
                fVar.f19088a = gVar6;
            } else {
                gVar5.f19095e = gVar.f19095e;
            }
            g<K, V> gVar7 = gVar.f19095e;
            g<K, V> gVar8 = gVar.f19096f;
            if (gVar7 == null) {
                Objects.requireNonNull(gVar8);
                fVar.f19089b = gVar8;
            } else {
                gVar7.f19096f = gVar8;
            }
        }
        this.f19075h--;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.f19074g = y.c0();
        int readInt = objectInputStream.readInt();
        for (int i8 = 0; i8 < readInt; i8++) {
            put(objectInputStream.readObject(), objectInputStream.readObject());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public g<K, V> t(K k8, V v8, g<K, V> gVar) {
        Map<K, f<K, V>> map;
        f<K, V> fVar;
        g<K, V> gVar2 = new g<>(k8, v8);
        if (this.f19072e != null) {
            if (gVar == null) {
                g<K, V> gVar3 = this.f19073f;
                Objects.requireNonNull(gVar3);
                gVar3.f19093c = gVar2;
                gVar2.f19094d = this.f19073f;
                this.f19073f = gVar2;
                f<K, V> fVar2 = this.f19074g.get(k8);
                if (fVar2 == null) {
                    map = this.f19074g;
                    fVar = new f<>(gVar2);
                } else {
                    fVar2.f19090c++;
                    g<K, V> gVar4 = fVar2.f19089b;
                    gVar4.f19095e = gVar2;
                    gVar2.f19096f = gVar4;
                    fVar2.f19089b = gVar2;
                }
            } else {
                f<K, V> fVar3 = this.f19074g.get(k8);
                Objects.requireNonNull(fVar3);
                fVar3.f19090c++;
                gVar2.f19094d = gVar.f19094d;
                gVar2.f19096f = gVar.f19096f;
                gVar2.f19093c = gVar;
                gVar2.f19095e = gVar;
                g<K, V> gVar5 = gVar.f19096f;
                if (gVar5 == null) {
                    fVar3.f19088a = gVar2;
                } else {
                    gVar5.f19095e = gVar2;
                }
                g<K, V> gVar6 = gVar.f19094d;
                if (gVar6 == null) {
                    this.f19072e = gVar2;
                } else {
                    gVar6.f19093c = gVar2;
                }
                gVar.f19094d = gVar2;
                gVar.f19096f = gVar2;
            }
            this.f19075h++;
            return gVar2;
        }
        this.f19073f = gVar2;
        this.f19072e = gVar2;
        map = this.f19074g;
        fVar = new f<>(gVar2);
        map.put(k8, fVar);
        this.f19076j++;
        this.f19075h++;
        return gVar2;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(size());
        for (Map.Entry<K, V> entry : i()) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }

    private List<V> y(K k8) {
        return Collections.unmodifiableList(j1.i(new i(k8)));
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    /* renamed from: D */
    public List<V> values() {
        return (List) super.values();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ Map b() {
        return super.b();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean c(Object obj, Object obj2) {
        return super.c(obj, obj2);
    }

    @Override // com.google.common.collect.n1
    public void clear() {
        this.f19072e = null;
        this.f19073f = null;
        this.f19074g.clear();
        this.f19075h = 0;
        this.f19076j++;
    }

    @Override // com.google.common.collect.n1
    public boolean containsKey(Object obj) {
        return this.f19074g.containsKey(obj);
    }

    @Override // com.google.common.collect.h
    public boolean d(Object obj) {
        return values().contains(obj);
    }

    @Override // com.google.common.collect.h
    Map<K, Collection<V>> e() {
        return new q1.a(this);
    }

    @Override // com.google.common.collect.h
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.google.common.collect.h
    Set<K> g() {
        return new c();
    }

    @Override // com.google.common.collect.h
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public boolean isEmpty() {
        return this.f19072e == null;
    }

    @Override // com.google.common.collect.h
    Iterator<Map.Entry<K, V>> j() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ Set keySet() {
        return super.keySet();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public boolean put(K k8, V v8) {
        t(k8, v8, null);
        return true;
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean remove(Object obj, Object obj2) {
        return super.remove(obj, obj2);
    }

    @Override // com.google.common.collect.n1
    public int size() {
        return this.f19075h;
    }

    @Override // com.google.common.collect.h
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.h
    /* renamed from: u */
    public List<Map.Entry<K, V>> f() {
        return new b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.h
    /* renamed from: v */
    public List<V> h() {
        return new d();
    }

    @Override // com.google.common.collect.h
    /* renamed from: w */
    public List<Map.Entry<K, V>> i() {
        return (List) super.i();
    }

    @Override // com.google.common.collect.n1
    /* renamed from: x */
    public List<V> get(K k8) {
        return new a(k8);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.n1
    /* renamed from: z */
    public List<V> a(Object obj) {
        List<V> y8 = y(obj);
        A(obj);
        return y8;
    }
}
