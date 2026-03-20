package com.google.android.exoplayer2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b {

    /* renamed from: a  reason: collision with root package name */
    private final Context f9451a;

    /* renamed from: b  reason: collision with root package name */
    private final a f9452b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f9453c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private final class a extends BroadcastReceiver implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        private final InterfaceC0105b f9454a;

        /* renamed from: b  reason: collision with root package name */
        private final Handler f9455b;

        public a(Handler handler, InterfaceC0105b interfaceC0105b) {
            this.f9455b = handler;
            this.f9454a = interfaceC0105b;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.media.AUDIO_BECOMING_NOISY".equals(intent.getAction())) {
                this.f9455b.post(this);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (b.this.f9453c) {
                this.f9454a.w();
            }
        }
    }

    /* renamed from: com.google.android.exoplayer2.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface InterfaceC0105b {
        void w();
    }

    public b(Context context, Handler handler, InterfaceC0105b interfaceC0105b) {
        this.f9451a = context.getApplicationContext();
        this.f9452b = new a(handler, interfaceC0105b);
    }

    public void b(boolean z4) {
        boolean z8;
        if (z4 && !this.f9453c) {
            this.f9451a.registerReceiver(this.f9452b, new IntentFilter("android.media.AUDIO_BECOMING_NOISY"));
            z8 = true;
        } else if (z4 || !this.f9453c) {
            return;
        } else {
            this.f9451a.unregisterReceiver(this.f9452b);
            z8 = false;
        }
        this.f9453c = z8;
    }
}
