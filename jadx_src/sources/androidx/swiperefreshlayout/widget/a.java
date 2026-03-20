package androidx.swiperefreshlayout.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.animation.Animation;
import android.widget.ImageView;
import androidx.core.view.c0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends ImageView {

    /* renamed from: a  reason: collision with root package name */
    private Animation.AnimationListener f7331a;

    /* renamed from: b  reason: collision with root package name */
    int f7332b;

    /* renamed from: androidx.swiperefreshlayout.widget.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class C0080a extends OvalShape {

        /* renamed from: a  reason: collision with root package name */
        private RadialGradient f7333a;

        /* renamed from: b  reason: collision with root package name */
        private Paint f7334b = new Paint();

        C0080a(int i8) {
            a.this.f7332b = i8;
            b((int) rect().width());
        }

        private void b(int i8) {
            float f5 = i8 / 2;
            RadialGradient radialGradient = new RadialGradient(f5, f5, a.this.f7332b, new int[]{1023410176, 0}, (float[]) null, Shader.TileMode.CLAMP);
            this.f7333a = radialGradient;
            this.f7334b.setShader(radialGradient);
        }

        @Override // android.graphics.drawable.shapes.OvalShape, android.graphics.drawable.shapes.RectShape, android.graphics.drawable.shapes.Shape
        public void draw(Canvas canvas, Paint paint) {
            int width;
            float width2 = a.this.getWidth() / 2;
            float height = a.this.getHeight() / 2;
            canvas.drawCircle(width2, height, width2, this.f7334b);
            canvas.drawCircle(width2, height, width - a.this.f7332b, paint);
        }

        @Override // android.graphics.drawable.shapes.RectShape, android.graphics.drawable.shapes.Shape
        protected void onResize(float f5, float f8) {
            super.onResize(f5, f8);
            b((int) f5);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(Context context, int i8) {
        super(context);
        ShapeDrawable shapeDrawable;
        float f5 = getContext().getResources().getDisplayMetrics().density;
        int i9 = (int) (1.75f * f5);
        int i10 = (int) (0.0f * f5);
        this.f7332b = (int) (3.5f * f5);
        if (a()) {
            shapeDrawable = new ShapeDrawable(new OvalShape());
            c0.B0(this, f5 * 4.0f);
        } else {
            ShapeDrawable shapeDrawable2 = new ShapeDrawable(new C0080a(this.f7332b));
            setLayerType(1, shapeDrawable2.getPaint());
            shapeDrawable2.getPaint().setShadowLayer(this.f7332b, i10, i9, 503316480);
            int i11 = this.f7332b;
            setPadding(i11, i11, i11, i11);
            shapeDrawable = shapeDrawable2;
        }
        shapeDrawable.getPaint().setColor(i8);
        c0.x0(this, shapeDrawable);
    }

    private boolean a() {
        return Build.VERSION.SDK_INT >= 21;
    }

    public void b(Animation.AnimationListener animationListener) {
        this.f7331a = animationListener;
    }

    @Override // android.view.View
    public void onAnimationEnd() {
        super.onAnimationEnd();
        Animation.AnimationListener animationListener = this.f7331a;
        if (animationListener != null) {
            animationListener.onAnimationEnd(getAnimation());
        }
    }

    @Override // android.view.View
    public void onAnimationStart() {
        super.onAnimationStart();
        Animation.AnimationListener animationListener = this.f7331a;
        if (animationListener != null) {
            animationListener.onAnimationStart(getAnimation());
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i8, int i9) {
        super.onMeasure(i8, i9);
        if (a()) {
            return;
        }
        setMeasuredDimension(getMeasuredWidth() + (this.f7332b * 2), getMeasuredHeight() + (this.f7332b * 2));
    }

    @Override // android.view.View
    public void setBackgroundColor(int i8) {
        if (getBackground() instanceof ShapeDrawable) {
            ((ShapeDrawable) getBackground()).getPaint().setColor(i8);
        }
    }
}
