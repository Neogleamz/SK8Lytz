package m5;

import android.util.SparseArray;
import b6.h0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q {

    /* renamed from: a  reason: collision with root package name */
    private final SparseArray<h0> f21961a = new SparseArray<>();

    public h0 a(int i8) {
        h0 h0Var = this.f21961a.get(i8);
        if (h0Var == null) {
            h0 h0Var2 = new h0(9223372036854775806L);
            this.f21961a.put(i8, h0Var2);
            return h0Var2;
        }
        return h0Var;
    }

    public void b() {
        this.f21961a.clear();
    }
}
