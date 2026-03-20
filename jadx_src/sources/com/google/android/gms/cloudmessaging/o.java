package com.google.android.gms.cloudmessaging;

import android.os.Bundle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class o extends p {
    /* JADX INFO: Access modifiers changed from: package-private */
    public o(int i8, int i9, Bundle bundle) {
        super(i8, i9, bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.cloudmessaging.p
    public final void a(Bundle bundle) {
        if (bundle.getBoolean("ack", false)) {
            d(null);
        } else {
            c(new zzs(4, "Invalid response to one way request", null));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.cloudmessaging.p
    public final boolean b() {
        return true;
    }
}
