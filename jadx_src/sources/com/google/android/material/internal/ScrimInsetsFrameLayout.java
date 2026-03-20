package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.view.c0;
import androidx.core.view.m0;
import androidx.core.view.v;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ScrimInsetsFrameLayout extends FrameLayout {

    /* renamed from: a  reason: collision with root package name */
    Drawable f18041a;

    /* renamed from: b  reason: collision with root package name */
    Rect f18042b;

    /* renamed from: c  reason: collision with root package name */
    private Rect f18043c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f18044d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f18045e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements v {
        a() {
        }

        @Override // androidx.core.view.v
        public m0 a(View view, m0 m0Var) {
            ScrimInsetsFrameLayout scrimInsetsFrameLayout = ScrimInsetsFrameLayout.this;
            if (scrimInsetsFrameLayout.f18042b == null) {
                scrimInsetsFrameLayout.f18042b = new Rect();
            }
            ScrimInsetsFrameLayout.this.f18042b.set(m0Var.k(), m0Var.m(), m0Var.l(), m0Var.j());
            ScrimInsetsFrameLayout.this.a(m0Var);
            ScrimInsetsFrameLayout.this.setWillNotDraw(!m0Var.n() || ScrimInsetsFrameLayout.this.f18041a == null);
            c0.j0(ScrimInsetsFrameLayout.this);
            return m0Var.c();
        }
    }

    public ScrimInsetsFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScrimInsetsFrameLayout(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f18043c = new Rect();
        this.f18044d = true;
        this.f18045e = true;
        TypedArray h8 = m.h(context, attributeSet, k7.l.f21301e6, i8, k7.k.f21243n, new int[0]);
        this.f18041a = h8.getDrawable(k7.l.f21309f6);
        h8.recycle();
        setWillNotDraw(true);
        c0.I0(this, new a());
    }

    protected void a(m0 m0Var) {
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (this.f18042b == null || this.f18041a == null) {
            return;
        }
        int save = canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        if (this.f18044d) {
            this.f18043c.set(0, 0, width, this.f18042b.top);
            this.f18041a.setBounds(this.f18043c);
            this.f18041a.draw(canvas);
        }
        if (this.f18045e) {
            this.f18043c.set(0, height - this.f18042b.bottom, width, height);
            this.f18041a.setBounds(this.f18043c);
            this.f18041a.draw(canvas);
        }
        Rect rect = this.f18043c;
        Rect rect2 = this.f18042b;
        rect.set(0, rect2.top, rect2.left, height - rect2.bottom);
        this.f18041a.setBounds(this.f18043c);
        this.f18041a.draw(canvas);
        Rect rect3 = this.f18043c;
        Rect rect4 = this.f18042b;
        rect3.set(width - rect4.right, rect4.top, width, height - rect4.bottom);
        this.f18041a.setBounds(this.f18043c);
        this.f18041a.draw(canvas);
        canvas.restoreToCount(save);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Drawable drawable = this.f18041a;
        if (drawable != null) {
            drawable.setCallback(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Drawable drawable = this.f18041a;
        if (drawable != null) {
            drawable.setCallback(null);
        }
    }

    public void setDrawBottomInsetForeground(boolean z4) {
        this.f18045e = z4;
    }

    public void setDrawTopInsetForeground(boolean z4) {
        this.f18044d = z4;
    }

    public void setScrimInsetForeground(Drawable drawable) {
        this.f18041a = drawable;
    }
}
