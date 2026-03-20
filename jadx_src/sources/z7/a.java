package z7;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.google.android.material.internal.j;
import com.google.android.material.internal.m;
import k7.b;
import k7.d;
import k7.k;
import k7.l;
import u7.c;
import x7.f;
import x7.g;
import x7.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends h implements j.b {

    /* renamed from: d0  reason: collision with root package name */
    private static final int f24772d0 = k.L;

    /* renamed from: e0  reason: collision with root package name */
    private static final int f24773e0 = b.f21051c0;
    private CharSequence F;
    private final Context G;
    private final Paint.FontMetrics H;
    private final j K;
    private final View.OnLayoutChangeListener L;
    private final Rect O;
    private int P;
    private int Q;
    private int R;
    private int T;
    private int W;
    private int X;
    private float Y;
    private float Z;

    /* renamed from: a0  reason: collision with root package name */
    private final float f24774a0;

    /* renamed from: b0  reason: collision with root package name */
    private float f24775b0;

    /* renamed from: c0  reason: collision with root package name */
    private float f24776c0;

    /* renamed from: z7.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class View$OnLayoutChangeListenerC0237a implements View.OnLayoutChangeListener {
        View$OnLayoutChangeListenerC0237a() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15) {
            a.this.E0(view);
        }
    }

    private a(Context context, AttributeSet attributeSet, int i8, int i9) {
        super(context, attributeSet, i8, i9);
        this.H = new Paint.FontMetrics();
        j jVar = new j(this);
        this.K = jVar;
        this.L = new View$OnLayoutChangeListenerC0237a();
        this.O = new Rect();
        this.Y = 1.0f;
        this.Z = 1.0f;
        this.f24774a0 = 0.5f;
        this.f24775b0 = 0.5f;
        this.f24776c0 = 1.0f;
        this.G = context;
        jVar.e().density = context.getResources().getDisplayMetrics().density;
        jVar.e().setTextAlign(Paint.Align.CENTER);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void E0(View view) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        this.X = iArr[0];
        view.getWindowVisibleDisplayFrame(this.O);
    }

    private float r0() {
        int i8;
        if (((this.O.right - getBounds().right) - this.X) - this.T < 0) {
            i8 = ((this.O.right - getBounds().right) - this.X) - this.T;
        } else if (((this.O.left - getBounds().left) - this.X) + this.T <= 0) {
            return 0.0f;
        } else {
            i8 = ((this.O.left - getBounds().left) - this.X) + this.T;
        }
        return i8;
    }

    private float s0() {
        this.K.e().getFontMetrics(this.H);
        Paint.FontMetrics fontMetrics = this.H;
        return (fontMetrics.descent + fontMetrics.ascent) / 2.0f;
    }

    private float t0(Rect rect) {
        return rect.centerY() - s0();
    }

    public static a u0(Context context, AttributeSet attributeSet, int i8, int i9) {
        a aVar = new a(context, attributeSet, i8, i9);
        aVar.z0(attributeSet, i8, i9);
        return aVar;
    }

    private f v0() {
        float width = ((float) (getBounds().width() - (this.W * Math.sqrt(2.0d)))) / 2.0f;
        return new x7.j(new g(this.W), Math.min(Math.max(-r0(), -width), width));
    }

    private void x0(Canvas canvas) {
        if (this.F == null) {
            return;
        }
        Rect bounds = getBounds();
        int t02 = (int) t0(bounds);
        if (this.K.d() != null) {
            this.K.e().drawableState = getState();
            this.K.j(this.G);
            this.K.e().setAlpha((int) (this.f24776c0 * 255.0f));
        }
        CharSequence charSequence = this.F;
        canvas.drawText(charSequence, 0, charSequence.length(), bounds.centerX(), t02, this.K.e());
    }

    private float y0() {
        CharSequence charSequence = this.F;
        if (charSequence == null) {
            return 0.0f;
        }
        return this.K.f(charSequence.toString());
    }

    private void z0(AttributeSet attributeSet, int i8, int i9) {
        TypedArray h8 = m.h(this.G, attributeSet, l.E9, i8, i9, new int[0]);
        this.W = this.G.getResources().getDimensionPixelSize(d.f21140z0);
        setShapeAppearanceModel(D().v().s(v0()).m());
        C0(h8.getText(l.K9));
        D0(c.f(this.G, h8, l.F9));
        a0(ColorStateList.valueOf(h8.getColor(l.L9, n7.a.g(androidx.core.graphics.b.p(n7.a.c(this.G, 16842801, a.class.getCanonicalName()), 229), androidx.core.graphics.b.p(n7.a.c(this.G, b.f21063o, a.class.getCanonicalName()), 153)))));
        l0(ColorStateList.valueOf(n7.a.c(this.G, b.f21066s, a.class.getCanonicalName())));
        this.P = h8.getDimensionPixelSize(l.G9, 0);
        this.Q = h8.getDimensionPixelSize(l.I9, 0);
        this.R = h8.getDimensionPixelSize(l.J9, 0);
        this.T = h8.getDimensionPixelSize(l.H9, 0);
        h8.recycle();
    }

    public void A0(View view) {
        if (view == null) {
            return;
        }
        E0(view);
        view.addOnLayoutChangeListener(this.L);
    }

    public void B0(float f5) {
        this.f24775b0 = 1.2f;
        this.Y = f5;
        this.Z = f5;
        this.f24776c0 = l7.a.b(0.0f, 1.0f, 0.19f, 1.0f, f5);
        invalidateSelf();
    }

    public void C0(CharSequence charSequence) {
        if (TextUtils.equals(this.F, charSequence)) {
            return;
        }
        this.F = charSequence;
        this.K.i(true);
        invalidateSelf();
    }

    public void D0(u7.d dVar) {
        this.K.h(dVar, this.G);
    }

    @Override // com.google.android.material.internal.j.b
    public void a() {
        invalidateSelf();
    }

    @Override // x7.h, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.scale(this.Y, this.Z, getBounds().left + (getBounds().width() * 0.5f), getBounds().top + (getBounds().height() * this.f24775b0));
        canvas.translate(r0(), (float) (-((this.W * Math.sqrt(2.0d)) - this.W)));
        super.draw(canvas);
        x0(canvas);
        canvas.restore();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return (int) Math.max(this.K.e().getTextSize(), this.R);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return (int) Math.max((this.P * 2) + y0(), this.Q);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // x7.h, android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        setShapeAppearanceModel(D().v().s(v0()).m());
    }

    @Override // x7.h, android.graphics.drawable.Drawable, com.google.android.material.internal.j.b
    public boolean onStateChange(int[] iArr) {
        return super.onStateChange(iArr);
    }

    public void w0(View view) {
        if (view == null) {
            return;
        }
        view.removeOnLayoutChangeListener(this.L);
    }
}
