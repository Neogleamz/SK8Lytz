package com.google.android.exoplayer2;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import com.google.android.exoplayer2.c2;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.i;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.j;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.t1;
import com.google.android.exoplayer2.upstream.DataSourceException;
import com.google.android.exoplayer2.z0;
import com.google.android.exoplayer2.z1;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.p2;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import z5.a0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class v0 implements Handler.Callback, j.a, a0.a, t1.d, i.a, z1.a {
    private final y0 A;
    private final long B;
    private i4.i0 C;
    private w1 E;
    private e F;
    private boolean G;
    private boolean H;
    private boolean K;
    private boolean L;
    private boolean O;
    private int P;
    private boolean Q;
    private boolean R;
    private boolean T;
    private boolean W;
    private int X;
    private h Y;
    private long Z;

    /* renamed from: a  reason: collision with root package name */
    private final c2[] f11007a;

    /* renamed from: a0  reason: collision with root package name */
    private int f11008a0;

    /* renamed from: b  reason: collision with root package name */
    private final Set<c2> f11009b;

    /* renamed from: b0  reason: collision with root package name */
    private boolean f11010b0;

    /* renamed from: c  reason: collision with root package name */
    private final i4.f0[] f11011c;

    /* renamed from: c0  reason: collision with root package name */
    private ExoPlaybackException f11012c0;

    /* renamed from: d  reason: collision with root package name */
    private final z5.a0 f11013d;

    /* renamed from: d0  reason: collision with root package name */
    private long f11014d0;

    /* renamed from: e  reason: collision with root package name */
    private final z5.b0 f11015e;

    /* renamed from: e0  reason: collision with root package name */
    private long f11016e0 = -9223372036854775807L;

    /* renamed from: f  reason: collision with root package name */
    private final i4.u f11017f;

    /* renamed from: g  reason: collision with root package name */
    private final a6.d f11018g;

    /* renamed from: h  reason: collision with root package name */
    private final b6.l f11019h;

    /* renamed from: j  reason: collision with root package name */
    private final HandlerThread f11020j;

    /* renamed from: k  reason: collision with root package name */
    private final Looper f11021k;

    /* renamed from: l  reason: collision with root package name */
    private final h2.d f11022l;

    /* renamed from: m  reason: collision with root package name */
    private final h2.b f11023m;

    /* renamed from: n  reason: collision with root package name */
    private final long f11024n;

    /* renamed from: p  reason: collision with root package name */
    private final boolean f11025p;
    private final i q;

    /* renamed from: t  reason: collision with root package name */
    private final ArrayList<d> f11026t;

    /* renamed from: w  reason: collision with root package name */
    private final b6.d f11027w;

    /* renamed from: x  reason: collision with root package name */
    private final f f11028x;

    /* renamed from: y  reason: collision with root package name */
    private final e1 f11029y;

    /* renamed from: z  reason: collision with root package name */
    private final t1 f11030z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements c2.a {
        a() {
        }

        @Override // com.google.android.exoplayer2.c2.a
        public void a() {
            v0.this.T = true;
        }

        @Override // com.google.android.exoplayer2.c2.a
        public void b() {
            v0.this.f11019h.e(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private final List<t1.c> f11032a;

        /* renamed from: b  reason: collision with root package name */
        private final com.google.android.exoplayer2.source.x f11033b;

        /* renamed from: c  reason: collision with root package name */
        private final int f11034c;

        /* renamed from: d  reason: collision with root package name */
        private final long f11035d;

        private b(List<t1.c> list, com.google.android.exoplayer2.source.x xVar, int i8, long j8) {
            this.f11032a = list;
            this.f11033b = xVar;
            this.f11034c = i8;
            this.f11035d = j8;
        }

        /* synthetic */ b(List list, com.google.android.exoplayer2.source.x xVar, int i8, long j8, a aVar) {
            this(list, xVar, i8, j8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: a  reason: collision with root package name */
        public final int f11036a;

        /* renamed from: b  reason: collision with root package name */
        public final int f11037b;

        /* renamed from: c  reason: collision with root package name */
        public final int f11038c;

        /* renamed from: d  reason: collision with root package name */
        public final com.google.android.exoplayer2.source.x f11039d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d implements Comparable<d> {

        /* renamed from: a  reason: collision with root package name */
        public final z1 f11040a;

        /* renamed from: b  reason: collision with root package name */
        public int f11041b;

        /* renamed from: c  reason: collision with root package name */
        public long f11042c;

        /* renamed from: d  reason: collision with root package name */
        public Object f11043d;

        public d(z1 z1Var) {
            this.f11040a = z1Var;
        }

        @Override // java.lang.Comparable
        /* renamed from: c */
        public int compareTo(d dVar) {
            Object obj = this.f11043d;
            if ((obj == null) != (dVar.f11043d == null)) {
                return obj != null ? -1 : 1;
            } else if (obj == null) {
                return 0;
            } else {
                int i8 = this.f11041b - dVar.f11041b;
                return i8 != 0 ? i8 : b6.l0.o(this.f11042c, dVar.f11042c);
            }
        }

        public void f(int i8, long j8, Object obj) {
            this.f11041b = i8;
            this.f11042c = j8;
            this.f11043d = obj;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: a  reason: collision with root package name */
        private boolean f11044a;

        /* renamed from: b  reason: collision with root package name */
        public w1 f11045b;

        /* renamed from: c  reason: collision with root package name */
        public int f11046c;

        /* renamed from: d  reason: collision with root package name */
        public boolean f11047d;

        /* renamed from: e  reason: collision with root package name */
        public int f11048e;

        /* renamed from: f  reason: collision with root package name */
        public boolean f11049f;

        /* renamed from: g  reason: collision with root package name */
        public int f11050g;

        public e(w1 w1Var) {
            this.f11045b = w1Var;
        }

        public void b(int i8) {
            this.f11044a |= i8 > 0;
            this.f11046c += i8;
        }

        public void c(int i8) {
            this.f11044a = true;
            this.f11049f = true;
            this.f11050g = i8;
        }

        public void d(w1 w1Var) {
            this.f11044a |= this.f11045b != w1Var;
            this.f11045b = w1Var;
        }

        public void e(int i8) {
            if (this.f11047d && this.f11048e != 5) {
                b6.a.a(i8 == 5);
                return;
            }
            this.f11044a = true;
            this.f11047d = true;
            this.f11048e = i8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f {
        void a(e eVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g {

        /* renamed from: a  reason: collision with root package name */
        public final k.b f11051a;

        /* renamed from: b  reason: collision with root package name */
        public final long f11052b;

        /* renamed from: c  reason: collision with root package name */
        public final long f11053c;

        /* renamed from: d  reason: collision with root package name */
        public final boolean f11054d;

        /* renamed from: e  reason: collision with root package name */
        public final boolean f11055e;

        /* renamed from: f  reason: collision with root package name */
        public final boolean f11056f;

        public g(k.b bVar, long j8, long j9, boolean z4, boolean z8, boolean z9) {
            this.f11051a = bVar;
            this.f11052b = j8;
            this.f11053c = j9;
            this.f11054d = z4;
            this.f11055e = z8;
            this.f11056f = z9;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class h {

        /* renamed from: a  reason: collision with root package name */
        public final h2 f11057a;

        /* renamed from: b  reason: collision with root package name */
        public final int f11058b;

        /* renamed from: c  reason: collision with root package name */
        public final long f11059c;

        public h(h2 h2Var, int i8, long j8) {
            this.f11057a = h2Var;
            this.f11058b = i8;
            this.f11059c = j8;
        }
    }

    public v0(c2[] c2VarArr, z5.a0 a0Var, z5.b0 b0Var, i4.u uVar, a6.d dVar, int i8, boolean z4, j4.a aVar, i4.i0 i0Var, y0 y0Var, long j8, boolean z8, Looper looper, b6.d dVar2, f fVar, j4.t1 t1Var, Looper looper2) {
        this.f11028x = fVar;
        this.f11007a = c2VarArr;
        this.f11013d = a0Var;
        this.f11015e = b0Var;
        this.f11017f = uVar;
        this.f11018g = dVar;
        this.P = i8;
        this.Q = z4;
        this.C = i0Var;
        this.A = y0Var;
        this.B = j8;
        this.f11014d0 = j8;
        this.H = z8;
        this.f11027w = dVar2;
        this.f11024n = uVar.c();
        this.f11025p = uVar.b();
        w1 j9 = w1.j(b0Var);
        this.E = j9;
        this.F = new e(j9);
        this.f11011c = new i4.f0[c2VarArr.length];
        for (int i9 = 0; i9 < c2VarArr.length; i9++) {
            c2VarArr[i9].l(i9, t1Var);
            this.f11011c[i9] = c2VarArr[i9].q();
        }
        this.q = new i(this, dVar2);
        this.f11026t = new ArrayList<>();
        this.f11009b = p2.h();
        this.f11022l = new h2.d();
        this.f11023m = new h2.b();
        a0Var.b(this, dVar);
        this.f11010b0 = true;
        b6.l d8 = dVar2.d(looper, null);
        this.f11029y = new e1(aVar, d8);
        this.f11030z = new t1(this, aVar, d8, t1Var);
        if (looper2 != null) {
            this.f11020j = null;
            this.f11021k = looper2;
        } else {
            HandlerThread handlerThread = new HandlerThread("ExoPlayer:Playback", -16);
            this.f11020j = handlerThread;
            handlerThread.start();
            this.f11021k = handlerThread.getLooper();
        }
        this.f11019h = dVar2.d(this.f11021k, this);
    }

    private long A(h2 h2Var, Object obj, long j8) {
        h2Var.r(h2Var.l(obj, this.f11023m).f9758c, this.f11022l);
        h2.d dVar = this.f11022l;
        if (dVar.f9775f != -9223372036854775807L && dVar.h()) {
            h2.d dVar2 = this.f11022l;
            if (dVar2.f9778j) {
                return b6.l0.C0(dVar2.c() - this.f11022l.f9775f) - (j8 + this.f11023m.q());
            }
        }
        return -9223372036854775807L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object A0(h2.d dVar, h2.b bVar, int i8, boolean z4, Object obj, h2 h2Var, h2 h2Var2) {
        int f5 = h2Var.f(obj);
        int m8 = h2Var.m();
        int i9 = f5;
        int i10 = -1;
        for (int i11 = 0; i11 < m8 && i10 == -1; i11++) {
            i9 = h2Var.h(i9, bVar, dVar, i8, z4);
            if (i9 == -1) {
                break;
            }
            i10 = h2Var2.f(h2Var.q(i9));
        }
        if (i10 == -1) {
            return null;
        }
        return h2Var2.q(i10);
    }

    private long B() {
        b1 q = this.f11029y.q();
        if (q == null) {
            return 0L;
        }
        long l8 = q.l();
        if (!q.f9462d) {
            return l8;
        }
        int i8 = 0;
        while (true) {
            c2[] c2VarArr = this.f11007a;
            if (i8 >= c2VarArr.length) {
                return l8;
            }
            if (S(c2VarArr[i8]) && this.f11007a[i8].y() == q.f9461c[i8]) {
                long B = this.f11007a[i8].B();
                if (B == Long.MIN_VALUE) {
                    return Long.MIN_VALUE;
                }
                l8 = Math.max(B, l8);
            }
            i8++;
        }
    }

    private void B0(long j8, long j9) {
        this.f11019h.g(2, j8 + j9);
    }

    private Pair<k.b, Long> C(h2 h2Var) {
        if (h2Var.u()) {
            return Pair.create(w1.k(), 0L);
        }
        Pair<Object, Long> n8 = h2Var.n(this.f11022l, this.f11023m, h2Var.e(this.Q), -9223372036854775807L);
        k.b B = this.f11029y.B(h2Var, n8.first, 0L);
        long longValue = ((Long) n8.second).longValue();
        if (B.b()) {
            h2Var.l(B.f20286a, this.f11023m);
            longValue = B.f20288c == this.f11023m.n(B.f20287b) ? this.f11023m.j() : 0L;
        }
        return Pair.create(B, Long.valueOf(longValue));
    }

    private void D0(boolean z4) {
        k.b bVar = this.f11029y.p().f9464f.f9480a;
        long G0 = G0(bVar, this.E.f11257r, true, false);
        if (G0 != this.E.f11257r) {
            w1 w1Var = this.E;
            this.E = N(bVar, G0, w1Var.f11243c, w1Var.f11244d, z4, 5);
        }
    }

    private long E() {
        return F(this.E.f11256p);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x00ac A[Catch: all -> 0x0147, TryCatch #1 {all -> 0x0147, blocks: (B:22:0x00a2, B:24:0x00ac, B:27:0x00b2, B:29:0x00b8, B:30:0x00bb, B:32:0x00c1, B:34:0x00cb, B:36:0x00d3, B:40:0x00db, B:42:0x00e5, B:44:0x00f5, B:48:0x00ff, B:52:0x0111, B:56:0x011a), top: B:74:0x00a2 }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00af  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void E0(com.google.android.exoplayer2.v0.h r19) {
        /*
            Method dump skipped, instructions count: 344
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.v0.E0(com.google.android.exoplayer2.v0$h):void");
    }

    private long F(long j8) {
        b1 j9 = this.f11029y.j();
        if (j9 == null) {
            return 0L;
        }
        return Math.max(0L, j8 - j9.y(this.Z));
    }

    private long F0(k.b bVar, long j8, boolean z4) {
        return G0(bVar, j8, this.f11029y.p() != this.f11029y.q(), z4);
    }

    private void G(com.google.android.exoplayer2.source.j jVar) {
        if (this.f11029y.v(jVar)) {
            this.f11029y.y(this.Z);
            X();
        }
    }

    private long G0(k.b bVar, long j8, boolean z4, boolean z8) {
        l1();
        this.L = false;
        if (z8 || this.E.f11245e == 3) {
            c1(2);
        }
        b1 p8 = this.f11029y.p();
        b1 b1Var = p8;
        while (b1Var != null && !bVar.equals(b1Var.f9464f.f9480a)) {
            b1Var = b1Var.j();
        }
        if (z4 || p8 != b1Var || (b1Var != null && b1Var.z(j8) < 0)) {
            for (c2 c2Var : this.f11007a) {
                o(c2Var);
            }
            if (b1Var != null) {
                while (this.f11029y.p() != b1Var) {
                    this.f11029y.b();
                }
                this.f11029y.z(b1Var);
                b1Var.x(1000000000000L);
                r();
            }
        }
        e1 e1Var = this.f11029y;
        if (b1Var != null) {
            e1Var.z(b1Var);
            if (!b1Var.f9462d) {
                b1Var.f9464f = b1Var.f9464f.b(j8);
            } else if (b1Var.f9463e) {
                long n8 = b1Var.f9459a.n(j8);
                b1Var.f9459a.u(n8 - this.f11024n, this.f11025p);
                j8 = n8;
            }
            u0(j8);
            X();
        } else {
            e1Var.f();
            u0(j8);
        }
        I(false);
        this.f11019h.e(2);
        return j8;
    }

    private void H(IOException iOException, int i8) {
        ExoPlaybackException g8 = ExoPlaybackException.g(iOException, i8);
        b1 p8 = this.f11029y.p();
        if (p8 != null) {
            g8 = g8.e(p8.f9464f.f9480a);
        }
        b6.p.d("ExoPlayerImplInternal", "Playback error", g8);
        k1(false, false);
        this.E = this.E.e(g8);
    }

    private void H0(z1 z1Var) {
        if (z1Var.f() == -9223372036854775807L) {
            I0(z1Var);
        } else if (this.E.f11241a.u()) {
            this.f11026t.add(new d(z1Var));
        } else {
            d dVar = new d(z1Var);
            h2 h2Var = this.E.f11241a;
            if (!w0(dVar, h2Var, h2Var, this.P, this.Q, this.f11022l, this.f11023m)) {
                z1Var.k(false);
                return;
            }
            this.f11026t.add(dVar);
            Collections.sort(this.f11026t);
        }
    }

    private void I(boolean z4) {
        b1 j8 = this.f11029y.j();
        k.b bVar = j8 == null ? this.E.f11242b : j8.f9464f.f9480a;
        boolean z8 = !this.E.f11251k.equals(bVar);
        if (z8) {
            this.E = this.E.b(bVar);
        }
        w1 w1Var = this.E;
        w1Var.f11256p = j8 == null ? w1Var.f11257r : j8.i();
        this.E.q = E();
        if ((z8 || z4) && j8 != null && j8.f9462d) {
            n1(j8.n(), j8.o());
        }
    }

    private void I0(z1 z1Var) {
        if (z1Var.c() != this.f11021k) {
            this.f11019h.j(15, z1Var).a();
            return;
        }
        n(z1Var);
        int i8 = this.E.f11245e;
        if (i8 == 3 || i8 == 2) {
            this.f11019h.e(2);
        }
    }

    /* JADX WARN: Not initialized variable reg: 25, insn: 0x0141: MOVE  (r5 I:??[long, double]) = (r25 I:??[long, double]), block:B:73:0x0140 */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x010d  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0110  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0138  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0153  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x019b  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x019e  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x01c6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void J(com.google.android.exoplayer2.h2 r28, boolean r29) {
        /*
            Method dump skipped, instructions count: 461
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.v0.J(com.google.android.exoplayer2.h2, boolean):void");
    }

    private void J0(final z1 z1Var) {
        Looper c9 = z1Var.c();
        if (c9.getThread().isAlive()) {
            this.f11027w.d(c9, null).b(new Runnable() { // from class: com.google.android.exoplayer2.u0
                @Override // java.lang.Runnable
                public final void run() {
                    v0.this.W(z1Var);
                }
            });
            return;
        }
        b6.p.i("TAG", "Trying to send message on a dead thread.");
        z1Var.k(false);
    }

    private void K(com.google.android.exoplayer2.source.j jVar) {
        if (this.f11029y.v(jVar)) {
            b1 j8 = this.f11029y.j();
            j8.p(this.q.c().f11268a, this.E.f11241a);
            n1(j8.n(), j8.o());
            if (j8 == this.f11029y.p()) {
                u0(j8.f9464f.f9481b);
                r();
                w1 w1Var = this.E;
                k.b bVar = w1Var.f11242b;
                long j9 = j8.f9464f.f9481b;
                this.E = N(bVar, j9, w1Var.f11243c, j9, false, 5);
            }
            X();
        }
    }

    private void K0(long j8) {
        c2[] c2VarArr;
        for (c2 c2Var : this.f11007a) {
            if (c2Var.y() != null) {
                L0(c2Var, j8);
            }
        }
    }

    private void L(x1 x1Var, float f5, boolean z4, boolean z8) {
        c2[] c2VarArr;
        if (z4) {
            if (z8) {
                this.F.b(1);
            }
            this.E = this.E.f(x1Var);
        }
        r1(x1Var.f11268a);
        for (c2 c2Var : this.f11007a) {
            if (c2Var != null) {
                c2Var.s(f5, x1Var.f11268a);
            }
        }
    }

    private void L0(c2 c2Var, long j8) {
        c2Var.m();
        if (c2Var instanceof p5.n) {
            ((p5.n) c2Var).i0(j8);
        }
    }

    private void M(x1 x1Var, boolean z4) {
        L(x1Var, x1Var.f11268a, true, z4);
    }

    private void M0(boolean z4, AtomicBoolean atomicBoolean) {
        c2[] c2VarArr;
        if (this.R != z4) {
            this.R = z4;
            if (!z4) {
                for (c2 c2Var : this.f11007a) {
                    if (!S(c2Var) && this.f11009b.remove(c2Var)) {
                        c2Var.reset();
                    }
                }
            }
        }
        if (atomicBoolean != null) {
            synchronized (this) {
                atomicBoolean.set(true);
                notifyAll();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private w1 N(k.b bVar, long j8, long j9, long j10, boolean z4, int i8) {
        List list;
        h5.w wVar;
        z5.b0 b0Var;
        this.f11010b0 = (!this.f11010b0 && j8 == this.E.f11257r && bVar.equals(this.E.f11242b)) ? false : true;
        t0();
        w1 w1Var = this.E;
        h5.w wVar2 = w1Var.f11248h;
        z5.b0 b0Var2 = w1Var.f11249i;
        List list2 = w1Var.f11250j;
        if (this.f11030z.s()) {
            b1 p8 = this.f11029y.p();
            h5.w n8 = p8 == null ? h5.w.f20313d : p8.n();
            z5.b0 o5 = p8 == null ? this.f11015e : p8.o();
            List x8 = x(o5.f24605c);
            if (p8 != null) {
                c1 c1Var = p8.f9464f;
                if (c1Var.f9482c != j9) {
                    p8.f9464f = c1Var.a(j9);
                }
            }
            wVar = n8;
            b0Var = o5;
            list = x8;
        } else if (bVar.equals(this.E.f11242b)) {
            list = list2;
            wVar = wVar2;
            b0Var = b0Var2;
        } else {
            wVar = h5.w.f20313d;
            b0Var = this.f11015e;
            list = ImmutableList.E();
        }
        if (z4) {
            this.F.e(i8);
        }
        return this.E.c(bVar, j8, j9, j10, E(), wVar, b0Var, list);
    }

    private void N0(x1 x1Var) {
        this.f11019h.i(16);
        this.q.d(x1Var);
    }

    private boolean O(c2 c2Var, b1 b1Var) {
        b1 j8 = b1Var.j();
        return b1Var.f9464f.f9485f && j8.f9462d && ((c2Var instanceof p5.n) || (c2Var instanceof com.google.android.exoplayer2.metadata.a) || c2Var.B() >= j8.m());
    }

    private void O0(b bVar) {
        this.F.b(1);
        if (bVar.f11034c != -1) {
            this.Y = new h(new a2(bVar.f11032a, bVar.f11033b), bVar.f11034c, bVar.f11035d);
        }
        J(this.f11030z.C(bVar.f11032a, bVar.f11033b), false);
    }

    private boolean P() {
        b1 q = this.f11029y.q();
        if (q.f9462d) {
            int i8 = 0;
            while (true) {
                c2[] c2VarArr = this.f11007a;
                if (i8 >= c2VarArr.length) {
                    return true;
                }
                c2 c2Var = c2VarArr[i8];
                h5.r rVar = q.f9461c[i8];
                if (c2Var.y() != rVar || (rVar != null && !c2Var.i() && !O(c2Var, q))) {
                    break;
                }
                i8++;
            }
            return false;
        }
        return false;
    }

    private static boolean Q(boolean z4, k.b bVar, long j8, k.b bVar2, h2.b bVar3, long j9) {
        if (!z4 && j8 == j9 && bVar.f20286a.equals(bVar2.f20286a)) {
            return (bVar.b() && bVar3.t(bVar.f20287b)) ? (bVar3.k(bVar.f20287b, bVar.f20288c) == 4 || bVar3.k(bVar.f20287b, bVar.f20288c) == 2) ? false : true : bVar2.b() && bVar3.t(bVar2.f20287b);
        }
        return false;
    }

    private void Q0(boolean z4) {
        if (z4 == this.W) {
            return;
        }
        this.W = z4;
        if (z4 || !this.E.f11255o) {
            return;
        }
        this.f11019h.e(2);
    }

    private boolean R() {
        b1 j8 = this.f11029y.j();
        return (j8 == null || j8.k() == Long.MIN_VALUE) ? false : true;
    }

    private void R0(boolean z4) {
        this.H = z4;
        t0();
        if (!this.K || this.f11029y.q() == this.f11029y.p()) {
            return;
        }
        D0(true);
        I(false);
    }

    private static boolean S(c2 c2Var) {
        return c2Var.getState() != 0;
    }

    private boolean T() {
        b1 p8 = this.f11029y.p();
        long j8 = p8.f9464f.f9484e;
        return p8.f9462d && (j8 == -9223372036854775807L || this.E.f11257r < j8 || !f1());
    }

    private void T0(boolean z4, int i8, boolean z8, int i9) {
        this.F.b(z8 ? 1 : 0);
        this.F.c(i9);
        this.E = this.E.d(z4, i8);
        this.L = false;
        h0(z4);
        if (!f1()) {
            l1();
            p1();
            return;
        }
        int i10 = this.E.f11245e;
        if (i10 == 3) {
            i1();
        } else if (i10 != 2) {
            return;
        }
        this.f11019h.e(2);
    }

    private static boolean U(w1 w1Var, h2.b bVar) {
        k.b bVar2 = w1Var.f11242b;
        h2 h2Var = w1Var.f11241a;
        return h2Var.u() || h2Var.l(bVar2.f20286a, bVar).f9761f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean V() {
        return Boolean.valueOf(this.G);
    }

    private void V0(x1 x1Var) {
        N0(x1Var);
        M(this.q.c(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void W(z1 z1Var) {
        try {
            n(z1Var);
        } catch (ExoPlaybackException e8) {
            b6.p.d("ExoPlayerImplInternal", "Unexpected error delivering message on external thread.", e8);
            throw new RuntimeException(e8);
        }
    }

    private void X() {
        boolean e12 = e1();
        this.O = e12;
        if (e12) {
            this.f11029y.j().d(this.Z);
        }
        m1();
    }

    private void X0(int i8) {
        this.P = i8;
        if (!this.f11029y.G(this.E.f11241a, i8)) {
            D0(true);
        }
        I(false);
    }

    private void Y() {
        this.F.d(this.E);
        if (this.F.f11044a) {
            this.f11028x.a(this.F);
            this.F = new e(this.E);
        }
    }

    private void Y0(i4.i0 i0Var) {
        this.C = i0Var;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0037, code lost:
        if (r1 > 0) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0039, code lost:
        r3 = r7.f11026t.get(r1 - 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0044, code lost:
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0045, code lost:
        if (r3 == null) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0047, code lost:
        r4 = r3.f11041b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0049, code lost:
        if (r4 > r0) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x004b, code lost:
        if (r4 != r0) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0051, code lost:
        if (r3.f11042c <= r8) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0053, code lost:
        r1 = r1 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0055, code lost:
        if (r1 <= 0) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x005e, code lost:
        if (r1 >= r7.f11026t.size()) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0060, code lost:
        r3 = r7.f11026t.get(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0069, code lost:
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x006a, code lost:
        if (r3 == null) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x006e, code lost:
        if (r3.f11043d == null) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0070, code lost:
        r4 = r3.f11041b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0072, code lost:
        if (r4 < r0) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0074, code lost:
        if (r4 != r0) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x007a, code lost:
        if (r3.f11042c > r8) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x007c, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0084, code lost:
        if (r1 >= r7.f11026t.size()) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0087, code lost:
        if (r3 == null) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x008b, code lost:
        if (r3.f11043d == null) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x008f, code lost:
        if (r3.f11041b != r0) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0091, code lost:
        r4 = r3.f11042c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0095, code lost:
        if (r4 <= r8) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0099, code lost:
        if (r4 > r10) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x009b, code lost:
        I0(r3.f11040a);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00a6, code lost:
        if (r3.f11040a.b() != false) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00ae, code lost:
        if (r3.f11040a.j() == false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00b1, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00b4, code lost:
        r7.f11026t.remove(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00bf, code lost:
        if (r1 >= r7.f11026t.size()) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00c1, code lost:
        r3 = r7.f11026t.get(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00ca, code lost:
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00cc, code lost:
        r8 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00d3, code lost:
        if (r3.f11040a.b() != false) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00dd, code lost:
        r7.f11026t.remove(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00e2, code lost:
        throw r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00e3, code lost:
        r7.f11008a0 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00e5, code lost:
        return;
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:13:0x0044 -> B:14:0x0045). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:21:0x0055 -> B:12:0x0039). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x0069 -> B:27:0x006a). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:36:0x0084 -> B:25:0x0060). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void Z(long r8, long r10) {
        /*
            Method dump skipped, instructions count: 230
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.v0.Z(long, long):void");
    }

    private void a0() {
        c1 o5;
        this.f11029y.y(this.Z);
        if (this.f11029y.D() && (o5 = this.f11029y.o(this.Z, this.E)) != null) {
            b1 g8 = this.f11029y.g(this.f11011c, this.f11013d, this.f11017f.h(), this.f11030z, o5, this.f11015e);
            g8.f9459a.q(this, o5.f9481b);
            if (this.f11029y.p() == g8) {
                u0(o5.f9481b);
            }
            I(false);
        }
        if (!this.O) {
            X();
            return;
        }
        this.O = R();
        m1();
    }

    private void a1(boolean z4) {
        this.Q = z4;
        if (!this.f11029y.H(this.E.f11241a, z4)) {
            D0(true);
        }
        I(false);
    }

    private void b0() {
        boolean z4;
        boolean z8 = false;
        while (d1()) {
            if (z8) {
                Y();
            }
            b1 b1Var = (b1) b6.a.e(this.f11029y.b());
            if (this.E.f11242b.f20286a.equals(b1Var.f9464f.f9480a.f20286a)) {
                k.b bVar = this.E.f11242b;
                if (bVar.f20287b == -1) {
                    k.b bVar2 = b1Var.f9464f.f9480a;
                    if (bVar2.f20287b == -1 && bVar.f20290e != bVar2.f20290e) {
                        z4 = true;
                        c1 c1Var = b1Var.f9464f;
                        k.b bVar3 = c1Var.f9480a;
                        long j8 = c1Var.f9481b;
                        this.E = N(bVar3, j8, c1Var.f9482c, j8, !z4, 0);
                        t0();
                        p1();
                        z8 = true;
                    }
                }
            }
            z4 = false;
            c1 c1Var2 = b1Var.f9464f;
            k.b bVar32 = c1Var2.f9480a;
            long j82 = c1Var2.f9481b;
            this.E = N(bVar32, j82, c1Var2.f9482c, j82, !z4, 0);
            t0();
            p1();
            z8 = true;
        }
    }

    private void b1(com.google.android.exoplayer2.source.x xVar) {
        this.F.b(1);
        J(this.f11030z.D(xVar), false);
    }

    private void c0() {
        b1 q = this.f11029y.q();
        if (q == null) {
            return;
        }
        int i8 = 0;
        if (q.j() != null && !this.K) {
            if (P()) {
                if (q.j().f9462d || this.Z >= q.j().m()) {
                    z5.b0 o5 = q.o();
                    b1 c9 = this.f11029y.c();
                    z5.b0 o8 = c9.o();
                    h2 h2Var = this.E.f11241a;
                    q1(h2Var, c9.f9464f.f9480a, h2Var, q.f9464f.f9480a, -9223372036854775807L, false);
                    if (c9.f9462d && c9.f9459a.p() != -9223372036854775807L) {
                        K0(c9.m());
                        return;
                    }
                    for (int i9 = 0; i9 < this.f11007a.length; i9++) {
                        boolean c10 = o5.c(i9);
                        boolean c11 = o8.c(i9);
                        if (c10 && !this.f11007a[i9].D()) {
                            boolean z4 = this.f11011c[i9].h() == -2;
                            i4.g0 g0Var = o5.f24604b[i9];
                            i4.g0 g0Var2 = o8.f24604b[i9];
                            if (!c11 || !g0Var2.equals(g0Var) || z4) {
                                L0(this.f11007a[i9], c9.m());
                            }
                        }
                    }
                }
            }
        } else if (q.f9464f.f9488i || this.K) {
            while (true) {
                c2[] c2VarArr = this.f11007a;
                if (i8 >= c2VarArr.length) {
                    return;
                }
                c2 c2Var = c2VarArr[i8];
                h5.r rVar = q.f9461c[i8];
                if (rVar != null && c2Var.y() == rVar && c2Var.i()) {
                    long j8 = q.f9464f.f9484e;
                    L0(c2Var, (j8 == -9223372036854775807L || j8 == Long.MIN_VALUE) ? -9223372036854775807L : q.l() + q.f9464f.f9484e);
                }
                i8++;
            }
        }
    }

    private void c1(int i8) {
        w1 w1Var = this.E;
        if (w1Var.f11245e != i8) {
            if (i8 != 2) {
                this.f11016e0 = -9223372036854775807L;
            }
            this.E = w1Var.g(i8);
        }
    }

    private void d0() {
        b1 q = this.f11029y.q();
        if (q == null || this.f11029y.p() == q || q.f9465g || !q0()) {
            return;
        }
        r();
    }

    private boolean d1() {
        b1 p8;
        b1 j8;
        return f1() && !this.K && (p8 = this.f11029y.p()) != null && (j8 = p8.j()) != null && this.Z >= j8.m() && j8.f9465g;
    }

    private void e0() {
        J(this.f11030z.i(), true);
    }

    private boolean e1() {
        if (R()) {
            b1 j8 = this.f11029y.j();
            long F = F(j8.k());
            long y8 = j8 == this.f11029y.p() ? j8.y(this.Z) : j8.y(this.Z) - j8.f9464f.f9481b;
            boolean g8 = this.f11017f.g(y8, F, this.q.c().f11268a);
            if (g8 || F >= 500000) {
                return g8;
            }
            if (this.f11024n > 0 || this.f11025p) {
                this.f11029y.p().f9459a.u(this.E.f11257r, false);
                return this.f11017f.g(y8, F, this.q.c().f11268a);
            }
            return g8;
        }
        return false;
    }

    private void f0(c cVar) {
        this.F.b(1);
        J(this.f11030z.v(cVar.f11036a, cVar.f11037b, cVar.f11038c, cVar.f11039d), false);
    }

    private boolean f1() {
        w1 w1Var = this.E;
        return w1Var.f11252l && w1Var.f11253m == 0;
    }

    private void g0() {
        z5.r[] rVarArr;
        for (b1 p8 = this.f11029y.p(); p8 != null; p8 = p8.j()) {
            for (z5.r rVar : p8.o().f24605c) {
                if (rVar != null) {
                    rVar.s();
                }
            }
        }
    }

    private boolean g1(boolean z4) {
        if (this.X == 0) {
            return T();
        }
        if (z4) {
            w1 w1Var = this.E;
            if (w1Var.f11247g) {
                long c9 = h1(w1Var.f11241a, this.f11029y.p().f9464f.f9480a) ? this.A.c() : -9223372036854775807L;
                b1 j8 = this.f11029y.j();
                return (j8.q() && j8.f9464f.f9488i) || (j8.f9464f.f9480a.b() && !j8.f9462d) || this.f11017f.f(E(), this.q.c().f11268a, this.L, c9);
            }
            return true;
        }
        return false;
    }

    private void h0(boolean z4) {
        z5.r[] rVarArr;
        for (b1 p8 = this.f11029y.p(); p8 != null; p8 = p8.j()) {
            for (z5.r rVar : p8.o().f24605c) {
                if (rVar != null) {
                    rVar.f(z4);
                }
            }
        }
    }

    private boolean h1(h2 h2Var, k.b bVar) {
        if (bVar.b() || h2Var.u()) {
            return false;
        }
        h2Var.r(h2Var.l(bVar.f20286a, this.f11023m).f9758c, this.f11022l);
        if (this.f11022l.h()) {
            h2.d dVar = this.f11022l;
            return dVar.f9778j && dVar.f9775f != -9223372036854775807L;
        }
        return false;
    }

    private void i0() {
        z5.r[] rVarArr;
        for (b1 p8 = this.f11029y.p(); p8 != null; p8 = p8.j()) {
            for (z5.r rVar : p8.o().f24605c) {
                if (rVar != null) {
                    rVar.t();
                }
            }
        }
    }

    private void i1() {
        c2[] c2VarArr;
        this.L = false;
        this.q.h();
        for (c2 c2Var : this.f11007a) {
            if (S(c2Var)) {
                c2Var.start();
            }
        }
    }

    private void k1(boolean z4, boolean z8) {
        s0(z4 || !this.R, false, true, false);
        this.F.b(z8 ? 1 : 0);
        this.f11017f.i();
        c1(1);
    }

    private void l(b bVar, int i8) {
        this.F.b(1);
        t1 t1Var = this.f11030z;
        if (i8 == -1) {
            i8 = t1Var.q();
        }
        J(t1Var.f(i8, bVar.f11032a, bVar.f11033b), false);
    }

    private void l0() {
        this.F.b(1);
        s0(false, false, false, true);
        this.f11017f.a();
        c1(this.E.f11241a.u() ? 4 : 2);
        this.f11030z.w(this.f11018g.d());
        this.f11019h.e(2);
    }

    private void l1() {
        c2[] c2VarArr;
        this.q.i();
        for (c2 c2Var : this.f11007a) {
            if (S(c2Var)) {
                t(c2Var);
            }
        }
    }

    private void m() {
        D0(true);
    }

    private void m1() {
        b1 j8 = this.f11029y.j();
        boolean z4 = this.O || (j8 != null && j8.f9459a.f());
        w1 w1Var = this.E;
        if (z4 != w1Var.f11247g) {
            this.E = w1Var.a(z4);
        }
    }

    private void n(z1 z1Var) {
        if (z1Var.j()) {
            return;
        }
        try {
            z1Var.g().x(z1Var.i(), z1Var.e());
        } finally {
            z1Var.k(true);
        }
    }

    private void n0() {
        s0(true, false, true, false);
        this.f11017f.d();
        c1(1);
        HandlerThread handlerThread = this.f11020j;
        if (handlerThread != null) {
            handlerThread.quit();
        }
        synchronized (this) {
            this.G = true;
            notifyAll();
        }
    }

    private void n1(h5.w wVar, z5.b0 b0Var) {
        this.f11017f.e(this.f11007a, wVar, b0Var.f24605c);
    }

    private void o(c2 c2Var) {
        if (S(c2Var)) {
            this.q.a(c2Var);
            t(c2Var);
            c2Var.g();
            this.X--;
        }
    }

    private void o0(int i8, int i9, com.google.android.exoplayer2.source.x xVar) {
        this.F.b(1);
        J(this.f11030z.A(i8, i9, xVar), false);
    }

    private void o1() {
        if (this.E.f11241a.u() || !this.f11030z.s()) {
            return;
        }
        a0();
        c0();
        d0();
        b0();
    }

    /* JADX WARN: Removed duplicated region for block: B:109:0x017e  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0181  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x01a5  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x01b2  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x01c1  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x01cb  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x013e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void p() {
        /*
            Method dump skipped, instructions count: 499
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.v0.p():void");
    }

    private void p1() {
        b1 p8 = this.f11029y.p();
        if (p8 == null) {
            return;
        }
        long p9 = p8.f9462d ? p8.f9459a.p() : -9223372036854775807L;
        if (p9 != -9223372036854775807L) {
            u0(p9);
            if (p9 != this.E.f11257r) {
                w1 w1Var = this.E;
                this.E = N(w1Var.f11242b, p9, w1Var.f11243c, p9, true, 5);
            }
        } else {
            long j8 = this.q.j(p8 != this.f11029y.q());
            this.Z = j8;
            long y8 = p8.y(j8);
            Z(this.E.f11257r, y8);
            this.E.f11257r = y8;
        }
        this.E.f11256p = this.f11029y.j().i();
        this.E.q = E();
        w1 w1Var2 = this.E;
        if (w1Var2.f11252l && w1Var2.f11245e == 3 && h1(w1Var2.f11241a, w1Var2.f11242b) && this.E.f11254n.f11268a == 1.0f) {
            float b9 = this.A.b(y(), E());
            if (this.q.c().f11268a != b9) {
                N0(this.E.f11254n.d(b9));
                L(this.E.f11254n, this.q.c().f11268a, false, false);
            }
        }
    }

    private void q(int i8, boolean z4) {
        c2 c2Var = this.f11007a[i8];
        if (S(c2Var)) {
            return;
        }
        b1 q = this.f11029y.q();
        boolean z8 = q == this.f11029y.p();
        z5.b0 o5 = q.o();
        i4.g0 g0Var = o5.f24604b[i8];
        w0[] z9 = z(o5.f24605c[i8]);
        boolean z10 = f1() && this.E.f11245e == 3;
        boolean z11 = !z4 && z10;
        this.X++;
        this.f11009b.add(c2Var);
        c2Var.k(g0Var, z9, q.f9461c[i8], this.Z, z11, z8, q.m(), q.l());
        c2Var.x(11, new a());
        this.q.b(c2Var);
        if (z10) {
            c2Var.start();
        }
    }

    private boolean q0() {
        b1 q = this.f11029y.q();
        z5.b0 o5 = q.o();
        int i8 = 0;
        boolean z4 = false;
        while (true) {
            c2[] c2VarArr = this.f11007a;
            if (i8 >= c2VarArr.length) {
                return !z4;
            }
            c2 c2Var = c2VarArr[i8];
            if (S(c2Var)) {
                boolean z8 = c2Var.y() != q.f9461c[i8];
                if (!o5.c(i8) || z8) {
                    if (!c2Var.D()) {
                        c2Var.z(z(o5.f24605c[i8]), q.f9461c[i8], q.m(), q.l());
                    } else if (c2Var.b()) {
                        o(c2Var);
                    } else {
                        z4 = true;
                    }
                }
            }
            i8++;
        }
    }

    private void q1(h2 h2Var, k.b bVar, h2 h2Var2, k.b bVar2, long j8, boolean z4) {
        if (!h1(h2Var, bVar)) {
            x1 x1Var = bVar.b() ? x1.f11264d : this.E.f11254n;
            if (this.q.c().equals(x1Var)) {
                return;
            }
            N0(x1Var);
            L(this.E.f11254n, x1Var.f11268a, false, false);
            return;
        }
        h2Var.r(h2Var.l(bVar.f20286a, this.f11023m).f9758c, this.f11022l);
        this.A.a((z0.g) b6.l0.j(this.f11022l.f9780l));
        if (j8 != -9223372036854775807L) {
            this.A.e(A(h2Var, bVar.f20286a, j8));
            return;
        }
        if (!b6.l0.c(h2Var2.u() ? null : h2Var2.r(h2Var2.l(bVar2.f20286a, this.f11023m).f9758c, this.f11022l).f9770a, this.f11022l.f9770a) || z4) {
            this.A.e(-9223372036854775807L);
        }
    }

    private void r() {
        s(new boolean[this.f11007a.length]);
    }

    private void r0() {
        float f5 = this.q.c().f11268a;
        b1 q = this.f11029y.q();
        boolean z4 = true;
        for (b1 p8 = this.f11029y.p(); p8 != null && p8.f9462d; p8 = p8.j()) {
            z5.b0 v8 = p8.v(f5, this.E.f11241a);
            if (!v8.a(p8.o())) {
                e1 e1Var = this.f11029y;
                if (z4) {
                    b1 p9 = e1Var.p();
                    boolean z8 = this.f11029y.z(p9);
                    boolean[] zArr = new boolean[this.f11007a.length];
                    long b9 = p9.b(v8, this.E.f11257r, z8, zArr);
                    w1 w1Var = this.E;
                    boolean z9 = (w1Var.f11245e == 4 || b9 == w1Var.f11257r) ? false : true;
                    w1 w1Var2 = this.E;
                    this.E = N(w1Var2.f11242b, b9, w1Var2.f11243c, w1Var2.f11244d, z9, 5);
                    if (z9) {
                        u0(b9);
                    }
                    boolean[] zArr2 = new boolean[this.f11007a.length];
                    int i8 = 0;
                    while (true) {
                        c2[] c2VarArr = this.f11007a;
                        if (i8 >= c2VarArr.length) {
                            break;
                        }
                        c2 c2Var = c2VarArr[i8];
                        zArr2[i8] = S(c2Var);
                        h5.r rVar = p9.f9461c[i8];
                        if (zArr2[i8]) {
                            if (rVar != c2Var.y()) {
                                o(c2Var);
                            } else if (zArr[i8]) {
                                c2Var.C(this.Z);
                            }
                        }
                        i8++;
                    }
                    s(zArr2);
                } else {
                    e1Var.z(p8);
                    if (p8.f9462d) {
                        p8.a(v8, Math.max(p8.f9464f.f9481b, p8.y(this.Z)), false);
                    }
                }
                I(true);
                if (this.E.f11245e != 4) {
                    X();
                    p1();
                    this.f11019h.e(2);
                    return;
                }
                return;
            }
            if (p8 == q) {
                z4 = false;
            }
        }
    }

    private void r1(float f5) {
        z5.r[] rVarArr;
        for (b1 p8 = this.f11029y.p(); p8 != null; p8 = p8.j()) {
            for (z5.r rVar : p8.o().f24605c) {
                if (rVar != null) {
                    rVar.q(f5);
                }
            }
        }
    }

    private void s(boolean[] zArr) {
        b1 q = this.f11029y.q();
        z5.b0 o5 = q.o();
        for (int i8 = 0; i8 < this.f11007a.length; i8++) {
            if (!o5.c(i8) && this.f11009b.remove(this.f11007a[i8])) {
                this.f11007a[i8].reset();
            }
        }
        for (int i9 = 0; i9 < this.f11007a.length; i9++) {
            if (o5.c(i9)) {
                q(i9, zArr[i9]);
            }
        }
        q.f9465g = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00ca  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00d4  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:66:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void s0(boolean r29, boolean r30, boolean r31, boolean r32) {
        /*
            Method dump skipped, instructions count: 254
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.v0.s0(boolean, boolean, boolean, boolean):void");
    }

    private synchronized void s1(com.google.common.base.r<Boolean> rVar, long j8) {
        long b9 = this.f11027w.b() + j8;
        boolean z4 = false;
        while (!rVar.get().booleanValue() && j8 > 0) {
            try {
                this.f11027w.e();
                wait(j8);
            } catch (InterruptedException unused) {
                z4 = true;
            }
            j8 = b9 - this.f11027w.b();
        }
        if (z4) {
            Thread.currentThread().interrupt();
        }
    }

    private void t(c2 c2Var) {
        if (c2Var.getState() == 2) {
            c2Var.stop();
        }
    }

    private void t0() {
        b1 p8 = this.f11029y.p();
        this.K = p8 != null && p8.f9464f.f9487h && this.H;
    }

    private void u0(long j8) {
        c2[] c2VarArr;
        b1 p8 = this.f11029y.p();
        long z4 = p8 == null ? j8 + 1000000000000L : p8.z(j8);
        this.Z = z4;
        this.q.e(z4);
        for (c2 c2Var : this.f11007a) {
            if (S(c2Var)) {
                c2Var.C(this.Z);
            }
        }
        g0();
    }

    private static void v0(h2 h2Var, d dVar, h2.d dVar2, h2.b bVar) {
        int i8 = h2Var.r(h2Var.l(dVar.f11043d, bVar).f9758c, dVar2).f9784t;
        Object obj = h2Var.k(i8, bVar, true).f9757b;
        long j8 = bVar.f9759d;
        dVar.f(i8, j8 != -9223372036854775807L ? j8 - 1 : Long.MAX_VALUE, obj);
    }

    private static boolean w0(d dVar, h2 h2Var, h2 h2Var2, int i8, boolean z4, h2.d dVar2, h2.b bVar) {
        Object obj = dVar.f11043d;
        if (obj == null) {
            Pair<Object, Long> z02 = z0(h2Var, new h(dVar.f11040a.h(), dVar.f11040a.d(), dVar.f11040a.f() == Long.MIN_VALUE ? -9223372036854775807L : b6.l0.C0(dVar.f11040a.f())), false, i8, z4, dVar2, bVar);
            if (z02 == null) {
                return false;
            }
            dVar.f(h2Var.f(z02.first), ((Long) z02.second).longValue(), z02.first);
            if (dVar.f11040a.f() == Long.MIN_VALUE) {
                v0(h2Var, dVar, dVar2, bVar);
            }
            return true;
        }
        int f5 = h2Var.f(obj);
        if (f5 == -1) {
            return false;
        }
        if (dVar.f11040a.f() == Long.MIN_VALUE) {
            v0(h2Var, dVar, dVar2, bVar);
            return true;
        }
        dVar.f11041b = f5;
        h2Var2.l(dVar.f11043d, bVar);
        if (bVar.f9761f && h2Var2.r(bVar.f9758c, dVar2).q == h2Var2.f(dVar.f11043d)) {
            Pair<Object, Long> n8 = h2Var.n(dVar2, bVar, h2Var.l(dVar.f11043d, bVar).f9758c, dVar.f11042c + bVar.q());
            dVar.f(h2Var.f(n8.first), ((Long) n8.second).longValue(), n8.first);
        }
        return true;
    }

    private ImmutableList<Metadata> x(z5.r[] rVarArr) {
        ImmutableList.a aVar = new ImmutableList.a();
        boolean z4 = false;
        for (z5.r rVar : rVarArr) {
            if (rVar != null) {
                Metadata metadata = rVar.h(0).f11205k;
                if (metadata == null) {
                    aVar.a(new Metadata(new Metadata.Entry[0]));
                } else {
                    aVar.a(metadata);
                    z4 = true;
                }
            }
        }
        return z4 ? aVar.k() : ImmutableList.E();
    }

    private void x0(h2 h2Var, h2 h2Var2) {
        if (h2Var.u() && h2Var2.u()) {
            return;
        }
        for (int size = this.f11026t.size() - 1; size >= 0; size--) {
            if (!w0(this.f11026t.get(size), h2Var, h2Var2, this.P, this.Q, this.f11022l, this.f11023m)) {
                this.f11026t.get(size).f11040a.k(false);
                this.f11026t.remove(size);
            }
        }
        Collections.sort(this.f11026t);
    }

    private long y() {
        w1 w1Var = this.E;
        return A(w1Var.f11241a, w1Var.f11242b.f20286a, w1Var.f11257r);
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x0157  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0175  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01c1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static com.google.android.exoplayer2.v0.g y0(com.google.android.exoplayer2.h2 r30, com.google.android.exoplayer2.w1 r31, com.google.android.exoplayer2.v0.h r32, com.google.android.exoplayer2.e1 r33, int r34, boolean r35, com.google.android.exoplayer2.h2.d r36, com.google.android.exoplayer2.h2.b r37) {
        /*
            Method dump skipped, instructions count: 492
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.v0.y0(com.google.android.exoplayer2.h2, com.google.android.exoplayer2.w1, com.google.android.exoplayer2.v0$h, com.google.android.exoplayer2.e1, int, boolean, com.google.android.exoplayer2.h2$d, com.google.android.exoplayer2.h2$b):com.google.android.exoplayer2.v0$g");
    }

    private static w0[] z(z5.r rVar) {
        int length = rVar != null ? rVar.length() : 0;
        w0[] w0VarArr = new w0[length];
        for (int i8 = 0; i8 < length; i8++) {
            w0VarArr[i8] = rVar.h(i8);
        }
        return w0VarArr;
    }

    private static Pair<Object, Long> z0(h2 h2Var, h hVar, boolean z4, int i8, boolean z8, h2.d dVar, h2.b bVar) {
        Pair<Object, Long> n8;
        Object A0;
        h2 h2Var2 = hVar.f11057a;
        if (h2Var.u()) {
            return null;
        }
        h2 h2Var3 = h2Var2.u() ? h2Var : h2Var2;
        try {
            n8 = h2Var3.n(dVar, bVar, hVar.f11058b, hVar.f11059c);
        } catch (IndexOutOfBoundsException unused) {
        }
        if (h2Var.equals(h2Var3)) {
            return n8;
        }
        if (h2Var.f(n8.first) != -1) {
            return (h2Var3.l(n8.first, bVar).f9761f && h2Var3.r(bVar.f9758c, dVar).q == h2Var3.f(n8.first)) ? h2Var.n(dVar, bVar, h2Var.l(n8.first, bVar).f9758c, hVar.f11059c) : n8;
        }
        if (z4 && (A0 = A0(dVar, bVar, i8, z8, n8.first, h2Var3, h2Var)) != null) {
            return h2Var.n(dVar, bVar, h2Var.l(A0, bVar).f9758c, -9223372036854775807L);
        }
        return null;
    }

    public void C0(h2 h2Var, int i8, long j8) {
        this.f11019h.j(3, new h(h2Var, i8, j8)).a();
    }

    public Looper D() {
        return this.f11021k;
    }

    public void P0(List<t1.c> list, int i8, long j8, com.google.android.exoplayer2.source.x xVar) {
        this.f11019h.j(17, new b(list, xVar, i8, j8, null)).a();
    }

    public void S0(boolean z4, int i8) {
        this.f11019h.a(1, z4 ? 1 : 0, i8).a();
    }

    public void U0(x1 x1Var) {
        this.f11019h.j(4, x1Var).a();
    }

    public void W0(int i8) {
        this.f11019h.a(11, i8, 0).a();
    }

    public void Z0(boolean z4) {
        this.f11019h.a(12, z4 ? 1 : 0, 0).a();
    }

    @Override // z5.a0.a
    public void b() {
        this.f11019h.e(10);
    }

    @Override // com.google.android.exoplayer2.z1.a
    public synchronized void c(z1 z1Var) {
        if (!this.G && this.f11021k.getThread().isAlive()) {
            this.f11019h.j(14, z1Var).a();
            return;
        }
        b6.p.i("ExoPlayerImplInternal", "Ignoring messages sent after release.");
        z1Var.k(false);
    }

    @Override // com.google.android.exoplayer2.t1.d
    public void d() {
        this.f11019h.e(22);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        ExoPlaybackException e8;
        int i8;
        IOException iOException;
        int i9;
        b1 q;
        int i10 = 1000;
        try {
            switch (message.what) {
                case 0:
                    l0();
                    break;
                case 1:
                    T0(message.arg1 != 0, message.arg2, true, 1);
                    break;
                case 2:
                    p();
                    break;
                case 3:
                    E0((h) message.obj);
                    break;
                case 4:
                    V0((x1) message.obj);
                    break;
                case 5:
                    Y0((i4.i0) message.obj);
                    break;
                case 6:
                    k1(false, true);
                    break;
                case 7:
                    n0();
                    return true;
                case 8:
                    K((com.google.android.exoplayer2.source.j) message.obj);
                    break;
                case 9:
                    G((com.google.android.exoplayer2.source.j) message.obj);
                    break;
                case 10:
                    r0();
                    break;
                case 11:
                    X0(message.arg1);
                    break;
                case 12:
                    a1(message.arg1 != 0);
                    break;
                case 13:
                    M0(message.arg1 != 0, (AtomicBoolean) message.obj);
                    break;
                case 14:
                    H0((z1) message.obj);
                    break;
                case 15:
                    J0((z1) message.obj);
                    break;
                case 16:
                    M((x1) message.obj, false);
                    break;
                case 17:
                    O0((b) message.obj);
                    break;
                case 18:
                    l((b) message.obj, message.arg1);
                    break;
                case 19:
                    f0((c) message.obj);
                    break;
                case 20:
                    o0(message.arg1, message.arg2, (com.google.android.exoplayer2.source.x) message.obj);
                    break;
                case 21:
                    b1((com.google.android.exoplayer2.source.x) message.obj);
                    break;
                case 22:
                    e0();
                    break;
                case 23:
                    R0(message.arg1 != 0);
                    break;
                case 24:
                    Q0(message.arg1 == 1);
                    break;
                case 25:
                    m();
                    break;
                default:
                    return false;
            }
        } catch (ExoPlaybackException e9) {
            e8 = e9;
            if (e8.f9131j == 1 && (q = this.f11029y.q()) != null) {
                e8 = e8.e(q.f9464f.f9480a);
            }
            if (e8.q && this.f11012c0 == null) {
                b6.p.j("ExoPlayerImplInternal", "Recoverable renderer error", e8);
                this.f11012c0 = e8;
                b6.l lVar = this.f11019h;
                lVar.h(lVar.j(25, e8));
            } else {
                ExoPlaybackException exoPlaybackException = this.f11012c0;
                if (exoPlaybackException != null) {
                    exoPlaybackException.addSuppressed(e8);
                    e8 = this.f11012c0;
                }
                b6.p.d("ExoPlayerImplInternal", "Playback error", e8);
                k1(true, false);
                this.E = this.E.e(e8);
            }
        } catch (ParserException e10) {
            int i11 = e10.f9142b;
            if (i11 == 1) {
                i9 = e10.f9141a ? 3001 : 3003;
            } else {
                if (i11 == 4) {
                    i9 = e10.f9141a ? 3002 : 3004;
                }
                H(e10, i10);
            }
            i10 = i9;
            H(e10, i10);
        } catch (DrmSession.DrmSessionException e11) {
            i8 = e11.f9601a;
            iOException = e11;
            H(iOException, i8);
        } catch (BehindLiveWindowException e12) {
            i8 = 1002;
            iOException = e12;
            H(iOException, i8);
        } catch (DataSourceException e13) {
            i8 = e13.f10894a;
            iOException = e13;
            H(iOException, i8);
        } catch (IOException e14) {
            i8 = 2000;
            iOException = e14;
            H(iOException, i8);
        } catch (RuntimeException e15) {
            e8 = ExoPlaybackException.i(e15, ((e15 instanceof IllegalStateException) || (e15 instanceof IllegalArgumentException)) ? 1004 : 1004);
            b6.p.d("ExoPlayerImplInternal", "Playback error", e8);
            k1(true, false);
            this.E = this.E.e(e8);
        }
        Y();
        return true;
    }

    @Override // com.google.android.exoplayer2.source.w.a
    /* renamed from: j0 */
    public void e(com.google.android.exoplayer2.source.j jVar) {
        this.f11019h.j(9, jVar).a();
    }

    public void j1() {
        this.f11019h.c(6).a();
    }

    @Override // com.google.android.exoplayer2.source.j.a
    public void k(com.google.android.exoplayer2.source.j jVar) {
        this.f11019h.j(8, jVar).a();
    }

    public void k0() {
        this.f11019h.c(0).a();
    }

    public synchronized boolean m0() {
        if (!this.G && this.f11021k.getThread().isAlive()) {
            this.f11019h.e(7);
            s1(new com.google.common.base.r() { // from class: com.google.android.exoplayer2.t0
                @Override // com.google.common.base.r
                public final Object get() {
                    Boolean V;
                    V = v0.this.V();
                    return V;
                }
            }, this.B);
            return this.G;
        }
        return true;
    }

    public void p0(int i8, int i9, com.google.android.exoplayer2.source.x xVar) {
        this.f11019h.f(20, i8, i9, xVar).a();
    }

    public void u(long j8) {
        this.f11014d0 = j8;
    }

    public void v(boolean z4) {
        this.f11019h.a(24, z4 ? 1 : 0, 0).a();
    }

    @Override // com.google.android.exoplayer2.i.a
    public void w(x1 x1Var) {
        this.f11019h.j(16, x1Var).a();
    }
}
