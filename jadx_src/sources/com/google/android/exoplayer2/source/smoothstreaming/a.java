package com.google.android.exoplayer2.source.smoothstreaming;

import a6.h;
import a6.t;
import a6.y;
import android.net.Uri;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.smoothstreaming.b;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.a;
import com.google.android.exoplayer2.upstream.c;
import com.google.android.exoplayer2.w0;
import i4.i0;
import j5.e;
import j5.f;
import j5.g;
import j5.k;
import j5.n;
import java.io.IOException;
import java.util.List;
import v4.o;
import v4.p;
import z5.r;
import z5.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements com.google.android.exoplayer2.source.smoothstreaming.b {

    /* renamed from: a  reason: collision with root package name */
    private final t f10708a;

    /* renamed from: b  reason: collision with root package name */
    private final int f10709b;

    /* renamed from: c  reason: collision with root package name */
    private final g[] f10710c;

    /* renamed from: d  reason: collision with root package name */
    private final h f10711d;

    /* renamed from: e  reason: collision with root package name */
    private r f10712e;

    /* renamed from: f  reason: collision with root package name */
    private com.google.android.exoplayer2.source.smoothstreaming.manifest.a f10713f;

    /* renamed from: g  reason: collision with root package name */
    private int f10714g;

    /* renamed from: h  reason: collision with root package name */
    private IOException f10715h;

    /* renamed from: com.google.android.exoplayer2.source.smoothstreaming.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0114a implements b.a {

        /* renamed from: a  reason: collision with root package name */
        private final h.a f10716a;

        public C0114a(h.a aVar) {
            this.f10716a = aVar;
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.b.a
        public com.google.android.exoplayer2.source.smoothstreaming.b a(t tVar, com.google.android.exoplayer2.source.smoothstreaming.manifest.a aVar, int i8, r rVar, y yVar) {
            h a9 = this.f10716a.a();
            if (yVar != null) {
                a9.w(yVar);
            }
            return new a(tVar, aVar, i8, rVar, a9);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b extends j5.b {

        /* renamed from: e  reason: collision with root package name */
        private final a.b f10717e;

        /* renamed from: f  reason: collision with root package name */
        private final int f10718f;

        public b(a.b bVar, int i8, int i9) {
            super(i9, bVar.f10785k - 1);
            this.f10717e = bVar;
            this.f10718f = i8;
        }

        @Override // j5.o
        public long a() {
            c();
            return this.f10717e.e((int) d());
        }

        @Override // j5.o
        public long b() {
            return a() + this.f10717e.c((int) d());
        }
    }

    public a(t tVar, com.google.android.exoplayer2.source.smoothstreaming.manifest.a aVar, int i8, r rVar, h hVar) {
        this.f10708a = tVar;
        this.f10713f = aVar;
        this.f10709b = i8;
        this.f10712e = rVar;
        this.f10711d = hVar;
        a.b bVar = aVar.f10769f[i8];
        this.f10710c = new g[rVar.length()];
        int i9 = 0;
        while (i9 < this.f10710c.length) {
            int j8 = rVar.j(i9);
            w0 w0Var = bVar.f10784j[j8];
            p[] pVarArr = w0Var.q != null ? ((a.C0115a) b6.a.e(aVar.f10768e)).f10774c : null;
            int i10 = bVar.f10775a;
            int i11 = i9;
            this.f10710c[i11] = new e(new v4.g(3, null, new o(j8, i10, bVar.f10777c, -9223372036854775807L, aVar.f10770g, w0Var, 0, pVarArr, i10 == 2 ? 4 : 0, null, null)), bVar.f10775a, w0Var);
            i9 = i11 + 1;
        }
    }

    private static n k(w0 w0Var, h hVar, Uri uri, int i8, long j8, long j9, long j10, int i9, Object obj, g gVar) {
        return new k(hVar, new com.google.android.exoplayer2.upstream.a(uri), w0Var, i9, obj, j8, j9, j10, -9223372036854775807L, i8, 1, j8, gVar);
    }

    private long l(long j8) {
        com.google.android.exoplayer2.source.smoothstreaming.manifest.a aVar = this.f10713f;
        if (aVar.f10767d) {
            a.b bVar = aVar.f10769f[this.f10709b];
            int i8 = bVar.f10785k - 1;
            return (bVar.e(i8) + bVar.c(i8)) - j8;
        }
        return -9223372036854775807L;
    }

    @Override // j5.j
    public void a() {
        IOException iOException = this.f10715h;
        if (iOException != null) {
            throw iOException;
        }
        this.f10708a.a();
    }

    @Override // com.google.android.exoplayer2.source.smoothstreaming.b
    public void b(r rVar) {
        this.f10712e = rVar;
    }

    @Override // j5.j
    public long c(long j8, i0 i0Var) {
        a.b bVar = this.f10713f.f10769f[this.f10709b];
        int d8 = bVar.d(j8);
        long e8 = bVar.e(d8);
        return i0Var.a(j8, e8, (e8 >= j8 || d8 >= bVar.f10785k + (-1)) ? e8 : bVar.e(d8 + 1));
    }

    @Override // j5.j
    public boolean d(long j8, f fVar, List<? extends n> list) {
        if (this.f10715h != null) {
            return false;
        }
        return this.f10712e.p(j8, fVar, list);
    }

    @Override // j5.j
    public boolean e(f fVar, boolean z4, c.C0117c c0117c, com.google.android.exoplayer2.upstream.c cVar) {
        c.b b9 = cVar.b(z.c(this.f10712e), c0117c);
        if (z4 && b9 != null && b9.f10968a == 2) {
            r rVar = this.f10712e;
            if (rVar.d(rVar.l(fVar.f20744d), b9.f10969b)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.android.exoplayer2.source.smoothstreaming.b
    public void f(com.google.android.exoplayer2.source.smoothstreaming.manifest.a aVar) {
        a.b[] bVarArr = this.f10713f.f10769f;
        int i8 = this.f10709b;
        a.b bVar = bVarArr[i8];
        int i9 = bVar.f10785k;
        a.b bVar2 = aVar.f10769f[i8];
        if (i9 != 0 && bVar2.f10785k != 0) {
            int i10 = i9 - 1;
            long e8 = bVar.e(i10) + bVar.c(i10);
            long e9 = bVar2.e(0);
            if (e8 > e9) {
                this.f10714g += bVar.d(e9);
                this.f10713f = aVar;
            }
        }
        this.f10714g += i9;
        this.f10713f = aVar;
    }

    @Override // j5.j
    public void g(f fVar) {
    }

    @Override // j5.j
    public int i(long j8, List<? extends n> list) {
        return (this.f10715h != null || this.f10712e.length() < 2) ? list.size() : this.f10712e.k(j8, list);
    }

    @Override // j5.j
    public final void j(long j8, long j9, List<? extends n> list, j5.h hVar) {
        int g8;
        long j10 = j9;
        if (this.f10715h != null) {
            return;
        }
        com.google.android.exoplayer2.source.smoothstreaming.manifest.a aVar = this.f10713f;
        a.b bVar = aVar.f10769f[this.f10709b];
        if (bVar.f10785k == 0) {
            hVar.f20751b = !aVar.f10767d;
            return;
        }
        if (list.isEmpty()) {
            g8 = bVar.d(j10);
        } else {
            g8 = (int) (list.get(list.size() - 1).g() - this.f10714g);
            if (g8 < 0) {
                this.f10715h = new BehindLiveWindowException();
                return;
            }
        }
        if (g8 >= bVar.f10785k) {
            hVar.f20751b = !this.f10713f.f10767d;
            return;
        }
        long j11 = j10 - j8;
        long l8 = l(j8);
        int length = this.f10712e.length();
        j5.o[] oVarArr = new j5.o[length];
        for (int i8 = 0; i8 < length; i8++) {
            oVarArr[i8] = new b(bVar, this.f10712e.j(i8), g8);
        }
        this.f10712e.a(j8, j11, l8, list, oVarArr);
        long e8 = bVar.e(g8);
        long c9 = e8 + bVar.c(g8);
        if (!list.isEmpty()) {
            j10 = -9223372036854775807L;
        }
        long j12 = j10;
        int i9 = g8 + this.f10714g;
        int c10 = this.f10712e.c();
        g gVar = this.f10710c[c10];
        hVar.f20750a = k(this.f10712e.n(), this.f10711d, bVar.a(this.f10712e.j(c10), g8), i9, e8, c9, j12, this.f10712e.o(), this.f10712e.r(), gVar);
    }

    @Override // j5.j
    public void release() {
        for (g gVar : this.f10710c) {
            gVar.release();
        }
    }
}
