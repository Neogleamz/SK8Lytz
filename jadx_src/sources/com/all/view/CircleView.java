package com.all.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.zengge.wifi.Common.App;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class CircleView extends TextView {

    /* renamed from: a  reason: collision with root package name */
    Paint f8642a;

    /* renamed from: b  reason: collision with root package name */
    float f8643b;

    /* renamed from: c  reason: collision with root package name */
    int f8644c;

    /* renamed from: d  reason: collision with root package name */
    float f8645d;

    public CircleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f8642a = new Paint();
        this.f8643b = 0.0f;
        this.f8644c = -1;
        this.f8645d = 0.0f;
        this.f8645d = App.e(10.0f);
        this.f8642a.setColor(this.f8644c);
        this.f8642a.setAntiAlias(true);
        this.f8642a.setFlags(1);
        this.f8642a.setStyle(Paint.Style.STROKE);
        this.f8642a.setStrokeWidth(App.e(3.0f));
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = (int) (getWidth() - this.f8645d);
        int height = (int) (getHeight() - this.f8645d);
        double sqrt = Math.sqrt((width * width) + (height * height)) / 2.0d;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) (Math.sqrt((sqrt * sqrt) / 2.0d) * this.f8643b), this.f8642a);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i8, int i9) {
        int min = Math.min(View.MeasureSpec.getSize(i8), View.MeasureSpec.getSize(i9));
        setMeasuredDimension(min, min);
    }
}
