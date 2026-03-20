package com.google.android.material.imageview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.AppCompatImageView;
import k7.k;
import x7.h;
import x7.m;
import x7.n;
import x7.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ShapeableImageView extends AppCompatImageView implements p {
    private static final int B = k.H;
    private boolean A;

    /* renamed from: d  reason: collision with root package name */
    private final n f18006d;

    /* renamed from: e  reason: collision with root package name */
    private final RectF f18007e;

    /* renamed from: f  reason: collision with root package name */
    private final RectF f18008f;

    /* renamed from: g  reason: collision with root package name */
    private final Paint f18009g;

    /* renamed from: h  reason: collision with root package name */
    private final Paint f18010h;

    /* renamed from: j  reason: collision with root package name */
    private final Path f18011j;

    /* renamed from: k  reason: collision with root package name */
    private ColorStateList f18012k;

    /* renamed from: l  reason: collision with root package name */
    private h f18013l;

    /* renamed from: m  reason: collision with root package name */
    private m f18014m;

    /* renamed from: n  reason: collision with root package name */
    private float f18015n;

    /* renamed from: p  reason: collision with root package name */
    private Path f18016p;
    private int q;

    /* renamed from: t  reason: collision with root package name */
    private int f18017t;

    /* renamed from: w  reason: collision with root package name */
    private int f18018w;

    /* renamed from: x  reason: collision with root package name */
    private int f18019x;

    /* renamed from: y  reason: collision with root package name */
    private int f18020y;

    /* renamed from: z  reason: collision with root package name */
    private int f18021z;

    @TargetApi(21)
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends ViewOutlineProvider {

        /* renamed from: a  reason: collision with root package name */
        private final Rect f18022a = new Rect();

        a() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            if (ShapeableImageView.this.f18014m == null) {
                return;
            }
            if (ShapeableImageView.this.f18013l == null) {
                ShapeableImageView.this.f18013l = new h(ShapeableImageView.this.f18014m);
            }
            ShapeableImageView.this.f18007e.round(this.f18022a);
            ShapeableImageView.this.f18013l.setBounds(this.f18022a);
            ShapeableImageView.this.f18013l.getOutline(outline);
        }
    }

    public ShapeableImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ShapeableImageView(android.content.Context r7, android.util.AttributeSet r8, int r9) {
        /*
            r6 = this;
            int r0 = com.google.android.material.imageview.ShapeableImageView.B
            android.content.Context r7 = y7.a.c(r7, r8, r9, r0)
            r6.<init>(r7, r8, r9)
            x7.n r7 = x7.n.k()
            r6.f18006d = r7
            android.graphics.Path r7 = new android.graphics.Path
            r7.<init>()
            r6.f18011j = r7
            r7 = 0
            r6.A = r7
            android.content.Context r1 = r6.getContext()
            android.graphics.Paint r2 = new android.graphics.Paint
            r2.<init>()
            r6.f18010h = r2
            r3 = 1
            r2.setAntiAlias(r3)
            r4 = -1
            r2.setColor(r4)
            android.graphics.PorterDuffXfermode r4 = new android.graphics.PorterDuffXfermode
            android.graphics.PorterDuff$Mode r5 = android.graphics.PorterDuff.Mode.DST_OUT
            r4.<init>(r5)
            r2.setXfermode(r4)
            android.graphics.RectF r2 = new android.graphics.RectF
            r2.<init>()
            r6.f18007e = r2
            android.graphics.RectF r2 = new android.graphics.RectF
            r2.<init>()
            r6.f18008f = r2
            android.graphics.Path r2 = new android.graphics.Path
            r2.<init>()
            r6.f18016p = r2
            int[] r2 = k7.l.f21441u6
            android.content.res.TypedArray r2 = r1.obtainStyledAttributes(r8, r2, r9, r0)
            int r4 = k7.l.C6
            android.content.res.ColorStateList r4 = u7.c.a(r1, r2, r4)
            r6.f18012k = r4
            int r4 = k7.l.D6
            int r4 = r2.getDimensionPixelSize(r4, r7)
            float r4 = (float) r4
            r6.f18015n = r4
            int r4 = k7.l.f21450v6
            int r7 = r2.getDimensionPixelSize(r4, r7)
            r6.q = r7
            r6.f18017t = r7
            r6.f18018w = r7
            r6.f18019x = r7
            int r4 = k7.l.f21477y6
            int r4 = r2.getDimensionPixelSize(r4, r7)
            r6.q = r4
            int r4 = k7.l.B6
            int r4 = r2.getDimensionPixelSize(r4, r7)
            r6.f18017t = r4
            int r4 = k7.l.f21485z6
            int r4 = r2.getDimensionPixelSize(r4, r7)
            r6.f18018w = r4
            int r4 = k7.l.f21459w6
            int r7 = r2.getDimensionPixelSize(r4, r7)
            r6.f18019x = r7
            int r7 = k7.l.A6
            r4 = -2147483648(0xffffffff80000000, float:-0.0)
            int r7 = r2.getDimensionPixelSize(r7, r4)
            r6.f18020y = r7
            int r7 = k7.l.f21468x6
            int r7 = r2.getDimensionPixelSize(r7, r4)
            r6.f18021z = r7
            r2.recycle()
            android.graphics.Paint r7 = new android.graphics.Paint
            r7.<init>()
            r6.f18009g = r7
            android.graphics.Paint$Style r2 = android.graphics.Paint.Style.STROKE
            r7.setStyle(r2)
            r7.setAntiAlias(r3)
            x7.m$b r7 = x7.m.e(r1, r8, r9, r0)
            x7.m r7 = r7.m()
            r6.f18014m = r7
            int r7 = android.os.Build.VERSION.SDK_INT
            r8 = 21
            if (r7 < r8) goto Lcc
            com.google.android.material.imageview.ShapeableImageView$a r7 = new com.google.android.material.imageview.ShapeableImageView$a
            r7.<init>()
            r6.setOutlineProvider(r7)
        Lcc:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.imageview.ShapeableImageView.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private void g(Canvas canvas) {
        if (this.f18012k == null) {
            return;
        }
        this.f18009g.setStrokeWidth(this.f18015n);
        int colorForState = this.f18012k.getColorForState(getDrawableState(), this.f18012k.getDefaultColor());
        if (this.f18015n <= 0.0f || colorForState == 0) {
            return;
        }
        this.f18009g.setColor(colorForState);
        canvas.drawPath(this.f18011j, this.f18009g);
    }

    private boolean h() {
        return (this.f18020y == Integer.MIN_VALUE && this.f18021z == Integer.MIN_VALUE) ? false : true;
    }

    private boolean i() {
        return Build.VERSION.SDK_INT >= 17 && getLayoutDirection() == 1;
    }

    private void j(int i8, int i9) {
        this.f18007e.set(getPaddingLeft(), getPaddingTop(), i8 - getPaddingRight(), i9 - getPaddingBottom());
        this.f18006d.d(this.f18014m, 1.0f, this.f18007e, this.f18011j);
        this.f18016p.rewind();
        this.f18016p.addPath(this.f18011j);
        this.f18008f.set(0.0f, 0.0f, i8, i9);
        this.f18016p.addRect(this.f18008f, Path.Direction.CCW);
    }

    public int getContentPaddingBottom() {
        return this.f18019x;
    }

    public final int getContentPaddingEnd() {
        int i8 = this.f18021z;
        return i8 != Integer.MIN_VALUE ? i8 : i() ? this.q : this.f18018w;
    }

    public int getContentPaddingLeft() {
        int i8;
        int i9;
        if (h()) {
            if (i() && (i9 = this.f18021z) != Integer.MIN_VALUE) {
                return i9;
            }
            if (!i() && (i8 = this.f18020y) != Integer.MIN_VALUE) {
                return i8;
            }
        }
        return this.q;
    }

    public int getContentPaddingRight() {
        int i8;
        int i9;
        if (h()) {
            if (i() && (i9 = this.f18020y) != Integer.MIN_VALUE) {
                return i9;
            }
            if (!i() && (i8 = this.f18021z) != Integer.MIN_VALUE) {
                return i8;
            }
        }
        return this.f18018w;
    }

    public final int getContentPaddingStart() {
        int i8 = this.f18020y;
        return i8 != Integer.MIN_VALUE ? i8 : i() ? this.f18018w : this.q;
    }

    public int getContentPaddingTop() {
        return this.f18017t;
    }

    @Override // android.view.View
    public int getPaddingBottom() {
        return super.getPaddingBottom() - getContentPaddingBottom();
    }

    @Override // android.view.View
    public int getPaddingEnd() {
        return super.getPaddingEnd() - getContentPaddingEnd();
    }

    @Override // android.view.View
    public int getPaddingLeft() {
        return super.getPaddingLeft() - getContentPaddingLeft();
    }

    @Override // android.view.View
    public int getPaddingRight() {
        return super.getPaddingRight() - getContentPaddingRight();
    }

    @Override // android.view.View
    public int getPaddingStart() {
        return super.getPaddingStart() - getContentPaddingStart();
    }

    @Override // android.view.View
    public int getPaddingTop() {
        return super.getPaddingTop() - getContentPaddingTop();
    }

    public m getShapeAppearanceModel() {
        return this.f18014m;
    }

    public ColorStateList getStrokeColor() {
        return this.f18012k;
    }

    public float getStrokeWidth() {
        return this.f18015n;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setLayerType(2, null);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        setLayerType(0, null);
        super.onDetachedFromWindow();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(this.f18016p, this.f18010h);
        g(canvas);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i8, int i9) {
        super.onMeasure(i8, i9);
        if (this.A) {
            return;
        }
        int i10 = Build.VERSION.SDK_INT;
        if (i10 <= 19 || isLayoutDirectionResolved()) {
            this.A = true;
            if (i10 < 21 || !(isPaddingRelative() || h())) {
                setPadding(super.getPaddingLeft(), super.getPaddingTop(), super.getPaddingRight(), super.getPaddingBottom());
            } else {
                setPaddingRelative(super.getPaddingStart(), super.getPaddingTop(), super.getPaddingEnd(), super.getPaddingBottom());
            }
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i8, int i9, int i10, int i11) {
        super.onSizeChanged(i8, i9, i10, i11);
        j(i8, i9);
    }

    @Override // android.view.View
    public void setPadding(int i8, int i9, int i10, int i11) {
        super.setPadding(i8 + getContentPaddingLeft(), i9 + getContentPaddingTop(), i10 + getContentPaddingRight(), i11 + getContentPaddingBottom());
    }

    @Override // android.view.View
    public void setPaddingRelative(int i8, int i9, int i10, int i11) {
        super.setPaddingRelative(i8 + getContentPaddingStart(), i9 + getContentPaddingTop(), i10 + getContentPaddingEnd(), i11 + getContentPaddingBottom());
    }

    @Override // x7.p
    public void setShapeAppearanceModel(m mVar) {
        this.f18014m = mVar;
        h hVar = this.f18013l;
        if (hVar != null) {
            hVar.setShapeAppearanceModel(mVar);
        }
        j(getWidth(), getHeight());
        invalidate();
        if (Build.VERSION.SDK_INT >= 21) {
            invalidateOutline();
        }
    }

    public void setStrokeColor(ColorStateList colorStateList) {
        this.f18012k = colorStateList;
        invalidate();
    }

    public void setStrokeColorResource(int i8) {
        setStrokeColor(h.a.a(getContext(), i8));
    }

    public void setStrokeWidth(float f5) {
        if (this.f18015n != f5) {
            this.f18015n = f5;
            invalidate();
        }
    }

    public void setStrokeWidthResource(int i8) {
        setStrokeWidth(getResources().getDimensionPixelSize(i8));
    }
}
