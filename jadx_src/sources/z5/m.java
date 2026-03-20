package z5;

import android.content.Context;
import android.graphics.Point;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.Spatializer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import b6.l0;
import b6.p;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.g;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.y1;
import i4.f0;
import i4.g0;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.RandomAccess;
import z5.a;
import z5.r;
import z5.t;
import z5.y;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m extends t {

    /* renamed from: k  reason: collision with root package name */
    private static final y1<Integer> f24615k = y1.a(z5.f.a);

    /* renamed from: l  reason: collision with root package name */
    private static final y1<Integer> f24616l = y1.a(z5.e.a);

    /* renamed from: d  reason: collision with root package name */
    private final Object f24617d;

    /* renamed from: e  reason: collision with root package name */
    public final Context f24618e;

    /* renamed from: f  reason: collision with root package name */
    private final r.b f24619f;

    /* renamed from: g  reason: collision with root package name */
    private final boolean f24620g;

    /* renamed from: h  reason: collision with root package name */
    private d f24621h;

    /* renamed from: i  reason: collision with root package name */
    private f f24622i;

    /* renamed from: j  reason: collision with root package name */
    private com.google.android.exoplayer2.audio.a f24623j;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends h<b> implements Comparable<b> {
        private final boolean A;
        private final boolean B;

        /* renamed from: e  reason: collision with root package name */
        private final int f24624e;

        /* renamed from: f  reason: collision with root package name */
        private final boolean f24625f;

        /* renamed from: g  reason: collision with root package name */
        private final String f24626g;

        /* renamed from: h  reason: collision with root package name */
        private final d f24627h;

        /* renamed from: j  reason: collision with root package name */
        private final boolean f24628j;

        /* renamed from: k  reason: collision with root package name */
        private final int f24629k;

        /* renamed from: l  reason: collision with root package name */
        private final int f24630l;

        /* renamed from: m  reason: collision with root package name */
        private final int f24631m;

        /* renamed from: n  reason: collision with root package name */
        private final boolean f24632n;

        /* renamed from: p  reason: collision with root package name */
        private final int f24633p;
        private final int q;

        /* renamed from: t  reason: collision with root package name */
        private final boolean f24634t;

        /* renamed from: w  reason: collision with root package name */
        private final int f24635w;

        /* renamed from: x  reason: collision with root package name */
        private final int f24636x;

        /* renamed from: y  reason: collision with root package name */
        private final int f24637y;

        /* renamed from: z  reason: collision with root package name */
        private final int f24638z;

        public b(int i8, h5.u uVar, int i9, d dVar, int i10, boolean z4, com.google.common.base.m<w0> mVar) {
            super(i8, uVar, i9);
            int i11;
            int i12;
            int i13;
            this.f24627h = dVar;
            this.f24626g = m.Q(this.f24675d.f11198c);
            this.f24628j = m.I(i10, false);
            int i14 = 0;
            while (true) {
                i11 = Integer.MAX_VALUE;
                if (i14 >= dVar.f24737p.size()) {
                    i12 = 0;
                    i14 = Integer.MAX_VALUE;
                    break;
                }
                i12 = m.B(this.f24675d, dVar.f24737p.get(i14), false);
                if (i12 > 0) {
                    break;
                }
                i14++;
            }
            this.f24630l = i14;
            this.f24629k = i12;
            this.f24631m = m.E(this.f24675d.f11200e, dVar.q);
            w0 w0Var = this.f24675d;
            int i15 = w0Var.f11200e;
            this.f24632n = i15 == 0 || (i15 & 1) != 0;
            this.f24634t = (w0Var.f11199d & 1) != 0;
            int i16 = w0Var.F;
            this.f24635w = i16;
            this.f24636x = w0Var.G;
            int i17 = w0Var.f11203h;
            this.f24637y = i17;
            this.f24625f = (i17 == -1 || i17 <= dVar.f24739w) && (i16 == -1 || i16 <= dVar.f24738t) && mVar.apply(w0Var);
            String[] g02 = l0.g0();
            int i18 = 0;
            while (true) {
                if (i18 >= g02.length) {
                    i13 = 0;
                    i18 = Integer.MAX_VALUE;
                    break;
                }
                i13 = m.B(this.f24675d, g02[i18], false);
                if (i13 > 0) {
                    break;
                }
                i18++;
            }
            this.f24633p = i18;
            this.q = i13;
            int i19 = 0;
            while (true) {
                if (i19 < dVar.f24740x.size()) {
                    String str = this.f24675d.f11207m;
                    if (str != null && str.equals(dVar.f24740x.get(i19))) {
                        i11 = i19;
                        break;
                    }
                    i19++;
                } else {
                    break;
                }
            }
            this.f24638z = i11;
            this.A = f0.j(i10) == 128;
            this.B = f0.o(i10) == 64;
            this.f24624e = k(i10, z4);
        }

        public static int h(List<b> list, List<b> list2) {
            return ((b) Collections.max(list)).compareTo((b) Collections.max(list2));
        }

        public static ImmutableList<b> j(int i8, h5.u uVar, d dVar, int[] iArr, boolean z4, com.google.common.base.m<w0> mVar) {
            ImmutableList.a u8 = ImmutableList.u();
            for (int i9 = 0; i9 < uVar.f20308a; i9++) {
                u8.a(new b(i8, uVar, i9, dVar, iArr[i9], z4, mVar));
            }
            return u8.k();
        }

        private int k(int i8, boolean z4) {
            if (m.I(i8, this.f24627h.B0)) {
                if (this.f24625f || this.f24627h.f24645v0) {
                    if (m.I(i8, false) && this.f24625f && this.f24675d.f11203h != -1) {
                        d dVar = this.f24627h;
                        if (!dVar.E && !dVar.C && (dVar.D0 || !z4)) {
                            return 2;
                        }
                    }
                    return 1;
                }
                return 0;
            }
            return 0;
        }

        @Override // z5.m.h
        public int c() {
            return this.f24624e;
        }

        @Override // java.lang.Comparable
        /* renamed from: i */
        public int compareTo(b bVar) {
            y1 f5 = (this.f24625f && this.f24628j) ? m.f24615k : m.f24615k.f();
            com.google.common.collect.b0 g8 = com.google.common.collect.b0.k().h(this.f24628j, bVar.f24628j).g(Integer.valueOf(this.f24630l), Integer.valueOf(bVar.f24630l), y1.c().f()).d(this.f24629k, bVar.f24629k).d(this.f24631m, bVar.f24631m).h(this.f24634t, bVar.f24634t).h(this.f24632n, bVar.f24632n).g(Integer.valueOf(this.f24633p), Integer.valueOf(bVar.f24633p), y1.c().f()).d(this.q, bVar.q).h(this.f24625f, bVar.f24625f).g(Integer.valueOf(this.f24638z), Integer.valueOf(bVar.f24638z), y1.c().f()).g(Integer.valueOf(this.f24637y), Integer.valueOf(bVar.f24637y), this.f24627h.C ? m.f24615k.f() : m.f24616l).h(this.A, bVar.A).h(this.B, bVar.B).g(Integer.valueOf(this.f24635w), Integer.valueOf(bVar.f24635w), f5).g(Integer.valueOf(this.f24636x), Integer.valueOf(bVar.f24636x), f5);
            Integer valueOf = Integer.valueOf(this.f24637y);
            Integer valueOf2 = Integer.valueOf(bVar.f24637y);
            if (!l0.c(this.f24626g, bVar.f24626g)) {
                f5 = m.f24616l;
            }
            return g8.g(valueOf, valueOf2, f5).j();
        }

        @Override // z5.m.h
        /* renamed from: o */
        public boolean f(b bVar) {
            int i8;
            String str;
            int i9;
            d dVar = this.f24627h;
            if ((dVar.f24648y0 || ((i9 = this.f24675d.F) != -1 && i9 == bVar.f24675d.F)) && (dVar.f24646w0 || ((str = this.f24675d.f11207m) != null && TextUtils.equals(str, bVar.f24675d.f11207m)))) {
                d dVar2 = this.f24627h;
                if ((dVar2.f24647x0 || ((i8 = this.f24675d.G) != -1 && i8 == bVar.f24675d.G)) && (dVar2.f24649z0 || (this.A == bVar.A && this.B == bVar.B))) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c implements Comparable<c> {

        /* renamed from: a  reason: collision with root package name */
        private final boolean f24639a;

        /* renamed from: b  reason: collision with root package name */
        private final boolean f24640b;

        public c(w0 w0Var, int i8) {
            this.f24639a = (w0Var.f11199d & 1) != 0;
            this.f24640b = m.I(i8, false);
        }

        @Override // java.lang.Comparable
        /* renamed from: c */
        public int compareTo(c cVar) {
            return com.google.common.collect.b0.k().h(this.f24640b, cVar.f24640b).h(this.f24639a, cVar.f24639a).j();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d extends y {
        public static final d G0;
        @Deprecated
        public static final d H0;
        private static final String I0;
        private static final String J0;
        private static final String K0;
        private static final String L0;
        private static final String M0;
        private static final String N0;
        private static final String O0;
        private static final String P0;
        private static final String Q0;
        private static final String R0;
        private static final String S0;
        private static final String T0;
        private static final String U0;
        private static final String V0;
        private static final String W0;
        private static final String X0;
        private static final String Y0;
        public static final g.a<d> Z0;
        public final boolean A0;
        public final boolean B0;
        public final boolean C0;
        public final boolean D0;
        private final SparseArray<Map<h5.w, e>> E0;
        private final SparseBooleanArray F0;

        /* renamed from: r0  reason: collision with root package name */
        public final boolean f24641r0;

        /* renamed from: s0  reason: collision with root package name */
        public final boolean f24642s0;

        /* renamed from: t0  reason: collision with root package name */
        public final boolean f24643t0;

        /* renamed from: u0  reason: collision with root package name */
        public final boolean f24644u0;

        /* renamed from: v0  reason: collision with root package name */
        public final boolean f24645v0;

        /* renamed from: w0  reason: collision with root package name */
        public final boolean f24646w0;

        /* renamed from: x0  reason: collision with root package name */
        public final boolean f24647x0;

        /* renamed from: y0  reason: collision with root package name */
        public final boolean f24648y0;

        /* renamed from: z0  reason: collision with root package name */
        public final boolean f24649z0;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a extends y.a {
            private boolean A;
            private boolean B;
            private boolean C;
            private boolean D;
            private boolean E;
            private boolean F;
            private boolean G;
            private boolean H;
            private boolean I;
            private boolean J;
            private boolean K;
            private boolean L;
            private boolean M;
            private final SparseArray<Map<h5.w, e>> N;
            private final SparseBooleanArray O;

            @Deprecated
            public a() {
                this.N = new SparseArray<>();
                this.O = new SparseBooleanArray();
                Z();
            }

            public a(Context context) {
                super(context);
                this.N = new SparseArray<>();
                this.O = new SparseBooleanArray();
                Z();
            }

            private a(Bundle bundle) {
                super(bundle);
                Z();
                d dVar = d.G0;
                n0(bundle.getBoolean(d.I0, dVar.f24641r0));
                i0(bundle.getBoolean(d.J0, dVar.f24642s0));
                j0(bundle.getBoolean(d.K0, dVar.f24643t0));
                h0(bundle.getBoolean(d.W0, dVar.f24644u0));
                l0(bundle.getBoolean(d.L0, dVar.f24645v0));
                e0(bundle.getBoolean(d.M0, dVar.f24646w0));
                f0(bundle.getBoolean(d.N0, dVar.f24647x0));
                c0(bundle.getBoolean(d.O0, dVar.f24648y0));
                d0(bundle.getBoolean(d.X0, dVar.f24649z0));
                k0(bundle.getBoolean(d.Y0, dVar.A0));
                m0(bundle.getBoolean(d.P0, dVar.B0));
                r0(bundle.getBoolean(d.Q0, dVar.C0));
                g0(bundle.getBoolean(d.R0, dVar.D0));
                this.N = new SparseArray<>();
                q0(bundle);
                this.O = a0(bundle.getIntArray(d.V0));
            }

            private a(d dVar) {
                super(dVar);
                this.A = dVar.f24641r0;
                this.B = dVar.f24642s0;
                this.C = dVar.f24643t0;
                this.D = dVar.f24644u0;
                this.E = dVar.f24645v0;
                this.F = dVar.f24646w0;
                this.G = dVar.f24647x0;
                this.H = dVar.f24648y0;
                this.I = dVar.f24649z0;
                this.J = dVar.A0;
                this.K = dVar.B0;
                this.L = dVar.C0;
                this.M = dVar.D0;
                this.N = Y(dVar.E0);
                this.O = dVar.F0.clone();
            }

            private static SparseArray<Map<h5.w, e>> Y(SparseArray<Map<h5.w, e>> sparseArray) {
                SparseArray<Map<h5.w, e>> sparseArray2 = new SparseArray<>();
                for (int i8 = 0; i8 < sparseArray.size(); i8++) {
                    sparseArray2.put(sparseArray.keyAt(i8), new HashMap(sparseArray.valueAt(i8)));
                }
                return sparseArray2;
            }

            private void Z() {
                this.A = true;
                this.B = false;
                this.C = true;
                this.D = false;
                this.E = true;
                this.F = false;
                this.G = false;
                this.H = false;
                this.I = false;
                this.J = true;
                this.K = true;
                this.L = false;
                this.M = true;
            }

            private SparseBooleanArray a0(int[] iArr) {
                if (iArr == null) {
                    return new SparseBooleanArray();
                }
                SparseBooleanArray sparseBooleanArray = new SparseBooleanArray(iArr.length);
                for (int i8 : iArr) {
                    sparseBooleanArray.append(i8, true);
                }
                return sparseBooleanArray;
            }

            private void q0(Bundle bundle) {
                int[] intArray = bundle.getIntArray(d.S0);
                ArrayList parcelableArrayList = bundle.getParcelableArrayList(d.T0);
                ImmutableList E = parcelableArrayList == null ? ImmutableList.E() : b6.c.b(h5.w.f20315f, parcelableArrayList);
                SparseArray sparseParcelableArray = bundle.getSparseParcelableArray(d.U0);
                SparseArray sparseArray = sparseParcelableArray == null ? new SparseArray() : b6.c.c(e.f24653h, sparseParcelableArray);
                if (intArray == null || intArray.length != E.size()) {
                    return;
                }
                for (int i8 = 0; i8 < intArray.length; i8++) {
                    p0(intArray[i8], (h5.w) E.get(i8), (e) sparseArray.get(i8));
                }
            }

            @Override // z5.y.a
            /* renamed from: X */
            public d A() {
                return new d(this);
            }

            protected a b0(y yVar) {
                super.D(yVar);
                return this;
            }

            public a c0(boolean z4) {
                this.H = z4;
                return this;
            }

            public a d0(boolean z4) {
                this.I = z4;
                return this;
            }

            public a e0(boolean z4) {
                this.F = z4;
                return this;
            }

            public a f0(boolean z4) {
                this.G = z4;
                return this;
            }

            public a g0(boolean z4) {
                this.M = z4;
                return this;
            }

            public a h0(boolean z4) {
                this.D = z4;
                return this;
            }

            public a i0(boolean z4) {
                this.B = z4;
                return this;
            }

            public a j0(boolean z4) {
                this.C = z4;
                return this;
            }

            public a k0(boolean z4) {
                this.J = z4;
                return this;
            }

            public a l0(boolean z4) {
                this.E = z4;
                return this;
            }

            public a m0(boolean z4) {
                this.K = z4;
                return this;
            }

            public a n0(boolean z4) {
                this.A = z4;
                return this;
            }

            @Override // z5.y.a
            /* renamed from: o0 */
            public a E(Context context) {
                super.E(context);
                return this;
            }

            @Deprecated
            public a p0(int i8, h5.w wVar, e eVar) {
                Map<h5.w, e> map = this.N.get(i8);
                if (map == null) {
                    map = new HashMap<>();
                    this.N.put(i8, map);
                }
                if (map.containsKey(wVar) && l0.c(map.get(wVar), eVar)) {
                    return this;
                }
                map.put(wVar, eVar);
                return this;
            }

            public a r0(boolean z4) {
                this.L = z4;
                return this;
            }

            @Override // z5.y.a
            /* renamed from: s0 */
            public a G(int i8, int i9, boolean z4) {
                super.G(i8, i9, z4);
                return this;
            }

            @Override // z5.y.a
            /* renamed from: t0 */
            public a H(Context context, boolean z4) {
                super.H(context, z4);
                return this;
            }
        }

        static {
            d A = new a().A();
            G0 = A;
            H0 = A;
            I0 = l0.r0(1000);
            J0 = l0.r0(1001);
            K0 = l0.r0(1002);
            L0 = l0.r0(1003);
            M0 = l0.r0(1004);
            N0 = l0.r0(1005);
            O0 = l0.r0(1006);
            P0 = l0.r0(1007);
            Q0 = l0.r0(1008);
            R0 = l0.r0(1009);
            S0 = l0.r0(1010);
            T0 = l0.r0(1011);
            U0 = l0.r0(1012);
            V0 = l0.r0(1013);
            W0 = l0.r0(1014);
            X0 = l0.r0(1015);
            Y0 = l0.r0(1016);
            Z0 = n.a;
        }

        private d(a aVar) {
            super(aVar);
            this.f24641r0 = aVar.A;
            this.f24642s0 = aVar.B;
            this.f24643t0 = aVar.C;
            this.f24644u0 = aVar.D;
            this.f24645v0 = aVar.E;
            this.f24646w0 = aVar.F;
            this.f24647x0 = aVar.G;
            this.f24648y0 = aVar.H;
            this.f24649z0 = aVar.I;
            this.A0 = aVar.J;
            this.B0 = aVar.K;
            this.C0 = aVar.L;
            this.D0 = aVar.M;
            this.E0 = aVar.N;
            this.F0 = aVar.O;
        }

        private static boolean E(SparseBooleanArray sparseBooleanArray, SparseBooleanArray sparseBooleanArray2) {
            int size = sparseBooleanArray.size();
            if (sparseBooleanArray2.size() != size) {
                return false;
            }
            for (int i8 = 0; i8 < size; i8++) {
                if (sparseBooleanArray2.indexOfKey(sparseBooleanArray.keyAt(i8)) < 0) {
                    return false;
                }
            }
            return true;
        }

        private static boolean F(SparseArray<Map<h5.w, e>> sparseArray, SparseArray<Map<h5.w, e>> sparseArray2) {
            int size = sparseArray.size();
            if (sparseArray2.size() != size) {
                return false;
            }
            for (int i8 = 0; i8 < size; i8++) {
                int indexOfKey = sparseArray2.indexOfKey(sparseArray.keyAt(i8));
                if (indexOfKey < 0 || !G(sparseArray.valueAt(i8), sparseArray2.valueAt(indexOfKey))) {
                    return false;
                }
            }
            return true;
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x001a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private static boolean G(java.util.Map<h5.w, z5.m.e> r4, java.util.Map<h5.w, z5.m.e> r5) {
            /*
                int r0 = r4.size()
                int r1 = r5.size()
                r2 = 0
                if (r1 == r0) goto Lc
                return r2
            Lc:
                java.util.Set r4 = r4.entrySet()
                java.util.Iterator r4 = r4.iterator()
            L14:
                boolean r0 = r4.hasNext()
                if (r0 == 0) goto L3b
                java.lang.Object r0 = r4.next()
                java.util.Map$Entry r0 = (java.util.Map.Entry) r0
                java.lang.Object r1 = r0.getKey()
                h5.w r1 = (h5.w) r1
                boolean r3 = r5.containsKey(r1)
                if (r3 == 0) goto L3a
                java.lang.Object r0 = r0.getValue()
                java.lang.Object r1 = r5.get(r1)
                boolean r0 = b6.l0.c(r0, r1)
                if (r0 != 0) goto L14
            L3a:
                return r2
            L3b:
                r4 = 1
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: z5.m.d.G(java.util.Map, java.util.Map):boolean");
        }

        public static d I(Context context) {
            return new a(context).A();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ d M(Bundle bundle) {
            return new a(bundle).A();
        }

        public a H() {
            return new a();
        }

        public boolean J(int i8) {
            return this.F0.get(i8);
        }

        @Deprecated
        public e K(int i8, h5.w wVar) {
            Map<h5.w, e> map = this.E0.get(i8);
            if (map != null) {
                return map.get(wVar);
            }
            return null;
        }

        @Deprecated
        public boolean L(int i8, h5.w wVar) {
            Map<h5.w, e> map = this.E0.get(i8);
            return map != null && map.containsKey(wVar);
        }

        @Override // z5.y
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || d.class != obj.getClass()) {
                return false;
            }
            d dVar = (d) obj;
            return super.equals(dVar) && this.f24641r0 == dVar.f24641r0 && this.f24642s0 == dVar.f24642s0 && this.f24643t0 == dVar.f24643t0 && this.f24644u0 == dVar.f24644u0 && this.f24645v0 == dVar.f24645v0 && this.f24646w0 == dVar.f24646w0 && this.f24647x0 == dVar.f24647x0 && this.f24648y0 == dVar.f24648y0 && this.f24649z0 == dVar.f24649z0 && this.A0 == dVar.A0 && this.B0 == dVar.B0 && this.C0 == dVar.C0 && this.D0 == dVar.D0 && E(this.F0, dVar.F0) && F(this.E0, dVar.E0);
        }

        @Override // z5.y
        public int hashCode() {
            return ((((((((((((((((((((((((((super.hashCode() + 31) * 31) + (this.f24641r0 ? 1 : 0)) * 31) + (this.f24642s0 ? 1 : 0)) * 31) + (this.f24643t0 ? 1 : 0)) * 31) + (this.f24644u0 ? 1 : 0)) * 31) + (this.f24645v0 ? 1 : 0)) * 31) + (this.f24646w0 ? 1 : 0)) * 31) + (this.f24647x0 ? 1 : 0)) * 31) + (this.f24648y0 ? 1 : 0)) * 31) + (this.f24649z0 ? 1 : 0)) * 31) + (this.A0 ? 1 : 0)) * 31) + (this.B0 ? 1 : 0)) * 31) + (this.C0 ? 1 : 0)) * 31) + (this.D0 ? 1 : 0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e implements com.google.android.exoplayer2.g {

        /* renamed from: e  reason: collision with root package name */
        private static final String f24650e = l0.r0(0);

        /* renamed from: f  reason: collision with root package name */
        private static final String f24651f = l0.r0(1);

        /* renamed from: g  reason: collision with root package name */
        private static final String f24652g = l0.r0(2);

        /* renamed from: h  reason: collision with root package name */
        public static final g.a<e> f24653h = o.a;

        /* renamed from: a  reason: collision with root package name */
        public final int f24654a;

        /* renamed from: b  reason: collision with root package name */
        public final int[] f24655b;

        /* renamed from: c  reason: collision with root package name */
        public final int f24656c;

        /* renamed from: d  reason: collision with root package name */
        public final int f24657d;

        public e(int i8, int[] iArr, int i9) {
            this.f24654a = i8;
            int[] copyOf = Arrays.copyOf(iArr, iArr.length);
            this.f24655b = copyOf;
            this.f24656c = iArr.length;
            this.f24657d = i9;
            Arrays.sort(copyOf);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ e b(Bundle bundle) {
            int i8 = bundle.getInt(f24650e, -1);
            int[] intArray = bundle.getIntArray(f24651f);
            int i9 = bundle.getInt(f24652g, -1);
            b6.a.a(i8 >= 0 && i9 >= 0);
            b6.a.e(intArray);
            return new e(i8, intArray, i9);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || e.class != obj.getClass()) {
                return false;
            }
            e eVar = (e) obj;
            return this.f24654a == eVar.f24654a && Arrays.equals(this.f24655b, eVar.f24655b) && this.f24657d == eVar.f24657d;
        }

        public int hashCode() {
            return (((this.f24654a * 31) + Arrays.hashCode(this.f24655b)) * 31) + this.f24657d;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f {

        /* renamed from: a  reason: collision with root package name */
        private final Spatializer f24658a;

        /* renamed from: b  reason: collision with root package name */
        private final boolean f24659b;

        /* renamed from: c  reason: collision with root package name */
        private Handler f24660c;

        /* renamed from: d  reason: collision with root package name */
        private Spatializer.OnSpatializerStateChangedListener f24661d;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a implements Spatializer.OnSpatializerStateChangedListener {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ m f24662a;

            a(f fVar, m mVar) {
                this.f24662a = mVar;
            }

            @Override // android.media.Spatializer.OnSpatializerStateChangedListener
            public void onSpatializerAvailableChanged(Spatializer spatializer, boolean z4) {
                this.f24662a.P();
            }

            @Override // android.media.Spatializer.OnSpatializerStateChangedListener
            public void onSpatializerEnabledChanged(Spatializer spatializer, boolean z4) {
                this.f24662a.P();
            }
        }

        private f(Spatializer spatializer) {
            this.f24658a = spatializer;
            this.f24659b = spatializer.getImmersiveAudioLevel() != 0;
        }

        public static f g(Context context) {
            AudioManager audioManager = (AudioManager) context.getSystemService("audio");
            if (audioManager == null) {
                return null;
            }
            return new f(audioManager.getSpatializer());
        }

        public boolean a(com.google.android.exoplayer2.audio.a aVar, w0 w0Var) {
            AudioFormat.Builder channelMask = new AudioFormat.Builder().setEncoding(2).setChannelMask(l0.G(("audio/eac3-joc".equals(w0Var.f11207m) && w0Var.F == 16) ? 12 : w0Var.F));
            int i8 = w0Var.G;
            if (i8 != -1) {
                channelMask.setSampleRate(i8);
            }
            return this.f24658a.canBeSpatialized(aVar.b().f9326a, channelMask.build());
        }

        public void b(m mVar, Looper looper) {
            if (this.f24661d == null && this.f24660c == null) {
                this.f24661d = new a(this, mVar);
                Handler handler = new Handler(looper);
                this.f24660c = handler;
                Spatializer spatializer = this.f24658a;
                Objects.requireNonNull(handler);
                spatializer.addOnSpatializerStateChangedListener(new k4.s(handler), this.f24661d);
            }
        }

        public boolean c() {
            return this.f24658a.isAvailable();
        }

        public boolean d() {
            return this.f24658a.isEnabled();
        }

        public boolean e() {
            return this.f24659b;
        }

        public void f() {
            Spatializer.OnSpatializerStateChangedListener onSpatializerStateChangedListener = this.f24661d;
            if (onSpatializerStateChangedListener == null || this.f24660c == null) {
                return;
            }
            this.f24658a.removeOnSpatializerStateChangedListener(onSpatializerStateChangedListener);
            ((Handler) l0.j(this.f24660c)).removeCallbacksAndMessages(null);
            this.f24660c = null;
            this.f24661d = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g extends h<g> implements Comparable<g> {

        /* renamed from: e  reason: collision with root package name */
        private final int f24663e;

        /* renamed from: f  reason: collision with root package name */
        private final boolean f24664f;

        /* renamed from: g  reason: collision with root package name */
        private final boolean f24665g;

        /* renamed from: h  reason: collision with root package name */
        private final boolean f24666h;

        /* renamed from: j  reason: collision with root package name */
        private final int f24667j;

        /* renamed from: k  reason: collision with root package name */
        private final int f24668k;

        /* renamed from: l  reason: collision with root package name */
        private final int f24669l;

        /* renamed from: m  reason: collision with root package name */
        private final int f24670m;

        /* renamed from: n  reason: collision with root package name */
        private final boolean f24671n;

        public g(int i8, h5.u uVar, int i9, d dVar, int i10, String str) {
            super(i8, uVar, i9);
            int i11;
            int i12 = 0;
            this.f24664f = m.I(i10, false);
            int i13 = this.f24675d.f11199d & (~dVar.A);
            this.f24665g = (i13 & 1) != 0;
            this.f24666h = (i13 & 2) != 0;
            int i14 = Integer.MAX_VALUE;
            ImmutableList<String> F = dVar.f24741y.isEmpty() ? ImmutableList.F(BuildConfig.FLAVOR) : dVar.f24741y;
            int i15 = 0;
            while (true) {
                if (i15 >= F.size()) {
                    i11 = 0;
                    break;
                }
                i11 = m.B(this.f24675d, F.get(i15), dVar.B);
                if (i11 > 0) {
                    i14 = i15;
                    break;
                }
                i15++;
            }
            this.f24667j = i14;
            this.f24668k = i11;
            int E = m.E(this.f24675d.f11200e, dVar.f24742z);
            this.f24669l = E;
            this.f24671n = (this.f24675d.f11200e & 1088) != 0;
            int B = m.B(this.f24675d, str, m.Q(str) == null);
            this.f24670m = B;
            boolean z4 = i11 > 0 || (dVar.f24741y.isEmpty() && E > 0) || this.f24665g || (this.f24666h && B > 0);
            if (m.I(i10, dVar.B0) && z4) {
                i12 = 1;
            }
            this.f24663e = i12;
        }

        public static int h(List<g> list, List<g> list2) {
            return list.get(0).compareTo(list2.get(0));
        }

        public static ImmutableList<g> j(int i8, h5.u uVar, d dVar, int[] iArr, String str) {
            ImmutableList.a u8 = ImmutableList.u();
            for (int i9 = 0; i9 < uVar.f20308a; i9++) {
                u8.a(new g(i8, uVar, i9, dVar, iArr[i9], str));
            }
            return u8.k();
        }

        @Override // z5.m.h
        public int c() {
            return this.f24663e;
        }

        @Override // java.lang.Comparable
        /* renamed from: i */
        public int compareTo(g gVar) {
            com.google.common.collect.b0 d8 = com.google.common.collect.b0.k().h(this.f24664f, gVar.f24664f).g(Integer.valueOf(this.f24667j), Integer.valueOf(gVar.f24667j), y1.c().f()).d(this.f24668k, gVar.f24668k).d(this.f24669l, gVar.f24669l).h(this.f24665g, gVar.f24665g).g(Boolean.valueOf(this.f24666h), Boolean.valueOf(gVar.f24666h), this.f24668k == 0 ? y1.c() : y1.c().f()).d(this.f24670m, gVar.f24670m);
            if (this.f24669l == 0) {
                d8 = d8.i(this.f24671n, gVar.f24671n);
            }
            return d8.j();
        }

        @Override // z5.m.h
        /* renamed from: k */
        public boolean f(g gVar) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class h<T extends h<T>> {

        /* renamed from: a  reason: collision with root package name */
        public final int f24672a;

        /* renamed from: b  reason: collision with root package name */
        public final h5.u f24673b;

        /* renamed from: c  reason: collision with root package name */
        public final int f24674c;

        /* renamed from: d  reason: collision with root package name */
        public final w0 f24675d;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface a<T extends h<T>> {
            List<T> a(int i8, h5.u uVar, int[] iArr);
        }

        public h(int i8, h5.u uVar, int i9) {
            this.f24672a = i8;
            this.f24673b = uVar;
            this.f24674c = i9;
            this.f24675d = uVar.b(i9);
        }

        public abstract int c();

        public abstract boolean f(T t8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class i extends h<i> {

        /* renamed from: e  reason: collision with root package name */
        private final boolean f24676e;

        /* renamed from: f  reason: collision with root package name */
        private final d f24677f;

        /* renamed from: g  reason: collision with root package name */
        private final boolean f24678g;

        /* renamed from: h  reason: collision with root package name */
        private final boolean f24679h;

        /* renamed from: j  reason: collision with root package name */
        private final int f24680j;

        /* renamed from: k  reason: collision with root package name */
        private final int f24681k;

        /* renamed from: l  reason: collision with root package name */
        private final int f24682l;

        /* renamed from: m  reason: collision with root package name */
        private final int f24683m;

        /* renamed from: n  reason: collision with root package name */
        private final boolean f24684n;

        /* renamed from: p  reason: collision with root package name */
        private final boolean f24685p;
        private final int q;

        /* renamed from: t  reason: collision with root package name */
        private final boolean f24686t;

        /* renamed from: w  reason: collision with root package name */
        private final boolean f24687w;

        /* renamed from: x  reason: collision with root package name */
        private final int f24688x;

        /* JADX WARN: Removed duplicated region for block: B:54:0x00a0  */
        /* JADX WARN: Removed duplicated region for block: B:62:0x00b5  */
        /* JADX WARN: Removed duplicated region for block: B:70:0x00d6  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x00d8  */
        /* JADX WARN: Removed duplicated region for block: B:75:0x00e4  */
        /* JADX WARN: Removed duplicated region for block: B:78:0x00cc A[EDGE_INSN: B:78:0x00cc->B:68:0x00cc ?: BREAK  , SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public i(int r5, h5.u r6, int r7, z5.m.d r8, int r9, int r10, boolean r11) {
            /*
                Method dump skipped, instructions count: 248
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: z5.m.i.<init>(int, h5.u, int, z5.m$d, int, int, boolean):void");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static int j(i iVar, i iVar2) {
            com.google.common.collect.b0 h8 = com.google.common.collect.b0.k().h(iVar.f24679h, iVar2.f24679h).d(iVar.f24683m, iVar2.f24683m).h(iVar.f24684n, iVar2.f24684n).h(iVar.f24676e, iVar2.f24676e).h(iVar.f24678g, iVar2.f24678g).g(Integer.valueOf(iVar.f24682l), Integer.valueOf(iVar2.f24682l), y1.c().f()).h(iVar.f24686t, iVar2.f24686t).h(iVar.f24687w, iVar2.f24687w);
            if (iVar.f24686t && iVar.f24687w) {
                h8 = h8.d(iVar.f24688x, iVar2.f24688x);
            }
            return h8.j();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static int k(i iVar, i iVar2) {
            y1 f5 = (iVar.f24676e && iVar.f24679h) ? m.f24615k : m.f24615k.f();
            return com.google.common.collect.b0.k().g(Integer.valueOf(iVar.f24680j), Integer.valueOf(iVar2.f24680j), iVar.f24677f.C ? m.f24615k.f() : m.f24616l).g(Integer.valueOf(iVar.f24681k), Integer.valueOf(iVar2.f24681k), f5).g(Integer.valueOf(iVar.f24680j), Integer.valueOf(iVar2.f24680j), f5).j();
        }

        public static int o(List<i> list, List<i> list2) {
            return com.google.common.collect.b0.k().g((i) Collections.max(list, p.a), (i) Collections.max(list2, p.a), p.a).d(list.size(), list2.size()).g((i) Collections.max(list, q.a), (i) Collections.max(list2, q.a), q.a).j();
        }

        public static ImmutableList<i> q(int i8, h5.u uVar, d dVar, int[] iArr, int i9) {
            int C = m.C(uVar, dVar.f24732j, dVar.f24733k, dVar.f24734l);
            ImmutableList.a u8 = ImmutableList.u();
            for (int i10 = 0; i10 < uVar.f20308a; i10++) {
                int f5 = uVar.b(i10).f();
                u8.a(new i(i8, uVar, i10, dVar, iArr[i10], i9, C == Integer.MAX_VALUE || (f5 != -1 && f5 <= C)));
            }
            return u8.k();
        }

        private int r(int i8, int i9) {
            if ((this.f24675d.f11200e & 16384) == 0 && m.I(i8, this.f24677f.B0)) {
                if (this.f24676e || this.f24677f.f24641r0) {
                    if (m.I(i8, false) && this.f24678g && this.f24676e && this.f24675d.f11203h != -1) {
                        d dVar = this.f24677f;
                        if (!dVar.E && !dVar.C && (i8 & i9) != 0) {
                            return 2;
                        }
                    }
                    return 1;
                }
                return 0;
            }
            return 0;
        }

        @Override // z5.m.h
        public int c() {
            return this.q;
        }

        @Override // z5.m.h
        /* renamed from: s */
        public boolean f(i iVar) {
            return (this.f24685p || l0.c(this.f24675d.f11207m, iVar.f24675d.f11207m)) && (this.f24677f.f24644u0 || (this.f24686t == iVar.f24686t && this.f24687w == iVar.f24687w));
        }
    }

    public m(Context context) {
        this(context, new a.b());
    }

    public m(Context context, r.b bVar) {
        this(context, d.I(context), bVar);
    }

    public m(Context context, y yVar, r.b bVar) {
        this(yVar, bVar, context);
    }

    private m(y yVar, r.b bVar, Context context) {
        d A;
        this.f24617d = new Object();
        this.f24618e = context != null ? context.getApplicationContext() : null;
        this.f24619f = bVar;
        if (yVar instanceof d) {
            A = (d) yVar;
        } else {
            A = (context == null ? d.G0 : d.I(context)).H().b0(yVar).A();
        }
        this.f24621h = A;
        this.f24623j = com.google.android.exoplayer2.audio.a.f9313g;
        boolean z4 = context != null && l0.x0(context);
        this.f24620g = z4;
        if (!z4 && context != null && l0.f8063a >= 32) {
            this.f24622i = f.g(context);
        }
        if (this.f24621h.A0 && context == null) {
            p.i("DefaultTrackSelector", "Audio channel count constraints cannot be applied without reference to Context. Build the track selector instance with one of the non-deprecated constructors that take a Context argument.");
        }
    }

    private static void A(h5.w wVar, y yVar, Map<Integer, w> map) {
        w wVar2;
        for (int i8 = 0; i8 < wVar.f20316a; i8++) {
            w wVar3 = yVar.F.get(wVar.b(i8));
            if (wVar3 != null && ((wVar2 = map.get(Integer.valueOf(wVar3.b()))) == null || (wVar2.f24706b.isEmpty() && !wVar3.f24706b.isEmpty()))) {
                map.put(Integer.valueOf(wVar3.b()), wVar3);
            }
        }
    }

    protected static int B(w0 w0Var, String str, boolean z4) {
        if (TextUtils.isEmpty(str) || !str.equals(w0Var.f11198c)) {
            String Q = Q(str);
            String Q2 = Q(w0Var.f11198c);
            if (Q2 == null || Q == null) {
                return (z4 && Q2 == null) ? 1 : 0;
            } else if (Q2.startsWith(Q) || Q.startsWith(Q2)) {
                return 3;
            } else {
                return l0.S0(Q2, "-")[0].equals(l0.S0(Q, "-")[0]) ? 2 : 0;
            }
        }
        return 4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int C(h5.u uVar, int i8, int i9, boolean z4) {
        int i10;
        int i11 = Integer.MAX_VALUE;
        if (i8 != Integer.MAX_VALUE && i9 != Integer.MAX_VALUE) {
            for (int i12 = 0; i12 < uVar.f20308a; i12++) {
                w0 b9 = uVar.b(i12);
                int i13 = b9.f11211w;
                if (i13 > 0 && (i10 = b9.f11212x) > 0) {
                    Point D = D(z4, i8, i9, i13, i10);
                    int i14 = b9.f11211w;
                    int i15 = b9.f11212x;
                    int i16 = i14 * i15;
                    if (i14 >= ((int) (D.x * 0.98f)) && i15 >= ((int) (D.y * 0.98f)) && i16 < i11) {
                        i11 = i16;
                    }
                }
            }
        }
        return i11;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x000d, code lost:
        if ((r6 > r7) != (r4 > r5)) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.graphics.Point D(boolean r3, int r4, int r5, int r6, int r7) {
        /*
            if (r3 == 0) goto L10
            r3 = 1
            r0 = 0
            if (r6 <= r7) goto L8
            r1 = r3
            goto L9
        L8:
            r1 = r0
        L9:
            if (r4 <= r5) goto Lc
            goto Ld
        Lc:
            r3 = r0
        Ld:
            if (r1 == r3) goto L10
            goto L13
        L10:
            r2 = r5
            r5 = r4
            r4 = r2
        L13:
            int r3 = r6 * r4
            int r0 = r7 * r5
            if (r3 < r0) goto L23
            android.graphics.Point r3 = new android.graphics.Point
            int r4 = b6.l0.l(r0, r6)
            r3.<init>(r5, r4)
            return r3
        L23:
            android.graphics.Point r5 = new android.graphics.Point
            int r3 = b6.l0.l(r3, r7)
            r5.<init>(r3, r4)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: z5.m.D(boolean, int, int, int, int):android.graphics.Point");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int E(int i8, int i9) {
        if (i8 == 0 || i8 != i9) {
            return Integer.bitCount(i8 & i9);
        }
        return Integer.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int F(String str) {
        if (str == null) {
            return 0;
        }
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1851077871:
                if (str.equals("video/dolby-vision")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1662735862:
                if (str.equals("video/av01")) {
                    c9 = 1;
                    break;
                }
                break;
            case -1662541442:
                if (str.equals("video/hevc")) {
                    c9 = 2;
                    break;
                }
                break;
            case 1331836730:
                if (str.equals("video/avc")) {
                    c9 = 3;
                    break;
                }
                break;
            case 1599127257:
                if (str.equals("video/x-vnd.on2.vp9")) {
                    c9 = 4;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return 5;
            case 1:
                return 4;
            case 2:
                return 3;
            case 3:
                return 1;
            case 4:
                return 2;
            default:
                return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean G(w0 w0Var) {
        boolean z4;
        f fVar;
        f fVar2;
        synchronized (this.f24617d) {
            z4 = !this.f24621h.A0 || this.f24620g || w0Var.F <= 2 || (H(w0Var) && (l0.f8063a < 32 || (fVar2 = this.f24622i) == null || !fVar2.e())) || (l0.f8063a >= 32 && (fVar = this.f24622i) != null && fVar.e() && this.f24622i.c() && this.f24622i.d() && this.f24622i.a(this.f24623j, w0Var));
        }
        return z4;
    }

    private static boolean H(w0 w0Var) {
        String str = w0Var.f11207m;
        if (str == null) {
            return false;
        }
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -2123537834:
                if (str.equals("audio/eac3-joc")) {
                    c9 = 0;
                    break;
                }
                break;
            case 187078296:
                if (str.equals("audio/ac3")) {
                    c9 = 1;
                    break;
                }
                break;
            case 187078297:
                if (str.equals("audio/ac4")) {
                    c9 = 2;
                    break;
                }
                break;
            case 1504578661:
                if (str.equals("audio/eac3")) {
                    c9 = 3;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
            case 1:
            case 2:
            case 3:
                return true;
            default:
                return false;
        }
    }

    protected static boolean I(int i8, boolean z4) {
        int F = f0.F(i8);
        return F == 4 || (z4 && F == 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ List J(d dVar, boolean z4, int i8, h5.u uVar, int[] iArr) {
        return b.j(i8, uVar, dVar, iArr, z4, new z5.d(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ List K(d dVar, String str, int i8, h5.u uVar, int[] iArr) {
        return g.j(i8, uVar, dVar, iArr, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ List L(d dVar, int[] iArr, int i8, h5.u uVar, int[] iArr2) {
        return i.q(i8, uVar, dVar, iArr2, iArr[i8]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int M(Integer num, Integer num2) {
        if (num.intValue() == -1) {
            return num2.intValue() == -1 ? 0 : -1;
        } else if (num2.intValue() == -1) {
            return 1;
        } else {
            return num.intValue() - num2.intValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int N(Integer num, Integer num2) {
        return 0;
    }

    private static void O(t.a aVar, int[][][] iArr, g0[] g0VarArr, r[] rVarArr) {
        boolean z4;
        boolean z8 = false;
        int i8 = -1;
        int i9 = -1;
        for (int i10 = 0; i10 < aVar.d(); i10++) {
            int e8 = aVar.e(i10);
            r rVar = rVarArr[i10];
            if ((e8 == 1 || e8 == 2) && rVar != null && R(iArr[i10], aVar.f(i10), rVar)) {
                if (e8 == 1) {
                    if (i9 != -1) {
                        z4 = false;
                        break;
                    }
                    i9 = i10;
                } else if (i8 != -1) {
                    z4 = false;
                    break;
                } else {
                    i8 = i10;
                }
            }
        }
        z4 = true;
        if (i9 != -1 && i8 != -1) {
            z8 = true;
        }
        if (z4 && z8) {
            g0 g0Var = new g0(true);
            g0VarArr[i9] = g0Var;
            g0VarArr[i8] = g0Var;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void P() {
        boolean z4;
        f fVar;
        synchronized (this.f24617d) {
            z4 = this.f24621h.A0 && !this.f24620g && l0.f8063a >= 32 && (fVar = this.f24622i) != null && fVar.e();
        }
        if (z4) {
            c();
        }
    }

    protected static String Q(String str) {
        if (TextUtils.isEmpty(str) || TextUtils.equals(str, "und")) {
            return null;
        }
        return str;
    }

    private static boolean R(int[][] iArr, h5.w wVar, r rVar) {
        if (rVar == null) {
            return false;
        }
        int c9 = wVar.c(rVar.b());
        for (int i8 = 0; i8 < rVar.length(); i8++) {
            if (f0.p(iArr[c9][rVar.j(i8)]) != 32) {
                return false;
            }
        }
        return true;
    }

    private <T extends h<T>> Pair<r.a, Integer> W(int i8, t.a aVar, int[][][] iArr, h.a<T> aVar2, Comparator<List<T>> comparator) {
        int i9;
        RandomAccess randomAccess;
        t.a aVar3 = aVar;
        ArrayList arrayList = new ArrayList();
        int d8 = aVar.d();
        int i10 = 0;
        while (i10 < d8) {
            if (i8 == aVar3.e(i10)) {
                h5.w f5 = aVar3.f(i10);
                for (int i11 = 0; i11 < f5.f20316a; i11++) {
                    h5.u b9 = f5.b(i11);
                    List<T> a9 = aVar2.a(i10, b9, iArr[i10][i11]);
                    boolean[] zArr = new boolean[b9.f20308a];
                    int i12 = 0;
                    while (i12 < b9.f20308a) {
                        T t8 = a9.get(i12);
                        int c9 = t8.c();
                        if (zArr[i12] || c9 == 0) {
                            i9 = d8;
                        } else {
                            if (c9 == 1) {
                                randomAccess = ImmutableList.F(t8);
                                i9 = d8;
                            } else {
                                ArrayList arrayList2 = new ArrayList();
                                arrayList2.add(t8);
                                int i13 = i12 + 1;
                                while (i13 < b9.f20308a) {
                                    T t9 = a9.get(i13);
                                    int i14 = d8;
                                    if (t9.c() == 2 && t8.f(t9)) {
                                        arrayList2.add(t9);
                                        zArr[i13] = true;
                                    }
                                    i13++;
                                    d8 = i14;
                                }
                                i9 = d8;
                                randomAccess = arrayList2;
                            }
                            arrayList.add(randomAccess);
                        }
                        i12++;
                        d8 = i9;
                    }
                }
            }
            i10++;
            aVar3 = aVar;
            d8 = d8;
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        List list = (List) Collections.max(arrayList, comparator);
        int[] iArr2 = new int[list.size()];
        for (int i15 = 0; i15 < list.size(); i15++) {
            iArr2[i15] = ((h) list.get(i15)).f24674c;
        }
        h hVar = (h) list.get(0);
        return Pair.create(new r.a(hVar.f24673b, iArr2), Integer.valueOf(hVar.f24672a));
    }

    private static void y(t.a aVar, d dVar, r.a[] aVarArr) {
        int d8 = aVar.d();
        for (int i8 = 0; i8 < d8; i8++) {
            h5.w f5 = aVar.f(i8);
            if (dVar.L(i8, f5)) {
                e K = dVar.K(i8, f5);
                aVarArr[i8] = (K == null || K.f24655b.length == 0) ? null : new r.a(f5.b(K.f24654a), K.f24655b, K.f24657d);
            }
        }
    }

    private static void z(t.a aVar, y yVar, r.a[] aVarArr) {
        int d8 = aVar.d();
        HashMap hashMap = new HashMap();
        for (int i8 = 0; i8 < d8; i8++) {
            A(aVar.f(i8), yVar, hashMap);
        }
        A(aVar.h(), yVar, hashMap);
        for (int i9 = 0; i9 < d8; i9++) {
            w wVar = (w) hashMap.get(Integer.valueOf(aVar.e(i9)));
            if (wVar != null) {
                aVarArr[i9] = (wVar.f24706b.isEmpty() || aVar.f(i9).c(wVar.f24705a) == -1) ? null : new r.a(wVar.f24705a, com.google.common.primitives.g.l(wVar.f24706b));
            }
        }
    }

    protected r.a[] S(t.a aVar, int[][][] iArr, int[] iArr2, d dVar) {
        String str;
        int d8 = aVar.d();
        r.a[] aVarArr = new r.a[d8];
        Pair<r.a, Integer> X = X(aVar, iArr, iArr2, dVar);
        if (X != null) {
            aVarArr[((Integer) X.second).intValue()] = (r.a) X.first;
        }
        Pair<r.a, Integer> T = T(aVar, iArr, iArr2, dVar);
        if (T != null) {
            aVarArr[((Integer) T.second).intValue()] = (r.a) T.first;
        }
        if (T == null) {
            str = null;
        } else {
            Object obj = T.first;
            str = ((r.a) obj).f24689a.b(((r.a) obj).f24690b[0]).f11198c;
        }
        Pair<r.a, Integer> V = V(aVar, iArr, dVar, str);
        if (V != null) {
            aVarArr[((Integer) V.second).intValue()] = (r.a) V.first;
        }
        for (int i8 = 0; i8 < d8; i8++) {
            int e8 = aVar.e(i8);
            if (e8 != 2 && e8 != 1 && e8 != 3) {
                aVarArr[i8] = U(e8, aVar.f(i8), iArr[i8], dVar);
            }
        }
        return aVarArr;
    }

    protected Pair<r.a, Integer> T(t.a aVar, int[][][] iArr, int[] iArr2, d dVar) {
        boolean z4 = false;
        int i8 = 0;
        while (true) {
            if (i8 < aVar.d()) {
                if (2 == aVar.e(i8) && aVar.f(i8).f20316a > 0) {
                    z4 = true;
                    break;
                }
                i8++;
            } else {
                break;
            }
        }
        return W(1, aVar, iArr, new l(this, dVar, z4), z5.g.a);
    }

    protected r.a U(int i8, h5.w wVar, int[][] iArr, d dVar) {
        h5.u uVar = null;
        c cVar = null;
        int i9 = 0;
        for (int i10 = 0; i10 < wVar.f20316a; i10++) {
            h5.u b9 = wVar.b(i10);
            int[] iArr2 = iArr[i10];
            for (int i11 = 0; i11 < b9.f20308a; i11++) {
                if (I(iArr2[i11], dVar.B0)) {
                    c cVar2 = new c(b9.b(i11), iArr2[i11]);
                    if (cVar == null || cVar2.compareTo(cVar) > 0) {
                        uVar = b9;
                        i9 = i11;
                        cVar = cVar2;
                    }
                }
            }
        }
        if (uVar == null) {
            return null;
        }
        return new r.a(uVar, i9);
    }

    protected Pair<r.a, Integer> V(t.a aVar, int[][][] iArr, d dVar, String str) {
        return W(3, aVar, iArr, new j(dVar, str), z5.h.a);
    }

    protected Pair<r.a, Integer> X(t.a aVar, int[][][] iArr, int[] iArr2, d dVar) {
        return W(2, aVar, iArr, new k(dVar, iArr2), z5.i.a);
    }

    @Override // z5.a0
    public boolean d() {
        return true;
    }

    @Override // z5.a0
    public void f() {
        f fVar;
        synchronized (this.f24617d) {
            if (l0.f8063a >= 32 && (fVar = this.f24622i) != null) {
                fVar.f();
            }
        }
        super.f();
    }

    @Override // z5.a0
    public void h(com.google.android.exoplayer2.audio.a aVar) {
        boolean z4;
        synchronized (this.f24617d) {
            z4 = !this.f24623j.equals(aVar);
            this.f24623j = aVar;
        }
        if (z4) {
            P();
        }
    }

    @Override // z5.t
    protected final Pair<g0[], r[]> l(t.a aVar, int[][][] iArr, int[] iArr2, k.b bVar, h2 h2Var) {
        d dVar;
        f fVar;
        synchronized (this.f24617d) {
            dVar = this.f24621h;
            if (dVar.A0 && l0.f8063a >= 32 && (fVar = this.f24622i) != null) {
                fVar.b(this, (Looper) b6.a.h(Looper.myLooper()));
            }
        }
        int d8 = aVar.d();
        r.a[] S = S(aVar, iArr, iArr2, dVar);
        z(aVar, dVar, S);
        y(aVar, dVar, S);
        for (int i8 = 0; i8 < d8; i8++) {
            int e8 = aVar.e(i8);
            if (dVar.J(i8) || dVar.G.contains(Integer.valueOf(e8))) {
                S[i8] = null;
            }
        }
        r[] a9 = this.f24619f.a(S, a(), bVar, h2Var);
        g0[] g0VarArr = new g0[d8];
        for (int i9 = 0; i9 < d8; i9++) {
            boolean z4 = true;
            if ((dVar.J(i9) || dVar.G.contains(Integer.valueOf(aVar.e(i9)))) || (aVar.e(i9) != -2 && a9[i9] == null)) {
                z4 = false;
            }
            g0VarArr[i9] = z4 ? g0.f20500b : null;
        }
        if (dVar.C0) {
            O(aVar, iArr, g0VarArr, a9);
        }
        return Pair.create(g0VarArr, a9);
    }
}
