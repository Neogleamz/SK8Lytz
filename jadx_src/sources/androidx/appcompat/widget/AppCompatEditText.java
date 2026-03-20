package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.DragEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.textclassifier.TextClassifier;
import android.widget.EditText;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AppCompatEditText extends EditText implements androidx.core.view.a0, androidx.core.view.x, androidx.core.widget.o {

    /* renamed from: a  reason: collision with root package name */
    private final d f1153a;

    /* renamed from: b  reason: collision with root package name */
    private final p f1154b;

    /* renamed from: c  reason: collision with root package name */
    private final o f1155c;

    /* renamed from: d  reason: collision with root package name */
    private final androidx.core.widget.l f1156d;

    /* renamed from: e  reason: collision with root package name */
    private final h f1157e;

    /* renamed from: f  reason: collision with root package name */
    private a f1158f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a {
        a() {
        }

        public TextClassifier a() {
            return AppCompatEditText.super.getTextClassifier();
        }

        public void b(TextClassifier textClassifier) {
            AppCompatEditText.super.setTextClassifier(textClassifier);
        }
    }

    public AppCompatEditText(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, g.a.D);
    }

    public AppCompatEditText(Context context, AttributeSet attributeSet, int i8) {
        super(g0.b(context), attributeSet, i8);
        e0.a(this, getContext());
        d dVar = new d(this);
        this.f1153a = dVar;
        dVar.e(attributeSet, i8);
        p pVar = new p(this);
        this.f1154b = pVar;
        pVar.m(attributeSet, i8);
        pVar.b();
        this.f1155c = new o(this);
        this.f1156d = new androidx.core.widget.l();
        h hVar = new h(this);
        this.f1157e = hVar;
        hVar.c(attributeSet, i8);
        d(hVar);
    }

    private a getSuperCaller() {
        if (this.f1158f == null) {
            this.f1158f = new a();
        }
        return this.f1158f;
    }

    @Override // androidx.core.view.x
    public androidx.core.view.c a(androidx.core.view.c cVar) {
        return this.f1156d.a(this, cVar);
    }

    void d(h hVar) {
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
        d dVar = this.f1153a;
        if (dVar != null) {
            dVar.b();
        }
        p pVar = this.f1154b;
        if (pVar != null) {
            pVar.b();
        }
    }

    @Override // android.widget.TextView
    public ActionMode.Callback getCustomSelectionActionModeCallback() {
        return androidx.core.widget.k.s(super.getCustomSelectionActionModeCallback());
    }

    @Override // androidx.core.view.a0
    public ColorStateList getSupportBackgroundTintList() {
        d dVar = this.f1153a;
        if (dVar != null) {
            return dVar.c();
        }
        return null;
    }

    @Override // androidx.core.view.a0
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        d dVar = this.f1153a;
        if (dVar != null) {
            return dVar.d();
        }
        return null;
    }

    public ColorStateList getSupportCompoundDrawablesTintList() {
        return this.f1154b.j();
    }

    public PorterDuff.Mode getSupportCompoundDrawablesTintMode() {
        return this.f1154b.k();
    }

    @Override // android.widget.EditText, android.widget.TextView
    public Editable getText() {
        return Build.VERSION.SDK_INT >= 28 ? super.getText() : super.getEditableText();
    }

    @Override // android.widget.TextView
    public TextClassifier getTextClassifier() {
        o oVar;
        return (Build.VERSION.SDK_INT >= 28 || (oVar = this.f1155c) == null) ? getSuperCaller().a() : oVar.a();
    }

    @Override // android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        String[] H;
        InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
        this.f1154b.r(this, onCreateInputConnection, editorInfo);
        InputConnection a9 = j.a(onCreateInputConnection, editorInfo, this);
        if (a9 != null && Build.VERSION.SDK_INT <= 30 && (H = androidx.core.view.c0.H(this)) != null) {
            u0.a.d(editorInfo, H);
            a9 = u0.c.c(this, a9, editorInfo);
        }
        return this.f1157e.d(a9, editorInfo);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onDragEvent(DragEvent dragEvent) {
        if (m.a(this, dragEvent)) {
            return true;
        }
        return super.onDragEvent(dragEvent);
    }

    @Override // android.widget.TextView
    public boolean onTextContextMenuItem(int i8) {
        if (m.b(this, i8)) {
            return true;
        }
        return super.onTextContextMenuItem(i8);
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        d dVar = this.f1153a;
        if (dVar != null) {
            dVar.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i8) {
        super.setBackgroundResource(i8);
        d dVar = this.f1153a;
        if (dVar != null) {
            dVar.g(i8);
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        p pVar = this.f1154b;
        if (pVar != null) {
            pVar.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        p pVar = this.f1154b;
        if (pVar != null) {
            pVar.p();
        }
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(androidx.core.widget.k.t(this, callback));
    }

    public void setEmojiCompatEnabled(boolean z4) {
        this.f1157e.e(z4);
    }

    @Override // android.widget.TextView
    public void setKeyListener(KeyListener keyListener) {
        super.setKeyListener(this.f1157e.a(keyListener));
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        d dVar = this.f1153a;
        if (dVar != null) {
            dVar.i(colorStateList);
        }
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        d dVar = this.f1153a;
        if (dVar != null) {
            dVar.j(mode);
        }
    }

    @Override // androidx.core.widget.o
    public void setSupportCompoundDrawablesTintList(ColorStateList colorStateList) {
        this.f1154b.w(colorStateList);
        this.f1154b.b();
    }

    @Override // androidx.core.widget.o
    public void setSupportCompoundDrawablesTintMode(PorterDuff.Mode mode) {
        this.f1154b.x(mode);
        this.f1154b.b();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i8) {
        super.setTextAppearance(context, i8);
        p pVar = this.f1154b;
        if (pVar != null) {
            pVar.q(context, i8);
        }
    }

    @Override // android.widget.TextView
    public void setTextClassifier(TextClassifier textClassifier) {
        o oVar;
        if (Build.VERSION.SDK_INT >= 28 || (oVar = this.f1155c) == null) {
            getSuperCaller().b(textClassifier);
        } else {
            oVar.b(textClassifier);
        }
    }
}
