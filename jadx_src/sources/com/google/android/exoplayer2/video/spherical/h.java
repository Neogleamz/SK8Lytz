package com.google.android.exoplayer2.video.spherical;

import android.content.Context;
import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.exoplayer2.video.spherical.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener, b.a {

    /* renamed from: c  reason: collision with root package name */
    private final a f11165c;

    /* renamed from: d  reason: collision with root package name */
    private final float f11166d;

    /* renamed from: e  reason: collision with root package name */
    private final GestureDetector f11167e;

    /* renamed from: a  reason: collision with root package name */
    private final PointF f11163a = new PointF();

    /* renamed from: b  reason: collision with root package name */
    private final PointF f11164b = new PointF();

    /* renamed from: f  reason: collision with root package name */
    private volatile float f11168f = 3.1415927f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void b(PointF pointF);

        default boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }
    }

    public h(Context context, a aVar, float f5) {
        this.f11165c = aVar;
        this.f11166d = f5;
        this.f11167e = new GestureDetector(context, this);
    }

    @Override // com.google.android.exoplayer2.video.spherical.b.a
    public void a(float[] fArr, float f5) {
        this.f11168f = -f5;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent motionEvent) {
        this.f11163a.set(motionEvent.getX(), motionEvent.getY());
        return true;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f5, float f8) {
        float x8 = (motionEvent2.getX() - this.f11163a.x) / this.f11166d;
        float y8 = motionEvent2.getY();
        PointF pointF = this.f11163a;
        float f9 = (y8 - pointF.y) / this.f11166d;
        pointF.set(motionEvent2.getX(), motionEvent2.getY());
        double d8 = this.f11168f;
        float cos = (float) Math.cos(d8);
        float sin = (float) Math.sin(d8);
        PointF pointF2 = this.f11164b;
        pointF2.x -= (cos * x8) - (sin * f9);
        float f10 = pointF2.y + (sin * x8) + (cos * f9);
        pointF2.y = f10;
        pointF2.y = Math.max(-45.0f, Math.min(45.0f, f10));
        this.f11165c.b(this.f11164b);
        return true;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return this.f11165c.onSingleTapUp(motionEvent);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return this.f11167e.onTouchEvent(motionEvent);
    }
}
