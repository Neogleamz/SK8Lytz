package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.text.TextUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j8 implements rb {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ h7 f16707a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j8(h7 h7Var) {
        this.f16707a = h7Var;
    }

    @Override // com.google.android.gms.measurement.internal.rb
    public final void a(String str, String str2, Bundle bundle) {
        if (TextUtils.isEmpty(str)) {
            this.f16707a.z0("auto", str2, bundle);
        } else {
            this.f16707a.X("auto", str2, bundle, str);
        }
    }
}
