package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.RadioButton;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AppCompatRadioButton extends RadioButton implements androidx.core.widget.n, androidx.core.view.a0, androidx.core.widget.o {

    /* renamed from: a  reason: collision with root package name */
    private final f f1172a;

    /* renamed from: b  reason: collision with root package name */
    private final d f1173b;

    /* renamed from: c  reason: collision with root package name */
    private final p f1174c;

    /* renamed from: d  reason: collision with root package name */
    private i f1175d;

    public AppCompatRadioButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, g.a.J);
    }

    public AppCompatRadioButton(Context context, AttributeSet attributeSet, int i8) {
        super(g0.b(context), attributeSet, i8);
        e0.a(this, getContext());
        f fVar = new f(this);
        this.f1172a = fVar;
        fVar.e(attributeSet, i8);
        d dVar = new d(this);
        this.f1173b = dVar;
        dVar.e(attributeSet, i8);
        p pVar = new p(this);
        this.f1174c = pVar;
        pVar.m(attributeSet, i8);
        getEmojiTextViewHelper().c(attributeSet, i8);
    }

    private i getEmojiTextViewHelper() {
        if (this.f1175d == null) {
            this.f1175d = new i(this);
        }
        return this.f1175d;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        d dVar = this.f1173b;
        if (dVar != null) {
            dVar.b();
        }
        p pVar = this.f1174c;
        if (pVar != null) {
            pVar.b();
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView
    public int getCompoundPaddingLeft() {
        int compoundPaddingLeft = super.getCompoundPaddingLeft();
        f fVar = this.f1172a;
        return fVar != null ? fVar.b(compoundPaddingLeft) : compoundPaddingLeft;
    }

    @Override // androidx.core.view.a0
    public ColorStateList getSupportBackgroundTintList() {
        d dVar = this.f1173b;
        if (dVar != null) {
            return dVar.c();
        }
        return null;
    }

    @Override // androidx.core.view.a0
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        d dVar = this.f1173b;
        if (dVar != null) {
            return dVar.d();
        }
        return null;
    }

    @Override // androidx.core.widget.n
    public ColorStateList getSupportButtonTintList() {
        f fVar = this.f1172a;
        if (fVar != null) {
            return fVar.c();
        }
        return null;
    }

    public PorterDuff.Mode getSupportButtonTintMode() {
        f fVar = this.f1172a;
        if (fVar != null) {
            return fVar.d();
        }
        return null;
    }

    public ColorStateList getSupportCompoundDrawablesTintList() {
        return this.f1174c.j();
    }

    public PorterDuff.Mode getSupportCompoundDrawablesTintMode() {
        return this.f1174c.k();
    }

    @Override // android.widget.TextView
    public void setAllCaps(boolean z4) {
        super.setAllCaps(z4);
        getEmojiTextViewHelper().d(z4);
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        d dVar = this.f1173b;
        if (dVar != null) {
            dVar.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i8) {
        super.setBackgroundResource(i8);
        d dVar = this.f1173b;
        if (dVar != null) {
            dVar.g(i8);
        }
    }

    @Override // android.widget.CompoundButton
    public void setButtonDrawable(int i8) {
        setButtonDrawable(h.a.b(getContext(), i8));
    }

    @Override // android.widget.CompoundButton
    public void setButtonDrawable(Drawable drawable) {
        super.setButtonDrawable(drawable);
        f fVar = this.f1172a;
        if (fVar != null) {
            fVar.f();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        p pVar = this.f1174c;
        if (pVar != null) {
            pVar.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        p pVar = this.f1174c;
        if (pVar != null) {
            pVar.p();
        }
    }

    public void setEmojiCompatEnabled(boolean z4) {
        getEmojiTextViewHelper().e(z4);
    }

    @Override // android.widget.TextView
    public void setFilters(InputFilter[] inputFilterArr) {
        super.setFilters(getEmojiTextViewHelper().a(inputFilterArr));
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        d dVar = this.f1173b;
        if (dVar != null) {
            dVar.i(colorStateList);
        }
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        d dVar = this.f1173b;
        if (dVar != null) {
            dVar.j(mode);
        }
    }

    @Override // androidx.core.widget.n
    public void setSupportButtonTintList(ColorStateList colorStateList) {
        f fVar = this.f1172a;
        if (fVar != null) {
            fVar.g(colorStateList);
        }
    }

    @Override // androidx.core.widget.n
    public void setSupportButtonTintMode(PorterDuff.Mode mode) {
        f fVar = this.f1172a;
        if (fVar != null) {
            fVar.h(mode);
        }
    }

    @Override // androidx.core.widget.o
    public void setSupportCompoundDrawablesTintList(ColorStateList colorStateList) {
        this.f1174c.w(colorStateList);
        this.f1174c.b();
    }

    @Override // androidx.core.widget.o
    public void setSupportCompoundDrawablesTintMode(PorterDuff.Mode mode) {
        this.f1174c.x(mode);
        this.f1174c.b();
    }
}
