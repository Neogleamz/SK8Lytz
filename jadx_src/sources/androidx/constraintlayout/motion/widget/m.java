package androidx.constraintlayout.motion.widget;

import android.os.Build;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.motion.widget.r;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.b;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m implements Comparable<m> {
    static String[] L = {"position", "x", "y", "width", "height", "pathRotate"};
    private float A;
    private float B;

    /* renamed from: c  reason: collision with root package name */
    int f3370c;

    /* renamed from: t  reason: collision with root package name */
    private l0.c f3382t;

    /* renamed from: x  reason: collision with root package name */
    private float f3384x;

    /* renamed from: y  reason: collision with root package name */
    private float f3385y;

    /* renamed from: z  reason: collision with root package name */
    private float f3386z;

    /* renamed from: a  reason: collision with root package name */
    private float f3368a = 1.0f;

    /* renamed from: b  reason: collision with root package name */
    int f3369b = 0;

    /* renamed from: d  reason: collision with root package name */
    private boolean f3371d = false;

    /* renamed from: e  reason: collision with root package name */
    private float f3372e = 0.0f;

    /* renamed from: f  reason: collision with root package name */
    private float f3373f = 0.0f;

    /* renamed from: g  reason: collision with root package name */
    private float f3374g = 0.0f;

    /* renamed from: h  reason: collision with root package name */
    public float f3375h = 0.0f;

    /* renamed from: j  reason: collision with root package name */
    private float f3376j = 1.0f;

    /* renamed from: k  reason: collision with root package name */
    private float f3377k = 1.0f;

    /* renamed from: l  reason: collision with root package name */
    private float f3378l = Float.NaN;

    /* renamed from: m  reason: collision with root package name */
    private float f3379m = Float.NaN;

    /* renamed from: n  reason: collision with root package name */
    private float f3380n = 0.0f;

    /* renamed from: p  reason: collision with root package name */
    private float f3381p = 0.0f;
    private float q = 0.0f;

    /* renamed from: w  reason: collision with root package name */
    private int f3383w = 0;
    private float C = Float.NaN;
    private float E = Float.NaN;
    LinkedHashMap<String, ConstraintAttribute> F = new LinkedHashMap<>();
    int G = 0;
    double[] H = new double[18];
    double[] K = new double[18];

    private boolean j(float f5, float f8) {
        return (Float.isNaN(f5) || Float.isNaN(f8)) ? Float.isNaN(f5) != Float.isNaN(f8) : Math.abs(f5 - f8) > 1.0E-6f;
    }

    public void c(HashMap<String, r> hashMap, int i8) {
        String str;
        for (String str2 : hashMap.keySet()) {
            r rVar = hashMap.get(str2);
            str2.hashCode();
            char c9 = 65535;
            switch (str2.hashCode()) {
                case -1249320806:
                    if (str2.equals("rotationX")) {
                        c9 = 0;
                        break;
                    }
                    break;
                case -1249320805:
                    if (str2.equals("rotationY")) {
                        c9 = 1;
                        break;
                    }
                    break;
                case -1225497657:
                    if (str2.equals("translationX")) {
                        c9 = 2;
                        break;
                    }
                    break;
                case -1225497656:
                    if (str2.equals("translationY")) {
                        c9 = 3;
                        break;
                    }
                    break;
                case -1225497655:
                    if (str2.equals("translationZ")) {
                        c9 = 4;
                        break;
                    }
                    break;
                case -1001078227:
                    if (str2.equals("progress")) {
                        c9 = 5;
                        break;
                    }
                    break;
                case -908189618:
                    if (str2.equals("scaleX")) {
                        c9 = 6;
                        break;
                    }
                    break;
                case -908189617:
                    if (str2.equals("scaleY")) {
                        c9 = 7;
                        break;
                    }
                    break;
                case -760884510:
                    if (str2.equals("transformPivotX")) {
                        c9 = '\b';
                        break;
                    }
                    break;
                case -760884509:
                    if (str2.equals("transformPivotY")) {
                        c9 = '\t';
                        break;
                    }
                    break;
                case -40300674:
                    if (str2.equals("rotation")) {
                        c9 = '\n';
                        break;
                    }
                    break;
                case -4379043:
                    if (str2.equals("elevation")) {
                        c9 = 11;
                        break;
                    }
                    break;
                case 37232917:
                    if (str2.equals("transitionPathRotate")) {
                        c9 = '\f';
                        break;
                    }
                    break;
                case 92909918:
                    if (str2.equals("alpha")) {
                        c9 = '\r';
                        break;
                    }
                    break;
            }
            float f5 = 1.0f;
            float f8 = 0.0f;
            switch (c9) {
                case 0:
                    if (!Float.isNaN(this.f3374g)) {
                        f8 = this.f3374g;
                    }
                    rVar.e(i8, f8);
                    break;
                case 1:
                    if (!Float.isNaN(this.f3375h)) {
                        f8 = this.f3375h;
                    }
                    rVar.e(i8, f8);
                    break;
                case 2:
                    if (!Float.isNaN(this.f3380n)) {
                        f8 = this.f3380n;
                    }
                    rVar.e(i8, f8);
                    break;
                case 3:
                    if (!Float.isNaN(this.f3381p)) {
                        f8 = this.f3381p;
                    }
                    rVar.e(i8, f8);
                    break;
                case 4:
                    if (!Float.isNaN(this.q)) {
                        f8 = this.q;
                    }
                    rVar.e(i8, f8);
                    break;
                case 5:
                    if (!Float.isNaN(this.E)) {
                        f8 = this.E;
                    }
                    rVar.e(i8, f8);
                    break;
                case 6:
                    if (!Float.isNaN(this.f3376j)) {
                        f5 = this.f3376j;
                    }
                    rVar.e(i8, f5);
                    break;
                case 7:
                    if (!Float.isNaN(this.f3377k)) {
                        f5 = this.f3377k;
                    }
                    rVar.e(i8, f5);
                    break;
                case '\b':
                    if (!Float.isNaN(this.f3378l)) {
                        f8 = this.f3378l;
                    }
                    rVar.e(i8, f8);
                    break;
                case '\t':
                    if (!Float.isNaN(this.f3379m)) {
                        f8 = this.f3379m;
                    }
                    rVar.e(i8, f8);
                    break;
                case '\n':
                    if (!Float.isNaN(this.f3373f)) {
                        f8 = this.f3373f;
                    }
                    rVar.e(i8, f8);
                    break;
                case 11:
                    if (!Float.isNaN(this.f3372e)) {
                        f8 = this.f3372e;
                    }
                    rVar.e(i8, f8);
                    break;
                case '\f':
                    if (!Float.isNaN(this.C)) {
                        f8 = this.C;
                    }
                    rVar.e(i8, f8);
                    break;
                case '\r':
                    if (!Float.isNaN(this.f3368a)) {
                        f5 = this.f3368a;
                    }
                    rVar.e(i8, f5);
                    break;
                default:
                    if (str2.startsWith("CUSTOM")) {
                        String str3 = str2.split(",")[1];
                        if (this.F.containsKey(str3)) {
                            ConstraintAttribute constraintAttribute = this.F.get(str3);
                            if (rVar instanceof r.b) {
                                ((r.b) rVar).i(i8, constraintAttribute);
                                break;
                            } else {
                                str = str2 + " splineSet not a CustomSet frame = " + i8 + ", value" + constraintAttribute.d() + rVar;
                            }
                        } else {
                            str = "UNKNOWN customName " + str3;
                        }
                    } else {
                        str = "UNKNOWN spline " + str2;
                    }
                    Log.e("MotionPaths", str);
                    break;
            }
        }
    }

    public void f(View view) {
        this.f3370c = view.getVisibility();
        this.f3368a = view.getVisibility() != 0 ? 0.0f : view.getAlpha();
        this.f3371d = false;
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 21) {
            this.f3372e = view.getElevation();
        }
        this.f3373f = view.getRotation();
        this.f3374g = view.getRotationX();
        this.f3375h = view.getRotationY();
        this.f3376j = view.getScaleX();
        this.f3377k = view.getScaleY();
        this.f3378l = view.getPivotX();
        this.f3379m = view.getPivotY();
        this.f3380n = view.getTranslationX();
        this.f3381p = view.getTranslationY();
        if (i8 >= 21) {
            this.q = view.getTranslationZ();
        }
    }

    public void h(b.a aVar) {
        b.d dVar = aVar.f4046b;
        int i8 = dVar.f4097c;
        this.f3369b = i8;
        int i9 = dVar.f4096b;
        this.f3370c = i9;
        this.f3368a = (i9 == 0 || i8 != 0) ? dVar.f4098d : 0.0f;
        b.e eVar = aVar.f4049e;
        this.f3371d = eVar.f4112l;
        this.f3372e = eVar.f4113m;
        this.f3373f = eVar.f4102b;
        this.f3374g = eVar.f4103c;
        this.f3375h = eVar.f4104d;
        this.f3376j = eVar.f4105e;
        this.f3377k = eVar.f4106f;
        this.f3378l = eVar.f4107g;
        this.f3379m = eVar.f4108h;
        this.f3380n = eVar.f4109i;
        this.f3381p = eVar.f4110j;
        this.q = eVar.f4111k;
        this.f3382t = l0.c.c(aVar.f4047c.f4090c);
        b.c cVar = aVar.f4047c;
        this.C = cVar.f4094g;
        this.f3383w = cVar.f4092e;
        this.E = aVar.f4046b.f4099e;
        for (String str : aVar.f4050f.keySet()) {
            ConstraintAttribute constraintAttribute = aVar.f4050f.get(str);
            if (constraintAttribute.c() != ConstraintAttribute.AttributeType.STRING_TYPE) {
                this.F.put(str, constraintAttribute);
            }
        }
    }

    @Override // java.lang.Comparable
    /* renamed from: i */
    public int compareTo(m mVar) {
        return Float.compare(this.f3384x, mVar.f3384x);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(m mVar, HashSet<String> hashSet) {
        if (j(this.f3368a, mVar.f3368a)) {
            hashSet.add("alpha");
        }
        if (j(this.f3372e, mVar.f3372e)) {
            hashSet.add("elevation");
        }
        int i8 = this.f3370c;
        int i9 = mVar.f3370c;
        if (i8 != i9 && this.f3369b == 0 && (i8 == 0 || i9 == 0)) {
            hashSet.add("alpha");
        }
        if (j(this.f3373f, mVar.f3373f)) {
            hashSet.add("rotation");
        }
        if (!Float.isNaN(this.C) || !Float.isNaN(mVar.C)) {
            hashSet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.E) || !Float.isNaN(mVar.E)) {
            hashSet.add("progress");
        }
        if (j(this.f3374g, mVar.f3374g)) {
            hashSet.add("rotationX");
        }
        if (j(this.f3375h, mVar.f3375h)) {
            hashSet.add("rotationY");
        }
        if (j(this.f3378l, mVar.f3378l)) {
            hashSet.add("transformPivotX");
        }
        if (j(this.f3379m, mVar.f3379m)) {
            hashSet.add("transformPivotY");
        }
        if (j(this.f3376j, mVar.f3376j)) {
            hashSet.add("scaleX");
        }
        if (j(this.f3377k, mVar.f3377k)) {
            hashSet.add("scaleY");
        }
        if (j(this.f3380n, mVar.f3380n)) {
            hashSet.add("translationX");
        }
        if (j(this.f3381p, mVar.f3381p)) {
            hashSet.add("translationY");
        }
        if (j(this.q, mVar.q)) {
            hashSet.add("translationZ");
        }
    }

    void o(float f5, float f8, float f9, float f10) {
        this.f3385y = f5;
        this.f3386z = f8;
        this.A = f9;
        this.B = f10;
    }

    public void q(View view) {
        o(view.getX(), view.getY(), view.getWidth(), view.getHeight());
        f(view);
    }

    public void r(ConstraintWidget constraintWidget, androidx.constraintlayout.widget.b bVar, int i8) {
        o(constraintWidget.R(), constraintWidget.S(), constraintWidget.Q(), constraintWidget.w());
        h(bVar.s(i8));
    }
}
