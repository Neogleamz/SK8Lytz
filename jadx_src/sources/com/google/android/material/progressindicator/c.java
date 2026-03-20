package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c extends g<CircularProgressIndicatorSpec> {

    /* renamed from: c  reason: collision with root package name */
    private int f18269c;

    /* renamed from: d  reason: collision with root package name */
    private float f18270d;

    /* renamed from: e  reason: collision with root package name */
    private float f18271e;

    /* renamed from: f  reason: collision with root package name */
    private float f18272f;

    public c(CircularProgressIndicatorSpec circularProgressIndicatorSpec) {
        super(circularProgressIndicatorSpec);
        this.f18269c = 1;
    }

    private void h(Canvas canvas, Paint paint, float f5, float f8, float f9, boolean z4, RectF rectF) {
        float f10 = z4 ? -1.0f : 1.0f;
        canvas.save();
        canvas.rotate(f9);
        float f11 = f5 / 2.0f;
        float f12 = f10 * f8;
        canvas.drawRect((this.f18272f - f11) + f8, Math.min(0.0f, this.f18269c * f12), (this.f18272f + f11) - f8, Math.max(0.0f, f12 * this.f18269c), paint);
        canvas.translate((this.f18272f - f11) + f8, 0.0f);
        canvas.drawArc(rectF, 180.0f, (-f10) * 90.0f * this.f18269c, true, paint);
        canvas.translate(f5 - (f8 * 2.0f), 0.0f);
        canvas.drawArc(rectF, 0.0f, f10 * 90.0f * this.f18269c, true, paint);
        canvas.restore();
    }

    private int i() {
        S s8 = this.f18309a;
        return ((CircularProgressIndicatorSpec) s8).f18238g + (((CircularProgressIndicatorSpec) s8).f18239h * 2);
    }

    @Override // com.google.android.material.progressindicator.g
    public void a(Canvas canvas, float f5) {
        float f8;
        S s8 = this.f18309a;
        float f9 = (((CircularProgressIndicatorSpec) s8).f18238g / 2.0f) + ((CircularProgressIndicatorSpec) s8).f18239h;
        canvas.translate(f9, f9);
        canvas.rotate(-90.0f);
        float f10 = -f9;
        canvas.clipRect(f10, f10, f9, f9);
        S s9 = this.f18309a;
        this.f18269c = ((CircularProgressIndicatorSpec) s9).f18240i == 0 ? 1 : -1;
        this.f18270d = ((CircularProgressIndicatorSpec) s9).f18263a * f5;
        this.f18271e = ((CircularProgressIndicatorSpec) s9).f18264b * f5;
        this.f18272f = (((CircularProgressIndicatorSpec) s9).f18238g - ((CircularProgressIndicatorSpec) s9).f18263a) / 2.0f;
        if ((this.f18310b.j() && ((CircularProgressIndicatorSpec) this.f18309a).f18267e == 2) || (this.f18310b.i() && ((CircularProgressIndicatorSpec) this.f18309a).f18268f == 1)) {
            f8 = this.f18272f + (((1.0f - f5) * ((CircularProgressIndicatorSpec) this.f18309a).f18263a) / 2.0f);
        } else if ((!this.f18310b.j() || ((CircularProgressIndicatorSpec) this.f18309a).f18267e != 1) && (!this.f18310b.i() || ((CircularProgressIndicatorSpec) this.f18309a).f18268f != 2)) {
            return;
        } else {
            f8 = this.f18272f - (((1.0f - f5) * ((CircularProgressIndicatorSpec) this.f18309a).f18263a) / 2.0f);
        }
        this.f18272f = f8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.g
    public void b(Canvas canvas, Paint paint, float f5, float f8, int i8) {
        if (f5 == f8) {
            return;
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setAntiAlias(true);
        paint.setColor(i8);
        paint.setStrokeWidth(this.f18270d);
        int i9 = this.f18269c;
        float f9 = f5 * 360.0f * i9;
        float f10 = (f8 >= f5 ? f8 - f5 : (f8 + 1.0f) - f5) * 360.0f * i9;
        float f11 = this.f18272f;
        canvas.drawArc(new RectF(-f11, -f11, f11, f11), f9, f10, false, paint);
        if (this.f18271e <= 0.0f || Math.abs(f10) >= 360.0f) {
            return;
        }
        paint.setStyle(Paint.Style.FILL);
        float f12 = this.f18271e;
        RectF rectF = new RectF(-f12, -f12, f12, f12);
        h(canvas, paint, this.f18270d, this.f18271e, f9, true, rectF);
        h(canvas, paint, this.f18270d, this.f18271e, f9 + f10, false, rectF);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.g
    public void c(Canvas canvas, Paint paint) {
        int a9 = n7.a.a(((CircularProgressIndicatorSpec) this.f18309a).f18266d, this.f18310b.getAlpha());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setAntiAlias(true);
        paint.setColor(a9);
        paint.setStrokeWidth(this.f18270d);
        float f5 = this.f18272f;
        canvas.drawArc(new RectF(-f5, -f5, f5, f5), 0.0f, 360.0f, false, paint);
    }

    @Override // com.google.android.material.progressindicator.g
    public int d() {
        return i();
    }

    @Override // com.google.android.material.progressindicator.g
    public int e() {
        return i();
    }
}
