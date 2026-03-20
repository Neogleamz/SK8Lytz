package com.google.android.material.navigation;

import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.view.menu.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b extends g {
    private final Class<?> B;
    private final int C;

    public b(Context context, Class<?> cls, int i8) {
        super(context);
        this.B = cls;
        this.C = i8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.view.menu.g
    public MenuItem a(int i8, int i9, int i10, CharSequence charSequence) {
        if (size() + 1 <= this.C) {
            h0();
            MenuItem a9 = super.a(i8, i9, i10, charSequence);
            if (a9 instanceof i) {
                ((i) a9).t(true);
            }
            g0();
            return a9;
        }
        String simpleName = this.B.getSimpleName();
        throw new IllegalArgumentException("Maximum number of items supported by " + simpleName + " is " + this.C + ". Limit can be checked with " + simpleName + "#getMaxItemCount()");
    }

    @Override // androidx.appcompat.view.menu.g, android.view.Menu
    public SubMenu addSubMenu(int i8, int i9, int i10, CharSequence charSequence) {
        throw new UnsupportedOperationException(this.B.getSimpleName() + " does not support submenus");
    }
}
