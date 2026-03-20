package androidx.lifecycle;

import android.os.Bundle;
import androidx.savedstate.a;
import java.util.Map;
import kotlin.jvm.internal.Lambda;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y implements a.c {

    /* renamed from: a  reason: collision with root package name */
    private final androidx.savedstate.a f5932a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f5933b;

    /* renamed from: c  reason: collision with root package name */
    private Bundle f5934c;

    /* renamed from: d  reason: collision with root package name */
    private final cj.j f5935d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class a extends Lambda implements mj.a<z> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ j0 f5936a;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(j0 j0Var) {
            super(0);
            this.f5936a = j0Var;
        }

        /* renamed from: c */
        public final z invoke() {
            return x.e(this.f5936a);
        }
    }

    public y(androidx.savedstate.a aVar, j0 j0Var) {
        kotlin.jvm.internal.p.e(aVar, "savedStateRegistry");
        kotlin.jvm.internal.p.e(j0Var, "viewModelStoreOwner");
        this.f5932a = aVar;
        this.f5935d = cj.k.b(new a(j0Var));
    }

    private final z c() {
        return (z) this.f5935d.getValue();
    }

    @Override // androidx.savedstate.a.c
    public Bundle a() {
        Bundle bundle = new Bundle();
        Bundle bundle2 = this.f5934c;
        if (bundle2 != null) {
            bundle.putAll(bundle2);
        }
        for (Map.Entry<String, w> entry : c().f().entrySet()) {
            String key = entry.getKey();
            Bundle a9 = entry.getValue().c().a();
            if (!kotlin.jvm.internal.p.a(a9, Bundle.EMPTY)) {
                bundle.putBundle(key, a9);
            }
        }
        this.f5933b = false;
        return bundle;
    }

    public final Bundle b(String str) {
        kotlin.jvm.internal.p.e(str, "key");
        d();
        Bundle bundle = this.f5934c;
        Bundle bundle2 = bundle != null ? bundle.getBundle(str) : null;
        Bundle bundle3 = this.f5934c;
        if (bundle3 != null) {
            bundle3.remove(str);
        }
        Bundle bundle4 = this.f5934c;
        boolean z4 = true;
        if (bundle4 == null || !bundle4.isEmpty()) {
            z4 = false;
        }
        if (z4) {
            this.f5934c = null;
        }
        return bundle2;
    }

    public final void d() {
        if (this.f5933b) {
            return;
        }
        this.f5934c = this.f5932a.b("androidx.lifecycle.internal.SavedStateHandlesProvider");
        this.f5933b = true;
        c();
    }
}
