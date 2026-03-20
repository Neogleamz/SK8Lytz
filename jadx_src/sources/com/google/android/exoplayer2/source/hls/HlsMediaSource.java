package com.google.android.exoplayer2.source.hls;

import a6.h;
import a6.y;
import android.os.Looper;
import b6.l0;
import com.google.android.exoplayer2.drm.i;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.source.hls.playlist.d;
import com.google.android.exoplayer2.source.j;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.source.l;
import com.google.android.exoplayer2.upstream.b;
import com.google.android.exoplayer2.upstream.c;
import com.google.android.exoplayer2.z0;
import h5.d;
import h5.f;
import h5.s;
import i4.q;
import java.util.List;
import m5.g;
import m5.h;
import n5.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class HlsMediaSource extends com.google.android.exoplayer2.source.a implements HlsPlaylistTracker.c {
    private y A;

    /* renamed from: h  reason: collision with root package name */
    private final h f10448h;

    /* renamed from: j  reason: collision with root package name */
    private final z0.h f10449j;

    /* renamed from: k  reason: collision with root package name */
    private final g f10450k;

    /* renamed from: l  reason: collision with root package name */
    private final d f10451l;

    /* renamed from: m  reason: collision with root package name */
    private final i f10452m;

    /* renamed from: n  reason: collision with root package name */
    private final c f10453n;

    /* renamed from: p  reason: collision with root package name */
    private final boolean f10454p;
    private final int q;

    /* renamed from: t  reason: collision with root package name */
    private final boolean f10455t;

    /* renamed from: w  reason: collision with root package name */
    private final HlsPlaylistTracker f10456w;

    /* renamed from: x  reason: collision with root package name */
    private final long f10457x;

    /* renamed from: y  reason: collision with root package name */
    private final z0 f10458y;

    /* renamed from: z  reason: collision with root package name */
    private z0.g f10459z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class Factory implements k.a {

        /* renamed from: a  reason: collision with root package name */
        private final g f10460a;

        /* renamed from: b  reason: collision with root package name */
        private h f10461b;

        /* renamed from: c  reason: collision with root package name */
        private e f10462c;

        /* renamed from: d  reason: collision with root package name */
        private HlsPlaylistTracker.a f10463d;

        /* renamed from: e  reason: collision with root package name */
        private d f10464e;

        /* renamed from: f  reason: collision with root package name */
        private m4.k f10465f;

        /* renamed from: g  reason: collision with root package name */
        private c f10466g;

        /* renamed from: h  reason: collision with root package name */
        private boolean f10467h;

        /* renamed from: i  reason: collision with root package name */
        private int f10468i;

        /* renamed from: j  reason: collision with root package name */
        private boolean f10469j;

        /* renamed from: k  reason: collision with root package name */
        private long f10470k;

        public Factory(h.a aVar) {
            this(new m5.c(aVar));
        }

        public Factory(g gVar) {
            this.f10460a = (g) b6.a.e(gVar);
            this.f10465f = new com.google.android.exoplayer2.drm.g();
            this.f10462c = new n5.a();
            this.f10463d = com.google.android.exoplayer2.source.hls.playlist.a.f10515t;
            this.f10461b = m5.h.f21882a;
            this.f10466g = new b();
            this.f10464e = new f();
            this.f10468i = 1;
            this.f10470k = -9223372036854775807L;
            this.f10467h = true;
        }

        public HlsMediaSource a(z0 z0Var) {
            b6.a.e(z0Var.f11304b);
            e eVar = this.f10462c;
            List<StreamKey> list = z0Var.f11304b.f11382e;
            if (!list.isEmpty()) {
                eVar = new n5.c(eVar, list);
            }
            g gVar = this.f10460a;
            m5.h hVar = this.f10461b;
            d dVar = this.f10464e;
            i a9 = this.f10465f.a(z0Var);
            c cVar = this.f10466g;
            return new HlsMediaSource(z0Var, gVar, hVar, dVar, a9, cVar, this.f10463d.a(this.f10460a, cVar, eVar), this.f10470k, this.f10467h, this.f10468i, this.f10469j);
        }
    }

    static {
        q.a("goog.exo.hls");
    }

    private HlsMediaSource(z0 z0Var, g gVar, m5.h hVar, d dVar, i iVar, c cVar, HlsPlaylistTracker hlsPlaylistTracker, long j8, boolean z4, int i8, boolean z8) {
        this.f10449j = (z0.h) b6.a.e(z0Var.f11304b);
        this.f10458y = z0Var;
        this.f10459z = z0Var.f11306d;
        this.f10450k = gVar;
        this.f10448h = hVar;
        this.f10451l = dVar;
        this.f10452m = iVar;
        this.f10453n = cVar;
        this.f10456w = hlsPlaylistTracker;
        this.f10457x = j8;
        this.f10454p = z4;
        this.q = i8;
        this.f10455t = z8;
    }

    private s F(com.google.android.exoplayer2.source.hls.playlist.d dVar, long j8, long j9, com.google.android.exoplayer2.source.hls.a aVar) {
        long d8 = dVar.f10548h - this.f10456w.d();
        long j10 = dVar.f10555o ? d8 + dVar.f10560u : -9223372036854775807L;
        long J = J(dVar);
        long j11 = this.f10459z.f11368a;
        M(dVar, l0.r(j11 != -9223372036854775807L ? l0.C0(j11) : L(dVar, J), J, dVar.f10560u + J));
        return new s(j8, j9, -9223372036854775807L, j10, dVar.f10560u, d8, K(dVar, J), true, !dVar.f10555o, dVar.f10544d == 2 && dVar.f10546f, aVar, this.f10458y, this.f10459z);
    }

    private s G(com.google.android.exoplayer2.source.hls.playlist.d dVar, long j8, long j9, com.google.android.exoplayer2.source.hls.a aVar) {
        long j10;
        if (dVar.f10545e == -9223372036854775807L || dVar.f10557r.isEmpty()) {
            j10 = 0;
        } else {
            if (!dVar.f10547g) {
                long j11 = dVar.f10545e;
                if (j11 != dVar.f10560u) {
                    j10 = I(dVar.f10557r, j11).f10573e;
                }
            }
            j10 = dVar.f10545e;
        }
        long j12 = dVar.f10560u;
        return new s(j8, j9, -9223372036854775807L, j12, j12, 0L, j10, true, false, true, aVar, this.f10458y, null);
    }

    private static d.b H(List<d.b> list, long j8) {
        d.b bVar = null;
        for (int i8 = 0; i8 < list.size(); i8++) {
            d.b bVar2 = list.get(i8);
            long j9 = bVar2.f10573e;
            if (j9 <= j8 && bVar2.f10562m) {
                bVar = bVar2;
            } else if (j9 > j8) {
                break;
            }
        }
        return bVar;
    }

    private static d.C0112d I(List<d.C0112d> list, long j8) {
        return list.get(l0.g(list, Long.valueOf(j8), true, true));
    }

    private long J(com.google.android.exoplayer2.source.hls.playlist.d dVar) {
        if (dVar.f10556p) {
            return l0.C0(l0.a0(this.f10457x)) - dVar.e();
        }
        return 0L;
    }

    private long K(com.google.android.exoplayer2.source.hls.playlist.d dVar, long j8) {
        long j9 = dVar.f10545e;
        if (j9 == -9223372036854775807L) {
            j9 = (dVar.f10560u + j8) - l0.C0(this.f10459z.f11368a);
        }
        if (dVar.f10547g) {
            return j9;
        }
        d.b H = H(dVar.f10558s, j9);
        if (H != null) {
            return H.f10573e;
        }
        if (dVar.f10557r.isEmpty()) {
            return 0L;
        }
        d.C0112d I = I(dVar.f10557r, j9);
        d.b H2 = H(I.f10568n, j9);
        return H2 != null ? H2.f10573e : I.f10573e;
    }

    private static long L(com.google.android.exoplayer2.source.hls.playlist.d dVar, long j8) {
        long j9;
        d.f fVar = dVar.f10561v;
        long j10 = dVar.f10545e;
        if (j10 != -9223372036854775807L) {
            j9 = dVar.f10560u - j10;
        } else {
            long j11 = fVar.f10583d;
            if (j11 == -9223372036854775807L || dVar.f10554n == -9223372036854775807L) {
                long j12 = fVar.f10582c;
                j9 = j12 != -9223372036854775807L ? j12 : dVar.f10553m * 3;
            } else {
                j9 = j11;
            }
        }
        return j9 + j8;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0047  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void M(com.google.android.exoplayer2.source.hls.playlist.d r5, long r6) {
        /*
            r4 = this;
            com.google.android.exoplayer2.z0 r0 = r4.f10458y
            com.google.android.exoplayer2.z0$g r0 = r0.f11306d
            float r1 = r0.f11371d
            r2 = -8388609(0xffffffffff7fffff, float:-3.4028235E38)
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 != 0) goto L28
            float r0 = r0.f11372e
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 != 0) goto L28
            com.google.android.exoplayer2.source.hls.playlist.d$f r5 = r5.f10561v
            long r0 = r5.f10582c
            r2 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 != 0) goto L28
            long r0 = r5.f10583d
            int r5 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r5 != 0) goto L28
            r5 = 1
            goto L29
        L28:
            r5 = 0
        L29:
            com.google.android.exoplayer2.z0$g$a r0 = new com.google.android.exoplayer2.z0$g$a
            r0.<init>()
            long r6 = b6.l0.a1(r6)
            com.google.android.exoplayer2.z0$g$a r6 = r0.k(r6)
            r7 = 1065353216(0x3f800000, float:1.0)
            if (r5 == 0) goto L3c
            r0 = r7
            goto L40
        L3c:
            com.google.android.exoplayer2.z0$g r0 = r4.f10459z
            float r0 = r0.f11371d
        L40:
            com.google.android.exoplayer2.z0$g$a r6 = r6.j(r0)
            if (r5 == 0) goto L47
            goto L4b
        L47:
            com.google.android.exoplayer2.z0$g r5 = r4.f10459z
            float r7 = r5.f11372e
        L4b:
            com.google.android.exoplayer2.z0$g$a r5 = r6.h(r7)
            com.google.android.exoplayer2.z0$g r5 = r5.f()
            r4.f10459z = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.hls.HlsMediaSource.M(com.google.android.exoplayer2.source.hls.playlist.d, long):void");
    }

    @Override // com.google.android.exoplayer2.source.a
    protected void C(y yVar) {
        this.A = yVar;
        this.f10452m.b((Looper) b6.a.e(Looper.myLooper()), A());
        this.f10452m.a();
        this.f10456w.h(this.f10449j.f11378a, w(null), this);
    }

    @Override // com.google.android.exoplayer2.source.a
    protected void E() {
        this.f10456w.stop();
        this.f10452m.release();
    }

    @Override // com.google.android.exoplayer2.source.k
    public j b(k.b bVar, a6.b bVar2, long j8) {
        l.a w8 = w(bVar);
        return new m5.k(this.f10448h, this.f10456w, this.f10450k, this.A, this.f10452m, u(bVar), this.f10453n, w8, bVar2, this.f10451l, this.f10454p, this.q, this.f10455t, A());
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.c
    public void d(com.google.android.exoplayer2.source.hls.playlist.d dVar) {
        long a12 = dVar.f10556p ? l0.a1(dVar.f10548h) : -9223372036854775807L;
        int i8 = dVar.f10544d;
        long j8 = (i8 == 2 || i8 == 1) ? a12 : -9223372036854775807L;
        com.google.android.exoplayer2.source.hls.a aVar = new com.google.android.exoplayer2.source.hls.a((com.google.android.exoplayer2.source.hls.playlist.e) b6.a.e(this.f10456w.f()), dVar);
        D(this.f10456w.e() ? F(dVar, j8, a12, aVar) : G(dVar, j8, a12, aVar));
    }

    @Override // com.google.android.exoplayer2.source.k
    public z0 i() {
        return this.f10458y;
    }

    @Override // com.google.android.exoplayer2.source.k
    public void n() {
        this.f10456w.i();
    }

    @Override // com.google.android.exoplayer2.source.k
    public void p(j jVar) {
        ((m5.k) jVar).B();
    }
}
