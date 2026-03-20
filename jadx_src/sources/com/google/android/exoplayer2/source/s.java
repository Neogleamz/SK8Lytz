package com.google.android.exoplayer2.source;

import a6.h;
import android.os.Looper;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.source.m;
import com.google.android.exoplayer2.source.r;
import com.google.android.exoplayer2.z0;
import j4.t1;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s extends com.google.android.exoplayer2.source.a implements r.b {

    /* renamed from: h  reason: collision with root package name */
    private final z0 f10671h;

    /* renamed from: j  reason: collision with root package name */
    private final z0.h f10672j;

    /* renamed from: k  reason: collision with root package name */
    private final h.a f10673k;

    /* renamed from: l  reason: collision with root package name */
    private final m.a f10674l;

    /* renamed from: m  reason: collision with root package name */
    private final com.google.android.exoplayer2.drm.i f10675m;

    /* renamed from: n  reason: collision with root package name */
    private final com.google.android.exoplayer2.upstream.c f10676n;

    /* renamed from: p  reason: collision with root package name */
    private final int f10677p;
    private boolean q;

    /* renamed from: t  reason: collision with root package name */
    private long f10678t;

    /* renamed from: w  reason: collision with root package name */
    private boolean f10679w;

    /* renamed from: x  reason: collision with root package name */
    private boolean f10680x;

    /* renamed from: y  reason: collision with root package name */
    private a6.y f10681y;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends f {
        a(s sVar, h2 h2Var) {
            super(h2Var);
        }

        @Override // com.google.android.exoplayer2.source.f, com.google.android.exoplayer2.h2
        public h2.b k(int i8, h2.b bVar, boolean z4) {
            super.k(i8, bVar, z4);
            bVar.f9761f = true;
            return bVar;
        }

        @Override // com.google.android.exoplayer2.source.f, com.google.android.exoplayer2.h2
        public h2.d s(int i8, h2.d dVar, long j8) {
            super.s(i8, dVar, j8);
            dVar.f9781m = true;
            return dVar;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements k.a {

        /* renamed from: a  reason: collision with root package name */
        private final h.a f10682a;

        /* renamed from: b  reason: collision with root package name */
        private m.a f10683b;

        /* renamed from: c  reason: collision with root package name */
        private m4.k f10684c;

        /* renamed from: d  reason: collision with root package name */
        private com.google.android.exoplayer2.upstream.c f10685d;

        /* renamed from: e  reason: collision with root package name */
        private int f10686e;

        /* renamed from: f  reason: collision with root package name */
        private String f10687f;

        /* renamed from: g  reason: collision with root package name */
        private Object f10688g;

        public b(h.a aVar) {
            this(aVar, new n4.h());
        }

        public b(h.a aVar, m.a aVar2) {
            this(aVar, aVar2, new com.google.android.exoplayer2.drm.g(), new com.google.android.exoplayer2.upstream.b(), 1048576);
        }

        public b(h.a aVar, m.a aVar2, m4.k kVar, com.google.android.exoplayer2.upstream.c cVar, int i8) {
            this.f10682a = aVar;
            this.f10683b = aVar2;
            this.f10684c = kVar;
            this.f10685d = cVar;
            this.f10686e = i8;
        }

        public b(h.a aVar, n4.p pVar) {
            this(aVar, (m.a) new h5.q(pVar));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ m c(n4.p pVar, t1 t1Var) {
            return new h5.a(pVar);
        }

        public s b(z0 z0Var) {
            z0.c b9;
            z0.c e8;
            b6.a.e(z0Var.f11304b);
            z0.h hVar = z0Var.f11304b;
            boolean z4 = true;
            boolean z8 = hVar.f11386i == null && this.f10688g != null;
            if (hVar.f11383f != null || this.f10687f == null) {
                z4 = false;
            }
            if (!z8 || !z4) {
                if (z8) {
                    e8 = z0Var.b().e(this.f10688g);
                    z0Var = e8.a();
                    z0 z0Var2 = z0Var;
                    return new s(z0Var2, this.f10682a, this.f10683b, this.f10684c.a(z0Var2), this.f10685d, this.f10686e, null);
                }
                if (z4) {
                    b9 = z0Var.b();
                }
                z0 z0Var22 = z0Var;
                return new s(z0Var22, this.f10682a, this.f10683b, this.f10684c.a(z0Var22), this.f10685d, this.f10686e, null);
            }
            b9 = z0Var.b().e(this.f10688g);
            e8 = b9.b(this.f10687f);
            z0Var = e8.a();
            z0 z0Var222 = z0Var;
            return new s(z0Var222, this.f10682a, this.f10683b, this.f10684c.a(z0Var222), this.f10685d, this.f10686e, null);
        }
    }

    private s(z0 z0Var, h.a aVar, m.a aVar2, com.google.android.exoplayer2.drm.i iVar, com.google.android.exoplayer2.upstream.c cVar, int i8) {
        this.f10672j = (z0.h) b6.a.e(z0Var.f11304b);
        this.f10671h = z0Var;
        this.f10673k = aVar;
        this.f10674l = aVar2;
        this.f10675m = iVar;
        this.f10676n = cVar;
        this.f10677p = i8;
        this.q = true;
        this.f10678t = -9223372036854775807L;
    }

    /* synthetic */ s(z0 z0Var, h.a aVar, m.a aVar2, com.google.android.exoplayer2.drm.i iVar, com.google.android.exoplayer2.upstream.c cVar, int i8, a aVar3) {
        this(z0Var, aVar, aVar2, iVar, cVar, i8);
    }

    private void F() {
        h2 sVar = new h5.s(this.f10678t, this.f10679w, false, this.f10680x, null, this.f10671h);
        if (this.q) {
            sVar = new a(this, sVar);
        }
        D(sVar);
    }

    @Override // com.google.android.exoplayer2.source.a
    protected void C(a6.y yVar) {
        this.f10681y = yVar;
        this.f10675m.b((Looper) b6.a.e(Looper.myLooper()), A());
        this.f10675m.a();
        F();
    }

    @Override // com.google.android.exoplayer2.source.a
    protected void E() {
        this.f10675m.release();
    }

    @Override // com.google.android.exoplayer2.source.k
    public j b(k.b bVar, a6.b bVar2, long j8) {
        a6.h a9 = this.f10673k.a();
        a6.y yVar = this.f10681y;
        if (yVar != null) {
            a9.w(yVar);
        }
        return new r(this.f10672j.f11378a, a9, this.f10674l.a(A()), this.f10675m, u(bVar), this.f10676n, w(bVar), this, bVar2, this.f10672j.f11383f, this.f10677p);
    }

    @Override // com.google.android.exoplayer2.source.r.b
    public void h(long j8, boolean z4, boolean z8) {
        if (j8 == -9223372036854775807L) {
            j8 = this.f10678t;
        }
        if (!this.q && this.f10678t == j8 && this.f10679w == z4 && this.f10680x == z8) {
            return;
        }
        this.f10678t = j8;
        this.f10679w = z4;
        this.f10680x = z8;
        this.q = false;
        F();
    }

    @Override // com.google.android.exoplayer2.source.k
    public z0 i() {
        return this.f10671h;
    }

    @Override // com.google.android.exoplayer2.source.k
    public void n() {
    }

    @Override // com.google.android.exoplayer2.source.k
    public void p(j jVar) {
        ((r) jVar).f0();
    }
}
