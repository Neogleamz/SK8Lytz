package com.google.android.material.tabs;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends b {
    private static float e(float f5) {
        return (float) (1.0d - Math.cos((f5 * 3.141592653589793d) / 2.0d));
    }

    private static float f(float f5) {
        return (float) Math.sin((f5 * 3.141592653589793d) / 2.0d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.tabs.b
    public void c(TabLayout tabLayout, View view, View view2, float f5, Drawable drawable) {
        float f8;
        float e8;
        RectF a9 = b.a(tabLayout, view);
        RectF a10 = b.a(tabLayout, view2);
        if (a9.left < a10.left) {
            f8 = e(f5);
            e8 = f(f5);
        } else {
            f8 = f(f5);
            e8 = e(f5);
        }
        drawable.setBounds(l7.a.c((int) a9.left, (int) a10.left, f8), drawable.getBounds().top, l7.a.c((int) a9.right, (int) a10.right, e8), drawable.getBounds().bottom);
    }
}
