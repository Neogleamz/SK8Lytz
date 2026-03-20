package com.google.android.material.internal;

import android.content.Context;
import android.view.SubMenu;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e extends androidx.appcompat.view.menu.g {
    public e(Context context) {
        super(context);
    }

    @Override // androidx.appcompat.view.menu.g, android.view.Menu
    public SubMenu addSubMenu(int i8, int i9, int i10, CharSequence charSequence) {
        androidx.appcompat.view.menu.i iVar = (androidx.appcompat.view.menu.i) a(i8, i9, i10, charSequence);
        g gVar = new g(w(), this, iVar);
        iVar.x(gVar);
        return gVar;
    }
}
