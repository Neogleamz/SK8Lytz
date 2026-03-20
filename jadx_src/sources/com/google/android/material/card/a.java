package com.google.android.material.card;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import androidx.core.view.c0;
import k7.f;
import k7.k;
import k7.l;
import u7.c;
import v7.b;
import x7.d;
import x7.e;
import x7.h;
import x7.m;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: t  reason: collision with root package name */
    private static final int[] f17654t = {16842912};

    /* renamed from: u  reason: collision with root package name */
    private static final double f17655u = Math.cos(Math.toRadians(45.0d));

    /* renamed from: a  reason: collision with root package name */
    private final MaterialCardView f17656a;

    /* renamed from: c  reason: collision with root package name */
    private final h f17658c;

    /* renamed from: d  reason: collision with root package name */
    private final h f17659d;

    /* renamed from: e  reason: collision with root package name */
    private int f17660e;

    /* renamed from: f  reason: collision with root package name */
    private int f17661f;

    /* renamed from: g  reason: collision with root package name */
    private int f17662g;

    /* renamed from: h  reason: collision with root package name */
    private Drawable f17663h;

    /* renamed from: i  reason: collision with root package name */
    private Drawable f17664i;

    /* renamed from: j  reason: collision with root package name */
    private ColorStateList f17665j;

    /* renamed from: k  reason: collision with root package name */
    private ColorStateList f17666k;

    /* renamed from: l  reason: collision with root package name */
    private m f17667l;

    /* renamed from: m  reason: collision with root package name */
    private ColorStateList f17668m;

    /* renamed from: n  reason: collision with root package name */
    private Drawable f17669n;

    /* renamed from: o  reason: collision with root package name */
    private LayerDrawable f17670o;

    /* renamed from: p  reason: collision with root package name */
    private h f17671p;
    private h q;

    /* renamed from: s  reason: collision with root package name */
    private boolean f17673s;

    /* renamed from: b  reason: collision with root package name */
    private final Rect f17657b = new Rect();

    /* renamed from: r  reason: collision with root package name */
    private boolean f17672r = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.android.material.card.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0129a extends InsetDrawable {
        C0129a(Drawable drawable, int i8, int i9, int i10, int i11) {
            super(drawable, i8, i9, i10, i11);
        }

        @Override // android.graphics.drawable.Drawable
        public int getMinimumHeight() {
            return -1;
        }

        @Override // android.graphics.drawable.Drawable
        public int getMinimumWidth() {
            return -1;
        }

        @Override // android.graphics.drawable.InsetDrawable, android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
        public boolean getPadding(Rect rect) {
            return false;
        }
    }

    public a(MaterialCardView materialCardView, AttributeSet attributeSet, int i8, int i9) {
        this.f17656a = materialCardView;
        h hVar = new h(materialCardView.getContext(), attributeSet, i8, i9);
        this.f17658c = hVar;
        hVar.P(materialCardView.getContext());
        hVar.g0(-12303292);
        m.b v8 = hVar.D().v();
        TypedArray obtainStyledAttributes = materialCardView.getContext().obtainStyledAttributes(attributeSet, l.A0, i8, k.f21230a);
        int i10 = l.B0;
        if (obtainStyledAttributes.hasValue(i10)) {
            v8.o(obtainStyledAttributes.getDimension(i10, 0.0f));
        }
        this.f17659d = new h();
        R(v8.m());
        obtainStyledAttributes.recycle();
    }

    private Drawable B(Drawable drawable) {
        int ceil;
        int i8;
        if ((Build.VERSION.SDK_INT < 21) || this.f17656a.getUseCompatPadding()) {
            int ceil2 = (int) Math.ceil(d());
            ceil = (int) Math.ceil(c());
            i8 = ceil2;
        } else {
            ceil = 0;
            i8 = 0;
        }
        return new C0129a(drawable, ceil, i8, ceil, i8);
    }

    private boolean V() {
        return this.f17656a.getPreventCornerOverlap() && !e();
    }

    private boolean W() {
        return this.f17656a.getPreventCornerOverlap() && e() && this.f17656a.getUseCompatPadding();
    }

    private float a() {
        return Math.max(Math.max(b(this.f17667l.q(), this.f17658c.I()), b(this.f17667l.s(), this.f17658c.J())), Math.max(b(this.f17667l.k(), this.f17658c.t()), b(this.f17667l.i(), this.f17658c.s())));
    }

    private void a0(Drawable drawable) {
        if (Build.VERSION.SDK_INT < 23 || !(this.f17656a.getForeground() instanceof InsetDrawable)) {
            this.f17656a.setForeground(B(drawable));
        } else {
            ((InsetDrawable) this.f17656a.getForeground()).setDrawable(drawable);
        }
    }

    private float b(d dVar, float f5) {
        if (dVar instanceof x7.l) {
            return (float) ((1.0d - f17655u) * f5);
        }
        if (dVar instanceof e) {
            return f5 / 2.0f;
        }
        return 0.0f;
    }

    private float c() {
        return this.f17656a.getMaxCardElevation() + (W() ? a() : 0.0f);
    }

    private void c0() {
        Drawable drawable;
        if (b.f23352a && (drawable = this.f17669n) != null) {
            ((RippleDrawable) drawable).setColor(this.f17665j);
            return;
        }
        h hVar = this.f17671p;
        if (hVar != null) {
            hVar.a0(this.f17665j);
        }
    }

    private float d() {
        return (this.f17656a.getMaxCardElevation() * 1.5f) + (W() ? a() : 0.0f);
    }

    private boolean e() {
        return Build.VERSION.SDK_INT >= 21 && this.f17658c.S();
    }

    private Drawable f() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable drawable = this.f17664i;
        if (drawable != null) {
            stateListDrawable.addState(f17654t, drawable);
        }
        return stateListDrawable;
    }

    private Drawable g() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        h i8 = i();
        this.f17671p = i8;
        i8.a0(this.f17665j);
        stateListDrawable.addState(new int[]{16842919}, this.f17671p);
        return stateListDrawable;
    }

    private Drawable h() {
        if (b.f23352a) {
            this.q = i();
            return new RippleDrawable(this.f17665j, null, this.q);
        }
        return g();
    }

    private h i() {
        return new h(this.f17667l);
    }

    private Drawable r() {
        if (this.f17669n == null) {
            this.f17669n = h();
        }
        if (this.f17670o == null) {
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{this.f17669n, this.f17659d, f()});
            this.f17670o = layerDrawable;
            layerDrawable.setId(2, f.C);
        }
        return this.f17670o;
    }

    private float t() {
        if (this.f17656a.getPreventCornerOverlap()) {
            if (Build.VERSION.SDK_INT < 21 || this.f17656a.getUseCompatPadding()) {
                return (float) ((1.0d - f17655u) * this.f17656a.getCardViewRadius());
            }
            return 0.0f;
        }
        return 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect A() {
        return this.f17657b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean C() {
        return this.f17672r;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean D() {
        return this.f17673s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void E(TypedArray typedArray) {
        ColorStateList a9 = c.a(this.f17656a.getContext(), typedArray, l.A4);
        this.f17668m = a9;
        if (a9 == null) {
            this.f17668m = ColorStateList.valueOf(-1);
        }
        this.f17662g = typedArray.getDimensionPixelSize(l.B4, 0);
        boolean z4 = typedArray.getBoolean(l.f21430t4, false);
        this.f17673s = z4;
        this.f17656a.setLongClickable(z4);
        this.f17666k = c.a(this.f17656a.getContext(), typedArray, l.f21475y4);
        K(c.d(this.f17656a.getContext(), typedArray, l.f21448v4));
        M(typedArray.getDimensionPixelSize(l.f21466x4, 0));
        L(typedArray.getDimensionPixelSize(l.f21457w4, 0));
        ColorStateList a10 = c.a(this.f17656a.getContext(), typedArray, l.z4);
        this.f17665j = a10;
        if (a10 == null) {
            this.f17665j = ColorStateList.valueOf(n7.a.d(this.f17656a, k7.b.f21062n));
        }
        I(c.a(this.f17656a.getContext(), typedArray, l.f21439u4));
        c0();
        Z();
        d0();
        this.f17656a.setBackgroundInternal(B(this.f17658c));
        Drawable r4 = this.f17656a.isClickable() ? r() : this.f17659d;
        this.f17663h = r4;
        this.f17656a.setForeground(B(r4));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void F(int i8, int i9) {
        int i10;
        int i11;
        if (this.f17670o != null) {
            int i12 = this.f17660e;
            int i13 = this.f17661f;
            int i14 = (i8 - i12) - i13;
            int i15 = (i9 - i12) - i13;
            if ((Build.VERSION.SDK_INT < 21) || this.f17656a.getUseCompatPadding()) {
                i15 -= (int) Math.ceil(d() * 2.0f);
                i14 -= (int) Math.ceil(c() * 2.0f);
            }
            int i16 = i15;
            int i17 = this.f17660e;
            if (c0.E(this.f17656a) == 1) {
                i11 = i14;
                i10 = i17;
            } else {
                i10 = i14;
                i11 = i17;
            }
            this.f17670o.setLayerInset(2, i10, this.f17660e, i11, i16);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void G(boolean z4) {
        this.f17672r = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void H(ColorStateList colorStateList) {
        this.f17658c.a0(colorStateList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void I(ColorStateList colorStateList) {
        h hVar = this.f17659d;
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(0);
        }
        hVar.a0(colorStateList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J(boolean z4) {
        this.f17673s = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void K(Drawable drawable) {
        this.f17664i = drawable;
        if (drawable != null) {
            Drawable r4 = androidx.core.graphics.drawable.a.r(drawable.mutate());
            this.f17664i = r4;
            androidx.core.graphics.drawable.a.o(r4, this.f17666k);
        }
        if (this.f17670o != null) {
            this.f17670o.setDrawableByLayerId(f.C, f());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void L(int i8) {
        this.f17660e = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void M(int i8) {
        this.f17661f = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void N(ColorStateList colorStateList) {
        this.f17666k = colorStateList;
        Drawable drawable = this.f17664i;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.o(drawable, colorStateList);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void O(float f5) {
        R(this.f17667l.w(f5));
        this.f17663h.invalidateSelf();
        if (W() || V()) {
            Y();
        }
        if (W()) {
            b0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void P(float f5) {
        this.f17658c.b0(f5);
        h hVar = this.f17659d;
        if (hVar != null) {
            hVar.b0(f5);
        }
        h hVar2 = this.q;
        if (hVar2 != null) {
            hVar2.b0(f5);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Q(ColorStateList colorStateList) {
        this.f17665j = colorStateList;
        c0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void R(m mVar) {
        this.f17667l = mVar;
        this.f17658c.setShapeAppearanceModel(mVar);
        h hVar = this.f17658c;
        hVar.f0(!hVar.S());
        h hVar2 = this.f17659d;
        if (hVar2 != null) {
            hVar2.setShapeAppearanceModel(mVar);
        }
        h hVar3 = this.q;
        if (hVar3 != null) {
            hVar3.setShapeAppearanceModel(mVar);
        }
        h hVar4 = this.f17671p;
        if (hVar4 != null) {
            hVar4.setShapeAppearanceModel(mVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void S(ColorStateList colorStateList) {
        if (this.f17668m == colorStateList) {
            return;
        }
        this.f17668m = colorStateList;
        d0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void T(int i8) {
        if (i8 == this.f17662g) {
            return;
        }
        this.f17662g = i8;
        d0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void U(int i8, int i9, int i10, int i11) {
        this.f17657b.set(i8, i9, i10, i11);
        Y();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void X() {
        Drawable drawable = this.f17663h;
        Drawable r4 = this.f17656a.isClickable() ? r() : this.f17659d;
        this.f17663h = r4;
        if (drawable != r4) {
            a0(r4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Y() {
        int a9 = (int) ((V() || W() ? a() : 0.0f) - t());
        MaterialCardView materialCardView = this.f17656a;
        Rect rect = this.f17657b;
        materialCardView.m(rect.left + a9, rect.top + a9, rect.right + a9, rect.bottom + a9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Z() {
        this.f17658c.Z(this.f17656a.getCardElevation());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b0() {
        if (!C()) {
            this.f17656a.setBackgroundInternal(B(this.f17658c));
        }
        this.f17656a.setForeground(B(this.f17663h));
    }

    void d0() {
        this.f17659d.k0(this.f17662g, this.f17668m);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j() {
        Drawable drawable = this.f17669n;
        if (drawable != null) {
            Rect bounds = drawable.getBounds();
            int i8 = bounds.bottom;
            this.f17669n.setBounds(bounds.left, bounds.top, bounds.right, i8 - 1);
            this.f17669n.setBounds(bounds.left, bounds.top, bounds.right, i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public h k() {
        return this.f17658c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList l() {
        return this.f17658c.x();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList m() {
        return this.f17659d.x();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Drawable n() {
        return this.f17664i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int o() {
        return this.f17660e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int p() {
        return this.f17661f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList q() {
        return this.f17666k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float s() {
        return this.f17658c.I();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float u() {
        return this.f17658c.y();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList v() {
        return this.f17665j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public m w() {
        return this.f17667l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int x() {
        ColorStateList colorStateList = this.f17668m;
        if (colorStateList == null) {
            return -1;
        }
        return colorStateList.getDefaultColor();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList y() {
        return this.f17668m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int z() {
        return this.f17662g;
    }
}
