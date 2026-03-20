package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.text.TextUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class jb implements rb {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ gb f16715a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public jb(gb gbVar) {
        this.f16715a = gbVar;
    }

    @Override // com.google.android.gms.measurement.internal.rb
    public final void a(String str, String str2, Bundle bundle) {
        f6 f6Var;
        f6 f6Var2;
        if (!TextUtils.isEmpty(str)) {
            this.f16715a.l().B(new mb(this, str, str2, bundle));
            return;
        }
        f6Var = this.f16715a.f16574l;
        if (f6Var != null) {
            f6Var2 = this.f16715a.f16574l;
            f6Var2.i().E().b("AppId not known when logging event", str2);
        }
    }
}
