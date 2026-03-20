package androidx.emoji2.text;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o extends i {

    /* renamed from: f  reason: collision with root package name */
    private static Paint f5304f;

    public o(g gVar) {
        super(gVar);
    }

    private static Paint c() {
        if (f5304f == null) {
            TextPaint textPaint = new TextPaint();
            f5304f = textPaint;
            textPaint.setColor(e.b().c());
            f5304f.setStyle(Paint.Style.FILL);
        }
        return f5304f;
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, @SuppressLint({"UnknownNullness"}) CharSequence charSequence, int i8, int i9, float f5, int i10, int i11, int i12, Paint paint) {
        if (e.b().i()) {
            canvas.drawRect(f5, i10, f5 + b(), i12, c());
        }
        a().a(canvas, f5, i11, paint);
    }
}
