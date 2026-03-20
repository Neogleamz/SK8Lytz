package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.MultiAutoCompleteTextView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AppCompatMultiAutoCompleteTextView extends MultiAutoCompleteTextView implements androidx.core.view.a0, androidx.core.widget.o {

    /* renamed from: d  reason: collision with root package name */
    private static final int[] f1166d = {16843126};

    /* renamed from: a  reason: collision with root package name */
    private final d f1167a;

    /* renamed from: b  reason: collision with root package name */
    private final p f1168b;

    /* renamed from: c  reason: collision with root package name */
    private final h f1169c;

    public AppCompatMultiAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, g.a.f19877p);
    }

    public AppCompatMultiAutoCompleteTextView(Context context, AttributeSet attributeSet, int i8) {
        super(g0.b(context), attributeSet, i8);
        e0.a(this, getContext());
        j0 v8 = j0.v(getContext(), attributeSet, f1166d, i8, 0);
        if (v8.s(0)) {
            setDropDownBackgroundDrawable(v8.g(0));
        }
        v8.w();
        d dVar = new d(this);
        this.f1167a = dVar;
        dVar.e(attributeSet, i8);
        p pVar = new p(this);
        this.f1168b = pVar;
        pVar.m(attributeSet, i8);
        pVar.b();
        h hVar = new h(this);
        this.f1169c = hVar;
        hVar.c(attributeSet, i8);
        a(hVar);
    }

    void a(h hVar) {
        KeyListener keyListener = getKeyListener();
        if (hVar.b(keyListener)) {
            boolean isFocusable = super.isFocusable();
            boolean isClickable = super.isClickable();
            boolean isLongClickable = super.isLongClickable();
            int inputType = super.getInputType();
            KeyListener a9 = hVar.a(keyListener);
            if (a9 == keyListener) {
                return;
            }
            super.setKeyListener(a9);
            super.setRawInputType(inputType);
            super.setFocusable(isFocusable);
            super.setClickable(isClickable);
            super.setLongClickable(isLongClickable);
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        d dVar = this.f1167a;
        if (dVar != null) {
            dVar.b();
        }
        p pVar = this.f1168b;
        if (pVar != null) {
            pVar.b();
        }
    }

    @Override // androidx.core.view.a0
    public ColorStateList getSupportBackgroundTintList() {
        d dVar = this.f1167a;
        if (dVar != null) {
            return dVar.c();
        }
        return null;
    }

    @Override // androidx.core.view.a0
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        d dVar = this.f1167a;
        if (dVar != null) {
            return dVar.d();
        }
        return null;
    }

    public ColorStateList getSupportCompoundDrawablesTintList() {
        return this.f1168b.j();
    }

    public PorterDuff.Mode getSupportCompoundDrawablesTintMode() {
        return this.f1168b.k();
    }

    @Override // android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        return this.f1169c.d(j.a(super.onCreateInputConnection(editorInfo), editorInfo, this), editorInfo);
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        d dVar = this.f1167a;
        if (dVar != null) {
            dVar.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i8) {
        super.setBackgroundResource(i8);
        d dVar = this.f1167a;
        if (dVar != null) {
            dVar.g(i8);
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        p pVar = this.f1168b;
        if (pVar != null) {
            pVar.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        p pVar = this.f1168b;
        if (pVar != null) {
            pVar.p();
        }
    }

    @Override // android.widget.AutoCompleteTextView
    public void setDropDownBackgroundResource(int i8) {
        setDropDownBackgroundDrawable(h.a.b(getContext(), i8));
    }

    public void setEmojiCompatEnabled(boolean z4) {
        this.f1169c.e(z4);
    }

    @Override // android.widget.TextView
    public void setKeyListener(KeyListener keyListener) {
        super.setKeyListener(this.f1169c.a(keyListener));
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        d dVar = this.f1167a;
        if (dVar != null) {
            dVar.i(colorStateList);
        }
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        d dVar = this.f1167a;
        if (dVar != null) {
            dVar.j(mode);
        }
    }

    @Override // androidx.core.widget.o
    public void setSupportCompoundDrawablesTintList(ColorStateList colorStateList) {
        this.f1168b.w(colorStateList);
        this.f1168b.b();
    }

    @Override // androidx.core.widget.o
    public void setSupportCompoundDrawablesTintMode(PorterDuff.Mode mode) {
        this.f1168b.x(mode);
        this.f1168b.b();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i8) {
        super.setTextAppearance(context, i8);
        p pVar = this.f1168b;
        if (pVar != null) {
            pVar.q(context, i8);
        }
    }
}
