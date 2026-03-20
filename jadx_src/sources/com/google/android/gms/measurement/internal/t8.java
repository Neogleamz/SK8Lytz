package com.google.android.gms.measurement.internal;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final URL f17003a;

    /* renamed from: b  reason: collision with root package name */
    private final byte[] f17004b;

    /* renamed from: c  reason: collision with root package name */
    private final u8 f17005c;

    /* renamed from: d  reason: collision with root package name */
    private final String f17006d;

    /* renamed from: e  reason: collision with root package name */
    private final Map<String, String> f17007e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ s8 f17008f;

    public t8(s8 s8Var, String str, URL url, byte[] bArr, Map<String, String> map, u8 u8Var) {
        this.f17008f = s8Var;
        n6.j.f(str);
        n6.j.l(url);
        n6.j.l(u8Var);
        this.f17003a = url;
        this.f17004b = null;
        this.f17005c = u8Var;
        this.f17006d = str;
        this.f17007e = null;
    }

    private final void b(final int i8, final Exception exc, final byte[] bArr, final Map<String, List<String>> map) {
        this.f17008f.l().B(new Runnable() { // from class: com.google.android.gms.measurement.internal.w8
            @Override // java.lang.Runnable
            public final void run() {
                t8.this.a(i8, exc, bArr, map);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void a(int i8, Exception exc, byte[] bArr, Map map) {
        this.f17005c.a(this.f17006d, i8, exc, bArr, map);
    }

    @Override // java.lang.Runnable
    public final void run() {
        HttpURLConnection httpURLConnection;
        Map<String, List<String>> map;
        byte[] t8;
        this.f17008f.h();
        int i8 = 0;
        try {
            URLConnection b9 = com.google.android.gms.internal.measurement.p1.a().b(this.f17003a, "client-measurement");
            if (!(b9 instanceof HttpURLConnection)) {
                throw new IOException("Failed to obtain HTTP connection");
            }
            httpURLConnection = (HttpURLConnection) b9;
            httpURLConnection.setDefaultUseCaches(false);
            httpURLConnection.setConnectTimeout(60000);
            httpURLConnection.setReadTimeout(61000);
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setDoInput(true);
            try {
                i8 = httpURLConnection.getResponseCode();
                map = httpURLConnection.getHeaderFields();
                try {
                    s8 s8Var = this.f17008f;
                    t8 = s8.t(httpURLConnection);
                    httpURLConnection.disconnect();
                    b(i8, null, t8, map);
                } catch (IOException e8) {
                    e = e8;
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    b(i8, e, null, map);
                } catch (Throwable th) {
                    th = th;
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    b(i8, null, null, map);
                    throw th;
                }
            } catch (IOException e9) {
                e = e9;
                map = null;
            } catch (Throwable th2) {
                th = th2;
                map = null;
            }
        } catch (IOException e10) {
            e = e10;
            httpURLConnection = null;
            map = null;
        } catch (Throwable th3) {
            th = th3;
            httpURLConnection = null;
            map = null;
        }
    }
}
