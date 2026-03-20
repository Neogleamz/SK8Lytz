package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.cardview.widget.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class c implements e {

    /* renamed from: a  reason: collision with root package name */
    final RectF f3080a = new RectF();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements g.a {
        a() {
        }

        @Override // androidx.cardview.widget.g.a
        public void a(Canvas canvas, RectF rectF, float f5, Paint paint) {
            float f8 = 2.0f * f5;
            float width = (rectF.width() - f8) - 1.0f;
            float height = (rectF.height() - f8) - 1.0f;
            if (f5 >= 1.0f) {
                float f9 = f5 + 0.5f;
                float f10 = -f9;
                c.this.f3080a.set(f10, f10, f9, f9);
                int save = canvas.save();
                canvas.translate(rectF.left + f9, rectF.top + f9);
                canvas.drawArc(c.this.f3080a, 180.0f, 90.0f, true, paint);
                canvas.translate(width, 0.0f);
                canvas.rotate(90.0f);
                canvas.drawArc(c.this.f3080a, 180.0f, 90.0f, true, paint);
                canvas.translate(height, 0.0f);
                canvas.rotate(90.0f);
                canvas.drawArc(c.this.f3080a, 180.0f, 90.0f, true, paint);
                canvas.translate(width, 0.0f);
                canvas.rotate(90.0f);
                canvas.drawArc(c.this.f3080a, 180.0f, 90.0f, true, paint);
                canvas.restoreToCount(save);
                float f11 = rectF.top;
                canvas.drawRect((rectF.left + f9) - 1.0f, f11, (rectF.right - f9) + 1.0f, f11 + f9, paint);
                float f12 = rectF.bottom;
                canvas.drawRect((rectF.left + f9) - 1.0f, f12 - f9, (rectF.right - f9) + 1.0f, f12, paint);
            }
            canvas.drawRect(rectF.left, rectF.top + f5, rectF.right, rectF.bottom - f5, paint);
        }
    }

    private g p(Context context, ColorStateList colorStateList, float f5, float f8, float f9) {
        return new g(context.getResources(), colorStateList, f5, f8, f9);
    }

    private g q(d dVar) {
        return (g) dVar.f();
    }

    @Override // androidx.cardview.widget.e
    public void a(d dVar, Context context, ColorStateList colorStateList, float f5, float f8, float f9) {
        g p8 = p(context, colorStateList, f5, f8, f9);
        p8.m(dVar.d());
        dVar.c(p8);
        i(dVar);
    }

    @Override // androidx.cardview.widget.e
    public void b(d dVar, float f5) {
        q(dVar).p(f5);
        i(dVar);
    }

    @Override // androidx.cardview.widget.e
    public float c(d dVar) {
        return q(dVar).l();
    }

    @Override // androidx.cardview.widget.e
    public float d(d dVar) {
        return q(dVar).g();
    }

    @Override // androidx.cardview.widget.e
    public void e(d dVar) {
    }

    @Override // androidx.cardview.widget.e
    public void f(d dVar, float f5) {
        q(dVar).r(f5);
    }

    @Override // androidx.cardview.widget.e
    public float g(d dVar) {
        return q(dVar).i();
    }

    @Override // androidx.cardview.widget.e
    public ColorStateList h(d dVar) {
        return q(dVar).f();
    }

    @Override // androidx.cardview.widget.e
    public void i(d dVar) {
        Rect rect = new Rect();
        q(dVar).h(rect);
        dVar.b((int) Math.ceil(l(dVar)), (int) Math.ceil(k(dVar)));
        dVar.a(rect.left, rect.top, rect.right, rect.bottom);
    }

    @Override // androidx.cardview.widget.e
    public void j() {
        g.f3093r = new a();
    }

    @Override // androidx.cardview.widget.e
    public float k(d dVar) {
        return q(dVar).j();
    }

    @Override // androidx.cardview.widget.e
    public float l(d dVar) {
        return q(dVar).k();
    }

    @Override // androidx.cardview.widget.e
    public void m(d dVar) {
        q(dVar).m(dVar.d());
        i(dVar);
    }

    @Override // androidx.cardview.widget.e
    public void n(d dVar, ColorStateList colorStateList) {
        q(dVar).o(colorStateList);
    }

    @Override // androidx.cardview.widget.e
    public void o(d dVar, float f5) {
        q(dVar).q(f5);
        i(dVar);
    }
}
