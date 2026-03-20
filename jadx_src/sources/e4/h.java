package e4;

import android.content.Context;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h implements y3.b<String> {

    /* renamed from: a  reason: collision with root package name */
    private final bj.a<Context> f19767a;

    public h(bj.a<Context> aVar) {
        this.f19767a = aVar;
    }

    public static h a(bj.a<Context> aVar) {
        return new h(aVar);
    }

    public static String c(Context context) {
        return (String) y3.d.c(f.b(context), "Cannot return null from a non-@Nullable @Provides method");
    }

    /* renamed from: b */
    public String get() {
        return c((Context) this.f19767a.get());
    }
}
