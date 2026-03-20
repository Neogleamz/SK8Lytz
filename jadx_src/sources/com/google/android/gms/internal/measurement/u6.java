package com.google.android.gms.internal.measurement;

import android.util.Log;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u6 extends n6<Boolean> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public u6(v6 v6Var, String str, Boolean bool, boolean z4) {
        super(v6Var, str, bool);
    }

    @Override // com.google.android.gms.internal.measurement.n6
    final /* synthetic */ Boolean h(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (m5.f12340c.matcher(str).matches()) {
                return Boolean.TRUE;
            }
            if (m5.f12341d.matcher(str).matches()) {
                return Boolean.FALSE;
            }
        }
        String k8 = super.k();
        String valueOf = String.valueOf(obj);
        Log.e("PhenotypeFlag", "Invalid boolean value for " + k8 + ": " + valueOf);
        return null;
    }
}
