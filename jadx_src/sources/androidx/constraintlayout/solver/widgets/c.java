package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {

    /* renamed from: a  reason: collision with root package name */
    protected ConstraintWidget f3798a;

    /* renamed from: b  reason: collision with root package name */
    protected ConstraintWidget f3799b;

    /* renamed from: c  reason: collision with root package name */
    protected ConstraintWidget f3800c;

    /* renamed from: d  reason: collision with root package name */
    protected ConstraintWidget f3801d;

    /* renamed from: e  reason: collision with root package name */
    protected ConstraintWidget f3802e;

    /* renamed from: f  reason: collision with root package name */
    protected ConstraintWidget f3803f;

    /* renamed from: g  reason: collision with root package name */
    protected ConstraintWidget f3804g;

    /* renamed from: h  reason: collision with root package name */
    protected ArrayList<ConstraintWidget> f3805h;

    /* renamed from: i  reason: collision with root package name */
    protected int f3806i;

    /* renamed from: j  reason: collision with root package name */
    protected int f3807j;

    /* renamed from: k  reason: collision with root package name */
    protected float f3808k = 0.0f;

    /* renamed from: l  reason: collision with root package name */
    int f3809l;

    /* renamed from: m  reason: collision with root package name */
    int f3810m;

    /* renamed from: n  reason: collision with root package name */
    int f3811n;

    /* renamed from: o  reason: collision with root package name */
    boolean f3812o;

    /* renamed from: p  reason: collision with root package name */
    private int f3813p;
    private boolean q;

    /* renamed from: r  reason: collision with root package name */
    protected boolean f3814r;

    /* renamed from: s  reason: collision with root package name */
    protected boolean f3815s;

    /* renamed from: t  reason: collision with root package name */
    protected boolean f3816t;

    /* renamed from: u  reason: collision with root package name */
    protected boolean f3817u;

    /* renamed from: v  reason: collision with root package name */
    private boolean f3818v;

    public c(ConstraintWidget constraintWidget, int i8, boolean z4) {
        this.q = false;
        this.f3798a = constraintWidget;
        this.f3813p = i8;
        this.q = z4;
    }

    private void b() {
        int i8 = this.f3813p * 2;
        ConstraintWidget constraintWidget = this.f3798a;
        boolean z4 = true;
        this.f3812o = true;
        ConstraintWidget constraintWidget2 = constraintWidget;
        boolean z8 = false;
        while (!z8) {
            this.f3806i++;
            ConstraintWidget[] constraintWidgetArr = constraintWidget.C0;
            int i9 = this.f3813p;
            ConstraintWidget constraintWidget3 = null;
            constraintWidgetArr[i9] = null;
            constraintWidget.B0[i9] = null;
            if (constraintWidget.P() != 8) {
                this.f3809l++;
                ConstraintWidget.DimensionBehaviour t8 = constraintWidget.t(this.f3813p);
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                if (t8 != dimensionBehaviour) {
                    this.f3810m += constraintWidget.B(this.f3813p);
                }
                int c9 = this.f3810m + constraintWidget.L[i8].c();
                this.f3810m = c9;
                int i10 = i8 + 1;
                this.f3810m = c9 + constraintWidget.L[i10].c();
                int c10 = this.f3811n + constraintWidget.L[i8].c();
                this.f3811n = c10;
                this.f3811n = c10 + constraintWidget.L[i10].c();
                if (this.f3799b == null) {
                    this.f3799b = constraintWidget;
                }
                this.f3801d = constraintWidget;
                ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr = constraintWidget.O;
                int i11 = this.f3813p;
                if (dimensionBehaviourArr[i11] == dimensionBehaviour) {
                    int[] iArr = constraintWidget.f3690n;
                    if (iArr[i11] == 0 || iArr[i11] == 3 || iArr[i11] == 2) {
                        this.f3807j++;
                        float[] fArr = constraintWidget.A0;
                        float f5 = fArr[i11];
                        if (f5 > 0.0f) {
                            this.f3808k += fArr[i11];
                        }
                        if (c(constraintWidget, i11)) {
                            if (f5 < 0.0f) {
                                this.f3814r = true;
                            } else {
                                this.f3815s = true;
                            }
                            if (this.f3805h == null) {
                                this.f3805h = new ArrayList<>();
                            }
                            this.f3805h.add(constraintWidget);
                        }
                        if (this.f3803f == null) {
                            this.f3803f = constraintWidget;
                        }
                        ConstraintWidget constraintWidget4 = this.f3804g;
                        if (constraintWidget4 != null) {
                            constraintWidget4.B0[this.f3813p] = constraintWidget;
                        }
                        this.f3804g = constraintWidget;
                    }
                    if (this.f3813p != 0 ? !(constraintWidget.f3688m == 0 && constraintWidget.f3697r == 0 && constraintWidget.f3699s == 0) : !(constraintWidget.f3686l == 0 && constraintWidget.f3692o == 0 && constraintWidget.f3694p == 0)) {
                        this.f3812o = false;
                    }
                    if (constraintWidget.S != 0.0f) {
                        this.f3812o = false;
                        this.f3817u = true;
                    }
                }
            }
            if (constraintWidget2 != constraintWidget) {
                constraintWidget2.C0[this.f3813p] = constraintWidget;
            }
            ConstraintAnchor constraintAnchor = constraintWidget.L[i8 + 1].f3649d;
            if (constraintAnchor != null) {
                ConstraintWidget constraintWidget5 = constraintAnchor.f3647b;
                ConstraintAnchor[] constraintAnchorArr = constraintWidget5.L;
                if (constraintAnchorArr[i8].f3649d != null && constraintAnchorArr[i8].f3649d.f3647b == constraintWidget) {
                    constraintWidget3 = constraintWidget5;
                }
            }
            if (constraintWidget3 == null) {
                constraintWidget3 = constraintWidget;
                z8 = true;
            }
            constraintWidget2 = constraintWidget;
            constraintWidget = constraintWidget3;
        }
        ConstraintWidget constraintWidget6 = this.f3799b;
        if (constraintWidget6 != null) {
            this.f3810m -= constraintWidget6.L[i8].c();
        }
        ConstraintWidget constraintWidget7 = this.f3801d;
        if (constraintWidget7 != null) {
            this.f3810m -= constraintWidget7.L[i8 + 1].c();
        }
        this.f3800c = constraintWidget;
        if (this.f3813p == 0 && this.q) {
            this.f3802e = constraintWidget;
        } else {
            this.f3802e = this.f3798a;
        }
        if (!this.f3815s || !this.f3814r) {
            z4 = false;
        }
        this.f3816t = z4;
    }

    private static boolean c(ConstraintWidget constraintWidget, int i8) {
        if (constraintWidget.P() != 8 && constraintWidget.O[i8] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            int[] iArr = constraintWidget.f3690n;
            if (iArr[i8] == 0 || iArr[i8] == 3) {
                return true;
            }
        }
        return false;
    }

    public void a() {
        if (!this.f3818v) {
            b();
        }
        this.f3818v = true;
    }
}
