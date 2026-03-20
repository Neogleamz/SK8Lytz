package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.g;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.a0;
import androidx.core.view.c0;
import androidx.core.widget.p;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.d;
import com.google.android.material.internal.VisibilityAwareImageButton;
import com.google.android.material.stateful.ExtendableSavedState;
import java.util.List;
import k7.k;
import k7.l;
import l7.h;
import x7.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class FloatingActionButton extends VisibilityAwareImageButton implements a0, p, r7.a, x7.p, CoordinatorLayout.b {

    /* renamed from: x  reason: collision with root package name */
    private static final int f17913x = k.f21241l;

    /* renamed from: b  reason: collision with root package name */
    private ColorStateList f17914b;

    /* renamed from: c  reason: collision with root package name */
    private PorterDuff.Mode f17915c;

    /* renamed from: d  reason: collision with root package name */
    private ColorStateList f17916d;

    /* renamed from: e  reason: collision with root package name */
    private PorterDuff.Mode f17917e;

    /* renamed from: f  reason: collision with root package name */
    private ColorStateList f17918f;

    /* renamed from: g  reason: collision with root package name */
    private int f17919g;

    /* renamed from: h  reason: collision with root package name */
    private int f17920h;

    /* renamed from: j  reason: collision with root package name */
    private int f17921j;

    /* renamed from: k  reason: collision with root package name */
    private int f17922k;

    /* renamed from: l  reason: collision with root package name */
    private int f17923l;

    /* renamed from: m  reason: collision with root package name */
    boolean f17924m;

    /* renamed from: n  reason: collision with root package name */
    final Rect f17925n;

    /* renamed from: p  reason: collision with root package name */
    private final Rect f17926p;
    private final androidx.appcompat.widget.k q;

    /* renamed from: t  reason: collision with root package name */
    private final r7.b f17927t;

    /* renamed from: w  reason: collision with root package name */
    private com.google.android.material.floatingactionbutton.d f17928w;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    protected static class BaseBehavior<T extends FloatingActionButton> extends CoordinatorLayout.Behavior<T> {

        /* renamed from: a  reason: collision with root package name */
        private Rect f17929a;

        /* renamed from: b  reason: collision with root package name */
        private b f17930b;

        /* renamed from: c  reason: collision with root package name */
        private boolean f17931c;

        public BaseBehavior() {
            this.f17931c = true;
        }

        public BaseBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, l.S2);
            this.f17931c = obtainStyledAttributes.getBoolean(l.T2, true);
            obtainStyledAttributes.recycle();
        }

        private static boolean F(View view) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.e) {
                return ((CoordinatorLayout.e) layoutParams).f() instanceof BottomSheetBehavior;
            }
            return false;
        }

        private void G(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton) {
            Rect rect = floatingActionButton.f17925n;
            if (rect == null || rect.centerX() <= 0 || rect.centerY() <= 0) {
                return;
            }
            CoordinatorLayout.e eVar = (CoordinatorLayout.e) floatingActionButton.getLayoutParams();
            int i8 = 0;
            int i9 = floatingActionButton.getRight() >= coordinatorLayout.getWidth() - ((ViewGroup.MarginLayoutParams) eVar).rightMargin ? rect.right : floatingActionButton.getLeft() <= ((ViewGroup.MarginLayoutParams) eVar).leftMargin ? -rect.left : 0;
            if (floatingActionButton.getBottom() >= coordinatorLayout.getHeight() - ((ViewGroup.MarginLayoutParams) eVar).bottomMargin) {
                i8 = rect.bottom;
            } else if (floatingActionButton.getTop() <= ((ViewGroup.MarginLayoutParams) eVar).topMargin) {
                i8 = -rect.top;
            }
            if (i8 != 0) {
                c0.d0(floatingActionButton, i8);
            }
            if (i9 != 0) {
                c0.c0(floatingActionButton, i9);
            }
        }

        private boolean J(View view, FloatingActionButton floatingActionButton) {
            return this.f17931c && ((CoordinatorLayout.e) floatingActionButton.getLayoutParams()).e() == view.getId() && floatingActionButton.getUserSetVisibility() == 0;
        }

        private boolean K(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, FloatingActionButton floatingActionButton) {
            if (J(appBarLayout, floatingActionButton)) {
                if (this.f17929a == null) {
                    this.f17929a = new Rect();
                }
                Rect rect = this.f17929a;
                com.google.android.material.internal.c.a(coordinatorLayout, appBarLayout, rect);
                if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                    floatingActionButton.m(this.f17930b, false);
                    return true;
                }
                floatingActionButton.t(this.f17930b, false);
                return true;
            }
            return false;
        }

        private boolean L(View view, FloatingActionButton floatingActionButton) {
            if (J(view, floatingActionButton)) {
                if (view.getTop() < (floatingActionButton.getHeight() / 2) + ((ViewGroup.MarginLayoutParams) ((CoordinatorLayout.e) floatingActionButton.getLayoutParams())).topMargin) {
                    floatingActionButton.m(this.f17930b, false);
                    return true;
                }
                floatingActionButton.t(this.f17930b, false);
                return true;
            }
            return false;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: E */
        public boolean b(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, Rect rect) {
            Rect rect2 = floatingActionButton.f17925n;
            rect.set(floatingActionButton.getLeft() + rect2.left, floatingActionButton.getTop() + rect2.top, floatingActionButton.getRight() - rect2.right, floatingActionButton.getBottom() - rect2.bottom);
            return true;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: H */
        public boolean h(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            if (view instanceof AppBarLayout) {
                K(coordinatorLayout, (AppBarLayout) view, floatingActionButton);
                return false;
            } else if (F(view)) {
                L(view, floatingActionButton);
                return false;
            } else {
                return false;
            }
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: I */
        public boolean l(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, int i8) {
            List<View> v8 = coordinatorLayout.v(floatingActionButton);
            int size = v8.size();
            for (int i9 = 0; i9 < size; i9++) {
                View view = v8.get(i9);
                if (!(view instanceof AppBarLayout)) {
                    if (F(view) && L(view, floatingActionButton)) {
                        break;
                    }
                } else if (K(coordinatorLayout, (AppBarLayout) view, floatingActionButton)) {
                    break;
                }
            }
            coordinatorLayout.M(floatingActionButton, i8);
            G(coordinatorLayout, floatingActionButton);
            return true;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void g(CoordinatorLayout.e eVar) {
            if (eVar.f4391h == 0) {
                eVar.f4391h = 80;
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class Behavior extends BaseBehavior<FloatingActionButton> {
        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior
        public /* bridge */ /* synthetic */ boolean E(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, Rect rect) {
            return super.b(coordinatorLayout, floatingActionButton, rect);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior
        public /* bridge */ /* synthetic */ boolean H(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            return super.h(coordinatorLayout, floatingActionButton, view);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior
        public /* bridge */ /* synthetic */ boolean I(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, int i8) {
            return super.l(coordinatorLayout, floatingActionButton, i8);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void g(CoordinatorLayout.e eVar) {
            super.g(eVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements d.j {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ b f17932a;

        a(b bVar) {
            this.f17932a = bVar;
        }

        @Override // com.google.android.material.floatingactionbutton.d.j
        public void a() {
            this.f17932a.b(FloatingActionButton.this);
        }

        @Override // com.google.android.material.floatingactionbutton.d.j
        public void b() {
            this.f17932a.a(FloatingActionButton.this);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class b {
        public void a(FloatingActionButton floatingActionButton) {
        }

        public void b(FloatingActionButton floatingActionButton) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements w7.b {
        c() {
        }

        @Override // w7.b
        public void a(int i8, int i9, int i10, int i11) {
            FloatingActionButton.this.f17925n.set(i8, i9, i10, i11);
            FloatingActionButton floatingActionButton = FloatingActionButton.this;
            floatingActionButton.setPadding(i8 + floatingActionButton.f17922k, i9 + FloatingActionButton.this.f17922k, i10 + FloatingActionButton.this.f17922k, i11 + FloatingActionButton.this.f17922k);
        }

        @Override // w7.b
        public boolean b() {
            return FloatingActionButton.this.f17924m;
        }

        @Override // w7.b
        public void c(Drawable drawable) {
            if (drawable != null) {
                FloatingActionButton.super.setBackgroundDrawable(drawable);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d<T extends FloatingActionButton> implements d.i {

        /* renamed from: a  reason: collision with root package name */
        private final l7.k<T> f17935a;

        d(l7.k<T> kVar) {
            this.f17935a = kVar;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.android.material.floatingactionbutton.d.i
        public void a() {
            this.f17935a.b(FloatingActionButton.this);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.android.material.floatingactionbutton.d.i
        public void b() {
            this.f17935a.a(FloatingActionButton.this);
        }

        public boolean equals(Object obj) {
            return (obj instanceof d) && ((d) obj).f17935a.equals(this.f17935a);
        }

        public int hashCode() {
            return this.f17935a.hashCode();
        }
    }

    public FloatingActionButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.f21072y);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public FloatingActionButton(android.content.Context r11, android.util.AttributeSet r12, int r13) {
        /*
            Method dump skipped, instructions count: 283
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.floatingactionbutton.FloatingActionButton.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private com.google.android.material.floatingactionbutton.d getImpl() {
        if (this.f17928w == null) {
            this.f17928w = h();
        }
        return this.f17928w;
    }

    private com.google.android.material.floatingactionbutton.d h() {
        return Build.VERSION.SDK_INT >= 21 ? new e(this, new c()) : new com.google.android.material.floatingactionbutton.d(this, new c());
    }

    private int k(int i8) {
        int i9 = this.f17921j;
        if (i9 != 0) {
            return i9;
        }
        Resources resources = getResources();
        if (i8 != -1) {
            return resources.getDimensionPixelSize(i8 != 1 ? k7.d.f21110k : k7.d.f21108j);
        }
        return Math.max(resources.getConfiguration().screenWidthDp, resources.getConfiguration().screenHeightDp) < 470 ? k(1) : k(0);
    }

    private void p(Rect rect) {
        int i8 = rect.left;
        Rect rect2 = this.f17925n;
        rect.left = i8 + rect2.left;
        rect.top += rect2.top;
        rect.right -= rect2.right;
        rect.bottom -= rect2.bottom;
    }

    private void q() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        ColorStateList colorStateList = this.f17916d;
        if (colorStateList == null) {
            androidx.core.graphics.drawable.a.c(drawable);
            return;
        }
        int colorForState = colorStateList.getColorForState(getDrawableState(), 0);
        PorterDuff.Mode mode = this.f17917e;
        if (mode == null) {
            mode = PorterDuff.Mode.SRC_IN;
        }
        drawable.mutate().setColorFilter(g.e(colorForState, mode));
    }

    private static int r(int i8, int i9) {
        int mode = View.MeasureSpec.getMode(i9);
        int size = View.MeasureSpec.getSize(i9);
        if (mode != Integer.MIN_VALUE) {
            if (mode != 0) {
                if (mode == 1073741824) {
                    return size;
                }
                throw new IllegalArgumentException();
            }
            return i8;
        }
        return Math.min(i8, size);
    }

    private d.j u(b bVar) {
        if (bVar == null) {
            return null;
        }
        return new a(bVar);
    }

    @Override // r7.a
    public boolean a() {
        return this.f17927t.c();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        getImpl().E(getDrawableState());
    }

    public void e(Animator.AnimatorListener animatorListener) {
        getImpl().d(animatorListener);
    }

    public void f(Animator.AnimatorListener animatorListener) {
        getImpl().e(animatorListener);
    }

    public void g(l7.k<? extends FloatingActionButton> kVar) {
        getImpl().f(new d(kVar));
    }

    @Override // android.view.View
    public ColorStateList getBackgroundTintList() {
        return this.f17914b;
    }

    @Override // android.view.View
    public PorterDuff.Mode getBackgroundTintMode() {
        return this.f17915c;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.b
    public CoordinatorLayout.Behavior<FloatingActionButton> getBehavior() {
        return new Behavior();
    }

    public float getCompatElevation() {
        return getImpl().n();
    }

    public float getCompatHoveredFocusedTranslationZ() {
        return getImpl().q();
    }

    public float getCompatPressedTranslationZ() {
        return getImpl().t();
    }

    public Drawable getContentBackground() {
        return getImpl().k();
    }

    public int getCustomSize() {
        return this.f17921j;
    }

    public int getExpandedComponentIdHint() {
        return this.f17927t.b();
    }

    public h getHideMotionSpec() {
        return getImpl().p();
    }

    @Deprecated
    public int getRippleColor() {
        ColorStateList colorStateList = this.f17918f;
        if (colorStateList != null) {
            return colorStateList.getDefaultColor();
        }
        return 0;
    }

    public ColorStateList getRippleColorStateList() {
        return this.f17918f;
    }

    public m getShapeAppearanceModel() {
        return (m) androidx.core.util.h.h(getImpl().u());
    }

    public h getShowMotionSpec() {
        return getImpl().v();
    }

    public int getSize() {
        return this.f17920h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSizeDimension() {
        return k(this.f17920h);
    }

    @Override // androidx.core.view.a0
    public ColorStateList getSupportBackgroundTintList() {
        return getBackgroundTintList();
    }

    @Override // androidx.core.view.a0
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        return getBackgroundTintMode();
    }

    @Override // androidx.core.widget.p
    public ColorStateList getSupportImageTintList() {
        return this.f17916d;
    }

    @Override // androidx.core.widget.p
    public PorterDuff.Mode getSupportImageTintMode() {
        return this.f17917e;
    }

    public boolean getUseCompatPadding() {
        return this.f17924m;
    }

    @Deprecated
    public boolean i(Rect rect) {
        if (c0.W(this)) {
            rect.set(0, 0, getWidth(), getHeight());
            p(rect);
            return true;
        }
        return false;
    }

    public void j(Rect rect) {
        rect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        p(rect);
    }

    @Override // android.widget.ImageView, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        getImpl().A();
    }

    public void l(b bVar) {
        m(bVar, true);
    }

    void m(b bVar, boolean z4) {
        getImpl().w(u(bVar), z4);
    }

    public boolean n() {
        return getImpl().y();
    }

    public boolean o() {
        return getImpl().z();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getImpl().B();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getImpl().D();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i8, int i9) {
        int sizeDimension = getSizeDimension();
        this.f17922k = (sizeDimension - this.f17923l) / 2;
        getImpl().f0();
        int min = Math.min(r(sizeDimension, i8), r(sizeDimension, i9));
        Rect rect = this.f17925n;
        setMeasuredDimension(rect.left + min + rect.right, min + rect.top + rect.bottom);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof ExtendableSavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        ExtendableSavedState extendableSavedState = (ExtendableSavedState) parcelable;
        super.onRestoreInstanceState(extendableSavedState.a());
        this.f17927t.d((Bundle) androidx.core.util.h.h(extendableSavedState.f18470c.get("expandableWidgetHelper")));
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        if (onSaveInstanceState == null) {
            onSaveInstanceState = new Bundle();
        }
        ExtendableSavedState extendableSavedState = new ExtendableSavedState(onSaveInstanceState);
        extendableSavedState.f18470c.put("expandableWidgetHelper", this.f17927t.e());
        return extendableSavedState;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && i(this.f17926p) && !this.f17926p.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
            return false;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void s(b bVar) {
        t(bVar, true);
    }

    @Override // android.view.View
    public void setBackgroundColor(int i8) {
        Log.i("FloatingActionButton", "Setting a custom background is not supported.");
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        Log.i("FloatingActionButton", "Setting a custom background is not supported.");
    }

    @Override // android.view.View
    public void setBackgroundResource(int i8) {
        Log.i("FloatingActionButton", "Setting a custom background is not supported.");
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList colorStateList) {
        if (this.f17914b != colorStateList) {
            this.f17914b = colorStateList;
            getImpl().L(colorStateList);
        }
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        if (this.f17915c != mode) {
            this.f17915c = mode;
            getImpl().M(mode);
        }
    }

    public void setCompatElevation(float f5) {
        getImpl().N(f5);
    }

    public void setCompatElevationResource(int i8) {
        setCompatElevation(getResources().getDimension(i8));
    }

    public void setCompatHoveredFocusedTranslationZ(float f5) {
        getImpl().Q(f5);
    }

    public void setCompatHoveredFocusedTranslationZResource(int i8) {
        setCompatHoveredFocusedTranslationZ(getResources().getDimension(i8));
    }

    public void setCompatPressedTranslationZ(float f5) {
        getImpl().U(f5);
    }

    public void setCompatPressedTranslationZResource(int i8) {
        setCompatPressedTranslationZ(getResources().getDimension(i8));
    }

    public void setCustomSize(int i8) {
        if (i8 < 0) {
            throw new IllegalArgumentException("Custom size must be non-negative");
        }
        if (i8 != this.f17921j) {
            this.f17921j = i8;
            requestLayout();
        }
    }

    @Override // android.view.View
    public void setElevation(float f5) {
        super.setElevation(f5);
        getImpl().g0(f5);
    }

    public void setEnsureMinTouchTargetSize(boolean z4) {
        if (z4 != getImpl().o()) {
            getImpl().O(z4);
            requestLayout();
        }
    }

    public void setExpandedComponentIdHint(int i8) {
        this.f17927t.f(i8);
    }

    public void setHideMotionSpec(h hVar) {
        getImpl().P(hVar);
    }

    public void setHideMotionSpecResource(int i8) {
        setHideMotionSpec(h.d(getContext(), i8));
    }

    @Override // android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        if (getDrawable() != drawable) {
            super.setImageDrawable(drawable);
            getImpl().e0();
            if (this.f17916d != null) {
                q();
            }
        }
    }

    @Override // android.widget.ImageView
    public void setImageResource(int i8) {
        this.q.i(i8);
        q();
    }

    public void setRippleColor(int i8) {
        setRippleColor(ColorStateList.valueOf(i8));
    }

    public void setRippleColor(ColorStateList colorStateList) {
        if (this.f17918f != colorStateList) {
            this.f17918f = colorStateList;
            getImpl().V(this.f17918f);
        }
    }

    @Override // android.view.View
    public void setScaleX(float f5) {
        super.setScaleX(f5);
        getImpl().I();
    }

    @Override // android.view.View
    public void setScaleY(float f5) {
        super.setScaleY(f5);
        getImpl().I();
    }

    public void setShadowPaddingEnabled(boolean z4) {
        getImpl().W(z4);
    }

    @Override // x7.p
    public void setShapeAppearanceModel(m mVar) {
        getImpl().X(mVar);
    }

    public void setShowMotionSpec(h hVar) {
        getImpl().Y(hVar);
    }

    public void setShowMotionSpecResource(int i8) {
        setShowMotionSpec(h.d(getContext(), i8));
    }

    public void setSize(int i8) {
        this.f17921j = 0;
        if (i8 != this.f17920h) {
            this.f17920h = i8;
            requestLayout();
        }
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        setBackgroundTintList(colorStateList);
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        setBackgroundTintMode(mode);
    }

    @Override // androidx.core.widget.p
    public void setSupportImageTintList(ColorStateList colorStateList) {
        if (this.f17916d != colorStateList) {
            this.f17916d = colorStateList;
            q();
        }
    }

    @Override // androidx.core.widget.p
    public void setSupportImageTintMode(PorterDuff.Mode mode) {
        if (this.f17917e != mode) {
            this.f17917e = mode;
            q();
        }
    }

    @Override // android.view.View
    public void setTranslationX(float f5) {
        super.setTranslationX(f5);
        getImpl().J();
    }

    @Override // android.view.View
    public void setTranslationY(float f5) {
        super.setTranslationY(f5);
        getImpl().J();
    }

    @Override // android.view.View
    public void setTranslationZ(float f5) {
        super.setTranslationZ(f5);
        getImpl().J();
    }

    public void setUseCompatPadding(boolean z4) {
        if (this.f17924m != z4) {
            this.f17924m = z4;
            getImpl().C();
        }
    }

    @Override // com.google.android.material.internal.VisibilityAwareImageButton, android.widget.ImageView, android.view.View
    public void setVisibility(int i8) {
        super.setVisibility(i8);
    }

    void t(b bVar, boolean z4) {
        getImpl().c0(u(bVar), z4);
    }
}
