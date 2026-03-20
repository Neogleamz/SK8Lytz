package com.google.android.material.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import androidx.appcompat.widget.LinearLayoutCompat;
import com.example.seedpoint.R;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ForegroundLinearLayout extends LinearLayoutCompat {
    boolean A;

    /* renamed from: t  reason: collision with root package name */
    private Drawable f18035t;

    /* renamed from: w  reason: collision with root package name */
    private final Rect f18036w;

    /* renamed from: x  reason: collision with root package name */
    private final Rect f18037x;

    /* renamed from: y  reason: collision with root package name */
    private int f18038y;

    /* renamed from: z  reason: collision with root package name */
    protected boolean f18039z;

    public ForegroundLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ForegroundLinearLayout(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f18036w = new Rect();
        this.f18037x = new Rect();
        this.f18038y = 119;
        this.f18039z = true;
        this.A = false;
        TypedArray h8 = m.h(context, attributeSet, k7.l.Z2, i8, 0, new int[0]);
        this.f18038y = h8.getInt(k7.l.f21269b3, this.f18038y);
        Drawable drawable = h8.getDrawable(k7.l.f21259a3);
        if (drawable != null) {
            setForeground(drawable);
        }
        this.f18039z = h8.getBoolean(k7.l.f21279c3, true);
        h8.recycle();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Drawable drawable = this.f18035t;
        if (drawable != null) {
            if (this.A) {
                this.A = false;
                Rect rect = this.f18036w;
                Rect rect2 = this.f18037x;
                int right = getRight() - getLeft();
                int bottom = getBottom() - getTop();
                if (this.f18039z) {
                    rect.set(0, 0, right, bottom);
                } else {
                    rect.set(getPaddingLeft(), getPaddingTop(), right - getPaddingRight(), bottom - getPaddingBottom());
                }
                Gravity.apply(this.f18038y, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), rect, rect2);
                drawable.setBounds(rect2);
            }
            drawable.draw(canvas);
        }
    }

    @Override // android.view.View
    @TargetApi(21)
    public void drawableHotspotChanged(float f5, float f8) {
        super.drawableHotspotChanged(f5, f8);
        Drawable drawable = this.f18035t;
        if (drawable != null) {
            drawable.setHotspot(f5, f8);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.f18035t;
        if (drawable == null || !drawable.isStateful()) {
            return;
        }
        this.f18035t.setState(getDrawableState());
    }

    @Override // android.view.View
    public Drawable getForeground() {
        return this.f18035t;
    }

    @Override // android.view.View
    public int getForegroundGravity() {
        return this.f18038y;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.f18035t;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        this.A = z4 | this.A;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i8, int i9, int i10, int i11) {
        super.onSizeChanged(i8, i9, i10, i11);
        this.A = true;
    }

    @Override // android.view.View
    public void setForeground(Drawable drawable) {
        Drawable drawable2 = this.f18035t;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback(null);
                unscheduleDrawable(this.f18035t);
            }
            this.f18035t = drawable;
            if (drawable != null) {
                setWillNotDraw(false);
                drawable.setCallback(this);
                if (drawable.isStateful()) {
                    drawable.setState(getDrawableState());
                }
                if (this.f18038y == 119) {
                    drawable.getPadding(new Rect());
                }
            } else {
                setWillNotDraw(true);
            }
            requestLayout();
            invalidate();
        }
    }

    @Override // android.view.View
    public void setForegroundGravity(int i8) {
        if (this.f18038y != i8) {
            if ((8388615 & i8) == 0) {
                i8 |= 8388611;
            }
            if ((i8 & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle) == 0) {
                i8 |= 48;
            }
            this.f18038y = i8;
            if (i8 == 119 && this.f18035t != null) {
                this.f18035t.getPadding(new Rect());
            }
            requestLayout();
        }
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.f18035t;
    }
}
