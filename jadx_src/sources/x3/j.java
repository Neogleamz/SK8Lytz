package x3;

import android.content.Context;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j implements y3.b<i> {

    /* renamed from: a  reason: collision with root package name */
    private final bj.a<Context> f23791a;

    /* renamed from: b  reason: collision with root package name */
    private final bj.a<g> f23792b;

    public j(bj.a<Context> aVar, bj.a<g> aVar2) {
        this.f23791a = aVar;
        this.f23792b = aVar2;
    }

    public static j a(bj.a<Context> aVar, bj.a<g> aVar2) {
        return new j(aVar, aVar2);
    }

    public static i c(Context context, Object obj) {
        return new i(context, (g) obj);
    }

    /* renamed from: b */
    public i get() {
        return c((Context) this.f23791a.get(), this.f23792b.get());
    }
}
