package com.google.android.gms.common.api.internal;

import android.app.Dialog;
import android.app.PendingIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiActivity;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j0 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final h0 f11663a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ k0 f11664b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j0(k0 k0Var, h0 h0Var) {
        this.f11664b = k0Var;
        this.f11663a = h0Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.f11664b.f11667b) {
            ConnectionResult b9 = this.f11663a.b();
            if (b9.D0()) {
                k0 k0Var = this.f11664b;
                k0Var.f11595a.startActivityForResult(GoogleApiActivity.a(k0Var.b(), (PendingIntent) n6.j.l(b9.Z()), this.f11663a.a(), false), 1);
                return;
            }
            k0 k0Var2 = this.f11664b;
            if (k0Var2.f11670e.b(k0Var2.b(), b9.t(), null) != null) {
                k0 k0Var3 = this.f11664b;
                k0Var3.f11670e.v(k0Var3.b(), this.f11664b.f11595a, b9.t(), 2, this.f11664b);
            } else if (b9.t() != 18) {
                this.f11664b.l(b9, this.f11663a.a());
            } else {
                k0 k0Var4 = this.f11664b;
                Dialog q = k0Var4.f11670e.q(k0Var4.b(), this.f11664b);
                k0 k0Var5 = this.f11664b;
                k0Var5.f11670e.r(k0Var5.b().getApplicationContext(), new i0(this, q));
            }
        }
    }
}
