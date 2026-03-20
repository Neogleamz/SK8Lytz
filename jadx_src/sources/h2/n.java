package h2;

import com.arthenica.ffmpegkit.FFmpegKitConfig;
import com.arthenica.ffmpegkit.p;
import io.flutter.plugin.common.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class n implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final p f20257a;

    /* renamed from: b  reason: collision with root package name */
    private final int f20258b;

    /* renamed from: c  reason: collision with root package name */
    private final e f20259c;

    /* renamed from: d  reason: collision with root package name */
    private final j.d f20260d;

    public n(p pVar, int i8, e eVar, j.d dVar) {
        this.f20257a = pVar;
        this.f20258b = i8;
        this.f20259c = eVar;
        this.f20260d = dVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        FFmpegKitConfig.D(this.f20257a, this.f20258b);
        this.f20259c.m(this.f20260d, null);
    }
}
