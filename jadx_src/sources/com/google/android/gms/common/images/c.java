package com.google.android.gms.common.images;

import a7.g;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class c {

    /* renamed from: a  reason: collision with root package name */
    protected int f11770a;

    protected abstract void a(Drawable drawable, boolean z4, boolean z8, boolean z9);

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void b(Context context, g gVar, boolean z4) {
        int i8 = this.f11770a;
        a(i8 != 0 ? context.getResources().getDrawable(i8) : null, z4, false, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void c(Context context, Bitmap bitmap, boolean z4) {
        n6.b.c(bitmap);
        a(new BitmapDrawable(context.getResources(), bitmap), false, false, true);
    }
}
