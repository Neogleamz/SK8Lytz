package x4;

import b6.a;
import b6.z;
import com.google.android.exoplayer2.w0;
import java.util.List;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d0 {

    /* renamed from: a  reason: collision with root package name */
    private final List<w0> f23835a;

    /* renamed from: b  reason: collision with root package name */
    private final n4.b0[] f23836b;

    public d0(List<w0> list) {
        this.f23835a = list;
        this.f23836b = new n4.b0[list.size()];
    }

    public void a(long j8, z zVar) {
        n4.b.a(j8, zVar, this.f23836b);
    }

    public void b(n4.m mVar, i0.d dVar) {
        for (int i8 = 0; i8 < this.f23836b.length; i8++) {
            dVar.a();
            n4.b0 e8 = mVar.e(dVar.c(), 3);
            w0 w0Var = this.f23835a.get(i8);
            String str = w0Var.f11207m;
            a.b("application/cea-608".equals(str) || "application/cea-708".equals(str), "Invalid closed caption mime type provided: " + str);
            String str2 = w0Var.f11196a;
            if (str2 == null) {
                str2 = dVar.b();
            }
            e8.f(new w0.b().U(str2).g0(str).i0(w0Var.f11199d).X(w0Var.f11198c).H(w0Var.O).V(w0Var.f11209p).G());
            this.f23836b[i8] = e8;
        }
    }
}
