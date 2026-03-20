package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class n extends l {

    /* renamed from: d  reason: collision with root package name */
    private final SeekBar f1519d;

    /* renamed from: e  reason: collision with root package name */
    private Drawable f1520e;

    /* renamed from: f  reason: collision with root package name */
    private ColorStateList f1521f;

    /* renamed from: g  reason: collision with root package name */
    private PorterDuff.Mode f1522g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f1523h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f1524i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(SeekBar seekBar) {
        super(seekBar);
        this.f1521f = null;
        this.f1522g = null;
        this.f1523h = false;
        this.f1524i = false;
        this.f1519d = seekBar;
    }

    private void f() {
        Drawable drawable = this.f1520e;
        if (drawable != null) {
            if (this.f1523h || this.f1524i) {
                Drawable r4 = androidx.core.graphics.drawable.a.r(drawable.mutate());
                this.f1520e = r4;
                if (this.f1523h) {
                    androidx.core.graphics.drawable.a.o(r4, this.f1521f);
                }
                if (this.f1524i) {
                    androidx.core.graphics.drawable.a.p(this.f1520e, this.f1522g);
                }
                if (this.f1520e.isStateful()) {
                    this.f1520e.setState(this.f1519d.getDrawableState());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.appcompat.widget.l
    public void c(AttributeSet attributeSet, int i8) {
        super.c(attributeSet, i8);
        Context context = this.f1519d.getContext();
        int[] iArr = g.j.V;
        j0 v8 = j0.v(context, attributeSet, iArr, i8, 0);
        SeekBar seekBar = this.f1519d;
        androidx.core.view.c0.r0(seekBar, seekBar.getContext(), iArr, attributeSet, v8.r(), i8, 0);
        Drawable h8 = v8.h(g.j.W);
        if (h8 != null) {
            this.f1519d.setThumb(h8);
        }
        j(v8.g(g.j.X));
        int i9 = g.j.Z;
        if (v8.s(i9)) {
            this.f1522g = t.e(v8.k(i9, -1), this.f1522g);
            this.f1524i = true;
        }
        int i10 = g.j.Y;
        if (v8.s(i10)) {
            this.f1521f = v8.c(i10);
            this.f1523h = true;
        }
        v8.w();
        f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(Canvas canvas) {
        if (this.f1520e != null) {
            int max = this.f1519d.getMax();
            if (max > 1) {
                int intrinsicWidth = this.f1520e.getIntrinsicWidth();
                int intrinsicHeight = this.f1520e.getIntrinsicHeight();
                int i8 = intrinsicWidth >= 0 ? intrinsicWidth / 2 : 1;
                int i9 = intrinsicHeight >= 0 ? intrinsicHeight / 2 : 1;
                this.f1520e.setBounds(-i8, -i9, i8, i9);
                float width = ((this.f1519d.getWidth() - this.f1519d.getPaddingLeft()) - this.f1519d.getPaddingRight()) / max;
                int save = canvas.save();
                canvas.translate(this.f1519d.getPaddingLeft(), this.f1519d.getHeight() / 2);
                for (int i10 = 0; i10 <= max; i10++) {
                    this.f1520e.draw(canvas);
                    canvas.translate(width, 0.0f);
                }
                canvas.restoreToCount(save);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h() {
        Drawable drawable = this.f1520e;
        if (drawable != null && drawable.isStateful() && drawable.setState(this.f1519d.getDrawableState())) {
            this.f1519d.invalidateDrawable(drawable);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i() {
        Drawable drawable = this.f1520e;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    void j(Drawable drawable) {
        Drawable drawable2 = this.f1520e;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.f1520e = drawable;
        if (drawable != null) {
            drawable.setCallback(this.f1519d);
            androidx.core.graphics.drawable.a.m(drawable, androidx.core.view.c0.E(this.f1519d));
            if (drawable.isStateful()) {
                drawable.setState(this.f1519d.getDrawableState());
            }
            f();
        }
        this.f1519d.invalidate();
    }
}
