package v7;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import x7.h;
import x7.m;
import x7.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends Drawable implements p, androidx.core.graphics.drawable.b {

    /* renamed from: a  reason: collision with root package name */
    private b f23349a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends Drawable.ConstantState {

        /* renamed from: a  reason: collision with root package name */
        h f23350a;

        /* renamed from: b  reason: collision with root package name */
        boolean f23351b;

        public b(b bVar) {
            this.f23350a = (h) bVar.f23350a.getConstantState().newDrawable();
            this.f23351b = bVar.f23351b;
        }

        public b(h hVar) {
            this.f23350a = hVar;
            this.f23351b = false;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        /* renamed from: a */
        public a newDrawable() {
            return new a(new b(this));
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return 0;
        }
    }

    private a(b bVar) {
        this.f23349a = bVar;
    }

    public a(m mVar) {
        this(new b(new h(mVar)));
    }

    @Override // android.graphics.drawable.Drawable
    /* renamed from: a */
    public a mutate() {
        this.f23349a = new b(this.f23349a);
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        b bVar = this.f23349a;
        if (bVar.f23351b) {
            bVar.f23350a.draw(canvas);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.f23349a;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return this.f23349a.f23350a.getOpacity();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.f23349a.f23350a.setBounds(rect);
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        boolean onStateChange = super.onStateChange(iArr);
        if (this.f23349a.f23350a.setState(iArr)) {
            onStateChange = true;
        }
        boolean e8 = v7.b.e(iArr);
        b bVar = this.f23349a;
        if (bVar.f23351b != e8) {
            bVar.f23351b = e8;
            return true;
        }
        return onStateChange;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        this.f23349a.f23350a.setAlpha(i8);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f23349a.f23350a.setColorFilter(colorFilter);
    }

    @Override // x7.p
    public void setShapeAppearanceModel(m mVar) {
        this.f23349a.f23350a.setShapeAppearanceModel(mVar);
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTint(int i8) {
        this.f23349a.f23350a.setTint(i8);
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTintList(ColorStateList colorStateList) {
        this.f23349a.f23350a.setTintList(colorStateList);
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTintMode(PorterDuff.Mode mode) {
        this.f23349a.f23350a.setTintMode(mode);
    }
}
