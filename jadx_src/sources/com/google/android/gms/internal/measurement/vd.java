package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class vd implements wd {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12622a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12623b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Boolean> f12624c;

    /* renamed from: d  reason: collision with root package name */
    private static final n6<Boolean> f12625d;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12622a = e8.d("measurement.service.audience.fix_skip_audience_with_failed_filters", true);
        f12623b = e8.d("measurement.audience.refresh_event_count_filters_timestamp", false);
        f12624c = e8.d("measurement.audience.use_bundle_end_timestamp_for_non_sequence_property_filters", false);
        f12625d = e8.d("measurement.audience.use_bundle_timestamp_for_event_count_filters", false);
    }

    @Override // com.google.android.gms.internal.measurement.wd
    public final boolean a() {
        return f12624c.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.wd
    public final boolean b() {
        return f12625d.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.wd
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.wd
    public final boolean zzb() {
        return f12623b.f().booleanValue();
    }
}
