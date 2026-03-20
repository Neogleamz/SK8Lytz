package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.c0;
import androidx.core.widget.k;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {

    /* renamed from: a  reason: collision with root package name */
    private final Context f18659a;

    /* renamed from: b  reason: collision with root package name */
    private final TextInputLayout f18660b;

    /* renamed from: c  reason: collision with root package name */
    private LinearLayout f18661c;

    /* renamed from: d  reason: collision with root package name */
    private int f18662d;

    /* renamed from: e  reason: collision with root package name */
    private FrameLayout f18663e;

    /* renamed from: f  reason: collision with root package name */
    private Animator f18664f;

    /* renamed from: g  reason: collision with root package name */
    private final float f18665g;

    /* renamed from: h  reason: collision with root package name */
    private int f18666h;

    /* renamed from: i  reason: collision with root package name */
    private int f18667i;

    /* renamed from: j  reason: collision with root package name */
    private CharSequence f18668j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f18669k;

    /* renamed from: l  reason: collision with root package name */
    private TextView f18670l;

    /* renamed from: m  reason: collision with root package name */
    private CharSequence f18671m;

    /* renamed from: n  reason: collision with root package name */
    private int f18672n;

    /* renamed from: o  reason: collision with root package name */
    private ColorStateList f18673o;

    /* renamed from: p  reason: collision with root package name */
    private CharSequence f18674p;
    private boolean q;

    /* renamed from: r  reason: collision with root package name */
    private TextView f18675r;

    /* renamed from: s  reason: collision with root package name */
    private int f18676s;

    /* renamed from: t  reason: collision with root package name */
    private ColorStateList f18677t;

    /* renamed from: u  reason: collision with root package name */
    private Typeface f18678u;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f18679a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ TextView f18680b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ int f18681c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ TextView f18682d;

        a(int i8, TextView textView, int i9, TextView textView2) {
            this.f18679a = i8;
            this.f18680b = textView;
            this.f18681c = i9;
            this.f18682d = textView2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            f.this.f18666h = this.f18679a;
            f.this.f18664f = null;
            TextView textView = this.f18680b;
            if (textView != null) {
                textView.setVisibility(4);
                if (this.f18681c == 1 && f.this.f18670l != null) {
                    f.this.f18670l.setText((CharSequence) null);
                }
            }
            TextView textView2 = this.f18682d;
            if (textView2 != null) {
                textView2.setTranslationY(0.0f);
                this.f18682d.setAlpha(1.0f);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            TextView textView = this.f18682d;
            if (textView != null) {
                textView.setVisibility(0);
            }
        }
    }

    public f(TextInputLayout textInputLayout) {
        Context context = textInputLayout.getContext();
        this.f18659a = context;
        this.f18660b = textInputLayout;
        this.f18665g = context.getResources().getDimensionPixelSize(k7.d.f21123r);
    }

    private void A(int i8, int i9) {
        TextView l8;
        TextView l9;
        if (i8 == i9) {
            return;
        }
        if (i9 != 0 && (l9 = l(i9)) != null) {
            l9.setVisibility(0);
            l9.setAlpha(1.0f);
        }
        if (i8 != 0 && (l8 = l(i8)) != null) {
            l8.setVisibility(4);
            if (i8 == 1) {
                l8.setText((CharSequence) null);
            }
        }
        this.f18666h = i9;
    }

    private void I(TextView textView, Typeface typeface) {
        if (textView != null) {
            textView.setTypeface(typeface);
        }
    }

    private void K(ViewGroup viewGroup, int i8) {
        if (i8 == 0) {
            viewGroup.setVisibility(8);
        }
    }

    private boolean L(TextView textView, CharSequence charSequence) {
        return c0.W(this.f18660b) && this.f18660b.isEnabled() && !(this.f18667i == this.f18666h && textView != null && TextUtils.equals(textView.getText(), charSequence));
    }

    private void O(int i8, int i9, boolean z4) {
        if (i8 == i9) {
            return;
        }
        if (z4) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.f18664f = animatorSet;
            ArrayList arrayList = new ArrayList();
            h(arrayList, this.q, this.f18675r, 2, i8, i9);
            h(arrayList, this.f18669k, this.f18670l, 1, i8, i9);
            l7.b.a(animatorSet, arrayList);
            animatorSet.addListener(new a(i9, l(i8), i8, l(i9)));
            animatorSet.start();
        } else {
            A(i8, i9);
        }
        this.f18660b.s0();
        this.f18660b.v0(z4);
        this.f18660b.F0();
    }

    private boolean f() {
        return (this.f18661c == null || this.f18660b.getEditText() == null) ? false : true;
    }

    private void h(List<Animator> list, boolean z4, TextView textView, int i8, int i9, int i10) {
        if (textView == null || !z4) {
            return;
        }
        if (i8 == i10 || i8 == i9) {
            list.add(i(textView, i10 == i8));
            if (i10 == i8) {
                list.add(j(textView));
            }
        }
    }

    private ObjectAnimator i(TextView textView, boolean z4) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(textView, View.ALPHA, z4 ? 1.0f : 0.0f);
        ofFloat.setDuration(167L);
        ofFloat.setInterpolator(l7.a.f21786a);
        return ofFloat;
    }

    private ObjectAnimator j(TextView textView) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(textView, View.TRANSLATION_Y, -this.f18665g, 0.0f);
        ofFloat.setDuration(217L);
        ofFloat.setInterpolator(l7.a.f21789d);
        return ofFloat;
    }

    private TextView l(int i8) {
        if (i8 != 1) {
            if (i8 != 2) {
                return null;
            }
            return this.f18675r;
        }
        return this.f18670l;
    }

    private int s(boolean z4, int i8, int i9) {
        return z4 ? this.f18659a.getResources().getDimensionPixelSize(i8) : i9;
    }

    private boolean v(int i8) {
        return (i8 != 1 || this.f18670l == null || TextUtils.isEmpty(this.f18668j)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void B(CharSequence charSequence) {
        this.f18671m = charSequence;
        TextView textView = this.f18670l;
        if (textView != null) {
            textView.setContentDescription(charSequence);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void C(boolean z4) {
        if (this.f18669k == z4) {
            return;
        }
        g();
        if (z4) {
            AppCompatTextView appCompatTextView = new AppCompatTextView(this.f18659a);
            this.f18670l = appCompatTextView;
            appCompatTextView.setId(k7.f.W);
            if (Build.VERSION.SDK_INT >= 17) {
                this.f18670l.setTextAlignment(5);
            }
            Typeface typeface = this.f18678u;
            if (typeface != null) {
                this.f18670l.setTypeface(typeface);
            }
            D(this.f18672n);
            E(this.f18673o);
            B(this.f18671m);
            this.f18670l.setVisibility(4);
            c0.v0(this.f18670l, 1);
            d(this.f18670l, 0);
        } else {
            t();
            z(this.f18670l, 0);
            this.f18670l = null;
            this.f18660b.s0();
            this.f18660b.F0();
        }
        this.f18669k = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void D(int i8) {
        this.f18672n = i8;
        TextView textView = this.f18670l;
        if (textView != null) {
            this.f18660b.e0(textView, i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void E(ColorStateList colorStateList) {
        this.f18673o = colorStateList;
        TextView textView = this.f18670l;
        if (textView == null || colorStateList == null) {
            return;
        }
        textView.setTextColor(colorStateList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void F(int i8) {
        this.f18676s = i8;
        TextView textView = this.f18675r;
        if (textView != null) {
            k.q(textView, i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void G(boolean z4) {
        if (this.q == z4) {
            return;
        }
        g();
        if (z4) {
            AppCompatTextView appCompatTextView = new AppCompatTextView(this.f18659a);
            this.f18675r = appCompatTextView;
            appCompatTextView.setId(k7.f.X);
            if (Build.VERSION.SDK_INT >= 17) {
                this.f18675r.setTextAlignment(5);
            }
            Typeface typeface = this.f18678u;
            if (typeface != null) {
                this.f18675r.setTypeface(typeface);
            }
            this.f18675r.setVisibility(4);
            c0.v0(this.f18675r, 1);
            F(this.f18676s);
            H(this.f18677t);
            d(this.f18675r, 1);
        } else {
            u();
            z(this.f18675r, 1);
            this.f18675r = null;
            this.f18660b.s0();
            this.f18660b.F0();
        }
        this.q = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void H(ColorStateList colorStateList) {
        this.f18677t = colorStateList;
        TextView textView = this.f18675r;
        if (textView == null || colorStateList == null) {
            return;
        }
        textView.setTextColor(colorStateList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J(Typeface typeface) {
        if (typeface != this.f18678u) {
            this.f18678u = typeface;
            I(this.f18670l, typeface);
            I(this.f18675r, typeface);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void M(CharSequence charSequence) {
        g();
        this.f18668j = charSequence;
        this.f18670l.setText(charSequence);
        int i8 = this.f18666h;
        if (i8 != 1) {
            this.f18667i = 1;
        }
        O(i8, this.f18667i, L(this.f18670l, charSequence));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void N(CharSequence charSequence) {
        g();
        this.f18674p = charSequence;
        this.f18675r.setText(charSequence);
        int i8 = this.f18666h;
        if (i8 != 2) {
            this.f18667i = 2;
        }
        O(i8, this.f18667i, L(this.f18675r, charSequence));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(TextView textView, int i8) {
        if (this.f18661c == null && this.f18663e == null) {
            LinearLayout linearLayout = new LinearLayout(this.f18659a);
            this.f18661c = linearLayout;
            linearLayout.setOrientation(0);
            this.f18660b.addView(this.f18661c, -1, -2);
            this.f18663e = new FrameLayout(this.f18659a);
            this.f18661c.addView(this.f18663e, new LinearLayout.LayoutParams(0, -2, 1.0f));
            if (this.f18660b.getEditText() != null) {
                e();
            }
        }
        if (w(i8)) {
            this.f18663e.setVisibility(0);
            this.f18663e.addView(textView);
        } else {
            this.f18661c.addView(textView, new LinearLayout.LayoutParams(-2, -2));
        }
        this.f18661c.setVisibility(0);
        this.f18662d++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        if (f()) {
            EditText editText = this.f18660b.getEditText();
            boolean g8 = u7.c.g(this.f18659a);
            LinearLayout linearLayout = this.f18661c;
            int i8 = k7.d.D;
            c0.J0(linearLayout, s(g8, i8, c0.J(editText)), s(g8, k7.d.E, this.f18659a.getResources().getDimensionPixelSize(k7.d.C)), s(g8, i8, c0.I(editText)), 0);
        }
    }

    void g() {
        Animator animator = this.f18664f;
        if (animator != null) {
            animator.cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean k() {
        return v(this.f18667i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence m() {
        return this.f18671m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence n() {
        return this.f18668j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int o() {
        TextView textView = this.f18670l;
        if (textView != null) {
            return textView.getCurrentTextColor();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList p() {
        TextView textView = this.f18670l;
        if (textView != null) {
            return textView.getTextColors();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence q() {
        return this.f18674p;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int r() {
        TextView textView = this.f18675r;
        if (textView != null) {
            return textView.getCurrentTextColor();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t() {
        this.f18668j = null;
        g();
        if (this.f18666h == 1) {
            this.f18667i = (!this.q || TextUtils.isEmpty(this.f18674p)) ? 0 : 2;
        }
        O(this.f18666h, this.f18667i, L(this.f18670l, null));
    }

    void u() {
        g();
        int i8 = this.f18666h;
        if (i8 == 2) {
            this.f18667i = 0;
        }
        O(i8, this.f18667i, L(this.f18675r, null));
    }

    boolean w(int i8) {
        return i8 == 0 || i8 == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean x() {
        return this.f18669k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean y() {
        return this.q;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void z(TextView textView, int i8) {
        FrameLayout frameLayout;
        if (this.f18661c == null) {
            return;
        }
        if (!w(i8) || (frameLayout = this.f18663e) == null) {
            this.f18661c.removeView(textView);
        } else {
            frameLayout.removeView(textView);
        }
        int i9 = this.f18662d - 1;
        this.f18662d = i9;
        K(this.f18661c, i9);
    }
}
