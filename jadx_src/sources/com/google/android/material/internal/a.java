package com.google.android.material.internal;

import android.animation.TimeInterpolator;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.core.view.c0;
import com.google.android.material.internal.i;
import u7.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: j0  reason: collision with root package name */
    private static final boolean f18048j0;

    /* renamed from: k0  reason: collision with root package name */
    private static final Paint f18049k0;
    private u7.a A;
    private CharSequence B;
    private CharSequence C;
    private boolean D;
    private boolean F;
    private Bitmap G;
    private Paint H;
    private float I;
    private float J;
    private int[] K;
    private boolean L;
    private final TextPaint M;
    private final TextPaint N;
    private TimeInterpolator O;
    private TimeInterpolator P;
    private float Q;
    private float R;
    private float S;
    private ColorStateList T;
    private float U;
    private float V;
    private float W;
    private ColorStateList X;
    private float Y;
    private float Z;

    /* renamed from: a  reason: collision with root package name */
    private final View f18050a;

    /* renamed from: a0  reason: collision with root package name */
    private StaticLayout f18051a0;

    /* renamed from: b  reason: collision with root package name */
    private boolean f18052b;

    /* renamed from: b0  reason: collision with root package name */
    private float f18053b0;

    /* renamed from: c  reason: collision with root package name */
    private float f18054c;

    /* renamed from: c0  reason: collision with root package name */
    private float f18055c0;

    /* renamed from: d  reason: collision with root package name */
    private boolean f18056d;

    /* renamed from: d0  reason: collision with root package name */
    private float f18057d0;

    /* renamed from: e  reason: collision with root package name */
    private float f18058e;

    /* renamed from: e0  reason: collision with root package name */
    private CharSequence f18059e0;

    /* renamed from: f  reason: collision with root package name */
    private float f18060f;

    /* renamed from: g  reason: collision with root package name */
    private int f18062g;

    /* renamed from: h  reason: collision with root package name */
    private final Rect f18064h;

    /* renamed from: i  reason: collision with root package name */
    private final Rect f18066i;

    /* renamed from: j  reason: collision with root package name */
    private final RectF f18068j;

    /* renamed from: o  reason: collision with root package name */
    private ColorStateList f18073o;

    /* renamed from: p  reason: collision with root package name */
    private ColorStateList f18074p;
    private float q;

    /* renamed from: r  reason: collision with root package name */
    private float f18075r;

    /* renamed from: s  reason: collision with root package name */
    private float f18076s;

    /* renamed from: t  reason: collision with root package name */
    private float f18077t;

    /* renamed from: u  reason: collision with root package name */
    private float f18078u;

    /* renamed from: v  reason: collision with root package name */
    private float f18079v;

    /* renamed from: w  reason: collision with root package name */
    private Typeface f18080w;

    /* renamed from: x  reason: collision with root package name */
    private Typeface f18081x;

    /* renamed from: y  reason: collision with root package name */
    private Typeface f18082y;

    /* renamed from: z  reason: collision with root package name */
    private u7.a f18083z;

    /* renamed from: k  reason: collision with root package name */
    private int f18069k = 16;

    /* renamed from: l  reason: collision with root package name */
    private int f18070l = 16;

    /* renamed from: m  reason: collision with root package name */
    private float f18071m = 15.0f;

    /* renamed from: n  reason: collision with root package name */
    private float f18072n = 15.0f;
    private boolean E = true;

    /* renamed from: f0  reason: collision with root package name */
    private int f18061f0 = 1;

    /* renamed from: g0  reason: collision with root package name */
    private float f18063g0 = 0.0f;

    /* renamed from: h0  reason: collision with root package name */
    private float f18065h0 = 1.0f;

    /* renamed from: i0  reason: collision with root package name */
    private int f18067i0 = i.f18124n;

    /* renamed from: com.google.android.material.internal.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class C0135a implements a.InterfaceC0213a {
        C0135a() {
        }

        @Override // u7.a.InterfaceC0213a
        public void a(Typeface typeface) {
            a.this.d0(typeface);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements a.InterfaceC0213a {
        b() {
        }

        @Override // u7.a.InterfaceC0213a
        public void a(Typeface typeface) {
            a.this.n0(typeface);
        }
    }

    static {
        f18048j0 = Build.VERSION.SDK_INT < 18;
        f18049k0 = null;
    }

    public a(View view) {
        this.f18050a = view;
        TextPaint textPaint = new TextPaint(129);
        this.M = textPaint;
        this.N = new TextPaint(textPaint);
        this.f18066i = new Rect();
        this.f18064h = new Rect();
        this.f18068j = new RectF();
        this.f18060f = f();
    }

    private boolean D0() {
        return this.f18061f0 > 1 && (!this.D || this.f18056d) && !this.F;
    }

    private void L(TextPaint textPaint) {
        textPaint.setTextSize(this.f18072n);
        textPaint.setTypeface(this.f18080w);
        if (Build.VERSION.SDK_INT >= 21) {
            textPaint.setLetterSpacing(this.Y);
        }
    }

    private void M(TextPaint textPaint) {
        textPaint.setTextSize(this.f18071m);
        textPaint.setTypeface(this.f18081x);
        if (Build.VERSION.SDK_INT >= 21) {
            textPaint.setLetterSpacing(this.Z);
        }
    }

    private void N(float f5) {
        if (this.f18056d) {
            this.f18068j.set(f5 < this.f18060f ? this.f18064h : this.f18066i);
            return;
        }
        this.f18068j.left = S(this.f18064h.left, this.f18066i.left, f5, this.O);
        this.f18068j.top = S(this.q, this.f18075r, f5, this.O);
        this.f18068j.right = S(this.f18064h.right, this.f18066i.right, f5, this.O);
        this.f18068j.bottom = S(this.f18064h.bottom, this.f18066i.bottom, f5, this.O);
    }

    private static boolean O(float f5, float f8) {
        return Math.abs(f5 - f8) < 0.001f;
    }

    private boolean P() {
        return c0.E(this.f18050a) == 1;
    }

    private boolean R(CharSequence charSequence, boolean z4) {
        return (z4 ? androidx.core.text.e.f4871d : androidx.core.text.e.f4870c).a(charSequence, 0, charSequence.length());
    }

    private static float S(float f5, float f8, float f9, TimeInterpolator timeInterpolator) {
        if (timeInterpolator != null) {
            f9 = timeInterpolator.getInterpolation(f9);
        }
        return l7.a.a(f5, f8, f9);
    }

    private static boolean W(Rect rect, int i8, int i9, int i10, int i11) {
        return rect.left == i8 && rect.top == i9 && rect.right == i10 && rect.bottom == i11;
    }

    private static int a(int i8, int i9, float f5) {
        float f8 = 1.0f - f5;
        return Color.argb((int) ((Color.alpha(i8) * f8) + (Color.alpha(i9) * f5)), (int) ((Color.red(i8) * f8) + (Color.red(i9) * f5)), (int) ((Color.green(i8) * f8) + (Color.green(i9) * f5)), (int) ((Color.blue(i8) * f8) + (Color.blue(i9) * f5)));
    }

    private void a0(float f5) {
        this.f18053b0 = f5;
        c0.j0(this.f18050a);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00a8  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00b3  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0116  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void b(boolean r13) {
        /*
            Method dump skipped, instructions count: 296
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.internal.a.b(boolean):void");
    }

    private void d() {
        h(this.f18054c);
    }

    private float e(float f5) {
        float f8 = this.f18060f;
        return f5 <= f8 ? l7.a.b(1.0f, 0.0f, this.f18058e, f8, f5) : l7.a.b(0.0f, 1.0f, f8, 1.0f, f5);
    }

    private boolean e0(Typeface typeface) {
        u7.a aVar = this.A;
        if (aVar != null) {
            aVar.c();
        }
        if (this.f18080w != typeface) {
            this.f18080w = typeface;
            return true;
        }
        return false;
    }

    private float f() {
        float f5 = this.f18058e;
        return f5 + ((1.0f - f5) * 0.5f);
    }

    private boolean g(CharSequence charSequence) {
        boolean P = P();
        return this.E ? R(charSequence, P) : P;
    }

    private void h(float f5) {
        float f8;
        N(f5);
        if (!this.f18056d) {
            this.f18078u = S(this.f18076s, this.f18077t, f5, this.O);
            this.f18079v = S(this.q, this.f18075r, f5, this.O);
            t0(S(this.f18071m, this.f18072n, f5, this.P));
            f8 = f5;
        } else if (f5 < this.f18060f) {
            this.f18078u = this.f18076s;
            this.f18079v = this.q;
            t0(this.f18071m);
            f8 = 0.0f;
        } else {
            this.f18078u = this.f18077t;
            this.f18079v = this.f18075r - Math.max(0, this.f18062g);
            t0(this.f18072n);
            f8 = 1.0f;
        }
        TimeInterpolator timeInterpolator = l7.a.f21787b;
        a0(1.0f - S(0.0f, 1.0f, 1.0f - f5, timeInterpolator));
        j0(S(1.0f, 0.0f, f5, timeInterpolator));
        if (this.f18074p != this.f18073o) {
            this.M.setColor(a(y(), w(), f8));
        } else {
            this.M.setColor(w());
        }
        if (Build.VERSION.SDK_INT >= 21) {
            float f9 = this.Y;
            float f10 = this.Z;
            if (f9 != f10) {
                this.M.setLetterSpacing(S(f10, f9, f5, timeInterpolator));
            } else {
                this.M.setLetterSpacing(f9);
            }
        }
        this.M.setShadowLayer(S(this.U, this.Q, f5, null), S(this.V, this.R, f5, null), S(this.W, this.S, f5, null), a(x(this.X), x(this.T), f5));
        if (this.f18056d) {
            this.M.setAlpha((int) (e(f5) * 255.0f));
        }
        c0.j0(this.f18050a);
    }

    private void i(float f5) {
        j(f5, false);
    }

    private void j(float f5, boolean z4) {
        boolean z8;
        float f8;
        boolean z9;
        if (this.B == null) {
            return;
        }
        float width = this.f18066i.width();
        float width2 = this.f18064h.width();
        if (O(f5, this.f18072n)) {
            f8 = this.f18072n;
            this.I = 1.0f;
            Typeface typeface = this.f18082y;
            Typeface typeface2 = this.f18080w;
            if (typeface != typeface2) {
                this.f18082y = typeface2;
                z9 = true;
            } else {
                z9 = false;
            }
        } else {
            float f9 = this.f18071m;
            Typeface typeface3 = this.f18082y;
            Typeface typeface4 = this.f18081x;
            if (typeface3 != typeface4) {
                this.f18082y = typeface4;
                z8 = true;
            } else {
                z8 = false;
            }
            if (O(f5, f9)) {
                this.I = 1.0f;
            } else {
                this.I = f5 / this.f18071m;
            }
            float f10 = this.f18072n / this.f18071m;
            width = (!z4 && width2 * f10 > width) ? Math.min(width / f10, width2) : width2;
            f8 = f9;
            z9 = z8;
        }
        if (width > 0.0f) {
            z9 = this.J != f8 || this.L || z9;
            this.J = f8;
            this.L = false;
        }
        if (this.C == null || z9) {
            this.M.setTextSize(this.J);
            this.M.setTypeface(this.f18082y);
            this.M.setLinearText(this.I != 1.0f);
            this.D = g(this.B);
            StaticLayout l8 = l(D0() ? this.f18061f0 : 1, width, this.D);
            this.f18051a0 = l8;
            this.C = l8.getText();
        }
    }

    private void j0(float f5) {
        this.f18055c0 = f5;
        c0.j0(this.f18050a);
    }

    private void k() {
        Bitmap bitmap = this.G;
        if (bitmap != null) {
            bitmap.recycle();
            this.G = null;
        }
    }

    private StaticLayout l(int i8, float f5, boolean z4) {
        StaticLayout staticLayout;
        try {
            staticLayout = i.c(this.B, this.M, (int) f5).e(TextUtils.TruncateAt.END).h(z4).d(Layout.Alignment.ALIGN_NORMAL).g(false).j(i8).i(this.f18063g0, this.f18065h0).f(this.f18067i0).a();
        } catch (i.a e8) {
            Log.e("CollapsingTextHelper", e8.getCause().getMessage(), e8);
            staticLayout = null;
        }
        return (StaticLayout) androidx.core.util.h.h(staticLayout);
    }

    private void n(Canvas canvas, float f5, float f8) {
        int alpha = this.M.getAlpha();
        canvas.translate(f5, f8);
        float f9 = alpha;
        this.M.setAlpha((int) (this.f18055c0 * f9));
        this.f18051a0.draw(canvas);
        this.M.setAlpha((int) (this.f18053b0 * f9));
        int lineBaseline = this.f18051a0.getLineBaseline(0);
        CharSequence charSequence = this.f18059e0;
        float f10 = lineBaseline;
        canvas.drawText(charSequence, 0, charSequence.length(), 0.0f, f10, this.M);
        if (this.f18056d) {
            return;
        }
        String trim = this.f18059e0.toString().trim();
        if (trim.endsWith("…")) {
            trim = trim.substring(0, trim.length() - 1);
        }
        String str = trim;
        this.M.setAlpha(alpha);
        canvas.drawText(str, 0, Math.min(this.f18051a0.getLineEnd(0), str.length()), 0.0f, f10, (Paint) this.M);
    }

    private void o() {
        if (this.G != null || this.f18064h.isEmpty() || TextUtils.isEmpty(this.C)) {
            return;
        }
        h(0.0f);
        int width = this.f18051a0.getWidth();
        int height = this.f18051a0.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        this.G = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.f18051a0.draw(new Canvas(this.G));
        if (this.H == null) {
            this.H = new Paint(3);
        }
    }

    private boolean o0(Typeface typeface) {
        u7.a aVar = this.f18083z;
        if (aVar != null) {
            aVar.c();
        }
        if (this.f18081x != typeface) {
            this.f18081x = typeface;
            return true;
        }
        return false;
    }

    private float t(int i8, int i9) {
        return (i9 == 17 || (i9 & 7) == 1) ? (i8 / 2.0f) - (c() / 2.0f) : ((i9 & 8388613) == 8388613 || (i9 & 5) == 5) ? this.D ? this.f18066i.left : this.f18066i.right - c() : this.D ? this.f18066i.right - c() : this.f18066i.left;
    }

    private void t0(float f5) {
        i(f5);
        boolean z4 = f18048j0 && this.I != 1.0f;
        this.F = z4;
        if (z4) {
            o();
        }
        c0.j0(this.f18050a);
    }

    private float u(RectF rectF, int i8, int i9) {
        return (i9 == 17 || (i9 & 7) == 1) ? (i8 / 2.0f) + (c() / 2.0f) : ((i9 & 8388613) == 8388613 || (i9 & 5) == 5) ? this.D ? rectF.left + c() : this.f18066i.right : this.D ? this.f18066i.right : rectF.left + c();
    }

    private int x(ColorStateList colorStateList) {
        if (colorStateList == null) {
            return 0;
        }
        int[] iArr = this.K;
        return iArr != null ? colorStateList.getColorForState(iArr, 0) : colorStateList.getDefaultColor();
    }

    private int y() {
        return x(this.f18073o);
    }

    public int A() {
        return this.f18069k;
    }

    public void A0(CharSequence charSequence) {
        if (charSequence == null || !TextUtils.equals(this.B, charSequence)) {
            this.B = charSequence;
            this.C = null;
            k();
            U();
        }
    }

    public float B() {
        M(this.N);
        return -this.N.ascent();
    }

    public void B0(TimeInterpolator timeInterpolator) {
        this.P = timeInterpolator;
        U();
    }

    public Typeface C() {
        Typeface typeface = this.f18081x;
        return typeface != null ? typeface : Typeface.DEFAULT;
    }

    public void C0(Typeface typeface) {
        boolean e02 = e0(typeface);
        boolean o02 = o0(typeface);
        if (e02 || o02) {
            U();
        }
    }

    public float D() {
        return this.f18054c;
    }

    public float E() {
        return this.f18060f;
    }

    public int F() {
        return this.f18067i0;
    }

    public int G() {
        StaticLayout staticLayout = this.f18051a0;
        if (staticLayout != null) {
            return staticLayout.getLineCount();
        }
        return 0;
    }

    public float H() {
        return this.f18051a0.getSpacingAdd();
    }

    public float I() {
        return this.f18051a0.getSpacingMultiplier();
    }

    public int J() {
        return this.f18061f0;
    }

    public CharSequence K() {
        return this.B;
    }

    public final boolean Q() {
        ColorStateList colorStateList;
        ColorStateList colorStateList2 = this.f18074p;
        return (colorStateList2 != null && colorStateList2.isStateful()) || ((colorStateList = this.f18073o) != null && colorStateList.isStateful());
    }

    void T() {
        this.f18052b = this.f18066i.width() > 0 && this.f18066i.height() > 0 && this.f18064h.width() > 0 && this.f18064h.height() > 0;
    }

    public void U() {
        V(false);
    }

    public void V(boolean z4) {
        if ((this.f18050a.getHeight() <= 0 || this.f18050a.getWidth() <= 0) && !z4) {
            return;
        }
        b(z4);
        d();
    }

    public void X(int i8, int i9, int i10, int i11) {
        if (W(this.f18066i, i8, i9, i10, i11)) {
            return;
        }
        this.f18066i.set(i8, i9, i10, i11);
        this.L = true;
        T();
    }

    public void Y(Rect rect) {
        X(rect.left, rect.top, rect.right, rect.bottom);
    }

    public void Z(int i8) {
        u7.d dVar = new u7.d(this.f18050a.getContext(), i8);
        ColorStateList colorStateList = dVar.f23093a;
        if (colorStateList != null) {
            this.f18074p = colorStateList;
        }
        float f5 = dVar.f23106n;
        if (f5 != 0.0f) {
            this.f18072n = f5;
        }
        ColorStateList colorStateList2 = dVar.f23096d;
        if (colorStateList2 != null) {
            this.T = colorStateList2;
        }
        this.R = dVar.f23101i;
        this.S = dVar.f23102j;
        this.Q = dVar.f23103k;
        this.Y = dVar.f23105m;
        u7.a aVar = this.A;
        if (aVar != null) {
            aVar.c();
        }
        this.A = new u7.a(new C0135a(), dVar.e());
        dVar.h(this.f18050a.getContext(), this.A);
        U();
    }

    public void b0(ColorStateList colorStateList) {
        if (this.f18074p != colorStateList) {
            this.f18074p = colorStateList;
            U();
        }
    }

    public float c() {
        if (this.B == null) {
            return 0.0f;
        }
        L(this.N);
        TextPaint textPaint = this.N;
        CharSequence charSequence = this.B;
        return textPaint.measureText(charSequence, 0, charSequence.length());
    }

    public void c0(int i8) {
        if (this.f18070l != i8) {
            this.f18070l = i8;
            U();
        }
    }

    public void d0(Typeface typeface) {
        if (e0(typeface)) {
            U();
        }
    }

    public void f0(int i8) {
        this.f18062g = i8;
    }

    public void g0(int i8, int i9, int i10, int i11) {
        if (W(this.f18064h, i8, i9, i10, i11)) {
            return;
        }
        this.f18064h.set(i8, i9, i10, i11);
        this.L = true;
        T();
    }

    public void h0(Rect rect) {
        g0(rect.left, rect.top, rect.right, rect.bottom);
    }

    public void i0(int i8) {
        u7.d dVar = new u7.d(this.f18050a.getContext(), i8);
        ColorStateList colorStateList = dVar.f23093a;
        if (colorStateList != null) {
            this.f18073o = colorStateList;
        }
        float f5 = dVar.f23106n;
        if (f5 != 0.0f) {
            this.f18071m = f5;
        }
        ColorStateList colorStateList2 = dVar.f23096d;
        if (colorStateList2 != null) {
            this.X = colorStateList2;
        }
        this.V = dVar.f23101i;
        this.W = dVar.f23102j;
        this.U = dVar.f23103k;
        this.Z = dVar.f23105m;
        u7.a aVar = this.f18083z;
        if (aVar != null) {
            aVar.c();
        }
        this.f18083z = new u7.a(new b(), dVar.e());
        dVar.h(this.f18050a.getContext(), this.f18083z);
        U();
    }

    public void k0(ColorStateList colorStateList) {
        if (this.f18073o != colorStateList) {
            this.f18073o = colorStateList;
            U();
        }
    }

    public void l0(int i8) {
        if (this.f18069k != i8) {
            this.f18069k = i8;
            U();
        }
    }

    public void m(Canvas canvas) {
        int save = canvas.save();
        if (this.C == null || !this.f18052b) {
            return;
        }
        boolean z4 = true;
        float lineStart = (this.f18078u + (this.f18061f0 > 1 ? this.f18051a0.getLineStart(0) : this.f18051a0.getLineLeft(0))) - (this.f18057d0 * 2.0f);
        this.M.setTextSize(this.J);
        float f5 = this.f18078u;
        float f8 = this.f18079v;
        if (!this.F || this.G == null) {
            z4 = false;
        }
        float f9 = this.I;
        if (f9 != 1.0f && !this.f18056d) {
            canvas.scale(f9, f9, f5, f8);
        }
        if (z4) {
            canvas.drawBitmap(this.G, f5, f8, this.H);
            canvas.restoreToCount(save);
            return;
        }
        if (!D0() || (this.f18056d && this.f18054c <= this.f18060f)) {
            canvas.translate(f5, f8);
            this.f18051a0.draw(canvas);
        } else {
            n(canvas, lineStart, f8);
        }
        canvas.restoreToCount(save);
    }

    public void m0(float f5) {
        if (this.f18071m != f5) {
            this.f18071m = f5;
            U();
        }
    }

    public void n0(Typeface typeface) {
        if (o0(typeface)) {
            U();
        }
    }

    public void p(RectF rectF, int i8, int i9) {
        this.D = g(this.B);
        rectF.left = t(i8, i9);
        rectF.top = this.f18066i.top;
        rectF.right = u(rectF, i8, i9);
        rectF.bottom = this.f18066i.top + s();
    }

    public void p0(float f5) {
        float b9 = t0.a.b(f5, 0.0f, 1.0f);
        if (b9 != this.f18054c) {
            this.f18054c = b9;
            d();
        }
    }

    public ColorStateList q() {
        return this.f18074p;
    }

    public void q0(boolean z4) {
        this.f18056d = z4;
    }

    public int r() {
        return this.f18070l;
    }

    public void r0(float f5) {
        this.f18058e = f5;
        this.f18060f = f();
    }

    public float s() {
        L(this.N);
        return -this.N.ascent();
    }

    public void s0(int i8) {
        this.f18067i0 = i8;
    }

    public void u0(float f5) {
        this.f18063g0 = f5;
    }

    public Typeface v() {
        Typeface typeface = this.f18080w;
        return typeface != null ? typeface : Typeface.DEFAULT;
    }

    public void v0(float f5) {
        this.f18065h0 = f5;
    }

    public int w() {
        return x(this.f18074p);
    }

    public void w0(int i8) {
        if (i8 != this.f18061f0) {
            this.f18061f0 = i8;
            k();
            U();
        }
    }

    public void x0(TimeInterpolator timeInterpolator) {
        this.O = timeInterpolator;
        U();
    }

    public void y0(boolean z4) {
        this.E = z4;
    }

    public float z() {
        M(this.N);
        return (-this.N.ascent()) + this.N.descent();
    }

    public final boolean z0(int[] iArr) {
        this.K = iArr;
        if (Q()) {
            U();
            return true;
        }
        return false;
    }
}
