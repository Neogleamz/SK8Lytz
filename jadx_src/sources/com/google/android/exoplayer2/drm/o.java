package com.google.android.exoplayer2.drm;

import a6.h;
import a6.x;
import android.net.Uri;
import android.text.TextUtils;
import b6.l0;
import com.google.android.exoplayer2.drm.m;
import com.google.android.exoplayer2.upstream.HttpDataSource$InvalidResponseCodeException;
import com.google.android.exoplayer2.upstream.a;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o implements p {

    /* renamed from: a  reason: collision with root package name */
    private final h.a f9636a;

    /* renamed from: b  reason: collision with root package name */
    private final String f9637b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f9638c;

    /* renamed from: d  reason: collision with root package name */
    private final Map<String, String> f9639d;

    public o(String str, boolean z4, h.a aVar) {
        b6.a.a((z4 && TextUtils.isEmpty(str)) ? false : true);
        this.f9636a = aVar;
        this.f9637b = str;
        this.f9638c = z4;
        this.f9639d = new HashMap();
    }

    private static byte[] c(h.a aVar, String str, byte[] bArr, Map<String, String> map) {
        x xVar = new x(aVar.a());
        com.google.android.exoplayer2.upstream.a a9 = new a.b().j(str).e(map).d(2).c(bArr).b(1).a();
        int i8 = 0;
        com.google.android.exoplayer2.upstream.a aVar2 = a9;
        while (true) {
            try {
                a6.i iVar = new a6.i(xVar, aVar2);
                try {
                    byte[] W0 = l0.W0(iVar);
                    l0.n(iVar);
                    return W0;
                } catch (HttpDataSource$InvalidResponseCodeException e8) {
                    String d8 = d(e8, i8);
                    if (d8 == null) {
                        throw e8;
                    }
                    i8++;
                    aVar2 = aVar2.a().j(d8).a();
                    l0.n(iVar);
                }
            } catch (Exception e9) {
                throw new MediaDrmCallbackException(a9, (Uri) b6.a.e(xVar.m()), xVar.y(), xVar.l(), e9);
            }
        }
    }

    private static String d(HttpDataSource$InvalidResponseCodeException httpDataSource$InvalidResponseCodeException, int i8) {
        Map<String, List<String>> map;
        List<String> list;
        int i9 = httpDataSource$InvalidResponseCodeException.f10902d;
        if (!((i9 == 307 || i9 == 308) && i8 < 5) || (map = httpDataSource$InvalidResponseCodeException.f10904f) == null || (list = map.get("Location")) == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override // com.google.android.exoplayer2.drm.p
    public byte[] a(UUID uuid, m.a aVar) {
        String b9 = aVar.b();
        if (this.f9638c || TextUtils.isEmpty(b9)) {
            b9 = this.f9637b;
        }
        if (TextUtils.isEmpty(b9)) {
            throw new MediaDrmCallbackException(new a.b().i(Uri.EMPTY).a(), Uri.EMPTY, ImmutableMap.n(), 0L, new IllegalStateException("No license URL"));
        }
        HashMap hashMap = new HashMap();
        UUID uuid2 = i4.b.f20469e;
        hashMap.put("Content-Type", uuid2.equals(uuid) ? "text/xml" : i4.b.f20467c.equals(uuid) ? "application/json" : "application/octet-stream");
        if (uuid2.equals(uuid)) {
            hashMap.put("SOAPAction", "http://schemas.microsoft.com/DRM/2007/03/protocols/AcquireLicense");
        }
        synchronized (this.f9639d) {
            hashMap.putAll(this.f9639d);
        }
        return c(this.f9636a, b9, aVar.a(), hashMap);
    }

    @Override // com.google.android.exoplayer2.drm.p
    public byte[] b(UUID uuid, m.d dVar) {
        return c(this.f9636a, dVar.b() + "&signedRequest=" + l0.D(dVar.a()), null, Collections.emptyMap());
    }

    public void e(String str, String str2) {
        b6.a.e(str);
        b6.a.e(str2);
        synchronized (this.f9639d) {
            this.f9639d.put(str, str2);
        }
    }
}
