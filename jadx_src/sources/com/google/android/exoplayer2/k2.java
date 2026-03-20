package com.google.android.exoplayer2;

import android.content.Context;
import android.net.wifi.WifiManager;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class k2 {

    /* renamed from: a  reason: collision with root package name */
    private final WifiManager f9906a;

    /* renamed from: b  reason: collision with root package name */
    private WifiManager.WifiLock f9907b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f9908c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f9909d;

    public k2(Context context) {
        this.f9906a = (WifiManager) context.getApplicationContext().getSystemService("wifi");
    }

    private void c() {
        WifiManager.WifiLock wifiLock = this.f9907b;
        if (wifiLock == null) {
            return;
        }
        if (this.f9908c && this.f9909d) {
            wifiLock.acquire();
        } else {
            wifiLock.release();
        }
    }

    public void a(boolean z4) {
        if (z4 && this.f9907b == null) {
            WifiManager wifiManager = this.f9906a;
            if (wifiManager == null) {
                b6.p.i("WifiLockManager", "WifiManager is null, therefore not creating the WifiLock.");
                return;
            }
            WifiManager.WifiLock createWifiLock = wifiManager.createWifiLock(3, "ExoPlayer:WifiLockManager");
            this.f9907b = createWifiLock;
            createWifiLock.setReferenceCounted(false);
        }
        this.f9908c = z4;
        c();
    }

    public void b(boolean z4) {
        this.f9909d = z4;
        c();
    }
}
