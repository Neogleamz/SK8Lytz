package androidx.navigation;

import android.os.Bundle;
import androidx.navigation.q;
@q.b("navigation")
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k extends q<j> {

    /* renamed from: a  reason: collision with root package name */
    private final r f6407a;

    public k(r rVar) {
        this.f6407a = rVar;
    }

    @Override // androidx.navigation.q
    public boolean e() {
        return true;
    }

    @Override // androidx.navigation.q
    /* renamed from: f */
    public j a() {
        return new j(this);
    }

    @Override // androidx.navigation.q
    /* renamed from: g */
    public i b(j jVar, Bundle bundle, n nVar, q.a aVar) {
        int K = jVar.K();
        if (K == 0) {
            throw new IllegalStateException("no start destination defined via app:startDestination for " + jVar.n());
        }
        i H = jVar.H(K, false);
        if (H != null) {
            return this.f6407a.e(H.t()).b(H, H.h(bundle), nVar, aVar);
        }
        String I = jVar.I();
        throw new IllegalArgumentException("navigation destination " + I + " is not a direct child of this NavGraph");
    }
}
