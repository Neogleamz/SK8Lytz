package com.google.common.collect;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.m2;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ImmutableSetMultimap<K, V> extends ImmutableMultimap<K, V> implements n2<K, V> {
    private static final long serialVersionUID = 0;

    /* renamed from: g  reason: collision with root package name */
    private final transient ImmutableSet<V> f19016g;

    /* renamed from: h  reason: collision with root package name */
    private transient ImmutableSet<Map.Entry<K, V>> f19017h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a<K, V> extends ImmutableMultimap.c<K, V> {
        public ImmutableSetMultimap<K, V> a() {
            Collection entrySet = this.f18986a.entrySet();
            Comparator<? super K> comparator = this.f18987b;
            if (comparator != null) {
                entrySet = y1.a(comparator).d().b(entrySet);
            }
            return ImmutableSetMultimap.x(entrySet, this.f18988c);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b<K, V> extends ImmutableSet<Map.Entry<K, V>> {

        /* renamed from: c  reason: collision with root package name */
        private final transient ImmutableSetMultimap<K, V> f19018c;

        b(ImmutableSetMultimap<K, V> immutableSetMultimap) {
            this.f19018c = immutableSetMultimap;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                return this.f19018c.c(entry.getKey(), entry.getValue());
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean n() {
            return false;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        /* renamed from: p */
        public d3<Map.Entry<K, V>> iterator() {
            return this.f19018c.j();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f19018c.size();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class c {

        /* renamed from: a  reason: collision with root package name */
        static final m2.b<ImmutableSetMultimap> f19019a = m2.a(ImmutableSetMultimap.class, "emptySet");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableSetMultimap(ImmutableMap<K, ImmutableSet<V>> immutableMap, int i8, Comparator<? super V> comparator) {
        super(immutableMap, i8);
        this.f19016g = v(comparator);
    }

    private static <V> ImmutableSet<V> D(Comparator<? super V> comparator, Collection<? extends V> collection) {
        return comparator == null ? ImmutableSet.A(collection) : ImmutableSortedSet.S(comparator, collection);
    }

    private static <V> ImmutableSet.a<V> E(Comparator<? super V> comparator) {
        return comparator == null ? new ImmutableSet.a<>() : new ImmutableSortedSet.a(comparator);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        Comparator comparator = (Comparator) objectInputStream.readObject();
        int readInt = objectInputStream.readInt();
        if (readInt < 0) {
            StringBuilder sb = new StringBuilder(29);
            sb.append("Invalid key count ");
            sb.append(readInt);
            throw new InvalidObjectException(sb.toString());
        }
        ImmutableMap.b a9 = ImmutableMap.a();
        int i8 = 0;
        for (int i9 = 0; i9 < readInt; i9++) {
            Object readObject = objectInputStream.readObject();
            int readInt2 = objectInputStream.readInt();
            if (readInt2 <= 0) {
                StringBuilder sb2 = new StringBuilder(31);
                sb2.append("Invalid value count ");
                sb2.append(readInt2);
                throw new InvalidObjectException(sb2.toString());
            }
            ImmutableSet.a E = E(comparator);
            for (int i10 = 0; i10 < readInt2; i10++) {
                E.a(objectInputStream.readObject());
            }
            ImmutableSet l8 = E.l();
            if (l8.size() != readInt2) {
                String valueOf = String.valueOf(readObject);
                StringBuilder sb3 = new StringBuilder(valueOf.length() + 40);
                sb3.append("Duplicate key-value pairs exist for key ");
                sb3.append(valueOf);
                throw new InvalidObjectException(sb3.toString());
            }
            a9.g(readObject, l8);
            i8 += readInt2;
        }
        try {
            ImmutableMultimap.e.f18990a.b(this, a9.d());
            ImmutableMultimap.e.f18991b.a(this, i8);
            c.f19019a.b(this, v(comparator));
        } catch (IllegalArgumentException e8) {
            throw ((InvalidObjectException) new InvalidObjectException(e8.getMessage()).initCause(e8));
        }
    }

    private static <V> ImmutableSet<V> v(Comparator<? super V> comparator) {
        return comparator == null ? ImmutableSet.H() : ImmutableSortedSet.X(comparator);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(B());
        m2.j(this, objectOutputStream);
    }

    static <K, V> ImmutableSetMultimap<K, V> x(Collection<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>> collection, Comparator<? super V> comparator) {
        if (collection.isEmpty()) {
            return z();
        }
        ImmutableMap.b bVar = new ImmutableMap.b(collection.size());
        int i8 = 0;
        for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : collection) {
            K key = entry.getKey();
            ImmutableSet D = D(comparator, entry.getValue());
            if (!D.isEmpty()) {
                bVar.g(key, D);
                i8 += D.size();
            }
        }
        return new ImmutableSetMultimap<>(bVar.d(), i8, comparator);
    }

    public static <K, V> ImmutableSetMultimap<K, V> z() {
        return h0.f19314j;
    }

    @Override // com.google.common.collect.ImmutableMultimap
    @Deprecated
    /* renamed from: A */
    public final ImmutableSet<V> s(Object obj) {
        throw new UnsupportedOperationException();
    }

    Comparator<? super V> B() {
        ImmutableSet<V> immutableSet = this.f19016g;
        if (immutableSet instanceof ImmutableSortedSet) {
            return ((ImmutableSortedSet) immutableSet).comparator();
        }
        return null;
    }

    @Override // com.google.common.collect.ImmutableMultimap
    /* renamed from: w */
    public ImmutableSet<Map.Entry<K, V>> o() {
        ImmutableSet<Map.Entry<K, V>> immutableSet = this.f19017h;
        if (immutableSet == null) {
            b bVar = new b(this);
            this.f19017h = bVar;
            return bVar;
        }
        return immutableSet;
    }

    @Override // com.google.common.collect.ImmutableMultimap
    /* renamed from: y */
    public ImmutableSet<V> q(K k8) {
        return (ImmutableSet) com.google.common.base.i.a((ImmutableSet) this.f18977e.get(k8), this.f19016g);
    }
}
