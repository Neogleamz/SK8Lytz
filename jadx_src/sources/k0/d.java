package k0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d<E> implements Cloneable {

    /* renamed from: e  reason: collision with root package name */
    private static final Object f20869e = new Object();

    /* renamed from: a  reason: collision with root package name */
    private boolean f20870a;

    /* renamed from: b  reason: collision with root package name */
    private long[] f20871b;

    /* renamed from: c  reason: collision with root package name */
    private Object[] f20872c;

    /* renamed from: d  reason: collision with root package name */
    private int f20873d;

    public d() {
        this(10);
    }

    public d(int i8) {
        this.f20870a = false;
        if (i8 == 0) {
            this.f20871b = c.f20867b;
            this.f20872c = c.f20868c;
            return;
        }
        int f5 = c.f(i8);
        this.f20871b = new long[f5];
        this.f20872c = new Object[f5];
    }

    private void e() {
        int i8 = this.f20873d;
        long[] jArr = this.f20871b;
        Object[] objArr = this.f20872c;
        int i9 = 0;
        for (int i10 = 0; i10 < i8; i10++) {
            Object obj = objArr[i10];
            if (obj != f20869e) {
                if (i10 != i9) {
                    jArr[i9] = jArr[i10];
                    objArr[i9] = obj;
                    objArr[i10] = null;
                }
                i9++;
            }
        }
        this.f20870a = false;
        this.f20873d = i9;
    }

    public void b(long j8, E e8) {
        int i8 = this.f20873d;
        if (i8 != 0 && j8 <= this.f20871b[i8 - 1]) {
            l(j8, e8);
            return;
        }
        if (this.f20870a && i8 >= this.f20871b.length) {
            e();
        }
        int i9 = this.f20873d;
        if (i9 >= this.f20871b.length) {
            int f5 = c.f(i9 + 1);
            long[] jArr = new long[f5];
            Object[] objArr = new Object[f5];
            long[] jArr2 = this.f20871b;
            System.arraycopy(jArr2, 0, jArr, 0, jArr2.length);
            Object[] objArr2 = this.f20872c;
            System.arraycopy(objArr2, 0, objArr, 0, objArr2.length);
            this.f20871b = jArr;
            this.f20872c = objArr;
        }
        this.f20871b[i9] = j8;
        this.f20872c[i9] = e8;
        this.f20873d = i9 + 1;
    }

    public void c() {
        int i8 = this.f20873d;
        Object[] objArr = this.f20872c;
        for (int i9 = 0; i9 < i8; i9++) {
            objArr[i9] = null;
        }
        this.f20873d = 0;
        this.f20870a = false;
    }

    /* renamed from: d */
    public d<E> clone() {
        try {
            d<E> dVar = (d) super.clone();
            dVar.f20871b = (long[]) this.f20871b.clone();
            dVar.f20872c = (Object[]) this.f20872c.clone();
            return dVar;
        } catch (CloneNotSupportedException e8) {
            throw new AssertionError(e8);
        }
    }

    public E f(long j8) {
        return g(j8, null);
    }

    public E g(long j8, E e8) {
        int b9 = c.b(this.f20871b, this.f20873d, j8);
        if (b9 >= 0) {
            Object[] objArr = this.f20872c;
            if (objArr[b9] != f20869e) {
                return (E) objArr[b9];
            }
        }
        return e8;
    }

    public int j(long j8) {
        if (this.f20870a) {
            e();
        }
        return c.b(this.f20871b, this.f20873d, j8);
    }

    public long k(int i8) {
        if (this.f20870a) {
            e();
        }
        return this.f20871b[i8];
    }

    public void l(long j8, E e8) {
        int b9 = c.b(this.f20871b, this.f20873d, j8);
        if (b9 >= 0) {
            this.f20872c[b9] = e8;
            return;
        }
        int i8 = ~b9;
        int i9 = this.f20873d;
        if (i8 < i9) {
            Object[] objArr = this.f20872c;
            if (objArr[i8] == f20869e) {
                this.f20871b[i8] = j8;
                objArr[i8] = e8;
                return;
            }
        }
        if (this.f20870a && i9 >= this.f20871b.length) {
            e();
            i8 = ~c.b(this.f20871b, this.f20873d, j8);
        }
        int i10 = this.f20873d;
        if (i10 >= this.f20871b.length) {
            int f5 = c.f(i10 + 1);
            long[] jArr = new long[f5];
            Object[] objArr2 = new Object[f5];
            long[] jArr2 = this.f20871b;
            System.arraycopy(jArr2, 0, jArr, 0, jArr2.length);
            Object[] objArr3 = this.f20872c;
            System.arraycopy(objArr3, 0, objArr2, 0, objArr3.length);
            this.f20871b = jArr;
            this.f20872c = objArr2;
        }
        int i11 = this.f20873d;
        if (i11 - i8 != 0) {
            long[] jArr3 = this.f20871b;
            int i12 = i8 + 1;
            System.arraycopy(jArr3, i8, jArr3, i12, i11 - i8);
            Object[] objArr4 = this.f20872c;
            System.arraycopy(objArr4, i8, objArr4, i12, this.f20873d - i8);
        }
        this.f20871b[i8] = j8;
        this.f20872c[i8] = e8;
        this.f20873d++;
    }

    public void m(long j8) {
        int b9 = c.b(this.f20871b, this.f20873d, j8);
        if (b9 >= 0) {
            Object[] objArr = this.f20872c;
            Object obj = objArr[b9];
            Object obj2 = f20869e;
            if (obj != obj2) {
                objArr[b9] = obj2;
                this.f20870a = true;
            }
        }
    }

    public void o(int i8) {
        Object[] objArr = this.f20872c;
        Object obj = objArr[i8];
        Object obj2 = f20869e;
        if (obj != obj2) {
            objArr[i8] = obj2;
            this.f20870a = true;
        }
    }

    public int q() {
        if (this.f20870a) {
            e();
        }
        return this.f20873d;
    }

    public E r(int i8) {
        if (this.f20870a) {
            e();
        }
        return (E) this.f20872c[i8];
    }

    public String toString() {
        if (q() <= 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.f20873d * 28);
        sb.append('{');
        for (int i8 = 0; i8 < this.f20873d; i8++) {
            if (i8 > 0) {
                sb.append(", ");
            }
            sb.append(k(i8));
            sb.append('=');
            E r4 = r(i8);
            if (r4 != this) {
                sb.append(r4);
            } else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
