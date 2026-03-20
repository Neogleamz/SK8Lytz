package com.arthenica.ffmpegkit;

import android.util.Log;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final p f8804a;

    /* renamed from: b  reason: collision with root package name */
    private final q f8805b;

    /* renamed from: c  reason: collision with root package name */
    private final Integer f8806c;

    public d(p pVar, Integer num) {
        this.f8804a = pVar;
        this.f8805b = pVar.A();
        this.f8806c = num;
    }

    @Override // java.lang.Runnable
    public void run() {
        FFmpegKitConfig.D(this.f8804a, this.f8806c.intValue());
        q qVar = this.f8805b;
        if (qVar != null) {
            try {
                qVar.a(this.f8804a);
            } catch (Exception e8) {
                Log.e("ffmpeg-kit", String.format("Exception thrown inside session complete callback.%s", j2.a.a(e8)));
            }
        }
        q E = FFmpegKitConfig.E();
        if (E != null) {
            try {
                E.a(this.f8804a);
            } catch (Exception e9) {
                Log.e("ffmpeg-kit", String.format("Exception thrown inside global complete callback.%s", j2.a.a(e9)));
            }
        }
    }
}
