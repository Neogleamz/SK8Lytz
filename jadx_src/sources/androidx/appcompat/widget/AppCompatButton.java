package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AppCompatButton extends Button implements androidx.core.view.a0, androidx.core.widget.o {

    /* renamed from: a  reason: collision with root package name */
    private final d f1142a;

    /* renamed from: b  reason: collision with root package name */
    private final p f1143b;

    /* renamed from: c  reason: collision with root package name */
    private i f1144c;

    public AppCompatButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, g.a.q);
    }

    public AppCompatButton(Context context, AttributeSet attributeSet, int i8) {
        super(g0.b(context), attributeSet, i8);
        e0.a(this, getContext());
        d dVar = new d(this);
        this.f1142a = dVar;
        dVar.e(attributeSet, i8);
        p pVar = new p(this);
        this.f1143b = pVar;
        pVar.m(attributeSet, i8);
        pVar.b();
        getEmojiTextViewHelper().c(attributeSet, i8);
    }

    private i getEmojiTextViewHelper() {
        if (this.f1144c == null) {
            this.f1144c = new i(this);
        }
        return this.f1144c;
    }

    @Override // android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        d dVar = this.f1142a;
        if (dVar != null) {
            dVar.b();
        }
        p pVar = this.f1143b;
        if (pVar != null) {
            pVar.b();
        }
    }

    @Override // android.widget.TextView
    public int getAutoSizeMaxTextSize() {
        if (u0.f1636b) {
            return super.getAutoSizeMaxTextSize();
        }
        p pVar = this.f1143b;
        if (pVar != null) {
            return pVar.e();
        }
        return -1;
    }

    @Override // android.widget.TextView
    public int getAutoSizeMinTextSize() {
        if (u0.f1636b) {
            return super.getAutoSizeMinTextSize();
        }
        p pVar = this.f1143b;
        if (pVar != null) {
            return pVar.f();
        }
        return -1;
    }

    @Override // android.widget.TextView
    public int getAutoSizeStepGranularity() {
        if (u0.f1636b) {
            return super.getAutoSizeStepGranularity();
        }
        p pVar = this.f1143b;
        if (pVar != null) {
            return pVar.g();
        }
        return -1;
    }

    @Override // android.widget.TextView
    public int[] getAutoSizeTextAvailableSizes() {
        if (u0.f1636b) {
            return super.getAutoSizeTextAvailableSizes();
        }
        p pVar = this.f1143b;
        return pVar != null ? pVar.h() : new int[0];
    }

    @Override // android.widget.TextView
    @SuppressLint({"WrongConstant"})
    public int getAutoSizeTextType() {
        if (u0.f1636b) {
            return super.getAutoSizeTextType() == 1 ? 1 : 0;
        }
        p pVar = this.f1143b;
        if (pVar != null) {
            return pVar.i();
        }
        return 0;
    }

    @Override // android.widget.TextView
    public ActionMode.Callback getCustomSelectionActionModeCallback() {
        return androidx.core.widget.k.s(super.getCustomSelectionActionModeCallback());
    }

    @Override // androidx.core.view.a0
    public ColorStateList getSupportBackgroundTintList() {
        d dVar = this.f1142a;
        if (dVar != null) {
            return dVar.c();
        }
        return null;
    }

    @Override // androidx.core.view.a0
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        d dVar = this.f1142a;
        if (dVar != null) {
            return dVar.d();
        }
        return null;
    }

    public ColorStateList getSupportCompoundDrawablesTintList() {
        return this.f1143b.j();
    }

    public PorterDuff.Mode getSupportCompoundDrawablesTintMode() {
        return this.f1143b.k();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(Button.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(Button.class.getName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        p pVar = this.f1143b;
        if (pVar != null) {
            pVar.o(z4, i8, i9, i10, i11);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView
    public void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
        super.onTextChanged(charSequence, i8, i9, i10);
        p pVar = this.f1143b;
        if ((pVar == null || u0.f1636b || !pVar.l()) ? false : true) {
            this.f1143b.c();
        }
    }

    @Override // android.widget.TextView
    public void setAllCaps(boolean z4) {
        super.setAllCaps(z4);
        getEmojiTextViewHelper().d(z4);
    }

    @Override // android.widget.TextView
    public void setAutoSizeTextTypeUniformWithConfiguration(int i8, int i9, int i10, int i11) {
        if (u0.f1636b) {
            super.setAutoSizeTextTypeUniformWithConfiguration(i8, i9, i10, i11);
            return;
        }
        p pVar = this.f1143b;
        if (pVar != null) {
            pVar.t(i8, i9, i10, i11);
        }
    }

    @Override // android.widget.TextView
    public void setAutoSizeTextTypeUniformWithPresetSizes(int[] iArr, int i8) {
        if (u0.f1636b) {
            super.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i8);
            return;
        }
        p pVar = this.f1143b;
        if (pVar != null) {
            pVar.u(iArr, i8);
        }
    }

    @Override // android.widget.TextView
    public void setAutoSizeTextTypeWithDefaults(int i8) {
        if (u0.f1636b) {
            super.setAutoSizeTextTypeWithDefaults(i8);
            return;
        }
        p pVar = this.f1143b;
        if (pVar != null) {
            pVar.v(i8);
        }
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        d dVar = this.f1142a;
        if (dVar != null) {
            dVar.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i8) {
        super.setBackgroundResource(i8);
        d dVar = this.f1142a;
        if (dVar != null) {
            dVar.g(i8);
        }
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(androidx.core.widget.k.t(this, callback));
    }

    public void setEmojiCompatEnabled(boolean z4) {
        getEmojiTextViewHelper().e(z4);
    }

    @Override // android.widget.TextView
    public void setFilters(InputFilter[] inputFilterArr) {
        super.setFilters(getEmojiTextViewHelper().a(inputFilterArr));
    }

    public void setSupportAllCaps(boolean z4) {
        p pVar = this.f1143b;
        if (pVar != null) {
            pVar.s(z4);
        }
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        d dVar = this.f1142a;
        if (dVar != null) {
            dVar.i(colorStateList);
        }
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        d dVar = this.f1142a;
        if (dVar != null) {
            dVar.j(mode);
        }
    }

    @Override // androidx.core.widget.o
    public void setSupportCompoundDrawablesTintList(ColorStateList colorStateList) {
        this.f1143b.w(colorStateList);
        this.f1143b.b();
    }

    @Override // androidx.core.widget.o
    public void setSupportCompoundDrawablesTintMode(PorterDuff.Mode mode) {
        this.f1143b.x(mode);
        this.f1143b.b();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i8) {
        super.setTextAppearance(context, i8);
        p pVar = this.f1143b;
        if (pVar != null) {
            pVar.q(context, i8);
        }
    }

    @Override // android.widget.TextView
    public void setTextSize(int i8, float f5) {
        if (u0.f1636b) {
            super.setTextSize(i8, f5);
            return;
        }
        p pVar = this.f1143b;
        if (pVar != null) {
            pVar.A(i8, f5);
        }
    }
}
