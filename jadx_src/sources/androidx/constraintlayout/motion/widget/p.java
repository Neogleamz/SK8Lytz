package androidx.constraintlayout.motion.widget;

import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.b;
import java.util.LinkedHashMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class p implements Comparable<p> {

    /* renamed from: t  reason: collision with root package name */
    static String[] f3412t = {"position", "x", "y", "width", "height", "pathRotate"};

    /* renamed from: a  reason: collision with root package name */
    l0.c f3413a;

    /* renamed from: b  reason: collision with root package name */
    int f3414b;

    /* renamed from: c  reason: collision with root package name */
    float f3415c;

    /* renamed from: d  reason: collision with root package name */
    float f3416d;

    /* renamed from: e  reason: collision with root package name */
    float f3417e;

    /* renamed from: f  reason: collision with root package name */
    float f3418f;

    /* renamed from: g  reason: collision with root package name */
    float f3419g;

    /* renamed from: h  reason: collision with root package name */
    float f3420h;

    /* renamed from: j  reason: collision with root package name */
    float f3421j;

    /* renamed from: k  reason: collision with root package name */
    float f3422k;

    /* renamed from: l  reason: collision with root package name */
    int f3423l;

    /* renamed from: m  reason: collision with root package name */
    LinkedHashMap<String, ConstraintAttribute> f3424m;

    /* renamed from: n  reason: collision with root package name */
    int f3425n;

    /* renamed from: p  reason: collision with root package name */
    double[] f3426p;
    double[] q;

    public p() {
        this.f3414b = 0;
        this.f3421j = Float.NaN;
        this.f3422k = Float.NaN;
        this.f3423l = c.f3247f;
        this.f3424m = new LinkedHashMap<>();
        this.f3425n = 0;
        this.f3426p = new double[18];
        this.q = new double[18];
    }

    public p(int i8, int i9, i iVar, p pVar, p pVar2) {
        this.f3414b = 0;
        this.f3421j = Float.NaN;
        this.f3422k = Float.NaN;
        this.f3423l = c.f3247f;
        this.f3424m = new LinkedHashMap<>();
        this.f3425n = 0;
        this.f3426p = new double[18];
        this.q = new double[18];
        int i10 = iVar.q;
        if (i10 == 1) {
            u(iVar, pVar, pVar2);
        } else if (i10 != 2) {
            t(iVar, pVar, pVar2);
        } else {
            v(i8, i9, iVar, pVar, pVar2);
        }
    }

    private boolean h(float f5, float f8) {
        return (Float.isNaN(f5) || Float.isNaN(f8)) ? Float.isNaN(f5) != Float.isNaN(f8) : Math.abs(f5 - f8) > 1.0E-6f;
    }

    public void c(b.a aVar) {
        this.f3413a = l0.c.c(aVar.f4047c.f4090c);
        b.c cVar = aVar.f4047c;
        this.f3423l = cVar.f4091d;
        this.f3421j = cVar.f4094g;
        this.f3414b = cVar.f4092e;
        this.f3422k = aVar.f4046b.f4099e;
        for (String str : aVar.f4050f.keySet()) {
            ConstraintAttribute constraintAttribute = aVar.f4050f.get(str);
            if (constraintAttribute.c() != ConstraintAttribute.AttributeType.STRING_TYPE) {
                this.f3424m.put(str, constraintAttribute);
            }
        }
    }

    @Override // java.lang.Comparable
    /* renamed from: f */
    public int compareTo(p pVar) {
        return Float.compare(this.f3416d, pVar.f3416d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i(p pVar, boolean[] zArr, String[] strArr, boolean z4) {
        zArr[0] = zArr[0] | h(this.f3416d, pVar.f3416d);
        zArr[1] = zArr[1] | h(this.f3417e, pVar.f3417e) | z4;
        zArr[2] = z4 | h(this.f3418f, pVar.f3418f) | zArr[2];
        zArr[3] = zArr[3] | h(this.f3419g, pVar.f3419g);
        zArr[4] = h(this.f3420h, pVar.f3420h) | zArr[4];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j(double[] dArr, int[] iArr) {
        float[] fArr = {this.f3416d, this.f3417e, this.f3418f, this.f3419g, this.f3420h, this.f3421j};
        int i8 = 0;
        for (int i9 = 0; i9 < iArr.length; i9++) {
            if (iArr[i9] < 6) {
                dArr[i8] = fArr[iArr[i9]];
                i8++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(int[] iArr, double[] dArr, float[] fArr, int i8) {
        float f5 = this.f3417e;
        float f8 = this.f3418f;
        float f9 = this.f3419g;
        float f10 = this.f3420h;
        for (int i9 = 0; i9 < iArr.length; i9++) {
            float f11 = (float) dArr[i9];
            int i10 = iArr[i9];
            if (i10 == 1) {
                f5 = f11;
            } else if (i10 == 2) {
                f8 = f11;
            } else if (i10 == 3) {
                f9 = f11;
            } else if (i10 == 4) {
                f10 = f11;
            }
        }
        fArr[i8] = f5 + (f9 / 2.0f) + 0.0f;
        fArr[i8 + 1] = f8 + (f10 / 2.0f) + 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int o(String str, double[] dArr, int i8) {
        ConstraintAttribute constraintAttribute = this.f3424m.get(str);
        if (constraintAttribute.f() == 1) {
            dArr[i8] = constraintAttribute.d();
            return 1;
        }
        int f5 = constraintAttribute.f();
        float[] fArr = new float[f5];
        constraintAttribute.e(fArr);
        int i9 = 0;
        while (i9 < f5) {
            dArr[i8] = fArr[i9];
            i9++;
            i8++;
        }
        return f5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int q(String str) {
        return this.f3424m.get(str).f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(int[] iArr, double[] dArr, float[] fArr, int i8) {
        float f5 = this.f3417e;
        float f8 = this.f3418f;
        float f9 = this.f3419g;
        float f10 = this.f3420h;
        for (int i9 = 0; i9 < iArr.length; i9++) {
            float f11 = (float) dArr[i9];
            int i10 = iArr[i9];
            if (i10 == 1) {
                f5 = f11;
            } else if (i10 == 2) {
                f8 = f11;
            } else if (i10 == 3) {
                f9 = f11;
            } else if (i10 == 4) {
                f10 = f11;
            }
        }
        float f12 = f9 + f5;
        float f13 = f10 + f8;
        Float.isNaN(Float.NaN);
        Float.isNaN(Float.NaN);
        int i11 = i8 + 1;
        fArr[i8] = f5 + 0.0f;
        int i12 = i11 + 1;
        fArr[i11] = f8 + 0.0f;
        int i13 = i12 + 1;
        fArr[i12] = f12 + 0.0f;
        int i14 = i13 + 1;
        fArr[i13] = f8 + 0.0f;
        int i15 = i14 + 1;
        fArr[i14] = f12 + 0.0f;
        int i16 = i15 + 1;
        fArr[i15] = f13 + 0.0f;
        fArr[i16] = f5 + 0.0f;
        fArr[i16 + 1] = f13 + 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean s(String str) {
        return this.f3424m.containsKey(str);
    }

    void t(i iVar, p pVar, p pVar2) {
        float f5 = iVar.f3248a / 100.0f;
        this.f3415c = f5;
        this.f3414b = iVar.f3320j;
        float f8 = Float.isNaN(iVar.f3321k) ? f5 : iVar.f3321k;
        float f9 = Float.isNaN(iVar.f3322l) ? f5 : iVar.f3322l;
        float f10 = pVar2.f3419g;
        float f11 = pVar.f3419g;
        float f12 = pVar2.f3420h;
        float f13 = pVar.f3420h;
        this.f3416d = this.f3415c;
        float f14 = pVar.f3417e;
        float f15 = pVar.f3418f;
        float f16 = (pVar2.f3417e + (f10 / 2.0f)) - ((f11 / 2.0f) + f14);
        float f17 = (pVar2.f3418f + (f12 / 2.0f)) - (f15 + (f13 / 2.0f));
        float f18 = (f10 - f11) * f8;
        float f19 = f18 / 2.0f;
        this.f3417e = (int) ((f14 + (f16 * f5)) - f19);
        float f20 = (f12 - f13) * f9;
        float f21 = f20 / 2.0f;
        this.f3418f = (int) ((f15 + (f17 * f5)) - f21);
        this.f3419g = (int) (f11 + f18);
        this.f3420h = (int) (f13 + f20);
        float f22 = Float.isNaN(iVar.f3323m) ? f5 : iVar.f3323m;
        float f23 = Float.isNaN(iVar.f3326p) ? 0.0f : iVar.f3326p;
        if (!Float.isNaN(iVar.f3324n)) {
            f5 = iVar.f3324n;
        }
        float f24 = Float.isNaN(iVar.f3325o) ? 0.0f : iVar.f3325o;
        this.f3425n = 2;
        this.f3417e = (int) (((pVar.f3417e + (f22 * f16)) + (f24 * f17)) - f19);
        this.f3418f = (int) (((pVar.f3418f + (f16 * f23)) + (f17 * f5)) - f21);
        this.f3413a = l0.c.c(iVar.f3318h);
        this.f3423l = iVar.f3319i;
    }

    void u(i iVar, p pVar, p pVar2) {
        float f5;
        float f8;
        float f9 = iVar.f3248a / 100.0f;
        this.f3415c = f9;
        this.f3414b = iVar.f3320j;
        float f10 = Float.isNaN(iVar.f3321k) ? f9 : iVar.f3321k;
        float f11 = Float.isNaN(iVar.f3322l) ? f9 : iVar.f3322l;
        float f12 = pVar2.f3419g - pVar.f3419g;
        float f13 = pVar2.f3420h - pVar.f3420h;
        this.f3416d = this.f3415c;
        if (!Float.isNaN(iVar.f3323m)) {
            f9 = iVar.f3323m;
        }
        float f14 = pVar.f3417e;
        float f15 = pVar.f3419g;
        float f16 = pVar.f3418f;
        float f17 = pVar.f3420h;
        float f18 = (pVar2.f3417e + (pVar2.f3419g / 2.0f)) - ((f15 / 2.0f) + f14);
        float f19 = (pVar2.f3418f + (pVar2.f3420h / 2.0f)) - ((f17 / 2.0f) + f16);
        float f20 = f18 * f9;
        float f21 = (f12 * f10) / 2.0f;
        this.f3417e = (int) ((f14 + f20) - f21);
        float f22 = f9 * f19;
        float f23 = (f13 * f11) / 2.0f;
        this.f3418f = (int) ((f16 + f22) - f23);
        this.f3419g = (int) (f15 + f5);
        this.f3420h = (int) (f17 + f8);
        float f24 = Float.isNaN(iVar.f3324n) ? 0.0f : iVar.f3324n;
        this.f3425n = 1;
        float f25 = (int) ((pVar.f3417e + f20) - f21);
        this.f3417e = f25;
        float f26 = (int) ((pVar.f3418f + f22) - f23);
        this.f3418f = f26;
        this.f3417e = f25 + ((-f19) * f24);
        this.f3418f = f26 + (f18 * f24);
        this.f3413a = l0.c.c(iVar.f3318h);
        this.f3423l = iVar.f3319i;
    }

    void v(int i8, int i9, i iVar, p pVar, p pVar2) {
        float f5 = iVar.f3248a / 100.0f;
        this.f3415c = f5;
        this.f3414b = iVar.f3320j;
        float f8 = Float.isNaN(iVar.f3321k) ? f5 : iVar.f3321k;
        float f9 = Float.isNaN(iVar.f3322l) ? f5 : iVar.f3322l;
        float f10 = pVar2.f3419g;
        float f11 = pVar.f3419g;
        float f12 = pVar2.f3420h;
        float f13 = pVar.f3420h;
        this.f3416d = this.f3415c;
        float f14 = pVar.f3417e;
        float f15 = pVar.f3418f;
        float f16 = pVar2.f3417e + (f10 / 2.0f);
        float f17 = pVar2.f3418f + (f12 / 2.0f);
        float f18 = (f10 - f11) * f8;
        this.f3417e = (int) ((f14 + ((f16 - ((f11 / 2.0f) + f14)) * f5)) - (f18 / 2.0f));
        float f19 = (f12 - f13) * f9;
        this.f3418f = (int) ((f15 + ((f17 - (f15 + (f13 / 2.0f))) * f5)) - (f19 / 2.0f));
        this.f3419g = (int) (f11 + f18);
        this.f3420h = (int) (f13 + f19);
        this.f3425n = 3;
        if (!Float.isNaN(iVar.f3323m)) {
            this.f3417e = (int) (iVar.f3323m * ((int) (i8 - this.f3419g)));
        }
        if (!Float.isNaN(iVar.f3324n)) {
            this.f3418f = (int) (iVar.f3324n * ((int) (i9 - this.f3420h)));
        }
        this.f3413a = l0.c.c(iVar.f3318h);
        this.f3423l = iVar.f3319i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w(float f5, float f8, float f9, float f10) {
        this.f3417e = f5;
        this.f3418f = f8;
        this.f3419g = f9;
        this.f3420h = f10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(float f5, float f8, float[] fArr, int[] iArr, double[] dArr, double[] dArr2) {
        float f9 = 0.0f;
        float f10 = 0.0f;
        float f11 = 0.0f;
        float f12 = 0.0f;
        for (int i8 = 0; i8 < iArr.length; i8++) {
            float f13 = (float) dArr[i8];
            double d8 = dArr2[i8];
            int i9 = iArr[i8];
            if (i9 == 1) {
                f9 = f13;
            } else if (i9 == 2) {
                f11 = f13;
            } else if (i9 == 3) {
                f10 = f13;
            } else if (i9 == 4) {
                f12 = f13;
            }
        }
        float f14 = f9 - ((0.0f * f10) / 2.0f);
        float f15 = f11 - ((0.0f * f12) / 2.0f);
        fArr[0] = (f14 * (1.0f - f5)) + (((f10 * 1.0f) + f14) * f5) + 0.0f;
        fArr[1] = (f15 * (1.0f - f8)) + (((f12 * 1.0f) + f15) * f8) + 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00bb, code lost:
        if (java.lang.Float.isNaN(Float.NaN) == false) goto L50;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void y(android.view.View r22, int[] r23, double[] r24, double[] r25, double[] r26) {
        /*
            Method dump skipped, instructions count: 276
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.p.y(android.view.View, int[], double[], double[], double[]):void");
    }
}
