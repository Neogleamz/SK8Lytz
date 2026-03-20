package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class nf implements of {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12388a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12389b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Boolean> f12390c;

    /* renamed from: d  reason: collision with root package name */
    private static final n6<Boolean> f12391d;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12388a = e8.d("measurement.collection.enable_session_stitching_token.client.dev", true);
        f12389b = e8.d("measurement.collection.enable_session_stitching_token.first_open_fix", true);
        f12390c = e8.d("measurement.session_stitching_token_enabled", false);
        f12391d = e8.d("measurement.link_sst_to_sid", true);
    }

    @Override // com.google.android.gms.internal.measurement.of
    public final boolean a() {
        return f12389b.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.of
    public final boolean b() {
        return f12390c.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.of
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.of
    public final boolean zzb() {
        return f12388a.f().booleanValue();
    }
}
