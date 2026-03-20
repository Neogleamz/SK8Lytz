package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class cd implements zc {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12116a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12117b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Boolean> f12118c;

    /* renamed from: d  reason: collision with root package name */
    private static final n6<Long> f12119d;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12116a = e8.d("measurement.client.consent_state_v1", true);
        f12117b = e8.d("measurement.client.3p_consent_state_v1", true);
        f12118c = e8.d("measurement.service.consent_state_v1_W36", true);
        f12119d = e8.b("measurement.service.storage_consent_support_version", 203600L);
    }

    @Override // com.google.android.gms.internal.measurement.zc
    public final long zza() {
        return f12119d.f().longValue();
    }
}
