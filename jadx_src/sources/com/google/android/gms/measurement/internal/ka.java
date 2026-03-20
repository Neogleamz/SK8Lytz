package com.google.android.gms.measurement.internal;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import f7.y;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ka<T extends Context & f7.y> {

    /* renamed from: a  reason: collision with root package name */
    private final T f16735a;

    public ka(T t8) {
        n6.j.l(t8);
        this.f16735a = t8;
    }

    private final void f(Runnable runnable) {
        gb j8 = gb.j(this.f16735a);
        j8.l().B(new la(this, j8, runnable));
    }

    private final x4 j() {
        return f6.a(this.f16735a, null, null).i();
    }

    public final int a(final Intent intent, int i8, final int i9) {
        final x4 i10 = f6.a(this.f16735a, null, null).i();
        if (intent == null) {
            i10.J().a("AppMeasurementService started with null intent");
            return 2;
        }
        String action = intent.getAction();
        i10.I().c("Local AppMeasurementService called. startId, action", Integer.valueOf(i9), action);
        if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
            f(new Runnable() { // from class: com.google.android.gms.measurement.internal.ma
                @Override // java.lang.Runnable
                public final void run() {
                    ka.this.d(i9, i10, intent);
                }
            });
        }
        return 2;
    }

    public final IBinder b(Intent intent) {
        if (intent == null) {
            j().E().a("onBind called with null intent");
            return null;
        }
        String action = intent.getAction();
        if ("com.google.android.gms.measurement.START".equals(action)) {
            return new j6(gb.j(this.f16735a));
        }
        j().J().b("onBind received unknown action", action);
        return null;
    }

    public final void c() {
        f6.a(this.f16735a, null, null).i().I().a("Local AppMeasurementService is starting up");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void d(int i8, x4 x4Var, Intent intent) {
        if (this.f16735a.c(i8)) {
            x4Var.I().b("Local AppMeasurementService processed last upload request. StartId", Integer.valueOf(i8));
            j().I().a("Completed wakeful intent.");
            this.f16735a.a(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void e(x4 x4Var, JobParameters jobParameters) {
        x4Var.I().a("AppMeasurementJobService processed last upload request.");
        this.f16735a.b(jobParameters, false);
    }

    @TargetApi(24)
    public final boolean g(final JobParameters jobParameters) {
        final x4 i8 = f6.a(this.f16735a, null, null).i();
        String string = jobParameters.getExtras().getString("action");
        i8.I().b("Local AppMeasurementJobService called. action", string);
        if ("com.google.android.gms.measurement.UPLOAD".equals(string)) {
            f(new Runnable() { // from class: com.google.android.gms.measurement.internal.ja
                @Override // java.lang.Runnable
                public final void run() {
                    ka.this.e(i8, jobParameters);
                }
            });
            return true;
        }
        return true;
    }

    public final void h() {
        f6.a(this.f16735a, null, null).i().I().a("Local AppMeasurementService is shutting down");
    }

    public final void i(Intent intent) {
        if (intent == null) {
            j().E().a("onRebind called with null intent");
            return;
        }
        j().I().b("onRebind called. action", intent.getAction());
    }

    public final boolean k(Intent intent) {
        if (intent == null) {
            j().E().a("onUnbind called with null intent");
            return true;
        }
        j().I().b("onUnbind called for intent. action", intent.getAction());
        return true;
    }
}
