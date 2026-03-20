package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.view.View;
import java.util.ArrayList;
import x7.h;
import x7.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class e extends d {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends h {
        a(m mVar) {
            super(mVar);
        }

        @Override // x7.h, android.graphics.drawable.Drawable
        public boolean isStateful() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(FloatingActionButton floatingActionButton, w7.b bVar) {
        super(floatingActionButton, bVar);
    }

    private Animator j0(float f5, float f8) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ObjectAnimator.ofFloat(this.f17985y, "elevation", f5).setDuration(0L)).with(ObjectAnimator.ofFloat(this.f17985y, View.TRANSLATION_Z, f8).setDuration(100L));
        animatorSet.setInterpolator(d.F);
        return animatorSet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.floatingactionbutton.d
    public void A() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.floatingactionbutton.d
    public void C() {
        f0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.floatingactionbutton.d
    public void E(int[] iArr) {
        FloatingActionButton floatingActionButton;
        if (Build.VERSION.SDK_INT == 21) {
            float f5 = 0.0f;
            if (this.f17985y.isEnabled()) {
                this.f17985y.setElevation(this.f17969h);
                if (this.f17985y.isPressed()) {
                    floatingActionButton = this.f17985y;
                    f5 = this.f17971j;
                } else if (this.f17985y.isFocused() || this.f17985y.isHovered()) {
                    floatingActionButton = this.f17985y;
                    f5 = this.f17970i;
                }
                floatingActionButton.setTranslationZ(f5);
            }
            this.f17985y.setElevation(0.0f);
            floatingActionButton = this.f17985y;
            floatingActionButton.setTranslationZ(f5);
        }
    }

    @Override // com.google.android.material.floatingactionbutton.d
    void F(float f5, float f8, float f9) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 == 21) {
            this.f17985y.refreshDrawableState();
        } else {
            StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(d.G, j0(f5, f9));
            stateListAnimator.addState(d.H, j0(f5, f8));
            stateListAnimator.addState(d.I, j0(f5, f8));
            stateListAnimator.addState(d.J, j0(f5, f8));
            AnimatorSet animatorSet = new AnimatorSet();
            ArrayList arrayList = new ArrayList();
            arrayList.add(ObjectAnimator.ofFloat(this.f17985y, "elevation", f5).setDuration(0L));
            if (i8 >= 22 && i8 <= 24) {
                FloatingActionButton floatingActionButton = this.f17985y;
                arrayList.add(ObjectAnimator.ofFloat(floatingActionButton, View.TRANSLATION_Z, floatingActionButton.getTranslationZ()).setDuration(100L));
            }
            arrayList.add(ObjectAnimator.ofFloat(this.f17985y, View.TRANSLATION_Z, 0.0f).setDuration(100L));
            animatorSet.playSequentially((Animator[]) arrayList.toArray(new Animator[0]));
            animatorSet.setInterpolator(d.F);
            stateListAnimator.addState(d.K, animatorSet);
            stateListAnimator.addState(d.L, j0(0.0f, 0.0f));
            this.f17985y.setStateListAnimator(stateListAnimator);
        }
        if (Z()) {
            f0();
        }
    }

    @Override // com.google.android.material.floatingactionbutton.d
    boolean K() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.floatingactionbutton.d
    public void V(ColorStateList colorStateList) {
        Drawable drawable = this.f17964c;
        if (drawable instanceof RippleDrawable) {
            ((RippleDrawable) drawable).setColor(v7.b.d(colorStateList));
        } else {
            super.V(colorStateList);
        }
    }

    @Override // com.google.android.material.floatingactionbutton.d
    boolean Z() {
        return this.f17986z.b() || !b0();
    }

    @Override // com.google.android.material.floatingactionbutton.d
    void d0() {
    }

    c i0(int i8, ColorStateList colorStateList) {
        Context context = this.f17985y.getContext();
        c cVar = new c((m) androidx.core.util.h.h(this.f17962a));
        cVar.e(androidx.core.content.a.d(context, k7.c.f21079f), androidx.core.content.a.d(context, k7.c.f21078e), androidx.core.content.a.d(context, k7.c.f21076c), androidx.core.content.a.d(context, k7.c.f21077d));
        cVar.d(i8);
        cVar.c(colorStateList);
        return cVar;
    }

    @Override // com.google.android.material.floatingactionbutton.d
    h j() {
        return new a((m) androidx.core.util.h.h(this.f17962a));
    }

    @Override // com.google.android.material.floatingactionbutton.d
    public float n() {
        return this.f17985y.getElevation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.floatingactionbutton.d
    public void s(Rect rect) {
        if (this.f17986z.b()) {
            super.s(rect);
            return;
        }
        int sizeDimension = !b0() ? (this.f17972k - this.f17985y.getSizeDimension()) / 2 : 0;
        rect.set(sizeDimension, sizeDimension, sizeDimension, sizeDimension);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.floatingactionbutton.d
    public void x(ColorStateList colorStateList, PorterDuff.Mode mode, ColorStateList colorStateList2, int i8) {
        Drawable drawable;
        h j8 = j();
        this.f17963b = j8;
        j8.setTintList(colorStateList);
        if (mode != null) {
            this.f17963b.setTintMode(mode);
        }
        this.f17963b.P(this.f17985y.getContext());
        if (i8 > 0) {
            this.f17965d = i0(i8, colorStateList);
            drawable = new LayerDrawable(new Drawable[]{(Drawable) androidx.core.util.h.h(this.f17965d), (Drawable) androidx.core.util.h.h(this.f17963b)});
        } else {
            this.f17965d = null;
            drawable = this.f17963b;
        }
        RippleDrawable rippleDrawable = new RippleDrawable(v7.b.d(colorStateList2), drawable, null);
        this.f17964c = rippleDrawable;
        this.f17966e = rippleDrawable;
    }
}
