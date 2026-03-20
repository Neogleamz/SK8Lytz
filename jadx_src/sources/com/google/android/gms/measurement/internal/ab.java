package com.google.android.gms.measurement.internal;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ab extends bb {

    /* renamed from: d  reason: collision with root package name */
    private final AlarmManager f16318d;

    /* renamed from: e  reason: collision with root package name */
    private t f16319e;

    /* renamed from: f  reason: collision with root package name */
    private Integer f16320f;

    /* JADX INFO: Access modifiers changed from: protected */
    public ab(gb gbVar) {
        super(gbVar);
        this.f16318d = (AlarmManager) zza().getSystemService("alarm");
    }

    private final t A() {
        if (this.f16319e == null) {
            this.f16319e = new za(this, this.f16446b.j0());
        }
        return this.f16319e;
    }

    @TargetApi(24)
    private final void B() {
        JobScheduler jobScheduler = (JobScheduler) zza().getSystemService("jobscheduler");
        if (jobScheduler != null) {
            jobScheduler.cancel(y());
        }
    }

    private final int y() {
        if (this.f16320f == null) {
            String packageName = zza().getPackageName();
            this.f16320f = Integer.valueOf(("measurement" + packageName).hashCode());
        }
        return this.f16320f.intValue();
    }

    private final PendingIntent z() {
        Context zza = zza();
        return com.google.android.gms.internal.measurement.n1.a(zza, 0, new Intent().setClassName(zza, "com.google.android.gms.measurement.AppMeasurementReceiver").setAction("com.google.android.gms.measurement.UPLOAD"), com.google.android.gms.internal.measurement.n1.f12361b);
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ e a() {
        return super.a();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ d b() {
        return super.b();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ x c() {
        return super.c();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ s4 e() {
        return super.e();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ h5 f() {
        return super.f();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ sb g() {
        return super.g();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void h() {
        super.h();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ x4 i() {
        return super.i();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void j() {
        super.j();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void k() {
        super.k();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ a6 l() {
        return super.l();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ nb m() {
        return super.m();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ wb n() {
        return super.n();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ l o() {
        return super.o();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ r5 p() {
        return super.p();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ ia q() {
        return super.q();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ eb r() {
        return super.r();
    }

    @Override // com.google.android.gms.measurement.internal.bb
    protected final boolean v() {
        AlarmManager alarmManager = this.f16318d;
        if (alarmManager != null) {
            alarmManager.cancel(z());
        }
        if (Build.VERSION.SDK_INT >= 24) {
            B();
            return false;
        }
        return false;
    }

    public final void w(long j8) {
        s();
        Context zza = zza();
        if (!sb.b0(zza)) {
            i().D().a("Receiver not registered/enabled");
        }
        if (!sb.c0(zza, false)) {
            i().D().a("Service not registered/enabled");
        }
        x();
        i().I().b("Scheduling upload, millis", Long.valueOf(j8));
        long b9 = zzb().b() + j8;
        if (j8 < Math.max(0L, c0.f16420z.a(null).longValue()) && !A().e()) {
            A().b(j8);
        }
        if (Build.VERSION.SDK_INT < 24) {
            AlarmManager alarmManager = this.f16318d;
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(2, b9, Math.max(c0.f16410u.a(null).longValue(), j8), z());
                return;
            }
            return;
        }
        Context zza2 = zza();
        ComponentName componentName = new ComponentName(zza2, "com.google.android.gms.measurement.AppMeasurementJobService");
        int y8 = y();
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString("action", "com.google.android.gms.measurement.UPLOAD");
        com.google.android.gms.internal.measurement.m1.c(zza2, new JobInfo.Builder(y8, componentName).setMinimumLatency(j8).setOverrideDeadline(j8 << 1).setExtras(persistableBundle).build(), "com.google.android.gms", "UploadAlarm");
    }

    public final void x() {
        s();
        i().I().a("Unscheduling upload");
        AlarmManager alarmManager = this.f16318d;
        if (alarmManager != null) {
            alarmManager.cancel(z());
        }
        A().a();
        if (Build.VERSION.SDK_INT >= 24) {
            B();
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ Context zza() {
        return super.zza();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ u6.d zzb() {
        return super.zzb();
    }
}
