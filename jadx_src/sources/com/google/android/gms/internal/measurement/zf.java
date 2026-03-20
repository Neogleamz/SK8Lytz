package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zf implements ag {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12734a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12735b;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12734a = e8.d("measurement.tcf.client", false);
        f12735b = e8.d("measurement.tcf.service", false);
    }

    @Override // com.google.android.gms.internal.measurement.ag
    public final boolean a() {
        return f12735b.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.ag
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.ag
    public final boolean zzb() {
        return f12734a.f().booleanValue();
    }
}
