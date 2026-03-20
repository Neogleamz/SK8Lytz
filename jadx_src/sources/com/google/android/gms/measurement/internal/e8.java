package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import com.daimajia.numberprogressbar.BuildConfig;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ Bundle f16486a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f16487b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e8(h7 h7Var, Bundle bundle) {
        this.f16486a = bundle;
        this.f16487b = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        h7 h7Var = this.f16487b;
        Bundle bundle = this.f16486a;
        h7Var.k();
        h7Var.t();
        n6.j.l(bundle);
        String f5 = n6.j.f(bundle.getString("name"));
        if (!h7Var.f16485a.n()) {
            h7Var.i().I().a("Conditional property not cleared since app measurement is disabled");
            return;
        }
        try {
            h7Var.r().F(new zzac(bundle.getString("app_id"), BuildConfig.FLAVOR, new zzno(f5, 0L, null, BuildConfig.FLAVOR), bundle.getLong("creation_timestamp"), bundle.getBoolean("active"), bundle.getString("trigger_event_name"), null, bundle.getLong("trigger_timeout"), null, bundle.getLong("time_to_live"), h7Var.g().F(bundle.getString("app_id"), bundle.getString("expired_event_name"), bundle.getBundle("expired_event_params"), BuildConfig.FLAVOR, bundle.getLong("creation_timestamp"), true, true)));
        } catch (IllegalArgumentException unused) {
        }
    }
}
