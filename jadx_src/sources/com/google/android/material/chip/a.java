package com.google.android.material.chip;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.material.internal.j;
import com.google.android.material.internal.s;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import u7.d;
import v7.b;
import x7.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends h implements Drawable.Callback, j.b {
    private static final int[] W0 = {16842910};
    private static final ShapeDrawable X0 = new ShapeDrawable(new OvalShape());
    private final j A0;
    private int B0;
    private int C0;
    private int D0;
    private int E0;
    private ColorStateList F;
    private int F0;
    private ColorStateList G;
    private int G0;
    private float H;
    private boolean H0;
    private int I0;
    private int J0;
    private float K;
    private ColorFilter K0;
    private ColorStateList L;
    private PorterDuffColorFilter L0;
    private ColorStateList M0;
    private PorterDuff.Mode N0;
    private float O;
    private int[] O0;
    private ColorStateList P;
    private boolean P0;
    private CharSequence Q;
    private ColorStateList Q0;
    private boolean R;
    private WeakReference<InterfaceC0130a> R0;
    private TextUtils.TruncateAt S0;
    private Drawable T;
    private boolean T0;
    private int U0;
    private boolean V0;
    private ColorStateList W;
    private float X;
    private boolean Y;
    private boolean Z;

    /* renamed from: a0  reason: collision with root package name */
    private Drawable f17709a0;

    /* renamed from: b0  reason: collision with root package name */
    private Drawable f17710b0;

    /* renamed from: c0  reason: collision with root package name */
    private ColorStateList f17711c0;

    /* renamed from: d0  reason: collision with root package name */
    private float f17712d0;

    /* renamed from: e0  reason: collision with root package name */
    private CharSequence f17713e0;

    /* renamed from: f0  reason: collision with root package name */
    private boolean f17714f0;

    /* renamed from: g0  reason: collision with root package name */
    private boolean f17715g0;

    /* renamed from: h0  reason: collision with root package name */
    private Drawable f17716h0;

    /* renamed from: i0  reason: collision with root package name */
    private ColorStateList f17717i0;

    /* renamed from: j0  reason: collision with root package name */
    private l7.h f17718j0;

    /* renamed from: k0  reason: collision with root package name */
    private l7.h f17719k0;

    /* renamed from: l0  reason: collision with root package name */
    private float f17720l0;

    /* renamed from: m0  reason: collision with root package name */
    private float f17721m0;

    /* renamed from: n0  reason: collision with root package name */
    private float f17722n0;

    /* renamed from: o0  reason: collision with root package name */
    private float f17723o0;

    /* renamed from: p0  reason: collision with root package name */
    private float f17724p0;

    /* renamed from: q0  reason: collision with root package name */
    private float f17725q0;

    /* renamed from: r0  reason: collision with root package name */
    private float f17726r0;

    /* renamed from: s0  reason: collision with root package name */
    private float f17727s0;

    /* renamed from: t0  reason: collision with root package name */
    private final Context f17728t0;

    /* renamed from: u0  reason: collision with root package name */
    private final Paint f17729u0;

    /* renamed from: v0  reason: collision with root package name */
    private final Paint f17730v0;

    /* renamed from: w0  reason: collision with root package name */
    private final Paint.FontMetrics f17731w0;

    /* renamed from: x0  reason: collision with root package name */
    private final RectF f17732x0;

    /* renamed from: y0  reason: collision with root package name */
    private final PointF f17733y0;

    /* renamed from: z0  reason: collision with root package name */
    private final Path f17734z0;

    /* renamed from: com.google.android.material.chip.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface InterfaceC0130a {
        void a();
    }

    private a(Context context, AttributeSet attributeSet, int i8, int i9) {
        super(context, attributeSet, i8, i9);
        this.K = -1.0f;
        this.f17729u0 = new Paint(1);
        this.f17731w0 = new Paint.FontMetrics();
        this.f17732x0 = new RectF();
        this.f17733y0 = new PointF();
        this.f17734z0 = new Path();
        this.J0 = 255;
        this.N0 = PorterDuff.Mode.SRC_IN;
        this.R0 = new WeakReference<>(null);
        P(context);
        this.f17728t0 = context;
        j jVar = new j(this);
        this.A0 = jVar;
        this.Q = BuildConfig.FLAVOR;
        jVar.e().density = context.getResources().getDisplayMetrics().density;
        this.f17730v0 = null;
        int[] iArr = W0;
        setState(iArr);
        r2(iArr);
        this.T0 = true;
        if (b.f23352a) {
            X0.setTint(-1);
        }
    }

    private boolean A0() {
        return this.f17715g0 && this.f17716h0 != null && this.f17714f0;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00d6  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x016d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void A1(android.util.AttributeSet r8, int r9, int r10) {
        /*
            Method dump skipped, instructions count: 484
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.chip.a.A1(android.util.AttributeSet, int, int):void");
    }

    public static a B0(Context context, AttributeSet attributeSet, int i8, int i9) {
        a aVar = new a(context, attributeSet, i8, i9);
        aVar.A1(attributeSet, i8, i9);
        return aVar;
    }

    private void C0(Canvas canvas, Rect rect) {
        if (S2()) {
            r0(rect, this.f17732x0);
            RectF rectF = this.f17732x0;
            float f5 = rectF.left;
            float f8 = rectF.top;
            canvas.translate(f5, f8);
            this.f17716h0.setBounds(0, 0, (int) this.f17732x0.width(), (int) this.f17732x0.height());
            this.f17716h0.draw(canvas);
            canvas.translate(-f5, -f8);
        }
    }

    private boolean C1(int[] iArr, int[] iArr2) {
        boolean z4;
        boolean onStateChange = super.onStateChange(iArr);
        ColorStateList colorStateList = this.F;
        int l8 = l(colorStateList != null ? colorStateList.getColorForState(iArr, this.B0) : 0);
        boolean z8 = true;
        if (this.B0 != l8) {
            this.B0 = l8;
            onStateChange = true;
        }
        ColorStateList colorStateList2 = this.G;
        int l9 = l(colorStateList2 != null ? colorStateList2.getColorForState(iArr, this.C0) : 0);
        if (this.C0 != l9) {
            this.C0 = l9;
            onStateChange = true;
        }
        int g8 = n7.a.g(l8, l9);
        if ((this.D0 != g8) | (x() == null)) {
            this.D0 = g8;
            a0(ColorStateList.valueOf(g8));
            onStateChange = true;
        }
        ColorStateList colorStateList3 = this.L;
        int colorForState = colorStateList3 != null ? colorStateList3.getColorForState(iArr, this.E0) : 0;
        if (this.E0 != colorForState) {
            this.E0 = colorForState;
            onStateChange = true;
        }
        int colorForState2 = (this.Q0 == null || !b.e(iArr)) ? 0 : this.Q0.getColorForState(iArr, this.F0);
        if (this.F0 != colorForState2) {
            this.F0 = colorForState2;
            if (this.P0) {
                onStateChange = true;
            }
        }
        int colorForState3 = (this.A0.d() == null || this.A0.d().f23093a == null) ? 0 : this.A0.d().f23093a.getColorForState(iArr, this.G0);
        if (this.G0 != colorForState3) {
            this.G0 = colorForState3;
            onStateChange = true;
        }
        boolean z9 = t1(getState(), 16842912) && this.f17714f0;
        if (this.H0 == z9 || this.f17716h0 == null) {
            z4 = false;
        } else {
            float s02 = s0();
            this.H0 = z9;
            if (s02 != s0()) {
                onStateChange = true;
                z4 = true;
            } else {
                z4 = false;
                onStateChange = true;
            }
        }
        ColorStateList colorStateList4 = this.M0;
        int colorForState4 = colorStateList4 != null ? colorStateList4.getColorForState(iArr, this.I0) : 0;
        if (this.I0 != colorForState4) {
            this.I0 = colorForState4;
            this.L0 = p7.a.b(this, this.M0, this.N0);
        } else {
            z8 = onStateChange;
        }
        if (y1(this.T)) {
            z8 |= this.T.setState(iArr);
        }
        if (y1(this.f17716h0)) {
            z8 |= this.f17716h0.setState(iArr);
        }
        if (y1(this.f17709a0)) {
            int[] iArr3 = new int[iArr.length + iArr2.length];
            System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
            System.arraycopy(iArr2, 0, iArr3, iArr.length, iArr2.length);
            z8 |= this.f17709a0.setState(iArr3);
        }
        if (b.f23352a && y1(this.f17710b0)) {
            z8 |= this.f17710b0.setState(iArr2);
        }
        if (z8) {
            invalidateSelf();
        }
        if (z4) {
            B1();
        }
        return z8;
    }

    private void D0(Canvas canvas, Rect rect) {
        if (this.V0) {
            return;
        }
        this.f17729u0.setColor(this.C0);
        this.f17729u0.setStyle(Paint.Style.FILL);
        this.f17729u0.setColorFilter(r1());
        this.f17732x0.set(rect);
        canvas.drawRoundRect(this.f17732x0, O0(), O0(), this.f17729u0);
    }

    private void E0(Canvas canvas, Rect rect) {
        if (T2()) {
            r0(rect, this.f17732x0);
            RectF rectF = this.f17732x0;
            float f5 = rectF.left;
            float f8 = rectF.top;
            canvas.translate(f5, f8);
            this.T.setBounds(0, 0, (int) this.f17732x0.width(), (int) this.f17732x0.height());
            this.T.draw(canvas);
            canvas.translate(-f5, -f8);
        }
    }

    private void F0(Canvas canvas, Rect rect) {
        if (this.O <= 0.0f || this.V0) {
            return;
        }
        this.f17729u0.setColor(this.E0);
        this.f17729u0.setStyle(Paint.Style.STROKE);
        if (!this.V0) {
            this.f17729u0.setColorFilter(r1());
        }
        RectF rectF = this.f17732x0;
        float f5 = this.O;
        rectF.set(rect.left + (f5 / 2.0f), rect.top + (f5 / 2.0f), rect.right - (f5 / 2.0f), rect.bottom - (f5 / 2.0f));
        float f8 = this.K - (this.O / 2.0f);
        canvas.drawRoundRect(this.f17732x0, f8, f8, this.f17729u0);
    }

    private void G0(Canvas canvas, Rect rect) {
        if (this.V0) {
            return;
        }
        this.f17729u0.setColor(this.B0);
        this.f17729u0.setStyle(Paint.Style.FILL);
        this.f17732x0.set(rect);
        canvas.drawRoundRect(this.f17732x0, O0(), O0(), this.f17729u0);
    }

    private void H0(Canvas canvas, Rect rect) {
        Drawable drawable;
        if (U2()) {
            u0(rect, this.f17732x0);
            RectF rectF = this.f17732x0;
            float f5 = rectF.left;
            float f8 = rectF.top;
            canvas.translate(f5, f8);
            this.f17709a0.setBounds(0, 0, (int) this.f17732x0.width(), (int) this.f17732x0.height());
            if (b.f23352a) {
                this.f17710b0.setBounds(this.f17709a0.getBounds());
                this.f17710b0.jumpToCurrentState();
                drawable = this.f17710b0;
            } else {
                drawable = this.f17709a0;
            }
            drawable.draw(canvas);
            canvas.translate(-f5, -f8);
        }
    }

    private void I0(Canvas canvas, Rect rect) {
        this.f17729u0.setColor(this.F0);
        this.f17729u0.setStyle(Paint.Style.FILL);
        this.f17732x0.set(rect);
        if (!this.V0) {
            canvas.drawRoundRect(this.f17732x0, O0(), O0(), this.f17729u0);
            return;
        }
        h(new RectF(rect), this.f17734z0);
        super.p(canvas, this.f17729u0, this.f17734z0, u());
    }

    private void J0(Canvas canvas, Rect rect) {
        Paint paint = this.f17730v0;
        if (paint != null) {
            paint.setColor(androidx.core.graphics.b.p(-16777216, 127));
            canvas.drawRect(rect, this.f17730v0);
            if (T2() || S2()) {
                r0(rect, this.f17732x0);
                canvas.drawRect(this.f17732x0, this.f17730v0);
            }
            if (this.Q != null) {
                canvas.drawLine(rect.left, rect.exactCenterY(), rect.right, rect.exactCenterY(), this.f17730v0);
            }
            if (U2()) {
                u0(rect, this.f17732x0);
                canvas.drawRect(this.f17732x0, this.f17730v0);
            }
            this.f17730v0.setColor(androidx.core.graphics.b.p(-65536, 127));
            t0(rect, this.f17732x0);
            canvas.drawRect(this.f17732x0, this.f17730v0);
            this.f17730v0.setColor(androidx.core.graphics.b.p(-16711936, 127));
            v0(rect, this.f17732x0);
            canvas.drawRect(this.f17732x0, this.f17730v0);
        }
    }

    private void K0(Canvas canvas, Rect rect) {
        if (this.Q != null) {
            Paint.Align z02 = z0(rect, this.f17733y0);
            x0(rect, this.f17732x0);
            if (this.A0.d() != null) {
                this.A0.e().drawableState = getState();
                this.A0.j(this.f17728t0);
            }
            this.A0.e().setTextAlign(z02);
            int i8 = 0;
            boolean z4 = Math.round(this.A0.f(n1().toString())) > Math.round(this.f17732x0.width());
            if (z4) {
                i8 = canvas.save();
                canvas.clipRect(this.f17732x0);
            }
            CharSequence charSequence = this.Q;
            if (z4 && this.S0 != null) {
                charSequence = TextUtils.ellipsize(charSequence, this.A0.e(), this.f17732x0.width(), this.S0);
            }
            CharSequence charSequence2 = charSequence;
            int length = charSequence2.length();
            PointF pointF = this.f17733y0;
            canvas.drawText(charSequence2, 0, length, pointF.x, pointF.y, this.A0.e());
            if (z4) {
                canvas.restoreToCount(i8);
            }
        }
    }

    private boolean S2() {
        return this.f17715g0 && this.f17716h0 != null && this.H0;
    }

    private boolean T2() {
        return this.R && this.T != null;
    }

    private boolean U2() {
        return this.Z && this.f17709a0 != null;
    }

    private void V2(Drawable drawable) {
        if (drawable != null) {
            drawable.setCallback(null);
        }
    }

    private void W2() {
        this.Q0 = this.P0 ? b.d(this.P) : null;
    }

    @TargetApi(21)
    private void X2() {
        this.f17710b0 = new RippleDrawable(b.d(l1()), this.f17709a0, X0);
    }

    private float f1() {
        Drawable drawable = this.H0 ? this.f17716h0 : this.T;
        float f5 = this.X;
        if (f5 <= 0.0f && drawable != null) {
            f5 = (float) Math.ceil(s.c(this.f17728t0, 24));
            if (drawable.getIntrinsicHeight() <= f5) {
                return drawable.getIntrinsicHeight();
            }
        }
        return f5;
    }

    private float g1() {
        Drawable drawable = this.H0 ? this.f17716h0 : this.T;
        float f5 = this.X;
        return (f5 > 0.0f || drawable == null) ? f5 : drawable.getIntrinsicWidth();
    }

    private void h2(ColorStateList colorStateList) {
        if (this.F != colorStateList) {
            this.F = colorStateList;
            onStateChange(getState());
        }
    }

    private void q0(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        drawable.setCallback(this);
        androidx.core.graphics.drawable.a.m(drawable, androidx.core.graphics.drawable.a.f(this));
        drawable.setLevel(getLevel());
        drawable.setVisible(isVisible(), false);
        if (drawable == this.f17709a0) {
            if (drawable.isStateful()) {
                drawable.setState(c1());
            }
            androidx.core.graphics.drawable.a.o(drawable, this.f17711c0);
            return;
        }
        if (drawable.isStateful()) {
            drawable.setState(getState());
        }
        Drawable drawable2 = this.T;
        if (drawable == drawable2 && this.Y) {
            androidx.core.graphics.drawable.a.o(drawable2, this.W);
        }
    }

    private void r0(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (T2() || S2()) {
            float f5 = this.f17720l0 + this.f17721m0;
            float g12 = g1();
            if (androidx.core.graphics.drawable.a.f(this) == 0) {
                float f8 = rect.left + f5;
                rectF.left = f8;
                rectF.right = f8 + g12;
            } else {
                float f9 = rect.right - f5;
                rectF.right = f9;
                rectF.left = f9 - g12;
            }
            float f12 = f1();
            float exactCenterY = rect.exactCenterY() - (f12 / 2.0f);
            rectF.top = exactCenterY;
            rectF.bottom = exactCenterY + f12;
        }
    }

    private ColorFilter r1() {
        ColorFilter colorFilter = this.K0;
        return colorFilter != null ? colorFilter : this.L0;
    }

    private void t0(Rect rect, RectF rectF) {
        rectF.set(rect);
        if (U2()) {
            float f5 = this.f17727s0 + this.f17726r0 + this.f17712d0 + this.f17725q0 + this.f17724p0;
            if (androidx.core.graphics.drawable.a.f(this) == 0) {
                rectF.right = rect.right - f5;
            } else {
                rectF.left = rect.left + f5;
            }
        }
    }

    private static boolean t1(int[] iArr, int i8) {
        if (iArr == null) {
            return false;
        }
        for (int i9 : iArr) {
            if (i9 == i8) {
                return true;
            }
        }
        return false;
    }

    private void u0(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (U2()) {
            float f5 = this.f17727s0 + this.f17726r0;
            if (androidx.core.graphics.drawable.a.f(this) == 0) {
                float f8 = rect.right - f5;
                rectF.right = f8;
                rectF.left = f8 - this.f17712d0;
            } else {
                float f9 = rect.left + f5;
                rectF.left = f9;
                rectF.right = f9 + this.f17712d0;
            }
            float exactCenterY = rect.exactCenterY();
            float f10 = this.f17712d0;
            float f11 = exactCenterY - (f10 / 2.0f);
            rectF.top = f11;
            rectF.bottom = f11 + f10;
        }
    }

    private void v0(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (U2()) {
            float f5 = this.f17727s0 + this.f17726r0 + this.f17712d0 + this.f17725q0 + this.f17724p0;
            if (androidx.core.graphics.drawable.a.f(this) == 0) {
                float f8 = rect.right;
                rectF.right = f8;
                rectF.left = f8 - f5;
            } else {
                int i8 = rect.left;
                rectF.left = i8;
                rectF.right = i8 + f5;
            }
            rectF.top = rect.top;
            rectF.bottom = rect.bottom;
        }
    }

    private void x0(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (this.Q != null) {
            float s02 = this.f17720l0 + s0() + this.f17723o0;
            float w02 = this.f17727s0 + w0() + this.f17724p0;
            if (androidx.core.graphics.drawable.a.f(this) == 0) {
                rectF.left = rect.left + s02;
                rectF.right = rect.right - w02;
            } else {
                rectF.left = rect.left + w02;
                rectF.right = rect.right - s02;
            }
            rectF.top = rect.top;
            rectF.bottom = rect.bottom;
        }
    }

    private static boolean x1(ColorStateList colorStateList) {
        return colorStateList != null && colorStateList.isStateful();
    }

    private float y0() {
        this.A0.e().getFontMetrics(this.f17731w0);
        Paint.FontMetrics fontMetrics = this.f17731w0;
        return (fontMetrics.descent + fontMetrics.ascent) / 2.0f;
    }

    private static boolean y1(Drawable drawable) {
        return drawable != null && drawable.isStateful();
    }

    private static boolean z1(d dVar) {
        ColorStateList colorStateList;
        return (dVar == null || (colorStateList = dVar.f23093a) == null || !colorStateList.isStateful()) ? false : true;
    }

    public void A2(int i8) {
        z2(this.f17728t0.getResources().getDimension(i8));
    }

    protected void B1() {
        InterfaceC0130a interfaceC0130a = this.R0.get();
        if (interfaceC0130a != null) {
            interfaceC0130a.a();
        }
    }

    public void B2(float f5) {
        if (this.f17721m0 != f5) {
            float s02 = s0();
            this.f17721m0 = f5;
            float s03 = s0();
            invalidateSelf();
            if (s02 != s03) {
                B1();
            }
        }
    }

    public void C2(int i8) {
        B2(this.f17728t0.getResources().getDimension(i8));
    }

    public void D1(boolean z4) {
        if (this.f17714f0 != z4) {
            this.f17714f0 = z4;
            float s02 = s0();
            if (!z4 && this.H0) {
                this.H0 = false;
            }
            float s03 = s0();
            invalidateSelf();
            if (s02 != s03) {
                B1();
            }
        }
    }

    public void D2(int i8) {
        this.U0 = i8;
    }

    public void E1(int i8) {
        D1(this.f17728t0.getResources().getBoolean(i8));
    }

    public void E2(ColorStateList colorStateList) {
        if (this.P != colorStateList) {
            this.P = colorStateList;
            W2();
            onStateChange(getState());
        }
    }

    public void F1(Drawable drawable) {
        if (this.f17716h0 != drawable) {
            float s02 = s0();
            this.f17716h0 = drawable;
            float s03 = s0();
            V2(this.f17716h0);
            q0(this.f17716h0);
            invalidateSelf();
            if (s02 != s03) {
                B1();
            }
        }
    }

    public void F2(int i8) {
        E2(h.a.a(this.f17728t0, i8));
    }

    public void G1(int i8) {
        F1(h.a.b(this.f17728t0, i8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void G2(boolean z4) {
        this.T0 = z4;
    }

    public void H1(ColorStateList colorStateList) {
        if (this.f17717i0 != colorStateList) {
            this.f17717i0 = colorStateList;
            if (A0()) {
                androidx.core.graphics.drawable.a.o(this.f17716h0, colorStateList);
            }
            onStateChange(getState());
        }
    }

    public void H2(l7.h hVar) {
        this.f17718j0 = hVar;
    }

    public void I1(int i8) {
        H1(h.a.a(this.f17728t0, i8));
    }

    public void I2(int i8) {
        H2(l7.h.d(this.f17728t0, i8));
    }

    public void J1(int i8) {
        K1(this.f17728t0.getResources().getBoolean(i8));
    }

    public void J2(CharSequence charSequence) {
        if (charSequence == null) {
            charSequence = BuildConfig.FLAVOR;
        }
        if (TextUtils.equals(this.Q, charSequence)) {
            return;
        }
        this.Q = charSequence;
        this.A0.i(true);
        invalidateSelf();
        B1();
    }

    public void K1(boolean z4) {
        if (this.f17715g0 != z4) {
            boolean S2 = S2();
            this.f17715g0 = z4;
            boolean S22 = S2();
            if (S2 != S22) {
                if (S22) {
                    q0(this.f17716h0);
                } else {
                    V2(this.f17716h0);
                }
                invalidateSelf();
                B1();
            }
        }
    }

    public void K2(d dVar) {
        this.A0.h(dVar, this.f17728t0);
    }

    public Drawable L0() {
        return this.f17716h0;
    }

    public void L1(ColorStateList colorStateList) {
        if (this.G != colorStateList) {
            this.G = colorStateList;
            onStateChange(getState());
        }
    }

    public void L2(int i8) {
        K2(new d(this.f17728t0, i8));
    }

    public ColorStateList M0() {
        return this.f17717i0;
    }

    public void M1(int i8) {
        L1(h.a.a(this.f17728t0, i8));
    }

    public void M2(float f5) {
        if (this.f17724p0 != f5) {
            this.f17724p0 = f5;
            invalidateSelf();
            B1();
        }
    }

    public ColorStateList N0() {
        return this.G;
    }

    @Deprecated
    public void N1(float f5) {
        if (this.K != f5) {
            this.K = f5;
            setShapeAppearanceModel(D().w(f5));
        }
    }

    public void N2(int i8) {
        M2(this.f17728t0.getResources().getDimension(i8));
    }

    public float O0() {
        return this.V0 ? I() : this.K;
    }

    @Deprecated
    public void O1(int i8) {
        N1(this.f17728t0.getResources().getDimension(i8));
    }

    public void O2(float f5) {
        if (this.f17723o0 != f5) {
            this.f17723o0 = f5;
            invalidateSelf();
            B1();
        }
    }

    public float P0() {
        return this.f17727s0;
    }

    public void P1(float f5) {
        if (this.f17727s0 != f5) {
            this.f17727s0 = f5;
            invalidateSelf();
            B1();
        }
    }

    public void P2(int i8) {
        O2(this.f17728t0.getResources().getDimension(i8));
    }

    public Drawable Q0() {
        Drawable drawable = this.T;
        if (drawable != null) {
            return androidx.core.graphics.drawable.a.q(drawable);
        }
        return null;
    }

    public void Q1(int i8) {
        P1(this.f17728t0.getResources().getDimension(i8));
    }

    public void Q2(boolean z4) {
        if (this.P0 != z4) {
            this.P0 = z4;
            W2();
            onStateChange(getState());
        }
    }

    public float R0() {
        return this.X;
    }

    public void R1(Drawable drawable) {
        Drawable Q0 = Q0();
        if (Q0 != drawable) {
            float s02 = s0();
            this.T = drawable != null ? androidx.core.graphics.drawable.a.r(drawable).mutate() : null;
            float s03 = s0();
            V2(Q0);
            if (T2()) {
                q0(this.T);
            }
            invalidateSelf();
            if (s02 != s03) {
                B1();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean R2() {
        return this.T0;
    }

    public ColorStateList S0() {
        return this.W;
    }

    public void S1(int i8) {
        R1(h.a.b(this.f17728t0, i8));
    }

    public float T0() {
        return this.H;
    }

    public void T1(float f5) {
        if (this.X != f5) {
            float s02 = s0();
            this.X = f5;
            float s03 = s0();
            invalidateSelf();
            if (s02 != s03) {
                B1();
            }
        }
    }

    public float U0() {
        return this.f17720l0;
    }

    public void U1(int i8) {
        T1(this.f17728t0.getResources().getDimension(i8));
    }

    public ColorStateList V0() {
        return this.L;
    }

    public void V1(ColorStateList colorStateList) {
        this.Y = true;
        if (this.W != colorStateList) {
            this.W = colorStateList;
            if (T2()) {
                androidx.core.graphics.drawable.a.o(this.T, colorStateList);
            }
            onStateChange(getState());
        }
    }

    public float W0() {
        return this.O;
    }

    public void W1(int i8) {
        V1(h.a.a(this.f17728t0, i8));
    }

    public Drawable X0() {
        Drawable drawable = this.f17709a0;
        if (drawable != null) {
            return androidx.core.graphics.drawable.a.q(drawable);
        }
        return null;
    }

    public void X1(int i8) {
        Y1(this.f17728t0.getResources().getBoolean(i8));
    }

    public CharSequence Y0() {
        return this.f17713e0;
    }

    public void Y1(boolean z4) {
        if (this.R != z4) {
            boolean T2 = T2();
            this.R = z4;
            boolean T22 = T2();
            if (T2 != T22) {
                if (T22) {
                    q0(this.T);
                } else {
                    V2(this.T);
                }
                invalidateSelf();
                B1();
            }
        }
    }

    public float Z0() {
        return this.f17726r0;
    }

    public void Z1(float f5) {
        if (this.H != f5) {
            this.H = f5;
            invalidateSelf();
            B1();
        }
    }

    @Override // com.google.android.material.internal.j.b
    public void a() {
        B1();
        invalidateSelf();
    }

    public float a1() {
        return this.f17712d0;
    }

    public void a2(int i8) {
        Z1(this.f17728t0.getResources().getDimension(i8));
    }

    public float b1() {
        return this.f17725q0;
    }

    public void b2(float f5) {
        if (this.f17720l0 != f5) {
            this.f17720l0 = f5;
            invalidateSelf();
            B1();
        }
    }

    public int[] c1() {
        return this.O0;
    }

    public void c2(int i8) {
        b2(this.f17728t0.getResources().getDimension(i8));
    }

    public ColorStateList d1() {
        return this.f17711c0;
    }

    public void d2(ColorStateList colorStateList) {
        if (this.L != colorStateList) {
            this.L = colorStateList;
            if (this.V0) {
                l0(colorStateList);
            }
            onStateChange(getState());
        }
    }

    @Override // x7.h, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        if (bounds.isEmpty() || getAlpha() == 0) {
            return;
        }
        int i8 = this.J0;
        int a9 = i8 < 255 ? m7.a.a(canvas, bounds.left, bounds.top, bounds.right, bounds.bottom, i8) : 0;
        G0(canvas, bounds);
        D0(canvas, bounds);
        if (this.V0) {
            super.draw(canvas);
        }
        F0(canvas, bounds);
        I0(canvas, bounds);
        E0(canvas, bounds);
        C0(canvas, bounds);
        if (this.T0) {
            K0(canvas, bounds);
        }
        H0(canvas, bounds);
        J0(canvas, bounds);
        if (this.J0 < 255) {
            canvas.restoreToCount(a9);
        }
    }

    public void e1(RectF rectF) {
        v0(getBounds(), rectF);
    }

    public void e2(int i8) {
        d2(h.a.a(this.f17728t0, i8));
    }

    public void f2(float f5) {
        if (this.O != f5) {
            this.O = f5;
            this.f17729u0.setStrokeWidth(f5);
            if (this.V0) {
                super.m0(f5);
            }
            invalidateSelf();
        }
    }

    public void g2(int i8) {
        f2(this.f17728t0.getResources().getDimension(i8));
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.J0;
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        return this.K0;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return (int) this.H;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return Math.min(Math.round(this.f17720l0 + s0() + this.f17723o0 + this.A0.f(n1().toString()) + this.f17724p0 + w0() + this.f17727s0), this.U0);
    }

    @Override // x7.h, android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // x7.h, android.graphics.drawable.Drawable
    @TargetApi(21)
    public void getOutline(Outline outline) {
        if (this.V0) {
            super.getOutline(outline);
            return;
        }
        Rect bounds = getBounds();
        if (bounds.isEmpty()) {
            outline.setRoundRect(0, 0, getIntrinsicWidth(), getIntrinsicHeight(), this.K);
        } else {
            outline.setRoundRect(bounds, this.K);
        }
        outline.setAlpha(getAlpha() / 255.0f);
    }

    public TextUtils.TruncateAt h1() {
        return this.S0;
    }

    public l7.h i1() {
        return this.f17719k0;
    }

    public void i2(Drawable drawable) {
        Drawable X02 = X0();
        if (X02 != drawable) {
            float w02 = w0();
            this.f17709a0 = drawable != null ? androidx.core.graphics.drawable.a.r(drawable).mutate() : null;
            if (b.f23352a) {
                X2();
            }
            float w03 = w0();
            V2(X02);
            if (U2()) {
                q0(this.f17709a0);
            }
            invalidateSelf();
            if (w02 != w03) {
                B1();
            }
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override // x7.h, android.graphics.drawable.Drawable
    public boolean isStateful() {
        return x1(this.F) || x1(this.G) || x1(this.L) || (this.P0 && x1(this.Q0)) || z1(this.A0.d()) || A0() || y1(this.T) || y1(this.f17716h0) || x1(this.M0);
    }

    public float j1() {
        return this.f17722n0;
    }

    public void j2(CharSequence charSequence) {
        if (this.f17713e0 != charSequence) {
            this.f17713e0 = androidx.core.text.a.c().h(charSequence);
            invalidateSelf();
        }
    }

    public float k1() {
        return this.f17721m0;
    }

    public void k2(float f5) {
        if (this.f17726r0 != f5) {
            this.f17726r0 = f5;
            invalidateSelf();
            if (U2()) {
                B1();
            }
        }
    }

    public ColorStateList l1() {
        return this.P;
    }

    public void l2(int i8) {
        k2(this.f17728t0.getResources().getDimension(i8));
    }

    public l7.h m1() {
        return this.f17718j0;
    }

    public void m2(int i8) {
        i2(h.a.b(this.f17728t0, i8));
    }

    public CharSequence n1() {
        return this.Q;
    }

    public void n2(float f5) {
        if (this.f17712d0 != f5) {
            this.f17712d0 = f5;
            invalidateSelf();
            if (U2()) {
                B1();
            }
        }
    }

    public d o1() {
        return this.A0.d();
    }

    public void o2(int i8) {
        n2(this.f17728t0.getResources().getDimension(i8));
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onLayoutDirectionChanged(int i8) {
        boolean onLayoutDirectionChanged = super.onLayoutDirectionChanged(i8);
        if (T2()) {
            onLayoutDirectionChanged |= androidx.core.graphics.drawable.a.m(this.T, i8);
        }
        if (S2()) {
            onLayoutDirectionChanged |= androidx.core.graphics.drawable.a.m(this.f17716h0, i8);
        }
        if (U2()) {
            onLayoutDirectionChanged |= androidx.core.graphics.drawable.a.m(this.f17709a0, i8);
        }
        if (onLayoutDirectionChanged) {
            invalidateSelf();
            return true;
        }
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int i8) {
        boolean onLevelChange = super.onLevelChange(i8);
        if (T2()) {
            onLevelChange |= this.T.setLevel(i8);
        }
        if (S2()) {
            onLevelChange |= this.f17716h0.setLevel(i8);
        }
        if (U2()) {
            onLevelChange |= this.f17709a0.setLevel(i8);
        }
        if (onLevelChange) {
            invalidateSelf();
        }
        return onLevelChange;
    }

    @Override // x7.h, android.graphics.drawable.Drawable, com.google.android.material.internal.j.b
    public boolean onStateChange(int[] iArr) {
        if (this.V0) {
            super.onStateChange(iArr);
        }
        return C1(iArr, c1());
    }

    public float p1() {
        return this.f17724p0;
    }

    public void p2(float f5) {
        if (this.f17725q0 != f5) {
            this.f17725q0 = f5;
            invalidateSelf();
            if (U2()) {
                B1();
            }
        }
    }

    public float q1() {
        return this.f17723o0;
    }

    public void q2(int i8) {
        p2(this.f17728t0.getResources().getDimension(i8));
    }

    public boolean r2(int[] iArr) {
        if (Arrays.equals(this.O0, iArr)) {
            return false;
        }
        this.O0 = iArr;
        if (U2()) {
            return C1(getState(), iArr);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float s0() {
        if (T2() || S2()) {
            return this.f17721m0 + g1() + this.f17722n0;
        }
        return 0.0f;
    }

    public boolean s1() {
        return this.P0;
    }

    public void s2(ColorStateList colorStateList) {
        if (this.f17711c0 != colorStateList) {
            this.f17711c0 = colorStateList;
            if (U2()) {
                androidx.core.graphics.drawable.a.o(this.f17709a0, colorStateList);
            }
            onStateChange(getState());
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j8) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, runnable, j8);
        }
    }

    @Override // x7.h, android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        if (this.J0 != i8) {
            this.J0 = i8;
            invalidateSelf();
        }
    }

    @Override // x7.h, android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        if (this.K0 != colorFilter) {
            this.K0 = colorFilter;
            invalidateSelf();
        }
    }

    @Override // x7.h, android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTintList(ColorStateList colorStateList) {
        if (this.M0 != colorStateList) {
            this.M0 = colorStateList;
            onStateChange(getState());
        }
    }

    @Override // x7.h, android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTintMode(PorterDuff.Mode mode) {
        if (this.N0 != mode) {
            this.N0 = mode;
            this.L0 = p7.a.b(this, this.M0, mode);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z4, boolean z8) {
        boolean visible = super.setVisible(z4, z8);
        if (T2()) {
            visible |= this.T.setVisible(z4, z8);
        }
        if (S2()) {
            visible |= this.f17716h0.setVisible(z4, z8);
        }
        if (U2()) {
            visible |= this.f17709a0.setVisible(z4, z8);
        }
        if (visible) {
            invalidateSelf();
        }
        return visible;
    }

    public void t2(int i8) {
        s2(h.a.a(this.f17728t0, i8));
    }

    public boolean u1() {
        return this.f17714f0;
    }

    public void u2(boolean z4) {
        if (this.Z != z4) {
            boolean U2 = U2();
            this.Z = z4;
            boolean U22 = U2();
            if (U2 != U22) {
                if (U22) {
                    q0(this.f17709a0);
                } else {
                    V2(this.f17709a0);
                }
                invalidateSelf();
                B1();
            }
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, runnable);
        }
    }

    public boolean v1() {
        return y1(this.f17709a0);
    }

    public void v2(InterfaceC0130a interfaceC0130a) {
        this.R0 = new WeakReference<>(interfaceC0130a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float w0() {
        if (U2()) {
            return this.f17725q0 + this.f17712d0 + this.f17726r0;
        }
        return 0.0f;
    }

    public boolean w1() {
        return this.Z;
    }

    public void w2(TextUtils.TruncateAt truncateAt) {
        this.S0 = truncateAt;
    }

    public void x2(l7.h hVar) {
        this.f17719k0 = hVar;
    }

    public void y2(int i8) {
        x2(l7.h.d(this.f17728t0, i8));
    }

    Paint.Align z0(Rect rect, PointF pointF) {
        pointF.set(0.0f, 0.0f);
        Paint.Align align = Paint.Align.LEFT;
        if (this.Q != null) {
            float s02 = this.f17720l0 + s0() + this.f17723o0;
            if (androidx.core.graphics.drawable.a.f(this) == 0) {
                pointF.x = rect.left + s02;
                align = Paint.Align.LEFT;
            } else {
                pointF.x = rect.right - s02;
                align = Paint.Align.RIGHT;
            }
            pointF.y = rect.centerY() - y0();
        }
        return align;
    }

    public void z2(float f5) {
        if (this.f17722n0 != f5) {
            float s02 = s0();
            this.f17722n0 = f5;
            float s03 = s0();
            invalidateSelf();
            if (s02 != s03) {
                B1();
            }
        }
    }
}
