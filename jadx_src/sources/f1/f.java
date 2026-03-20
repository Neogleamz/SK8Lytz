package f1;

import androidx.lifecycle.e0;
import kotlin.jvm.internal.p;
import mj.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f<T extends e0> {

    /* renamed from: a  reason: collision with root package name */
    private final Class<T> f19840a;

    /* renamed from: b  reason: collision with root package name */
    private final l<a, T> f19841b;

    /* JADX WARN: Multi-variable type inference failed */
    public f(Class<T> cls, l<? super a, ? extends T> lVar) {
        p.e(cls, "clazz");
        p.e(lVar, "initializer");
        this.f19840a = cls;
        this.f19841b = lVar;
    }

    public final Class<T> a() {
        return this.f19840a;
    }

    public final l<a, T> b() {
        return this.f19841b;
    }
}
