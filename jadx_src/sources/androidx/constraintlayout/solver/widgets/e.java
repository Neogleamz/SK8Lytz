package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e extends h {

    /* renamed from: s1  reason: collision with root package name */
    private ConstraintWidget[] f3841s1;
    private int V0 = -1;
    private int W0 = -1;
    private int X0 = -1;
    private int Y0 = -1;
    private int Z0 = -1;

    /* renamed from: a1  reason: collision with root package name */
    private int f3823a1 = -1;

    /* renamed from: b1  reason: collision with root package name */
    private float f3824b1 = 0.5f;

    /* renamed from: c1  reason: collision with root package name */
    private float f3825c1 = 0.5f;

    /* renamed from: d1  reason: collision with root package name */
    private float f3826d1 = 0.5f;

    /* renamed from: e1  reason: collision with root package name */
    private float f3827e1 = 0.5f;

    /* renamed from: f1  reason: collision with root package name */
    private float f3828f1 = 0.5f;

    /* renamed from: g1  reason: collision with root package name */
    private float f3829g1 = 0.5f;

    /* renamed from: h1  reason: collision with root package name */
    private int f3830h1 = 0;

    /* renamed from: i1  reason: collision with root package name */
    private int f3831i1 = 0;

    /* renamed from: j1  reason: collision with root package name */
    private int f3832j1 = 2;

    /* renamed from: k1  reason: collision with root package name */
    private int f3833k1 = 2;

    /* renamed from: l1  reason: collision with root package name */
    private int f3834l1 = 0;

    /* renamed from: m1  reason: collision with root package name */
    private int f3835m1 = -1;

    /* renamed from: n1  reason: collision with root package name */
    private int f3836n1 = 0;

    /* renamed from: o1  reason: collision with root package name */
    private ArrayList<a> f3837o1 = new ArrayList<>();

    /* renamed from: p1  reason: collision with root package name */
    private ConstraintWidget[] f3838p1 = null;

    /* renamed from: q1  reason: collision with root package name */
    private ConstraintWidget[] f3839q1 = null;

    /* renamed from: r1  reason: collision with root package name */
    private int[] f3840r1 = null;

    /* renamed from: t1  reason: collision with root package name */
    private int f3842t1 = 0;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a {

        /* renamed from: a  reason: collision with root package name */
        private int f3843a;

        /* renamed from: d  reason: collision with root package name */
        private ConstraintAnchor f3846d;

        /* renamed from: e  reason: collision with root package name */
        private ConstraintAnchor f3847e;

        /* renamed from: f  reason: collision with root package name */
        private ConstraintAnchor f3848f;

        /* renamed from: g  reason: collision with root package name */
        private ConstraintAnchor f3849g;

        /* renamed from: h  reason: collision with root package name */
        private int f3850h;

        /* renamed from: i  reason: collision with root package name */
        private int f3851i;

        /* renamed from: j  reason: collision with root package name */
        private int f3852j;

        /* renamed from: k  reason: collision with root package name */
        private int f3853k;
        private int q;

        /* renamed from: b  reason: collision with root package name */
        private ConstraintWidget f3844b = null;

        /* renamed from: c  reason: collision with root package name */
        int f3845c = 0;

        /* renamed from: l  reason: collision with root package name */
        private int f3854l = 0;

        /* renamed from: m  reason: collision with root package name */
        private int f3855m = 0;

        /* renamed from: n  reason: collision with root package name */
        private int f3856n = 0;

        /* renamed from: o  reason: collision with root package name */
        private int f3857o = 0;

        /* renamed from: p  reason: collision with root package name */
        private int f3858p = 0;

        public a(int i8, ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, ConstraintAnchor constraintAnchor3, ConstraintAnchor constraintAnchor4, int i9) {
            this.f3843a = 0;
            this.f3850h = 0;
            this.f3851i = 0;
            this.f3852j = 0;
            this.f3853k = 0;
            this.q = 0;
            this.f3843a = i8;
            this.f3846d = constraintAnchor;
            this.f3847e = constraintAnchor2;
            this.f3848f = constraintAnchor3;
            this.f3849g = constraintAnchor4;
            this.f3850h = e.this.Q0();
            this.f3851i = e.this.S0();
            this.f3852j = e.this.R0();
            this.f3853k = e.this.P0();
            this.q = i9;
        }

        private void h() {
            this.f3854l = 0;
            this.f3855m = 0;
            this.f3844b = null;
            this.f3845c = 0;
            int i8 = this.f3857o;
            for (int i9 = 0; i9 < i8 && this.f3856n + i9 < e.this.f3842t1; i9++) {
                ConstraintWidget constraintWidget = e.this.f3841s1[this.f3856n + i9];
                if (this.f3843a == 0) {
                    int Q = constraintWidget.Q();
                    int i10 = e.this.f3830h1;
                    if (constraintWidget.P() == 8) {
                        i10 = 0;
                    }
                    this.f3854l += Q + i10;
                    int B1 = e.this.B1(constraintWidget, this.q);
                    if (this.f3844b == null || this.f3845c < B1) {
                        this.f3844b = constraintWidget;
                        this.f3845c = B1;
                        this.f3855m = B1;
                    }
                } else {
                    int C1 = e.this.C1(constraintWidget, this.q);
                    int B12 = e.this.B1(constraintWidget, this.q);
                    int i11 = e.this.f3831i1;
                    if (constraintWidget.P() == 8) {
                        i11 = 0;
                    }
                    this.f3855m += B12 + i11;
                    if (this.f3844b == null || this.f3845c < C1) {
                        this.f3844b = constraintWidget;
                        this.f3845c = C1;
                        this.f3854l = C1;
                    }
                }
            }
        }

        public void b(ConstraintWidget constraintWidget) {
            if (this.f3843a == 0) {
                int C1 = e.this.C1(constraintWidget, this.q);
                if (constraintWidget.z() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    this.f3858p++;
                    C1 = 0;
                }
                this.f3854l += C1 + (constraintWidget.P() != 8 ? e.this.f3830h1 : 0);
                int B1 = e.this.B1(constraintWidget, this.q);
                if (this.f3844b == null || this.f3845c < B1) {
                    this.f3844b = constraintWidget;
                    this.f3845c = B1;
                    this.f3855m = B1;
                }
            } else {
                int C12 = e.this.C1(constraintWidget, this.q);
                int B12 = e.this.B1(constraintWidget, this.q);
                if (constraintWidget.N() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    this.f3858p++;
                    B12 = 0;
                }
                this.f3855m += B12 + (constraintWidget.P() != 8 ? e.this.f3831i1 : 0);
                if (this.f3844b == null || this.f3845c < C12) {
                    this.f3844b = constraintWidget;
                    this.f3845c = C12;
                    this.f3854l = C12;
                }
            }
            this.f3857o++;
        }

        public void c() {
            this.f3845c = 0;
            this.f3844b = null;
            this.f3854l = 0;
            this.f3855m = 0;
            this.f3856n = 0;
            this.f3857o = 0;
            this.f3858p = 0;
        }

        /* JADX WARN: Removed duplicated region for block: B:136:0x024c  */
        /* JADX WARN: Removed duplicated region for block: B:148:0x029a  */
        /* JADX WARN: Removed duplicated region for block: B:150:0x02a5  */
        /* JADX WARN: Removed duplicated region for block: B:157:0x02d0  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void d(boolean r17, int r18, boolean r19) {
            /*
                Method dump skipped, instructions count: 812
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.e.a.d(boolean, int, boolean):void");
        }

        public int e() {
            return this.f3843a == 1 ? this.f3855m - e.this.f3831i1 : this.f3855m;
        }

        public int f() {
            return this.f3843a == 0 ? this.f3854l - e.this.f3830h1 : this.f3854l;
        }

        public void g(int i8) {
            e eVar;
            ConstraintWidget.DimensionBehaviour z4;
            int Q;
            ConstraintWidget.DimensionBehaviour dimensionBehaviour;
            int i9;
            int i10 = this.f3858p;
            if (i10 == 0) {
                return;
            }
            int i11 = this.f3857o;
            int i12 = i8 / i10;
            for (int i13 = 0; i13 < i11 && this.f3856n + i13 < e.this.f3842t1; i13++) {
                ConstraintWidget constraintWidget = e.this.f3841s1[this.f3856n + i13];
                if (this.f3843a == 0) {
                    if (constraintWidget != null && constraintWidget.z() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.f3686l == 0) {
                        eVar = e.this;
                        z4 = ConstraintWidget.DimensionBehaviour.FIXED;
                        dimensionBehaviour = constraintWidget.N();
                        i9 = constraintWidget.w();
                        Q = i12;
                        eVar.U0(constraintWidget, z4, Q, dimensionBehaviour, i9);
                    }
                } else {
                    if (constraintWidget != null && constraintWidget.N() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.f3688m == 0) {
                        eVar = e.this;
                        z4 = constraintWidget.z();
                        Q = constraintWidget.Q();
                        dimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
                        i9 = i12;
                        eVar.U0(constraintWidget, z4, Q, dimensionBehaviour, i9);
                    }
                }
            }
            h();
        }

        public void i(int i8) {
            this.f3856n = i8;
        }

        public void j(int i8, ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, ConstraintAnchor constraintAnchor3, ConstraintAnchor constraintAnchor4, int i9, int i10, int i11, int i12, int i13) {
            this.f3843a = i8;
            this.f3846d = constraintAnchor;
            this.f3847e = constraintAnchor2;
            this.f3848f = constraintAnchor3;
            this.f3849g = constraintAnchor4;
            this.f3850h = i9;
            this.f3851i = i10;
            this.f3852j = i11;
            this.f3853k = i12;
            this.q = i13;
        }
    }

    private void A1(boolean z4) {
        ConstraintWidget constraintWidget;
        if (this.f3840r1 == null || this.f3839q1 == null || this.f3838p1 == null) {
            return;
        }
        for (int i8 = 0; i8 < this.f3842t1; i8++) {
            this.f3841s1[i8].a0();
        }
        int[] iArr = this.f3840r1;
        int i9 = iArr[0];
        int i10 = iArr[1];
        ConstraintWidget constraintWidget2 = null;
        for (int i11 = 0; i11 < i9; i11++) {
            ConstraintWidget constraintWidget3 = this.f3839q1[z4 ? (i9 - i11) - 1 : i11];
            if (constraintWidget3 != null && constraintWidget3.P() != 8) {
                if (i11 == 0) {
                    constraintWidget3.j(constraintWidget3.D, this.D, Q0());
                    constraintWidget3.k0(this.V0);
                    constraintWidget3.j0(this.f3824b1);
                }
                if (i11 == i9 - 1) {
                    constraintWidget3.j(constraintWidget3.F, this.F, R0());
                }
                if (i11 > 0) {
                    constraintWidget3.j(constraintWidget3.D, constraintWidget2.F, this.f3830h1);
                    constraintWidget2.j(constraintWidget2.F, constraintWidget3.D, 0);
                }
                constraintWidget2 = constraintWidget3;
            }
        }
        for (int i12 = 0; i12 < i10; i12++) {
            ConstraintWidget constraintWidget4 = this.f3838p1[i12];
            if (constraintWidget4 != null && constraintWidget4.P() != 8) {
                if (i12 == 0) {
                    constraintWidget4.j(constraintWidget4.E, this.E, S0());
                    constraintWidget4.z0(this.W0);
                    constraintWidget4.y0(this.f3825c1);
                }
                if (i12 == i10 - 1) {
                    constraintWidget4.j(constraintWidget4.G, this.G, P0());
                }
                if (i12 > 0) {
                    constraintWidget4.j(constraintWidget4.E, constraintWidget2.G, this.f3831i1);
                    constraintWidget2.j(constraintWidget2.G, constraintWidget4.E, 0);
                }
                constraintWidget2 = constraintWidget4;
            }
        }
        for (int i13 = 0; i13 < i9; i13++) {
            for (int i14 = 0; i14 < i10; i14++) {
                int i15 = (i14 * i9) + i13;
                if (this.f3836n1 == 1) {
                    i15 = (i13 * i10) + i14;
                }
                ConstraintWidget[] constraintWidgetArr = this.f3841s1;
                if (i15 < constraintWidgetArr.length && (constraintWidget = constraintWidgetArr[i15]) != null && constraintWidget.P() != 8) {
                    ConstraintWidget constraintWidget5 = this.f3839q1[i13];
                    ConstraintWidget constraintWidget6 = this.f3838p1[i14];
                    if (constraintWidget != constraintWidget5) {
                        constraintWidget.j(constraintWidget.D, constraintWidget5.D, 0);
                        constraintWidget.j(constraintWidget.F, constraintWidget5.F, 0);
                    }
                    if (constraintWidget != constraintWidget6) {
                        constraintWidget.j(constraintWidget.E, constraintWidget6.E, 0);
                        constraintWidget.j(constraintWidget.G, constraintWidget6.G, 0);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int B1(ConstraintWidget constraintWidget, int i8) {
        if (constraintWidget == null) {
            return 0;
        }
        if (constraintWidget.N() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            int i9 = constraintWidget.f3688m;
            if (i9 == 0) {
                return 0;
            }
            if (i9 == 2) {
                int i10 = (int) (constraintWidget.f3701t * i8);
                if (i10 != constraintWidget.w()) {
                    U0(constraintWidget, constraintWidget.z(), constraintWidget.Q(), ConstraintWidget.DimensionBehaviour.FIXED, i10);
                }
                return i10;
            } else if (i9 == 1) {
                return constraintWidget.w();
            } else {
                if (i9 == 3) {
                    return (int) ((constraintWidget.Q() * constraintWidget.S) + 0.5f);
                }
            }
        }
        return constraintWidget.w();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int C1(ConstraintWidget constraintWidget, int i8) {
        if (constraintWidget == null) {
            return 0;
        }
        if (constraintWidget.z() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            int i9 = constraintWidget.f3686l;
            if (i9 == 0) {
                return 0;
            }
            if (i9 == 2) {
                int i10 = (int) (constraintWidget.q * i8);
                if (i10 != constraintWidget.Q()) {
                    U0(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, i10, constraintWidget.N(), constraintWidget.w());
                }
                return i10;
            } else if (i9 == 1) {
                return constraintWidget.Q();
            } else {
                if (i9 == 3) {
                    return (int) ((constraintWidget.w() * constraintWidget.S) + 0.5f);
                }
            }
        }
        return constraintWidget.Q();
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x0066  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:103:0x0119 -> B:40:0x0061). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:104:0x011b -> B:40:0x0061). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:106:0x0121 -> B:40:0x0061). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:107:0x0123 -> B:40:0x0061). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void D1(androidx.constraintlayout.solver.widgets.ConstraintWidget[] r17, int r18, int r19, int r20, int[] r21) {
        /*
            Method dump skipped, instructions count: 304
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.e.D1(androidx.constraintlayout.solver.widgets.ConstraintWidget[], int, int, int, int[]):void");
    }

    private void E1(ConstraintWidget[] constraintWidgetArr, int i8, int i9, int i10, int[] iArr) {
        int i11;
        int i12;
        ConstraintAnchor constraintAnchor;
        int R0;
        ConstraintAnchor constraintAnchor2;
        int P0;
        int i13;
        if (i8 == 0) {
            return;
        }
        this.f3837o1.clear();
        a aVar = new a(i9, this.D, this.E, this.F, this.G, i10);
        this.f3837o1.add(aVar);
        int i14 = 0;
        int i15 = 0;
        int i16 = 0;
        if (i9 == 0) {
            while (i16 < i8) {
                ConstraintWidget constraintWidget = constraintWidgetArr[i16];
                int C1 = C1(constraintWidget, i10);
                if (constraintWidget.z() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    i14++;
                }
                int i17 = i14;
                boolean z4 = (i15 == i10 || (this.f3830h1 + i15) + C1 > i10) && aVar.f3844b != null;
                if (!z4 && i16 > 0 && (i13 = this.f3835m1) > 0 && i16 % i13 == 0) {
                    z4 = true;
                }
                if (z4) {
                    aVar = new a(i9, this.D, this.E, this.F, this.G, i10);
                    aVar.i(i16);
                    this.f3837o1.add(aVar);
                } else if (i16 > 0) {
                    i15 += this.f3830h1 + C1;
                    aVar.b(constraintWidget);
                    i16++;
                    i14 = i17;
                }
                i15 = C1;
                aVar.b(constraintWidget);
                i16++;
                i14 = i17;
            }
        } else {
            while (i16 < i8) {
                ConstraintWidget constraintWidget2 = constraintWidgetArr[i16];
                int B1 = B1(constraintWidget2, i10);
                if (constraintWidget2.N() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    i14++;
                }
                int i18 = i14;
                boolean z8 = (i15 == i10 || (this.f3831i1 + i15) + B1 > i10) && aVar.f3844b != null;
                if (!z8 && i16 > 0 && (i11 = this.f3835m1) > 0 && i16 % i11 == 0) {
                    z8 = true;
                }
                if (z8) {
                    aVar = new a(i9, this.D, this.E, this.F, this.G, i10);
                    aVar.i(i16);
                    this.f3837o1.add(aVar);
                } else if (i16 > 0) {
                    i15 += this.f3831i1 + B1;
                    aVar.b(constraintWidget2);
                    i16++;
                    i14 = i18;
                }
                i15 = B1;
                aVar.b(constraintWidget2);
                i16++;
                i14 = i18;
            }
        }
        int size = this.f3837o1.size();
        ConstraintAnchor constraintAnchor3 = this.D;
        ConstraintAnchor constraintAnchor4 = this.E;
        ConstraintAnchor constraintAnchor5 = this.F;
        ConstraintAnchor constraintAnchor6 = this.G;
        int Q0 = Q0();
        int S0 = S0();
        int R02 = R0();
        int P02 = P0();
        ConstraintWidget.DimensionBehaviour z9 = z();
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        boolean z10 = z9 == dimensionBehaviour || N() == dimensionBehaviour;
        if (i14 > 0 && z10) {
            for (int i19 = 0; i19 < size; i19++) {
                a aVar2 = this.f3837o1.get(i19);
                aVar2.g(i10 - (i9 == 0 ? aVar2.f() : aVar2.e()));
            }
        }
        int i20 = S0;
        int i21 = R02;
        int i22 = 0;
        int i23 = 0;
        int i24 = 0;
        int i25 = Q0;
        ConstraintAnchor constraintAnchor7 = constraintAnchor4;
        ConstraintAnchor constraintAnchor8 = constraintAnchor3;
        int i26 = P02;
        while (i24 < size) {
            a aVar3 = this.f3837o1.get(i24);
            if (i9 == 0) {
                if (i24 < size - 1) {
                    constraintAnchor2 = this.f3837o1.get(i24 + 1).f3844b.E;
                    P0 = 0;
                } else {
                    constraintAnchor2 = this.G;
                    P0 = P0();
                }
                ConstraintAnchor constraintAnchor9 = aVar3.f3844b.G;
                ConstraintAnchor constraintAnchor10 = constraintAnchor8;
                ConstraintAnchor constraintAnchor11 = constraintAnchor8;
                int i27 = i22;
                ConstraintAnchor constraintAnchor12 = constraintAnchor7;
                int i28 = i23;
                ConstraintAnchor constraintAnchor13 = constraintAnchor5;
                ConstraintAnchor constraintAnchor14 = constraintAnchor5;
                i12 = i24;
                aVar3.j(i9, constraintAnchor10, constraintAnchor12, constraintAnchor13, constraintAnchor2, i25, i20, i21, P0, i10);
                int max = Math.max(i28, aVar3.f());
                i22 = i27 + aVar3.e();
                if (i12 > 0) {
                    i22 += this.f3831i1;
                }
                constraintAnchor8 = constraintAnchor11;
                i23 = max;
                i20 = 0;
                constraintAnchor7 = constraintAnchor9;
                constraintAnchor = constraintAnchor14;
                int i29 = P0;
                constraintAnchor6 = constraintAnchor2;
                i26 = i29;
            } else {
                ConstraintAnchor constraintAnchor15 = constraintAnchor8;
                int i30 = i22;
                int i31 = i23;
                i12 = i24;
                if (i12 < size - 1) {
                    constraintAnchor = this.f3837o1.get(i12 + 1).f3844b.D;
                    R0 = 0;
                } else {
                    constraintAnchor = this.F;
                    R0 = R0();
                }
                ConstraintAnchor constraintAnchor16 = aVar3.f3844b.F;
                aVar3.j(i9, constraintAnchor15, constraintAnchor7, constraintAnchor, constraintAnchor6, i25, i20, R0, i26, i10);
                i23 = i31 + aVar3.f();
                int max2 = Math.max(i30, aVar3.e());
                if (i12 > 0) {
                    i23 += this.f3830h1;
                }
                i22 = max2;
                i25 = 0;
                i21 = R0;
                constraintAnchor8 = constraintAnchor16;
            }
            i24 = i12 + 1;
            constraintAnchor5 = constraintAnchor;
        }
        iArr[0] = i23;
        iArr[1] = i22;
    }

    private void F1(ConstraintWidget[] constraintWidgetArr, int i8, int i9, int i10, int[] iArr) {
        a aVar;
        if (i8 == 0) {
            return;
        }
        if (this.f3837o1.size() == 0) {
            aVar = new a(i9, this.D, this.E, this.F, this.G, i10);
            this.f3837o1.add(aVar);
        } else {
            a aVar2 = this.f3837o1.get(0);
            aVar2.c();
            aVar = aVar2;
            aVar.j(i9, this.D, this.E, this.F, this.G, Q0(), S0(), R0(), P0(), i10);
        }
        for (int i11 = 0; i11 < i8; i11++) {
            aVar.b(constraintWidgetArr[i11]);
        }
        iArr[0] = aVar.f();
        iArr[1] = aVar.e();
    }

    public void G1(float f5) {
        this.f3826d1 = f5;
    }

    public void H1(int i8) {
        this.X0 = i8;
    }

    public void I1(float f5) {
        this.f3827e1 = f5;
    }

    public void J1(int i8) {
        this.Y0 = i8;
    }

    public void K1(int i8) {
        this.f3832j1 = i8;
    }

    public void L1(float f5) {
        this.f3824b1 = f5;
    }

    public void M1(int i8) {
        this.f3830h1 = i8;
    }

    public void N1(int i8) {
        this.V0 = i8;
    }

    public void O1(float f5) {
        this.f3828f1 = f5;
    }

    public void P1(int i8) {
        this.Z0 = i8;
    }

    public void Q1(float f5) {
        this.f3829g1 = f5;
    }

    public void R1(int i8) {
        this.f3823a1 = i8;
    }

    public void S1(int i8) {
        this.f3835m1 = i8;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0047, code lost:
        if (r18.W0 == (-1)) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0052, code lost:
        if (r18.W0 == (-1)) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0054, code lost:
        r18.W0 = 0;
     */
    @Override // androidx.constraintlayout.solver.widgets.h
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void T0(int r19, int r20, int r21, int r22) {
        /*
            Method dump skipped, instructions count: 263
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.e.T0(int, int, int, int):void");
    }

    public void T1(int i8) {
        this.f3836n1 = i8;
    }

    public void U1(int i8) {
        this.f3833k1 = i8;
    }

    public void V1(float f5) {
        this.f3825c1 = f5;
    }

    public void W1(int i8) {
        this.f3831i1 = i8;
    }

    public void X1(int i8) {
        this.W0 = i8;
    }

    public void Y1(int i8) {
        this.f3834l1 = i8;
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void f(androidx.constraintlayout.solver.d dVar) {
        super.f(dVar);
        boolean c12 = H() != null ? ((d) H()).c1() : false;
        int i8 = this.f3834l1;
        if (i8 != 0) {
            if (i8 == 1) {
                int size = this.f3837o1.size();
                int i9 = 0;
                while (i9 < size) {
                    this.f3837o1.get(i9).d(c12, i9, i9 == size + (-1));
                    i9++;
                }
            } else if (i8 == 2) {
                A1(c12);
            }
        } else if (this.f3837o1.size() > 0) {
            this.f3837o1.get(0).d(c12, 0, true);
        }
        X0(false);
    }

    @Override // n0.b, androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void l(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.l(constraintWidget, hashMap);
        e eVar = (e) constraintWidget;
        this.V0 = eVar.V0;
        this.W0 = eVar.W0;
        this.X0 = eVar.X0;
        this.Y0 = eVar.Y0;
        this.Z0 = eVar.Z0;
        this.f3823a1 = eVar.f3823a1;
        this.f3824b1 = eVar.f3824b1;
        this.f3825c1 = eVar.f3825c1;
        this.f3826d1 = eVar.f3826d1;
        this.f3827e1 = eVar.f3827e1;
        this.f3828f1 = eVar.f3828f1;
        this.f3829g1 = eVar.f3829g1;
        this.f3830h1 = eVar.f3830h1;
        this.f3831i1 = eVar.f3831i1;
        this.f3832j1 = eVar.f3832j1;
        this.f3833k1 = eVar.f3833k1;
        this.f3834l1 = eVar.f3834l1;
        this.f3835m1 = eVar.f3835m1;
        this.f3836n1 = eVar.f3836n1;
    }
}
