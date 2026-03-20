package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l extends c {

    /* renamed from: g  reason: collision with root package name */
    private int f3348g = -1;

    /* renamed from: h  reason: collision with root package name */
    private String f3349h = null;

    /* renamed from: i  reason: collision with root package name */
    private int f3350i;

    /* renamed from: j  reason: collision with root package name */
    private String f3351j;

    /* renamed from: k  reason: collision with root package name */
    private String f3352k;

    /* renamed from: l  reason: collision with root package name */
    private int f3353l;

    /* renamed from: m  reason: collision with root package name */
    private int f3354m;

    /* renamed from: n  reason: collision with root package name */
    private View f3355n;

    /* renamed from: o  reason: collision with root package name */
    float f3356o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f3357p;
    private boolean q;

    /* renamed from: r  reason: collision with root package name */
    private boolean f3358r;

    /* renamed from: s  reason: collision with root package name */
    private float f3359s;

    /* renamed from: t  reason: collision with root package name */
    private Method f3360t;

    /* renamed from: u  reason: collision with root package name */
    private Method f3361u;

    /* renamed from: v  reason: collision with root package name */
    private Method f3362v;

    /* renamed from: w  reason: collision with root package name */
    private float f3363w;

    /* renamed from: x  reason: collision with root package name */
    private boolean f3364x;

    /* renamed from: y  reason: collision with root package name */
    RectF f3365y;

    /* renamed from: z  reason: collision with root package name */
    RectF f3366z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a {

        /* renamed from: a  reason: collision with root package name */
        private static SparseIntArray f3367a;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f3367a = sparseIntArray;
            sparseIntArray.append(androidx.constraintlayout.widget.e.W4, 8);
            f3367a.append(androidx.constraintlayout.widget.e.f4121a5, 4);
            f3367a.append(androidx.constraintlayout.widget.e.f4131b5, 1);
            f3367a.append(androidx.constraintlayout.widget.e.f4141c5, 2);
            f3367a.append(androidx.constraintlayout.widget.e.X4, 7);
            f3367a.append(androidx.constraintlayout.widget.e.f4151d5, 6);
            f3367a.append(androidx.constraintlayout.widget.e.f5, 5);
            f3367a.append(androidx.constraintlayout.widget.e.Z4, 9);
            f3367a.append(androidx.constraintlayout.widget.e.Y4, 10);
            f3367a.append(androidx.constraintlayout.widget.e.f4160e5, 11);
        }

        public static void a(l lVar, TypedArray typedArray, Context context) {
            int indexCount = typedArray.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = typedArray.getIndex(i8);
                switch (f3367a.get(index)) {
                    case 1:
                        lVar.f3351j = typedArray.getString(index);
                        continue;
                    case 2:
                        lVar.f3352k = typedArray.getString(index);
                        continue;
                    case 4:
                        lVar.f3349h = typedArray.getString(index);
                        continue;
                    case 5:
                        lVar.f3356o = typedArray.getFloat(index, lVar.f3356o);
                        continue;
                    case 6:
                        lVar.f3353l = typedArray.getResourceId(index, lVar.f3353l);
                        continue;
                    case 7:
                        if (MotionLayout.f3162b1) {
                            int resourceId = typedArray.getResourceId(index, lVar.f3249b);
                            lVar.f3249b = resourceId;
                            if (resourceId != -1) {
                                continue;
                            }
                            lVar.f3250c = typedArray.getString(index);
                        } else {
                            if (typedArray.peekValue(index).type != 3) {
                                lVar.f3249b = typedArray.getResourceId(index, lVar.f3249b);
                            }
                            lVar.f3250c = typedArray.getString(index);
                        }
                    case 8:
                        int integer = typedArray.getInteger(index, lVar.f3248a);
                        lVar.f3248a = integer;
                        lVar.f3359s = (integer + 0.5f) / 100.0f;
                        continue;
                    case 9:
                        lVar.f3354m = typedArray.getResourceId(index, lVar.f3354m);
                        continue;
                    case 10:
                        lVar.f3364x = typedArray.getBoolean(index, lVar.f3364x);
                        continue;
                    case 11:
                        lVar.f3350i = typedArray.getResourceId(index, lVar.f3350i);
                        break;
                }
                Log.e("KeyTrigger", "unused attribute 0x" + Integer.toHexString(index) + "   " + f3367a.get(index));
            }
        }
    }

    public l() {
        int i8 = c.f3247f;
        this.f3350i = i8;
        this.f3351j = null;
        this.f3352k = null;
        this.f3353l = i8;
        this.f3354m = i8;
        this.f3355n = null;
        this.f3356o = 0.1f;
        this.f3357p = true;
        this.q = true;
        this.f3358r = true;
        this.f3359s = Float.NaN;
        this.f3364x = false;
        this.f3365y = new RectF();
        this.f3366z = new RectF();
        this.f3251d = 5;
        this.f3252e = new HashMap<>();
    }

    private void s(RectF rectF, View view, boolean z4) {
        rectF.top = view.getTop();
        rectF.bottom = view.getBottom();
        rectF.left = view.getLeft();
        rectF.right = view.getRight();
        if (z4) {
            view.getMatrix().mapRect(rectF);
        }
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void a(HashMap<String, r> hashMap) {
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void b(HashSet<String> hashSet) {
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void c(Context context, AttributeSet attributeSet) {
        a.a(this, context.obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.V4), context);
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00c9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void r(float r11, android.view.View r12) {
        /*
            Method dump skipped, instructions count: 631
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.l.r(float, android.view.View):void");
    }
}
