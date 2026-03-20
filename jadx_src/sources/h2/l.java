package h2;

import com.arthenica.ffmpegkit.FFmpegKitConfig;
import com.arthenica.ffmpegkit.g;
import io.flutter.plugin.common.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final g f20251a;

    /* renamed from: b  reason: collision with root package name */
    private final e f20252b;

    /* renamed from: c  reason: collision with root package name */
    private final j.d f20253c;

    public l(g gVar, e eVar, j.d dVar) {
        this.f20251a = gVar;
        this.f20252b = eVar;
        this.f20253c = dVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        FFmpegKitConfig.r(this.f20251a);
        this.f20252b.m(this.f20253c, null);
    }
}
