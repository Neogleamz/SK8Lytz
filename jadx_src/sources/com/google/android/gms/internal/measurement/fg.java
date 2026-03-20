package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class fg implements cg {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12188a = new v6(o6.a("com.google.android.gms.measurement")).f().e().d("measurement.integration.disable_firebase_instance_id", false);

    @Override // com.google.android.gms.internal.measurement.cg
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.cg
    public final boolean zzb() {
        return f12188a.f().booleanValue();
    }
}
