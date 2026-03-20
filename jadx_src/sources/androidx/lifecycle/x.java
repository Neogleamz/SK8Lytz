package androidx.lifecycle;

import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.f0;
import androidx.savedstate.a;
import f1.a;
import kotlin.jvm.internal.Lambda;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x {

    /* renamed from: a  reason: collision with root package name */
    public static final a.b<s1.d> f5928a = new b();

    /* renamed from: b  reason: collision with root package name */
    public static final a.b<j0> f5929b = new c();

    /* renamed from: c  reason: collision with root package name */
    public static final a.b<Bundle> f5930c = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a implements a.b<Bundle> {
        a() {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements a.b<s1.d> {
        b() {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c implements a.b<j0> {
        c() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d extends Lambda implements mj.l<f1.a, z> {

        /* renamed from: a  reason: collision with root package name */
        public static final d f5931a = new d();

        d() {
            super(1);
        }

        /* renamed from: c */
        public final z invoke(f1.a aVar) {
            kotlin.jvm.internal.p.e(aVar, "$this$initializer");
            return new z();
        }
    }

    public static final w a(f1.a aVar) {
        kotlin.jvm.internal.p.e(aVar, "<this>");
        s1.d dVar = (s1.d) aVar.a(f5928a);
        if (dVar != null) {
            j0 j0Var = (j0) aVar.a(f5929b);
            if (j0Var != null) {
                Bundle bundle = (Bundle) aVar.a(f5930c);
                String str = (String) aVar.a(f0.c.f5881d);
                if (str != null) {
                    return b(dVar, j0Var, str, bundle);
                }
                throw new IllegalArgumentException("CreationExtras must have a value by `VIEW_MODEL_KEY`");
            }
            throw new IllegalArgumentException("CreationExtras must have a value by `VIEW_MODEL_STORE_OWNER_KEY`");
        }
        throw new IllegalArgumentException("CreationExtras must have a value by `SAVED_STATE_REGISTRY_OWNER_KEY`");
    }

    private static final w b(s1.d dVar, j0 j0Var, String str, Bundle bundle) {
        y d8 = d(dVar);
        z e8 = e(j0Var);
        w wVar = e8.f().get(str);
        if (wVar == null) {
            w a9 = w.f5921f.a(d8.b(str), bundle);
            e8.f().put(str, a9);
            return a9;
        }
        return wVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T extends s1.d & j0> void c(T t8) {
        kotlin.jvm.internal.p.e(t8, "<this>");
        Lifecycle.State b9 = t8.getLifecycle().b();
        if (!(b9 == Lifecycle.State.INITIALIZED || b9 == Lifecycle.State.CREATED)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (t8.getSavedStateRegistry().c("androidx.lifecycle.internal.SavedStateHandlesProvider") == null) {
            y yVar = new y(t8.getSavedStateRegistry(), t8);
            t8.getSavedStateRegistry().h("androidx.lifecycle.internal.SavedStateHandlesProvider", yVar);
            t8.getLifecycle().a(new SavedStateHandleAttacher(yVar));
        }
    }

    public static final y d(s1.d dVar) {
        kotlin.jvm.internal.p.e(dVar, "<this>");
        a.c c9 = dVar.getSavedStateRegistry().c("androidx.lifecycle.internal.SavedStateHandlesProvider");
        y yVar = c9 instanceof y ? (y) c9 : null;
        if (yVar != null) {
            return yVar;
        }
        throw new IllegalStateException("enableSavedStateHandles() wasn't called prior to createSavedStateHandle() call");
    }

    public static final z e(j0 j0Var) {
        kotlin.jvm.internal.p.e(j0Var, "<this>");
        f1.c cVar = new f1.c();
        cVar.a(kotlin.jvm.internal.t.b(z.class), d.f5931a);
        return (z) new f0(j0Var, cVar.b()).b("androidx.lifecycle.internal.SavedStateHandlesVM", z.class);
    }
}
