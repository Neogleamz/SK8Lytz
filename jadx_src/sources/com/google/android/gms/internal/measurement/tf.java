package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class tf implements uf {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12540a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12541b;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12540a = e8.d("measurement.sfmc.client", true);
        f12541b = e8.d("measurement.sfmc.service", true);
    }

    @Override // com.google.android.gms.internal.measurement.uf
    public final boolean a() {
        return f12541b.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.uf
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.uf
    public final boolean zzb() {
        return f12540a.f().booleanValue();
    }
}
