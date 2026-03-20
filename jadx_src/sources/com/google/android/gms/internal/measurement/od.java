package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class od implements ld {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12405a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12406b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Boolean> f12407c;

    /* renamed from: d  reason: collision with root package name */
    private static final n6<Boolean> f12408d;

    /* renamed from: e  reason: collision with root package name */
    private static final n6<Boolean> f12409e;

    /* renamed from: f  reason: collision with root package name */
    private static final n6<Boolean> f12410f;

    /* renamed from: g  reason: collision with root package name */
    private static final n6<Long> f12411g;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12405a = e8.d("measurement.dma_consent.client", true);
        f12406b = e8.d("measurement.dma_consent.client_bow_check2", true);
        f12407c = e8.d("measurement.dma_consent.service", true);
        f12408d = e8.d("measurement.dma_consent.service_dcu_event", false);
        f12409e = e8.d("measurement.dma_consent.service_npa_remote_default", true);
        f12410f = e8.d("measurement.dma_consent.service_split_batch_on_consent", true);
        f12411g = e8.b("measurement.id.dma_consent.service_dcu_event", 0L);
    }

    @Override // com.google.android.gms.internal.measurement.ld
    public final boolean a() {
        return f12406b.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.ld
    public final boolean b() {
        return f12407c.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.ld
    public final boolean d() {
        return f12408d.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.ld
    public final boolean e() {
        return f12409e.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.ld
    public final boolean g() {
        return f12410f.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.ld
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.ld
    public final boolean zzb() {
        return f12405a.f().booleanValue();
    }
}
