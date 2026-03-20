package com.google.android.material.internal;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class n {
    public static TextView a(Toolbar toolbar) {
        return b(toolbar, toolbar.getSubtitle());
    }

    private static TextView b(Toolbar toolbar, CharSequence charSequence) {
        for (int i8 = 0; i8 < toolbar.getChildCount(); i8++) {
            View childAt = toolbar.getChildAt(i8);
            if (childAt instanceof TextView) {
                TextView textView = (TextView) childAt;
                if (TextUtils.equals(textView.getText(), charSequence)) {
                    return textView;
                }
            }
        }
        return null;
    }

    public static TextView c(Toolbar toolbar) {
        return b(toolbar, toolbar.getTitle());
    }
}
