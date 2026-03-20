package com.google.android.gms.measurement.internal;

import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.internal.measurement.fe;
import com.google.android.gms.internal.measurement.rf;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class q5 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ com.google.android.gms.internal.measurement.z0 f16896a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ ServiceConnection f16897b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ o5 f16898c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q5(o5 o5Var, com.google.android.gms.internal.measurement.z0 z0Var, ServiceConnection serviceConnection) {
        this.f16896a = z0Var;
        this.f16897b = serviceConnection;
        this.f16898c = o5Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String str;
        z4 E;
        String str2;
        o5 o5Var = this.f16898c;
        p5 p5Var = o5Var.f16850b;
        str = o5Var.f16849a;
        com.google.android.gms.internal.measurement.z0 z0Var = this.f16896a;
        ServiceConnection serviceConnection = this.f16897b;
        Bundle a9 = p5Var.a(str, z0Var);
        p5Var.f16870a.l().k();
        p5Var.f16870a.O();
        if (a9 != null) {
            long j8 = a9.getLong("install_begin_timestamp_seconds", 0L) * 1000;
            if (j8 == 0) {
                E = p5Var.f16870a.i().J();
                str2 = "Service response is missing Install Referrer install timestamp";
            } else {
                String string = a9.getString("install_referrer");
                if (string == null || string.isEmpty()) {
                    E = p5Var.f16870a.i().E();
                    str2 = "No referrer defined in Install Referrer response";
                } else {
                    p5Var.f16870a.i().I().b("InstallReferrer API result", string);
                    sb J = p5Var.f16870a.J();
                    Bundle A = J.A(Uri.parse("?" + string), rf.a() && p5Var.f16870a.x().r(c0.D0), fe.a() && p5Var.f16870a.x().r(c0.Z0));
                    if (A == null) {
                        E = p5Var.f16870a.i().E();
                        str2 = "No campaign params defined in Install Referrer result";
                    } else {
                        String string2 = A.getString("medium");
                        if ((string2 == null || "(not set)".equalsIgnoreCase(string2) || "organic".equalsIgnoreCase(string2)) ? false : true) {
                            long j9 = a9.getLong("referrer_click_timestamp_seconds", 0L) * 1000;
                            if (j9 == 0) {
                                E = p5Var.f16870a.i().E();
                                str2 = "Install Referrer is missing click timestamp for ad campaign";
                            } else {
                                A.putLong("click_timestamp", j9);
                            }
                        }
                        if (j8 == p5Var.f16870a.D().f16606h.a()) {
                            p5Var.f16870a.i().I().a("Logging Install Referrer campaign from module while it may have already been logged.");
                        }
                        if (p5Var.f16870a.n()) {
                            p5Var.f16870a.D().f16606h.b(j8);
                            p5Var.f16870a.i().I().b("Logging Install Referrer campaign from gmscore with ", "referrer API v2");
                            A.putString("_cis", "referrer API v2");
                            p5Var.f16870a.F().X("auto", "_cmp", A, str);
                        }
                    }
                }
            }
            E.a(str2);
        }
        if (serviceConnection != null) {
            t6.a.b().c(p5Var.f16870a.zza(), serviceConnection);
        }
    }
}
