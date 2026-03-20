package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class bf implements cf {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12104a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12105b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Boolean> f12106c;

    /* renamed from: d  reason: collision with root package name */
    private static final n6<Boolean> f12107d;

    /* renamed from: e  reason: collision with root package name */
    private static final n6<Boolean> f12108e;

    /* renamed from: f  reason: collision with root package name */
    private static final n6<Boolean> f12109f;

    /* renamed from: g  reason: collision with root package name */
    private static final n6<Boolean> f12110g;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12104a = e8.d("measurement.rb.attribution.client2", true);
        f12105b = e8.d("measurement.rb.attribution.dma_fix", true);
        f12106c = e8.d("measurement.rb.attribution.followup1.service", false);
        f12107d = e8.d("measurement.rb.attribution.index_out_of_bounds_fix", false);
        f12108e = e8.d("measurement.rb.attribution.service", true);
        f12109f = e8.d("measurement.rb.attribution.enable_trigger_redaction", true);
        f12110g = e8.d("measurement.rb.attribution.uuid_generation", true);
    }

    @Override // com.google.android.gms.internal.measurement.cf
    public final boolean a() {
        return f12105b.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.cf
    public final boolean b() {
        return f12106c.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.cf
    public final boolean d() {
        return f12107d.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.cf
    public final boolean e() {
        return f12108e.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.cf
    public final boolean f() {
        return f12110g.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.cf
    public final boolean g() {
        return f12109f.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.cf
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.cf
    public final boolean zzb() {
        return f12104a.f().booleanValue();
    }
}
