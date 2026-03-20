package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class te implements qe {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12536a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12537b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Boolean> f12538c;

    /* renamed from: d  reason: collision with root package name */
    private static final n6<Long> f12539d;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12536a = e8.d("measurement.sdk.collection.enable_extend_user_property_size", true);
        f12537b = e8.d("measurement.sdk.collection.last_deep_link_referrer2", true);
        f12538c = e8.d("measurement.sdk.collection.last_deep_link_referrer_campaign2", false);
        f12539d = e8.b("measurement.id.sdk.collection.last_deep_link_referrer2", 0L);
    }

    @Override // com.google.android.gms.internal.measurement.qe
    public final boolean zza() {
        return f12538c.f().booleanValue();
    }
}
