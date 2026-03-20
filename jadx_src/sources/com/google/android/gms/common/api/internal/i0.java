package com.google.android.gms.common.api.internal;

import android.app.Dialog;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class i0 extends l6.p {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ Dialog f11659a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ j0 f11660b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i0(j0 j0Var, Dialog dialog) {
        this.f11660b = j0Var;
        this.f11659a = dialog;
    }

    @Override // l6.p
    public final void a() {
        this.f11660b.f11664b.o();
        if (this.f11659a.isShowing()) {
            this.f11659a.dismiss();
        }
    }
}
