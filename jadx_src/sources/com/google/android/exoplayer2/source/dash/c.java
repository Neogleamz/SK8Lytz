package com.google.android.exoplayer2.source.dash;

import a6.h;
import a6.t;
import a6.y;
import android.os.SystemClock;
import b6.l0;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.dash.a;
import com.google.android.exoplayer2.source.dash.e;
import com.google.android.exoplayer2.upstream.HttpDataSource$InvalidResponseCodeException;
import com.google.android.exoplayer2.upstream.c;
import com.google.android.exoplayer2.w0;
import i4.i0;
import j4.t1;
import j5.f;
import j5.g;
import j5.k;
import j5.m;
import j5.n;
import j5.o;
import j5.p;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import l5.i;
import l5.j;
import z5.r;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c implements com.google.android.exoplayer2.source.dash.a {

    /* renamed from: a  reason: collision with root package name */
    private final t f10371a;

    /* renamed from: b  reason: collision with root package name */
    private final k5.b f10372b;

    /* renamed from: c  reason: collision with root package name */
    private final int[] f10373c;

    /* renamed from: d  reason: collision with root package name */
    private final int f10374d;

    /* renamed from: e  reason: collision with root package name */
    private final h f10375e;

    /* renamed from: f  reason: collision with root package name */
    private final long f10376f;

    /* renamed from: g  reason: collision with root package name */
    private final int f10377g;

    /* renamed from: h  reason: collision with root package name */
    private final e.c f10378h;

    /* renamed from: i  reason: collision with root package name */
    protected final b[] f10379i;

    /* renamed from: j  reason: collision with root package name */
    private r f10380j;

    /* renamed from: k  reason: collision with root package name */
    private l5.c f10381k;

    /* renamed from: l  reason: collision with root package name */
    private int f10382l;

    /* renamed from: m  reason: collision with root package name */
    private IOException f10383m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f10384n;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a implements a.InterfaceC0109a {

        /* renamed from: a  reason: collision with root package name */
        private final h.a f10385a;

        /* renamed from: b  reason: collision with root package name */
        private final int f10386b;

        /* renamed from: c  reason: collision with root package name */
        private final g.a f10387c;

        public a(h.a aVar) {
            this(aVar, 1);
        }

        public a(h.a aVar, int i8) {
            this(j5.e.f20723k, aVar, i8);
        }

        public a(g.a aVar, h.a aVar2, int i8) {
            this.f10387c = aVar;
            this.f10385a = aVar2;
            this.f10386b = i8;
        }

        @Override // com.google.android.exoplayer2.source.dash.a.InterfaceC0109a
        public com.google.android.exoplayer2.source.dash.a a(t tVar, l5.c cVar, k5.b bVar, int i8, int[] iArr, r rVar, int i9, long j8, boolean z4, List<w0> list, e.c cVar2, y yVar, t1 t1Var) {
            h a9 = this.f10385a.a();
            if (yVar != null) {
                a9.w(yVar);
            }
            return new c(this.f10387c, tVar, cVar, bVar, i8, iArr, rVar, i9, a9, j8, this.f10386b, z4, list, cVar2, t1Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        final g f10388a;

        /* renamed from: b  reason: collision with root package name */
        public final j f10389b;

        /* renamed from: c  reason: collision with root package name */
        public final l5.b f10390c;

        /* renamed from: d  reason: collision with root package name */
        public final k5.e f10391d;

        /* renamed from: e  reason: collision with root package name */
        private final long f10392e;

        /* renamed from: f  reason: collision with root package name */
        private final long f10393f;

        b(long j8, j jVar, l5.b bVar, g gVar, long j9, k5.e eVar) {
            this.f10392e = j8;
            this.f10389b = jVar;
            this.f10390c = bVar;
            this.f10393f = j9;
            this.f10388a = gVar;
            this.f10391d = eVar;
        }

        b b(long j8, j jVar) {
            long f5;
            long f8;
            k5.e l8 = this.f10389b.l();
            k5.e l9 = jVar.l();
            if (l8 == null) {
                return new b(j8, jVar, this.f10390c, this.f10388a, this.f10393f, l8);
            }
            if (l8.g()) {
                long i8 = l8.i(j8);
                if (i8 == 0) {
                    return new b(j8, jVar, this.f10390c, this.f10388a, this.f10393f, l9);
                }
                long h8 = l8.h();
                long a9 = l8.a(h8);
                long j9 = (i8 + h8) - 1;
                long h9 = l9.h();
                long a10 = l9.a(h9);
                long j10 = this.f10393f;
                int i9 = ((l8.a(j9) + l8.b(j9, j8)) > a10 ? 1 : ((l8.a(j9) + l8.b(j9, j8)) == a10 ? 0 : -1));
                if (i9 == 0) {
                    f5 = j9 + 1;
                } else if (i9 < 0) {
                    throw new BehindLiveWindowException();
                } else {
                    if (a10 < a9) {
                        f8 = j10 - (l9.f(a9, j8) - h8);
                        return new b(j8, jVar, this.f10390c, this.f10388a, f8, l9);
                    }
                    f5 = l8.f(a10, j8);
                }
                f8 = j10 + (f5 - h9);
                return new b(j8, jVar, this.f10390c, this.f10388a, f8, l9);
            }
            return new b(j8, jVar, this.f10390c, this.f10388a, this.f10393f, l9);
        }

        b c(k5.e eVar) {
            return new b(this.f10392e, this.f10389b, this.f10390c, this.f10388a, this.f10393f, eVar);
        }

        b d(l5.b bVar) {
            return new b(this.f10392e, this.f10389b, bVar, this.f10388a, this.f10393f, this.f10391d);
        }

        public long e(long j8) {
            return this.f10391d.c(this.f10392e, j8) + this.f10393f;
        }

        public long f() {
            return this.f10391d.h() + this.f10393f;
        }

        public long g(long j8) {
            return (e(j8) + this.f10391d.j(this.f10392e, j8)) - 1;
        }

        public long h() {
            return this.f10391d.i(this.f10392e);
        }

        public long i(long j8) {
            return k(j8) + this.f10391d.b(j8 - this.f10393f, this.f10392e);
        }

        public long j(long j8) {
            return this.f10391d.f(j8, this.f10392e) + this.f10393f;
        }

        public long k(long j8) {
            return this.f10391d.a(j8 - this.f10393f);
        }

        public i l(long j8) {
            return this.f10391d.e(j8 - this.f10393f);
        }

        public boolean m(long j8, long j9) {
            return this.f10391d.g() || j9 == -9223372036854775807L || i(j8) <= j9;
        }
    }

    /* renamed from: com.google.android.exoplayer2.source.dash.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    protected static final class C0110c extends j5.b {

        /* renamed from: e  reason: collision with root package name */
        private final b f10394e;

        /* renamed from: f  reason: collision with root package name */
        private final long f10395f;

        public C0110c(b bVar, long j8, long j9, long j10) {
            super(j8, j9);
            this.f10394e = bVar;
            this.f10395f = j10;
        }

        @Override // j5.o
        public long a() {
            c();
            return this.f10394e.k(d());
        }

        @Override // j5.o
        public long b() {
            c();
            return this.f10394e.i(d());
        }
    }

    public c(g.a aVar, t tVar, l5.c cVar, k5.b bVar, int i8, int[] iArr, r rVar, int i9, h hVar, long j8, int i10, boolean z4, List<w0> list, e.c cVar2, t1 t1Var) {
        this.f10371a = tVar;
        this.f10381k = cVar;
        this.f10372b = bVar;
        this.f10373c = iArr;
        this.f10380j = rVar;
        this.f10374d = i9;
        this.f10375e = hVar;
        this.f10382l = i8;
        this.f10376f = j8;
        this.f10377g = i10;
        this.f10378h = cVar2;
        long g8 = cVar.g(i8);
        ArrayList<j> n8 = n();
        this.f10379i = new b[rVar.length()];
        int i11 = 0;
        while (i11 < this.f10379i.length) {
            j jVar = n8.get(rVar.j(i11));
            l5.b j9 = bVar.j(jVar.f21685c);
            b[] bVarArr = this.f10379i;
            if (j9 == null) {
                j9 = jVar.f21685c.get(0);
            }
            int i12 = i11;
            bVarArr[i12] = new b(g8, jVar, j9, aVar.a(i9, jVar.f21684b, z4, list, cVar2, t1Var), 0L, jVar.l());
            i11 = i12 + 1;
        }
    }

    private c.a k(r rVar, List<l5.b> list) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        int length = rVar.length();
        int i8 = 0;
        for (int i9 = 0; i9 < length; i9++) {
            if (rVar.e(i9, elapsedRealtime)) {
                i8++;
            }
        }
        int f5 = k5.b.f(list);
        return new c.a(f5, f5 - this.f10372b.g(list), length, i8);
    }

    private long l(long j8, long j9) {
        if (!this.f10381k.f21637d || this.f10379i[0].h() == 0) {
            return -9223372036854775807L;
        }
        return Math.max(0L, Math.min(m(j8), this.f10379i[0].i(this.f10379i[0].g(j8))) - j9);
    }

    private long m(long j8) {
        l5.c cVar = this.f10381k;
        long j9 = cVar.f21634a;
        if (j9 == -9223372036854775807L) {
            return -9223372036854775807L;
        }
        return j8 - l0.C0(j9 + cVar.d(this.f10382l).f21670b);
    }

    private ArrayList<j> n() {
        List<l5.a> list = this.f10381k.d(this.f10382l).f21671c;
        ArrayList<j> arrayList = new ArrayList<>();
        for (int i8 : this.f10373c) {
            arrayList.addAll(list.get(i8).f21626c);
        }
        return arrayList;
    }

    private long o(b bVar, n nVar, long j8, long j9, long j10) {
        return nVar != null ? nVar.g() : l0.r(bVar.j(j8), j9, j10);
    }

    private b r(int i8) {
        b bVar = this.f10379i[i8];
        l5.b j8 = this.f10372b.j(bVar.f10389b.f21685c);
        if (j8 == null || j8.equals(bVar.f10390c)) {
            return bVar;
        }
        b d8 = bVar.d(j8);
        this.f10379i[i8] = d8;
        return d8;
    }

    @Override // j5.j
    public void a() {
        IOException iOException = this.f10383m;
        if (iOException != null) {
            throw iOException;
        }
        this.f10371a.a();
    }

    @Override // com.google.android.exoplayer2.source.dash.a
    public void b(r rVar) {
        this.f10380j = rVar;
    }

    @Override // j5.j
    public long c(long j8, i0 i0Var) {
        b[] bVarArr;
        for (b bVar : this.f10379i) {
            if (bVar.f10391d != null) {
                long h8 = bVar.h();
                if (h8 != 0) {
                    long j9 = bVar.j(j8);
                    long k8 = bVar.k(j9);
                    return i0Var.a(j8, k8, (k8 >= j8 || (h8 != -1 && j9 >= (bVar.f() + h8) - 1)) ? k8 : bVar.k(j9 + 1));
                }
            }
        }
        return j8;
    }

    @Override // j5.j
    public boolean d(long j8, f fVar, List<? extends n> list) {
        if (this.f10383m != null) {
            return false;
        }
        return this.f10380j.p(j8, fVar, list);
    }

    @Override // j5.j
    public boolean e(f fVar, boolean z4, c.C0117c c0117c, com.google.android.exoplayer2.upstream.c cVar) {
        c.b b9;
        if (z4) {
            e.c cVar2 = this.f10378h;
            if (cVar2 == null || !cVar2.j(fVar)) {
                if (!this.f10381k.f21637d && (fVar instanceof n)) {
                    IOException iOException = c0117c.f10972c;
                    if ((iOException instanceof HttpDataSource$InvalidResponseCodeException) && ((HttpDataSource$InvalidResponseCodeException) iOException).f10902d == 404) {
                        b bVar = this.f10379i[this.f10380j.l(fVar.f20744d)];
                        long h8 = bVar.h();
                        if (h8 != -1 && h8 != 0) {
                            if (((n) fVar).g() > (bVar.f() + h8) - 1) {
                                this.f10384n = true;
                                return true;
                            }
                        }
                    }
                }
                b bVar2 = this.f10379i[this.f10380j.l(fVar.f20744d)];
                l5.b j8 = this.f10372b.j(bVar2.f10389b.f21685c);
                if (j8 == null || bVar2.f10390c.equals(j8)) {
                    c.a k8 = k(this.f10380j, bVar2.f10389b.f21685c);
                    if ((k8.a(2) || k8.a(1)) && (b9 = cVar.b(k8, c0117c)) != null && k8.a(b9.f10968a)) {
                        int i8 = b9.f10968a;
                        if (i8 == 2) {
                            r rVar = this.f10380j;
                            return rVar.d(rVar.l(fVar.f20744d), b9.f10969b);
                        } else if (i8 == 1) {
                            this.f10372b.e(bVar2.f10390c, b9.f10969b);
                            return true;
                        } else {
                            return false;
                        }
                    }
                    return false;
                }
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // j5.j
    public void g(f fVar) {
        n4.c d8;
        if (fVar instanceof m) {
            int l8 = this.f10380j.l(((m) fVar).f20744d);
            b bVar = this.f10379i[l8];
            if (bVar.f10391d == null && (d8 = bVar.f10388a.d()) != null) {
                this.f10379i[l8] = bVar.c(new k5.g(d8, bVar.f10389b.f21686d));
            }
        }
        e.c cVar = this.f10378h;
        if (cVar != null) {
            cVar.i(fVar);
        }
    }

    @Override // com.google.android.exoplayer2.source.dash.a
    public void h(l5.c cVar, int i8) {
        try {
            this.f10381k = cVar;
            this.f10382l = i8;
            long g8 = cVar.g(i8);
            ArrayList<j> n8 = n();
            for (int i9 = 0; i9 < this.f10379i.length; i9++) {
                b[] bVarArr = this.f10379i;
                bVarArr[i9] = bVarArr[i9].b(g8, n8.get(this.f10380j.j(i9)));
            }
        } catch (BehindLiveWindowException e8) {
            this.f10383m = e8;
        }
    }

    @Override // j5.j
    public int i(long j8, List<? extends n> list) {
        return (this.f10383m != null || this.f10380j.length() < 2) ? list.size() : this.f10380j.k(j8, list);
    }

    @Override // j5.j
    public void j(long j8, long j9, List<? extends n> list, j5.h hVar) {
        int i8;
        int i9;
        o[] oVarArr;
        long j10;
        long j11;
        if (this.f10383m != null) {
            return;
        }
        long j12 = j9 - j8;
        long C0 = l0.C0(this.f10381k.f21634a) + l0.C0(this.f10381k.d(this.f10382l).f21670b) + j9;
        e.c cVar = this.f10378h;
        if (cVar == null || !cVar.h(C0)) {
            long C02 = l0.C0(l0.a0(this.f10376f));
            long m8 = m(C02);
            n nVar = list.isEmpty() ? null : list.get(list.size() - 1);
            int length = this.f10380j.length();
            o[] oVarArr2 = new o[length];
            int i10 = 0;
            while (i10 < length) {
                b bVar = this.f10379i[i10];
                if (bVar.f10391d == null) {
                    oVarArr2[i10] = o.f20788a;
                    i8 = i10;
                    i9 = length;
                    oVarArr = oVarArr2;
                    j10 = j12;
                    j11 = C02;
                } else {
                    long e8 = bVar.e(C02);
                    long g8 = bVar.g(C02);
                    i8 = i10;
                    i9 = length;
                    oVarArr = oVarArr2;
                    j10 = j12;
                    j11 = C02;
                    long o5 = o(bVar, nVar, j9, e8, g8);
                    if (o5 < e8) {
                        oVarArr[i8] = o.f20788a;
                    } else {
                        oVarArr[i8] = new C0110c(r(i8), o5, g8, m8);
                    }
                }
                i10 = i8 + 1;
                C02 = j11;
                oVarArr2 = oVarArr;
                length = i9;
                j12 = j10;
            }
            long j13 = j12;
            long j14 = C02;
            this.f10380j.a(j8, j13, l(j14, j8), list, oVarArr2);
            b r4 = r(this.f10380j.c());
            g gVar = r4.f10388a;
            if (gVar != null) {
                j jVar = r4.f10389b;
                i n8 = gVar.c() == null ? jVar.n() : null;
                i m9 = r4.f10391d == null ? jVar.m() : null;
                if (n8 != null || m9 != null) {
                    hVar.f20750a = p(r4, this.f10375e, this.f10380j.n(), this.f10380j.o(), this.f10380j.r(), n8, m9);
                    return;
                }
            }
            long j15 = r4.f10392e;
            int i11 = (j15 > (-9223372036854775807L) ? 1 : (j15 == (-9223372036854775807L) ? 0 : -1));
            boolean z4 = i11 != 0;
            if (r4.h() == 0) {
                hVar.f20751b = z4;
                return;
            }
            long e9 = r4.e(j14);
            long g9 = r4.g(j14);
            long o8 = o(r4, nVar, j9, e9, g9);
            if (o8 < e9) {
                this.f10383m = new BehindLiveWindowException();
                return;
            }
            int i12 = (o8 > g9 ? 1 : (o8 == g9 ? 0 : -1));
            if (i12 > 0 || (this.f10384n && i12 >= 0)) {
                hVar.f20751b = z4;
            } else if (z4 && r4.k(o8) >= j15) {
                hVar.f20751b = true;
            } else {
                int min = (int) Math.min(this.f10377g, (g9 - o8) + 1);
                if (i11 != 0) {
                    while (min > 1 && r4.k((min + o8) - 1) >= j15) {
                        min--;
                    }
                }
                hVar.f20750a = q(r4, this.f10375e, this.f10374d, this.f10380j.n(), this.f10380j.o(), this.f10380j.r(), o8, min, list.isEmpty() ? j9 : -9223372036854775807L, m8);
            }
        }
    }

    protected f p(b bVar, h hVar, w0 w0Var, int i8, Object obj, i iVar, i iVar2) {
        i iVar3 = iVar;
        j jVar = bVar.f10389b;
        if (iVar3 != null) {
            i a9 = iVar3.a(iVar2, bVar.f10390c.f21630a);
            if (a9 != null) {
                iVar3 = a9;
            }
        } else {
            iVar3 = iVar2;
        }
        return new m(hVar, k5.f.a(jVar, bVar.f10390c.f21630a, iVar3, 0), w0Var, i8, obj, bVar.f10388a);
    }

    protected f q(b bVar, h hVar, int i8, w0 w0Var, int i9, Object obj, long j8, int i10, long j9, long j10) {
        j jVar = bVar.f10389b;
        long k8 = bVar.k(j8);
        i l8 = bVar.l(j8);
        if (bVar.f10388a == null) {
            return new p(hVar, k5.f.a(jVar, bVar.f10390c.f21630a, l8, bVar.m(j8, j10) ? 0 : 8), w0Var, i9, obj, k8, bVar.i(j8), j8, i8, w0Var);
        }
        int i11 = 1;
        int i12 = 1;
        while (i11 < i10) {
            i a9 = l8.a(bVar.l(i11 + j8), bVar.f10390c.f21630a);
            if (a9 == null) {
                break;
            }
            i12++;
            i11++;
            l8 = a9;
        }
        long j11 = (i12 + j8) - 1;
        long i13 = bVar.i(j11);
        long j12 = bVar.f10392e;
        return new k(hVar, k5.f.a(jVar, bVar.f10390c.f21630a, l8, bVar.m(j11, j10) ? 0 : 8), w0Var, i9, obj, k8, i13, j9, (j12 == -9223372036854775807L || j12 > i13) ? -9223372036854775807L : j12, j8, i12, -jVar.f21686d, bVar.f10388a);
    }

    @Override // j5.j
    public void release() {
        for (b bVar : this.f10379i) {
            g gVar = bVar.f10388a;
            if (gVar != null) {
                gVar.release();
            }
        }
    }
}
