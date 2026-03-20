package androidx.lifecycle;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i0 {

    /* renamed from: a  reason: collision with root package name */
    private final Map<String, e0> f5885a = new LinkedHashMap();

    public final void a() {
        for (e0 e0Var : this.f5885a.values()) {
            e0Var.a();
        }
        this.f5885a.clear();
    }

    public final e0 b(String str) {
        kotlin.jvm.internal.p.e(str, "key");
        return this.f5885a.get(str);
    }

    public final Set<String> c() {
        return new HashSet(this.f5885a.keySet());
    }

    public final void d(String str, e0 e0Var) {
        kotlin.jvm.internal.p.e(str, "key");
        kotlin.jvm.internal.p.e(e0Var, "viewModel");
        e0 put = this.f5885a.put(str, e0Var);
        if (put != null) {
            put.d();
        }
    }
}
