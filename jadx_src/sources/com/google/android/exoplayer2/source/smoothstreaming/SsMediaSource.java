package com.google.android.exoplayer2.source.smoothstreaming;

import a6.h;
import a6.t;
import a6.y;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import b6.l0;
import com.google.android.exoplayer2.drm.g;
import com.google.android.exoplayer2.drm.i;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.source.j;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.source.l;
import com.google.android.exoplayer2.source.smoothstreaming.a;
import com.google.android.exoplayer2.source.smoothstreaming.b;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.a;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.c;
import com.google.android.exoplayer2.upstream.d;
import com.google.android.exoplayer2.z0;
import h5.f;
import h5.s;
import i4.q;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SsMediaSource extends com.google.android.exoplayer2.source.a implements Loader.b<d<com.google.android.exoplayer2.source.smoothstreaming.manifest.a>> {
    private h A;
    private Loader B;
    private t C;
    private y E;
    private long F;
    private com.google.android.exoplayer2.source.smoothstreaming.manifest.a G;
    private Handler H;

    /* renamed from: h  reason: collision with root package name */
    private final boolean f10689h;

    /* renamed from: j  reason: collision with root package name */
    private final Uri f10690j;

    /* renamed from: k  reason: collision with root package name */
    private final z0.h f10691k;

    /* renamed from: l  reason: collision with root package name */
    private final z0 f10692l;

    /* renamed from: m  reason: collision with root package name */
    private final h.a f10693m;

    /* renamed from: n  reason: collision with root package name */
    private final b.a f10694n;

    /* renamed from: p  reason: collision with root package name */
    private final h5.d f10695p;
    private final i q;

    /* renamed from: t  reason: collision with root package name */
    private final com.google.android.exoplayer2.upstream.c f10696t;

    /* renamed from: w  reason: collision with root package name */
    private final long f10697w;

    /* renamed from: x  reason: collision with root package name */
    private final l.a f10698x;

    /* renamed from: y  reason: collision with root package name */
    private final d.a<? extends com.google.android.exoplayer2.source.smoothstreaming.manifest.a> f10699y;

    /* renamed from: z  reason: collision with root package name */
    private final ArrayList<c> f10700z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class Factory implements k.a {

        /* renamed from: a  reason: collision with root package name */
        private final b.a f10701a;

        /* renamed from: b  reason: collision with root package name */
        private final h.a f10702b;

        /* renamed from: c  reason: collision with root package name */
        private h5.d f10703c;

        /* renamed from: d  reason: collision with root package name */
        private m4.k f10704d;

        /* renamed from: e  reason: collision with root package name */
        private com.google.android.exoplayer2.upstream.c f10705e;

        /* renamed from: f  reason: collision with root package name */
        private long f10706f;

        /* renamed from: g  reason: collision with root package name */
        private d.a<? extends com.google.android.exoplayer2.source.smoothstreaming.manifest.a> f10707g;

        public Factory(h.a aVar) {
            this(new a.C0114a(aVar), aVar);
        }

        public Factory(b.a aVar, h.a aVar2) {
            this.f10701a = (b.a) b6.a.e(aVar);
            this.f10702b = aVar2;
            this.f10704d = new g();
            this.f10705e = new com.google.android.exoplayer2.upstream.b();
            this.f10706f = 30000L;
            this.f10703c = new f();
        }

        public SsMediaSource a(z0 z0Var) {
            b6.a.e(z0Var.f11304b);
            d.a aVar = this.f10707g;
            if (aVar == null) {
                aVar = new SsManifestParser();
            }
            List<StreamKey> list = z0Var.f11304b.f11382e;
            return new SsMediaSource(z0Var, null, this.f10702b, !list.isEmpty() ? new g5.c(aVar, list) : aVar, this.f10701a, this.f10703c, this.f10704d.a(z0Var), this.f10705e, this.f10706f);
        }
    }

    static {
        q.a("goog.exo.smoothstreaming");
    }

    private SsMediaSource(z0 z0Var, com.google.android.exoplayer2.source.smoothstreaming.manifest.a aVar, h.a aVar2, d.a<? extends com.google.android.exoplayer2.source.smoothstreaming.manifest.a> aVar3, b.a aVar4, h5.d dVar, i iVar, com.google.android.exoplayer2.upstream.c cVar, long j8) {
        b6.a.f(aVar == null || !aVar.f10767d);
        this.f10692l = z0Var;
        z0.h hVar = (z0.h) b6.a.e(z0Var.f11304b);
        this.f10691k = hVar;
        this.G = aVar;
        this.f10690j = hVar.f11378a.equals(Uri.EMPTY) ? null : l0.B(hVar.f11378a);
        this.f10693m = aVar2;
        this.f10699y = aVar3;
        this.f10694n = aVar4;
        this.f10695p = dVar;
        this.q = iVar;
        this.f10696t = cVar;
        this.f10697w = j8;
        this.f10698x = w(null);
        this.f10689h = aVar != null;
        this.f10700z = new ArrayList<>();
    }

    private void J() {
        a.b[] bVarArr;
        s sVar;
        for (int i8 = 0; i8 < this.f10700z.size(); i8++) {
            this.f10700z.get(i8).w(this.G);
        }
        long j8 = Long.MIN_VALUE;
        long j9 = Long.MAX_VALUE;
        for (a.b bVar : this.G.f10769f) {
            if (bVar.f10785k > 0) {
                j9 = Math.min(j9, bVar.e(0));
                j8 = Math.max(j8, bVar.e(bVar.f10785k - 1) + bVar.c(bVar.f10785k - 1));
            }
        }
        if (j9 == Long.MAX_VALUE) {
            long j10 = this.G.f10767d ? -9223372036854775807L : 0L;
            com.google.android.exoplayer2.source.smoothstreaming.manifest.a aVar = this.G;
            boolean z4 = aVar.f10767d;
            sVar = new s(j10, 0L, 0L, 0L, true, z4, z4, aVar, this.f10692l);
        } else {
            com.google.android.exoplayer2.source.smoothstreaming.manifest.a aVar2 = this.G;
            if (aVar2.f10767d) {
                long j11 = aVar2.f10771h;
                if (j11 != -9223372036854775807L && j11 > 0) {
                    j9 = Math.max(j9, j8 - j11);
                }
                long j12 = j9;
                long j13 = j8 - j12;
                long C0 = j13 - l0.C0(this.f10697w);
                if (C0 < 5000000) {
                    C0 = Math.min(5000000L, j13 / 2);
                }
                sVar = new s(-9223372036854775807L, j13, j12, C0, true, true, true, this.G, this.f10692l);
            } else {
                long j14 = aVar2.f10770g;
                long j15 = j14 != -9223372036854775807L ? j14 : j8 - j9;
                sVar = new s(j9 + j15, j15, j9, 0L, true, false, false, this.G, this.f10692l);
            }
        }
        D(sVar);
    }

    private void K() {
        if (this.G.f10767d) {
            this.H.postDelayed(new o5.a(this), Math.max(0L, (this.F + 5000) - SystemClock.elapsedRealtime()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void L() {
        if (this.B.i()) {
            return;
        }
        d dVar = new d(this.A, this.f10690j, 4, this.f10699y);
        this.f10698x.z(new h5.h(dVar.f10974a, dVar.f10975b, this.B.n(dVar, this, this.f10696t.d(dVar.f10976c))), dVar.f10976c);
    }

    @Override // com.google.android.exoplayer2.source.a
    protected void C(y yVar) {
        this.E = yVar;
        this.q.b(Looper.myLooper(), A());
        this.q.a();
        if (this.f10689h) {
            this.C = new t.a();
            J();
            return;
        }
        this.A = this.f10693m.a();
        Loader loader = new Loader("SsMediaSource");
        this.B = loader;
        this.C = loader;
        this.H = l0.w();
        L();
    }

    @Override // com.google.android.exoplayer2.source.a
    protected void E() {
        this.G = this.f10689h ? this.G : null;
        this.A = null;
        this.F = 0L;
        Loader loader = this.B;
        if (loader != null) {
            loader.l();
            this.B = null;
        }
        Handler handler = this.H;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.H = null;
        }
        this.q.release();
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: G */
    public void j(d<com.google.android.exoplayer2.source.smoothstreaming.manifest.a> dVar, long j8, long j9, boolean z4) {
        h5.h hVar = new h5.h(dVar.f10974a, dVar.f10975b, dVar.f(), dVar.d(), j8, j9, dVar.b());
        this.f10696t.c(dVar.f10974a);
        this.f10698x.q(hVar, dVar.f10976c);
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: H */
    public void k(d<com.google.android.exoplayer2.source.smoothstreaming.manifest.a> dVar, long j8, long j9) {
        h5.h hVar = new h5.h(dVar.f10974a, dVar.f10975b, dVar.f(), dVar.d(), j8, j9, dVar.b());
        this.f10696t.c(dVar.f10974a);
        this.f10698x.t(hVar, dVar.f10976c);
        this.G = dVar.e();
        this.F = j8 - j9;
        J();
        K();
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: I */
    public Loader.c t(d<com.google.android.exoplayer2.source.smoothstreaming.manifest.a> dVar, long j8, long j9, IOException iOException, int i8) {
        h5.h hVar = new h5.h(dVar.f10974a, dVar.f10975b, dVar.f(), dVar.d(), j8, j9, dVar.b());
        long a9 = this.f10696t.a(new c.C0117c(hVar, new h5.i(dVar.f10976c), iOException, i8));
        Loader.c h8 = a9 == -9223372036854775807L ? Loader.f10909g : Loader.h(false, a9);
        boolean z4 = !h8.c();
        this.f10698x.x(hVar, dVar.f10976c, iOException, z4);
        if (z4) {
            this.f10696t.c(dVar.f10974a);
        }
        return h8;
    }

    @Override // com.google.android.exoplayer2.source.k
    public j b(k.b bVar, a6.b bVar2, long j8) {
        l.a w8 = w(bVar);
        c cVar = new c(this.G, this.f10694n, this.E, this.f10695p, this.q, u(bVar), this.f10696t, w8, this.C, bVar2);
        this.f10700z.add(cVar);
        return cVar;
    }

    @Override // com.google.android.exoplayer2.source.k
    public z0 i() {
        return this.f10692l;
    }

    @Override // com.google.android.exoplayer2.source.k
    public void n() {
        this.C.a();
    }

    @Override // com.google.android.exoplayer2.source.k
    public void p(j jVar) {
        ((c) jVar).v();
        this.f10700z.remove(jVar);
    }
}
