package e4;

import android.content.Context;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u0 implements y3.b<t0> {

    /* renamed from: a  reason: collision with root package name */
    private final bj.a<Context> f19793a;

    /* renamed from: b  reason: collision with root package name */
    private final bj.a<String> f19794b;

    /* renamed from: c  reason: collision with root package name */
    private final bj.a<Integer> f19795c;

    public u0(bj.a<Context> aVar, bj.a<String> aVar2, bj.a<Integer> aVar3) {
        this.f19793a = aVar;
        this.f19794b = aVar2;
        this.f19795c = aVar3;
    }

    public static u0 a(bj.a<Context> aVar, bj.a<String> aVar2, bj.a<Integer> aVar3) {
        return new u0(aVar, aVar2, aVar3);
    }

    public static t0 c(Context context, String str, int i8) {
        return new t0(context, str, i8);
    }

    /* renamed from: b */
    public t0 get() {
        return c((Context) this.f19793a.get(), (String) this.f19794b.get(), ((Integer) this.f19795c.get()).intValue());
    }
}
