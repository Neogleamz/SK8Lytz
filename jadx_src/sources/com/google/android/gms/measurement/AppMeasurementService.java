package com.google.android.gms.measurement;

import android.app.Service;
import android.app.job.JobParameters;
import android.content.Intent;
import android.os.IBinder;
import androidx.legacy.content.WakefulBroadcastReceiver;
import com.google.android.gms.measurement.internal.ka;
import f7.y;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class AppMeasurementService extends Service implements y {

    /* renamed from: a  reason: collision with root package name */
    private ka<AppMeasurementService> f16284a;

    private final ka<AppMeasurementService> d() {
        if (this.f16284a == null) {
            this.f16284a = new ka<>(this);
        }
        return this.f16284a;
    }

    @Override // f7.y
    public final void a(Intent intent) {
        WakefulBroadcastReceiver.b(intent);
    }

    @Override // f7.y
    public final void b(JobParameters jobParameters, boolean z4) {
        throw new UnsupportedOperationException();
    }

    @Override // f7.y
    public final boolean c(int i8) {
        return stopSelfResult(i8);
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return d().b(intent);
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

    @Override // android.app.Service
    public final int onStartCommand(Intent intent, int i8, int i9) {
        return d().a(intent, i8, i9);
    }

    @Override // android.app.Service
    public final boolean onUnbind(Intent intent) {
        return d().k(intent);
    }
}
