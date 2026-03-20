package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class he implements ee {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12231a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12232b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Long> f12233c;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12231a = e8.d("measurement.gbraid_campaign.gbraid.client.dev", false);
        f12232b = e8.d("measurement.gbraid_campaign.gbraid.service", false);
        f12233c = e8.b("measurement.id.gbraid_campaign.service", 0L);
    }

    @Override // com.google.android.gms.internal.measurement.ee
    public final boolean a() {
        return f12232b.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.ee
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.ee
    public final boolean zzb() {
        return f12231a.f().booleanValue();
    }
}
