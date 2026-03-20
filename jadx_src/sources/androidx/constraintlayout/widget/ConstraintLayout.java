package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ConstraintLayout extends ViewGroup {
    b A;
    private int B;
    private int C;

    /* renamed from: a  reason: collision with root package name */
    SparseArray<View> f3937a;

    /* renamed from: b  reason: collision with root package name */
    private ArrayList<ConstraintHelper> f3938b;

    /* renamed from: c  reason: collision with root package name */
    protected androidx.constraintlayout.solver.widgets.d f3939c;

    /* renamed from: d  reason: collision with root package name */
    private int f3940d;

    /* renamed from: e  reason: collision with root package name */
    private int f3941e;

    /* renamed from: f  reason: collision with root package name */
    private int f3942f;

    /* renamed from: g  reason: collision with root package name */
    private int f3943g;

    /* renamed from: h  reason: collision with root package name */
    protected boolean f3944h;

    /* renamed from: j  reason: collision with root package name */
    private int f3945j;

    /* renamed from: k  reason: collision with root package name */
    private androidx.constraintlayout.widget.b f3946k;

    /* renamed from: l  reason: collision with root package name */
    protected androidx.constraintlayout.widget.a f3947l;

    /* renamed from: m  reason: collision with root package name */
    private int f3948m;

    /* renamed from: n  reason: collision with root package name */
    private HashMap<String, Integer> f3949n;

    /* renamed from: p  reason: collision with root package name */
    private int f3950p;
    private int q;

    /* renamed from: t  reason: collision with root package name */
    int f3951t;

    /* renamed from: w  reason: collision with root package name */
    int f3952w;

    /* renamed from: x  reason: collision with root package name */
    int f3953x;

    /* renamed from: y  reason: collision with root package name */
    int f3954y;

    /* renamed from: z  reason: collision with root package name */
    private SparseArray<ConstraintWidget> f3955z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public float A;
        public String B;
        float C;
        int D;
        public float E;
        public float F;
        public int G;
        public int H;
        public int I;
        public int J;
        public int K;
        public int L;
        public int M;
        public int N;
        public float O;
        public float P;
        public int Q;
        public int R;
        public int S;
        public boolean T;
        public boolean U;
        public String V;
        boolean W;
        boolean X;
        boolean Y;
        boolean Z;

        /* renamed from: a  reason: collision with root package name */
        public int f3956a;

        /* renamed from: a0  reason: collision with root package name */
        boolean f3957a0;

        /* renamed from: b  reason: collision with root package name */
        public int f3958b;

        /* renamed from: b0  reason: collision with root package name */
        boolean f3959b0;

        /* renamed from: c  reason: collision with root package name */
        public float f3960c;

        /* renamed from: c0  reason: collision with root package name */
        boolean f3961c0;

        /* renamed from: d  reason: collision with root package name */
        public int f3962d;

        /* renamed from: d0  reason: collision with root package name */
        int f3963d0;

        /* renamed from: e  reason: collision with root package name */
        public int f3964e;

        /* renamed from: e0  reason: collision with root package name */
        int f3965e0;

        /* renamed from: f  reason: collision with root package name */
        public int f3966f;

        /* renamed from: f0  reason: collision with root package name */
        int f3967f0;

        /* renamed from: g  reason: collision with root package name */
        public int f3968g;

        /* renamed from: g0  reason: collision with root package name */
        int f3969g0;

        /* renamed from: h  reason: collision with root package name */
        public int f3970h;

        /* renamed from: h0  reason: collision with root package name */
        int f3971h0;

        /* renamed from: i  reason: collision with root package name */
        public int f3972i;

        /* renamed from: i0  reason: collision with root package name */
        int f3973i0;

        /* renamed from: j  reason: collision with root package name */
        public int f3974j;

        /* renamed from: j0  reason: collision with root package name */
        float f3975j0;

        /* renamed from: k  reason: collision with root package name */
        public int f3976k;

        /* renamed from: k0  reason: collision with root package name */
        int f3977k0;

        /* renamed from: l  reason: collision with root package name */
        public int f3978l;

        /* renamed from: l0  reason: collision with root package name */
        int f3979l0;

        /* renamed from: m  reason: collision with root package name */
        public int f3980m;

        /* renamed from: m0  reason: collision with root package name */
        float f3981m0;

        /* renamed from: n  reason: collision with root package name */
        public int f3982n;

        /* renamed from: n0  reason: collision with root package name */
        ConstraintWidget f3983n0;

        /* renamed from: o  reason: collision with root package name */
        public float f3984o;

        /* renamed from: o0  reason: collision with root package name */
        public boolean f3985o0;

        /* renamed from: p  reason: collision with root package name */
        public int f3986p;
        public int q;

        /* renamed from: r  reason: collision with root package name */
        public int f3987r;

        /* renamed from: s  reason: collision with root package name */
        public int f3988s;

        /* renamed from: t  reason: collision with root package name */
        public int f3989t;

        /* renamed from: u  reason: collision with root package name */
        public int f3990u;

        /* renamed from: v  reason: collision with root package name */
        public int f3991v;

        /* renamed from: w  reason: collision with root package name */
        public int f3992w;

        /* renamed from: x  reason: collision with root package name */
        public int f3993x;

        /* renamed from: y  reason: collision with root package name */
        public int f3994y;

        /* renamed from: z  reason: collision with root package name */
        public float f3995z;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private static class a {

            /* renamed from: a  reason: collision with root package name */
            public static final SparseIntArray f3996a;

            static {
                SparseIntArray sparseIntArray = new SparseIntArray();
                f3996a = sparseIntArray;
                sparseIntArray.append(e.f4219l2, 8);
                sparseIntArray.append(e.f4228m2, 9);
                sparseIntArray.append(e.f4246o2, 10);
                sparseIntArray.append(e.f4254p2, 11);
                sparseIntArray.append(e.f4306v2, 12);
                sparseIntArray.append(e.f4297u2, 13);
                sparseIntArray.append(e.T1, 14);
                sparseIntArray.append(e.S1, 15);
                sparseIntArray.append(e.Q1, 16);
                sparseIntArray.append(e.U1, 2);
                sparseIntArray.append(e.W1, 3);
                sparseIntArray.append(e.V1, 4);
                sparseIntArray.append(e.D2, 49);
                sparseIntArray.append(e.E2, 50);
                sparseIntArray.append(e.f4118a2, 5);
                sparseIntArray.append(e.f4128b2, 6);
                sparseIntArray.append(e.f4138c2, 7);
                sparseIntArray.append(e.f4127b1, 1);
                sparseIntArray.append(e.f4262q2, 17);
                sparseIntArray.append(e.f4271r2, 18);
                sparseIntArray.append(e.Z1, 19);
                sparseIntArray.append(e.Y1, 20);
                sparseIntArray.append(e.H2, 21);
                sparseIntArray.append(e.K2, 22);
                sparseIntArray.append(e.I2, 23);
                sparseIntArray.append(e.F2, 24);
                sparseIntArray.append(e.J2, 25);
                sparseIntArray.append(e.G2, 26);
                sparseIntArray.append(e.f4183h2, 29);
                sparseIntArray.append(e.f4315w2, 30);
                sparseIntArray.append(e.X1, 44);
                sparseIntArray.append(e.f4201j2, 45);
                sparseIntArray.append(e.f4333y2, 46);
                sparseIntArray.append(e.f4192i2, 47);
                sparseIntArray.append(e.f4324x2, 48);
                sparseIntArray.append(e.O1, 27);
                sparseIntArray.append(e.N1, 28);
                sparseIntArray.append(e.f4342z2, 31);
                sparseIntArray.append(e.f4148d2, 32);
                sparseIntArray.append(e.B2, 33);
                sparseIntArray.append(e.A2, 34);
                sparseIntArray.append(e.C2, 35);
                sparseIntArray.append(e.f4166f2, 36);
                sparseIntArray.append(e.f4157e2, 37);
                sparseIntArray.append(e.f4174g2, 38);
                sparseIntArray.append(e.f4210k2, 39);
                sparseIntArray.append(e.f4288t2, 40);
                sparseIntArray.append(e.f4237n2, 41);
                sparseIntArray.append(e.R1, 42);
                sparseIntArray.append(e.P1, 43);
                sparseIntArray.append(e.f4279s2, 51);
            }
        }

        public LayoutParams(int i8, int i9) {
            super(i8, i9);
            this.f3956a = -1;
            this.f3958b = -1;
            this.f3960c = -1.0f;
            this.f3962d = -1;
            this.f3964e = -1;
            this.f3966f = -1;
            this.f3968g = -1;
            this.f3970h = -1;
            this.f3972i = -1;
            this.f3974j = -1;
            this.f3976k = -1;
            this.f3978l = -1;
            this.f3980m = -1;
            this.f3982n = 0;
            this.f3984o = 0.0f;
            this.f3986p = -1;
            this.q = -1;
            this.f3987r = -1;
            this.f3988s = -1;
            this.f3989t = -1;
            this.f3990u = -1;
            this.f3991v = -1;
            this.f3992w = -1;
            this.f3993x = -1;
            this.f3994y = -1;
            this.f3995z = 0.5f;
            this.A = 0.5f;
            this.B = null;
            this.C = 0.0f;
            this.D = 1;
            this.E = -1.0f;
            this.F = -1.0f;
            this.G = 0;
            this.H = 0;
            this.I = 0;
            this.J = 0;
            this.K = 0;
            this.L = 0;
            this.M = 0;
            this.N = 0;
            this.O = 1.0f;
            this.P = 1.0f;
            this.Q = -1;
            this.R = -1;
            this.S = -1;
            this.T = false;
            this.U = false;
            this.V = null;
            this.W = true;
            this.X = true;
            this.Y = false;
            this.Z = false;
            this.f3957a0 = false;
            this.f3959b0 = false;
            this.f3961c0 = false;
            this.f3963d0 = -1;
            this.f3965e0 = -1;
            this.f3967f0 = -1;
            this.f3969g0 = -1;
            this.f3971h0 = -1;
            this.f3973i0 = -1;
            this.f3975j0 = 0.5f;
            this.f3983n0 = new ConstraintWidget();
            this.f3985o0 = false;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            String str;
            int i8;
            float parseFloat;
            this.f3956a = -1;
            this.f3958b = -1;
            this.f3960c = -1.0f;
            this.f3962d = -1;
            this.f3964e = -1;
            this.f3966f = -1;
            this.f3968g = -1;
            this.f3970h = -1;
            this.f3972i = -1;
            this.f3974j = -1;
            this.f3976k = -1;
            this.f3978l = -1;
            this.f3980m = -1;
            this.f3982n = 0;
            this.f3984o = 0.0f;
            this.f3986p = -1;
            this.q = -1;
            this.f3987r = -1;
            this.f3988s = -1;
            this.f3989t = -1;
            this.f3990u = -1;
            this.f3991v = -1;
            this.f3992w = -1;
            this.f3993x = -1;
            this.f3994y = -1;
            this.f3995z = 0.5f;
            this.A = 0.5f;
            this.B = null;
            this.C = 0.0f;
            this.D = 1;
            this.E = -1.0f;
            this.F = -1.0f;
            this.G = 0;
            this.H = 0;
            this.I = 0;
            this.J = 0;
            this.K = 0;
            this.L = 0;
            this.M = 0;
            this.N = 0;
            this.O = 1.0f;
            this.P = 1.0f;
            this.Q = -1;
            this.R = -1;
            this.S = -1;
            this.T = false;
            this.U = false;
            this.V = null;
            this.W = true;
            this.X = true;
            this.Y = false;
            this.Z = false;
            this.f3957a0 = false;
            this.f3959b0 = false;
            this.f3961c0 = false;
            this.f3963d0 = -1;
            this.f3965e0 = -1;
            this.f3967f0 = -1;
            this.f3969g0 = -1;
            this.f3971h0 = -1;
            this.f3973i0 = -1;
            this.f3975j0 = 0.5f;
            this.f3983n0 = new ConstraintWidget();
            this.f3985o0 = false;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, e.f4117a1);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i9 = 0; i9 < indexCount; i9++) {
                int index = obtainStyledAttributes.getIndex(i9);
                int i10 = a.f3996a.get(index);
                switch (i10) {
                    case 1:
                        this.S = obtainStyledAttributes.getInt(index, this.S);
                        continue;
                    case 2:
                        int resourceId = obtainStyledAttributes.getResourceId(index, this.f3980m);
                        this.f3980m = resourceId;
                        if (resourceId == -1) {
                            this.f3980m = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 3:
                        this.f3982n = obtainStyledAttributes.getDimensionPixelSize(index, this.f3982n);
                        continue;
                    case 4:
                        float f5 = obtainStyledAttributes.getFloat(index, this.f3984o) % 360.0f;
                        this.f3984o = f5;
                        if (f5 < 0.0f) {
                            this.f3984o = (360.0f - f5) % 360.0f;
                        } else {
                            continue;
                        }
                    case 5:
                        this.f3956a = obtainStyledAttributes.getDimensionPixelOffset(index, this.f3956a);
                        continue;
                    case 6:
                        this.f3958b = obtainStyledAttributes.getDimensionPixelOffset(index, this.f3958b);
                        continue;
                    case 7:
                        this.f3960c = obtainStyledAttributes.getFloat(index, this.f3960c);
                        continue;
                    case 8:
                        int resourceId2 = obtainStyledAttributes.getResourceId(index, this.f3962d);
                        this.f3962d = resourceId2;
                        if (resourceId2 == -1) {
                            this.f3962d = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 9:
                        int resourceId3 = obtainStyledAttributes.getResourceId(index, this.f3964e);
                        this.f3964e = resourceId3;
                        if (resourceId3 == -1) {
                            this.f3964e = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 10:
                        int resourceId4 = obtainStyledAttributes.getResourceId(index, this.f3966f);
                        this.f3966f = resourceId4;
                        if (resourceId4 == -1) {
                            this.f3966f = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 11:
                        int resourceId5 = obtainStyledAttributes.getResourceId(index, this.f3968g);
                        this.f3968g = resourceId5;
                        if (resourceId5 == -1) {
                            this.f3968g = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 12:
                        int resourceId6 = obtainStyledAttributes.getResourceId(index, this.f3970h);
                        this.f3970h = resourceId6;
                        if (resourceId6 == -1) {
                            this.f3970h = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 13:
                        int resourceId7 = obtainStyledAttributes.getResourceId(index, this.f3972i);
                        this.f3972i = resourceId7;
                        if (resourceId7 == -1) {
                            this.f3972i = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 14:
                        int resourceId8 = obtainStyledAttributes.getResourceId(index, this.f3974j);
                        this.f3974j = resourceId8;
                        if (resourceId8 == -1) {
                            this.f3974j = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 15:
                        int resourceId9 = obtainStyledAttributes.getResourceId(index, this.f3976k);
                        this.f3976k = resourceId9;
                        if (resourceId9 == -1) {
                            this.f3976k = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 16:
                        int resourceId10 = obtainStyledAttributes.getResourceId(index, this.f3978l);
                        this.f3978l = resourceId10;
                        if (resourceId10 == -1) {
                            this.f3978l = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 17:
                        int resourceId11 = obtainStyledAttributes.getResourceId(index, this.f3986p);
                        this.f3986p = resourceId11;
                        if (resourceId11 == -1) {
                            this.f3986p = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 18:
                        int resourceId12 = obtainStyledAttributes.getResourceId(index, this.q);
                        this.q = resourceId12;
                        if (resourceId12 == -1) {
                            this.q = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 19:
                        int resourceId13 = obtainStyledAttributes.getResourceId(index, this.f3987r);
                        this.f3987r = resourceId13;
                        if (resourceId13 == -1) {
                            this.f3987r = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 20:
                        int resourceId14 = obtainStyledAttributes.getResourceId(index, this.f3988s);
                        this.f3988s = resourceId14;
                        if (resourceId14 == -1) {
                            this.f3988s = obtainStyledAttributes.getInt(index, -1);
                        } else {
                            continue;
                        }
                    case 21:
                        this.f3989t = obtainStyledAttributes.getDimensionPixelSize(index, this.f3989t);
                        continue;
                    case 22:
                        this.f3990u = obtainStyledAttributes.getDimensionPixelSize(index, this.f3990u);
                        continue;
                    case 23:
                        this.f3991v = obtainStyledAttributes.getDimensionPixelSize(index, this.f3991v);
                        continue;
                    case 24:
                        this.f3992w = obtainStyledAttributes.getDimensionPixelSize(index, this.f3992w);
                        continue;
                    case 25:
                        this.f3993x = obtainStyledAttributes.getDimensionPixelSize(index, this.f3993x);
                        continue;
                    case 26:
                        this.f3994y = obtainStyledAttributes.getDimensionPixelSize(index, this.f3994y);
                        continue;
                    case 27:
                        this.T = obtainStyledAttributes.getBoolean(index, this.T);
                        continue;
                    case 28:
                        this.U = obtainStyledAttributes.getBoolean(index, this.U);
                        continue;
                    case 29:
                        this.f3995z = obtainStyledAttributes.getFloat(index, this.f3995z);
                        continue;
                    case 30:
                        this.A = obtainStyledAttributes.getFloat(index, this.A);
                        continue;
                    case 31:
                        int i11 = obtainStyledAttributes.getInt(index, 0);
                        this.I = i11;
                        if (i11 == 1) {
                            str = "layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead.";
                            break;
                        } else {
                            continue;
                        }
                    case 32:
                        int i12 = obtainStyledAttributes.getInt(index, 0);
                        this.J = i12;
                        if (i12 == 1) {
                            str = "layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead.";
                            break;
                        } else {
                            continue;
                        }
                    case 33:
                        try {
                            this.K = obtainStyledAttributes.getDimensionPixelSize(index, this.K);
                            continue;
                        } catch (Exception unused) {
                            if (obtainStyledAttributes.getInt(index, this.K) == -2) {
                                this.K = -2;
                            }
                        }
                    case 34:
                        try {
                            this.M = obtainStyledAttributes.getDimensionPixelSize(index, this.M);
                            continue;
                        } catch (Exception unused2) {
                            if (obtainStyledAttributes.getInt(index, this.M) == -2) {
                                this.M = -2;
                            }
                        }
                    case 35:
                        this.O = Math.max(0.0f, obtainStyledAttributes.getFloat(index, this.O));
                        this.I = 2;
                        continue;
                    case 36:
                        try {
                            this.L = obtainStyledAttributes.getDimensionPixelSize(index, this.L);
                            continue;
                        } catch (Exception unused3) {
                            if (obtainStyledAttributes.getInt(index, this.L) == -2) {
                                this.L = -2;
                            }
                        }
                    case 37:
                        try {
                            this.N = obtainStyledAttributes.getDimensionPixelSize(index, this.N);
                            continue;
                        } catch (Exception unused4) {
                            if (obtainStyledAttributes.getInt(index, this.N) == -2) {
                                this.N = -2;
                            }
                        }
                    case 38:
                        this.P = Math.max(0.0f, obtainStyledAttributes.getFloat(index, this.P));
                        this.J = 2;
                        continue;
                    default:
                        switch (i10) {
                            case 44:
                                String string = obtainStyledAttributes.getString(index);
                                this.B = string;
                                this.C = Float.NaN;
                                this.D = -1;
                                if (string != null) {
                                    int length = string.length();
                                    int indexOf = this.B.indexOf(44);
                                    if (indexOf <= 0 || indexOf >= length - 1) {
                                        i8 = 0;
                                    } else {
                                        String substring = this.B.substring(0, indexOf);
                                        if (substring.equalsIgnoreCase("W")) {
                                            this.D = 0;
                                        } else if (substring.equalsIgnoreCase("H")) {
                                            this.D = 1;
                                        }
                                        i8 = indexOf + 1;
                                    }
                                    int indexOf2 = this.B.indexOf(58);
                                    if (indexOf2 >= 0 && indexOf2 < length - 1) {
                                        String substring2 = this.B.substring(i8, indexOf2);
                                        String substring3 = this.B.substring(indexOf2 + 1);
                                        if (substring2.length() > 0 && substring3.length() > 0) {
                                            try {
                                                float parseFloat2 = Float.parseFloat(substring2);
                                                float parseFloat3 = Float.parseFloat(substring3);
                                                parseFloat = (parseFloat2 > 0.0f && parseFloat3 > 0.0f) ? this.D == 1 ? Math.abs(parseFloat3 / parseFloat2) : Math.abs(parseFloat2 / parseFloat3) : parseFloat;
                                            } catch (NumberFormatException unused5) {
                                                break;
                                            }
                                        }
                                    } else {
                                        String substring4 = this.B.substring(i8);
                                        if (substring4.length() <= 0) {
                                            break;
                                        } else {
                                            parseFloat = Float.parseFloat(substring4);
                                        }
                                    }
                                    this.C = parseFloat;
                                    break;
                                } else {
                                    continue;
                                }
                            case 45:
                                this.E = obtainStyledAttributes.getFloat(index, this.E);
                                continue;
                            case 46:
                                this.F = obtainStyledAttributes.getFloat(index, this.F);
                                continue;
                            case 47:
                                this.G = obtainStyledAttributes.getInt(index, 0);
                                continue;
                            case 48:
                                this.H = obtainStyledAttributes.getInt(index, 0);
                                continue;
                            case 49:
                                this.Q = obtainStyledAttributes.getDimensionPixelOffset(index, this.Q);
                                continue;
                            case 50:
                                this.R = obtainStyledAttributes.getDimensionPixelOffset(index, this.R);
                                continue;
                            case 51:
                                this.V = obtainStyledAttributes.getString(index);
                                continue;
                                continue;
                        }
                        break;
                }
                Log.e("ConstraintLayout", str);
            }
            obtainStyledAttributes.recycle();
            c();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f3956a = -1;
            this.f3958b = -1;
            this.f3960c = -1.0f;
            this.f3962d = -1;
            this.f3964e = -1;
            this.f3966f = -1;
            this.f3968g = -1;
            this.f3970h = -1;
            this.f3972i = -1;
            this.f3974j = -1;
            this.f3976k = -1;
            this.f3978l = -1;
            this.f3980m = -1;
            this.f3982n = 0;
            this.f3984o = 0.0f;
            this.f3986p = -1;
            this.q = -1;
            this.f3987r = -1;
            this.f3988s = -1;
            this.f3989t = -1;
            this.f3990u = -1;
            this.f3991v = -1;
            this.f3992w = -1;
            this.f3993x = -1;
            this.f3994y = -1;
            this.f3995z = 0.5f;
            this.A = 0.5f;
            this.B = null;
            this.C = 0.0f;
            this.D = 1;
            this.E = -1.0f;
            this.F = -1.0f;
            this.G = 0;
            this.H = 0;
            this.I = 0;
            this.J = 0;
            this.K = 0;
            this.L = 0;
            this.M = 0;
            this.N = 0;
            this.O = 1.0f;
            this.P = 1.0f;
            this.Q = -1;
            this.R = -1;
            this.S = -1;
            this.T = false;
            this.U = false;
            this.V = null;
            this.W = true;
            this.X = true;
            this.Y = false;
            this.Z = false;
            this.f3957a0 = false;
            this.f3959b0 = false;
            this.f3961c0 = false;
            this.f3963d0 = -1;
            this.f3965e0 = -1;
            this.f3967f0 = -1;
            this.f3969g0 = -1;
            this.f3971h0 = -1;
            this.f3973i0 = -1;
            this.f3975j0 = 0.5f;
            this.f3983n0 = new ConstraintWidget();
            this.f3985o0 = false;
        }

        public String a() {
            return this.V;
        }

        public ConstraintWidget b() {
            return this.f3983n0;
        }

        public void c() {
            this.Z = false;
            this.W = true;
            this.X = true;
            int i8 = ((ViewGroup.MarginLayoutParams) this).width;
            if (i8 == -2 && this.T) {
                this.W = false;
                if (this.I == 0) {
                    this.I = 1;
                }
            }
            int i9 = ((ViewGroup.MarginLayoutParams) this).height;
            if (i9 == -2 && this.U) {
                this.X = false;
                if (this.J == 0) {
                    this.J = 1;
                }
            }
            if (i8 == 0 || i8 == -1) {
                this.W = false;
                if (i8 == 0 && this.I == 1) {
                    ((ViewGroup.MarginLayoutParams) this).width = -2;
                    this.T = true;
                }
            }
            if (i9 == 0 || i9 == -1) {
                this.X = false;
                if (i9 == 0 && this.J == 1) {
                    ((ViewGroup.MarginLayoutParams) this).height = -2;
                    this.U = true;
                }
            }
            if (this.f3960c == -1.0f && this.f3956a == -1 && this.f3958b == -1) {
                return;
            }
            this.Z = true;
            this.W = true;
            this.X = true;
            if (!(this.f3983n0 instanceof androidx.constraintlayout.solver.widgets.f)) {
                this.f3983n0 = new androidx.constraintlayout.solver.widgets.f();
            }
            ((androidx.constraintlayout.solver.widgets.f) this.f3983n0).S0(this.S);
        }

        /* JADX WARN: Code restructure failed: missing block: B:74:0x00d4, code lost:
            if (r1 > 0) goto L52;
         */
        /* JADX WARN: Code restructure failed: missing block: B:75:0x00d6, code lost:
            ((android.view.ViewGroup.MarginLayoutParams) r9).rightMargin = r1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:80:0x00e3, code lost:
            if (r1 > 0) goto L52;
         */
        /* JADX WARN: Removed duplicated region for block: B:10:0x003f  */
        /* JADX WARN: Removed duplicated region for block: B:19:0x0052  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0059  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x0060  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x0066  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x006c  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x007e  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x0086  */
        /* JADX WARN: Removed duplicated region for block: B:44:0x0096  */
        /* JADX WARN: Removed duplicated region for block: B:84:0x00ea  */
        /* JADX WARN: Removed duplicated region for block: B:88:0x00f5  */
        @Override // android.view.ViewGroup.MarginLayoutParams, android.view.ViewGroup.LayoutParams
        @android.annotation.TargetApi(17)
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void resolveLayoutDirection(int r10) {
            /*
                Method dump skipped, instructions count: 259
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.resolveLayoutDirection(int):void");
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f3997a;

        static {
            int[] iArr = new int[ConstraintWidget.DimensionBehaviour.values().length];
            f3997a = iArr;
            try {
                iArr[ConstraintWidget.DimensionBehaviour.FIXED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3997a[ConstraintWidget.DimensionBehaviour.WRAP_CONTENT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3997a[ConstraintWidget.DimensionBehaviour.MATCH_PARENT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f3997a[ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements BasicMeasure.b {

        /* renamed from: a  reason: collision with root package name */
        ConstraintLayout f3998a;

        /* renamed from: b  reason: collision with root package name */
        int f3999b;

        /* renamed from: c  reason: collision with root package name */
        int f4000c;

        /* renamed from: d  reason: collision with root package name */
        int f4001d;

        /* renamed from: e  reason: collision with root package name */
        int f4002e;

        /* renamed from: f  reason: collision with root package name */
        int f4003f;

        /* renamed from: g  reason: collision with root package name */
        int f4004g;

        public b(ConstraintLayout constraintLayout) {
            this.f3998a = constraintLayout;
        }

        @Override // androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.b
        public final void a() {
            int childCount = this.f3998a.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = this.f3998a.getChildAt(i8);
                if (childAt instanceof Placeholder) {
                    ((Placeholder) childAt).b(this.f3998a);
                }
            }
            int size = this.f3998a.f3938b.size();
            if (size > 0) {
                for (int i9 = 0; i9 < size; i9++) {
                    ((ConstraintHelper) this.f3998a.f3938b.get(i9)).q(this.f3998a);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:104:0x016e A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:114:0x0187  */
        /* JADX WARN: Removed duplicated region for block: B:120:0x01a6  */
        /* JADX WARN: Removed duplicated region for block: B:121:0x01b1  */
        /* JADX WARN: Removed duplicated region for block: B:123:0x01bd  */
        /* JADX WARN: Removed duplicated region for block: B:124:0x01c7  */
        /* JADX WARN: Removed duplicated region for block: B:127:0x01d4  */
        /* JADX WARN: Removed duplicated region for block: B:128:0x01d9  */
        /* JADX WARN: Removed duplicated region for block: B:131:0x01de  */
        /* JADX WARN: Removed duplicated region for block: B:134:0x01e6  */
        /* JADX WARN: Removed duplicated region for block: B:135:0x01eb  */
        /* JADX WARN: Removed duplicated region for block: B:138:0x01f0  */
        /* JADX WARN: Removed duplicated region for block: B:141:0x01f8 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:144:0x0204 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:147:0x020f A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:153:0x021b  */
        /* JADX WARN: Removed duplicated region for block: B:155:0x0221  */
        /* JADX WARN: Removed duplicated region for block: B:158:0x0237  */
        /* JADX WARN: Removed duplicated region for block: B:159:0x0239  */
        /* JADX WARN: Removed duplicated region for block: B:162:0x023f  */
        /* JADX WARN: Removed duplicated region for block: B:169:0x024e  */
        /* JADX WARN: Removed duplicated region for block: B:170:0x0250  */
        /* JADX WARN: Removed duplicated region for block: B:172:0x0253  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00ba  */
        /* JADX WARN: Removed duplicated region for block: B:69:0x0120  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x012f  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x0131  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x0134  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x0136  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x013b  */
        /* JADX WARN: Removed duplicated region for block: B:86:0x0145  */
        /* JADX WARN: Removed duplicated region for block: B:93:0x0150  */
        /* JADX WARN: Removed duplicated region for block: B:98:0x015b  */
        @Override // androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.b
        @android.annotation.SuppressLint({"WrongCall"})
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void b(androidx.constraintlayout.solver.widgets.ConstraintWidget r20, androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.a r21) {
            /*
                Method dump skipped, instructions count: 616
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintLayout.b.b(androidx.constraintlayout.solver.widgets.ConstraintWidget, androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$a):void");
        }

        public void c(int i8, int i9, int i10, int i11, int i12, int i13) {
            this.f3999b = i10;
            this.f4000c = i11;
            this.f4001d = i12;
            this.f4002e = i13;
            this.f4003f = i8;
            this.f4004g = i9;
        }
    }

    public ConstraintLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f3937a = new SparseArray<>();
        this.f3938b = new ArrayList<>(4);
        this.f3939c = new androidx.constraintlayout.solver.widgets.d();
        this.f3940d = 0;
        this.f3941e = 0;
        this.f3942f = Integer.MAX_VALUE;
        this.f3943g = Integer.MAX_VALUE;
        this.f3944h = true;
        this.f3945j = 263;
        this.f3946k = null;
        this.f3947l = null;
        this.f3948m = -1;
        this.f3949n = new HashMap<>();
        this.f3950p = -1;
        this.q = -1;
        this.f3951t = -1;
        this.f3952w = -1;
        this.f3953x = 0;
        this.f3954y = 0;
        this.f3955z = new SparseArray<>();
        this.A = new b(this);
        this.B = 0;
        this.C = 0;
        p(attributeSet, 0, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f3937a = new SparseArray<>();
        this.f3938b = new ArrayList<>(4);
        this.f3939c = new androidx.constraintlayout.solver.widgets.d();
        this.f3940d = 0;
        this.f3941e = 0;
        this.f3942f = Integer.MAX_VALUE;
        this.f3943g = Integer.MAX_VALUE;
        this.f3944h = true;
        this.f3945j = 263;
        this.f3946k = null;
        this.f3947l = null;
        this.f3948m = -1;
        this.f3949n = new HashMap<>();
        this.f3950p = -1;
        this.q = -1;
        this.f3951t = -1;
        this.f3952w = -1;
        this.f3953x = 0;
        this.f3954y = 0;
        this.f3955z = new SparseArray<>();
        this.A = new b(this);
        this.B = 0;
        this.C = 0;
        p(attributeSet, i8, 0);
    }

    private final ConstraintWidget g(int i8) {
        if (i8 == 0) {
            return this.f3939c;
        }
        View view = this.f3937a.get(i8);
        if (view == null && (view = findViewById(i8)) != null && view != this && view.getParent() == this) {
            onViewAdded(view);
        }
        if (view == this) {
            return this.f3939c;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams) view.getLayoutParams()).f3983n0;
    }

    private int getPaddingWidth() {
        int max = Math.max(0, getPaddingLeft()) + Math.max(0, getPaddingRight());
        int max2 = Build.VERSION.SDK_INT >= 17 ? Math.max(0, getPaddingEnd()) + Math.max(0, getPaddingStart()) : 0;
        return max2 > 0 ? max2 : max;
    }

    private void p(AttributeSet attributeSet, int i8, int i9) {
        this.f3939c.d0(this);
        this.f3939c.h1(this.A);
        this.f3937a.put(getId(), this);
        this.f3946k = null;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, e.f4117a1, i8, i9);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == e.f4209k1) {
                    this.f3940d = obtainStyledAttributes.getDimensionPixelOffset(index, this.f3940d);
                } else if (index == e.f4218l1) {
                    this.f3941e = obtainStyledAttributes.getDimensionPixelOffset(index, this.f3941e);
                } else if (index == e.f4191i1) {
                    this.f3942f = obtainStyledAttributes.getDimensionPixelOffset(index, this.f3942f);
                } else if (index == e.f4200j1) {
                    this.f3943g = obtainStyledAttributes.getDimensionPixelOffset(index, this.f3943g);
                } else if (index == e.L2) {
                    this.f3945j = obtainStyledAttributes.getInt(index, this.f3945j);
                } else if (index == e.M1) {
                    int resourceId = obtainStyledAttributes.getResourceId(index, 0);
                    if (resourceId != 0) {
                        try {
                            s(resourceId);
                        } catch (Resources.NotFoundException unused) {
                            this.f3947l = null;
                        }
                    }
                } else if (index == e.f4278s1) {
                    int resourceId2 = obtainStyledAttributes.getResourceId(index, 0);
                    try {
                        androidx.constraintlayout.widget.b bVar = new androidx.constraintlayout.widget.b();
                        this.f3946k = bVar;
                        bVar.w(getContext(), resourceId2);
                    } catch (Resources.NotFoundException unused2) {
                        this.f3946k = null;
                    }
                    this.f3948m = resourceId2;
                }
            }
            obtainStyledAttributes.recycle();
        }
        this.f3939c.i1(this.f3945j);
    }

    private void r() {
        this.f3944h = true;
        this.f3950p = -1;
        this.q = -1;
        this.f3951t = -1;
        this.f3952w = -1;
        this.f3953x = 0;
        this.f3954y = 0;
    }

    private void v() {
        boolean isInEditMode = isInEditMode();
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            ConstraintWidget i9 = i(getChildAt(i8));
            if (i9 != null) {
                i9.Z();
            }
        }
        if (isInEditMode) {
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = getChildAt(i10);
                try {
                    String resourceName = getResources().getResourceName(childAt.getId());
                    w(0, resourceName, Integer.valueOf(childAt.getId()));
                    int indexOf = resourceName.indexOf(47);
                    if (indexOf != -1) {
                        resourceName = resourceName.substring(indexOf + 1);
                    }
                    g(childAt.getId()).e0(resourceName);
                } catch (Resources.NotFoundException unused) {
                }
            }
        }
        if (this.f3948m != -1) {
            for (int i11 = 0; i11 < childCount; i11++) {
                View childAt2 = getChildAt(i11);
                if (childAt2.getId() == this.f3948m && (childAt2 instanceof Constraints)) {
                    this.f3946k = ((Constraints) childAt2).getConstraintSet();
                }
            }
        }
        androidx.constraintlayout.widget.b bVar = this.f3946k;
        if (bVar != null) {
            bVar.f(this, true);
        }
        this.f3939c.O0();
        int size = this.f3938b.size();
        if (size > 0) {
            for (int i12 = 0; i12 < size; i12++) {
                this.f3938b.get(i12).t(this);
            }
        }
        for (int i13 = 0; i13 < childCount; i13++) {
            View childAt3 = getChildAt(i13);
            if (childAt3 instanceof Placeholder) {
                ((Placeholder) childAt3).c(this);
            }
        }
        this.f3955z.clear();
        this.f3955z.put(0, this.f3939c);
        this.f3955z.put(getId(), this.f3939c);
        for (int i14 = 0; i14 < childCount; i14++) {
            View childAt4 = getChildAt(i14);
            this.f3955z.put(childAt4.getId(), i(childAt4));
        }
        for (int i15 = 0; i15 < childCount; i15++) {
            View childAt5 = getChildAt(i15);
            ConstraintWidget i16 = i(childAt5);
            if (i16 != null) {
                LayoutParams layoutParams = (LayoutParams) childAt5.getLayoutParams();
                this.f3939c.a(i16);
                c(isInEditMode, childAt5, i16, layoutParams, this.f3955z);
            }
        }
    }

    private boolean y() {
        int childCount = getChildCount();
        boolean z4 = false;
        int i8 = 0;
        while (true) {
            if (i8 >= childCount) {
                break;
            } else if (getChildAt(i8).isLayoutRequested()) {
                z4 = true;
                break;
            } else {
                i8++;
            }
        }
        if (z4) {
            v();
        }
        return z4;
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i8, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, i8, layoutParams);
        if (Build.VERSION.SDK_INT < 14) {
            onViewAdded(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:100:0x01e5  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0205  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0233  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x024a  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0278  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0103  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0115  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x012f  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0160  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0174  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0191  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void c(boolean r20, android.view.View r21, androidx.constraintlayout.solver.widgets.ConstraintWidget r22, androidx.constraintlayout.widget.ConstraintLayout.LayoutParams r23, android.util.SparseArray<androidx.constraintlayout.solver.widgets.ConstraintWidget> r24) {
        /*
            Method dump skipped, instructions count: 699
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintLayout.c(boolean, android.view.View, androidx.constraintlayout.solver.widgets.ConstraintWidget, androidx.constraintlayout.widget.ConstraintLayout$LayoutParams, android.util.SparseArray):void");
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: d */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        Object tag;
        int size;
        ArrayList<ConstraintHelper> arrayList = this.f3938b;
        if (arrayList != null && (size = arrayList.size()) > 0) {
            for (int i8 = 0; i8 < size; i8++) {
                this.f3938b.get(i8).r(this);
            }
        }
        super.dispatchDraw(canvas);
        if (isInEditMode()) {
            int childCount = getChildCount();
            float width = getWidth();
            float height = getHeight();
            for (int i9 = 0; i9 < childCount; i9++) {
                View childAt = getChildAt(i9);
                if (childAt.getVisibility() != 8 && (tag = childAt.getTag()) != null && (tag instanceof String)) {
                    String[] split = ((String) tag).split(",");
                    if (split.length == 4) {
                        int parseInt = Integer.parseInt(split[0]);
                        int parseInt2 = Integer.parseInt(split[1]);
                        int parseInt3 = Integer.parseInt(split[2]);
                        int i10 = (int) ((parseInt / 1080.0f) * width);
                        int i11 = (int) ((parseInt2 / 1920.0f) * height);
                        Paint paint = new Paint();
                        paint.setColor(-65536);
                        float f5 = i10;
                        float f8 = i11;
                        float f9 = i10 + ((int) ((parseInt3 / 1080.0f) * width));
                        canvas.drawLine(f5, f8, f9, f8, paint);
                        float parseInt4 = i11 + ((int) ((Integer.parseInt(split[3]) / 1920.0f) * height));
                        canvas.drawLine(f9, f8, f9, parseInt4, paint);
                        canvas.drawLine(f9, parseInt4, f5, parseInt4, paint);
                        canvas.drawLine(f5, parseInt4, f5, f8, paint);
                        paint.setColor(-16711936);
                        canvas.drawLine(f5, f8, f9, parseInt4, paint);
                        canvas.drawLine(f5, parseInt4, f9, f8, paint);
                    }
                }
            }
        }
    }

    @Override // android.view.ViewGroup
    /* renamed from: e */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public Object f(int i8, Object obj) {
        if (i8 == 0 && (obj instanceof String)) {
            String str = (String) obj;
            HashMap<String, Integer> hashMap = this.f3949n;
            if (hashMap == null || !hashMap.containsKey(str)) {
                return null;
            }
            return this.f3949n.get(str);
        }
        return null;
    }

    @Override // android.view.View
    public void forceLayout() {
        r();
        super.forceLayout();
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public int getMaxHeight() {
        return this.f3943g;
    }

    public int getMaxWidth() {
        return this.f3942f;
    }

    public int getMinHeight() {
        return this.f3941e;
    }

    public int getMinWidth() {
        return this.f3940d;
    }

    public int getOptimizationLevel() {
        return this.f3939c.X0();
    }

    public View h(int i8) {
        return this.f3937a.get(i8);
    }

    public final ConstraintWidget i(View view) {
        if (view == this) {
            return this.f3939c;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams) view.getLayoutParams()).f3983n0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        View content;
        int childCount = getChildCount();
        boolean isInEditMode = isInEditMode();
        for (int i12 = 0; i12 < childCount; i12++) {
            View childAt = getChildAt(i12);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            ConstraintWidget constraintWidget = layoutParams.f3983n0;
            if ((childAt.getVisibility() != 8 || layoutParams.Z || layoutParams.f3957a0 || layoutParams.f3961c0 || isInEditMode) && !layoutParams.f3959b0) {
                int R = constraintWidget.R();
                int S = constraintWidget.S();
                int Q = constraintWidget.Q() + R;
                int w8 = constraintWidget.w() + S;
                childAt.layout(R, S, Q, w8);
                if ((childAt instanceof Placeholder) && (content = ((Placeholder) childAt).getContent()) != null) {
                    content.setVisibility(0);
                    content.layout(R, S, Q, w8);
                }
            }
        }
        int size = this.f3938b.size();
        if (size > 0) {
            for (int i13 = 0; i13 < size; i13++) {
                this.f3938b.get(i13).p(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int i8, int i9) {
        this.B = i8;
        this.C = i9;
        this.f3939c.j1(q());
        if (this.f3944h) {
            this.f3944h = false;
            if (y()) {
                this.f3939c.l1();
            }
        }
        u(this.f3939c, this.f3945j, i8, i9);
        t(i8, i9, this.f3939c.Q(), this.f3939c.w(), this.f3939c.d1(), this.f3939c.b1());
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewAdded(view);
        }
        ConstraintWidget i8 = i(view);
        if ((view instanceof Guideline) && !(i8 instanceof androidx.constraintlayout.solver.widgets.f)) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            androidx.constraintlayout.solver.widgets.f fVar = new androidx.constraintlayout.solver.widgets.f();
            layoutParams.f3983n0 = fVar;
            layoutParams.Z = true;
            fVar.S0(layoutParams.S);
        }
        if (view instanceof ConstraintHelper) {
            ConstraintHelper constraintHelper = (ConstraintHelper) view;
            constraintHelper.u();
            ((LayoutParams) view.getLayoutParams()).f3957a0 = true;
            if (!this.f3938b.contains(constraintHelper)) {
                this.f3938b.add(constraintHelper);
            }
        }
        this.f3937a.put(view.getId(), view);
        this.f3944h = true;
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewRemoved(view);
        }
        this.f3937a.remove(view.getId());
        this.f3939c.N0(i(view));
        this.f3938b.remove(view);
        this.f3944h = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean q() {
        if (Build.VERSION.SDK_INT >= 17) {
            return ((getContext().getApplicationInfo().flags & 4194304) != 0) && 1 == getLayoutDirection();
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        super.removeView(view);
        if (Build.VERSION.SDK_INT < 14) {
            onViewRemoved(view);
        }
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        r();
        super.requestLayout();
    }

    protected void s(int i8) {
        this.f3947l = new androidx.constraintlayout.widget.a(getContext(), this, i8);
    }

    public void setConstraintSet(androidx.constraintlayout.widget.b bVar) {
        this.f3946k = bVar;
    }

    @Override // android.view.View
    public void setId(int i8) {
        this.f3937a.remove(getId());
        super.setId(i8);
        this.f3937a.put(getId(), this);
    }

    public void setMaxHeight(int i8) {
        if (i8 == this.f3943g) {
            return;
        }
        this.f3943g = i8;
        requestLayout();
    }

    public void setMaxWidth(int i8) {
        if (i8 == this.f3942f) {
            return;
        }
        this.f3942f = i8;
        requestLayout();
    }

    public void setMinHeight(int i8) {
        if (i8 == this.f3941e) {
            return;
        }
        this.f3941e = i8;
        requestLayout();
    }

    public void setMinWidth(int i8) {
        if (i8 == this.f3940d) {
            return;
        }
        this.f3940d = i8;
        requestLayout();
    }

    public void setOnConstraintsChanged(c cVar) {
        androidx.constraintlayout.widget.a aVar = this.f3947l;
        if (aVar != null) {
            aVar.c(cVar);
        }
    }

    public void setOptimizationLevel(int i8) {
        this.f3945j = i8;
        this.f3939c.i1(i8);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void t(int i8, int i9, int i10, int i11, boolean z4, boolean z8) {
        b bVar = this.A;
        int i12 = bVar.f4002e;
        int i13 = i10 + bVar.f4001d;
        int i14 = i11 + i12;
        if (Build.VERSION.SDK_INT < 11) {
            setMeasuredDimension(i13, i14);
            this.f3950p = i13;
            this.q = i14;
            return;
        }
        int resolveSizeAndState = ViewGroup.resolveSizeAndState(i13, i8, 0);
        int min = Math.min(this.f3942f, resolveSizeAndState & 16777215);
        int min2 = Math.min(this.f3943g, ViewGroup.resolveSizeAndState(i14, i9, 0) & 16777215);
        if (z4) {
            min |= 16777216;
        }
        if (z8) {
            min2 |= 16777216;
        }
        setMeasuredDimension(min, min2);
        this.f3950p = min;
        this.q = min2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void u(androidx.constraintlayout.solver.widgets.d dVar, int i8, int i9, int i10) {
        int max;
        int mode = View.MeasureSpec.getMode(i9);
        int size = View.MeasureSpec.getSize(i9);
        int mode2 = View.MeasureSpec.getMode(i10);
        int size2 = View.MeasureSpec.getSize(i10);
        int max2 = Math.max(0, getPaddingTop());
        int max3 = Math.max(0, getPaddingBottom());
        int i11 = max2 + max3;
        int paddingWidth = getPaddingWidth();
        this.A.c(i9, i10, max2, max3, paddingWidth, i11);
        if (Build.VERSION.SDK_INT >= 17) {
            int max4 = Math.max(0, getPaddingStart());
            int max5 = Math.max(0, getPaddingEnd());
            if (max4 <= 0 && max5 <= 0) {
                max4 = Math.max(0, getPaddingLeft());
            } else if (q()) {
                max4 = max5;
            }
            max = max4;
        } else {
            max = Math.max(0, getPaddingLeft());
        }
        int i12 = size - paddingWidth;
        int i13 = size2 - i11;
        x(dVar, mode, i12, mode2, i13);
        dVar.e1(i8, mode, i12, mode2, i13, this.f3950p, this.q, max, max2);
    }

    public void w(int i8, Object obj, Object obj2) {
        if (i8 == 0 && (obj instanceof String) && (obj2 instanceof Integer)) {
            if (this.f3949n == null) {
                this.f3949n = new HashMap<>();
            }
            String str = (String) obj;
            int indexOf = str.indexOf("/");
            if (indexOf != -1) {
                str = str.substring(indexOf + 1);
            }
            this.f3949n.put(str, Integer.valueOf(((Integer) obj2).intValue()));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0025, code lost:
        if (r3 == 0) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x002a, code lost:
        if (r3 == 0) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x002c, code lost:
        r10 = java.lang.Math.max(0, r7.f3940d);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0044, code lost:
        if (r3 == 0) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0049, code lost:
        if (r3 == 0) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x004b, code lost:
        r12 = java.lang.Math.max(0, r7.f3941e);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void x(androidx.constraintlayout.solver.widgets.d r8, int r9, int r10, int r11, int r12) {
        /*
            r7 = this;
            androidx.constraintlayout.widget.ConstraintLayout$b r0 = r7.A
            int r1 = r0.f4002e
            int r0 = r0.f4001d
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            int r3 = r7.getChildCount()
            r4 = 1073741824(0x40000000, float:2.0)
            r5 = -2147483648(0xffffffff80000000, float:-0.0)
            r6 = 0
            if (r9 == r5) goto L28
            if (r9 == 0) goto L23
            if (r9 == r4) goto L1a
            r9 = r2
        L18:
            r10 = r6
            goto L32
        L1a:
            int r9 = r7.f3942f
            int r9 = r9 - r0
            int r10 = java.lang.Math.min(r9, r10)
            r9 = r2
            goto L32
        L23:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r3 != 0) goto L18
            goto L2c
        L28:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r3 != 0) goto L32
        L2c:
            int r10 = r7.f3940d
            int r10 = java.lang.Math.max(r6, r10)
        L32:
            if (r11 == r5) goto L47
            if (r11 == 0) goto L42
            if (r11 == r4) goto L3a
        L38:
            r12 = r6
            goto L51
        L3a:
            int r11 = r7.f3943g
            int r11 = r11 - r1
            int r12 = java.lang.Math.min(r11, r12)
            goto L51
        L42:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r3 != 0) goto L38
            goto L4b
        L47:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r3 != 0) goto L51
        L4b:
            int r11 = r7.f3941e
            int r12 = java.lang.Math.max(r6, r11)
        L51:
            int r11 = r8.Q()
            if (r10 != r11) goto L5d
            int r11 = r8.w()
            if (r12 == r11) goto L60
        L5d:
            r8.a1()
        L60:
            r8.G0(r6)
            r8.H0(r6)
            int r11 = r7.f3942f
            int r11 = r11 - r0
            r8.t0(r11)
            int r11 = r7.f3943g
            int r11 = r11 - r1
            r8.s0(r11)
            r8.v0(r6)
            r8.u0(r6)
            r8.m0(r9)
            r8.F0(r10)
            r8.B0(r2)
            r8.i0(r12)
            int r9 = r7.f3940d
            int r9 = r9 - r0
            r8.v0(r9)
            int r9 = r7.f3941e
            int r9 = r9 - r1
            r8.u0(r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintLayout.x(androidx.constraintlayout.solver.widgets.d, int, int, int, int):void");
    }
}
