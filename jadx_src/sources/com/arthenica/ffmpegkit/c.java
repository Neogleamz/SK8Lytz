package com.arthenica.ffmpegkit;

import android.util.Log;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final j f8802a;

    /* renamed from: b  reason: collision with root package name */
    private final k f8803b;

    public c(j jVar) {
        this.f8802a = jVar;
        this.f8803b = jVar.A();
    }

    @Override // java.lang.Runnable
    public void run() {
        FFmpegKitConfig.s(this.f8802a);
        k kVar = this.f8803b;
        if (kVar != null) {
            try {
                kVar.a(this.f8802a);
            } catch (Exception e8) {
                Log.e("ffmpeg-kit", String.format("Exception thrown inside session complete callback.%s", j2.a.a(e8)));
            }
        }
        k x8 = FFmpegKitConfig.x();
        if (x8 != null) {
            try {
                x8.a(this.f8802a);
            } catch (Exception e9) {
                Log.e("ffmpeg-kit", String.format("Exception thrown inside global complete callback.%s", j2.a.a(e9)));
            }
        }
    }
}
