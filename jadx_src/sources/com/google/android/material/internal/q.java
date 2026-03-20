package com.google.android.material.internal;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewOverlay;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class q implements r {

    /* renamed from: a  reason: collision with root package name */
    private final ViewOverlay f18158a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(View view) {
        this.f18158a = view.getOverlay();
    }

    @Override // com.google.android.material.internal.r
    public void a(Drawable drawable) {
        this.f18158a.add(drawable);
    }

    @Override // com.google.android.material.internal.r
    public void b(Drawable drawable) {
        this.f18158a.remove(drawable);
    }
}
