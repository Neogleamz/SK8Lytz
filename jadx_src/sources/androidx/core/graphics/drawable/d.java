package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d extends Drawable implements Drawable.Callback, c, b {

    /* renamed from: g  reason: collision with root package name */
    static final PorterDuff.Mode f4728g = PorterDuff.Mode.SRC_IN;

    /* renamed from: a  reason: collision with root package name */
    private int f4729a;

    /* renamed from: b  reason: collision with root package name */
    private PorterDuff.Mode f4730b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f4731c;

    /* renamed from: d  reason: collision with root package name */
    f f4732d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f4733e;

    /* renamed from: f  reason: collision with root package name */
    Drawable f4734f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(Drawable drawable) {
        this.f4732d = d();
        a(drawable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(f fVar, Resources resources) {
        this.f4732d = fVar;
        e(resources);
    }

    private f d() {
        return new f(this.f4732d);
    }

    private void e(Resources resources) {
        Drawable.ConstantState constantState;
        f fVar = this.f4732d;
        if (fVar == null || (constantState = fVar.f4737b) == null) {
            return;
        }
        a(constantState.newDrawable(resources));
    }

    private boolean f(int[] iArr) {
        if (c()) {
            f fVar = this.f4732d;
            ColorStateList colorStateList = fVar.f4738c;
            PorterDuff.Mode mode = fVar.f4739d;
            if (colorStateList == null || mode == null) {
                this.f4731c = false;
                clearColorFilter();
            } else {
                int colorForState = colorStateList.getColorForState(iArr, colorStateList.getDefaultColor());
                if (!this.f4731c || colorForState != this.f4729a || mode != this.f4730b) {
                    setColorFilter(colorForState, mode);
                    this.f4729a = colorForState;
                    this.f4730b = mode;
                    this.f4731c = true;
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // androidx.core.graphics.drawable.c
    public final void a(Drawable drawable) {
        Drawable drawable2 = this.f4734f;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.f4734f = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            setVisible(drawable.isVisible(), true);
            setState(drawable.getState());
            setLevel(drawable.getLevel());
            setBounds(drawable.getBounds());
            f fVar = this.f4732d;
            if (fVar != null) {
                fVar.f4737b = drawable.getConstantState();
            }
        }
        invalidateSelf();
    }

    @Override // androidx.core.graphics.drawable.c
    public final Drawable b() {
        return this.f4734f;
    }

    protected boolean c() {
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.f4734f.draw(canvas);
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        int changingConfigurations = super.getChangingConfigurations();
        f fVar = this.f4732d;
        return changingConfigurations | (fVar != null ? fVar.getChangingConfigurations() : 0) | this.f4734f.getChangingConfigurations();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        f fVar = this.f4732d;
        if (fVar == null || !fVar.a()) {
            return null;
        }
        this.f4732d.f4736a = getChangingConfigurations();
        return this.f4732d;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable getCurrent() {
        return this.f4734f.getCurrent();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.f4734f.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.f4734f.getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getLayoutDirection() {
        return a.f(this.f4734f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        return this.f4734f.getMinimumHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumWidth() {
        return this.f4734f.getMinimumWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return this.f4734f.getOpacity();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        return this.f4734f.getPadding(rect);
    }

    @Override // android.graphics.drawable.Drawable
    public int[] getState() {
        return this.f4734f.getState();
    }

    @Override // android.graphics.drawable.Drawable
    public Region getTransparentRegion() {
        return this.f4734f.getTransparentRegion();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        return a.h(this.f4734f);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        f fVar;
        ColorStateList colorStateList = (!c() || (fVar = this.f4732d) == null) ? null : fVar.f4738c;
        return (colorStateList != null && colorStateList.isStateful()) || this.f4734f.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        this.f4734f.jumpToCurrentState();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.f4733e && super.mutate() == this) {
            this.f4732d = d();
            Drawable drawable = this.f4734f;
            if (drawable != null) {
                drawable.mutate();
            }
            f fVar = this.f4732d;
            if (fVar != null) {
                Drawable drawable2 = this.f4734f;
                fVar.f4737b = drawable2 != null ? drawable2.getConstantState() : null;
            }
            this.f4733e = true;
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        Drawable drawable = this.f4734f;
        if (drawable != null) {
            drawable.setBounds(rect);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onLayoutDirectionChanged(int i8) {
        return a.m(this.f4734f, i8);
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int i8) {
        return this.f4734f.setLevel(i8);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j8) {
        scheduleSelf(runnable, j8);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        this.f4734f.setAlpha(i8);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean z4) {
        a.j(this.f4734f, z4);
    }

    @Override // android.graphics.drawable.Drawable
    public void setChangingConfigurations(int i8) {
        this.f4734f.setChangingConfigurations(i8);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f4734f.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean z4) {
        this.f4734f.setDither(z4);
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean z4) {
        this.f4734f.setFilterBitmap(z4);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setState(int[] iArr) {
        return f(iArr) || this.f4734f.setState(iArr);
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTint(int i8) {
        setTintList(ColorStateList.valueOf(i8));
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTintList(ColorStateList colorStateList) {
        this.f4732d.f4738c = colorStateList;
        f(getState());
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTintMode(PorterDuff.Mode mode) {
        this.f4732d.f4739d = mode;
        f(getState());
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z4, boolean z8) {
        return super.setVisible(z4, z8) || this.f4734f.setVisible(z4, z8);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        unscheduleSelf(runnable);
    }
}
