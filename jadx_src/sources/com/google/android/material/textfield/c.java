package com.google.android.material.textfield;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import x7.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class c extends x7.h {
    private final Paint F;
    private final RectF G;
    private int H;

    c() {
        this(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(m mVar) {
        super(mVar == null ? new m() : mVar);
        this.F = new Paint(1);
        x0();
        this.G = new RectF();
    }

    private void r0(Canvas canvas) {
        if (y0(getCallback())) {
            return;
        }
        canvas.restoreToCount(this.H);
    }

    private void s0(Canvas canvas) {
        Drawable.Callback callback = getCallback();
        if (!y0(callback)) {
            u0(canvas);
            return;
        }
        View view = (View) callback;
        if (view.getLayerType() != 2) {
            view.setLayerType(2, null);
        }
    }

    private void u0(Canvas canvas) {
        this.H = Build.VERSION.SDK_INT >= 21 ? canvas.saveLayer(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight(), null) : canvas.saveLayer(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight(), null, 31);
    }

    private void x0() {
        this.F.setStyle(Paint.Style.FILL_AND_STROKE);
        this.F.setColor(-1);
        this.F.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    private boolean y0(Drawable.Callback callback) {
        return callback instanceof View;
    }

    @Override // x7.h, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        s0(canvas);
        super.draw(canvas);
        canvas.drawRect(this.G, this.F);
        r0(canvas);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean q0() {
        return !this.G.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t0() {
        v0(0.0f, 0.0f, 0.0f, 0.0f);
    }

    void v0(float f5, float f8, float f9, float f10) {
        RectF rectF = this.G;
        if (f5 == rectF.left && f8 == rectF.top && f9 == rectF.right && f10 == rectF.bottom) {
            return;
        }
        rectF.set(f5, f8, f9, f10);
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w0(RectF rectF) {
        v0(rectF.left, rectF.top, rectF.right, rectF.bottom);
    }
}
