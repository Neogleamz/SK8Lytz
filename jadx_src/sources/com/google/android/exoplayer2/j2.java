package com.google.android.exoplayer2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class j2 {

    /* renamed from: a  reason: collision with root package name */
    private final PowerManager f9822a;

    /* renamed from: b  reason: collision with root package name */
    private PowerManager.WakeLock f9823b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f9824c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f9825d;

    public j2(Context context) {
        this.f9822a = (PowerManager) context.getApplicationContext().getSystemService("power");
    }

    @SuppressLint({"WakelockTimeout"})
    private void c() {
        PowerManager.WakeLock wakeLock = this.f9823b;
        if (wakeLock == null) {
            return;
        }
        if (this.f9824c && this.f9825d) {
            wakeLock.acquire();
        } else {
            wakeLock.release();
        }
    }

    public void a(boolean z4) {
        if (z4 && this.f9823b == null) {
            PowerManager powerManager = this.f9822a;
            if (powerManager == null) {
                b6.p.i("WakeLockManager", "PowerManager is null, therefore not creating the WakeLock.");
                return;
            }
            PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(1, "ExoPlayer:WakeLockManager");
            this.f9823b = newWakeLock;
            newWakeLock.setReferenceCounted(false);
        }
        this.f9824c = z4;
        c();
    }

    public void b(boolean z4) {
        this.f9825d = z4;
        c();
    }
}
