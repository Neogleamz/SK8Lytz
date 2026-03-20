package com.google.android.exoplayer2.drm;

import a6.h;
import a6.o;
import android.net.Uri;
import b6.l0;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.z0;
import com.google.common.collect.d3;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g implements m4.k {

    /* renamed from: a  reason: collision with root package name */
    private final Object f9613a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private z0.f f9614b;

    /* renamed from: c  reason: collision with root package name */
    private i f9615c;

    /* renamed from: d  reason: collision with root package name */
    private h.a f9616d;

    /* renamed from: e  reason: collision with root package name */
    private String f9617e;

    private i b(z0.f fVar) {
        h.a aVar = this.f9616d;
        if (aVar == null) {
            aVar = new o.b().e(this.f9617e);
        }
        Uri uri = fVar.f11344c;
        o oVar = new o(uri == null ? null : uri.toString(), fVar.f11349h, aVar);
        d3<Map.Entry<String, String>> it = fVar.f11346e.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> next = it.next();
            oVar.e(next.getKey(), next.getValue());
        }
        DefaultDrmSessionManager a9 = new DefaultDrmSessionManager.b().e(fVar.f11342a, n.f9632d).b(fVar.f11347f).c(fVar.f11348g).d(com.google.common.primitives.g.l(fVar.f11351j)).a(oVar);
        a9.F(0, fVar.c());
        return a9;
    }

    @Override // m4.k
    public i a(z0 z0Var) {
        i iVar;
        b6.a.e(z0Var.f11304b);
        z0.f fVar = z0Var.f11304b.f11380c;
        if (fVar == null || l0.f8063a < 18) {
            return i.f9623a;
        }
        synchronized (this.f9613a) {
            if (!l0.c(fVar, this.f9614b)) {
                this.f9614b = fVar;
                this.f9615c = b(fVar);
            }
            iVar = (i) b6.a.e(this.f9615c);
        }
        return iVar;
    }
}
