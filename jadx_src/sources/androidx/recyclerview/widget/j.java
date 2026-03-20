package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.MotionEvent;
import androidx.core.view.c0;
import androidx.recyclerview.widget.RecyclerView;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j extends RecyclerView.n implements RecyclerView.r {
    private static final int[] D = {16842919};
    private static final int[] E = new int[0];
    int A;
    private final Runnable B;
    private final RecyclerView.s C;

    /* renamed from: a  reason: collision with root package name */
    private final int f6905a;

    /* renamed from: b  reason: collision with root package name */
    private final int f6906b;

    /* renamed from: c  reason: collision with root package name */
    final StateListDrawable f6907c;

    /* renamed from: d  reason: collision with root package name */
    final Drawable f6908d;

    /* renamed from: e  reason: collision with root package name */
    private final int f6909e;

    /* renamed from: f  reason: collision with root package name */
    private final int f6910f;

    /* renamed from: g  reason: collision with root package name */
    private final StateListDrawable f6911g;

    /* renamed from: h  reason: collision with root package name */
    private final Drawable f6912h;

    /* renamed from: i  reason: collision with root package name */
    private final int f6913i;

    /* renamed from: j  reason: collision with root package name */
    private final int f6914j;

    /* renamed from: k  reason: collision with root package name */
    int f6915k;

    /* renamed from: l  reason: collision with root package name */
    int f6916l;

    /* renamed from: m  reason: collision with root package name */
    float f6917m;

    /* renamed from: n  reason: collision with root package name */
    int f6918n;

    /* renamed from: o  reason: collision with root package name */
    int f6919o;

    /* renamed from: p  reason: collision with root package name */
    float f6920p;

    /* renamed from: s  reason: collision with root package name */
    private RecyclerView f6922s;

    /* renamed from: z  reason: collision with root package name */
    final ValueAnimator f6929z;
    private int q = 0;

    /* renamed from: r  reason: collision with root package name */
    private int f6921r = 0;

    /* renamed from: t  reason: collision with root package name */
    private boolean f6923t = false;

    /* renamed from: u  reason: collision with root package name */
    private boolean f6924u = false;

    /* renamed from: v  reason: collision with root package name */
    private int f6925v = 0;

    /* renamed from: w  reason: collision with root package name */
    private int f6926w = 0;

    /* renamed from: x  reason: collision with root package name */
    private final int[] f6927x = new int[2];

    /* renamed from: y  reason: collision with root package name */
    private final int[] f6928y = new int[2];

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            j.this.s(500);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends RecyclerView.s {
        b() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.s
        public void b(RecyclerView recyclerView, int i8, int i9) {
            j.this.D(recyclerView.computeHorizontalScrollOffset(), recyclerView.computeVerticalScrollOffset());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class c extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        private boolean f6932a = false;

        c() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f6932a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.f6932a) {
                this.f6932a = false;
            } else if (((Float) j.this.f6929z.getAnimatedValue()).floatValue() == 0.0f) {
                j jVar = j.this;
                jVar.A = 0;
                jVar.A(0);
            } else {
                j jVar2 = j.this;
                jVar2.A = 2;
                jVar2.x();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class d implements ValueAnimator.AnimatorUpdateListener {
        d() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int floatValue = (int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * 255.0f);
            j.this.f6907c.setAlpha(floatValue);
            j.this.f6908d.setAlpha(floatValue);
            j.this.x();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public j(RecyclerView recyclerView, StateListDrawable stateListDrawable, Drawable drawable, StateListDrawable stateListDrawable2, Drawable drawable2, int i8, int i9, int i10) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f6929z = ofFloat;
        this.A = 0;
        this.B = new a();
        this.C = new b();
        this.f6907c = stateListDrawable;
        this.f6908d = drawable;
        this.f6911g = stateListDrawable2;
        this.f6912h = drawable2;
        this.f6909e = Math.max(i8, stateListDrawable.getIntrinsicWidth());
        this.f6910f = Math.max(i8, drawable.getIntrinsicWidth());
        this.f6913i = Math.max(i8, stateListDrawable2.getIntrinsicWidth());
        this.f6914j = Math.max(i8, drawable2.getIntrinsicWidth());
        this.f6905a = i9;
        this.f6906b = i10;
        stateListDrawable.setAlpha(255);
        drawable.setAlpha(255);
        ofFloat.addListener(new c());
        ofFloat.addUpdateListener(new d());
        l(recyclerView);
    }

    private void B() {
        this.f6922s.h(this);
        this.f6922s.k(this);
        this.f6922s.l(this.C);
    }

    private void E(float f5) {
        int[] r4 = r();
        float max = Math.max(r4[0], Math.min(r4[1], f5));
        if (Math.abs(this.f6916l - max) < 2.0f) {
            return;
        }
        int z4 = z(this.f6917m, max, r4, this.f6922s.computeVerticalScrollRange(), this.f6922s.computeVerticalScrollOffset(), this.f6921r);
        if (z4 != 0) {
            this.f6922s.scrollBy(0, z4);
        }
        this.f6917m = max;
    }

    private void m() {
        this.f6922s.removeCallbacks(this.B);
    }

    private void n() {
        this.f6922s.Z0(this);
        this.f6922s.b1(this);
        this.f6922s.c1(this.C);
        m();
    }

    private void o(Canvas canvas) {
        int i8 = this.f6921r;
        int i9 = this.f6913i;
        int i10 = i8 - i9;
        int i11 = this.f6919o;
        int i12 = this.f6918n;
        int i13 = i11 - (i12 / 2);
        this.f6911g.setBounds(0, 0, i12, i9);
        this.f6912h.setBounds(0, 0, this.q, this.f6914j);
        canvas.translate(0.0f, i10);
        this.f6912h.draw(canvas);
        canvas.translate(i13, 0.0f);
        this.f6911g.draw(canvas);
        canvas.translate(-i13, -i10);
    }

    private void p(Canvas canvas) {
        int i8 = this.q;
        int i9 = this.f6909e;
        int i10 = i8 - i9;
        int i11 = this.f6916l;
        int i12 = this.f6915k;
        int i13 = i11 - (i12 / 2);
        this.f6907c.setBounds(0, 0, i9, i12);
        this.f6908d.setBounds(0, 0, this.f6910f, this.f6921r);
        if (u()) {
            this.f6908d.draw(canvas);
            canvas.translate(this.f6909e, i13);
            canvas.scale(-1.0f, 1.0f);
            this.f6907c.draw(canvas);
            canvas.scale(1.0f, 1.0f);
            i10 = this.f6909e;
        } else {
            canvas.translate(i10, 0.0f);
            this.f6908d.draw(canvas);
            canvas.translate(0.0f, i13);
            this.f6907c.draw(canvas);
        }
        canvas.translate(-i10, -i13);
    }

    private int[] q() {
        int[] iArr = this.f6928y;
        int i8 = this.f6906b;
        iArr[0] = i8;
        iArr[1] = this.q - i8;
        return iArr;
    }

    private int[] r() {
        int[] iArr = this.f6927x;
        int i8 = this.f6906b;
        iArr[0] = i8;
        iArr[1] = this.f6921r - i8;
        return iArr;
    }

    private void t(float f5) {
        int[] q = q();
        float max = Math.max(q[0], Math.min(q[1], f5));
        if (Math.abs(this.f6919o - max) < 2.0f) {
            return;
        }
        int z4 = z(this.f6920p, max, q, this.f6922s.computeHorizontalScrollRange(), this.f6922s.computeHorizontalScrollOffset(), this.q);
        if (z4 != 0) {
            this.f6922s.scrollBy(z4, 0);
        }
        this.f6920p = max;
    }

    private boolean u() {
        return c0.E(this.f6922s) == 1;
    }

    private void y(int i8) {
        m();
        this.f6922s.postDelayed(this.B, i8);
    }

    private int z(float f5, float f8, int[] iArr, int i8, int i9, int i10) {
        int i11 = iArr[1] - iArr[0];
        if (i11 == 0) {
            return 0;
        }
        int i12 = i8 - i10;
        int i13 = (int) (((f8 - f5) / i11) * i12);
        int i14 = i9 + i13;
        if (i14 >= i12 || i14 < 0) {
            return 0;
        }
        return i13;
    }

    void A(int i8) {
        int i9;
        if (i8 == 2 && this.f6925v != 2) {
            this.f6907c.setState(D);
            m();
        }
        if (i8 == 0) {
            x();
        } else {
            C();
        }
        if (this.f6925v != 2 || i8 == 2) {
            i9 = i8 == 1 ? 1500 : 1500;
            this.f6925v = i8;
        }
        this.f6907c.setState(E);
        i9 = 1200;
        y(i9);
        this.f6925v = i8;
    }

    public void C() {
        int i8 = this.A;
        if (i8 != 0) {
            if (i8 != 3) {
                return;
            }
            this.f6929z.cancel();
        }
        this.A = 1;
        ValueAnimator valueAnimator = this.f6929z;
        valueAnimator.setFloatValues(((Float) valueAnimator.getAnimatedValue()).floatValue(), 1.0f);
        this.f6929z.setDuration(500L);
        this.f6929z.setStartDelay(0L);
        this.f6929z.start();
    }

    void D(int i8, int i9) {
        int computeVerticalScrollRange = this.f6922s.computeVerticalScrollRange();
        int i10 = this.f6921r;
        this.f6923t = computeVerticalScrollRange - i10 > 0 && i10 >= this.f6905a;
        int computeHorizontalScrollRange = this.f6922s.computeHorizontalScrollRange();
        int i11 = this.q;
        boolean z4 = computeHorizontalScrollRange - i11 > 0 && i11 >= this.f6905a;
        this.f6924u = z4;
        boolean z8 = this.f6923t;
        if (!z8 && !z4) {
            if (this.f6925v != 0) {
                A(0);
                return;
            }
            return;
        }
        if (z8) {
            float f5 = i10;
            this.f6916l = (int) ((f5 * (i9 + (f5 / 2.0f))) / computeVerticalScrollRange);
            this.f6915k = Math.min(i10, (i10 * i10) / computeVerticalScrollRange);
        }
        if (this.f6924u) {
            float f8 = i11;
            this.f6919o = (int) ((f8 * (i8 + (f8 / 2.0f))) / computeHorizontalScrollRange);
            this.f6918n = Math.min(i11, (i11 * i11) / computeHorizontalScrollRange);
        }
        int i12 = this.f6925v;
        if (i12 == 0 || i12 == 1) {
            A(1);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.r
    public void a(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (this.f6925v == 0) {
            return;
        }
        if (motionEvent.getAction() == 0) {
            boolean w8 = w(motionEvent.getX(), motionEvent.getY());
            boolean v8 = v(motionEvent.getX(), motionEvent.getY());
            if (w8 || v8) {
                if (v8) {
                    this.f6926w = 1;
                    this.f6920p = (int) motionEvent.getX();
                } else if (w8) {
                    this.f6926w = 2;
                    this.f6917m = (int) motionEvent.getY();
                }
                A(2);
            }
        } else if (motionEvent.getAction() == 1 && this.f6925v == 2) {
            this.f6917m = 0.0f;
            this.f6920p = 0.0f;
            A(1);
            this.f6926w = 0;
        } else if (motionEvent.getAction() == 2 && this.f6925v == 2) {
            C();
            if (this.f6926w == 1) {
                t(motionEvent.getX());
            }
            if (this.f6926w == 2) {
                E(motionEvent.getY());
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.r
    public boolean c(RecyclerView recyclerView, MotionEvent motionEvent) {
        int i8 = this.f6925v;
        if (i8 == 1) {
            boolean w8 = w(motionEvent.getX(), motionEvent.getY());
            boolean v8 = v(motionEvent.getX(), motionEvent.getY());
            if (motionEvent.getAction() != 0) {
                return false;
            }
            if (!w8 && !v8) {
                return false;
            }
            if (v8) {
                this.f6926w = 1;
                this.f6920p = (int) motionEvent.getX();
            } else if (w8) {
                this.f6926w = 2;
                this.f6917m = (int) motionEvent.getY();
            }
            A(2);
        } else if (i8 != 2) {
            return false;
        }
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.r
    public void e(boolean z4) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.n
    public void k(Canvas canvas, RecyclerView recyclerView, RecyclerView.y yVar) {
        if (this.q != this.f6922s.getWidth() || this.f6921r != this.f6922s.getHeight()) {
            this.q = this.f6922s.getWidth();
            this.f6921r = this.f6922s.getHeight();
            A(0);
        } else if (this.A != 0) {
            if (this.f6923t) {
                p(canvas);
            }
            if (this.f6924u) {
                o(canvas);
            }
        }
    }

    public void l(RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.f6922s;
        if (recyclerView2 == recyclerView) {
            return;
        }
        if (recyclerView2 != null) {
            n();
        }
        this.f6922s = recyclerView;
        if (recyclerView != null) {
            B();
        }
    }

    void s(int i8) {
        int i9 = this.A;
        if (i9 == 1) {
            this.f6929z.cancel();
        } else if (i9 != 2) {
            return;
        }
        this.A = 3;
        ValueAnimator valueAnimator = this.f6929z;
        valueAnimator.setFloatValues(((Float) valueAnimator.getAnimatedValue()).floatValue(), 0.0f);
        this.f6929z.setDuration(i8);
        this.f6929z.start();
    }

    boolean v(float f5, float f8) {
        if (f8 >= this.f6921r - this.f6913i) {
            int i8 = this.f6919o;
            int i9 = this.f6918n;
            if (f5 >= i8 - (i9 / 2) && f5 <= i8 + (i9 / 2)) {
                return true;
            }
        }
        return false;
    }

    boolean w(float f5, float f8) {
        if (!u() ? f5 >= this.q - this.f6909e : f5 <= this.f6909e / 2) {
            int i8 = this.f6916l;
            int i9 = this.f6915k;
            if (f8 >= i8 - (i9 / 2) && f8 <= i8 + (i9 / 2)) {
                return true;
            }
        }
        return false;
    }

    void x() {
        this.f6922s.invalidate();
    }
}
