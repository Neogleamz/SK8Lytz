package b2;

import android.content.Context;
import android.net.Uri;
import android.webkit.SafeBrowsingResponse;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {
    public static void a(SafeBrowsingResponse safeBrowsingResponse, boolean z4) {
        safeBrowsingResponse.backToSafety(z4);
    }

    public static Uri b() {
        return WebView.getSafeBrowsingPrivacyPolicyUrl();
    }

    public static void c(SafeBrowsingResponse safeBrowsingResponse, boolean z4) {
        safeBrowsingResponse.proceed(z4);
    }

    public static void d(List<String> list, ValueCallback<Boolean> valueCallback) {
        WebView.setSafeBrowsingWhitelist(list, valueCallback);
    }

    public static void e(SafeBrowsingResponse safeBrowsingResponse, boolean z4) {
        safeBrowsingResponse.showInterstitial(z4);
    }

    public static void f(Context context, ValueCallback<Boolean> valueCallback) {
        WebView.startSafeBrowsing(context, valueCallback);
    }
}
