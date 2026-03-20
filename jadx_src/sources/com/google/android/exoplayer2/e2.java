package com.google.android.exoplayer2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e2 {

    /* renamed from: a  reason: collision with root package name */
    private final Context f9655a;

    /* renamed from: b  reason: collision with root package name */
    private final Handler f9656b;

    /* renamed from: c  reason: collision with root package name */
    private final b f9657c;

    /* renamed from: d  reason: collision with root package name */
    private final AudioManager f9658d;

    /* renamed from: e  reason: collision with root package name */
    private c f9659e;

    /* renamed from: f  reason: collision with root package name */
    private int f9660f;

    /* renamed from: g  reason: collision with root package name */
    private int f9661g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f9662h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void E(int i8, boolean z4);

        void l(int i8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private final class c extends BroadcastReceiver {
        private c() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Handler handler = e2.this.f9656b;
            final e2 e2Var = e2.this;
            handler.post(new Runnable() { // from class: com.google.android.exoplayer2.f2
                @Override // java.lang.Runnable
                public final void run() {
                    e2.b(e2.this);
                }
            });
        }
    }

    public e2(Context context, Handler handler, b bVar) {
        Context applicationContext = context.getApplicationContext();
        this.f9655a = applicationContext;
        this.f9656b = handler;
        this.f9657c = bVar;
        AudioManager audioManager = (AudioManager) b6.a.h((AudioManager) applicationContext.getSystemService("audio"));
        this.f9658d = audioManager;
        this.f9660f = 3;
        this.f9661g = f(audioManager, 3);
        this.f9662h = e(audioManager, this.f9660f);
        c cVar = new c();
        try {
            applicationContext.registerReceiver(cVar, new IntentFilter("android.media.VOLUME_CHANGED_ACTION"));
            this.f9659e = cVar;
        } catch (RuntimeException e8) {
            b6.p.j("StreamVolumeManager", "Error registering stream volume receiver", e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void b(e2 e2Var) {
        e2Var.i();
    }

    private static boolean e(AudioManager audioManager, int i8) {
        return b6.l0.f8063a >= 23 ? audioManager.isStreamMute(i8) : f(audioManager, i8) == 0;
    }

    private static int f(AudioManager audioManager, int i8) {
        try {
            return audioManager.getStreamVolume(i8);
        } catch (RuntimeException e8) {
            b6.p.j("StreamVolumeManager", "Could not retrieve stream volume for stream type " + i8, e8);
            return audioManager.getStreamMaxVolume(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        int f5 = f(this.f9658d, this.f9660f);
        boolean e8 = e(this.f9658d, this.f9660f);
        if (this.f9661g == f5 && this.f9662h == e8) {
            return;
        }
        this.f9661g = f5;
        this.f9662h = e8;
        this.f9657c.E(f5, e8);
    }

    public int c() {
        return this.f9658d.getStreamMaxVolume(this.f9660f);
    }

    public int d() {
        if (b6.l0.f8063a >= 28) {
            return this.f9658d.getStreamMinVolume(this.f9660f);
        }
        return 0;
    }

    public void g() {
        c cVar = this.f9659e;
        if (cVar != null) {
            try {
                this.f9655a.unregisterReceiver(cVar);
            } catch (RuntimeException e8) {
                b6.p.j("StreamVolumeManager", "Error unregistering stream volume receiver", e8);
            }
            this.f9659e = null;
        }
    }

    public void h(int i8) {
        if (this.f9660f == i8) {
            return;
        }
        this.f9660f = i8;
        i();
        this.f9657c.l(i8);
    }
}
