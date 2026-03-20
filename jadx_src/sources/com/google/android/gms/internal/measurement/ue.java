package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ue implements ve {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12566a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Double> f12567b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Long> f12568c;

    /* renamed from: d  reason: collision with root package name */
    private static final n6<Long> f12569d;

    /* renamed from: e  reason: collision with root package name */
    private static final n6<String> f12570e;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12566a = e8.d("measurement.test.boolean_flag", false);
        f12567b = e8.a("measurement.test.double_flag", -3.0d);
        f12568c = e8.b("measurement.test.int_flag", -2L);
        f12569d = e8.b("measurement.test.long_flag", -1L);
        f12570e = e8.c("measurement.test.string_flag", "---");
    }

    @Override // com.google.android.gms.internal.measurement.ve
    public final long a() {
        return f12569d.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.ve
    public final String b() {
        return f12570e.f();
    }

    @Override // com.google.android.gms.internal.measurement.ve
    public final boolean d() {
        return f12566a.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.ve
    public final double zza() {
        return f12567b.f().doubleValue();
    }

    @Override // com.google.android.gms.internal.measurement.ve
    public final long zzb() {
        return f12568c.f().longValue();
    }
}
