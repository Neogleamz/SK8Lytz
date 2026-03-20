package com.google.common.collect;

import com.google.common.collect.m2;
import com.google.common.collect.r1;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ConcurrentHashMultiset<E> extends i<E> implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: c  reason: collision with root package name */
    private final transient ConcurrentMap<E, AtomicInteger> f18889c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends r0<E> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Set f18890a;

        a(ConcurrentHashMultiset concurrentHashMultiset, Set set) {
            this.f18890a = set;
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return obj != null && u.c(this.f18890a, obj);
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> collection) {
            return n(collection);
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return obj != null && u.d(this.f18890a, obj);
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> collection) {
            return x(collection);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.r0, com.google.common.collect.j0
        /* renamed from: v */
        public Set<E> i() {
            return this.f18890a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends com.google.common.collect.c<r1.a<E>> {

        /* renamed from: c  reason: collision with root package name */
        private final Iterator<Map.Entry<E, AtomicInteger>> f18891c;

        b() {
            this.f18891c = ConcurrentHashMultiset.this.f18889c.entrySet().iterator();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.c
        /* renamed from: d */
        public r1.a<E> a() {
            while (this.f18891c.hasNext()) {
                Map.Entry<E, AtomicInteger> next = this.f18891c.next();
                int i8 = next.getValue().get();
                if (i8 != 0) {
                    return s1.g(next.getKey(), i8);
                }
            }
            return b();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends l0<r1.a<E>> {

        /* renamed from: a  reason: collision with root package name */
        private r1.a<E> f18893a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Iterator f18894b;

        c(Iterator it) {
            this.f18894b = it;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.p0
        /* renamed from: i */
        public Iterator<r1.a<E>> h() {
            return this.f18894b;
        }

        @Override // com.google.common.collect.l0, java.util.Iterator
        /* renamed from: k */
        public r1.a<E> next() {
            r1.a<E> aVar = (r1.a) super.next();
            this.f18893a = aVar;
            return aVar;
        }

        @Override // java.util.Iterator
        public void remove() {
            com.google.common.base.l.t(this.f18893a != null, "no calls to next() since the last call to remove()");
            ConcurrentHashMultiset.this.U(this.f18893a.a(), 0);
            this.f18893a = null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class d extends i<E>.b {
        private d() {
            super();
        }

        /* synthetic */ d(ConcurrentHashMultiset concurrentHashMultiset, a aVar) {
            this();
        }

        private List<r1.a<E>> p() {
            ArrayList l8 = j1.l(size());
            g1.a(l8, iterator());
            return l8;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.i.b, com.google.common.collect.s1.d
        /* renamed from: n */
        public ConcurrentHashMultiset<E> k() {
            return ConcurrentHashMultiset.this;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return p().toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] tArr) {
            return (T[]) p().toArray(tArr);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class e {

        /* renamed from: a  reason: collision with root package name */
        static final m2.b<ConcurrentHashMultiset> f18897a = m2.a(ConcurrentHashMultiset.class, "countMap");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private List<E> p() {
        ArrayList l8 = j1.l(size());
        for (r1.a aVar : entrySet()) {
            Object a9 = aVar.a();
            for (int count = aVar.getCount(); count > 0; count--) {
                l8.add(a9);
            }
        }
        return l8;
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        e.f18897a.b(this, (ConcurrentMap) objectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.f18889c);
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public int B(Object obj, int i8) {
        int i9;
        int max;
        if (i8 == 0) {
            return m0(obj);
        }
        t.c(i8, "occurrences");
        AtomicInteger atomicInteger = (AtomicInteger) m1.o(this.f18889c, obj);
        if (atomicInteger == null) {
            return 0;
        }
        do {
            i9 = atomicInteger.get();
            if (i9 == 0) {
                return 0;
            }
            max = Math.max(0, i9 - i8);
        } while (!atomicInteger.compareAndSet(i9, max));
        if (max == 0) {
            this.f18889c.remove(obj, atomicInteger);
        }
        return i9;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x005c, code lost:
        r2 = new java.util.concurrent.atomic.AtomicInteger(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0067, code lost:
        if (r4.f18889c.putIfAbsent(r5, r2) == null) goto L31;
     */
    @Override // com.google.common.collect.i, com.google.common.collect.r1
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int J(E r5, int r6) {
        /*
            r4 = this;
            com.google.common.base.l.n(r5)
            if (r6 != 0) goto La
            int r5 = r4.m0(r5)
            return r5
        La:
            java.lang.String r0 = "occurrences"
            com.google.common.collect.t.c(r6, r0)
        Lf:
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r0 = r4.f18889c
            java.lang.Object r0 = com.google.common.collect.m1.o(r0, r5)
            java.util.concurrent.atomic.AtomicInteger r0 = (java.util.concurrent.atomic.AtomicInteger) r0
            r1 = 0
            if (r0 != 0) goto L2a
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r0 = r4.f18889c
            java.util.concurrent.atomic.AtomicInteger r2 = new java.util.concurrent.atomic.AtomicInteger
            r2.<init>(r6)
            java.lang.Object r0 = r0.putIfAbsent(r5, r2)
            java.util.concurrent.atomic.AtomicInteger r0 = (java.util.concurrent.atomic.AtomicInteger) r0
            if (r0 != 0) goto L2a
            return r1
        L2a:
            int r2 = r0.get()
            if (r2 == 0) goto L5c
            int r3 = b8.b.a(r2, r6)     // Catch: java.lang.ArithmeticException -> L3b
            boolean r3 = r0.compareAndSet(r2, r3)     // Catch: java.lang.ArithmeticException -> L3b
            if (r3 == 0) goto L2a
            return r2
        L3b:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            r0 = 65
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>(r0)
            java.lang.String r0 = "Overflow adding "
            r1.append(r0)
            r1.append(r6)
            java.lang.String r6 = " occurrences to a count of "
            r1.append(r6)
            r1.append(r2)
            java.lang.String r6 = r1.toString()
            r5.<init>(r6)
            throw r5
        L5c:
            java.util.concurrent.atomic.AtomicInteger r2 = new java.util.concurrent.atomic.AtomicInteger
            r2.<init>(r6)
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r3 = r4.f18889c
            java.lang.Object r3 = r3.putIfAbsent(r5, r2)
            if (r3 == 0) goto L71
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r3 = r4.f18889c
            boolean r0 = r3.replace(r5, r0, r2)
            if (r0 == 0) goto Lf
        L71:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ConcurrentHashMultiset.J(java.lang.Object, int):int");
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x002c, code lost:
        if (r6 != 0) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x002e, code lost:
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x002f, code lost:
        r2 = new java.util.concurrent.atomic.AtomicInteger(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x003a, code lost:
        if (r4.f18889c.putIfAbsent(r5, r2) == null) goto L29;
     */
    @Override // com.google.common.collect.i, com.google.common.collect.r1
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int U(E r5, int r6) {
        /*
            r4 = this;
            com.google.common.base.l.n(r5)
            java.lang.String r0 = "count"
            com.google.common.collect.t.b(r6, r0)
        L8:
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r0 = r4.f18889c
            java.lang.Object r0 = com.google.common.collect.m1.o(r0, r5)
            java.util.concurrent.atomic.AtomicInteger r0 = (java.util.concurrent.atomic.AtomicInteger) r0
            r1 = 0
            if (r0 != 0) goto L26
            if (r6 != 0) goto L16
            return r1
        L16:
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r0 = r4.f18889c
            java.util.concurrent.atomic.AtomicInteger r2 = new java.util.concurrent.atomic.AtomicInteger
            r2.<init>(r6)
            java.lang.Object r0 = r0.putIfAbsent(r5, r2)
            java.util.concurrent.atomic.AtomicInteger r0 = (java.util.concurrent.atomic.AtomicInteger) r0
            if (r0 != 0) goto L26
            return r1
        L26:
            int r2 = r0.get()
            if (r2 != 0) goto L45
            if (r6 != 0) goto L2f
            return r1
        L2f:
            java.util.concurrent.atomic.AtomicInteger r2 = new java.util.concurrent.atomic.AtomicInteger
            r2.<init>(r6)
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r3 = r4.f18889c
            java.lang.Object r3 = r3.putIfAbsent(r5, r2)
            if (r3 == 0) goto L44
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r3 = r4.f18889c
            boolean r0 = r3.replace(r5, r0, r2)
            if (r0 == 0) goto L8
        L44:
            return r1
        L45:
            boolean r3 = r0.compareAndSet(r2, r6)
            if (r3 == 0) goto L26
            if (r6 != 0) goto L52
            java.util.concurrent.ConcurrentMap<E, java.util.concurrent.atomic.AtomicInteger> r6 = r4.f18889c
            r6.remove(r5, r0)
        L52:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ConcurrentHashMultiset.U(java.lang.Object, int):int");
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public boolean Y(E e8, int i8, int i9) {
        com.google.common.base.l.n(e8);
        t.b(i8, "oldCount");
        t.b(i9, "newCount");
        AtomicInteger atomicInteger = (AtomicInteger) m1.o(this.f18889c, e8);
        if (atomicInteger == null) {
            if (i8 != 0) {
                return false;
            }
            return i9 == 0 || this.f18889c.putIfAbsent(e8, new AtomicInteger(i9)) == null;
        }
        int i10 = atomicInteger.get();
        if (i10 == i8) {
            if (i10 == 0) {
                if (i9 == 0) {
                    this.f18889c.remove(e8, atomicInteger);
                    return true;
                }
                AtomicInteger atomicInteger2 = new AtomicInteger(i9);
                return this.f18889c.putIfAbsent(e8, atomicInteger2) == null || this.f18889c.replace(e8, atomicInteger, atomicInteger2);
            } else if (atomicInteger.compareAndSet(i10, i9)) {
                if (i9 == 0) {
                    this.f18889c.remove(e8, atomicInteger);
                }
                return true;
            }
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public void clear() {
        this.f18889c.clear();
    }

    @Override // com.google.common.collect.i, java.util.AbstractCollection, java.util.Collection, com.google.common.collect.r1
    public /* bridge */ /* synthetic */ boolean contains(Object obj) {
        return super.contains(obj);
    }

    @Override // com.google.common.collect.i
    Set<E> e() {
        return new a(this, this.f18889c.keySet());
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.i
    @Deprecated
    public Set<r1.a<E>> g() {
        return new d(this, null);
    }

    @Override // com.google.common.collect.i
    int h() {
        return this.f18889c.size();
    }

    @Override // com.google.common.collect.i
    Iterator<E> i() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.i, java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.f18889c.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return s1.h(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.i
    public Iterator<r1.a<E>> k() {
        return new c(new b());
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public /* bridge */ /* synthetic */ Set l() {
        return super.l();
    }

    @Override // com.google.common.collect.r1
    public int m0(Object obj) {
        AtomicInteger atomicInteger = (AtomicInteger) m1.o(this.f18889c, obj);
        if (atomicInteger == null) {
            return 0;
        }
        return atomicInteger.get();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.r1
    public int size() {
        long j8 = 0;
        for (AtomicInteger atomicInteger : this.f18889c.values()) {
            j8 += atomicInteger.get();
        }
        return com.google.common.primitives.g.k(j8);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public Object[] toArray() {
        return p().toArray();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) p().toArray(tArr);
    }
}
