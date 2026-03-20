package com.google.android.material.card;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.FrameLayout;
import androidx.cardview.widget.CardView;
import k7.b;
import k7.k;
import x7.i;
import x7.m;
import x7.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MaterialCardView extends CardView implements Checkable, p {
    private static final int[] q = {16842911};

    /* renamed from: t  reason: collision with root package name */
    private static final int[] f17646t = {16842912};

    /* renamed from: w  reason: collision with root package name */
    private static final int[] f17647w = {b.T};

    /* renamed from: x  reason: collision with root package name */
    private static final int f17648x = k.f21249u;

    /* renamed from: k  reason: collision with root package name */
    private final com.google.android.material.card.a f17649k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f17650l;

    /* renamed from: m  reason: collision with root package name */
    private boolean f17651m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f17652n;

    /* renamed from: p  reason: collision with root package name */
    private a f17653p;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(MaterialCardView materialCardView, boolean z4);
    }

    public MaterialCardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, b.G);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MaterialCardView(android.content.Context r8, android.util.AttributeSet r9, int r10) {
        /*
            r7 = this;
            int r6 = com.google.android.material.card.MaterialCardView.f17648x
            android.content.Context r8 = y7.a.c(r8, r9, r10, r6)
            r7.<init>(r8, r9, r10)
            r8 = 0
            r7.f17651m = r8
            r7.f17652n = r8
            r0 = 1
            r7.f17650l = r0
            android.content.Context r0 = r7.getContext()
            int[] r2 = k7.l.f21421s4
            int[] r5 = new int[r8]
            r1 = r9
            r3 = r10
            r4 = r6
            android.content.res.TypedArray r8 = com.google.android.material.internal.m.h(r0, r1, r2, r3, r4, r5)
            com.google.android.material.card.a r0 = new com.google.android.material.card.a
            r0.<init>(r7, r9, r10, r6)
            r7.f17649k = r0
            android.content.res.ColorStateList r9 = super.getCardBackgroundColor()
            r0.H(r9)
            int r9 = super.getContentPaddingLeft()
            int r10 = super.getContentPaddingTop()
            int r1 = super.getContentPaddingRight()
            int r2 = super.getContentPaddingBottom()
            r0.U(r9, r10, r1, r2)
            r0.E(r8)
            r8.recycle()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.card.MaterialCardView.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private RectF getBoundsAsRectF() {
        RectF rectF = new RectF();
        rectF.set(this.f17649k.k().getBounds());
        return rectF;
    }

    private void j() {
        if (Build.VERSION.SDK_INT > 26) {
            this.f17649k.j();
        }
    }

    @Override // androidx.cardview.widget.CardView
    public ColorStateList getCardBackgroundColor() {
        return this.f17649k.l();
    }

    public ColorStateList getCardForegroundColor() {
        return this.f17649k.m();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getCardViewRadius() {
        return super.getRadius();
    }

    public Drawable getCheckedIcon() {
        return this.f17649k.n();
    }

    public int getCheckedIconMargin() {
        return this.f17649k.o();
    }

    public int getCheckedIconSize() {
        return this.f17649k.p();
    }

    public ColorStateList getCheckedIconTint() {
        return this.f17649k.q();
    }

    @Override // androidx.cardview.widget.CardView
    public int getContentPaddingBottom() {
        return this.f17649k.A().bottom;
    }

    @Override // androidx.cardview.widget.CardView
    public int getContentPaddingLeft() {
        return this.f17649k.A().left;
    }

    @Override // androidx.cardview.widget.CardView
    public int getContentPaddingRight() {
        return this.f17649k.A().right;
    }

    @Override // androidx.cardview.widget.CardView
    public int getContentPaddingTop() {
        return this.f17649k.A().top;
    }

    public float getProgress() {
        return this.f17649k.u();
    }

    @Override // androidx.cardview.widget.CardView
    public float getRadius() {
        return this.f17649k.s();
    }

    public ColorStateList getRippleColor() {
        return this.f17649k.v();
    }

    public m getShapeAppearanceModel() {
        return this.f17649k.w();
    }

    @Deprecated
    public int getStrokeColor() {
        return this.f17649k.x();
    }

    public ColorStateList getStrokeColorStateList() {
        return this.f17649k.y();
    }

    public int getStrokeWidth() {
        return this.f17649k.z();
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.f17651m;
    }

    public boolean k() {
        com.google.android.material.card.a aVar = this.f17649k;
        return aVar != null && aVar.D();
    }

    public boolean l() {
        return this.f17652n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(int i8, int i9, int i10, int i11) {
        super.h(i8, i9, i10, i11);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        i.f(this, this.f17649k.k());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected int[] onCreateDrawableState(int i8) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i8 + 3);
        if (k()) {
            FrameLayout.mergeDrawableStates(onCreateDrawableState, q);
        }
        if (isChecked()) {
            FrameLayout.mergeDrawableStates(onCreateDrawableState, f17646t);
        }
        if (l()) {
            FrameLayout.mergeDrawableStates(onCreateDrawableState, f17647w);
        }
        return onCreateDrawableState;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName("androidx.cardview.widget.CardView");
        accessibilityEvent.setChecked(isChecked());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("androidx.cardview.widget.CardView");
        accessibilityNodeInfo.setCheckable(k());
        accessibilityNodeInfo.setClickable(isClickable());
        accessibilityNodeInfo.setChecked(isChecked());
    }

    @Override // androidx.cardview.widget.CardView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i8, int i9) {
        super.onMeasure(i8, i9);
        this.f17649k.F(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (this.f17650l) {
            if (!this.f17649k.C()) {
                Log.i("MaterialCardView", "Setting a custom background is not supported.");
                this.f17649k.G(true);
            }
            super.setBackgroundDrawable(drawable);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBackgroundInternal(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
    }

    @Override // androidx.cardview.widget.CardView
    public void setCardBackgroundColor(int i8) {
        this.f17649k.H(ColorStateList.valueOf(i8));
    }

    @Override // androidx.cardview.widget.CardView
    public void setCardBackgroundColor(ColorStateList colorStateList) {
        this.f17649k.H(colorStateList);
    }

    @Override // androidx.cardview.widget.CardView
    public void setCardElevation(float f5) {
        super.setCardElevation(f5);
        this.f17649k.Z();
    }

    public void setCardForegroundColor(ColorStateList colorStateList) {
        this.f17649k.I(colorStateList);
    }

    public void setCheckable(boolean z4) {
        this.f17649k.J(z4);
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z4) {
        if (this.f17651m != z4) {
            toggle();
        }
    }

    public void setCheckedIcon(Drawable drawable) {
        this.f17649k.K(drawable);
    }

    public void setCheckedIconMargin(int i8) {
        this.f17649k.L(i8);
    }

    public void setCheckedIconMarginResource(int i8) {
        if (i8 != -1) {
            this.f17649k.L(getResources().getDimensionPixelSize(i8));
        }
    }

    public void setCheckedIconResource(int i8) {
        this.f17649k.K(h.a.b(getContext(), i8));
    }

    public void setCheckedIconSize(int i8) {
        this.f17649k.M(i8);
    }

    public void setCheckedIconSizeResource(int i8) {
        if (i8 != 0) {
            this.f17649k.M(getResources().getDimensionPixelSize(i8));
        }
    }

    public void setCheckedIconTint(ColorStateList colorStateList) {
        this.f17649k.N(colorStateList);
    }

    @Override // android.view.View
    public void setClickable(boolean z4) {
        super.setClickable(z4);
        com.google.android.material.card.a aVar = this.f17649k;
        if (aVar != null) {
            aVar.X();
        }
    }

    public void setDragged(boolean z4) {
        if (this.f17652n != z4) {
            this.f17652n = z4;
            refreshDrawableState();
            j();
            invalidate();
        }
    }

    @Override // androidx.cardview.widget.CardView
    public void setMaxCardElevation(float f5) {
        super.setMaxCardElevation(f5);
        this.f17649k.b0();
    }

    public void setOnCheckedChangeListener(a aVar) {
        this.f17653p = aVar;
    }

    @Override // androidx.cardview.widget.CardView
    public void setPreventCornerOverlap(boolean z4) {
        super.setPreventCornerOverlap(z4);
        this.f17649k.b0();
        this.f17649k.Y();
    }

    public void setProgress(float f5) {
        this.f17649k.P(f5);
    }

    @Override // androidx.cardview.widget.CardView
    public void setRadius(float f5) {
        super.setRadius(f5);
        this.f17649k.O(f5);
    }

    public void setRippleColor(ColorStateList colorStateList) {
        this.f17649k.Q(colorStateList);
    }

    public void setRippleColorResource(int i8) {
        this.f17649k.Q(h.a.a(getContext(), i8));
    }

    @Override // x7.p
    public void setShapeAppearanceModel(m mVar) {
        if (Build.VERSION.SDK_INT >= 21) {
            setClipToOutline(mVar.u(getBoundsAsRectF()));
        }
        this.f17649k.R(mVar);
    }

    public void setStrokeColor(int i8) {
        this.f17649k.S(ColorStateList.valueOf(i8));
    }

    public void setStrokeColor(ColorStateList colorStateList) {
        this.f17649k.S(colorStateList);
    }

    public void setStrokeWidth(int i8) {
        this.f17649k.T(i8);
    }

    @Override // androidx.cardview.widget.CardView
    public void setUseCompatPadding(boolean z4) {
        super.setUseCompatPadding(z4);
        this.f17649k.b0();
        this.f17649k.Y();
    }

    @Override // android.widget.Checkable
    public void toggle() {
        if (k() && isEnabled()) {
            this.f17651m = !this.f17651m;
            refreshDrawableState();
            j();
            a aVar = this.f17653p;
            if (aVar != null) {
                aVar.a(this, this.f17651m);
            }
        }
    }
}
