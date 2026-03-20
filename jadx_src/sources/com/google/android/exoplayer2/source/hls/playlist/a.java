package com.google.android.exoplayer2.source.hls.playlist;

import a6.h;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import b6.l0;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.source.hls.playlist.a;
import com.google.android.exoplayer2.source.hls.playlist.d;
import com.google.android.exoplayer2.source.hls.playlist.e;
import com.google.android.exoplayer2.source.l;
import com.google.android.exoplayer2.upstream.HttpDataSource$InvalidResponseCodeException;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.c;
import com.google.common.collect.f1;
import h5.i;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import m5.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements HlsPlaylistTracker, Loader.b<com.google.android.exoplayer2.upstream.d<n5.d>> {

    /* renamed from: t  reason: collision with root package name */
    public static final HlsPlaylistTracker.a f10515t = n5.b.a;

    /* renamed from: a  reason: collision with root package name */
    private final g f10516a;

    /* renamed from: b  reason: collision with root package name */
    private final n5.e f10517b;

    /* renamed from: c  reason: collision with root package name */
    private final com.google.android.exoplayer2.upstream.c f10518c;

    /* renamed from: d  reason: collision with root package name */
    private final HashMap<Uri, c> f10519d;

    /* renamed from: e  reason: collision with root package name */
    private final CopyOnWriteArrayList<HlsPlaylistTracker.b> f10520e;

    /* renamed from: f  reason: collision with root package name */
    private final double f10521f;

    /* renamed from: g  reason: collision with root package name */
    private l.a f10522g;

    /* renamed from: h  reason: collision with root package name */
    private Loader f10523h;

    /* renamed from: j  reason: collision with root package name */
    private Handler f10524j;

    /* renamed from: k  reason: collision with root package name */
    private HlsPlaylistTracker.c f10525k;

    /* renamed from: l  reason: collision with root package name */
    private e f10526l;

    /* renamed from: m  reason: collision with root package name */
    private Uri f10527m;

    /* renamed from: n  reason: collision with root package name */
    private d f10528n;

    /* renamed from: p  reason: collision with root package name */
    private boolean f10529p;
    private long q;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements HlsPlaylistTracker.b {
        private b() {
        }

        @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.b
        public void a() {
            a.this.f10520e.remove(this);
        }

        @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.b
        public boolean e(Uri uri, c.C0117c c0117c, boolean z4) {
            c cVar;
            if (a.this.f10528n == null) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                List<e.b> list = ((e) l0.j(a.this.f10526l)).f10587e;
                int i8 = 0;
                for (int i9 = 0; i9 < list.size(); i9++) {
                    c cVar2 = (c) a.this.f10519d.get(list.get(i9).f10600a);
                    if (cVar2 != null && elapsedRealtime < cVar2.f10538h) {
                        i8++;
                    }
                }
                c.b b9 = a.this.f10518c.b(new c.a(1, 0, a.this.f10526l.f10587e.size(), i8), c0117c);
                if (b9 != null && b9.f10968a == 2 && (cVar = (c) a.this.f10519d.get(uri)) != null) {
                    cVar.h(b9.f10969b);
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class c implements Loader.b<com.google.android.exoplayer2.upstream.d<n5.d>> {

        /* renamed from: a  reason: collision with root package name */
        private final Uri f10531a;

        /* renamed from: b  reason: collision with root package name */
        private final Loader f10532b = new Loader("DefaultHlsPlaylistTracker:MediaPlaylist");

        /* renamed from: c  reason: collision with root package name */
        private final h f10533c;

        /* renamed from: d  reason: collision with root package name */
        private d f10534d;

        /* renamed from: e  reason: collision with root package name */
        private long f10535e;

        /* renamed from: f  reason: collision with root package name */
        private long f10536f;

        /* renamed from: g  reason: collision with root package name */
        private long f10537g;

        /* renamed from: h  reason: collision with root package name */
        private long f10538h;

        /* renamed from: j  reason: collision with root package name */
        private boolean f10539j;

        /* renamed from: k  reason: collision with root package name */
        private IOException f10540k;

        public c(Uri uri) {
            this.f10531a = uri;
            this.f10533c = a.this.f10516a.a(4);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean h(long j8) {
            this.f10538h = SystemClock.elapsedRealtime() + j8;
            return this.f10531a.equals(a.this.f10527m) && !a.this.L();
        }

        private Uri i() {
            d dVar = this.f10534d;
            if (dVar != null) {
                d.f fVar = dVar.f10561v;
                if (fVar.f10580a != -9223372036854775807L || fVar.f10584e) {
                    Uri.Builder buildUpon = this.f10531a.buildUpon();
                    d dVar2 = this.f10534d;
                    if (dVar2.f10561v.f10584e) {
                        buildUpon.appendQueryParameter("_HLS_msn", String.valueOf(dVar2.f10551k + dVar2.f10557r.size()));
                        d dVar3 = this.f10534d;
                        if (dVar3.f10554n != -9223372036854775807L) {
                            List<d.b> list = dVar3.f10558s;
                            int size = list.size();
                            if (!list.isEmpty() && ((d.b) f1.f(list)).f10563n) {
                                size--;
                            }
                            buildUpon.appendQueryParameter("_HLS_part", String.valueOf(size));
                        }
                    }
                    d.f fVar2 = this.f10534d.f10561v;
                    if (fVar2.f10580a != -9223372036854775807L) {
                        buildUpon.appendQueryParameter("_HLS_skip", fVar2.f10581b ? "v2" : "YES");
                    }
                    return buildUpon.build();
                }
            }
            return this.f10531a;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void n(Uri uri) {
            this.f10539j = false;
            p(uri);
        }

        private void p(Uri uri) {
            com.google.android.exoplayer2.upstream.d dVar = new com.google.android.exoplayer2.upstream.d(this.f10533c, uri, 4, a.this.f10517b.a(a.this.f10526l, this.f10534d));
            a.this.f10522g.z(new h5.h(dVar.f10974a, dVar.f10975b, this.f10532b.n(dVar, this, a.this.f10518c.d(dVar.f10976c))), dVar.f10976c);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void q(final Uri uri) {
            this.f10538h = 0L;
            if (this.f10539j || this.f10532b.j() || this.f10532b.i()) {
                return;
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (elapsedRealtime >= this.f10537g) {
                p(uri);
                return;
            }
            this.f10539j = true;
            a.this.f10524j.postDelayed(new Runnable() { // from class: com.google.android.exoplayer2.source.hls.playlist.b
                @Override // java.lang.Runnable
                public final void run() {
                    a.c.this.n(uri);
                }
            }, this.f10537g - elapsedRealtime);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void w(d dVar, h5.h hVar) {
            IOException playlistStuckException;
            boolean z4;
            d dVar2 = this.f10534d;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            this.f10535e = elapsedRealtime;
            d G = a.this.G(dVar2, dVar);
            this.f10534d = G;
            boolean z8 = false;
            if (G != dVar2) {
                this.f10540k = null;
                this.f10536f = elapsedRealtime;
                a.this.R(this.f10531a, G);
            } else if (!G.f10555o) {
                d dVar3 = this.f10534d;
                if (dVar.f10551k + dVar.f10557r.size() < dVar3.f10551k) {
                    playlistStuckException = new HlsPlaylistTracker.PlaylistResetException(this.f10531a);
                    z4 = true;
                } else {
                    playlistStuckException = ((double) (elapsedRealtime - this.f10536f)) > ((double) l0.a1(dVar3.f10553m)) * a.this.f10521f ? new HlsPlaylistTracker.PlaylistStuckException(this.f10531a) : null;
                    z4 = false;
                }
                if (playlistStuckException != null) {
                    this.f10540k = playlistStuckException;
                    a.this.N(this.f10531a, new c.C0117c(hVar, new i(4), playlistStuckException, 1), z4);
                }
            }
            long j8 = 0;
            d dVar4 = this.f10534d;
            if (!dVar4.f10561v.f10584e) {
                j8 = dVar4.f10553m;
                if (dVar4 == dVar2) {
                    j8 /= 2;
                }
            }
            this.f10537g = elapsedRealtime + l0.a1(j8);
            if (this.f10534d.f10554n != -9223372036854775807L || this.f10531a.equals(a.this.f10527m)) {
                z8 = true;
            }
            if (!z8 || this.f10534d.f10555o) {
                return;
            }
            q(i());
        }

        public d l() {
            return this.f10534d;
        }

        public boolean m() {
            int i8;
            if (this.f10534d == null) {
                return false;
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long max = Math.max(30000L, l0.a1(this.f10534d.f10560u));
            d dVar = this.f10534d;
            return dVar.f10555o || (i8 = dVar.f10544d) == 2 || i8 == 1 || this.f10535e + max > elapsedRealtime;
        }

        public void o() {
            q(this.f10531a);
        }

        public void r() {
            this.f10532b.a();
            IOException iOException = this.f10540k;
            if (iOException != null) {
                throw iOException;
            }
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.b
        /* renamed from: s */
        public void j(com.google.android.exoplayer2.upstream.d<n5.d> dVar, long j8, long j9, boolean z4) {
            h5.h hVar = new h5.h(dVar.f10974a, dVar.f10975b, dVar.f(), dVar.d(), j8, j9, dVar.b());
            a.this.f10518c.c(dVar.f10974a);
            a.this.f10522g.q(hVar, 4);
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.b
        /* renamed from: u */
        public void k(com.google.android.exoplayer2.upstream.d<n5.d> dVar, long j8, long j9) {
            n5.d e8 = dVar.e();
            h5.h hVar = new h5.h(dVar.f10974a, dVar.f10975b, dVar.f(), dVar.d(), j8, j9, dVar.b());
            if (e8 instanceof d) {
                w((d) e8, hVar);
                a.this.f10522g.t(hVar, 4);
            } else {
                this.f10540k = ParserException.c("Loaded playlist has unexpected type.", null);
                a.this.f10522g.x(hVar, 4, this.f10540k, true);
            }
            a.this.f10518c.c(dVar.f10974a);
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.b
        /* renamed from: v */
        public Loader.c t(com.google.android.exoplayer2.upstream.d<n5.d> dVar, long j8, long j9, IOException iOException, int i8) {
            Loader.c cVar;
            h5.h hVar = new h5.h(dVar.f10974a, dVar.f10975b, dVar.f(), dVar.d(), j8, j9, dVar.b());
            boolean z4 = iOException instanceof HlsPlaylistParser.DeltaUpdateException;
            if ((dVar.f().getQueryParameter("_HLS_msn") != null) || z4) {
                int i9 = iOException instanceof HttpDataSource$InvalidResponseCodeException ? ((HttpDataSource$InvalidResponseCodeException) iOException).f10902d : Integer.MAX_VALUE;
                if (z4 || i9 == 400 || i9 == 503) {
                    this.f10537g = SystemClock.elapsedRealtime();
                    o();
                    ((l.a) l0.j(a.this.f10522g)).x(hVar, dVar.f10976c, iOException, true);
                    return Loader.f10908f;
                }
            }
            c.C0117c c0117c = new c.C0117c(hVar, new i(dVar.f10976c), iOException, i8);
            if (a.this.N(this.f10531a, c0117c, false)) {
                long a9 = a.this.f10518c.a(c0117c);
                cVar = a9 != -9223372036854775807L ? Loader.h(false, a9) : Loader.f10909g;
            } else {
                cVar = Loader.f10908f;
            }
            boolean c9 = true ^ cVar.c();
            a.this.f10522g.x(hVar, dVar.f10976c, iOException, c9);
            if (c9) {
                a.this.f10518c.c(dVar.f10974a);
            }
            return cVar;
        }

        public void x() {
            this.f10532b.l();
        }
    }

    public a(g gVar, com.google.android.exoplayer2.upstream.c cVar, n5.e eVar) {
        this(gVar, cVar, eVar, 3.5d);
    }

    public a(g gVar, com.google.android.exoplayer2.upstream.c cVar, n5.e eVar, double d8) {
        this.f10516a = gVar;
        this.f10517b = eVar;
        this.f10518c = cVar;
        this.f10521f = d8;
        this.f10520e = new CopyOnWriteArrayList<>();
        this.f10519d = new HashMap<>();
        this.q = -9223372036854775807L;
    }

    private void E(List<Uri> list) {
        int size = list.size();
        for (int i8 = 0; i8 < size; i8++) {
            Uri uri = list.get(i8);
            this.f10519d.put(uri, new c(uri));
        }
    }

    private static d.C0112d F(d dVar, d dVar2) {
        int i8 = (int) (dVar2.f10551k - dVar.f10551k);
        List<d.C0112d> list = dVar.f10557r;
        if (i8 < list.size()) {
            return list.get(i8);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public d G(d dVar, d dVar2) {
        return !dVar2.f(dVar) ? dVar2.f10555o ? dVar.d() : dVar : dVar2.c(I(dVar, dVar2), H(dVar, dVar2));
    }

    private int H(d dVar, d dVar2) {
        d.C0112d F;
        if (dVar2.f10549i) {
            return dVar2.f10550j;
        }
        d dVar3 = this.f10528n;
        int i8 = dVar3 != null ? dVar3.f10550j : 0;
        return (dVar == null || (F = F(dVar, dVar2)) == null) ? i8 : (dVar.f10550j + F.f10572d) - dVar2.f10557r.get(0).f10572d;
    }

    private long I(d dVar, d dVar2) {
        if (dVar2.f10556p) {
            return dVar2.f10548h;
        }
        d dVar3 = this.f10528n;
        long j8 = dVar3 != null ? dVar3.f10548h : 0L;
        if (dVar == null) {
            return j8;
        }
        int size = dVar.f10557r.size();
        d.C0112d F = F(dVar, dVar2);
        return F != null ? dVar.f10548h + F.f10573e : ((long) size) == dVar2.f10551k - dVar.f10551k ? dVar.e() : j8;
    }

    private Uri J(Uri uri) {
        d.c cVar;
        d dVar = this.f10528n;
        if (dVar == null || !dVar.f10561v.f10584e || (cVar = dVar.f10559t.get(uri)) == null) {
            return uri;
        }
        Uri.Builder buildUpon = uri.buildUpon();
        buildUpon.appendQueryParameter("_HLS_msn", String.valueOf(cVar.f10565b));
        int i8 = cVar.f10566c;
        if (i8 != -1) {
            buildUpon.appendQueryParameter("_HLS_part", String.valueOf(i8));
        }
        return buildUpon.build();
    }

    private boolean K(Uri uri) {
        List<e.b> list = this.f10526l.f10587e;
        for (int i8 = 0; i8 < list.size(); i8++) {
            if (uri.equals(list.get(i8).f10600a)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean L() {
        List<e.b> list = this.f10526l.f10587e;
        int size = list.size();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        for (int i8 = 0; i8 < size; i8++) {
            c cVar = (c) b6.a.e(this.f10519d.get(list.get(i8).f10600a));
            if (elapsedRealtime > cVar.f10538h) {
                Uri uri = cVar.f10531a;
                this.f10527m = uri;
                cVar.q(J(uri));
                return true;
            }
        }
        return false;
    }

    private void M(Uri uri) {
        if (uri.equals(this.f10527m) || !K(uri)) {
            return;
        }
        d dVar = this.f10528n;
        if (dVar == null || !dVar.f10555o) {
            this.f10527m = uri;
            c cVar = this.f10519d.get(uri);
            d dVar2 = cVar.f10534d;
            if (dVar2 == null || !dVar2.f10555o) {
                cVar.q(J(uri));
                return;
            }
            this.f10528n = dVar2;
            this.f10525k.d(dVar2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean N(Uri uri, c.C0117c c0117c, boolean z4) {
        Iterator<HlsPlaylistTracker.b> it = this.f10520e.iterator();
        boolean z8 = false;
        while (it.hasNext()) {
            z8 |= !it.next().e(uri, c0117c, z4);
        }
        return z8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void R(Uri uri, d dVar) {
        if (uri.equals(this.f10527m)) {
            if (this.f10528n == null) {
                this.f10529p = !dVar.f10555o;
                this.q = dVar.f10548h;
            }
            this.f10528n = dVar;
            this.f10525k.d(dVar);
        }
        Iterator<HlsPlaylistTracker.b> it = this.f10520e.iterator();
        while (it.hasNext()) {
            it.next().a();
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: O */
    public void j(com.google.android.exoplayer2.upstream.d<n5.d> dVar, long j8, long j9, boolean z4) {
        h5.h hVar = new h5.h(dVar.f10974a, dVar.f10975b, dVar.f(), dVar.d(), j8, j9, dVar.b());
        this.f10518c.c(dVar.f10974a);
        this.f10522g.q(hVar, 4);
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: P */
    public void k(com.google.android.exoplayer2.upstream.d<n5.d> dVar, long j8, long j9) {
        n5.d e8 = dVar.e();
        boolean z4 = e8 instanceof d;
        e e9 = z4 ? e.e(e8.f22159a) : (e) e8;
        this.f10526l = e9;
        this.f10527m = e9.f10587e.get(0).f10600a;
        this.f10520e.add(new b());
        E(e9.f10586d);
        h5.h hVar = new h5.h(dVar.f10974a, dVar.f10975b, dVar.f(), dVar.d(), j8, j9, dVar.b());
        c cVar = this.f10519d.get(this.f10527m);
        if (z4) {
            cVar.w((d) e8, hVar);
        } else {
            cVar.o();
        }
        this.f10518c.c(dVar.f10974a);
        this.f10522g.t(hVar, 4);
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: Q */
    public Loader.c t(com.google.android.exoplayer2.upstream.d<n5.d> dVar, long j8, long j9, IOException iOException, int i8) {
        h5.h hVar = new h5.h(dVar.f10974a, dVar.f10975b, dVar.f(), dVar.d(), j8, j9, dVar.b());
        long a9 = this.f10518c.a(new c.C0117c(hVar, new i(dVar.f10976c), iOException, i8));
        boolean z4 = a9 == -9223372036854775807L;
        this.f10522g.x(hVar, dVar.f10976c, iOException, z4);
        if (z4) {
            this.f10518c.c(dVar.f10974a);
        }
        return z4 ? Loader.f10909g : Loader.h(false, a9);
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public boolean a(Uri uri) {
        return this.f10519d.get(uri).m();
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void b(HlsPlaylistTracker.b bVar) {
        this.f10520e.remove(bVar);
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void c(Uri uri) {
        this.f10519d.get(uri).r();
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public long d() {
        return this.q;
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public boolean e() {
        return this.f10529p;
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public e f() {
        return this.f10526l;
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public boolean g(Uri uri, long j8) {
        c cVar = this.f10519d.get(uri);
        if (cVar != null) {
            return !cVar.h(j8);
        }
        return false;
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void h(Uri uri, l.a aVar, HlsPlaylistTracker.c cVar) {
        this.f10524j = l0.w();
        this.f10522g = aVar;
        this.f10525k = cVar;
        com.google.android.exoplayer2.upstream.d dVar = new com.google.android.exoplayer2.upstream.d(this.f10516a.a(4), uri, 4, this.f10517b.b());
        b6.a.f(this.f10523h == null);
        Loader loader = new Loader("DefaultHlsPlaylistTracker:MultivariantPlaylist");
        this.f10523h = loader;
        aVar.z(new h5.h(dVar.f10974a, dVar.f10975b, loader.n(dVar, this, this.f10518c.d(dVar.f10976c))), dVar.f10976c);
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void i() {
        Loader loader = this.f10523h;
        if (loader != null) {
            loader.a();
        }
        Uri uri = this.f10527m;
        if (uri != null) {
            c(uri);
        }
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void l(Uri uri) {
        this.f10519d.get(uri).o();
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void m(HlsPlaylistTracker.b bVar) {
        b6.a.e(bVar);
        this.f10520e.add(bVar);
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public d n(Uri uri, boolean z4) {
        d l8 = this.f10519d.get(uri).l();
        if (l8 != null && z4) {
            M(uri);
        }
        return l8;
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void stop() {
        this.f10527m = null;
        this.f10528n = null;
        this.f10526l = null;
        this.q = -9223372036854775807L;
        this.f10523h.l();
        this.f10523h = null;
        for (c cVar : this.f10519d.values()) {
            cVar.x();
        }
        this.f10524j.removeCallbacksAndMessages(null);
        this.f10524j = null;
        this.f10519d.clear();
    }
}
