package androidx.webkit;

import a2.a;
import a2.e;
import a2.g;
import android.app.PendingIntent;
import android.os.Build;
import android.webkit.SafeBrowsingResponse;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import b2.i;
import b2.j;
import java.lang.reflect.InvocationHandler;
import org.chromium.support_lib_boundary.WebViewClientBoundaryInterface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class WebViewClientCompat extends WebViewClient implements WebViewClientBoundaryInterface {

    /* renamed from: a  reason: collision with root package name */
    private static final String[] f7901a = {"VISUAL_STATE_CALLBACK", "RECEIVE_WEB_RESOURCE_ERROR", "RECEIVE_HTTP_ERROR", "SHOULD_OVERRIDE_WITH_REDIRECTS", "SAFE_BROWSING_HIT"};

    public void a(WebView webView, WebResourceRequest webResourceRequest, e eVar) {
        throw null;
    }

    public void b(WebView webView, WebResourceRequest webResourceRequest, int i8, a aVar) {
        if (!g.a("SAFE_BROWSING_RESPONSE_SHOW_INTERSTITIAL")) {
            throw j.a();
        }
        aVar.a(true);
    }

    public final String[] getSupportedFeatures() {
        return f7901a;
    }

    @Override // android.webkit.WebViewClient
    public void onPageCommitVisible(WebView webView, String str) {
    }

    @Override // android.webkit.WebViewClient
    public final void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        a(webView, webResourceRequest, new i(webResourceError));
    }

    public final void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, InvocationHandler invocationHandler) {
        a(webView, webResourceRequest, new i(invocationHandler));
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
    }

    @Override // android.webkit.WebViewClient
    public final void onSafeBrowsingHit(WebView webView, WebResourceRequest webResourceRequest, int i8, SafeBrowsingResponse safeBrowsingResponse) {
        b(webView, webResourceRequest, i8, new b2.g(safeBrowsingResponse));
    }

    public final void onSafeBrowsingHit(WebView webView, WebResourceRequest webResourceRequest, int i8, InvocationHandler invocationHandler) {
        b(webView, webResourceRequest, i8, new b2.g(invocationHandler));
    }

    public boolean onWebAuthnIntent(WebView webView, PendingIntent pendingIntent, InvocationHandler invocationHandler) {
        return false;
    }
}
