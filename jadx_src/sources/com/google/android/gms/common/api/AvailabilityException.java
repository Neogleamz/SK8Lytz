package com.google.android.gms.common.api;

import android.text.TextUtils;
import com.google.android.gms.common.ConnectionResult;
import java.util.ArrayList;
import n6.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AvailabilityException extends Exception {

    /* renamed from: a  reason: collision with root package name */
    private final k0.a f11542a;

    public AvailabilityException(k0.a aVar) {
        this.f11542a = aVar;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        ArrayList arrayList = new ArrayList();
        boolean z4 = true;
        for (l6.b bVar : this.f11542a.keySet()) {
            ConnectionResult connectionResult = (ConnectionResult) j.l((ConnectionResult) this.f11542a.get(bVar));
            z4 &= !connectionResult.E0();
            arrayList.add(bVar.b() + ": " + String.valueOf(connectionResult));
        }
        StringBuilder sb = new StringBuilder();
        sb.append(z4 ? "None of the queried APIs are available. " : "Some of the queried APIs are unavailable. ");
        sb.append(TextUtils.join("; ", arrayList));
        return sb.toString();
    }
}
