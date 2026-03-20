package b2;

import android.webkit.WebResourceError;
import b2.a;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import org.chromium.support_lib_boundary.WebResourceErrorBoundaryInterface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i extends a2.e {

    /* renamed from: a  reason: collision with root package name */
    private WebResourceError f7972a;

    /* renamed from: b  reason: collision with root package name */
    private WebResourceErrorBoundaryInterface f7973b;

    public i(WebResourceError webResourceError) {
        this.f7972a = webResourceError;
    }

    public i(InvocationHandler invocationHandler) {
        this.f7973b = (WebResourceErrorBoundaryInterface) bl.a.a(WebResourceErrorBoundaryInterface.class, invocationHandler);
    }

    private WebResourceErrorBoundaryInterface c() {
        if (this.f7973b == null) {
            this.f7973b = (WebResourceErrorBoundaryInterface) bl.a.a(WebResourceErrorBoundaryInterface.class, k.c().e(this.f7972a));
        }
        return this.f7973b;
    }

    private WebResourceError d() {
        if (this.f7972a == null) {
            this.f7972a = k.c().d(Proxy.getInvocationHandler(this.f7973b));
        }
        return this.f7972a;
    }

    @Override // a2.e
    public CharSequence a() {
        a.b bVar = j.f7994v;
        if (bVar.c()) {
            return b.e(d());
        }
        if (bVar.d()) {
            return c().getDescription();
        }
        throw j.a();
    }

    @Override // a2.e
    public int b() {
        a.b bVar = j.f7995w;
        if (bVar.c()) {
            return b.f(d());
        }
        if (bVar.d()) {
            return c().getErrorCode();
        }
        throw j.a();
    }
}
