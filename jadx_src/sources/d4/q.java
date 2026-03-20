package d4;

import android.content.Context;
import java.util.concurrent.Executor;
import x3.d;
import y3.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q implements b<p> {

    /* renamed from: a  reason: collision with root package name */
    private final bj.a<Context> f19720a;

    /* renamed from: b  reason: collision with root package name */
    private final bj.a<d> f19721b;

    /* renamed from: c  reason: collision with root package name */
    private final bj.a<e4.d> f19722c;

    /* renamed from: d  reason: collision with root package name */
    private final bj.a<v> f19723d;

    /* renamed from: e  reason: collision with root package name */
    private final bj.a<Executor> f19724e;

    /* renamed from: f  reason: collision with root package name */
    private final bj.a<f4.a> f19725f;

    /* renamed from: g  reason: collision with root package name */
    private final bj.a<g4.a> f19726g;

    /* renamed from: h  reason: collision with root package name */
    private final bj.a<g4.a> f19727h;

    /* renamed from: i  reason: collision with root package name */
    private final bj.a<e4.c> f19728i;

    public q(bj.a<Context> aVar, bj.a<d> aVar2, bj.a<e4.d> aVar3, bj.a<v> aVar4, bj.a<Executor> aVar5, bj.a<f4.a> aVar6, bj.a<g4.a> aVar7, bj.a<g4.a> aVar8, bj.a<e4.c> aVar9) {
        this.f19720a = aVar;
        this.f19721b = aVar2;
        this.f19722c = aVar3;
        this.f19723d = aVar4;
        this.f19724e = aVar5;
        this.f19725f = aVar6;
        this.f19726g = aVar7;
        this.f19727h = aVar8;
        this.f19728i = aVar9;
    }

    public static q a(bj.a<Context> aVar, bj.a<d> aVar2, bj.a<e4.d> aVar3, bj.a<v> aVar4, bj.a<Executor> aVar5, bj.a<f4.a> aVar6, bj.a<g4.a> aVar7, bj.a<g4.a> aVar8, bj.a<e4.c> aVar9) {
        return new q(aVar, aVar2, aVar3, aVar4, aVar5, aVar6, aVar7, aVar8, aVar9);
    }

    public static p c(Context context, d dVar, e4.d dVar2, v vVar, Executor executor, f4.a aVar, g4.a aVar2, g4.a aVar3, e4.c cVar) {
        return new p(context, dVar, dVar2, vVar, executor, aVar, aVar2, aVar3, cVar);
    }

    /* renamed from: b */
    public p get() {
        return c((Context) this.f19720a.get(), (d) this.f19721b.get(), (e4.d) this.f19722c.get(), (v) this.f19723d.get(), (Executor) this.f19724e.get(), (f4.a) this.f19725f.get(), (g4.a) this.f19726g.get(), (g4.a) this.f19727h.get(), (e4.c) this.f19728i.get());
    }
}
