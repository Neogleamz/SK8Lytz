package androidx.viewpager.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class PagerTabStrip extends PagerTitleStrip {
    private int A;
    private int B;
    private final Paint C;
    private final Rect E;
    private int F;
    private boolean G;
    private boolean H;
    private int K;
    private boolean L;
    private float O;
    private float P;
    private int Q;

    /* renamed from: w  reason: collision with root package name */
    private int f7747w;

    /* renamed from: x  reason: collision with root package name */
    private int f7748x;

    /* renamed from: y  reason: collision with root package name */
    private int f7749y;

    /* renamed from: z  reason: collision with root package name */
    private int f7750z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ViewPager viewPager = PagerTabStrip.this.f7754a;
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements View.OnClickListener {
        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ViewPager viewPager = PagerTabStrip.this.f7754a;
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    public PagerTabStrip(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.C = paint;
        this.E = new Rect();
        this.F = 255;
        this.G = false;
        this.H = false;
        int i8 = this.f7767p;
        this.f7747w = i8;
        paint.setColor(i8);
        float f5 = context.getResources().getDisplayMetrics().density;
        this.f7748x = (int) ((3.0f * f5) + 0.5f);
        this.f7749y = (int) ((6.0f * f5) + 0.5f);
        this.f7750z = (int) (64.0f * f5);
        this.B = (int) ((16.0f * f5) + 0.5f);
        this.K = (int) ((1.0f * f5) + 0.5f);
        this.A = (int) ((f5 * 32.0f) + 0.5f);
        this.Q = ViewConfiguration.get(context).getScaledTouchSlop();
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        setTextSpacing(getTextSpacing());
        setWillNotDraw(false);
        this.f7755b.setFocusable(true);
        this.f7755b.setOnClickListener(new a());
        this.f7757d.setFocusable(true);
        this.f7757d.setOnClickListener(new b());
        if (getBackground() == null) {
            this.G = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.viewpager.widget.PagerTitleStrip
    public void d(int i8, float f5, boolean z4) {
        Rect rect = this.E;
        int height = getHeight();
        int left = this.f7756c.getLeft() - this.B;
        int right = this.f7756c.getRight() + this.B;
        int i9 = height - this.f7748x;
        rect.set(left, i9, right, height);
        super.d(i8, f5, z4);
        this.F = (int) (Math.abs(f5 - 0.5f) * 2.0f * 255.0f);
        rect.union(this.f7756c.getLeft() - this.B, i9, this.f7756c.getRight() + this.B, height);
        invalidate(rect);
    }

    public boolean getDrawFullUnderline() {
        return this.G;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.viewpager.widget.PagerTitleStrip
    public int getMinHeight() {
        return Math.max(super.getMinHeight(), this.A);
    }

    public int getTabIndicatorColor() {
        return this.f7747w;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int left = this.f7756c.getLeft() - this.B;
        int right = this.f7756c.getRight() + this.B;
        this.C.setColor((this.F << 24) | (this.f7747w & 16777215));
        float f5 = height;
        canvas.drawRect(left, height - this.f7748x, right, f5, this.C);
        if (this.G) {
            this.C.setColor((-16777216) | (this.f7747w & 16777215));
            canvas.drawRect(getPaddingLeft(), height - this.K, getWidth() - getPaddingRight(), f5, this.C);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ViewPager viewPager;
        int currentItem;
        int action = motionEvent.getAction();
        if (action == 0 || !this.L) {
            float x8 = motionEvent.getX();
            float y8 = motionEvent.getY();
            if (action == 0) {
                this.O = x8;
                this.P = y8;
                this.L = false;
            } else if (action == 1) {
                if (x8 < this.f7756c.getLeft() - this.B) {
                    viewPager = this.f7754a;
                    currentItem = viewPager.getCurrentItem() - 1;
                } else if (x8 > this.f7756c.getRight() + this.B) {
                    viewPager = this.f7754a;
                    currentItem = viewPager.getCurrentItem() + 1;
                }
                viewPager.setCurrentItem(currentItem);
            } else if (action == 2 && (Math.abs(x8 - this.O) > this.Q || Math.abs(y8 - this.P) > this.Q)) {
                this.L = true;
            }
            return true;
        }
        return false;
    }

    @Override // android.view.View
    public void setBackgroundColor(int i8) {
        super.setBackgroundColor(i8);
        if (this.H) {
            return;
        }
        this.G = (i8 & (-16777216)) == 0;
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        if (this.H) {
            return;
        }
        this.G = drawable == null;
    }

    @Override // android.view.View
    public void setBackgroundResource(int i8) {
        super.setBackgroundResource(i8);
        if (this.H) {
            return;
        }
        this.G = i8 == 0;
    }

    public void setDrawFullUnderline(boolean z4) {
        this.G = z4;
        this.H = true;
        invalidate();
    }

    @Override // android.view.View
    public void setPadding(int i8, int i9, int i10, int i11) {
        int i12 = this.f7749y;
        if (i11 < i12) {
            i11 = i12;
        }
        super.setPadding(i8, i9, i10, i11);
    }

    public void setTabIndicatorColor(int i8) {
        this.f7747w = i8;
        this.C.setColor(i8);
        invalidate();
    }

    public void setTabIndicatorColorResource(int i8) {
        setTabIndicatorColor(androidx.core.content.a.d(getContext(), i8));
    }

    @Override // androidx.viewpager.widget.PagerTitleStrip
    public void setTextSpacing(int i8) {
        int i9 = this.f7750z;
        if (i8 < i9) {
            i8 = i9;
        }
        super.setTextSpacing(i8);
    }
}
