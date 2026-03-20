package com.all.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.google.android.libraries.barhopper.RecognitionOptions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ImagePreviewView extends View {

    /* renamed from: a  reason: collision with root package name */
    Paint f8646a;

    /* renamed from: b  reason: collision with root package name */
    Paint f8647b;

    /* renamed from: c  reason: collision with root package name */
    Paint f8648c;

    /* renamed from: d  reason: collision with root package name */
    int f8649d;

    /* renamed from: e  reason: collision with root package name */
    int f8650e;

    public ImagePreviewView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f8649d = 500;
        this.f8650e = 500;
        Paint paint = new Paint();
        this.f8646a = paint;
        paint.setColor(Color.argb(150, (int) RecognitionOptions.ITF, (int) RecognitionOptions.ITF, (int) RecognitionOptions.ITF));
        Paint paint2 = new Paint();
        this.f8647b = paint2;
        paint2.setColor(Color.argb(80, 255, 0, 0));
        this.f8647b.setStrokeWidth(2.0f);
        Paint paint3 = new Paint();
        this.f8648c = paint3;
        paint3.setColor(Color.argb(200, 255, 255, 255));
        this.f8648c.setStrokeWidth(4.0f);
    }

    public void a(int i8, int i9) {
        this.f8649d = i8;
        this.f8650e = i9;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int i8 = this.f8650e;
        int i9 = this.f8649d;
        float f5 = 0;
        float f8 = width;
        float f9 = (height - i8) / 2;
        canvas.drawRect(f5, f5, f8, f9, this.f8646a);
        float f10 = (width - i9) / 2;
        float f11 = (height + i8) / 2;
        canvas.drawRect(f5, (height - i8) / 2, f10, f11, this.f8646a);
        float f12 = (i9 + width) / 2;
        float f13 = (height - i8) / 2;
        canvas.drawRect(f12, f13, f8, (height + i8) / 2, this.f8646a);
        canvas.drawRect(f5, (i8 + height) / 2, f8, height, this.f8646a);
        canvas.drawLine(f10, f9, f12, f13, this.f8647b);
        canvas.drawLine(f10, f9, f10, f11, this.f8647b);
        canvas.drawLine(f10, f11, f12, f11, this.f8647b);
        canvas.drawLine(f12, f13, f12, f11, this.f8647b);
        canvas.drawLine(getWidth() / 2, (getHeight() / 2) + 15, getWidth() / 2, (getHeight() / 2) - 15, this.f8648c);
        canvas.drawLine((getWidth() / 2) - 15, getHeight() / 2, (getWidth() / 2) + 15, getHeight() / 2, this.f8648c);
    }
}
