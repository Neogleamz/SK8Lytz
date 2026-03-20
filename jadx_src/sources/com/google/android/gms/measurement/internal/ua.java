package com.google.android.gms.measurement.internal;

import android.app.ActivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.internal.measurement.lf;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ua {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ na f17033a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ua(na naVar) {
        this.f17033a = naVar;
    }

    private final void c(long j8, boolean z4) {
        this.f17033a.k();
        if (this.f17033a.f16485a.n()) {
            this.f17033a.f().f16615r.b(j8);
            this.f17033a.i().I().b("Session started, time", Long.valueOf(this.f17033a.zzb().b()));
            Long valueOf = Long.valueOf(j8 / 1000);
            this.f17033a.p().Z("auto", "_sid", valueOf, j8);
            this.f17033a.f().f16616s.b(valueOf.longValue());
            this.f17033a.f().f16612n.a(false);
            Bundle bundle = new Bundle();
            bundle.putLong("_sid", valueOf.longValue());
            this.f17033a.p().T("auto", "_s", j8, bundle);
            String a9 = this.f17033a.f().f16621x.a();
            if (TextUtils.isEmpty(a9)) {
                return;
            }
            Bundle bundle2 = new Bundle();
            bundle2.putString("_ffr", a9);
            this.f17033a.p().T("auto", "_ssr", j8, bundle2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a() {
        this.f17033a.k();
        if (this.f17033a.f().w(this.f17033a.zzb().a())) {
            this.f17033a.f().f16612n.a(true);
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(runningAppProcessInfo);
            if (runningAppProcessInfo.importance == 100) {
                this.f17033a.i().I().a("Detected application was in foreground");
                c(this.f17033a.zzb().a(), false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void b(long j8, boolean z4) {
        this.f17033a.k();
        this.f17033a.E();
        if (this.f17033a.f().w(j8)) {
            this.f17033a.f().f16612n.a(true);
            if (lf.a() && this.f17033a.a().r(c0.f16409t0)) {
                this.f17033a.n().G();
            }
        }
        this.f17033a.f().f16615r.b(j8);
        if (this.f17033a.f().f16612n.b()) {
            c(j8, z4);
        }
    }
}
