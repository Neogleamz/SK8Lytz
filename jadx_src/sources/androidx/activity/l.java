package androidx.activity;

import cj.a0;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class l {

    /* renamed from: a  reason: collision with root package name */
    private boolean f407a;

    /* renamed from: b  reason: collision with root package name */
    private final CopyOnWriteArrayList<a> f408b = new CopyOnWriteArrayList<>();

    /* renamed from: c  reason: collision with root package name */
    private mj.a<a0> f409c;

    public l(boolean z4) {
        this.f407a = z4;
    }

    public final void a(a aVar) {
        kotlin.jvm.internal.p.e(aVar, "cancellable");
        this.f408b.add(aVar);
    }

    public abstract void b();

    public final boolean c() {
        return this.f407a;
    }

    public final void d() {
        for (a aVar : this.f408b) {
            aVar.cancel();
        }
    }

    public final void e(a aVar) {
        kotlin.jvm.internal.p.e(aVar, "cancellable");
        this.f408b.remove(aVar);
    }

    public final void f(boolean z4) {
        this.f407a = z4;
        mj.a<a0> aVar = this.f409c;
        if (aVar != null) {
            aVar.invoke();
        }
    }

    public final void g(mj.a<a0> aVar) {
        this.f409c = aVar;
    }
}
