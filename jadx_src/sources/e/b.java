package e;

import android.content.Context;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    private final Set<d> f19745a = new CopyOnWriteArraySet();

    /* renamed from: b  reason: collision with root package name */
    private volatile Context f19746b;

    public final void a(d dVar) {
        p.e(dVar, "listener");
        Context context = this.f19746b;
        if (context != null) {
            dVar.a(context);
        }
        this.f19745a.add(dVar);
    }

    public final void b() {
        this.f19746b = null;
    }

    public final void c(Context context) {
        p.e(context, "context");
        this.f19746b = context;
        for (d dVar : this.f19745a) {
            dVar.a(context);
        }
    }

    public final Context d() {
        return this.f19746b;
    }

    public final void e(d dVar) {
        p.e(dVar, "listener");
        this.f19745a.remove(dVar);
    }
}
