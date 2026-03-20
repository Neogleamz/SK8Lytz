package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ud implements rd {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12562a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12563b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Boolean> f12564c;

    /* renamed from: d  reason: collision with root package name */
    private static final n6<Boolean> f12565d;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12562a = e8.d("measurement.client.ad_id_consent_fix", true);
        f12563b = e8.d("measurement.service.consent.aiid_reset_fix", true);
        f12564c = e8.d("measurement.service.consent.app_start_fix", true);
        f12565d = e8.d("measurement.service.consent.pfo_on_fx", true);
    }

    @Override // com.google.android.gms.internal.measurement.rd
    public final boolean a() {
        return f12565d.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.rd
    public final boolean zza() {
        return f12563b.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.rd
    public final boolean zzb() {
        return f12564c.f().booleanValue();
    }
}
