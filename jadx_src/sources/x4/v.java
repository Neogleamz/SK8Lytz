package x4;

import b6.a;
import b6.l0;
import b6.z;
import com.google.android.exoplayer2.w0;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class v implements b0 {

    /* renamed from: a  reason: collision with root package name */
    private w0 f24125a;

    /* renamed from: b  reason: collision with root package name */
    private b6.h0 f24126b;

    /* renamed from: c  reason: collision with root package name */
    private n4.b0 f24127c;

    public v(String str) {
        this.f24125a = new w0.b().g0(str).G();
    }

    private void c() {
        a.h(this.f24126b);
        l0.j(this.f24127c);
    }

    @Override // x4.b0
    public void a(b6.h0 h0Var, n4.m mVar, i0.d dVar) {
        this.f24126b = h0Var;
        dVar.a();
        n4.b0 e8 = mVar.e(dVar.c(), 5);
        this.f24127c = e8;
        e8.f(this.f24125a);
    }

    @Override // x4.b0
    public void b(z zVar) {
        c();
        long d8 = this.f24126b.d();
        long e8 = this.f24126b.e();
        if (d8 == -9223372036854775807L || e8 == -9223372036854775807L) {
            return;
        }
        w0 w0Var = this.f24125a;
        if (e8 != w0Var.f11210t) {
            w0 G = w0Var.b().k0(e8).G();
            this.f24125a = G;
            this.f24127c.f(G);
        }
        int a9 = zVar.a();
        this.f24127c.b(zVar, a9);
        this.f24127c.d(d8, 1, a9, 0, null);
    }
}
