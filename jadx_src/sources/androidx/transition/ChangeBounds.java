package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ChangeBounds extends Transition {

    /* renamed from: b0  reason: collision with root package name */
    private static final String[] f7376b0 = {"android:changeBounds:bounds", "android:changeBounds:clip", "android:changeBounds:parent", "android:changeBounds:windowX", "android:changeBounds:windowY"};

    /* renamed from: c0  reason: collision with root package name */
    private static final Property<Drawable, PointF> f7377c0 = new b(PointF.class, "boundsOrigin");

    /* renamed from: d0  reason: collision with root package name */
    private static final Property<k, PointF> f7378d0 = new c(PointF.class, "topLeft");

    /* renamed from: e0  reason: collision with root package name */
    private static final Property<k, PointF> f7379e0 = new d(PointF.class, "bottomRight");

    /* renamed from: f0  reason: collision with root package name */
    private static final Property<View, PointF> f7380f0 = new e(PointF.class, "bottomRight");

    /* renamed from: g0  reason: collision with root package name */
    private static final Property<View, PointF> f7381g0 = new f(PointF.class, "topLeft");

    /* renamed from: h0  reason: collision with root package name */
    private static final Property<View, PointF> f7382h0 = new g(PointF.class, "position");

    /* renamed from: i0  reason: collision with root package name */
    private static o f7383i0 = new o();
    private int[] Y;
    private boolean Z;

    /* renamed from: a0  reason: collision with root package name */
    private boolean f7384a0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ViewGroup f7385a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ BitmapDrawable f7386b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ View f7387c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ float f7388d;

        a(ViewGroup viewGroup, BitmapDrawable bitmapDrawable, View view, float f5) {
            this.f7385a = viewGroup;
            this.f7386b = bitmapDrawable;
            this.f7387c = view;
            this.f7388d = f5;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            f0.b(this.f7385a).b(this.f7386b);
            f0.h(this.f7387c, this.f7388d);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b extends Property<Drawable, PointF> {

        /* renamed from: a  reason: collision with root package name */
        private Rect f7390a;

        b(Class cls, String str) {
            super(cls, str);
            this.f7390a = new Rect();
        }

        @Override // android.util.Property
        /* renamed from: a */
        public PointF get(Drawable drawable) {
            drawable.copyBounds(this.f7390a);
            Rect rect = this.f7390a;
            return new PointF(rect.left, rect.top);
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(Drawable drawable, PointF pointF) {
            drawable.copyBounds(this.f7390a);
            this.f7390a.offsetTo(Math.round(pointF.x), Math.round(pointF.y));
            drawable.setBounds(this.f7390a);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c extends Property<k, PointF> {
        c(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public PointF get(k kVar) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(k kVar, PointF pointF) {
            kVar.c(pointF);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class d extends Property<k, PointF> {
        d(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public PointF get(k kVar) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(k kVar, PointF pointF) {
            kVar.a(pointF);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class e extends Property<View, PointF> {
        e(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public PointF get(View view) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(View view, PointF pointF) {
            f0.g(view, view.getLeft(), view.getTop(), Math.round(pointF.x), Math.round(pointF.y));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class f extends Property<View, PointF> {
        f(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public PointF get(View view) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(View view, PointF pointF) {
            f0.g(view, Math.round(pointF.x), Math.round(pointF.y), view.getRight(), view.getBottom());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class g extends Property<View, PointF> {
        g(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public PointF get(View view) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(View view, PointF pointF) {
            int round = Math.round(pointF.x);
            int round2 = Math.round(pointF.y);
            f0.g(view, round, round2, view.getWidth() + round, view.getHeight() + round2);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class h extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ k f7391a;
        private k mViewBounds;

        h(k kVar) {
            this.f7391a = kVar;
            this.mViewBounds = kVar;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class i extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        private boolean f7393a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f7394b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ Rect f7395c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ int f7396d;

        /* renamed from: e  reason: collision with root package name */
        final /* synthetic */ int f7397e;

        /* renamed from: f  reason: collision with root package name */
        final /* synthetic */ int f7398f;

        /* renamed from: g  reason: collision with root package name */
        final /* synthetic */ int f7399g;

        i(View view, Rect rect, int i8, int i9, int i10, int i11) {
            this.f7394b = view;
            this.f7395c = rect;
            this.f7396d = i8;
            this.f7397e = i9;
            this.f7398f = i10;
            this.f7399g = i11;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f7393a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.f7393a) {
                return;
            }
            androidx.core.view.c0.A0(this.f7394b, this.f7395c);
            f0.g(this.f7394b, this.f7396d, this.f7397e, this.f7398f, this.f7399g);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class j extends r {

        /* renamed from: a  reason: collision with root package name */
        boolean f7401a = false;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ ViewGroup f7402b;

        j(ViewGroup viewGroup) {
            this.f7402b = viewGroup;
        }

        @Override // androidx.transition.r, androidx.transition.Transition.f
        public void b(Transition transition) {
            a0.d(this.f7402b, false);
        }

        @Override // androidx.transition.Transition.f
        public void c(Transition transition) {
            if (!this.f7401a) {
                a0.d(this.f7402b, false);
            }
            transition.a0(this);
        }

        @Override // androidx.transition.r, androidx.transition.Transition.f
        public void d(Transition transition) {
            a0.d(this.f7402b, false);
            this.f7401a = true;
        }

        @Override // androidx.transition.r, androidx.transition.Transition.f
        public void e(Transition transition) {
            a0.d(this.f7402b, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class k {

        /* renamed from: a  reason: collision with root package name */
        private int f7404a;

        /* renamed from: b  reason: collision with root package name */
        private int f7405b;

        /* renamed from: c  reason: collision with root package name */
        private int f7406c;

        /* renamed from: d  reason: collision with root package name */
        private int f7407d;

        /* renamed from: e  reason: collision with root package name */
        private View f7408e;

        /* renamed from: f  reason: collision with root package name */
        private int f7409f;

        /* renamed from: g  reason: collision with root package name */
        private int f7410g;

        k(View view) {
            this.f7408e = view;
        }

        private void b() {
            f0.g(this.f7408e, this.f7404a, this.f7405b, this.f7406c, this.f7407d);
            this.f7409f = 0;
            this.f7410g = 0;
        }

        void a(PointF pointF) {
            this.f7406c = Math.round(pointF.x);
            this.f7407d = Math.round(pointF.y);
            int i8 = this.f7410g + 1;
            this.f7410g = i8;
            if (this.f7409f == i8) {
                b();
            }
        }

        void c(PointF pointF) {
            this.f7404a = Math.round(pointF.x);
            this.f7405b = Math.round(pointF.y);
            int i8 = this.f7409f + 1;
            this.f7409f = i8;
            if (i8 == this.f7410g) {
                b();
            }
        }
    }

    public ChangeBounds() {
        this.Y = new int[2];
        this.Z = false;
        this.f7384a0 = false;
    }

    @SuppressLint({"RestrictedApi"})
    public ChangeBounds(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.Y = new int[2];
        this.Z = false;
        this.f7384a0 = false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, q.f7598d);
        boolean a9 = androidx.core.content.res.k.a(obtainStyledAttributes, (XmlResourceParser) attributeSet, "resizeClip", 0, false);
        obtainStyledAttributes.recycle();
        q0(a9);
    }

    private void o0(u uVar) {
        View view = uVar.f7620b;
        if (!androidx.core.view.c0.W(view) && view.getWidth() == 0 && view.getHeight() == 0) {
            return;
        }
        uVar.f7619a.put("android:changeBounds:bounds", new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
        uVar.f7619a.put("android:changeBounds:parent", uVar.f7620b.getParent());
        if (this.f7384a0) {
            uVar.f7620b.getLocationInWindow(this.Y);
            uVar.f7619a.put("android:changeBounds:windowX", Integer.valueOf(this.Y[0]));
            uVar.f7619a.put("android:changeBounds:windowY", Integer.valueOf(this.Y[1]));
        }
        if (this.Z) {
            uVar.f7619a.put("android:changeBounds:clip", androidx.core.view.c0.w(view));
        }
    }

    private boolean p0(View view, View view2) {
        if (this.f7384a0) {
            u B = B(view, true);
            if (B == null) {
                if (view == view2) {
                    return true;
                }
            } else if (view2 == B.f7620b) {
                return true;
            }
            return false;
        }
        return true;
    }

    @Override // androidx.transition.Transition
    public String[] L() {
        return f7376b0;
    }

    @Override // androidx.transition.Transition
    public void j(u uVar) {
        o0(uVar);
    }

    @Override // androidx.transition.Transition
    public void m(u uVar) {
        o0(uVar);
    }

    public void q0(boolean z4) {
        this.Z = z4;
    }

    @Override // androidx.transition.Transition
    public Animator s(ViewGroup viewGroup, u uVar, u uVar2) {
        int i8;
        View view;
        int i9;
        Rect rect;
        ObjectAnimator objectAnimator;
        Animator c9;
        Path a9;
        Property<View, PointF> property;
        if (uVar == null || uVar2 == null) {
            return null;
        }
        Map<String, Object> map = uVar.f7619a;
        Map<String, Object> map2 = uVar2.f7619a;
        ViewGroup viewGroup2 = (ViewGroup) map.get("android:changeBounds:parent");
        ViewGroup viewGroup3 = (ViewGroup) map2.get("android:changeBounds:parent");
        if (viewGroup2 == null || viewGroup3 == null) {
            return null;
        }
        View view2 = uVar2.f7620b;
        if (!p0(viewGroup2, viewGroup3)) {
            int intValue = ((Integer) uVar.f7619a.get("android:changeBounds:windowX")).intValue();
            int intValue2 = ((Integer) uVar.f7619a.get("android:changeBounds:windowY")).intValue();
            int intValue3 = ((Integer) uVar2.f7619a.get("android:changeBounds:windowX")).intValue();
            int intValue4 = ((Integer) uVar2.f7619a.get("android:changeBounds:windowY")).intValue();
            if (intValue == intValue3 && intValue2 == intValue4) {
                return null;
            }
            viewGroup.getLocationInWindow(this.Y);
            Bitmap createBitmap = Bitmap.createBitmap(view2.getWidth(), view2.getHeight(), Bitmap.Config.ARGB_8888);
            view2.draw(new Canvas(createBitmap));
            BitmapDrawable bitmapDrawable = new BitmapDrawable(createBitmap);
            float c10 = f0.c(view2);
            f0.h(view2, 0.0f);
            f0.b(viewGroup).a(bitmapDrawable);
            PathMotion D = D();
            int[] iArr = this.Y;
            ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(bitmapDrawable, n.a(f7377c0, D.a(intValue - iArr[0], intValue2 - iArr[1], intValue3 - iArr[0], intValue4 - iArr[1])));
            ofPropertyValuesHolder.addListener(new a(viewGroup, bitmapDrawable, view2, c10));
            return ofPropertyValuesHolder;
        }
        Rect rect2 = (Rect) uVar.f7619a.get("android:changeBounds:bounds");
        Rect rect3 = (Rect) uVar2.f7619a.get("android:changeBounds:bounds");
        int i10 = rect2.left;
        int i11 = rect3.left;
        int i12 = rect2.top;
        int i13 = rect3.top;
        int i14 = rect2.right;
        int i15 = rect3.right;
        int i16 = rect2.bottom;
        int i17 = rect3.bottom;
        int i18 = i14 - i10;
        int i19 = i16 - i12;
        int i20 = i15 - i11;
        int i21 = i17 - i13;
        Rect rect4 = (Rect) uVar.f7619a.get("android:changeBounds:clip");
        Rect rect5 = (Rect) uVar2.f7619a.get("android:changeBounds:clip");
        if ((i18 == 0 || i19 == 0) && (i20 == 0 || i21 == 0)) {
            i8 = 0;
        } else {
            i8 = (i10 == i11 && i12 == i13) ? 0 : 1;
            if (i14 != i15 || i16 != i17) {
                i8++;
            }
        }
        if ((rect4 != null && !rect4.equals(rect5)) || (rect4 == null && rect5 != null)) {
            i8++;
        }
        if (i8 > 0) {
            if (this.Z) {
                view = view2;
                f0.g(view, i10, i12, Math.max(i18, i20) + i10, Math.max(i19, i21) + i12);
                ObjectAnimator a10 = (i10 == i11 && i12 == i13) ? null : l.a(view, f7382h0, D().a(i10, i12, i11, i13));
                if (rect4 == null) {
                    i9 = 0;
                    rect = new Rect(0, 0, i18, i19);
                } else {
                    i9 = 0;
                    rect = rect4;
                }
                Rect rect6 = rect5 == null ? new Rect(i9, i9, i20, i21) : rect5;
                if (rect.equals(rect6)) {
                    objectAnimator = null;
                } else {
                    androidx.core.view.c0.A0(view, rect);
                    o oVar = f7383i0;
                    Object[] objArr = new Object[2];
                    objArr[i9] = rect;
                    objArr[1] = rect6;
                    ObjectAnimator ofObject = ObjectAnimator.ofObject(view, "clipBounds", oVar, objArr);
                    ofObject.addListener(new i(view, rect5, i11, i13, i15, i17));
                    objectAnimator = ofObject;
                }
                c9 = t.c(a10, objectAnimator);
            } else {
                view = view2;
                f0.g(view, i10, i12, i14, i16);
                if (i8 == 2) {
                    if (i18 == i20 && i19 == i21) {
                        a9 = D().a(i10, i12, i11, i13);
                        property = f7382h0;
                    } else {
                        k kVar = new k(view);
                        ObjectAnimator a11 = l.a(kVar, f7378d0, D().a(i10, i12, i11, i13));
                        ObjectAnimator a12 = l.a(kVar, f7379e0, D().a(i14, i16, i15, i17));
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(a11, a12);
                        animatorSet.addListener(new h(kVar));
                        c9 = animatorSet;
                    }
                } else if (i10 == i11 && i12 == i13) {
                    a9 = D().a(i14, i16, i15, i17);
                    property = f7380f0;
                } else {
                    a9 = D().a(i10, i12, i11, i13);
                    property = f7381g0;
                }
                c9 = l.a(view, property, a9);
            }
            if (view.getParent() instanceof ViewGroup) {
                ViewGroup viewGroup4 = (ViewGroup) view.getParent();
                a0.d(viewGroup4, true);
                b(new j(viewGroup4));
            }
            return c9;
        }
        return null;
    }
}
