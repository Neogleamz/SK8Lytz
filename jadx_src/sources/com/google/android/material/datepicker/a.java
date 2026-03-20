package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.widget.TextView;
import androidx.core.view.c0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a {

    /* renamed from: a  reason: collision with root package name */
    private final Rect f17803a;

    /* renamed from: b  reason: collision with root package name */
    private final ColorStateList f17804b;

    /* renamed from: c  reason: collision with root package name */
    private final ColorStateList f17805c;

    /* renamed from: d  reason: collision with root package name */
    private final ColorStateList f17806d;

    /* renamed from: e  reason: collision with root package name */
    private final int f17807e;

    /* renamed from: f  reason: collision with root package name */
    private final x7.m f17808f;

    private a(ColorStateList colorStateList, ColorStateList colorStateList2, ColorStateList colorStateList3, int i8, x7.m mVar, Rect rect) {
        androidx.core.util.h.e(rect.left);
        androidx.core.util.h.e(rect.top);
        androidx.core.util.h.e(rect.right);
        androidx.core.util.h.e(rect.bottom);
        this.f17803a = rect;
        this.f17804b = colorStateList2;
        this.f17805c = colorStateList;
        this.f17806d = colorStateList3;
        this.f17807e = i8;
        this.f17808f = mVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static a a(Context context, int i8) {
        androidx.core.util.h.b(i8 != 0, "Cannot create a CalendarItemStyle with a styleResId of 0");
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(i8, k7.l.f21325h4);
        Rect rect = new Rect(obtainStyledAttributes.getDimensionPixelOffset(k7.l.f21334i4, 0), obtainStyledAttributes.getDimensionPixelOffset(k7.l.f21352k4, 0), obtainStyledAttributes.getDimensionPixelOffset(k7.l.f21343j4, 0), obtainStyledAttributes.getDimensionPixelOffset(k7.l.f21361l4, 0));
        ColorStateList a9 = u7.c.a(context, obtainStyledAttributes, k7.l.f21370m4);
        ColorStateList a10 = u7.c.a(context, obtainStyledAttributes, k7.l.r4);
        ColorStateList a11 = u7.c.a(context, obtainStyledAttributes, k7.l.f21396p4);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(k7.l.f21404q4, 0);
        x7.m m8 = x7.m.b(context, obtainStyledAttributes.getResourceId(k7.l.f21379n4, 0), obtainStyledAttributes.getResourceId(k7.l.f21388o4, 0)).m();
        obtainStyledAttributes.recycle();
        return new a(a9, a10, a11, dimensionPixelSize, m8, rect);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        return this.f17803a.bottom;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int c() {
        return this.f17803a.top;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(TextView textView) {
        x7.h hVar = new x7.h();
        x7.h hVar2 = new x7.h();
        hVar.setShapeAppearanceModel(this.f17808f);
        hVar2.setShapeAppearanceModel(this.f17808f);
        hVar.a0(this.f17805c);
        hVar.k0(this.f17807e, this.f17806d);
        textView.setTextColor(this.f17804b);
        Drawable rippleDrawable = Build.VERSION.SDK_INT >= 21 ? new RippleDrawable(this.f17804b.withAlpha(30), hVar, hVar2) : hVar;
        Rect rect = this.f17803a;
        c0.x0(textView, new InsetDrawable(rippleDrawable, rect.left, rect.top, rect.right, rect.bottom));
    }
}
