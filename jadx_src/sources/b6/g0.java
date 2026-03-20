package b6;

import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g0<V> {

    /* renamed from: a  reason: collision with root package name */
    private long[] f8047a;

    /* renamed from: b  reason: collision with root package name */
    private V[] f8048b;

    /* renamed from: c  reason: collision with root package name */
    private int f8049c;

    /* renamed from: d  reason: collision with root package name */
    private int f8050d;

    public g0() {
        this(10);
    }

    public g0(int i8) {
        this.f8047a = new long[i8];
        this.f8048b = (V[]) f(i8);
    }

    private void b(long j8, V v8) {
        int i8 = this.f8049c;
        int i9 = this.f8050d;
        V[] vArr = this.f8048b;
        int length = (i8 + i9) % vArr.length;
        this.f8047a[length] = j8;
        vArr[length] = v8;
        this.f8050d = i9 + 1;
    }

    private void d(long j8) {
        int i8 = this.f8050d;
        if (i8 > 0) {
            if (j8 <= this.f8047a[((this.f8049c + i8) - 1) % this.f8048b.length]) {
                c();
            }
        }
    }

    private void e() {
        int length = this.f8048b.length;
        if (this.f8050d < length) {
            return;
        }
        int i8 = length * 2;
        long[] jArr = new long[i8];
        V[] vArr = (V[]) f(i8);
        int i9 = this.f8049c;
        int i10 = length - i9;
        System.arraycopy(this.f8047a, i9, jArr, 0, i10);
        System.arraycopy(this.f8048b, this.f8049c, vArr, 0, i10);
        int i11 = this.f8049c;
        if (i11 > 0) {
            System.arraycopy(this.f8047a, 0, jArr, i10, i11);
            System.arraycopy(this.f8048b, 0, vArr, i10, this.f8049c);
        }
        this.f8047a = jArr;
        this.f8048b = vArr;
        this.f8049c = 0;
    }

    private static <V> V[] f(int i8) {
        return (V[]) new Object[i8];
    }

    private V h(long j8, boolean z4) {
        V v8 = null;
        long j9 = Long.MAX_VALUE;
        while (this.f8050d > 0) {
            long j10 = j8 - this.f8047a[this.f8049c];
            if (j10 < 0 && (z4 || (-j10) >= j9)) {
                break;
            }
            v8 = k();
            j9 = j10;
        }
        return v8;
    }

    private V k() {
        a.f(this.f8050d > 0);
        V[] vArr = this.f8048b;
        int i8 = this.f8049c;
        V v8 = vArr[i8];
        vArr[i8] = null;
        this.f8049c = (i8 + 1) % vArr.length;
        this.f8050d--;
        return v8;
    }

    public synchronized void a(long j8, V v8) {
        d(j8);
        e();
        b(j8, v8);
    }

    public synchronized void c() {
        this.f8049c = 0;
        this.f8050d = 0;
        Arrays.fill(this.f8048b, (Object) null);
    }

    public synchronized V g(long j8) {
        return h(j8, false);
    }

    public synchronized V i() {
        return this.f8050d == 0 ? null : k();
    }

    public synchronized V j(long j8) {
        return h(j8, true);
    }

    public synchronized int l() {
        return this.f8050d;
    }
}
