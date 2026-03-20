package s1;

import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
import androidx.savedstate.Recreator;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: d  reason: collision with root package name */
    public static final a f22767d = new a(null);

    /* renamed from: a  reason: collision with root package name */
    private final d f22768a;

    /* renamed from: b  reason: collision with root package name */
    private final androidx.savedstate.a f22769b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f22770c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(i iVar) {
            this();
        }

        public final c a(d dVar) {
            p.e(dVar, "owner");
            return new c(dVar, null);
        }
    }

    private c(d dVar) {
        this.f22768a = dVar;
        this.f22769b = new androidx.savedstate.a();
    }

    public /* synthetic */ c(d dVar, i iVar) {
        this(dVar);
    }

    public static final c a(d dVar) {
        return f22767d.a(dVar);
    }

    public final androidx.savedstate.a b() {
        return this.f22769b;
    }

    public final void c() {
        Lifecycle lifecycle = this.f22768a.getLifecycle();
        if (!(lifecycle.b() == Lifecycle.State.INITIALIZED)) {
            throw new IllegalStateException("Restarter must be created only during owner's initialization stage".toString());
        }
        lifecycle.a(new Recreator(this.f22768a));
        this.f22769b.e(lifecycle);
        this.f22770c = true;
    }

    public final void d(Bundle bundle) {
        if (!this.f22770c) {
            c();
        }
        Lifecycle lifecycle = this.f22768a.getLifecycle();
        if (!lifecycle.b().f(Lifecycle.State.STARTED)) {
            this.f22769b.f(bundle);
            return;
        }
        throw new IllegalStateException(("performRestore cannot be called when owner is " + lifecycle.b()).toString());
    }

    public final void e(Bundle bundle) {
        p.e(bundle, "outBundle");
        this.f22769b.g(bundle);
    }
}
