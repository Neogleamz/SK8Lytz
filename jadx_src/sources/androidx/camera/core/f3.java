package androidx.camera.core;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfigFactory;
import androidx.camera.core.impl.v;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f3 extends a3 {
    public static final d K = new d();
    private static final int[] L = {8, 6, 5, 4};
    private volatile AudioRecord A;
    private volatile int B;
    private volatile boolean C;
    private int D;
    private int E;
    private int F;
    private DeferrableSurface G;
    private final AtomicBoolean H;
    private e I;
    private Throwable J;

    /* renamed from: m  reason: collision with root package name */
    private final Object f2363m;

    /* renamed from: n  reason: collision with root package name */
    private final AtomicBoolean f2364n;

    /* renamed from: o  reason: collision with root package name */
    private final AtomicBoolean f2365o;

    /* renamed from: p  reason: collision with root package name */
    private HandlerThread f2366p;
    private Handler q;

    /* renamed from: r  reason: collision with root package name */
    private HandlerThread f2367r;

    /* renamed from: s  reason: collision with root package name */
    private Handler f2368s;

    /* renamed from: t  reason: collision with root package name */
    MediaCodec f2369t;

    /* renamed from: u  reason: collision with root package name */
    private MediaCodec f2370u;

    /* renamed from: v  reason: collision with root package name */
    private com.google.common.util.concurrent.d<Void> f2371v;

    /* renamed from: w  reason: collision with root package name */
    private SessionConfig.b f2372w;

    /* renamed from: x  reason: collision with root package name */
    private int f2373x;

    /* renamed from: y  reason: collision with root package name */
    private int f2374y;

    /* renamed from: z  reason: collision with root package name */
    Surface f2375z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements SessionConfig.c {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ String f2376a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Size f2377b;

        a(String str, Size size) {
            this.f2376a = str;
            this.f2377b = size;
        }

        @Override // androidx.camera.core.impl.SessionConfig.c
        public void a(SessionConfig sessionConfig, SessionConfig.SessionError sessionError) {
            if (f3.this.r(this.f2376a)) {
                f3.this.Z(this.f2376a, this.f2377b);
                f3.this.v();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {
        static int a(MediaCodec.CodecException codecException) {
            return codecException.getErrorCode();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c implements v.a<f3, androidx.camera.core.impl.w, c> {

        /* renamed from: a  reason: collision with root package name */
        private final androidx.camera.core.impl.n f2379a;

        public c() {
            this(androidx.camera.core.impl.n.P());
        }

        private c(androidx.camera.core.impl.n nVar) {
            this.f2379a = nVar;
            Class cls = (Class) nVar.f(b0.h.f7926x, null);
            if (cls == null || cls.equals(f3.class)) {
                n(f3.class);
                return;
            }
            throw new IllegalArgumentException("Invalid target class configuration for " + this + ": " + cls);
        }

        static c c(Config config) {
            return new c(androidx.camera.core.impl.n.Q(config));
        }

        @Override // androidx.camera.core.g0
        public androidx.camera.core.impl.m a() {
            return this.f2379a;
        }

        @Override // androidx.camera.core.impl.v.a
        /* renamed from: d */
        public androidx.camera.core.impl.w b() {
            return new androidx.camera.core.impl.w(androidx.camera.core.impl.o.N(this.f2379a));
        }

        public c e(int i8) {
            a().s(androidx.camera.core.impl.w.E, Integer.valueOf(i8));
            return this;
        }

        public c f(int i8) {
            a().s(androidx.camera.core.impl.w.G, Integer.valueOf(i8));
            return this;
        }

        public c g(int i8) {
            a().s(androidx.camera.core.impl.w.H, Integer.valueOf(i8));
            return this;
        }

        public c h(int i8) {
            a().s(androidx.camera.core.impl.w.F, Integer.valueOf(i8));
            return this;
        }

        public c i(int i8) {
            a().s(androidx.camera.core.impl.w.C, Integer.valueOf(i8));
            return this;
        }

        public c j(int i8) {
            a().s(androidx.camera.core.impl.w.D, Integer.valueOf(i8));
            return this;
        }

        public c k(Size size) {
            a().s(androidx.camera.core.impl.l.f2581l, size);
            return this;
        }

        public c l(int i8) {
            a().s(androidx.camera.core.impl.v.f2662r, Integer.valueOf(i8));
            return this;
        }

        public c m(int i8) {
            a().s(androidx.camera.core.impl.l.f2576g, Integer.valueOf(i8));
            return this;
        }

        public c n(Class<f3> cls) {
            a().s(b0.h.f7926x, cls);
            if (a().f(b0.h.f7925w, null) == null) {
                o(cls.getCanonicalName() + "-" + UUID.randomUUID());
            }
            return this;
        }

        public c o(String str) {
            a().s(b0.h.f7925w, str);
            return this;
        }

        public c p(int i8) {
            a().s(androidx.camera.core.impl.w.B, Integer.valueOf(i8));
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        private static final Size f2380a;

        /* renamed from: b  reason: collision with root package name */
        private static final androidx.camera.core.impl.w f2381b;

        static {
            Size size = new Size(1920, 1080);
            f2380a = size;
            f2381b = new c().p(30).i(8388608).j(1).e(64000).h(8000).f(1).g(RecognitionOptions.UPC_E).k(size).l(3).m(1).b();
        }

        public androidx.camera.core.impl.w a() {
            return f2381b;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum e {
        VIDEO_ENCODER_INIT_STATUS_UNINITIALIZED,
        VIDEO_ENCODER_INIT_STATUS_INITIALIZED_FAILED,
        VIDEO_ENCODER_INIT_STATUS_INSUFFICIENT_RESOURCE,
        VIDEO_ENCODER_INIT_STATUS_RESOURCE_RECLAIMED
    }

    private AudioRecord P(androidx.camera.core.impl.w wVar) {
        int i8 = this.D == 1 ? 16 : 12;
        try {
            int minBufferSize = AudioRecord.getMinBufferSize(this.E, i8, 2);
            if (minBufferSize <= 0) {
                minBufferSize = wVar.N();
            }
            int i9 = minBufferSize;
            AudioRecord audioRecord = new AudioRecord(5, this.E, i8, 2, i9 * 2);
            if (audioRecord.getState() == 1) {
                this.B = i9;
                p1.e("VideoCapture", "source: 5 audioSampleRate: " + this.E + " channelConfig: " + i8 + " audioFormat: 2 bufferSize: " + i9);
                return audioRecord;
            }
            return null;
        } catch (Exception e8) {
            p1.d("VideoCapture", "Exception, keep trying.", e8);
            return null;
        }
    }

    private MediaFormat Q() {
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", this.E, this.D);
        createAudioFormat.setInteger("aac-profile", 2);
        createAudioFormat.setInteger("bitrate", this.F);
        return createAudioFormat;
    }

    private static MediaFormat R(androidx.camera.core.impl.w wVar, Size size) {
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", size.getWidth(), size.getHeight());
        createVideoFormat.setInteger("color-format", 2130708361);
        createVideoFormat.setInteger("bitrate", wVar.P());
        createVideoFormat.setInteger("frame-rate", wVar.R());
        createVideoFormat.setInteger("i-frame-interval", wVar.Q());
        return createVideoFormat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void T(boolean z4, MediaCodec mediaCodec) {
        if (!z4 || mediaCodec == null) {
            return;
        }
        mediaCodec.release();
    }

    private void V() {
        this.f2367r.quitSafely();
        MediaCodec mediaCodec = this.f2370u;
        if (mediaCodec != null) {
            mediaCodec.release();
            this.f2370u = null;
        }
        if (this.A != null) {
            this.A.release();
            this.A = null;
        }
    }

    private void W(final boolean z4) {
        DeferrableSurface deferrableSurface = this.G;
        if (deferrableSurface == null) {
            return;
        }
        final MediaCodec mediaCodec = this.f2369t;
        deferrableSurface.c();
        this.G.i().c(new Runnable() { // from class: androidx.camera.core.e3
            @Override // java.lang.Runnable
            public final void run() {
                f3.T(z4, mediaCodec);
            }
        }, z.a.d());
        if (z4) {
            this.f2369t = null;
        }
        this.f2375z = null;
        this.G = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: X */
    public void S() {
        this.f2366p.quitSafely();
        V();
        if (this.f2375z != null) {
            W(true);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x002b, code lost:
        r7.D = r4.audioChannels;
        r7.E = r4.audioSampleRate;
        r7.F = r4.audioBitRate;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0037, code lost:
        r0 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void Y(android.util.Size r8, java.lang.String r9) {
        /*
            r7 = this;
            r0 = 0
            int[] r1 = androidx.camera.core.f3.L     // Catch: java.lang.NumberFormatException -> L3d
            int r2 = r1.length     // Catch: java.lang.NumberFormatException -> L3d
            r3 = r0
        L5:
            if (r3 >= r2) goto L44
            r4 = r1[r3]     // Catch: java.lang.NumberFormatException -> L3d
            int r5 = java.lang.Integer.parseInt(r9)     // Catch: java.lang.NumberFormatException -> L3d
            boolean r5 = android.media.CamcorderProfile.hasProfile(r5, r4)     // Catch: java.lang.NumberFormatException -> L3d
            if (r5 == 0) goto L3a
            int r5 = java.lang.Integer.parseInt(r9)     // Catch: java.lang.NumberFormatException -> L3d
            android.media.CamcorderProfile r4 = android.media.CamcorderProfile.get(r5, r4)     // Catch: java.lang.NumberFormatException -> L3d
            int r5 = r8.getWidth()     // Catch: java.lang.NumberFormatException -> L3d
            int r6 = r4.videoFrameWidth     // Catch: java.lang.NumberFormatException -> L3d
            if (r5 != r6) goto L3a
            int r5 = r8.getHeight()     // Catch: java.lang.NumberFormatException -> L3d
            int r6 = r4.videoFrameHeight     // Catch: java.lang.NumberFormatException -> L3d
            if (r5 != r6) goto L3a
            int r8 = r4.audioChannels     // Catch: java.lang.NumberFormatException -> L3d
            r7.D = r8     // Catch: java.lang.NumberFormatException -> L3d
            int r8 = r4.audioSampleRate     // Catch: java.lang.NumberFormatException -> L3d
            r7.E = r8     // Catch: java.lang.NumberFormatException -> L3d
            int r8 = r4.audioBitRate     // Catch: java.lang.NumberFormatException -> L3d
            r7.F = r8     // Catch: java.lang.NumberFormatException -> L3d
            r8 = 1
            r0 = r8
            goto L44
        L3a:
            int r3 = r3 + 1
            goto L5
        L3d:
            java.lang.String r8 = "VideoCapture"
            java.lang.String r9 = "The camera Id is not an integer because the camera may be a removable device. Use the default values for the audio related settings."
            androidx.camera.core.p1.e(r8, r9)
        L44:
            if (r0 != 0) goto L5e
            androidx.camera.core.impl.v r8 = r7.g()
            androidx.camera.core.impl.w r8 = (androidx.camera.core.impl.w) r8
            int r9 = r8.M()
            r7.D = r9
            int r9 = r8.O()
            r7.E = r9
            int r8 = r8.L()
            r7.F = r8
        L5e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.core.f3.Y(android.util.Size, java.lang.String):void");
    }

    @Override // androidx.camera.core.a3
    public void C() {
        U();
        com.google.common.util.concurrent.d<Void> dVar = this.f2371v;
        if (dVar != null) {
            dVar.c(new Runnable() { // from class: androidx.camera.core.d3
                @Override // java.lang.Runnable
                public final void run() {
                    f3.this.S();
                }
            }, z.a.d());
        } else {
            S();
        }
    }

    @Override // androidx.camera.core.a3
    public void F() {
        U();
    }

    @Override // androidx.camera.core.a3
    protected Size G(Size size) {
        if (this.f2375z != null) {
            this.f2369t.stop();
            this.f2369t.release();
            this.f2370u.stop();
            this.f2370u.release();
            W(false);
        }
        try {
            this.f2369t = MediaCodec.createEncoderByType("video/avc");
            this.f2370u = MediaCodec.createEncoderByType("audio/mp4a-latm");
            Z(f(), size);
            t();
            return size;
        } catch (IOException e8) {
            throw new IllegalStateException("Unable to create MediaCodec due to: " + e8.getCause());
        }
    }

    void Z(String str, Size size) {
        e eVar;
        androidx.camera.core.impl.w wVar = (androidx.camera.core.impl.w) g();
        this.f2369t.reset();
        this.I = e.VIDEO_ENCODER_INIT_STATUS_UNINITIALIZED;
        try {
            this.f2369t.configure(R(wVar, size), (Surface) null, (MediaCrypto) null, 1);
            if (this.f2375z != null) {
                W(false);
            }
            final Surface createInputSurface = this.f2369t.createInputSurface();
            this.f2375z = createInputSurface;
            this.f2372w = SessionConfig.b.o(wVar);
            DeferrableSurface deferrableSurface = this.G;
            if (deferrableSurface != null) {
                deferrableSurface.c();
            }
            y.h0 h0Var = new y.h0(this.f2375z, size, i());
            this.G = h0Var;
            com.google.common.util.concurrent.d<Void> i8 = h0Var.i();
            Objects.requireNonNull(createInputSurface);
            i8.c(new Runnable() { // from class: androidx.camera.core.b3
                @Override // java.lang.Runnable
                public final void run() {
                    createInputSurface.release();
                }
            }, z.a.d());
            this.f2372w.h(this.G);
            this.f2372w.f(new a(str, size));
            K(this.f2372w.m());
            this.H.set(true);
            Y(size, str);
            this.f2370u.reset();
            this.f2370u.configure(Q(), (Surface) null, (MediaCrypto) null, 1);
            if (this.A != null) {
                this.A.release();
            }
            this.A = P(wVar);
            if (this.A == null) {
                p1.c("VideoCapture", "AudioRecord object cannot initialized correctly!");
                this.H.set(false);
            }
            synchronized (this.f2363m) {
                this.f2373x = -1;
                this.f2374y = -1;
            }
            this.C = false;
        } catch (MediaCodec.CodecException e8) {
            if (Build.VERSION.SDK_INT >= 23) {
                int a9 = b.a(e8);
                String diagnosticInfo = e8.getDiagnosticInfo();
                if (a9 != 1100) {
                    if (a9 == 1101) {
                        p1.e("VideoCapture", "CodecException: code: " + a9 + " diagnostic: " + diagnosticInfo);
                        eVar = e.VIDEO_ENCODER_INIT_STATUS_RESOURCE_RECLAIMED;
                    }
                    this.J = e8;
                }
                p1.e("VideoCapture", "CodecException: code: " + a9 + " diagnostic: " + diagnosticInfo);
                eVar = e.VIDEO_ENCODER_INIT_STATUS_INSUFFICIENT_RESOURCE;
            } else {
                eVar = e.VIDEO_ENCODER_INIT_STATUS_INITIALIZED_FAILED;
            }
            this.I = eVar;
            this.J = e8;
        } catch (IllegalArgumentException e9) {
            e = e9;
            this.I = e.VIDEO_ENCODER_INIT_STATUS_INITIALIZED_FAILED;
            this.J = e;
        } catch (IllegalStateException e10) {
            e = e10;
            this.I = e.VIDEO_ENCODER_INIT_STATUS_INITIALIZED_FAILED;
            this.J = e;
        }
    }

    /* renamed from: a0 */
    public void U() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            z.a.d().execute(new Runnable() { // from class: androidx.camera.core.c3
                @Override // java.lang.Runnable
                public final void run() {
                    f3.this.U();
                }
            });
            return;
        }
        p1.e("VideoCapture", "stopRecording");
        this.f2372w.n();
        this.f2372w.h(this.G);
        K(this.f2372w.m());
        x();
        if (this.C) {
            (this.H.get() ? this.f2365o : this.f2364n).set(true);
        }
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [androidx.camera.core.impl.v<?>, androidx.camera.core.impl.v] */
    @Override // androidx.camera.core.a3
    public androidx.camera.core.impl.v<?> h(boolean z4, UseCaseConfigFactory useCaseConfigFactory) {
        Config a9 = useCaseConfigFactory.a(UseCaseConfigFactory.CaptureType.VIDEO_CAPTURE, 1);
        if (z4) {
            a9 = Config.A(a9, K.a());
        }
        if (a9 == null) {
            return null;
        }
        return p(a9).b();
    }

    @Override // androidx.camera.core.a3
    public v.a<?, ?, ?> p(Config config) {
        return c.c(config);
    }

    @Override // androidx.camera.core.a3
    public void z() {
        this.f2366p = new HandlerThread("CameraX-video encoding thread");
        this.f2367r = new HandlerThread("CameraX-audio encoding thread");
        this.f2366p.start();
        this.q = new Handler(this.f2366p.getLooper());
        this.f2367r.start();
        this.f2368s = new Handler(this.f2367r.getLooper());
    }
}
