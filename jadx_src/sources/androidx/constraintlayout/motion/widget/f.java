package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f extends c {

    /* renamed from: g  reason: collision with root package name */
    private String f3271g = null;

    /* renamed from: h  reason: collision with root package name */
    private int f3272h = 0;

    /* renamed from: i  reason: collision with root package name */
    private int f3273i = -1;

    /* renamed from: j  reason: collision with root package name */
    private float f3274j = Float.NaN;

    /* renamed from: k  reason: collision with root package name */
    private float f3275k = 0.0f;

    /* renamed from: l  reason: collision with root package name */
    private float f3276l = Float.NaN;

    /* renamed from: m  reason: collision with root package name */
    private int f3277m = -1;

    /* renamed from: n  reason: collision with root package name */
    private float f3278n = Float.NaN;

    /* renamed from: o  reason: collision with root package name */
    private float f3279o = Float.NaN;

    /* renamed from: p  reason: collision with root package name */
    private float f3280p = Float.NaN;
    private float q = Float.NaN;

    /* renamed from: r  reason: collision with root package name */
    private float f3281r = Float.NaN;

    /* renamed from: s  reason: collision with root package name */
    private float f3282s = Float.NaN;

    /* renamed from: t  reason: collision with root package name */
    private float f3283t = Float.NaN;

    /* renamed from: u  reason: collision with root package name */
    private float f3284u = Float.NaN;

    /* renamed from: v  reason: collision with root package name */
    private float f3285v = Float.NaN;

    /* renamed from: w  reason: collision with root package name */
    private float f3286w = Float.NaN;

    /* renamed from: x  reason: collision with root package name */
    private float f3287x = Float.NaN;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a {

        /* renamed from: a  reason: collision with root package name */
        private static SparseIntArray f3288a;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f3288a = sparseIntArray;
            sparseIntArray.append(androidx.constraintlayout.widget.e.f4185h4, 1);
            f3288a.append(androidx.constraintlayout.widget.e.f4168f4, 2);
            f3288a.append(androidx.constraintlayout.widget.e.f4194i4, 3);
            f3288a.append(androidx.constraintlayout.widget.e.f4159e4, 4);
            f3288a.append(androidx.constraintlayout.widget.e.f4230m4, 5);
            f3288a.append(androidx.constraintlayout.widget.e.f4221l4, 6);
            f3288a.append(androidx.constraintlayout.widget.e.f4212k4, 7);
            f3288a.append(androidx.constraintlayout.widget.e.f4239n4, 8);
            f3288a.append(androidx.constraintlayout.widget.e.U3, 9);
            f3288a.append(androidx.constraintlayout.widget.e.f4150d4, 10);
            f3288a.append(androidx.constraintlayout.widget.e.Z3, 11);
            f3288a.append(androidx.constraintlayout.widget.e.f4120a4, 12);
            f3288a.append(androidx.constraintlayout.widget.e.f4130b4, 13);
            f3288a.append(androidx.constraintlayout.widget.e.f4203j4, 14);
            f3288a.append(androidx.constraintlayout.widget.e.X3, 15);
            f3288a.append(androidx.constraintlayout.widget.e.Y3, 16);
            f3288a.append(androidx.constraintlayout.widget.e.V3, 17);
            f3288a.append(androidx.constraintlayout.widget.e.W3, 18);
            f3288a.append(androidx.constraintlayout.widget.e.f4140c4, 19);
            f3288a.append(androidx.constraintlayout.widget.e.f4176g4, 20);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void b(f fVar, TypedArray typedArray) {
            int indexCount = typedArray.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = typedArray.getIndex(i8);
                switch (f3288a.get(index)) {
                    case 1:
                        if (MotionLayout.f3162b1) {
                            int resourceId = typedArray.getResourceId(index, fVar.f3249b);
                            fVar.f3249b = resourceId;
                            if (resourceId != -1) {
                                break;
                            }
                            fVar.f3250c = typedArray.getString(index);
                            break;
                        } else {
                            if (typedArray.peekValue(index).type != 3) {
                                fVar.f3249b = typedArray.getResourceId(index, fVar.f3249b);
                                break;
                            }
                            fVar.f3250c = typedArray.getString(index);
                        }
                    case 2:
                        fVar.f3248a = typedArray.getInt(index, fVar.f3248a);
                        break;
                    case 3:
                        fVar.f3271g = typedArray.getString(index);
                        break;
                    case 4:
                        fVar.f3272h = typedArray.getInteger(index, fVar.f3272h);
                        break;
                    case 5:
                        fVar.f3273i = typedArray.getInt(index, fVar.f3273i);
                        break;
                    case 6:
                        fVar.f3274j = typedArray.getFloat(index, fVar.f3274j);
                        break;
                    case 7:
                        fVar.f3275k = typedArray.peekValue(index).type == 5 ? typedArray.getDimension(index, fVar.f3275k) : typedArray.getFloat(index, fVar.f3275k);
                        break;
                    case 8:
                        fVar.f3277m = typedArray.getInt(index, fVar.f3277m);
                        break;
                    case 9:
                        fVar.f3278n = typedArray.getFloat(index, fVar.f3278n);
                        break;
                    case 10:
                        fVar.f3279o = typedArray.getDimension(index, fVar.f3279o);
                        break;
                    case 11:
                        fVar.f3280p = typedArray.getFloat(index, fVar.f3280p);
                        break;
                    case 12:
                        fVar.f3281r = typedArray.getFloat(index, fVar.f3281r);
                        break;
                    case 13:
                        fVar.f3282s = typedArray.getFloat(index, fVar.f3282s);
                        break;
                    case 14:
                        fVar.q = typedArray.getFloat(index, fVar.q);
                        break;
                    case 15:
                        fVar.f3283t = typedArray.getFloat(index, fVar.f3283t);
                        break;
                    case 16:
                        fVar.f3284u = typedArray.getFloat(index, fVar.f3284u);
                        break;
                    case 17:
                        fVar.f3285v = typedArray.getDimension(index, fVar.f3285v);
                        break;
                    case 18:
                        fVar.f3286w = typedArray.getDimension(index, fVar.f3286w);
                        break;
                    case 19:
                        if (Build.VERSION.SDK_INT >= 21) {
                            fVar.f3287x = typedArray.getDimension(index, fVar.f3287x);
                            break;
                        } else {
                            break;
                        }
                    case 20:
                        fVar.f3276l = typedArray.getFloat(index, fVar.f3276l);
                        break;
                    default:
                        Log.e("KeyCycle", "unused attribute 0x" + Integer.toHexString(index) + "   " + f3288a.get(index));
                        break;
                }
            }
        }
    }

    public f() {
        this.f3251d = 4;
        this.f3252e = new HashMap<>();
    }

    public void O(HashMap<String, g> hashMap) {
        for (String str : hashMap.keySet()) {
            if (str.startsWith("CUSTOM")) {
                ConstraintAttribute constraintAttribute = this.f3252e.get(str.substring(7));
                if (constraintAttribute != null && constraintAttribute.c() == ConstraintAttribute.AttributeType.FLOAT_TYPE) {
                    hashMap.get(str).e(this.f3248a, this.f3273i, this.f3277m, this.f3274j, this.f3275k, constraintAttribute.d(), constraintAttribute);
                }
            }
            float P = P(str);
            if (!Float.isNaN(P)) {
                hashMap.get(str).d(this.f3248a, this.f3273i, this.f3277m, this.f3274j, this.f3275k, P);
            }
        }
    }

    public float P(String str) {
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1249320806:
                if (str.equals("rotationX")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1249320805:
                if (str.equals("rotationY")) {
                    c9 = 1;
                    break;
                }
                break;
            case -1225497657:
                if (str.equals("translationX")) {
                    c9 = 2;
                    break;
                }
                break;
            case -1225497656:
                if (str.equals("translationY")) {
                    c9 = 3;
                    break;
                }
                break;
            case -1225497655:
                if (str.equals("translationZ")) {
                    c9 = 4;
                    break;
                }
                break;
            case -1001078227:
                if (str.equals("progress")) {
                    c9 = 5;
                    break;
                }
                break;
            case -908189618:
                if (str.equals("scaleX")) {
                    c9 = 6;
                    break;
                }
                break;
            case -908189617:
                if (str.equals("scaleY")) {
                    c9 = 7;
                    break;
                }
                break;
            case -40300674:
                if (str.equals("rotation")) {
                    c9 = '\b';
                    break;
                }
                break;
            case -4379043:
                if (str.equals("elevation")) {
                    c9 = '\t';
                    break;
                }
                break;
            case 37232917:
                if (str.equals("transitionPathRotate")) {
                    c9 = '\n';
                    break;
                }
                break;
            case 92909918:
                if (str.equals("alpha")) {
                    c9 = 11;
                    break;
                }
                break;
            case 156108012:
                if (str.equals("waveOffset")) {
                    c9 = '\f';
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return this.f3281r;
            case 1:
                return this.f3282s;
            case 2:
                return this.f3285v;
            case 3:
                return this.f3286w;
            case 4:
                return this.f3287x;
            case 5:
                return this.f3276l;
            case 6:
                return this.f3283t;
            case 7:
                return this.f3284u;
            case '\b':
                return this.f3280p;
            case '\t':
                return this.f3279o;
            case '\n':
                return this.q;
            case 11:
                return this.f3278n;
            case '\f':
                return this.f3275k;
            default:
                Log.v("WARNING! KeyCycle", "  UNKNOWN  " + str);
                return Float.NaN;
        }
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void a(HashMap<String, r> hashMap) {
        int i8;
        float f5;
        androidx.constraintlayout.motion.widget.a.e("KeyCycle", "add " + hashMap.size() + " values", 2);
        for (String str : hashMap.keySet()) {
            r rVar = hashMap.get(str);
            str.hashCode();
            char c9 = 65535;
            switch (str.hashCode()) {
                case -1249320806:
                    if (str.equals("rotationX")) {
                        c9 = 0;
                        break;
                    }
                    break;
                case -1249320805:
                    if (str.equals("rotationY")) {
                        c9 = 1;
                        break;
                    }
                    break;
                case -1225497657:
                    if (str.equals("translationX")) {
                        c9 = 2;
                        break;
                    }
                    break;
                case -1225497656:
                    if (str.equals("translationY")) {
                        c9 = 3;
                        break;
                    }
                    break;
                case -1225497655:
                    if (str.equals("translationZ")) {
                        c9 = 4;
                        break;
                    }
                    break;
                case -1001078227:
                    if (str.equals("progress")) {
                        c9 = 5;
                        break;
                    }
                    break;
                case -908189618:
                    if (str.equals("scaleX")) {
                        c9 = 6;
                        break;
                    }
                    break;
                case -908189617:
                    if (str.equals("scaleY")) {
                        c9 = 7;
                        break;
                    }
                    break;
                case -40300674:
                    if (str.equals("rotation")) {
                        c9 = '\b';
                        break;
                    }
                    break;
                case -4379043:
                    if (str.equals("elevation")) {
                        c9 = '\t';
                        break;
                    }
                    break;
                case 37232917:
                    if (str.equals("transitionPathRotate")) {
                        c9 = '\n';
                        break;
                    }
                    break;
                case 92909918:
                    if (str.equals("alpha")) {
                        c9 = 11;
                        break;
                    }
                    break;
                case 156108012:
                    if (str.equals("waveOffset")) {
                        c9 = '\f';
                        break;
                    }
                    break;
            }
            switch (c9) {
                case 0:
                    i8 = this.f3248a;
                    f5 = this.f3281r;
                    break;
                case 1:
                    i8 = this.f3248a;
                    f5 = this.f3282s;
                    break;
                case 2:
                    i8 = this.f3248a;
                    f5 = this.f3285v;
                    break;
                case 3:
                    i8 = this.f3248a;
                    f5 = this.f3286w;
                    break;
                case 4:
                    i8 = this.f3248a;
                    f5 = this.f3287x;
                    break;
                case 5:
                    i8 = this.f3248a;
                    f5 = this.f3276l;
                    break;
                case 6:
                    i8 = this.f3248a;
                    f5 = this.f3283t;
                    break;
                case 7:
                    i8 = this.f3248a;
                    f5 = this.f3284u;
                    break;
                case '\b':
                    i8 = this.f3248a;
                    f5 = this.f3280p;
                    break;
                case '\t':
                    i8 = this.f3248a;
                    f5 = this.f3279o;
                    break;
                case '\n':
                    i8 = this.f3248a;
                    f5 = this.q;
                    break;
                case 11:
                    i8 = this.f3248a;
                    f5 = this.f3278n;
                    break;
                case '\f':
                    i8 = this.f3248a;
                    f5 = this.f3275k;
                    break;
                default:
                    Log.v("WARNING KeyCycle", "  UNKNOWN  " + str);
                    continue;
            }
            rVar.e(i8, f5);
        }
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void b(HashSet<String> hashSet) {
        if (!Float.isNaN(this.f3278n)) {
            hashSet.add("alpha");
        }
        if (!Float.isNaN(this.f3279o)) {
            hashSet.add("elevation");
        }
        if (!Float.isNaN(this.f3280p)) {
            hashSet.add("rotation");
        }
        if (!Float.isNaN(this.f3281r)) {
            hashSet.add("rotationX");
        }
        if (!Float.isNaN(this.f3282s)) {
            hashSet.add("rotationY");
        }
        if (!Float.isNaN(this.f3283t)) {
            hashSet.add("scaleX");
        }
        if (!Float.isNaN(this.f3284u)) {
            hashSet.add("scaleY");
        }
        if (!Float.isNaN(this.q)) {
            hashSet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.f3285v)) {
            hashSet.add("translationX");
        }
        if (!Float.isNaN(this.f3286w)) {
            hashSet.add("translationY");
        }
        if (!Float.isNaN(this.f3287x)) {
            hashSet.add("translationZ");
        }
        if (this.f3252e.size() > 0) {
            Iterator<String> it = this.f3252e.keySet().iterator();
            while (it.hasNext()) {
                hashSet.add("CUSTOM," + it.next());
            }
        }
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void c(Context context, AttributeSet attributeSet) {
        a.b(this, context.obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.T3));
    }
}
