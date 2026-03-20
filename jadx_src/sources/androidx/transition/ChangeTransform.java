package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import org.xmlpull.v1.XmlPullParser;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ChangeTransform extends Transition {

    /* renamed from: b0  reason: collision with root package name */
    private static final String[] f7415b0 = {"android:changeTransform:matrix", "android:changeTransform:transforms", "android:changeTransform:parentMatrix"};

    /* renamed from: c0  reason: collision with root package name */
    private static final Property<e, float[]> f7416c0 = new a(float[].class, "nonTranslations");

    /* renamed from: d0  reason: collision with root package name */
    private static final Property<e, PointF> f7417d0 = new b(PointF.class, "translations");

    /* renamed from: e0  reason: collision with root package name */
    private static final boolean f7418e0;
    boolean Y;
    private boolean Z;

    /* renamed from: a0  reason: collision with root package name */
    private Matrix f7419a0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a extends Property<e, float[]> {
        a(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public float[] get(e eVar) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(e eVar, float[] fArr) {
            eVar.d(fArr);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b extends Property<e, PointF> {
        b(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public PointF get(e eVar) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(e eVar, PointF pointF) {
            eVar.c(pointF);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        private boolean f7420a;

        /* renamed from: b  reason: collision with root package name */
        private Matrix f7421b = new Matrix();

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ boolean f7422c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ Matrix f7423d;

        /* renamed from: e  reason: collision with root package name */
        final /* synthetic */ View f7424e;

        /* renamed from: f  reason: collision with root package name */
        final /* synthetic */ f f7425f;

        /* renamed from: g  reason: collision with root package name */
        final /* synthetic */ e f7426g;

        c(boolean z4, Matrix matrix, View view, f fVar, e eVar) {
            this.f7422c = z4;
            this.f7423d = matrix;
            this.f7424e = view;
            this.f7425f = fVar;
            this.f7426g = eVar;
        }

        private void a(Matrix matrix) {
            this.f7421b.set(matrix);
            this.f7424e.setTag(x1.b.f23768i, this.f7421b);
            this.f7425f.a(this.f7424e);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f7420a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (!this.f7420a) {
                if (this.f7422c && ChangeTransform.this.Y) {
                    a(this.f7423d);
                } else {
                    this.f7424e.setTag(x1.b.f23768i, null);
                    this.f7424e.setTag(x1.b.f23762c, null);
                }
            }
            f0.f(this.f7424e, null);
            this.f7425f.a(this.f7424e);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationPause(Animator animator) {
            a(this.f7426g.a());
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationResume(Animator animator) {
            ChangeTransform.s0(this.f7424e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d extends r {

        /* renamed from: a  reason: collision with root package name */
        private View f7428a;

        /* renamed from: b  reason: collision with root package name */
        private androidx.transition.e f7429b;

        d(View view, androidx.transition.e eVar) {
            this.f7428a = view;
            this.f7429b = eVar;
        }

        @Override // androidx.transition.r, androidx.transition.Transition.f
        public void b(Transition transition) {
            this.f7429b.setVisibility(4);
        }

        @Override // androidx.transition.Transition.f
        public void c(Transition transition) {
            transition.a0(this);
            i.b(this.f7428a);
            this.f7428a.setTag(x1.b.f23768i, null);
            this.f7428a.setTag(x1.b.f23762c, null);
        }

        @Override // androidx.transition.r, androidx.transition.Transition.f
        public void e(Transition transition) {
            this.f7429b.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e {

        /* renamed from: a  reason: collision with root package name */
        private final Matrix f7430a = new Matrix();

        /* renamed from: b  reason: collision with root package name */
        private final View f7431b;

        /* renamed from: c  reason: collision with root package name */
        private final float[] f7432c;

        /* renamed from: d  reason: collision with root package name */
        private float f7433d;

        /* renamed from: e  reason: collision with root package name */
        private float f7434e;

        e(View view, float[] fArr) {
            this.f7431b = view;
            float[] fArr2 = (float[]) fArr.clone();
            this.f7432c = fArr2;
            this.f7433d = fArr2[2];
            this.f7434e = fArr2[5];
            b();
        }

        private void b() {
            float[] fArr = this.f7432c;
            fArr[2] = this.f7433d;
            fArr[5] = this.f7434e;
            this.f7430a.setValues(fArr);
            f0.f(this.f7431b, this.f7430a);
        }

        Matrix a() {
            return this.f7430a;
        }

        void c(PointF pointF) {
            this.f7433d = pointF.x;
            this.f7434e = pointF.y;
            b();
        }

        void d(float[] fArr) {
            System.arraycopy(fArr, 0, this.f7432c, 0, fArr.length);
            b();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f {

        /* renamed from: a  reason: collision with root package name */
        final float f7435a;

        /* renamed from: b  reason: collision with root package name */
        final float f7436b;

        /* renamed from: c  reason: collision with root package name */
        final float f7437c;

        /* renamed from: d  reason: collision with root package name */
        final float f7438d;

        /* renamed from: e  reason: collision with root package name */
        final float f7439e;

        /* renamed from: f  reason: collision with root package name */
        final float f7440f;

        /* renamed from: g  reason: collision with root package name */
        final float f7441g;

        /* renamed from: h  reason: collision with root package name */
        final float f7442h;

        f(View view) {
            this.f7435a = view.getTranslationX();
            this.f7436b = view.getTranslationY();
            this.f7437c = androidx.core.view.c0.O(view);
            this.f7438d = view.getScaleX();
            this.f7439e = view.getScaleY();
            this.f7440f = view.getRotationX();
            this.f7441g = view.getRotationY();
            this.f7442h = view.getRotation();
        }

        public void a(View view) {
            ChangeTransform.u0(view, this.f7435a, this.f7436b, this.f7437c, this.f7438d, this.f7439e, this.f7440f, this.f7441g, this.f7442h);
        }

        public boolean equals(Object obj) {
            if (obj instanceof f) {
                f fVar = (f) obj;
                return fVar.f7435a == this.f7435a && fVar.f7436b == this.f7436b && fVar.f7437c == this.f7437c && fVar.f7438d == this.f7438d && fVar.f7439e == this.f7439e && fVar.f7440f == this.f7440f && fVar.f7441g == this.f7441g && fVar.f7442h == this.f7442h;
            }
            return false;
        }

        public int hashCode() {
            float f5 = this.f7435a;
            int floatToIntBits = (f5 != 0.0f ? Float.floatToIntBits(f5) : 0) * 31;
            float f8 = this.f7436b;
            int floatToIntBits2 = (floatToIntBits + (f8 != 0.0f ? Float.floatToIntBits(f8) : 0)) * 31;
            float f9 = this.f7437c;
            int floatToIntBits3 = (floatToIntBits2 + (f9 != 0.0f ? Float.floatToIntBits(f9) : 0)) * 31;
            float f10 = this.f7438d;
            int floatToIntBits4 = (floatToIntBits3 + (f10 != 0.0f ? Float.floatToIntBits(f10) : 0)) * 31;
            float f11 = this.f7439e;
            int floatToIntBits5 = (floatToIntBits4 + (f11 != 0.0f ? Float.floatToIntBits(f11) : 0)) * 31;
            float f12 = this.f7440f;
            int floatToIntBits6 = (floatToIntBits5 + (f12 != 0.0f ? Float.floatToIntBits(f12) : 0)) * 31;
            float f13 = this.f7441g;
            int floatToIntBits7 = (floatToIntBits6 + (f13 != 0.0f ? Float.floatToIntBits(f13) : 0)) * 31;
            float f14 = this.f7442h;
            return floatToIntBits7 + (f14 != 0.0f ? Float.floatToIntBits(f14) : 0);
        }
    }

    static {
        f7418e0 = Build.VERSION.SDK_INT >= 21;
    }

    public ChangeTransform() {
        this.Y = true;
        this.Z = true;
        this.f7419a0 = new Matrix();
    }

    @SuppressLint({"RestrictedApi"})
    public ChangeTransform(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.Y = true;
        this.Z = true;
        this.f7419a0 = new Matrix();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, q.f7601g);
        XmlPullParser xmlPullParser = (XmlPullParser) attributeSet;
        this.Y = androidx.core.content.res.k.a(obtainStyledAttributes, xmlPullParser, "reparentWithOverlay", 1, true);
        this.Z = androidx.core.content.res.k.a(obtainStyledAttributes, xmlPullParser, "reparent", 0, true);
        obtainStyledAttributes.recycle();
    }

    private void o0(u uVar) {
        View view = uVar.f7620b;
        if (view.getVisibility() == 8) {
            return;
        }
        uVar.f7619a.put("android:changeTransform:parent", view.getParent());
        uVar.f7619a.put("android:changeTransform:transforms", new f(view));
        Matrix matrix = view.getMatrix();
        uVar.f7619a.put("android:changeTransform:matrix", (matrix == null || matrix.isIdentity()) ? null : new Matrix(matrix));
        if (this.Z) {
            Matrix matrix2 = new Matrix();
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            f0.j(viewGroup, matrix2);
            matrix2.preTranslate(-viewGroup.getScrollX(), -viewGroup.getScrollY());
            uVar.f7619a.put("android:changeTransform:parentMatrix", matrix2);
            uVar.f7619a.put("android:changeTransform:intermediateMatrix", view.getTag(x1.b.f23768i));
            uVar.f7619a.put("android:changeTransform:intermediateParentMatrix", view.getTag(x1.b.f23762c));
        }
    }

    private void p0(ViewGroup viewGroup, u uVar, u uVar2) {
        View view = uVar2.f7620b;
        Matrix matrix = new Matrix((Matrix) uVar2.f7619a.get("android:changeTransform:parentMatrix"));
        f0.k(viewGroup, matrix);
        androidx.transition.e a9 = i.a(view, viewGroup, matrix);
        if (a9 == null) {
            return;
        }
        a9.a((ViewGroup) uVar.f7619a.get("android:changeTransform:parent"), uVar.f7620b);
        Transition transition = this;
        while (true) {
            Transition transition2 = transition.f7479x;
            if (transition2 == null) {
                break;
            }
            transition = transition2;
        }
        transition.b(new d(view, a9));
        if (f7418e0) {
            View view2 = uVar.f7620b;
            if (view2 != uVar2.f7620b) {
                f0.h(view2, 0.0f);
            }
            f0.h(view, 1.0f);
        }
    }

    private ObjectAnimator q0(u uVar, u uVar2, boolean z4) {
        Matrix matrix = (Matrix) uVar.f7619a.get("android:changeTransform:matrix");
        Matrix matrix2 = (Matrix) uVar2.f7619a.get("android:changeTransform:matrix");
        if (matrix == null) {
            matrix = k.f7578a;
        }
        if (matrix2 == null) {
            matrix2 = k.f7578a;
        }
        Matrix matrix3 = matrix2;
        if (matrix.equals(matrix3)) {
            return null;
        }
        f fVar = (f) uVar2.f7619a.get("android:changeTransform:transforms");
        View view = uVar2.f7620b;
        s0(view);
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        float[] fArr2 = new float[9];
        matrix3.getValues(fArr2);
        e eVar = new e(view, fArr);
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(eVar, PropertyValuesHolder.ofObject(f7416c0, new androidx.transition.c(new float[9]), fArr, fArr2), n.a(f7417d0, D().a(fArr[2], fArr[5], fArr2[2], fArr2[5])));
        c cVar = new c(z4, matrix3, view, fVar, eVar);
        ofPropertyValuesHolder.addListener(cVar);
        androidx.transition.a.a(ofPropertyValuesHolder, cVar);
        return ofPropertyValuesHolder;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0017, code lost:
        if (r5 == r4.f7620b) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x001a, code lost:
        if (r4 == r5) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x001d, code lost:
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x001f, code lost:
        return r1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean r0(android.view.ViewGroup r4, android.view.ViewGroup r5) {
        /*
            r3 = this;
            boolean r0 = r3.P(r4)
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L1a
            boolean r0 = r3.P(r5)
            if (r0 != 0) goto Lf
            goto L1a
        Lf:
            androidx.transition.u r4 = r3.B(r4, r1)
            if (r4 == 0) goto L1f
            android.view.View r4 = r4.f7620b
            if (r5 != r4) goto L1d
            goto L1e
        L1a:
            if (r4 != r5) goto L1d
            goto L1e
        L1d:
            r1 = r2
        L1e:
            r2 = r1
        L1f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.transition.ChangeTransform.r0(android.view.ViewGroup, android.view.ViewGroup):boolean");
    }

    static void s0(View view) {
        u0(view, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f);
    }

    private void t0(u uVar, u uVar2) {
        Matrix matrix = (Matrix) uVar2.f7619a.get("android:changeTransform:parentMatrix");
        uVar2.f7620b.setTag(x1.b.f23762c, matrix);
        Matrix matrix2 = this.f7419a0;
        matrix2.reset();
        matrix.invert(matrix2);
        Matrix matrix3 = (Matrix) uVar.f7619a.get("android:changeTransform:matrix");
        if (matrix3 == null) {
            matrix3 = new Matrix();
            uVar.f7619a.put("android:changeTransform:matrix", matrix3);
        }
        matrix3.postConcat((Matrix) uVar.f7619a.get("android:changeTransform:parentMatrix"));
        matrix3.postConcat(matrix2);
    }

    static void u0(View view, float f5, float f8, float f9, float f10, float f11, float f12, float f13, float f14) {
        view.setTranslationX(f5);
        view.setTranslationY(f8);
        androidx.core.view.c0.P0(view, f9);
        view.setScaleX(f10);
        view.setScaleY(f11);
        view.setRotationX(f12);
        view.setRotationY(f13);
        view.setRotation(f14);
    }

    @Override // androidx.transition.Transition
    public String[] L() {
        return f7415b0;
    }

    @Override // androidx.transition.Transition
    public void j(u uVar) {
        o0(uVar);
    }

    @Override // androidx.transition.Transition
    public void m(u uVar) {
        o0(uVar);
        if (f7418e0) {
            return;
        }
        ((ViewGroup) uVar.f7620b.getParent()).startViewTransition(uVar.f7620b);
    }

    @Override // androidx.transition.Transition
    public Animator s(ViewGroup viewGroup, u uVar, u uVar2) {
        if (uVar == null || uVar2 == null || !uVar.f7619a.containsKey("android:changeTransform:parent") || !uVar2.f7619a.containsKey("android:changeTransform:parent")) {
            return null;
        }
        ViewGroup viewGroup2 = (ViewGroup) uVar.f7619a.get("android:changeTransform:parent");
        boolean z4 = this.Z && !r0(viewGroup2, (ViewGroup) uVar2.f7619a.get("android:changeTransform:parent"));
        Matrix matrix = (Matrix) uVar.f7619a.get("android:changeTransform:intermediateMatrix");
        if (matrix != null) {
            uVar.f7619a.put("android:changeTransform:matrix", matrix);
        }
        Matrix matrix2 = (Matrix) uVar.f7619a.get("android:changeTransform:intermediateParentMatrix");
        if (matrix2 != null) {
            uVar.f7619a.put("android:changeTransform:parentMatrix", matrix2);
        }
        if (z4) {
            t0(uVar, uVar2);
        }
        ObjectAnimator q02 = q0(uVar, uVar2, z4);
        if (z4 && q02 != null && this.Y) {
            p0(viewGroup, uVar, uVar2);
        } else if (!f7418e0) {
            viewGroup2.endViewTransition(uVar.f7620b);
        }
        return q02;
    }
}
