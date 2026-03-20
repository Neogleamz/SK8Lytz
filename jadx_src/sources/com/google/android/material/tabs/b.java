package com.google.android.material.tabs;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.material.internal.s;
import com.google.android.material.tabs.TabLayout;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static RectF a(TabLayout tabLayout, View view) {
        return view == null ? new RectF() : (tabLayout.y() || !(view instanceof TabLayout.i)) ? new RectF(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()) : b((TabLayout.i) view, 24);
    }

    static RectF b(TabLayout.i iVar, int i8) {
        int contentWidth = iVar.getContentWidth();
        int contentHeight = iVar.getContentHeight();
        int c9 = (int) s.c(iVar.getContext(), i8);
        if (contentWidth < c9) {
            contentWidth = c9;
        }
        int left = (iVar.getLeft() + iVar.getRight()) / 2;
        int top = (iVar.getTop() + iVar.getBottom()) / 2;
        int i9 = contentWidth / 2;
        return new RectF(left - i9, top - (contentHeight / 2), i9 + left, top + (left / 2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(TabLayout tabLayout, View view, View view2, float f5, Drawable drawable) {
        RectF a9 = a(tabLayout, view);
        RectF a10 = a(tabLayout, view2);
        drawable.setBounds(l7.a.c((int) a9.left, (int) a10.left, f5), drawable.getBounds().top, l7.a.c((int) a9.right, (int) a10.right, f5), drawable.getBounds().bottom);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(TabLayout tabLayout, View view, Drawable drawable) {
        RectF a9 = a(tabLayout, view);
        drawable.setBounds((int) a9.left, drawable.getBounds().top, (int) a9.right, drawable.getBounds().bottom);
    }
}
