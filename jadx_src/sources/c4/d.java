package c4;

import d4.v;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d implements y3.b<c> {

    /* renamed from: a  reason: collision with root package name */
    private final bj.a<Executor> f8313a;

    /* renamed from: b  reason: collision with root package name */
    private final bj.a<x3.d> f8314b;

    /* renamed from: c  reason: collision with root package name */
    private final bj.a<v> f8315c;

    /* renamed from: d  reason: collision with root package name */
    private final bj.a<e4.d> f8316d;

    /* renamed from: e  reason: collision with root package name */
    private final bj.a<f4.a> f8317e;

    public d(bj.a<Executor> aVar, bj.a<x3.d> aVar2, bj.a<v> aVar3, bj.a<e4.d> aVar4, bj.a<f4.a> aVar5) {
        this.f8313a = aVar;
        this.f8314b = aVar2;
        this.f8315c = aVar3;
        this.f8316d = aVar4;
        this.f8317e = aVar5;
    }

    public static d a(bj.a<Executor> aVar, bj.a<x3.d> aVar2, bj.a<v> aVar3, bj.a<e4.d> aVar4, bj.a<f4.a> aVar5) {
        return new d(aVar, aVar2, aVar3, aVar4, aVar5);
    }

    public static c c(Executor executor, x3.d dVar, v vVar, e4.d dVar2, f4.a aVar) {
        return new c(executor, dVar, vVar, dVar2, aVar);
    }

    /* renamed from: b */
    public c get() {
        return c((Executor) this.f8313a.get(), (x3.d) this.f8314b.get(), (v) this.f8315c.get(), (e4.d) this.f8316d.get(), (f4.a) this.f8317e.get());
    }
}
