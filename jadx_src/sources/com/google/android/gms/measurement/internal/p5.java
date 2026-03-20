package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import com.google.android.libraries.barhopper.RecognitionOptions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p5 {

    /* renamed from: a  reason: collision with root package name */
    final f6 f16870a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p5(gb gbVar) {
        this.f16870a = gbVar.j0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Bundle a(String str, com.google.android.gms.internal.measurement.z0 z0Var) {
        this.f16870a.l().k();
        if (z0Var == null) {
            this.f16870a.i().J().a("Attempting to use Install Referrer Service while it is not initialized");
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("package_name", str);
        try {
            Bundle c9 = z0Var.c(bundle);
            if (c9 == null) {
                this.f16870a.i().E().a("Install Referrer Service returned a null response");
                return null;
            }
            return c9;
        } catch (Exception e8) {
            this.f16870a.i().E().b("Exception occurred while retrieving the Install Referrer", e8.getMessage());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean b() {
        try {
            w6.b a9 = w6.c.a(this.f16870a.zza());
            if (a9 != null) {
                return a9.e("com.android.vending", RecognitionOptions.ITF).versionCode >= 80837300;
            }
            this.f16870a.i().I().a("Failed to get PackageManager for Install Referrer Play Store compatibility check");
            return false;
        } catch (Exception e8) {
            this.f16870a.i().I().b("Failed to retrieve Play Store version for Install Referrer", e8);
            return false;
        }
    }
}
