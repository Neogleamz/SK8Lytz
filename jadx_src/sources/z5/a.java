package z5;

import a6.d;
import b6.l0;
import b6.p;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.w0;
import com.google.android.gms.dynamite.descriptors.com.google.mlkit.dynamite.barcode.ModuleDescriptor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.f1;
import com.google.common.collect.n1;
import com.google.common.collect.p1;
import j5.n;
import j5.o;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import z5.r;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends c {

    /* renamed from: h  reason: collision with root package name */
    private final d f24577h;

    /* renamed from: i  reason: collision with root package name */
    private final long f24578i;

    /* renamed from: j  reason: collision with root package name */
    private final long f24579j;

    /* renamed from: k  reason: collision with root package name */
    private final long f24580k;

    /* renamed from: l  reason: collision with root package name */
    private final int f24581l;

    /* renamed from: m  reason: collision with root package name */
    private final int f24582m;

    /* renamed from: n  reason: collision with root package name */
    private final float f24583n;

    /* renamed from: o  reason: collision with root package name */
    private final float f24584o;

    /* renamed from: p  reason: collision with root package name */
    private final ImmutableList<C0236a> f24585p;
    private final b6.d q;

    /* renamed from: r  reason: collision with root package name */
    private float f24586r;

    /* renamed from: s  reason: collision with root package name */
    private int f24587s;

    /* renamed from: t  reason: collision with root package name */
    private int f24588t;

    /* renamed from: u  reason: collision with root package name */
    private long f24589u;

    /* renamed from: v  reason: collision with root package name */
    private n f24590v;

    /* renamed from: z5.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0236a {

        /* renamed from: a  reason: collision with root package name */
        public final long f24591a;

        /* renamed from: b  reason: collision with root package name */
        public final long f24592b;

        public C0236a(long j8, long j9) {
            this.f24591a = j8;
            this.f24592b = j9;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof C0236a) {
                C0236a c0236a = (C0236a) obj;
                return this.f24591a == c0236a.f24591a && this.f24592b == c0236a.f24592b;
            }
            return false;
        }

        public int hashCode() {
            return (((int) this.f24591a) * 31) + ((int) this.f24592b);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements r.b {

        /* renamed from: a  reason: collision with root package name */
        private final int f24593a;

        /* renamed from: b  reason: collision with root package name */
        private final int f24594b;

        /* renamed from: c  reason: collision with root package name */
        private final int f24595c;

        /* renamed from: d  reason: collision with root package name */
        private final int f24596d;

        /* renamed from: e  reason: collision with root package name */
        private final int f24597e;

        /* renamed from: f  reason: collision with root package name */
        private final float f24598f;

        /* renamed from: g  reason: collision with root package name */
        private final float f24599g;

        /* renamed from: h  reason: collision with root package name */
        private final b6.d f24600h;

        public b() {
            this(ModuleDescriptor.MODULE_VERSION, 25000, 25000, 0.7f);
        }

        public b(int i8, int i9, int i10, float f5) {
            this(i8, i9, i10, 1279, 719, f5, 0.75f, b6.d.f8029a);
        }

        public b(int i8, int i9, int i10, int i11, int i12, float f5, float f8, b6.d dVar) {
            this.f24593a = i8;
            this.f24594b = i9;
            this.f24595c = i10;
            this.f24596d = i11;
            this.f24597e = i12;
            this.f24598f = f5;
            this.f24599g = f8;
            this.f24600h = dVar;
        }

        @Override // z5.r.b
        public final r[] a(r.a[] aVarArr, d dVar, k.b bVar, h2 h2Var) {
            ImmutableList B = a.B(aVarArr);
            r[] rVarArr = new r[aVarArr.length];
            for (int i8 = 0; i8 < aVarArr.length; i8++) {
                r.a aVar = aVarArr[i8];
                if (aVar != null) {
                    int[] iArr = aVar.f24690b;
                    if (iArr.length != 0) {
                        rVarArr[i8] = iArr.length == 1 ? new s(aVar.f24689a, iArr[0], aVar.f24691c) : b(aVar.f24689a, iArr, aVar.f24691c, dVar, (ImmutableList) B.get(i8));
                    }
                }
            }
            return rVarArr;
        }

        protected a b(h5.u uVar, int[] iArr, int i8, d dVar, ImmutableList<C0236a> immutableList) {
            return new a(uVar, iArr, i8, dVar, this.f24593a, this.f24594b, this.f24595c, this.f24596d, this.f24597e, this.f24598f, this.f24599g, immutableList, this.f24600h);
        }
    }

    protected a(h5.u uVar, int[] iArr, int i8, d dVar, long j8, long j9, long j10, int i9, int i10, float f5, float f8, List<C0236a> list, b6.d dVar2) {
        super(uVar, iArr, i8);
        d dVar3;
        long j11;
        if (j10 < j8) {
            p.i("AdaptiveTrackSelection", "Adjusting minDurationToRetainAfterDiscardMs to be at least minDurationForQualityIncreaseMs");
            dVar3 = dVar;
            j11 = j8;
        } else {
            dVar3 = dVar;
            j11 = j10;
        }
        this.f24577h = dVar3;
        this.f24578i = j8 * 1000;
        this.f24579j = j9 * 1000;
        this.f24580k = j11 * 1000;
        this.f24581l = i9;
        this.f24582m = i10;
        this.f24583n = f5;
        this.f24584o = f8;
        this.f24585p = ImmutableList.x(list);
        this.q = dVar2;
        this.f24586r = 1.0f;
        this.f24588t = 0;
        this.f24589u = -9223372036854775807L;
    }

    private int A(long j8, long j9) {
        long C = C(j9);
        int i8 = 0;
        for (int i9 = 0; i9 < this.f24609b; i9++) {
            if (j8 == Long.MIN_VALUE || !e(i9, j8)) {
                w0 h8 = h(i9);
                if (z(h8, h8.f11203h, C)) {
                    return i9;
                }
                i8 = i9;
            }
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ImmutableList<ImmutableList<C0236a>> B(r.a[] aVarArr) {
        ImmutableList.a aVar;
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < aVarArr.length; i8++) {
            if (aVarArr[i8] == null || aVarArr[i8].f24690b.length <= 1) {
                aVar = null;
            } else {
                aVar = ImmutableList.u();
                aVar.a(new C0236a(0L, 0L));
            }
            arrayList.add(aVar);
        }
        long[][] G = G(aVarArr);
        int[] iArr = new int[G.length];
        long[] jArr = new long[G.length];
        for (int i9 = 0; i9 < G.length; i9++) {
            jArr[i9] = G[i9].length == 0 ? 0L : G[i9][0];
        }
        y(arrayList, jArr);
        ImmutableList<Integer> H = H(G);
        for (int i10 = 0; i10 < H.size(); i10++) {
            int intValue = H.get(i10).intValue();
            int i11 = iArr[intValue] + 1;
            iArr[intValue] = i11;
            jArr[intValue] = G[intValue][i11];
            y(arrayList, jArr);
        }
        for (int i12 = 0; i12 < aVarArr.length; i12++) {
            if (arrayList.get(i12) != null) {
                jArr[i12] = jArr[i12] * 2;
            }
        }
        y(arrayList, jArr);
        ImmutableList.a u8 = ImmutableList.u();
        for (int i13 = 0; i13 < arrayList.size(); i13++) {
            ImmutableList.a aVar2 = (ImmutableList.a) arrayList.get(i13);
            u8.a(aVar2 == null ? ImmutableList.E() : aVar2.k());
        }
        return u8.k();
    }

    private long C(long j8) {
        long j9;
        long I = I(j8);
        if (this.f24585p.isEmpty()) {
            return I;
        }
        int i8 = 1;
        while (i8 < this.f24585p.size() - 1 && this.f24585p.get(i8).f24591a < I) {
            i8++;
        }
        C0236a c0236a = this.f24585p.get(i8 - 1);
        C0236a c0236a2 = this.f24585p.get(i8);
        long j10 = c0236a.f24591a;
        return c0236a.f24592b + ((((float) (I - j10)) / ((float) (c0236a2.f24591a - j10))) * ((float) (c0236a2.f24592b - j9)));
    }

    private long D(List<? extends n> list) {
        if (list.isEmpty()) {
            return -9223372036854775807L;
        }
        n nVar = (n) f1.f(list);
        long j8 = nVar.f20747g;
        if (j8 != -9223372036854775807L) {
            long j9 = nVar.f20748h;
            if (j9 != -9223372036854775807L) {
                return j9 - j8;
            }
            return -9223372036854775807L;
        }
        return -9223372036854775807L;
    }

    private long F(o[] oVarArr, List<? extends n> list) {
        int i8 = this.f24587s;
        if (i8 < oVarArr.length && oVarArr[i8].next()) {
            o oVar = oVarArr[this.f24587s];
            return oVar.b() - oVar.a();
        }
        for (o oVar2 : oVarArr) {
            if (oVar2.next()) {
                return oVar2.b() - oVar2.a();
            }
        }
        return D(list);
    }

    private static long[][] G(r.a[] aVarArr) {
        long[][] jArr = new long[aVarArr.length];
        for (int i8 = 0; i8 < aVarArr.length; i8++) {
            r.a aVar = aVarArr[i8];
            if (aVar == null) {
                jArr[i8] = new long[0];
            } else {
                jArr[i8] = new long[aVar.f24690b.length];
                int i9 = 0;
                while (true) {
                    int[] iArr = aVar.f24690b;
                    if (i9 >= iArr.length) {
                        break;
                    }
                    long j8 = aVar.f24689a.b(iArr[i9]).f11203h;
                    long[] jArr2 = jArr[i8];
                    if (j8 == -1) {
                        j8 = 0;
                    }
                    jArr2[i9] = j8;
                    i9++;
                }
                Arrays.sort(jArr[i8]);
            }
        }
        return jArr;
    }

    private static ImmutableList<Integer> H(long[][] jArr) {
        n1 c9 = p1.a().a().c();
        for (int i8 = 0; i8 < jArr.length; i8++) {
            if (jArr[i8].length > 1) {
                int length = jArr[i8].length;
                double[] dArr = new double[length];
                int i9 = 0;
                while (true) {
                    double d8 = 0.0d;
                    if (i9 >= jArr[i8].length) {
                        break;
                    }
                    if (jArr[i8][i9] != -1) {
                        d8 = Math.log(jArr[i8][i9]);
                    }
                    dArr[i9] = d8;
                    i9++;
                }
                int i10 = length - 1;
                double d9 = dArr[i10] - dArr[0];
                int i11 = 0;
                while (i11 < i10) {
                    double d10 = dArr[i11];
                    i11++;
                    c9.put(Double.valueOf(d9 == 0.0d ? 1.0d : (((d10 + dArr[i11]) * 0.5d) - dArr[0]) / d9), Integer.valueOf(i8));
                }
            }
        }
        return ImmutableList.x(c9.values());
    }

    private long I(long j8) {
        long b9;
        long f5 = ((float) this.f24577h.f()) * this.f24583n;
        if (this.f24577h.b() == -9223372036854775807L || j8 == -9223372036854775807L) {
            return ((float) f5) / this.f24586r;
        }
        float f8 = (float) j8;
        return (((float) f5) * Math.max((f8 / this.f24586r) - ((float) b9), 0.0f)) / f8;
    }

    private long J(long j8, long j9) {
        if (j8 == -9223372036854775807L) {
            return this.f24578i;
        }
        if (j9 != -9223372036854775807L) {
            j8 -= j9;
        }
        return Math.min(((float) j8) * this.f24584o, this.f24578i);
    }

    private static void y(List<ImmutableList.a<C0236a>> list, long[] jArr) {
        long j8 = 0;
        for (long j9 : jArr) {
            j8 += j9;
        }
        for (int i8 = 0; i8 < list.size(); i8++) {
            ImmutableList.a<C0236a> aVar = list.get(i8);
            if (aVar != null) {
                aVar.a(new C0236a(j8, jArr[i8]));
            }
        }
    }

    protected long E() {
        return this.f24580k;
    }

    protected boolean K(long j8, List<? extends n> list) {
        long j9 = this.f24589u;
        return j9 == -9223372036854775807L || j8 - j9 >= 1000 || !(list.isEmpty() || ((n) f1.f(list)).equals(this.f24590v));
    }

    @Override // z5.r
    public void a(long j8, long j9, long j10, List<? extends n> list, o[] oVarArr) {
        long b9 = this.q.b();
        long F = F(oVarArr, list);
        int i8 = this.f24588t;
        if (i8 == 0) {
            this.f24588t = 1;
            this.f24587s = A(b9, F);
            return;
        }
        int i9 = this.f24587s;
        int l8 = list.isEmpty() ? -1 : l(((n) f1.f(list)).f20744d);
        if (l8 != -1) {
            i8 = ((n) f1.f(list)).f20745e;
            i9 = l8;
        }
        int A = A(b9, F);
        if (!e(i9, b9)) {
            w0 h8 = h(i9);
            w0 h9 = h(A);
            long J = J(j10, F);
            int i10 = h9.f11203h;
            int i11 = h8.f11203h;
            if ((i10 > i11 && j9 < J) || (i10 < i11 && j9 >= this.f24579j)) {
                A = i9;
            }
        }
        if (A != i9) {
            i8 = 3;
        }
        this.f24588t = i8;
        this.f24587s = A;
    }

    @Override // z5.r
    public int c() {
        return this.f24587s;
    }

    @Override // z5.c, z5.r
    public void g() {
        this.f24590v = null;
    }

    @Override // z5.c, z5.r
    public void i() {
        this.f24589u = -9223372036854775807L;
        this.f24590v = null;
    }

    @Override // z5.c, z5.r
    public int k(long j8, List<? extends n> list) {
        int i8;
        int i9;
        long b9 = this.q.b();
        if (K(b9, list)) {
            this.f24589u = b9;
            this.f24590v = list.isEmpty() ? null : (n) f1.f(list);
            if (list.isEmpty()) {
                return 0;
            }
            int size = list.size();
            long e02 = l0.e0(list.get(size - 1).f20747g - j8, this.f24586r);
            long E = E();
            if (e02 < E) {
                return size;
            }
            w0 h8 = h(A(b9, D(list)));
            for (int i10 = 0; i10 < size; i10++) {
                n nVar = list.get(i10);
                w0 w0Var = nVar.f20744d;
                if (l0.e0(nVar.f20747g - j8, this.f24586r) >= E && w0Var.f11203h < h8.f11203h && (i8 = w0Var.f11212x) != -1 && i8 <= this.f24582m && (i9 = w0Var.f11211w) != -1 && i9 <= this.f24581l && i8 < h8.f11212x) {
                    return i10;
                }
            }
            return size;
        }
        return list.size();
    }

    @Override // z5.r
    public int o() {
        return this.f24588t;
    }

    @Override // z5.c, z5.r
    public void q(float f5) {
        this.f24586r = f5;
    }

    @Override // z5.r
    public Object r() {
        return null;
    }

    protected boolean z(w0 w0Var, int i8, long j8) {
        return ((long) i8) <= j8;
    }
}
