package z5;

import b6.l0;
import com.google.android.exoplayer2.i2;
import i4.g0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b0 {

    /* renamed from: a  reason: collision with root package name */
    public final int f24603a;

    /* renamed from: b  reason: collision with root package name */
    public final g0[] f24604b;

    /* renamed from: c  reason: collision with root package name */
    public final r[] f24605c;

    /* renamed from: d  reason: collision with root package name */
    public final i2 f24606d;

    /* renamed from: e  reason: collision with root package name */
    public final Object f24607e;

    public b0(g0[] g0VarArr, r[] rVarArr, i2 i2Var, Object obj) {
        this.f24604b = g0VarArr;
        this.f24605c = (r[]) rVarArr.clone();
        this.f24606d = i2Var;
        this.f24607e = obj;
        this.f24603a = g0VarArr.length;
    }

    public boolean a(b0 b0Var) {
        if (b0Var == null || b0Var.f24605c.length != this.f24605c.length) {
            return false;
        }
        for (int i8 = 0; i8 < this.f24605c.length; i8++) {
            if (!b(b0Var, i8)) {
                return false;
            }
        }
        return true;
    }

    public boolean b(b0 b0Var, int i8) {
        return b0Var != null && l0.c(this.f24604b[i8], b0Var.f24604b[i8]) && l0.c(this.f24605c[i8], b0Var.f24605c[i8]);
    }

    public boolean c(int i8) {
        return this.f24604b[i8] != null;
    }
}
