package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class yf implements vf {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12721a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12722b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Boolean> f12723c;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12721a = e8.d("measurement.sgtm.client.dev", false);
        f12722b = e8.d("measurement.sgtm.preview_mode_enabled.dev", false);
        f12723c = e8.d("measurement.sgtm.service", false);
    }

    @Override // com.google.android.gms.internal.measurement.vf
    public final boolean a() {
        return f12722b.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.vf
    public final boolean b() {
        return f12723c.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.vf
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.vf
    public final boolean zzb() {
        return f12721a.f().booleanValue();
    }
}
