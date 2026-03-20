package k0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h<E> implements Cloneable {

    /* renamed from: e  reason: collision with root package name */
    private static final Object f20904e = new Object();

    /* renamed from: a  reason: collision with root package name */
    private boolean f20905a;

    /* renamed from: b  reason: collision with root package name */
    private int[] f20906b;

    /* renamed from: c  reason: collision with root package name */
    private Object[] f20907c;

    /* renamed from: d  reason: collision with root package name */
    private int f20908d;

    public h() {
        this(10);
    }

    public h(int i8) {
        this.f20905a = false;
        if (i8 == 0) {
            this.f20906b = c.f20866a;
            this.f20907c = c.f20868c;
            return;
        }
        int e8 = c.e(i8);
        this.f20906b = new int[e8];
        this.f20907c = new Object[e8];
    }

    private void e() {
        int i8 = this.f20908d;
        int[] iArr = this.f20906b;
        Object[] objArr = this.f20907c;
        int i9 = 0;
        for (int i10 = 0; i10 < i8; i10++) {
            Object obj = objArr[i10];
            if (obj != f20904e) {
                if (i10 != i9) {
                    iArr[i9] = iArr[i10];
                    objArr[i9] = obj;
                    objArr[i10] = null;
                }
                i9++;
            }
        }
        this.f20905a = false;
        this.f20908d = i9;
    }

    public void b(int i8, E e8) {
        int i9 = this.f20908d;
        if (i9 != 0 && i8 <= this.f20906b[i9 - 1]) {
            l(i8, e8);
            return;
        }
        if (this.f20905a && i9 >= this.f20906b.length) {
            e();
        }
        int i10 = this.f20908d;
        if (i10 >= this.f20906b.length) {
            int e9 = c.e(i10 + 1);
            int[] iArr = new int[e9];
            Object[] objArr = new Object[e9];
            int[] iArr2 = this.f20906b;
            System.arraycopy(iArr2, 0, iArr, 0, iArr2.length);
            Object[] objArr2 = this.f20907c;
            System.arraycopy(objArr2, 0, objArr, 0, objArr2.length);
            this.f20906b = iArr;
            this.f20907c = objArr;
        }
        this.f20906b[i10] = i8;
        this.f20907c[i10] = e8;
        this.f20908d = i10 + 1;
    }

    public void c() {
        int i8 = this.f20908d;
        Object[] objArr = this.f20907c;
        for (int i9 = 0; i9 < i8; i9++) {
            objArr[i9] = null;
        }
        this.f20908d = 0;
        this.f20905a = false;
    }

    /* renamed from: d */
    public h<E> clone() {
        try {
            h<E> hVar = (h) super.clone();
            hVar.f20906b = (int[]) this.f20906b.clone();
            hVar.f20907c = (Object[]) this.f20907c.clone();
            return hVar;
        } catch (CloneNotSupportedException e8) {
            throw new AssertionError(e8);
        }
    }

    public E f(int i8) {
        return g(i8, null);
    }

    public E g(int i8, E e8) {
        int a9 = c.a(this.f20906b, this.f20908d, i8);
        if (a9 >= 0) {
            Object[] objArr = this.f20907c;
            if (objArr[a9] != f20904e) {
                return (E) objArr[a9];
            }
        }
        return e8;
    }

    public int j(E e8) {
        if (this.f20905a) {
            e();
        }
        for (int i8 = 0; i8 < this.f20908d; i8++) {
            if (this.f20907c[i8] == e8) {
                return i8;
            }
        }
        return -1;
    }

    public int k(int i8) {
        if (this.f20905a) {
            e();
        }
        return this.f20906b[i8];
    }

    public void l(int i8, E e8) {
        int a9 = c.a(this.f20906b, this.f20908d, i8);
        if (a9 >= 0) {
            this.f20907c[a9] = e8;
            return;
        }
        int i9 = ~a9;
        int i10 = this.f20908d;
        if (i9 < i10) {
            Object[] objArr = this.f20907c;
            if (objArr[i9] == f20904e) {
                this.f20906b[i9] = i8;
                objArr[i9] = e8;
                return;
            }
        }
        if (this.f20905a && i10 >= this.f20906b.length) {
            e();
            i9 = ~c.a(this.f20906b, this.f20908d, i8);
        }
        int i11 = this.f20908d;
        if (i11 >= this.f20906b.length) {
            int e9 = c.e(i11 + 1);
            int[] iArr = new int[e9];
            Object[] objArr2 = new Object[e9];
            int[] iArr2 = this.f20906b;
            System.arraycopy(iArr2, 0, iArr, 0, iArr2.length);
            Object[] objArr3 = this.f20907c;
            System.arraycopy(objArr3, 0, objArr2, 0, objArr3.length);
            this.f20906b = iArr;
            this.f20907c = objArr2;
        }
        int i12 = this.f20908d;
        if (i12 - i9 != 0) {
            int[] iArr3 = this.f20906b;
            int i13 = i9 + 1;
            System.arraycopy(iArr3, i9, iArr3, i13, i12 - i9);
            Object[] objArr4 = this.f20907c;
            System.arraycopy(objArr4, i9, objArr4, i13, this.f20908d - i9);
        }
        this.f20906b[i9] = i8;
        this.f20907c[i9] = e8;
        this.f20908d++;
    }

    public void m(int i8) {
        Object[] objArr = this.f20907c;
        Object obj = objArr[i8];
        Object obj2 = f20904e;
        if (obj != obj2) {
            objArr[i8] = obj2;
            this.f20905a = true;
        }
    }

    public int o() {
        if (this.f20905a) {
            e();
        }
        return this.f20908d;
    }

    public E q(int i8) {
        if (this.f20905a) {
            e();
        }
        return (E) this.f20907c[i8];
    }

    public String toString() {
        if (o() <= 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.f20908d * 28);
        sb.append('{');
        for (int i8 = 0; i8 < this.f20908d; i8++) {
            if (i8 > 0) {
                sb.append(", ");
            }
            sb.append(k(i8));
            sb.append('=');
            E q = q(i8);
            if (q != this) {
                sb.append(q);
            } else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
