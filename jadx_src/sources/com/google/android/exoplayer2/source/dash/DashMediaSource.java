package com.google.android.exoplayer2.source.dash;

import a6.h;
import a6.t;
import a6.y;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.SparseArray;
import b6.c0;
import b6.l0;
import b6.p;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.i;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.source.dash.a;
import com.google.android.exoplayer2.source.dash.c;
import com.google.android.exoplayer2.source.dash.e;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.source.l;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.c;
import com.google.android.exoplayer2.upstream.d;
import com.google.android.exoplayer2.z0;
import i4.q;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import l5.j;
import l5.o;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class DashMediaSource extends com.google.android.exoplayer2.source.a {
    private final SparseArray<com.google.android.exoplayer2.source.dash.b> A;
    private final Runnable B;
    private final Runnable C;
    private final e.b E;
    private final t F;
    private a6.h G;
    private Loader H;
    private y K;
    private IOException L;
    private Handler O;
    private z0.g P;
    private Uri Q;
    private Uri R;
    private l5.c T;
    private boolean W;
    private long X;
    private long Y;
    private long Z;

    /* renamed from: a0  reason: collision with root package name */
    private int f10308a0;

    /* renamed from: b0  reason: collision with root package name */
    private long f10309b0;

    /* renamed from: c0  reason: collision with root package name */
    private int f10310c0;

    /* renamed from: h  reason: collision with root package name */
    private final z0 f10311h;

    /* renamed from: j  reason: collision with root package name */
    private final boolean f10312j;

    /* renamed from: k  reason: collision with root package name */
    private final h.a f10313k;

    /* renamed from: l  reason: collision with root package name */
    private final a.InterfaceC0109a f10314l;

    /* renamed from: m  reason: collision with root package name */
    private final h5.d f10315m;

    /* renamed from: n  reason: collision with root package name */
    private final i f10316n;

    /* renamed from: p  reason: collision with root package name */
    private final com.google.android.exoplayer2.upstream.c f10317p;
    private final k5.b q;

    /* renamed from: t  reason: collision with root package name */
    private final long f10318t;

    /* renamed from: w  reason: collision with root package name */
    private final l.a f10319w;

    /* renamed from: x  reason: collision with root package name */
    private final d.a<? extends l5.c> f10320x;

    /* renamed from: y  reason: collision with root package name */
    private final e f10321y;

    /* renamed from: z  reason: collision with root package name */
    private final Object f10322z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class Factory implements k.a {

        /* renamed from: a  reason: collision with root package name */
        private final a.InterfaceC0109a f10323a;

        /* renamed from: b  reason: collision with root package name */
        private final h.a f10324b;

        /* renamed from: c  reason: collision with root package name */
        private m4.k f10325c;

        /* renamed from: d  reason: collision with root package name */
        private h5.d f10326d;

        /* renamed from: e  reason: collision with root package name */
        private com.google.android.exoplayer2.upstream.c f10327e;

        /* renamed from: f  reason: collision with root package name */
        private long f10328f;

        /* renamed from: g  reason: collision with root package name */
        private d.a<? extends l5.c> f10329g;

        public Factory(h.a aVar) {
            this(new c.a(aVar), aVar);
        }

        public Factory(a.InterfaceC0109a interfaceC0109a, h.a aVar) {
            this.f10323a = (a.InterfaceC0109a) b6.a.e(interfaceC0109a);
            this.f10324b = aVar;
            this.f10325c = new com.google.android.exoplayer2.drm.g();
            this.f10327e = new com.google.android.exoplayer2.upstream.b();
            this.f10328f = 30000L;
            this.f10326d = new h5.f();
        }

        public DashMediaSource a(z0 z0Var) {
            b6.a.e(z0Var.f11304b);
            d.a aVar = this.f10329g;
            if (aVar == null) {
                aVar = new l5.d();
            }
            List<StreamKey> list = z0Var.f11304b.f11382e;
            return new DashMediaSource(z0Var, null, this.f10324b, !list.isEmpty() ? new g5.c(aVar, list) : aVar, this.f10323a, this.f10326d, this.f10325c.a(z0Var), this.f10327e, this.f10328f, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements c0.b {
        a() {
        }

        @Override // b6.c0.b
        public void a(IOException iOException) {
            DashMediaSource.this.a0(iOException);
        }

        @Override // b6.c0.b
        public void b() {
            DashMediaSource.this.b0(c0.h());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends h2 {

        /* renamed from: f  reason: collision with root package name */
        private final long f10331f;

        /* renamed from: g  reason: collision with root package name */
        private final long f10332g;

        /* renamed from: h  reason: collision with root package name */
        private final long f10333h;

        /* renamed from: j  reason: collision with root package name */
        private final int f10334j;

        /* renamed from: k  reason: collision with root package name */
        private final long f10335k;

        /* renamed from: l  reason: collision with root package name */
        private final long f10336l;

        /* renamed from: m  reason: collision with root package name */
        private final long f10337m;

        /* renamed from: n  reason: collision with root package name */
        private final l5.c f10338n;

        /* renamed from: p  reason: collision with root package name */
        private final z0 f10339p;
        private final z0.g q;

        public b(long j8, long j9, long j10, int i8, long j11, long j12, long j13, l5.c cVar, z0 z0Var, z0.g gVar) {
            b6.a.f(cVar.f21637d == (gVar != null));
            this.f10331f = j8;
            this.f10332g = j9;
            this.f10333h = j10;
            this.f10334j = i8;
            this.f10335k = j11;
            this.f10336l = j12;
            this.f10337m = j13;
            this.f10338n = cVar;
            this.f10339p = z0Var;
            this.q = gVar;
        }

        private long w(long j8) {
            k5.e l8;
            long j9 = this.f10337m;
            if (x(this.f10338n)) {
                if (j8 > 0) {
                    j9 += j8;
                    if (j9 > this.f10336l) {
                        return -9223372036854775807L;
                    }
                }
                long j10 = this.f10335k + j9;
                long g8 = this.f10338n.g(0);
                int i8 = 0;
                while (i8 < this.f10338n.e() - 1 && j10 >= g8) {
                    j10 -= g8;
                    i8++;
                    g8 = this.f10338n.g(i8);
                }
                l5.g d8 = this.f10338n.d(i8);
                int a9 = d8.a(2);
                return (a9 == -1 || (l8 = d8.f21671c.get(a9).f21626c.get(0).l()) == null || l8.i(g8) == 0) ? j9 : (j9 + l8.a(l8.f(j10, g8))) - j10;
            }
            return j9;
        }

        private static boolean x(l5.c cVar) {
            return cVar.f21637d && cVar.f21638e != -9223372036854775807L && cVar.f21635b == -9223372036854775807L;
        }

        @Override // com.google.android.exoplayer2.h2
        public int f(Object obj) {
            int intValue;
            if ((obj instanceof Integer) && (intValue = ((Integer) obj).intValue() - this.f10334j) >= 0 && intValue < m()) {
                return intValue;
            }
            return -1;
        }

        @Override // com.google.android.exoplayer2.h2
        public h2.b k(int i8, h2.b bVar, boolean z4) {
            b6.a.c(i8, 0, m());
            return bVar.u(z4 ? this.f10338n.d(i8).f21669a : null, z4 ? Integer.valueOf(this.f10334j + i8) : null, 0, this.f10338n.g(i8), l0.C0(this.f10338n.d(i8).f21670b - this.f10338n.d(0).f21670b) - this.f10335k);
        }

        @Override // com.google.android.exoplayer2.h2
        public int m() {
            return this.f10338n.e();
        }

        @Override // com.google.android.exoplayer2.h2
        public Object q(int i8) {
            b6.a.c(i8, 0, m());
            return Integer.valueOf(this.f10334j + i8);
        }

        @Override // com.google.android.exoplayer2.h2
        public h2.d s(int i8, h2.d dVar, long j8) {
            b6.a.c(i8, 0, 1);
            long w8 = w(j8);
            Object obj = h2.d.f9767x;
            z0 z0Var = this.f10339p;
            l5.c cVar = this.f10338n;
            return dVar.i(obj, z0Var, cVar, this.f10331f, this.f10332g, this.f10333h, true, x(cVar), this.q, w8, this.f10336l, 0, m() - 1, this.f10335k);
        }

        @Override // com.google.android.exoplayer2.h2
        public int t() {
            return 1;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private final class c implements e.b {
        private c() {
        }

        /* synthetic */ c(DashMediaSource dashMediaSource, a aVar) {
            this();
        }

        @Override // com.google.android.exoplayer2.source.dash.e.b
        public void a() {
            DashMediaSource.this.U();
        }

        @Override // com.google.android.exoplayer2.source.dash.e.b
        public void b(long j8) {
            DashMediaSource.this.T(j8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d implements d.a<Long> {

        /* renamed from: a  reason: collision with root package name */
        private static final Pattern f10341a = Pattern.compile("(.+?)(Z|((\\+|-|−)(\\d\\d)(:?(\\d\\d))?))");

        d() {
        }

        @Override // com.google.android.exoplayer2.upstream.d.a
        /* renamed from: b */
        public Long a(Uri uri, InputStream inputStream) {
            String readLine = new BufferedReader(new InputStreamReader(inputStream, com.google.common.base.e.f18817c)).readLine();
            try {
                Matcher matcher = f10341a.matcher(readLine);
                if (!matcher.matches()) {
                    throw ParserException.c("Couldn't parse timestamp: " + readLine, null);
                }
                String group = matcher.group(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                long time = simpleDateFormat.parse(group).getTime();
                if (!"Z".equals(matcher.group(2))) {
                    long j8 = "+".equals(matcher.group(4)) ? 1L : -1L;
                    long parseLong = Long.parseLong(matcher.group(5));
                    String group2 = matcher.group(7);
                    time -= j8 * ((((parseLong * 60) + (TextUtils.isEmpty(group2) ? 0L : Long.parseLong(group2))) * 60) * 1000);
                }
                return Long.valueOf(time);
            } catch (ParseException e8) {
                throw ParserException.c(null, e8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class e implements Loader.b<com.google.android.exoplayer2.upstream.d<l5.c>> {
        private e() {
        }

        /* synthetic */ e(DashMediaSource dashMediaSource, a aVar) {
            this();
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.b
        /* renamed from: a */
        public void j(com.google.android.exoplayer2.upstream.d<l5.c> dVar, long j8, long j9, boolean z4) {
            DashMediaSource.this.V(dVar, j8, j9);
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.b
        /* renamed from: b */
        public void k(com.google.android.exoplayer2.upstream.d<l5.c> dVar, long j8, long j9) {
            DashMediaSource.this.W(dVar, j8, j9);
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.b
        /* renamed from: c */
        public Loader.c t(com.google.android.exoplayer2.upstream.d<l5.c> dVar, long j8, long j9, IOException iOException, int i8) {
            return DashMediaSource.this.X(dVar, j8, j9, iOException, i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class f implements t {
        f() {
        }

        private void b() {
            if (DashMediaSource.this.L != null) {
                throw DashMediaSource.this.L;
            }
        }

        @Override // a6.t
        public void a() {
            DashMediaSource.this.H.a();
            b();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class g implements Loader.b<com.google.android.exoplayer2.upstream.d<Long>> {
        private g() {
        }

        /* synthetic */ g(DashMediaSource dashMediaSource, a aVar) {
            this();
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.b
        /* renamed from: a */
        public void j(com.google.android.exoplayer2.upstream.d<Long> dVar, long j8, long j9, boolean z4) {
            DashMediaSource.this.V(dVar, j8, j9);
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.b
        /* renamed from: b */
        public void k(com.google.android.exoplayer2.upstream.d<Long> dVar, long j8, long j9) {
            DashMediaSource.this.Y(dVar, j8, j9);
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.b
        /* renamed from: c */
        public Loader.c t(com.google.android.exoplayer2.upstream.d<Long> dVar, long j8, long j9, IOException iOException, int i8) {
            return DashMediaSource.this.Z(dVar, j8, j9, iOException);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class h implements d.a<Long> {
        private h() {
        }

        /* synthetic */ h(a aVar) {
            this();
        }

        @Override // com.google.android.exoplayer2.upstream.d.a
        /* renamed from: b */
        public Long a(Uri uri, InputStream inputStream) {
            return Long.valueOf(l0.J0(new BufferedReader(new InputStreamReader(inputStream)).readLine()));
        }
    }

    static {
        q.a("goog.exo.dash");
    }

    private DashMediaSource(z0 z0Var, l5.c cVar, h.a aVar, d.a<? extends l5.c> aVar2, a.InterfaceC0109a interfaceC0109a, h5.d dVar, i iVar, com.google.android.exoplayer2.upstream.c cVar2, long j8) {
        this.f10311h = z0Var;
        this.P = z0Var.f11306d;
        this.Q = ((z0.h) b6.a.e(z0Var.f11304b)).f11378a;
        this.R = z0Var.f11304b.f11378a;
        this.T = cVar;
        this.f10313k = aVar;
        this.f10320x = aVar2;
        this.f10314l = interfaceC0109a;
        this.f10316n = iVar;
        this.f10317p = cVar2;
        this.f10318t = j8;
        this.f10315m = dVar;
        this.q = new k5.b();
        boolean z4 = cVar != null;
        this.f10312j = z4;
        this.f10319w = w(null);
        this.f10322z = new Object();
        this.A = new SparseArray<>();
        this.E = new c(this, null);
        this.f10309b0 = -9223372036854775807L;
        this.Z = -9223372036854775807L;
        if (!z4) {
            this.f10321y = new e(this, null);
            this.F = new f();
            this.B = new k5.c(this);
            this.C = new k5.d(this);
            return;
        }
        b6.a.f(true ^ cVar.f21637d);
        this.f10321y = null;
        this.B = null;
        this.C = null;
        this.F = new t.a();
    }

    /* synthetic */ DashMediaSource(z0 z0Var, l5.c cVar, h.a aVar, d.a aVar2, a.InterfaceC0109a interfaceC0109a, h5.d dVar, i iVar, com.google.android.exoplayer2.upstream.c cVar2, long j8, a aVar3) {
        this(z0Var, cVar, aVar, aVar2, interfaceC0109a, dVar, iVar, cVar2, j8);
    }

    private static long L(l5.g gVar, long j8, long j9) {
        long C0 = l0.C0(gVar.f21670b);
        boolean P = P(gVar);
        long j10 = Long.MAX_VALUE;
        for (int i8 = 0; i8 < gVar.f21671c.size(); i8++) {
            l5.a aVar = gVar.f21671c.get(i8);
            List<j> list = aVar.f21626c;
            int i9 = aVar.f21625b;
            boolean z4 = true;
            z4 = (i9 == 1 || i9 == 2) ? false : false;
            if ((!P || !z4) && !list.isEmpty()) {
                k5.e l8 = list.get(0).l();
                if (l8 == null) {
                    return C0 + j8;
                }
                long j11 = l8.j(j8, j9);
                if (j11 == 0) {
                    return C0;
                }
                long c9 = (l8.c(j8, j9) + j11) - 1;
                j10 = Math.min(j10, l8.b(c9, j8) + l8.a(c9) + C0);
            }
        }
        return j10;
    }

    private static long M(l5.g gVar, long j8, long j9) {
        long C0 = l0.C0(gVar.f21670b);
        boolean P = P(gVar);
        long j10 = C0;
        for (int i8 = 0; i8 < gVar.f21671c.size(); i8++) {
            l5.a aVar = gVar.f21671c.get(i8);
            List<j> list = aVar.f21626c;
            int i9 = aVar.f21625b;
            boolean z4 = true;
            z4 = (i9 == 1 || i9 == 2) ? false : false;
            if ((!P || !z4) && !list.isEmpty()) {
                k5.e l8 = list.get(0).l();
                if (l8 == null || l8.j(j8, j9) == 0) {
                    return C0;
                }
                j10 = Math.max(j10, l8.a(l8.c(j8, j9)) + C0);
            }
        }
        return j10;
    }

    private static long N(l5.c cVar, long j8) {
        k5.e l8;
        int e8 = cVar.e() - 1;
        l5.g d8 = cVar.d(e8);
        long C0 = l0.C0(d8.f21670b);
        long g8 = cVar.g(e8);
        long C02 = l0.C0(j8);
        long C03 = l0.C0(cVar.f21634a);
        long C04 = l0.C0(5000L);
        for (int i8 = 0; i8 < d8.f21671c.size(); i8++) {
            List<j> list = d8.f21671c.get(i8).f21626c;
            if (!list.isEmpty() && (l8 = list.get(0).l()) != null) {
                long d9 = ((C03 + C0) + l8.d(g8, C02)) - C02;
                if (d9 < C04 - 100000 || (d9 > C04 && d9 < C04 + 100000)) {
                    C04 = d9;
                }
            }
        }
        return b8.c.a(C04, 1000L, RoundingMode.CEILING);
    }

    private long O() {
        return Math.min((this.f10308a0 - 1) * 1000, 5000);
    }

    private static boolean P(l5.g gVar) {
        for (int i8 = 0; i8 < gVar.f21671c.size(); i8++) {
            int i9 = gVar.f21671c.get(i8).f21625b;
            if (i9 == 1 || i9 == 2) {
                return true;
            }
        }
        return false;
    }

    private static boolean Q(l5.g gVar) {
        for (int i8 = 0; i8 < gVar.f21671c.size(); i8++) {
            k5.e l8 = gVar.f21671c.get(i8).f21626c.get(0).l();
            if (l8 == null || l8.g()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void R() {
        c0(false);
    }

    private void S() {
        c0.j(this.H, new a());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a0(IOException iOException) {
        p.d("DashMediaSource", "Failed to resolve time offset.", iOException);
        c0(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b0(long j8) {
        this.Z = j8;
        c0(true);
    }

    private void c0(boolean z4) {
        l5.g gVar;
        long j8;
        long j9;
        for (int i8 = 0; i8 < this.A.size(); i8++) {
            int keyAt = this.A.keyAt(i8);
            if (keyAt >= this.f10310c0) {
                this.A.valueAt(i8).M(this.T, keyAt - this.f10310c0);
            }
        }
        l5.g d8 = this.T.d(0);
        int e8 = this.T.e() - 1;
        l5.g d9 = this.T.d(e8);
        long g8 = this.T.g(e8);
        long C0 = l0.C0(l0.a0(this.Z));
        long M = M(d8, this.T.g(0), C0);
        long L = L(d9, g8, C0);
        boolean z8 = this.T.f21637d && !Q(d9);
        if (z8) {
            long j10 = this.T.f21639f;
            if (j10 != -9223372036854775807L) {
                M = Math.max(M, L - l0.C0(j10));
            }
        }
        long j11 = L - M;
        l5.c cVar = this.T;
        if (cVar.f21637d) {
            b6.a.f(cVar.f21634a != -9223372036854775807L);
            long C02 = (C0 - l0.C0(this.T.f21634a)) - M;
            j0(C02, j11);
            long a12 = this.T.f21634a + l0.a1(M);
            long C03 = C02 - l0.C0(this.P.f11368a);
            long min = Math.min(5000000L, j11 / 2);
            j8 = a12;
            j9 = C03 < min ? min : C03;
            gVar = d8;
        } else {
            gVar = d8;
            j8 = -9223372036854775807L;
            j9 = 0;
        }
        long C04 = M - l0.C0(gVar.f21670b);
        l5.c cVar2 = this.T;
        D(new b(cVar2.f21634a, j8, this.Z, this.f10310c0, C04, j11, j9, cVar2, this.f10311h, cVar2.f21637d ? this.P : null));
        if (this.f10312j) {
            return;
        }
        this.O.removeCallbacks(this.C);
        if (z8) {
            this.O.postDelayed(this.C, N(this.T, l0.a0(this.Z)));
        }
        if (this.W) {
            i0();
        } else if (z4) {
            l5.c cVar3 = this.T;
            if (cVar3.f21637d) {
                long j12 = cVar3.f21638e;
                if (j12 != -9223372036854775807L) {
                    if (j12 == 0) {
                        j12 = 5000;
                    }
                    g0(Math.max(0L, (this.X + j12) - SystemClock.elapsedRealtime()));
                }
            }
        }
    }

    private void d0(o oVar) {
        d.a<Long> dVar;
        String str = oVar.f21724a;
        if (l0.c(str, "urn:mpeg:dash:utc:direct:2014") || l0.c(str, "urn:mpeg:dash:utc:direct:2012")) {
            e0(oVar);
            return;
        }
        if (l0.c(str, "urn:mpeg:dash:utc:http-iso:2014") || l0.c(str, "urn:mpeg:dash:utc:http-iso:2012")) {
            dVar = new d();
        } else if (!l0.c(str, "urn:mpeg:dash:utc:http-xsdate:2014") && !l0.c(str, "urn:mpeg:dash:utc:http-xsdate:2012")) {
            if (l0.c(str, "urn:mpeg:dash:utc:ntp:2014") || l0.c(str, "urn:mpeg:dash:utc:ntp:2012")) {
                S();
                return;
            } else {
                a0(new IOException("Unsupported UTC timing scheme"));
                return;
            }
        } else {
            dVar = new h(null);
        }
        f0(oVar, dVar);
    }

    private void e0(o oVar) {
        try {
            b0(l0.J0(oVar.f21725b) - this.Y);
        } catch (ParserException e8) {
            a0(e8);
        }
    }

    private void f0(o oVar, d.a<Long> aVar) {
        h0(new com.google.android.exoplayer2.upstream.d(this.G, Uri.parse(oVar.f21725b), 5, aVar), new g(this, null), 1);
    }

    private void g0(long j8) {
        this.O.postDelayed(this.B, j8);
    }

    private <T> void h0(com.google.android.exoplayer2.upstream.d<T> dVar, Loader.b<com.google.android.exoplayer2.upstream.d<T>> bVar, int i8) {
        this.f10319w.z(new h5.h(dVar.f10974a, dVar.f10975b, this.H.n(dVar, bVar, i8)), dVar.f10976c);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i0() {
        Uri uri;
        this.O.removeCallbacks(this.B);
        if (this.H.i()) {
            return;
        }
        if (this.H.j()) {
            this.W = true;
            return;
        }
        synchronized (this.f10322z) {
            uri = this.Q;
        }
        this.W = false;
        h0(new com.google.android.exoplayer2.upstream.d(this.G, uri, 4, this.f10320x), this.f10321y, this.f10317p.d(4));
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0061, code lost:
        if (r1 != (-9223372036854775807L)) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0025, code lost:
        if (r1 != (-9223372036854775807L)) goto L3;
     */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0092  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00d8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void j0(long r18, long r20) {
        /*
            Method dump skipped, instructions count: 266
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.dash.DashMediaSource.j0(long, long):void");
    }

    @Override // com.google.android.exoplayer2.source.a
    protected void C(y yVar) {
        this.K = yVar;
        this.f10316n.b(Looper.myLooper(), A());
        this.f10316n.a();
        if (this.f10312j) {
            c0(false);
            return;
        }
        this.G = this.f10313k.a();
        this.H = new Loader("DashMediaSource");
        this.O = l0.w();
        i0();
    }

    @Override // com.google.android.exoplayer2.source.a
    protected void E() {
        this.W = false;
        this.G = null;
        Loader loader = this.H;
        if (loader != null) {
            loader.l();
            this.H = null;
        }
        this.X = 0L;
        this.Y = 0L;
        this.T = this.f10312j ? this.T : null;
        this.Q = this.R;
        this.L = null;
        Handler handler = this.O;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.O = null;
        }
        this.Z = -9223372036854775807L;
        this.f10308a0 = 0;
        this.f10309b0 = -9223372036854775807L;
        this.f10310c0 = 0;
        this.A.clear();
        this.q.i();
        this.f10316n.release();
    }

    void T(long j8) {
        long j9 = this.f10309b0;
        if (j9 == -9223372036854775807L || j9 < j8) {
            this.f10309b0 = j8;
        }
    }

    void U() {
        this.O.removeCallbacks(this.C);
        i0();
    }

    void V(com.google.android.exoplayer2.upstream.d<?> dVar, long j8, long j9) {
        h5.h hVar = new h5.h(dVar.f10974a, dVar.f10975b, dVar.f(), dVar.d(), j8, j9, dVar.b());
        this.f10317p.c(dVar.f10974a);
        this.f10319w.q(hVar, dVar.f10976c);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00c7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void W(com.google.android.exoplayer2.upstream.d<l5.c> r19, long r20, long r22) {
        /*
            Method dump skipped, instructions count: 278
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.dash.DashMediaSource.W(com.google.android.exoplayer2.upstream.d, long, long):void");
    }

    Loader.c X(com.google.android.exoplayer2.upstream.d<l5.c> dVar, long j8, long j9, IOException iOException, int i8) {
        h5.h hVar = new h5.h(dVar.f10974a, dVar.f10975b, dVar.f(), dVar.d(), j8, j9, dVar.b());
        long a9 = this.f10317p.a(new c.C0117c(hVar, new h5.i(dVar.f10976c), iOException, i8));
        Loader.c h8 = a9 == -9223372036854775807L ? Loader.f10909g : Loader.h(false, a9);
        boolean z4 = !h8.c();
        this.f10319w.x(hVar, dVar.f10976c, iOException, z4);
        if (z4) {
            this.f10317p.c(dVar.f10974a);
        }
        return h8;
    }

    void Y(com.google.android.exoplayer2.upstream.d<Long> dVar, long j8, long j9) {
        h5.h hVar = new h5.h(dVar.f10974a, dVar.f10975b, dVar.f(), dVar.d(), j8, j9, dVar.b());
        this.f10317p.c(dVar.f10974a);
        this.f10319w.t(hVar, dVar.f10976c);
        b0(dVar.e().longValue() - j8);
    }

    Loader.c Z(com.google.android.exoplayer2.upstream.d<Long> dVar, long j8, long j9, IOException iOException) {
        this.f10319w.x(new h5.h(dVar.f10974a, dVar.f10975b, dVar.f(), dVar.d(), j8, j9, dVar.b()), dVar.f10976c, iOException, true);
        this.f10317p.c(dVar.f10974a);
        a0(iOException);
        return Loader.f10908f;
    }

    @Override // com.google.android.exoplayer2.source.k
    public com.google.android.exoplayer2.source.j b(k.b bVar, a6.b bVar2, long j8) {
        int intValue = ((Integer) bVar.f20286a).intValue() - this.f10310c0;
        l.a x8 = x(bVar, this.T.d(intValue).f21670b);
        com.google.android.exoplayer2.source.dash.b bVar3 = new com.google.android.exoplayer2.source.dash.b(intValue + this.f10310c0, this.T, this.q, intValue, this.f10314l, this.K, this.f10316n, u(bVar), this.f10317p, x8, this.Z, this.F, bVar2, this.f10315m, this.E, A());
        this.A.put(bVar3.f10345a, bVar3);
        return bVar3;
    }

    @Override // com.google.android.exoplayer2.source.k
    public z0 i() {
        return this.f10311h;
    }

    @Override // com.google.android.exoplayer2.source.k
    public void n() {
        this.F.a();
    }

    @Override // com.google.android.exoplayer2.source.k
    public void p(com.google.android.exoplayer2.source.j jVar) {
        com.google.android.exoplayer2.source.dash.b bVar = (com.google.android.exoplayer2.source.dash.b) jVar;
        bVar.I();
        this.A.remove(bVar.f10345a);
    }
}
