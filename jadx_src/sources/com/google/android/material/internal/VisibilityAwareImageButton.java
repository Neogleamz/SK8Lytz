package com.google.android.material.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
@SuppressLint({"AppCompatCustomView"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class VisibilityAwareImageButton extends ImageButton {

    /* renamed from: a  reason: collision with root package name */
    private int f18047a;

    public VisibilityAwareImageButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VisibilityAwareImageButton(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f18047a = getVisibility();
    }

    public final void b(int i8, boolean z4) {
        super.setVisibility(i8);
        if (z4) {
            this.f18047a = i8;
        }
    }

    public final int getUserSetVisibility() {
        return this.f18047a;
    }

    @Override // android.widget.ImageView, android.view.View
    public void setVisibility(int i8) {
        b(i8, true);
    }
}
