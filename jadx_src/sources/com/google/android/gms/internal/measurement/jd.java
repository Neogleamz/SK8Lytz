package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class jd implements kd {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12267a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Long> f12268b;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12267a = e8.d("measurement.disable_npa_for_dasher_and_unicorn", false);
        f12268b = e8.b("measurement.id.disable_npa_for_dasher_and_unicorn.client", 0L);
    }

    @Override // com.google.android.gms.internal.measurement.kd
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.kd
    public final boolean zzb() {
        return f12267a.f().booleanValue();
    }
}
