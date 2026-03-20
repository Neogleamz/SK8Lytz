package com.google.android.material.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.CompoundButton;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.c0;
import androidx.customview.view.AbsSavedState;
import java.util.Iterator;
import java.util.LinkedHashSet;
import k7.k;
import x7.i;
import x7.m;
import x7.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MaterialButton extends AppCompatButton implements Checkable, p {

    /* renamed from: w  reason: collision with root package name */
    private static final int[] f17590w = {16842911};

    /* renamed from: x  reason: collision with root package name */
    private static final int[] f17591x = {16842912};

    /* renamed from: y  reason: collision with root package name */
    private static final int f17592y = k.f21248t;

    /* renamed from: d  reason: collision with root package name */
    private final com.google.android.material.button.a f17593d;

    /* renamed from: e  reason: collision with root package name */
    private final LinkedHashSet<a> f17594e;

    /* renamed from: f  reason: collision with root package name */
    private b f17595f;

    /* renamed from: g  reason: collision with root package name */
    private PorterDuff.Mode f17596g;

    /* renamed from: h  reason: collision with root package name */
    private ColorStateList f17597h;

    /* renamed from: j  reason: collision with root package name */
    private Drawable f17598j;

    /* renamed from: k  reason: collision with root package name */
    private int f17599k;

    /* renamed from: l  reason: collision with root package name */
    private int f17600l;

    /* renamed from: m  reason: collision with root package name */
    private int f17601m;

    /* renamed from: n  reason: collision with root package name */
    private int f17602n;

    /* renamed from: p  reason: collision with root package name */
    private boolean f17603p;
    private boolean q;

    /* renamed from: t  reason: collision with root package name */
    private int f17604t;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        boolean f17605c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            if (classLoader == null) {
                getClass().getClassLoader();
            }
            b(parcel);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private void b(Parcel parcel) {
            this.f17605c = parcel.readInt() == 1;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeInt(this.f17605c ? 1 : 0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(MaterialButton materialButton, boolean z4);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface b {
        void a(MaterialButton materialButton, boolean z4);
    }

    public MaterialButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.B);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MaterialButton(android.content.Context r9, android.util.AttributeSet r10, int r11) {
        /*
            r8 = this;
            int r6 = com.google.android.material.button.MaterialButton.f17592y
            android.content.Context r9 = y7.a.c(r9, r10, r11, r6)
            r8.<init>(r9, r10, r11)
            java.util.LinkedHashSet r9 = new java.util.LinkedHashSet
            r9.<init>()
            r8.f17594e = r9
            r9 = 0
            r8.f17603p = r9
            r8.q = r9
            android.content.Context r7 = r8.getContext()
            int[] r2 = k7.l.A3
            int[] r5 = new int[r9]
            r0 = r7
            r1 = r10
            r3 = r11
            r4 = r6
            android.content.res.TypedArray r0 = com.google.android.material.internal.m.h(r0, r1, r2, r3, r4, r5)
            int r1 = k7.l.N3
            int r1 = r0.getDimensionPixelSize(r1, r9)
            r8.f17602n = r1
            int r1 = k7.l.Q3
            r2 = -1
            int r1 = r0.getInt(r1, r2)
            android.graphics.PorterDuff$Mode r2 = android.graphics.PorterDuff.Mode.SRC_IN
            android.graphics.PorterDuff$Mode r1 = com.google.android.material.internal.s.i(r1, r2)
            r8.f17596g = r1
            android.content.Context r1 = r8.getContext()
            int r2 = k7.l.P3
            android.content.res.ColorStateList r1 = u7.c.a(r1, r0, r2)
            r8.f17597h = r1
            android.content.Context r1 = r8.getContext()
            int r2 = k7.l.L3
            android.graphics.drawable.Drawable r1 = u7.c.d(r1, r0, r2)
            r8.f17598j = r1
            int r1 = k7.l.M3
            r2 = 1
            int r1 = r0.getInteger(r1, r2)
            r8.f17604t = r1
            int r1 = k7.l.O3
            int r1 = r0.getDimensionPixelSize(r1, r9)
            r8.f17599k = r1
            x7.m$b r10 = x7.m.e(r7, r10, r11, r6)
            x7.m r10 = r10.m()
            com.google.android.material.button.a r11 = new com.google.android.material.button.a
            r11.<init>(r8, r10)
            r8.f17593d = r11
            r11.q(r0)
            r0.recycle()
            int r10 = r8.f17602n
            r8.setCompoundDrawablePadding(r10)
            android.graphics.drawable.Drawable r10 = r8.f17598j
            if (r10 == 0) goto L84
            r9 = r2
        L84:
            r8.j(r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.button.MaterialButton.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private boolean c() {
        int i8 = this.f17604t;
        return i8 == 3 || i8 == 4;
    }

    private boolean d() {
        int i8 = this.f17604t;
        return i8 == 1 || i8 == 2;
    }

    private boolean e() {
        int i8 = this.f17604t;
        return i8 == 16 || i8 == 32;
    }

    private boolean f() {
        return c0.E(this) == 1;
    }

    private boolean g() {
        com.google.android.material.button.a aVar = this.f17593d;
        return (aVar == null || aVar.o()) ? false : true;
    }

    private String getA11yClassName() {
        return (b() ? CompoundButton.class : Button.class).getName();
    }

    private int getTextHeight() {
        TextPaint paint = getPaint();
        String charSequence = getText().toString();
        if (getTransformationMethod() != null) {
            charSequence = getTransformationMethod().getTransformation(charSequence, this).toString();
        }
        Rect rect = new Rect();
        paint.getTextBounds(charSequence, 0, charSequence.length(), rect);
        return Math.min(rect.height(), getLayout().getHeight());
    }

    private int getTextWidth() {
        TextPaint paint = getPaint();
        String charSequence = getText().toString();
        if (getTransformationMethod() != null) {
            charSequence = getTransformationMethod().getTransformation(charSequence, this).toString();
        }
        return Math.min((int) paint.measureText(charSequence), getLayout().getEllipsizedWidth());
    }

    private void i() {
        if (d()) {
            androidx.core.widget.k.l(this, this.f17598j, null, null, null);
        } else if (c()) {
            androidx.core.widget.k.l(this, null, null, this.f17598j, null);
        } else if (e()) {
            androidx.core.widget.k.l(this, null, this.f17598j, null, null);
        }
    }

    private void j(boolean z4) {
        Drawable drawable = this.f17598j;
        if (drawable != null) {
            Drawable mutate = androidx.core.graphics.drawable.a.r(drawable).mutate();
            this.f17598j = mutate;
            androidx.core.graphics.drawable.a.o(mutate, this.f17597h);
            PorterDuff.Mode mode = this.f17596g;
            if (mode != null) {
                androidx.core.graphics.drawable.a.p(this.f17598j, mode);
            }
            int i8 = this.f17599k;
            if (i8 == 0) {
                i8 = this.f17598j.getIntrinsicWidth();
            }
            int i9 = this.f17599k;
            if (i9 == 0) {
                i9 = this.f17598j.getIntrinsicHeight();
            }
            Drawable drawable2 = this.f17598j;
            int i10 = this.f17600l;
            int i11 = this.f17601m;
            drawable2.setBounds(i10, i11, i8 + i10, i9 + i11);
        }
        if (z4) {
            i();
            return;
        }
        Drawable[] a9 = androidx.core.widget.k.a(this);
        boolean z8 = false;
        Drawable drawable3 = a9[0];
        Drawable drawable4 = a9[1];
        Drawable drawable5 = a9[2];
        if ((d() && drawable3 != this.f17598j) || ((c() && drawable5 != this.f17598j) || (e() && drawable4 != this.f17598j))) {
            z8 = true;
        }
        if (z8) {
            i();
        }
    }

    private void k(int i8, int i9) {
        if (this.f17598j == null || getLayout() == null) {
            return;
        }
        if (!d() && !c()) {
            if (e()) {
                this.f17600l = 0;
                if (this.f17604t == 16) {
                    this.f17601m = 0;
                    j(false);
                    return;
                }
                int i10 = this.f17599k;
                if (i10 == 0) {
                    i10 = this.f17598j.getIntrinsicHeight();
                }
                int textHeight = (((((i9 - getTextHeight()) - getPaddingTop()) - i10) - this.f17602n) - getPaddingBottom()) / 2;
                if (this.f17601m != textHeight) {
                    this.f17601m = textHeight;
                    j(false);
                }
                return;
            }
            return;
        }
        this.f17601m = 0;
        int i11 = this.f17604t;
        if (i11 == 1 || i11 == 3) {
            this.f17600l = 0;
            j(false);
            return;
        }
        int i12 = this.f17599k;
        if (i12 == 0) {
            i12 = this.f17598j.getIntrinsicWidth();
        }
        int textWidth = (((((i8 - getTextWidth()) - c0.I(this)) - i12) - this.f17602n) - c0.J(this)) / 2;
        if (f() != (this.f17604t == 4)) {
            textWidth = -textWidth;
        }
        if (this.f17600l != textWidth) {
            this.f17600l = textWidth;
            j(false);
        }
    }

    public void a(a aVar) {
        this.f17594e.add(aVar);
    }

    public boolean b() {
        com.google.android.material.button.a aVar = this.f17593d;
        return aVar != null && aVar.p();
    }

    @Override // android.view.View
    public ColorStateList getBackgroundTintList() {
        return getSupportBackgroundTintList();
    }

    @Override // android.view.View
    public PorterDuff.Mode getBackgroundTintMode() {
        return getSupportBackgroundTintMode();
    }

    public int getCornerRadius() {
        if (g()) {
            return this.f17593d.b();
        }
        return 0;
    }

    public Drawable getIcon() {
        return this.f17598j;
    }

    public int getIconGravity() {
        return this.f17604t;
    }

    public int getIconPadding() {
        return this.f17602n;
    }

    public int getIconSize() {
        return this.f17599k;
    }

    public ColorStateList getIconTint() {
        return this.f17597h;
    }

    public PorterDuff.Mode getIconTintMode() {
        return this.f17596g;
    }

    public int getInsetBottom() {
        return this.f17593d.c();
    }

    public int getInsetTop() {
        return this.f17593d.d();
    }

    public ColorStateList getRippleColor() {
        if (g()) {
            return this.f17593d.h();
        }
        return null;
    }

    public m getShapeAppearanceModel() {
        if (g()) {
            return this.f17593d.i();
        }
        throw new IllegalStateException("Attempted to get ShapeAppearanceModel from a MaterialButton which has an overwritten background.");
    }

    public ColorStateList getStrokeColor() {
        if (g()) {
            return this.f17593d.j();
        }
        return null;
    }

    public int getStrokeWidth() {
        if (g()) {
            return this.f17593d.k();
        }
        return 0;
    }

    @Override // androidx.appcompat.widget.AppCompatButton, androidx.core.view.a0
    public ColorStateList getSupportBackgroundTintList() {
        return g() ? this.f17593d.l() : super.getSupportBackgroundTintList();
    }

    @Override // androidx.appcompat.widget.AppCompatButton, androidx.core.view.a0
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        return g() ? this.f17593d.m() : super.getSupportBackgroundTintMode();
    }

    public void h(a aVar) {
        this.f17594e.remove(aVar);
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.f17603p;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (g()) {
            i.f(this, this.f17593d.f());
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int i8) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i8 + 2);
        if (b()) {
            Button.mergeDrawableStates(onCreateDrawableState, f17590w);
        }
        if (isChecked()) {
            Button.mergeDrawableStates(onCreateDrawableState, f17591x);
        }
        return onCreateDrawableState;
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(getA11yClassName());
        accessibilityEvent.setChecked(isChecked());
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(getA11yClassName());
        accessibilityNodeInfo.setCheckable(b());
        accessibilityNodeInfo.setChecked(isChecked());
        accessibilityNodeInfo.setClickable(isClickable());
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.widget.TextView, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        com.google.android.material.button.a aVar;
        super.onLayout(z4, i8, i9, i10, i11);
        if (Build.VERSION.SDK_INT != 21 || (aVar = this.f17593d) == null) {
            return;
        }
        aVar.H(i11 - i9, i10 - i8);
    }

    @Override // android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.a());
        setChecked(savedState.f17605c);
    }

    @Override // android.widget.TextView, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f17605c = this.f17603p;
        return savedState;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i8, int i9, int i10, int i11) {
        super.onSizeChanged(i8, i9, i10, i11);
        k(i8, i9);
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.widget.TextView
    protected void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
        super.onTextChanged(charSequence, i8, i9, i10);
        k(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override // android.view.View
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    @Override // android.view.View
    public void setBackgroundColor(int i8) {
        if (g()) {
            this.f17593d.r(i8);
        } else {
            super.setBackgroundColor(i8);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (g()) {
            if (drawable == getBackground()) {
                getBackground().setState(drawable.getState());
                return;
            } else {
                Log.w("MaterialButton", "MaterialButton manages its own background to control elevation, shape, color and states. Consider using backgroundTint, shapeAppearance and other attributes where available. A custom background will ignore these attributes and you should consider handling interaction states such as pressed, focused and disabled");
                this.f17593d.s();
            }
        }
        super.setBackgroundDrawable(drawable);
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void setBackgroundResource(int i8) {
        setBackgroundDrawable(i8 != 0 ? h.a.b(getContext(), i8) : null);
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList colorStateList) {
        setSupportBackgroundTintList(colorStateList);
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        setSupportBackgroundTintMode(mode);
    }

    public void setCheckable(boolean z4) {
        if (g()) {
            this.f17593d.t(z4);
        }
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z4) {
        if (b() && isEnabled() && this.f17603p != z4) {
            this.f17603p = z4;
            refreshDrawableState();
            if (this.q) {
                return;
            }
            this.q = true;
            Iterator<a> it = this.f17594e.iterator();
            while (it.hasNext()) {
                it.next().a(this, this.f17603p);
            }
            this.q = false;
        }
    }

    public void setCornerRadius(int i8) {
        if (g()) {
            this.f17593d.u(i8);
        }
    }

    public void setCornerRadiusResource(int i8) {
        if (g()) {
            setCornerRadius(getResources().getDimensionPixelSize(i8));
        }
    }

    @Override // android.view.View
    public void setElevation(float f5) {
        super.setElevation(f5);
        if (g()) {
            this.f17593d.f().Z(f5);
        }
    }

    public void setIcon(Drawable drawable) {
        if (this.f17598j != drawable) {
            this.f17598j = drawable;
            j(true);
            k(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    public void setIconGravity(int i8) {
        if (this.f17604t != i8) {
            this.f17604t = i8;
            k(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    public void setIconPadding(int i8) {
        if (this.f17602n != i8) {
            this.f17602n = i8;
            setCompoundDrawablePadding(i8);
        }
    }

    public void setIconResource(int i8) {
        setIcon(i8 != 0 ? h.a.b(getContext(), i8) : null);
    }

    public void setIconSize(int i8) {
        if (i8 < 0) {
            throw new IllegalArgumentException("iconSize cannot be less than 0");
        }
        if (this.f17599k != i8) {
            this.f17599k = i8;
            j(true);
        }
    }

    public void setIconTint(ColorStateList colorStateList) {
        if (this.f17597h != colorStateList) {
            this.f17597h = colorStateList;
            j(false);
        }
    }

    public void setIconTintMode(PorterDuff.Mode mode) {
        if (this.f17596g != mode) {
            this.f17596g = mode;
            j(false);
        }
    }

    public void setIconTintResource(int i8) {
        setIconTint(h.a.a(getContext(), i8));
    }

    public void setInsetBottom(int i8) {
        this.f17593d.v(i8);
    }

    public void setInsetTop(int i8) {
        this.f17593d.w(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInternalBackground(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOnPressedChangeListenerInternal(b bVar) {
        this.f17595f = bVar;
    }

    @Override // android.view.View
    public void setPressed(boolean z4) {
        b bVar = this.f17595f;
        if (bVar != null) {
            bVar.a(this, z4);
        }
        super.setPressed(z4);
    }

    public void setRippleColor(ColorStateList colorStateList) {
        if (g()) {
            this.f17593d.x(colorStateList);
        }
    }

    public void setRippleColorResource(int i8) {
        if (g()) {
            setRippleColor(h.a.a(getContext(), i8));
        }
    }

    @Override // x7.p
    public void setShapeAppearanceModel(m mVar) {
        if (!g()) {
            throw new IllegalStateException("Attempted to set ShapeAppearanceModel on a MaterialButton which has an overwritten background.");
        }
        this.f17593d.y(mVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setShouldDrawSurfaceColorStroke(boolean z4) {
        if (g()) {
            this.f17593d.z(z4);
        }
    }

    public void setStrokeColor(ColorStateList colorStateList) {
        if (g()) {
            this.f17593d.A(colorStateList);
        }
    }

    public void setStrokeColorResource(int i8) {
        if (g()) {
            setStrokeColor(h.a.a(getContext(), i8));
        }
    }

    public void setStrokeWidth(int i8) {
        if (g()) {
            this.f17593d.B(i8);
        }
    }

    public void setStrokeWidthResource(int i8) {
        if (g()) {
            setStrokeWidth(getResources().getDimensionPixelSize(i8));
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton, androidx.core.view.a0
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        if (g()) {
            this.f17593d.C(colorStateList);
        } else {
            super.setSupportBackgroundTintList(colorStateList);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton, androidx.core.view.a0
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        if (g()) {
            this.f17593d.D(mode);
        } else {
            super.setSupportBackgroundTintMode(mode);
        }
    }

    @Override // android.widget.Checkable
    public void toggle() {
        setChecked(!this.f17603p);
    }
}
