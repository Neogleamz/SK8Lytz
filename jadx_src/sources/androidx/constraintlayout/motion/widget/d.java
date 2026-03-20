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
public class d extends c {

    /* renamed from: g  reason: collision with root package name */
    private String f3253g;

    /* renamed from: h  reason: collision with root package name */
    private int f3254h = -1;

    /* renamed from: i  reason: collision with root package name */
    private boolean f3255i = false;

    /* renamed from: j  reason: collision with root package name */
    private float f3256j = Float.NaN;

    /* renamed from: k  reason: collision with root package name */
    private float f3257k = Float.NaN;

    /* renamed from: l  reason: collision with root package name */
    private float f3258l = Float.NaN;

    /* renamed from: m  reason: collision with root package name */
    private float f3259m = Float.NaN;

    /* renamed from: n  reason: collision with root package name */
    private float f3260n = Float.NaN;

    /* renamed from: o  reason: collision with root package name */
    private float f3261o = Float.NaN;

    /* renamed from: p  reason: collision with root package name */
    private float f3262p = Float.NaN;
    private float q = Float.NaN;

    /* renamed from: r  reason: collision with root package name */
    private float f3263r = Float.NaN;

    /* renamed from: s  reason: collision with root package name */
    private float f3264s = Float.NaN;

    /* renamed from: t  reason: collision with root package name */
    private float f3265t = Float.NaN;

    /* renamed from: u  reason: collision with root package name */
    private float f3266u = Float.NaN;

    /* renamed from: v  reason: collision with root package name */
    private float f3267v = Float.NaN;

    /* renamed from: w  reason: collision with root package name */
    private float f3268w = Float.NaN;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a {

        /* renamed from: a  reason: collision with root package name */
        private static SparseIntArray f3269a;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f3269a = sparseIntArray;
            sparseIntArray.append(androidx.constraintlayout.widget.e.B3, 1);
            f3269a.append(androidx.constraintlayout.widget.e.M3, 2);
            f3269a.append(androidx.constraintlayout.widget.e.I3, 4);
            f3269a.append(androidx.constraintlayout.widget.e.J3, 5);
            f3269a.append(androidx.constraintlayout.widget.e.K3, 6);
            f3269a.append(androidx.constraintlayout.widget.e.C3, 19);
            f3269a.append(androidx.constraintlayout.widget.e.D3, 20);
            f3269a.append(androidx.constraintlayout.widget.e.G3, 7);
            f3269a.append(androidx.constraintlayout.widget.e.S3, 8);
            f3269a.append(androidx.constraintlayout.widget.e.R3, 9);
            f3269a.append(androidx.constraintlayout.widget.e.Q3, 10);
            f3269a.append(androidx.constraintlayout.widget.e.O3, 12);
            f3269a.append(androidx.constraintlayout.widget.e.N3, 13);
            f3269a.append(androidx.constraintlayout.widget.e.H3, 14);
            f3269a.append(androidx.constraintlayout.widget.e.E3, 15);
            f3269a.append(androidx.constraintlayout.widget.e.F3, 16);
            f3269a.append(androidx.constraintlayout.widget.e.L3, 17);
            f3269a.append(androidx.constraintlayout.widget.e.P3, 18);
        }

        public static void a(d dVar, TypedArray typedArray) {
            int indexCount = typedArray.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = typedArray.getIndex(i8);
                switch (f3269a.get(index)) {
                    case 1:
                        dVar.f3256j = typedArray.getFloat(index, dVar.f3256j);
                        break;
                    case 2:
                        dVar.f3257k = typedArray.getDimension(index, dVar.f3257k);
                        break;
                    case 3:
                    case 11:
                    default:
                        Log.e("KeyAttribute", "unused attribute 0x" + Integer.toHexString(index) + "   " + f3269a.get(index));
                        break;
                    case 4:
                        dVar.f3258l = typedArray.getFloat(index, dVar.f3258l);
                        break;
                    case 5:
                        dVar.f3259m = typedArray.getFloat(index, dVar.f3259m);
                        break;
                    case 6:
                        dVar.f3260n = typedArray.getFloat(index, dVar.f3260n);
                        break;
                    case 7:
                        dVar.f3263r = typedArray.getFloat(index, dVar.f3263r);
                        break;
                    case 8:
                        dVar.q = typedArray.getFloat(index, dVar.q);
                        break;
                    case 9:
                        dVar.f3253g = typedArray.getString(index);
                        break;
                    case 10:
                        if (MotionLayout.f3162b1) {
                            int resourceId = typedArray.getResourceId(index, dVar.f3249b);
                            dVar.f3249b = resourceId;
                            if (resourceId != -1) {
                                break;
                            }
                            dVar.f3250c = typedArray.getString(index);
                            break;
                        } else {
                            if (typedArray.peekValue(index).type != 3) {
                                dVar.f3249b = typedArray.getResourceId(index, dVar.f3249b);
                                break;
                            }
                            dVar.f3250c = typedArray.getString(index);
                        }
                    case 12:
                        dVar.f3248a = typedArray.getInt(index, dVar.f3248a);
                        break;
                    case 13:
                        dVar.f3254h = typedArray.getInteger(index, dVar.f3254h);
                        break;
                    case 14:
                        dVar.f3264s = typedArray.getFloat(index, dVar.f3264s);
                        break;
                    case 15:
                        dVar.f3265t = typedArray.getDimension(index, dVar.f3265t);
                        break;
                    case 16:
                        dVar.f3266u = typedArray.getDimension(index, dVar.f3266u);
                        break;
                    case 17:
                        if (Build.VERSION.SDK_INT >= 21) {
                            dVar.f3267v = typedArray.getDimension(index, dVar.f3267v);
                            break;
                        } else {
                            break;
                        }
                    case 18:
                        dVar.f3268w = typedArray.getFloat(index, dVar.f3268w);
                        break;
                    case 19:
                        dVar.f3261o = typedArray.getDimension(index, dVar.f3261o);
                        break;
                    case 20:
                        dVar.f3262p = typedArray.getDimension(index, dVar.f3262p);
                        break;
                }
            }
        }
    }

    public d() {
        this.f3251d = 1;
        this.f3252e = new HashMap<>();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0097, code lost:
        if (r1.equals("scaleY") == false) goto L9;
     */
    @Override // androidx.constraintlayout.motion.widget.c
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.widget.r> r7) {
        /*
            Method dump skipped, instructions count: 548
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.d.a(java.util.HashMap):void");
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void b(HashSet<String> hashSet) {
        if (!Float.isNaN(this.f3256j)) {
            hashSet.add("alpha");
        }
        if (!Float.isNaN(this.f3257k)) {
            hashSet.add("elevation");
        }
        if (!Float.isNaN(this.f3258l)) {
            hashSet.add("rotation");
        }
        if (!Float.isNaN(this.f3259m)) {
            hashSet.add("rotationX");
        }
        if (!Float.isNaN(this.f3260n)) {
            hashSet.add("rotationY");
        }
        if (!Float.isNaN(this.f3261o)) {
            hashSet.add("transformPivotX");
        }
        if (!Float.isNaN(this.f3262p)) {
            hashSet.add("transformPivotY");
        }
        if (!Float.isNaN(this.f3265t)) {
            hashSet.add("translationX");
        }
        if (!Float.isNaN(this.f3266u)) {
            hashSet.add("translationY");
        }
        if (!Float.isNaN(this.f3267v)) {
            hashSet.add("translationZ");
        }
        if (!Float.isNaN(this.q)) {
            hashSet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.f3263r)) {
            hashSet.add("scaleX");
        }
        if (!Float.isNaN(this.f3263r)) {
            hashSet.add("scaleY");
        }
        if (!Float.isNaN(this.f3268w)) {
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
        a.a(this, context.obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.A3));
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void e(HashMap<String, Integer> hashMap) {
        if (this.f3254h == -1) {
            return;
        }
        if (!Float.isNaN(this.f3256j)) {
            hashMap.put("alpha", Integer.valueOf(this.f3254h));
        }
        if (!Float.isNaN(this.f3257k)) {
            hashMap.put("elevation", Integer.valueOf(this.f3254h));
        }
        if (!Float.isNaN(this.f3258l)) {
            hashMap.put("rotation", Integer.valueOf(this.f3254h));
        }
        if (!Float.isNaN(this.f3259m)) {
            hashMap.put("rotationX", Integer.valueOf(this.f3254h));
        }
        if (!Float.isNaN(this.f3260n)) {
            hashMap.put("rotationY", Integer.valueOf(this.f3254h));
        }
        if (!Float.isNaN(this.f3261o)) {
            hashMap.put("transformPivotX", Integer.valueOf(this.f3254h));
        }
        if (!Float.isNaN(this.f3262p)) {
            hashMap.put("transformPivotY", Integer.valueOf(this.f3254h));
        }
        if (!Float.isNaN(this.f3265t)) {
            hashMap.put("translationX", Integer.valueOf(this.f3254h));
        }
        if (!Float.isNaN(this.f3266u)) {
            hashMap.put("translationY", Integer.valueOf(this.f3254h));
        }
        if (!Float.isNaN(this.f3267v)) {
            hashMap.put("translationZ", Integer.valueOf(this.f3254h));
        }
        if (!Float.isNaN(this.q)) {
            hashMap.put("transitionPathRotate", Integer.valueOf(this.f3254h));
        }
        if (!Float.isNaN(this.f3263r)) {
            hashMap.put("scaleX", Integer.valueOf(this.f3254h));
        }
        if (!Float.isNaN(this.f3264s)) {
            hashMap.put("scaleY", Integer.valueOf(this.f3254h));
        }
        if (!Float.isNaN(this.f3268w)) {
            hashMap.put("progress", Integer.valueOf(this.f3254h));
        }
        if (this.f3252e.size() > 0) {
            Iterator<String> it = this.f3252e.keySet().iterator();
            while (it.hasNext()) {
                hashMap.put("CUSTOM," + it.next(), Integer.valueOf(this.f3254h));
            }
        }
    }
}
