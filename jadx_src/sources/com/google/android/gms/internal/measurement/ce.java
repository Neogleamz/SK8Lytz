package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ce implements de {

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Boolean> f12120a;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Boolean> f12121b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Boolean> f12122c;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12120a = e8.d("measurement.client.sessions.check_on_reset_and_enable2", true);
        f12121b = e8.d("measurement.client.sessions.check_on_startup", true);
        f12122c = e8.d("measurement.client.sessions.start_session_before_view_screen", true);
    }

    @Override // com.google.android.gms.internal.measurement.de
    public final boolean zza() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.de
    public final boolean zzb() {
        return f12120a.f().booleanValue();
    }
}
