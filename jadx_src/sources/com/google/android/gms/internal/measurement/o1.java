package com.google.android.gms.internal.measurement;

import java.net.URL;
import java.net.URLConnection;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class o1 extends p1 {
    private o1() {
    }

    @Override // com.google.android.gms.internal.measurement.p1
    public final URLConnection b(URL url, String str) {
        return url.openConnection();
    }
}
