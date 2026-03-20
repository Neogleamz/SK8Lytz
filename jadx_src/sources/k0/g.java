package k0;

import java.util.ConcurrentModificationException;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g<K, V> {

    /* renamed from: d  reason: collision with root package name */
    static Object[] f20897d;

    /* renamed from: e  reason: collision with root package name */
    static int f20898e;

    /* renamed from: f  reason: collision with root package name */
    static Object[] f20899f;

    /* renamed from: g  reason: collision with root package name */
    static int f20900g;

    /* renamed from: a  reason: collision with root package name */
    int[] f20901a;

    /* renamed from: b  reason: collision with root package name */
    Object[] f20902b;

    /* renamed from: c  reason: collision with root package name */
    int f20903c;

    public g() {
        this.f20901a = c.f20866a;
        this.f20902b = c.f20868c;
        this.f20903c = 0;
    }

    public g(int i8) {
        if (i8 == 0) {
            this.f20901a = c.f20866a;
            this.f20902b = c.f20868c;
        } else {
            a(i8);
        }
        this.f20903c = 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public g(g<K, V> gVar) {
        this();
        if (gVar != 0) {
            l(gVar);
        }
    }

    private void a(int i8) {
        if (i8 == 8) {
            synchronized (g.class) {
                Object[] objArr = f20899f;
                if (objArr != null) {
                    this.f20902b = objArr;
                    f20899f = (Object[]) objArr[0];
                    this.f20901a = (int[]) objArr[1];
                    objArr[1] = null;
                    objArr[0] = null;
                    f20900g--;
                    return;
                }
            }
        } else if (i8 == 4) {
            synchronized (g.class) {
                Object[] objArr2 = f20897d;
                if (objArr2 != null) {
                    this.f20902b = objArr2;
                    f20897d = (Object[]) objArr2[0];
                    this.f20901a = (int[]) objArr2[1];
                    objArr2[1] = null;
                    objArr2[0] = null;
                    f20898e--;
                    return;
                }
            }
        }
        this.f20901a = new int[i8];
        this.f20902b = new Object[i8 << 1];
    }

    private static int b(int[] iArr, int i8, int i9) {
        try {
            return c.a(iArr, i8, i9);
        } catch (ArrayIndexOutOfBoundsException unused) {
            throw new ConcurrentModificationException();
        }
    }

    private static void d(int[] iArr, Object[] objArr, int i8) {
        if (iArr.length == 8) {
            synchronized (g.class) {
                if (f20900g < 10) {
                    objArr[0] = f20899f;
                    objArr[1] = iArr;
                    for (int i9 = (i8 << 1) - 1; i9 >= 2; i9--) {
                        objArr[i9] = null;
                    }
                    f20899f = objArr;
                    f20900g++;
                }
            }
        } else if (iArr.length == 4) {
            synchronized (g.class) {
                if (f20898e < 10) {
                    objArr[0] = f20897d;
                    objArr[1] = iArr;
                    for (int i10 = (i8 << 1) - 1; i10 >= 2; i10--) {
                        objArr[i10] = null;
                    }
                    f20897d = objArr;
                    f20898e++;
                }
            }
        }
    }

    public void c(int i8) {
        int i9 = this.f20903c;
        int[] iArr = this.f20901a;
        if (iArr.length < i8) {
            Object[] objArr = this.f20902b;
            a(i8);
            if (this.f20903c > 0) {
                System.arraycopy(iArr, 0, this.f20901a, 0, i9);
                System.arraycopy(objArr, 0, this.f20902b, 0, i9 << 1);
            }
            d(iArr, objArr, i9);
        }
        if (this.f20903c != i9) {
            throw new ConcurrentModificationException();
        }
    }

    public void clear() {
        int i8 = this.f20903c;
        if (i8 > 0) {
            int[] iArr = this.f20901a;
            Object[] objArr = this.f20902b;
            this.f20901a = c.f20866a;
            this.f20902b = c.f20868c;
            this.f20903c = 0;
            d(iArr, objArr, i8);
        }
        if (this.f20903c > 0) {
            throw new ConcurrentModificationException();
        }
    }

    public boolean containsKey(Object obj) {
        return h(obj) >= 0;
    }

    public boolean containsValue(Object obj) {
        return j(obj) >= 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof g) {
            g gVar = (g) obj;
            if (size() != gVar.size()) {
                return false;
            }
            for (int i8 = 0; i8 < this.f20903c; i8++) {
                try {
                    K k8 = k(i8);
                    V o5 = o(i8);
                    Object obj2 = gVar.get(k8);
                    if (o5 == null) {
                        if (obj2 != null || !gVar.containsKey(k8)) {
                            return false;
                        }
                    } else if (!o5.equals(obj2)) {
                        return false;
                    }
                } catch (ClassCastException | NullPointerException unused) {
                    return false;
                }
            }
            return true;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (size() != map.size()) {
                return false;
            }
            for (int i9 = 0; i9 < this.f20903c; i9++) {
                try {
                    K k9 = k(i9);
                    V o8 = o(i9);
                    Object obj3 = map.get(k9);
                    if (o8 == null) {
                        if (obj3 != null || !map.containsKey(k9)) {
                            return false;
                        }
                    } else if (!o8.equals(obj3)) {
                        return false;
                    }
                } catch (ClassCastException | NullPointerException unused2) {
                }
            }
            return true;
        }
        return false;
    }

    int f(Object obj, int i8) {
        int i9 = this.f20903c;
        if (i9 == 0) {
            return -1;
        }
        int b9 = b(this.f20901a, i9, i8);
        if (b9 >= 0 && !obj.equals(this.f20902b[b9 << 1])) {
            int i10 = b9 + 1;
            while (i10 < i9 && this.f20901a[i10] == i8) {
                if (obj.equals(this.f20902b[i10 << 1])) {
                    return i10;
                }
                i10++;
            }
            for (int i11 = b9 - 1; i11 >= 0 && this.f20901a[i11] == i8; i11--) {
                if (obj.equals(this.f20902b[i11 << 1])) {
                    return i11;
                }
            }
            return ~i10;
        }
        return b9;
    }

    public V get(Object obj) {
        return getOrDefault(obj, null);
    }

    public V getOrDefault(Object obj, V v8) {
        int h8 = h(obj);
        return h8 >= 0 ? (V) this.f20902b[(h8 << 1) + 1] : v8;
    }

    public int h(Object obj) {
        return obj == null ? i() : f(obj, obj.hashCode());
    }

    public int hashCode() {
        int[] iArr = this.f20901a;
        Object[] objArr = this.f20902b;
        int i8 = this.f20903c;
        int i9 = 1;
        int i10 = 0;
        int i11 = 0;
        while (i10 < i8) {
            Object obj = objArr[i9];
            i11 += (obj == null ? 0 : obj.hashCode()) ^ iArr[i10];
            i10++;
            i9 += 2;
        }
        return i11;
    }

    int i() {
        int i8 = this.f20903c;
        if (i8 == 0) {
            return -1;
        }
        int b9 = b(this.f20901a, i8, 0);
        if (b9 >= 0 && this.f20902b[b9 << 1] != null) {
            int i9 = b9 + 1;
            while (i9 < i8 && this.f20901a[i9] == 0) {
                if (this.f20902b[i9 << 1] == null) {
                    return i9;
                }
                i9++;
            }
            for (int i10 = b9 - 1; i10 >= 0 && this.f20901a[i10] == 0; i10--) {
                if (this.f20902b[i10 << 1] == null) {
                    return i10;
                }
            }
            return ~i9;
        }
        return b9;
    }

    public boolean isEmpty() {
        return this.f20903c <= 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int j(Object obj) {
        int i8 = this.f20903c * 2;
        Object[] objArr = this.f20902b;
        if (obj == null) {
            for (int i9 = 1; i9 < i8; i9 += 2) {
                if (objArr[i9] == null) {
                    return i9 >> 1;
                }
            }
            return -1;
        }
        for (int i10 = 1; i10 < i8; i10 += 2) {
            if (obj.equals(objArr[i10])) {
                return i10 >> 1;
            }
        }
        return -1;
    }

    public K k(int i8) {
        return (K) this.f20902b[i8 << 1];
    }

    public void l(g<? extends K, ? extends V> gVar) {
        int i8 = gVar.f20903c;
        c(this.f20903c + i8);
        if (this.f20903c != 0) {
            for (int i9 = 0; i9 < i8; i9++) {
                put(gVar.k(i9), gVar.o(i9));
            }
        } else if (i8 > 0) {
            System.arraycopy(gVar.f20901a, 0, this.f20901a, 0, i8);
            System.arraycopy(gVar.f20902b, 0, this.f20902b, 0, i8 << 1);
            this.f20903c = i8;
        }
    }

    public V m(int i8) {
        Object[] objArr = this.f20902b;
        int i9 = i8 << 1;
        V v8 = (V) objArr[i9 + 1];
        int i10 = this.f20903c;
        int i11 = 0;
        if (i10 <= 1) {
            d(this.f20901a, objArr, i10);
            this.f20901a = c.f20866a;
            this.f20902b = c.f20868c;
        } else {
            int i12 = i10 - 1;
            int[] iArr = this.f20901a;
            if (iArr.length <= 8 || i10 >= iArr.length / 3) {
                if (i8 < i12) {
                    int i13 = i8 + 1;
                    int i14 = i12 - i8;
                    System.arraycopy(iArr, i13, iArr, i8, i14);
                    Object[] objArr2 = this.f20902b;
                    System.arraycopy(objArr2, i13 << 1, objArr2, i9, i14 << 1);
                }
                Object[] objArr3 = this.f20902b;
                int i15 = i12 << 1;
                objArr3[i15] = null;
                objArr3[i15 + 1] = null;
            } else {
                a(i10 > 8 ? i10 + (i10 >> 1) : 8);
                if (i10 != this.f20903c) {
                    throw new ConcurrentModificationException();
                }
                if (i8 > 0) {
                    System.arraycopy(iArr, 0, this.f20901a, 0, i8);
                    System.arraycopy(objArr, 0, this.f20902b, 0, i9);
                }
                if (i8 < i12) {
                    int i16 = i8 + 1;
                    int i17 = i12 - i8;
                    System.arraycopy(iArr, i16, this.f20901a, i8, i17);
                    System.arraycopy(objArr, i16 << 1, this.f20902b, i9, i17 << 1);
                }
            }
            i11 = i12;
        }
        if (i10 == this.f20903c) {
            this.f20903c = i11;
            return v8;
        }
        throw new ConcurrentModificationException();
    }

    public V n(int i8, V v8) {
        int i9 = (i8 << 1) + 1;
        Object[] objArr = this.f20902b;
        V v9 = (V) objArr[i9];
        objArr[i9] = v8;
        return v9;
    }

    public V o(int i8) {
        return (V) this.f20902b[(i8 << 1) + 1];
    }

    public V put(K k8, V v8) {
        int i8;
        int f5;
        int i9 = this.f20903c;
        if (k8 == null) {
            f5 = i();
            i8 = 0;
        } else {
            int hashCode = k8.hashCode();
            i8 = hashCode;
            f5 = f(k8, hashCode);
        }
        if (f5 >= 0) {
            int i10 = (f5 << 1) + 1;
            Object[] objArr = this.f20902b;
            V v9 = (V) objArr[i10];
            objArr[i10] = v8;
            return v9;
        }
        int i11 = ~f5;
        int[] iArr = this.f20901a;
        if (i9 >= iArr.length) {
            int i12 = 4;
            if (i9 >= 8) {
                i12 = (i9 >> 1) + i9;
            } else if (i9 >= 4) {
                i12 = 8;
            }
            Object[] objArr2 = this.f20902b;
            a(i12);
            if (i9 != this.f20903c) {
                throw new ConcurrentModificationException();
            }
            int[] iArr2 = this.f20901a;
            if (iArr2.length > 0) {
                System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                System.arraycopy(objArr2, 0, this.f20902b, 0, objArr2.length);
            }
            d(iArr, objArr2, i9);
        }
        if (i11 < i9) {
            int[] iArr3 = this.f20901a;
            int i13 = i11 + 1;
            System.arraycopy(iArr3, i11, iArr3, i13, i9 - i11);
            Object[] objArr3 = this.f20902b;
            System.arraycopy(objArr3, i11 << 1, objArr3, i13 << 1, (this.f20903c - i11) << 1);
        }
        int i14 = this.f20903c;
        if (i9 == i14) {
            int[] iArr4 = this.f20901a;
            if (i11 < iArr4.length) {
                iArr4[i11] = i8;
                Object[] objArr4 = this.f20902b;
                int i15 = i11 << 1;
                objArr4[i15] = k8;
                objArr4[i15 + 1] = v8;
                this.f20903c = i14 + 1;
                return null;
            }
        }
        throw new ConcurrentModificationException();
    }

    public V putIfAbsent(K k8, V v8) {
        V v9 = get(k8);
        return v9 == null ? put(k8, v8) : v9;
    }

    public V remove(Object obj) {
        int h8 = h(obj);
        if (h8 >= 0) {
            return m(h8);
        }
        return null;
    }

    public boolean remove(Object obj, Object obj2) {
        int h8 = h(obj);
        if (h8 >= 0) {
            V o5 = o(h8);
            if (obj2 == o5 || (obj2 != null && obj2.equals(o5))) {
                m(h8);
                return true;
            }
            return false;
        }
        return false;
    }

    public V replace(K k8, V v8) {
        int h8 = h(k8);
        if (h8 >= 0) {
            return n(h8, v8);
        }
        return null;
    }

    public boolean replace(K k8, V v8, V v9) {
        int h8 = h(k8);
        if (h8 >= 0) {
            V o5 = o(h8);
            if (o5 == v8 || (v8 != null && v8.equals(o5))) {
                n(h8, v9);
                return true;
            }
            return false;
        }
        return false;
    }

    public int size() {
        return this.f20903c;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.f20903c * 28);
        sb.append('{');
        for (int i8 = 0; i8 < this.f20903c; i8++) {
            if (i8 > 0) {
                sb.append(", ");
            }
            K k8 = k(i8);
            if (k8 != this) {
                sb.append(k8);
            } else {
                sb.append("(this Map)");
            }
            sb.append('=');
            V o5 = o(i8);
            if (o5 != this) {
                sb.append(o5);
            } else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
