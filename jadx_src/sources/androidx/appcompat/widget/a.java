package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a extends ViewGroup {

    /* renamed from: a  reason: collision with root package name */
    protected final C0013a f1401a;

    /* renamed from: b  reason: collision with root package name */
    protected final Context f1402b;

    /* renamed from: c  reason: collision with root package name */
    protected ActionMenuView f1403c;

    /* renamed from: d  reason: collision with root package name */
    protected ActionMenuPresenter f1404d;

    /* renamed from: e  reason: collision with root package name */
    protected int f1405e;

    /* renamed from: f  reason: collision with root package name */
    protected androidx.core.view.i0 f1406f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f1407g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f1408h;

    /* renamed from: androidx.appcompat.widget.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    protected class C0013a implements androidx.core.view.j0 {

        /* renamed from: a  reason: collision with root package name */
        private boolean f1409a = false;

        /* renamed from: b  reason: collision with root package name */
        int f1410b;

        protected C0013a() {
        }

        @Override // androidx.core.view.j0
        public void a(View view) {
            this.f1409a = true;
        }

        @Override // androidx.core.view.j0
        public void b(View view) {
            if (this.f1409a) {
                return;
            }
            a aVar = a.this;
            aVar.f1406f = null;
            a.super.setVisibility(this.f1410b);
        }

        @Override // androidx.core.view.j0
        public void c(View view) {
            a.super.setVisibility(0);
            this.f1409a = false;
        }

        public C0013a d(androidx.core.view.i0 i0Var, int i8) {
            a.this.f1406f = i0Var;
            this.f1410b = i8;
            return this;
        }
    }

    a(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f1401a = new C0013a();
        TypedValue typedValue = new TypedValue();
        if (!context.getTheme().resolveAttribute(g.a.f19862a, typedValue, true) || typedValue.resourceId == 0) {
            this.f1402b = context;
        } else {
            this.f1402b = new ContextThemeWrapper(context, typedValue.resourceId);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int d(int i8, int i9, boolean z4) {
        return z4 ? i8 - i9 : i8 + i9;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int c(View view, int i8, int i9, int i10) {
        view.measure(View.MeasureSpec.makeMeasureSpec(i8, Integer.MIN_VALUE), i9);
        return Math.max(0, (i8 - view.getMeasuredWidth()) - i10);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int e(View view, int i8, int i9, int i10, boolean z4) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        int i11 = i9 + ((i10 - measuredHeight) / 2);
        if (z4) {
            view.layout(i8 - measuredWidth, i11, i8, measuredHeight + i11);
        } else {
            view.layout(i8, i11, i8 + measuredWidth, measuredHeight + i11);
        }
        return z4 ? -measuredWidth : measuredWidth;
    }

    public androidx.core.view.i0 f(int i8, long j8) {
        androidx.core.view.i0 b9;
        androidx.core.view.i0 i0Var = this.f1406f;
        if (i0Var != null) {
            i0Var.c();
        }
        if (i8 == 0) {
            if (getVisibility() != 0) {
                setAlpha(0.0f);
            }
            b9 = androidx.core.view.c0.e(this).b(1.0f);
        } else {
            b9 = androidx.core.view.c0.e(this).b(0.0f);
        }
        b9.f(j8);
        b9.h(this.f1401a.d(b9, i8));
        return b9;
    }

    public int getAnimatedVisibility() {
        return this.f1406f != null ? this.f1401a.f1410b : getVisibility();
    }

    public int getContentHeight() {
        return this.f1405e;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(null, g.j.f20003a, g.a.f19864c, 0);
        setContentHeight(obtainStyledAttributes.getLayoutDimension(g.j.f20052j, 0));
        obtainStyledAttributes.recycle();
        ActionMenuPresenter actionMenuPresenter = this.f1404d;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.J(configuration);
        }
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 9) {
            this.f1408h = false;
        }
        if (!this.f1408h) {
            boolean onHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !onHoverEvent) {
                this.f1408h = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.f1408h = false;
        }
        return true;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.f1407g = false;
        }
        if (!this.f1407g) {
            boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !onTouchEvent) {
                this.f1407g = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.f1407g = false;
        }
        return true;
    }

    public void setContentHeight(int i8) {
        this.f1405e = i8;
        requestLayout();
    }

    @Override // android.view.View
    public void setVisibility(int i8) {
        if (i8 != getVisibility()) {
            androidx.core.view.i0 i0Var = this.f1406f;
            if (i0Var != null) {
                i0Var.c();
            }
            super.setVisibility(i8);
        }
    }
}
