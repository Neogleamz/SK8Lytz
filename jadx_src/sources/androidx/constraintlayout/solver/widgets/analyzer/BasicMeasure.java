package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class BasicMeasure {

    /* renamed from: a  reason: collision with root package name */
    private final ArrayList<ConstraintWidget> f3722a = new ArrayList<>();

    /* renamed from: b  reason: collision with root package name */
    private a f3723b = new a();

    /* renamed from: c  reason: collision with root package name */
    private androidx.constraintlayout.solver.widgets.d f3724c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum MeasureType {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        public ConstraintWidget.DimensionBehaviour f3726a;

        /* renamed from: b  reason: collision with root package name */
        public ConstraintWidget.DimensionBehaviour f3727b;

        /* renamed from: c  reason: collision with root package name */
        public int f3728c;

        /* renamed from: d  reason: collision with root package name */
        public int f3729d;

        /* renamed from: e  reason: collision with root package name */
        public int f3730e;

        /* renamed from: f  reason: collision with root package name */
        public int f3731f;

        /* renamed from: g  reason: collision with root package name */
        public int f3732g;

        /* renamed from: h  reason: collision with root package name */
        public boolean f3733h;

        /* renamed from: i  reason: collision with root package name */
        public boolean f3734i;

        /* renamed from: j  reason: collision with root package name */
        public boolean f3735j;
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a();

        void b(ConstraintWidget constraintWidget, a aVar);
    }

    public BasicMeasure(androidx.constraintlayout.solver.widgets.d dVar) {
        this.f3724c = dVar;
    }

    private boolean a(b bVar, ConstraintWidget constraintWidget, boolean z4) {
        this.f3723b.f3726a = constraintWidget.z();
        this.f3723b.f3727b = constraintWidget.N();
        this.f3723b.f3728c = constraintWidget.Q();
        this.f3723b.f3729d = constraintWidget.w();
        a aVar = this.f3723b;
        aVar.f3734i = false;
        aVar.f3735j = z4;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = aVar.f3726a;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean z8 = dimensionBehaviour == dimensionBehaviour2;
        boolean z9 = aVar.f3727b == dimensionBehaviour2;
        boolean z10 = z8 && constraintWidget.S > 0.0f;
        boolean z11 = z9 && constraintWidget.S > 0.0f;
        if (z10 && constraintWidget.f3690n[0] == 4) {
            aVar.f3726a = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        if (z11 && constraintWidget.f3690n[1] == 4) {
            aVar.f3727b = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        bVar.b(constraintWidget, aVar);
        constraintWidget.F0(this.f3723b.f3730e);
        constraintWidget.i0(this.f3723b.f3731f);
        constraintWidget.h0(this.f3723b.f3733h);
        constraintWidget.c0(this.f3723b.f3732g);
        a aVar2 = this.f3723b;
        aVar2.f3735j = false;
        return aVar2.f3734i;
    }

    private void b(androidx.constraintlayout.solver.widgets.d dVar) {
        int size = dVar.G0.size();
        b W0 = dVar.W0();
        for (int i8 = 0; i8 < size; i8++) {
            ConstraintWidget constraintWidget = dVar.G0.get(i8);
            if (!(constraintWidget instanceof androidx.constraintlayout.solver.widgets.f) && (!constraintWidget.f3672e.f3786e.f3756j || !constraintWidget.f3674f.f3786e.f3756j)) {
                ConstraintWidget.DimensionBehaviour t8 = constraintWidget.t(0);
                boolean z4 = true;
                ConstraintWidget.DimensionBehaviour t9 = constraintWidget.t(1);
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                if (!((t8 != dimensionBehaviour || constraintWidget.f3686l == 1 || t9 != dimensionBehaviour || constraintWidget.f3688m == 1) ? false : false)) {
                    a(W0, constraintWidget, false);
                }
            }
        }
        W0.a();
    }

    private void c(androidx.constraintlayout.solver.widgets.d dVar, String str, int i8, int i9) {
        int F = dVar.F();
        int E = dVar.E();
        dVar.v0(0);
        dVar.u0(0);
        dVar.F0(i8);
        dVar.i0(i9);
        dVar.v0(F);
        dVar.u0(E);
        this.f3724c.M0();
    }

    public long d(androidx.constraintlayout.solver.widgets.d dVar, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16) {
        boolean z4;
        int i17;
        boolean z8;
        boolean z9;
        int i18;
        int i19;
        b bVar;
        int i20;
        boolean z10;
        boolean z11;
        int i21;
        b W0 = dVar.W0();
        int size = dVar.G0.size();
        int Q = dVar.Q();
        int w8 = dVar.w();
        boolean b9 = androidx.constraintlayout.solver.widgets.g.b(i8, RecognitionOptions.ITF);
        boolean z12 = b9 || androidx.constraintlayout.solver.widgets.g.b(i8, 64);
        if (z12) {
            for (int i22 = 0; i22 < size; i22++) {
                ConstraintWidget constraintWidget = dVar.G0.get(i22);
                ConstraintWidget.DimensionBehaviour z13 = constraintWidget.z();
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                boolean z14 = (z13 == dimensionBehaviour) && (constraintWidget.N() == dimensionBehaviour) && constraintWidget.u() > 0.0f;
                if ((constraintWidget.W() && z14) || ((constraintWidget.Y() && z14) || (constraintWidget instanceof androidx.constraintlayout.solver.widgets.h) || constraintWidget.W() || constraintWidget.Y())) {
                    z12 = false;
                    break;
                }
            }
        }
        if (z12) {
            m0.a aVar = androidx.constraintlayout.solver.d.f3560r;
        }
        int i23 = 2;
        if (z12 && ((i11 == 1073741824 && i13 == 1073741824) || b9)) {
            int min = Math.min(dVar.D(), i12);
            int min2 = Math.min(dVar.C(), i14);
            if (i11 == 1073741824 && dVar.Q() != min) {
                dVar.F0(min);
                dVar.Z0();
            }
            if (i13 == 1073741824 && dVar.w() != min2) {
                dVar.i0(min2);
                dVar.Z0();
            }
            if (i11 == 1073741824 && i13 == 1073741824) {
                z4 = dVar.T0(b9);
                i17 = 2;
            } else {
                boolean U0 = dVar.U0(b9);
                if (i11 == 1073741824) {
                    z11 = U0 & dVar.V0(b9, 0);
                    i21 = 1;
                } else {
                    z11 = U0;
                    i21 = 0;
                }
                if (i13 == 1073741824) {
                    boolean V0 = dVar.V0(b9, 1) & z11;
                    i17 = i21 + 1;
                    z4 = V0;
                } else {
                    i17 = i21;
                    z4 = z11;
                }
            }
            if (z4) {
                dVar.J0(i11 == 1073741824, i13 == 1073741824);
            }
        } else {
            z4 = false;
            i17 = 0;
        }
        if (z4 && i17 == 2) {
            return 0L;
        }
        if (size > 0) {
            b(dVar);
        }
        int X0 = dVar.X0();
        int size2 = this.f3722a.size();
        if (size > 0) {
            c(dVar, "First pass", Q, w8);
        }
        if (size2 > 0) {
            ConstraintWidget.DimensionBehaviour z15 = dVar.z();
            ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            boolean z16 = z15 == dimensionBehaviour2;
            boolean z17 = dVar.N() == dimensionBehaviour2;
            int max = Math.max(dVar.Q(), this.f3724c.F());
            int max2 = Math.max(dVar.w(), this.f3724c.E());
            int i24 = 0;
            boolean z18 = false;
            while (i24 < size2) {
                ConstraintWidget constraintWidget2 = this.f3722a.get(i24);
                if (constraintWidget2 instanceof androidx.constraintlayout.solver.widgets.h) {
                    int Q2 = constraintWidget2.Q();
                    int w9 = constraintWidget2.w();
                    i20 = X0;
                    boolean a9 = z18 | a(W0, constraintWidget2, true);
                    int Q3 = constraintWidget2.Q();
                    int w10 = constraintWidget2.w();
                    if (Q3 != Q2) {
                        constraintWidget2.F0(Q3);
                        if (z16 && constraintWidget2.J() > max) {
                            max = Math.max(max, constraintWidget2.J() + constraintWidget2.n(ConstraintAnchor.Type.RIGHT).c());
                        }
                        z10 = true;
                    } else {
                        z10 = a9;
                    }
                    if (w10 != w9) {
                        constraintWidget2.i0(w10);
                        if (z17 && constraintWidget2.q() > max2) {
                            max2 = Math.max(max2, constraintWidget2.q() + constraintWidget2.n(ConstraintAnchor.Type.BOTTOM).c());
                        }
                        z10 = true;
                    }
                    z18 = z10 | ((androidx.constraintlayout.solver.widgets.h) constraintWidget2).W0();
                } else {
                    i20 = X0;
                }
                i24++;
                X0 = i20;
                i23 = 2;
            }
            int i25 = X0;
            int i26 = 0;
            for (int i27 = i23; i26 < i27; i27 = 2) {
                int i28 = 0;
                while (i28 < size2) {
                    ConstraintWidget constraintWidget3 = this.f3722a.get(i28);
                    if (((constraintWidget3 instanceof n0.a) && !(constraintWidget3 instanceof androidx.constraintlayout.solver.widgets.h)) || (constraintWidget3 instanceof androidx.constraintlayout.solver.widgets.f) || constraintWidget3.P() == 8 || ((constraintWidget3.f3672e.f3786e.f3756j && constraintWidget3.f3674f.f3786e.f3756j) || (constraintWidget3 instanceof androidx.constraintlayout.solver.widgets.h))) {
                        i19 = i26;
                        i18 = size2;
                        bVar = W0;
                    } else {
                        int Q4 = constraintWidget3.Q();
                        int w11 = constraintWidget3.w();
                        i18 = size2;
                        int o5 = constraintWidget3.o();
                        i19 = i26;
                        z18 |= a(W0, constraintWidget3, true);
                        int Q5 = constraintWidget3.Q();
                        bVar = W0;
                        int w12 = constraintWidget3.w();
                        if (Q5 != Q4) {
                            constraintWidget3.F0(Q5);
                            if (z16 && constraintWidget3.J() > max) {
                                max = Math.max(max, constraintWidget3.J() + constraintWidget3.n(ConstraintAnchor.Type.RIGHT).c());
                            }
                            z18 = true;
                        }
                        if (w12 != w11) {
                            constraintWidget3.i0(w12);
                            if (z17 && constraintWidget3.q() > max2) {
                                max2 = Math.max(max2, constraintWidget3.q() + constraintWidget3.n(ConstraintAnchor.Type.BOTTOM).c());
                            }
                            z18 = true;
                        }
                        if (constraintWidget3.T() && o5 != constraintWidget3.o()) {
                            z18 = true;
                        }
                    }
                    i28++;
                    size2 = i18;
                    W0 = bVar;
                    i26 = i19;
                }
                int i29 = i26;
                int i30 = size2;
                b bVar2 = W0;
                if (z18) {
                    c(dVar, "intermediate pass", Q, w8);
                    z18 = false;
                }
                i26 = i29 + 1;
                size2 = i30;
                W0 = bVar2;
            }
            if (z18) {
                c(dVar, "2nd pass", Q, w8);
                if (dVar.Q() < max) {
                    dVar.F0(max);
                    z8 = true;
                } else {
                    z8 = false;
                }
                if (dVar.w() < max2) {
                    dVar.i0(max2);
                    z9 = true;
                } else {
                    z9 = z8;
                }
                if (z9) {
                    c(dVar, "3rd pass", Q, w8);
                }
            }
            X0 = i25;
        }
        dVar.i1(X0);
        return 0L;
    }

    public void e(androidx.constraintlayout.solver.widgets.d dVar) {
        int i8;
        this.f3722a.clear();
        int size = dVar.G0.size();
        while (i8 < size) {
            ConstraintWidget constraintWidget = dVar.G0.get(i8);
            ConstraintWidget.DimensionBehaviour z4 = constraintWidget.z();
            ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
            if (z4 != dimensionBehaviour) {
                ConstraintWidget.DimensionBehaviour z8 = constraintWidget.z();
                ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.MATCH_PARENT;
                i8 = (z8 == dimensionBehaviour2 || constraintWidget.N() == dimensionBehaviour || constraintWidget.N() == dimensionBehaviour2) ? 0 : i8 + 1;
            }
            this.f3722a.add(constraintWidget);
        }
        dVar.Z0();
    }
}
