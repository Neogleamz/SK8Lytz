package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class wc implements xc {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12642a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12643b;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12642a = e8.d("measurement.consent_regional_defaults.client", false);
        f12643b = e8.d("measurement.consent_regional_defaults.service", false);
    }

    @Override // com.google.android.gms.internal.measurement.xc
    public final boolean a() {
        return f12643b.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.xc
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.xc
    public final boolean zzb() {
        return f12642a.f().booleanValue();
    }
}
