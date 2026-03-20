package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.collect.l1.i;
import com.google.common.collect.l1.n;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l1<K, V, E extends i<K, V, E>, S extends n<K, V, E, S>> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {

    /* renamed from: k  reason: collision with root package name */
    static final b0<Object, Object, e> f19338k = new a();
    private static final long serialVersionUID = 5;

    /* renamed from: a  reason: collision with root package name */
    final transient int f19339a;

    /* renamed from: b  reason: collision with root package name */
    final transient int f19340b;

    /* renamed from: c  reason: collision with root package name */
    final transient n<K, V, E, S>[] f19341c;

    /* renamed from: d  reason: collision with root package name */
    final int f19342d;

    /* renamed from: e  reason: collision with root package name */
    final Equivalence<Object> f19343e;

    /* renamed from: f  reason: collision with root package name */
    final transient j<K, V, E, S> f19344f;

    /* renamed from: g  reason: collision with root package name */
    transient Set<K> f19345g;

    /* renamed from: h  reason: collision with root package name */
    transient Collection<V> f19346h;

    /* renamed from: j  reason: collision with root package name */
    transient Set<Map.Entry<K, V>> f19347j;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements b0<Object, Object, e> {
        a() {
        }

        @Override // com.google.common.collect.l1.b0
        /* renamed from: c */
        public b0<Object, Object, e> b(ReferenceQueue<Object> referenceQueue, e eVar) {
            return this;
        }

        @Override // com.google.common.collect.l1.b0
        public void clear() {
        }

        @Override // com.google.common.collect.l1.b0
        /* renamed from: d */
        public e a() {
            return null;
        }

        @Override // com.google.common.collect.l1.b0
        public Object get() {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a0<K, V, E extends i<K, V, E>> extends i<K, V, E> {
        b0<K, V, E> b();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static abstract class b<K, V> extends k0<K, V> implements Serializable {
        private static final long serialVersionUID = 3;

        /* renamed from: a  reason: collision with root package name */
        final p f19348a;

        /* renamed from: b  reason: collision with root package name */
        final p f19349b;

        /* renamed from: c  reason: collision with root package name */
        final Equivalence<Object> f19350c;

        /* renamed from: d  reason: collision with root package name */
        final Equivalence<Object> f19351d;

        /* renamed from: e  reason: collision with root package name */
        final int f19352e;

        /* renamed from: f  reason: collision with root package name */
        transient ConcurrentMap<K, V> f19353f;

        b(p pVar, p pVar2, Equivalence<Object> equivalence, Equivalence<Object> equivalence2, int i8, ConcurrentMap<K, V> concurrentMap) {
            this.f19348a = pVar;
            this.f19349b = pVar2;
            this.f19350c = equivalence;
            this.f19351d = equivalence2;
            this.f19352e = i8;
            this.f19353f = concurrentMap;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.m0
        /* renamed from: q */
        public ConcurrentMap<K, V> i() {
            return this.f19353f;
        }

        /* JADX WARN: Multi-variable type inference failed */
        void t(ObjectInputStream objectInputStream) {
            while (true) {
                Object readObject = objectInputStream.readObject();
                if (readObject == null) {
                    return;
                }
                this.f19353f.put(readObject, objectInputStream.readObject());
            }
        }

        k1 u(ObjectInputStream objectInputStream) {
            return new k1().g(objectInputStream.readInt()).j(this.f19348a).k(this.f19349b).h(this.f19350c).a(this.f19352e);
        }

        void v(ObjectOutputStream objectOutputStream) {
            objectOutputStream.writeInt(this.f19353f.size());
            for (Map.Entry<K, V> entry : this.f19353f.entrySet()) {
                objectOutputStream.writeObject(entry.getKey());
                objectOutputStream.writeObject(entry.getValue());
            }
            objectOutputStream.writeObject(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b0<K, V, E extends i<K, V, E>> {
        E a();

        b0<K, V, E> b(ReferenceQueue<V> referenceQueue, E e8);

        void clear();

        V get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class c<K, V, E extends i<K, V, E>> implements i<K, V, E> {

        /* renamed from: a  reason: collision with root package name */
        final K f19354a;

        /* renamed from: b  reason: collision with root package name */
        final int f19355b;

        /* renamed from: c  reason: collision with root package name */
        final E f19356c;

        c(K k8, int i8, E e8) {
            this.f19354a = k8;
            this.f19355b = i8;
            this.f19356c = e8;
        }

        @Override // com.google.common.collect.l1.i
        public E a() {
            return this.f19356c;
        }

        @Override // com.google.common.collect.l1.i
        public int c() {
            return this.f19355b;
        }

        @Override // com.google.common.collect.l1.i
        public K getKey() {
            return this.f19354a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c0<K, V, E extends i<K, V, E>> extends WeakReference<V> implements b0<K, V, E> {

        /* renamed from: a  reason: collision with root package name */
        final E f19357a;

        c0(ReferenceQueue<V> referenceQueue, V v8, E e8) {
            super(v8, referenceQueue);
            this.f19357a = e8;
        }

        @Override // com.google.common.collect.l1.b0
        public E a() {
            return this.f19357a;
        }

        @Override // com.google.common.collect.l1.b0
        public b0<K, V, E> b(ReferenceQueue<V> referenceQueue, E e8) {
            return new c0(referenceQueue, get(), e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class d<K, V, E extends i<K, V, E>> extends WeakReference<K> implements i<K, V, E> {

        /* renamed from: a  reason: collision with root package name */
        final int f19358a;

        /* renamed from: b  reason: collision with root package name */
        final E f19359b;

        d(ReferenceQueue<K> referenceQueue, K k8, int i8, E e8) {
            super(k8, referenceQueue);
            this.f19358a = i8;
            this.f19359b = e8;
        }

        @Override // com.google.common.collect.l1.i
        public E a() {
            return this.f19359b;
        }

        @Override // com.google.common.collect.l1.i
        public int c() {
            return this.f19358a;
        }

        @Override // com.google.common.collect.l1.i
        public K getKey() {
            return get();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class d0 extends com.google.common.collect.g<K, V> {

        /* renamed from: a  reason: collision with root package name */
        final K f19360a;

        /* renamed from: b  reason: collision with root package name */
        V f19361b;

        d0(K k8, V v8) {
            this.f19360a = k8;
            this.f19361b = v8;
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public boolean equals(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                return this.f19360a.equals(entry.getKey()) && this.f19361b.equals(entry.getValue());
            }
            return false;
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public K getKey() {
            return this.f19360a;
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public V getValue() {
            return this.f19361b;
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public int hashCode() {
            return this.f19360a.hashCode() ^ this.f19361b.hashCode();
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public V setValue(V v8) {
            V v9 = (V) l1.this.put(this.f19360a, v8);
            this.f19361b = v8;
            return v9;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e implements i<Object, Object, e> {
        private e() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.l1.i
        public int c() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.l1.i
        /* renamed from: d */
        public e a() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.l1.i
        public Object getKey() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.l1.i
        public Object getValue() {
            throw new AssertionError();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class f extends l1<K, V, E, S>.h<Map.Entry<K, V>> {
        f(l1 l1Var) {
            super();
        }

        @Override // java.util.Iterator
        /* renamed from: f */
        public Map.Entry<K, V> next() {
            return c();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class g extends m<Map.Entry<K, V>> {
        g() {
            super(null);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            l1.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            Map.Entry entry;
            Object key;
            Object obj2;
            return (obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && (obj2 = l1.this.get(key)) != null && l1.this.o().d(entry.getValue(), obj2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return l1.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new f(l1.this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Map.Entry entry;
            Object key;
            return (obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && l1.this.remove(key, entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return l1.this.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public abstract class h<T> implements Iterator<T> {

        /* renamed from: a  reason: collision with root package name */
        int f19364a;

        /* renamed from: b  reason: collision with root package name */
        int f19365b = -1;

        /* renamed from: c  reason: collision with root package name */
        n<K, V, E, S> f19366c;

        /* renamed from: d  reason: collision with root package name */
        AtomicReferenceArray<E> f19367d;

        /* renamed from: e  reason: collision with root package name */
        E f19368e;

        /* renamed from: f  reason: collision with root package name */
        l1<K, V, E, S>.d0 f19369f;

        /* renamed from: g  reason: collision with root package name */
        l1<K, V, E, S>.d0 f19370g;

        h() {
            this.f19364a = l1.this.f19341c.length - 1;
            a();
        }

        final void a() {
            this.f19369f = null;
            if (d() || e()) {
                return;
            }
            while (true) {
                int i8 = this.f19364a;
                if (i8 < 0) {
                    return;
                }
                n<K, V, E, S>[] nVarArr = l1.this.f19341c;
                this.f19364a = i8 - 1;
                n<K, V, E, S> nVar = nVarArr[i8];
                this.f19366c = nVar;
                if (nVar.f19374b != 0) {
                    AtomicReferenceArray<E> atomicReferenceArray = this.f19366c.f19377e;
                    this.f19367d = atomicReferenceArray;
                    this.f19365b = atomicReferenceArray.length() - 1;
                    if (e()) {
                        return;
                    }
                }
            }
        }

        boolean b(E e8) {
            boolean z4;
            try {
                Object key = e8.getKey();
                Object d8 = l1.this.d(e8);
                if (d8 != null) {
                    this.f19369f = new d0(key, d8);
                    z4 = true;
                } else {
                    z4 = false;
                }
                return z4;
            } finally {
                this.f19366c.r();
            }
        }

        l1<K, V, E, S>.d0 c() {
            l1<K, V, E, S>.d0 d0Var = this.f19369f;
            if (d0Var != null) {
                this.f19370g = d0Var;
                a();
                return this.f19370g;
            }
            throw new NoSuchElementException();
        }

        boolean d() {
            E e8 = this.f19368e;
            if (e8 == null) {
                return false;
            }
            while (true) {
                this.f19368e = (E) e8.a();
                E e9 = this.f19368e;
                if (e9 == null) {
                    return false;
                }
                if (b(e9)) {
                    return true;
                }
                e8 = this.f19368e;
            }
        }

        boolean e() {
            while (true) {
                int i8 = this.f19365b;
                if (i8 < 0) {
                    return false;
                }
                AtomicReferenceArray<E> atomicReferenceArray = this.f19367d;
                this.f19365b = i8 - 1;
                E e8 = atomicReferenceArray.get(i8);
                this.f19368e = e8;
                if (e8 != null && (b(e8) || d())) {
                    return true;
                }
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f19369f != null;
        }

        @Override // java.util.Iterator
        public void remove() {
            com.google.common.collect.t.d(this.f19370g != null);
            l1.this.remove(this.f19370g.getKey());
            this.f19370g = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface i<K, V, E extends i<K, V, E>> {
        E a();

        int c();

        K getKey();

        V getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface j<K, V, E extends i<K, V, E>, S extends n<K, V, E, S>> {
        E a(S s8, E e8, E e9);

        p b();

        p c();

        void d(S s8, E e8, V v8);

        S e(l1<K, V, E, S> l1Var, int i8, int i9);

        E f(S s8, K k8, int i8, E e8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class k extends l1<K, V, E, S>.h<K> {
        k(l1 l1Var) {
            super();
        }

        @Override // java.util.Iterator
        public K next() {
            return c().getKey();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class l extends m<K> {
        l() {
            super(null);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            l1.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return l1.this.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return l1.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new k(l1.this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return l1.this.remove(obj) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return l1.this.size();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static abstract class m<E> extends AbstractSet<E> {
        private m() {
        }

        /* synthetic */ m(a aVar) {
            this();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return l1.m(this).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] tArr) {
            return (T[]) l1.m(this).toArray(tArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class n<K, V, E extends i<K, V, E>, S extends n<K, V, E, S>> extends ReentrantLock {

        /* renamed from: a  reason: collision with root package name */
        final l1<K, V, E, S> f19373a;

        /* renamed from: b  reason: collision with root package name */
        volatile int f19374b;

        /* renamed from: c  reason: collision with root package name */
        int f19375c;

        /* renamed from: d  reason: collision with root package name */
        int f19376d;

        /* renamed from: e  reason: collision with root package name */
        volatile AtomicReferenceArray<E> f19377e;

        /* renamed from: f  reason: collision with root package name */
        final int f19378f;

        /* renamed from: g  reason: collision with root package name */
        final AtomicInteger f19379g = new AtomicInteger();

        n(l1<K, V, E, S> l1Var, int i8, int i9) {
            this.f19373a = l1Var;
            this.f19378f = i9;
            m(q(i8));
        }

        static <K, V, E extends i<K, V, E>> boolean n(E e8) {
            return e8.getValue() == null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        boolean A(K k8, int i8, V v8, V v9) {
            lock();
            try {
                s();
                AtomicReferenceArray<E> atomicReferenceArray = this.f19377e;
                int length = (atomicReferenceArray.length() - 1) & i8;
                E e8 = atomicReferenceArray.get(length);
                for (i iVar = e8; iVar != null; iVar = iVar.a()) {
                    Object key = iVar.getKey();
                    if (iVar.c() == i8 && key != null && this.f19373a.f19343e.d(k8, key)) {
                        Object value = iVar.getValue();
                        if (value != null) {
                            if (this.f19373a.o().d(v8, value)) {
                                this.f19375c++;
                                F(iVar, v9);
                                return true;
                            }
                            return false;
                        }
                        if (n(iVar)) {
                            this.f19375c++;
                            atomicReferenceArray.set(length, y(e8, iVar));
                            this.f19374b--;
                        }
                        return false;
                    }
                }
                return false;
            } finally {
                unlock();
            }
        }

        void B() {
            D();
        }

        void D() {
            if (tryLock()) {
                try {
                    p();
                    this.f19379g.set(0);
                } finally {
                    unlock();
                }
            }
        }

        abstract S E();

        void F(E e8, V v8) {
            this.f19373a.f19344f.d(E(), e8, v8);
        }

        void G() {
            if (tryLock()) {
                try {
                    p();
                } finally {
                    unlock();
                }
            }
        }

        void a() {
            if (this.f19374b != 0) {
                lock();
                try {
                    AtomicReferenceArray<E> atomicReferenceArray = this.f19377e;
                    for (int i8 = 0; i8 < atomicReferenceArray.length(); i8++) {
                        atomicReferenceArray.set(i8, null);
                    }
                    o();
                    this.f19379g.set(0);
                    this.f19375c++;
                    this.f19374b = 0;
                } finally {
                    unlock();
                }
            }
        }

        <T> void b(ReferenceQueue<T> referenceQueue) {
            do {
            } while (referenceQueue.poll() != null);
        }

        boolean c(Object obj, int i8) {
            try {
                boolean z4 = false;
                if (this.f19374b != 0) {
                    E k8 = k(obj, i8);
                    if (k8 != null) {
                        if (k8.getValue() != null) {
                            z4 = true;
                        }
                    }
                    return z4;
                }
                return false;
            } finally {
                r();
            }
        }

        E d(E e8, E e9) {
            return this.f19373a.f19344f.a(E(), e8, e9);
        }

        /* JADX WARN: Multi-variable type inference failed */
        void e(ReferenceQueue<K> referenceQueue) {
            int i8 = 0;
            do {
                Reference<? extends K> poll = referenceQueue.poll();
                if (poll == null) {
                    return;
                }
                this.f19373a.i((i) poll);
                i8++;
            } while (i8 != 16);
        }

        void f(ReferenceQueue<V> referenceQueue) {
            int i8 = 0;
            do {
                Reference<? extends V> poll = referenceQueue.poll();
                if (poll == null) {
                    return;
                }
                this.f19373a.j((b0) poll);
                i8++;
            } while (i8 != 16);
        }

        /* JADX WARN: Multi-variable type inference failed */
        void g() {
            AtomicReferenceArray<E> atomicReferenceArray = this.f19377e;
            int length = atomicReferenceArray.length();
            if (length >= 1073741824) {
                return;
            }
            int i8 = this.f19374b;
            AtomicReferenceArray<E> atomicReferenceArray2 = (AtomicReferenceArray<E>) q(length << 1);
            this.f19376d = (atomicReferenceArray2.length() * 3) / 4;
            int length2 = atomicReferenceArray2.length() - 1;
            for (int i9 = 0; i9 < length; i9++) {
                E e8 = atomicReferenceArray.get(i9);
                if (e8 != null) {
                    i a9 = e8.a();
                    int c9 = e8.c() & length2;
                    if (a9 == null) {
                        atomicReferenceArray2.set(c9, e8);
                    } else {
                        i iVar = e8;
                        while (a9 != null) {
                            int c10 = a9.c() & length2;
                            if (c10 != c9) {
                                iVar = a9;
                                c9 = c10;
                            }
                            a9 = a9.a();
                        }
                        atomicReferenceArray2.set(c9, iVar);
                        while (e8 != iVar) {
                            int c11 = e8.c() & length2;
                            i d8 = d(e8, (i) atomicReferenceArray2.get(c11));
                            if (d8 != null) {
                                atomicReferenceArray2.set(c11, d8);
                            } else {
                                i8--;
                            }
                            e8 = e8.a();
                        }
                    }
                }
            }
            this.f19377e = atomicReferenceArray2;
            this.f19374b = i8;
        }

        V h(Object obj, int i8) {
            try {
                E k8 = k(obj, i8);
                if (k8 == null) {
                    return null;
                }
                V v8 = (V) k8.getValue();
                if (v8 == null) {
                    G();
                }
                return v8;
            } finally {
                r();
            }
        }

        E i(Object obj, int i8) {
            if (this.f19374b != 0) {
                for (E j8 = j(i8); j8 != null; j8 = (E) j8.a()) {
                    if (j8.c() == i8) {
                        Object key = j8.getKey();
                        if (key == null) {
                            G();
                        } else if (this.f19373a.f19343e.d(obj, key)) {
                            return j8;
                        }
                    }
                }
                return null;
            }
            return null;
        }

        E j(int i8) {
            AtomicReferenceArray<E> atomicReferenceArray = this.f19377e;
            return atomicReferenceArray.get(i8 & (atomicReferenceArray.length() - 1));
        }

        E k(Object obj, int i8) {
            return i(obj, i8);
        }

        V l(E e8) {
            if (e8.getKey() == null) {
                G();
                return null;
            }
            V v8 = (V) e8.getValue();
            if (v8 == null) {
                G();
                return null;
            }
            return v8;
        }

        void m(AtomicReferenceArray<E> atomicReferenceArray) {
            int length = (atomicReferenceArray.length() * 3) / 4;
            this.f19376d = length;
            if (length == this.f19378f) {
                this.f19376d = length + 1;
            }
            this.f19377e = atomicReferenceArray;
        }

        void o() {
        }

        void p() {
        }

        AtomicReferenceArray<E> q(int i8) {
            return new AtomicReferenceArray<>(i8);
        }

        void r() {
            if ((this.f19379g.incrementAndGet() & 63) == 0) {
                B();
            }
        }

        void s() {
            D();
        }

        /* JADX WARN: Multi-variable type inference failed */
        V t(K k8, int i8, V v8, boolean z4) {
            lock();
            try {
                s();
                int i9 = this.f19374b + 1;
                if (i9 > this.f19376d) {
                    g();
                    i9 = this.f19374b + 1;
                }
                AtomicReferenceArray<E> atomicReferenceArray = this.f19377e;
                int length = (atomicReferenceArray.length() - 1) & i8;
                E e8 = atomicReferenceArray.get(length);
                for (i iVar = e8; iVar != null; iVar = iVar.a()) {
                    Object key = iVar.getKey();
                    if (iVar.c() == i8 && key != null && this.f19373a.f19343e.d(k8, key)) {
                        V v9 = (V) iVar.getValue();
                        if (v9 == null) {
                            this.f19375c++;
                            F(iVar, v8);
                            this.f19374b = this.f19374b;
                            return null;
                        } else if (z4) {
                            return v9;
                        } else {
                            this.f19375c++;
                            F(iVar, v8);
                            return v9;
                        }
                    }
                }
                this.f19375c++;
                E f5 = this.f19373a.f19344f.f(E(), k8, i8, e8);
                F(f5, v8);
                atomicReferenceArray.set(length, f5);
                this.f19374b = i9;
                return null;
            } finally {
                unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        boolean u(E e8, int i8) {
            lock();
            try {
                AtomicReferenceArray<E> atomicReferenceArray = this.f19377e;
                int length = i8 & (atomicReferenceArray.length() - 1);
                E e9 = atomicReferenceArray.get(length);
                for (i iVar = e9; iVar != null; iVar = iVar.a()) {
                    if (iVar == e8) {
                        this.f19375c++;
                        atomicReferenceArray.set(length, y(e9, iVar));
                        this.f19374b--;
                        return true;
                    }
                }
                return false;
            } finally {
                unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        boolean v(K k8, int i8, b0<K, V, E> b0Var) {
            lock();
            try {
                AtomicReferenceArray<E> atomicReferenceArray = this.f19377e;
                int length = (atomicReferenceArray.length() - 1) & i8;
                E e8 = atomicReferenceArray.get(length);
                for (i iVar = e8; iVar != null; iVar = iVar.a()) {
                    Object key = iVar.getKey();
                    if (iVar.c() == i8 && key != null && this.f19373a.f19343e.d(k8, key)) {
                        if (((a0) iVar).b() == b0Var) {
                            this.f19375c++;
                            atomicReferenceArray.set(length, y(e8, iVar));
                            this.f19374b--;
                            return true;
                        }
                        return false;
                    }
                }
                return false;
            } finally {
                unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        V w(Object obj, int i8) {
            lock();
            try {
                s();
                AtomicReferenceArray<E> atomicReferenceArray = this.f19377e;
                int length = (atomicReferenceArray.length() - 1) & i8;
                E e8 = atomicReferenceArray.get(length);
                for (i iVar = e8; iVar != null; iVar = iVar.a()) {
                    Object key = iVar.getKey();
                    if (iVar.c() == i8 && key != null && this.f19373a.f19343e.d(obj, key)) {
                        V v8 = (V) iVar.getValue();
                        if (v8 == null && !n(iVar)) {
                            return null;
                        }
                        this.f19375c++;
                        atomicReferenceArray.set(length, y(e8, iVar));
                        this.f19374b--;
                        return v8;
                    }
                }
                return null;
            } finally {
                unlock();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x003d, code lost:
            if (r8.f19373a.o().d(r11, r4.getValue()) == false) goto L20;
         */
        /* JADX WARN: Code restructure failed: missing block: B:13:0x003f, code lost:
            r5 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:15:0x0045, code lost:
            if (n(r4) == false) goto L22;
         */
        /* JADX WARN: Code restructure failed: missing block: B:16:0x0047, code lost:
            r8.f19375c++;
            r0.set(r1, y(r3, r4));
            r8.f19374b--;
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x005b, code lost:
            return r5;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x005f, code lost:
            return false;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        boolean x(java.lang.Object r9, int r10, java.lang.Object r11) {
            /*
                r8 = this;
                r8.lock()
                r8.s()     // Catch: java.lang.Throwable -> L69
                java.util.concurrent.atomic.AtomicReferenceArray<E extends com.google.common.collect.l1$i<K, V, E>> r0 = r8.f19377e     // Catch: java.lang.Throwable -> L69
                int r1 = r0.length()     // Catch: java.lang.Throwable -> L69
                r2 = 1
                int r1 = r1 - r2
                r1 = r1 & r10
                java.lang.Object r3 = r0.get(r1)     // Catch: java.lang.Throwable -> L69
                com.google.common.collect.l1$i r3 = (com.google.common.collect.l1.i) r3     // Catch: java.lang.Throwable -> L69
                r4 = r3
            L16:
                r5 = 0
                if (r4 == 0) goto L65
                java.lang.Object r6 = r4.getKey()     // Catch: java.lang.Throwable -> L69
                int r7 = r4.c()     // Catch: java.lang.Throwable -> L69
                if (r7 != r10) goto L60
                if (r6 == 0) goto L60
                com.google.common.collect.l1<K, V, E extends com.google.common.collect.l1$i<K, V, E>, S extends com.google.common.collect.l1$n<K, V, E, S>> r7 = r8.f19373a     // Catch: java.lang.Throwable -> L69
                com.google.common.base.Equivalence<java.lang.Object> r7 = r7.f19343e     // Catch: java.lang.Throwable -> L69
                boolean r6 = r7.d(r9, r6)     // Catch: java.lang.Throwable -> L69
                if (r6 == 0) goto L60
                java.lang.Object r9 = r4.getValue()     // Catch: java.lang.Throwable -> L69
                com.google.common.collect.l1<K, V, E extends com.google.common.collect.l1$i<K, V, E>, S extends com.google.common.collect.l1$n<K, V, E, S>> r10 = r8.f19373a     // Catch: java.lang.Throwable -> L69
                com.google.common.base.Equivalence r10 = r10.o()     // Catch: java.lang.Throwable -> L69
                boolean r9 = r10.d(r11, r9)     // Catch: java.lang.Throwable -> L69
                if (r9 == 0) goto L41
                r5 = r2
                goto L47
            L41:
                boolean r9 = n(r4)     // Catch: java.lang.Throwable -> L69
                if (r9 == 0) goto L5c
            L47:
                int r9 = r8.f19375c     // Catch: java.lang.Throwable -> L69
                int r9 = r9 + r2
                r8.f19375c = r9     // Catch: java.lang.Throwable -> L69
                com.google.common.collect.l1$i r9 = r8.y(r3, r4)     // Catch: java.lang.Throwable -> L69
                int r10 = r8.f19374b     // Catch: java.lang.Throwable -> L69
                int r10 = r10 - r2
                r0.set(r1, r9)     // Catch: java.lang.Throwable -> L69
                r8.f19374b = r10     // Catch: java.lang.Throwable -> L69
                r8.unlock()
                return r5
            L5c:
                r8.unlock()
                return r5
            L60:
                com.google.common.collect.l1$i r4 = r4.a()     // Catch: java.lang.Throwable -> L69
                goto L16
            L65:
                r8.unlock()
                return r5
            L69:
                r9 = move-exception
                r8.unlock()
                throw r9
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.l1.n.x(java.lang.Object, int, java.lang.Object):boolean");
        }

        E y(E e8, E e9) {
            int i8 = this.f19374b;
            E e10 = (E) e9.a();
            while (e8 != e9) {
                E d8 = d(e8, e10);
                if (d8 != null) {
                    e10 = d8;
                } else {
                    i8--;
                }
                e8 = (E) e8.a();
            }
            this.f19374b = i8;
            return e10;
        }

        /* JADX WARN: Multi-variable type inference failed */
        V z(K k8, int i8, V v8) {
            lock();
            try {
                s();
                AtomicReferenceArray<E> atomicReferenceArray = this.f19377e;
                int length = (atomicReferenceArray.length() - 1) & i8;
                E e8 = atomicReferenceArray.get(length);
                for (i iVar = e8; iVar != null; iVar = iVar.a()) {
                    Object key = iVar.getKey();
                    if (iVar.c() == i8 && key != null && this.f19373a.f19343e.d(k8, key)) {
                        V v9 = (V) iVar.getValue();
                        if (v9 != null) {
                            this.f19375c++;
                            F(iVar, v8);
                            return v9;
                        }
                        if (n(iVar)) {
                            this.f19375c++;
                            atomicReferenceArray.set(length, y(e8, iVar));
                            this.f19374b--;
                        }
                        return null;
                    }
                }
                return null;
            } finally {
                unlock();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class o<K, V> extends b<K, V> {
        private static final long serialVersionUID = 3;

        o(p pVar, p pVar2, Equivalence<Object> equivalence, Equivalence<Object> equivalence2, int i8, ConcurrentMap<K, V> concurrentMap) {
            super(pVar, pVar2, equivalence, equivalence2, i8, concurrentMap);
        }

        private void readObject(ObjectInputStream objectInputStream) {
            objectInputStream.defaultReadObject();
            this.f19353f = u(objectInputStream).i();
            t(objectInputStream);
        }

        private Object readResolve() {
            return this.f19353f;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) {
            objectOutputStream.defaultWriteObject();
            v(objectOutputStream);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class p {

        /* renamed from: a  reason: collision with root package name */
        public static final p f19380a = new a("STRONG", 0);

        /* renamed from: b  reason: collision with root package name */
        public static final p f19381b = new b("WEAK", 1);

        /* renamed from: c  reason: collision with root package name */
        private static final /* synthetic */ p[] f19382c = c();

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum a extends p {
            a(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.collect.l1.p
            Equivalence<Object> f() {
                return Equivalence.c();
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum b extends p {
            b(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.collect.l1.p
            Equivalence<Object> f() {
                return Equivalence.f();
            }
        }

        private p(String str, int i8) {
        }

        /* synthetic */ p(String str, int i8, a aVar) {
            this(str, i8);
        }

        private static /* synthetic */ p[] c() {
            return new p[]{f19380a, f19381b};
        }

        public static p valueOf(String str) {
            return (p) Enum.valueOf(p.class, str);
        }

        public static p[] values() {
            return (p[]) f19382c.clone();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract Equivalence<Object> f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class q<K, V> extends c<K, V, q<K, V>> {

        /* renamed from: d  reason: collision with root package name */
        private volatile V f19383d;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a<K, V> implements j<K, V, q<K, V>, r<K, V>> {

            /* renamed from: a  reason: collision with root package name */
            private static final a<?, ?> f19384a = new a<>();

            a() {
            }

            static <K, V> a<K, V> h() {
                return (a<K, V>) f19384a;
            }

            @Override // com.google.common.collect.l1.j
            public p b() {
                return p.f19380a;
            }

            @Override // com.google.common.collect.l1.j
            public p c() {
                return p.f19380a;
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: g */
            public q<K, V> a(r<K, V> rVar, q<K, V> qVar, q<K, V> qVar2) {
                return qVar.d(qVar2);
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: i */
            public q<K, V> f(r<K, V> rVar, K k8, int i8, q<K, V> qVar) {
                return new q<>(k8, i8, qVar);
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: j */
            public r<K, V> e(l1<K, V, q<K, V>, r<K, V>> l1Var, int i8, int i9) {
                return new r<>(l1Var, i8, i9);
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: k */
            public void d(r<K, V> rVar, q<K, V> qVar, V v8) {
                qVar.e(v8);
            }
        }

        q(K k8, int i8, q<K, V> qVar) {
            super(k8, i8, qVar);
            this.f19383d = null;
        }

        q<K, V> d(q<K, V> qVar) {
            q<K, V> qVar2 = new q<>(this.f19354a, this.f19355b, qVar);
            qVar2.f19383d = this.f19383d;
            return qVar2;
        }

        void e(V v8) {
            this.f19383d = v8;
        }

        @Override // com.google.common.collect.l1.i
        public V getValue() {
            return this.f19383d;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class r<K, V> extends n<K, V, q<K, V>, r<K, V>> {
        r(l1<K, V, q<K, V>, r<K, V>> l1Var, int i8, int i9) {
            super(l1Var, i8, i9);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.l1.n
        /* renamed from: H */
        public r<K, V> E() {
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class s<K, V> extends c<K, V, s<K, V>> implements a0<K, V, s<K, V>> {

        /* renamed from: d  reason: collision with root package name */
        private volatile b0<K, V, s<K, V>> f19385d;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a<K, V> implements j<K, V, s<K, V>, t<K, V>> {

            /* renamed from: a  reason: collision with root package name */
            private static final a<?, ?> f19386a = new a<>();

            a() {
            }

            static <K, V> a<K, V> h() {
                return (a<K, V>) f19386a;
            }

            @Override // com.google.common.collect.l1.j
            public p b() {
                return p.f19380a;
            }

            @Override // com.google.common.collect.l1.j
            public p c() {
                return p.f19381b;
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: g */
            public s<K, V> a(t<K, V> tVar, s<K, V> sVar, s<K, V> sVar2) {
                if (n.n(sVar)) {
                    return null;
                }
                return sVar.d(((t) tVar).f19387h, sVar2);
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: i */
            public s<K, V> f(t<K, V> tVar, K k8, int i8, s<K, V> sVar) {
                return new s<>(k8, i8, sVar);
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: j */
            public t<K, V> e(l1<K, V, s<K, V>, t<K, V>> l1Var, int i8, int i9) {
                return new t<>(l1Var, i8, i9);
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: k */
            public void d(t<K, V> tVar, s<K, V> sVar, V v8) {
                sVar.e(v8, ((t) tVar).f19387h);
            }
        }

        s(K k8, int i8, s<K, V> sVar) {
            super(k8, i8, sVar);
            this.f19385d = l1.n();
        }

        @Override // com.google.common.collect.l1.a0
        public b0<K, V, s<K, V>> b() {
            return this.f19385d;
        }

        s<K, V> d(ReferenceQueue<V> referenceQueue, s<K, V> sVar) {
            s<K, V> sVar2 = new s<>(this.f19354a, this.f19355b, sVar);
            sVar2.f19385d = this.f19385d.b(referenceQueue, sVar2);
            return sVar2;
        }

        void e(V v8, ReferenceQueue<V> referenceQueue) {
            b0<K, V, s<K, V>> b0Var = this.f19385d;
            this.f19385d = new c0(referenceQueue, v8, this);
            b0Var.clear();
        }

        @Override // com.google.common.collect.l1.i
        public V getValue() {
            return this.f19385d.get();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class t<K, V> extends n<K, V, s<K, V>, t<K, V>> {

        /* renamed from: h  reason: collision with root package name */
        private final ReferenceQueue<V> f19387h;

        t(l1<K, V, s<K, V>, t<K, V>> l1Var, int i8, int i9) {
            super(l1Var, i8, i9);
            this.f19387h = new ReferenceQueue<>();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.l1.n
        /* renamed from: I */
        public t<K, V> E() {
            return this;
        }

        @Override // com.google.common.collect.l1.n
        void o() {
            b((ReferenceQueue<V>) this.f19387h);
        }

        @Override // com.google.common.collect.l1.n
        void p() {
            f(this.f19387h);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class u extends l1<K, V, E, S>.h<V> {
        u(l1 l1Var) {
            super();
        }

        @Override // java.util.Iterator
        public V next() {
            return c().getValue();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class v extends AbstractCollection<V> {
        v() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            l1.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return l1.this.containsValue(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return l1.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return new u(l1.this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return l1.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public Object[] toArray() {
            return l1.m(this).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) l1.m(this).toArray(tArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class w<K, V> extends d<K, V, w<K, V>> {

        /* renamed from: c  reason: collision with root package name */
        private volatile V f19389c;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a<K, V> implements j<K, V, w<K, V>, x<K, V>> {

            /* renamed from: a  reason: collision with root package name */
            private static final a<?, ?> f19390a = new a<>();

            a() {
            }

            static <K, V> a<K, V> h() {
                return (a<K, V>) f19390a;
            }

            @Override // com.google.common.collect.l1.j
            public p b() {
                return p.f19381b;
            }

            @Override // com.google.common.collect.l1.j
            public p c() {
                return p.f19380a;
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: g */
            public w<K, V> a(x<K, V> xVar, w<K, V> wVar, w<K, V> wVar2) {
                if (wVar.getKey() == null) {
                    return null;
                }
                return wVar.d(((x) xVar).f19391h, wVar2);
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: i */
            public w<K, V> f(x<K, V> xVar, K k8, int i8, w<K, V> wVar) {
                return new w<>(((x) xVar).f19391h, k8, i8, wVar);
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: j */
            public x<K, V> e(l1<K, V, w<K, V>, x<K, V>> l1Var, int i8, int i9) {
                return new x<>(l1Var, i8, i9);
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: k */
            public void d(x<K, V> xVar, w<K, V> wVar, V v8) {
                wVar.e(v8);
            }
        }

        w(ReferenceQueue<K> referenceQueue, K k8, int i8, w<K, V> wVar) {
            super(referenceQueue, k8, i8, wVar);
            this.f19389c = null;
        }

        w<K, V> d(ReferenceQueue<K> referenceQueue, w<K, V> wVar) {
            w<K, V> wVar2 = new w<>(referenceQueue, getKey(), this.f19358a, wVar);
            wVar2.e(this.f19389c);
            return wVar2;
        }

        void e(V v8) {
            this.f19389c = v8;
        }

        @Override // com.google.common.collect.l1.i
        public V getValue() {
            return this.f19389c;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class x<K, V> extends n<K, V, w<K, V>, x<K, V>> {

        /* renamed from: h  reason: collision with root package name */
        private final ReferenceQueue<K> f19391h;

        x(l1<K, V, w<K, V>, x<K, V>> l1Var, int i8, int i9) {
            super(l1Var, i8, i9);
            this.f19391h = new ReferenceQueue<>();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.l1.n
        /* renamed from: I */
        public x<K, V> E() {
            return this;
        }

        @Override // com.google.common.collect.l1.n
        void o() {
            b((ReferenceQueue<K>) this.f19391h);
        }

        @Override // com.google.common.collect.l1.n
        void p() {
            e(this.f19391h);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class y<K, V> extends d<K, V, y<K, V>> implements a0<K, V, y<K, V>> {

        /* renamed from: c  reason: collision with root package name */
        private volatile b0<K, V, y<K, V>> f19392c;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a<K, V> implements j<K, V, y<K, V>, z<K, V>> {

            /* renamed from: a  reason: collision with root package name */
            private static final a<?, ?> f19393a = new a<>();

            a() {
            }

            static <K, V> a<K, V> h() {
                return (a<K, V>) f19393a;
            }

            @Override // com.google.common.collect.l1.j
            public p b() {
                return p.f19381b;
            }

            @Override // com.google.common.collect.l1.j
            public p c() {
                return p.f19381b;
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: g */
            public y<K, V> a(z<K, V> zVar, y<K, V> yVar, y<K, V> yVar2) {
                if (yVar.getKey() == null || n.n(yVar)) {
                    return null;
                }
                return yVar.d(((z) zVar).f19394h, ((z) zVar).f19395j, yVar2);
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: i */
            public y<K, V> f(z<K, V> zVar, K k8, int i8, y<K, V> yVar) {
                return new y<>(((z) zVar).f19394h, k8, i8, yVar);
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: j */
            public z<K, V> e(l1<K, V, y<K, V>, z<K, V>> l1Var, int i8, int i9) {
                return new z<>(l1Var, i8, i9);
            }

            @Override // com.google.common.collect.l1.j
            /* renamed from: k */
            public void d(z<K, V> zVar, y<K, V> yVar, V v8) {
                yVar.e(v8, ((z) zVar).f19395j);
            }
        }

        y(ReferenceQueue<K> referenceQueue, K k8, int i8, y<K, V> yVar) {
            super(referenceQueue, k8, i8, yVar);
            this.f19392c = l1.n();
        }

        @Override // com.google.common.collect.l1.a0
        public b0<K, V, y<K, V>> b() {
            return this.f19392c;
        }

        y<K, V> d(ReferenceQueue<K> referenceQueue, ReferenceQueue<V> referenceQueue2, y<K, V> yVar) {
            y<K, V> yVar2 = new y<>(referenceQueue, getKey(), this.f19358a, yVar);
            yVar2.f19392c = this.f19392c.b(referenceQueue2, yVar2);
            return yVar2;
        }

        void e(V v8, ReferenceQueue<V> referenceQueue) {
            b0<K, V, y<K, V>> b0Var = this.f19392c;
            this.f19392c = new c0(referenceQueue, v8, this);
            b0Var.clear();
        }

        @Override // com.google.common.collect.l1.i
        public V getValue() {
            return this.f19392c.get();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class z<K, V> extends n<K, V, y<K, V>, z<K, V>> {

        /* renamed from: h  reason: collision with root package name */
        private final ReferenceQueue<K> f19394h;

        /* renamed from: j  reason: collision with root package name */
        private final ReferenceQueue<V> f19395j;

        z(l1<K, V, y<K, V>, z<K, V>> l1Var, int i8, int i9) {
            super(l1Var, i8, i9);
            this.f19394h = new ReferenceQueue<>();
            this.f19395j = new ReferenceQueue<>();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.l1.n
        /* renamed from: J */
        public z<K, V> E() {
            return this;
        }

        @Override // com.google.common.collect.l1.n
        void o() {
            b((ReferenceQueue<K>) this.f19394h);
        }

        @Override // com.google.common.collect.l1.n
        void p() {
            e(this.f19394h);
            f(this.f19395j);
        }
    }

    private l1(k1 k1Var, j<K, V, E, S> jVar) {
        this.f19342d = Math.min(k1Var.b(), 65536);
        this.f19343e = k1Var.d();
        this.f19344f = jVar;
        int min = Math.min(k1Var.c(), 1073741824);
        int i8 = 0;
        int i9 = 1;
        int i10 = 0;
        int i11 = 1;
        while (i11 < this.f19342d) {
            i10++;
            i11 <<= 1;
        }
        this.f19340b = 32 - i10;
        this.f19339a = i11 - 1;
        this.f19341c = h(i11);
        int i12 = min / i11;
        while (i9 < (i11 * i12 < min ? i12 + 1 : i12)) {
            i9 <<= 1;
        }
        while (true) {
            n<K, V, E, S>[] nVarArr = this.f19341c;
            if (i8 >= nVarArr.length) {
                return;
            }
            nVarArr[i8] = c(i9, -1);
            i8++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> l1<K, V, ? extends i<K, V, ?>, ?> b(k1 k1Var) {
        p e8 = k1Var.e();
        p pVar = p.f19380a;
        if (e8 == pVar && k1Var.f() == pVar) {
            return new l1<>(k1Var, q.a.h());
        }
        if (k1Var.e() == pVar && k1Var.f() == p.f19381b) {
            return new l1<>(k1Var, s.a.h());
        }
        p e9 = k1Var.e();
        p pVar2 = p.f19381b;
        if (e9 == pVar2 && k1Var.f() == pVar) {
            return new l1<>(k1Var, w.a.h());
        }
        if (k1Var.e() == pVar2 && k1Var.f() == pVar2) {
            return new l1<>(k1Var, y.a.h());
        }
        throw new AssertionError();
    }

    static int k(int i8) {
        int i9 = i8 + ((i8 << 15) ^ (-12931));
        int i10 = i9 ^ (i9 >>> 10);
        int i11 = i10 + (i10 << 3);
        int i12 = i11 ^ (i11 >>> 6);
        int i13 = i12 + (i12 << 2) + (i12 << 14);
        return i13 ^ (i13 >>> 16);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <E> ArrayList<E> m(Collection<E> collection) {
        ArrayList<E> arrayList = new ArrayList<>(collection.size());
        g1.a(arrayList, collection.iterator());
        return arrayList;
    }

    static <K, V, E extends i<K, V, E>> b0<K, V, E> n() {
        return (b0<K, V, E>) f19338k;
    }

    n<K, V, E, S> c(int i8, int i9) {
        return (S) this.f19344f.e(this, i8, i9);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        for (n<K, V, E, S> nVar : this.f19341c) {
            nVar.a();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        if (obj == null) {
            return false;
        }
        int f5 = f(obj);
        return l(f5).c(obj, f5);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v0 */
    /* JADX WARN: Type inference failed for: r10v1, types: [int] */
    /* JADX WARN: Type inference failed for: r11v0, types: [com.google.common.collect.l1$n] */
    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v1, types: [int] */
    /* JADX WARN: Type inference failed for: r3v0, types: [com.google.common.collect.l1$n<K, V, E extends com.google.common.collect.l1$i<K, V, E>, S extends com.google.common.collect.l1$n<K, V, E, S>>[]] */
    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        boolean z4 = false;
        if (obj == null) {
            return false;
        }
        n<K, V, E, S>[] nVarArr = this.f19341c;
        long j8 = -1;
        int i8 = 0;
        while (i8 < 3) {
            long j9 = 0;
            int length = nVarArr.length;
            for (int i9 = z4; i9 < length; i9++) {
                ?? r11 = nVarArr[i9];
                int i10 = r11.f19374b;
                AtomicReferenceArray<E> atomicReferenceArray = r11.f19377e;
                for (int i11 = z4; i11 < atomicReferenceArray.length(); i11++) {
                    for (E e8 = atomicReferenceArray.get(i11); e8 != null; e8 = e8.a()) {
                        Object l8 = r11.l(e8);
                        if (l8 != null && o().d(obj, l8)) {
                            return true;
                        }
                    }
                }
                j9 += r11.f19375c;
                z4 = false;
            }
            if (j9 == j8) {
                return false;
            }
            i8++;
            j8 = j9;
            z4 = false;
        }
        return z4;
    }

    V d(E e8) {
        if (e8.getKey() == null) {
            return null;
        }
        return (V) e8.getValue();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.f19347j;
        if (set != null) {
            return set;
        }
        g gVar = new g();
        this.f19347j = gVar;
        return gVar;
    }

    int f(Object obj) {
        return k(this.f19343e.e(obj));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        if (obj == null) {
            return null;
        }
        int f5 = f(obj);
        return l(f5).h(obj, f5);
    }

    final n<K, V, E, S>[] h(int i8) {
        return new n[i8];
    }

    void i(E e8) {
        int c9 = e8.c();
        l(c9).u(e8, c9);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        n<K, V, E, S>[] nVarArr = this.f19341c;
        long j8 = 0;
        for (int i8 = 0; i8 < nVarArr.length; i8++) {
            if (nVarArr[i8].f19374b != 0) {
                return false;
            }
            j8 += nVarArr[i8].f19375c;
        }
        if (j8 != 0) {
            for (int i9 = 0; i9 < nVarArr.length; i9++) {
                if (nVarArr[i9].f19374b != 0) {
                    return false;
                }
                j8 -= nVarArr[i9].f19375c;
            }
            return j8 == 0;
        }
        return true;
    }

    void j(b0<K, V, E> b0Var) {
        E a9 = b0Var.a();
        int c9 = a9.c();
        l(c9).v((K) a9.getKey(), c9, b0Var);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> set = this.f19345g;
        if (set != null) {
            return set;
        }
        l lVar = new l();
        this.f19345g = lVar;
        return lVar;
    }

    n<K, V, E, S> l(int i8) {
        return this.f19341c[(i8 >>> this.f19340b) & this.f19339a];
    }

    Equivalence<Object> o() {
        return this.f19344f.c().f();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k8, V v8) {
        com.google.common.base.l.n(k8);
        com.google.common.base.l.n(v8);
        int f5 = f(k8);
        return l(f5).t(k8, f5, v8, false);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V putIfAbsent(K k8, V v8) {
        com.google.common.base.l.n(k8);
        com.google.common.base.l.n(v8);
        int f5 = f(k8);
        return l(f5).t(k8, f5, v8, true);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        if (obj == null) {
            return null;
        }
        int f5 = f(obj);
        return l(f5).w(obj, f5);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean remove(Object obj, Object obj2) {
        if (obj == null || obj2 == null) {
            return false;
        }
        int f5 = f(obj);
        return l(f5).x(obj, f5, obj2);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V replace(K k8, V v8) {
        com.google.common.base.l.n(k8);
        com.google.common.base.l.n(v8);
        int f5 = f(k8);
        return l(f5).z(k8, f5, v8);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean replace(K k8, V v8, V v9) {
        com.google.common.base.l.n(k8);
        com.google.common.base.l.n(v9);
        if (v8 == null) {
            return false;
        }
        int f5 = f(k8);
        return l(f5).A(k8, f5, v8, v9);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        long j8 = 0;
        for (n<K, V, E, S> nVar : this.f19341c) {
            j8 += nVar.f19374b;
        }
        return com.google.common.primitives.g.k(j8);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Collection<V> collection = this.f19346h;
        if (collection != null) {
            return collection;
        }
        v vVar = new v();
        this.f19346h = vVar;
        return vVar;
    }

    Object writeReplace() {
        return new o(this.f19344f.b(), this.f19344f.c(), this.f19343e, this.f19344f.c().f(), this.f19342d, this);
    }
}
