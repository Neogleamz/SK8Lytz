package x3;

import android.content.Context;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h implements y3.b<g> {

    /* renamed from: a  reason: collision with root package name */
    private final bj.a<Context> f23783a;

    /* renamed from: b  reason: collision with root package name */
    private final bj.a<g4.a> f23784b;

    /* renamed from: c  reason: collision with root package name */
    private final bj.a<g4.a> f23785c;

    public h(bj.a<Context> aVar, bj.a<g4.a> aVar2, bj.a<g4.a> aVar3) {
        this.f23783a = aVar;
        this.f23784b = aVar2;
        this.f23785c = aVar3;
    }

    public static h a(bj.a<Context> aVar, bj.a<g4.a> aVar2, bj.a<g4.a> aVar3) {
        return new h(aVar, aVar2, aVar3);
    }

    public static g c(Context context, g4.a aVar, g4.a aVar2) {
        return new g(context, aVar, aVar2);
    }

    /* renamed from: b */
    public g get() {
        return c((Context) this.f23783a.get(), (g4.a) this.f23784b.get(), (g4.a) this.f23785c.get());
    }
}
