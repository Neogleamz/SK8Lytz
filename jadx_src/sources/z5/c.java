package z5;

import android.os.SystemClock;
import b6.l0;
import com.google.android.exoplayer2.w0;
import j5.n;
import java.util.Arrays;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class c implements r {

    /* renamed from: a  reason: collision with root package name */
    protected final h5.u f24608a;

    /* renamed from: b  reason: collision with root package name */
    protected final int f24609b;

    /* renamed from: c  reason: collision with root package name */
    protected final int[] f24610c;

    /* renamed from: d  reason: collision with root package name */
    private final int f24611d;

    /* renamed from: e  reason: collision with root package name */
    private final w0[] f24612e;

    /* renamed from: f  reason: collision with root package name */
    private final long[] f24613f;

    /* renamed from: g  reason: collision with root package name */
    private int f24614g;

    public c(h5.u uVar, int... iArr) {
        this(uVar, iArr, 0);
    }

    public c(h5.u uVar, int[] iArr, int i8) {
        int i9 = 0;
        b6.a.f(iArr.length > 0);
        this.f24611d = i8;
        this.f24608a = (h5.u) b6.a.e(uVar);
        int length = iArr.length;
        this.f24609b = length;
        this.f24612e = new w0[length];
        for (int i10 = 0; i10 < iArr.length; i10++) {
            this.f24612e[i10] = uVar.b(iArr[i10]);
        }
        Arrays.sort(this.f24612e, b.a);
        this.f24610c = new int[this.f24609b];
        while (true) {
            int i11 = this.f24609b;
            if (i9 >= i11) {
                this.f24613f = new long[i11];
                return;
            } else {
                this.f24610c[i9] = uVar.c(this.f24612e[i9]);
                i9++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int w(w0 w0Var, w0 w0Var2) {
        return w0Var2.f11203h - w0Var.f11203h;
    }

    @Override // z5.u
    public final h5.u b() {
        return this.f24608a;
    }

    @Override // z5.r
    public boolean d(int i8, long j8) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        boolean e8 = e(i8, elapsedRealtime);
        int i9 = 0;
        while (i9 < this.f24609b && !e8) {
            e8 = (i9 == i8 || e(i9, elapsedRealtime)) ? false : true;
            i9++;
        }
        if (e8) {
            long[] jArr = this.f24613f;
            jArr[i8] = Math.max(jArr[i8], l0.b(elapsedRealtime, j8, Long.MAX_VALUE));
            return true;
        }
        return false;
    }

    @Override // z5.r
    public boolean e(int i8, long j8) {
        return this.f24613f[i8] > j8;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        c cVar = (c) obj;
        return this.f24608a == cVar.f24608a && Arrays.equals(this.f24610c, cVar.f24610c);
    }

    @Override // z5.r
    public void g() {
    }

    @Override // z5.u
    public final w0 h(int i8) {
        return this.f24612e[i8];
    }

    public int hashCode() {
        if (this.f24614g == 0) {
            this.f24614g = (System.identityHashCode(this.f24608a) * 31) + Arrays.hashCode(this.f24610c);
        }
        return this.f24614g;
    }

    @Override // z5.r
    public void i() {
    }

    @Override // z5.u
    public final int j(int i8) {
        return this.f24610c[i8];
    }

    @Override // z5.r
    public int k(long j8, List<? extends n> list) {
        return list.size();
    }

    @Override // z5.u
    public final int l(w0 w0Var) {
        for (int i8 = 0; i8 < this.f24609b; i8++) {
            if (this.f24612e[i8] == w0Var) {
                return i8;
            }
        }
        return -1;
    }

    @Override // z5.u
    public final int length() {
        return this.f24610c.length;
    }

    @Override // z5.r
    public final int m() {
        return this.f24610c[c()];
    }

    @Override // z5.r
    public final w0 n() {
        return this.f24612e[c()];
    }

    @Override // z5.r
    public void q(float f5) {
    }

    @Override // z5.u
    public final int u(int i8) {
        for (int i9 = 0; i9 < this.f24609b; i9++) {
            if (this.f24610c[i9] == i8) {
                return i9;
            }
        }
        return -1;
    }
}
