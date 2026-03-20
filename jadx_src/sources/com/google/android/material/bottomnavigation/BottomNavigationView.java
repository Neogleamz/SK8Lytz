package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.widget.j0;
import com.google.android.material.internal.m;
import com.google.android.material.navigation.NavigationBarView;
import k7.c;
import k7.d;
import k7.k;
import k7.l;
import x7.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class BottomNavigationView extends NavigationBarView {

    @Deprecated
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a extends NavigationBarView.c {
    }

    @Deprecated
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b extends NavigationBarView.d {
    }

    public BottomNavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.f21053e);
    }

    public BottomNavigationView(Context context, AttributeSet attributeSet, int i8) {
        this(context, attributeSet, i8, k.f21238i);
    }

    public BottomNavigationView(Context context, AttributeSet attributeSet, int i8, int i9) {
        super(context, attributeSet, i8, i9);
        Context context2 = getContext();
        j0 i10 = m.i(context2, attributeSet, l.f21304f0, i8, i9, new int[0]);
        setItemHorizontalTranslationEnabled(i10.a(l.f21312g0, true));
        i10.w();
        if (h()) {
            g(context2);
        }
    }

    private void g(Context context) {
        View view = new View(context);
        view.setBackgroundColor(androidx.core.content.a.d(context, c.f21074a));
        view.setLayoutParams(new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(d.f21104h)));
        addView(view);
    }

    private boolean h() {
        return Build.VERSION.SDK_INT < 21 && !(getBackground() instanceof h);
    }

    @Override // com.google.android.material.navigation.NavigationBarView
    protected com.google.android.material.navigation.c e(Context context) {
        return new com.google.android.material.bottomnavigation.b(context);
    }

    @Override // com.google.android.material.navigation.NavigationBarView
    public int getMaxItemCount() {
        return 5;
    }

    public void setItemHorizontalTranslationEnabled(boolean z4) {
        com.google.android.material.bottomnavigation.b bVar = (com.google.android.material.bottomnavigation.b) getMenuView();
        if (bVar.l() != z4) {
            bVar.setItemHorizontalTranslationEnabled(z4);
            getPresenter().f(false);
        }
    }

    @Deprecated
    public void setOnNavigationItemReselectedListener(a aVar) {
        setOnItemReselectedListener(aVar);
    }

    @Deprecated
    public void setOnNavigationItemSelectedListener(b bVar) {
        setOnItemSelectedListener(bVar);
    }
}
