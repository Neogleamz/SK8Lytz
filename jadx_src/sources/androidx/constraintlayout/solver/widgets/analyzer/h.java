package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.d;
import androidx.constraintlayout.solver.widgets.analyzer.k;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h extends k {

    /* renamed from: k  reason: collision with root package name */
    private static int[] f3769k = new int[2];

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f3770a;

        static {
            int[] iArr = new int[k.b.values().length];
            f3770a = iArr;
            try {
                iArr[k.b.START.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3770a[k.b.END.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3770a[k.b.CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public h(ConstraintWidget constraintWidget) {
        super(constraintWidget);
        this.f3789h.f3751e = d.a.LEFT;
        this.f3790i.f3751e = d.a.RIGHT;
        this.f3787f = 0;
    }

    private void q(int[] iArr, int i8, int i9, int i10, int i11, float f5, int i12) {
        int i13 = i9 - i8;
        int i14 = i11 - i10;
        if (i12 != -1) {
            if (i12 == 0) {
                iArr[0] = (int) ((i14 * f5) + 0.5f);
                iArr[1] = i14;
                return;
            } else if (i12 != 1) {
                return;
            } else {
                iArr[0] = i13;
                iArr[1] = (int) ((i13 * f5) + 0.5f);
                return;
            }
        }
        int i15 = (int) ((i14 * f5) + 0.5f);
        int i16 = (int) ((i13 / f5) + 0.5f);
        if (i15 <= i13) {
            iArr[0] = i15;
            iArr[1] = i14;
        } else if (i16 <= i14) {
            iArr[0] = i13;
            iArr[1] = i16;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:116:0x0292, code lost:
        if (r14 != 1) goto L130;
     */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k, o0.a
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(o0.a r17) {
        /*
            Method dump skipped, instructions count: 1032
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.h.a(o0.a):void");
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    void d() {
        ConstraintWidget H;
        d dVar;
        d dVar2;
        int R;
        d dVar3;
        ConstraintAnchor constraintAnchor;
        List<o0.a> list;
        o0.a aVar;
        d dVar4;
        d dVar5;
        d dVar6;
        int R2;
        d dVar7;
        d dVar8;
        int i8;
        ConstraintWidget H2;
        ConstraintWidget constraintWidget = this.f3783b;
        if (constraintWidget.f3664a) {
            this.f3786e.d(constraintWidget.Q());
        }
        if (this.f3786e.f3756j) {
            ConstraintWidget.DimensionBehaviour dimensionBehaviour = this.f3785d;
            ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.MATCH_PARENT;
            if (dimensionBehaviour == dimensionBehaviour2 && (((H = this.f3783b.H()) != null && H.z() == ConstraintWidget.DimensionBehaviour.FIXED) || H.z() == dimensionBehaviour2)) {
                b(this.f3789h, H.f3672e.f3789h, this.f3783b.D.c());
                b(this.f3790i, H.f3672e.f3790i, -this.f3783b.F.c());
                return;
            }
        } else {
            ConstraintWidget.DimensionBehaviour z4 = this.f3783b.z();
            this.f3785d = z4;
            if (z4 != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.MATCH_PARENT;
                if (z4 == dimensionBehaviour3 && (((H2 = this.f3783b.H()) != null && H2.z() == ConstraintWidget.DimensionBehaviour.FIXED) || H2.z() == dimensionBehaviour3)) {
                    int Q = (H2.Q() - this.f3783b.D.c()) - this.f3783b.F.c();
                    b(this.f3789h, H2.f3672e.f3789h, this.f3783b.D.c());
                    b(this.f3790i, H2.f3672e.f3790i, -this.f3783b.F.c());
                    this.f3786e.d(Q);
                    return;
                } else if (this.f3785d == ConstraintWidget.DimensionBehaviour.FIXED) {
                    this.f3786e.d(this.f3783b.Q());
                }
            }
        }
        e eVar = this.f3786e;
        if (eVar.f3756j) {
            ConstraintWidget constraintWidget2 = this.f3783b;
            if (constraintWidget2.f3664a) {
                ConstraintAnchor[] constraintAnchorArr = constraintWidget2.L;
                if (constraintAnchorArr[0].f3649d != null && constraintAnchorArr[1].f3649d != null) {
                    if (constraintWidget2.W()) {
                        this.f3789h.f3752f = this.f3783b.L[0].c();
                        dVar3 = this.f3790i;
                        constraintAnchor = this.f3783b.L[1];
                        dVar3.f3752f = -constraintAnchor.c();
                        return;
                    }
                    d h8 = h(this.f3783b.L[0]);
                    if (h8 != null) {
                        b(this.f3789h, h8, this.f3783b.L[0].c());
                    }
                    d h9 = h(this.f3783b.L[1]);
                    if (h9 != null) {
                        b(this.f3790i, h9, -this.f3783b.L[1].c());
                    }
                    this.f3789h.f3748b = true;
                    this.f3790i.f3748b = true;
                    return;
                }
                if (constraintAnchorArr[0].f3649d != null) {
                    dVar5 = h(constraintAnchorArr[0]);
                    if (dVar5 == null) {
                        return;
                    }
                    dVar6 = this.f3789h;
                    R2 = this.f3783b.L[0].c();
                } else if (constraintAnchorArr[1].f3649d != null) {
                    d h10 = h(constraintAnchorArr[1]);
                    if (h10 != null) {
                        b(this.f3790i, h10, -this.f3783b.L[1].c());
                        dVar7 = this.f3789h;
                        dVar8 = this.f3790i;
                        i8 = -this.f3786e.f3753g;
                        b(dVar7, dVar8, i8);
                        return;
                    }
                    return;
                } else if ((constraintWidget2 instanceof n0.a) || constraintWidget2.H() == null || this.f3783b.n(ConstraintAnchor.Type.CENTER).f3649d != null) {
                    return;
                } else {
                    dVar5 = this.f3783b.H().f3672e.f3789h;
                    dVar6 = this.f3789h;
                    R2 = this.f3783b.R();
                }
                b(dVar6, dVar5, R2);
                dVar7 = this.f3790i;
                dVar8 = this.f3789h;
                i8 = this.f3786e.f3753g;
                b(dVar7, dVar8, i8);
                return;
            }
        }
        if (this.f3785d == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            ConstraintWidget constraintWidget3 = this.f3783b;
            int i9 = constraintWidget3.f3686l;
            if (i9 == 2) {
                ConstraintWidget H3 = constraintWidget3.H();
                if (H3 != null) {
                    e eVar2 = H3.f3674f.f3786e;
                    this.f3786e.f3758l.add(eVar2);
                    eVar2.f3757k.add(this.f3786e);
                    e eVar3 = this.f3786e;
                    eVar3.f3748b = true;
                    eVar3.f3757k.add(this.f3789h);
                    list = this.f3786e.f3757k;
                    aVar = this.f3790i;
                    list.add(aVar);
                }
            } else if (i9 == 3) {
                if (constraintWidget3.f3688m == 3) {
                    this.f3789h.f3747a = this;
                    this.f3790i.f3747a = this;
                    j jVar = constraintWidget3.f3674f;
                    jVar.f3789h.f3747a = this;
                    jVar.f3790i.f3747a = this;
                    eVar.f3747a = this;
                    if (constraintWidget3.Y()) {
                        this.f3786e.f3758l.add(this.f3783b.f3674f.f3786e);
                        this.f3783b.f3674f.f3786e.f3757k.add(this.f3786e);
                        j jVar2 = this.f3783b.f3674f;
                        jVar2.f3786e.f3747a = this;
                        this.f3786e.f3758l.add(jVar2.f3789h);
                        this.f3786e.f3758l.add(this.f3783b.f3674f.f3790i);
                        this.f3783b.f3674f.f3789h.f3757k.add(this.f3786e);
                        list = this.f3783b.f3674f.f3790i.f3757k;
                        aVar = this.f3786e;
                        list.add(aVar);
                    } else if (this.f3783b.W()) {
                        this.f3783b.f3674f.f3786e.f3758l.add(this.f3786e);
                        list = this.f3786e.f3757k;
                        aVar = this.f3783b.f3674f.f3786e;
                        list.add(aVar);
                    } else {
                        dVar4 = this.f3783b.f3674f.f3786e;
                    }
                } else {
                    e eVar4 = constraintWidget3.f3674f.f3786e;
                    eVar.f3758l.add(eVar4);
                    eVar4.f3757k.add(this.f3786e);
                    this.f3783b.f3674f.f3789h.f3757k.add(this.f3786e);
                    this.f3783b.f3674f.f3790i.f3757k.add(this.f3786e);
                    e eVar5 = this.f3786e;
                    eVar5.f3748b = true;
                    eVar5.f3757k.add(this.f3789h);
                    this.f3786e.f3757k.add(this.f3790i);
                    this.f3789h.f3758l.add(this.f3786e);
                    dVar4 = this.f3790i;
                }
                list = dVar4.f3758l;
                aVar = this.f3786e;
                list.add(aVar);
            }
            dVar3.f3752f = -constraintAnchor.c();
            return;
        }
        ConstraintWidget constraintWidget4 = this.f3783b;
        ConstraintAnchor[] constraintAnchorArr2 = constraintWidget4.L;
        if (constraintAnchorArr2[0].f3649d != null && constraintAnchorArr2[1].f3649d != null) {
            if (constraintWidget4.W()) {
                this.f3789h.f3752f = this.f3783b.L[0].c();
                dVar3 = this.f3790i;
                constraintAnchor = this.f3783b.L[1];
                dVar3.f3752f = -constraintAnchor.c();
                return;
            }
            d h11 = h(this.f3783b.L[0]);
            d h12 = h(this.f3783b.L[1]);
            h11.b(this);
            h12.b(this);
            this.f3791j = k.b.CENTER;
            return;
        }
        if (constraintAnchorArr2[0].f3649d != null) {
            dVar = h(constraintAnchorArr2[0]);
            if (dVar == null) {
                return;
            }
            dVar2 = this.f3789h;
            R = this.f3783b.L[0].c();
        } else if (constraintAnchorArr2[1].f3649d != null) {
            d h13 = h(constraintAnchorArr2[1]);
            if (h13 != null) {
                b(this.f3790i, h13, -this.f3783b.L[1].c());
                c(this.f3789h, this.f3790i, -1, this.f3786e);
                return;
            }
            return;
        } else if ((constraintWidget4 instanceof n0.a) || constraintWidget4.H() == null) {
            return;
        } else {
            dVar = this.f3783b.H().f3672e.f3789h;
            dVar2 = this.f3789h;
            R = this.f3783b.R();
        }
        b(dVar2, dVar, R);
        c(this.f3790i, this.f3789h, 1, this.f3786e);
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public void e() {
        d dVar = this.f3789h;
        if (dVar.f3756j) {
            this.f3783b.G0(dVar.f3753g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public void f() {
        this.f3784c = null;
        this.f3789h.c();
        this.f3790i.c();
        this.f3786e.c();
        this.f3788g = false;
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    boolean m() {
        return this.f3785d != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.f3783b.f3686l == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r() {
        this.f3788g = false;
        this.f3789h.c();
        this.f3789h.f3756j = false;
        this.f3790i.c();
        this.f3790i.f3756j = false;
        this.f3786e.f3756j = false;
    }

    public String toString() {
        return "HorizontalRun " + this.f3783b.s();
    }
}
