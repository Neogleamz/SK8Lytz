package com.google.android.material.button;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import androidx.core.view.c0;
import com.google.android.material.internal.s;
import k7.b;
import k7.l;
import u7.c;
import x7.h;
import x7.m;
import x7.p;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: t  reason: collision with root package name */
    private static final boolean f17627t;

    /* renamed from: a  reason: collision with root package name */
    private final MaterialButton f17628a;

    /* renamed from: b  reason: collision with root package name */
    private m f17629b;

    /* renamed from: c  reason: collision with root package name */
    private int f17630c;

    /* renamed from: d  reason: collision with root package name */
    private int f17631d;

    /* renamed from: e  reason: collision with root package name */
    private int f17632e;

    /* renamed from: f  reason: collision with root package name */
    private int f17633f;

    /* renamed from: g  reason: collision with root package name */
    private int f17634g;

    /* renamed from: h  reason: collision with root package name */
    private int f17635h;

    /* renamed from: i  reason: collision with root package name */
    private PorterDuff.Mode f17636i;

    /* renamed from: j  reason: collision with root package name */
    private ColorStateList f17637j;

    /* renamed from: k  reason: collision with root package name */
    private ColorStateList f17638k;

    /* renamed from: l  reason: collision with root package name */
    private ColorStateList f17639l;

    /* renamed from: m  reason: collision with root package name */
    private Drawable f17640m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f17641n = false;

    /* renamed from: o  reason: collision with root package name */
    private boolean f17642o = false;

    /* renamed from: p  reason: collision with root package name */
    private boolean f17643p = false;
    private boolean q;

    /* renamed from: r  reason: collision with root package name */
    private LayerDrawable f17644r;

    /* renamed from: s  reason: collision with root package name */
    private int f17645s;

    static {
        f17627t = Build.VERSION.SDK_INT >= 21;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(MaterialButton materialButton, m mVar) {
        this.f17628a = materialButton;
        this.f17629b = mVar;
    }

    private void E(int i8, int i9) {
        int J = c0.J(this.f17628a);
        int paddingTop = this.f17628a.getPaddingTop();
        int I = c0.I(this.f17628a);
        int paddingBottom = this.f17628a.getPaddingBottom();
        int i10 = this.f17632e;
        int i11 = this.f17633f;
        this.f17633f = i9;
        this.f17632e = i8;
        if (!this.f17642o) {
            F();
        }
        c0.J0(this.f17628a, J, (paddingTop + i8) - i10, I, (paddingBottom + i9) - i11);
    }

    private void F() {
        this.f17628a.setInternalBackground(a());
        h f5 = f();
        if (f5 != null) {
            f5.Z(this.f17645s);
        }
    }

    private void G(m mVar) {
        if (f() != null) {
            f().setShapeAppearanceModel(mVar);
        }
        if (n() != null) {
            n().setShapeAppearanceModel(mVar);
        }
        if (e() != null) {
            e().setShapeAppearanceModel(mVar);
        }
    }

    private void I() {
        h f5 = f();
        h n8 = n();
        if (f5 != null) {
            f5.k0(this.f17635h, this.f17638k);
            if (n8 != null) {
                n8.j0(this.f17635h, this.f17641n ? n7.a.d(this.f17628a, b.f21066s) : 0);
            }
        }
    }

    private InsetDrawable J(Drawable drawable) {
        return new InsetDrawable(drawable, this.f17630c, this.f17632e, this.f17631d, this.f17633f);
    }

    private Drawable a() {
        h hVar = new h(this.f17629b);
        hVar.P(this.f17628a.getContext());
        androidx.core.graphics.drawable.a.o(hVar, this.f17637j);
        PorterDuff.Mode mode = this.f17636i;
        if (mode != null) {
            androidx.core.graphics.drawable.a.p(hVar, mode);
        }
        hVar.k0(this.f17635h, this.f17638k);
        h hVar2 = new h(this.f17629b);
        hVar2.setTint(0);
        hVar2.j0(this.f17635h, this.f17641n ? n7.a.d(this.f17628a, b.f21066s) : 0);
        if (f17627t) {
            h hVar3 = new h(this.f17629b);
            this.f17640m = hVar3;
            androidx.core.graphics.drawable.a.n(hVar3, -1);
            RippleDrawable rippleDrawable = new RippleDrawable(v7.b.d(this.f17639l), J(new LayerDrawable(new Drawable[]{hVar2, hVar})), this.f17640m);
            this.f17644r = rippleDrawable;
            return rippleDrawable;
        }
        v7.a aVar = new v7.a(this.f17629b);
        this.f17640m = aVar;
        androidx.core.graphics.drawable.a.o(aVar, v7.b.d(this.f17639l));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{hVar2, hVar, this.f17640m});
        this.f17644r = layerDrawable;
        return J(layerDrawable);
    }

    private h g(boolean z4) {
        LayerDrawable layerDrawable = this.f17644r;
        if (layerDrawable == null || layerDrawable.getNumberOfLayers() <= 0) {
            return null;
        }
        return (h) (f17627t ? (LayerDrawable) ((InsetDrawable) this.f17644r.getDrawable(0)).getDrawable() : this.f17644r).getDrawable(!z4 ? 1 : 0);
    }

    private h n() {
        return g(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A(ColorStateList colorStateList) {
        if (this.f17638k != colorStateList) {
            this.f17638k = colorStateList;
            I();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void B(int i8) {
        if (this.f17635h != i8) {
            this.f17635h = i8;
            I();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void C(ColorStateList colorStateList) {
        if (this.f17637j != colorStateList) {
            this.f17637j = colorStateList;
            if (f() != null) {
                androidx.core.graphics.drawable.a.o(f(), this.f17637j);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void D(PorterDuff.Mode mode) {
        if (this.f17636i != mode) {
            this.f17636i = mode;
            if (f() == null || this.f17636i == null) {
                return;
            }
            androidx.core.graphics.drawable.a.p(f(), this.f17636i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void H(int i8, int i9) {
        Drawable drawable = this.f17640m;
        if (drawable != null) {
            drawable.setBounds(this.f17630c, this.f17632e, i9 - this.f17631d, i8 - this.f17633f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        return this.f17634g;
    }

    public int c() {
        return this.f17633f;
    }

    public int d() {
        return this.f17632e;
    }

    public p e() {
        LayerDrawable layerDrawable = this.f17644r;
        if (layerDrawable == null || layerDrawable.getNumberOfLayers() <= 1) {
            return null;
        }
        return (p) (this.f17644r.getNumberOfLayers() > 2 ? this.f17644r.getDrawable(2) : this.f17644r.getDrawable(1));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public h f() {
        return g(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList h() {
        return this.f17639l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public m i() {
        return this.f17629b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList j() {
        return this.f17638k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int k() {
        return this.f17635h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList l() {
        return this.f17637j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PorterDuff.Mode m() {
        return this.f17636i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean o() {
        return this.f17642o;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean p() {
        return this.q;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(TypedArray typedArray) {
        this.f17630c = typedArray.getDimensionPixelOffset(l.C3, 0);
        this.f17631d = typedArray.getDimensionPixelOffset(l.D3, 0);
        this.f17632e = typedArray.getDimensionPixelOffset(l.E3, 0);
        this.f17633f = typedArray.getDimensionPixelOffset(l.F3, 0);
        int i8 = l.J3;
        if (typedArray.hasValue(i8)) {
            int dimensionPixelSize = typedArray.getDimensionPixelSize(i8, -1);
            this.f17634g = dimensionPixelSize;
            y(this.f17629b.w(dimensionPixelSize));
            this.f17643p = true;
        }
        this.f17635h = typedArray.getDimensionPixelSize(l.T3, 0);
        this.f17636i = s.i(typedArray.getInt(l.I3, -1), PorterDuff.Mode.SRC_IN);
        this.f17637j = c.a(this.f17628a.getContext(), typedArray, l.H3);
        this.f17638k = c.a(this.f17628a.getContext(), typedArray, l.S3);
        this.f17639l = c.a(this.f17628a.getContext(), typedArray, l.R3);
        this.q = typedArray.getBoolean(l.G3, false);
        this.f17645s = typedArray.getDimensionPixelSize(l.K3, 0);
        int J = c0.J(this.f17628a);
        int paddingTop = this.f17628a.getPaddingTop();
        int I = c0.I(this.f17628a);
        int paddingBottom = this.f17628a.getPaddingBottom();
        if (typedArray.hasValue(l.B3)) {
            s();
        } else {
            F();
        }
        c0.J0(this.f17628a, J + this.f17630c, paddingTop + this.f17632e, I + this.f17631d, paddingBottom + this.f17633f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(int i8) {
        if (f() != null) {
            f().setTint(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s() {
        this.f17642o = true;
        this.f17628a.setSupportBackgroundTintList(this.f17637j);
        this.f17628a.setSupportBackgroundTintMode(this.f17636i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t(boolean z4) {
        this.q = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(int i8) {
        if (this.f17643p && this.f17634g == i8) {
            return;
        }
        this.f17634g = i8;
        this.f17643p = true;
        y(this.f17629b.w(i8));
    }

    public void v(int i8) {
        E(this.f17632e, i8);
    }

    public void w(int i8) {
        E(i8, this.f17633f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(ColorStateList colorStateList) {
        if (this.f17639l != colorStateList) {
            this.f17639l = colorStateList;
            boolean z4 = f17627t;
            if (z4 && (this.f17628a.getBackground() instanceof RippleDrawable)) {
                ((RippleDrawable) this.f17628a.getBackground()).setColor(v7.b.d(colorStateList));
            } else if (z4 || !(this.f17628a.getBackground() instanceof v7.a)) {
            } else {
                ((v7.a) this.f17628a.getBackground()).setTintList(v7.b.d(colorStateList));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y(m mVar) {
        this.f17629b = mVar;
        G(mVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void z(boolean z4) {
        this.f17641n = z4;
        I();
    }
}
