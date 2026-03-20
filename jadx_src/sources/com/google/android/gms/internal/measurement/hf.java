package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class hf implements Cif {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12234a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Long> f12235b;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12234a = e8.d("measurement.remove_app_background.client", false);
        f12235b = e8.b("measurement.id.remove_app_background.client", 0L);
    }

    @Override // com.google.android.gms.internal.measurement.Cif
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.Cif
    public final boolean zzb() {
        return f12234a.f().booleanValue();
    }
}
