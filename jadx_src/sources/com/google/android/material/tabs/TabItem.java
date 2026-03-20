package com.google.android.material.tabs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.j0;
import k7.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class TabItem extends View {

    /* renamed from: a  reason: collision with root package name */
    public final CharSequence f18477a;

    /* renamed from: b  reason: collision with root package name */
    public final Drawable f18478b;

    /* renamed from: c  reason: collision with root package name */
    public final int f18479c;

    public TabItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        j0 u8 = j0.u(context, attributeSet, l.f21424s7);
        this.f18477a = u8.p(l.f21451v7);
        this.f18478b = u8.g(l.f21433t7);
        this.f18479c = u8.n(l.f21442u7, 0);
        u8.w();
    }
}
