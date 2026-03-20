package com.google.android.datatransport.cct;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import com.google.android.datatransport.cct.d;
import com.google.android.datatransport.cct.internal.ClientInfo;
import com.google.android.datatransport.cct.internal.NetworkConnectionInfo;
import com.google.android.datatransport.cct.internal.QosTier;
import com.google.android.datatransport.cct.internal.i;
import com.google.android.datatransport.cct.internal.j;
import com.google.android.datatransport.cct.internal.k;
import com.google.android.datatransport.runtime.backends.BackendResponse;
import com.google.firebase.encoders.EncodingException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import w3.h;
import x3.k;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d implements k {

    /* renamed from: a  reason: collision with root package name */
    private final j8.a f8928a;

    /* renamed from: b  reason: collision with root package name */
    private final ConnectivityManager f8929b;

    /* renamed from: c  reason: collision with root package name */
    private final Context f8930c;

    /* renamed from: d  reason: collision with root package name */
    final URL f8931d;

    /* renamed from: e  reason: collision with root package name */
    private final g4.a f8932e;

    /* renamed from: f  reason: collision with root package name */
    private final g4.a f8933f;

    /* renamed from: g  reason: collision with root package name */
    private final int f8934g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        final URL f8935a;

        /* renamed from: b  reason: collision with root package name */
        final i f8936b;

        /* renamed from: c  reason: collision with root package name */
        final String f8937c;

        a(URL url, i iVar, String str) {
            this.f8935a = url;
            this.f8936b = iVar;
            this.f8937c = str;
        }

        a a(URL url) {
            return new a(url, this.f8936b, this.f8937c);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        final int f8938a;

        /* renamed from: b  reason: collision with root package name */
        final URL f8939b;

        /* renamed from: c  reason: collision with root package name */
        final long f8940c;

        b(int i8, URL url, long j8) {
            this.f8938a = i8;
            this.f8939b = url;
            this.f8940c = j8;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(Context context, g4.a aVar, g4.a aVar2) {
        this(context, aVar, aVar2, 130000);
    }

    d(Context context, g4.a aVar, g4.a aVar2, int i8) {
        this.f8928a = i.b();
        this.f8930c = context;
        this.f8929b = (ConnectivityManager) context.getSystemService("connectivity");
        this.f8931d = n(com.google.android.datatransport.cct.a.f8918c);
        this.f8932e = aVar2;
        this.f8933f = aVar;
        this.f8934g = i8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public b e(a aVar) {
        a4.a.f("CctTransportBackend", "Making request to: %s", aVar.f8935a);
        HttpURLConnection httpURLConnection = (HttpURLConnection) aVar.f8935a.openConnection();
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(this.f8934g);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("User-Agent", String.format("datatransport/%s android/", "3.1.8"));
        httpURLConnection.setRequestProperty("Content-Encoding", "gzip");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");
        String str = aVar.f8937c;
        if (str != null) {
            httpURLConnection.setRequestProperty("X-Goog-Api-Key", str);
        }
        try {
            OutputStream outputStream = httpURLConnection.getOutputStream();
            try {
                GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(outputStream);
                this.f8928a.a(aVar.f8936b, new BufferedWriter(new OutputStreamWriter(gZIPOutputStream)));
                gZIPOutputStream.close();
                if (outputStream != null) {
                    outputStream.close();
                }
                int responseCode = httpURLConnection.getResponseCode();
                a4.a.f("CctTransportBackend", "Status Code: %d", Integer.valueOf(responseCode));
                a4.a.b("CctTransportBackend", "Content-Type: %s", httpURLConnection.getHeaderField("Content-Type"));
                a4.a.b("CctTransportBackend", "Content-Encoding: %s", httpURLConnection.getHeaderField("Content-Encoding"));
                if (responseCode == 302 || responseCode == 301 || responseCode == 307) {
                    return new b(responseCode, new URL(httpURLConnection.getHeaderField("Location")), 0L);
                }
                if (responseCode != 200) {
                    return new b(responseCode, null, 0L);
                }
                InputStream inputStream = httpURLConnection.getInputStream();
                try {
                    InputStream m8 = m(inputStream, httpURLConnection.getHeaderField("Content-Encoding"));
                    b bVar = new b(responseCode, null, v3.b.b(new BufferedReader(new InputStreamReader(m8))).c());
                    if (m8 != null) {
                        m8.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    return bVar;
                } catch (Throwable th) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                    }
                }
                throw th3;
            }
        } catch (ConnectException e8) {
            e = e8;
            a4.a.d("CctTransportBackend", "Couldn't open connection, returning with 500", e);
            return new b(500, null, 0L);
        } catch (UnknownHostException e9) {
            e = e9;
            a4.a.d("CctTransportBackend", "Couldn't open connection, returning with 500", e);
            return new b(500, null, 0L);
        } catch (IOException e10) {
            e = e10;
            a4.a.d("CctTransportBackend", "Couldn't encode request, returning with 400", e);
            return new b(400, null, 0L);
        } catch (EncodingException e11) {
            e = e11;
            a4.a.d("CctTransportBackend", "Couldn't encode request, returning with 400", e);
            return new b(400, null, 0L);
        }
    }

    private static int f(NetworkInfo networkInfo) {
        NetworkConnectionInfo.MobileSubtype mobileSubtype;
        if (networkInfo == null) {
            mobileSubtype = NetworkConnectionInfo.MobileSubtype.UNKNOWN_MOBILE_SUBTYPE;
        } else {
            int subtype = networkInfo.getSubtype();
            if (subtype != -1) {
                if (NetworkConnectionInfo.MobileSubtype.c(subtype) != null) {
                    return subtype;
                }
                return 0;
            }
            mobileSubtype = NetworkConnectionInfo.MobileSubtype.COMBINED;
        }
        return mobileSubtype.f();
    }

    private static int g(NetworkInfo networkInfo) {
        return networkInfo == null ? NetworkConnectionInfo.NetworkType.NONE.f() : networkInfo.getType();
    }

    private static int h(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e8) {
            a4.a.d("CctTransportBackend", "Unable to find version code for package", e8);
            return -1;
        }
    }

    private i i(x3.e eVar) {
        j.a j8;
        HashMap hashMap = new HashMap();
        for (w3.i iVar : eVar.b()) {
            String j9 = iVar.j();
            if (hashMap.containsKey(j9)) {
                ((List) hashMap.get(j9)).add(iVar);
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.add(iVar);
                hashMap.put(j9, arrayList);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (Map.Entry entry : hashMap.entrySet()) {
            w3.i iVar2 = (w3.i) ((List) entry.getValue()).get(0);
            k.a b9 = com.google.android.datatransport.cct.internal.k.a().f(QosTier.DEFAULT).g(this.f8933f.a()).h(this.f8932e.a()).b(ClientInfo.a().c(ClientInfo.ClientType.ANDROID_FIREBASE).b(com.google.android.datatransport.cct.internal.a.a().m(Integer.valueOf(iVar2.g("sdk-version"))).j(iVar2.b("model")).f(iVar2.b("hardware")).d(iVar2.b("device")).l(iVar2.b("product")).k(iVar2.b("os-uild")).h(iVar2.b("manufacturer")).e(iVar2.b("fingerprint")).c(iVar2.b("country")).g(iVar2.b("locale")).i(iVar2.b("mcc_mnc")).b(iVar2.b("application_build")).a()).a());
            try {
                b9.i(Integer.parseInt((String) entry.getKey()));
            } catch (NumberFormatException unused) {
                b9.j((String) entry.getKey());
            }
            ArrayList arrayList3 = new ArrayList();
            for (w3.i iVar3 : (List) entry.getValue()) {
                h e8 = iVar3.e();
                u3.c b10 = e8.b();
                if (b10.equals(u3.c.b("proto"))) {
                    j8 = j.j(e8.a());
                } else if (b10.equals(u3.c.b("json"))) {
                    j8 = j.i(new String(e8.a(), Charset.forName("UTF-8")));
                } else {
                    a4.a.g("CctTransportBackend", "Received event of unsupported encoding %s. Skipping...", b10);
                }
                j8.c(iVar3.f()).d(iVar3.k()).h(iVar3.h("tz-offset")).e(NetworkConnectionInfo.a().c(NetworkConnectionInfo.NetworkType.c(iVar3.g("net-type"))).b(NetworkConnectionInfo.MobileSubtype.c(iVar3.g("mobile-subtype"))).a());
                if (iVar3.d() != null) {
                    j8.b(iVar3.d());
                }
                arrayList3.add(j8.a());
            }
            b9.c(arrayList3);
            arrayList2.add(b9.a());
        }
        return i.a(arrayList2);
    }

    private static TelephonyManager j(Context context) {
        return (TelephonyManager) context.getSystemService("phone");
    }

    static long k() {
        Calendar.getInstance();
        return TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis()) / 1000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ a l(a aVar, b bVar) {
        URL url = bVar.f8939b;
        if (url != null) {
            a4.a.b("CctTransportBackend", "Following redirect to: %s", url);
            return aVar.a(bVar.f8939b);
        }
        return null;
    }

    private static InputStream m(InputStream inputStream, String str) {
        return "gzip".equals(str) ? new GZIPInputStream(inputStream) : inputStream;
    }

    private static URL n(String str) {
        try {
            return new URL(str);
        } catch (MalformedURLException e8) {
            throw new IllegalArgumentException("Invalid url: " + str, e8);
        }
    }

    @Override // x3.k
    public BackendResponse a(x3.e eVar) {
        i i8 = i(eVar);
        URL url = this.f8931d;
        if (eVar.c() != null) {
            try {
                com.google.android.datatransport.cct.a c9 = com.google.android.datatransport.cct.a.c(eVar.c());
                r3 = c9.d() != null ? c9.d() : null;
                if (c9.e() != null) {
                    url = n(c9.e());
                }
            } catch (IllegalArgumentException unused) {
                return BackendResponse.a();
            }
        }
        try {
            b bVar = (b) b4.b.a(5, new a(url, i8, r3), new b4.a() { // from class: com.google.android.datatransport.cct.b
                @Override // b4.a
                public final Object apply(Object obj) {
                    d.b e8;
                    e8 = d.this.e((d.a) obj);
                    return e8;
                }
            }, new b4.c() { // from class: com.google.android.datatransport.cct.c
                @Override // b4.c
                public final Object a(Object obj, Object obj2) {
                    d.a l8;
                    l8 = d.l((d.a) obj, (d.b) obj2);
                    return l8;
                }
            });
            int i9 = bVar.f8938a;
            if (i9 == 200) {
                return BackendResponse.e(bVar.f8940c);
            }
            if (i9 < 500 && i9 != 404) {
                return i9 == 400 ? BackendResponse.d() : BackendResponse.a();
            }
            return BackendResponse.f();
        } catch (IOException e8) {
            a4.a.d("CctTransportBackend", "Could not make request to the backend", e8);
            return BackendResponse.f();
        }
    }

    @Override // x3.k
    public w3.i b(w3.i iVar) {
        NetworkInfo activeNetworkInfo = this.f8929b.getActiveNetworkInfo();
        return iVar.l().a("sdk-version", Build.VERSION.SDK_INT).c("model", Build.MODEL).c("hardware", Build.HARDWARE).c("device", Build.DEVICE).c("product", Build.PRODUCT).c("os-uild", Build.ID).c("manufacturer", Build.MANUFACTURER).c("fingerprint", Build.FINGERPRINT).b("tz-offset", k()).a("net-type", g(activeNetworkInfo)).a("mobile-subtype", f(activeNetworkInfo)).c("country", Locale.getDefault().getCountry()).c("locale", Locale.getDefault().getLanguage()).c("mcc_mnc", j(this.f8930c).getSimOperator()).c("application_build", Integer.toString(h(this.f8930c))).d();
    }
}
