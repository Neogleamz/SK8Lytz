package m5;

import b6.h0;
import com.google.android.exoplayer2.w0;
import n4.m;
import n4.y;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b implements j {

    /* renamed from: d  reason: collision with root package name */
    private static final y f21842d = new y();

    /* renamed from: a  reason: collision with root package name */
    final n4.k f21843a;

    /* renamed from: b  reason: collision with root package name */
    private final w0 f21844b;

    /* renamed from: c  reason: collision with root package name */
    private final h0 f21845c;

    public b(n4.k kVar, w0 w0Var, h0 h0Var) {
        this.f21843a = kVar;
        this.f21844b = w0Var;
        this.f21845c = h0Var;
    }

    @Override // m5.j
    public boolean a(n4.l lVar) {
        return this.f21843a.e(lVar, f21842d) == 0;
    }

    @Override // m5.j
    public void b(m mVar) {
        this.f21843a.b(mVar);
    }

    @Override // m5.j
    public void c() {
        this.f21843a.c(0L, 0L);
    }

    @Override // m5.j
    public boolean d() {
        n4.k kVar = this.f21843a;
        return (kVar instanceof x4.h) || (kVar instanceof x4.b) || (kVar instanceof x4.e) || (kVar instanceof u4.f);
    }

    @Override // m5.j
    public boolean e() {
        n4.k kVar = this.f21843a;
        return (kVar instanceof x4.h0) || (kVar instanceof v4.g);
    }

    @Override // m5.j
    public j f() {
        n4.k fVar;
        b6.a.f(!e());
        n4.k kVar = this.f21843a;
        if (kVar instanceof r) {
            fVar = new r(this.f21844b.f11198c, this.f21845c);
        } else if (kVar instanceof x4.h) {
            fVar = new x4.h();
        } else if (kVar instanceof x4.b) {
            fVar = new x4.b();
        } else if (kVar instanceof x4.e) {
            fVar = new x4.e();
        } else if (!(kVar instanceof u4.f)) {
            throw new IllegalStateException("Unexpected extractor type for recreation: " + this.f21843a.getClass().getSimpleName());
        } else {
            fVar = new u4.f();
        }
        return new b(fVar, this.f21844b, this.f21845c);
    }
}
