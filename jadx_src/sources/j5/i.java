package j5;

import b6.l0;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.h;
import com.google.android.exoplayer2.source.l;
import com.google.android.exoplayer2.source.v;
import com.google.android.exoplayer2.source.w;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.w0;
import h5.r;
import i4.i0;
import i4.s;
import j5.j;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i<T extends j> implements r, w, Loader.b<f>, Loader.f {
    private int A;
    private j5.a B;
    boolean C;

    /* renamed from: a  reason: collision with root package name */
    public final int f20752a;

    /* renamed from: b  reason: collision with root package name */
    private final int[] f20753b;

    /* renamed from: c  reason: collision with root package name */
    private final w0[] f20754c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean[] f20755d;

    /* renamed from: e  reason: collision with root package name */
    private final T f20756e;

    /* renamed from: f  reason: collision with root package name */
    private final w.a<i<T>> f20757f;

    /* renamed from: g  reason: collision with root package name */
    private final l.a f20758g;

    /* renamed from: h  reason: collision with root package name */
    private final com.google.android.exoplayer2.upstream.c f20759h;

    /* renamed from: j  reason: collision with root package name */
    private final Loader f20760j;

    /* renamed from: k  reason: collision with root package name */
    private final h f20761k;

    /* renamed from: l  reason: collision with root package name */
    private final ArrayList<j5.a> f20762l;

    /* renamed from: m  reason: collision with root package name */
    private final List<j5.a> f20763m;

    /* renamed from: n  reason: collision with root package name */
    private final v f20764n;

    /* renamed from: p  reason: collision with root package name */
    private final v[] f20765p;
    private final c q;

    /* renamed from: t  reason: collision with root package name */
    private f f20766t;

    /* renamed from: w  reason: collision with root package name */
    private w0 f20767w;

    /* renamed from: x  reason: collision with root package name */
    private b<T> f20768x;

    /* renamed from: y  reason: collision with root package name */
    private long f20769y;

    /* renamed from: z  reason: collision with root package name */
    private long f20770z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class a implements r {

        /* renamed from: a  reason: collision with root package name */
        public final i<T> f20771a;

        /* renamed from: b  reason: collision with root package name */
        private final v f20772b;

        /* renamed from: c  reason: collision with root package name */
        private final int f20773c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f20774d;

        public a(i<T> iVar, v vVar, int i8) {
            this.f20771a = iVar;
            this.f20772b = vVar;
            this.f20773c = i8;
        }

        private void b() {
            if (this.f20774d) {
                return;
            }
            i.this.f20758g.i(i.this.f20753b[this.f20773c], i.this.f20754c[this.f20773c], 0, null, i.this.f20770z);
            this.f20774d = true;
        }

        @Override // h5.r
        public void a() {
        }

        public void c() {
            b6.a.f(i.this.f20755d[this.f20773c]);
            i.this.f20755d[this.f20773c] = false;
        }

        @Override // h5.r
        public boolean e() {
            return !i.this.I() && this.f20772b.K(i.this.C);
        }

        @Override // h5.r
        public int m(long j8) {
            if (i.this.I()) {
                return 0;
            }
            int E = this.f20772b.E(j8, i.this.C);
            if (i.this.B != null) {
                E = Math.min(E, i.this.B.i(this.f20773c + 1) - this.f20772b.C());
            }
            this.f20772b.e0(E);
            if (E > 0) {
                b();
            }
            return E;
        }

        @Override // h5.r
        public int o(s sVar, DecoderInputBuffer decoderInputBuffer, int i8) {
            if (i.this.I()) {
                return -3;
            }
            if (i.this.B == null || i.this.B.i(this.f20773c + 1) > this.f20772b.C()) {
                b();
                return this.f20772b.S(sVar, decoderInputBuffer, i8, i.this.C);
            }
            return -3;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b<T extends j> {
        void j(i<T> iVar);
    }

    public i(int i8, int[] iArr, w0[] w0VarArr, T t8, w.a<i<T>> aVar, a6.b bVar, long j8, com.google.android.exoplayer2.drm.i iVar, h.a aVar2, com.google.android.exoplayer2.upstream.c cVar, l.a aVar3) {
        this.f20752a = i8;
        int i9 = 0;
        iArr = iArr == null ? new int[0] : iArr;
        this.f20753b = iArr;
        this.f20754c = w0VarArr == null ? new w0[0] : w0VarArr;
        this.f20756e = t8;
        this.f20757f = aVar;
        this.f20758g = aVar3;
        this.f20759h = cVar;
        this.f20760j = new Loader("ChunkSampleStream");
        this.f20761k = new h();
        ArrayList<j5.a> arrayList = new ArrayList<>();
        this.f20762l = arrayList;
        this.f20763m = Collections.unmodifiableList(arrayList);
        int length = iArr.length;
        this.f20765p = new v[length];
        this.f20755d = new boolean[length];
        int i10 = length + 1;
        int[] iArr2 = new int[i10];
        v[] vVarArr = new v[i10];
        v k8 = v.k(bVar, iVar, aVar2);
        this.f20764n = k8;
        iArr2[0] = i8;
        vVarArr[0] = k8;
        while (i9 < length) {
            v l8 = v.l(bVar);
            this.f20765p[i9] = l8;
            int i11 = i9 + 1;
            vVarArr[i11] = l8;
            iArr2[i11] = this.f20753b[i9];
            i9 = i11;
        }
        this.q = new c(iArr2, vVarArr);
        this.f20769y = j8;
        this.f20770z = j8;
    }

    private void B(int i8) {
        int min = Math.min(O(i8, 0), this.A);
        if (min > 0) {
            l0.N0(this.f20762l, 0, min);
            this.A -= min;
        }
    }

    private void C(int i8) {
        b6.a.f(!this.f20760j.j());
        int size = this.f20762l.size();
        while (true) {
            if (i8 >= size) {
                i8 = -1;
                break;
            } else if (!G(i8)) {
                break;
            } else {
                i8++;
            }
        }
        if (i8 == -1) {
            return;
        }
        long j8 = F().f20748h;
        j5.a D = D(i8);
        if (this.f20762l.isEmpty()) {
            this.f20769y = this.f20770z;
        }
        this.C = false;
        this.f20758g.D(this.f20752a, D.f20747g, j8);
    }

    private j5.a D(int i8) {
        j5.a aVar = this.f20762l.get(i8);
        ArrayList<j5.a> arrayList = this.f20762l;
        l0.N0(arrayList, i8, arrayList.size());
        this.A = Math.max(this.A, this.f20762l.size());
        v vVar = this.f20764n;
        int i9 = 0;
        while (true) {
            vVar.u(aVar.i(i9));
            v[] vVarArr = this.f20765p;
            if (i9 >= vVarArr.length) {
                return aVar;
            }
            vVar = vVarArr[i9];
            i9++;
        }
    }

    private j5.a F() {
        ArrayList<j5.a> arrayList = this.f20762l;
        return arrayList.get(arrayList.size() - 1);
    }

    private boolean G(int i8) {
        int C;
        j5.a aVar = this.f20762l.get(i8);
        if (this.f20764n.C() > aVar.i(0)) {
            return true;
        }
        int i9 = 0;
        do {
            v[] vVarArr = this.f20765p;
            if (i9 >= vVarArr.length) {
                return false;
            }
            C = vVarArr[i9].C();
            i9++;
        } while (C <= aVar.i(i9));
        return true;
    }

    private boolean H(f fVar) {
        return fVar instanceof j5.a;
    }

    private void J() {
        int O = O(this.f20764n.C(), this.A - 1);
        while (true) {
            int i8 = this.A;
            if (i8 > O) {
                return;
            }
            this.A = i8 + 1;
            K(i8);
        }
    }

    private void K(int i8) {
        j5.a aVar = this.f20762l.get(i8);
        w0 w0Var = aVar.f20744d;
        if (!w0Var.equals(this.f20767w)) {
            this.f20758g.i(this.f20752a, w0Var, aVar.f20745e, aVar.f20746f, aVar.f20747g);
        }
        this.f20767w = w0Var;
    }

    private int O(int i8, int i9) {
        do {
            i9++;
            if (i9 >= this.f20762l.size()) {
                return this.f20762l.size() - 1;
            }
        } while (this.f20762l.get(i9).i(0) <= i8);
        return i9 - 1;
    }

    private void R() {
        this.f20764n.V();
        for (v vVar : this.f20765p) {
            vVar.V();
        }
    }

    public T E() {
        return this.f20756e;
    }

    boolean I() {
        return this.f20769y != -9223372036854775807L;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: L */
    public void j(f fVar, long j8, long j9, boolean z4) {
        this.f20766t = null;
        this.B = null;
        h5.h hVar = new h5.h(fVar.f20741a, fVar.f20742b, fVar.f(), fVar.e(), j8, j9, fVar.b());
        this.f20759h.c(fVar.f20741a);
        this.f20758g.r(hVar, fVar.f20743c, this.f20752a, fVar.f20744d, fVar.f20745e, fVar.f20746f, fVar.f20747g, fVar.f20748h);
        if (z4) {
            return;
        }
        if (I()) {
            R();
        } else if (H(fVar)) {
            D(this.f20762l.size() - 1);
            if (this.f20762l.isEmpty()) {
                this.f20769y = this.f20770z;
            }
        }
        this.f20757f.e(this);
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: M */
    public void k(f fVar, long j8, long j9) {
        this.f20766t = null;
        this.f20756e.g(fVar);
        h5.h hVar = new h5.h(fVar.f20741a, fVar.f20742b, fVar.f(), fVar.e(), j8, j9, fVar.b());
        this.f20759h.c(fVar.f20741a);
        this.f20758g.u(hVar, fVar.f20743c, this.f20752a, fVar.f20744d, fVar.f20745e, fVar.f20746f, fVar.f20747g, fVar.f20748h);
        this.f20757f.e(this);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00f1  */
    @Override // com.google.android.exoplayer2.upstream.Loader.b
    /* renamed from: N */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.google.android.exoplayer2.upstream.Loader.c t(j5.f r31, long r32, long r34, java.io.IOException r36, int r37) {
        /*
            Method dump skipped, instructions count: 257
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: j5.i.t(j5.f, long, long, java.io.IOException, int):com.google.android.exoplayer2.upstream.Loader$c");
    }

    public void P() {
        Q(null);
    }

    public void Q(b<T> bVar) {
        this.f20768x = bVar;
        this.f20764n.R();
        for (v vVar : this.f20765p) {
            vVar.R();
        }
        this.f20760j.m(this);
    }

    public void S(long j8) {
        boolean Z;
        this.f20770z = j8;
        if (I()) {
            this.f20769y = j8;
            return;
        }
        j5.a aVar = null;
        int i8 = 0;
        int i9 = 0;
        while (true) {
            if (i9 >= this.f20762l.size()) {
                break;
            }
            j5.a aVar2 = this.f20762l.get(i9);
            int i10 = (aVar2.f20747g > j8 ? 1 : (aVar2.f20747g == j8 ? 0 : -1));
            if (i10 == 0 && aVar2.f20714k == -9223372036854775807L) {
                aVar = aVar2;
                break;
            } else if (i10 > 0) {
                break;
            } else {
                i9++;
            }
        }
        if (aVar != null) {
            Z = this.f20764n.Y(aVar.i(0));
        } else {
            Z = this.f20764n.Z(j8, j8 < b());
        }
        if (Z) {
            this.A = O(this.f20764n.C(), 0);
            v[] vVarArr = this.f20765p;
            int length = vVarArr.length;
            while (i8 < length) {
                vVarArr[i8].Z(j8, true);
                i8++;
            }
            return;
        }
        this.f20769y = j8;
        this.C = false;
        this.f20762l.clear();
        this.A = 0;
        if (!this.f20760j.j()) {
            this.f20760j.g();
            R();
            return;
        }
        this.f20764n.r();
        v[] vVarArr2 = this.f20765p;
        int length2 = vVarArr2.length;
        while (i8 < length2) {
            vVarArr2[i8].r();
            i8++;
        }
        this.f20760j.f();
    }

    public i<T>.a T(long j8, int i8) {
        for (int i9 = 0; i9 < this.f20765p.length; i9++) {
            if (this.f20753b[i9] == i8) {
                b6.a.f(!this.f20755d[i9]);
                this.f20755d[i9] = true;
                this.f20765p[i9].Z(j8, true);
                return new a(this, this.f20765p[i9], i9);
            }
        }
        throw new IllegalStateException();
    }

    @Override // h5.r
    public void a() {
        this.f20760j.a();
        this.f20764n.N();
        if (this.f20760j.j()) {
            return;
        }
        this.f20756e.a();
    }

    @Override // com.google.android.exoplayer2.source.w
    public long b() {
        if (I()) {
            return this.f20769y;
        }
        if (this.C) {
            return Long.MIN_VALUE;
        }
        return F().f20748h;
    }

    public long c(long j8, i0 i0Var) {
        return this.f20756e.c(j8, i0Var);
    }

    @Override // com.google.android.exoplayer2.source.w
    public boolean d(long j8) {
        List<j5.a> list;
        long j9;
        if (this.C || this.f20760j.j() || this.f20760j.i()) {
            return false;
        }
        boolean I = I();
        if (I) {
            list = Collections.emptyList();
            j9 = this.f20769y;
        } else {
            list = this.f20763m;
            j9 = F().f20748h;
        }
        this.f20756e.j(j8, j9, list, this.f20761k);
        h hVar = this.f20761k;
        boolean z4 = hVar.f20751b;
        f fVar = hVar.f20750a;
        hVar.a();
        if (z4) {
            this.f20769y = -9223372036854775807L;
            this.C = true;
            return true;
        } else if (fVar == null) {
            return false;
        } else {
            this.f20766t = fVar;
            if (H(fVar)) {
                j5.a aVar = (j5.a) fVar;
                if (I) {
                    long j10 = aVar.f20747g;
                    long j11 = this.f20769y;
                    if (j10 != j11) {
                        this.f20764n.b0(j11);
                        for (v vVar : this.f20765p) {
                            vVar.b0(this.f20769y);
                        }
                    }
                    this.f20769y = -9223372036854775807L;
                }
                aVar.k(this.q);
                this.f20762l.add(aVar);
            } else if (fVar instanceof m) {
                ((m) fVar).g(this.q);
            }
            this.f20758g.A(new h5.h(fVar.f20741a, fVar.f20742b, this.f20760j.n(fVar, this, this.f20759h.d(fVar.f20743c))), fVar.f20743c, this.f20752a, fVar.f20744d, fVar.f20745e, fVar.f20746f, fVar.f20747g, fVar.f20748h);
            return true;
        }
    }

    @Override // h5.r
    public boolean e() {
        return !I() && this.f20764n.K(this.C);
    }

    @Override // com.google.android.exoplayer2.source.w
    public boolean f() {
        return this.f20760j.j();
    }

    @Override // com.google.android.exoplayer2.source.w
    public long g() {
        if (this.C) {
            return Long.MIN_VALUE;
        }
        if (I()) {
            return this.f20769y;
        }
        long j8 = this.f20770z;
        j5.a F = F();
        if (!F.h()) {
            if (this.f20762l.size() > 1) {
                ArrayList<j5.a> arrayList = this.f20762l;
                F = arrayList.get(arrayList.size() - 2);
            } else {
                F = null;
            }
        }
        if (F != null) {
            j8 = Math.max(j8, F.f20748h);
        }
        return Math.max(j8, this.f20764n.z());
    }

    @Override // com.google.android.exoplayer2.source.w
    public void h(long j8) {
        if (this.f20760j.i() || I()) {
            return;
        }
        if (!this.f20760j.j()) {
            int i8 = this.f20756e.i(j8, this.f20763m);
            if (i8 < this.f20762l.size()) {
                C(i8);
                return;
            }
            return;
        }
        f fVar = (f) b6.a.e(this.f20766t);
        if (!(H(fVar) && G(this.f20762l.size() - 1)) && this.f20756e.d(j8, fVar, this.f20763m)) {
            this.f20760j.f();
            if (H(fVar)) {
                this.B = (j5.a) fVar;
            }
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.f
    public void i() {
        this.f20764n.T();
        for (v vVar : this.f20765p) {
            vVar.T();
        }
        this.f20756e.release();
        b<T> bVar = this.f20768x;
        if (bVar != null) {
            bVar.j(this);
        }
    }

    @Override // h5.r
    public int m(long j8) {
        if (I()) {
            return 0;
        }
        int E = this.f20764n.E(j8, this.C);
        j5.a aVar = this.B;
        if (aVar != null) {
            E = Math.min(E, aVar.i(0) - this.f20764n.C());
        }
        this.f20764n.e0(E);
        J();
        return E;
    }

    @Override // h5.r
    public int o(s sVar, DecoderInputBuffer decoderInputBuffer, int i8) {
        if (I()) {
            return -3;
        }
        j5.a aVar = this.B;
        if (aVar == null || aVar.i(0) > this.f20764n.C()) {
            J();
            return this.f20764n.S(sVar, decoderInputBuffer, i8, this.C);
        }
        return -3;
    }

    public void u(long j8, boolean z4) {
        if (I()) {
            return;
        }
        int x8 = this.f20764n.x();
        this.f20764n.q(j8, z4, true);
        int x9 = this.f20764n.x();
        if (x9 > x8) {
            long y8 = this.f20764n.y();
            int i8 = 0;
            while (true) {
                v[] vVarArr = this.f20765p;
                if (i8 >= vVarArr.length) {
                    break;
                }
                vVarArr[i8].q(y8, z4, this.f20755d[i8]);
                i8++;
            }
        }
        B(x9);
    }
}
