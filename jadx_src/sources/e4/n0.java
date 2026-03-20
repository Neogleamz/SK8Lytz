package e4;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n0 implements y3.b<m0> {

    /* renamed from: a  reason: collision with root package name */
    private final bj.a<g4.a> f19778a;

    /* renamed from: b  reason: collision with root package name */
    private final bj.a<g4.a> f19779b;

    /* renamed from: c  reason: collision with root package name */
    private final bj.a<e> f19780c;

    /* renamed from: d  reason: collision with root package name */
    private final bj.a<t0> f19781d;

    /* renamed from: e  reason: collision with root package name */
    private final bj.a<String> f19782e;

    public n0(bj.a<g4.a> aVar, bj.a<g4.a> aVar2, bj.a<e> aVar3, bj.a<t0> aVar4, bj.a<String> aVar5) {
        this.f19778a = aVar;
        this.f19779b = aVar2;
        this.f19780c = aVar3;
        this.f19781d = aVar4;
        this.f19782e = aVar5;
    }

    public static n0 a(bj.a<g4.a> aVar, bj.a<g4.a> aVar2, bj.a<e> aVar3, bj.a<t0> aVar4, bj.a<String> aVar5) {
        return new n0(aVar, aVar2, aVar3, aVar4, aVar5);
    }

    public static m0 c(g4.a aVar, g4.a aVar2, Object obj, Object obj2, bj.a<String> aVar3) {
        return new m0(aVar, aVar2, (e) obj, (t0) obj2, aVar3);
    }

    /* renamed from: b */
    public m0 get() {
        return c((g4.a) this.f19778a.get(), (g4.a) this.f19779b.get(), this.f19780c.get(), this.f19781d.get(), this.f19782e);
    }
}
