package a6;

import a6.h;
import a6.o;
import android.net.Uri;
import b6.l0;
import com.google.android.exoplayer2.upstream.DataSourceException;
import com.google.android.exoplayer2.upstream.HttpDataSource$HttpDataSourceException;
import com.google.android.exoplayer2.upstream.HttpDataSource$InvalidResponseCodeException;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.m0;
import com.google.common.collect.p2;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o extends e {

    /* renamed from: e  reason: collision with root package name */
    private final boolean f146e;

    /* renamed from: f  reason: collision with root package name */
    private final int f147f;

    /* renamed from: g  reason: collision with root package name */
    private final int f148g;

    /* renamed from: h  reason: collision with root package name */
    private final String f149h;

    /* renamed from: i  reason: collision with root package name */
    private final r f150i;

    /* renamed from: j  reason: collision with root package name */
    private final r f151j;

    /* renamed from: k  reason: collision with root package name */
    private final boolean f152k;

    /* renamed from: l  reason: collision with root package name */
    private com.google.common.base.m<String> f153l;

    /* renamed from: m  reason: collision with root package name */
    private com.google.android.exoplayer2.upstream.a f154m;

    /* renamed from: n  reason: collision with root package name */
    private HttpURLConnection f155n;

    /* renamed from: o  reason: collision with root package name */
    private InputStream f156o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f157p;
    private int q;

    /* renamed from: r  reason: collision with root package name */
    private long f158r;

    /* renamed from: s  reason: collision with root package name */
    private long f159s;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements h.a {

        /* renamed from: b  reason: collision with root package name */
        private y f161b;

        /* renamed from: c  reason: collision with root package name */
        private com.google.common.base.m<String> f162c;

        /* renamed from: d  reason: collision with root package name */
        private String f163d;

        /* renamed from: g  reason: collision with root package name */
        private boolean f166g;

        /* renamed from: h  reason: collision with root package name */
        private boolean f167h;

        /* renamed from: a  reason: collision with root package name */
        private final r f160a = new r();

        /* renamed from: e  reason: collision with root package name */
        private int f164e = 8000;

        /* renamed from: f  reason: collision with root package name */
        private int f165f = 8000;

        @Override // a6.h.a
        /* renamed from: b */
        public o a() {
            o oVar = new o(this.f163d, this.f164e, this.f165f, this.f166g, this.f160a, this.f162c, this.f167h);
            y yVar = this.f161b;
            if (yVar != null) {
                oVar.w(yVar);
            }
            return oVar;
        }

        public b c(boolean z4) {
            this.f166g = z4;
            return this;
        }

        public final b d(Map<String, String> map) {
            this.f160a.a(map);
            return this;
        }

        public b e(String str) {
            this.f163d = str;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c extends m0<String, List<String>> {

        /* renamed from: a  reason: collision with root package name */
        private final Map<String, List<String>> f168a;

        public c(Map<String, List<String>> map) {
            this.f168a = map;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean v(Map.Entry entry) {
            return entry.getKey() != null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean x(String str) {
            return str != null;
        }

        @Override // com.google.common.collect.m0, java.util.Map
        public boolean containsKey(Object obj) {
            return obj != null && super.containsKey(obj);
        }

        @Override // com.google.common.collect.m0, java.util.Map
        public boolean containsValue(Object obj) {
            return super.k(obj);
        }

        @Override // com.google.common.collect.m0, java.util.Map
        public Set<Map.Entry<String, List<String>>> entrySet() {
            return p2.b(super.entrySet(), new com.google.common.base.m() { // from class: a6.q
                @Override // com.google.common.base.m
                public final boolean apply(Object obj) {
                    boolean v8;
                    v8 = o.c.v((Map.Entry) obj);
                    return v8;
                }
            });
        }

        @Override // com.google.common.collect.m0, java.util.Map
        public boolean equals(Object obj) {
            return obj != null && super.n(obj);
        }

        @Override // com.google.common.collect.m0, java.util.Map
        public int hashCode() {
            return super.p();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.m0, com.google.common.collect.p0
        /* renamed from: i */
        public Map<String, List<String>> h() {
            return this.f168a;
        }

        @Override // com.google.common.collect.m0, java.util.Map
        public boolean isEmpty() {
            if (super.isEmpty()) {
                return true;
            }
            return super.size() == 1 && super.containsKey(null);
        }

        @Override // com.google.common.collect.m0, java.util.Map
        public Set<String> keySet() {
            return p2.b(super.keySet(), new com.google.common.base.m() { // from class: a6.p
                @Override // com.google.common.base.m
                public final boolean apply(Object obj) {
                    boolean x8;
                    x8 = o.c.x((String) obj);
                    return x8;
                }
            });
        }

        @Override // com.google.common.collect.m0, java.util.Map
        public int size() {
            return super.size() - (super.containsKey(null) ? 1 : 0);
        }

        @Override // com.google.common.collect.m0, java.util.Map
        /* renamed from: u */
        public List<String> get(Object obj) {
            if (obj == null) {
                return null;
            }
            return (List) super.get(obj);
        }
    }

    private o(String str, int i8, int i9, boolean z4, r rVar, com.google.common.base.m<String> mVar, boolean z8) {
        super(true);
        this.f149h = str;
        this.f147f = i8;
        this.f148g = i9;
        this.f146e = z4;
        this.f150i = rVar;
        this.f153l = mVar;
        this.f151j = new r();
        this.f152k = z8;
    }

    private int A(byte[] bArr, int i8, int i9) {
        if (i9 == 0) {
            return 0;
        }
        long j8 = this.f158r;
        if (j8 != -1) {
            long j9 = j8 - this.f159s;
            if (j9 == 0) {
                return -1;
            }
            i9 = (int) Math.min(i9, j9);
        }
        int read = ((InputStream) l0.j(this.f156o)).read(bArr, i8, i9);
        if (read == -1) {
            return -1;
        }
        this.f159s += read;
        l(read);
        return read;
    }

    private void B(long j8, com.google.android.exoplayer2.upstream.a aVar) {
        if (j8 == 0) {
            return;
        }
        byte[] bArr = new byte[RecognitionOptions.AZTEC];
        while (j8 > 0) {
            int read = ((InputStream) l0.j(this.f156o)).read(bArr, 0, (int) Math.min(j8, (long) RecognitionOptions.AZTEC));
            if (Thread.currentThread().isInterrupted()) {
                throw new HttpDataSource$HttpDataSourceException(new InterruptedIOException(), aVar, 2000, 1);
            }
            if (read == -1) {
                throw new HttpDataSource$HttpDataSourceException(aVar, 2008, 1);
            }
            j8 -= read;
            l(read);
        }
    }

    private void p() {
        HttpURLConnection httpURLConnection = this.f155n;
        if (httpURLConnection != null) {
            try {
                httpURLConnection.disconnect();
            } catch (Exception e8) {
                b6.p.d("DefaultHttpDataSource", "Unexpected error while disconnecting", e8);
            }
            this.f155n = null;
        }
    }

    private URL q(URL url, String str, com.google.android.exoplayer2.upstream.a aVar) {
        if (str != null) {
            try {
                URL url2 = new URL(url, str);
                String protocol = url2.getProtocol();
                if (!"https".equals(protocol) && !"http".equals(protocol)) {
                    throw new HttpDataSource$HttpDataSourceException("Unsupported protocol redirect: " + protocol, aVar, 2001, 1);
                } else if (this.f146e || protocol.equals(url.getProtocol())) {
                    return url2;
                } else {
                    throw new HttpDataSource$HttpDataSourceException("Disallowed cross-protocol redirect (" + url.getProtocol() + " to " + protocol + ")", aVar, 2001, 1);
                }
            } catch (MalformedURLException e8) {
                throw new HttpDataSource$HttpDataSourceException(e8, aVar, 2001, 1);
            }
        }
        throw new HttpDataSource$HttpDataSourceException("Null location redirect", aVar, 2001, 1);
    }

    private static boolean r(HttpURLConnection httpURLConnection) {
        return "gzip".equalsIgnoreCase(httpURLConnection.getHeaderField("Content-Encoding"));
    }

    private HttpURLConnection s(com.google.android.exoplayer2.upstream.a aVar) {
        HttpURLConnection t8;
        URL url = new URL(aVar.f10942a.toString());
        int i8 = aVar.f10944c;
        byte[] bArr = aVar.f10945d;
        long j8 = aVar.f10948g;
        long j9 = aVar.f10949h;
        boolean d8 = aVar.d(1);
        if (this.f146e || this.f152k) {
            URL url2 = url;
            int i9 = i8;
            byte[] bArr2 = bArr;
            int i10 = 0;
            while (true) {
                int i11 = i10 + 1;
                if (i10 > 20) {
                    throw new HttpDataSource$HttpDataSourceException(new NoRouteToHostException("Too many redirects: " + i11), aVar, 2001, 1);
                }
                int i12 = i9;
                long j10 = j8;
                URL url3 = url2;
                long j11 = j9;
                t8 = t(url2, i9, bArr2, j8, j9, d8, false, aVar.f10946e);
                int responseCode = t8.getResponseCode();
                String headerField = t8.getHeaderField("Location");
                if ((i12 == 1 || i12 == 3) && (responseCode == 300 || responseCode == 301 || responseCode == 302 || responseCode == 303 || responseCode == 307 || responseCode == 308)) {
                    t8.disconnect();
                    url2 = q(url3, headerField, aVar);
                    i9 = i12;
                } else if (i12 != 2 || (responseCode != 300 && responseCode != 301 && responseCode != 302 && responseCode != 303)) {
                    break;
                } else {
                    t8.disconnect();
                    if (this.f152k && responseCode == 302) {
                        i9 = i12;
                    } else {
                        bArr2 = null;
                        i9 = 1;
                    }
                    url2 = q(url3, headerField, aVar);
                }
                i10 = i11;
                j8 = j10;
                j9 = j11;
            }
            return t8;
        }
        return t(url, i8, bArr, j8, j9, d8, true, aVar.f10946e);
    }

    private HttpURLConnection t(URL url, int i8, byte[] bArr, long j8, long j9, boolean z4, boolean z8, Map<String, String> map) {
        HttpURLConnection z9 = z(url);
        z9.setConnectTimeout(this.f147f);
        z9.setReadTimeout(this.f148g);
        HashMap hashMap = new HashMap();
        r rVar = this.f150i;
        if (rVar != null) {
            hashMap.putAll(rVar.b());
        }
        hashMap.putAll(this.f151j.b());
        hashMap.putAll(map);
        for (Map.Entry entry : hashMap.entrySet()) {
            z9.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
        }
        String a9 = s.a(j8, j9);
        if (a9 != null) {
            z9.setRequestProperty("Range", a9);
        }
        String str = this.f149h;
        if (str != null) {
            z9.setRequestProperty("User-Agent", str);
        }
        z9.setRequestProperty("Accept-Encoding", z4 ? "gzip" : "identity");
        z9.setInstanceFollowRedirects(z8);
        z9.setDoOutput(bArr != null);
        z9.setRequestMethod(com.google.android.exoplayer2.upstream.a.c(i8));
        if (bArr != null) {
            z9.setFixedLengthStreamingMode(bArr.length);
            z9.connect();
            OutputStream outputStream = z9.getOutputStream();
            outputStream.write(bArr);
            outputStream.close();
        } else {
            z9.connect();
        }
        return z9;
    }

    private static void u(HttpURLConnection httpURLConnection, long j8) {
        int i8;
        if (httpURLConnection != null && (i8 = l0.f8063a) >= 19 && i8 <= 20) {
            try {
                InputStream inputStream = httpURLConnection.getInputStream();
                if (j8 == -1) {
                    if (inputStream.read() == -1) {
                        return;
                    }
                } else if (j8 <= 2048) {
                    return;
                }
                String name = inputStream.getClass().getName();
                if (!"com.android.okhttp.internal.http.HttpTransport$ChunkedInputStream".equals(name) && !"com.android.okhttp.internal.http.HttpTransport$FixedLengthInputStream".equals(name)) {
                    return;
                }
                Method declaredMethod = ((Class) b6.a.e(inputStream.getClass().getSuperclass())).getDeclaredMethod("unexpectedEndOfInput", new Class[0]);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(inputStream, new Object[0]);
            } catch (Exception unused) {
            }
        }
    }

    @Override // a6.h
    public void close() {
        try {
            InputStream inputStream = this.f156o;
            if (inputStream != null) {
                long j8 = this.f158r;
                long j9 = -1;
                if (j8 != -1) {
                    j9 = j8 - this.f159s;
                }
                u(this.f155n, j9);
                try {
                    inputStream.close();
                } catch (IOException e8) {
                    throw new HttpDataSource$HttpDataSourceException(e8, (com.google.android.exoplayer2.upstream.a) l0.j(this.f154m), 2000, 3);
                }
            }
        } finally {
            this.f156o = null;
            p();
            if (this.f157p) {
                this.f157p = false;
                m();
            }
        }
    }

    @Override // a6.f
    public int read(byte[] bArr, int i8, int i9) {
        try {
            return A(bArr, i8, i9);
        } catch (IOException e8) {
            throw HttpDataSource$HttpDataSourceException.c(e8, (com.google.android.exoplayer2.upstream.a) l0.j(this.f154m), 2);
        }
    }

    @Override // a6.h
    public Uri v() {
        HttpURLConnection httpURLConnection = this.f155n;
        if (httpURLConnection == null) {
            return null;
        }
        return Uri.parse(httpURLConnection.getURL().toString());
    }

    @Override // a6.h
    public long x(final com.google.android.exoplayer2.upstream.a aVar) {
        byte[] bArr;
        this.f154m = aVar;
        long j8 = 0;
        this.f159s = 0L;
        this.f158r = 0L;
        n(aVar);
        try {
            HttpURLConnection s8 = s(aVar);
            this.f155n = s8;
            this.q = s8.getResponseCode();
            String responseMessage = s8.getResponseMessage();
            int i8 = this.q;
            if (i8 < 200 || i8 > 299) {
                Map<String, List<String>> headerFields = s8.getHeaderFields();
                if (this.q == 416) {
                    if (aVar.f10948g == s.c(s8.getHeaderField("Content-Range"))) {
                        this.f157p = true;
                        o(aVar);
                        long j9 = aVar.f10949h;
                        if (j9 != -1) {
                            return j9;
                        }
                        return 0L;
                    }
                }
                InputStream errorStream = s8.getErrorStream();
                try {
                    bArr = errorStream != null ? l0.W0(errorStream) : l0.f8068f;
                } catch (IOException unused) {
                    bArr = l0.f8068f;
                }
                byte[] bArr2 = bArr;
                p();
                throw new HttpDataSource$InvalidResponseCodeException(this.q, responseMessage, this.q == 416 ? new DataSourceException(2008) : null, headerFields, aVar, bArr2);
            }
            final String contentType = s8.getContentType();
            com.google.common.base.m<String> mVar = this.f153l;
            if (mVar != null && !mVar.apply(contentType)) {
                p();
                throw new HttpDataSource$HttpDataSourceException(contentType, aVar) { // from class: com.google.android.exoplayer2.upstream.HttpDataSource$InvalidContentTypeException

                    /* renamed from: d  reason: collision with root package name */
                    public final String f10901d;

                    {
                        super("Invalid content type: " + contentType, aVar, 2003, 1);
                        this.f10901d = contentType;
                    }
                };
            }
            if (this.q == 200) {
                long j10 = aVar.f10948g;
                if (j10 != 0) {
                    j8 = j10;
                }
            }
            boolean r4 = r(s8);
            if (r4) {
                this.f158r = aVar.f10949h;
            } else {
                long j11 = aVar.f10949h;
                if (j11 != -1) {
                    this.f158r = j11;
                } else {
                    long b9 = s.b(s8.getHeaderField("Content-Length"), s8.getHeaderField("Content-Range"));
                    this.f158r = b9 != -1 ? b9 - j8 : -1L;
                }
            }
            try {
                this.f156o = s8.getInputStream();
                if (r4) {
                    this.f156o = new GZIPInputStream(this.f156o);
                }
                this.f157p = true;
                o(aVar);
                try {
                    B(j8, aVar);
                    return this.f158r;
                } catch (IOException e8) {
                    p();
                    if (e8 instanceof HttpDataSource$HttpDataSourceException) {
                        throw ((HttpDataSource$HttpDataSourceException) e8);
                    }
                    throw new HttpDataSource$HttpDataSourceException(e8, aVar, 2000, 1);
                }
            } catch (IOException e9) {
                p();
                throw new HttpDataSource$HttpDataSourceException(e9, aVar, 2000, 1);
            }
        } catch (IOException e10) {
            p();
            throw HttpDataSource$HttpDataSourceException.c(e10, aVar, 1);
        }
    }

    @Override // a6.h
    public Map<String, List<String>> y() {
        HttpURLConnection httpURLConnection = this.f155n;
        return httpURLConnection == null ? ImmutableMap.n() : new c(httpURLConnection.getHeaderFields());
    }

    HttpURLConnection z(URL url) {
        return (HttpURLConnection) url.openConnection();
    }
}
