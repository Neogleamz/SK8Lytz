package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.view.View;
import androidx.transition.Transition;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class w {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a extends AnimatorListenerAdapter implements Transition.f {

        /* renamed from: a  reason: collision with root package name */
        private final View f7626a;

        /* renamed from: b  reason: collision with root package name */
        private final View f7627b;

        /* renamed from: c  reason: collision with root package name */
        private final int f7628c;

        /* renamed from: d  reason: collision with root package name */
        private final int f7629d;

        /* renamed from: e  reason: collision with root package name */
        private int[] f7630e;

        /* renamed from: f  reason: collision with root package name */
        private float f7631f;

        /* renamed from: g  reason: collision with root package name */
        private float f7632g;

        /* renamed from: h  reason: collision with root package name */
        private final float f7633h;

        /* renamed from: i  reason: collision with root package name */
        private final float f7634i;

        a(View view, View view2, int i8, int i9, float f5, float f8) {
            this.f7627b = view;
            this.f7626a = view2;
            this.f7628c = i8 - Math.round(view.getTranslationX());
            this.f7629d = i9 - Math.round(view.getTranslationY());
            this.f7633h = f5;
            this.f7634i = f8;
            int i10 = x1.b.f23767h;
            int[] iArr = (int[]) view2.getTag(i10);
            this.f7630e = iArr;
            if (iArr != null) {
                view2.setTag(i10, null);
            }
        }

        @Override // androidx.transition.Transition.f
        public void a(Transition transition) {
        }

        @Override // androidx.transition.Transition.f
        public void b(Transition transition) {
        }

        @Override // androidx.transition.Transition.f
        public void c(Transition transition) {
            this.f7627b.setTranslationX(this.f7633h);
            this.f7627b.setTranslationY(this.f7634i);
            transition.a0(this);
        }

        @Override // androidx.transition.Transition.f
        public void d(Transition transition) {
        }

        @Override // androidx.transition.Transition.f
        public void e(Transition transition) {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (this.f7630e == null) {
                this.f7630e = new int[2];
            }
            this.f7630e[0] = Math.round(this.f7628c + this.f7627b.getTranslationX());
            this.f7630e[1] = Math.round(this.f7629d + this.f7627b.getTranslationY());
            this.f7626a.setTag(x1.b.f23767h, this.f7630e);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationPause(Animator animator) {
            this.f7631f = this.f7627b.getTranslationX();
            this.f7632g = this.f7627b.getTranslationY();
            this.f7627b.setTranslationX(this.f7633h);
            this.f7627b.setTranslationY(this.f7634i);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationResume(Animator animator) {
            this.f7627b.setTranslationX(this.f7631f);
            this.f7627b.setTranslationY(this.f7632g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Animator a(View view, u uVar, int i8, int i9, float f5, float f8, float f9, float f10, TimeInterpolator timeInterpolator, Transition transition) {
        float f11;
        float f12;
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        int[] iArr = (int[]) uVar.f7620b.getTag(x1.b.f23767h);
        if (iArr != null) {
            f11 = (iArr[0] - i8) + translationX;
            f12 = (iArr[1] - i9) + translationY;
        } else {
            f11 = f5;
            f12 = f8;
        }
        int round = i8 + Math.round(f11 - translationX);
        int round2 = i9 + Math.round(f12 - translationY);
        view.setTranslationX(f11);
        view.setTranslationY(f12);
        if (f11 == f9 && f12 == f10) {
            return null;
        }
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(View.TRANSLATION_X, f11, f9), PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, f12, f10));
        a aVar = new a(view, uVar.f7620b, round, round2, translationX, translationY);
        transition.b(aVar);
        ofPropertyValuesHolder.addListener(aVar);
        androidx.transition.a.a(ofPropertyValuesHolder, aVar);
        ofPropertyValuesHolder.setInterpolator(timeInterpolator);
        return ofPropertyValuesHolder;
    }
}
