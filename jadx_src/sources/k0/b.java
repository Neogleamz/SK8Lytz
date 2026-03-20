package k0;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b<E> implements Collection<E>, Set<E> {

    /* renamed from: e  reason: collision with root package name */
    private static final int[] f20855e = new int[0];

    /* renamed from: f  reason: collision with root package name */
    private static final Object[] f20856f = new Object[0];

    /* renamed from: g  reason: collision with root package name */
    private static Object[] f20857g;

    /* renamed from: h  reason: collision with root package name */
    private static int f20858h;

    /* renamed from: j  reason: collision with root package name */
    private static Object[] f20859j;

    /* renamed from: k  reason: collision with root package name */
    private static int f20860k;

    /* renamed from: a  reason: collision with root package name */
    private int[] f20861a;

    /* renamed from: b  reason: collision with root package name */
    Object[] f20862b;

    /* renamed from: c  reason: collision with root package name */
    int f20863c;

    /* renamed from: d  reason: collision with root package name */
    private f<E, E> f20864d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends f<E, E> {
        a() {
        }

        @Override // k0.f
        protected void a() {
            b.this.clear();
        }

        @Override // k0.f
        protected Object b(int i8, int i9) {
            return b.this.f20862b[i8];
        }

        @Override // k0.f
        protected Map<E, E> c() {
            throw new UnsupportedOperationException("not a map");
        }

        @Override // k0.f
        protected int d() {
            return b.this.f20863c;
        }

        @Override // k0.f
        protected int e(Object obj) {
            return b.this.indexOf(obj);
        }

        @Override // k0.f
        protected int f(Object obj) {
            return b.this.indexOf(obj);
        }

        @Override // k0.f
        protected void g(E e8, E e9) {
            b.this.add(e8);
        }

        @Override // k0.f
        protected void h(int i8) {
            b.this.p(i8);
        }

        @Override // k0.f
        protected E i(int i8, E e8) {
            throw new UnsupportedOperationException("not a map");
        }
    }

    public b() {
        this(0);
    }

    public b(int i8) {
        if (i8 == 0) {
            this.f20861a = f20855e;
            this.f20862b = f20856f;
        } else {
            e(i8);
        }
        this.f20863c = 0;
    }

    private void e(int i8) {
        if (i8 == 8) {
            synchronized (b.class) {
                Object[] objArr = f20859j;
                if (objArr != null) {
                    this.f20862b = objArr;
                    f20859j = (Object[]) objArr[0];
                    this.f20861a = (int[]) objArr[1];
                    objArr[1] = null;
                    objArr[0] = null;
                    f20860k--;
                    return;
                }
            }
        } else if (i8 == 4) {
            synchronized (b.class) {
                Object[] objArr2 = f20857g;
                if (objArr2 != null) {
                    this.f20862b = objArr2;
                    f20857g = (Object[]) objArr2[0];
                    this.f20861a = (int[]) objArr2[1];
                    objArr2[1] = null;
                    objArr2[0] = null;
                    f20858h--;
                    return;
                }
            }
        }
        this.f20861a = new int[i8];
        this.f20862b = new Object[i8];
    }

    private static void h(int[] iArr, Object[] objArr, int i8) {
        if (iArr.length == 8) {
            synchronized (b.class) {
                if (f20860k < 10) {
                    objArr[0] = f20859j;
                    objArr[1] = iArr;
                    for (int i9 = i8 - 1; i9 >= 2; i9--) {
                        objArr[i9] = null;
                    }
                    f20859j = objArr;
                    f20860k++;
                }
            }
        } else if (iArr.length == 4) {
            synchronized (b.class) {
                if (f20858h < 10) {
                    objArr[0] = f20857g;
                    objArr[1] = iArr;
                    for (int i10 = i8 - 1; i10 >= 2; i10--) {
                        objArr[i10] = null;
                    }
                    f20857g = objArr;
                    f20858h++;
                }
            }
        }
    }

    private f<E, E> i() {
        if (this.f20864d == null) {
            this.f20864d = new a();
        }
        return this.f20864d;
    }

    private int k(Object obj, int i8) {
        int i9 = this.f20863c;
        if (i9 == 0) {
            return -1;
        }
        int a9 = c.a(this.f20861a, i9, i8);
        if (a9 >= 0 && !obj.equals(this.f20862b[a9])) {
            int i10 = a9 + 1;
            while (i10 < i9 && this.f20861a[i10] == i8) {
                if (obj.equals(this.f20862b[i10])) {
                    return i10;
                }
                i10++;
            }
            for (int i11 = a9 - 1; i11 >= 0 && this.f20861a[i11] == i8; i11--) {
                if (obj.equals(this.f20862b[i11])) {
                    return i11;
                }
            }
            return ~i10;
        }
        return a9;
    }

    private int n() {
        int i8 = this.f20863c;
        if (i8 == 0) {
            return -1;
        }
        int a9 = c.a(this.f20861a, i8, 0);
        if (a9 >= 0 && this.f20862b[a9] != null) {
            int i9 = a9 + 1;
            while (i9 < i8 && this.f20861a[i9] == 0) {
                if (this.f20862b[i9] == null) {
                    return i9;
                }
                i9++;
            }
            for (int i10 = a9 - 1; i10 >= 0 && this.f20861a[i10] == 0; i10--) {
                if (this.f20862b[i10] == null) {
                    return i10;
                }
            }
            return ~i9;
        }
        return a9;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean add(E e8) {
        int i8;
        int k8;
        if (e8 == null) {
            k8 = n();
            i8 = 0;
        } else {
            int hashCode = e8.hashCode();
            i8 = hashCode;
            k8 = k(e8, hashCode);
        }
        if (k8 >= 0) {
            return false;
        }
        int i9 = ~k8;
        int i10 = this.f20863c;
        int[] iArr = this.f20861a;
        if (i10 >= iArr.length) {
            int i11 = 4;
            if (i10 >= 8) {
                i11 = (i10 >> 1) + i10;
            } else if (i10 >= 4) {
                i11 = 8;
            }
            Object[] objArr = this.f20862b;
            e(i11);
            int[] iArr2 = this.f20861a;
            if (iArr2.length > 0) {
                System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                System.arraycopy(objArr, 0, this.f20862b, 0, objArr.length);
            }
            h(iArr, objArr, this.f20863c);
        }
        int i12 = this.f20863c;
        if (i9 < i12) {
            int[] iArr3 = this.f20861a;
            int i13 = i9 + 1;
            System.arraycopy(iArr3, i9, iArr3, i13, i12 - i9);
            Object[] objArr2 = this.f20862b;
            System.arraycopy(objArr2, i9, objArr2, i13, this.f20863c - i9);
        }
        this.f20861a[i9] = i8;
        this.f20862b[i9] = e8;
        this.f20863c++;
        return true;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends E> collection) {
        g(this.f20863c + collection.size());
        boolean z4 = false;
        for (E e8 : collection) {
            z4 |= add(e8);
        }
        return z4;
    }

    @Override // java.util.Collection, java.util.Set
    public void clear() {
        int i8 = this.f20863c;
        if (i8 != 0) {
            h(this.f20861a, this.f20862b, i8);
            this.f20861a = f20855e;
            this.f20862b = f20856f;
            this.f20863c = 0;
        }
    }

    @Override // java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Set) {
            Set set = (Set) obj;
            if (size() != set.size()) {
                return false;
            }
            for (int i8 = 0; i8 < this.f20863c; i8++) {
                try {
                    if (!set.contains(q(i8))) {
                        return false;
                    }
                } catch (ClassCastException | NullPointerException unused) {
                }
            }
            return true;
        }
        return false;
    }

    public void g(int i8) {
        int[] iArr = this.f20861a;
        if (iArr.length < i8) {
            Object[] objArr = this.f20862b;
            e(i8);
            int i9 = this.f20863c;
            if (i9 > 0) {
                System.arraycopy(iArr, 0, this.f20861a, 0, i9);
                System.arraycopy(objArr, 0, this.f20862b, 0, this.f20863c);
            }
            h(iArr, objArr, this.f20863c);
        }
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        int[] iArr = this.f20861a;
        int i8 = this.f20863c;
        int i9 = 0;
        for (int i10 = 0; i10 < i8; i10++) {
            i9 += iArr[i10];
        }
        return i9;
    }

    public int indexOf(Object obj) {
        return obj == null ? n() : k(obj, obj.hashCode());
    }

    @Override // java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.f20863c <= 0;
    }

    @Override // java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return i().m().iterator();
    }

    public E p(int i8) {
        Object[] objArr = this.f20862b;
        E e8 = (E) objArr[i8];
        int i9 = this.f20863c;
        if (i9 <= 1) {
            h(this.f20861a, objArr, i9);
            this.f20861a = f20855e;
            this.f20862b = f20856f;
            this.f20863c = 0;
        } else {
            int[] iArr = this.f20861a;
            if (iArr.length <= 8 || i9 >= iArr.length / 3) {
                int i10 = i9 - 1;
                this.f20863c = i10;
                if (i8 < i10) {
                    int i11 = i8 + 1;
                    System.arraycopy(iArr, i11, iArr, i8, i10 - i8);
                    Object[] objArr2 = this.f20862b;
                    System.arraycopy(objArr2, i11, objArr2, i8, this.f20863c - i8);
                }
                this.f20862b[this.f20863c] = null;
            } else {
                e(i9 > 8 ? i9 + (i9 >> 1) : 8);
                this.f20863c--;
                if (i8 > 0) {
                    System.arraycopy(iArr, 0, this.f20861a, 0, i8);
                    System.arraycopy(objArr, 0, this.f20862b, 0, i8);
                }
                int i12 = this.f20863c;
                if (i8 < i12) {
                    int i13 = i8 + 1;
                    System.arraycopy(iArr, i13, this.f20861a, i8, i12 - i8);
                    System.arraycopy(objArr, i13, this.f20862b, i8, this.f20863c - i8);
                }
            }
        }
        return e8;
    }

    public E q(int i8) {
        return (E) this.f20862b[i8];
    }

    @Override // java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        int indexOf = indexOf(obj);
        if (indexOf >= 0) {
            p(indexOf);
            return true;
        }
        return false;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean removeAll(Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        boolean z4 = false;
        while (it.hasNext()) {
            z4 |= remove(it.next());
        }
        return z4;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean retainAll(Collection<?> collection) {
        boolean z4 = false;
        for (int i8 = this.f20863c - 1; i8 >= 0; i8--) {
            if (!collection.contains(this.f20862b[i8])) {
                p(i8);
                z4 = true;
            }
        }
        return z4;
    }

    @Override // java.util.Collection, java.util.Set
    public int size() {
        return this.f20863c;
    }

    @Override // java.util.Collection, java.util.Set
    public Object[] toArray() {
        int i8 = this.f20863c;
        Object[] objArr = new Object[i8];
        System.arraycopy(this.f20862b, 0, objArr, 0, i8);
        return objArr;
    }

    @Override // java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] tArr) {
        if (tArr.length < this.f20863c) {
            tArr = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), this.f20863c));
        }
        System.arraycopy(this.f20862b, 0, tArr, 0, this.f20863c);
        int length = tArr.length;
        int i8 = this.f20863c;
        if (length > i8) {
            tArr[i8] = null;
        }
        return tArr;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.f20863c * 14);
        sb.append('{');
        for (int i8 = 0; i8 < this.f20863c; i8++) {
            if (i8 > 0) {
                sb.append(", ");
            }
            E q = q(i8);
            if (q != this) {
                sb.append(q);
            } else {
                sb.append("(this Set)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
