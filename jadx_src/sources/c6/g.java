package c6;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Pair;
import android.view.Display;
import android.view.Surface;
import b6.i0;
import b6.l0;
import c6.v;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.mediacodec.MediaCodecDecoderException;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.mediacodec.j;
import com.google.android.exoplayer2.video.MediaCodecVideoDecoderException;
import com.google.android.exoplayer2.video.PlaceholderSurface;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.common.collect.ImmutableList;
import i4.f0;
import java.nio.ByteBuffer;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g extends MediaCodecRenderer {
    private static final int[] B1 = {1920, 1600, 1440, 1280, 960, 854, 640, 540, 480};
    private static boolean C1;
    private static boolean D1;
    private i A1;
    private final Context R0;
    private final k S0;
    private final v.a T0;
    private final long U0;
    private final int V0;
    private final boolean W0;
    private b X0;
    private boolean Y0;
    private boolean Z0;

    /* renamed from: a1  reason: collision with root package name */
    private Surface f8369a1;

    /* renamed from: b1  reason: collision with root package name */
    private PlaceholderSurface f8370b1;

    /* renamed from: c1  reason: collision with root package name */
    private boolean f8371c1;

    /* renamed from: d1  reason: collision with root package name */
    private int f8372d1;

    /* renamed from: e1  reason: collision with root package name */
    private boolean f8373e1;

    /* renamed from: f1  reason: collision with root package name */
    private boolean f8374f1;

    /* renamed from: g1  reason: collision with root package name */
    private boolean f8375g1;

    /* renamed from: h1  reason: collision with root package name */
    private long f8376h1;

    /* renamed from: i1  reason: collision with root package name */
    private long f8377i1;

    /* renamed from: j1  reason: collision with root package name */
    private long f8378j1;

    /* renamed from: k1  reason: collision with root package name */
    private int f8379k1;

    /* renamed from: l1  reason: collision with root package name */
    private int f8380l1;

    /* renamed from: m1  reason: collision with root package name */
    private int f8381m1;

    /* renamed from: n1  reason: collision with root package name */
    private long f8382n1;

    /* renamed from: o1  reason: collision with root package name */
    private long f8383o1;

    /* renamed from: p1  reason: collision with root package name */
    private long f8384p1;

    /* renamed from: q1  reason: collision with root package name */
    private int f8385q1;

    /* renamed from: r1  reason: collision with root package name */
    private long f8386r1;

    /* renamed from: s1  reason: collision with root package name */
    private int f8387s1;

    /* renamed from: t1  reason: collision with root package name */
    private int f8388t1;

    /* renamed from: u1  reason: collision with root package name */
    private int f8389u1;

    /* renamed from: v1  reason: collision with root package name */
    private float f8390v1;

    /* renamed from: w1  reason: collision with root package name */
    private x f8391w1;

    /* renamed from: x1  reason: collision with root package name */
    private boolean f8392x1;

    /* renamed from: y1  reason: collision with root package name */
    private int f8393y1;

    /* renamed from: z1  reason: collision with root package name */
    c f8394z1;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        public static boolean a(Context context) {
            DisplayManager displayManager = (DisplayManager) context.getSystemService("display");
            Display display = displayManager != null ? displayManager.getDisplay(0) : null;
            if (display == null || !display.isHdr()) {
                return false;
            }
            for (int i8 : display.getHdrCapabilities().getSupportedHdrTypes()) {
                if (i8 == 1) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final int f8395a;

        /* renamed from: b  reason: collision with root package name */
        public final int f8396b;

        /* renamed from: c  reason: collision with root package name */
        public final int f8397c;

        public b(int i8, int i9, int i10) {
            this.f8395a = i8;
            this.f8396b = i9;
            this.f8397c = i10;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class c implements j.c, Handler.Callback {

        /* renamed from: a  reason: collision with root package name */
        private final Handler f8398a;

        public c(com.google.android.exoplayer2.mediacodec.j jVar) {
            Handler x8 = l0.x(this);
            this.f8398a = x8;
            jVar.g(this, x8);
        }

        private void b(long j8) {
            g gVar = g.this;
            if (this != gVar.f8394z1 || gVar.w0() == null) {
                return;
            }
            if (j8 == Long.MAX_VALUE) {
                g.this.a2();
                return;
            }
            try {
                g.this.Z1(j8);
            } catch (ExoPlaybackException e8) {
                g.this.n1(e8);
            }
        }

        @Override // com.google.android.exoplayer2.mediacodec.j.c
        public void a(com.google.android.exoplayer2.mediacodec.j jVar, long j8, long j9) {
            if (l0.f8063a >= 30) {
                b(j8);
                return;
            }
            this.f8398a.sendMessageAtFrontOfQueue(Message.obtain(this.f8398a, 0, (int) (j8 >> 32), (int) j8));
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message.what != 0) {
                return false;
            }
            b(l0.X0(message.arg1, message.arg2));
            return true;
        }
    }

    public g(Context context, j.b bVar, com.google.android.exoplayer2.mediacodec.l lVar, long j8, boolean z4, Handler handler, v vVar, int i8) {
        this(context, bVar, lVar, j8, z4, handler, vVar, i8, 30.0f);
    }

    public g(Context context, j.b bVar, com.google.android.exoplayer2.mediacodec.l lVar, long j8, boolean z4, Handler handler, v vVar, int i8, float f5) {
        super(2, bVar, lVar, z4, f5);
        this.U0 = j8;
        this.V0 = i8;
        Context applicationContext = context.getApplicationContext();
        this.R0 = applicationContext;
        this.S0 = new k(applicationContext);
        this.T0 = new v.a(handler, vVar);
        this.W0 = F1();
        this.f8377i1 = -9223372036854775807L;
        this.f8387s1 = -1;
        this.f8388t1 = -1;
        this.f8390v1 = -1.0f;
        this.f8372d1 = 1;
        this.f8393y1 = 0;
        C1();
    }

    private void B1() {
        com.google.android.exoplayer2.mediacodec.j w02;
        this.f8373e1 = false;
        if (l0.f8063a < 23 || !this.f8392x1 || (w02 = w0()) == null) {
            return;
        }
        this.f8394z1 = new c(w02);
    }

    private void C1() {
        this.f8391w1 = null;
    }

    private static void E1(MediaFormat mediaFormat, int i8) {
        mediaFormat.setFeatureEnabled("tunneled-playback", true);
        mediaFormat.setInteger("audio-session-id", i8);
    }

    private static boolean F1() {
        return "NVIDIA".equals(l0.f8065c);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0722, code lost:
        if (r0.equals("ELUGA_Ray_X") == false) goto L46;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static boolean H1() {
        /*
            Method dump skipped, instructions count: 3180
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: c6.g.H1():boolean");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x007a, code lost:
        if (r3.equals("video/av01") == false) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int I1(com.google.android.exoplayer2.mediacodec.k r9, com.google.android.exoplayer2.w0 r10) {
        /*
            Method dump skipped, instructions count: 272
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: c6.g.I1(com.google.android.exoplayer2.mediacodec.k, com.google.android.exoplayer2.w0):int");
    }

    private static Point J1(com.google.android.exoplayer2.mediacodec.k kVar, w0 w0Var) {
        int[] iArr;
        int i8 = w0Var.f11212x;
        int i9 = w0Var.f11211w;
        boolean z4 = i8 > i9;
        int i10 = z4 ? i8 : i9;
        if (z4) {
            i8 = i9;
        }
        float f5 = i8 / i10;
        for (int i11 : B1) {
            int i12 = (int) (i11 * f5);
            if (i11 <= i10 || i12 <= i8) {
                break;
            }
            if (l0.f8063a >= 21) {
                int i13 = z4 ? i12 : i11;
                if (!z4) {
                    i11 = i12;
                }
                Point c9 = kVar.c(i13, i11);
                if (kVar.w(c9.x, c9.y, w0Var.f11213y)) {
                    return c9;
                }
            } else {
                try {
                    int l8 = l0.l(i11, 16) * 16;
                    int l9 = l0.l(i12, 16) * 16;
                    if (l8 * l9 <= MediaCodecUtil.N()) {
                        int i14 = z4 ? l9 : l8;
                        if (!z4) {
                            l8 = l9;
                        }
                        return new Point(i14, l8);
                    }
                } catch (MediaCodecUtil.DecoderQueryException unused) {
                }
            }
        }
        return null;
    }

    private static List<com.google.android.exoplayer2.mediacodec.k> L1(Context context, com.google.android.exoplayer2.mediacodec.l lVar, w0 w0Var, boolean z4, boolean z8) {
        String str = w0Var.f11207m;
        if (str == null) {
            return ImmutableList.E();
        }
        List<com.google.android.exoplayer2.mediacodec.k> a9 = lVar.a(str, z4, z8);
        String m8 = MediaCodecUtil.m(w0Var);
        if (m8 == null) {
            return ImmutableList.x(a9);
        }
        List<com.google.android.exoplayer2.mediacodec.k> a10 = lVar.a(m8, z4, z8);
        return (l0.f8063a < 26 || !"video/dolby-vision".equals(w0Var.f11207m) || a10.isEmpty() || a.a(context)) ? ImmutableList.u().j(a9).j(a10).k() : ImmutableList.x(a10);
    }

    protected static int M1(com.google.android.exoplayer2.mediacodec.k kVar, w0 w0Var) {
        if (w0Var.f11208n != -1) {
            int size = w0Var.f11209p.size();
            int i8 = 0;
            for (int i9 = 0; i9 < size; i9++) {
                i8 += w0Var.f11209p.get(i9).length;
            }
            return w0Var.f11208n + i8;
        }
        return I1(kVar, w0Var);
    }

    private static int N1(int i8, int i9) {
        return (i8 * 3) / (i9 * 2);
    }

    private static boolean P1(long j8) {
        return j8 < -30000;
    }

    private static boolean Q1(long j8) {
        return j8 < -500000;
    }

    private void S1() {
        if (this.f8379k1 > 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            this.T0.n(this.f8379k1, elapsedRealtime - this.f8378j1);
            this.f8379k1 = 0;
            this.f8378j1 = elapsedRealtime;
        }
    }

    private void U1() {
        int i8 = this.f8385q1;
        if (i8 != 0) {
            this.T0.B(this.f8384p1, i8);
            this.f8384p1 = 0L;
            this.f8385q1 = 0;
        }
    }

    private void V1() {
        int i8 = this.f8387s1;
        if (i8 == -1 && this.f8388t1 == -1) {
            return;
        }
        x xVar = this.f8391w1;
        if (xVar != null && xVar.f8461a == i8 && xVar.f8462b == this.f8388t1 && xVar.f8463c == this.f8389u1 && xVar.f8464d == this.f8390v1) {
            return;
        }
        x xVar2 = new x(this.f8387s1, this.f8388t1, this.f8389u1, this.f8390v1);
        this.f8391w1 = xVar2;
        this.T0.D(xVar2);
    }

    private void W1() {
        if (this.f8371c1) {
            this.T0.A(this.f8369a1);
        }
    }

    private void X1() {
        x xVar = this.f8391w1;
        if (xVar != null) {
            this.T0.D(xVar);
        }
    }

    private void Y1(long j8, long j9, w0 w0Var) {
        i iVar = this.A1;
        if (iVar != null) {
            iVar.d(j8, j9, w0Var, A0());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a2() {
        m1();
    }

    private void b2() {
        Surface surface = this.f8369a1;
        PlaceholderSurface placeholderSurface = this.f8370b1;
        if (surface == placeholderSurface) {
            this.f8369a1 = null;
        }
        placeholderSurface.release();
        this.f8370b1 = null;
    }

    private static void e2(com.google.android.exoplayer2.mediacodec.j jVar, byte[] bArr) {
        Bundle bundle = new Bundle();
        bundle.putByteArray("hdr10-plus-info", bArr);
        jVar.c(bundle);
    }

    private void f2() {
        this.f8377i1 = this.U0 > 0 ? SystemClock.elapsedRealtime() + this.U0 : -9223372036854775807L;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0, types: [c6.g, com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.f] */
    /* JADX WARN: Type inference failed for: r5v8, types: [android.view.Surface] */
    private void g2(Object obj) {
        PlaceholderSurface placeholderSurface = obj instanceof Surface ? (Surface) obj : null;
        if (placeholderSurface == null) {
            PlaceholderSurface placeholderSurface2 = this.f8370b1;
            if (placeholderSurface2 != null) {
                placeholderSurface = placeholderSurface2;
            } else {
                com.google.android.exoplayer2.mediacodec.k x02 = x0();
                if (x02 != null && l2(x02)) {
                    placeholderSurface = PlaceholderSurface.c(this.R0, x02.f10036g);
                    this.f8370b1 = placeholderSurface;
                }
            }
        }
        if (this.f8369a1 == placeholderSurface) {
            if (placeholderSurface == null || placeholderSurface == this.f8370b1) {
                return;
            }
            X1();
            W1();
            return;
        }
        this.f8369a1 = placeholderSurface;
        this.S0.m(placeholderSurface);
        this.f8371c1 = false;
        int state = getState();
        com.google.android.exoplayer2.mediacodec.j w02 = w0();
        if (w02 != null) {
            if (l0.f8063a < 23 || placeholderSurface == null || this.Y0) {
                e1();
                O0();
            } else {
                h2(w02, placeholderSurface);
            }
        }
        if (placeholderSurface == null || placeholderSurface == this.f8370b1) {
            C1();
            B1();
            return;
        }
        X1();
        B1();
        if (state == 2) {
            f2();
        }
    }

    private boolean l2(com.google.android.exoplayer2.mediacodec.k kVar) {
        return l0.f8063a >= 23 && !this.f8392x1 && !D1(kVar.f10030a) && (!kVar.f10036g || PlaceholderSurface.b(this.R0));
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected List<com.google.android.exoplayer2.mediacodec.k> B0(com.google.android.exoplayer2.mediacodec.l lVar, w0 w0Var, boolean z4) {
        return MediaCodecUtil.u(L1(this.R0, lVar, w0Var, z4, this.f8392x1), w0Var);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    @TargetApi(17)
    protected j.a D0(com.google.android.exoplayer2.mediacodec.k kVar, w0 w0Var, MediaCrypto mediaCrypto, float f5) {
        PlaceholderSurface placeholderSurface = this.f8370b1;
        if (placeholderSurface != null && placeholderSurface.f11065a != kVar.f10036g) {
            b2();
        }
        String str = kVar.f10032c;
        b K1 = K1(kVar, w0Var, M());
        this.X0 = K1;
        MediaFormat O1 = O1(w0Var, str, K1, f5, this.W0, this.f8392x1 ? this.f8393y1 : 0);
        if (this.f8369a1 == null) {
            if (!l2(kVar)) {
                throw new IllegalStateException();
            }
            if (this.f8370b1 == null) {
                this.f8370b1 = PlaceholderSurface.c(this.R0, kVar.f10036g);
            }
            this.f8369a1 = this.f8370b1;
        }
        return j.a.b(kVar, O1, w0Var, this.f8369a1, mediaCrypto);
    }

    protected boolean D1(String str) {
        if (str.startsWith("OMX.google")) {
            return false;
        }
        synchronized (g.class) {
            if (!C1) {
                D1 = H1();
                C1 = true;
            }
        }
        return D1;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    @TargetApi(29)
    protected void G0(DecoderInputBuffer decoderInputBuffer) {
        if (this.Z0) {
            ByteBuffer byteBuffer = (ByteBuffer) b6.a.e(decoderInputBuffer.f9515f);
            if (byteBuffer.remaining() >= 7) {
                byte b9 = byteBuffer.get();
                short s8 = byteBuffer.getShort();
                short s9 = byteBuffer.getShort();
                byte b10 = byteBuffer.get();
                byte b11 = byteBuffer.get();
                byteBuffer.position(0);
                if (b9 == -75 && s8 == 60 && s9 == 1 && b10 == 4) {
                    if (b11 == 0 || b11 == 1) {
                        byte[] bArr = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bArr);
                        byteBuffer.position(0);
                        e2(w0(), bArr);
                    }
                }
            }
        }
    }

    protected void G1(com.google.android.exoplayer2.mediacodec.j jVar, int i8, long j8) {
        i0.a("dropVideoBuffer");
        jVar.h(i8, false);
        i0.c();
        n2(0, 1);
    }

    protected b K1(com.google.android.exoplayer2.mediacodec.k kVar, w0 w0Var, w0[] w0VarArr) {
        int I1;
        int i8 = w0Var.f11211w;
        int i9 = w0Var.f11212x;
        int M1 = M1(kVar, w0Var);
        if (w0VarArr.length == 1) {
            if (M1 != -1 && (I1 = I1(kVar, w0Var)) != -1) {
                M1 = Math.min((int) (M1 * 1.5f), I1);
            }
            return new b(i8, i9, M1);
        }
        int length = w0VarArr.length;
        boolean z4 = false;
        for (int i10 = 0; i10 < length; i10++) {
            w0 w0Var2 = w0VarArr[i10];
            if (w0Var.E != null && w0Var2.E == null) {
                w0Var2 = w0Var2.b().L(w0Var.E).G();
            }
            if (kVar.f(w0Var, w0Var2).f21602d != 0) {
                int i11 = w0Var2.f11211w;
                z4 |= i11 == -1 || w0Var2.f11212x == -1;
                i8 = Math.max(i8, i11);
                i9 = Math.max(i9, w0Var2.f11212x);
                M1 = Math.max(M1, M1(kVar, w0Var2));
            }
        }
        if (z4) {
            b6.p.i("MediaCodecVideoRenderer", "Resolutions unknown. Codec max resolution: " + i8 + "x" + i9);
            Point J1 = J1(kVar, w0Var);
            if (J1 != null) {
                i8 = Math.max(i8, J1.x);
                i9 = Math.max(i9, J1.y);
                M1 = Math.max(M1, I1(kVar, w0Var.b().n0(i8).S(i9).G()));
                b6.p.i("MediaCodecVideoRenderer", "Codec max resolution adjusted to: " + i8 + "x" + i9);
            }
        }
        return new b(i8, i9, M1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.f
    public void O() {
        C1();
        B1();
        this.f8371c1 = false;
        this.f8394z1 = null;
        try {
            super.O();
        } finally {
            this.T0.m(this.M0);
        }
    }

    @SuppressLint({"InlinedApi"})
    @TargetApi(21)
    protected MediaFormat O1(w0 w0Var, String str, b bVar, float f5, boolean z4, int i8) {
        Pair<Integer, Integer> q;
        MediaFormat mediaFormat = new MediaFormat();
        mediaFormat.setString("mime", str);
        mediaFormat.setInteger("width", w0Var.f11211w);
        mediaFormat.setInteger("height", w0Var.f11212x);
        b6.s.e(mediaFormat, w0Var.f11209p);
        b6.s.c(mediaFormat, "frame-rate", w0Var.f11213y);
        b6.s.d(mediaFormat, "rotation-degrees", w0Var.f11214z);
        b6.s.b(mediaFormat, w0Var.E);
        if ("video/dolby-vision".equals(w0Var.f11207m) && (q = MediaCodecUtil.q(w0Var)) != null) {
            b6.s.d(mediaFormat, "profile", ((Integer) q.first).intValue());
        }
        mediaFormat.setInteger("max-width", bVar.f8395a);
        mediaFormat.setInteger("max-height", bVar.f8396b);
        b6.s.d(mediaFormat, "max-input-size", bVar.f8397c);
        if (l0.f8063a >= 23) {
            mediaFormat.setInteger("priority", 0);
            if (f5 != -1.0f) {
                mediaFormat.setFloat("operating-rate", f5);
            }
        }
        if (z4) {
            mediaFormat.setInteger("no-post-process", 1);
            mediaFormat.setInteger("auto-frc", 0);
        }
        if (i8 != 0) {
            E1(mediaFormat, i8);
        }
        return mediaFormat;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.f
    public void P(boolean z4, boolean z8) {
        super.P(z4, z8);
        boolean z9 = I().f20501a;
        b6.a.f((z9 && this.f8393y1 == 0) ? false : true);
        if (this.f8392x1 != z9) {
            this.f8392x1 = z9;
            e1();
        }
        this.T0.o(this.M0);
        this.f8374f1 = z8;
        this.f8375g1 = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.f
    public void Q(long j8, boolean z4) {
        super.Q(j8, z4);
        B1();
        this.S0.j();
        this.f8382n1 = -9223372036854775807L;
        this.f8376h1 = -9223372036854775807L;
        this.f8380l1 = 0;
        if (z4) {
            f2();
        } else {
            this.f8377i1 = -9223372036854775807L;
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void Q0(Exception exc) {
        b6.p.d("MediaCodecVideoRenderer", "Video codec error", exc);
        this.T0.C(exc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.f
    @TargetApi(17)
    public void R() {
        try {
            super.R();
        } finally {
            if (this.f8370b1 != null) {
                b2();
            }
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void R0(String str, j.a aVar, long j8, long j9) {
        this.T0.k(str, j8, j9);
        this.Y0 = D1(str);
        this.Z0 = ((com.google.android.exoplayer2.mediacodec.k) b6.a.e(x0())).p();
        if (l0.f8063a < 23 || !this.f8392x1) {
            return;
        }
        this.f8394z1 = new c((com.google.android.exoplayer2.mediacodec.j) b6.a.e(w0()));
    }

    protected boolean R1(long j8, boolean z4) {
        int X = X(j8);
        if (X == 0) {
            return false;
        }
        if (z4) {
            l4.e eVar = this.M0;
            eVar.f21588d += X;
            eVar.f21590f += this.f8381m1;
        } else {
            this.M0.f21594j++;
            n2(X, this.f8381m1);
        }
        t0();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.f
    public void S() {
        super.S();
        this.f8379k1 = 0;
        this.f8378j1 = SystemClock.elapsedRealtime();
        this.f8383o1 = SystemClock.elapsedRealtime() * 1000;
        this.f8384p1 = 0L;
        this.f8385q1 = 0;
        this.S0.k();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void S0(String str) {
        this.T0.l(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.f
    public void T() {
        this.f8377i1 = -9223372036854775807L;
        S1();
        U1();
        this.S0.l();
        super.T();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    public l4.g T0(i4.s sVar) {
        l4.g T0 = super.T0(sVar);
        this.T0.p(sVar.f20512b, T0);
        return T0;
    }

    void T1() {
        this.f8375g1 = true;
        if (this.f8373e1) {
            return;
        }
        this.f8373e1 = true;
        this.T0.A(this.f8369a1);
        this.f8371c1 = true;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void U0(w0 w0Var, MediaFormat mediaFormat) {
        com.google.android.exoplayer2.mediacodec.j w02 = w0();
        if (w02 != null) {
            w02.i(this.f8372d1);
        }
        if (this.f8392x1) {
            this.f8387s1 = w0Var.f11211w;
            this.f8388t1 = w0Var.f11212x;
        } else {
            b6.a.e(mediaFormat);
            boolean z4 = mediaFormat.containsKey("crop-right") && mediaFormat.containsKey("crop-left") && mediaFormat.containsKey("crop-bottom") && mediaFormat.containsKey("crop-top");
            this.f8387s1 = z4 ? (mediaFormat.getInteger("crop-right") - mediaFormat.getInteger("crop-left")) + 1 : mediaFormat.getInteger("width");
            this.f8388t1 = z4 ? (mediaFormat.getInteger("crop-bottom") - mediaFormat.getInteger("crop-top")) + 1 : mediaFormat.getInteger("height");
        }
        float f5 = w0Var.A;
        this.f8390v1 = f5;
        if (l0.f8063a >= 21) {
            int i8 = w0Var.f11214z;
            if (i8 == 90 || i8 == 270) {
                int i9 = this.f8387s1;
                this.f8387s1 = this.f8388t1;
                this.f8388t1 = i9;
                this.f8390v1 = 1.0f / f5;
            }
        } else {
            this.f8389u1 = w0Var.f11214z;
        }
        this.S0.g(w0Var.f11213y);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    public void W0(long j8) {
        super.W0(j8);
        if (this.f8392x1) {
            return;
        }
        this.f8381m1--;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    public void X0() {
        super.X0();
        B1();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void Y0(DecoderInputBuffer decoderInputBuffer) {
        boolean z4 = this.f8392x1;
        if (!z4) {
            this.f8381m1++;
        }
        if (l0.f8063a >= 23 || !z4) {
            return;
        }
        Z1(decoderInputBuffer.f9514e);
    }

    protected void Z1(long j8) {
        x1(j8);
        V1();
        this.M0.f21589e++;
        T1();
        W0(j8);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected l4.g a0(com.google.android.exoplayer2.mediacodec.k kVar, w0 w0Var, w0 w0Var2) {
        l4.g f5 = kVar.f(w0Var, w0Var2);
        int i8 = f5.f21603e;
        int i9 = w0Var2.f11211w;
        b bVar = this.X0;
        if (i9 > bVar.f8395a || w0Var2.f11212x > bVar.f8396b) {
            i8 |= RecognitionOptions.QR_CODE;
        }
        if (M1(kVar, w0Var2) > this.X0.f8397c) {
            i8 |= 64;
        }
        int i10 = i8;
        return new l4.g(kVar.f10030a, w0Var, w0Var2, i10 != 0 ? 0 : f5.f21602d, i10);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected boolean a1(long j8, long j9, com.google.android.exoplayer2.mediacodec.j jVar, ByteBuffer byteBuffer, int i8, int i9, int i10, long j10, boolean z4, boolean z8, w0 w0Var) {
        boolean z9;
        long j11;
        b6.a.e(jVar);
        if (this.f8376h1 == -9223372036854775807L) {
            this.f8376h1 = j8;
        }
        if (j10 != this.f8382n1) {
            this.S0.h(j10);
            this.f8382n1 = j10;
        }
        long E0 = E0();
        long j12 = j10 - E0;
        if (z4 && !z8) {
            m2(jVar, i8, j12);
            return true;
        }
        double F0 = F0();
        boolean z10 = getState() == 2;
        long elapsedRealtime = SystemClock.elapsedRealtime() * 1000;
        long j13 = (long) ((j10 - j8) / F0);
        if (z10) {
            j13 -= elapsedRealtime - j9;
        }
        if (this.f8369a1 == this.f8370b1) {
            if (P1(j13)) {
                m2(jVar, i8, j12);
                o2(j13);
                return true;
            }
            return false;
        }
        long j14 = elapsedRealtime - this.f8383o1;
        if (this.f8375g1 ? this.f8373e1 : !(z10 || this.f8374f1)) {
            j11 = j14;
            z9 = false;
        } else {
            z9 = true;
            j11 = j14;
        }
        if (!(this.f8377i1 == -9223372036854775807L && j8 >= E0 && (z9 || (z10 && k2(j13, j11))))) {
            if (z10 && j8 != this.f8376h1) {
                long nanoTime = System.nanoTime();
                long b9 = this.S0.b((j13 * 1000) + nanoTime);
                long j15 = (b9 - nanoTime) / 1000;
                boolean z11 = this.f8377i1 != -9223372036854775807L;
                if (i2(j15, j9, z8) && R1(j8, z11)) {
                    return false;
                }
                if (j2(j15, j9, z8)) {
                    if (z11) {
                        m2(jVar, i8, j12);
                    } else {
                        G1(jVar, i8, j12);
                    }
                    j13 = j15;
                } else {
                    j13 = j15;
                    if (l0.f8063a >= 21) {
                        if (j13 < 50000) {
                            if (b9 == this.f8386r1) {
                                m2(jVar, i8, j12);
                            } else {
                                Y1(j12, b9, w0Var);
                                d2(jVar, i8, j12, b9);
                            }
                            o2(j13);
                            this.f8386r1 = b9;
                            return true;
                        }
                    } else if (j13 < 30000) {
                        if (j13 > 11000) {
                            try {
                                Thread.sleep((j13 - 10000) / 1000);
                            } catch (InterruptedException unused) {
                                Thread.currentThread().interrupt();
                                return false;
                            }
                        }
                        Y1(j12, b9, w0Var);
                        c2(jVar, i8, j12);
                    }
                }
            }
            return false;
        }
        long nanoTime2 = System.nanoTime();
        Y1(j12, nanoTime2, w0Var);
        if (l0.f8063a >= 21) {
            d2(jVar, i8, j12, nanoTime2);
        }
        c2(jVar, i8, j12);
        o2(j13);
        return true;
    }

    protected void c2(com.google.android.exoplayer2.mediacodec.j jVar, int i8, long j8) {
        V1();
        i0.a("releaseOutputBuffer");
        jVar.h(i8, true);
        i0.c();
        this.f8383o1 = SystemClock.elapsedRealtime() * 1000;
        this.M0.f21589e++;
        this.f8380l1 = 0;
        T1();
    }

    protected void d2(com.google.android.exoplayer2.mediacodec.j jVar, int i8, long j8, long j9) {
        V1();
        i0.a("releaseOutputBuffer");
        jVar.d(i8, j9);
        i0.c();
        this.f8383o1 = SystemClock.elapsedRealtime() * 1000;
        this.M0.f21589e++;
        this.f8380l1 = 0;
        T1();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.c2
    public boolean e() {
        PlaceholderSurface placeholderSurface;
        if (super.e() && (this.f8373e1 || (((placeholderSurface = this.f8370b1) != null && this.f8369a1 == placeholderSurface) || w0() == null || this.f8392x1))) {
            this.f8377i1 = -9223372036854775807L;
            return true;
        } else if (this.f8377i1 == -9223372036854775807L) {
            return false;
        } else {
            if (SystemClock.elapsedRealtime() < this.f8377i1) {
                return true;
            }
            this.f8377i1 = -9223372036854775807L;
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    public void g1() {
        super.g1();
        this.f8381m1 = 0;
    }

    @Override // com.google.android.exoplayer2.c2, i4.f0
    public String getName() {
        return "MediaCodecVideoRenderer";
    }

    protected void h2(com.google.android.exoplayer2.mediacodec.j jVar, Surface surface) {
        jVar.k(surface);
    }

    protected boolean i2(long j8, long j9, boolean z4) {
        return Q1(j8) && !z4;
    }

    protected boolean j2(long j8, long j9, boolean z4) {
        return P1(j8) && !z4;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected MediaCodecDecoderException k0(Throwable th, com.google.android.exoplayer2.mediacodec.k kVar) {
        return new MediaCodecVideoDecoderException(th, kVar, this.f8369a1);
    }

    protected boolean k2(long j8, long j9) {
        return P1(j8) && j9 > 100000;
    }

    protected void m2(com.google.android.exoplayer2.mediacodec.j jVar, int i8, long j8) {
        i0.a("skipVideoBuffer");
        jVar.h(i8, false);
        i0.c();
        this.M0.f21590f++;
    }

    protected void n2(int i8, int i9) {
        l4.e eVar = this.M0;
        eVar.f21592h += i8;
        int i10 = i8 + i9;
        eVar.f21591g += i10;
        this.f8379k1 += i10;
        int i11 = this.f8380l1 + i10;
        this.f8380l1 = i11;
        eVar.f21593i = Math.max(i11, eVar.f21593i);
        int i12 = this.V0;
        if (i12 <= 0 || this.f8379k1 < i12) {
            return;
        }
        S1();
    }

    protected void o2(long j8) {
        this.M0.a(j8);
        this.f8384p1 += j8;
        this.f8385q1++;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected boolean q1(com.google.android.exoplayer2.mediacodec.k kVar) {
        return this.f8369a1 != null || l2(kVar);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.c2
    public void s(float f5, float f8) {
        super.s(f5, f8);
        this.S0.i(f5);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected int t1(com.google.android.exoplayer2.mediacodec.l lVar, w0 w0Var) {
        boolean z4;
        int i8 = 0;
        if (b6.t.s(w0Var.f11207m)) {
            boolean z8 = w0Var.q != null;
            List<com.google.android.exoplayer2.mediacodec.k> L1 = L1(this.R0, lVar, w0Var, z8, false);
            if (z8 && L1.isEmpty()) {
                L1 = L1(this.R0, lVar, w0Var, false, false);
            }
            if (L1.isEmpty()) {
                return f0.u(1);
            }
            if (MediaCodecRenderer.u1(w0Var)) {
                com.google.android.exoplayer2.mediacodec.k kVar = L1.get(0);
                boolean o5 = kVar.o(w0Var);
                if (!o5) {
                    for (int i9 = 1; i9 < L1.size(); i9++) {
                        com.google.android.exoplayer2.mediacodec.k kVar2 = L1.get(i9);
                        if (kVar2.o(w0Var)) {
                            z4 = false;
                            o5 = true;
                            kVar = kVar2;
                            break;
                        }
                    }
                }
                z4 = true;
                int i10 = o5 ? 4 : 3;
                int i11 = kVar.r(w0Var) ? 16 : 8;
                int i12 = kVar.f10037h ? 64 : 0;
                int i13 = z4 ? RecognitionOptions.ITF : 0;
                if (l0.f8063a >= 26 && "video/dolby-vision".equals(w0Var.f11207m) && !a.a(this.R0)) {
                    i13 = RecognitionOptions.QR_CODE;
                }
                if (o5) {
                    List<com.google.android.exoplayer2.mediacodec.k> L12 = L1(this.R0, lVar, w0Var, z8, true);
                    if (!L12.isEmpty()) {
                        com.google.android.exoplayer2.mediacodec.k kVar3 = MediaCodecUtil.u(L12, w0Var).get(0);
                        if (kVar3.o(w0Var) && kVar3.r(w0Var)) {
                            i8 = 32;
                        }
                    }
                }
                return f0.n(i10, i11, i8, i12, i13);
            }
            return f0.u(2);
        }
        return f0.u(0);
    }

    @Override // com.google.android.exoplayer2.f, com.google.android.exoplayer2.z1.b
    public void x(int i8, Object obj) {
        if (i8 == 1) {
            g2(obj);
        } else if (i8 == 7) {
            this.A1 = (i) obj;
        } else if (i8 == 10) {
            int intValue = ((Integer) obj).intValue();
            if (this.f8393y1 != intValue) {
                this.f8393y1 = intValue;
                if (this.f8392x1) {
                    e1();
                }
            }
        } else if (i8 != 4) {
            if (i8 != 5) {
                super.x(i8, obj);
            } else {
                this.S0.o(((Integer) obj).intValue());
            }
        } else {
            this.f8372d1 = ((Integer) obj).intValue();
            com.google.android.exoplayer2.mediacodec.j w02 = w0();
            if (w02 != null) {
                w02.i(this.f8372d1);
            }
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected boolean y0() {
        return this.f8392x1 && l0.f8063a < 23;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected float z0(float f5, w0 w0Var, w0[] w0VarArr) {
        float f8 = -1.0f;
        for (w0 w0Var2 : w0VarArr) {
            float f9 = w0Var2.f11213y;
            if (f9 != -1.0f) {
                f8 = Math.max(f8, f9);
            }
        }
        if (f8 == -1.0f) {
            return -1.0f;
        }
        return f8 * f5;
    }
}
