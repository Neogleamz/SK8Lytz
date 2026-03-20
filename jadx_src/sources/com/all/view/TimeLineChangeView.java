package com.all.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class TimeLineChangeView extends View {

    /* renamed from: a  reason: collision with root package name */
    SimpleDateFormat f8687a;

    /* renamed from: b  reason: collision with root package name */
    private int f8688b;

    /* renamed from: c  reason: collision with root package name */
    private int f8689c;

    /* renamed from: d  reason: collision with root package name */
    private int f8690d;

    /* renamed from: e  reason: collision with root package name */
    private int f8691e;

    /* renamed from: f  reason: collision with root package name */
    private float f8692f;

    /* renamed from: g  reason: collision with root package name */
    private int f8693g;

    /* renamed from: h  reason: collision with root package name */
    private Calendar f8694h;

    /* renamed from: j  reason: collision with root package name */
    ArrayList<a> f8695j;

    /* renamed from: k  reason: collision with root package name */
    private final Context f8696k;

    /* renamed from: l  reason: collision with root package name */
    private Rect f8697l;

    /* renamed from: m  reason: collision with root package name */
    Paint f8698m;

    /* renamed from: n  reason: collision with root package name */
    Paint f8699n;

    /* renamed from: p  reason: collision with root package name */
    private Paint f8700p;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a {

        /* renamed from: a  reason: collision with root package name */
        Calendar f8701a;

        /* renamed from: b  reason: collision with root package name */
        float f8702b;

        public a() {
        }
    }

    public TimeLineChangeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f8687a = new SimpleDateFormat("HH:mm");
        this.f8688b = 0;
        this.f8689c = 0;
        this.f8690d = 0;
        this.f8691e = 0;
        this.f8692f = 12.0f;
        this.f8693g = 10;
        this.f8694h = Calendar.getInstance();
        this.f8695j = new ArrayList<>();
        this.f8698m = new Paint();
        this.f8699n = new Paint();
        this.f8700p = new Paint();
        this.f8696k = context;
        l();
    }

    public TimeLineChangeView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f8687a = new SimpleDateFormat("HH:mm");
        this.f8688b = 0;
        this.f8689c = 0;
        this.f8690d = 0;
        this.f8691e = 0;
        this.f8692f = 12.0f;
        this.f8693g = 10;
        this.f8694h = Calendar.getInstance();
        this.f8695j = new ArrayList<>();
        this.f8698m = new Paint();
        this.f8699n = new Paint();
        this.f8700p = new Paint();
        this.f8696k = context;
        l();
    }

    private int a(float f5) {
        return (int) ((f5 * this.f8696k.getResources().getDisplayMetrics().density) + 0.5f);
    }

    private float b(Calendar calendar) {
        return (((int) ((calendar.getTimeInMillis() - this.f8694h.getTimeInMillis()) / 1000)) * (((this.f8697l.width() - this.f8689c) - this.f8691e) / ((this.f8693g * this.f8692f) * 60.0f))) + this.f8689c;
    }

    private float c(float f5) {
        int height = this.f8697l.height();
        int i8 = this.f8690d;
        return (height - i8) - (((height - this.f8688b) - i8) * f5);
    }

    private void d(Canvas canvas) {
        int width = (this.f8697l.width() - this.f8689c) - this.f8691e;
        int height = (this.f8697l.height() - this.f8688b) - this.f8690d;
        this.f8699n.setColor(Color.argb(0, 230, 250, 255));
        int i8 = this.f8689c;
        int i9 = this.f8688b;
        canvas.drawRect(i8, i9, width + i8, height + i9, this.f8699n);
    }

    private void e(Canvas canvas, float f5) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(a(12.0f));
        float a9 = a(2.0f);
        canvas.rotate(90.0f, a9, f5);
        canvas.rotate(-90.0f, a9, f5);
    }

    private void f(Canvas canvas) {
        String str;
        int width = this.f8697l.width();
        int height = this.f8697l.height();
        int i8 = this.f8690d;
        canvas.drawLine(0.0f, height - i8, width, height - i8, this.f8700p);
        h(canvas, (height - this.f8690d) - 2, "0%", true);
        float f5 = (height - (this.f8688b + this.f8690d)) / 10.0f;
        float f8 = width - this.f8691e;
        for (int i9 = 1; i9 <= 10; i9++) {
            float f9 = (height - (i9 * f5)) - this.f8690d;
            canvas.drawLine(this.f8689c, f9, f8, f9, this.f8700p);
            if (i9 == 5) {
                str = "50%";
            } else if (i9 == 10) {
                str = "100%";
            }
            h(canvas, f9, str, false);
        }
    }

    private void g(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(-7829368);
        paint.setStrokeWidth(a(1.0f));
        paint.setAntiAlias(true);
        paint.setFlags(1);
        if (this.f8695j.size() > 1) {
            canvas.drawLine(b(this.f8695j.get(0).f8701a), c(this.f8695j.get(0).f8702b), b(this.f8695j.get(1).f8701a), c(this.f8695j.get(1).f8702b), paint);
        }
    }

    private void h(Canvas canvas, float f5, String str, boolean z4) {
        float f8;
        Paint.FontMetrics fontMetrics = this.f8698m.getFontMetrics();
        float measureText = this.f8698m.measureText(str);
        if (z4) {
            f8 = (this.f8689c - measureText) - 5.0f;
        } else {
            f8 = (this.f8689c - measureText) - 5.0f;
            f5 += fontMetrics.descent * 2.0f;
        }
        canvas.drawText(str, f8, f5, this.f8698m);
    }

    private void i(Canvas canvas, float f5, float f8, String str) {
        canvas.drawText(str, f5 - (this.f8698m.measureText(str) / 2.0f), f8 - this.f8698m.getFontMetrics().top, this.f8698m);
    }

    private void j(Canvas canvas, float f5, float f8, String str) {
        canvas.drawText(str, f5 + 5.0f, f8 - this.f8698m.getFontMetrics().top, this.f8698m);
    }

    private void k(Canvas canvas) {
        this.f8697l.width();
        float height = this.f8697l.height() - this.f8690d;
        int i8 = this.f8689c;
        canvas.drawLine(i8, 0.0f, i8, this.f8697l.height(), this.f8700p);
        this.f8687a.setCalendar(this.f8694h);
        j(canvas, this.f8689c, height, this.f8687a.format(this.f8694h.getTime()));
        for (int i9 = 1; i9 <= this.f8692f; i9++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.f8694h.getTimeInMillis());
            calendar.add(12, this.f8693g * i9);
            float b9 = b(calendar);
            canvas.drawLine(b9, this.f8688b, b9, height, this.f8700p);
            if (i9 % 2 == 0) {
                this.f8687a.setCalendar(calendar);
                i(canvas, b9, height, this.f8687a.format(calendar.getTime()));
            }
        }
    }

    private void l() {
        this.f8694h.set(12, ((int) Math.floor((this.f8694h.get(12) * 1.0f) / this.f8693g)) * this.f8693g);
        this.f8694h.set(13, 0);
        this.f8700p.setColor(-7829368);
        this.f8688b = a(4.0f);
        this.f8689c = a(28.0f);
        this.f8690d = a(16.0f);
        this.f8691e = a(16.0f);
        Paint paint = new Paint();
        this.f8698m = paint;
        paint.setAntiAlias(true);
        this.f8698m.setTextSize(a(10.0f));
        this.f8698m.setColor(-7829368);
        this.f8699n.setAntiAlias(true);
        this.f8699n.setFlags(1);
        this.f8699n.setDither(true);
    }

    public void m(float f5, float f8, int i8, int i9, int i10) {
        ArrayList<a> arrayList = this.f8695j;
        if (arrayList == null) {
            this.f8695j = new ArrayList<>();
        } else {
            arrayList.clear();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, i9);
        calendar.set(12, i10);
        calendar.set(13, 0);
        int floor = ((int) Math.floor((i10 * 1.0f) / this.f8693g)) * this.f8693g;
        this.f8694h.setTimeInMillis(calendar.getTimeInMillis());
        this.f8694h.set(12, floor);
        this.f8694h.set(13, 0);
        a aVar = new a();
        aVar.f8701a = calendar;
        aVar.f8702b = f5;
        this.f8695j.add(aVar);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(11, i9 + (i8 / 60));
        calendar2.set(12, i10 + (i8 % 60));
        calendar2.set(13, 0);
        a aVar2 = new a();
        aVar2.f8701a = calendar2;
        aVar2.f8702b = f8;
        this.f8695j.add(aVar2);
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        this.f8697l.height();
        d(canvas);
        f(canvas);
        k(canvas);
        e(canvas, a(20.0f));
        g(canvas);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i8, int i9, int i10, int i11) {
        super.onSizeChanged(i8, i9, i10, i11);
        this.f8697l = new Rect(0, 0, i8, i9);
    }
}
