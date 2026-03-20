package androidx.constraintlayout.motion.widget;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.widget.g;
import androidx.constraintlayout.motion.widget.r;
import androidx.constraintlayout.motion.widget.s;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.b;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class n {
    private l[] A;

    /* renamed from: a  reason: collision with root package name */
    View f3387a;

    /* renamed from: b  reason: collision with root package name */
    int f3388b;

    /* renamed from: c  reason: collision with root package name */
    String f3389c;

    /* renamed from: i  reason: collision with root package name */
    private l0.b[] f3395i;

    /* renamed from: j  reason: collision with root package name */
    private l0.b f3396j;

    /* renamed from: n  reason: collision with root package name */
    private int[] f3400n;

    /* renamed from: o  reason: collision with root package name */
    private double[] f3401o;

    /* renamed from: p  reason: collision with root package name */
    private double[] f3402p;
    private String[] q;

    /* renamed from: r  reason: collision with root package name */
    private int[] f3403r;

    /* renamed from: x  reason: collision with root package name */
    private HashMap<String, s> f3409x;

    /* renamed from: y  reason: collision with root package name */
    private HashMap<String, r> f3410y;

    /* renamed from: z  reason: collision with root package name */
    private HashMap<String, g> f3411z;

    /* renamed from: d  reason: collision with root package name */
    private int f3390d = -1;

    /* renamed from: e  reason: collision with root package name */
    private p f3391e = new p();

    /* renamed from: f  reason: collision with root package name */
    private p f3392f = new p();

    /* renamed from: g  reason: collision with root package name */
    private m f3393g = new m();

    /* renamed from: h  reason: collision with root package name */
    private m f3394h = new m();

    /* renamed from: k  reason: collision with root package name */
    float f3397k = Float.NaN;

    /* renamed from: l  reason: collision with root package name */
    float f3398l = 0.0f;

    /* renamed from: m  reason: collision with root package name */
    float f3399m = 1.0f;

    /* renamed from: s  reason: collision with root package name */
    private int f3404s = 4;

    /* renamed from: t  reason: collision with root package name */
    private float[] f3405t = new float[4];

    /* renamed from: u  reason: collision with root package name */
    private ArrayList<p> f3406u = new ArrayList<>();

    /* renamed from: v  reason: collision with root package name */
    private float[] f3407v = new float[1];

    /* renamed from: w  reason: collision with root package name */
    private ArrayList<c> f3408w = new ArrayList<>();
    private int B = c.f3247f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(View view) {
        u(view);
    }

    private float f(float f5, float[] fArr) {
        float f8 = 0.0f;
        if (fArr != null) {
            fArr[0] = 1.0f;
        } else {
            float f9 = this.f3399m;
            if (f9 != 1.0d) {
                float f10 = this.f3398l;
                if (f5 < f10) {
                    f5 = 0.0f;
                }
                if (f5 > f10 && f5 < 1.0d) {
                    f5 = (f5 - f10) * f9;
                }
            }
        }
        l0.c cVar = this.f3391e.f3413a;
        float f11 = Float.NaN;
        Iterator<p> it = this.f3406u.iterator();
        while (it.hasNext()) {
            p next = it.next();
            l0.c cVar2 = next.f3413a;
            if (cVar2 != null) {
                float f12 = next.f3415c;
                if (f12 < f5) {
                    cVar = cVar2;
                    f8 = f12;
                } else if (Float.isNaN(f11)) {
                    f11 = next.f3415c;
                }
            }
        }
        if (cVar != null) {
            float f13 = (Float.isNaN(f11) ? 1.0f : f11) - f8;
            double d8 = (f5 - f8) / f13;
            f5 = (((float) cVar.a(d8)) * f13) + f8;
            if (fArr != null) {
                fArr[0] = (float) cVar.b(d8);
            }
        }
        return f5;
    }

    private float m() {
        float f5;
        float[] fArr = new float[2];
        float f8 = 1.0f / 99;
        double d8 = 0.0d;
        double d9 = 0.0d;
        int i8 = 0;
        float f9 = 0.0f;
        while (i8 < 100) {
            float f10 = i8 * f8;
            double d10 = f10;
            l0.c cVar = this.f3391e.f3413a;
            float f11 = Float.NaN;
            Iterator<p> it = this.f3406u.iterator();
            float f12 = 0.0f;
            while (it.hasNext()) {
                p next = it.next();
                l0.c cVar2 = next.f3413a;
                float f13 = f8;
                if (cVar2 != null) {
                    float f14 = next.f3415c;
                    if (f14 < f10) {
                        f12 = f14;
                        cVar = cVar2;
                    } else if (Float.isNaN(f11)) {
                        f11 = next.f3415c;
                    }
                }
                f8 = f13;
            }
            float f15 = f8;
            if (cVar != null) {
                if (Float.isNaN(f11)) {
                    f11 = 1.0f;
                }
                d10 = (((float) cVar.a((f10 - f12) / f5)) * (f11 - f12)) + f12;
            }
            this.f3395i[0].d(d10, this.f3401o);
            this.f3391e.k(this.f3400n, this.f3401o, fArr, 0);
            if (i8 > 0) {
                f9 = (float) (f9 + Math.hypot(d9 - fArr[1], d8 - fArr[0]));
            }
            d8 = fArr[0];
            d9 = fArr[1];
            i8++;
            f8 = f15;
        }
        return f9;
    }

    private void n(p pVar) {
        int binarySearch = Collections.binarySearch(this.f3406u, pVar);
        if (binarySearch == 0) {
            Log.e("MotionController", " KeyPath positon \"" + pVar.f3416d + "\" outside of range");
        }
        this.f3406u.add((-binarySearch) - 1, pVar);
    }

    private void p(p pVar) {
        pVar.w((int) this.f3387a.getX(), (int) this.f3387a.getY(), this.f3387a.getWidth(), this.f3387a.getHeight());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(c cVar) {
        this.f3408w.add(cVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(ArrayList<c> arrayList) {
        this.f3408w.addAll(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int c(float[] fArr, int[] iArr) {
        if (fArr != null) {
            double[] h8 = this.f3395i[0].h();
            if (iArr != null) {
                Iterator<p> it = this.f3406u.iterator();
                int i8 = 0;
                while (it.hasNext()) {
                    iArr[i8] = it.next().f3425n;
                    i8++;
                }
            }
            int i9 = 0;
            for (double d8 : h8) {
                this.f3395i[0].d(d8, this.f3401o);
                this.f3391e.k(this.f3400n, this.f3401o, fArr, i9);
                i9 += 2;
            }
            return i9 / 2;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(float[] fArr, int i8) {
        float f5;
        int i9 = i8;
        float f8 = 1.0f;
        float f9 = 1.0f / (i9 - 1);
        HashMap<String, r> hashMap = this.f3410y;
        r rVar = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, r> hashMap2 = this.f3410y;
        r rVar2 = hashMap2 == null ? null : hashMap2.get("translationY");
        HashMap<String, g> hashMap3 = this.f3411z;
        g gVar = hashMap3 == null ? null : hashMap3.get("translationX");
        HashMap<String, g> hashMap4 = this.f3411z;
        g gVar2 = hashMap4 != null ? hashMap4.get("translationY") : null;
        int i10 = 0;
        while (i10 < i9) {
            float f10 = i10 * f9;
            float f11 = this.f3399m;
            if (f11 != f8) {
                float f12 = this.f3398l;
                if (f10 < f12) {
                    f10 = 0.0f;
                }
                if (f10 > f12 && f10 < 1.0d) {
                    f10 = (f10 - f12) * f11;
                }
            }
            double d8 = f10;
            l0.c cVar = this.f3391e.f3413a;
            float f13 = Float.NaN;
            Iterator<p> it = this.f3406u.iterator();
            float f14 = 0.0f;
            while (it.hasNext()) {
                p next = it.next();
                l0.c cVar2 = next.f3413a;
                if (cVar2 != null) {
                    float f15 = next.f3415c;
                    if (f15 < f10) {
                        f14 = f15;
                        cVar = cVar2;
                    } else if (Float.isNaN(f13)) {
                        f13 = next.f3415c;
                    }
                }
            }
            if (cVar != null) {
                if (Float.isNaN(f13)) {
                    f13 = 1.0f;
                }
                d8 = (((float) cVar.a((f10 - f14) / f5)) * (f13 - f14)) + f14;
            }
            this.f3395i[0].d(d8, this.f3401o);
            l0.b bVar = this.f3396j;
            if (bVar != null) {
                double[] dArr = this.f3401o;
                if (dArr.length > 0) {
                    bVar.d(d8, dArr);
                }
            }
            int i11 = i10 * 2;
            this.f3391e.k(this.f3400n, this.f3401o, fArr, i11);
            if (gVar != null) {
                fArr[i11] = fArr[i11] + gVar.a(f10);
            } else if (rVar != null) {
                fArr[i11] = fArr[i11] + rVar.a(f10);
            }
            if (gVar2 != null) {
                int i12 = i11 + 1;
                fArr[i12] = fArr[i12] + gVar2.a(f10);
            } else if (rVar2 != null) {
                int i13 = i11 + 1;
                fArr[i13] = fArr[i13] + rVar2.a(f10);
            }
            i10++;
            i9 = i8;
            f8 = 1.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(float f5, float[] fArr, int i8) {
        this.f3395i[0].d(f(f5, null), this.f3401o);
        this.f3391e.r(this.f3400n, this.f3401o, fArr, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(float f5, float f8, float f9, float[] fArr) {
        double[] dArr;
        float f10 = f(f5, this.f3407v);
        l0.b[] bVarArr = this.f3395i;
        int i8 = 0;
        if (bVarArr == null) {
            p pVar = this.f3392f;
            float f11 = pVar.f3417e;
            p pVar2 = this.f3391e;
            float f12 = f11 - pVar2.f3417e;
            float f13 = pVar.f3418f - pVar2.f3418f;
            float f14 = (pVar.f3419g - pVar2.f3419g) + f12;
            float f15 = (pVar.f3420h - pVar2.f3420h) + f13;
            fArr[0] = (f12 * (1.0f - f8)) + (f14 * f8);
            fArr[1] = (f13 * (1.0f - f9)) + (f15 * f9);
            return;
        }
        double d8 = f10;
        bVarArr[0].g(d8, this.f3402p);
        this.f3395i[0].d(d8, this.f3401o);
        float f16 = this.f3407v[0];
        while (true) {
            dArr = this.f3402p;
            if (i8 >= dArr.length) {
                break;
            }
            dArr[i8] = dArr[i8] * f16;
            i8++;
        }
        l0.b bVar = this.f3396j;
        if (bVar == null) {
            this.f3391e.x(f8, f9, fArr, this.f3400n, dArr, this.f3401o);
            return;
        }
        double[] dArr2 = this.f3401o;
        if (dArr2.length > 0) {
            bVar.d(d8, dArr2);
            this.f3396j.g(d8, this.f3402p);
            this.f3391e.x(f8, f9, fArr, this.f3400n, this.f3402p, this.f3401o);
        }
    }

    public int h() {
        int i8 = this.f3391e.f3414b;
        Iterator<p> it = this.f3406u.iterator();
        while (it.hasNext()) {
            i8 = Math.max(i8, it.next().f3414b);
        }
        return Math.max(i8, this.f3392f.f3414b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float i() {
        return this.f3392f.f3417e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float j() {
        return this.f3392f.f3418f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p k(int i8) {
        return this.f3406u.get(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(float f5, int i8, int i9, float f8, float f9, float[] fArr) {
        float f10 = f(f5, this.f3407v);
        HashMap<String, r> hashMap = this.f3410y;
        r rVar = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, r> hashMap2 = this.f3410y;
        r rVar2 = hashMap2 == null ? null : hashMap2.get("translationY");
        HashMap<String, r> hashMap3 = this.f3410y;
        r rVar3 = hashMap3 == null ? null : hashMap3.get("rotation");
        HashMap<String, r> hashMap4 = this.f3410y;
        r rVar4 = hashMap4 == null ? null : hashMap4.get("scaleX");
        HashMap<String, r> hashMap5 = this.f3410y;
        r rVar5 = hashMap5 == null ? null : hashMap5.get("scaleY");
        HashMap<String, g> hashMap6 = this.f3411z;
        g gVar = hashMap6 == null ? null : hashMap6.get("translationX");
        HashMap<String, g> hashMap7 = this.f3411z;
        g gVar2 = hashMap7 == null ? null : hashMap7.get("translationY");
        HashMap<String, g> hashMap8 = this.f3411z;
        g gVar3 = hashMap8 == null ? null : hashMap8.get("rotation");
        HashMap<String, g> hashMap9 = this.f3411z;
        g gVar4 = hashMap9 == null ? null : hashMap9.get("scaleX");
        HashMap<String, g> hashMap10 = this.f3411z;
        g gVar5 = hashMap10 != null ? hashMap10.get("scaleY") : null;
        l0.h hVar = new l0.h();
        hVar.b();
        hVar.d(rVar3, f10);
        hVar.h(rVar, rVar2, f10);
        hVar.f(rVar4, rVar5, f10);
        hVar.c(gVar3, f10);
        hVar.g(gVar, gVar2, f10);
        hVar.e(gVar4, gVar5, f10);
        l0.b bVar = this.f3396j;
        if (bVar != null) {
            double[] dArr = this.f3401o;
            if (dArr.length > 0) {
                double d8 = f10;
                bVar.d(d8, dArr);
                this.f3396j.g(d8, this.f3402p);
                this.f3391e.x(f8, f9, fArr, this.f3400n, this.f3402p, this.f3401o);
            }
            hVar.a(f8, f9, i8, i9, fArr);
            return;
        }
        int i10 = 0;
        if (this.f3395i == null) {
            p pVar = this.f3392f;
            float f11 = pVar.f3417e;
            p pVar2 = this.f3391e;
            float f12 = f11 - pVar2.f3417e;
            float f13 = pVar.f3418f - pVar2.f3418f;
            g gVar6 = gVar4;
            float f14 = (pVar.f3420h - pVar2.f3420h) + f13;
            fArr[0] = (f12 * (1.0f - f8)) + (((pVar.f3419g - pVar2.f3419g) + f12) * f8);
            fArr[1] = (f13 * (1.0f - f9)) + (f14 * f9);
            hVar.b();
            hVar.d(rVar3, f10);
            hVar.h(rVar, rVar2, f10);
            hVar.f(rVar4, rVar5, f10);
            hVar.c(gVar3, f10);
            hVar.g(gVar, gVar2, f10);
            hVar.e(gVar6, gVar5, f10);
            hVar.a(f8, f9, i8, i9, fArr);
            return;
        }
        double f15 = f(f10, this.f3407v);
        this.f3395i[0].g(f15, this.f3402p);
        this.f3395i[0].d(f15, this.f3401o);
        float f16 = this.f3407v[0];
        while (true) {
            double[] dArr2 = this.f3402p;
            if (i10 >= dArr2.length) {
                this.f3391e.x(f8, f9, fArr, this.f3400n, dArr2, this.f3401o);
                hVar.a(f8, f9, i8, i9, fArr);
                return;
            }
            dArr2[i10] = dArr2[i10] * f16;
            i10++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean o(View view, float f5, long j8, e eVar) {
        s.d dVar;
        boolean z4;
        double d8;
        float f8 = f(f5, null);
        HashMap<String, r> hashMap = this.f3410y;
        if (hashMap != null) {
            for (r rVar : hashMap.values()) {
                rVar.f(view, f8);
            }
        }
        HashMap<String, s> hashMap2 = this.f3409x;
        if (hashMap2 != null) {
            dVar = null;
            boolean z8 = false;
            for (s sVar : hashMap2.values()) {
                if (sVar instanceof s.d) {
                    dVar = (s.d) sVar;
                } else {
                    z8 |= sVar.f(view, f8, j8, eVar);
                }
            }
            z4 = z8;
        } else {
            dVar = null;
            z4 = false;
        }
        l0.b[] bVarArr = this.f3395i;
        if (bVarArr != null) {
            double d9 = f8;
            bVarArr[0].d(d9, this.f3401o);
            this.f3395i[0].g(d9, this.f3402p);
            l0.b bVar = this.f3396j;
            if (bVar != null) {
                double[] dArr = this.f3401o;
                if (dArr.length > 0) {
                    bVar.d(d9, dArr);
                    this.f3396j.g(d9, this.f3402p);
                }
            }
            this.f3391e.y(view, this.f3400n, this.f3401o, this.f3402p, null);
            HashMap<String, r> hashMap3 = this.f3410y;
            if (hashMap3 != null) {
                for (r rVar2 : hashMap3.values()) {
                    if (rVar2 instanceof r.d) {
                        double[] dArr2 = this.f3402p;
                        ((r.d) rVar2).i(view, f8, dArr2[0], dArr2[1]);
                    }
                }
            }
            if (dVar != null) {
                double[] dArr3 = this.f3402p;
                d8 = d9;
                z4 = dVar.j(view, eVar, f8, j8, dArr3[0], dArr3[1]) | z4;
            } else {
                d8 = d9;
            }
            int i8 = 1;
            while (true) {
                l0.b[] bVarArr2 = this.f3395i;
                if (i8 >= bVarArr2.length) {
                    break;
                }
                bVarArr2[i8].e(d8, this.f3405t);
                this.f3391e.f3424m.get(this.q[i8 - 1]).i(view, this.f3405t);
                i8++;
            }
            m mVar = this.f3393g;
            if (mVar.f3369b == 0) {
                if (f8 > 0.0f) {
                    if (f8 >= 1.0f) {
                        mVar = this.f3394h;
                    } else if (this.f3394h.f3370c != mVar.f3370c) {
                        view.setVisibility(0);
                    }
                }
                view.setVisibility(mVar.f3370c);
            }
            if (this.A != null) {
                int i9 = 0;
                while (true) {
                    l[] lVarArr = this.A;
                    if (i9 >= lVarArr.length) {
                        break;
                    }
                    lVarArr[i9].r(f8, view);
                    i9++;
                }
            }
        } else {
            p pVar = this.f3391e;
            float f9 = pVar.f3417e;
            p pVar2 = this.f3392f;
            float f10 = f9 + ((pVar2.f3417e - f9) * f8);
            float f11 = pVar.f3418f;
            float f12 = f11 + ((pVar2.f3418f - f11) * f8);
            float f13 = pVar.f3419g;
            float f14 = pVar2.f3419g;
            float f15 = pVar.f3420h;
            float f16 = pVar2.f3420h;
            float f17 = f10 + 0.5f;
            int i10 = (int) f17;
            float f18 = f12 + 0.5f;
            int i11 = (int) f18;
            int i12 = (int) (f17 + ((f14 - f13) * f8) + f13);
            int i13 = (int) (f18 + ((f16 - f15) * f8) + f15);
            int i14 = i12 - i10;
            int i15 = i13 - i11;
            if (f14 != f13 || f16 != f15) {
                view.measure(View.MeasureSpec.makeMeasureSpec(i14, 1073741824), View.MeasureSpec.makeMeasureSpec(i15, 1073741824));
            }
            view.layout(i10, i11, i12, i13);
        }
        HashMap<String, g> hashMap4 = this.f3411z;
        if (hashMap4 != null) {
            for (g gVar : hashMap4.values()) {
                if (gVar instanceof g.f) {
                    double[] dArr4 = this.f3402p;
                    ((g.f) gVar).j(view, f8, dArr4[0], dArr4[1]);
                } else {
                    gVar.f(view, f8);
                }
            }
        }
        return z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(ConstraintWidget constraintWidget, androidx.constraintlayout.widget.b bVar) {
        p pVar = this.f3392f;
        pVar.f3415c = 1.0f;
        pVar.f3416d = 1.0f;
        p(pVar);
        this.f3392f.w(constraintWidget.R(), constraintWidget.S(), constraintWidget.Q(), constraintWidget.w());
        this.f3392f.c(bVar.s(this.f3388b));
        this.f3394h.r(constraintWidget, bVar, this.f3388b);
    }

    public void r(int i8) {
        this.B = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(View view) {
        p pVar = this.f3391e;
        pVar.f3415c = 0.0f;
        pVar.f3416d = 0.0f;
        pVar.w(view.getX(), view.getY(), view.getWidth(), view.getHeight());
        this.f3393g.q(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t(ConstraintWidget constraintWidget, androidx.constraintlayout.widget.b bVar) {
        p pVar = this.f3391e;
        pVar.f3415c = 0.0f;
        pVar.f3416d = 0.0f;
        p(pVar);
        this.f3391e.w(constraintWidget.R(), constraintWidget.S(), constraintWidget.Q(), constraintWidget.w());
        b.a s8 = bVar.s(this.f3388b);
        this.f3391e.c(s8);
        this.f3397k = s8.f4047c.f4093f;
        this.f3393g.r(constraintWidget, bVar, this.f3388b);
    }

    public String toString() {
        return " start: x: " + this.f3391e.f3417e + " y: " + this.f3391e.f3418f + " end: x: " + this.f3392f.f3417e + " y: " + this.f3392f.f3418f;
    }

    public void u(View view) {
        this.f3387a = view;
        this.f3388b = view.getId();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            this.f3389c = ((ConstraintLayout.LayoutParams) layoutParams).a();
        }
    }

    public void v(int i8, int i9, float f5, long j8) {
        ArrayList arrayList;
        String[] strArr;
        s d8;
        ConstraintAttribute constraintAttribute;
        r d9;
        ConstraintAttribute constraintAttribute2;
        new HashSet();
        HashSet<String> hashSet = new HashSet<>();
        HashSet<String> hashSet2 = new HashSet<>();
        HashSet<String> hashSet3 = new HashSet<>();
        HashMap<String, Integer> hashMap = new HashMap<>();
        int i10 = this.B;
        if (i10 != c.f3247f) {
            this.f3391e.f3423l = i10;
        }
        this.f3393g.k(this.f3394h, hashSet2);
        ArrayList<c> arrayList2 = this.f3408w;
        if (arrayList2 != null) {
            Iterator<c> it = arrayList2.iterator();
            arrayList = null;
            while (it.hasNext()) {
                c next = it.next();
                if (next instanceof i) {
                    i iVar = (i) next;
                    n(new p(i8, i9, iVar, this.f3391e, this.f3392f));
                    int i11 = iVar.f3330g;
                    if (i11 != c.f3247f) {
                        this.f3390d = i11;
                    }
                } else if (next instanceof f) {
                    next.b(hashSet3);
                } else if (next instanceof k) {
                    next.b(hashSet);
                } else if (next instanceof l) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add((l) next);
                } else {
                    next.e(hashMap);
                    next.b(hashSet2);
                }
            }
        } else {
            arrayList = null;
        }
        int i12 = 0;
        if (arrayList != null) {
            this.A = (l[]) arrayList.toArray(new l[0]);
        }
        char c9 = 1;
        if (!hashSet2.isEmpty()) {
            this.f3410y = new HashMap<>();
            Iterator<String> it2 = hashSet2.iterator();
            while (it2.hasNext()) {
                String next2 = it2.next();
                if (next2.startsWith("CUSTOM,")) {
                    SparseArray sparseArray = new SparseArray();
                    String str = next2.split(",")[c9];
                    Iterator<c> it3 = this.f3408w.iterator();
                    while (it3.hasNext()) {
                        c next3 = it3.next();
                        HashMap<String, ConstraintAttribute> hashMap2 = next3.f3252e;
                        if (hashMap2 != null && (constraintAttribute2 = hashMap2.get(str)) != null) {
                            sparseArray.append(next3.f3248a, constraintAttribute2);
                        }
                    }
                    d9 = r.c(next2, sparseArray);
                } else {
                    d9 = r.d(next2);
                }
                if (d9 != null) {
                    d9.g(next2);
                    this.f3410y.put(next2, d9);
                }
                c9 = 1;
            }
            ArrayList<c> arrayList3 = this.f3408w;
            if (arrayList3 != null) {
                Iterator<c> it4 = arrayList3.iterator();
                while (it4.hasNext()) {
                    c next4 = it4.next();
                    if (next4 instanceof d) {
                        next4.a(this.f3410y);
                    }
                }
            }
            this.f3393g.c(this.f3410y, 0);
            this.f3394h.c(this.f3410y, 100);
            for (String str2 : this.f3410y.keySet()) {
                this.f3410y.get(str2).h(hashMap.containsKey(str2) ? hashMap.get(str2).intValue() : 0);
            }
        }
        if (!hashSet.isEmpty()) {
            if (this.f3409x == null) {
                this.f3409x = new HashMap<>();
            }
            Iterator<String> it5 = hashSet.iterator();
            while (it5.hasNext()) {
                String next5 = it5.next();
                if (!this.f3409x.containsKey(next5)) {
                    if (next5.startsWith("CUSTOM,")) {
                        SparseArray sparseArray2 = new SparseArray();
                        String str3 = next5.split(",")[1];
                        Iterator<c> it6 = this.f3408w.iterator();
                        while (it6.hasNext()) {
                            c next6 = it6.next();
                            HashMap<String, ConstraintAttribute> hashMap3 = next6.f3252e;
                            if (hashMap3 != null && (constraintAttribute = hashMap3.get(str3)) != null) {
                                sparseArray2.append(next6.f3248a, constraintAttribute);
                            }
                        }
                        d8 = s.c(next5, sparseArray2);
                    } else {
                        d8 = s.d(next5, j8);
                    }
                    if (d8 != null) {
                        d8.h(next5);
                        this.f3409x.put(next5, d8);
                    }
                }
            }
            ArrayList<c> arrayList4 = this.f3408w;
            if (arrayList4 != null) {
                Iterator<c> it7 = arrayList4.iterator();
                while (it7.hasNext()) {
                    c next7 = it7.next();
                    if (next7 instanceof k) {
                        ((k) next7).M(this.f3409x);
                    }
                }
            }
            for (String str4 : this.f3409x.keySet()) {
                this.f3409x.get(str4).i(hashMap.containsKey(str4) ? hashMap.get(str4).intValue() : 0);
            }
        }
        int i13 = 2;
        int size = this.f3406u.size() + 2;
        p[] pVarArr = new p[size];
        pVarArr[0] = this.f3391e;
        pVarArr[size - 1] = this.f3392f;
        if (this.f3406u.size() > 0 && this.f3390d == -1) {
            this.f3390d = 0;
        }
        Iterator<p> it8 = this.f3406u.iterator();
        int i14 = 1;
        while (it8.hasNext()) {
            pVarArr[i14] = it8.next();
            i14++;
        }
        HashSet hashSet4 = new HashSet();
        for (String str5 : this.f3392f.f3424m.keySet()) {
            if (this.f3391e.f3424m.containsKey(str5)) {
                if (!hashSet2.contains("CUSTOM," + str5)) {
                    hashSet4.add(str5);
                }
            }
        }
        String[] strArr2 = (String[]) hashSet4.toArray(new String[0]);
        this.q = strArr2;
        this.f3403r = new int[strArr2.length];
        int i15 = 0;
        while (true) {
            strArr = this.q;
            if (i15 >= strArr.length) {
                break;
            }
            String str6 = strArr[i15];
            this.f3403r[i15] = 0;
            int i16 = 0;
            while (true) {
                if (i16 >= size) {
                    break;
                } else if (pVarArr[i16].f3424m.containsKey(str6)) {
                    int[] iArr = this.f3403r;
                    iArr[i15] = iArr[i15] + pVarArr[i16].f3424m.get(str6).f();
                    break;
                } else {
                    i16++;
                }
            }
            i15++;
        }
        boolean z4 = pVarArr[0].f3423l != c.f3247f;
        int length = 18 + strArr.length;
        boolean[] zArr = new boolean[length];
        for (int i17 = 1; i17 < size; i17++) {
            pVarArr[i17].i(pVarArr[i17 - 1], zArr, this.q, z4);
        }
        int i18 = 0;
        for (int i19 = 1; i19 < length; i19++) {
            if (zArr[i19]) {
                i18++;
            }
        }
        int[] iArr2 = new int[i18];
        this.f3400n = iArr2;
        this.f3401o = new double[iArr2.length];
        this.f3402p = new double[iArr2.length];
        int i20 = 0;
        for (int i21 = 1; i21 < length; i21++) {
            if (zArr[i21]) {
                this.f3400n[i20] = i21;
                i20++;
            }
        }
        double[][] dArr = (double[][]) Array.newInstance(double.class, size, this.f3400n.length);
        double[] dArr2 = new double[size];
        for (int i22 = 0; i22 < size; i22++) {
            pVarArr[i22].j(dArr[i22], this.f3400n);
            dArr2[i22] = pVarArr[i22].f3415c;
        }
        int i23 = 0;
        while (true) {
            int[] iArr3 = this.f3400n;
            if (i23 >= iArr3.length) {
                break;
            }
            if (iArr3[i23] < p.f3412t.length) {
                String str7 = p.f3412t[this.f3400n[i23]] + " [";
                for (int i24 = 0; i24 < size; i24++) {
                    str7 = str7 + dArr[i24][i23];
                }
            }
            i23++;
        }
        this.f3395i = new l0.b[this.q.length + 1];
        int i25 = 0;
        while (true) {
            String[] strArr3 = this.q;
            if (i25 >= strArr3.length) {
                break;
            }
            String str8 = strArr3[i25];
            int i26 = i12;
            int i27 = i26;
            double[] dArr3 = null;
            double[][] dArr4 = null;
            while (i26 < size) {
                if (pVarArr[i26].s(str8)) {
                    if (dArr4 == null) {
                        dArr3 = new double[size];
                        int[] iArr4 = new int[i13];
                        iArr4[1] = pVarArr[i26].q(str8);
                        iArr4[i12] = size;
                        dArr4 = (double[][]) Array.newInstance(double.class, iArr4);
                    }
                    dArr3[i27] = pVarArr[i26].f3415c;
                    pVarArr[i26].o(str8, dArr4[i27], 0);
                    i27++;
                }
                i26++;
                i13 = 2;
                i12 = 0;
            }
            i25++;
            this.f3395i[i25] = l0.b.a(this.f3390d, Arrays.copyOf(dArr3, i27), (double[][]) Arrays.copyOf(dArr4, i27));
            i13 = 2;
            i12 = 0;
        }
        this.f3395i[0] = l0.b.a(this.f3390d, dArr2, dArr);
        if (pVarArr[0].f3423l != c.f3247f) {
            int[] iArr5 = new int[size];
            double[] dArr5 = new double[size];
            double[][] dArr6 = (double[][]) Array.newInstance(double.class, size, 2);
            for (int i28 = 0; i28 < size; i28++) {
                iArr5[i28] = pVarArr[i28].f3423l;
                dArr5[i28] = pVarArr[i28].f3415c;
                dArr6[i28][0] = pVarArr[i28].f3417e;
                dArr6[i28][1] = pVarArr[i28].f3418f;
            }
            this.f3396j = l0.b.b(iArr5, dArr5, dArr6);
        }
        float f8 = Float.NaN;
        this.f3411z = new HashMap<>();
        if (this.f3408w != null) {
            Iterator<String> it9 = hashSet3.iterator();
            while (it9.hasNext()) {
                String next8 = it9.next();
                g c10 = g.c(next8);
                if (c10 != null) {
                    if (c10.i() && Float.isNaN(f8)) {
                        f8 = m();
                    }
                    c10.g(next8);
                    this.f3411z.put(next8, c10);
                }
            }
            Iterator<c> it10 = this.f3408w.iterator();
            while (it10.hasNext()) {
                c next9 = it10.next();
                if (next9 instanceof f) {
                    ((f) next9).O(this.f3411z);
                }
            }
            for (g gVar : this.f3411z.values()) {
                gVar.h(f8);
            }
        }
    }
}
