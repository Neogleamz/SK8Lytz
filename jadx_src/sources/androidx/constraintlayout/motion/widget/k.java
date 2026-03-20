package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k extends c {

    /* renamed from: g  reason: collision with root package name */
    private String f3331g;

    /* renamed from: h  reason: collision with root package name */
    private int f3332h = -1;

    /* renamed from: i  reason: collision with root package name */
    private float f3333i = Float.NaN;

    /* renamed from: j  reason: collision with root package name */
    private float f3334j = Float.NaN;

    /* renamed from: k  reason: collision with root package name */
    private float f3335k = Float.NaN;

    /* renamed from: l  reason: collision with root package name */
    private float f3336l = Float.NaN;

    /* renamed from: m  reason: collision with root package name */
    private float f3337m = Float.NaN;

    /* renamed from: n  reason: collision with root package name */
    private float f3338n = Float.NaN;

    /* renamed from: o  reason: collision with root package name */
    private float f3339o = Float.NaN;

    /* renamed from: p  reason: collision with root package name */
    private float f3340p = Float.NaN;
    private float q = Float.NaN;

    /* renamed from: r  reason: collision with root package name */
    private float f3341r = Float.NaN;

    /* renamed from: s  reason: collision with root package name */
    private float f3342s = Float.NaN;

    /* renamed from: t  reason: collision with root package name */
    private float f3343t = Float.NaN;

    /* renamed from: u  reason: collision with root package name */
    private int f3344u = 0;

    /* renamed from: v  reason: collision with root package name */
    private float f3345v = Float.NaN;

    /* renamed from: w  reason: collision with root package name */
    private float f3346w = 0.0f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a {

        /* renamed from: a  reason: collision with root package name */
        private static SparseIntArray f3347a;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f3347a = sparseIntArray;
            sparseIntArray.append(androidx.constraintlayout.widget.e.C4, 1);
            f3347a.append(androidx.constraintlayout.widget.e.L4, 2);
            f3347a.append(androidx.constraintlayout.widget.e.H4, 4);
            f3347a.append(androidx.constraintlayout.widget.e.I4, 5);
            f3347a.append(androidx.constraintlayout.widget.e.J4, 6);
            f3347a.append(androidx.constraintlayout.widget.e.F4, 7);
            f3347a.append(androidx.constraintlayout.widget.e.R4, 8);
            f3347a.append(androidx.constraintlayout.widget.e.Q4, 9);
            f3347a.append(androidx.constraintlayout.widget.e.P4, 10);
            f3347a.append(androidx.constraintlayout.widget.e.N4, 12);
            f3347a.append(androidx.constraintlayout.widget.e.M4, 13);
            f3347a.append(androidx.constraintlayout.widget.e.G4, 14);
            f3347a.append(androidx.constraintlayout.widget.e.D4, 15);
            f3347a.append(androidx.constraintlayout.widget.e.E4, 16);
            f3347a.append(androidx.constraintlayout.widget.e.K4, 17);
            f3347a.append(androidx.constraintlayout.widget.e.O4, 18);
            f3347a.append(androidx.constraintlayout.widget.e.T4, 20);
            f3347a.append(androidx.constraintlayout.widget.e.S4, 21);
            f3347a.append(androidx.constraintlayout.widget.e.U4, 19);
        }

        public static void a(k kVar, TypedArray typedArray) {
            int indexCount = typedArray.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = typedArray.getIndex(i8);
                switch (f3347a.get(index)) {
                    case 1:
                        kVar.f3333i = typedArray.getFloat(index, kVar.f3333i);
                        break;
                    case 2:
                        kVar.f3334j = typedArray.getDimension(index, kVar.f3334j);
                        break;
                    case 3:
                    case 11:
                    default:
                        Log.e("KeyTimeCycle", "unused attribute 0x" + Integer.toHexString(index) + "   " + f3347a.get(index));
                        break;
                    case 4:
                        kVar.f3335k = typedArray.getFloat(index, kVar.f3335k);
                        break;
                    case 5:
                        kVar.f3336l = typedArray.getFloat(index, kVar.f3336l);
                        break;
                    case 6:
                        kVar.f3337m = typedArray.getFloat(index, kVar.f3337m);
                        break;
                    case 7:
                        kVar.f3339o = typedArray.getFloat(index, kVar.f3339o);
                        break;
                    case 8:
                        kVar.f3338n = typedArray.getFloat(index, kVar.f3338n);
                        break;
                    case 9:
                        kVar.f3331g = typedArray.getString(index);
                        break;
                    case 10:
                        if (MotionLayout.f3162b1) {
                            int resourceId = typedArray.getResourceId(index, kVar.f3249b);
                            kVar.f3249b = resourceId;
                            if (resourceId != -1) {
                                break;
                            }
                            kVar.f3250c = typedArray.getString(index);
                            break;
                        } else {
                            if (typedArray.peekValue(index).type != 3) {
                                kVar.f3249b = typedArray.getResourceId(index, kVar.f3249b);
                                break;
                            }
                            kVar.f3250c = typedArray.getString(index);
                        }
                    case 12:
                        kVar.f3248a = typedArray.getInt(index, kVar.f3248a);
                        break;
                    case 13:
                        kVar.f3332h = typedArray.getInteger(index, kVar.f3332h);
                        break;
                    case 14:
                        kVar.f3340p = typedArray.getFloat(index, kVar.f3340p);
                        break;
                    case 15:
                        kVar.q = typedArray.getDimension(index, kVar.q);
                        break;
                    case 16:
                        kVar.f3341r = typedArray.getDimension(index, kVar.f3341r);
                        break;
                    case 17:
                        if (Build.VERSION.SDK_INT >= 21) {
                            kVar.f3342s = typedArray.getDimension(index, kVar.f3342s);
                            break;
                        } else {
                            break;
                        }
                    case 18:
                        kVar.f3343t = typedArray.getFloat(index, kVar.f3343t);
                        break;
                    case 19:
                        kVar.f3344u = typedArray.getInt(index, kVar.f3344u);
                        break;
                    case 20:
                        kVar.f3345v = typedArray.getFloat(index, kVar.f3345v);
                        break;
                    case 21:
                        kVar.f3346w = typedArray.peekValue(index).type == 5 ? typedArray.getDimension(index, kVar.f3346w) : typedArray.getFloat(index, kVar.f3346w);
                        break;
                }
            }
        }
    }

    public k() {
        this.f3251d = 3;
        this.f3252e = new HashMap<>();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0086, code lost:
        if (r1.equals("scaleY") == false) goto L9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void M(java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.widget.s> r11) {
        /*
            Method dump skipped, instructions count: 494
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.k.M(java.util.HashMap):void");
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void a(HashMap<String, r> hashMap) {
        throw new IllegalArgumentException(" KeyTimeCycles do not support SplineSet");
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void b(HashSet<String> hashSet) {
        if (!Float.isNaN(this.f3333i)) {
            hashSet.add("alpha");
        }
        if (!Float.isNaN(this.f3334j)) {
            hashSet.add("elevation");
        }
        if (!Float.isNaN(this.f3335k)) {
            hashSet.add("rotation");
        }
        if (!Float.isNaN(this.f3336l)) {
            hashSet.add("rotationX");
        }
        if (!Float.isNaN(this.f3337m)) {
            hashSet.add("rotationY");
        }
        if (!Float.isNaN(this.q)) {
            hashSet.add("translationX");
        }
        if (!Float.isNaN(this.f3341r)) {
            hashSet.add("translationY");
        }
        if (!Float.isNaN(this.f3342s)) {
            hashSet.add("translationZ");
        }
        if (!Float.isNaN(this.f3338n)) {
            hashSet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.f3339o)) {
            hashSet.add("scaleX");
        }
        if (!Float.isNaN(this.f3340p)) {
            hashSet.add("scaleY");
        }
        if (!Float.isNaN(this.f3343t)) {
            hashSet.add("progress");
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
        a.a(this, context.obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.B4));
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void e(HashMap<String, Integer> hashMap) {
        if (this.f3332h == -1) {
            return;
        }
        if (!Float.isNaN(this.f3333i)) {
            hashMap.put("alpha", Integer.valueOf(this.f3332h));
        }
        if (!Float.isNaN(this.f3334j)) {
            hashMap.put("elevation", Integer.valueOf(this.f3332h));
        }
        if (!Float.isNaN(this.f3335k)) {
            hashMap.put("rotation", Integer.valueOf(this.f3332h));
        }
        if (!Float.isNaN(this.f3336l)) {
            hashMap.put("rotationX", Integer.valueOf(this.f3332h));
        }
        if (!Float.isNaN(this.f3337m)) {
            hashMap.put("rotationY", Integer.valueOf(this.f3332h));
        }
        if (!Float.isNaN(this.q)) {
            hashMap.put("translationX", Integer.valueOf(this.f3332h));
        }
        if (!Float.isNaN(this.f3341r)) {
            hashMap.put("translationY", Integer.valueOf(this.f3332h));
        }
        if (!Float.isNaN(this.f3342s)) {
            hashMap.put("translationZ", Integer.valueOf(this.f3332h));
        }
        if (!Float.isNaN(this.f3338n)) {
            hashMap.put("transitionPathRotate", Integer.valueOf(this.f3332h));
        }
        if (!Float.isNaN(this.f3339o)) {
            hashMap.put("scaleX", Integer.valueOf(this.f3332h));
        }
        if (!Float.isNaN(this.f3339o)) {
            hashMap.put("scaleY", Integer.valueOf(this.f3332h));
        }
        if (!Float.isNaN(this.f3343t)) {
            hashMap.put("progress", Integer.valueOf(this.f3332h));
        }
        if (this.f3252e.size() > 0) {
            Iterator<String> it = this.f3252e.keySet().iterator();
            while (it.hasNext()) {
                hashMap.put("CUSTOM," + it.next(), Integer.valueOf(this.f3332h));
            }
        }
    }
}
