package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.widget.NestedScrollView;
import org.xmlpull.v1.XmlPullParser;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class t {

    /* renamed from: v  reason: collision with root package name */
    private static final float[][] f3493v = {new float[]{0.5f, 0.0f}, new float[]{0.0f, 0.5f}, new float[]{1.0f, 0.5f}, new float[]{0.5f, 1.0f}, new float[]{0.5f, 0.5f}, new float[]{0.0f, 0.5f}, new float[]{1.0f, 0.5f}};

    /* renamed from: w  reason: collision with root package name */
    private static final float[][] f3494w = {new float[]{0.0f, -1.0f}, new float[]{0.0f, 1.0f}, new float[]{-1.0f, 0.0f}, new float[]{1.0f, 0.0f}, new float[]{-1.0f, 0.0f}, new float[]{1.0f, 0.0f}};

    /* renamed from: m  reason: collision with root package name */
    private float f3507m;

    /* renamed from: n  reason: collision with root package name */
    private float f3508n;

    /* renamed from: o  reason: collision with root package name */
    private final MotionLayout f3509o;

    /* renamed from: a  reason: collision with root package name */
    private int f3495a = 0;

    /* renamed from: b  reason: collision with root package name */
    private int f3496b = 0;

    /* renamed from: c  reason: collision with root package name */
    private int f3497c = 0;

    /* renamed from: d  reason: collision with root package name */
    private int f3498d = -1;

    /* renamed from: e  reason: collision with root package name */
    private int f3499e = -1;

    /* renamed from: f  reason: collision with root package name */
    private int f3500f = -1;

    /* renamed from: g  reason: collision with root package name */
    private float f3501g = 0.5f;

    /* renamed from: h  reason: collision with root package name */
    private float f3502h = 0.5f;

    /* renamed from: i  reason: collision with root package name */
    private float f3503i = 0.0f;

    /* renamed from: j  reason: collision with root package name */
    private float f3504j = 1.0f;

    /* renamed from: k  reason: collision with root package name */
    private boolean f3505k = false;

    /* renamed from: l  reason: collision with root package name */
    private float[] f3506l = new float[2];

    /* renamed from: p  reason: collision with root package name */
    private float f3510p = 4.0f;
    private float q = 1.2f;

    /* renamed from: r  reason: collision with root package name */
    private boolean f3511r = true;

    /* renamed from: s  reason: collision with root package name */
    private float f3512s = 1.0f;

    /* renamed from: t  reason: collision with root package name */
    private int f3513t = 0;

    /* renamed from: u  reason: collision with root package name */
    private float f3514u = 10.0f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements View.OnTouchListener {
        a() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements NestedScrollView.c {
        b() {
        }

        @Override // androidx.core.widget.NestedScrollView.c
        public void a(NestedScrollView nestedScrollView, int i8, int i9, int i10, int i11) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public t(Context context, MotionLayout motionLayout, XmlPullParser xmlPullParser) {
        this.f3509o = motionLayout;
        c(context, Xml.asAttributeSet(xmlPullParser));
    }

    private void b(TypedArray typedArray) {
        int indexCount = typedArray.getIndexCount();
        for (int i8 = 0; i8 < indexCount; i8++) {
            int index = typedArray.getIndex(i8);
            if (index == androidx.constraintlayout.widget.e.f4197i7) {
                this.f3498d = typedArray.getResourceId(index, this.f3498d);
            } else if (index == androidx.constraintlayout.widget.e.f4206j7) {
                int i9 = typedArray.getInt(index, this.f3495a);
                this.f3495a = i9;
                float[][] fArr = f3493v;
                this.f3502h = fArr[i9][0];
                this.f3501g = fArr[i9][1];
            } else if (index == androidx.constraintlayout.widget.e.Z6) {
                int i10 = typedArray.getInt(index, this.f3496b);
                this.f3496b = i10;
                float[][] fArr2 = f3494w;
                this.f3503i = fArr2[i10][0];
                this.f3504j = fArr2[i10][1];
            } else if (index == androidx.constraintlayout.widget.e.f4162e7) {
                this.f3510p = typedArray.getFloat(index, this.f3510p);
            } else if (index == androidx.constraintlayout.widget.e.f4153d7) {
                this.q = typedArray.getFloat(index, this.q);
            } else if (index == androidx.constraintlayout.widget.e.f4170f7) {
                this.f3511r = typedArray.getBoolean(index, this.f3511r);
            } else if (index == androidx.constraintlayout.widget.e.f4123a7) {
                this.f3512s = typedArray.getFloat(index, this.f3512s);
            } else if (index == androidx.constraintlayout.widget.e.f4133b7) {
                this.f3514u = typedArray.getFloat(index, this.f3514u);
            } else if (index == androidx.constraintlayout.widget.e.f4215k7) {
                this.f3499e = typedArray.getResourceId(index, this.f3499e);
            } else if (index == androidx.constraintlayout.widget.e.f4188h7) {
                this.f3497c = typedArray.getInt(index, this.f3497c);
            } else if (index == androidx.constraintlayout.widget.e.f4179g7) {
                this.f3513t = typedArray.getInteger(index, 0);
            } else if (index == androidx.constraintlayout.widget.e.f4143c7) {
                this.f3500f = typedArray.getResourceId(index, 0);
            }
        }
    }

    private void c(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.Y6);
        b(obtainStyledAttributes);
        obtainStyledAttributes.recycle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float a(float f5, float f8) {
        return (f5 * this.f3503i) + (f8 * this.f3504j);
    }

    public int d() {
        return this.f3513t;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RectF e(ViewGroup viewGroup, RectF rectF) {
        View findViewById;
        int i8 = this.f3500f;
        if (i8 == -1 || (findViewById = viewGroup.findViewById(i8)) == null) {
            return null;
        }
        rectF.set(findViewById.getLeft(), findViewById.getTop(), findViewById.getRight(), findViewById.getBottom());
        return rectF;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float f() {
        return this.q;
    }

    public float g() {
        return this.f3510p;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean h() {
        return this.f3511r;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float i(float f5, float f8) {
        this.f3509o.e0(this.f3498d, this.f3509o.getProgress(), this.f3502h, this.f3501g, this.f3506l);
        float f9 = this.f3503i;
        if (f9 != 0.0f) {
            float[] fArr = this.f3506l;
            if (fArr[0] == 0.0f) {
                fArr[0] = 1.0E-7f;
            }
            return (f5 * f9) / fArr[0];
        }
        float[] fArr2 = this.f3506l;
        if (fArr2[1] == 0.0f) {
            fArr2[1] = 1.0E-7f;
        }
        return (f8 * this.f3504j) / fArr2[1];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RectF j(ViewGroup viewGroup, RectF rectF) {
        View findViewById;
        int i8 = this.f3499e;
        if (i8 == -1 || (findViewById = viewGroup.findViewById(i8)) == null) {
            return null;
        }
        rectF.set(findViewById.getLeft(), findViewById.getTop(), findViewById.getRight(), findViewById.getBottom());
        return rectF;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int k() {
        return this.f3499e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(MotionEvent motionEvent, MotionLayout.f fVar, int i8, q qVar) {
        int i9;
        fVar.b(motionEvent);
        int action = motionEvent.getAction();
        if (action == 0) {
            this.f3507m = motionEvent.getRawX();
            this.f3508n = motionEvent.getRawY();
            this.f3505k = false;
        } else if (action == 1) {
            this.f3505k = false;
            fVar.e(1000);
            float d8 = fVar.d();
            float c9 = fVar.c();
            float progress = this.f3509o.getProgress();
            int i10 = this.f3498d;
            if (i10 != -1) {
                this.f3509o.e0(i10, progress, this.f3502h, this.f3501g, this.f3506l);
            } else {
                float min = Math.min(this.f3509o.getWidth(), this.f3509o.getHeight());
                float[] fArr = this.f3506l;
                fArr[1] = this.f3504j * min;
                fArr[0] = min * this.f3503i;
            }
            float f5 = this.f3503i;
            float[] fArr2 = this.f3506l;
            float f8 = fArr2[0];
            float f9 = fArr2[1];
            float f10 = f5 != 0.0f ? d8 / fArr2[0] : c9 / fArr2[1];
            float f11 = !Float.isNaN(f10) ? (f10 / 3.0f) + progress : progress;
            if (f11 != 0.0f && f11 != 1.0f && (i9 = this.f3497c) != 3) {
                this.f3509o.s0(i9, ((double) f11) < 0.5d ? 0.0f : 1.0f, f10);
                if (0.0f < progress && 1.0f > progress) {
                    return;
                }
            } else if (0.0f < f11 && 1.0f > f11) {
                return;
            }
            this.f3509o.setState(MotionLayout.j.FINISHED);
        } else if (action != 2) {
        } else {
            float rawY = motionEvent.getRawY() - this.f3508n;
            float rawX = motionEvent.getRawX() - this.f3507m;
            if (Math.abs((this.f3503i * rawX) + (this.f3504j * rawY)) > this.f3514u || this.f3505k) {
                float progress2 = this.f3509o.getProgress();
                if (!this.f3505k) {
                    this.f3505k = true;
                    this.f3509o.setProgress(progress2);
                }
                int i11 = this.f3498d;
                if (i11 != -1) {
                    this.f3509o.e0(i11, progress2, this.f3502h, this.f3501g, this.f3506l);
                } else {
                    float min2 = Math.min(this.f3509o.getWidth(), this.f3509o.getHeight());
                    float[] fArr3 = this.f3506l;
                    fArr3[1] = this.f3504j * min2;
                    fArr3[0] = min2 * this.f3503i;
                }
                float f12 = this.f3503i;
                float[] fArr4 = this.f3506l;
                if (Math.abs(((f12 * fArr4[0]) + (this.f3504j * fArr4[1])) * this.f3512s) < 0.01d) {
                    float[] fArr5 = this.f3506l;
                    fArr5[0] = 0.01f;
                    fArr5[1] = 0.01f;
                }
                float max = Math.max(Math.min(progress2 + (this.f3503i != 0.0f ? rawX / this.f3506l[0] : rawY / this.f3506l[1]), 1.0f), 0.0f);
                if (max != this.f3509o.getProgress()) {
                    this.f3509o.setProgress(max);
                    fVar.e(1000);
                    this.f3509o.G = this.f3503i != 0.0f ? fVar.d() / this.f3506l[0] : fVar.c() / this.f3506l[1];
                } else {
                    this.f3509o.G = 0.0f;
                }
                this.f3507m = motionEvent.getRawX();
                this.f3508n = motionEvent.getRawY();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(float f5, float f8) {
        float progress = this.f3509o.getProgress();
        if (!this.f3505k) {
            this.f3505k = true;
            this.f3509o.setProgress(progress);
        }
        this.f3509o.e0(this.f3498d, progress, this.f3502h, this.f3501g, this.f3506l);
        float f9 = this.f3503i;
        float[] fArr = this.f3506l;
        if (Math.abs((f9 * fArr[0]) + (this.f3504j * fArr[1])) < 0.01d) {
            float[] fArr2 = this.f3506l;
            fArr2[0] = 0.01f;
            fArr2[1] = 0.01f;
        }
        float f10 = this.f3503i;
        float max = Math.max(Math.min(progress + (f10 != 0.0f ? (f5 * f10) / this.f3506l[0] : (f8 * this.f3504j) / this.f3506l[1]), 1.0f), 0.0f);
        if (max != this.f3509o.getProgress()) {
            this.f3509o.setProgress(max);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n(float f5, float f8) {
        this.f3505k = false;
        float progress = this.f3509o.getProgress();
        this.f3509o.e0(this.f3498d, progress, this.f3502h, this.f3501g, this.f3506l);
        float f9 = this.f3503i;
        float[] fArr = this.f3506l;
        float f10 = fArr[0];
        float f11 = this.f3504j;
        float f12 = fArr[1];
        float f13 = f9 != 0.0f ? (f5 * f9) / fArr[0] : (f8 * f11) / fArr[1];
        if (!Float.isNaN(f13)) {
            progress += f13 / 3.0f;
        }
        if (progress != 0.0f) {
            boolean z4 = progress != 1.0f;
            int i8 = this.f3497c;
            if ((i8 != 3) && z4) {
                this.f3509o.s0(i8, ((double) progress) >= 0.5d ? 1.0f : 0.0f, f13);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(float f5, float f8) {
        this.f3507m = f5;
        this.f3508n = f8;
    }

    public void p(boolean z4) {
        if (z4) {
            float[][] fArr = f3494w;
            fArr[4] = fArr[3];
            fArr[5] = fArr[2];
            float[][] fArr2 = f3493v;
            fArr2[5] = fArr2[2];
            fArr2[6] = fArr2[1];
        } else {
            float[][] fArr3 = f3494w;
            fArr3[4] = fArr3[2];
            fArr3[5] = fArr3[3];
            float[][] fArr4 = f3493v;
            fArr4[5] = fArr4[1];
            fArr4[6] = fArr4[2];
        }
        float[][] fArr5 = f3493v;
        int i8 = this.f3495a;
        this.f3502h = fArr5[i8][0];
        this.f3501g = fArr5[i8][1];
        float[][] fArr6 = f3494w;
        int i9 = this.f3496b;
        this.f3503i = fArr6[i9][0];
        this.f3504j = fArr6[i9][1];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(float f5, float f8) {
        this.f3507m = f5;
        this.f3508n = f8;
        this.f3505k = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r() {
        View view;
        int i8 = this.f3498d;
        if (i8 != -1) {
            view = this.f3509o.findViewById(i8);
            if (view == null) {
                Log.e("TouchResponse", "cannot find TouchAnchorId @id/" + androidx.constraintlayout.motion.widget.a.b(this.f3509o.getContext(), this.f3498d));
            }
        } else {
            view = null;
        }
        if (view instanceof NestedScrollView) {
            NestedScrollView nestedScrollView = (NestedScrollView) view;
            nestedScrollView.setOnTouchListener(new a());
            nestedScrollView.setOnScrollChangeListener(new b());
        }
    }

    public String toString() {
        return this.f3503i + " , " + this.f3504j;
    }
}
