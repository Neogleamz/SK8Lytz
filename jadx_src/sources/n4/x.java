package n4;

import b6.l0;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x implements z {

    /* renamed from: a  reason: collision with root package name */
    private final long[] f22148a;

    /* renamed from: b  reason: collision with root package name */
    private final long[] f22149b;

    /* renamed from: c  reason: collision with root package name */
    private final long f22150c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f22151d;

    public x(long[] jArr, long[] jArr2, long j8) {
        b6.a.a(jArr.length == jArr2.length);
        int length = jArr2.length;
        boolean z4 = length > 0;
        this.f22151d = z4;
        if (!z4 || jArr2[0] <= 0) {
            this.f22148a = jArr;
            this.f22149b = jArr2;
        } else {
            int i8 = length + 1;
            long[] jArr3 = new long[i8];
            this.f22148a = jArr3;
            long[] jArr4 = new long[i8];
            this.f22149b = jArr4;
            System.arraycopy(jArr, 0, jArr3, 1, length);
            System.arraycopy(jArr2, 0, jArr4, 1, length);
        }
        this.f22150c = j8;
    }

    @Override // n4.z
    public long d() {
        return this.f22150c;
    }

    @Override // n4.z
    public boolean h() {
        return this.f22151d;
    }

    @Override // n4.z
    public z.a i(long j8) {
        if (this.f22151d) {
            int i8 = l0.i(this.f22149b, j8, true, true);
            a0 a0Var = new a0(this.f22149b[i8], this.f22148a[i8]);
            if (a0Var.f22046a == j8 || i8 == this.f22149b.length - 1) {
                return new z.a(a0Var);
            }
            int i9 = i8 + 1;
            return new z.a(a0Var, new a0(this.f22149b[i9], this.f22148a[i9]));
        }
        return new z.a(a0.f22045c);
    }
}
