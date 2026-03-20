package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.google.android.material.progressindicator.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class g<S extends b> {

    /* renamed from: a  reason: collision with root package name */
    S f18309a;

    /* renamed from: b  reason: collision with root package name */
    protected f f18310b;

    public g(S s8) {
        this.f18309a = s8;
    }

    abstract void a(Canvas canvas, float f5);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void b(Canvas canvas, Paint paint, float f5, float f8, int i8);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void c(Canvas canvas, Paint paint);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int d();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int e();

    /* JADX INFO: Access modifiers changed from: protected */
    public void f(f fVar) {
        this.f18310b = fVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(Canvas canvas, float f5) {
        this.f18309a.e();
        a(canvas, f5);
    }
}
