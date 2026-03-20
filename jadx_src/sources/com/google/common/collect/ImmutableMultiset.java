package com.google.common.collect;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.r1;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ImmutableMultiset<E> extends a1<E> implements r1<E> {

    /* renamed from: b  reason: collision with root package name */
    private transient ImmutableList<E> f18993b;

    /* renamed from: c  reason: collision with root package name */
    private transient ImmutableSet<r1.a<E>> f18994c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends d3<E> {

        /* renamed from: a  reason: collision with root package name */
        int f18995a;

        /* renamed from: b  reason: collision with root package name */
        E f18996b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ Iterator f18997c;

        a(ImmutableMultiset immutableMultiset, Iterator it) {
            this.f18997c = it;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f18995a > 0 || this.f18997c.hasNext();
        }

        @Override // java.util.Iterator
        public E next() {
            if (this.f18995a <= 0) {
                r1.a aVar = (r1.a) this.f18997c.next();
                this.f18996b = (E) aVar.a();
                this.f18995a = aVar.getCount();
            }
            this.f18995a--;
            E e8 = this.f18996b;
            Objects.requireNonNull(e8);
            return e8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b<E> extends ImmutableCollection.b<E> {

        /* renamed from: b  reason: collision with root package name */
        boolean f18999b = false;

        /* renamed from: c  reason: collision with root package name */
        boolean f19000c = false;

        /* renamed from: a  reason: collision with root package name */
        w1<E> f18998a = null;

        /* JADX INFO: Access modifiers changed from: package-private */
        public b(boolean z4) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class c extends e1<r1.a<E>> {
        private static final long serialVersionUID = 0;

        private c() {
        }

        /* synthetic */ c(ImmutableMultiset immutableMultiset, a aVar) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.e1
        /* renamed from: Q */
        public r1.a<E> get(int i8) {
            return ImmutableMultiset.this.v(i8);
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            if (obj instanceof r1.a) {
                r1.a aVar = (r1.a) obj;
                return aVar.getCount() > 0 && ImmutableMultiset.this.m0(aVar.a()) == aVar.getCount();
            }
            return false;
        }

        @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
        public int hashCode() {
            return ImmutableMultiset.this.hashCode();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean n() {
            return ImmutableMultiset.this.n();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return ImmutableMultiset.this.l().size();
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return new d(ImmutableMultiset.this);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class d<E> implements Serializable {

        /* renamed from: a  reason: collision with root package name */
        final ImmutableMultiset<E> f19002a;

        d(ImmutableMultiset<E> immutableMultiset) {
            this.f19002a = immutableMultiset;
        }

        Object readResolve() {
            return this.f19002a.entrySet();
        }
    }

    private ImmutableSet<r1.a<E>> q() {
        return isEmpty() ? ImmutableSet.H() : new c(this, null);
    }

    @Override // com.google.common.collect.r1
    @Deprecated
    public final int B(Object obj, int i8) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.r1
    @Deprecated
    public final int J(E e8, int i8) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.r1
    @Deprecated
    public final int U(E e8, int i8) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.r1
    @Deprecated
    public final boolean Y(E e8, int i8, int i9) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        return m0(obj) > 0;
    }

    @Override // com.google.common.collect.ImmutableCollection
    public ImmutableList<E> e() {
        ImmutableList<E> immutableList = this.f18993b;
        if (immutableList == null) {
            ImmutableList<E> e8 = super.e();
            this.f18993b = e8;
            return e8;
        }
        return immutableList;
    }

    @Override // java.util.Collection, com.google.common.collect.r1
    public boolean equals(Object obj) {
        return s1.f(this, obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int g(Object[] objArr, int i8) {
        d3<r1.a<E>> it = entrySet().iterator();
        while (it.hasNext()) {
            r1.a<E> next = it.next();
            Arrays.fill(objArr, i8, next.getCount() + i8, next.a());
            i8 += next.getCount();
        }
        return i8;
    }

    @Override // java.util.Collection, com.google.common.collect.r1
    public int hashCode() {
        return p2.d(entrySet());
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: p */
    public d3<E> iterator() {
        return new a(this, entrySet().iterator());
    }

    @Override // com.google.common.collect.r1
    /* renamed from: t */
    public abstract ImmutableSet<E> l();

    @Override // java.util.AbstractCollection
    public String toString() {
        return entrySet().toString();
    }

    @Override // com.google.common.collect.r1
    /* renamed from: u */
    public ImmutableSet<r1.a<E>> entrySet() {
        ImmutableSet<r1.a<E>> immutableSet = this.f18994c;
        if (immutableSet == null) {
            ImmutableSet<r1.a<E>> q = q();
            this.f18994c = q;
            return q;
        }
        return immutableSet;
    }

    abstract r1.a<E> v(int i8);

    @Override // com.google.common.collect.ImmutableCollection
    abstract Object writeReplace();
}
