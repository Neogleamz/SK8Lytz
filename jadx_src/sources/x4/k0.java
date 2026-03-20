package x4;

import b6.a;
import b6.z;
import com.google.android.exoplayer2.w0;
import java.util.List;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class k0 {

    /* renamed from: a  reason: collision with root package name */
    private final List<w0> f23952a;

    /* renamed from: b  reason: collision with root package name */
    private final n4.b0[] f23953b;

    public k0(List<w0> list) {
        this.f23952a = list;
        this.f23953b = new n4.b0[list.size()];
    }

    public void a(long j8, z zVar) {
        if (zVar.a() < 9) {
            return;
        }
        int q = zVar.q();
        int q8 = zVar.q();
        int H = zVar.H();
        if (q == 434 && q8 == 1195456820 && H == 3) {
            n4.b.b(j8, zVar, this.f23953b);
        }
    }

    public void b(n4.m mVar, i0.d dVar) {
        for (int i8 = 0; i8 < this.f23953b.length; i8++) {
            dVar.a();
            n4.b0 e8 = mVar.e(dVar.c(), 3);
            w0 w0Var = this.f23952a.get(i8);
            String str = w0Var.f11207m;
            a.b("application/cea-608".equals(str) || "application/cea-708".equals(str), "Invalid closed caption mime type provided: " + str);
            e8.f(new w0.b().U(dVar.b()).g0(str).i0(w0Var.f11199d).X(w0Var.f11198c).H(w0Var.O).V(w0Var.f11209p).G());
            this.f23953b[i8] = e8;
        }
    }
}
