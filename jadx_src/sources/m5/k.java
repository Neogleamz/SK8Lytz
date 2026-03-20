package m5;

import a6.y;
import android.net.Uri;
import android.text.TextUtils;
import b6.l0;
import b6.t;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.h;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.source.hls.playlist.e;
import com.google.android.exoplayer2.source.j;
import com.google.android.exoplayer2.source.l;
import com.google.android.exoplayer2.source.w;
import com.google.android.exoplayer2.upstream.c;
import com.google.android.exoplayer2.w0;
import h5.u;
import i4.i0;
import j4.t1;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import m5.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k implements com.google.android.exoplayer2.source.j, HlsPlaylistTracker.b {
    private int E;
    private w F;

    /* renamed from: a  reason: collision with root package name */
    private final h f21898a;

    /* renamed from: b  reason: collision with root package name */
    private final HlsPlaylistTracker f21899b;

    /* renamed from: c  reason: collision with root package name */
    private final g f21900c;

    /* renamed from: d  reason: collision with root package name */
    private final y f21901d;

    /* renamed from: e  reason: collision with root package name */
    private final com.google.android.exoplayer2.drm.i f21902e;

    /* renamed from: f  reason: collision with root package name */
    private final h.a f21903f;

    /* renamed from: g  reason: collision with root package name */
    private final com.google.android.exoplayer2.upstream.c f21904g;

    /* renamed from: h  reason: collision with root package name */
    private final l.a f21905h;

    /* renamed from: j  reason: collision with root package name */
    private final a6.b f21906j;

    /* renamed from: m  reason: collision with root package name */
    private final h5.d f21909m;

    /* renamed from: n  reason: collision with root package name */
    private final boolean f21910n;

    /* renamed from: p  reason: collision with root package name */
    private final int f21911p;
    private final boolean q;

    /* renamed from: t  reason: collision with root package name */
    private final t1 f21912t;

    /* renamed from: x  reason: collision with root package name */
    private j.a f21914x;

    /* renamed from: y  reason: collision with root package name */
    private int f21915y;

    /* renamed from: z  reason: collision with root package name */
    private h5.w f21916z;

    /* renamed from: w  reason: collision with root package name */
    private final p.b f21913w = new b();

    /* renamed from: k  reason: collision with root package name */
    private final IdentityHashMap<h5.r, Integer> f21907k = new IdentityHashMap<>();

    /* renamed from: l  reason: collision with root package name */
    private final q f21908l = new q();
    private p[] A = new p[0];
    private p[] B = new p[0];
    private int[][] C = new int[0];

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class b implements p.b {
        private b() {
        }

        @Override // m5.p.b
        public void a() {
            p[] pVarArr;
            if (k.i(k.this) > 0) {
                return;
            }
            int i8 = 0;
            for (p pVar : k.this.A) {
                i8 += pVar.r().f20316a;
            }
            u[] uVarArr = new u[i8];
            int i9 = 0;
            for (p pVar2 : k.this.A) {
                int i10 = pVar2.r().f20316a;
                int i11 = 0;
                while (i11 < i10) {
                    uVarArr[i9] = pVar2.r().b(i11);
                    i11++;
                    i9++;
                }
            }
            k.this.f21916z = new h5.w(uVarArr);
            k.this.f21914x.k(k.this);
        }

        @Override // com.google.android.exoplayer2.source.w.a
        /* renamed from: b */
        public void e(p pVar) {
            k.this.f21914x.e(k.this);
        }

        @Override // m5.p.b
        public void i(Uri uri) {
            k.this.f21899b.l(uri);
        }
    }

    public k(h hVar, HlsPlaylistTracker hlsPlaylistTracker, g gVar, y yVar, com.google.android.exoplayer2.drm.i iVar, h.a aVar, com.google.android.exoplayer2.upstream.c cVar, l.a aVar2, a6.b bVar, h5.d dVar, boolean z4, int i8, boolean z8, t1 t1Var) {
        this.f21898a = hVar;
        this.f21899b = hlsPlaylistTracker;
        this.f21900c = gVar;
        this.f21901d = yVar;
        this.f21902e = iVar;
        this.f21903f = aVar;
        this.f21904g = cVar;
        this.f21905h = aVar2;
        this.f21906j = bVar;
        this.f21909m = dVar;
        this.f21910n = z4;
        this.f21911p = i8;
        this.q = z8;
        this.f21912t = t1Var;
        this.F = dVar.a(new w[0]);
    }

    private static w0 A(w0 w0Var) {
        String L = l0.L(w0Var.f11204j, 2);
        return new w0.b().U(w0Var.f11196a).W(w0Var.f11197b).M(w0Var.f11206l).g0(t.g(L)).K(L).Z(w0Var.f11205k).I(w0Var.f11201f).b0(w0Var.f11202g).n0(w0Var.f11211w).S(w0Var.f11212x).R(w0Var.f11213y).i0(w0Var.f11199d).e0(w0Var.f11200e).G();
    }

    static /* synthetic */ int i(k kVar) {
        int i8 = kVar.f21915y - 1;
        kVar.f21915y = i8;
        return i8;
    }

    private void t(long j8, List<e.a> list, List<p> list2, List<int[]> list3, Map<String, DrmInitData> map) {
        ArrayList arrayList = new ArrayList(list.size());
        ArrayList arrayList2 = new ArrayList(list.size());
        ArrayList arrayList3 = new ArrayList(list.size());
        HashSet hashSet = new HashSet();
        for (int i8 = 0; i8 < list.size(); i8++) {
            String str = list.get(i8).f10599d;
            if (hashSet.add(str)) {
                arrayList.clear();
                arrayList2.clear();
                arrayList3.clear();
                boolean z4 = true;
                for (int i9 = 0; i9 < list.size(); i9++) {
                    if (l0.c(str, list.get(i9).f10599d)) {
                        e.a aVar = list.get(i9);
                        arrayList3.add(Integer.valueOf(i9));
                        arrayList.add(aVar.f10596a);
                        arrayList2.add(aVar.f10597b);
                        z4 &= l0.K(aVar.f10597b.f11204j, 1) == 1;
                    }
                }
                String str2 = "audio:" + str;
                p x8 = x(str2, 1, (Uri[]) arrayList.toArray((Uri[]) l0.k(new Uri[0])), (w0[]) arrayList2.toArray(new w0[0]), null, Collections.emptyList(), map, j8);
                list3.add(com.google.common.primitives.g.l(arrayList3));
                list2.add(x8);
                if (this.f21910n && z4) {
                    x8.d0(new u[]{new u(str2, (w0[]) arrayList2.toArray(new w0[0]))}, 0, new int[0]);
                }
            }
        }
    }

    private void v(com.google.android.exoplayer2.source.hls.playlist.e eVar, long j8, List<p> list, List<int[]> list2, Map<String, DrmInitData> map) {
        boolean z4;
        boolean z8;
        int size = eVar.f10587e.size();
        int[] iArr = new int[size];
        int i8 = 0;
        int i9 = 0;
        for (int i10 = 0; i10 < eVar.f10587e.size(); i10++) {
            w0 w0Var = eVar.f10587e.get(i10).f10601b;
            if (w0Var.f11212x > 0 || l0.L(w0Var.f11204j, 2) != null) {
                iArr[i10] = 2;
                i8++;
            } else if (l0.L(w0Var.f11204j, 1) != null) {
                iArr[i10] = 1;
                i9++;
            } else {
                iArr[i10] = -1;
            }
        }
        if (i8 > 0) {
            size = i8;
            z4 = true;
            z8 = false;
        } else if (i9 < size) {
            size -= i9;
            z4 = false;
            z8 = true;
        } else {
            z4 = false;
            z8 = false;
        }
        Uri[] uriArr = new Uri[size];
        w0[] w0VarArr = new w0[size];
        int[] iArr2 = new int[size];
        int i11 = 0;
        for (int i12 = 0; i12 < eVar.f10587e.size(); i12++) {
            if ((!z4 || iArr[i12] == 2) && (!z8 || iArr[i12] != 1)) {
                e.b bVar = eVar.f10587e.get(i12);
                uriArr[i11] = bVar.f10600a;
                w0VarArr[i11] = bVar.f10601b;
                iArr2[i11] = i12;
                i11++;
            }
        }
        String str = w0VarArr[0].f11204j;
        int K = l0.K(str, 2);
        int K2 = l0.K(str, 1);
        boolean z9 = (K2 == 1 || (K2 == 0 && eVar.f10589g.isEmpty())) && K <= 1 && K2 + K > 0;
        p x8 = x("main", (z4 || K2 <= 0) ? 0 : 1, uriArr, w0VarArr, eVar.f10592j, eVar.f10593k, map, j8);
        list.add(x8);
        list2.add(iArr2);
        if (this.f21910n && z9) {
            ArrayList arrayList = new ArrayList();
            if (K > 0) {
                w0[] w0VarArr2 = new w0[size];
                for (int i13 = 0; i13 < size; i13++) {
                    w0VarArr2[i13] = A(w0VarArr[i13]);
                }
                arrayList.add(new u("main", w0VarArr2));
                if (K2 > 0 && (eVar.f10592j != null || eVar.f10589g.isEmpty())) {
                    arrayList.add(new u("main:audio", y(w0VarArr[0], eVar.f10592j, false)));
                }
                List<w0> list3 = eVar.f10593k;
                if (list3 != null) {
                    for (int i14 = 0; i14 < list3.size(); i14++) {
                        arrayList.add(new u("main:cc:" + i14, list3.get(i14)));
                    }
                }
            } else {
                w0[] w0VarArr3 = new w0[size];
                for (int i15 = 0; i15 < size; i15++) {
                    w0VarArr3[i15] = y(w0VarArr[i15], eVar.f10592j, true);
                }
                arrayList.add(new u("main", w0VarArr3));
            }
            u uVar = new u("main:id3", new w0.b().U("ID3").g0("application/id3").G());
            arrayList.add(uVar);
            x8.d0((u[]) arrayList.toArray(new u[0]), 0, arrayList.indexOf(uVar));
        }
    }

    private void w(long j8) {
        com.google.android.exoplayer2.source.hls.playlist.e eVar = (com.google.android.exoplayer2.source.hls.playlist.e) b6.a.e(this.f21899b.f());
        Map<String, DrmInitData> z4 = this.q ? z(eVar.f10595m) : Collections.emptyMap();
        boolean z8 = !eVar.f10587e.isEmpty();
        List<e.a> list = eVar.f10589g;
        List<e.a> list2 = eVar.f10590h;
        this.f21915y = 0;
        ArrayList arrayList = new ArrayList();
        List<int[]> arrayList2 = new ArrayList<>();
        if (z8) {
            v(eVar, j8, arrayList, arrayList2, z4);
        }
        t(j8, list, arrayList, arrayList2, z4);
        this.E = arrayList.size();
        int i8 = 0;
        while (i8 < list2.size()) {
            e.a aVar = list2.get(i8);
            String str = "subtitle:" + i8 + ":" + aVar.f10599d;
            ArrayList arrayList3 = arrayList2;
            int i9 = i8;
            p x8 = x(str, 3, new Uri[]{aVar.f10596a}, new w0[]{aVar.f10597b}, null, Collections.emptyList(), z4, j8);
            arrayList3.add(new int[]{i9});
            arrayList.add(x8);
            x8.d0(new u[]{new u(str, aVar.f10597b)}, 0, new int[0]);
            i8 = i9 + 1;
            arrayList2 = arrayList3;
        }
        this.A = (p[]) arrayList.toArray(new p[0]);
        this.C = (int[][]) arrayList2.toArray(new int[0]);
        this.f21915y = this.A.length;
        for (int i10 = 0; i10 < this.E; i10++) {
            this.A[i10].m0(true);
        }
        for (p pVar : this.A) {
            pVar.B();
        }
        this.B = this.A;
    }

    private p x(String str, int i8, Uri[] uriArr, w0[] w0VarArr, w0 w0Var, List<w0> list, Map<String, DrmInitData> map, long j8) {
        return new p(str, i8, this.f21913w, new f(this.f21898a, this.f21899b, uriArr, w0VarArr, this.f21900c, this.f21901d, this.f21908l, list, this.f21912t), map, this.f21906j, j8, w0Var, this.f21902e, this.f21903f, this.f21904g, this.f21905h, this.f21911p);
    }

    private static w0 y(w0 w0Var, w0 w0Var2, boolean z4) {
        String str;
        int i8;
        int i9;
        String str2;
        String str3;
        Metadata metadata;
        int i10;
        if (w0Var2 != null) {
            str2 = w0Var2.f11204j;
            metadata = w0Var2.f11205k;
            int i11 = w0Var2.F;
            i8 = w0Var2.f11199d;
            int i12 = w0Var2.f11200e;
            String str4 = w0Var2.f11198c;
            str3 = w0Var2.f11197b;
            i9 = i11;
            i10 = i12;
            str = str4;
        } else {
            String L = l0.L(w0Var.f11204j, 1);
            Metadata metadata2 = w0Var.f11205k;
            if (z4) {
                int i13 = w0Var.F;
                int i14 = w0Var.f11199d;
                int i15 = w0Var.f11200e;
                str = w0Var.f11198c;
                str2 = L;
                str3 = w0Var.f11197b;
                i9 = i13;
                i8 = i14;
                metadata = metadata2;
                i10 = i15;
            } else {
                str = null;
                i8 = 0;
                i9 = -1;
                str2 = L;
                str3 = null;
                metadata = metadata2;
                i10 = 0;
            }
        }
        return new w0.b().U(w0Var.f11196a).W(str3).M(w0Var.f11206l).g0(t.g(str2)).K(str2).Z(metadata).I(z4 ? w0Var.f11201f : -1).b0(z4 ? w0Var.f11202g : -1).J(i9).i0(i8).e0(i10).X(str).G();
    }

    private static Map<String, DrmInitData> z(List<DrmInitData> list) {
        ArrayList arrayList = new ArrayList(list);
        HashMap hashMap = new HashMap();
        int i8 = 0;
        while (i8 < arrayList.size()) {
            DrmInitData drmInitData = list.get(i8);
            String str = drmInitData.f9594c;
            i8++;
            int i9 = i8;
            while (i9 < arrayList.size()) {
                DrmInitData drmInitData2 = (DrmInitData) arrayList.get(i9);
                if (TextUtils.equals(drmInitData2.f9594c, str)) {
                    drmInitData = drmInitData.f(drmInitData2);
                    arrayList.remove(i9);
                } else {
                    i9++;
                }
            }
            hashMap.put(str, drmInitData);
        }
        return hashMap;
    }

    public void B() {
        this.f21899b.b(this);
        for (p pVar : this.A) {
            pVar.f0();
        }
        this.f21914x = null;
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.b
    public void a() {
        for (p pVar : this.A) {
            pVar.b0();
        }
        this.f21914x.e(this);
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public long b() {
        return this.F.b();
    }

    @Override // com.google.android.exoplayer2.source.j
    public long c(long j8, i0 i0Var) {
        p[] pVarArr;
        for (p pVar : this.B) {
            if (pVar.R()) {
                return pVar.c(j8, i0Var);
            }
        }
        return j8;
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public boolean d(long j8) {
        if (this.f21916z == null) {
            for (p pVar : this.A) {
                pVar.B();
            }
            return false;
        }
        return this.F.d(j8);
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.b
    public boolean e(Uri uri, c.C0117c c0117c, boolean z4) {
        boolean z8 = true;
        for (p pVar : this.A) {
            z8 &= pVar.a0(uri, c0117c, z4);
        }
        this.f21914x.e(this);
        return z8;
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public boolean f() {
        return this.F.f();
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public long g() {
        return this.F.g();
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public void h(long j8) {
        this.F.h(j8);
    }

    @Override // com.google.android.exoplayer2.source.j
    public void l() {
        for (p pVar : this.A) {
            pVar.l();
        }
    }

    @Override // com.google.android.exoplayer2.source.j
    public long n(long j8) {
        p[] pVarArr = this.B;
        if (pVarArr.length > 0) {
            boolean i02 = pVarArr[0].i0(j8, false);
            int i8 = 1;
            while (true) {
                p[] pVarArr2 = this.B;
                if (i8 >= pVarArr2.length) {
                    break;
                }
                pVarArr2[i8].i0(j8, i02);
                i8++;
            }
            if (i02) {
                this.f21908l.b();
            }
        }
        return j8;
    }

    @Override // com.google.android.exoplayer2.source.j
    public long p() {
        return -9223372036854775807L;
    }

    @Override // com.google.android.exoplayer2.source.j
    public void q(j.a aVar, long j8) {
        this.f21914x = aVar;
        this.f21899b.m(this);
        w(j8);
    }

    @Override // com.google.android.exoplayer2.source.j
    public h5.w r() {
        return (h5.w) b6.a.e(this.f21916z);
    }

    @Override // com.google.android.exoplayer2.source.j
    public long s(z5.r[] rVarArr, boolean[] zArr, h5.r[] rVarArr2, boolean[] zArr2, long j8) {
        h5.r[] rVarArr3 = rVarArr2;
        int[] iArr = new int[rVarArr.length];
        int[] iArr2 = new int[rVarArr.length];
        for (int i8 = 0; i8 < rVarArr.length; i8++) {
            iArr[i8] = rVarArr3[i8] == null ? -1 : this.f21907k.get(rVarArr3[i8]).intValue();
            iArr2[i8] = -1;
            if (rVarArr[i8] != null) {
                u b9 = rVarArr[i8].b();
                int i9 = 0;
                while (true) {
                    p[] pVarArr = this.A;
                    if (i9 >= pVarArr.length) {
                        break;
                    } else if (pVarArr[i9].r().c(b9) != -1) {
                        iArr2[i8] = i9;
                        break;
                    } else {
                        i9++;
                    }
                }
            }
        }
        this.f21907k.clear();
        int length = rVarArr.length;
        h5.r[] rVarArr4 = new h5.r[length];
        h5.r[] rVarArr5 = new h5.r[rVarArr.length];
        z5.r[] rVarArr6 = new z5.r[rVarArr.length];
        p[] pVarArr2 = new p[this.A.length];
        int i10 = 0;
        int i11 = 0;
        boolean z4 = false;
        while (i11 < this.A.length) {
            for (int i12 = 0; i12 < rVarArr.length; i12++) {
                z5.r rVar = null;
                rVarArr5[i12] = iArr[i12] == i11 ? rVarArr3[i12] : null;
                if (iArr2[i12] == i11) {
                    rVar = rVarArr[i12];
                }
                rVarArr6[i12] = rVar;
            }
            p pVar = this.A[i11];
            int i13 = i10;
            int i14 = length;
            int i15 = i11;
            z5.r[] rVarArr7 = rVarArr6;
            p[] pVarArr3 = pVarArr2;
            boolean j02 = pVar.j0(rVarArr6, zArr, rVarArr5, zArr2, j8, z4);
            int i16 = 0;
            boolean z8 = false;
            while (true) {
                if (i16 >= rVarArr.length) {
                    break;
                }
                h5.r rVar2 = rVarArr5[i16];
                if (iArr2[i16] == i15) {
                    b6.a.e(rVar2);
                    rVarArr4[i16] = rVar2;
                    this.f21907k.put(rVar2, Integer.valueOf(i15));
                    z8 = true;
                } else if (iArr[i16] == i15) {
                    b6.a.f(rVar2 == null);
                }
                i16++;
            }
            if (z8) {
                pVarArr3[i13] = pVar;
                i10 = i13 + 1;
                if (i13 == 0) {
                    pVar.m0(true);
                    if (!j02) {
                        p[] pVarArr4 = this.B;
                        if (pVarArr4.length != 0 && pVar == pVarArr4[0]) {
                        }
                    }
                    this.f21908l.b();
                    z4 = true;
                } else {
                    pVar.m0(i15 < this.E);
                }
            } else {
                i10 = i13;
            }
            i11 = i15 + 1;
            pVarArr2 = pVarArr3;
            length = i14;
            rVarArr6 = rVarArr7;
            rVarArr3 = rVarArr2;
        }
        System.arraycopy(rVarArr4, 0, rVarArr3, 0, length);
        p[] pVarArr5 = (p[]) l0.H0(pVarArr2, i10);
        this.B = pVarArr5;
        this.F = this.f21909m.a(pVarArr5);
        return j8;
    }

    @Override // com.google.android.exoplayer2.source.j
    public void u(long j8, boolean z4) {
        for (p pVar : this.B) {
            pVar.u(j8, z4);
        }
    }
}
