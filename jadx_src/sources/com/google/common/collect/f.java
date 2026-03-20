package com.google.common.collect;

import com.google.common.collect.r1;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class f<E> extends i<E> implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: c  reason: collision with root package name */
    transient w1<E> f19255c;

    /* renamed from: d  reason: collision with root package name */
    transient long f19256d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends f<E>.c<E> {
        a() {
            super();
        }

        @Override // com.google.common.collect.f.c
        E b(int i8) {
            return f.this.f19255c.f(i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends f<E>.c<r1.a<E>> {
        b() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.f.c
        /* renamed from: c */
        public r1.a<E> b(int i8) {
            return f.this.f19255c.d(i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    abstract class c<T> implements Iterator<T> {

        /* renamed from: a  reason: collision with root package name */
        int f19259a;

        /* renamed from: b  reason: collision with root package name */
        int f19260b = -1;

        /* renamed from: c  reason: collision with root package name */
        int f19261c;

        c() {
            this.f19259a = f.this.f19255c.b();
            this.f19261c = f.this.f19255c.f19493d;
        }

        private void a() {
            if (f.this.f19255c.f19493d != this.f19261c) {
                throw new ConcurrentModificationException();
            }
        }

        abstract T b(int i8);

        @Override // java.util.Iterator
        public boolean hasNext() {
            a();
            return this.f19259a >= 0;
        }

        @Override // java.util.Iterator
        public T next() {
            if (hasNext()) {
                T b9 = b(this.f19259a);
                int i8 = this.f19259a;
                this.f19260b = i8;
                this.f19259a = f.this.f19255c.p(i8);
                return b9;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            f fVar;
            a();
            t.d(this.f19260b != -1);
            f.this.f19256d -= fVar.f19255c.u(this.f19260b);
            this.f19259a = f.this.f19255c.q(this.f19259a, this.f19260b);
            this.f19260b = -1;
            this.f19261c = f.this.f19255c.f19493d;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        int h8 = m2.h(objectInputStream);
        this.f19255c = p(3);
        m2.g(this, objectInputStream, h8);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        m2.k(this, objectOutputStream);
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public final int B(Object obj, int i8) {
        if (i8 == 0) {
            return m0(obj);
        }
        com.google.common.base.l.f(i8 > 0, "occurrences cannot be negative: %s", i8);
        int j8 = this.f19255c.j(obj);
        if (j8 == -1) {
            return 0;
        }
        int h8 = this.f19255c.h(j8);
        if (h8 > i8) {
            this.f19255c.y(j8, h8 - i8);
        } else {
            this.f19255c.u(j8);
            i8 = h8;
        }
        this.f19256d -= i8;
        return h8;
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public final int J(E e8, int i8) {
        if (i8 == 0) {
            return m0(e8);
        }
        com.google.common.base.l.f(i8 > 0, "occurrences cannot be negative: %s", i8);
        int j8 = this.f19255c.j(e8);
        if (j8 == -1) {
            this.f19255c.r(e8, i8);
            this.f19256d += i8;
            return 0;
        }
        int h8 = this.f19255c.h(j8);
        long j9 = i8;
        long j10 = h8 + j9;
        com.google.common.base.l.h(j10 <= 2147483647L, "too many occurrences: %s", j10);
        this.f19255c.y(j8, (int) j10);
        this.f19256d += j9;
        return h8;
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public final int U(E e8, int i8) {
        t.b(i8, "count");
        w1<E> w1Var = this.f19255c;
        int s8 = i8 == 0 ? w1Var.s(e8) : w1Var.r(e8, i8);
        this.f19256d += i8 - s8;
        return s8;
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public final boolean Y(E e8, int i8, int i9) {
        long j8;
        t.b(i8, "oldCount");
        t.b(i9, "newCount");
        int j9 = this.f19255c.j(e8);
        if (j9 == -1) {
            if (i8 != 0) {
                return false;
            }
            if (i9 > 0) {
                this.f19255c.r(e8, i9);
                this.f19256d += i9;
            }
            return true;
        } else if (this.f19255c.h(j9) != i8) {
            return false;
        } else {
            w1<E> w1Var = this.f19255c;
            if (i9 == 0) {
                w1Var.u(j9);
                j8 = this.f19256d - i8;
            } else {
                w1Var.y(j9, i9);
                j8 = this.f19256d + (i9 - i8);
            }
            this.f19256d = j8;
            return true;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final void clear() {
        this.f19255c.a();
        this.f19256d = 0L;
    }

    @Override // com.google.common.collect.i
    final int h() {
        return this.f19255c.z();
    }

    @Override // com.google.common.collect.i
    final Iterator<E> i() {
        return new a();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public final Iterator<E> iterator() {
        return s1.h(this);
    }

    @Override // com.google.common.collect.i
    final Iterator<r1.a<E>> k() {
        return new b();
    }

    @Override // com.google.common.collect.r1
    public final int m0(Object obj) {
        return this.f19255c.c(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n(r1<? super E> r1Var) {
        com.google.common.base.l.n(r1Var);
        int b9 = this.f19255c.b();
        while (b9 >= 0) {
            r1Var.J((E) this.f19255c.f(b9), this.f19255c.h(b9));
            b9 = this.f19255c.p(b9);
        }
    }

    abstract w1<E> p(int i8);

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.r1
    public final int size() {
        return com.google.common.primitives.g.k(this.f19256d);
    }
}
