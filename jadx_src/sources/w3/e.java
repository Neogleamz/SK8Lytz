package w3;

import android.content.Context;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig;
import d4.q;
import e4.m0;
import e4.n0;
import e4.u0;
import java.util.concurrent.Executor;
import w3.u;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e extends u {

    /* renamed from: a  reason: collision with root package name */
    private bj.a<Executor> f23495a;

    /* renamed from: b  reason: collision with root package name */
    private bj.a<Context> f23496b;

    /* renamed from: c  reason: collision with root package name */
    private bj.a f23497c;

    /* renamed from: d  reason: collision with root package name */
    private bj.a f23498d;

    /* renamed from: e  reason: collision with root package name */
    private bj.a f23499e;

    /* renamed from: f  reason: collision with root package name */
    private bj.a<String> f23500f;

    /* renamed from: g  reason: collision with root package name */
    private bj.a<m0> f23501g;

    /* renamed from: h  reason: collision with root package name */
    private bj.a<SchedulerConfig> f23502h;

    /* renamed from: j  reason: collision with root package name */
    private bj.a<d4.v> f23503j;

    /* renamed from: k  reason: collision with root package name */
    private bj.a<c4.c> f23504k;

    /* renamed from: l  reason: collision with root package name */
    private bj.a<d4.p> f23505l;

    /* renamed from: m  reason: collision with root package name */
    private bj.a<d4.t> f23506m;

    /* renamed from: n  reason: collision with root package name */
    private bj.a<t> f23507n;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements u.a {

        /* renamed from: a  reason: collision with root package name */
        private Context f23508a;

        private b() {
        }

        @Override // w3.u.a
        public u a() {
            y3.d.a(this.f23508a, Context.class);
            return new e(this.f23508a);
        }

        @Override // w3.u.a
        /* renamed from: c */
        public b b(Context context) {
            this.f23508a = (Context) y3.d.b(context);
            return this;
        }
    }

    private e(Context context) {
        d(context);
    }

    public static u.a c() {
        return new b();
    }

    private void d(Context context) {
        this.f23495a = y3.a.a(k.a());
        y3.b a9 = y3.c.a(context);
        this.f23496b = a9;
        x3.h a10 = x3.h.a(a9, g4.c.a(), g4.d.a());
        this.f23497c = a10;
        this.f23498d = y3.a.a(x3.j.a(this.f23496b, a10));
        this.f23499e = u0.a(this.f23496b, e4.g.a(), e4.i.a());
        this.f23500f = y3.a.a(e4.h.a(this.f23496b));
        this.f23501g = y3.a.a(n0.a(g4.c.a(), g4.d.a(), e4.j.a(), this.f23499e, this.f23500f));
        c4.g b9 = c4.g.b(g4.c.a());
        this.f23502h = b9;
        c4.i a11 = c4.i.a(this.f23496b, this.f23501g, b9, g4.d.a());
        this.f23503j = a11;
        bj.a<Executor> aVar = this.f23495a;
        bj.a aVar2 = this.f23498d;
        bj.a<m0> aVar3 = this.f23501g;
        this.f23504k = c4.d.a(aVar, aVar2, a11, aVar3, aVar3);
        bj.a<Context> aVar4 = this.f23496b;
        bj.a aVar5 = this.f23498d;
        bj.a<m0> aVar6 = this.f23501g;
        this.f23505l = q.a(aVar4, aVar5, aVar6, this.f23503j, this.f23495a, aVar6, g4.c.a(), g4.d.a(), this.f23501g);
        bj.a<Executor> aVar7 = this.f23495a;
        bj.a<m0> aVar8 = this.f23501g;
        this.f23506m = d4.u.a(aVar7, aVar8, this.f23503j, aVar8);
        this.f23507n = y3.a.a(v.a(g4.c.a(), g4.d.a(), this.f23504k, this.f23505l, this.f23506m));
    }

    @Override // w3.u
    e4.d a() {
        return (e4.d) this.f23501g.get();
    }

    @Override // w3.u
    t b() {
        return (t) this.f23507n.get();
    }
}
