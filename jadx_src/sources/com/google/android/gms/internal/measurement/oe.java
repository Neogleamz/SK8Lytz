package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class oe implements pe {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12412a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12413b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Long> f12414c;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12412a = e8.d("measurement.item_scoped_custom_parameters.client", true);
        f12413b = e8.d("measurement.item_scoped_custom_parameters.service", false);
        f12414c = e8.b("measurement.id.item_scoped_custom_parameters.service", 0L);
    }

    @Override // com.google.android.gms.internal.measurement.pe
    public final boolean a() {
        return f12413b.f().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.pe
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.pe
    public final boolean zzb() {
        return f12412a.f().booleanValue();
    }
}
