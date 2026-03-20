package v4;

import b6.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class r {

    /* renamed from: a  reason: collision with root package name */
    public final o f23332a;

    /* renamed from: b  reason: collision with root package name */
    public final int f23333b;

    /* renamed from: c  reason: collision with root package name */
    public final long[] f23334c;

    /* renamed from: d  reason: collision with root package name */
    public final int[] f23335d;

    /* renamed from: e  reason: collision with root package name */
    public final int f23336e;

    /* renamed from: f  reason: collision with root package name */
    public final long[] f23337f;

    /* renamed from: g  reason: collision with root package name */
    public final int[] f23338g;

    /* renamed from: h  reason: collision with root package name */
    public final long f23339h;

    public r(o oVar, long[] jArr, int[] iArr, int i8, long[] jArr2, int[] iArr2, long j8) {
        b6.a.a(iArr.length == jArr2.length);
        b6.a.a(jArr.length == jArr2.length);
        b6.a.a(iArr2.length == jArr2.length);
        this.f23332a = oVar;
        this.f23334c = jArr;
        this.f23335d = iArr;
        this.f23336e = i8;
        this.f23337f = jArr2;
        this.f23338g = iArr2;
        this.f23339h = j8;
        this.f23333b = jArr.length;
        if (iArr2.length > 0) {
            int length = iArr2.length - 1;
            iArr2[length] = iArr2[length] | 536870912;
        }
    }

    public int a(long j8) {
        for (int i8 = l0.i(this.f23337f, j8, true, false); i8 >= 0; i8--) {
            if ((this.f23338g[i8] & 1) != 0) {
                return i8;
            }
        }
        return -1;
    }

    public int b(long j8) {
        for (int e8 = l0.e(this.f23337f, j8, true, false); e8 < this.f23337f.length; e8++) {
            if ((this.f23338g[e8] & 1) != 0) {
                return e8;
            }
        }
        return -1;
    }
}
