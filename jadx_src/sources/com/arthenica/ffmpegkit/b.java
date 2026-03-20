package com.arthenica.ffmpegkit;

import android.util.Log;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final g f8800a;

    /* renamed from: b  reason: collision with root package name */
    private final h f8801b;

    public b(g gVar) {
        this.f8800a = gVar;
        this.f8801b = gVar.C();
    }

    @Override // java.lang.Runnable
    public void run() {
        FFmpegKitConfig.r(this.f8800a);
        h hVar = this.f8801b;
        if (hVar != null) {
            try {
                hVar.a(this.f8800a);
            } catch (Exception e8) {
                Log.e("ffmpeg-kit", String.format("Exception thrown inside session complete callback.%s", j2.a.a(e8)));
            }
        }
        h u8 = FFmpegKitConfig.u();
        if (u8 != null) {
            try {
                u8.a(this.f8800a);
            } catch (Exception e9) {
                Log.e("ffmpeg-kit", String.format("Exception thrown inside global complete callback.%s", j2.a.a(e9)));
            }
        }
    }
}
