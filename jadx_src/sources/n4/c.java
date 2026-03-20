package n4;

import b6.l0;
import java.util.Arrays;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c implements z {

    /* renamed from: a  reason: collision with root package name */
    public final int f22052a;

    /* renamed from: b  reason: collision with root package name */
    public final int[] f22053b;

    /* renamed from: c  reason: collision with root package name */
    public final long[] f22054c;

    /* renamed from: d  reason: collision with root package name */
    public final long[] f22055d;

    /* renamed from: e  reason: collision with root package name */
    public final long[] f22056e;

    /* renamed from: f  reason: collision with root package name */
    private final long f22057f;

    public c(int[] iArr, long[] jArr, long[] jArr2, long[] jArr3) {
        this.f22053b = iArr;
        this.f22054c = jArr;
        this.f22055d = jArr2;
        this.f22056e = jArr3;
        int length = iArr.length;
        this.f22052a = length;
        if (length > 0) {
            this.f22057f = jArr2[length - 1] + jArr3[length - 1];
        } else {
            this.f22057f = 0L;
        }
    }

    public int b(long j8) {
        return l0.i(this.f22056e, j8, true, true);
    }

    @Override // n4.z
    public long d() {
        return this.f22057f;
    }

    @Override // n4.z
    public boolean h() {
        return true;
    }

    @Override // n4.z
    public z.a i(long j8) {
        int b9 = b(j8);
        a0 a0Var = new a0(this.f22056e[b9], this.f22054c[b9]);
        if (a0Var.f22046a >= j8 || b9 == this.f22052a - 1) {
            return new z.a(a0Var);
        }
        int i8 = b9 + 1;
        return new z.a(a0Var, new a0(this.f22056e[i8], this.f22054c[i8]));
    }

    public String toString() {
        return "ChunkIndex(length=" + this.f22052a + ", sizes=" + Arrays.toString(this.f22053b) + ", offsets=" + Arrays.toString(this.f22054c) + ", timeUs=" + Arrays.toString(this.f22056e) + ", durationsUs=" + Arrays.toString(this.f22055d) + ")";
    }
}
