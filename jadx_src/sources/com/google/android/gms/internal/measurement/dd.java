package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class dd implements ed {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12146a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12147b;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12146a = e8.d("measurement.consent.stop_reset_on_ads_storage_denied.client.dev", false);
        f12147b = e8.d("measurement.consent.stop_reset_on_ads_storage_denied.service", false);
    }

    @Override // com.google.android.gms.internal.measurement.ed
    public final boolean zza() {
        return f12146a.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.ed
    public final boolean zzb() {
        return f12147b.f().booleanValue();
    }
}
