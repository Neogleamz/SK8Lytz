package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {

    /* renamed from: e  reason: collision with root package name */
    private static final int[] f4039e = {0, 4, 8};

    /* renamed from: f  reason: collision with root package name */
    private static SparseIntArray f4040f;

    /* renamed from: a  reason: collision with root package name */
    private boolean f4041a;

    /* renamed from: b  reason: collision with root package name */
    private HashMap<String, ConstraintAttribute> f4042b = new HashMap<>();

    /* renamed from: c  reason: collision with root package name */
    private boolean f4043c = true;

    /* renamed from: d  reason: collision with root package name */
    private HashMap<Integer, a> f4044d = new HashMap<>();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        int f4045a;

        /* renamed from: b  reason: collision with root package name */
        public final d f4046b = new d();

        /* renamed from: c  reason: collision with root package name */
        public final c f4047c = new c();

        /* renamed from: d  reason: collision with root package name */
        public final C0027b f4048d = new C0027b();

        /* renamed from: e  reason: collision with root package name */
        public final e f4049e = new e();

        /* renamed from: f  reason: collision with root package name */
        public HashMap<String, ConstraintAttribute> f4050f = new HashMap<>();

        /* JADX INFO: Access modifiers changed from: private */
        public void f(int i8, ConstraintLayout.LayoutParams layoutParams) {
            this.f4045a = i8;
            C0027b c0027b = this.f4048d;
            c0027b.f4066h = layoutParams.f3962d;
            c0027b.f4068i = layoutParams.f3964e;
            c0027b.f4070j = layoutParams.f3966f;
            c0027b.f4072k = layoutParams.f3968g;
            c0027b.f4073l = layoutParams.f3970h;
            c0027b.f4074m = layoutParams.f3972i;
            c0027b.f4075n = layoutParams.f3974j;
            c0027b.f4076o = layoutParams.f3976k;
            c0027b.f4077p = layoutParams.f3978l;
            c0027b.q = layoutParams.f3986p;
            c0027b.f4078r = layoutParams.q;
            c0027b.f4079s = layoutParams.f3987r;
            c0027b.f4080t = layoutParams.f3988s;
            c0027b.f4081u = layoutParams.f3995z;
            c0027b.f4082v = layoutParams.A;
            c0027b.f4083w = layoutParams.B;
            c0027b.f4084x = layoutParams.f3980m;
            c0027b.f4085y = layoutParams.f3982n;
            c0027b.f4086z = layoutParams.f3984o;
            c0027b.A = layoutParams.Q;
            c0027b.B = layoutParams.R;
            c0027b.C = layoutParams.S;
            c0027b.f4064g = layoutParams.f3960c;
            c0027b.f4060e = layoutParams.f3956a;
            c0027b.f4062f = layoutParams.f3958b;
            c0027b.f4056c = ((ViewGroup.MarginLayoutParams) layoutParams).width;
            c0027b.f4058d = ((ViewGroup.MarginLayoutParams) layoutParams).height;
            c0027b.D = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
            c0027b.E = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
            c0027b.F = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
            c0027b.G = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
            c0027b.P = layoutParams.F;
            c0027b.Q = layoutParams.E;
            c0027b.S = layoutParams.H;
            c0027b.R = layoutParams.G;
            c0027b.f4067h0 = layoutParams.T;
            c0027b.f4069i0 = layoutParams.U;
            c0027b.T = layoutParams.I;
            c0027b.U = layoutParams.J;
            c0027b.V = layoutParams.M;
            c0027b.W = layoutParams.N;
            c0027b.X = layoutParams.K;
            c0027b.Y = layoutParams.L;
            c0027b.Z = layoutParams.O;
            c0027b.f4053a0 = layoutParams.P;
            c0027b.f4065g0 = layoutParams.V;
            c0027b.K = layoutParams.f3990u;
            c0027b.M = layoutParams.f3992w;
            c0027b.J = layoutParams.f3989t;
            c0027b.L = layoutParams.f3991v;
            c0027b.O = layoutParams.f3993x;
            c0027b.N = layoutParams.f3994y;
            if (Build.VERSION.SDK_INT >= 17) {
                c0027b.H = layoutParams.getMarginEnd();
                this.f4048d.I = layoutParams.getMarginStart();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void g(int i8, Constraints.LayoutParams layoutParams) {
            f(i8, layoutParams);
            this.f4046b.f4098d = layoutParams.f4007p0;
            e eVar = this.f4049e;
            eVar.f4102b = layoutParams.f4010s0;
            eVar.f4103c = layoutParams.f4011t0;
            eVar.f4104d = layoutParams.f4012u0;
            eVar.f4105e = layoutParams.f4013v0;
            eVar.f4106f = layoutParams.f4014w0;
            eVar.f4107g = layoutParams.f4015x0;
            eVar.f4108h = layoutParams.f4016y0;
            eVar.f4109i = layoutParams.f4017z0;
            eVar.f4110j = layoutParams.A0;
            eVar.f4111k = layoutParams.B0;
            eVar.f4113m = layoutParams.f4009r0;
            eVar.f4112l = layoutParams.f4008q0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void h(ConstraintHelper constraintHelper, int i8, Constraints.LayoutParams layoutParams) {
            g(i8, layoutParams);
            if (constraintHelper instanceof Barrier) {
                C0027b c0027b = this.f4048d;
                c0027b.f4059d0 = 1;
                Barrier barrier = (Barrier) constraintHelper;
                c0027b.f4055b0 = barrier.getType();
                this.f4048d.f4061e0 = barrier.getReferencedIds();
                this.f4048d.f4057c0 = barrier.getMargin();
            }
        }

        public void d(ConstraintLayout.LayoutParams layoutParams) {
            C0027b c0027b = this.f4048d;
            layoutParams.f3962d = c0027b.f4066h;
            layoutParams.f3964e = c0027b.f4068i;
            layoutParams.f3966f = c0027b.f4070j;
            layoutParams.f3968g = c0027b.f4072k;
            layoutParams.f3970h = c0027b.f4073l;
            layoutParams.f3972i = c0027b.f4074m;
            layoutParams.f3974j = c0027b.f4075n;
            layoutParams.f3976k = c0027b.f4076o;
            layoutParams.f3978l = c0027b.f4077p;
            layoutParams.f3986p = c0027b.q;
            layoutParams.q = c0027b.f4078r;
            layoutParams.f3987r = c0027b.f4079s;
            layoutParams.f3988s = c0027b.f4080t;
            ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = c0027b.D;
            ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = c0027b.E;
            ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = c0027b.F;
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = c0027b.G;
            layoutParams.f3993x = c0027b.O;
            layoutParams.f3994y = c0027b.N;
            layoutParams.f3990u = c0027b.K;
            layoutParams.f3992w = c0027b.M;
            layoutParams.f3995z = c0027b.f4081u;
            layoutParams.A = c0027b.f4082v;
            layoutParams.f3980m = c0027b.f4084x;
            layoutParams.f3982n = c0027b.f4085y;
            layoutParams.f3984o = c0027b.f4086z;
            layoutParams.B = c0027b.f4083w;
            layoutParams.Q = c0027b.A;
            layoutParams.R = c0027b.B;
            layoutParams.F = c0027b.P;
            layoutParams.E = c0027b.Q;
            layoutParams.H = c0027b.S;
            layoutParams.G = c0027b.R;
            layoutParams.T = c0027b.f4067h0;
            layoutParams.U = c0027b.f4069i0;
            layoutParams.I = c0027b.T;
            layoutParams.J = c0027b.U;
            layoutParams.M = c0027b.V;
            layoutParams.N = c0027b.W;
            layoutParams.K = c0027b.X;
            layoutParams.L = c0027b.Y;
            layoutParams.O = c0027b.Z;
            layoutParams.P = c0027b.f4053a0;
            layoutParams.S = c0027b.C;
            layoutParams.f3960c = c0027b.f4064g;
            layoutParams.f3956a = c0027b.f4060e;
            layoutParams.f3958b = c0027b.f4062f;
            ((ViewGroup.MarginLayoutParams) layoutParams).width = c0027b.f4056c;
            ((ViewGroup.MarginLayoutParams) layoutParams).height = c0027b.f4058d;
            String str = c0027b.f4065g0;
            if (str != null) {
                layoutParams.V = str;
            }
            if (Build.VERSION.SDK_INT >= 17) {
                layoutParams.setMarginStart(c0027b.I);
                layoutParams.setMarginEnd(this.f4048d.H);
            }
            layoutParams.c();
        }

        /* renamed from: e */
        public a clone() {
            a aVar = new a();
            aVar.f4048d.a(this.f4048d);
            aVar.f4047c.a(this.f4047c);
            aVar.f4046b.a(this.f4046b);
            aVar.f4049e.a(this.f4049e);
            aVar.f4045a = this.f4045a;
            return aVar;
        }
    }

    /* renamed from: androidx.constraintlayout.widget.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0027b {

        /* renamed from: k0  reason: collision with root package name */
        private static SparseIntArray f4051k0;

        /* renamed from: c  reason: collision with root package name */
        public int f4056c;

        /* renamed from: d  reason: collision with root package name */
        public int f4058d;

        /* renamed from: e0  reason: collision with root package name */
        public int[] f4061e0;

        /* renamed from: f0  reason: collision with root package name */
        public String f4063f0;

        /* renamed from: g0  reason: collision with root package name */
        public String f4065g0;

        /* renamed from: a  reason: collision with root package name */
        public boolean f4052a = false;

        /* renamed from: b  reason: collision with root package name */
        public boolean f4054b = false;

        /* renamed from: e  reason: collision with root package name */
        public int f4060e = -1;

        /* renamed from: f  reason: collision with root package name */
        public int f4062f = -1;

        /* renamed from: g  reason: collision with root package name */
        public float f4064g = -1.0f;

        /* renamed from: h  reason: collision with root package name */
        public int f4066h = -1;

        /* renamed from: i  reason: collision with root package name */
        public int f4068i = -1;

        /* renamed from: j  reason: collision with root package name */
        public int f4070j = -1;

        /* renamed from: k  reason: collision with root package name */
        public int f4072k = -1;

        /* renamed from: l  reason: collision with root package name */
        public int f4073l = -1;

        /* renamed from: m  reason: collision with root package name */
        public int f4074m = -1;

        /* renamed from: n  reason: collision with root package name */
        public int f4075n = -1;

        /* renamed from: o  reason: collision with root package name */
        public int f4076o = -1;

        /* renamed from: p  reason: collision with root package name */
        public int f4077p = -1;
        public int q = -1;

        /* renamed from: r  reason: collision with root package name */
        public int f4078r = -1;

        /* renamed from: s  reason: collision with root package name */
        public int f4079s = -1;

        /* renamed from: t  reason: collision with root package name */
        public int f4080t = -1;

        /* renamed from: u  reason: collision with root package name */
        public float f4081u = 0.5f;

        /* renamed from: v  reason: collision with root package name */
        public float f4082v = 0.5f;

        /* renamed from: w  reason: collision with root package name */
        public String f4083w = null;

        /* renamed from: x  reason: collision with root package name */
        public int f4084x = -1;

        /* renamed from: y  reason: collision with root package name */
        public int f4085y = 0;

        /* renamed from: z  reason: collision with root package name */
        public float f4086z = 0.0f;
        public int A = -1;
        public int B = -1;
        public int C = -1;
        public int D = -1;
        public int E = -1;
        public int F = -1;
        public int G = -1;
        public int H = -1;
        public int I = -1;
        public int J = -1;
        public int K = -1;
        public int L = -1;
        public int M = -1;
        public int N = -1;
        public int O = -1;
        public float P = -1.0f;
        public float Q = -1.0f;
        public int R = 0;
        public int S = 0;
        public int T = 0;
        public int U = 0;
        public int V = -1;
        public int W = -1;
        public int X = -1;
        public int Y = -1;
        public float Z = 1.0f;

        /* renamed from: a0  reason: collision with root package name */
        public float f4053a0 = 1.0f;

        /* renamed from: b0  reason: collision with root package name */
        public int f4055b0 = -1;

        /* renamed from: c0  reason: collision with root package name */
        public int f4057c0 = 0;

        /* renamed from: d0  reason: collision with root package name */
        public int f4059d0 = -1;

        /* renamed from: h0  reason: collision with root package name */
        public boolean f4067h0 = false;

        /* renamed from: i0  reason: collision with root package name */
        public boolean f4069i0 = false;

        /* renamed from: j0  reason: collision with root package name */
        public boolean f4071j0 = true;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f4051k0 = sparseIntArray;
            sparseIntArray.append(androidx.constraintlayout.widget.e.O5, 24);
            f4051k0.append(androidx.constraintlayout.widget.e.P5, 25);
            f4051k0.append(androidx.constraintlayout.widget.e.R5, 28);
            f4051k0.append(androidx.constraintlayout.widget.e.S5, 29);
            f4051k0.append(androidx.constraintlayout.widget.e.X5, 35);
            f4051k0.append(androidx.constraintlayout.widget.e.W5, 34);
            f4051k0.append(androidx.constraintlayout.widget.e.f4344z5, 4);
            f4051k0.append(androidx.constraintlayout.widget.e.f4336y5, 3);
            f4051k0.append(androidx.constraintlayout.widget.e.f4318w5, 1);
            f4051k0.append(androidx.constraintlayout.widget.e.f4142c6, 6);
            f4051k0.append(androidx.constraintlayout.widget.e.f4152d6, 7);
            f4051k0.append(androidx.constraintlayout.widget.e.G5, 17);
            f4051k0.append(androidx.constraintlayout.widget.e.H5, 18);
            f4051k0.append(androidx.constraintlayout.widget.e.I5, 19);
            f4051k0.append(androidx.constraintlayout.widget.e.f4186h5, 26);
            f4051k0.append(androidx.constraintlayout.widget.e.T5, 31);
            f4051k0.append(androidx.constraintlayout.widget.e.U5, 32);
            f4051k0.append(androidx.constraintlayout.widget.e.F5, 10);
            f4051k0.append(androidx.constraintlayout.widget.e.E5, 9);
            f4051k0.append(androidx.constraintlayout.widget.e.f4178g6, 13);
            f4051k0.append(androidx.constraintlayout.widget.e.f4205j6, 16);
            f4051k0.append(androidx.constraintlayout.widget.e.f4187h6, 14);
            f4051k0.append(androidx.constraintlayout.widget.e.f4161e6, 11);
            f4051k0.append(androidx.constraintlayout.widget.e.f4196i6, 15);
            f4051k0.append(androidx.constraintlayout.widget.e.f4169f6, 12);
            f4051k0.append(androidx.constraintlayout.widget.e.f4122a6, 38);
            f4051k0.append(androidx.constraintlayout.widget.e.M5, 37);
            f4051k0.append(androidx.constraintlayout.widget.e.L5, 39);
            f4051k0.append(androidx.constraintlayout.widget.e.Z5, 40);
            f4051k0.append(androidx.constraintlayout.widget.e.K5, 20);
            f4051k0.append(androidx.constraintlayout.widget.e.Y5, 36);
            f4051k0.append(androidx.constraintlayout.widget.e.D5, 5);
            f4051k0.append(androidx.constraintlayout.widget.e.N5, 76);
            f4051k0.append(androidx.constraintlayout.widget.e.V5, 76);
            f4051k0.append(androidx.constraintlayout.widget.e.Q5, 76);
            f4051k0.append(androidx.constraintlayout.widget.e.f4327x5, 76);
            f4051k0.append(androidx.constraintlayout.widget.e.f4309v5, 76);
            f4051k0.append(androidx.constraintlayout.widget.e.f4213k5, 23);
            f4051k0.append(androidx.constraintlayout.widget.e.f4231m5, 27);
            f4051k0.append(androidx.constraintlayout.widget.e.o5, 30);
            f4051k0.append(androidx.constraintlayout.widget.e.f4257p5, 8);
            f4051k0.append(androidx.constraintlayout.widget.e.f4222l5, 33);
            f4051k0.append(androidx.constraintlayout.widget.e.f4240n5, 2);
            f4051k0.append(androidx.constraintlayout.widget.e.f4195i5, 22);
            f4051k0.append(androidx.constraintlayout.widget.e.f4204j5, 21);
            f4051k0.append(androidx.constraintlayout.widget.e.A5, 61);
            f4051k0.append(androidx.constraintlayout.widget.e.C5, 62);
            f4051k0.append(androidx.constraintlayout.widget.e.B5, 63);
            f4051k0.append(androidx.constraintlayout.widget.e.f4132b6, 69);
            f4051k0.append(androidx.constraintlayout.widget.e.J5, 70);
            f4051k0.append(androidx.constraintlayout.widget.e.f4291t5, 71);
            f4051k0.append(androidx.constraintlayout.widget.e.f4273r5, 72);
            f4051k0.append(androidx.constraintlayout.widget.e.f4282s5, 73);
            f4051k0.append(androidx.constraintlayout.widget.e.f4300u5, 74);
            f4051k0.append(androidx.constraintlayout.widget.e.f4265q5, 75);
        }

        public void a(C0027b c0027b) {
            this.f4052a = c0027b.f4052a;
            this.f4056c = c0027b.f4056c;
            this.f4054b = c0027b.f4054b;
            this.f4058d = c0027b.f4058d;
            this.f4060e = c0027b.f4060e;
            this.f4062f = c0027b.f4062f;
            this.f4064g = c0027b.f4064g;
            this.f4066h = c0027b.f4066h;
            this.f4068i = c0027b.f4068i;
            this.f4070j = c0027b.f4070j;
            this.f4072k = c0027b.f4072k;
            this.f4073l = c0027b.f4073l;
            this.f4074m = c0027b.f4074m;
            this.f4075n = c0027b.f4075n;
            this.f4076o = c0027b.f4076o;
            this.f4077p = c0027b.f4077p;
            this.q = c0027b.q;
            this.f4078r = c0027b.f4078r;
            this.f4079s = c0027b.f4079s;
            this.f4080t = c0027b.f4080t;
            this.f4081u = c0027b.f4081u;
            this.f4082v = c0027b.f4082v;
            this.f4083w = c0027b.f4083w;
            this.f4084x = c0027b.f4084x;
            this.f4085y = c0027b.f4085y;
            this.f4086z = c0027b.f4086z;
            this.A = c0027b.A;
            this.B = c0027b.B;
            this.C = c0027b.C;
            this.D = c0027b.D;
            this.E = c0027b.E;
            this.F = c0027b.F;
            this.G = c0027b.G;
            this.H = c0027b.H;
            this.I = c0027b.I;
            this.J = c0027b.J;
            this.K = c0027b.K;
            this.L = c0027b.L;
            this.M = c0027b.M;
            this.N = c0027b.N;
            this.O = c0027b.O;
            this.P = c0027b.P;
            this.Q = c0027b.Q;
            this.R = c0027b.R;
            this.S = c0027b.S;
            this.T = c0027b.T;
            this.U = c0027b.U;
            this.V = c0027b.V;
            this.W = c0027b.W;
            this.X = c0027b.X;
            this.Y = c0027b.Y;
            this.Z = c0027b.Z;
            this.f4053a0 = c0027b.f4053a0;
            this.f4055b0 = c0027b.f4055b0;
            this.f4057c0 = c0027b.f4057c0;
            this.f4059d0 = c0027b.f4059d0;
            this.f4065g0 = c0027b.f4065g0;
            int[] iArr = c0027b.f4061e0;
            if (iArr != null) {
                this.f4061e0 = Arrays.copyOf(iArr, iArr.length);
            } else {
                this.f4061e0 = null;
            }
            this.f4063f0 = c0027b.f4063f0;
            this.f4067h0 = c0027b.f4067h0;
            this.f4069i0 = c0027b.f4069i0;
            this.f4071j0 = c0027b.f4071j0;
        }

        void b(Context context, AttributeSet attributeSet) {
            StringBuilder sb;
            String str;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.f4177g5);
            this.f4054b = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                int i9 = f4051k0.get(index);
                if (i9 == 80) {
                    this.f4067h0 = obtainStyledAttributes.getBoolean(index, this.f4067h0);
                } else if (i9 != 81) {
                    switch (i9) {
                        case 1:
                            this.f4077p = b.y(obtainStyledAttributes, index, this.f4077p);
                            continue;
                        case 2:
                            this.G = obtainStyledAttributes.getDimensionPixelSize(index, this.G);
                            continue;
                        case 3:
                            this.f4076o = b.y(obtainStyledAttributes, index, this.f4076o);
                            continue;
                        case 4:
                            this.f4075n = b.y(obtainStyledAttributes, index, this.f4075n);
                            continue;
                        case 5:
                            this.f4083w = obtainStyledAttributes.getString(index);
                            continue;
                        case 6:
                            this.A = obtainStyledAttributes.getDimensionPixelOffset(index, this.A);
                            continue;
                        case 7:
                            this.B = obtainStyledAttributes.getDimensionPixelOffset(index, this.B);
                            continue;
                        case 8:
                            if (Build.VERSION.SDK_INT >= 17) {
                                this.H = obtainStyledAttributes.getDimensionPixelSize(index, this.H);
                                break;
                            } else {
                                continue;
                            }
                        case 9:
                            this.f4080t = b.y(obtainStyledAttributes, index, this.f4080t);
                            continue;
                        case 10:
                            this.f4079s = b.y(obtainStyledAttributes, index, this.f4079s);
                            continue;
                        case 11:
                            this.M = obtainStyledAttributes.getDimensionPixelSize(index, this.M);
                            continue;
                        case 12:
                            this.N = obtainStyledAttributes.getDimensionPixelSize(index, this.N);
                            continue;
                        case 13:
                            this.J = obtainStyledAttributes.getDimensionPixelSize(index, this.J);
                            continue;
                        case 14:
                            this.L = obtainStyledAttributes.getDimensionPixelSize(index, this.L);
                            continue;
                        case 15:
                            this.O = obtainStyledAttributes.getDimensionPixelSize(index, this.O);
                            continue;
                        case 16:
                            this.K = obtainStyledAttributes.getDimensionPixelSize(index, this.K);
                            continue;
                        case 17:
                            this.f4060e = obtainStyledAttributes.getDimensionPixelOffset(index, this.f4060e);
                            continue;
                        case 18:
                            this.f4062f = obtainStyledAttributes.getDimensionPixelOffset(index, this.f4062f);
                            continue;
                        case 19:
                            this.f4064g = obtainStyledAttributes.getFloat(index, this.f4064g);
                            continue;
                        case 20:
                            this.f4081u = obtainStyledAttributes.getFloat(index, this.f4081u);
                            continue;
                        case 21:
                            this.f4058d = obtainStyledAttributes.getLayoutDimension(index, this.f4058d);
                            continue;
                        case 22:
                            this.f4056c = obtainStyledAttributes.getLayoutDimension(index, this.f4056c);
                            continue;
                        case 23:
                            this.D = obtainStyledAttributes.getDimensionPixelSize(index, this.D);
                            continue;
                        case 24:
                            this.f4066h = b.y(obtainStyledAttributes, index, this.f4066h);
                            continue;
                        case 25:
                            this.f4068i = b.y(obtainStyledAttributes, index, this.f4068i);
                            continue;
                        case 26:
                            this.C = obtainStyledAttributes.getInt(index, this.C);
                            continue;
                        case 27:
                            this.E = obtainStyledAttributes.getDimensionPixelSize(index, this.E);
                            continue;
                        case 28:
                            this.f4070j = b.y(obtainStyledAttributes, index, this.f4070j);
                            continue;
                        case 29:
                            this.f4072k = b.y(obtainStyledAttributes, index, this.f4072k);
                            continue;
                        case 30:
                            if (Build.VERSION.SDK_INT >= 17) {
                                this.I = obtainStyledAttributes.getDimensionPixelSize(index, this.I);
                                break;
                            } else {
                                continue;
                            }
                        case 31:
                            this.q = b.y(obtainStyledAttributes, index, this.q);
                            continue;
                        case 32:
                            this.f4078r = b.y(obtainStyledAttributes, index, this.f4078r);
                            continue;
                        case 33:
                            this.F = obtainStyledAttributes.getDimensionPixelSize(index, this.F);
                            continue;
                        case 34:
                            this.f4074m = b.y(obtainStyledAttributes, index, this.f4074m);
                            continue;
                        case 35:
                            this.f4073l = b.y(obtainStyledAttributes, index, this.f4073l);
                            continue;
                        case 36:
                            this.f4082v = obtainStyledAttributes.getFloat(index, this.f4082v);
                            continue;
                        case 37:
                            this.Q = obtainStyledAttributes.getFloat(index, this.Q);
                            continue;
                        case 38:
                            this.P = obtainStyledAttributes.getFloat(index, this.P);
                            continue;
                        case 39:
                            this.R = obtainStyledAttributes.getInt(index, this.R);
                            continue;
                        case 40:
                            this.S = obtainStyledAttributes.getInt(index, this.S);
                            continue;
                        default:
                            switch (i9) {
                                case 54:
                                    this.T = obtainStyledAttributes.getInt(index, this.T);
                                    continue;
                                case 55:
                                    this.U = obtainStyledAttributes.getInt(index, this.U);
                                    continue;
                                case 56:
                                    this.V = obtainStyledAttributes.getDimensionPixelSize(index, this.V);
                                    continue;
                                case 57:
                                    this.W = obtainStyledAttributes.getDimensionPixelSize(index, this.W);
                                    continue;
                                case 58:
                                    this.X = obtainStyledAttributes.getDimensionPixelSize(index, this.X);
                                    continue;
                                case 59:
                                    this.Y = obtainStyledAttributes.getDimensionPixelSize(index, this.Y);
                                    continue;
                                default:
                                    switch (i9) {
                                        case 61:
                                            this.f4084x = b.y(obtainStyledAttributes, index, this.f4084x);
                                            continue;
                                        case 62:
                                            this.f4085y = obtainStyledAttributes.getDimensionPixelSize(index, this.f4085y);
                                            continue;
                                        case 63:
                                            this.f4086z = obtainStyledAttributes.getFloat(index, this.f4086z);
                                            continue;
                                        default:
                                            switch (i9) {
                                                case 69:
                                                    this.Z = obtainStyledAttributes.getFloat(index, 1.0f);
                                                    break;
                                                case 70:
                                                    this.f4053a0 = obtainStyledAttributes.getFloat(index, 1.0f);
                                                    break;
                                                case 71:
                                                    Log.e("ConstraintSet", "CURRENTLY UNSUPPORTED");
                                                    break;
                                                case 72:
                                                    this.f4055b0 = obtainStyledAttributes.getInt(index, this.f4055b0);
                                                    break;
                                                case 73:
                                                    this.f4057c0 = obtainStyledAttributes.getDimensionPixelSize(index, this.f4057c0);
                                                    break;
                                                case 74:
                                                    this.f4063f0 = obtainStyledAttributes.getString(index);
                                                    break;
                                                case 75:
                                                    this.f4071j0 = obtainStyledAttributes.getBoolean(index, this.f4071j0);
                                                    break;
                                                case 76:
                                                    sb = new StringBuilder();
                                                    str = "unused attribute 0x";
                                                    sb.append(str);
                                                    sb.append(Integer.toHexString(index));
                                                    sb.append("   ");
                                                    sb.append(f4051k0.get(index));
                                                    Log.w("ConstraintSet", sb.toString());
                                                    continue;
                                                    continue;
                                                    continue;
                                                case 77:
                                                    this.f4065g0 = obtainStyledAttributes.getString(index);
                                                    break;
                                                default:
                                                    sb = new StringBuilder();
                                                    str = "Unknown attribute 0x";
                                                    sb.append(str);
                                                    sb.append(Integer.toHexString(index));
                                                    sb.append("   ");
                                                    sb.append(f4051k0.get(index));
                                                    Log.w("ConstraintSet", sb.toString());
                                                    continue;
                                                    continue;
                                                    continue;
                                            }
                                    }
                            }
                    }
                } else {
                    this.f4069i0 = obtainStyledAttributes.getBoolean(index, this.f4069i0);
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: h  reason: collision with root package name */
        private static SparseIntArray f4087h;

        /* renamed from: a  reason: collision with root package name */
        public boolean f4088a = false;

        /* renamed from: b  reason: collision with root package name */
        public int f4089b = -1;

        /* renamed from: c  reason: collision with root package name */
        public String f4090c = null;

        /* renamed from: d  reason: collision with root package name */
        public int f4091d = -1;

        /* renamed from: e  reason: collision with root package name */
        public int f4092e = 0;

        /* renamed from: f  reason: collision with root package name */
        public float f4093f = Float.NaN;

        /* renamed from: g  reason: collision with root package name */
        public float f4094g = Float.NaN;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f4087h = sparseIntArray;
            sparseIntArray.append(androidx.constraintlayout.widget.e.A6, 1);
            f4087h.append(androidx.constraintlayout.widget.e.C6, 2);
            f4087h.append(androidx.constraintlayout.widget.e.D6, 3);
            f4087h.append(androidx.constraintlayout.widget.e.f4345z6, 4);
            f4087h.append(androidx.constraintlayout.widget.e.f4337y6, 5);
            f4087h.append(androidx.constraintlayout.widget.e.B6, 6);
        }

        public void a(c cVar) {
            this.f4088a = cVar.f4088a;
            this.f4089b = cVar.f4089b;
            this.f4090c = cVar.f4090c;
            this.f4091d = cVar.f4091d;
            this.f4092e = cVar.f4092e;
            this.f4094g = cVar.f4094g;
            this.f4093f = cVar.f4093f;
        }

        void b(Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.f4328x6);
            this.f4088a = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                switch (f4087h.get(index)) {
                    case 1:
                        this.f4094g = obtainStyledAttributes.getFloat(index, this.f4094g);
                        break;
                    case 2:
                        this.f4091d = obtainStyledAttributes.getInt(index, this.f4091d);
                        break;
                    case 3:
                        this.f4090c = obtainStyledAttributes.peekValue(index).type == 3 ? obtainStyledAttributes.getString(index) : l0.c.f21520c[obtainStyledAttributes.getInteger(index, 0)];
                        break;
                    case 4:
                        this.f4092e = obtainStyledAttributes.getInt(index, 0);
                        break;
                    case 5:
                        this.f4089b = b.y(obtainStyledAttributes, index, this.f4089b);
                        break;
                    case 6:
                        this.f4093f = obtainStyledAttributes.getFloat(index, this.f4093f);
                        break;
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {

        /* renamed from: a  reason: collision with root package name */
        public boolean f4095a = false;

        /* renamed from: b  reason: collision with root package name */
        public int f4096b = 0;

        /* renamed from: c  reason: collision with root package name */
        public int f4097c = 0;

        /* renamed from: d  reason: collision with root package name */
        public float f4098d = 1.0f;

        /* renamed from: e  reason: collision with root package name */
        public float f4099e = Float.NaN;

        public void a(d dVar) {
            this.f4095a = dVar.f4095a;
            this.f4096b = dVar.f4096b;
            this.f4098d = dVar.f4098d;
            this.f4099e = dVar.f4099e;
            this.f4097c = dVar.f4097c;
        }

        void b(Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.f4242n7);
            this.f4095a = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == androidx.constraintlayout.widget.e.f4259p7) {
                    this.f4098d = obtainStyledAttributes.getFloat(index, this.f4098d);
                } else if (index == androidx.constraintlayout.widget.e.f4250o7) {
                    this.f4096b = obtainStyledAttributes.getInt(index, this.f4096b);
                    this.f4096b = b.f4039e[this.f4096b];
                } else if (index == androidx.constraintlayout.widget.e.f4275r7) {
                    this.f4097c = obtainStyledAttributes.getInt(index, this.f4097c);
                } else if (index == androidx.constraintlayout.widget.e.f4267q7) {
                    this.f4099e = obtainStyledAttributes.getFloat(index, this.f4099e);
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e {

        /* renamed from: n  reason: collision with root package name */
        private static SparseIntArray f4100n;

        /* renamed from: a  reason: collision with root package name */
        public boolean f4101a = false;

        /* renamed from: b  reason: collision with root package name */
        public float f4102b = 0.0f;

        /* renamed from: c  reason: collision with root package name */
        public float f4103c = 0.0f;

        /* renamed from: d  reason: collision with root package name */
        public float f4104d = 0.0f;

        /* renamed from: e  reason: collision with root package name */
        public float f4105e = 1.0f;

        /* renamed from: f  reason: collision with root package name */
        public float f4106f = 1.0f;

        /* renamed from: g  reason: collision with root package name */
        public float f4107g = Float.NaN;

        /* renamed from: h  reason: collision with root package name */
        public float f4108h = Float.NaN;

        /* renamed from: i  reason: collision with root package name */
        public float f4109i = 0.0f;

        /* renamed from: j  reason: collision with root package name */
        public float f4110j = 0.0f;

        /* renamed from: k  reason: collision with root package name */
        public float f4111k = 0.0f;

        /* renamed from: l  reason: collision with root package name */
        public boolean f4112l = false;

        /* renamed from: m  reason: collision with root package name */
        public float f4113m = 0.0f;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f4100n = sparseIntArray;
            sparseIntArray.append(androidx.constraintlayout.widget.e.M7, 1);
            f4100n.append(androidx.constraintlayout.widget.e.N7, 2);
            f4100n.append(androidx.constraintlayout.widget.e.O7, 3);
            f4100n.append(androidx.constraintlayout.widget.e.K7, 4);
            f4100n.append(androidx.constraintlayout.widget.e.L7, 5);
            f4100n.append(androidx.constraintlayout.widget.e.G7, 6);
            f4100n.append(androidx.constraintlayout.widget.e.H7, 7);
            f4100n.append(androidx.constraintlayout.widget.e.I7, 8);
            f4100n.append(androidx.constraintlayout.widget.e.J7, 9);
            f4100n.append(androidx.constraintlayout.widget.e.P7, 10);
            f4100n.append(androidx.constraintlayout.widget.e.Q7, 11);
        }

        public void a(e eVar) {
            this.f4101a = eVar.f4101a;
            this.f4102b = eVar.f4102b;
            this.f4103c = eVar.f4103c;
            this.f4104d = eVar.f4104d;
            this.f4105e = eVar.f4105e;
            this.f4106f = eVar.f4106f;
            this.f4107g = eVar.f4107g;
            this.f4108h = eVar.f4108h;
            this.f4109i = eVar.f4109i;
            this.f4110j = eVar.f4110j;
            this.f4111k = eVar.f4111k;
            this.f4112l = eVar.f4112l;
            this.f4113m = eVar.f4113m;
        }

        void b(Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.F7);
            this.f4101a = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                switch (f4100n.get(index)) {
                    case 1:
                        this.f4102b = obtainStyledAttributes.getFloat(index, this.f4102b);
                        break;
                    case 2:
                        this.f4103c = obtainStyledAttributes.getFloat(index, this.f4103c);
                        break;
                    case 3:
                        this.f4104d = obtainStyledAttributes.getFloat(index, this.f4104d);
                        break;
                    case 4:
                        this.f4105e = obtainStyledAttributes.getFloat(index, this.f4105e);
                        break;
                    case 5:
                        this.f4106f = obtainStyledAttributes.getFloat(index, this.f4106f);
                        break;
                    case 6:
                        this.f4107g = obtainStyledAttributes.getDimension(index, this.f4107g);
                        break;
                    case 7:
                        this.f4108h = obtainStyledAttributes.getDimension(index, this.f4108h);
                        break;
                    case 8:
                        this.f4109i = obtainStyledAttributes.getDimension(index, this.f4109i);
                        break;
                    case 9:
                        this.f4110j = obtainStyledAttributes.getDimension(index, this.f4110j);
                        break;
                    case 10:
                        if (Build.VERSION.SDK_INT >= 21) {
                            this.f4111k = obtainStyledAttributes.getDimension(index, this.f4111k);
                            break;
                        } else {
                            break;
                        }
                    case 11:
                        if (Build.VERSION.SDK_INT >= 21) {
                            this.f4112l = true;
                            this.f4113m = obtainStyledAttributes.getDimension(index, this.f4113m);
                            break;
                        } else {
                            break;
                        }
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        f4040f = sparseIntArray;
        sparseIntArray.append(androidx.constraintlayout.widget.e.f4295u0, 25);
        f4040f.append(androidx.constraintlayout.widget.e.f4304v0, 26);
        f4040f.append(androidx.constraintlayout.widget.e.f4322x0, 29);
        f4040f.append(androidx.constraintlayout.widget.e.f4331y0, 30);
        f4040f.append(androidx.constraintlayout.widget.e.E0, 36);
        f4040f.append(androidx.constraintlayout.widget.e.D0, 35);
        f4040f.append(androidx.constraintlayout.widget.e.f4136c0, 4);
        f4040f.append(androidx.constraintlayout.widget.e.f4126b0, 3);
        f4040f.append(androidx.constraintlayout.widget.e.Z, 1);
        f4040f.append(androidx.constraintlayout.widget.e.M0, 6);
        f4040f.append(androidx.constraintlayout.widget.e.N0, 7);
        f4040f.append(androidx.constraintlayout.widget.e.f4199j0, 17);
        f4040f.append(androidx.constraintlayout.widget.e.f4208k0, 18);
        f4040f.append(androidx.constraintlayout.widget.e.f4217l0, 19);
        f4040f.append(androidx.constraintlayout.widget.e.f4276s, 27);
        f4040f.append(androidx.constraintlayout.widget.e.f4340z0, 32);
        f4040f.append(androidx.constraintlayout.widget.e.A0, 33);
        f4040f.append(androidx.constraintlayout.widget.e.f4190i0, 10);
        f4040f.append(androidx.constraintlayout.widget.e.f4181h0, 9);
        f4040f.append(androidx.constraintlayout.widget.e.Q0, 13);
        f4040f.append(androidx.constraintlayout.widget.e.T0, 16);
        f4040f.append(androidx.constraintlayout.widget.e.R0, 14);
        f4040f.append(androidx.constraintlayout.widget.e.O0, 11);
        f4040f.append(androidx.constraintlayout.widget.e.S0, 15);
        f4040f.append(androidx.constraintlayout.widget.e.P0, 12);
        f4040f.append(androidx.constraintlayout.widget.e.H0, 40);
        f4040f.append(androidx.constraintlayout.widget.e.f4277s0, 39);
        f4040f.append(androidx.constraintlayout.widget.e.f4269r0, 41);
        f4040f.append(androidx.constraintlayout.widget.e.G0, 42);
        f4040f.append(androidx.constraintlayout.widget.e.f4260q0, 20);
        f4040f.append(androidx.constraintlayout.widget.e.F0, 37);
        f4040f.append(androidx.constraintlayout.widget.e.f4172g0, 5);
        f4040f.append(androidx.constraintlayout.widget.e.f4286t0, 82);
        f4040f.append(androidx.constraintlayout.widget.e.C0, 82);
        f4040f.append(androidx.constraintlayout.widget.e.f4313w0, 82);
        f4040f.append(androidx.constraintlayout.widget.e.f4116a0, 82);
        f4040f.append(androidx.constraintlayout.widget.e.Y, 82);
        f4040f.append(androidx.constraintlayout.widget.e.f4321x, 24);
        f4040f.append(androidx.constraintlayout.widget.e.f4339z, 28);
        f4040f.append(androidx.constraintlayout.widget.e.L, 31);
        f4040f.append(androidx.constraintlayout.widget.e.M, 8);
        f4040f.append(androidx.constraintlayout.widget.e.f4330y, 34);
        f4040f.append(androidx.constraintlayout.widget.e.A, 2);
        f4040f.append(androidx.constraintlayout.widget.e.f4303v, 23);
        f4040f.append(androidx.constraintlayout.widget.e.f4312w, 21);
        f4040f.append(androidx.constraintlayout.widget.e.f4294u, 22);
        f4040f.append(androidx.constraintlayout.widget.e.B, 43);
        f4040f.append(androidx.constraintlayout.widget.e.O, 44);
        f4040f.append(androidx.constraintlayout.widget.e.J, 45);
        f4040f.append(androidx.constraintlayout.widget.e.K, 46);
        f4040f.append(androidx.constraintlayout.widget.e.I, 60);
        f4040f.append(androidx.constraintlayout.widget.e.G, 47);
        f4040f.append(androidx.constraintlayout.widget.e.H, 48);
        f4040f.append(androidx.constraintlayout.widget.e.C, 49);
        f4040f.append(androidx.constraintlayout.widget.e.D, 50);
        f4040f.append(androidx.constraintlayout.widget.e.E, 51);
        f4040f.append(androidx.constraintlayout.widget.e.F, 52);
        f4040f.append(androidx.constraintlayout.widget.e.N, 53);
        f4040f.append(androidx.constraintlayout.widget.e.I0, 54);
        f4040f.append(androidx.constraintlayout.widget.e.f4226m0, 55);
        f4040f.append(androidx.constraintlayout.widget.e.J0, 56);
        f4040f.append(androidx.constraintlayout.widget.e.f4235n0, 57);
        f4040f.append(androidx.constraintlayout.widget.e.K0, 58);
        f4040f.append(androidx.constraintlayout.widget.e.f4244o0, 59);
        f4040f.append(androidx.constraintlayout.widget.e.f4146d0, 61);
        f4040f.append(androidx.constraintlayout.widget.e.f4164f0, 62);
        f4040f.append(androidx.constraintlayout.widget.e.f4155e0, 63);
        f4040f.append(androidx.constraintlayout.widget.e.P, 64);
        f4040f.append(androidx.constraintlayout.widget.e.X0, 65);
        f4040f.append(androidx.constraintlayout.widget.e.V, 66);
        f4040f.append(androidx.constraintlayout.widget.e.Y0, 67);
        f4040f.append(androidx.constraintlayout.widget.e.V0, 79);
        f4040f.append(androidx.constraintlayout.widget.e.f4285t, 38);
        f4040f.append(androidx.constraintlayout.widget.e.U0, 68);
        f4040f.append(androidx.constraintlayout.widget.e.L0, 69);
        f4040f.append(androidx.constraintlayout.widget.e.f4252p0, 70);
        f4040f.append(androidx.constraintlayout.widget.e.T, 71);
        f4040f.append(androidx.constraintlayout.widget.e.R, 72);
        f4040f.append(androidx.constraintlayout.widget.e.S, 73);
        f4040f.append(androidx.constraintlayout.widget.e.U, 74);
        f4040f.append(androidx.constraintlayout.widget.e.Q, 75);
        f4040f.append(androidx.constraintlayout.widget.e.W0, 76);
        f4040f.append(androidx.constraintlayout.widget.e.B0, 77);
        f4040f.append(androidx.constraintlayout.widget.e.Z0, 78);
        f4040f.append(androidx.constraintlayout.widget.e.X, 80);
        f4040f.append(androidx.constraintlayout.widget.e.W, 81);
    }

    private int[] m(View view, String str) {
        int i8;
        Object f5;
        String[] split = str.split(",");
        Context context = view.getContext();
        int[] iArr = new int[split.length];
        int i9 = 0;
        int i10 = 0;
        while (i9 < split.length) {
            String trim = split[i9].trim();
            try {
                i8 = androidx.constraintlayout.widget.d.class.getField(trim).getInt(null);
            } catch (Exception unused) {
                i8 = 0;
            }
            if (i8 == 0) {
                i8 = context.getResources().getIdentifier(trim, "id", context.getPackageName());
            }
            if (i8 == 0 && view.isInEditMode() && (view.getParent() instanceof ConstraintLayout) && (f5 = ((ConstraintLayout) view.getParent()).f(0, trim)) != null && (f5 instanceof Integer)) {
                i8 = ((Integer) f5).intValue();
            }
            iArr[i10] = i8;
            i9++;
            i10++;
        }
        return i10 != split.length ? Arrays.copyOf(iArr, i10) : iArr;
    }

    private a n(Context context, AttributeSet attributeSet) {
        a aVar = new a();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.f4268r);
        z(context, aVar, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        return aVar;
    }

    private a o(int i8) {
        if (!this.f4044d.containsKey(Integer.valueOf(i8))) {
            this.f4044d.put(Integer.valueOf(i8), new a());
        }
        return this.f4044d.get(Integer.valueOf(i8));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int y(TypedArray typedArray, int i8, int i9) {
        int resourceId = typedArray.getResourceId(i8, i9);
        return resourceId == -1 ? typedArray.getInt(i8, -1) : resourceId;
    }

    private void z(Context context, a aVar, TypedArray typedArray) {
        c cVar;
        String str;
        StringBuilder sb;
        String str2;
        int indexCount = typedArray.getIndexCount();
        for (int i8 = 0; i8 < indexCount; i8++) {
            int index = typedArray.getIndex(i8);
            if (index != androidx.constraintlayout.widget.e.f4285t && androidx.constraintlayout.widget.e.L != index && androidx.constraintlayout.widget.e.M != index) {
                aVar.f4047c.f4088a = true;
                aVar.f4048d.f4054b = true;
                aVar.f4046b.f4095a = true;
                aVar.f4049e.f4101a = true;
            }
            switch (f4040f.get(index)) {
                case 1:
                    C0027b c0027b = aVar.f4048d;
                    c0027b.f4077p = y(typedArray, index, c0027b.f4077p);
                    continue;
                case 2:
                    C0027b c0027b2 = aVar.f4048d;
                    c0027b2.G = typedArray.getDimensionPixelSize(index, c0027b2.G);
                    continue;
                case 3:
                    C0027b c0027b3 = aVar.f4048d;
                    c0027b3.f4076o = y(typedArray, index, c0027b3.f4076o);
                    continue;
                case 4:
                    C0027b c0027b4 = aVar.f4048d;
                    c0027b4.f4075n = y(typedArray, index, c0027b4.f4075n);
                    continue;
                case 5:
                    aVar.f4048d.f4083w = typedArray.getString(index);
                    continue;
                case 6:
                    C0027b c0027b5 = aVar.f4048d;
                    c0027b5.A = typedArray.getDimensionPixelOffset(index, c0027b5.A);
                    continue;
                case 7:
                    C0027b c0027b6 = aVar.f4048d;
                    c0027b6.B = typedArray.getDimensionPixelOffset(index, c0027b6.B);
                    continue;
                case 8:
                    if (Build.VERSION.SDK_INT >= 17) {
                        C0027b c0027b7 = aVar.f4048d;
                        c0027b7.H = typedArray.getDimensionPixelSize(index, c0027b7.H);
                    } else {
                        continue;
                    }
                case 9:
                    C0027b c0027b8 = aVar.f4048d;
                    c0027b8.f4080t = y(typedArray, index, c0027b8.f4080t);
                    continue;
                case 10:
                    C0027b c0027b9 = aVar.f4048d;
                    c0027b9.f4079s = y(typedArray, index, c0027b9.f4079s);
                    continue;
                case 11:
                    C0027b c0027b10 = aVar.f4048d;
                    c0027b10.M = typedArray.getDimensionPixelSize(index, c0027b10.M);
                    continue;
                case 12:
                    C0027b c0027b11 = aVar.f4048d;
                    c0027b11.N = typedArray.getDimensionPixelSize(index, c0027b11.N);
                    continue;
                case 13:
                    C0027b c0027b12 = aVar.f4048d;
                    c0027b12.J = typedArray.getDimensionPixelSize(index, c0027b12.J);
                    continue;
                case 14:
                    C0027b c0027b13 = aVar.f4048d;
                    c0027b13.L = typedArray.getDimensionPixelSize(index, c0027b13.L);
                    continue;
                case 15:
                    C0027b c0027b14 = aVar.f4048d;
                    c0027b14.O = typedArray.getDimensionPixelSize(index, c0027b14.O);
                    continue;
                case 16:
                    C0027b c0027b15 = aVar.f4048d;
                    c0027b15.K = typedArray.getDimensionPixelSize(index, c0027b15.K);
                    continue;
                case 17:
                    C0027b c0027b16 = aVar.f4048d;
                    c0027b16.f4060e = typedArray.getDimensionPixelOffset(index, c0027b16.f4060e);
                    continue;
                case 18:
                    C0027b c0027b17 = aVar.f4048d;
                    c0027b17.f4062f = typedArray.getDimensionPixelOffset(index, c0027b17.f4062f);
                    continue;
                case 19:
                    C0027b c0027b18 = aVar.f4048d;
                    c0027b18.f4064g = typedArray.getFloat(index, c0027b18.f4064g);
                    continue;
                case 20:
                    C0027b c0027b19 = aVar.f4048d;
                    c0027b19.f4081u = typedArray.getFloat(index, c0027b19.f4081u);
                    continue;
                case 21:
                    C0027b c0027b20 = aVar.f4048d;
                    c0027b20.f4058d = typedArray.getLayoutDimension(index, c0027b20.f4058d);
                    continue;
                case 22:
                    d dVar = aVar.f4046b;
                    dVar.f4096b = typedArray.getInt(index, dVar.f4096b);
                    d dVar2 = aVar.f4046b;
                    dVar2.f4096b = f4039e[dVar2.f4096b];
                    continue;
                case 23:
                    C0027b c0027b21 = aVar.f4048d;
                    c0027b21.f4056c = typedArray.getLayoutDimension(index, c0027b21.f4056c);
                    continue;
                case 24:
                    C0027b c0027b22 = aVar.f4048d;
                    c0027b22.D = typedArray.getDimensionPixelSize(index, c0027b22.D);
                    continue;
                case 25:
                    C0027b c0027b23 = aVar.f4048d;
                    c0027b23.f4066h = y(typedArray, index, c0027b23.f4066h);
                    continue;
                case 26:
                    C0027b c0027b24 = aVar.f4048d;
                    c0027b24.f4068i = y(typedArray, index, c0027b24.f4068i);
                    continue;
                case 27:
                    C0027b c0027b25 = aVar.f4048d;
                    c0027b25.C = typedArray.getInt(index, c0027b25.C);
                    continue;
                case 28:
                    C0027b c0027b26 = aVar.f4048d;
                    c0027b26.E = typedArray.getDimensionPixelSize(index, c0027b26.E);
                    continue;
                case 29:
                    C0027b c0027b27 = aVar.f4048d;
                    c0027b27.f4070j = y(typedArray, index, c0027b27.f4070j);
                    continue;
                case 30:
                    C0027b c0027b28 = aVar.f4048d;
                    c0027b28.f4072k = y(typedArray, index, c0027b28.f4072k);
                    continue;
                case 31:
                    if (Build.VERSION.SDK_INT >= 17) {
                        C0027b c0027b29 = aVar.f4048d;
                        c0027b29.I = typedArray.getDimensionPixelSize(index, c0027b29.I);
                    } else {
                        continue;
                    }
                case 32:
                    C0027b c0027b30 = aVar.f4048d;
                    c0027b30.q = y(typedArray, index, c0027b30.q);
                    continue;
                case 33:
                    C0027b c0027b31 = aVar.f4048d;
                    c0027b31.f4078r = y(typedArray, index, c0027b31.f4078r);
                    continue;
                case 34:
                    C0027b c0027b32 = aVar.f4048d;
                    c0027b32.F = typedArray.getDimensionPixelSize(index, c0027b32.F);
                    continue;
                case 35:
                    C0027b c0027b33 = aVar.f4048d;
                    c0027b33.f4074m = y(typedArray, index, c0027b33.f4074m);
                    continue;
                case 36:
                    C0027b c0027b34 = aVar.f4048d;
                    c0027b34.f4073l = y(typedArray, index, c0027b34.f4073l);
                    continue;
                case 37:
                    C0027b c0027b35 = aVar.f4048d;
                    c0027b35.f4082v = typedArray.getFloat(index, c0027b35.f4082v);
                    continue;
                case 38:
                    aVar.f4045a = typedArray.getResourceId(index, aVar.f4045a);
                    continue;
                case 39:
                    C0027b c0027b36 = aVar.f4048d;
                    c0027b36.Q = typedArray.getFloat(index, c0027b36.Q);
                    continue;
                case 40:
                    C0027b c0027b37 = aVar.f4048d;
                    c0027b37.P = typedArray.getFloat(index, c0027b37.P);
                    continue;
                case 41:
                    C0027b c0027b38 = aVar.f4048d;
                    c0027b38.R = typedArray.getInt(index, c0027b38.R);
                    continue;
                case 42:
                    C0027b c0027b39 = aVar.f4048d;
                    c0027b39.S = typedArray.getInt(index, c0027b39.S);
                    continue;
                case 43:
                    d dVar3 = aVar.f4046b;
                    dVar3.f4098d = typedArray.getFloat(index, dVar3.f4098d);
                    continue;
                case 44:
                    if (Build.VERSION.SDK_INT >= 21) {
                        e eVar = aVar.f4049e;
                        eVar.f4112l = true;
                        eVar.f4113m = typedArray.getDimension(index, eVar.f4113m);
                    } else {
                        continue;
                    }
                case 45:
                    e eVar2 = aVar.f4049e;
                    eVar2.f4103c = typedArray.getFloat(index, eVar2.f4103c);
                    continue;
                case 46:
                    e eVar3 = aVar.f4049e;
                    eVar3.f4104d = typedArray.getFloat(index, eVar3.f4104d);
                    continue;
                case 47:
                    e eVar4 = aVar.f4049e;
                    eVar4.f4105e = typedArray.getFloat(index, eVar4.f4105e);
                    continue;
                case 48:
                    e eVar5 = aVar.f4049e;
                    eVar5.f4106f = typedArray.getFloat(index, eVar5.f4106f);
                    continue;
                case 49:
                    e eVar6 = aVar.f4049e;
                    eVar6.f4107g = typedArray.getDimension(index, eVar6.f4107g);
                    continue;
                case 50:
                    e eVar7 = aVar.f4049e;
                    eVar7.f4108h = typedArray.getDimension(index, eVar7.f4108h);
                    continue;
                case 51:
                    e eVar8 = aVar.f4049e;
                    eVar8.f4109i = typedArray.getDimension(index, eVar8.f4109i);
                    continue;
                case 52:
                    e eVar9 = aVar.f4049e;
                    eVar9.f4110j = typedArray.getDimension(index, eVar9.f4110j);
                    continue;
                case 53:
                    if (Build.VERSION.SDK_INT >= 21) {
                        e eVar10 = aVar.f4049e;
                        eVar10.f4111k = typedArray.getDimension(index, eVar10.f4111k);
                    } else {
                        continue;
                    }
                case 54:
                    C0027b c0027b40 = aVar.f4048d;
                    c0027b40.T = typedArray.getInt(index, c0027b40.T);
                    continue;
                case 55:
                    C0027b c0027b41 = aVar.f4048d;
                    c0027b41.U = typedArray.getInt(index, c0027b41.U);
                    continue;
                case 56:
                    C0027b c0027b42 = aVar.f4048d;
                    c0027b42.V = typedArray.getDimensionPixelSize(index, c0027b42.V);
                    continue;
                case 57:
                    C0027b c0027b43 = aVar.f4048d;
                    c0027b43.W = typedArray.getDimensionPixelSize(index, c0027b43.W);
                    continue;
                case 58:
                    C0027b c0027b44 = aVar.f4048d;
                    c0027b44.X = typedArray.getDimensionPixelSize(index, c0027b44.X);
                    continue;
                case 59:
                    C0027b c0027b45 = aVar.f4048d;
                    c0027b45.Y = typedArray.getDimensionPixelSize(index, c0027b45.Y);
                    continue;
                case 60:
                    e eVar11 = aVar.f4049e;
                    eVar11.f4102b = typedArray.getFloat(index, eVar11.f4102b);
                    continue;
                case 61:
                    C0027b c0027b46 = aVar.f4048d;
                    c0027b46.f4084x = y(typedArray, index, c0027b46.f4084x);
                    continue;
                case 62:
                    C0027b c0027b47 = aVar.f4048d;
                    c0027b47.f4085y = typedArray.getDimensionPixelSize(index, c0027b47.f4085y);
                    continue;
                case 63:
                    C0027b c0027b48 = aVar.f4048d;
                    c0027b48.f4086z = typedArray.getFloat(index, c0027b48.f4086z);
                    continue;
                case 64:
                    c cVar2 = aVar.f4047c;
                    cVar2.f4089b = y(typedArray, index, cVar2.f4089b);
                    continue;
                case 65:
                    if (typedArray.peekValue(index).type == 3) {
                        cVar = aVar.f4047c;
                        str = typedArray.getString(index);
                    } else {
                        cVar = aVar.f4047c;
                        str = l0.c.f21520c[typedArray.getInteger(index, 0)];
                    }
                    cVar.f4090c = str;
                    continue;
                case 66:
                    aVar.f4047c.f4092e = typedArray.getInt(index, 0);
                    continue;
                case 67:
                    c cVar3 = aVar.f4047c;
                    cVar3.f4094g = typedArray.getFloat(index, cVar3.f4094g);
                    continue;
                case 68:
                    d dVar4 = aVar.f4046b;
                    dVar4.f4099e = typedArray.getFloat(index, dVar4.f4099e);
                    continue;
                case 69:
                    aVar.f4048d.Z = typedArray.getFloat(index, 1.0f);
                    continue;
                case 70:
                    aVar.f4048d.f4053a0 = typedArray.getFloat(index, 1.0f);
                    continue;
                case 71:
                    Log.e("ConstraintSet", "CURRENTLY UNSUPPORTED");
                    continue;
                case 72:
                    C0027b c0027b49 = aVar.f4048d;
                    c0027b49.f4055b0 = typedArray.getInt(index, c0027b49.f4055b0);
                    continue;
                case 73:
                    C0027b c0027b50 = aVar.f4048d;
                    c0027b50.f4057c0 = typedArray.getDimensionPixelSize(index, c0027b50.f4057c0);
                    continue;
                case 74:
                    aVar.f4048d.f4063f0 = typedArray.getString(index);
                    continue;
                case 75:
                    C0027b c0027b51 = aVar.f4048d;
                    c0027b51.f4071j0 = typedArray.getBoolean(index, c0027b51.f4071j0);
                    continue;
                case 76:
                    c cVar4 = aVar.f4047c;
                    cVar4.f4091d = typedArray.getInt(index, cVar4.f4091d);
                    continue;
                case 77:
                    aVar.f4048d.f4065g0 = typedArray.getString(index);
                    continue;
                case 78:
                    d dVar5 = aVar.f4046b;
                    dVar5.f4097c = typedArray.getInt(index, dVar5.f4097c);
                    continue;
                case 79:
                    c cVar5 = aVar.f4047c;
                    cVar5.f4093f = typedArray.getFloat(index, cVar5.f4093f);
                    continue;
                case 80:
                    C0027b c0027b52 = aVar.f4048d;
                    c0027b52.f4067h0 = typedArray.getBoolean(index, c0027b52.f4067h0);
                    continue;
                case 81:
                    C0027b c0027b53 = aVar.f4048d;
                    c0027b53.f4069i0 = typedArray.getBoolean(index, c0027b53.f4069i0);
                    continue;
                case 82:
                    sb = new StringBuilder();
                    str2 = "unused attribute 0x";
                    break;
                default:
                    sb = new StringBuilder();
                    str2 = "Unknown attribute 0x";
                    break;
            }
            sb.append(str2);
            sb.append(Integer.toHexString(index));
            sb.append("   ");
            sb.append(f4040f.get(index));
            Log.w("ConstraintSet", sb.toString());
        }
    }

    public void A(ConstraintLayout constraintLayout) {
        int childCount = constraintLayout.getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = constraintLayout.getChildAt(i8);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) childAt.getLayoutParams();
            int id = childAt.getId();
            if (this.f4043c && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.f4044d.containsKey(Integer.valueOf(id))) {
                this.f4044d.put(Integer.valueOf(id), new a());
            }
            a aVar = this.f4044d.get(Integer.valueOf(id));
            if (!aVar.f4048d.f4054b) {
                aVar.f(id, layoutParams);
                if (childAt instanceof ConstraintHelper) {
                    aVar.f4048d.f4061e0 = ((ConstraintHelper) childAt).getReferencedIds();
                    if (childAt instanceof Barrier) {
                        Barrier barrier = (Barrier) childAt;
                        aVar.f4048d.f4071j0 = barrier.v();
                        aVar.f4048d.f4055b0 = barrier.getType();
                        aVar.f4048d.f4057c0 = barrier.getMargin();
                    }
                }
                aVar.f4048d.f4054b = true;
            }
            d dVar = aVar.f4046b;
            if (!dVar.f4095a) {
                dVar.f4096b = childAt.getVisibility();
                aVar.f4046b.f4098d = childAt.getAlpha();
                aVar.f4046b.f4095a = true;
            }
            int i9 = Build.VERSION.SDK_INT;
            if (i9 >= 17) {
                e eVar = aVar.f4049e;
                if (!eVar.f4101a) {
                    eVar.f4101a = true;
                    eVar.f4102b = childAt.getRotation();
                    aVar.f4049e.f4103c = childAt.getRotationX();
                    aVar.f4049e.f4104d = childAt.getRotationY();
                    aVar.f4049e.f4105e = childAt.getScaleX();
                    aVar.f4049e.f4106f = childAt.getScaleY();
                    float pivotX = childAt.getPivotX();
                    float pivotY = childAt.getPivotY();
                    if (pivotX != 0.0d || pivotY != 0.0d) {
                        e eVar2 = aVar.f4049e;
                        eVar2.f4107g = pivotX;
                        eVar2.f4108h = pivotY;
                    }
                    aVar.f4049e.f4109i = childAt.getTranslationX();
                    aVar.f4049e.f4110j = childAt.getTranslationY();
                    if (i9 >= 21) {
                        aVar.f4049e.f4111k = childAt.getTranslationZ();
                        e eVar3 = aVar.f4049e;
                        if (eVar3.f4112l) {
                            eVar3.f4113m = childAt.getElevation();
                        }
                    }
                }
            }
        }
    }

    public void B(b bVar) {
        for (Integer num : bVar.f4044d.keySet()) {
            int intValue = num.intValue();
            a aVar = bVar.f4044d.get(num);
            if (!this.f4044d.containsKey(Integer.valueOf(intValue))) {
                this.f4044d.put(Integer.valueOf(intValue), new a());
            }
            a aVar2 = this.f4044d.get(Integer.valueOf(intValue));
            C0027b c0027b = aVar2.f4048d;
            if (!c0027b.f4054b) {
                c0027b.a(aVar.f4048d);
            }
            d dVar = aVar2.f4046b;
            if (!dVar.f4095a) {
                dVar.a(aVar.f4046b);
            }
            e eVar = aVar2.f4049e;
            if (!eVar.f4101a) {
                eVar.a(aVar.f4049e);
            }
            c cVar = aVar2.f4047c;
            if (!cVar.f4088a) {
                cVar.a(aVar.f4047c);
            }
            for (String str : aVar.f4050f.keySet()) {
                if (!aVar2.f4050f.containsKey(str)) {
                    aVar2.f4050f.put(str, aVar.f4050f.get(str));
                }
            }
        }
    }

    public void C(boolean z4) {
        this.f4043c = z4;
    }

    public void D(boolean z4) {
        this.f4041a = z4;
    }

    public void c(ConstraintLayout constraintLayout) {
        int childCount = constraintLayout.getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = constraintLayout.getChildAt(i8);
            int id = childAt.getId();
            if (!this.f4044d.containsKey(Integer.valueOf(id))) {
                Log.v("ConstraintSet", "id unknown " + androidx.constraintlayout.motion.widget.a.c(childAt));
            } else if (this.f4043c && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            } else {
                if (this.f4044d.containsKey(Integer.valueOf(id))) {
                    ConstraintAttribute.h(childAt, this.f4044d.get(Integer.valueOf(id)).f4050f);
                }
            }
        }
    }

    public void d(ConstraintLayout constraintLayout) {
        f(constraintLayout, true);
        constraintLayout.setConstraintSet(null);
        constraintLayout.requestLayout();
    }

    public void e(ConstraintHelper constraintHelper, ConstraintWidget constraintWidget, ConstraintLayout.LayoutParams layoutParams, SparseArray<ConstraintWidget> sparseArray) {
        int id = constraintHelper.getId();
        if (this.f4044d.containsKey(Integer.valueOf(id))) {
            a aVar = this.f4044d.get(Integer.valueOf(id));
            if (constraintWidget instanceof n0.b) {
                constraintHelper.n(aVar, (n0.b) constraintWidget, layoutParams, sparseArray);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(ConstraintLayout constraintLayout, boolean z4) {
        int childCount = constraintLayout.getChildCount();
        HashSet hashSet = new HashSet(this.f4044d.keySet());
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = constraintLayout.getChildAt(i8);
            int id = childAt.getId();
            if (!this.f4044d.containsKey(Integer.valueOf(id))) {
                Log.w("ConstraintSet", "id unknown " + androidx.constraintlayout.motion.widget.a.c(childAt));
            } else if (this.f4043c && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            } else {
                if (id != -1) {
                    if (this.f4044d.containsKey(Integer.valueOf(id))) {
                        hashSet.remove(Integer.valueOf(id));
                        a aVar = this.f4044d.get(Integer.valueOf(id));
                        if (childAt instanceof Barrier) {
                            aVar.f4048d.f4059d0 = 1;
                        }
                        int i9 = aVar.f4048d.f4059d0;
                        if (i9 != -1 && i9 == 1) {
                            Barrier barrier = (Barrier) childAt;
                            barrier.setId(id);
                            barrier.setType(aVar.f4048d.f4055b0);
                            barrier.setMargin(aVar.f4048d.f4057c0);
                            barrier.setAllowsGoneWidget(aVar.f4048d.f4071j0);
                            C0027b c0027b = aVar.f4048d;
                            int[] iArr = c0027b.f4061e0;
                            if (iArr != null) {
                                barrier.setReferencedIds(iArr);
                            } else {
                                String str = c0027b.f4063f0;
                                if (str != null) {
                                    c0027b.f4061e0 = m(barrier, str);
                                    barrier.setReferencedIds(aVar.f4048d.f4061e0);
                                }
                            }
                        }
                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) childAt.getLayoutParams();
                        layoutParams.c();
                        aVar.d(layoutParams);
                        if (z4) {
                            ConstraintAttribute.h(childAt, aVar.f4050f);
                        }
                        childAt.setLayoutParams(layoutParams);
                        d dVar = aVar.f4046b;
                        if (dVar.f4097c == 0) {
                            childAt.setVisibility(dVar.f4096b);
                        }
                        int i10 = Build.VERSION.SDK_INT;
                        if (i10 >= 17) {
                            childAt.setAlpha(aVar.f4046b.f4098d);
                            childAt.setRotation(aVar.f4049e.f4102b);
                            childAt.setRotationX(aVar.f4049e.f4103c);
                            childAt.setRotationY(aVar.f4049e.f4104d);
                            childAt.setScaleX(aVar.f4049e.f4105e);
                            childAt.setScaleY(aVar.f4049e.f4106f);
                            if (!Float.isNaN(aVar.f4049e.f4107g)) {
                                childAt.setPivotX(aVar.f4049e.f4107g);
                            }
                            if (!Float.isNaN(aVar.f4049e.f4108h)) {
                                childAt.setPivotY(aVar.f4049e.f4108h);
                            }
                            childAt.setTranslationX(aVar.f4049e.f4109i);
                            childAt.setTranslationY(aVar.f4049e.f4110j);
                            if (i10 >= 21) {
                                childAt.setTranslationZ(aVar.f4049e.f4111k);
                                e eVar = aVar.f4049e;
                                if (eVar.f4112l) {
                                    childAt.setElevation(eVar.f4113m);
                                }
                            }
                        }
                    } else {
                        Log.v("ConstraintSet", "WARNING NO CONSTRAINTS for view " + id);
                    }
                }
            }
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            Integer num = (Integer) it.next();
            a aVar2 = this.f4044d.get(num);
            int i11 = aVar2.f4048d.f4059d0;
            if (i11 != -1 && i11 == 1) {
                Barrier barrier2 = new Barrier(constraintLayout.getContext());
                barrier2.setId(num.intValue());
                C0027b c0027b2 = aVar2.f4048d;
                int[] iArr2 = c0027b2.f4061e0;
                if (iArr2 != null) {
                    barrier2.setReferencedIds(iArr2);
                } else {
                    String str2 = c0027b2.f4063f0;
                    if (str2 != null) {
                        c0027b2.f4061e0 = m(barrier2, str2);
                        barrier2.setReferencedIds(aVar2.f4048d.f4061e0);
                    }
                }
                barrier2.setType(aVar2.f4048d.f4055b0);
                barrier2.setMargin(aVar2.f4048d.f4057c0);
                ConstraintLayout.LayoutParams generateDefaultLayoutParams = constraintLayout.generateDefaultLayoutParams();
                barrier2.u();
                aVar2.d(generateDefaultLayoutParams);
                constraintLayout.addView(barrier2, generateDefaultLayoutParams);
            }
            if (aVar2.f4048d.f4052a) {
                View guideline = new Guideline(constraintLayout.getContext());
                guideline.setId(num.intValue());
                ConstraintLayout.LayoutParams generateDefaultLayoutParams2 = constraintLayout.generateDefaultLayoutParams();
                aVar2.d(generateDefaultLayoutParams2);
                constraintLayout.addView(guideline, generateDefaultLayoutParams2);
            }
        }
    }

    public void g(int i8, ConstraintLayout.LayoutParams layoutParams) {
        if (this.f4044d.containsKey(Integer.valueOf(i8))) {
            this.f4044d.get(Integer.valueOf(i8)).d(layoutParams);
        }
    }

    public void h(int i8, int i9) {
        if (this.f4044d.containsKey(Integer.valueOf(i8))) {
            a aVar = this.f4044d.get(Integer.valueOf(i8));
            switch (i9) {
                case 1:
                    C0027b c0027b = aVar.f4048d;
                    c0027b.f4068i = -1;
                    c0027b.f4066h = -1;
                    c0027b.D = -1;
                    c0027b.J = -1;
                    return;
                case 2:
                    C0027b c0027b2 = aVar.f4048d;
                    c0027b2.f4072k = -1;
                    c0027b2.f4070j = -1;
                    c0027b2.E = -1;
                    c0027b2.L = -1;
                    return;
                case 3:
                    C0027b c0027b3 = aVar.f4048d;
                    c0027b3.f4074m = -1;
                    c0027b3.f4073l = -1;
                    c0027b3.F = -1;
                    c0027b3.K = -1;
                    return;
                case 4:
                    C0027b c0027b4 = aVar.f4048d;
                    c0027b4.f4075n = -1;
                    c0027b4.f4076o = -1;
                    c0027b4.G = -1;
                    c0027b4.M = -1;
                    return;
                case 5:
                    aVar.f4048d.f4077p = -1;
                    return;
                case 6:
                    C0027b c0027b5 = aVar.f4048d;
                    c0027b5.q = -1;
                    c0027b5.f4078r = -1;
                    c0027b5.I = -1;
                    c0027b5.O = -1;
                    return;
                case 7:
                    C0027b c0027b6 = aVar.f4048d;
                    c0027b6.f4079s = -1;
                    c0027b6.f4080t = -1;
                    c0027b6.H = -1;
                    c0027b6.N = -1;
                    return;
                default:
                    throw new IllegalArgumentException("unknown constraint");
            }
        }
    }

    public void i(Context context, int i8) {
        j((ConstraintLayout) LayoutInflater.from(context).inflate(i8, (ViewGroup) null));
    }

    public void j(ConstraintLayout constraintLayout) {
        int childCount = constraintLayout.getChildCount();
        this.f4044d.clear();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = constraintLayout.getChildAt(i8);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) childAt.getLayoutParams();
            int id = childAt.getId();
            if (this.f4043c && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.f4044d.containsKey(Integer.valueOf(id))) {
                this.f4044d.put(Integer.valueOf(id), new a());
            }
            a aVar = this.f4044d.get(Integer.valueOf(id));
            aVar.f4050f = ConstraintAttribute.b(this.f4042b, childAt);
            aVar.f(id, layoutParams);
            aVar.f4046b.f4096b = childAt.getVisibility();
            int i9 = Build.VERSION.SDK_INT;
            if (i9 >= 17) {
                aVar.f4046b.f4098d = childAt.getAlpha();
                aVar.f4049e.f4102b = childAt.getRotation();
                aVar.f4049e.f4103c = childAt.getRotationX();
                aVar.f4049e.f4104d = childAt.getRotationY();
                aVar.f4049e.f4105e = childAt.getScaleX();
                aVar.f4049e.f4106f = childAt.getScaleY();
                float pivotX = childAt.getPivotX();
                float pivotY = childAt.getPivotY();
                if (pivotX != 0.0d || pivotY != 0.0d) {
                    e eVar = aVar.f4049e;
                    eVar.f4107g = pivotX;
                    eVar.f4108h = pivotY;
                }
                aVar.f4049e.f4109i = childAt.getTranslationX();
                aVar.f4049e.f4110j = childAt.getTranslationY();
                if (i9 >= 21) {
                    aVar.f4049e.f4111k = childAt.getTranslationZ();
                    e eVar2 = aVar.f4049e;
                    if (eVar2.f4112l) {
                        eVar2.f4113m = childAt.getElevation();
                    }
                }
            }
            if (childAt instanceof Barrier) {
                Barrier barrier = (Barrier) childAt;
                aVar.f4048d.f4071j0 = barrier.v();
                aVar.f4048d.f4061e0 = barrier.getReferencedIds();
                aVar.f4048d.f4055b0 = barrier.getType();
                aVar.f4048d.f4057c0 = barrier.getMargin();
            }
        }
    }

    public void k(Constraints constraints) {
        int childCount = constraints.getChildCount();
        this.f4044d.clear();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = constraints.getChildAt(i8);
            Constraints.LayoutParams layoutParams = (Constraints.LayoutParams) childAt.getLayoutParams();
            int id = childAt.getId();
            if (this.f4043c && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.f4044d.containsKey(Integer.valueOf(id))) {
                this.f4044d.put(Integer.valueOf(id), new a());
            }
            a aVar = this.f4044d.get(Integer.valueOf(id));
            if (childAt instanceof ConstraintHelper) {
                aVar.h((ConstraintHelper) childAt, id, layoutParams);
            }
            aVar.g(id, layoutParams);
        }
    }

    public void l(int i8, int i9, int i10, float f5) {
        C0027b c0027b = o(i8).f4048d;
        c0027b.f4084x = i9;
        c0027b.f4085y = i10;
        c0027b.f4086z = f5;
    }

    public a p(int i8) {
        if (this.f4044d.containsKey(Integer.valueOf(i8))) {
            return this.f4044d.get(Integer.valueOf(i8));
        }
        return null;
    }

    public int q(int i8) {
        return o(i8).f4048d.f4058d;
    }

    public int[] r() {
        Integer[] numArr = (Integer[]) this.f4044d.keySet().toArray(new Integer[0]);
        int length = numArr.length;
        int[] iArr = new int[length];
        for (int i8 = 0; i8 < length; i8++) {
            iArr[i8] = numArr[i8].intValue();
        }
        return iArr;
    }

    public a s(int i8) {
        return o(i8);
    }

    public int t(int i8) {
        return o(i8).f4046b.f4096b;
    }

    public int u(int i8) {
        return o(i8).f4046b.f4097c;
    }

    public int v(int i8) {
        return o(i8).f4048d.f4056c;
    }

    public void w(Context context, int i8) {
        XmlResourceParser xml = context.getResources().getXml(i8);
        try {
            for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.next()) {
                if (eventType == 0) {
                    xml.getName();
                    continue;
                } else if (eventType != 2) {
                    continue;
                } else {
                    String name = xml.getName();
                    a n8 = n(context, Xml.asAttributeSet(xml));
                    if (name.equalsIgnoreCase("Guideline")) {
                        n8.f4048d.f4052a = true;
                    }
                    this.f4044d.put(Integer.valueOf(n8.f4045a), n8);
                    continue;
                }
            }
        } catch (IOException e8) {
            e8.printStackTrace();
        } catch (XmlPullParserException e9) {
            e9.printStackTrace();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:98:0x0179, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void x(android.content.Context r10, org.xmlpull.v1.XmlPullParser r11) {
        /*
            Method dump skipped, instructions count: 448
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.b.x(android.content.Context, org.xmlpull.v1.XmlPullParser):void");
    }
}
