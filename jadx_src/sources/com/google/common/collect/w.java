package com.google.common.collect;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class w<E> extends AbstractSet<E> implements Serializable {

    /* renamed from: a  reason: collision with root package name */
    private transient Object f19479a;

    /* renamed from: b  reason: collision with root package name */
    private transient int[] f19480b;

    /* renamed from: c  reason: collision with root package name */
    transient Object[] f19481c;

    /* renamed from: d  reason: collision with root package name */
    private transient int f19482d;

    /* renamed from: e  reason: collision with root package name */
    private transient int f19483e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Iterator<E> {

        /* renamed from: a  reason: collision with root package name */
        int f19484a;

        /* renamed from: b  reason: collision with root package name */
        int f19485b;

        /* renamed from: c  reason: collision with root package name */
        int f19486c = -1;

        a() {
            this.f19484a = w.this.f19482d;
            this.f19485b = w.this.v();
        }

        private void a() {
            if (w.this.f19482d != this.f19484a) {
                throw new ConcurrentModificationException();
            }
        }

        void b() {
            this.f19484a += 32;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f19485b >= 0;
        }

        @Override // java.util.Iterator
        public E next() {
            a();
            if (hasNext()) {
                int i8 = this.f19485b;
                this.f19486c = i8;
                E e8 = (E) w.this.t(i8);
                this.f19485b = w.this.x(this.f19485b);
                return e8;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            a();
            t.d(this.f19486c >= 0);
            b();
            w wVar = w.this;
            wVar.remove(wVar.t(this.f19486c));
            this.f19485b = w.this.h(this.f19485b, this.f19486c);
            this.f19486c = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public w() {
        D(3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public w(int i8) {
        D(i8);
    }

    private Object[] H() {
        Object[] objArr = this.f19481c;
        Objects.requireNonNull(objArr);
        return objArr;
    }

    private int[] I() {
        int[] iArr = this.f19480b;
        Objects.requireNonNull(iArr);
        return iArr;
    }

    private Object K() {
        Object obj = this.f19479a;
        Objects.requireNonNull(obj);
        return obj;
    }

    private void M(int i8) {
        int min;
        int length = I().length;
        if (i8 <= length || (min = Math.min(1073741823, (Math.max(1, length >>> 1) + length) | 1)) == length) {
            return;
        }
        L(min);
    }

    private int N(int i8, int i9, int i10, int i11) {
        Object a9 = x.a(i9);
        int i12 = i9 - 1;
        if (i11 != 0) {
            x.i(a9, i10 & i12, i11 + 1);
        }
        Object K = K();
        int[] I = I();
        for (int i13 = 0; i13 <= i8; i13++) {
            int h8 = x.h(K, i13);
            while (h8 != 0) {
                int i14 = h8 - 1;
                int i15 = I[i14];
                int b9 = x.b(i15, i8) | i13;
                int i16 = b9 & i12;
                int h9 = x.h(a9, i16);
                x.i(a9, i16, h8);
                I[i14] = x.d(b9, h9, i12);
                h8 = x.c(i15, i8);
            }
        }
        this.f19479a = a9;
        R(i12);
        return i12;
    }

    private void O(int i8, E e8) {
        H()[i8] = e8;
    }

    private void Q(int i8, int i9) {
        I()[i8] = i9;
    }

    private void R(int i8) {
        this.f19482d = x.d(this.f19482d, 32 - Integer.numberOfLeadingZeros(i8), 31);
    }

    private Set<E> n(int i8) {
        return new LinkedHashSet(i8, 1.0f);
    }

    public static <E> w<E> p(int i8) {
        return new w<>(i8);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        if (readInt < 0) {
            StringBuilder sb = new StringBuilder(25);
            sb.append("Invalid size: ");
            sb.append(readInt);
            throw new InvalidObjectException(sb.toString());
        }
        D(readInt);
        for (int i8 = 0; i8 < readInt; i8++) {
            add(objectInputStream.readObject());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public E t(int i8) {
        return (E) H()[i8];
    }

    private int u(int i8) {
        return I()[i8];
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(size());
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            objectOutputStream.writeObject(it.next());
        }
    }

    private int y() {
        return (1 << (this.f19482d & 31)) - 1;
    }

    void A() {
        this.f19482d += 32;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void D(int i8) {
        com.google.common.base.l.e(i8 >= 0, "Expected size must be >= 0");
        this.f19482d = com.google.common.primitives.g.f(i8, 1, 1073741823);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void E(int i8, E e8, int i9, int i10) {
        Q(i8, x.d(i9, 0, i10));
        O(i8, e8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void F(int i8, int i9) {
        Object K = K();
        int[] I = I();
        Object[] H = H();
        int size = size() - 1;
        if (i8 >= size) {
            H[i8] = null;
            I[i8] = 0;
            return;
        }
        Object obj = H[size];
        H[i8] = obj;
        H[size] = null;
        I[i8] = I[size];
        I[size] = 0;
        int d8 = v0.d(obj) & i9;
        int h8 = x.h(K, d8);
        int i10 = size + 1;
        if (h8 == i10) {
            x.i(K, d8, i8 + 1);
            return;
        }
        while (true) {
            int i11 = h8 - 1;
            int i12 = I[i11];
            int c9 = x.c(i12, i9);
            if (c9 == i10) {
                I[i11] = x.d(i12, i8 + 1, i9);
                return;
            }
            h8 = c9;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean G() {
        return this.f19479a == null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void L(int i8) {
        this.f19480b = Arrays.copyOf(I(), i8);
        this.f19481c = Arrays.copyOf(H(), i8);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(E e8) {
        if (G()) {
            i();
        }
        Set<E> q = q();
        if (q != null) {
            return q.add(e8);
        }
        int[] I = I();
        Object[] H = H();
        int i8 = this.f19483e;
        int i9 = i8 + 1;
        int d8 = v0.d(e8);
        int y8 = y();
        int i10 = d8 & y8;
        int h8 = x.h(K(), i10);
        if (h8 == 0) {
            if (i9 <= y8) {
                x.i(K(), i10, i9);
                M(i9);
                E(i8, e8, d8, y8);
                this.f19483e = i9;
                A();
                return true;
            }
            y8 = N(y8, x.e(y8), d8, i8);
            M(i9);
            E(i8, e8, d8, y8);
            this.f19483e = i9;
            A();
            return true;
        }
        int b9 = x.b(d8, y8);
        int i11 = 0;
        while (true) {
            int i12 = h8 - 1;
            int i13 = I[i12];
            if (x.b(i13, y8) == b9 && com.google.common.base.k.a(e8, H[i12])) {
                return false;
            }
            int c9 = x.c(i13, y8);
            i11++;
            if (c9 != 0) {
                h8 = c9;
            } else if (i11 >= 9) {
                return k().add(e8);
            } else {
                if (i9 <= y8) {
                    I[i12] = x.d(i13, i9, y8);
                }
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        if (G()) {
            return;
        }
        A();
        Set<E> q = q();
        if (q != null) {
            this.f19482d = com.google.common.primitives.g.f(size(), 3, 1073741823);
            q.clear();
            this.f19479a = null;
        } else {
            Arrays.fill(H(), 0, this.f19483e, (Object) null);
            x.g(K());
            Arrays.fill(I(), 0, this.f19483e, 0);
        }
        this.f19483e = 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        if (G()) {
            return false;
        }
        Set<E> q = q();
        if (q != null) {
            return q.contains(obj);
        }
        int d8 = v0.d(obj);
        int y8 = y();
        int h8 = x.h(K(), d8 & y8);
        if (h8 == 0) {
            return false;
        }
        int b9 = x.b(d8, y8);
        do {
            int i8 = h8 - 1;
            int u8 = u(i8);
            if (x.b(u8, y8) == b9 && com.google.common.base.k.a(obj, t(i8))) {
                return true;
            }
            h8 = x.c(u8, y8);
        } while (h8 != 0);
        return false;
    }

    int h(int i8, int i9) {
        return i8 - 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int i() {
        com.google.common.base.l.t(G(), "Arrays already allocated");
        int i8 = this.f19482d;
        int j8 = x.j(i8);
        this.f19479a = x.a(j8);
        R(j8 - 1);
        this.f19480b = new int[i8];
        this.f19481c = new Object[i8];
        return i8;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        Set<E> q = q();
        return q != null ? q.iterator() : new a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<E> k() {
        Set<E> n8 = n(y() + 1);
        int v8 = v();
        while (v8 >= 0) {
            n8.add(t(v8));
            v8 = x(v8);
        }
        this.f19479a = n8;
        this.f19480b = null;
        this.f19481c = null;
        A();
        return n8;
    }

    Set<E> q() {
        Object obj = this.f19479a;
        if (obj instanceof Set) {
            return (Set) obj;
        }
        return null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        if (G()) {
            return false;
        }
        Set<E> q = q();
        if (q != null) {
            return q.remove(obj);
        }
        int y8 = y();
        int f5 = x.f(obj, null, y8, K(), I(), H(), null);
        if (f5 == -1) {
            return false;
        }
        F(f5, y8);
        this.f19483e--;
        A();
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        Set<E> q = q();
        return q != null ? q.size() : this.f19483e;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public Object[] toArray() {
        if (G()) {
            return new Object[0];
        }
        Set<E> q = q();
        return q != null ? q.toArray() : Arrays.copyOf(H(), this.f19483e);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] tArr) {
        if (!G()) {
            Set<E> q = q();
            return q != null ? (T[]) q.toArray(tArr) : (T[]) v1.h(H(), 0, this.f19483e, tArr);
        }
        if (tArr.length > 0) {
            tArr[0] = null;
        }
        return tArr;
    }

    int v() {
        return isEmpty() ? -1 : 0;
    }

    int x(int i8) {
        int i9 = i8 + 1;
        if (i9 < this.f19483e) {
            return i9;
        }
        return -1;
    }
}
