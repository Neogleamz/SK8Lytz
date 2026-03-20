package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class sf implements pf {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12508a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12509b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Boolean> f12510c;

    /* renamed from: d  reason: collision with root package name */
    private static final n6<Boolean> f12511d;

    /* renamed from: e  reason: collision with root package name */
    private static final n6<Boolean> f12512e;

    /* renamed from: f  reason: collision with root package name */
    private static final n6<Long> f12513f;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12508a = e8.d("measurement.client.sessions.background_sessions_enabled", true);
        f12509b = e8.d("measurement.client.sessions.enable_fix_background_engagement", false);
        f12510c = e8.d("measurement.client.sessions.immediate_start_enabled_foreground", true);
        f12511d = e8.d("measurement.client.sessions.remove_expired_session_properties_enabled", true);
        f12512e = e8.d("measurement.client.sessions.session_id_enabled", true);
        f12513f = e8.b("measurement.id.client.sessions.enable_fix_background_engagement", 0L);
    }

    @Override // com.google.android.gms.internal.measurement.pf
    public final boolean zza() {
        return f12509b.f().booleanValue();
    }
}
