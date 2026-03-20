package com.google.android.material.chip;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.view.accessibility.c;
import androidx.core.view.c0;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.material.chip.a;
import com.google.android.material.internal.m;
import com.google.android.material.internal.s;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import k7.j;
import k7.k;
import k7.l;
import l7.h;
import u7.d;
import u7.f;
import x7.i;
import x7.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Chip extends AppCompatCheckBox implements a.InterfaceC0130a, p {
    private static final int A = k.f21251w;
    private static final Rect B = new Rect();
    private static final int[] C = {16842913};
    private static final int[] E = {16842911};

    /* renamed from: e  reason: collision with root package name */
    private com.google.android.material.chip.a f17679e;

    /* renamed from: f  reason: collision with root package name */
    private InsetDrawable f17680f;

    /* renamed from: g  reason: collision with root package name */
    private RippleDrawable f17681g;

    /* renamed from: h  reason: collision with root package name */
    private View.OnClickListener f17682h;

    /* renamed from: j  reason: collision with root package name */
    private CompoundButton.OnCheckedChangeListener f17683j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f17684k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f17685l;

    /* renamed from: m  reason: collision with root package name */
    private boolean f17686m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f17687n;

    /* renamed from: p  reason: collision with root package name */
    private boolean f17688p;
    private int q;

    /* renamed from: t  reason: collision with root package name */
    private int f17689t;

    /* renamed from: w  reason: collision with root package name */
    private final c f17690w;

    /* renamed from: x  reason: collision with root package name */
    private final Rect f17691x;

    /* renamed from: y  reason: collision with root package name */
    private final RectF f17692y;

    /* renamed from: z  reason: collision with root package name */
    private final f f17693z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends f {
        a() {
        }

        @Override // u7.f
        public void a(int i8) {
        }

        @Override // u7.f
        public void b(Typeface typeface, boolean z4) {
            Chip chip = Chip.this;
            chip.setText(chip.f17679e.R2() ? Chip.this.f17679e.n1() : Chip.this.getText());
            Chip.this.requestLayout();
            Chip.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends ViewOutlineProvider {
        b() {
        }

        @Override // android.view.ViewOutlineProvider
        @TargetApi(21)
        public void getOutline(View view, Outline outline) {
            if (Chip.this.f17679e != null) {
                Chip.this.f17679e.getOutline(outline);
            } else {
                outline.setAlpha(0.0f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends w0.a {
        c(Chip chip) {
            super(chip);
        }

        @Override // w0.a
        protected int B(float f5, float f8) {
            return (Chip.this.n() && Chip.this.getCloseIconTouchBounds().contains(f5, f8)) ? 1 : 0;
        }

        @Override // w0.a
        protected void C(List<Integer> list) {
            list.add(0);
            if (Chip.this.n() && Chip.this.s() && Chip.this.f17682h != null) {
                list.add(1);
            }
        }

        @Override // w0.a
        protected boolean L(int i8, int i9, Bundle bundle) {
            if (i9 == 16) {
                if (i8 == 0) {
                    return Chip.this.performClick();
                }
                if (i8 == 1) {
                    return Chip.this.t();
                }
                return false;
            }
            return false;
        }

        @Override // w0.a
        protected void O(androidx.core.view.accessibility.c cVar) {
            cVar.a0(Chip.this.r());
            cVar.d0(Chip.this.isClickable());
            cVar.c0((Chip.this.r() || Chip.this.isClickable()) ? Chip.this.r() ? "android.widget.CompoundButton" : "android.widget.Button" : "android.view.View");
            CharSequence text = Chip.this.getText();
            if (Build.VERSION.SDK_INT >= 23) {
                cVar.E0(text);
            } else {
                cVar.g0(text);
            }
        }

        @Override // w0.a
        protected void P(int i8, androidx.core.view.accessibility.c cVar) {
            String str = BuildConfig.FLAVOR;
            if (i8 != 1) {
                cVar.g0(BuildConfig.FLAVOR);
                cVar.X(Chip.B);
                return;
            }
            CharSequence closeIconContentDescription = Chip.this.getCloseIconContentDescription();
            if (closeIconContentDescription == null) {
                CharSequence text = Chip.this.getText();
                Context context = Chip.this.getContext();
                int i9 = j.f21216l;
                Object[] objArr = new Object[1];
                if (!TextUtils.isEmpty(text)) {
                    str = text;
                }
                objArr[0] = str;
                closeIconContentDescription = context.getString(i9, objArr).trim();
            }
            cVar.g0(closeIconContentDescription);
            cVar.X(Chip.this.getCloseIconTouchBoundsInt());
            cVar.b(c.a.f4911i);
            cVar.i0(Chip.this.isEnabled());
        }

        @Override // w0.a
        protected void Q(int i8, boolean z4) {
            if (i8 == 1) {
                Chip.this.f17687n = z4;
                Chip.this.refreshDrawableState();
            }
        }
    }

    public Chip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.f21058j);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public Chip(android.content.Context r8, android.util.AttributeSet r9, int r10) {
        /*
            r7 = this;
            int r4 = com.google.android.material.chip.Chip.A
            android.content.Context r8 = y7.a.c(r8, r9, r10, r4)
            r7.<init>(r8, r9, r10)
            android.graphics.Rect r8 = new android.graphics.Rect
            r8.<init>()
            r7.f17691x = r8
            android.graphics.RectF r8 = new android.graphics.RectF
            r8.<init>()
            r7.f17692y = r8
            com.google.android.material.chip.Chip$a r8 = new com.google.android.material.chip.Chip$a
            r8.<init>()
            r7.f17693z = r8
            android.content.Context r8 = r7.getContext()
            r7.C(r9)
            com.google.android.material.chip.a r6 = com.google.android.material.chip.a.B0(r8, r9, r10, r4)
            r7.o(r8, r9, r10)
            r7.setChipDrawable(r6)
            float r0 = androidx.core.view.c0.y(r7)
            r6.Z(r0)
            int[] r2 = k7.l.C0
            r0 = 0
            int[] r5 = new int[r0]
            r0 = r8
            r1 = r9
            r3 = r10
            android.content.res.TypedArray r9 = com.google.android.material.internal.m.h(r0, r1, r2, r3, r4, r5)
            int r10 = android.os.Build.VERSION.SDK_INT
            r0 = 23
            if (r10 >= r0) goto L51
            int r10 = k7.l.F0
            android.content.res.ColorStateList r8 = u7.c.a(r8, r9, r10)
            r7.setTextColor(r8)
        L51:
            int r8 = k7.l.f21385o1
            boolean r8 = r9.hasValue(r8)
            r9.recycle()
            com.google.android.material.chip.Chip$c r9 = new com.google.android.material.chip.Chip$c
            r9.<init>(r7)
            r7.f17690w = r9
            r7.x()
            if (r8 != 0) goto L69
            r7.p()
        L69:
            boolean r8 = r7.f17684k
            r7.setChecked(r8)
            java.lang.CharSequence r8 = r6.n1()
            r7.setText(r8)
            android.text.TextUtils$TruncateAt r8 = r6.h1()
            r7.setEllipsize(r8)
            r7.B()
            com.google.android.material.chip.a r8 = r7.f17679e
            boolean r8 = r8.R2()
            if (r8 != 0) goto L8e
            r8 = 1
            r7.setLines(r8)
            r7.setHorizontallyScrolling(r8)
        L8e:
            r8 = 8388627(0x800013, float:1.175497E-38)
            r7.setGravity(r8)
            r7.A()
            boolean r8 = r7.v()
            if (r8 == 0) goto La2
            int r8 = r7.f17689t
            r7.setMinHeight(r8)
        La2:
            int r8 = androidx.core.view.c0.E(r7)
            r7.q = r8
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.chip.Chip.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private void A() {
        com.google.android.material.chip.a aVar;
        if (TextUtils.isEmpty(getText()) || (aVar = this.f17679e) == null) {
            return;
        }
        int P0 = (int) (aVar.P0() + this.f17679e.p1() + this.f17679e.w0());
        int U0 = (int) (this.f17679e.U0() + this.f17679e.q1() + this.f17679e.s0());
        if (this.f17680f != null) {
            Rect rect = new Rect();
            this.f17680f.getPadding(rect);
            U0 += rect.left;
            P0 += rect.right;
        }
        c0.J0(this, U0, getPaddingTop(), P0, getPaddingBottom());
    }

    private void B() {
        TextPaint paint = getPaint();
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            paint.drawableState = aVar.getState();
        }
        d textAppearance = getTextAppearance();
        if (textAppearance != null) {
            textAppearance.j(getContext(), paint, this.f17693z);
        }
    }

    private void C(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "background") != null) {
            Log.w("Chip", "Do not set the background; Chip manages its own background drawable.");
        }
        if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableLeft") != null) {
            throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
        }
        if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableStart") != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableEnd") != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableRight") != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        if (!attributeSet.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "singleLine", true) || attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "lines", 1) != 1 || attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "minLines", 1) != 1 || attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "maxLines", 1) != 1) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        if (attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "gravity", 8388627) != 8388627) {
            Log.w("Chip", "Chip text must be vertically center and start aligned");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RectF getCloseIconTouchBounds() {
        this.f17692y.setEmpty();
        if (n() && this.f17682h != null) {
            this.f17679e.e1(this.f17692y);
        }
        return this.f17692y;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rect getCloseIconTouchBoundsInt() {
        RectF closeIconTouchBounds = getCloseIconTouchBounds();
        this.f17691x.set((int) closeIconTouchBounds.left, (int) closeIconTouchBounds.top, (int) closeIconTouchBounds.right, (int) closeIconTouchBounds.bottom);
        return this.f17691x;
    }

    private d getTextAppearance() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.o1();
        }
        return null;
    }

    private void i(com.google.android.material.chip.a aVar) {
        aVar.v2(this);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [int, boolean] */
    private int[] j() {
        ?? isEnabled = isEnabled();
        int i8 = isEnabled;
        if (this.f17687n) {
            i8 = isEnabled + 1;
        }
        int i9 = i8;
        if (this.f17686m) {
            i9 = i8 + 1;
        }
        int i10 = i9;
        if (this.f17685l) {
            i10 = i9 + 1;
        }
        int i11 = i10;
        if (isChecked()) {
            i11 = i10 + 1;
        }
        int[] iArr = new int[i11];
        int i12 = 0;
        if (isEnabled()) {
            iArr[0] = 16842910;
            i12 = 1;
        }
        if (this.f17687n) {
            iArr[i12] = 16842908;
            i12++;
        }
        if (this.f17686m) {
            iArr[i12] = 16843623;
            i12++;
        }
        if (this.f17685l) {
            iArr[i12] = 16842919;
            i12++;
        }
        if (isChecked()) {
            iArr[i12] = 16842913;
        }
        return iArr;
    }

    private void l() {
        if (getBackgroundDrawable() == this.f17680f && this.f17679e.getCallback() == null) {
            this.f17679e.setCallback(this.f17680f);
        }
    }

    @SuppressLint({"PrivateApi"})
    private boolean m(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 10) {
            try {
                Field declaredField = w0.a.class.getDeclaredField("m");
                declaredField.setAccessible(true);
                if (((Integer) declaredField.get(this.f17690w)).intValue() != Integer.MIN_VALUE) {
                    Method declaredMethod = w0.a.class.getDeclaredMethod("X", Integer.TYPE);
                    declaredMethod.setAccessible(true);
                    declaredMethod.invoke(this.f17690w, Integer.MIN_VALUE);
                    return true;
                }
            } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException e8) {
                Log.e("Chip", "Unable to send Accessibility Exit event", e8);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean n() {
        com.google.android.material.chip.a aVar = this.f17679e;
        return (aVar == null || aVar.X0() == null) ? false : true;
    }

    private void o(Context context, AttributeSet attributeSet, int i8) {
        TypedArray h8 = m.h(context, attributeSet, l.C0, i8, A, new int[0]);
        this.f17688p = h8.getBoolean(l.f21340j1, false);
        this.f17689t = (int) Math.ceil(h8.getDimension(l.X0, (float) Math.ceil(s.c(getContext(), 48))));
        h8.recycle();
    }

    private void p() {
        if (Build.VERSION.SDK_INT >= 21) {
            setOutlineProvider(new b());
        }
    }

    private void q(int i8, int i9, int i10, int i11) {
        this.f17680f = new InsetDrawable((Drawable) this.f17679e, i8, i9, i10, i11);
    }

    private void setCloseIconHovered(boolean z4) {
        if (this.f17686m != z4) {
            this.f17686m = z4;
            refreshDrawableState();
        }
    }

    private void setCloseIconPressed(boolean z4) {
        if (this.f17685l != z4) {
            this.f17685l = z4;
            refreshDrawableState();
        }
    }

    private void u() {
        if (this.f17680f != null) {
            this.f17680f = null;
            setMinWidth(0);
            setMinHeight((int) getChipMinHeight());
            y();
        }
    }

    private void w(com.google.android.material.chip.a aVar) {
        if (aVar != null) {
            aVar.v2(null);
        }
    }

    private void x() {
        c0.t0(this, (n() && s() && this.f17682h != null) ? this.f17690w : null);
    }

    private void y() {
        if (v7.b.f23352a) {
            z();
            return;
        }
        this.f17679e.Q2(true);
        c0.x0(this, getBackgroundDrawable());
        A();
        l();
    }

    private void z() {
        this.f17681g = new RippleDrawable(v7.b.d(this.f17679e.l1()), getBackgroundDrawable(), null);
        this.f17679e.Q2(false);
        c0.x0(this, this.f17681g);
        A();
    }

    @Override // com.google.android.material.chip.a.InterfaceC0130a
    public void a() {
        k(this.f17689t);
        requestLayout();
        if (Build.VERSION.SDK_INT >= 21) {
            invalidateOutline();
        }
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return m(motionEvent) || this.f17690w.v(motionEvent) || super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!this.f17690w.w(keyEvent) || this.f17690w.A() == Integer.MIN_VALUE) {
            return super.dispatchKeyEvent(keyEvent);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        com.google.android.material.chip.a aVar = this.f17679e;
        if ((aVar == null || !aVar.v1()) ? false : this.f17679e.r2(j())) {
            invalidate();
        }
    }

    public Drawable getBackgroundDrawable() {
        InsetDrawable insetDrawable = this.f17680f;
        return insetDrawable == null ? this.f17679e : insetDrawable;
    }

    public Drawable getCheckedIcon() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.L0();
        }
        return null;
    }

    public ColorStateList getCheckedIconTint() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.M0();
        }
        return null;
    }

    public ColorStateList getChipBackgroundColor() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.N0();
        }
        return null;
    }

    public float getChipCornerRadius() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return Math.max(0.0f, aVar.O0());
        }
        return 0.0f;
    }

    public Drawable getChipDrawable() {
        return this.f17679e;
    }

    public float getChipEndPadding() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.P0();
        }
        return 0.0f;
    }

    public Drawable getChipIcon() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.Q0();
        }
        return null;
    }

    public float getChipIconSize() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.R0();
        }
        return 0.0f;
    }

    public ColorStateList getChipIconTint() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.S0();
        }
        return null;
    }

    public float getChipMinHeight() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.T0();
        }
        return 0.0f;
    }

    public float getChipStartPadding() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.U0();
        }
        return 0.0f;
    }

    public ColorStateList getChipStrokeColor() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.V0();
        }
        return null;
    }

    public float getChipStrokeWidth() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.W0();
        }
        return 0.0f;
    }

    @Deprecated
    public CharSequence getChipText() {
        return getText();
    }

    public Drawable getCloseIcon() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.X0();
        }
        return null;
    }

    public CharSequence getCloseIconContentDescription() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.Y0();
        }
        return null;
    }

    public float getCloseIconEndPadding() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.Z0();
        }
        return 0.0f;
    }

    public float getCloseIconSize() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.a1();
        }
        return 0.0f;
    }

    public float getCloseIconStartPadding() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.b1();
        }
        return 0.0f;
    }

    public ColorStateList getCloseIconTint() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.d1();
        }
        return null;
    }

    @Override // android.widget.TextView
    public TextUtils.TruncateAt getEllipsize() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.h1();
        }
        return null;
    }

    @Override // android.widget.TextView, android.view.View
    public void getFocusedRect(Rect rect) {
        if (this.f17690w.A() == 1 || this.f17690w.x() == 1) {
            rect.set(getCloseIconTouchBoundsInt());
        } else {
            super.getFocusedRect(rect);
        }
    }

    public h getHideMotionSpec() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.i1();
        }
        return null;
    }

    public float getIconEndPadding() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.j1();
        }
        return 0.0f;
    }

    public float getIconStartPadding() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.k1();
        }
        return 0.0f;
    }

    public ColorStateList getRippleColor() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.l1();
        }
        return null;
    }

    public x7.m getShapeAppearanceModel() {
        return this.f17679e.D();
    }

    public h getShowMotionSpec() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.m1();
        }
        return null;
    }

    public float getTextEndPadding() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.p1();
        }
        return 0.0f;
    }

    public float getTextStartPadding() {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            return aVar.q1();
        }
        return 0.0f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x007d, code lost:
        if (getMinWidth() != r6) goto L42;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean k(int r6) {
        /*
            r5 = this;
            r5.f17689t = r6
            boolean r0 = r5.v()
            r1 = 0
            if (r0 != 0) goto L15
            android.graphics.drawable.InsetDrawable r6 = r5.f17680f
            if (r6 == 0) goto L11
            r5.u()
            goto L14
        L11:
            r5.y()
        L14:
            return r1
        L15:
            com.google.android.material.chip.a r0 = r5.f17679e
            int r0 = r0.getIntrinsicHeight()
            int r0 = r6 - r0
            int r0 = java.lang.Math.max(r1, r0)
            com.google.android.material.chip.a r2 = r5.f17679e
            int r2 = r2.getIntrinsicWidth()
            int r2 = r6 - r2
            int r2 = java.lang.Math.max(r1, r2)
            if (r2 > 0) goto L3d
            if (r0 > 0) goto L3d
            android.graphics.drawable.InsetDrawable r6 = r5.f17680f
            if (r6 == 0) goto L39
            r5.u()
            goto L3c
        L39:
            r5.y()
        L3c:
            return r1
        L3d:
            if (r2 <= 0) goto L42
            int r2 = r2 / 2
            goto L43
        L42:
            r2 = r1
        L43:
            if (r0 <= 0) goto L47
            int r1 = r0 / 2
        L47:
            android.graphics.drawable.InsetDrawable r0 = r5.f17680f
            r3 = 1
            if (r0 == 0) goto L6a
            android.graphics.Rect r0 = new android.graphics.Rect
            r0.<init>()
            android.graphics.drawable.InsetDrawable r4 = r5.f17680f
            r4.getPadding(r0)
            int r4 = r0.top
            if (r4 != r1) goto L6a
            int r4 = r0.bottom
            if (r4 != r1) goto L6a
            int r4 = r0.left
            if (r4 != r2) goto L6a
            int r0 = r0.right
            if (r0 != r2) goto L6a
            r5.y()
            return r3
        L6a:
            int r0 = android.os.Build.VERSION.SDK_INT
            r4 = 16
            if (r0 < r4) goto L80
            int r0 = r5.getMinHeight()
            if (r0 == r6) goto L79
            r5.setMinHeight(r6)
        L79:
            int r0 = r5.getMinWidth()
            if (r0 == r6) goto L86
            goto L83
        L80:
            r5.setMinHeight(r6)
        L83:
            r5.setMinWidth(r6)
        L86:
            r5.q(r2, r1, r2, r1)
            r5.y()
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.chip.Chip.k(int):boolean");
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        i.f(this, this.f17679e);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int i8) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i8 + 2);
        if (isChecked()) {
            CheckBox.mergeDrawableStates(onCreateDrawableState, C);
        }
        if (r()) {
            CheckBox.mergeDrawableStates(onCreateDrawableState, E);
        }
        return onCreateDrawableState;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onFocusChanged(boolean z4, int i8, Rect rect) {
        super.onFocusChanged(z4, i8, rect);
        this.f17690w.K(z4, i8, rect);
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        boolean contains;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 7) {
            if (actionMasked == 10) {
                contains = false;
            }
            return super.onHoverEvent(motionEvent);
        }
        contains = getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY());
        setCloseIconHovered(contains);
        return super.onHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((r() || isClickable()) ? r() ? "android.widget.CompoundButton" : "android.widget.Button" : "android.view.View");
        accessibilityNodeInfo.setCheckable(r());
        accessibilityNodeInfo.setClickable(isClickable());
        if (getParent() instanceof ChipGroup) {
            ChipGroup chipGroup = (ChipGroup) getParent();
            androidx.core.view.accessibility.c.I0(accessibilityNodeInfo).f0(c.C0043c.a(chipGroup.b(this), 1, chipGroup.c() ? chipGroup.o(this) : -1, 1, false, isChecked()));
        }
    }

    @Override // android.widget.Button, android.widget.TextView, android.view.View
    @TargetApi(24)
    public PointerIcon onResolvePointerIcon(MotionEvent motionEvent, int i8) {
        if (getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY()) && isEnabled()) {
            return PointerIcon.getSystemIcon(getContext(), 1002);
        }
        return null;
    }

    @Override // android.widget.TextView, android.view.View
    @TargetApi(17)
    public void onRtlPropertiesChanged(int i8) {
        super.onRtlPropertiesChanged(i8);
        if (this.q != i8) {
            this.q = i8;
            A();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x001e, code lost:
        if (r0 != 3) goto L17;
     */
    @Override // android.widget.TextView, android.view.View
    @android.annotation.SuppressLint({"ClickableViewAccessibility"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r6) {
        /*
            r5 = this;
            int r0 = r6.getActionMasked()
            android.graphics.RectF r1 = r5.getCloseIconTouchBounds()
            float r2 = r6.getX()
            float r3 = r6.getY()
            boolean r1 = r1.contains(r2, r3)
            r2 = 0
            r3 = 1
            if (r0 == 0) goto L39
            if (r0 == r3) goto L2b
            r4 = 2
            if (r0 == r4) goto L21
            r1 = 3
            if (r0 == r1) goto L34
            goto L40
        L21:
            boolean r0 = r5.f17685l
            if (r0 == 0) goto L40
            if (r1 != 0) goto L3e
            r5.setCloseIconPressed(r2)
            goto L3e
        L2b:
            boolean r0 = r5.f17685l
            if (r0 == 0) goto L34
            r5.t()
            r0 = r3
            goto L35
        L34:
            r0 = r2
        L35:
            r5.setCloseIconPressed(r2)
            goto L41
        L39:
            if (r1 == 0) goto L40
            r5.setCloseIconPressed(r3)
        L3e:
            r0 = r3
            goto L41
        L40:
            r0 = r2
        L41:
            if (r0 != 0) goto L49
            boolean r6 = super.onTouchEvent(r6)
            if (r6 == 0) goto L4a
        L49:
            r2 = r3
        L4a:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.chip.Chip.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean r() {
        com.google.android.material.chip.a aVar = this.f17679e;
        return aVar != null && aVar.u1();
    }

    public boolean s() {
        com.google.android.material.chip.a aVar = this.f17679e;
        return aVar != null && aVar.w1();
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        if (drawable == getBackgroundDrawable() || drawable == this.f17681g) {
            super.setBackground(drawable);
        } else {
            Log.w("Chip", "Do not set the background; Chip manages its own background drawable.");
        }
    }

    @Override // android.view.View
    public void setBackgroundColor(int i8) {
        Log.w("Chip", "Do not set the background color; Chip manages its own background drawable.");
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (drawable == getBackgroundDrawable() || drawable == this.f17681g) {
            super.setBackgroundDrawable(drawable);
        } else {
            Log.w("Chip", "Do not set the background drawable; Chip manages its own background drawable.");
        }
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.view.View
    public void setBackgroundResource(int i8) {
        Log.w("Chip", "Do not set the background resource; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList colorStateList) {
        Log.w("Chip", "Do not set the background tint list; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        Log.w("Chip", "Do not set the background tint mode; Chip manages its own background drawable.");
    }

    public void setCheckable(boolean z4) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.D1(z4);
        }
    }

    public void setCheckableResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.E1(i8);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z4) {
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar == null) {
            this.f17684k = z4;
        } else if (aVar.u1()) {
            boolean isChecked = isChecked();
            super.setChecked(z4);
            if (isChecked == z4 || (onCheckedChangeListener = this.f17683j) == null) {
                return;
            }
            onCheckedChangeListener.onCheckedChanged(this, z4);
        }
    }

    public void setCheckedIcon(Drawable drawable) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.F1(drawable);
        }
    }

    @Deprecated
    public void setCheckedIconEnabled(boolean z4) {
        setCheckedIconVisible(z4);
    }

    @Deprecated
    public void setCheckedIconEnabledResource(int i8) {
        setCheckedIconVisible(i8);
    }

    public void setCheckedIconResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.G1(i8);
        }
    }

    public void setCheckedIconTint(ColorStateList colorStateList) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.H1(colorStateList);
        }
    }

    public void setCheckedIconTintResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.I1(i8);
        }
    }

    public void setCheckedIconVisible(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.J1(i8);
        }
    }

    public void setCheckedIconVisible(boolean z4) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.K1(z4);
        }
    }

    public void setChipBackgroundColor(ColorStateList colorStateList) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.L1(colorStateList);
        }
    }

    public void setChipBackgroundColorResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.M1(i8);
        }
    }

    @Deprecated
    public void setChipCornerRadius(float f5) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.N1(f5);
        }
    }

    @Deprecated
    public void setChipCornerRadiusResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.O1(i8);
        }
    }

    public void setChipDrawable(com.google.android.material.chip.a aVar) {
        com.google.android.material.chip.a aVar2 = this.f17679e;
        if (aVar2 != aVar) {
            w(aVar2);
            this.f17679e = aVar;
            aVar.G2(false);
            i(this.f17679e);
            k(this.f17689t);
        }
    }

    public void setChipEndPadding(float f5) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.P1(f5);
        }
    }

    public void setChipEndPaddingResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.Q1(i8);
        }
    }

    public void setChipIcon(Drawable drawable) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.R1(drawable);
        }
    }

    @Deprecated
    public void setChipIconEnabled(boolean z4) {
        setChipIconVisible(z4);
    }

    @Deprecated
    public void setChipIconEnabledResource(int i8) {
        setChipIconVisible(i8);
    }

    public void setChipIconResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.S1(i8);
        }
    }

    public void setChipIconSize(float f5) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.T1(f5);
        }
    }

    public void setChipIconSizeResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.U1(i8);
        }
    }

    public void setChipIconTint(ColorStateList colorStateList) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.V1(colorStateList);
        }
    }

    public void setChipIconTintResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.W1(i8);
        }
    }

    public void setChipIconVisible(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.X1(i8);
        }
    }

    public void setChipIconVisible(boolean z4) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.Y1(z4);
        }
    }

    public void setChipMinHeight(float f5) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.Z1(f5);
        }
    }

    public void setChipMinHeightResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.a2(i8);
        }
    }

    public void setChipStartPadding(float f5) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.b2(f5);
        }
    }

    public void setChipStartPaddingResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.c2(i8);
        }
    }

    public void setChipStrokeColor(ColorStateList colorStateList) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.d2(colorStateList);
        }
    }

    public void setChipStrokeColorResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.e2(i8);
        }
    }

    public void setChipStrokeWidth(float f5) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.f2(f5);
        }
    }

    public void setChipStrokeWidthResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.g2(i8);
        }
    }

    @Deprecated
    public void setChipText(CharSequence charSequence) {
        setText(charSequence);
    }

    @Deprecated
    public void setChipTextResource(int i8) {
        setText(getResources().getString(i8));
    }

    public void setCloseIcon(Drawable drawable) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.i2(drawable);
        }
        x();
    }

    public void setCloseIconContentDescription(CharSequence charSequence) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.j2(charSequence);
        }
    }

    @Deprecated
    public void setCloseIconEnabled(boolean z4) {
        setCloseIconVisible(z4);
    }

    @Deprecated
    public void setCloseIconEnabledResource(int i8) {
        setCloseIconVisible(i8);
    }

    public void setCloseIconEndPadding(float f5) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.k2(f5);
        }
    }

    public void setCloseIconEndPaddingResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.l2(i8);
        }
    }

    public void setCloseIconResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.m2(i8);
        }
        x();
    }

    public void setCloseIconSize(float f5) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.n2(f5);
        }
    }

    public void setCloseIconSizeResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.o2(i8);
        }
    }

    public void setCloseIconStartPadding(float f5) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.p2(f5);
        }
    }

    public void setCloseIconStartPaddingResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.q2(i8);
        }
    }

    public void setCloseIconTint(ColorStateList colorStateList) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.s2(colorStateList);
        }
    }

    public void setCloseIconTintResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.t2(i8);
        }
    }

    public void setCloseIconVisible(int i8) {
        setCloseIconVisible(getResources().getBoolean(i8));
    }

    public void setCloseIconVisible(boolean z4) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.u2(z4);
        }
        x();
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int i8, int i9, int i10, int i11) {
        if (i8 != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (i10 != 0) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(i8, i9, i10, i11);
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(int i8, int i9, int i10, int i11) {
        if (i8 != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (i10 != 0) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesWithIntrinsicBounds(i8, i9, i10, i11);
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
        }
        if (drawable3 != null) {
            throw new UnsupportedOperationException("Please set right drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
    }

    @Override // android.view.View
    public void setElevation(float f5) {
        super.setElevation(f5);
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.Z(f5);
        }
    }

    @Override // android.widget.TextView
    public void setEllipsize(TextUtils.TruncateAt truncateAt) {
        if (this.f17679e == null) {
            return;
        }
        if (truncateAt == TextUtils.TruncateAt.MARQUEE) {
            throw new UnsupportedOperationException("Text within a chip are not allowed to scroll.");
        }
        super.setEllipsize(truncateAt);
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.w2(truncateAt);
        }
    }

    public void setEnsureMinTouchTargetSize(boolean z4) {
        this.f17688p = z4;
        k(this.f17689t);
    }

    @Override // android.widget.TextView
    public void setGravity(int i8) {
        if (i8 != 8388627) {
            Log.w("Chip", "Chip text must be vertically center and start aligned");
        } else {
            super.setGravity(i8);
        }
    }

    public void setHideMotionSpec(h hVar) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.x2(hVar);
        }
    }

    public void setHideMotionSpecResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.y2(i8);
        }
    }

    public void setIconEndPadding(float f5) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.z2(f5);
        }
    }

    public void setIconEndPaddingResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.A2(i8);
        }
    }

    public void setIconStartPadding(float f5) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.B2(f5);
        }
    }

    public void setIconStartPaddingResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.C2(i8);
        }
    }

    @Override // android.view.View
    public void setLayoutDirection(int i8) {
        if (this.f17679e != null && Build.VERSION.SDK_INT >= 17) {
            super.setLayoutDirection(i8);
        }
    }

    @Override // android.widget.TextView
    public void setLines(int i8) {
        if (i8 > 1) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        super.setLines(i8);
    }

    @Override // android.widget.TextView
    public void setMaxLines(int i8) {
        if (i8 > 1) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        super.setMaxLines(i8);
    }

    @Override // android.widget.TextView
    public void setMaxWidth(int i8) {
        super.setMaxWidth(i8);
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.D2(i8);
        }
    }

    @Override // android.widget.TextView
    public void setMinLines(int i8) {
        if (i8 > 1) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        super.setMinLines(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOnCheckedChangeListenerInternal(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.f17683j = onCheckedChangeListener;
    }

    public void setOnCloseIconClickListener(View.OnClickListener onClickListener) {
        this.f17682h = onClickListener;
        x();
    }

    public void setRippleColor(ColorStateList colorStateList) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.E2(colorStateList);
        }
        if (this.f17679e.s1()) {
            return;
        }
        z();
    }

    public void setRippleColorResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.F2(i8);
            if (this.f17679e.s1()) {
                return;
            }
            z();
        }
    }

    @Override // x7.p
    public void setShapeAppearanceModel(x7.m mVar) {
        this.f17679e.setShapeAppearanceModel(mVar);
    }

    public void setShowMotionSpec(h hVar) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.H2(hVar);
        }
    }

    public void setShowMotionSpecResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.I2(i8);
        }
    }

    @Override // android.widget.TextView
    public void setSingleLine(boolean z4) {
        if (!z4) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        super.setSingleLine(z4);
    }

    @Override // android.widget.TextView
    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar == null) {
            return;
        }
        if (charSequence == null) {
            charSequence = BuildConfig.FLAVOR;
        }
        super.setText(aVar.R2() ? null : charSequence, bufferType);
        com.google.android.material.chip.a aVar2 = this.f17679e;
        if (aVar2 != null) {
            aVar2.J2(charSequence);
        }
    }

    @Override // android.widget.TextView
    public void setTextAppearance(int i8) {
        super.setTextAppearance(i8);
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.L2(i8);
        }
        B();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i8) {
        super.setTextAppearance(context, i8);
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.L2(i8);
        }
        B();
    }

    public void setTextAppearance(d dVar) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.K2(dVar);
        }
        B();
    }

    public void setTextAppearanceResource(int i8) {
        setTextAppearance(getContext(), i8);
    }

    public void setTextEndPadding(float f5) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.M2(f5);
        }
    }

    public void setTextEndPaddingResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.N2(i8);
        }
    }

    public void setTextStartPadding(float f5) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.O2(f5);
        }
    }

    public void setTextStartPaddingResource(int i8) {
        com.google.android.material.chip.a aVar = this.f17679e;
        if (aVar != null) {
            aVar.P2(i8);
        }
    }

    public boolean t() {
        boolean z4 = false;
        playSoundEffect(0);
        View.OnClickListener onClickListener = this.f17682h;
        if (onClickListener != null) {
            onClickListener.onClick(this);
            z4 = true;
        }
        this.f17690w.W(1, 1);
        return z4;
    }

    public boolean v() {
        return this.f17688p;
    }
}
