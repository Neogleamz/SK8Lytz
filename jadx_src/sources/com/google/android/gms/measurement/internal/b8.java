package com.google.android.gms.measurement.internal;

import android.os.Bundle;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ Bundle f16341a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f16342b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b8(h7 h7Var, Bundle bundle) {
        this.f16341a = bundle;
        this.f16342b = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        h7 h7Var = this.f16342b;
        Bundle bundle = this.f16341a;
        h7Var.k();
        h7Var.t();
        n6.j.l(bundle);
        String string = bundle.getString("name");
        String string2 = bundle.getString("origin");
        n6.j.f(string);
        n6.j.f(string2);
        n6.j.l(bundle.get("value"));
        if (!h7Var.f16485a.n()) {
            h7Var.i().I().a("Conditional property not set since app measurement is disabled");
            return;
        }
        zzno zznoVar = new zzno(string, bundle.getLong("triggered_timestamp"), bundle.get("value"), string2);
        try {
            zzbf F = h7Var.g().F(bundle.getString("app_id"), bundle.getString("triggered_event_name"), bundle.getBundle("triggered_event_params"), string2, 0L, true, true);
            h7Var.r().F(new zzac(bundle.getString("app_id"), string2, zznoVar, bundle.getLong("creation_timestamp"), false, bundle.getString("trigger_event_name"), h7Var.g().F(bundle.getString("app_id"), bundle.getString("timed_out_event_name"), bundle.getBundle("timed_out_event_params"), string2, 0L, true, true), bundle.getLong("trigger_timeout"), F, bundle.getLong("time_to_live"), h7Var.g().F(bundle.getString("app_id"), bundle.getString("expired_event_name"), bundle.getBundle("expired_event_params"), string2, 0L, true, true)));
        } catch (IllegalArgumentException unused) {
        }
    }
}
