package com.google.common.collect;

import com.google.common.collect.r1;
import com.google.common.collect.v2;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class m<E> extends i<E> implements u2<E> {

    /* renamed from: c  reason: collision with root package name */
    final Comparator<? super E> f19397c;

    /* renamed from: d  reason: collision with root package name */
    private transient u2<E> f19398d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends g0<E> {
        a() {
        }

        @Override // com.google.common.collect.g0
        u2<E> A() {
            return m.this;
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<E> iterator() {
            return m.this.descendingIterator();
        }

        @Override // com.google.common.collect.g0
        Iterator<r1.a<E>> y() {
            return m.this.q();
        }
    }

    m() {
        this(y1.c());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public m(Comparator<? super E> comparator) {
        this.f19397c = (Comparator) com.google.common.base.l.n(comparator);
    }

    public u2<E> P() {
        u2<E> u2Var = this.f19398d;
        if (u2Var == null) {
            u2<E> n8 = n();
            this.f19398d = n8;
            return n8;
        }
        return u2Var;
    }

    public u2<E> P0(E e8, BoundType boundType, E e9, BoundType boundType2) {
        com.google.common.base.l.n(boundType);
        com.google.common.base.l.n(boundType2);
        return n0(e8, boundType).g0(e9, boundType2);
    }

    public Comparator<? super E> comparator() {
        return this.f19397c;
    }

    Iterator<E> descendingIterator() {
        return s1.h(P());
    }

    public r1.a<E> firstEntry() {
        Iterator<r1.a<E>> k8 = k();
        if (k8.hasNext()) {
            return k8.next();
        }
        return null;
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public NavigableSet<E> l() {
        return (NavigableSet) super.l();
    }

    public r1.a<E> lastEntry() {
        Iterator<r1.a<E>> q = q();
        if (q.hasNext()) {
            return q.next();
        }
        return null;
    }

    u2<E> n() {
        return new a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.i
    /* renamed from: p */
    public NavigableSet<E> e() {
        return new v2.b(this);
    }

    public r1.a<E> pollFirstEntry() {
        Iterator<r1.a<E>> k8 = k();
        if (k8.hasNext()) {
            r1.a<E> next = k8.next();
            r1.a<E> g8 = s1.g(next.a(), next.getCount());
            k8.remove();
            return g8;
        }
        return null;
    }

    public r1.a<E> pollLastEntry() {
        Iterator<r1.a<E>> q = q();
        if (q.hasNext()) {
            r1.a<E> next = q.next();
            r1.a<E> g8 = s1.g(next.a(), next.getCount());
            q.remove();
            return g8;
        }
        return null;
    }

    abstract Iterator<r1.a<E>> q();
}
