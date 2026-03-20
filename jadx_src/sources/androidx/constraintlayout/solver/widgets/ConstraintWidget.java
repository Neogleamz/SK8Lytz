package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.analyzer.j;
import androidx.constraintlayout.solver.widgets.analyzer.k;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ConstraintWidget {
    public static float F0 = 0.5f;
    public float[] A0;
    private boolean B;
    protected ConstraintWidget[] B0;
    protected ConstraintWidget[] C0;
    ConstraintWidget D0;
    ConstraintWidget E0;
    ConstraintAnchor K;
    public ConstraintAnchor[] L;
    protected ArrayList<ConstraintAnchor> M;
    private boolean[] N;
    public DimensionBehaviour[] O;
    public ConstraintWidget P;
    int Q;
    int R;
    public float S;
    protected int T;
    protected int U;
    protected int V;
    int W;
    int X;
    protected int Y;
    protected int Z;

    /* renamed from: a0  reason: collision with root package name */
    int f3665a0;

    /* renamed from: b0  reason: collision with root package name */
    protected int f3667b0;

    /* renamed from: c  reason: collision with root package name */
    public androidx.constraintlayout.solver.widgets.analyzer.b f3668c;

    /* renamed from: c0  reason: collision with root package name */
    protected int f3669c0;

    /* renamed from: d  reason: collision with root package name */
    public androidx.constraintlayout.solver.widgets.analyzer.b f3670d;

    /* renamed from: d0  reason: collision with root package name */
    float f3671d0;

    /* renamed from: e0  reason: collision with root package name */
    float f3673e0;

    /* renamed from: f0  reason: collision with root package name */
    private Object f3675f0;

    /* renamed from: g0  reason: collision with root package name */
    private int f3677g0;

    /* renamed from: h0  reason: collision with root package name */
    private int f3679h0;

    /* renamed from: i0  reason: collision with root package name */
    private String f3681i0;

    /* renamed from: j0  reason: collision with root package name */
    private String f3683j0;

    /* renamed from: k0  reason: collision with root package name */
    int f3685k0;

    /* renamed from: l0  reason: collision with root package name */
    int f3687l0;

    /* renamed from: m0  reason: collision with root package name */
    int f3689m0;

    /* renamed from: n0  reason: collision with root package name */
    int f3691n0;

    /* renamed from: o0  reason: collision with root package name */
    boolean f3693o0;

    /* renamed from: p0  reason: collision with root package name */
    boolean f3695p0;

    /* renamed from: q0  reason: collision with root package name */
    boolean f3696q0;

    /* renamed from: r0  reason: collision with root package name */
    boolean f3698r0;

    /* renamed from: s0  reason: collision with root package name */
    boolean f3700s0;

    /* renamed from: t0  reason: collision with root package name */
    boolean f3702t0;

    /* renamed from: u  reason: collision with root package name */
    public boolean f3703u;

    /* renamed from: u0  reason: collision with root package name */
    boolean f3704u0;

    /* renamed from: v  reason: collision with root package name */
    public boolean f3705v;

    /* renamed from: v0  reason: collision with root package name */
    boolean f3706v0;

    /* renamed from: w0  reason: collision with root package name */
    int f3708w0;

    /* renamed from: x0  reason: collision with root package name */
    int f3710x0;

    /* renamed from: y0  reason: collision with root package name */
    boolean f3712y0;

    /* renamed from: z0  reason: collision with root package name */
    boolean f3714z0;

    /* renamed from: a  reason: collision with root package name */
    public boolean f3664a = false;

    /* renamed from: b  reason: collision with root package name */
    public k[] f3666b = new k[2];

    /* renamed from: e  reason: collision with root package name */
    public androidx.constraintlayout.solver.widgets.analyzer.h f3672e = new androidx.constraintlayout.solver.widgets.analyzer.h(this);

    /* renamed from: f  reason: collision with root package name */
    public j f3674f = new j(this);

    /* renamed from: g  reason: collision with root package name */
    public boolean[] f3676g = {true, true};

    /* renamed from: h  reason: collision with root package name */
    public int[] f3678h = {0, 0, 0, 0};

    /* renamed from: i  reason: collision with root package name */
    boolean f3680i = false;

    /* renamed from: j  reason: collision with root package name */
    public int f3682j = -1;

    /* renamed from: k  reason: collision with root package name */
    public int f3684k = -1;

    /* renamed from: l  reason: collision with root package name */
    public int f3686l = 0;

    /* renamed from: m  reason: collision with root package name */
    public int f3688m = 0;

    /* renamed from: n  reason: collision with root package name */
    public int[] f3690n = new int[2];

    /* renamed from: o  reason: collision with root package name */
    public int f3692o = 0;

    /* renamed from: p  reason: collision with root package name */
    public int f3694p = 0;
    public float q = 1.0f;

    /* renamed from: r  reason: collision with root package name */
    public int f3697r = 0;

    /* renamed from: s  reason: collision with root package name */
    public int f3699s = 0;

    /* renamed from: t  reason: collision with root package name */
    public float f3701t = 1.0f;

    /* renamed from: w  reason: collision with root package name */
    int f3707w = -1;

    /* renamed from: x  reason: collision with root package name */
    float f3709x = 1.0f;

    /* renamed from: y  reason: collision with root package name */
    private int[] f3711y = {Integer.MAX_VALUE, Integer.MAX_VALUE};

    /* renamed from: z  reason: collision with root package name */
    private float f3713z = 0.0f;
    private boolean A = false;
    private boolean C = false;
    public ConstraintAnchor D = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
    public ConstraintAnchor E = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
    public ConstraintAnchor F = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
    public ConstraintAnchor G = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
    ConstraintAnchor H = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
    ConstraintAnchor I = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
    ConstraintAnchor J = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f3720a;

        /* renamed from: b  reason: collision with root package name */
        static final /* synthetic */ int[] f3721b;

        static {
            int[] iArr = new int[DimensionBehaviour.values().length];
            f3721b = iArr;
            try {
                iArr[DimensionBehaviour.FIXED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3721b[DimensionBehaviour.WRAP_CONTENT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3721b[DimensionBehaviour.MATCH_PARENT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f3721b[DimensionBehaviour.MATCH_CONSTRAINT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            int[] iArr2 = new int[ConstraintAnchor.Type.values().length];
            f3720a = iArr2;
            try {
                iArr2[ConstraintAnchor.Type.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f3720a[ConstraintAnchor.Type.TOP.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f3720a[ConstraintAnchor.Type.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f3720a[ConstraintAnchor.Type.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f3720a[ConstraintAnchor.Type.BASELINE.ordinal()] = 5;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f3720a[ConstraintAnchor.Type.CENTER.ordinal()] = 6;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                f3720a[ConstraintAnchor.Type.CENTER_X.ordinal()] = 7;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                f3720a[ConstraintAnchor.Type.CENTER_Y.ordinal()] = 8;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                f3720a[ConstraintAnchor.Type.NONE.ordinal()] = 9;
            } catch (NoSuchFieldError unused13) {
            }
        }
    }

    public ConstraintWidget() {
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.K = constraintAnchor;
        this.L = new ConstraintAnchor[]{this.D, this.F, this.E, this.G, this.H, constraintAnchor};
        this.M = new ArrayList<>();
        this.N = new boolean[2];
        DimensionBehaviour dimensionBehaviour = DimensionBehaviour.FIXED;
        this.O = new DimensionBehaviour[]{dimensionBehaviour, dimensionBehaviour};
        this.P = null;
        this.Q = 0;
        this.R = 0;
        this.S = 0.0f;
        this.T = -1;
        this.U = 0;
        this.V = 0;
        this.W = 0;
        this.X = 0;
        this.Y = 0;
        this.Z = 0;
        this.f3665a0 = 0;
        float f5 = F0;
        this.f3671d0 = f5;
        this.f3673e0 = f5;
        this.f3677g0 = 0;
        this.f3679h0 = 0;
        this.f3681i0 = null;
        this.f3683j0 = null;
        this.f3704u0 = false;
        this.f3706v0 = false;
        this.f3708w0 = 0;
        this.f3710x0 = 0;
        this.A0 = new float[]{-1.0f, -1.0f};
        this.B0 = new ConstraintWidget[]{null, null};
        this.C0 = new ConstraintWidget[]{null, null};
        this.D0 = null;
        this.E0 = null;
        d();
    }

    private boolean V(int i8) {
        int i9 = i8 * 2;
        ConstraintAnchor[] constraintAnchorArr = this.L;
        if (constraintAnchorArr[i9].f3649d != null && constraintAnchorArr[i9].f3649d.f3649d != constraintAnchorArr[i9]) {
            int i10 = i9 + 1;
            if (constraintAnchorArr[i10].f3649d != null && constraintAnchorArr[i10].f3649d.f3649d == constraintAnchorArr[i10]) {
                return true;
            }
        }
        return false;
    }

    private void d() {
        this.M.add(this.D);
        this.M.add(this.E);
        this.M.add(this.F);
        this.M.add(this.G);
        this.M.add(this.I);
        this.M.add(this.J);
        this.M.add(this.K);
        this.M.add(this.H);
    }

    /* JADX WARN: Code restructure failed: missing block: B:291:0x0453, code lost:
        if (r1[r22] == r4) goto L59;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x01ae  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x0273  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0277  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x02d7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:183:0x02e6  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x031c  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0330 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:192:0x0331  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x0414  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x0422 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:301:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00ce  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void h(androidx.constraintlayout.solver.d r32, boolean r33, boolean r34, boolean r35, boolean r36, androidx.constraintlayout.solver.SolverVariable r37, androidx.constraintlayout.solver.SolverVariable r38, androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour r39, boolean r40, androidx.constraintlayout.solver.widgets.ConstraintAnchor r41, androidx.constraintlayout.solver.widgets.ConstraintAnchor r42, int r43, int r44, int r45, int r46, float r47, boolean r48, boolean r49, boolean r50, boolean r51, int r52, int r53, int r54, int r55, float r56, boolean r57) {
        /*
            Method dump skipped, instructions count: 1122
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.h(androidx.constraintlayout.solver.d, boolean, boolean, boolean, boolean, androidx.constraintlayout.solver.SolverVariable, androidx.constraintlayout.solver.SolverVariable, androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour, boolean, androidx.constraintlayout.solver.widgets.ConstraintAnchor, androidx.constraintlayout.solver.widgets.ConstraintAnchor, int, int, int, int, float, boolean, boolean, boolean, boolean, int, int, int, int, float, boolean):void");
    }

    public int A() {
        ConstraintAnchor constraintAnchor = this.D;
        int i8 = constraintAnchor != null ? 0 + constraintAnchor.f3650e : 0;
        ConstraintAnchor constraintAnchor2 = this.F;
        return constraintAnchor2 != null ? i8 + constraintAnchor2.f3650e : i8;
    }

    public void A0(int i8, int i9) {
        this.V = i8;
        int i10 = i9 - i8;
        this.R = i10;
        int i11 = this.f3669c0;
        if (i10 < i11) {
            this.R = i11;
        }
    }

    public int B(int i8) {
        if (i8 == 0) {
            return Q();
        }
        if (i8 == 1) {
            return w();
        }
        return 0;
    }

    public void B0(DimensionBehaviour dimensionBehaviour) {
        this.O[1] = dimensionBehaviour;
    }

    public int C() {
        return this.f3711y[1];
    }

    public void C0(int i8, int i9, int i10, float f5) {
        this.f3688m = i8;
        this.f3697r = i9;
        if (i10 == Integer.MAX_VALUE) {
            i10 = 0;
        }
        this.f3699s = i10;
        this.f3701t = f5;
        if (f5 <= 0.0f || f5 >= 1.0f || i8 != 0) {
            return;
        }
        this.f3688m = 2;
    }

    public int D() {
        return this.f3711y[0];
    }

    public void D0(float f5) {
        this.A0[1] = f5;
    }

    public int E() {
        return this.f3669c0;
    }

    public void E0(int i8) {
        this.f3679h0 = i8;
    }

    public int F() {
        return this.f3667b0;
    }

    public void F0(int i8) {
        this.Q = i8;
        int i9 = this.f3667b0;
        if (i8 < i9) {
            this.Q = i9;
        }
    }

    public ConstraintWidget G(int i8) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (i8 != 0) {
            if (i8 == 1 && (constraintAnchor2 = (constraintAnchor = this.G).f3649d) != null && constraintAnchor2.f3649d == constraintAnchor) {
                return constraintAnchor2.f3647b;
            }
            return null;
        }
        ConstraintAnchor constraintAnchor3 = this.F;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.f3649d;
        if (constraintAnchor4 == null || constraintAnchor4.f3649d != constraintAnchor3) {
            return null;
        }
        return constraintAnchor4.f3647b;
    }

    public void G0(int i8) {
        this.U = i8;
    }

    public ConstraintWidget H() {
        return this.P;
    }

    public void H0(int i8) {
        this.V = i8;
    }

    public ConstraintWidget I(int i8) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (i8 != 0) {
            if (i8 == 1 && (constraintAnchor2 = (constraintAnchor = this.E).f3649d) != null && constraintAnchor2.f3649d == constraintAnchor) {
                return constraintAnchor2.f3647b;
            }
            return null;
        }
        ConstraintAnchor constraintAnchor3 = this.D;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.f3649d;
        if (constraintAnchor4 == null || constraintAnchor4.f3649d != constraintAnchor3) {
            return null;
        }
        return constraintAnchor4.f3647b;
    }

    public void I0(boolean z4, boolean z8, boolean z9, boolean z10) {
        if (this.f3707w == -1) {
            if (z9 && !z10) {
                this.f3707w = 0;
            } else if (!z9 && z10) {
                this.f3707w = 1;
                if (this.T == -1) {
                    this.f3709x = 1.0f / this.f3709x;
                }
            }
        }
        if (this.f3707w == 0 && (!this.E.j() || !this.G.j())) {
            this.f3707w = 1;
        } else if (this.f3707w == 1 && (!this.D.j() || !this.F.j())) {
            this.f3707w = 0;
        }
        if (this.f3707w == -1 && (!this.E.j() || !this.G.j() || !this.D.j() || !this.F.j())) {
            if (this.E.j() && this.G.j()) {
                this.f3707w = 0;
            } else if (this.D.j() && this.F.j()) {
                this.f3709x = 1.0f / this.f3709x;
                this.f3707w = 1;
            }
        }
        if (this.f3707w == -1) {
            int i8 = this.f3692o;
            if (i8 > 0 && this.f3697r == 0) {
                this.f3707w = 0;
            } else if (i8 != 0 || this.f3697r <= 0) {
            } else {
                this.f3709x = 1.0f / this.f3709x;
                this.f3707w = 1;
            }
        }
    }

    public int J() {
        return R() + this.Q;
    }

    public void J0(boolean z4, boolean z8) {
        int i8;
        int i9;
        boolean k8 = z4 & this.f3672e.k();
        boolean k9 = z8 & this.f3674f.k();
        androidx.constraintlayout.solver.widgets.analyzer.h hVar = this.f3672e;
        int i10 = hVar.f3789h.f3753g;
        j jVar = this.f3674f;
        int i11 = jVar.f3789h.f3753g;
        int i12 = hVar.f3790i.f3753g;
        int i13 = jVar.f3790i.f3753g;
        int i14 = i13 - i11;
        if (i12 - i10 < 0 || i14 < 0 || i10 == Integer.MIN_VALUE || i10 == Integer.MAX_VALUE || i11 == Integer.MIN_VALUE || i11 == Integer.MAX_VALUE || i12 == Integer.MIN_VALUE || i12 == Integer.MAX_VALUE || i13 == Integer.MIN_VALUE || i13 == Integer.MAX_VALUE) {
            i12 = 0;
            i10 = 0;
            i13 = 0;
            i11 = 0;
        }
        int i15 = i12 - i10;
        int i16 = i13 - i11;
        if (k8) {
            this.U = i10;
        }
        if (k9) {
            this.V = i11;
        }
        if (this.f3679h0 == 8) {
            this.Q = 0;
            this.R = 0;
            return;
        }
        if (k8) {
            if (this.O[0] == DimensionBehaviour.FIXED && i15 < (i9 = this.Q)) {
                i15 = i9;
            }
            this.Q = i15;
            int i17 = this.f3667b0;
            if (i15 < i17) {
                this.Q = i17;
            }
        }
        if (k9) {
            if (this.O[1] == DimensionBehaviour.FIXED && i16 < (i8 = this.R)) {
                i16 = i8;
            }
            this.R = i16;
            int i18 = this.f3669c0;
            if (i16 < i18) {
                this.R = i18;
            }
        }
    }

    public k K(int i8) {
        if (i8 == 0) {
            return this.f3672e;
        }
        if (i8 == 1) {
            return this.f3674f;
        }
        return null;
    }

    public void K0(androidx.constraintlayout.solver.d dVar) {
        int x8 = dVar.x(this.D);
        int x9 = dVar.x(this.E);
        int x10 = dVar.x(this.F);
        int x11 = dVar.x(this.G);
        androidx.constraintlayout.solver.widgets.analyzer.h hVar = this.f3672e;
        androidx.constraintlayout.solver.widgets.analyzer.d dVar2 = hVar.f3789h;
        if (dVar2.f3756j) {
            androidx.constraintlayout.solver.widgets.analyzer.d dVar3 = hVar.f3790i;
            if (dVar3.f3756j) {
                x8 = dVar2.f3753g;
                x10 = dVar3.f3753g;
            }
        }
        j jVar = this.f3674f;
        androidx.constraintlayout.solver.widgets.analyzer.d dVar4 = jVar.f3789h;
        if (dVar4.f3756j) {
            androidx.constraintlayout.solver.widgets.analyzer.d dVar5 = jVar.f3790i;
            if (dVar5.f3756j) {
                x9 = dVar4.f3753g;
                x11 = dVar5.f3753g;
            }
        }
        int i8 = x11 - x9;
        if (x10 - x8 < 0 || i8 < 0 || x8 == Integer.MIN_VALUE || x8 == Integer.MAX_VALUE || x9 == Integer.MIN_VALUE || x9 == Integer.MAX_VALUE || x10 == Integer.MIN_VALUE || x10 == Integer.MAX_VALUE || x11 == Integer.MIN_VALUE || x11 == Integer.MAX_VALUE) {
            x11 = 0;
            x8 = 0;
            x9 = 0;
            x10 = 0;
        }
        g0(x8, x9, x10, x11);
    }

    public float L() {
        return this.f3673e0;
    }

    public int M() {
        return this.f3710x0;
    }

    public DimensionBehaviour N() {
        return this.O[1];
    }

    public int O() {
        int i8 = this.D != null ? 0 + this.E.f3650e : 0;
        return this.F != null ? i8 + this.G.f3650e : i8;
    }

    public int P() {
        return this.f3679h0;
    }

    public int Q() {
        if (this.f3679h0 == 8) {
            return 0;
        }
        return this.Q;
    }

    public int R() {
        ConstraintWidget constraintWidget = this.P;
        return (constraintWidget == null || !(constraintWidget instanceof d)) ? this.U : ((d) constraintWidget).M0 + this.U;
    }

    public int S() {
        ConstraintWidget constraintWidget = this.P;
        return (constraintWidget == null || !(constraintWidget instanceof d)) ? this.V : ((d) constraintWidget).N0 + this.V;
    }

    public boolean T() {
        return this.A;
    }

    public void U(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int i8, int i9) {
        n(type).b(constraintWidget.n(type2), i8, i9, true);
    }

    public boolean W() {
        ConstraintAnchor constraintAnchor = this.D;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.f3649d;
        if (constraintAnchor2 == null || constraintAnchor2.f3649d != constraintAnchor) {
            ConstraintAnchor constraintAnchor3 = this.F;
            ConstraintAnchor constraintAnchor4 = constraintAnchor3.f3649d;
            return constraintAnchor4 != null && constraintAnchor4.f3649d == constraintAnchor3;
        }
        return true;
    }

    public boolean X() {
        return this.B;
    }

    public boolean Y() {
        ConstraintAnchor constraintAnchor = this.E;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.f3649d;
        if (constraintAnchor2 == null || constraintAnchor2.f3649d != constraintAnchor) {
            ConstraintAnchor constraintAnchor3 = this.G;
            ConstraintAnchor constraintAnchor4 = constraintAnchor3.f3649d;
            return constraintAnchor4 != null && constraintAnchor4.f3649d == constraintAnchor3;
        }
        return true;
    }

    public void Z() {
        this.D.l();
        this.E.l();
        this.F.l();
        this.G.l();
        this.H.l();
        this.I.l();
        this.J.l();
        this.K.l();
        this.P = null;
        this.f3713z = 0.0f;
        this.Q = 0;
        this.R = 0;
        this.S = 0.0f;
        this.T = -1;
        this.U = 0;
        this.V = 0;
        this.Y = 0;
        this.Z = 0;
        this.f3665a0 = 0;
        this.f3667b0 = 0;
        this.f3669c0 = 0;
        float f5 = F0;
        this.f3671d0 = f5;
        this.f3673e0 = f5;
        DimensionBehaviour[] dimensionBehaviourArr = this.O;
        DimensionBehaviour dimensionBehaviour = DimensionBehaviour.FIXED;
        dimensionBehaviourArr[0] = dimensionBehaviour;
        dimensionBehaviourArr[1] = dimensionBehaviour;
        this.f3675f0 = null;
        this.f3677g0 = 0;
        this.f3679h0 = 0;
        this.f3683j0 = null;
        this.f3700s0 = false;
        this.f3702t0 = false;
        this.f3708w0 = 0;
        this.f3710x0 = 0;
        this.f3712y0 = false;
        this.f3714z0 = false;
        float[] fArr = this.A0;
        fArr[0] = -1.0f;
        fArr[1] = -1.0f;
        this.f3682j = -1;
        this.f3684k = -1;
        int[] iArr = this.f3711y;
        iArr[0] = Integer.MAX_VALUE;
        iArr[1] = Integer.MAX_VALUE;
        this.f3686l = 0;
        this.f3688m = 0;
        this.q = 1.0f;
        this.f3701t = 1.0f;
        this.f3694p = Integer.MAX_VALUE;
        this.f3699s = Integer.MAX_VALUE;
        this.f3692o = 0;
        this.f3697r = 0;
        this.f3680i = false;
        this.f3707w = -1;
        this.f3709x = 1.0f;
        this.f3704u0 = false;
        this.f3706v0 = false;
        boolean[] zArr = this.f3676g;
        zArr[0] = true;
        zArr[1] = true;
        this.C = false;
        boolean[] zArr2 = this.N;
        zArr2[0] = false;
        zArr2[1] = false;
    }

    public void a0() {
        ConstraintWidget H = H();
        if (H != null && (H instanceof d) && ((d) H()).Y0()) {
            return;
        }
        int size = this.M.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.M.get(i8).l();
        }
    }

    public void b0(androidx.constraintlayout.solver.c cVar) {
        this.D.m(cVar);
        this.E.m(cVar);
        this.F.m(cVar);
        this.G.m(cVar);
        this.H.m(cVar);
        this.K.m(cVar);
        this.I.m(cVar);
        this.J.m(cVar);
    }

    public void c0(int i8) {
        this.f3665a0 = i8;
        this.A = i8 > 0;
    }

    public void d0(Object obj) {
        this.f3675f0 = obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean e() {
        return (this instanceof h) || (this instanceof f);
    }

    public void e0(String str) {
        this.f3681i0 = str;
    }

    /* JADX WARN: Removed duplicated region for block: B:139:0x023a  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0244 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0250  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x025e  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0277  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x0359  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0375  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x03d2  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x03d4  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x03d7  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x0492  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x0498  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x04be  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x04c8  */
    /* JADX WARN: Removed duplicated region for block: B:254:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void f(androidx.constraintlayout.solver.d r48) {
        /*
            Method dump skipped, instructions count: 1257
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.f(androidx.constraintlayout.solver.d):void");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:38:0x0084 -> B:39:0x0085). Please submit an issue!!! */
    public void f0(String str) {
        float f5;
        int i8 = 0;
        if (str == null || str.length() == 0) {
            this.S = 0.0f;
            return;
        }
        int i9 = -1;
        int length = str.length();
        int indexOf = str.indexOf(44);
        int i10 = 0;
        if (indexOf > 0 && indexOf < length - 1) {
            String substring = str.substring(0, indexOf);
            if (substring.equalsIgnoreCase("W")) {
                i9 = 0;
            } else if (substring.equalsIgnoreCase("H")) {
                i9 = 1;
            }
            i10 = indexOf + 1;
        }
        int indexOf2 = str.indexOf(58);
        if (indexOf2 < 0 || indexOf2 >= length - 1) {
            String substring2 = str.substring(i10);
            if (substring2.length() > 0) {
                f5 = Float.parseFloat(substring2);
            }
            f5 = i8;
        } else {
            String substring3 = str.substring(i10, indexOf2);
            String substring4 = str.substring(indexOf2 + 1);
            if (substring3.length() > 0 && substring4.length() > 0) {
                float parseFloat = Float.parseFloat(substring3);
                float parseFloat2 = Float.parseFloat(substring4);
                if (parseFloat > 0.0f && parseFloat2 > 0.0f) {
                    f5 = i9 == 1 ? Math.abs(parseFloat2 / parseFloat) : Math.abs(parseFloat / parseFloat2);
                }
            }
            f5 = i8;
        }
        i8 = (f5 > i8 ? 1 : (f5 == i8 ? 0 : -1));
        if (i8 > 0) {
            this.S = f5;
            this.T = i9;
        }
    }

    public boolean g() {
        return this.f3679h0 != 8;
    }

    public void g0(int i8, int i9, int i10, int i11) {
        int i12;
        int i13;
        int i14 = i10 - i8;
        int i15 = i11 - i9;
        this.U = i8;
        this.V = i9;
        if (this.f3679h0 == 8) {
            this.Q = 0;
            this.R = 0;
            return;
        }
        DimensionBehaviour[] dimensionBehaviourArr = this.O;
        DimensionBehaviour dimensionBehaviour = dimensionBehaviourArr[0];
        DimensionBehaviour dimensionBehaviour2 = DimensionBehaviour.FIXED;
        if (dimensionBehaviour == dimensionBehaviour2 && i14 < (i13 = this.Q)) {
            i14 = i13;
        }
        if (dimensionBehaviourArr[1] == dimensionBehaviour2 && i15 < (i12 = this.R)) {
            i15 = i12;
        }
        this.Q = i14;
        this.R = i15;
        int i16 = this.f3669c0;
        if (i15 < i16) {
            this.R = i16;
        }
        int i17 = this.f3667b0;
        if (i14 < i17) {
            this.Q = i17;
        }
    }

    public void h0(boolean z4) {
        this.A = z4;
    }

    /* JADX WARN: Code restructure failed: missing block: B:87:0x018b, code lost:
        if (r11.j() != false) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x01b4, code lost:
        if (r11.j() != false) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x01b6, code lost:
        r9.l();
        r11.l();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void i(androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type r9, androidx.constraintlayout.solver.widgets.ConstraintWidget r10, androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type r11, int r12) {
        /*
            Method dump skipped, instructions count: 448
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.i(androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type, androidx.constraintlayout.solver.widgets.ConstraintWidget, androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type, int):void");
    }

    public void i0(int i8) {
        this.R = i8;
        int i9 = this.f3669c0;
        if (i8 < i9) {
            this.R = i9;
        }
    }

    public void j(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i8) {
        if (constraintAnchor.e() == this) {
            i(constraintAnchor.h(), constraintAnchor2.e(), constraintAnchor2.h(), i8);
        }
    }

    public void j0(float f5) {
        this.f3671d0 = f5;
    }

    public void k(ConstraintWidget constraintWidget, float f5, int i8) {
        ConstraintAnchor.Type type = ConstraintAnchor.Type.CENTER;
        U(type, constraintWidget, type, i8, 0);
        this.f3713z = f5;
    }

    public void k0(int i8) {
        this.f3708w0 = i8;
    }

    public void l(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        this.f3682j = constraintWidget.f3682j;
        this.f3684k = constraintWidget.f3684k;
        this.f3686l = constraintWidget.f3686l;
        this.f3688m = constraintWidget.f3688m;
        int[] iArr = this.f3690n;
        int[] iArr2 = constraintWidget.f3690n;
        iArr[0] = iArr2[0];
        iArr[1] = iArr2[1];
        this.f3692o = constraintWidget.f3692o;
        this.f3694p = constraintWidget.f3694p;
        this.f3697r = constraintWidget.f3697r;
        this.f3699s = constraintWidget.f3699s;
        this.f3701t = constraintWidget.f3701t;
        this.f3703u = constraintWidget.f3703u;
        this.f3705v = constraintWidget.f3705v;
        this.f3707w = constraintWidget.f3707w;
        this.f3709x = constraintWidget.f3709x;
        int[] iArr3 = constraintWidget.f3711y;
        this.f3711y = Arrays.copyOf(iArr3, iArr3.length);
        this.f3713z = constraintWidget.f3713z;
        this.A = constraintWidget.A;
        this.B = constraintWidget.B;
        this.D.l();
        this.E.l();
        this.F.l();
        this.G.l();
        this.H.l();
        this.I.l();
        this.J.l();
        this.K.l();
        this.O = (DimensionBehaviour[]) Arrays.copyOf(this.O, 2);
        this.P = this.P == null ? null : hashMap.get(constraintWidget.P);
        this.Q = constraintWidget.Q;
        this.R = constraintWidget.R;
        this.S = constraintWidget.S;
        this.T = constraintWidget.T;
        this.U = constraintWidget.U;
        this.V = constraintWidget.V;
        this.W = constraintWidget.W;
        this.X = constraintWidget.X;
        this.Y = constraintWidget.Y;
        this.Z = constraintWidget.Z;
        this.f3665a0 = constraintWidget.f3665a0;
        this.f3667b0 = constraintWidget.f3667b0;
        this.f3669c0 = constraintWidget.f3669c0;
        this.f3671d0 = constraintWidget.f3671d0;
        this.f3673e0 = constraintWidget.f3673e0;
        this.f3675f0 = constraintWidget.f3675f0;
        this.f3677g0 = constraintWidget.f3677g0;
        this.f3679h0 = constraintWidget.f3679h0;
        this.f3681i0 = constraintWidget.f3681i0;
        this.f3683j0 = constraintWidget.f3683j0;
        this.f3685k0 = constraintWidget.f3685k0;
        this.f3687l0 = constraintWidget.f3687l0;
        this.f3689m0 = constraintWidget.f3689m0;
        this.f3691n0 = constraintWidget.f3691n0;
        this.f3693o0 = constraintWidget.f3693o0;
        this.f3695p0 = constraintWidget.f3695p0;
        this.f3696q0 = constraintWidget.f3696q0;
        this.f3698r0 = constraintWidget.f3698r0;
        this.f3700s0 = constraintWidget.f3700s0;
        this.f3702t0 = constraintWidget.f3702t0;
        this.f3704u0 = constraintWidget.f3704u0;
        this.f3706v0 = constraintWidget.f3706v0;
        this.f3708w0 = constraintWidget.f3708w0;
        this.f3710x0 = constraintWidget.f3710x0;
        this.f3712y0 = constraintWidget.f3712y0;
        this.f3714z0 = constraintWidget.f3714z0;
        float[] fArr = this.A0;
        float[] fArr2 = constraintWidget.A0;
        fArr[0] = fArr2[0];
        fArr[1] = fArr2[1];
        ConstraintWidget[] constraintWidgetArr = this.B0;
        ConstraintWidget[] constraintWidgetArr2 = constraintWidget.B0;
        constraintWidgetArr[0] = constraintWidgetArr2[0];
        constraintWidgetArr[1] = constraintWidgetArr2[1];
        ConstraintWidget[] constraintWidgetArr3 = this.C0;
        ConstraintWidget[] constraintWidgetArr4 = constraintWidget.C0;
        constraintWidgetArr3[0] = constraintWidgetArr4[0];
        constraintWidgetArr3[1] = constraintWidgetArr4[1];
        ConstraintWidget constraintWidget2 = constraintWidget.D0;
        this.D0 = constraintWidget2 == null ? null : hashMap.get(constraintWidget2);
        ConstraintWidget constraintWidget3 = constraintWidget.E0;
        this.E0 = constraintWidget3 != null ? hashMap.get(constraintWidget3) : null;
    }

    public void l0(int i8, int i9) {
        this.U = i8;
        int i10 = i9 - i8;
        this.Q = i10;
        int i11 = this.f3667b0;
        if (i10 < i11) {
            this.Q = i11;
        }
    }

    public void m(androidx.constraintlayout.solver.d dVar) {
        dVar.q(this.D);
        dVar.q(this.E);
        dVar.q(this.F);
        dVar.q(this.G);
        if (this.f3665a0 > 0) {
            dVar.q(this.H);
        }
    }

    public void m0(DimensionBehaviour dimensionBehaviour) {
        this.O[0] = dimensionBehaviour;
    }

    public ConstraintAnchor n(ConstraintAnchor.Type type) {
        switch (a.f3720a[type.ordinal()]) {
            case 1:
                return this.D;
            case 2:
                return this.E;
            case 3:
                return this.F;
            case 4:
                return this.G;
            case 5:
                return this.H;
            case 6:
                return this.K;
            case 7:
                return this.I;
            case 8:
                return this.J;
            case 9:
                return null;
            default:
                throw new AssertionError(type.name());
        }
    }

    public void n0(int i8, int i9, int i10, float f5) {
        this.f3686l = i8;
        this.f3692o = i9;
        if (i10 == Integer.MAX_VALUE) {
            i10 = 0;
        }
        this.f3694p = i10;
        this.q = f5;
        if (f5 <= 0.0f || f5 >= 1.0f || i8 != 0) {
            return;
        }
        this.f3686l = 2;
    }

    public int o() {
        return this.f3665a0;
    }

    public void o0(float f5) {
        this.A0[0] = f5;
    }

    public float p(int i8) {
        if (i8 == 0) {
            return this.f3671d0;
        }
        if (i8 == 1) {
            return this.f3673e0;
        }
        return -1.0f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void p0(int i8, boolean z4) {
        this.N[i8] = z4;
    }

    public int q() {
        return S() + this.R;
    }

    public void q0(boolean z4) {
        this.B = z4;
    }

    public Object r() {
        return this.f3675f0;
    }

    public void r0(boolean z4) {
        this.C = z4;
    }

    public String s() {
        return this.f3681i0;
    }

    public void s0(int i8) {
        this.f3711y[1] = i8;
    }

    public DimensionBehaviour t(int i8) {
        if (i8 == 0) {
            return z();
        }
        if (i8 == 1) {
            return N();
        }
        return null;
    }

    public void t0(int i8) {
        this.f3711y[0] = i8;
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        String str2 = this.f3683j0;
        String str3 = BuildConfig.FLAVOR;
        if (str2 != null) {
            str = "type: " + this.f3683j0 + " ";
        } else {
            str = BuildConfig.FLAVOR;
        }
        sb.append(str);
        if (this.f3681i0 != null) {
            str3 = "id: " + this.f3681i0 + " ";
        }
        sb.append(str3);
        sb.append("(");
        sb.append(this.U);
        sb.append(", ");
        sb.append(this.V);
        sb.append(") - (");
        sb.append(this.Q);
        sb.append(" x ");
        sb.append(this.R);
        sb.append(")");
        return sb.toString();
    }

    public float u() {
        return this.S;
    }

    public void u0(int i8) {
        if (i8 < 0) {
            i8 = 0;
        }
        this.f3669c0 = i8;
    }

    public int v() {
        return this.T;
    }

    public void v0(int i8) {
        if (i8 < 0) {
            i8 = 0;
        }
        this.f3667b0 = i8;
    }

    public int w() {
        if (this.f3679h0 == 8) {
            return 0;
        }
        return this.R;
    }

    public void w0(int i8, int i9) {
        this.U = i8;
        this.V = i9;
    }

    public float x() {
        return this.f3671d0;
    }

    public void x0(ConstraintWidget constraintWidget) {
        this.P = constraintWidget;
    }

    public int y() {
        return this.f3708w0;
    }

    public void y0(float f5) {
        this.f3673e0 = f5;
    }

    public DimensionBehaviour z() {
        return this.O[0];
    }

    public void z0(int i8) {
        this.f3710x0 = i8;
    }
}
