package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintLayout;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Placeholder extends View {

    /* renamed from: a  reason: collision with root package name */
    private int f4018a;

    /* renamed from: b  reason: collision with root package name */
    private View f4019b;

    /* renamed from: c  reason: collision with root package name */
    private int f4020c;

    public Placeholder(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f4018a = -1;
        this.f4019b = null;
        this.f4020c = 4;
        a(attributeSet);
    }

    public Placeholder(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f4018a = -1;
        this.f4019b = null;
        this.f4020c = 4;
        a(attributeSet);
    }

    private void a(AttributeSet attributeSet) {
        super.setVisibility(this.f4020c);
        this.f4018a = -1;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, e.M2);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == e.N2) {
                    this.f4018a = obtainStyledAttributes.getResourceId(index, this.f4018a);
                } else if (index == e.O2) {
                    this.f4020c = obtainStyledAttributes.getInt(index, this.f4020c);
                }
            }
        }
    }

    public void b(ConstraintLayout constraintLayout) {
        if (this.f4019b == null) {
            return;
        }
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) getLayoutParams();
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) this.f4019b.getLayoutParams();
        layoutParams2.f3983n0.E0(0);
        ConstraintWidget.DimensionBehaviour z4 = layoutParams.f3983n0.z();
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        if (z4 != dimensionBehaviour) {
            layoutParams.f3983n0.F0(layoutParams2.f3983n0.Q());
        }
        if (layoutParams.f3983n0.N() != dimensionBehaviour) {
            layoutParams.f3983n0.i0(layoutParams2.f3983n0.w());
        }
        layoutParams2.f3983n0.E0(8);
    }

    public void c(ConstraintLayout constraintLayout) {
        if (this.f4018a == -1 && !isInEditMode()) {
            setVisibility(this.f4020c);
        }
        View findViewById = constraintLayout.findViewById(this.f4018a);
        this.f4019b = findViewById;
        if (findViewById != null) {
            ((ConstraintLayout.LayoutParams) findViewById.getLayoutParams()).f3959b0 = true;
            this.f4019b.setVisibility(0);
            setVisibility(0);
        }
    }

    public View getContent() {
        return this.f4019b;
    }

    public int getEmptyVisibility() {
        return this.f4020c;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            canvas.drawRGB(223, 223, 223);
            Paint paint = new Paint();
            paint.setARGB(255, 210, 210, 210);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, 0));
            Rect rect = new Rect();
            canvas.getClipBounds(rect);
            paint.setTextSize(rect.height());
            int height = rect.height();
            int width = rect.width();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.getTextBounds("?", 0, 1, rect);
            canvas.drawText("?", ((width / 2.0f) - (rect.width() / 2.0f)) - rect.left, ((height / 2.0f) + (rect.height() / 2.0f)) - rect.bottom, paint);
        }
    }

    public void setContentId(int i8) {
        View findViewById;
        if (this.f4018a == i8) {
            return;
        }
        View view = this.f4019b;
        if (view != null) {
            view.setVisibility(0);
            ((ConstraintLayout.LayoutParams) this.f4019b.getLayoutParams()).f3959b0 = false;
            this.f4019b = null;
        }
        this.f4018a = i8;
        if (i8 == -1 || (findViewById = ((View) getParent()).findViewById(i8)) == null) {
            return;
        }
        findViewById.setVisibility(8);
    }

    public void setEmptyVisibility(int i8) {
        this.f4020c = i8;
    }
}
