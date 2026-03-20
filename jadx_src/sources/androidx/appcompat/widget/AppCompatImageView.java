package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AppCompatImageView extends ImageView implements androidx.core.view.a0, androidx.core.widget.p {

    /* renamed from: a  reason: collision with root package name */
    private final d f1163a;

    /* renamed from: b  reason: collision with root package name */
    private final k f1164b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f1165c;

    public AppCompatImageView(Context context) {
        this(context, null);
    }

    public AppCompatImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AppCompatImageView(Context context, AttributeSet attributeSet, int i8) {
        super(g0.b(context), attributeSet, i8);
        this.f1165c = false;
        e0.a(this, getContext());
        d dVar = new d(this);
        this.f1163a = dVar;
        dVar.e(attributeSet, i8);
        k kVar = new k(this);
        this.f1164b = kVar;
        kVar.g(attributeSet, i8);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        d dVar = this.f1163a;
        if (dVar != null) {
            dVar.b();
        }
        k kVar = this.f1164b;
        if (kVar != null) {
            kVar.c();
        }
    }

    @Override // androidx.core.view.a0
    public ColorStateList getSupportBackgroundTintList() {
        d dVar = this.f1163a;
        if (dVar != null) {
            return dVar.c();
        }
        return null;
    }

    @Override // androidx.core.view.a0
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        d dVar = this.f1163a;
        if (dVar != null) {
            return dVar.d();
        }
        return null;
    }

    @Override // androidx.core.widget.p
    public ColorStateList getSupportImageTintList() {
        k kVar = this.f1164b;
        if (kVar != null) {
            return kVar.d();
        }
        return null;
    }

    @Override // androidx.core.widget.p
    public PorterDuff.Mode getSupportImageTintMode() {
        k kVar = this.f1164b;
        if (kVar != null) {
            return kVar.e();
        }
        return null;
    }

    @Override // android.widget.ImageView, android.view.View
    public boolean hasOverlappingRendering() {
        return this.f1164b.f() && super.hasOverlappingRendering();
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        d dVar = this.f1163a;
        if (dVar != null) {
            dVar.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i8) {
        super.setBackgroundResource(i8);
        d dVar = this.f1163a;
        if (dVar != null) {
            dVar.g(i8);
        }
    }

    @Override // android.widget.ImageView
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        k kVar = this.f1164b;
        if (kVar != null) {
            kVar.c();
        }
    }

    @Override // android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        k kVar = this.f1164b;
        if (kVar != null && drawable != null && !this.f1165c) {
            kVar.h(drawable);
        }
        super.setImageDrawable(drawable);
        k kVar2 = this.f1164b;
        if (kVar2 != null) {
            kVar2.c();
            if (this.f1165c) {
                return;
            }
            this.f1164b.b();
        }
    }

    @Override // android.widget.ImageView
    public void setImageLevel(int i8) {
        super.setImageLevel(i8);
        this.f1165c = true;
    }

    @Override // android.widget.ImageView
    public void setImageResource(int i8) {
        k kVar = this.f1164b;
        if (kVar != null) {
            kVar.i(i8);
        }
    }

    @Override // android.widget.ImageView
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        k kVar = this.f1164b;
        if (kVar != null) {
            kVar.c();
        }
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        d dVar = this.f1163a;
        if (dVar != null) {
            dVar.i(colorStateList);
        }
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        d dVar = this.f1163a;
        if (dVar != null) {
            dVar.j(mode);
        }
    }

    @Override // androidx.core.widget.p
    public void setSupportImageTintList(ColorStateList colorStateList) {
        k kVar = this.f1164b;
        if (kVar != null) {
            kVar.j(colorStateList);
        }
    }

    @Override // androidx.core.widget.p
    public void setSupportImageTintMode(PorterDuff.Mode mode) {
        k kVar = this.f1164b;
        if (kVar != null) {
            kVar.k(mode);
        }
    }
}
