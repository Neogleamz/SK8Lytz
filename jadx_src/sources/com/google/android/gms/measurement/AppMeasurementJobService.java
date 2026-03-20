package com.google.android.gms.measurement;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import com.google.android.gms.measurement.internal.ka;
import f7.y;
@TargetApi(24)
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class AppMeasurementJobService extends JobService implements y {

    /* renamed from: a  reason: collision with root package name */
    private ka<AppMeasurementJobService> f16282a;

    private final ka<AppMeasurementJobService> d() {
        if (this.f16282a == null) {
            this.f16282a = new ka<>(this);
        }
        return this.f16282a;
    }

    @Override // f7.y
    public final void a(Intent intent) {
    }

    @Override // f7.y
    @TargetApi(24)
    public final void b(JobParameters jobParameters, boolean z4) {
        jobFinished(jobParameters, false);
    }

    @Override // f7.y
    public final boolean c(int i8) {
        throw new UnsupportedOperationException();
    }

    @Override // android.app.Service
    public final void onCreate() {
        super.onCreate();
        d().c();
    }

    @Override // android.app.Service
    public final void onDestroy() {
        d().h();
        super.onDestroy();
    }

    @Override // android.app.Service
    public final void onRebind(Intent intent) {
        d().i(intent);
    }

    @Override // android.app.job.JobService
    public final boolean onStartJob(JobParameters jobParameters) {
        return d().g(jobParameters);
    }

    @Override // android.app.job.JobService
    public final boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @Override // android.app.Service
    public final boolean onUnbind(Intent intent) {
        return d().k(intent);
    }
}
