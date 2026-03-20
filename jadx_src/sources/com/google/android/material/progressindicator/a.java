package com.google.android.material.progressindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.ProgressBar;
import androidx.core.view.c0;
import com.google.android.material.internal.m;
import com.google.android.material.progressindicator.b;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a<S extends com.google.android.material.progressindicator.b> extends ProgressBar {
    static final int q = k7.k.G;

    /* renamed from: a  reason: collision with root package name */
    S f18245a;

    /* renamed from: b  reason: collision with root package name */
    private int f18246b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f18247c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f18248d;

    /* renamed from: e  reason: collision with root package name */
    private final int f18249e;

    /* renamed from: f  reason: collision with root package name */
    private final int f18250f;

    /* renamed from: g  reason: collision with root package name */
    private long f18251g;

    /* renamed from: h  reason: collision with root package name */
    t7.a f18252h;

    /* renamed from: j  reason: collision with root package name */
    private boolean f18253j;

    /* renamed from: k  reason: collision with root package name */
    private int f18254k;

    /* renamed from: l  reason: collision with root package name */
    private final Runnable f18255l;

    /* renamed from: m  reason: collision with root package name */
    private final Runnable f18256m;

    /* renamed from: n  reason: collision with root package name */
    private final androidx.vectordrawable.graphics.drawable.b f18257n;

    /* renamed from: p  reason: collision with root package name */
    private final androidx.vectordrawable.graphics.drawable.b f18258p;

    /* renamed from: com.google.android.material.progressindicator.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class RunnableC0138a implements Runnable {
        RunnableC0138a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            a.this.k();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            a.this.j();
            a.this.f18251g = -1L;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends androidx.vectordrawable.graphics.drawable.b {
        c() {
        }

        @Override // androidx.vectordrawable.graphics.drawable.b
        public void a(Drawable drawable) {
            a.this.setIndeterminate(false);
            a.this.o(0, false);
            a aVar = a.this;
            aVar.o(aVar.f18246b, a.this.f18247c);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d extends androidx.vectordrawable.graphics.drawable.b {
        d() {
        }

        @Override // androidx.vectordrawable.graphics.drawable.b
        public void a(Drawable drawable) {
            super.a(drawable);
            if (a.this.f18253j) {
                return;
            }
            a aVar = a.this;
            aVar.setVisibility(aVar.f18254k);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public a(Context context, AttributeSet attributeSet, int i8, int i9) {
        super(y7.a.c(context, attributeSet, i8, q), attributeSet, i8);
        this.f18251g = -1L;
        this.f18253j = false;
        this.f18254k = 4;
        this.f18255l = new RunnableC0138a();
        this.f18256m = new b();
        this.f18257n = new c();
        this.f18258p = new d();
        Context context2 = getContext();
        this.f18245a = i(context2, attributeSet);
        TypedArray h8 = m.h(context2, attributeSet, k7.l.K, i8, i9, new int[0]);
        this.f18249e = h8.getInt(k7.l.P, -1);
        this.f18250f = Math.min(h8.getInt(k7.l.N, -1), 1000);
        h8.recycle();
        this.f18252h = new t7.a();
        this.f18248d = true;
    }

    private g<S> getCurrentDrawingDelegate() {
        if (isIndeterminate()) {
            if (getIndeterminateDrawable() == null) {
                return null;
            }
            return getIndeterminateDrawable().v();
        } else if (getProgressDrawable() == null) {
            return null;
        } else {
            return getProgressDrawable().w();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        ((f) getCurrentDrawable()).p(false, false, true);
        if (m()) {
            setVisibility(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        if (this.f18250f > 0) {
            this.f18251g = SystemClock.uptimeMillis();
        }
        setVisibility(0);
    }

    private boolean m() {
        return (getProgressDrawable() == null || !getProgressDrawable().isVisible()) && (getIndeterminateDrawable() == null || !getIndeterminateDrawable().isVisible());
    }

    private void n() {
        if (getProgressDrawable() != null && getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().u().d(this.f18257n);
        }
        if (getProgressDrawable() != null) {
            getProgressDrawable().l(this.f18258p);
        }
        if (getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().l(this.f18258p);
        }
    }

    private void p() {
        if (getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().r(this.f18258p);
            getIndeterminateDrawable().u().h();
        }
        if (getProgressDrawable() != null) {
            getProgressDrawable().r(this.f18258p);
        }
    }

    @Override // android.widget.ProgressBar
    public Drawable getCurrentDrawable() {
        return isIndeterminate() ? getIndeterminateDrawable() : getProgressDrawable();
    }

    public int getHideAnimationBehavior() {
        return this.f18245a.f18268f;
    }

    @Override // android.widget.ProgressBar
    public i<S> getIndeterminateDrawable() {
        return (i) super.getIndeterminateDrawable();
    }

    public int[] getIndicatorColor() {
        return this.f18245a.f18265c;
    }

    @Override // android.widget.ProgressBar
    public e<S> getProgressDrawable() {
        return (e) super.getProgressDrawable();
    }

    public int getShowAnimationBehavior() {
        return this.f18245a.f18267e;
    }

    public int getTrackColor() {
        return this.f18245a.f18266d;
    }

    public int getTrackCornerRadius() {
        return this.f18245a.f18264b;
    }

    public int getTrackThickness() {
        return this.f18245a.f18263a;
    }

    protected void h(boolean z4) {
        if (this.f18248d) {
            ((f) getCurrentDrawable()).p(q(), false, z4);
        }
    }

    abstract S i(Context context, AttributeSet attributeSet);

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
        if (getCurrentDrawable() != null) {
            getCurrentDrawable().invalidateSelf();
        }
    }

    boolean l() {
        View view = this;
        while (view.getVisibility() == 0) {
            ViewParent parent = view.getParent();
            if (parent == null) {
                return getWindowVisibility() == 0;
            } else if (!(parent instanceof View)) {
                return true;
            } else {
                view = (View) parent;
            }
        }
        return false;
    }

    public void o(int i8, boolean z4) {
        if (!isIndeterminate()) {
            super.setProgress(i8);
            if (getProgressDrawable() == null || z4) {
                return;
            }
            getProgressDrawable().jumpToCurrentState();
        } else if (getProgressDrawable() != null) {
            this.f18246b = i8;
            this.f18247c = z4;
            this.f18253j = true;
            if (!getIndeterminateDrawable().isVisible() || this.f18252h.a(getContext().getContentResolver()) == 0.0f) {
                this.f18257n.a(getIndeterminateDrawable());
            } else {
                getIndeterminateDrawable().u().f();
            }
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        n();
        if (q()) {
            k();
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onDetachedFromWindow() {
        removeCallbacks(this.f18256m);
        removeCallbacks(this.f18255l);
        ((f) getCurrentDrawable()).h();
        p();
        super.onDetachedFromWindow();
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected synchronized void onDraw(Canvas canvas) {
        int save = canvas.save();
        if (getPaddingLeft() != 0 || getPaddingTop() != 0) {
            canvas.translate(getPaddingLeft(), getPaddingTop());
        }
        if (getPaddingRight() != 0 || getPaddingBottom() != 0) {
            canvas.clipRect(0, 0, getWidth() - (getPaddingLeft() + getPaddingRight()), getHeight() - (getPaddingTop() + getPaddingBottom()));
        }
        getCurrentDrawable().draw(canvas);
        canvas.restoreToCount(save);
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected synchronized void onMeasure(int i8, int i9) {
        super.onMeasure(i8, i9);
        g<S> currentDrawingDelegate = getCurrentDrawingDelegate();
        if (currentDrawingDelegate == null) {
            return;
        }
        int e8 = currentDrawingDelegate.e();
        int d8 = currentDrawingDelegate.d();
        setMeasuredDimension(e8 < 0 ? getMeasuredWidth() : e8 + getPaddingLeft() + getPaddingRight(), d8 < 0 ? getMeasuredHeight() : d8 + getPaddingTop() + getPaddingBottom());
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i8) {
        super.onVisibilityChanged(view, i8);
        h(i8 == 0);
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i8) {
        super.onWindowVisibilityChanged(i8);
        h(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean q() {
        return c0.V(this) && getWindowVisibility() == 0 && l();
    }

    public void setAnimatorDurationScaleProvider(t7.a aVar) {
        this.f18252h = aVar;
        if (getProgressDrawable() != null) {
            getProgressDrawable().f18295c = aVar;
        }
        if (getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().f18295c = aVar;
        }
    }

    public void setHideAnimationBehavior(int i8) {
        this.f18245a.f18268f = i8;
        invalidate();
    }

    @Override // android.widget.ProgressBar
    public synchronized void setIndeterminate(boolean z4) {
        if (z4 == isIndeterminate()) {
            return;
        }
        if (q() && z4) {
            throw new IllegalStateException("Cannot switch to indeterminate mode while the progress indicator is visible.");
        }
        f fVar = (f) getCurrentDrawable();
        if (fVar != null) {
            fVar.h();
        }
        super.setIndeterminate(z4);
        f fVar2 = (f) getCurrentDrawable();
        if (fVar2 != null) {
            fVar2.p(q(), false, false);
        }
        this.f18253j = false;
    }

    @Override // android.widget.ProgressBar
    public void setIndeterminateDrawable(Drawable drawable) {
        if (drawable == null) {
            super.setIndeterminateDrawable(null);
        } else if (!(drawable instanceof i)) {
            throw new IllegalArgumentException("Cannot set framework drawable as indeterminate drawable.");
        } else {
            ((f) drawable).h();
            super.setIndeterminateDrawable(drawable);
        }
    }

    public void setIndicatorColor(int... iArr) {
        if (iArr.length == 0) {
            iArr = new int[]{n7.a.b(getContext(), k7.b.q, -1)};
        }
        if (Arrays.equals(getIndicatorColor(), iArr)) {
            return;
        }
        this.f18245a.f18265c = iArr;
        getIndeterminateDrawable().u().c();
        invalidate();
    }

    @Override // android.widget.ProgressBar
    public synchronized void setProgress(int i8) {
        if (isIndeterminate()) {
            return;
        }
        o(i8, false);
    }

    @Override // android.widget.ProgressBar
    public void setProgressDrawable(Drawable drawable) {
        if (drawable == null) {
            super.setProgressDrawable(null);
        } else if (!(drawable instanceof e)) {
            throw new IllegalArgumentException("Cannot set framework drawable as progress drawable.");
        } else {
            e eVar = (e) drawable;
            eVar.h();
            super.setProgressDrawable(eVar);
            eVar.A(getProgress() / getMax());
        }
    }

    public void setShowAnimationBehavior(int i8) {
        this.f18245a.f18267e = i8;
        invalidate();
    }

    public void setTrackColor(int i8) {
        S s8 = this.f18245a;
        if (s8.f18266d != i8) {
            s8.f18266d = i8;
            invalidate();
        }
    }

    public void setTrackCornerRadius(int i8) {
        S s8 = this.f18245a;
        if (s8.f18264b != i8) {
            s8.f18264b = Math.min(i8, s8.f18263a / 2);
        }
    }

    public void setTrackThickness(int i8) {
        S s8 = this.f18245a;
        if (s8.f18263a != i8) {
            s8.f18263a = i8;
            requestLayout();
        }
    }

    public void setVisibilityAfterHide(int i8) {
        if (i8 != 0 && i8 != 4 && i8 != 8) {
            throw new IllegalArgumentException("The component's visibility must be one of VISIBLE, INVISIBLE, and GONE defined in View.");
        }
        this.f18254k = i8;
    }
}
