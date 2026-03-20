package com.google.android.exoplayer2.source;

import android.net.Uri;
import android.os.Handler;
import b6.l0;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.h;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.google.android.exoplayer2.source.g;
import com.google.android.exoplayer2.source.j;
import com.google.android.exoplayer2.source.l;
import com.google.android.exoplayer2.source.v;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.a;
import com.google.android.exoplayer2.upstream.c;
import com.google.android.exoplayer2.w0;
import i4.i0;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import n4.b0;
import n4.z;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r implements j, n4.m, Loader.b<a>, Loader.f, v.d {

    /* renamed from: a0  reason: collision with root package name */
    private static final Map<String, String> f10628a0 = L();

    /* renamed from: b0  reason: collision with root package name */
    private static final w0 f10629b0 = new w0.b().U("icy").g0("application/x-icy").G();
    private boolean A;
    private boolean B;
    private boolean C;
    private e E;
    private n4.z F;
    private boolean H;
    private boolean L;
    private boolean O;
    private int P;
    private boolean Q;
    private long R;
    private boolean W;
    private int X;
    private boolean Y;
    private boolean Z;

    /* renamed from: a  reason: collision with root package name */
    private final Uri f10630a;

    /* renamed from: b  reason: collision with root package name */
    private final a6.h f10631b;

    /* renamed from: c  reason: collision with root package name */
    private final com.google.android.exoplayer2.drm.i f10632c;

    /* renamed from: d  reason: collision with root package name */
    private final com.google.android.exoplayer2.upstream.c f10633d;

    /* renamed from: e  reason: collision with root package name */
    private final l.a f10634e;

    /* renamed from: f  reason: collision with root package name */
    private final h.a f10635f;

    /* renamed from: g  reason: collision with root package name */
    private final b f10636g;

    /* renamed from: h  reason: collision with root package name */
    private final a6.b f10637h;

    /* renamed from: j  reason: collision with root package name */
    private final String f10638j;

    /* renamed from: k  reason: collision with root package name */
    private final long f10639k;

    /* renamed from: m  reason: collision with root package name */
    private final m f10641m;

    /* renamed from: w  reason: collision with root package name */
    private j.a f10645w;

    /* renamed from: x  reason: collision with root package name */
    private IcyHeaders f10646x;

    /* renamed from: l  reason: collision with root package name */
    private final Loader f10640l = new Loader("ProgressiveMediaPeriod");

    /* renamed from: n  reason: collision with root package name */
    private final b6.g f10642n = new b6.g();

    /* renamed from: p  reason: collision with root package name */
    private final Runnable f10643p = new Runnable() { // from class: com.google.android.exoplayer2.source.n
        @Override // java.lang.Runnable
        public final void run() {
            r.this.U();
        }
    };
    private final Runnable q = new Runnable() { // from class: com.google.android.exoplayer2.source.p
        @Override // java.lang.Runnable
        public final void run() {
            r.this.R();
        }
    };

    /* renamed from: t  reason: collision with root package name */
    private final Handler f10644t = l0.w();

    /* renamed from: z  reason: collision with root package name */
    private d[] f10648z = new d[0];

    /* renamed from: y  reason: collision with root package name */
    private v[] f10647y = new v[0];
    private long T = -9223372036854775807L;
    private long G = -9223372036854775807L;
    private int K = 1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class a implements Loader.e, g.a {

        /* renamed from: b  reason: collision with root package name */
        private final Uri f10650b;

        /* renamed from: c  reason: collision with root package name */
        private final a6.x f10651c;

        /* renamed from: d  reason: collision with root package name */
        private final m f10652d;

        /* renamed from: e  reason: collision with root package name */
        private final n4.m f10653e;

        /* renamed from: f  reason: collision with root package name */
        private final b6.g f10654f;

        /* renamed from: h  reason: collision with root package name */
        private volatile boolean f10656h;

        /* renamed from: j  reason: collision with root package name */
        private long f10658j;

        /* renamed from: l  reason: collision with root package name */
        private b0 f10660l;

        /* renamed from: m  reason: collision with root package name */
        private boolean f10661m;

        /* renamed from: g  reason: collision with root package name */
        private final n4.y f10655g = new n4.y();

        /* renamed from: i  reason: collision with root package name */
        private boolean f10657i = true;

        /* renamed from: a  reason: collision with root package name */
        private final long f10649a = h5.h.a();

        /* renamed from: k  reason: collision with root package name */
        private com.google.android.exoplayer2.upstream.a f10659k = i(0);

        public a(Uri uri, a6.h hVar, m mVar, n4.m mVar2, b6.g gVar) {
            this.f10650b = uri;
            this.f10651c = new a6.x(hVar);
            this.f10652d = mVar;
            this.f10653e = mVar2;
            this.f10654f = gVar;
        }

        private com.google.android.exoplayer2.upstream.a i(long j8) {
            return new a.b().i(this.f10650b).h(j8).f(r.this.f10638j).b(6).e(r.f10628a0).a();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void j(long j8, long j9) {
            this.f10655g.f22152a = j8;
            this.f10658j = j9;
            this.f10657i = true;
            this.f10661m = false;
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.e
        public void a() {
            int i8 = 0;
            while (i8 == 0 && !this.f10656h) {
                try {
                    long j8 = this.f10655g.f22152a;
                    com.google.android.exoplayer2.upstream.a i9 = i(j8);
                    this.f10659k = i9;
                    long x8 = this.f10651c.x(i9);
                    if (x8 != -1) {
                        x8 += j8;
                        r.this.Z();
                    }
                    long j9 = x8;
                    r.this.f10646x = IcyHeaders.a(this.f10651c.y());
                    a6.f fVar = this.f10651c;
                    if (r.this.f10646x != null && r.this.f10646x.f10082f != -1) {
                        fVar = new g(this.f10651c, r.this.f10646x.f10082f, this);
                        b0 O = r.this.O();
                        this.f10660l = O;
                        O.f(r.f10629b0);
                    }
                    long j10 = j8;
                    this.f10652d.g(fVar, this.f10650b, this.f10651c.y(), j8, j9, this.f10653e);
                    if (r.this.f10646x != null) {
                        this.f10652d.f();
                    }
                    if (this.f10657i) {
                        this.f10652d.c(j10, this.f10658j);
                        this.f10657i = false;
                    }
                    while (true) {
                        long j11 = j10;
                        while (i8 == 0 && !this.f10656h) {
                            try {
                                this.f10654f.a();
                                i8 = this.f10652d.d(this.f10655g);
                                j10 = this.f10652d.e();
                                if (j10 > r.this.f10639k + j11) {
                                    break;
                                }
                            } catch (InterruptedException unused) {
                                throw new InterruptedIOException();
                            }
                        }
                        this.f10654f.c();
                        r.this.f10644t.post(r.this.q);
                    }
                    if (i8 == 1) {
                        i8 = 0;
                    } else if (this.f10652d.e() != -1) {
                        this.f10655g.f22152a = this.f10652d.e();
                    }
                    a6.j.a(this.f10651c);
                } catch (Throwable th) {
                    if (i8 != 1 && this.f10652d.e() != -1) {
                        this.f10655g.f22152a = this.f10652d.e();
                    }
                    a6.j.a(this.f10651c);
                    throw th;
                }
            }
        }

        @Override // com.google.android.exoplayer2.source.g.a
        public void b(b6.z zVar) {
            long max = !this.f10661m ? this.f10658j : Math.max(r.this.N(true), this.f10658j);
            int a9 = zVar.a();
            b0 b0Var = (b0) b6.a.e(this.f10660l);
            b0Var.b(zVar, a9);
            b0Var.d(max, 1, a9, 0, null);
            this.f10661m = true;
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.e
        public void c() {
            this.f10656h = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void h(long j8, boolean z4, boolean z8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private final class c implements h5.r {

        /* renamed from: a  reason: collision with root package name */
        private final int f10663a;

        public c(int i8) {
            this.f10663a = i8;
        }

        @Override // h5.r
        public void a() {
            r.this.Y(this.f10663a);
        }

        @Override // h5.r
        public boolean e() {
            return r.this.Q(this.f10663a);
        }

        @Override // h5.r
        public int m(long j8) {
            return r.this.i0(this.f10663a, j8);
        }

        @Override // h5.r
        public int o(i4.s sVar, DecoderInputBuffer decoderInputBuffer, int i8) {
            return r.this.e0(this.f10663a, sVar, decoderInputBuffer, i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        public final int f10665a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f10666b;

        public d(int i8, boolean z4) {
            this.f10665a = i8;
            this.f10666b = z4;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || d.class != obj.getClass()) {
                return false;
            }
            d dVar = (d) obj;
            return this.f10665a == dVar.f10665a && this.f10666b == dVar.f10666b;
        }

        public int hashCode() {
            return (this.f10665a * 31) + (this.f10666b ? 1 : 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: a  reason: collision with root package name */
        public final h5.w f10667a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean[] f10668b;

        /* renamed from: c  reason: collision with root package name */
        public final boolean[] f10669c;

        /* renamed from: d  reason: collision with root package name */
        public final boolean[] f10670d;

        public e(h5.w wVar, boolean[] zArr) {
            this.f10667a = wVar;
            this.f10668b = zArr;
            int i8 = wVar.f20316a;
            this.f10669c = new boolean[i8];
            this.f10670d = new boolean[i8];
        }
    }

    public r(Uri uri, a6.h hVar, m mVar, com.google.android.exoplayer2.drm.i iVar, h.a aVar, com.google.android.exoplayer2.upstream.c cVar, l.a aVar2, b bVar, a6.b bVar2, String str, int i8) {
        this.f10630a = uri;
        this.f10631b = hVar;
        this.f10632c = iVar;
        this.f10635f = aVar;
        this.f10633d = cVar;
        this.f10634e = aVar2;
        this.f10636g = bVar;
        this.f10637h = bVar2;
        this.f10638j = str;
        this.f10639k = i8;
        this.f10641m = mVar;
    }

    private void J() {
        b6.a.f(this.B);
        b6.a.e(this.E);
        b6.a.e(this.F);
    }

    private boolean K(a aVar, int i8) {
        n4.z zVar;
        if (this.Q || !((zVar = this.F) == null || zVar.d() == -9223372036854775807L)) {
            this.X = i8;
            return true;
        }
        if (this.B && !k0()) {
            this.W = true;
            return false;
        }
        this.O = this.B;
        this.R = 0L;
        this.X = 0;
        for (v vVar : this.f10647y) {
            vVar.V();
        }
        aVar.j(0L, 0L);
        return true;
    }

    private static Map<String, String> L() {
        HashMap hashMap = new HashMap();
        hashMap.put("Icy-MetaData", "1");
        return Collections.unmodifiableMap(hashMap);
    }

    private int M() {
        int i8 = 0;
        for (v vVar : this.f10647y) {
            i8 += vVar.G();
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long N(boolean z4) {
        long j8 = Long.MIN_VALUE;
        for (int i8 = 0; i8 < this.f10647y.length; i8++) {
            if (z4 || ((e) b6.a.e(this.E)).f10669c[i8]) {
                j8 = Math.max(j8, this.f10647y[i8].z());
            }
        }
        return j8;
    }

    private boolean P() {
        return this.T != -9223372036854775807L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void R() {
        if (this.Z) {
            return;
        }
        ((j.a) b6.a.e(this.f10645w)).e(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void S() {
        this.Q = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void U() {
        if (this.Z || this.B || !this.A || this.F == null) {
            return;
        }
        for (v vVar : this.f10647y) {
            if (vVar.F() == null) {
                return;
            }
        }
        this.f10642n.c();
        int length = this.f10647y.length;
        h5.u[] uVarArr = new h5.u[length];
        boolean[] zArr = new boolean[length];
        for (int i8 = 0; i8 < length; i8++) {
            w0 w0Var = (w0) b6.a.e(this.f10647y[i8].F());
            String str = w0Var.f11207m;
            boolean o5 = b6.t.o(str);
            boolean z4 = o5 || b6.t.s(str);
            zArr[i8] = z4;
            this.C = z4 | this.C;
            IcyHeaders icyHeaders = this.f10646x;
            if (icyHeaders != null) {
                if (o5 || this.f10648z[i8].f10666b) {
                    Metadata metadata = w0Var.f11205k;
                    w0Var = w0Var.b().Z(metadata == null ? new Metadata(icyHeaders) : metadata.a(icyHeaders)).G();
                }
                if (o5 && w0Var.f11201f == -1 && w0Var.f11202g == -1 && icyHeaders.f10077a != -1) {
                    w0Var = w0Var.b().I(icyHeaders.f10077a).G();
                }
            }
            uVarArr[i8] = new h5.u(Integer.toString(i8), w0Var.c(this.f10632c.c(w0Var)));
        }
        this.E = new e(new h5.w(uVarArr), zArr);
        this.B = true;
        ((j.a) b6.a.e(this.f10645w)).k(this);
    }

    private void V(int i8) {
        J();
        e eVar = this.E;
        boolean[] zArr = eVar.f10670d;
        if (zArr[i8]) {
            return;
        }
        w0 b9 = eVar.f10667a.b(i8).b(0);
        this.f10634e.i(b6.t.k(b9.f11207m), b9, 0, null, this.R);
        zArr[i8] = true;
    }

    private void W(int i8) {
        J();
        boolean[] zArr = this.E.f10668b;
        if (this.W && zArr[i8]) {
            if (this.f10647y[i8].K(false)) {
                return;
            }
            this.T = 0L;
            this.W = false;
            this.O = true;
            this.R = 0L;
            this.X = 0;
            for (v vVar : this.f10647y) {
                vVar.V();
            }
            ((j.a) b6.a.e(this.f10645w)).e(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void Z() {
        this.f10644t.post(new Runnable() { // from class: com.google.android.exoplayer2.source.o
            @Override // java.lang.Runnable
            public final void run() {
                r.this.S();
            }
        });
    }

    private b0 d0(d dVar) {
        int length = this.f10647y.length;
        for (int i8 = 0; i8 < length; i8++) {
            if (dVar.equals(this.f10648z[i8])) {
                return this.f10647y[i8];
            }
        }
        v k8 = v.k(this.f10637h, this.f10632c, this.f10635f);
        k8.d0(this);
        int i9 = length + 1;
        d[] dVarArr = (d[]) Arrays.copyOf(this.f10648z, i9);
        dVarArr[length] = dVar;
        this.f10648z = (d[]) l0.k(dVarArr);
        v[] vVarArr = (v[]) Arrays.copyOf(this.f10647y, i9);
        vVarArr[length] = k8;
        this.f10647y = (v[]) l0.k(vVarArr);
        return k8;
    }

    private boolean g0(boolean[] zArr, long j8) {
        int length = this.f10647y.length;
        for (int i8 = 0; i8 < length; i8++) {
            if (!this.f10647y[i8].Z(j8, false) && (zArr[i8] || !this.C)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: h0 */
    public void T(n4.z zVar) {
        this.F = this.f10646x == null ? zVar : new z.b(-9223372036854775807L);
        this.G = zVar.d();
        boolean z4 = !this.Q && zVar.d() == -9223372036854775807L;
        this.H = z4;
        this.K = z4 ? 7 : 1;
        this.f10636g.h(this.G, zVar.h(), this.H);
        if (this.B) {
            return;
        }
        U();
    }

    private void j0() {
        a aVar = new a(this.f10630a, this.f10631b, this.f10641m, this, this.f10642n);
        if (this.B) {
            b6.a.f(P());
            long j8 = this.G;
            if (j8 != -9223372036854775807L && this.T > j8) {
                this.Y = true;
                this.T = -9223372036854775807L;
                return;
            }
            aVar.j(((n4.z) b6.a.e(this.F)).i(this.T).f22153a.f22047b, this.T);
            for (v vVar : this.f10647y) {
                vVar.b0(this.T);
            }
            this.T = -9223372036854775807L;
        }
        this.X = M();
        this.f10634e.A(new h5.h(aVar.f10649a, aVar.f10659k, this.f10640l.n(aVar, this, this.f10633d.d(this.K))), 1, -1, null, 0, null, aVar.f10658j, this.G);
    }

    private boolean k0() {
        return this.O || P();
    }

    b0 O() {
        return d0(new d(0, true));
    }

    boolean Q(int i8) {
        return !k0() && this.f10647y[i8].K(this.Y);
    }

    void X() {
        this.f10640l.k(this.f10633d.d(this.K));
    }

    void Y(int i8) {
        this.f10647y[i8].N();
        X();
    }

    @Override // com.google.android.exoplayer2.source.v.d
    public void a(w0 w0Var) {
        this.f10644t.post(this.f10643p);
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: a0 */
    public void j(a aVar, long j8, long j9, boolean z4) {
        a6.x xVar = aVar.f10651c;
        h5.h hVar = new h5.h(aVar.f10649a, aVar.f10659k, xVar.m(), xVar.n(), j8, j9, xVar.l());
        this.f10633d.c(aVar.f10649a);
        this.f10634e.r(hVar, 1, -1, null, 0, null, aVar.f10658j, this.G);
        if (z4) {
            return;
        }
        for (v vVar : this.f10647y) {
            vVar.V();
        }
        if (this.P > 0) {
            ((j.a) b6.a.e(this.f10645w)).e(this);
        }
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public long b() {
        return g();
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: b0 */
    public void k(a aVar, long j8, long j9) {
        n4.z zVar;
        if (this.G == -9223372036854775807L && (zVar = this.F) != null) {
            boolean h8 = zVar.h();
            long N = N(true);
            long j10 = N == Long.MIN_VALUE ? 0L : N + 10000;
            this.G = j10;
            this.f10636g.h(j10, h8, this.H);
        }
        a6.x xVar = aVar.f10651c;
        h5.h hVar = new h5.h(aVar.f10649a, aVar.f10659k, xVar.m(), xVar.n(), j8, j9, xVar.l());
        this.f10633d.c(aVar.f10649a);
        this.f10634e.u(hVar, 1, -1, null, 0, null, aVar.f10658j, this.G);
        this.Y = true;
        ((j.a) b6.a.e(this.f10645w)).e(this);
    }

    @Override // com.google.android.exoplayer2.source.j
    public long c(long j8, i0 i0Var) {
        J();
        if (this.F.h()) {
            z.a i8 = this.F.i(j8);
            return i0Var.a(j8, i8.f22153a.f22046a, i8.f22154b.f22046a);
        }
        return 0L;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: c0 */
    public Loader.c t(a aVar, long j8, long j9, IOException iOException, int i8) {
        boolean z4;
        a aVar2;
        Loader.c h8;
        a6.x xVar = aVar.f10651c;
        h5.h hVar = new h5.h(aVar.f10649a, aVar.f10659k, xVar.m(), xVar.n(), j8, j9, xVar.l());
        long a9 = this.f10633d.a(new c.C0117c(hVar, new h5.i(1, -1, null, 0, null, l0.a1(aVar.f10658j), l0.a1(this.G)), iOException, i8));
        if (a9 == -9223372036854775807L) {
            h8 = Loader.f10909g;
        } else {
            int M = M();
            if (M > this.X) {
                aVar2 = aVar;
                z4 = true;
            } else {
                z4 = false;
                aVar2 = aVar;
            }
            h8 = K(aVar2, M) ? Loader.h(z4, a9) : Loader.f10908f;
        }
        boolean z8 = !h8.c();
        this.f10634e.w(hVar, 1, -1, null, 0, null, aVar.f10658j, this.G, iOException, z8);
        if (z8) {
            this.f10633d.c(aVar.f10649a);
        }
        return h8;
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public boolean d(long j8) {
        if (this.Y || this.f10640l.i() || this.W) {
            return false;
        }
        if (this.B && this.P == 0) {
            return false;
        }
        boolean e8 = this.f10642n.e();
        if (this.f10640l.j()) {
            return e8;
        }
        j0();
        return true;
    }

    @Override // n4.m
    public b0 e(int i8, int i9) {
        return d0(new d(i8, false));
    }

    int e0(int i8, i4.s sVar, DecoderInputBuffer decoderInputBuffer, int i9) {
        if (k0()) {
            return -3;
        }
        V(i8);
        int S = this.f10647y[i8].S(sVar, decoderInputBuffer, i9, this.Y);
        if (S == -3) {
            W(i8);
        }
        return S;
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public boolean f() {
        return this.f10640l.j() && this.f10642n.d();
    }

    public void f0() {
        if (this.B) {
            for (v vVar : this.f10647y) {
                vVar.R();
            }
        }
        this.f10640l.m(this);
        this.f10644t.removeCallbacksAndMessages(null);
        this.f10645w = null;
        this.Z = true;
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public long g() {
        long j8;
        J();
        if (this.Y || this.P == 0) {
            return Long.MIN_VALUE;
        }
        if (P()) {
            return this.T;
        }
        if (this.C) {
            int length = this.f10647y.length;
            j8 = Long.MAX_VALUE;
            for (int i8 = 0; i8 < length; i8++) {
                e eVar = this.E;
                if (eVar.f10668b[i8] && eVar.f10669c[i8] && !this.f10647y[i8].J()) {
                    j8 = Math.min(j8, this.f10647y[i8].z());
                }
            }
        } else {
            j8 = Long.MAX_VALUE;
        }
        if (j8 == Long.MAX_VALUE) {
            j8 = N(false);
        }
        return j8 == Long.MIN_VALUE ? this.R : j8;
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public void h(long j8) {
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.f
    public void i() {
        for (v vVar : this.f10647y) {
            vVar.T();
        }
        this.f10641m.release();
    }

    int i0(int i8, long j8) {
        if (k0()) {
            return 0;
        }
        V(i8);
        v vVar = this.f10647y[i8];
        int E = vVar.E(j8, this.Y);
        vVar.e0(E);
        if (E == 0) {
            W(i8);
        }
        return E;
    }

    @Override // com.google.android.exoplayer2.source.j
    public void l() {
        X();
        if (this.Y && !this.B) {
            throw ParserException.a("Loading finished before preparation is complete.", null);
        }
    }

    @Override // n4.m
    public void m(final n4.z zVar) {
        this.f10644t.post(new Runnable() { // from class: com.google.android.exoplayer2.source.q
            @Override // java.lang.Runnable
            public final void run() {
                r.this.T(zVar);
            }
        });
    }

    @Override // com.google.android.exoplayer2.source.j
    public long n(long j8) {
        J();
        boolean[] zArr = this.E.f10668b;
        if (!this.F.h()) {
            j8 = 0;
        }
        int i8 = 0;
        this.O = false;
        this.R = j8;
        if (P()) {
            this.T = j8;
            return j8;
        } else if (this.K == 7 || !g0(zArr, j8)) {
            this.W = false;
            this.T = j8;
            this.Y = false;
            if (this.f10640l.j()) {
                v[] vVarArr = this.f10647y;
                int length = vVarArr.length;
                while (i8 < length) {
                    vVarArr[i8].r();
                    i8++;
                }
                this.f10640l.f();
            } else {
                this.f10640l.g();
                v[] vVarArr2 = this.f10647y;
                int length2 = vVarArr2.length;
                while (i8 < length2) {
                    vVarArr2[i8].V();
                    i8++;
                }
            }
            return j8;
        } else {
            return j8;
        }
    }

    @Override // n4.m
    public void o() {
        this.A = true;
        this.f10644t.post(this.f10643p);
    }

    @Override // com.google.android.exoplayer2.source.j
    public long p() {
        if (this.O) {
            if (this.Y || M() > this.X) {
                this.O = false;
                return this.R;
            }
            return -9223372036854775807L;
        }
        return -9223372036854775807L;
    }

    @Override // com.google.android.exoplayer2.source.j
    public void q(j.a aVar, long j8) {
        this.f10645w = aVar;
        this.f10642n.e();
        j0();
    }

    @Override // com.google.android.exoplayer2.source.j
    public h5.w r() {
        J();
        return this.E.f10667a;
    }

    @Override // com.google.android.exoplayer2.source.j
    public long s(z5.r[] rVarArr, boolean[] zArr, h5.r[] rVarArr2, boolean[] zArr2, long j8) {
        J();
        e eVar = this.E;
        h5.w wVar = eVar.f10667a;
        boolean[] zArr3 = eVar.f10669c;
        int i8 = this.P;
        int i9 = 0;
        for (int i10 = 0; i10 < rVarArr.length; i10++) {
            if (rVarArr2[i10] != null && (rVarArr[i10] == null || !zArr[i10])) {
                int i11 = ((c) rVarArr2[i10]).f10663a;
                b6.a.f(zArr3[i11]);
                this.P--;
                zArr3[i11] = false;
                rVarArr2[i10] = null;
            }
        }
        boolean z4 = !this.L ? j8 == 0 : i8 != 0;
        for (int i12 = 0; i12 < rVarArr.length; i12++) {
            if (rVarArr2[i12] == null && rVarArr[i12] != null) {
                z5.r rVar = rVarArr[i12];
                b6.a.f(rVar.length() == 1);
                b6.a.f(rVar.j(0) == 0);
                int c9 = wVar.c(rVar.b());
                b6.a.f(!zArr3[c9]);
                this.P++;
                zArr3[c9] = true;
                rVarArr2[i12] = new c(c9);
                zArr2[i12] = true;
                if (!z4) {
                    v vVar = this.f10647y[c9];
                    z4 = (vVar.Z(j8, true) || vVar.C() == 0) ? false : true;
                }
            }
        }
        if (this.P == 0) {
            this.W = false;
            this.O = false;
            if (this.f10640l.j()) {
                v[] vVarArr = this.f10647y;
                int length = vVarArr.length;
                while (i9 < length) {
                    vVarArr[i9].r();
                    i9++;
                }
                this.f10640l.f();
            } else {
                v[] vVarArr2 = this.f10647y;
                int length2 = vVarArr2.length;
                while (i9 < length2) {
                    vVarArr2[i9].V();
                    i9++;
                }
            }
        } else if (z4) {
            j8 = n(j8);
            while (i9 < rVarArr2.length) {
                if (rVarArr2[i9] != null) {
                    zArr2[i9] = true;
                }
                i9++;
            }
        }
        this.L = true;
        return j8;
    }

    @Override // com.google.android.exoplayer2.source.j
    public void u(long j8, boolean z4) {
        J();
        if (P()) {
            return;
        }
        boolean[] zArr = this.E.f10669c;
        int length = this.f10647y.length;
        for (int i8 = 0; i8 < length; i8++) {
            this.f10647y[i8].q(j8, z4, zArr[i8]);
        }
    }
}
