package com.google.android.material.progressindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import com.google.android.material.progressindicator.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e<S extends b> extends f {
    private static final y0.c<e> A = new a("indicatorLevel");

    /* renamed from: t  reason: collision with root package name */
    private g<S> f18288t;

    /* renamed from: w  reason: collision with root package name */
    private final y0.e f18289w;

    /* renamed from: x  reason: collision with root package name */
    private final y0.d f18290x;

    /* renamed from: y  reason: collision with root package name */
    private float f18291y;

    /* renamed from: z  reason: collision with root package name */
    private boolean f18292z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a extends y0.c<e> {
        a(String str) {
            super(str);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(e eVar) {
            return eVar.x() * 10000.0f;
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(e eVar, float f5) {
            eVar.z(f5 / 10000.0f);
        }
    }

    e(Context context, b bVar, g<S> gVar) {
        super(context, bVar);
        this.f18292z = false;
        y(gVar);
        y0.e eVar = new y0.e();
        this.f18289w = eVar;
        eVar.d(1.0f);
        eVar.f(50.0f);
        y0.d dVar = new y0.d(this, A);
        this.f18290x = dVar;
        dVar.p(eVar);
        m(1.0f);
    }

    public static e<CircularProgressIndicatorSpec> u(Context context, CircularProgressIndicatorSpec circularProgressIndicatorSpec) {
        return new e<>(context, circularProgressIndicatorSpec, new c(circularProgressIndicatorSpec));
    }

    public static e<LinearProgressIndicatorSpec> v(Context context, LinearProgressIndicatorSpec linearProgressIndicatorSpec) {
        return new e<>(context, linearProgressIndicatorSpec, new j(linearProgressIndicatorSpec));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float x() {
        return this.f18291y;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void z(float f5) {
        this.f18291y = f5;
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A(float f5) {
        setLevel((int) (f5 * 10000.0f));
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect rect = new Rect();
        if (!getBounds().isEmpty() && isVisible() && canvas.getClipBounds(rect)) {
            canvas.save();
            this.f18288t.g(canvas, g());
            this.f18288t.c(canvas, this.f18305n);
            this.f18288t.b(canvas, this.f18305n, 0.0f, x(), n7.a.a(this.f18294b.f18265c[0], getAlpha()));
            canvas.restore();
        }
    }

    @Override // com.google.android.material.progressindicator.f, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int getAlpha() {
        return super.getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.f18288t.d();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.f18288t.e();
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

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        this.f18290x.b();
        z(getLevel() / 10000.0f);
    }

    @Override // com.google.android.material.progressindicator.f
    public /* bridge */ /* synthetic */ void l(androidx.vectordrawable.graphics.drawable.b bVar) {
        super.l(bVar);
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int i8) {
        if (this.f18292z) {
            this.f18290x.b();
            z(i8 / 10000.0f);
            return true;
        }
        this.f18290x.i(x() * 10000.0f);
        this.f18290x.m(i8);
        return true;
    }

    @Override // com.google.android.material.progressindicator.f
    public /* bridge */ /* synthetic */ boolean p(boolean z4, boolean z8, boolean z9) {
        return super.p(z4, z8, z9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.f
    public boolean q(boolean z4, boolean z8, boolean z9) {
        boolean q = super.q(z4, z8, z9);
        float a9 = this.f18295c.a(this.f18293a.getContentResolver());
        if (a9 == 0.0f) {
            this.f18292z = true;
        } else {
            this.f18292z = false;
            this.f18289w.f(50.0f / a9);
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
    public g<S> w() {
        return this.f18288t;
    }

    void y(g<S> gVar) {
        this.f18288t = gVar;
        gVar.f(this);
    }
}
