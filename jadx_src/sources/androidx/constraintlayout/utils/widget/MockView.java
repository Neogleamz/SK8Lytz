package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.widget.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MockView extends View {

    /* renamed from: a  reason: collision with root package name */
    private Paint f3893a;

    /* renamed from: b  reason: collision with root package name */
    private Paint f3894b;

    /* renamed from: c  reason: collision with root package name */
    private Paint f3895c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f3896d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f3897e;

    /* renamed from: f  reason: collision with root package name */
    protected String f3898f;

    /* renamed from: g  reason: collision with root package name */
    private Rect f3899g;

    /* renamed from: h  reason: collision with root package name */
    private int f3900h;

    /* renamed from: j  reason: collision with root package name */
    private int f3901j;

    /* renamed from: k  reason: collision with root package name */
    private int f3902k;

    /* renamed from: l  reason: collision with root package name */
    private int f3903l;

    public MockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f3893a = new Paint();
        this.f3894b = new Paint();
        this.f3895c = new Paint();
        this.f3896d = true;
        this.f3897e = true;
        this.f3898f = null;
        this.f3899g = new Rect();
        this.f3900h = Color.argb(255, 0, 0, 0);
        this.f3901j = Color.argb(255, 200, 200, 200);
        this.f3902k = Color.argb(255, 50, 50, 50);
        this.f3903l = 4;
        a(context, attributeSet);
    }

    public MockView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f3893a = new Paint();
        this.f3894b = new Paint();
        this.f3895c = new Paint();
        this.f3896d = true;
        this.f3897e = true;
        this.f3898f = null;
        this.f3899g = new Rect();
        this.f3900h = Color.argb(255, 0, 0, 0);
        this.f3901j = Color.argb(255, 200, 200, 200);
        this.f3902k = Color.argb(255, 50, 50, 50);
        this.f3903l = 4;
        a(context, attributeSet);
    }

    private void a(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, e.f4266q6);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == e.f4283s6) {
                    this.f3898f = obtainStyledAttributes.getString(index);
                } else if (index == e.f4310v6) {
                    this.f3896d = obtainStyledAttributes.getBoolean(index, this.f3896d);
                } else if (index == e.f4274r6) {
                    this.f3900h = obtainStyledAttributes.getColor(index, this.f3900h);
                } else if (index == e.f4292t6) {
                    this.f3902k = obtainStyledAttributes.getColor(index, this.f3902k);
                } else if (index == e.f4301u6) {
                    this.f3901j = obtainStyledAttributes.getColor(index, this.f3901j);
                } else if (index == e.f4319w6) {
                    this.f3897e = obtainStyledAttributes.getBoolean(index, this.f3897e);
                }
            }
        }
        if (this.f3898f == null) {
            try {
                this.f3898f = context.getResources().getResourceEntryName(getId());
            } catch (Exception unused) {
            }
        }
        this.f3893a.setColor(this.f3900h);
        this.f3893a.setAntiAlias(true);
        this.f3894b.setColor(this.f3901j);
        this.f3894b.setAntiAlias(true);
        this.f3895c.setColor(this.f3902k);
        this.f3903l = Math.round(this.f3903l * (getResources().getDisplayMetrics().xdpi / 160.0f));
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (this.f3896d) {
            width--;
            height--;
            float f5 = width;
            float f8 = height;
            canvas.drawLine(0.0f, 0.0f, f5, f8, this.f3893a);
            canvas.drawLine(0.0f, f8, f5, 0.0f, this.f3893a);
            canvas.drawLine(0.0f, 0.0f, f5, 0.0f, this.f3893a);
            canvas.drawLine(f5, 0.0f, f5, f8, this.f3893a);
            canvas.drawLine(f5, f8, 0.0f, f8, this.f3893a);
            canvas.drawLine(0.0f, f8, 0.0f, 0.0f, this.f3893a);
        }
        String str = this.f3898f;
        if (str == null || !this.f3897e) {
            return;
        }
        this.f3894b.getTextBounds(str, 0, str.length(), this.f3899g);
        float width2 = (width - this.f3899g.width()) / 2.0f;
        float height2 = ((height - this.f3899g.height()) / 2.0f) + this.f3899g.height();
        this.f3899g.offset((int) width2, (int) height2);
        Rect rect = this.f3899g;
        int i8 = rect.left;
        int i9 = this.f3903l;
        rect.set(i8 - i9, rect.top - i9, rect.right + i9, rect.bottom + i9);
        canvas.drawRect(this.f3899g, this.f3895c);
        canvas.drawText(this.f3898f, width2, height2, this.f3894b);
    }
}
