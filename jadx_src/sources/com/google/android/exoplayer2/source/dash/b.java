package com.google.android.exoplayer2.source.dash;

import a6.t;
import a6.y;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseIntArray;
import b6.l0;
import com.google.android.exoplayer2.drm.h;
import com.google.android.exoplayer2.source.dash.a;
import com.google.android.exoplayer2.source.dash.e;
import com.google.android.exoplayer2.source.j;
import com.google.android.exoplayer2.source.l;
import com.google.android.exoplayer2.source.w;
import com.google.android.exoplayer2.w0;
import h5.u;
import i4.i0;
import j4.t1;
import j5.i;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import l5.f;
import l5.g;
import z5.r;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b implements j, w.a<i<com.google.android.exoplayer2.source.dash.a>>, i.b<com.google.android.exoplayer2.source.dash.a> {
    private static final Pattern F = Pattern.compile("CC([1-4])=(.+)");
    private static final Pattern G = Pattern.compile("([1-4])=lang:(\\w+)(,.+)?");
    private w A;
    private l5.c B;
    private int C;
    private List<f> E;

    /* renamed from: a  reason: collision with root package name */
    final int f10345a;

    /* renamed from: b  reason: collision with root package name */
    private final a.InterfaceC0109a f10346b;

    /* renamed from: c  reason: collision with root package name */
    private final y f10347c;

    /* renamed from: d  reason: collision with root package name */
    private final com.google.android.exoplayer2.drm.i f10348d;

    /* renamed from: e  reason: collision with root package name */
    private final com.google.android.exoplayer2.upstream.c f10349e;

    /* renamed from: f  reason: collision with root package name */
    private final k5.b f10350f;

    /* renamed from: g  reason: collision with root package name */
    private final long f10351g;

    /* renamed from: h  reason: collision with root package name */
    private final t f10352h;

    /* renamed from: j  reason: collision with root package name */
    private final a6.b f10353j;

    /* renamed from: k  reason: collision with root package name */
    private final h5.w f10354k;

    /* renamed from: l  reason: collision with root package name */
    private final a[] f10355l;

    /* renamed from: m  reason: collision with root package name */
    private final h5.d f10356m;

    /* renamed from: n  reason: collision with root package name */
    private final e f10357n;
    private final l.a q;

    /* renamed from: t  reason: collision with root package name */
    private final h.a f10359t;

    /* renamed from: w  reason: collision with root package name */
    private final t1 f10360w;

    /* renamed from: x  reason: collision with root package name */
    private j.a f10361x;

    /* renamed from: y  reason: collision with root package name */
    private i<com.google.android.exoplayer2.source.dash.a>[] f10362y = F(0);

    /* renamed from: z  reason: collision with root package name */
    private d[] f10363z = new d[0];

    /* renamed from: p  reason: collision with root package name */
    private final IdentityHashMap<i<com.google.android.exoplayer2.source.dash.a>, e.c> f10358p = new IdentityHashMap<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final int[] f10364a;

        /* renamed from: b  reason: collision with root package name */
        public final int f10365b;

        /* renamed from: c  reason: collision with root package name */
        public final int f10366c;

        /* renamed from: d  reason: collision with root package name */
        public final int f10367d;

        /* renamed from: e  reason: collision with root package name */
        public final int f10368e;

        /* renamed from: f  reason: collision with root package name */
        public final int f10369f;

        /* renamed from: g  reason: collision with root package name */
        public final int f10370g;

        private a(int i8, int i9, int[] iArr, int i10, int i11, int i12, int i13) {
            this.f10365b = i8;
            this.f10364a = iArr;
            this.f10366c = i9;
            this.f10368e = i10;
            this.f10369f = i11;
            this.f10370g = i12;
            this.f10367d = i13;
        }

        public static a a(int[] iArr, int i8) {
            return new a(3, 1, iArr, i8, -1, -1, -1);
        }

        public static a b(int[] iArr, int i8) {
            return new a(5, 1, iArr, i8, -1, -1, -1);
        }

        public static a c(int i8) {
            return new a(5, 2, new int[0], -1, -1, -1, i8);
        }

        public static a d(int i8, int[] iArr, int i9, int i10, int i11) {
            return new a(i8, 0, iArr, i9, i10, i11, -1);
        }
    }

    public b(int i8, l5.c cVar, k5.b bVar, int i9, a.InterfaceC0109a interfaceC0109a, y yVar, com.google.android.exoplayer2.drm.i iVar, h.a aVar, com.google.android.exoplayer2.upstream.c cVar2, l.a aVar2, long j8, t tVar, a6.b bVar2, h5.d dVar, e.b bVar3, t1 t1Var) {
        this.f10345a = i8;
        this.B = cVar;
        this.f10350f = bVar;
        this.C = i9;
        this.f10346b = interfaceC0109a;
        this.f10347c = yVar;
        this.f10348d = iVar;
        this.f10359t = aVar;
        this.f10349e = cVar2;
        this.q = aVar2;
        this.f10351g = j8;
        this.f10352h = tVar;
        this.f10353j = bVar2;
        this.f10356m = dVar;
        this.f10360w = t1Var;
        this.f10357n = new e(cVar, bVar3, bVar2);
        this.A = dVar.a(this.f10362y);
        g d8 = cVar.d(i9);
        List<f> list = d8.f21672d;
        this.E = list;
        Pair<h5.w, a[]> v8 = v(iVar, d8.f21671c, list);
        this.f10354k = (h5.w) v8.first;
        this.f10355l = (a[]) v8.second;
    }

    private static int[][] A(List<l5.a> list) {
        int i8;
        l5.e w8;
        int size = list.size();
        SparseIntArray sparseIntArray = new SparseIntArray(size);
        ArrayList arrayList = new ArrayList(size);
        SparseArray sparseArray = new SparseArray(size);
        for (int i9 = 0; i9 < size; i9++) {
            sparseIntArray.put(list.get(i9).f21624a, i9);
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(Integer.valueOf(i9));
            arrayList.add(arrayList2);
            sparseArray.put(i9, arrayList2);
        }
        for (int i10 = 0; i10 < size; i10++) {
            l5.a aVar = list.get(i10);
            l5.e y8 = y(aVar.f21628e);
            if (y8 == null) {
                y8 = y(aVar.f21629f);
            }
            if (y8 == null || (i8 = sparseIntArray.get(Integer.parseInt(y8.f21662b), -1)) == -1) {
                i8 = i10;
            }
            if (i8 == i10 && (w8 = w(aVar.f21629f)) != null) {
                for (String str : l0.R0(w8.f21662b, ",")) {
                    int i11 = sparseIntArray.get(Integer.parseInt(str), -1);
                    if (i11 != -1) {
                        i8 = Math.min(i8, i11);
                    }
                }
            }
            if (i8 != i10) {
                List list2 = (List) sparseArray.get(i10);
                List list3 = (List) sparseArray.get(i8);
                list3.addAll(list2);
                sparseArray.put(i10, list3);
                arrayList.remove(list2);
            }
        }
        int size2 = arrayList.size();
        int[][] iArr = new int[size2];
        for (int i12 = 0; i12 < size2; i12++) {
            iArr[i12] = com.google.common.primitives.g.l((Collection) arrayList.get(i12));
            Arrays.sort(iArr[i12]);
        }
        return iArr;
    }

    private int B(int i8, int[] iArr) {
        int i9 = iArr[i8];
        if (i9 == -1) {
            return -1;
        }
        int i10 = this.f10355l[i9].f10368e;
        for (int i11 = 0; i11 < iArr.length; i11++) {
            int i12 = iArr[i11];
            if (i12 == i10 && this.f10355l[i12].f10366c == 0) {
                return i11;
            }
        }
        return -1;
    }

    private int[] C(r[] rVarArr) {
        int[] iArr = new int[rVarArr.length];
        for (int i8 = 0; i8 < rVarArr.length; i8++) {
            if (rVarArr[i8] != null) {
                iArr[i8] = this.f10354k.c(rVarArr[i8].b());
            } else {
                iArr[i8] = -1;
            }
        }
        return iArr;
    }

    private static boolean D(List<l5.a> list, int[] iArr) {
        for (int i8 : iArr) {
            List<l5.j> list2 = list.get(i8).f21626c;
            for (int i9 = 0; i9 < list2.size(); i9++) {
                if (!list2.get(i9).f21687e.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int E(int i8, List<l5.a> list, int[][] iArr, boolean[] zArr, w0[][] w0VarArr) {
        int i9 = 0;
        for (int i10 = 0; i10 < i8; i10++) {
            if (D(list, iArr[i10])) {
                zArr[i10] = true;
                i9++;
            }
            w0VarArr[i10] = z(list, iArr[i10]);
            if (w0VarArr[i10].length != 0) {
                i9++;
            }
        }
        return i9;
    }

    private static i<com.google.android.exoplayer2.source.dash.a>[] F(int i8) {
        return new i[i8];
    }

    private static w0[] H(l5.e eVar, Pattern pattern, w0 w0Var) {
        String str = eVar.f21662b;
        if (str == null) {
            return new w0[]{w0Var};
        }
        String[] R0 = l0.R0(str, ";");
        w0[] w0VarArr = new w0[R0.length];
        for (int i8 = 0; i8 < R0.length; i8++) {
            Matcher matcher = pattern.matcher(R0[i8]);
            if (!matcher.matches()) {
                return new w0[]{w0Var};
            }
            int parseInt = Integer.parseInt(matcher.group(1));
            w0VarArr[i8] = w0Var.b().U(w0Var.f11196a + ":" + parseInt).H(parseInt).X(matcher.group(2)).G();
        }
        return w0VarArr;
    }

    private void J(r[] rVarArr, boolean[] zArr, h5.r[] rVarArr2) {
        for (int i8 = 0; i8 < rVarArr.length; i8++) {
            if (rVarArr[i8] == null || !zArr[i8]) {
                if (rVarArr2[i8] instanceof i) {
                    ((i) rVarArr2[i8]).Q(this);
                } else if (rVarArr2[i8] instanceof i.a) {
                    ((i.a) rVarArr2[i8]).c();
                }
                rVarArr2[i8] = null;
            }
        }
    }

    private void K(r[] rVarArr, h5.r[] rVarArr2, int[] iArr) {
        for (int i8 = 0; i8 < rVarArr.length; i8++) {
            if ((rVarArr2[i8] instanceof h5.g) || (rVarArr2[i8] instanceof i.a)) {
                int B = B(i8, iArr);
                if (!(B == -1 ? rVarArr2[i8] instanceof h5.g : (rVarArr2[i8] instanceof i.a) && ((i.a) rVarArr2[i8]).f20771a == rVarArr2[B])) {
                    if (rVarArr2[i8] instanceof i.a) {
                        ((i.a) rVarArr2[i8]).c();
                    }
                    rVarArr2[i8] = null;
                }
            }
        }
    }

    private void L(r[] rVarArr, h5.r[] rVarArr2, boolean[] zArr, long j8, int[] iArr) {
        for (int i8 = 0; i8 < rVarArr.length; i8++) {
            r rVar = rVarArr[i8];
            if (rVar != null) {
                if (rVarArr2[i8] == null) {
                    zArr[i8] = true;
                    a aVar = this.f10355l[iArr[i8]];
                    int i9 = aVar.f10366c;
                    if (i9 == 0) {
                        rVarArr2[i8] = t(aVar, rVar, j8);
                    } else if (i9 == 2) {
                        rVarArr2[i8] = new d(this.E.get(aVar.f10367d), rVar.b().b(0), this.B.f21637d);
                    }
                } else if (rVarArr2[i8] instanceof i) {
                    ((com.google.android.exoplayer2.source.dash.a) ((i) rVarArr2[i8]).E()).b(rVar);
                }
            }
        }
        for (int i10 = 0; i10 < rVarArr.length; i10++) {
            if (rVarArr2[i10] == null && rVarArr[i10] != null) {
                a aVar2 = this.f10355l[iArr[i10]];
                if (aVar2.f10366c == 1) {
                    int B = B(i10, iArr);
                    if (B == -1) {
                        rVarArr2[i10] = new h5.g();
                    } else {
                        rVarArr2[i10] = ((i) rVarArr2[B]).T(j8, aVar2.f10365b);
                    }
                }
            }
        }
    }

    private static void m(List<f> list, u[] uVarArr, a[] aVarArr, int i8) {
        f fVar;
        int i9 = 0;
        while (i9 < list.size()) {
            uVarArr[i8] = new u(fVar.a() + ":" + i9, new w0.b().U(list.get(i9).a()).g0("application/x-emsg").G());
            aVarArr[i8] = a.c(i9);
            i9++;
            i8++;
        }
    }

    private static int o(com.google.android.exoplayer2.drm.i iVar, List<l5.a> list, int[][] iArr, int i8, boolean[] zArr, w0[][] w0VarArr, u[] uVarArr, a[] aVarArr) {
        int i9;
        int i10;
        int i11 = 0;
        int i12 = 0;
        while (i11 < i8) {
            int[] iArr2 = iArr[i11];
            ArrayList arrayList = new ArrayList();
            for (int i13 : iArr2) {
                arrayList.addAll(list.get(i13).f21626c);
            }
            int size = arrayList.size();
            w0[] w0VarArr2 = new w0[size];
            for (int i14 = 0; i14 < size; i14++) {
                w0 w0Var = ((l5.j) arrayList.get(i14)).f21684b;
                w0VarArr2[i14] = w0Var.c(iVar.c(w0Var));
            }
            l5.a aVar = list.get(iArr2[0]);
            int i15 = aVar.f21624a;
            String num = i15 != -1 ? Integer.toString(i15) : "unset:" + i11;
            int i16 = i12 + 1;
            if (zArr[i11]) {
                i9 = i16 + 1;
            } else {
                i9 = i16;
                i16 = -1;
            }
            if (w0VarArr[i11].length != 0) {
                i10 = i9 + 1;
            } else {
                i10 = i9;
                i9 = -1;
            }
            uVarArr[i12] = new u(num, w0VarArr2);
            aVarArr[i12] = a.d(aVar.f21625b, iArr2, i12, i16, i9);
            if (i16 != -1) {
                String str = num + ":emsg";
                uVarArr[i16] = new u(str, new w0.b().U(str).g0("application/x-emsg").G());
                aVarArr[i16] = a.b(iArr2, i12);
            }
            if (i9 != -1) {
                uVarArr[i9] = new u(num + ":cc", w0VarArr[i11]);
                aVarArr[i9] = a.a(iArr2, i12);
            }
            i11++;
            i12 = i10;
        }
        return i12;
    }

    private i<com.google.android.exoplayer2.source.dash.a> t(a aVar, r rVar, long j8) {
        int i8;
        u uVar;
        u uVar2;
        int i9;
        int i10 = aVar.f10369f;
        boolean z4 = i10 != -1;
        e.c cVar = null;
        if (z4) {
            uVar = this.f10354k.b(i10);
            i8 = 1;
        } else {
            i8 = 0;
            uVar = null;
        }
        int i11 = aVar.f10370g;
        boolean z8 = i11 != -1;
        if (z8) {
            uVar2 = this.f10354k.b(i11);
            i8 += uVar2.f20308a;
        } else {
            uVar2 = null;
        }
        w0[] w0VarArr = new w0[i8];
        int[] iArr = new int[i8];
        if (z4) {
            w0VarArr[0] = uVar.b(0);
            iArr[0] = 5;
            i9 = 1;
        } else {
            i9 = 0;
        }
        ArrayList arrayList = new ArrayList();
        if (z8) {
            for (int i12 = 0; i12 < uVar2.f20308a; i12++) {
                w0VarArr[i9] = uVar2.b(i12);
                iArr[i9] = 3;
                arrayList.add(w0VarArr[i9]);
                i9++;
            }
        }
        if (this.B.f21637d && z4) {
            cVar = this.f10357n.k();
        }
        e.c cVar2 = cVar;
        i<com.google.android.exoplayer2.source.dash.a> iVar = new i<>(aVar.f10365b, iArr, w0VarArr, this.f10346b.a(this.f10352h, this.B, this.f10350f, this.C, aVar.f10364a, rVar, aVar.f10365b, this.f10351g, z4, arrayList, cVar2, this.f10347c, this.f10360w), this, this.f10353j, j8, this.f10348d, this.f10359t, this.f10349e, this.q);
        synchronized (this) {
            this.f10358p.put(iVar, cVar2);
        }
        return iVar;
    }

    private static Pair<h5.w, a[]> v(com.google.android.exoplayer2.drm.i iVar, List<l5.a> list, List<f> list2) {
        int[][] A = A(list);
        int length = A.length;
        boolean[] zArr = new boolean[length];
        w0[][] w0VarArr = new w0[length];
        int E = E(length, list, A, zArr, w0VarArr) + length + list2.size();
        u[] uVarArr = new u[E];
        a[] aVarArr = new a[E];
        m(list2, uVarArr, aVarArr, o(iVar, list, A, length, zArr, w0VarArr, uVarArr, aVarArr));
        return Pair.create(new h5.w(uVarArr), aVarArr);
    }

    private static l5.e w(List<l5.e> list) {
        return x(list, "urn:mpeg:dash:adaptation-set-switching:2016");
    }

    private static l5.e x(List<l5.e> list, String str) {
        for (int i8 = 0; i8 < list.size(); i8++) {
            l5.e eVar = list.get(i8);
            if (str.equals(eVar.f21661a)) {
                return eVar;
            }
        }
        return null;
    }

    private static l5.e y(List<l5.e> list) {
        return x(list, "http://dashif.org/guidelines/trickmode");
    }

    private static w0[] z(List<l5.a> list, int[] iArr) {
        w0 G2;
        Pattern pattern;
        for (int i8 : iArr) {
            l5.a aVar = list.get(i8);
            List<l5.e> list2 = list.get(i8).f21627d;
            for (int i9 = 0; i9 < list2.size(); i9++) {
                l5.e eVar = list2.get(i9);
                if ("urn:scte:dash:cc:cea-608:2015".equals(eVar.f21661a)) {
                    G2 = new w0.b().g0("application/cea-608").U(aVar.f21624a + ":cea608").G();
                    pattern = F;
                } else if ("urn:scte:dash:cc:cea-708:2015".equals(eVar.f21661a)) {
                    G2 = new w0.b().g0("application/cea-708").U(aVar.f21624a + ":cea708").G();
                    pattern = G;
                }
                return H(eVar, pattern, G2);
            }
        }
        return new w0[0];
    }

    @Override // com.google.android.exoplayer2.source.w.a
    /* renamed from: G */
    public void e(i<com.google.android.exoplayer2.source.dash.a> iVar) {
        this.f10361x.e(this);
    }

    public void I() {
        this.f10357n.o();
        for (i<com.google.android.exoplayer2.source.dash.a> iVar : this.f10362y) {
            iVar.Q(this);
        }
        this.f10361x = null;
    }

    public void M(l5.c cVar, int i8) {
        d[] dVarArr;
        this.B = cVar;
        this.C = i8;
        this.f10357n.q(cVar);
        i<com.google.android.exoplayer2.source.dash.a>[] iVarArr = this.f10362y;
        if (iVarArr != null) {
            for (i<com.google.android.exoplayer2.source.dash.a> iVar : iVarArr) {
                iVar.E().h(cVar, i8);
            }
            this.f10361x.e(this);
        }
        this.E = cVar.d(i8).f21672d;
        for (d dVar : this.f10363z) {
            Iterator<f> it = this.E.iterator();
            while (true) {
                if (it.hasNext()) {
                    f next = it.next();
                    if (next.a().equals(dVar.b())) {
                        boolean z4 = true;
                        dVar.d(next, (cVar.f21637d && i8 == cVar.e() - 1) ? false : false);
                    }
                }
            }
        }
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public long b() {
        return this.A.b();
    }

    @Override // com.google.android.exoplayer2.source.j
    public long c(long j8, i0 i0Var) {
        i<com.google.android.exoplayer2.source.dash.a>[] iVarArr;
        for (i<com.google.android.exoplayer2.source.dash.a> iVar : this.f10362y) {
            if (iVar.f20752a == 2) {
                return iVar.c(j8, i0Var);
            }
        }
        return j8;
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public boolean d(long j8) {
        return this.A.d(j8);
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public boolean f() {
        return this.A.f();
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public long g() {
        return this.A.g();
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public void h(long j8) {
        this.A.h(j8);
    }

    @Override // j5.i.b
    public synchronized void j(i<com.google.android.exoplayer2.source.dash.a> iVar) {
        e.c remove = this.f10358p.remove(iVar);
        if (remove != null) {
            remove.n();
        }
    }

    @Override // com.google.android.exoplayer2.source.j
    public void l() {
        this.f10352h.a();
    }

    @Override // com.google.android.exoplayer2.source.j
    public long n(long j8) {
        for (i<com.google.android.exoplayer2.source.dash.a> iVar : this.f10362y) {
            iVar.S(j8);
        }
        for (d dVar : this.f10363z) {
            dVar.c(j8);
        }
        return j8;
    }

    @Override // com.google.android.exoplayer2.source.j
    public long p() {
        return -9223372036854775807L;
    }

    @Override // com.google.android.exoplayer2.source.j
    public void q(j.a aVar, long j8) {
        this.f10361x = aVar;
        aVar.k(this);
    }

    @Override // com.google.android.exoplayer2.source.j
    public h5.w r() {
        return this.f10354k;
    }

    @Override // com.google.android.exoplayer2.source.j
    public long s(r[] rVarArr, boolean[] zArr, h5.r[] rVarArr2, boolean[] zArr2, long j8) {
        int[] C = C(rVarArr);
        J(rVarArr, zArr, rVarArr2);
        K(rVarArr, rVarArr2, C);
        L(rVarArr, rVarArr2, zArr2, j8, C);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (h5.r rVar : rVarArr2) {
            if (rVar instanceof i) {
                arrayList.add((i) rVar);
            } else if (rVar instanceof d) {
                arrayList2.add((d) rVar);
            }
        }
        i<com.google.android.exoplayer2.source.dash.a>[] F2 = F(arrayList.size());
        this.f10362y = F2;
        arrayList.toArray(F2);
        d[] dVarArr = new d[arrayList2.size()];
        this.f10363z = dVarArr;
        arrayList2.toArray(dVarArr);
        this.A = this.f10356m.a(this.f10362y);
        return j8;
    }

    @Override // com.google.android.exoplayer2.source.j
    public void u(long j8, boolean z4) {
        for (i<com.google.android.exoplayer2.source.dash.a> iVar : this.f10362y) {
            iVar.u(j8, z4);
        }
    }
}
