package com.google.android.exoplayer2.source.smoothstreaming;

import a6.t;
import a6.y;
import com.google.android.exoplayer2.drm.h;
import com.google.android.exoplayer2.source.j;
import com.google.android.exoplayer2.source.l;
import com.google.android.exoplayer2.source.smoothstreaming.b;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.a;
import com.google.android.exoplayer2.source.w;
import com.google.android.exoplayer2.w0;
import h5.d;
import h5.u;
import i4.i0;
import j5.i;
import java.util.ArrayList;
import z5.r;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c implements j, w.a<i<b>> {

    /* renamed from: a  reason: collision with root package name */
    private final b.a f10719a;

    /* renamed from: b  reason: collision with root package name */
    private final y f10720b;

    /* renamed from: c  reason: collision with root package name */
    private final t f10721c;

    /* renamed from: d  reason: collision with root package name */
    private final com.google.android.exoplayer2.drm.i f10722d;

    /* renamed from: e  reason: collision with root package name */
    private final h.a f10723e;

    /* renamed from: f  reason: collision with root package name */
    private final com.google.android.exoplayer2.upstream.c f10724f;

    /* renamed from: g  reason: collision with root package name */
    private final l.a f10725g;

    /* renamed from: h  reason: collision with root package name */
    private final a6.b f10726h;

    /* renamed from: j  reason: collision with root package name */
    private final h5.w f10727j;

    /* renamed from: k  reason: collision with root package name */
    private final d f10728k;

    /* renamed from: l  reason: collision with root package name */
    private j.a f10729l;

    /* renamed from: m  reason: collision with root package name */
    private com.google.android.exoplayer2.source.smoothstreaming.manifest.a f10730m;

    /* renamed from: n  reason: collision with root package name */
    private i<b>[] f10731n;

    /* renamed from: p  reason: collision with root package name */
    private w f10732p;

    public c(com.google.android.exoplayer2.source.smoothstreaming.manifest.a aVar, b.a aVar2, y yVar, d dVar, com.google.android.exoplayer2.drm.i iVar, h.a aVar3, com.google.android.exoplayer2.upstream.c cVar, l.a aVar4, t tVar, a6.b bVar) {
        this.f10730m = aVar;
        this.f10719a = aVar2;
        this.f10720b = yVar;
        this.f10721c = tVar;
        this.f10722d = iVar;
        this.f10723e = aVar3;
        this.f10724f = cVar;
        this.f10725g = aVar4;
        this.f10726h = bVar;
        this.f10728k = dVar;
        this.f10727j = m(aVar, iVar);
        i<b>[] o5 = o(0);
        this.f10731n = o5;
        this.f10732p = dVar.a(o5);
    }

    private i<b> j(r rVar, long j8) {
        int c9 = this.f10727j.c(rVar.b());
        return new i<>(this.f10730m.f10769f[c9].f10775a, null, null, this.f10719a.a(this.f10721c, this.f10730m, c9, rVar, this.f10720b), this, this.f10726h, j8, this.f10722d, this.f10723e, this.f10724f, this.f10725g);
    }

    private static h5.w m(com.google.android.exoplayer2.source.smoothstreaming.manifest.a aVar, com.google.android.exoplayer2.drm.i iVar) {
        u[] uVarArr = new u[aVar.f10769f.length];
        int i8 = 0;
        while (true) {
            a.b[] bVarArr = aVar.f10769f;
            if (i8 >= bVarArr.length) {
                return new h5.w(uVarArr);
            }
            w0[] w0VarArr = bVarArr[i8].f10784j;
            w0[] w0VarArr2 = new w0[w0VarArr.length];
            for (int i9 = 0; i9 < w0VarArr.length; i9++) {
                w0 w0Var = w0VarArr[i9];
                w0VarArr2[i9] = w0Var.c(iVar.c(w0Var));
            }
            uVarArr[i8] = new u(Integer.toString(i8), w0VarArr2);
            i8++;
        }
    }

    private static i<b>[] o(int i8) {
        return new i[i8];
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public long b() {
        return this.f10732p.b();
    }

    @Override // com.google.android.exoplayer2.source.j
    public long c(long j8, i0 i0Var) {
        i<b>[] iVarArr;
        for (i<b> iVar : this.f10731n) {
            if (iVar.f20752a == 2) {
                return iVar.c(j8, i0Var);
            }
        }
        return j8;
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public boolean d(long j8) {
        return this.f10732p.d(j8);
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public boolean f() {
        return this.f10732p.f();
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public long g() {
        return this.f10732p.g();
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public void h(long j8) {
        this.f10732p.h(j8);
    }

    @Override // com.google.android.exoplayer2.source.j
    public void l() {
        this.f10721c.a();
    }

    @Override // com.google.android.exoplayer2.source.j
    public long n(long j8) {
        for (i<b> iVar : this.f10731n) {
            iVar.S(j8);
        }
        return j8;
    }

    @Override // com.google.android.exoplayer2.source.j
    public long p() {
        return -9223372036854775807L;
    }

    @Override // com.google.android.exoplayer2.source.j
    public void q(j.a aVar, long j8) {
        this.f10729l = aVar;
        aVar.k(this);
    }

    @Override // com.google.android.exoplayer2.source.j
    public h5.w r() {
        return this.f10727j;
    }

    @Override // com.google.android.exoplayer2.source.j
    public long s(r[] rVarArr, boolean[] zArr, h5.r[] rVarArr2, boolean[] zArr2, long j8) {
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < rVarArr.length; i8++) {
            if (rVarArr2[i8] != null) {
                i iVar = (i) rVarArr2[i8];
                if (rVarArr[i8] == null || !zArr[i8]) {
                    iVar.P();
                    rVarArr2[i8] = null;
                } else {
                    ((b) iVar.E()).b(rVarArr[i8]);
                    arrayList.add(iVar);
                }
            }
            if (rVarArr2[i8] == null && rVarArr[i8] != null) {
                i<b> j9 = j(rVarArr[i8], j8);
                arrayList.add(j9);
                rVarArr2[i8] = j9;
                zArr2[i8] = true;
            }
        }
        i<b>[] o5 = o(arrayList.size());
        this.f10731n = o5;
        arrayList.toArray(o5);
        this.f10732p = this.f10728k.a(this.f10731n);
        return j8;
    }

    @Override // com.google.android.exoplayer2.source.w.a
    /* renamed from: t */
    public void e(i<b> iVar) {
        this.f10729l.e(this);
    }

    @Override // com.google.android.exoplayer2.source.j
    public void u(long j8, boolean z4) {
        for (i<b> iVar : this.f10731n) {
            iVar.u(j8, z4);
        }
    }

    public void v() {
        for (i<b> iVar : this.f10731n) {
            iVar.P();
        }
        this.f10729l = null;
    }

    public void w(com.google.android.exoplayer2.source.smoothstreaming.manifest.a aVar) {
        this.f10730m = aVar;
        for (i<b> iVar : this.f10731n) {
            iVar.E().f(aVar);
        }
        this.f10729l.e(this);
    }
}
