package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class gf implements df {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12207a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12208b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Boolean> f12209c;

    /* renamed from: d  reason: collision with root package name */
    private static final n6<Boolean> f12210d;

    /* renamed from: e  reason: collision with root package name */
    private static final n6<Boolean> f12211e;

    /* renamed from: f  reason: collision with root package name */
    private static final n6<Boolean> f12212f;

    /* renamed from: g  reason: collision with root package name */
    private static final n6<Boolean> f12213g;

    /* renamed from: h  reason: collision with root package name */
    private static final n6<Boolean> f12214h;

    /* renamed from: i  reason: collision with root package name */
    private static final n6<Boolean> f12215i;

    /* renamed from: j  reason: collision with root package name */
    private static final n6<Boolean> f12216j;

    /* renamed from: k  reason: collision with root package name */
    private static final n6<Boolean> f12217k;

    /* renamed from: l  reason: collision with root package name */
    private static final n6<Boolean> f12218l;

    /* renamed from: m  reason: collision with root package name */
    private static final n6<Boolean> f12219m;

    /* renamed from: n  reason: collision with root package name */
    private static final n6<Boolean> f12220n;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12207a = e8.d("measurement.redaction.app_instance_id", true);
        f12208b = e8.d("measurement.redaction.client_ephemeral_aiid_generation", true);
        f12209c = e8.d("measurement.redaction.config_redacted_fields", true);
        f12210d = e8.d("measurement.redaction.device_info", true);
        f12211e = e8.d("measurement.redaction.e_tag", true);
        f12212f = e8.d("measurement.redaction.enhanced_uid", true);
        f12213g = e8.d("measurement.redaction.populate_ephemeral_app_instance_id", true);
        f12214h = e8.d("measurement.redaction.google_signals", true);
        f12215i = e8.d("measurement.redaction.no_aiid_in_config_request", true);
        f12216j = e8.d("measurement.redaction.retain_major_os_version", true);
        f12217k = e8.d("measurement.redaction.scion_payload_generator", true);
        f12218l = e8.d("measurement.redaction.upload_redacted_fields", true);
        f12219m = e8.d("measurement.redaction.upload_subdomain_override", true);
        f12220n = e8.d("measurement.redaction.user_id", true);
    }

    @Override // com.google.android.gms.internal.measurement.df
    public final boolean zza() {
        return f12216j.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.df
    public final boolean zzb() {
        return f12217k.f().booleanValue();
    }
}
