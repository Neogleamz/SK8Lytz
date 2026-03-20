package com.google.common.collect;

import com.google.common.collect.r1;
import com.google.common.collect.s1;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.Enum;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class EnumMultiset<E extends Enum<E>> extends i<E> implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: c  reason: collision with root package name */
    private transient Class<E> f18901c;

    /* renamed from: d  reason: collision with root package name */
    private transient E[] f18902d;

    /* renamed from: e  reason: collision with root package name */
    private transient int[] f18903e;

    /* renamed from: f  reason: collision with root package name */
    private transient int f18904f;

    /* renamed from: g  reason: collision with root package name */
    private transient long f18905g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends EnumMultiset<E>.c<E> {
        a() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.EnumMultiset.c
        /* renamed from: b */
        public E a(int i8) {
            return (E) EnumMultiset.this.f18902d[i8];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends EnumMultiset<E>.c<r1.a<E>> {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a extends s1.b<E> {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ int f18908a;

            a(int i8) {
                this.f18908a = i8;
            }

            @Override // com.google.common.collect.r1.a
            /* renamed from: b */
            public E a() {
                return (E) EnumMultiset.this.f18902d[this.f18908a];
            }

            @Override // com.google.common.collect.r1.a
            public int getCount() {
                return EnumMultiset.this.f18903e[this.f18908a];
            }
        }

        b() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.EnumMultiset.c
        /* renamed from: b */
        public r1.a<E> a(int i8) {
            return new a(i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    abstract class c<T> implements Iterator<T> {

        /* renamed from: a  reason: collision with root package name */
        int f18910a = 0;

        /* renamed from: b  reason: collision with root package name */
        int f18911b = -1;

        c() {
        }

        abstract T a(int i8);

        @Override // java.util.Iterator
        public boolean hasNext() {
            while (this.f18910a < EnumMultiset.this.f18902d.length) {
                int[] iArr = EnumMultiset.this.f18903e;
                int i8 = this.f18910a;
                if (iArr[i8] > 0) {
                    return true;
                }
                this.f18910a = i8 + 1;
            }
            return false;
        }

        @Override // java.util.Iterator
        public T next() {
            if (hasNext()) {
                T a9 = a(this.f18910a);
                int i8 = this.f18910a;
                this.f18911b = i8;
                this.f18910a = i8 + 1;
                return a9;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            t.d(this.f18911b >= 0);
            if (EnumMultiset.this.f18903e[this.f18911b] > 0) {
                EnumMultiset.q(EnumMultiset.this);
                EnumMultiset enumMultiset = EnumMultiset.this;
                EnumMultiset.t(enumMultiset, enumMultiset.f18903e[this.f18911b]);
                EnumMultiset.this.f18903e[this.f18911b] = 0;
            }
            this.f18911b = -1;
        }
    }

    static /* synthetic */ int q(EnumMultiset enumMultiset) {
        int i8 = enumMultiset.f18904f;
        enumMultiset.f18904f = i8 - 1;
        return i8;
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        Class<E> cls = (Class) objectInputStream.readObject();
        this.f18901c = cls;
        E[] enumConstants = cls.getEnumConstants();
        this.f18902d = enumConstants;
        this.f18903e = new int[enumConstants.length];
        m2.f(this, objectInputStream);
    }

    static /* synthetic */ long t(EnumMultiset enumMultiset, long j8) {
        long j9 = enumMultiset.f18905g - j8;
        enumMultiset.f18905g = j9;
        return j9;
    }

    private void v(Object obj) {
        com.google.common.base.l.n(obj);
        if (x(obj)) {
            return;
        }
        String valueOf = String.valueOf(this.f18901c);
        String valueOf2 = String.valueOf(obj);
        StringBuilder sb = new StringBuilder(valueOf.length() + 21 + valueOf2.length());
        sb.append("Expected an ");
        sb.append(valueOf);
        sb.append(" but got ");
        sb.append(valueOf2);
        throw new ClassCastException(sb.toString());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.f18901c);
        m2.k(this, objectOutputStream);
    }

    private boolean x(Object obj) {
        if (obj instanceof Enum) {
            Enum r52 = (Enum) obj;
            int ordinal = r52.ordinal();
            E[] eArr = this.f18902d;
            return ordinal < eArr.length && eArr[ordinal] == r52;
        }
        return false;
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public int B(Object obj, int i8) {
        if (obj == null || !x(obj)) {
            return 0;
        }
        Enum r12 = (Enum) obj;
        t.b(i8, "occurrences");
        if (i8 == 0) {
            return m0(obj);
        }
        int ordinal = r12.ordinal();
        int[] iArr = this.f18903e;
        int i9 = iArr[ordinal];
        if (i9 == 0) {
            return 0;
        }
        if (i9 <= i8) {
            iArr[ordinal] = 0;
            this.f18904f--;
            this.f18905g -= i9;
        } else {
            iArr[ordinal] = i9 - i8;
            this.f18905g -= i8;
        }
        return i9;
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public /* bridge */ /* synthetic */ boolean Y(Object obj, int i8, int i9) {
        return super.Y(obj, i8, i9);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public void clear() {
        Arrays.fill(this.f18903e, 0);
        this.f18905g = 0L;
        this.f18904f = 0;
    }

    @Override // com.google.common.collect.i, java.util.AbstractCollection, java.util.Collection, com.google.common.collect.r1
    public /* bridge */ /* synthetic */ boolean contains(Object obj) {
        return super.contains(obj);
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.i
    int h() {
        return this.f18904f;
    }

    @Override // com.google.common.collect.i
    Iterator<E> i() {
        return new a();
    }

    @Override // com.google.common.collect.i, java.util.AbstractCollection, java.util.Collection
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return s1.h(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.i
    public Iterator<r1.a<E>> k() {
        return new b();
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public /* bridge */ /* synthetic */ Set l() {
        return super.l();
    }

    @Override // com.google.common.collect.r1
    public int m0(Object obj) {
        if (obj == null || !x(obj)) {
            return 0;
        }
        return this.f18903e[((Enum) obj).ordinal()];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.r1
    public int size() {
        return com.google.common.primitives.g.k(this.f18905g);
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    /* renamed from: u */
    public int J(E e8, int i8) {
        v(e8);
        t.b(i8, "occurrences");
        if (i8 == 0) {
            return m0(e8);
        }
        int ordinal = e8.ordinal();
        int i9 = this.f18903e[ordinal];
        long j8 = i8;
        long j9 = i9 + j8;
        com.google.common.base.l.h(j9 <= 2147483647L, "too many occurrences: %s", j9);
        this.f18903e[ordinal] = (int) j9;
        if (i9 == 0) {
            this.f18904f++;
        }
        this.f18905g += j8;
        return i9;
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    /* renamed from: y */
    public int U(E e8, int i8) {
        int i9;
        v(e8);
        t.b(i8, "count");
        int ordinal = e8.ordinal();
        int[] iArr = this.f18903e;
        int i10 = iArr[ordinal];
        iArr[ordinal] = i8;
        this.f18905g += i8 - i10;
        if (i10 != 0 || i8 <= 0) {
            if (i10 > 0 && i8 == 0) {
                i9 = this.f18904f - 1;
            }
            return i10;
        }
        i9 = this.f18904f + 1;
        this.f18904f = i9;
        return i10;
    }
}
