package androidx.emoji2.text;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class i extends ReplacementSpan {

    /* renamed from: b  reason: collision with root package name */
    private final g f5275b;

    /* renamed from: a  reason: collision with root package name */
    private final Paint.FontMetricsInt f5274a = new Paint.FontMetricsInt();

    /* renamed from: c  reason: collision with root package name */
    private short f5276c = -1;

    /* renamed from: d  reason: collision with root package name */
    private short f5277d = -1;

    /* renamed from: e  reason: collision with root package name */
    private float f5278e = 1.0f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(g gVar) {
        androidx.core.util.h.i(gVar, "metadata cannot be null");
        this.f5275b = gVar;
    }

    public final g a() {
        return this.f5275b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int b() {
        return this.f5276c;
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, @SuppressLint({"UnknownNullness"}) CharSequence charSequence, int i8, int i9, Paint.FontMetricsInt fontMetricsInt) {
        paint.getFontMetricsInt(this.f5274a);
        Paint.FontMetricsInt fontMetricsInt2 = this.f5274a;
        this.f5278e = (Math.abs(fontMetricsInt2.descent - fontMetricsInt2.ascent) * 1.0f) / this.f5275b.e();
        this.f5277d = (short) (this.f5275b.e() * this.f5278e);
        short i10 = (short) (this.f5275b.i() * this.f5278e);
        this.f5276c = i10;
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fontMetricsInt3 = this.f5274a;
            fontMetricsInt.ascent = fontMetricsInt3.ascent;
            fontMetricsInt.descent = fontMetricsInt3.descent;
            fontMetricsInt.top = fontMetricsInt3.top;
            fontMetricsInt.bottom = fontMetricsInt3.bottom;
        }
        return i10;
    }
}
