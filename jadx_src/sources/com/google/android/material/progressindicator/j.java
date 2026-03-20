package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class j extends g<LinearProgressIndicatorSpec> {

    /* renamed from: c  reason: collision with root package name */
    private float f18316c;

    /* renamed from: d  reason: collision with root package name */
    private float f18317d;

    /* renamed from: e  reason: collision with root package name */
    private float f18318e;

    public j(LinearProgressIndicatorSpec linearProgressIndicatorSpec) {
        super(linearProgressIndicatorSpec);
        this.f18316c = 300.0f;
    }

    private static void h(Canvas canvas, Paint paint, float f5, float f8, float f9, boolean z4, RectF rectF) {
        canvas.save();
        canvas.translate(f9, 0.0f);
        if (!z4) {
            canvas.rotate(180.0f);
        }
        float f10 = ((-f5) / 2.0f) + f8;
        float f11 = (f5 / 2.0f) - f8;
        canvas.drawRect(-f8, f10, 0.0f, f11, paint);
        canvas.save();
        canvas.translate(0.0f, f10);
        canvas.drawArc(rectF, 180.0f, 90.0f, true, paint);
        canvas.restore();
        canvas.translate(0.0f, f11);
        canvas.drawArc(rectF, 180.0f, -90.0f, true, paint);
        canvas.restore();
    }

    @Override // com.google.android.material.progressindicator.g
    public void a(Canvas canvas, float f5) {
        Rect clipBounds = canvas.getClipBounds();
        this.f18316c = clipBounds.width();
        float f8 = ((LinearProgressIndicatorSpec) this.f18309a).f18263a;
        canvas.translate(clipBounds.left + (clipBounds.width() / 2.0f), clipBounds.top + (clipBounds.height() / 2.0f) + Math.max(0.0f, (clipBounds.height() - ((LinearProgressIndicatorSpec) this.f18309a).f18263a) / 2.0f));
        if (((LinearProgressIndicatorSpec) this.f18309a).f18244i) {
            canvas.scale(-1.0f, 1.0f);
        }
        if ((this.f18310b.j() && ((LinearProgressIndicatorSpec) this.f18309a).f18267e == 1) || (this.f18310b.i() && ((LinearProgressIndicatorSpec) this.f18309a).f18268f == 2)) {
            canvas.scale(1.0f, -1.0f);
        }
        if (this.f18310b.j() || this.f18310b.i()) {
            canvas.translate(0.0f, (((LinearProgressIndicatorSpec) this.f18309a).f18263a * (f5 - 1.0f)) / 2.0f);
        }
        float f9 = this.f18316c;
        canvas.clipRect((-f9) / 2.0f, (-f8) / 2.0f, f9 / 2.0f, f8 / 2.0f);
        S s8 = this.f18309a;
        this.f18317d = ((LinearProgressIndicatorSpec) s8).f18263a * f5;
        this.f18318e = ((LinearProgressIndicatorSpec) s8).f18264b * f5;
    }

    @Override // com.google.android.material.progressindicator.g
    public void b(Canvas canvas, Paint paint, float f5, float f8, int i8) {
        if (f5 == f8) {
            return;
        }
        float f9 = this.f18316c;
        float f10 = this.f18318e;
        float f11 = ((-f9) / 2.0f) + f10 + ((f9 - (f10 * 2.0f)) * f5);
        float f12 = ((-f9) / 2.0f) + f10 + ((f9 - (f10 * 2.0f)) * f8);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(i8);
        float f13 = this.f18317d;
        canvas.drawRect(f11, (-f13) / 2.0f, f12, f13 / 2.0f, paint);
        float f14 = this.f18318e;
        RectF rectF = new RectF(-f14, -f14, f14, f14);
        h(canvas, paint, this.f18317d, this.f18318e, f11, true, rectF);
        h(canvas, paint, this.f18317d, this.f18318e, f12, false, rectF);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.g
    public void c(Canvas canvas, Paint paint) {
        int a9 = n7.a.a(((LinearProgressIndicatorSpec) this.f18309a).f18266d, this.f18310b.getAlpha());
        float f5 = ((-this.f18316c) / 2.0f) + this.f18318e;
        float f8 = -f5;
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(a9);
        float f9 = this.f18317d;
        canvas.drawRect(f5, (-f9) / 2.0f, f8, f9 / 2.0f, paint);
        float f10 = this.f18318e;
        RectF rectF = new RectF(-f10, -f10, f10, f10);
        h(canvas, paint, this.f18317d, this.f18318e, f5, true, rectF);
        h(canvas, paint, this.f18317d, this.f18318e, f8, false, rectF);
    }

    @Override // com.google.android.material.progressindicator.g
    public int d() {
        return ((LinearProgressIndicatorSpec) this.f18309a).f18263a;
    }

    @Override // com.google.android.material.progressindicator.g
    public int e() {
        return -1;
    }
}
