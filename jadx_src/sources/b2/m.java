package b2;

import org.chromium.support_lib_boundary.WebViewProviderFactoryBoundaryInterface;
import org.chromium.support_lib_boundary.WebkitToCompatConverterBoundaryInterface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m implements l {

    /* renamed from: a  reason: collision with root package name */
    final WebViewProviderFactoryBoundaryInterface f8001a;

    public m(WebViewProviderFactoryBoundaryInterface webViewProviderFactoryBoundaryInterface) {
        this.f8001a = webViewProviderFactoryBoundaryInterface;
    }

    @Override // b2.l
    public String[] a() {
        return this.f8001a.getSupportedFeatures();
    }

    @Override // b2.l
    public WebkitToCompatConverterBoundaryInterface getWebkitToCompatConverter() {
        return (WebkitToCompatConverterBoundaryInterface) bl.a.a(WebkitToCompatConverterBoundaryInterface.class, this.f8001a.getWebkitToCompatConverter());
    }
}
