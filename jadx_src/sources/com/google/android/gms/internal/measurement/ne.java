package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ne implements ke {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12386a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Long> f12387b;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12386a = e8.d("measurement.increase_param_lengths", false);
        f12387b = e8.b("measurement.id.increase_param_lengths", 0L);
    }

    @Override // com.google.android.gms.internal.measurement.ke
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.ke
    public final boolean zzb() {
        return f12386a.f().booleanValue();
    }
}
