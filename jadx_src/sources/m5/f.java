package m5;

import a6.y;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Pair;
import b6.j0;
import b6.l0;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.source.hls.playlist.d;
import com.google.android.exoplayer2.upstream.a;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.f1;
import h5.u;
import i4.i0;
import j4.t1;
import j5.n;
import j5.o;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class f {

    /* renamed from: a  reason: collision with root package name */
    private final h f21852a;

    /* renamed from: b  reason: collision with root package name */
    private final a6.h f21853b;

    /* renamed from: c  reason: collision with root package name */
    private final a6.h f21854c;

    /* renamed from: d  reason: collision with root package name */
    private final q f21855d;

    /* renamed from: e  reason: collision with root package name */
    private final Uri[] f21856e;

    /* renamed from: f  reason: collision with root package name */
    private final w0[] f21857f;

    /* renamed from: g  reason: collision with root package name */
    private final HlsPlaylistTracker f21858g;

    /* renamed from: h  reason: collision with root package name */
    private final u f21859h;

    /* renamed from: i  reason: collision with root package name */
    private final List<w0> f21860i;

    /* renamed from: k  reason: collision with root package name */
    private final t1 f21862k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f21863l;

    /* renamed from: n  reason: collision with root package name */
    private IOException f21865n;

    /* renamed from: o  reason: collision with root package name */
    private Uri f21866o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f21867p;
    private z5.r q;

    /* renamed from: s  reason: collision with root package name */
    private boolean f21869s;

    /* renamed from: j  reason: collision with root package name */
    private final m5.e f21861j = new m5.e(4);

    /* renamed from: m  reason: collision with root package name */
    private byte[] f21864m = l0.f8068f;

    /* renamed from: r  reason: collision with root package name */
    private long f21868r = -9223372036854775807L;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends j5.l {

        /* renamed from: l  reason: collision with root package name */
        private byte[] f21870l;

        public a(a6.h hVar, com.google.android.exoplayer2.upstream.a aVar, w0 w0Var, int i8, Object obj, byte[] bArr) {
            super(hVar, aVar, 3, w0Var, i8, obj, bArr);
        }

        @Override // j5.l
        protected void g(byte[] bArr, int i8) {
            this.f21870l = Arrays.copyOf(bArr, i8);
        }

        public byte[] j() {
            return this.f21870l;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public j5.f f21871a;

        /* renamed from: b  reason: collision with root package name */
        public boolean f21872b;

        /* renamed from: c  reason: collision with root package name */
        public Uri f21873c;

        public b() {
            a();
        }

        public void a() {
            this.f21871a = null;
            this.f21872b = false;
            this.f21873c = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c extends j5.b {

        /* renamed from: e  reason: collision with root package name */
        private final List<d.e> f21874e;

        /* renamed from: f  reason: collision with root package name */
        private final long f21875f;

        /* renamed from: g  reason: collision with root package name */
        private final String f21876g;

        public c(String str, long j8, List<d.e> list) {
            super(0L, list.size() - 1);
            this.f21876g = str;
            this.f21875f = j8;
            this.f21874e = list;
        }

        @Override // j5.o
        public long a() {
            c();
            return this.f21875f + this.f21874e.get((int) d()).f10573e;
        }

        @Override // j5.o
        public long b() {
            c();
            d.e eVar = this.f21874e.get((int) d());
            return this.f21875f + eVar.f10573e + eVar.f10571c;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class d extends z5.c {

        /* renamed from: h  reason: collision with root package name */
        private int f21877h;

        public d(u uVar, int[] iArr) {
            super(uVar, iArr);
            this.f21877h = l(uVar.b(iArr[0]));
        }

        @Override // z5.r
        public void a(long j8, long j9, long j10, List<? extends n> list, o[] oVarArr) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (e(this.f21877h, elapsedRealtime)) {
                for (int i8 = this.f24609b - 1; i8 >= 0; i8--) {
                    if (!e(i8, elapsedRealtime)) {
                        this.f21877h = i8;
                        return;
                    }
                }
                throw new IllegalStateException();
            }
        }

        @Override // z5.r
        public int c() {
            return this.f21877h;
        }

        @Override // z5.r
        public int o() {
            return 0;
        }

        @Override // z5.r
        public Object r() {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: a  reason: collision with root package name */
        public final d.e f21878a;

        /* renamed from: b  reason: collision with root package name */
        public final long f21879b;

        /* renamed from: c  reason: collision with root package name */
        public final int f21880c;

        /* renamed from: d  reason: collision with root package name */
        public final boolean f21881d;

        public e(d.e eVar, long j8, int i8) {
            this.f21878a = eVar;
            this.f21879b = j8;
            this.f21880c = i8;
            this.f21881d = (eVar instanceof d.b) && ((d.b) eVar).f10563n;
        }
    }

    public f(h hVar, HlsPlaylistTracker hlsPlaylistTracker, Uri[] uriArr, w0[] w0VarArr, g gVar, y yVar, q qVar, List<w0> list, t1 t1Var) {
        this.f21852a = hVar;
        this.f21858g = hlsPlaylistTracker;
        this.f21856e = uriArr;
        this.f21857f = w0VarArr;
        this.f21855d = qVar;
        this.f21860i = list;
        this.f21862k = t1Var;
        a6.h a9 = gVar.a(1);
        this.f21853b = a9;
        if (yVar != null) {
            a9.w(yVar);
        }
        this.f21854c = gVar.a(3);
        this.f21859h = new u(w0VarArr);
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < uriArr.length; i8++) {
            if ((w0VarArr[i8].f11200e & 16384) == 0) {
                arrayList.add(Integer.valueOf(i8));
            }
        }
        this.q = new d(this.f21859h, com.google.common.primitives.g.l(arrayList));
    }

    private static Uri d(com.google.android.exoplayer2.source.hls.playlist.d dVar, d.e eVar) {
        String str;
        if (eVar == null || (str = eVar.f10575g) == null) {
            return null;
        }
        return j0.e(dVar.f22159a, str);
    }

    private Pair<Long, Integer> f(i iVar, boolean z4, com.google.android.exoplayer2.source.hls.playlist.d dVar, long j8, long j9) {
        if (iVar != null && !z4) {
            if (iVar.h()) {
                Long valueOf = Long.valueOf(iVar.f21887o == -1 ? iVar.g() : iVar.f20787j);
                int i8 = iVar.f21887o;
                return new Pair<>(valueOf, Integer.valueOf(i8 != -1 ? i8 + 1 : -1));
            }
            return new Pair<>(Long.valueOf(iVar.f20787j), Integer.valueOf(iVar.f21887o));
        }
        long j10 = dVar.f10560u + j8;
        if (iVar != null && !this.f21867p) {
            j9 = iVar.f20747g;
        }
        if (dVar.f10555o || j9 < j10) {
            long j11 = j9 - j8;
            int i9 = 0;
            int g8 = l0.g(dVar.f10557r, Long.valueOf(j11), true, !this.f21858g.e() || iVar == null);
            long j12 = g8 + dVar.f10551k;
            if (g8 >= 0) {
                d.C0112d c0112d = dVar.f10557r.get(g8);
                List<d.b> list = j11 < c0112d.f10573e + c0112d.f10571c ? c0112d.f10568n : dVar.f10558s;
                while (true) {
                    if (i9 >= list.size()) {
                        break;
                    }
                    d.b bVar = list.get(i9);
                    if (j11 >= bVar.f10573e + bVar.f10571c) {
                        i9++;
                    } else if (bVar.f10562m) {
                        j12 += list == dVar.f10558s ? 1L : 0L;
                        r1 = i9;
                    }
                }
            }
            return new Pair<>(Long.valueOf(j12), Integer.valueOf(r1));
        }
        return new Pair<>(Long.valueOf(dVar.f10551k + dVar.f10557r.size()), -1);
    }

    private static e g(com.google.android.exoplayer2.source.hls.playlist.d dVar, long j8, int i8) {
        int i9 = (int) (j8 - dVar.f10551k);
        if (i9 == dVar.f10557r.size()) {
            if (i8 == -1) {
                i8 = 0;
            }
            if (i8 < dVar.f10558s.size()) {
                return new e(dVar.f10558s.get(i8), j8, i8);
            }
            return null;
        }
        d.C0112d c0112d = dVar.f10557r.get(i9);
        if (i8 == -1) {
            return new e(c0112d, j8, -1);
        }
        if (i8 < c0112d.f10568n.size()) {
            return new e(c0112d.f10568n.get(i8), j8, i8);
        }
        int i10 = i9 + 1;
        if (i10 < dVar.f10557r.size()) {
            return new e(dVar.f10557r.get(i10), j8 + 1, -1);
        }
        if (dVar.f10558s.isEmpty()) {
            return null;
        }
        return new e(dVar.f10558s.get(0), j8 + 1, 0);
    }

    static List<d.e> i(com.google.android.exoplayer2.source.hls.playlist.d dVar, long j8, int i8) {
        int i9 = (int) (j8 - dVar.f10551k);
        if (i9 < 0 || dVar.f10557r.size() < i9) {
            return ImmutableList.E();
        }
        ArrayList arrayList = new ArrayList();
        if (i9 < dVar.f10557r.size()) {
            if (i8 != -1) {
                d.C0112d c0112d = dVar.f10557r.get(i9);
                if (i8 == 0) {
                    arrayList.add(c0112d);
                } else if (i8 < c0112d.f10568n.size()) {
                    List<d.b> list = c0112d.f10568n;
                    arrayList.addAll(list.subList(i8, list.size()));
                }
                i9++;
            }
            List<d.C0112d> list2 = dVar.f10557r;
            arrayList.addAll(list2.subList(i9, list2.size()));
            i8 = 0;
        }
        if (dVar.f10554n != -9223372036854775807L) {
            int i10 = i8 != -1 ? i8 : 0;
            if (i10 < dVar.f10558s.size()) {
                List<d.b> list3 = dVar.f10558s;
                arrayList.addAll(list3.subList(i10, list3.size()));
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    private j5.f l(Uri uri, int i8) {
        if (uri == null) {
            return null;
        }
        byte[] c9 = this.f21861j.c(uri);
        if (c9 != null) {
            this.f21861j.b(uri, c9);
            return null;
        }
        return new a(this.f21854c, new a.b().i(uri).b(1).a(), this.f21857f[i8], this.q.o(), this.q.r(), this.f21864m);
    }

    private long s(long j8) {
        long j9 = this.f21868r;
        if (j9 != -9223372036854775807L) {
            return j9 - j8;
        }
        return -9223372036854775807L;
    }

    private void w(com.google.android.exoplayer2.source.hls.playlist.d dVar) {
        this.f21868r = dVar.f10555o ? -9223372036854775807L : dVar.e() - this.f21858g.d();
    }

    public o[] a(i iVar, long j8) {
        int i8;
        int c9 = iVar == null ? -1 : this.f21859h.c(iVar.f20744d);
        int length = this.q.length();
        o[] oVarArr = new o[length];
        boolean z4 = false;
        int i9 = 0;
        while (i9 < length) {
            int j9 = this.q.j(i9);
            Uri uri = this.f21856e[j9];
            if (this.f21858g.a(uri)) {
                com.google.android.exoplayer2.source.hls.playlist.d n8 = this.f21858g.n(uri, z4);
                b6.a.e(n8);
                long d8 = n8.f10548h - this.f21858g.d();
                i8 = i9;
                Pair<Long, Integer> f5 = f(iVar, j9 != c9 ? true : z4, n8, d8, j8);
                oVarArr[i8] = new c(n8.f22159a, d8, i(n8, ((Long) f5.first).longValue(), ((Integer) f5.second).intValue()));
            } else {
                oVarArr[i9] = o.f20788a;
                i8 = i9;
            }
            i9 = i8 + 1;
            z4 = false;
        }
        return oVarArr;
    }

    public long b(long j8, i0 i0Var) {
        int c9 = this.q.c();
        Uri[] uriArr = this.f21856e;
        com.google.android.exoplayer2.source.hls.playlist.d n8 = (c9 >= uriArr.length || c9 == -1) ? null : this.f21858g.n(uriArr[this.q.m()], true);
        if (n8 == null || n8.f10557r.isEmpty() || !n8.f22161c) {
            return j8;
        }
        long d8 = n8.f10548h - this.f21858g.d();
        long j9 = j8 - d8;
        int g8 = l0.g(n8.f10557r, Long.valueOf(j9), true, true);
        long j10 = n8.f10557r.get(g8).f10573e;
        return i0Var.a(j9, j10, g8 != n8.f10557r.size() - 1 ? n8.f10557r.get(g8 + 1).f10573e : j10) + d8;
    }

    public int c(i iVar) {
        if (iVar.f21887o == -1) {
            return 1;
        }
        com.google.android.exoplayer2.source.hls.playlist.d dVar = (com.google.android.exoplayer2.source.hls.playlist.d) b6.a.e(this.f21858g.n(this.f21856e[this.f21859h.c(iVar.f20744d)], false));
        int i8 = (int) (iVar.f20787j - dVar.f10551k);
        if (i8 < 0) {
            return 1;
        }
        List<d.b> list = i8 < dVar.f10557r.size() ? dVar.f10557r.get(i8).f10568n : dVar.f10558s;
        if (iVar.f21887o >= list.size()) {
            return 2;
        }
        d.b bVar = list.get(iVar.f21887o);
        if (bVar.f10563n) {
            return 0;
        }
        return l0.c(Uri.parse(j0.d(dVar.f22159a, bVar.f10569a)), iVar.f20742b.f10942a) ? 1 : 2;
    }

    public void e(long j8, long j9, List<i> list, boolean z4, b bVar) {
        com.google.android.exoplayer2.source.hls.playlist.d dVar;
        long j10;
        Uri uri;
        int i8;
        i iVar = list.isEmpty() ? null : (i) f1.f(list);
        int c9 = iVar == null ? -1 : this.f21859h.c(iVar.f20744d);
        long j11 = j9 - j8;
        long s8 = s(j8);
        if (iVar != null && !this.f21867p) {
            long d8 = iVar.d();
            j11 = Math.max(0L, j11 - d8);
            if (s8 != -9223372036854775807L) {
                s8 = Math.max(0L, s8 - d8);
            }
        }
        this.q.a(j8, j11, s8, list, a(iVar, j9));
        int m8 = this.q.m();
        boolean z8 = c9 != m8;
        Uri uri2 = this.f21856e[m8];
        if (!this.f21858g.a(uri2)) {
            bVar.f21873c = uri2;
            this.f21869s &= uri2.equals(this.f21866o);
            this.f21866o = uri2;
            return;
        }
        com.google.android.exoplayer2.source.hls.playlist.d n8 = this.f21858g.n(uri2, true);
        b6.a.e(n8);
        this.f21867p = n8.f22161c;
        w(n8);
        long d9 = n8.f10548h - this.f21858g.d();
        Pair<Long, Integer> f5 = f(iVar, z8, n8, d9, j9);
        long longValue = ((Long) f5.first).longValue();
        int intValue = ((Integer) f5.second).intValue();
        if (longValue >= n8.f10551k || iVar == null || !z8) {
            dVar = n8;
            j10 = d9;
            uri = uri2;
            i8 = m8;
        } else {
            Uri uri3 = this.f21856e[c9];
            com.google.android.exoplayer2.source.hls.playlist.d n9 = this.f21858g.n(uri3, true);
            b6.a.e(n9);
            j10 = n9.f10548h - this.f21858g.d();
            Pair<Long, Integer> f8 = f(iVar, false, n9, j10, j9);
            longValue = ((Long) f8.first).longValue();
            intValue = ((Integer) f8.second).intValue();
            i8 = c9;
            uri = uri3;
            dVar = n9;
        }
        if (longValue < dVar.f10551k) {
            this.f21865n = new BehindLiveWindowException();
            return;
        }
        e g8 = g(dVar, longValue, intValue);
        if (g8 == null) {
            if (!dVar.f10555o) {
                bVar.f21873c = uri;
                this.f21869s &= uri.equals(this.f21866o);
                this.f21866o = uri;
                return;
            } else if (z4 || dVar.f10557r.isEmpty()) {
                bVar.f21872b = true;
                return;
            } else {
                g8 = new e((d.e) f1.f(dVar.f10557r), (dVar.f10551k + dVar.f10557r.size()) - 1, -1);
            }
        }
        this.f21869s = false;
        this.f21866o = null;
        Uri d10 = d(dVar, g8.f21878a.f10570b);
        j5.f l8 = l(d10, i8);
        bVar.f21871a = l8;
        if (l8 != null) {
            return;
        }
        Uri d11 = d(dVar, g8.f21878a);
        j5.f l9 = l(d11, i8);
        bVar.f21871a = l9;
        if (l9 != null) {
            return;
        }
        boolean w8 = i.w(iVar, uri, dVar, g8, j10);
        if (w8 && g8.f21881d) {
            return;
        }
        bVar.f21871a = i.j(this.f21852a, this.f21853b, this.f21857f[i8], j10, dVar, g8, uri, this.f21860i, this.q.o(), this.q.r(), this.f21863l, this.f21855d, iVar, this.f21861j.a(d11), this.f21861j.a(d10), w8, this.f21862k);
    }

    public int h(long j8, List<? extends n> list) {
        return (this.f21865n != null || this.q.length() < 2) ? list.size() : this.q.k(j8, list);
    }

    public u j() {
        return this.f21859h;
    }

    public z5.r k() {
        return this.q;
    }

    public boolean m(j5.f fVar, long j8) {
        z5.r rVar = this.q;
        return rVar.d(rVar.u(this.f21859h.c(fVar.f20744d)), j8);
    }

    public void n() {
        IOException iOException = this.f21865n;
        if (iOException != null) {
            throw iOException;
        }
        Uri uri = this.f21866o;
        if (uri == null || !this.f21869s) {
            return;
        }
        this.f21858g.c(uri);
    }

    public boolean o(Uri uri) {
        return l0.s(this.f21856e, uri);
    }

    public void p(j5.f fVar) {
        if (fVar instanceof a) {
            a aVar = (a) fVar;
            this.f21864m = aVar.h();
            this.f21861j.b(aVar.f20742b.f10942a, (byte[]) b6.a.e(aVar.j()));
        }
    }

    public boolean q(Uri uri, long j8) {
        int u8;
        int i8 = 0;
        while (true) {
            Uri[] uriArr = this.f21856e;
            if (i8 >= uriArr.length) {
                i8 = -1;
                break;
            } else if (uriArr[i8].equals(uri)) {
                break;
            } else {
                i8++;
            }
        }
        if (i8 == -1 || (u8 = this.q.u(i8)) == -1) {
            return true;
        }
        this.f21869s |= uri.equals(this.f21866o);
        return j8 == -9223372036854775807L || (this.q.d(u8, j8) && this.f21858g.g(uri, j8));
    }

    public void r() {
        this.f21865n = null;
    }

    public void t(boolean z4) {
        this.f21863l = z4;
    }

    public void u(z5.r rVar) {
        this.q = rVar;
    }

    public boolean v(long j8, j5.f fVar, List<? extends n> list) {
        if (this.f21865n != null) {
            return false;
        }
        return this.q.p(j8, fVar, list);
    }
}
