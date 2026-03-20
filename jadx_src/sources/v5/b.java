package v5;

import b6.l0;
import java.util.Collections;
import java.util.List;
import p5.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b implements h {

    /* renamed from: a  reason: collision with root package name */
    private final p5.b[] f23343a;

    /* renamed from: b  reason: collision with root package name */
    private final long[] f23344b;

    public b(p5.b[] bVarArr, long[] jArr) {
        this.f23343a = bVarArr;
        this.f23344b = jArr;
    }

    @Override // p5.h
    public int c(long j8) {
        int e8 = l0.e(this.f23344b, j8, false, false);
        if (e8 < this.f23344b.length) {
            return e8;
        }
        return -1;
    }

    @Override // p5.h
    public long f(int i8) {
        b6.a.a(i8 >= 0);
        b6.a.a(i8 < this.f23344b.length);
        return this.f23344b[i8];
    }

    @Override // p5.h
    public List<p5.b> h(long j8) {
        int i8 = l0.i(this.f23344b, j8, true, false);
        if (i8 != -1) {
            p5.b[] bVarArr = this.f23343a;
            if (bVarArr[i8] != p5.b.f22371x) {
                return Collections.singletonList(bVarArr[i8]);
            }
        }
        return Collections.emptyList();
    }

    @Override // p5.h
    public int i() {
        return this.f23344b.length;
    }
}
