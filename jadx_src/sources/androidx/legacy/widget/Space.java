package androidx.legacy.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Space extends View {
    @Deprecated
    public Space(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Deprecated
    public Space(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        if (getVisibility() == 0) {
            setVisibility(4);
        }
    }

    private static int a(int i8, int i9) {
        int mode = View.MeasureSpec.getMode(i9);
        int size = View.MeasureSpec.getSize(i9);
        return mode != Integer.MIN_VALUE ? mode != 1073741824 ? i8 : size : Math.min(i8, size);
    }

    @Override // android.view.View
    @SuppressLint({"MissingSuperCall"})
    @Deprecated
    public void draw(Canvas canvas) {
    }

    @Override // android.view.View
    @Deprecated
    protected void onMeasure(int i8, int i9) {
        setMeasuredDimension(a(getSuggestedMinimumWidth(), i8), a(getSuggestedMinimumHeight(), i9));
    }
}
