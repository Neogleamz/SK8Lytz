package com.google.android.gms.measurement.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g5 extends BroadcastReceiver {

    /* renamed from: d  reason: collision with root package name */
    private static final String f16543d = g5.class.getName();

    /* renamed from: a  reason: collision with root package name */
    private final gb f16544a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f16545b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f16546c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g5(gb gbVar) {
        n6.j.l(gbVar);
        this.f16544a = gbVar;
    }

    public final void b() {
        this.f16544a.q0();
        this.f16544a.l().k();
        if (this.f16545b) {
            return;
        }
        this.f16544a.zza().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        this.f16546c = this.f16544a.h0().y();
        this.f16544a.i().I().b("Registering connectivity change receiver. Network connected", Boolean.valueOf(this.f16546c));
        this.f16545b = true;
    }

    public final void c() {
        this.f16544a.q0();
        this.f16544a.l().k();
        this.f16544a.l().k();
        if (this.f16545b) {
            this.f16544a.i().I().a("Unregistering connectivity change receiver");
            this.f16545b = false;
            this.f16546c = false;
            try {
                this.f16544a.zza().unregisterReceiver(this);
            } catch (IllegalArgumentException e8) {
                this.f16544a.i().E().b("Failed to unregister the network broadcast receiver", e8);
            }
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        this.f16544a.q0();
        String action = intent.getAction();
        this.f16544a.i().I().b("NetworkBroadcastReceiver received action", action);
        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            this.f16544a.i().J().b("NetworkBroadcastReceiver received unknown action", action);
            return;
        }
        boolean y8 = this.f16544a.h0().y();
        if (this.f16546c != y8) {
            this.f16546c = y8;
            this.f16544a.l().B(new f5(this, y8));
        }
    }
}
