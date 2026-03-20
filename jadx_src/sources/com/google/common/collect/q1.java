package com.google.common.collect;

import com.google.common.collect.m1;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q1 {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a<K, V> extends m1.k<K, Collection<V>> {

        /* renamed from: d  reason: collision with root package name */
        private final n1<K, V> f19427d;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.google.common.collect.q1$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class C0154a extends m1.f<K, Collection<V>> {

            /* renamed from: com.google.common.collect.q1$a$a$a  reason: collision with other inner class name */
            /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
            class C0155a implements com.google.common.base.g<K, Collection<V>> {
                C0155a() {
                }

                @Override // com.google.common.base.g
                /* renamed from: a */
                public Collection<V> apply(K k8) {
                    return a.this.f19427d.get(k8);
                }
            }

            C0154a() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return m1.a(a.this.f19427d.keySet(), new C0155a());
            }

            @Override // com.google.common.collect.m1.f
            Map<K, Collection<V>> k() {
                return a.this;
            }

            @Override // com.google.common.collect.m1.f, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object obj) {
                if (contains(obj)) {
                    Map.Entry entry = (Map.Entry) obj;
                    Objects.requireNonNull(entry);
                    a.this.i(entry.getKey());
                    return true;
                }
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(n1<K, V> n1Var) {
            this.f19427d = (n1) com.google.common.base.l.n(n1Var);
        }

        @Override // com.google.common.collect.m1.k
        protected Set<Map.Entry<K, Collection<V>>> a() {
            return new C0154a();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            this.f19427d.clear();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object obj) {
            return this.f19427d.containsKey(obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        /* renamed from: f */
        public Collection<V> get(Object obj) {
            if (containsKey(obj)) {
                return this.f19427d.get(obj);
            }
            return null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        /* renamed from: h */
        public Collection<V> remove(Object obj) {
            if (containsKey(obj)) {
                return this.f19427d.a(obj);
            }
            return null;
        }

        void i(Object obj) {
            this.f19427d.keySet().remove(obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            return this.f19427d.isEmpty();
        }

        @Override // com.google.common.collect.m1.k, java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return this.f19427d.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.f19427d.keySet().size();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class b<K, V> extends d<K, V> {
        private static final long serialVersionUID = 0;

        /* renamed from: g  reason: collision with root package name */
        transient com.google.common.base.r<? extends List<V>> f19430g;

        b(Map<K, Collection<V>> map, com.google.common.base.r<? extends List<V>> rVar) {
            super(map);
            this.f19430g = (com.google.common.base.r) com.google.common.base.l.n(rVar);
        }

        private void readObject(ObjectInputStream objectInputStream) {
            objectInputStream.defaultReadObject();
            this.f19430g = (com.google.common.base.r) objectInputStream.readObject();
            A((Map) objectInputStream.readObject());
        }

        private void writeObject(ObjectOutputStream objectOutputStream) {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeObject(this.f19430g);
            objectOutputStream.writeObject(s());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.e
        /* renamed from: I */
        public List<V> t() {
            return this.f19430g.get();
        }

        @Override // com.google.common.collect.e, com.google.common.collect.h
        Map<K, Collection<V>> e() {
            return v();
        }

        @Override // com.google.common.collect.e, com.google.common.collect.h
        Set<K> g() {
            return w();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class c<K, V> extends AbstractCollection<Map.Entry<K, V>> {
        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            e().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                return e().c(entry.getKey(), entry.getValue());
            }
            return false;
        }

        abstract n1<K, V> e();

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                return e().remove(entry.getKey(), entry.getValue());
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return e().size();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(n1<?, ?> n1Var, Object obj) {
        if (obj == n1Var) {
            return true;
        }
        if (obj instanceof n1) {
            return n1Var.b().equals(((n1) obj).b());
        }
        return false;
    }

    public static <K, V> i1<K, V> b(Map<K, Collection<V>> map, com.google.common.base.r<? extends List<V>> rVar) {
        return new b(map, rVar);
    }
}
