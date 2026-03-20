package com.google.android.material.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.Property;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.c0;
import com.example.seedpoint.R;
import com.google.android.material.circularreveal.c;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import k7.f;
import l7.h;
import l7.i;
import l7.j;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class FabTransformationBehavior extends ExpandableTransformationBehavior {

    /* renamed from: c  reason: collision with root package name */
    private final Rect f18745c;

    /* renamed from: d  reason: collision with root package name */
    private final RectF f18746d;

    /* renamed from: e  reason: collision with root package name */
    private final RectF f18747e;

    /* renamed from: f  reason: collision with root package name */
    private final int[] f18748f;

    /* renamed from: g  reason: collision with root package name */
    private float f18749g;

    /* renamed from: h  reason: collision with root package name */
    private float f18750h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ boolean f18751a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f18752b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ View f18753c;

        a(boolean z4, View view, View view2) {
            this.f18751a = z4;
            this.f18752b = view;
            this.f18753c = view2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.f18751a) {
                return;
            }
            this.f18752b.setVisibility(4);
            this.f18753c.setAlpha(1.0f);
            this.f18753c.setVisibility(0);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (this.f18751a) {
                this.f18752b.setVisibility(0);
                this.f18753c.setAlpha(0.0f);
                this.f18753c.setVisibility(4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f18755a;

        b(View view) {
            this.f18755a = view;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.f18755a.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ com.google.android.material.circularreveal.c f18757a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Drawable f18758b;

        c(com.google.android.material.circularreveal.c cVar, Drawable drawable) {
            this.f18757a = cVar;
            this.f18758b = drawable;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f18757a.setCircularRevealOverlayDrawable(null);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.f18757a.setCircularRevealOverlayDrawable(this.f18758b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ com.google.android.material.circularreveal.c f18760a;

        d(com.google.android.material.circularreveal.c cVar) {
            this.f18760a = cVar;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            c.e revealInfo = this.f18760a.getRevealInfo();
            revealInfo.f17756c = Float.MAX_VALUE;
            this.f18760a.setRevealInfo(revealInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e {

        /* renamed from: a  reason: collision with root package name */
        public h f18762a;

        /* renamed from: b  reason: collision with root package name */
        public j f18763b;
    }

    public FabTransformationBehavior() {
        this.f18745c = new Rect();
        this.f18746d = new RectF();
        this.f18747e = new RectF();
        this.f18748f = new int[2];
    }

    public FabTransformationBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f18745c = new Rect();
        this.f18746d = new RectF();
        this.f18747e = new RectF();
        this.f18748f = new int[2];
    }

    private ViewGroup K(View view) {
        View findViewById = view.findViewById(f.D);
        return findViewById != null ? f0(findViewById) : ((view instanceof TransformationChildLayout) || (view instanceof TransformationChildCard)) ? f0(((ViewGroup) view).getChildAt(0)) : f0(view);
    }

    private void L(View view, e eVar, i iVar, i iVar2, float f5, float f8, float f9, float f10, RectF rectF) {
        float S = S(eVar, iVar, f5, f9);
        float S2 = S(eVar, iVar2, f8, f10);
        Rect rect = this.f18745c;
        view.getWindowVisibleDisplayFrame(rect);
        RectF rectF2 = this.f18746d;
        rectF2.set(rect);
        RectF rectF3 = this.f18747e;
        T(view, rectF3);
        rectF3.offset(S, S2);
        rectF3.intersect(rectF2);
        rectF.set(rectF3);
    }

    private void M(View view, RectF rectF) {
        T(view, rectF);
        rectF.offset(this.f18749g, this.f18750h);
    }

    private Pair<i, i> N(float f5, float f8, boolean z4, e eVar) {
        i h8;
        h hVar;
        String str;
        int i8;
        if (f5 == 0.0f || f8 == 0.0f) {
            h8 = eVar.f18762a.h("translationXLinear");
            hVar = eVar.f18762a;
            str = "translationYLinear";
        } else if ((!z4 || f8 >= 0.0f) && (z4 || i8 <= 0)) {
            h8 = eVar.f18762a.h("translationXCurveDownwards");
            hVar = eVar.f18762a;
            str = "translationYCurveDownwards";
        } else {
            h8 = eVar.f18762a.h("translationXCurveUpwards");
            hVar = eVar.f18762a;
            str = "translationYCurveUpwards";
        }
        return new Pair<>(h8, hVar.h(str));
    }

    private float O(View view, View view2, j jVar) {
        RectF rectF = this.f18746d;
        RectF rectF2 = this.f18747e;
        M(view, rectF);
        T(view2, rectF2);
        rectF2.offset(-Q(view, view2, jVar), 0.0f);
        return rectF.centerX() - rectF2.left;
    }

    private float P(View view, View view2, j jVar) {
        RectF rectF = this.f18746d;
        RectF rectF2 = this.f18747e;
        M(view, rectF);
        T(view2, rectF2);
        rectF2.offset(0.0f, -R(view, view2, jVar));
        return rectF.centerY() - rectF2.top;
    }

    private float Q(View view, View view2, j jVar) {
        float centerX;
        float centerX2;
        float f5;
        RectF rectF = this.f18746d;
        RectF rectF2 = this.f18747e;
        M(view, rectF);
        T(view2, rectF2);
        int i8 = jVar.f21806a & 7;
        if (i8 == 1) {
            centerX = rectF2.centerX();
            centerX2 = rectF.centerX();
        } else if (i8 == 3) {
            centerX = rectF2.left;
            centerX2 = rectF.left;
        } else if (i8 != 5) {
            f5 = 0.0f;
            return f5 + jVar.f21807b;
        } else {
            centerX = rectF2.right;
            centerX2 = rectF.right;
        }
        f5 = centerX - centerX2;
        return f5 + jVar.f21807b;
    }

    private float R(View view, View view2, j jVar) {
        float centerY;
        float centerY2;
        float f5;
        RectF rectF = this.f18746d;
        RectF rectF2 = this.f18747e;
        M(view, rectF);
        T(view2, rectF2);
        int i8 = jVar.f21806a & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
        if (i8 == 16) {
            centerY = rectF2.centerY();
            centerY2 = rectF.centerY();
        } else if (i8 == 48) {
            centerY = rectF2.top;
            centerY2 = rectF.top;
        } else if (i8 != 80) {
            f5 = 0.0f;
            return f5 + jVar.f21808c;
        } else {
            centerY = rectF2.bottom;
            centerY2 = rectF.bottom;
        }
        f5 = centerY - centerY2;
        return f5 + jVar.f21808c;
    }

    private float S(e eVar, i iVar, float f5, float f8) {
        long c9 = iVar.c();
        long d8 = iVar.d();
        i h8 = eVar.f18762a.h("expansion");
        return l7.a.a(f5, f8, iVar.e().getInterpolation(((float) (((h8.c() + h8.d()) + 17) - c9)) / ((float) d8)));
    }

    private void T(View view, RectF rectF) {
        rectF.set(0.0f, 0.0f, view.getWidth(), view.getHeight());
        int[] iArr = this.f18748f;
        view.getLocationInWindow(iArr);
        rectF.offsetTo(iArr[0], iArr[1]);
        rectF.offset((int) (-view.getTranslationX()), (int) (-view.getTranslationY()));
    }

    private void U(View view, View view2, boolean z4, boolean z8, e eVar, List<Animator> list, List<Animator.AnimatorListener> list2) {
        ViewGroup K;
        ObjectAnimator ofFloat;
        if (view2 instanceof ViewGroup) {
            if (((view2 instanceof com.google.android.material.circularreveal.c) && com.google.android.material.circularreveal.b.f17740j == 0) || (K = K(view2)) == null) {
                return;
            }
            if (z4) {
                if (!z8) {
                    l7.d.f21792a.set(K, Float.valueOf(0.0f));
                }
                ofFloat = ObjectAnimator.ofFloat(K, l7.d.f21792a, 1.0f);
            } else {
                ofFloat = ObjectAnimator.ofFloat(K, l7.d.f21792a, 0.0f);
            }
            eVar.f18762a.h("contentFade").a(ofFloat);
            list.add(ofFloat);
        }
    }

    private void V(View view, View view2, boolean z4, boolean z8, e eVar, List<Animator> list, List<Animator.AnimatorListener> list2) {
        ObjectAnimator ofInt;
        if (view2 instanceof com.google.android.material.circularreveal.c) {
            com.google.android.material.circularreveal.c cVar = (com.google.android.material.circularreveal.c) view2;
            int d02 = d0(view);
            int i8 = 16777215 & d02;
            if (z4) {
                if (!z8) {
                    cVar.setCircularRevealScrimColor(d02);
                }
                ofInt = ObjectAnimator.ofInt(cVar, c.d.f17753a, i8);
            } else {
                ofInt = ObjectAnimator.ofInt(cVar, c.d.f17753a, d02);
            }
            ofInt.setEvaluator(l7.c.b());
            eVar.f18762a.h("color").a(ofInt);
            list.add(ofInt);
        }
    }

    private void W(View view, View view2, boolean z4, e eVar, List<Animator> list) {
        float Q = Q(view, view2, eVar.f18763b);
        float R = R(view, view2, eVar.f18763b);
        Pair<i, i> N = N(Q, R, z4, eVar);
        i iVar = (i) N.first;
        i iVar2 = (i) N.second;
        Property property = View.TRANSLATION_X;
        float[] fArr = new float[1];
        if (!z4) {
            Q = this.f18749g;
        }
        fArr[0] = Q;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, property, fArr);
        Property property2 = View.TRANSLATION_Y;
        float[] fArr2 = new float[1];
        if (!z4) {
            R = this.f18750h;
        }
        fArr2[0] = R;
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, property2, fArr2);
        iVar.a(ofFloat);
        iVar2.a(ofFloat2);
        list.add(ofFloat);
        list.add(ofFloat2);
    }

    @TargetApi(21)
    private void X(View view, View view2, boolean z4, boolean z8, e eVar, List<Animator> list, List<Animator.AnimatorListener> list2) {
        ObjectAnimator ofFloat;
        float y8 = c0.y(view2) - c0.y(view);
        if (z4) {
            if (!z8) {
                view2.setTranslationZ(-y8);
            }
            ofFloat = ObjectAnimator.ofFloat(view2, View.TRANSLATION_Z, 0.0f);
        } else {
            ofFloat = ObjectAnimator.ofFloat(view2, View.TRANSLATION_Z, -y8);
        }
        eVar.f18762a.h("elevation").a(ofFloat);
        list.add(ofFloat);
    }

    private void Y(View view, View view2, boolean z4, boolean z8, e eVar, float f5, float f8, List<Animator> list, List<Animator.AnimatorListener> list2) {
        Animator animator;
        if (view2 instanceof com.google.android.material.circularreveal.c) {
            com.google.android.material.circularreveal.c cVar = (com.google.android.material.circularreveal.c) view2;
            float O = O(view, view2, eVar.f18763b);
            float P = P(view, view2, eVar.f18763b);
            ((FloatingActionButton) view).i(this.f18745c);
            float width = this.f18745c.width() / 2.0f;
            i h8 = eVar.f18762a.h("expansion");
            if (z4) {
                if (!z8) {
                    cVar.setRevealInfo(new c.e(O, P, width));
                }
                if (z8) {
                    width = cVar.getRevealInfo().f17756c;
                }
                animator = com.google.android.material.circularreveal.a.a(cVar, O, P, s7.a.b(O, P, 0.0f, 0.0f, f5, f8));
                animator.addListener(new d(cVar));
                b0(view2, h8.c(), (int) O, (int) P, width, list);
            } else {
                float f9 = cVar.getRevealInfo().f17756c;
                Animator a9 = com.google.android.material.circularreveal.a.a(cVar, O, P, width);
                int i8 = (int) O;
                int i9 = (int) P;
                b0(view2, h8.c(), i8, i9, f9, list);
                a0(view2, h8.c(), h8.d(), eVar.f18762a.i(), i8, i9, width, list);
                animator = a9;
            }
            h8.a(animator);
            list.add(animator);
            list2.add(com.google.android.material.circularreveal.a.b(cVar));
        }
    }

    private void Z(View view, View view2, boolean z4, boolean z8, e eVar, List<Animator> list, List<Animator.AnimatorListener> list2) {
        ObjectAnimator ofInt;
        if ((view2 instanceof com.google.android.material.circularreveal.c) && (view instanceof ImageView)) {
            com.google.android.material.circularreveal.c cVar = (com.google.android.material.circularreveal.c) view2;
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable == null) {
                return;
            }
            drawable.mutate();
            if (z4) {
                if (!z8) {
                    drawable.setAlpha(255);
                }
                ofInt = ObjectAnimator.ofInt(drawable, l7.e.f21793b, 0);
            } else {
                ofInt = ObjectAnimator.ofInt(drawable, l7.e.f21793b, 255);
            }
            ofInt.addUpdateListener(new b(view2));
            eVar.f18762a.h("iconFade").a(ofInt);
            list.add(ofInt);
            list2.add(new c(cVar, drawable));
        }
    }

    private void a0(View view, long j8, long j9, long j10, int i8, int i9, float f5, List<Animator> list) {
        if (Build.VERSION.SDK_INT >= 21) {
            long j11 = j8 + j9;
            if (j11 < j10) {
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(view, i8, i9, f5, f5);
                createCircularReveal.setStartDelay(j11);
                createCircularReveal.setDuration(j10 - j11);
                list.add(createCircularReveal);
            }
        }
    }

    private void b0(View view, long j8, int i8, int i9, float f5, List<Animator> list) {
        if (Build.VERSION.SDK_INT < 21 || j8 <= 0) {
            return;
        }
        Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(view, i8, i9, f5, f5);
        createCircularReveal.setStartDelay(0L);
        createCircularReveal.setDuration(j8);
        list.add(createCircularReveal);
    }

    private void c0(View view, View view2, boolean z4, boolean z8, e eVar, List<Animator> list, List<Animator.AnimatorListener> list2, RectF rectF) {
        ObjectAnimator ofFloat;
        ObjectAnimator ofFloat2;
        float Q = Q(view, view2, eVar.f18763b);
        float R = R(view, view2, eVar.f18763b);
        Pair<i, i> N = N(Q, R, z4, eVar);
        i iVar = (i) N.first;
        i iVar2 = (i) N.second;
        if (z4) {
            if (!z8) {
                view2.setTranslationX(-Q);
                view2.setTranslationY(-R);
            }
            ofFloat = ObjectAnimator.ofFloat(view2, View.TRANSLATION_X, 0.0f);
            ofFloat2 = ObjectAnimator.ofFloat(view2, View.TRANSLATION_Y, 0.0f);
            L(view2, eVar, iVar, iVar2, -Q, -R, 0.0f, 0.0f, rectF);
        } else {
            ofFloat = ObjectAnimator.ofFloat(view2, View.TRANSLATION_X, -Q);
            ofFloat2 = ObjectAnimator.ofFloat(view2, View.TRANSLATION_Y, -R);
        }
        iVar.a(ofFloat);
        iVar2.a(ofFloat2);
        list.add(ofFloat);
        list.add(ofFloat2);
    }

    private int d0(View view) {
        ColorStateList u8 = c0.u(view);
        if (u8 != null) {
            return u8.getColorForState(view.getDrawableState(), u8.getDefaultColor());
        }
        return 0;
    }

    private ViewGroup f0(View view) {
        if (view instanceof ViewGroup) {
            return (ViewGroup) view;
        }
        return null;
    }

    @Override // com.google.android.material.transformation.ExpandableTransformationBehavior
    protected AnimatorSet J(View view, View view2, boolean z4, boolean z8) {
        e e02 = e0(view2.getContext(), z4);
        if (z4) {
            this.f18749g = view.getTranslationX();
            this.f18750h = view.getTranslationY();
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (Build.VERSION.SDK_INT >= 21) {
            X(view, view2, z4, z8, e02, arrayList, arrayList2);
        }
        RectF rectF = this.f18746d;
        c0(view, view2, z4, z8, e02, arrayList, arrayList2, rectF);
        float width = rectF.width();
        float height = rectF.height();
        W(view, view2, z4, e02, arrayList);
        Z(view, view2, z4, z8, e02, arrayList, arrayList2);
        Y(view, view2, z4, z8, e02, width, height, arrayList, arrayList2);
        V(view, view2, z4, z8, e02, arrayList, arrayList2);
        U(view, view2, z4, z8, e02, arrayList, arrayList2);
        AnimatorSet animatorSet = new AnimatorSet();
        l7.b.a(animatorSet, arrayList);
        animatorSet.addListener(new a(z4, view2, view));
        int size = arrayList2.size();
        for (int i8 = 0; i8 < size; i8++) {
            animatorSet.addListener(arrayList2.get(i8));
        }
        return animatorSet;
    }

    @Override // com.google.android.material.transformation.ExpandableBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean e(CoordinatorLayout coordinatorLayout, View view, View view2) {
        if (view.getVisibility() != 8) {
            if (view2 instanceof FloatingActionButton) {
                int expandedComponentIdHint = ((FloatingActionButton) view2).getExpandedComponentIdHint();
                return expandedComponentIdHint == 0 || expandedComponentIdHint == view.getId();
            }
            return false;
        }
        throw new IllegalStateException("This behavior cannot be attached to a GONE view. Set the view to INVISIBLE instead.");
    }

    protected abstract e e0(Context context, boolean z4);

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void g(CoordinatorLayout.e eVar) {
        if (eVar.f4391h == 0) {
            eVar.f4391h = 80;
        }
    }
}
