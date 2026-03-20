package com.google.android.material.progressindicator;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import com.google.android.material.progressindicator.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i<S extends b> extends f {

    /* renamed from: t  reason: collision with root package name */
    private g<S> f18314t;

    /* renamed from: w  reason: collision with root package name */
    private h<ObjectAnimator> f18315w;

    i(Context context, b bVar, g<S> gVar, h<ObjectAnimator> hVar) {
        super(context, bVar);
        x(gVar);
        w(hVar);
    }

    public static i<CircularProgressIndicatorSpec> s(Context context, CircularProgressIndicatorSpec circularProgressIndicatorSpec) {
        return new i<>(context, circularProgressIndicatorSpec, new c(circularProgressIndicatorSpec), new d(circularProgressIndicatorSpec));
    }

    public static i<LinearProgressIndicatorSpec> t(Context context, LinearProgressIndicatorSpec linearProgressIndicatorSpec) {
        return new i<>(context, linearProgressIndicatorSpec, new j(linearProgressIndicatorSpec), linearProgressIndicatorSpec.f18242g == 0 ? new k(linearProgressIndicatorSpec) : new l(context, linearProgressIndicatorSpec));
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect rect = new Rect();
        if (getBounds().isEmpty() || !isVisible() || !canvas.getClipBounds(rect)) {
            return;
        }
        canvas.save();
        this.f18314t.g(canvas, g());
        this.f18314t.c(canvas, this.f18305n);
        int i8 = 0;
        while (true) {
            h<ObjectAnimator> hVar = this.f18315w;
            int[] iArr = hVar.f18313c;
            if (i8 >= iArr.length) {
                canvas.restore();
                return;
            }
            g<S> gVar = this.f18314t;
            Paint paint = this.f18305n;
            float[] fArr = hVar.f18312b;
            int i9 = i8 * 2;
            gVar.b(canvas, paint, fArr[i9], fArr[i9 + 1], iArr[i8]);
            i8++;
        }
    }

    @Override // com.google.android.material.progressindicator.f, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int getAlpha() {
        return super.getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.f18314t.d();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.f18314t.e();
    }

    @Override // com.google.android.material.progressindicator.f, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int getOpacity() {
        return super.getOpacity();
    }

    @Override // com.google.android.material.progressindicator.f
    public /* bridge */ /* synthetic */ boolean h() {
        return super.h();
    }

    @Override // com.google.android.material.progressindicator.f
    public /* bridge */ /* synthetic */ boolean i() {
        return super.i();
    }

    @Override // com.google.android.material.progressindicator.f, android.graphics.drawable.Animatable
    public /* bridge */ /* synthetic */ boolean isRunning() {
        return super.isRunning();
    }

    @Override // com.google.android.material.progressindicator.f
    public /* bridge */ /* synthetic */ boolean j() {
        return super.j();
    }

    @Override // com.google.android.material.progressindicator.f
    public /* bridge */ /* synthetic */ void l(androidx.vectordrawable.graphics.drawable.b bVar) {
        super.l(bVar);
    }

    @Override // com.google.android.material.progressindicator.f
    public /* bridge */ /* synthetic */ boolean p(boolean z4, boolean z8, boolean z9) {
        return super.p(z4, z8, z9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.f
    public boolean q(boolean z4, boolean z8, boolean z9) {
        boolean q = super.q(z4, z8, z9);
        if (!isRunning()) {
            this.f18315w.a();
        }
        float a9 = this.f18295c.a(this.f18293a.getContentResolver());
        if (z4 && (z9 || (Build.VERSION.SDK_INT <= 21 && a9 > 0.0f))) {
            this.f18315w.g();
        }
        return q;
    }

    @Override // com.google.android.material.progressindicator.f
    public /* bridge */ /* synthetic */ boolean r(androidx.vectordrawable.graphics.drawable.b bVar) {
        return super.r(bVar);
    }

    @Override // com.google.android.material.progressindicator.f, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setAlpha(int i8) {
        super.setAlpha(i8);
    }

    @Override // com.google.android.material.progressindicator.f, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setColorFilter(ColorFilter colorFilter) {
        super.setColorFilter(colorFilter);
    }

    @Override // com.google.android.material.progressindicator.f, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ boolean setVisible(boolean z4, boolean z8) {
        return super.setVisible(z4, z8);
    }

    @Override // com.google.android.material.progressindicator.f, android.graphics.drawable.Animatable
    public /* bridge */ /* synthetic */ void start() {
        super.start();
    }

    @Override // com.google.android.material.progressindicator.f, android.graphics.drawable.Animatable
    public /* bridge */ /* synthetic */ void stop() {
        super.stop();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public h<ObjectAnimator> u() {
        return this.f18315w;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g<S> v() {
        return this.f18314t;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w(h<ObjectAnimator> hVar) {
        this.f18315w = hVar;
        hVar.e(this);
    }

    void x(g<S> gVar) {
        this.f18314t = gVar;
        gVar.f(this);
    }
}
