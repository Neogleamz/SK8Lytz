package h2;

import com.arthenica.ffmpegkit.FFmpegKitConfig;
import com.arthenica.ffmpegkit.j;
import io.flutter.plugin.common.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final j f20254a;

    /* renamed from: b  reason: collision with root package name */
    private final e f20255b;

    /* renamed from: c  reason: collision with root package name */
    private final j.d f20256c;

    public m(com.arthenica.ffmpegkit.j jVar, e eVar, j.d dVar) {
        this.f20254a = jVar;
        this.f20255b = eVar;
        this.f20256c = dVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        FFmpegKitConfig.s(this.f20254a);
        this.f20255b.m(this.f20256c, null);
    }
}
