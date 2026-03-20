package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class pd implements qd {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12444a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12445b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Boolean> f12446c;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12444a = e8.d("measurement.collection.event_safelist", true);
        f12445b = e8.d("measurement.service.store_null_safelist", true);
        f12446c = e8.d("measurement.service.store_safelist", true);
    }

    @Override // com.google.android.gms.internal.measurement.qd
    public final boolean a() {
        return f12446c.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.qd
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.qd
    public final boolean zzb() {
        return f12445b.f().booleanValue();
    }
}
