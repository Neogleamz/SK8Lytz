package b2;

import android.webkit.WebMessage;
import android.webkit.WebMessagePort;
import java.lang.reflect.Proxy;
import org.chromium.support_lib_boundary.WebMessagePortBoundaryInterface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h extends a2.d {

    /* renamed from: a  reason: collision with root package name */
    private WebMessagePort f7970a;

    /* renamed from: b  reason: collision with root package name */
    private WebMessagePortBoundaryInterface f7971b;

    public h(WebMessagePort webMessagePort) {
        this.f7970a = webMessagePort;
    }

    public static WebMessagePort[] b(a2.d[] dVarArr) {
        if (dVarArr == null) {
            return null;
        }
        int length = dVarArr.length;
        WebMessagePort[] webMessagePortArr = new WebMessagePort[length];
        for (int i8 = 0; i8 < length; i8++) {
            webMessagePortArr[i8] = dVarArr[i8].a();
        }
        return webMessagePortArr;
    }

    public static a2.c c(WebMessage webMessage) {
        return b.d(webMessage);
    }

    private WebMessagePort d() {
        if (this.f7970a == null) {
            this.f7970a = k.c().c(Proxy.getInvocationHandler(this.f7971b));
        }
        return this.f7970a;
    }

    public static a2.d[] e(WebMessagePort[] webMessagePortArr) {
        if (webMessagePortArr == null) {
            return null;
        }
        a2.d[] dVarArr = new a2.d[webMessagePortArr.length];
        for (int i8 = 0; i8 < webMessagePortArr.length; i8++) {
            dVarArr[i8] = new h(webMessagePortArr[i8]);
        }
        return dVarArr;
    }

    @Override // a2.d
    public WebMessagePort a() {
        return d();
    }
}
