package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Fade extends Visibility {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends r {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f7446a;

        a(View view) {
            this.f7446a = view;
        }

        @Override // androidx.transition.Transition.f
        public void c(Transition transition) {
            f0.h(this.f7446a, 1.0f);
            f0.a(this.f7446a);
            transition.a0(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        private final View f7448a;

        /* renamed from: b  reason: collision with root package name */
        private boolean f7449b = false;

        b(View view) {
            this.f7448a = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            f0.h(this.f7448a, 1.0f);
            if (this.f7449b) {
                this.f7448a.setLayerType(0, null);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (androidx.core.view.c0.S(this.f7448a) && this.f7448a.getLayerType() == 0) {
                this.f7449b = true;
                this.f7448a.setLayerType(2, null);
            }
        }
    }

    public Fade() {
    }

    public Fade(int i8) {
        v0(i8);
    }

    @SuppressLint({"RestrictedApi"})
    public Fade(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, q.f7600f);
        v0(androidx.core.content.res.k.g(obtainStyledAttributes, (XmlResourceParser) attributeSet, "fadingMode", 0, p0()));
        obtainStyledAttributes.recycle();
    }

    private Animator w0(View view, float f5, float f8) {
        if (f5 == f8) {
            return null;
        }
        f0.h(view, f5);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, f0.f7553b, f8);
        ofFloat.addListener(new b(view));
        b(new a(view));
        return ofFloat;
    }

    private static float x0(u uVar, float f5) {
        Float f8;
        return (uVar == null || (f8 = (Float) uVar.f7619a.get("android:fade:transitionAlpha")) == null) ? f5 : f8.floatValue();
    }

    @Override // androidx.transition.Visibility, androidx.transition.Transition
    public void m(u uVar) {
        super.m(uVar);
        uVar.f7619a.put("android:fade:transitionAlpha", Float.valueOf(f0.c(uVar.f7620b)));
    }

    @Override // androidx.transition.Visibility
    public Animator r0(ViewGroup viewGroup, View view, u uVar, u uVar2) {
        float x02 = x0(uVar, 0.0f);
        return w0(view, x02 != 1.0f ? x02 : 0.0f, 1.0f);
    }

    @Override // androidx.transition.Visibility
    public Animator t0(ViewGroup viewGroup, View view, u uVar, u uVar2) {
        f0.e(view);
        return w0(view, x0(uVar, 1.0f), 0.0f);
    }
}
