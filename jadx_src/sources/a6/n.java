package a6;

import a6.h;
import a6.o;
import android.content.Context;
import android.net.Uri;
import b6.l0;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.ContentDataSource;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.upstream.UdpDataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n implements h {

    /* renamed from: a  reason: collision with root package name */
    private final Context f132a;

    /* renamed from: b  reason: collision with root package name */
    private final List<y> f133b = new ArrayList();

    /* renamed from: c  reason: collision with root package name */
    private final h f134c;

    /* renamed from: d  reason: collision with root package name */
    private h f135d;

    /* renamed from: e  reason: collision with root package name */
    private h f136e;

    /* renamed from: f  reason: collision with root package name */
    private h f137f;

    /* renamed from: g  reason: collision with root package name */
    private h f138g;

    /* renamed from: h  reason: collision with root package name */
    private h f139h;

    /* renamed from: i  reason: collision with root package name */
    private h f140i;

    /* renamed from: j  reason: collision with root package name */
    private h f141j;

    /* renamed from: k  reason: collision with root package name */
    private h f142k;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a implements h.a {

        /* renamed from: a  reason: collision with root package name */
        private final Context f143a;

        /* renamed from: b  reason: collision with root package name */
        private final h.a f144b;

        /* renamed from: c  reason: collision with root package name */
        private y f145c;

        public a(Context context) {
            this(context, new o.b());
        }

        public a(Context context, h.a aVar) {
            this.f143a = context.getApplicationContext();
            this.f144b = aVar;
        }

        @Override // a6.h.a
        /* renamed from: b */
        public n a() {
            n nVar = new n(this.f143a, this.f144b.a());
            y yVar = this.f145c;
            if (yVar != null) {
                nVar.w(yVar);
            }
            return nVar;
        }
    }

    public n(Context context, h hVar) {
        this.f132a = context.getApplicationContext();
        this.f134c = (h) b6.a.e(hVar);
    }

    private void l(h hVar) {
        for (int i8 = 0; i8 < this.f133b.size(); i8++) {
            hVar.w(this.f133b.get(i8));
        }
    }

    private h m() {
        if (this.f136e == null) {
            AssetDataSource assetDataSource = new AssetDataSource(this.f132a);
            this.f136e = assetDataSource;
            l(assetDataSource);
        }
        return this.f136e;
    }

    private h n() {
        if (this.f137f == null) {
            ContentDataSource contentDataSource = new ContentDataSource(this.f132a);
            this.f137f = contentDataSource;
            l(contentDataSource);
        }
        return this.f137f;
    }

    private h o() {
        if (this.f140i == null) {
            g gVar = new g();
            this.f140i = gVar;
            l(gVar);
        }
        return this.f140i;
    }

    private h p() {
        if (this.f135d == null) {
            FileDataSource fileDataSource = new FileDataSource();
            this.f135d = fileDataSource;
            l(fileDataSource);
        }
        return this.f135d;
    }

    private h q() {
        if (this.f141j == null) {
            RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(this.f132a);
            this.f141j = rawResourceDataSource;
            l(rawResourceDataSource);
        }
        return this.f141j;
    }

    private h r() {
        if (this.f138g == null) {
            try {
                h hVar = (h) Class.forName("com.google.android.exoplayer2.ext.rtmp.RtmpDataSource").getConstructor(new Class[0]).newInstance(new Object[0]);
                this.f138g = hVar;
                l(hVar);
            } catch (ClassNotFoundException unused) {
                b6.p.i("DefaultDataSource", "Attempting to play RTMP stream without depending on the RTMP extension");
            } catch (Exception e8) {
                throw new RuntimeException("Error instantiating RTMP extension", e8);
            }
            if (this.f138g == null) {
                this.f138g = this.f134c;
            }
        }
        return this.f138g;
    }

    private h s() {
        if (this.f139h == null) {
            UdpDataSource udpDataSource = new UdpDataSource();
            this.f139h = udpDataSource;
            l(udpDataSource);
        }
        return this.f139h;
    }

    private void t(h hVar, y yVar) {
        if (hVar != null) {
            hVar.w(yVar);
        }
    }

    @Override // a6.h
    public void close() {
        h hVar = this.f142k;
        if (hVar != null) {
            try {
                hVar.close();
            } finally {
                this.f142k = null;
            }
        }
    }

    @Override // a6.f
    public int read(byte[] bArr, int i8, int i9) {
        return ((h) b6.a.e(this.f142k)).read(bArr, i8, i9);
    }

    @Override // a6.h
    public Uri v() {
        h hVar = this.f142k;
        if (hVar == null) {
            return null;
        }
        return hVar.v();
    }

    @Override // a6.h
    public void w(y yVar) {
        b6.a.e(yVar);
        this.f134c.w(yVar);
        this.f133b.add(yVar);
        t(this.f135d, yVar);
        t(this.f136e, yVar);
        t(this.f137f, yVar);
        t(this.f138g, yVar);
        t(this.f139h, yVar);
        t(this.f140i, yVar);
        t(this.f141j, yVar);
    }

    @Override // a6.h
    public long x(com.google.android.exoplayer2.upstream.a aVar) {
        h n8;
        b6.a.f(this.f142k == null);
        String scheme = aVar.f10942a.getScheme();
        if (l0.w0(aVar.f10942a)) {
            String path = aVar.f10942a.getPath();
            if (path == null || !path.startsWith("/android_asset/")) {
                n8 = p();
            }
            n8 = m();
        } else {
            if (!"asset".equals(scheme)) {
                n8 = "content".equals(scheme) ? n() : "rtmp".equals(scheme) ? r() : "udp".equals(scheme) ? s() : "data".equals(scheme) ? o() : ("rawresource".equals(scheme) || "android.resource".equals(scheme)) ? q() : this.f134c;
            }
            n8 = m();
        }
        this.f142k = n8;
        return this.f142k.x(aVar);
    }

    @Override // a6.h
    public Map<String, List<String>> y() {
        h hVar = this.f142k;
        return hVar == null ? Collections.emptyMap() : hVar.y();
    }
}
