package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    final a f17809a;

    /* renamed from: b  reason: collision with root package name */
    final a f17810b;

    /* renamed from: c  reason: collision with root package name */
    final a f17811c;

    /* renamed from: d  reason: collision with root package name */
    final a f17812d;

    /* renamed from: e  reason: collision with root package name */
    final a f17813e;

    /* renamed from: f  reason: collision with root package name */
    final a f17814f;

    /* renamed from: g  reason: collision with root package name */
    final a f17815g;

    /* renamed from: h  reason: collision with root package name */
    final Paint f17816h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(u7.b.c(context, k7.b.E, f.class.getCanonicalName()), k7.l.Y3);
        this.f17809a = a.a(context, obtainStyledAttributes.getResourceId(k7.l.f21270b4, 0));
        this.f17815g = a.a(context, obtainStyledAttributes.getResourceId(k7.l.Z3, 0));
        this.f17810b = a.a(context, obtainStyledAttributes.getResourceId(k7.l.f21260a4, 0));
        this.f17811c = a.a(context, obtainStyledAttributes.getResourceId(k7.l.f21280c4, 0));
        ColorStateList a9 = u7.c.a(context, obtainStyledAttributes, k7.l.f21290d4);
        this.f17812d = a.a(context, obtainStyledAttributes.getResourceId(k7.l.f21308f4, 0));
        this.f17813e = a.a(context, obtainStyledAttributes.getResourceId(k7.l.f21299e4, 0));
        this.f17814f = a.a(context, obtainStyledAttributes.getResourceId(k7.l.f21316g4, 0));
        Paint paint = new Paint();
        this.f17816h = paint;
        paint.setColor(a9.getDefaultColor());
        obtainStyledAttributes.recycle();
    }
}
