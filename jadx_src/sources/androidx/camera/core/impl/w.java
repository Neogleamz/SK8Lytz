package androidx.camera.core.impl;

import androidx.camera.core.f3;
import androidx.camera.core.impl.Config;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w implements v<f3>, l, b0.i {
    public static final Config.a<Integer> B;
    public static final Config.a<Integer> C;
    public static final Config.a<Integer> D;
    public static final Config.a<Integer> E;
    public static final Config.a<Integer> F;
    public static final Config.a<Integer> G;
    public static final Config.a<Integer> H;
    private final o A;

    static {
        Class cls = Integer.TYPE;
        B = Config.a.a("camerax.core.videoCapture.recordingFrameRate", cls);
        C = Config.a.a("camerax.core.videoCapture.bitRate", cls);
        D = Config.a.a("camerax.core.videoCapture.intraFrameInterval", cls);
        E = Config.a.a("camerax.core.videoCapture.audioBitRate", cls);
        F = Config.a.a("camerax.core.videoCapture.audioSampleRate", cls);
        G = Config.a.a("camerax.core.videoCapture.audioChannelCount", cls);
        H = Config.a.a("camerax.core.videoCapture.audioMinBufferSize", cls);
    }

    public w(o oVar) {
        this.A = oVar;
    }

    public int L() {
        return ((Integer) a(E)).intValue();
    }

    public int M() {
        return ((Integer) a(G)).intValue();
    }

    public int N() {
        return ((Integer) a(H)).intValue();
    }

    public int O() {
        return ((Integer) a(F)).intValue();
    }

    public int P() {
        return ((Integer) a(C)).intValue();
    }

    public int Q() {
        return ((Integer) a(D)).intValue();
    }

    public int R() {
        return ((Integer) a(B)).intValue();
    }

    @Override // androidx.camera.core.impl.q
    public Config l() {
        return this.A;
    }

    @Override // androidx.camera.core.impl.k
    public int m() {
        return 34;
    }
}
