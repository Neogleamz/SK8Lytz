package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RatingBar;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AppCompatRatingBar extends RatingBar {

    /* renamed from: a  reason: collision with root package name */
    private final l f1176a;

    public AppCompatRatingBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, g.a.K);
    }

    public AppCompatRatingBar(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        e0.a(this, getContext());
        l lVar = new l(this);
        this.f1176a = lVar;
        lVar.c(attributeSet, i8);
    }

    @Override // android.widget.RatingBar, android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    protected synchronized void onMeasure(int i8, int i9) {
        super.onMeasure(i8, i9);
        Bitmap b9 = this.f1176a.b();
        if (b9 != null) {
            setMeasuredDimension(View.resolveSizeAndState(b9.getWidth() * getNumStars(), i8, 0), getMeasuredHeight());
        }
    }
}
