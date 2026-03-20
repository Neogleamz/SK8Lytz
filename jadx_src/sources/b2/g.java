package b2;

import android.webkit.SafeBrowsingResponse;
import b2.a;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import org.chromium.support_lib_boundary.SafeBrowsingResponseBoundaryInterface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g extends a2.a {

    /* renamed from: a  reason: collision with root package name */
    private SafeBrowsingResponse f7968a;

    /* renamed from: b  reason: collision with root package name */
    private SafeBrowsingResponseBoundaryInterface f7969b;

    public g(SafeBrowsingResponse safeBrowsingResponse) {
        this.f7968a = safeBrowsingResponse;
    }

    public g(InvocationHandler invocationHandler) {
        this.f7969b = (SafeBrowsingResponseBoundaryInterface) bl.a.a(SafeBrowsingResponseBoundaryInterface.class, invocationHandler);
    }

    private SafeBrowsingResponseBoundaryInterface b() {
        if (this.f7969b == null) {
            this.f7969b = (SafeBrowsingResponseBoundaryInterface) bl.a.a(SafeBrowsingResponseBoundaryInterface.class, k.c().b(this.f7968a));
        }
        return this.f7969b;
    }

    private SafeBrowsingResponse c() {
        if (this.f7968a == null) {
            this.f7968a = k.c().a(Proxy.getInvocationHandler(this.f7969b));
        }
        return this.f7968a;
    }

    @Override // a2.a
    public void a(boolean z4) {
        a.f fVar = j.f7998z;
        if (fVar.c()) {
            c.e(c(), z4);
        } else if (!fVar.d()) {
            throw j.a();
        } else {
            b().showInterstitial(z4);
        }
    }
}
