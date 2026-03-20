package h2;

import android.util.Log;
import io.flutter.plugin.common.j;
import java.io.IOException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final String f20261a;

    /* renamed from: b  reason: collision with root package name */
    private final String f20262b;

    /* renamed from: c  reason: collision with root package name */
    private final e f20263c;

    /* renamed from: d  reason: collision with root package name */
    private final j.d f20264d;

    public o(String str, String str2, e eVar, j.d dVar) {
        this.f20261a = str;
        this.f20262b = str2;
        this.f20263c = eVar;
        this.f20264d = dVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            Log.d("ffmpeg-kit-flutter", String.format("Starting copy %s to pipe %s operation.", this.f20261a, this.f20262b));
            long currentTimeMillis = System.currentTimeMillis();
            int waitFor = Runtime.getRuntime().exec(new String[]{"sh", "-c", "cat " + this.f20261a + " > " + this.f20262b}).waitFor();
            Log.d("ffmpeg-kit-flutter", String.format("Copying %s to pipe %s operation completed with rc %d in %d seconds.", this.f20261a, this.f20262b, Integer.valueOf(waitFor), Long.valueOf((System.currentTimeMillis() - currentTimeMillis) / 1000)));
            this.f20263c.m(this.f20264d, Integer.valueOf(waitFor));
        } catch (IOException | InterruptedException e8) {
            Log.e("ffmpeg-kit-flutter", String.format("Copy %s to pipe %s failed with error.", this.f20261a, this.f20262b), e8);
            this.f20263c.e(this.f20264d, "WRITE_TO_PIPE_FAILED", e8.getMessage());
        }
    }
}
