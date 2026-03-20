package d4;

import e4.d;
import java.util.concurrent.Executor;
import y3.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u implements b<t> {

    /* renamed from: a  reason: collision with root package name */
    private final bj.a<Executor> f19733a;

    /* renamed from: b  reason: collision with root package name */
    private final bj.a<d> f19734b;

    /* renamed from: c  reason: collision with root package name */
    private final bj.a<v> f19735c;

    /* renamed from: d  reason: collision with root package name */
    private final bj.a<f4.a> f19736d;

    public u(bj.a<Executor> aVar, bj.a<d> aVar2, bj.a<v> aVar3, bj.a<f4.a> aVar4) {
        this.f19733a = aVar;
        this.f19734b = aVar2;
        this.f19735c = aVar3;
        this.f19736d = aVar4;
    }

    public static u a(bj.a<Executor> aVar, bj.a<d> aVar2, bj.a<v> aVar3, bj.a<f4.a> aVar4) {
        return new u(aVar, aVar2, aVar3, aVar4);
    }

    public static t c(Executor executor, d dVar, v vVar, f4.a aVar) {
        return new t(executor, dVar, vVar, aVar);
    }

    /* renamed from: b */
    public t get() {
        return c((Executor) this.f19733a.get(), (d) this.f19734b.get(), (v) this.f19735c.get(), (f4.a) this.f19736d.get());
    }
}
