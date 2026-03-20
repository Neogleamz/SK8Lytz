package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i extends j {

    /* renamed from: h  reason: collision with root package name */
    String f3318h = null;

    /* renamed from: i  reason: collision with root package name */
    int f3319i = c.f3247f;

    /* renamed from: j  reason: collision with root package name */
    int f3320j = 0;

    /* renamed from: k  reason: collision with root package name */
    float f3321k = Float.NaN;

    /* renamed from: l  reason: collision with root package name */
    float f3322l = Float.NaN;

    /* renamed from: m  reason: collision with root package name */
    float f3323m = Float.NaN;

    /* renamed from: n  reason: collision with root package name */
    float f3324n = Float.NaN;

    /* renamed from: o  reason: collision with root package name */
    float f3325o = Float.NaN;

    /* renamed from: p  reason: collision with root package name */
    float f3326p = Float.NaN;
    int q = 0;

    /* renamed from: r  reason: collision with root package name */
    private float f3327r = Float.NaN;

    /* renamed from: s  reason: collision with root package name */
    private float f3328s = Float.NaN;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a {

        /* renamed from: a  reason: collision with root package name */
        private static SparseIntArray f3329a;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f3329a = sparseIntArray;
            sparseIntArray.append(androidx.constraintlayout.widget.e.f4290t4, 1);
            f3329a.append(androidx.constraintlayout.widget.e.r4, 2);
            f3329a.append(androidx.constraintlayout.widget.e.A4, 3);
            f3329a.append(androidx.constraintlayout.widget.e.f4256p4, 4);
            f3329a.append(androidx.constraintlayout.widget.e.f4264q4, 5);
            f3329a.append(androidx.constraintlayout.widget.e.f4326x4, 6);
            f3329a.append(androidx.constraintlayout.widget.e.f4335y4, 7);
            f3329a.append(androidx.constraintlayout.widget.e.f4281s4, 9);
            f3329a.append(androidx.constraintlayout.widget.e.z4, 8);
            f3329a.append(androidx.constraintlayout.widget.e.f4317w4, 11);
            f3329a.append(androidx.constraintlayout.widget.e.f4308v4, 12);
            f3329a.append(androidx.constraintlayout.widget.e.f4299u4, 10);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void b(i iVar, TypedArray typedArray) {
            float f5;
            int indexCount = typedArray.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = typedArray.getIndex(i8);
                switch (f3329a.get(index)) {
                    case 1:
                        if (MotionLayout.f3162b1) {
                            int resourceId = typedArray.getResourceId(index, iVar.f3249b);
                            iVar.f3249b = resourceId;
                            if (resourceId != -1) {
                            }
                            iVar.f3250c = typedArray.getString(index);
                        } else {
                            if (typedArray.peekValue(index).type != 3) {
                                iVar.f3249b = typedArray.getResourceId(index, iVar.f3249b);
                                continue;
                            }
                            iVar.f3250c = typedArray.getString(index);
                        }
                    case 2:
                        iVar.f3248a = typedArray.getInt(index, iVar.f3248a);
                        continue;
                    case 3:
                        iVar.f3318h = typedArray.peekValue(index).type == 3 ? typedArray.getString(index) : l0.c.f21520c[typedArray.getInteger(index, 0)];
                        continue;
                    case 4:
                        iVar.f3330g = typedArray.getInteger(index, iVar.f3330g);
                        continue;
                    case 5:
                        iVar.f3320j = typedArray.getInt(index, iVar.f3320j);
                        continue;
                    case 6:
                        iVar.f3323m = typedArray.getFloat(index, iVar.f3323m);
                        continue;
                    case 7:
                        iVar.f3324n = typedArray.getFloat(index, iVar.f3324n);
                        continue;
                    case 8:
                        f5 = typedArray.getFloat(index, iVar.f3322l);
                        iVar.f3321k = f5;
                        break;
                    case 9:
                        iVar.q = typedArray.getInt(index, iVar.q);
                        continue;
                    case 10:
                        iVar.f3319i = typedArray.getInt(index, iVar.f3319i);
                        continue;
                    case 11:
                        iVar.f3321k = typedArray.getFloat(index, iVar.f3321k);
                        continue;
                    case 12:
                        f5 = typedArray.getFloat(index, iVar.f3322l);
                        break;
                    default:
                        Log.e("KeyPosition", "unused attribute 0x" + Integer.toHexString(index) + "   " + f3329a.get(index));
                        continue;
                }
                iVar.f3322l = f5;
            }
            if (iVar.f3248a == -1) {
                Log.e("KeyPosition", "no frame position");
            }
        }
    }

    public i() {
        this.f3251d = 2;
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void a(HashMap<String, r> hashMap) {
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void c(Context context, AttributeSet attributeSet) {
        a.b(this, context.obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.f4248o4));
    }
}
