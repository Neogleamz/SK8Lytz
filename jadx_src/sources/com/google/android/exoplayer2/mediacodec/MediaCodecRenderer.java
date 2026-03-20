package com.google.android.exoplayer2.mediacodec;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaCryptoException;
import android.media.MediaFormat;
import android.media.metrics.LogSessionId;
import android.os.Bundle;
import android.os.SystemClock;
import b6.g0;
import b6.i0;
import b6.l0;
import b6.u;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.mediacodec.j;
import com.google.android.exoplayer2.w0;
import i4.s;
import j4.t1;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class MediaCodecRenderer extends com.google.android.exoplayer2.f {
    private static final byte[] Q0 = {0, 0, 1, 103, 66, -64, 11, -38, 37, -112, 0, 0, 1, 104, -50, 15, 19, 32, 0, 0, 1, 101, -120, -124, 13, -50, 113, 24, -96, 0, 47, -65, 28, 49, -61, 39, 93, 120};
    private final f A;
    private int A0;
    private final ArrayList<Long> B;
    private int B0;
    private final MediaCodec.BufferInfo C;
    private boolean C0;
    private boolean D0;
    private final ArrayDeque<b> E;
    private boolean E0;
    private w0 F;
    private long F0;
    private w0 G;
    private long G0;
    private DrmSession H;
    private boolean H0;
    private boolean I0;
    private boolean J0;
    private DrmSession K;
    private boolean K0;
    private MediaCrypto L;
    private ExoPlaybackException L0;
    protected l4.e M0;
    private b N0;
    private boolean O;
    private long O0;
    private long P;
    private boolean P0;
    private float Q;
    private float R;
    private j T;
    private w0 W;
    private MediaFormat X;
    private boolean Y;
    private float Z;

    /* renamed from: a0  reason: collision with root package name */
    private ArrayDeque<k> f9921a0;

    /* renamed from: b0  reason: collision with root package name */
    private DecoderInitializationException f9922b0;

    /* renamed from: c0  reason: collision with root package name */
    private k f9923c0;

    /* renamed from: d0  reason: collision with root package name */
    private int f9924d0;

    /* renamed from: e0  reason: collision with root package name */
    private boolean f9925e0;

    /* renamed from: f0  reason: collision with root package name */
    private boolean f9926f0;

    /* renamed from: g0  reason: collision with root package name */
    private boolean f9927g0;

    /* renamed from: h0  reason: collision with root package name */
    private boolean f9928h0;

    /* renamed from: i0  reason: collision with root package name */
    private boolean f9929i0;

    /* renamed from: j0  reason: collision with root package name */
    private boolean f9930j0;

    /* renamed from: k0  reason: collision with root package name */
    private boolean f9931k0;

    /* renamed from: l0  reason: collision with root package name */
    private boolean f9932l0;

    /* renamed from: m0  reason: collision with root package name */
    private boolean f9933m0;

    /* renamed from: n0  reason: collision with root package name */
    private boolean f9934n0;

    /* renamed from: o0  reason: collision with root package name */
    private g f9935o0;

    /* renamed from: p  reason: collision with root package name */
    private final j.b f9936p;

    /* renamed from: p0  reason: collision with root package name */
    private long f9937p0;
    private final l q;

    /* renamed from: q0  reason: collision with root package name */
    private int f9938q0;

    /* renamed from: r0  reason: collision with root package name */
    private int f9939r0;

    /* renamed from: s0  reason: collision with root package name */
    private ByteBuffer f9940s0;

    /* renamed from: t  reason: collision with root package name */
    private final boolean f9941t;

    /* renamed from: t0  reason: collision with root package name */
    private boolean f9942t0;

    /* renamed from: u0  reason: collision with root package name */
    private boolean f9943u0;

    /* renamed from: v0  reason: collision with root package name */
    private boolean f9944v0;

    /* renamed from: w  reason: collision with root package name */
    private final float f9945w;

    /* renamed from: w0  reason: collision with root package name */
    private boolean f9946w0;

    /* renamed from: x  reason: collision with root package name */
    private final DecoderInputBuffer f9947x;

    /* renamed from: x0  reason: collision with root package name */
    private boolean f9948x0;

    /* renamed from: y  reason: collision with root package name */
    private final DecoderInputBuffer f9949y;

    /* renamed from: y0  reason: collision with root package name */
    private boolean f9950y0;

    /* renamed from: z  reason: collision with root package name */
    private final DecoderInputBuffer f9951z;

    /* renamed from: z0  reason: collision with root package name */
    private int f9952z0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class DecoderInitializationException extends Exception {

        /* renamed from: a  reason: collision with root package name */
        public final String f9953a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f9954b;

        /* renamed from: c  reason: collision with root package name */
        public final k f9955c;

        /* renamed from: d  reason: collision with root package name */
        public final String f9956d;

        /* renamed from: e  reason: collision with root package name */
        public final DecoderInitializationException f9957e;

        public DecoderInitializationException(w0 w0Var, Throwable th, boolean z4, int i8) {
            this("Decoder init failed: [" + i8 + "], " + w0Var, th, w0Var.f11207m, z4, null, b(i8), null);
        }

        public DecoderInitializationException(w0 w0Var, Throwable th, boolean z4, k kVar) {
            this("Decoder init failed: " + kVar.f10030a + ", " + w0Var, th, w0Var.f11207m, z4, kVar, l0.f8063a >= 21 ? d(th) : null, null);
        }

        private DecoderInitializationException(String str, Throwable th, String str2, boolean z4, k kVar, String str3, DecoderInitializationException decoderInitializationException) {
            super(str, th);
            this.f9953a = str2;
            this.f9954b = z4;
            this.f9955c = kVar;
            this.f9956d = str3;
            this.f9957e = decoderInitializationException;
        }

        private static String b(int i8) {
            String str = i8 < 0 ? "neg_" : BuildConfig.FLAVOR;
            return "com.google.android.exoplayer2.mediacodec.MediaCodecRenderer_" + str + Math.abs(i8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public DecoderInitializationException c(DecoderInitializationException decoderInitializationException) {
            return new DecoderInitializationException(getMessage(), getCause(), this.f9953a, this.f9954b, this.f9955c, this.f9956d, decoderInitializationException);
        }

        private static String d(Throwable th) {
            if (th instanceof MediaCodec.CodecException) {
                return ((MediaCodec.CodecException) th).getDiagnosticInfo();
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        public static void a(j.a aVar, t1 t1Var) {
            LogSessionId a9 = t1Var.a();
            if (a9.equals(LogSessionId.LOG_SESSION_ID_NONE)) {
                return;
            }
            aVar.f10025b.setString("log-session-id", a9.getStringId());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: e  reason: collision with root package name */
        public static final b f9958e = new b(-9223372036854775807L, -9223372036854775807L, -9223372036854775807L);

        /* renamed from: a  reason: collision with root package name */
        public final long f9959a;

        /* renamed from: b  reason: collision with root package name */
        public final long f9960b;

        /* renamed from: c  reason: collision with root package name */
        public final long f9961c;

        /* renamed from: d  reason: collision with root package name */
        public final g0<w0> f9962d = new g0<>();

        public b(long j8, long j9, long j10) {
            this.f9959a = j8;
            this.f9960b = j9;
            this.f9961c = j10;
        }
    }

    public MediaCodecRenderer(int i8, j.b bVar, l lVar, boolean z4, float f5) {
        super(i8);
        this.f9936p = bVar;
        this.q = (l) b6.a.e(lVar);
        this.f9941t = z4;
        this.f9945w = f5;
        this.f9947x = DecoderInputBuffer.C();
        this.f9949y = new DecoderInputBuffer(0);
        this.f9951z = new DecoderInputBuffer(2);
        f fVar = new f();
        this.A = fVar;
        this.B = new ArrayList<>();
        this.C = new MediaCodec.BufferInfo();
        this.Q = 1.0f;
        this.R = 1.0f;
        this.P = -9223372036854775807L;
        this.E = new ArrayDeque<>();
        l1(b.f9958e);
        fVar.z(0);
        fVar.f9512c.order(ByteOrder.nativeOrder());
        this.Z = -1.0f;
        this.f9924d0 = 0;
        this.f9952z0 = 0;
        this.f9938q0 = -1;
        this.f9939r0 = -1;
        this.f9937p0 = -9223372036854775807L;
        this.F0 = -9223372036854775807L;
        this.G0 = -9223372036854775807L;
        this.O0 = -9223372036854775807L;
        this.A0 = 0;
        this.B0 = 0;
    }

    private m4.l C0(DrmSession drmSession) {
        l4.b i8 = drmSession.i();
        if (i8 == null || (i8 instanceof m4.l)) {
            return (m4.l) i8;
        }
        throw G(new IllegalArgumentException("Expecting FrameworkCryptoConfig but found: " + i8), this.F, 6001);
    }

    private boolean H0() {
        return this.f9939r0 >= 0;
    }

    private void I0(w0 w0Var) {
        l0();
        String str = w0Var.f11207m;
        if ("audio/mp4a-latm".equals(str) || "audio/mpeg".equals(str) || "audio/opus".equals(str)) {
            this.A.K(32);
        } else {
            this.A.K(1);
        }
        this.f9944v0 = true;
    }

    private void J0(k kVar, MediaCrypto mediaCrypto) {
        String str = kVar.f10030a;
        int i8 = l0.f8063a;
        float z02 = i8 < 23 ? -1.0f : z0(this.R, this.F, M());
        float f5 = z02 > this.f9945w ? z02 : -1.0f;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        j.a D0 = D0(kVar, this.F, mediaCrypto, f5);
        if (i8 >= 31) {
            a.a(D0, L());
        }
        try {
            i0.a("createCodec:" + str);
            this.T = this.f9936p.a(D0);
            i0.c();
            long elapsedRealtime2 = SystemClock.elapsedRealtime();
            if (!kVar.o(this.F)) {
                b6.p.i("MediaCodecRenderer", l0.C("Format exceeds selected codec's capabilities [%s, %s]", w0.i(this.F), str));
            }
            this.f9923c0 = kVar;
            this.Z = f5;
            this.W = this.F;
            this.f9924d0 = b0(str);
            this.f9925e0 = c0(str, this.W);
            this.f9926f0 = h0(str);
            this.f9927g0 = j0(str);
            this.f9928h0 = e0(str);
            this.f9929i0 = f0(str);
            this.f9930j0 = d0(str);
            this.f9931k0 = i0(str, this.W);
            this.f9934n0 = g0(kVar) || y0();
            if (this.T.a()) {
                this.f9950y0 = true;
                this.f9952z0 = 1;
                this.f9932l0 = this.f9924d0 != 0;
            }
            if ("c2.android.mp3.decoder".equals(kVar.f10030a)) {
                this.f9935o0 = new g();
            }
            if (getState() == 2) {
                this.f9937p0 = SystemClock.elapsedRealtime() + 1000;
            }
            this.M0.f21585a++;
            R0(str, D0, elapsedRealtime2, elapsedRealtime2 - elapsedRealtime);
        } catch (Throwable th) {
            i0.c();
            throw th;
        }
    }

    private boolean K0(long j8) {
        int size = this.B.size();
        for (int i8 = 0; i8 < size; i8++) {
            if (this.B.get(i8).longValue() == j8) {
                this.B.remove(i8);
                return true;
            }
        }
        return false;
    }

    private static boolean L0(IllegalStateException illegalStateException) {
        if (l0.f8063a < 21 || !M0(illegalStateException)) {
            StackTraceElement[] stackTrace = illegalStateException.getStackTrace();
            return stackTrace.length > 0 && stackTrace[0].getClassName().equals("android.media.MediaCodec");
        }
        return true;
    }

    private static boolean M0(IllegalStateException illegalStateException) {
        return illegalStateException instanceof MediaCodec.CodecException;
    }

    private static boolean N0(IllegalStateException illegalStateException) {
        if (illegalStateException instanceof MediaCodec.CodecException) {
            return ((MediaCodec.CodecException) illegalStateException).isRecoverable();
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x009f  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00ae A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0049 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void P0(android.media.MediaCrypto r8, boolean r9) {
        /*
            r7 = this;
            java.util.ArrayDeque<com.google.android.exoplayer2.mediacodec.k> r0 = r7.f9921a0
            r1 = 0
            if (r0 != 0) goto L39
            java.util.List r0 = r7.v0(r9)     // Catch: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException -> L2d
            java.util.ArrayDeque r2 = new java.util.ArrayDeque     // Catch: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException -> L2d
            r2.<init>()     // Catch: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException -> L2d
            r7.f9921a0 = r2     // Catch: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException -> L2d
            boolean r3 = r7.f9941t     // Catch: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException -> L2d
            if (r3 == 0) goto L18
            r2.addAll(r0)     // Catch: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException -> L2d
            goto L2a
        L18:
            boolean r2 = r0.isEmpty()     // Catch: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException -> L2d
            if (r2 != 0) goto L2a
            java.util.ArrayDeque<com.google.android.exoplayer2.mediacodec.k> r2 = r7.f9921a0     // Catch: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException -> L2d
            r3 = 0
            java.lang.Object r0 = r0.get(r3)     // Catch: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException -> L2d
            com.google.android.exoplayer2.mediacodec.k r0 = (com.google.android.exoplayer2.mediacodec.k) r0     // Catch: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException -> L2d
            r2.add(r0)     // Catch: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException -> L2d
        L2a:
            r7.f9922b0 = r1     // Catch: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException -> L2d
            goto L39
        L2d:
            r8 = move-exception
            com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$DecoderInitializationException r0 = new com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$DecoderInitializationException
            com.google.android.exoplayer2.w0 r1 = r7.F
            r2 = -49998(0xffffffffffff3cb2, float:NaN)
            r0.<init>(r1, r8, r9, r2)
            throw r0
        L39:
            java.util.ArrayDeque<com.google.android.exoplayer2.mediacodec.k> r0 = r7.f9921a0
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto Lb4
            java.util.ArrayDeque<com.google.android.exoplayer2.mediacodec.k> r0 = r7.f9921a0
            java.lang.Object r0 = r0.peekFirst()
            com.google.android.exoplayer2.mediacodec.k r0 = (com.google.android.exoplayer2.mediacodec.k) r0
        L49:
            com.google.android.exoplayer2.mediacodec.j r2 = r7.T
            if (r2 != 0) goto Lb1
            java.util.ArrayDeque<com.google.android.exoplayer2.mediacodec.k> r2 = r7.f9921a0
            java.lang.Object r2 = r2.peekFirst()
            com.google.android.exoplayer2.mediacodec.k r2 = (com.google.android.exoplayer2.mediacodec.k) r2
            boolean r3 = r7.q1(r2)
            if (r3 != 0) goto L5c
            return
        L5c:
            r7.J0(r2, r8)     // Catch: java.lang.Exception -> L60
            goto L49
        L60:
            r3 = move-exception
            java.lang.String r4 = "MediaCodecRenderer"
            if (r2 != r0) goto L73
            java.lang.String r3 = "Preferred decoder instantiation failed. Sleeping for 50ms then retrying."
            b6.p.i(r4, r3)     // Catch: java.lang.Exception -> L74
            r5 = 50
            java.lang.Thread.sleep(r5)     // Catch: java.lang.Exception -> L74
            r7.J0(r2, r8)     // Catch: java.lang.Exception -> L74
            goto L49
        L73:
            throw r3     // Catch: java.lang.Exception -> L74
        L74:
            r3 = move-exception
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Failed to initialize decoder: "
            r5.append(r6)
            r5.append(r2)
            java.lang.String r5 = r5.toString()
            b6.p.j(r4, r5, r3)
            java.util.ArrayDeque<com.google.android.exoplayer2.mediacodec.k> r4 = r7.f9921a0
            r4.removeFirst()
            com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$DecoderInitializationException r4 = new com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$DecoderInitializationException
            com.google.android.exoplayer2.w0 r5 = r7.F
            r4.<init>(r5, r3, r9, r2)
            r7.Q0(r4)
            com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$DecoderInitializationException r2 = r7.f9922b0
            if (r2 != 0) goto L9f
            r7.f9922b0 = r4
            goto La5
        L9f:
            com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$DecoderInitializationException r2 = com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException.a(r2, r4)
            r7.f9922b0 = r2
        La5:
            java.util.ArrayDeque<com.google.android.exoplayer2.mediacodec.k> r2 = r7.f9921a0
            boolean r2 = r2.isEmpty()
            if (r2 != 0) goto Lae
            goto L49
        Lae:
            com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$DecoderInitializationException r8 = r7.f9922b0
            throw r8
        Lb1:
            r7.f9921a0 = r1
            return
        Lb4:
            com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$DecoderInitializationException r8 = new com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$DecoderInitializationException
            com.google.android.exoplayer2.w0 r0 = r7.F
            r2 = -49999(0xffffffffffff3cb1, float:NaN)
            r8.<init>(r0, r1, r9, r2)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.P0(android.media.MediaCrypto, boolean):void");
    }

    private void Y() {
        b6.a.f(!this.H0);
        s J = J();
        this.f9951z.k();
        do {
            this.f9951z.k();
            int V = V(J, this.f9951z, 0);
            if (V == -5) {
                T0(J);
                return;
            } else if (V != -4) {
                if (V != -3) {
                    throw new IllegalStateException();
                }
                return;
            } else if (this.f9951z.t()) {
                this.H0 = true;
                return;
            } else {
                if (this.J0) {
                    w0 w0Var = (w0) b6.a.e(this.F);
                    this.G = w0Var;
                    U0(w0Var, null);
                    this.J0 = false;
                }
                this.f9951z.A();
            }
        } while (this.A.E(this.f9951z));
        this.f9946w0 = true;
    }

    private boolean Z(long j8, long j9) {
        boolean z4;
        b6.a.f(!this.I0);
        if (this.A.J()) {
            f fVar = this.A;
            if (!a1(j8, j9, null, fVar.f9512c, this.f9939r0, 0, fVar.I(), this.A.G(), this.A.s(), this.A.t(), this.G)) {
                return false;
            }
            W0(this.A.H());
            this.A.k();
            z4 = false;
        } else {
            z4 = false;
        }
        if (this.H0) {
            this.I0 = true;
            return z4;
        }
        if (this.f9946w0) {
            b6.a.f(this.A.E(this.f9951z));
            this.f9946w0 = z4;
        }
        if (this.f9948x0) {
            if (this.A.J()) {
                return true;
            }
            l0();
            this.f9948x0 = z4;
            O0();
            if (!this.f9944v0) {
                return z4;
            }
        }
        Y();
        if (this.A.J()) {
            this.A.A();
        }
        if (this.A.J() || this.H0 || this.f9948x0) {
            return true;
        }
        return z4;
    }

    @TargetApi(23)
    private void Z0() {
        int i8 = this.B0;
        if (i8 == 1) {
            s0();
        } else if (i8 == 2) {
            s0();
            w1();
        } else if (i8 == 3) {
            d1();
        } else {
            this.I0 = true;
            f1();
        }
    }

    private int b0(String str) {
        int i8 = l0.f8063a;
        if (i8 <= 25 && "OMX.Exynos.avc.dec.secure".equals(str)) {
            String str2 = l0.f8066d;
            if (str2.startsWith("SM-T585") || str2.startsWith("SM-A510") || str2.startsWith("SM-A520") || str2.startsWith("SM-J700")) {
                return 2;
            }
        }
        if (i8 < 24) {
            if ("OMX.Nvidia.h264.decode".equals(str) || "OMX.Nvidia.h264.decode.secure".equals(str)) {
                String str3 = l0.f8064b;
                return ("flounder".equals(str3) || "flounder_lte".equals(str3) || "grouper".equals(str3) || "tilapia".equals(str3)) ? 1 : 0;
            }
            return 0;
        }
        return 0;
    }

    private void b1() {
        this.E0 = true;
        MediaFormat b9 = this.T.b();
        if (this.f9924d0 != 0 && b9.getInteger("width") == 32 && b9.getInteger("height") == 32) {
            this.f9933m0 = true;
            return;
        }
        if (this.f9931k0) {
            b9.setInteger("channel-count", 1);
        }
        this.X = b9;
        this.Y = true;
    }

    private static boolean c0(String str, w0 w0Var) {
        return l0.f8063a < 21 && w0Var.f11209p.isEmpty() && "OMX.MTK.VIDEO.DECODER.AVC".equals(str);
    }

    private boolean c1(int i8) {
        s J = J();
        this.f9947x.k();
        int V = V(J, this.f9947x, i8 | 4);
        if (V == -5) {
            T0(J);
            return true;
        } else if (V == -4 && this.f9947x.t()) {
            this.H0 = true;
            Z0();
            return false;
        } else {
            return false;
        }
    }

    private static boolean d0(String str) {
        if (l0.f8063a < 21 && "OMX.SEC.mp3.dec".equals(str) && "samsung".equals(l0.f8065c)) {
            String str2 = l0.f8064b;
            if (str2.startsWith("baffin") || str2.startsWith("grand") || str2.startsWith("fortuna") || str2.startsWith("gprimelte") || str2.startsWith("j2y18lte") || str2.startsWith("ms01")) {
                return true;
            }
        }
        return false;
    }

    private void d1() {
        e1();
        O0();
    }

    private static boolean e0(String str) {
        int i8 = l0.f8063a;
        if (i8 > 23 || !"OMX.google.vorbis.decoder".equals(str)) {
            if (i8 <= 19) {
                String str2 = l0.f8064b;
                if (("hb2000".equals(str2) || "stvm8".equals(str2)) && ("OMX.amlogic.avc.decoder.awesome".equals(str) || "OMX.amlogic.avc.decoder.awesome.secure".equals(str))) {
                }
            }
            return false;
        }
        return true;
    }

    private static boolean f0(String str) {
        return l0.f8063a == 21 && "OMX.google.aac.decoder".equals(str);
    }

    private static boolean g0(k kVar) {
        String str = kVar.f10030a;
        int i8 = l0.f8063a;
        return (i8 <= 25 && "OMX.rk.video_decoder.avc".equals(str)) || (i8 <= 17 && "OMX.allwinner.video.decoder.avc".equals(str)) || ((i8 <= 29 && ("OMX.broadcom.video_decoder.tunnel".equals(str) || "OMX.broadcom.video_decoder.tunnel.secure".equals(str) || "OMX.bcm.vdec.avc.tunnel".equals(str) || "OMX.bcm.vdec.avc.tunnel.secure".equals(str) || "OMX.bcm.vdec.hevc.tunnel".equals(str) || "OMX.bcm.vdec.hevc.tunnel.secure".equals(str))) || ("Amazon".equals(l0.f8065c) && "AFTS".equals(l0.f8066d) && kVar.f10036g));
    }

    private static boolean h0(String str) {
        int i8 = l0.f8063a;
        return i8 < 18 || (i8 == 18 && ("OMX.SEC.avc.dec".equals(str) || "OMX.SEC.avc.dec.secure".equals(str))) || (i8 == 19 && l0.f8066d.startsWith("SM-G800") && ("OMX.Exynos.avc.dec".equals(str) || "OMX.Exynos.avc.dec.secure".equals(str)));
    }

    private static boolean i0(String str, w0 w0Var) {
        return l0.f8063a <= 18 && w0Var.F == 1 && "OMX.MTK.AUDIO.DECODER.MP3".equals(str);
    }

    private void i1() {
        this.f9938q0 = -1;
        this.f9949y.f9512c = null;
    }

    private static boolean j0(String str) {
        return l0.f8063a == 29 && "c2.android.aac.decoder".equals(str);
    }

    private void j1() {
        this.f9939r0 = -1;
        this.f9940s0 = null;
    }

    private void k1(DrmSession drmSession) {
        DrmSession.c(this.H, drmSession);
        this.H = drmSession;
    }

    private void l0() {
        this.f9948x0 = false;
        this.A.k();
        this.f9951z.k();
        this.f9946w0 = false;
        this.f9944v0 = false;
    }

    private void l1(b bVar) {
        this.N0 = bVar;
        long j8 = bVar.f9961c;
        if (j8 != -9223372036854775807L) {
            this.P0 = true;
            V0(j8);
        }
    }

    private boolean m0() {
        if (this.C0) {
            this.A0 = 1;
            if (this.f9926f0 || this.f9928h0) {
                this.B0 = 3;
                return false;
            }
            this.B0 = 1;
        }
        return true;
    }

    private void n0() {
        if (!this.C0) {
            d1();
            return;
        }
        this.A0 = 1;
        this.B0 = 3;
    }

    @TargetApi(23)
    private boolean o0() {
        if (this.C0) {
            this.A0 = 1;
            if (this.f9926f0 || this.f9928h0) {
                this.B0 = 3;
                return false;
            }
            this.B0 = 2;
        } else {
            w1();
        }
        return true;
    }

    private void o1(DrmSession drmSession) {
        DrmSession.c(this.K, drmSession);
        this.K = drmSession;
    }

    private boolean p0(long j8, long j9) {
        boolean z4;
        boolean a12;
        j jVar;
        ByteBuffer byteBuffer;
        int i8;
        MediaCodec.BufferInfo bufferInfo;
        int f5;
        if (!H0()) {
            if (this.f9929i0 && this.D0) {
                try {
                    f5 = this.T.f(this.C);
                } catch (IllegalStateException unused) {
                    Z0();
                    if (this.I0) {
                        e1();
                    }
                    return false;
                }
            } else {
                f5 = this.T.f(this.C);
            }
            if (f5 < 0) {
                if (f5 == -2) {
                    b1();
                    return true;
                }
                if (this.f9934n0 && (this.H0 || this.A0 == 2)) {
                    Z0();
                }
                return false;
            } else if (this.f9933m0) {
                this.f9933m0 = false;
                this.T.h(f5, false);
                return true;
            } else {
                MediaCodec.BufferInfo bufferInfo2 = this.C;
                if (bufferInfo2.size == 0 && (bufferInfo2.flags & 4) != 0) {
                    Z0();
                    return false;
                }
                this.f9939r0 = f5;
                ByteBuffer m8 = this.T.m(f5);
                this.f9940s0 = m8;
                if (m8 != null) {
                    m8.position(this.C.offset);
                    ByteBuffer byteBuffer2 = this.f9940s0;
                    MediaCodec.BufferInfo bufferInfo3 = this.C;
                    byteBuffer2.limit(bufferInfo3.offset + bufferInfo3.size);
                }
                if (this.f9930j0) {
                    MediaCodec.BufferInfo bufferInfo4 = this.C;
                    if (bufferInfo4.presentationTimeUs == 0 && (bufferInfo4.flags & 4) != 0) {
                        long j10 = this.F0;
                        if (j10 != -9223372036854775807L) {
                            bufferInfo4.presentationTimeUs = j10;
                        }
                    }
                }
                this.f9942t0 = K0(this.C.presentationTimeUs);
                long j11 = this.G0;
                long j12 = this.C.presentationTimeUs;
                this.f9943u0 = j11 == j12;
                x1(j12);
            }
        }
        if (this.f9929i0 && this.D0) {
            try {
                jVar = this.T;
                byteBuffer = this.f9940s0;
                i8 = this.f9939r0;
                bufferInfo = this.C;
                z4 = false;
            } catch (IllegalStateException unused2) {
                z4 = false;
            }
            try {
                a12 = a1(j8, j9, jVar, byteBuffer, i8, bufferInfo.flags, 1, bufferInfo.presentationTimeUs, this.f9942t0, this.f9943u0, this.G);
            } catch (IllegalStateException unused3) {
                Z0();
                if (this.I0) {
                    e1();
                }
                return z4;
            }
        } else {
            z4 = false;
            j jVar2 = this.T;
            ByteBuffer byteBuffer3 = this.f9940s0;
            int i9 = this.f9939r0;
            MediaCodec.BufferInfo bufferInfo5 = this.C;
            a12 = a1(j8, j9, jVar2, byteBuffer3, i9, bufferInfo5.flags, 1, bufferInfo5.presentationTimeUs, this.f9942t0, this.f9943u0, this.G);
        }
        if (a12) {
            W0(this.C.presentationTimeUs);
            boolean z8 = (this.C.flags & 4) != 0 ? true : z4;
            j1();
            if (!z8) {
                return true;
            }
            Z0();
        }
        return z4;
    }

    private boolean p1(long j8) {
        return this.P == -9223372036854775807L || SystemClock.elapsedRealtime() - j8 < this.P;
    }

    private boolean q0(k kVar, w0 w0Var, DrmSession drmSession, DrmSession drmSession2) {
        m4.l C0;
        if (drmSession == drmSession2) {
            return false;
        }
        if (drmSession2 == null || drmSession == null || !drmSession2.d().equals(drmSession.d()) || l0.f8063a < 23) {
            return true;
        }
        UUID uuid = i4.b.f20469e;
        if (uuid.equals(drmSession.d()) || uuid.equals(drmSession2.d()) || (C0 = C0(drmSession2)) == null) {
            return true;
        }
        return !kVar.f10036g && (C0.f21837c ? false : drmSession2.g(w0Var.f11207m));
    }

    private boolean r0() {
        int i8;
        if (this.T == null || (i8 = this.A0) == 2 || this.H0) {
            return false;
        }
        if (i8 == 0 && r1()) {
            n0();
        }
        if (this.f9938q0 < 0) {
            int e8 = this.T.e();
            this.f9938q0 = e8;
            if (e8 < 0) {
                return false;
            }
            this.f9949y.f9512c = this.T.j(e8);
            this.f9949y.k();
        }
        if (this.A0 == 1) {
            if (!this.f9934n0) {
                this.D0 = true;
                this.T.l(this.f9938q0, 0, 0, 0L, 4);
                i1();
            }
            this.A0 = 2;
            return false;
        } else if (this.f9932l0) {
            this.f9932l0 = false;
            ByteBuffer byteBuffer = this.f9949y.f9512c;
            byte[] bArr = Q0;
            byteBuffer.put(bArr);
            this.T.l(this.f9938q0, 0, bArr.length, 0L, 0);
            i1();
            this.C0 = true;
            return true;
        } else {
            if (this.f9952z0 == 1) {
                for (int i9 = 0; i9 < this.W.f11209p.size(); i9++) {
                    this.f9949y.f9512c.put(this.W.f11209p.get(i9));
                }
                this.f9952z0 = 2;
            }
            int position = this.f9949y.f9512c.position();
            s J = J();
            try {
                int V = V(J, this.f9949y, 0);
                if (i() || this.f9949y.w()) {
                    this.G0 = this.F0;
                }
                if (V == -3) {
                    return false;
                }
                if (V == -5) {
                    if (this.f9952z0 == 2) {
                        this.f9949y.k();
                        this.f9952z0 = 1;
                    }
                    T0(J);
                    return true;
                } else if (this.f9949y.t()) {
                    if (this.f9952z0 == 2) {
                        this.f9949y.k();
                        this.f9952z0 = 1;
                    }
                    this.H0 = true;
                    if (!this.C0) {
                        Z0();
                        return false;
                    }
                    try {
                        if (!this.f9934n0) {
                            this.D0 = true;
                            this.T.l(this.f9938q0, 0, 0, 0L, 4);
                            i1();
                        }
                        return false;
                    } catch (MediaCodec.CryptoException e9) {
                        throw G(e9, this.F, l0.U(e9.getErrorCode()));
                    }
                } else if (!this.C0 && !this.f9949y.v()) {
                    this.f9949y.k();
                    if (this.f9952z0 == 2) {
                        this.f9952z0 = 1;
                    }
                    return true;
                } else {
                    boolean B = this.f9949y.B();
                    if (B) {
                        this.f9949y.f9511b.b(position);
                    }
                    if (this.f9925e0 && !B) {
                        u.b(this.f9949y.f9512c);
                        if (this.f9949y.f9512c.position() == 0) {
                            return true;
                        }
                        this.f9925e0 = false;
                    }
                    DecoderInputBuffer decoderInputBuffer = this.f9949y;
                    long j8 = decoderInputBuffer.f9514e;
                    g gVar = this.f9935o0;
                    if (gVar != null) {
                        j8 = gVar.d(this.F, decoderInputBuffer);
                        this.F0 = Math.max(this.F0, this.f9935o0.b(this.F));
                    }
                    long j9 = j8;
                    if (this.f9949y.s()) {
                        this.B.add(Long.valueOf(j9));
                    }
                    if (this.J0) {
                        (!this.E.isEmpty() ? this.E.peekLast() : this.N0).f9962d.a(j9, this.F);
                        this.J0 = false;
                    }
                    this.F0 = Math.max(this.F0, j9);
                    this.f9949y.A();
                    if (this.f9949y.r()) {
                        G0(this.f9949y);
                    }
                    Y0(this.f9949y);
                    try {
                        if (B) {
                            this.T.n(this.f9938q0, 0, this.f9949y.f9511b, j9, 0);
                        } else {
                            this.T.l(this.f9938q0, 0, this.f9949y.f9512c.limit(), j9, 0);
                        }
                        i1();
                        this.C0 = true;
                        this.f9952z0 = 0;
                        this.M0.f21587c++;
                        return true;
                    } catch (MediaCodec.CryptoException e10) {
                        throw G(e10, this.F, l0.U(e10.getErrorCode()));
                    }
                }
            } catch (DecoderInputBuffer.InsufficientCapacityException e11) {
                Q0(e11);
                c1(0);
                s0();
                return true;
            }
        }
    }

    private void s0() {
        try {
            this.T.flush();
        } finally {
            g1();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean u1(w0 w0Var) {
        int i8 = w0Var.R;
        return i8 == 0 || i8 == 2;
    }

    private List<k> v0(boolean z4) {
        List<k> B0 = B0(this.q, this.F, z4);
        if (B0.isEmpty() && z4) {
            B0 = B0(this.q, this.F, false);
            if (!B0.isEmpty()) {
                b6.p.i("MediaCodecRenderer", "Drm session requires secure decoder for " + this.F.f11207m + ", but no secure decoder available. Trying to proceed with " + B0 + ".");
            }
        }
        return B0;
    }

    private boolean v1(w0 w0Var) {
        if (l0.f8063a >= 23 && this.T != null && this.B0 != 3 && getState() != 0) {
            float z02 = z0(this.R, w0Var, M());
            float f5 = this.Z;
            if (f5 == z02) {
                return true;
            }
            if (z02 == -1.0f) {
                n0();
                return false;
            } else if (f5 == -1.0f && z02 <= this.f9945w) {
                return true;
            } else {
                Bundle bundle = new Bundle();
                bundle.putFloat("operating-rate", z02);
                this.T.c(bundle);
                this.Z = z02;
            }
        }
        return true;
    }

    private void w1() {
        try {
            this.L.setMediaDrmSession(C0(this.K).f21836b);
            k1(this.K);
            this.A0 = 0;
            this.B0 = 0;
        } catch (MediaCryptoException e8) {
            throw G(e8, this.F, 6006);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final MediaFormat A0() {
        return this.X;
    }

    protected abstract List<k> B0(l lVar, w0 w0Var, boolean z4);

    protected abstract j.a D0(k kVar, w0 w0Var, MediaCrypto mediaCrypto, float f5);

    /* JADX INFO: Access modifiers changed from: protected */
    public final long E0() {
        return this.N0.f9961c;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float F0() {
        return this.Q;
    }

    protected void G0(DecoderInputBuffer decoderInputBuffer) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.f
    public void O() {
        this.F = null;
        l1(b.f9958e);
        this.E.clear();
        u0();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void O0() {
        w0 w0Var;
        if (this.T != null || this.f9944v0 || (w0Var = this.F) == null) {
            return;
        }
        if (this.K == null && s1(w0Var)) {
            I0(this.F);
            return;
        }
        k1(this.K);
        String str = this.F.f11207m;
        DrmSession drmSession = this.H;
        if (drmSession != null) {
            if (this.L == null) {
                m4.l C0 = C0(drmSession);
                if (C0 != null) {
                    try {
                        MediaCrypto mediaCrypto = new MediaCrypto(C0.f21835a, C0.f21836b);
                        this.L = mediaCrypto;
                        this.O = !C0.f21837c && mediaCrypto.requiresSecureDecoderComponent(str);
                    } catch (MediaCryptoException e8) {
                        throw G(e8, this.F, 6006);
                    }
                } else if (this.H.h() == null) {
                    return;
                }
            }
            if (m4.l.f21834d) {
                int state = this.H.getState();
                if (state == 1) {
                    DrmSession.DrmSessionException drmSessionException = (DrmSession.DrmSessionException) b6.a.e(this.H.h());
                    throw G(drmSessionException, this.F, drmSessionException.f9601a);
                } else if (state != 4) {
                    return;
                }
            }
        }
        try {
            P0(this.L, this.O);
        } catch (DecoderInitializationException e9) {
            throw G(e9, this.F, 4001);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.f
    public void P(boolean z4, boolean z8) {
        this.M0 = new l4.e();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.f
    public void Q(long j8, boolean z4) {
        this.H0 = false;
        this.I0 = false;
        this.K0 = false;
        if (this.f9944v0) {
            this.A.k();
            this.f9951z.k();
            this.f9946w0 = false;
        } else {
            t0();
        }
        if (this.N0.f9962d.l() > 0) {
            this.J0 = true;
        }
        this.N0.f9962d.c();
        this.E.clear();
    }

    protected abstract void Q0(Exception exc);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.f
    public void R() {
        try {
            l0();
            e1();
        } finally {
            o1(null);
        }
    }

    protected abstract void R0(String str, j.a aVar, long j8, long j9);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.f
    public void S() {
    }

    protected abstract void S0(String str);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.f
    public void T() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0080, code lost:
        if (o0() == false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00b2, code lost:
        if (o0() == false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00ce, code lost:
        r7 = 2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public l4.g T0(i4.s r12) {
        /*
            Method dump skipped, instructions count: 247
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.T0(i4.s):l4.g");
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0036, code lost:
        if (r5 >= r1) goto L14;
     */
    @Override // com.google.android.exoplayer2.f
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void U(com.google.android.exoplayer2.w0[] r13, long r14, long r16) {
        /*
            r12 = this;
            r0 = r12
            com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$b r1 = r0.N0
            long r1 = r1.f9961c
            r3 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 != 0) goto L20
            com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$b r1 = new com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$b
            r6 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
            r5 = r1
            r8 = r14
            r10 = r16
            r5.<init>(r6, r8, r10)
            r12.l1(r1)
            goto L65
        L20:
            java.util.ArrayDeque<com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$b> r1 = r0.E
            boolean r1 = r1.isEmpty()
            if (r1 == 0) goto L55
            long r1 = r0.F0
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 == 0) goto L38
            long r5 = r0.O0
            int r7 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r7 == 0) goto L55
            int r1 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r1 < 0) goto L55
        L38:
            com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$b r1 = new com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$b
            r6 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
            r5 = r1
            r8 = r14
            r10 = r16
            r5.<init>(r6, r8, r10)
            r12.l1(r1)
            com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$b r1 = r0.N0
            long r1 = r1.f9961c
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 == 0) goto L65
            r12.X0()
            goto L65
        L55:
            java.util.ArrayDeque<com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$b> r1 = r0.E
            com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$b r9 = new com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$b
            long r3 = r0.F0
            r2 = r9
            r5 = r14
            r7 = r16
            r2.<init>(r3, r5, r7)
            r1.add(r9)
        L65:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.U(com.google.android.exoplayer2.w0[], long, long):void");
    }

    protected abstract void U0(w0 w0Var, MediaFormat mediaFormat);

    protected void V0(long j8) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void W0(long j8) {
        this.O0 = j8;
        while (!this.E.isEmpty() && j8 >= this.E.peek().f9959a) {
            l1(this.E.poll());
            X0();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void X0() {
    }

    protected abstract void Y0(DecoderInputBuffer decoderInputBuffer);

    @Override // i4.f0
    public final int a(w0 w0Var) {
        try {
            return t1(this.q, w0Var);
        } catch (MediaCodecUtil.DecoderQueryException e8) {
            throw G(e8, w0Var, 4002);
        }
    }

    protected abstract l4.g a0(k kVar, w0 w0Var, w0 w0Var2);

    protected abstract boolean a1(long j8, long j9, j jVar, ByteBuffer byteBuffer, int i8, int i9, int i10, long j10, boolean z4, boolean z8, w0 w0Var);

    @Override // com.google.android.exoplayer2.c2
    public boolean b() {
        return this.I0;
    }

    @Override // com.google.android.exoplayer2.c2
    public boolean e() {
        return this.F != null && (N() || H0() || (this.f9937p0 != -9223372036854775807L && SystemClock.elapsedRealtime() < this.f9937p0));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.exoplayer2.drm.DrmSession, android.media.MediaCrypto] */
    public void e1() {
        try {
            j jVar = this.T;
            if (jVar != null) {
                jVar.release();
                this.M0.f21586b++;
                S0(this.f9923c0.f10030a);
            }
            this.T = null;
            try {
                MediaCrypto mediaCrypto = this.L;
                if (mediaCrypto != null) {
                    mediaCrypto.release();
                }
            } finally {
            }
        } catch (Throwable th) {
            this.T = null;
            try {
                MediaCrypto mediaCrypto2 = this.L;
                if (mediaCrypto2 != null) {
                    mediaCrypto2.release();
                }
                throw th;
            } finally {
            }
        }
    }

    protected void f1() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void g1() {
        i1();
        j1();
        this.f9937p0 = -9223372036854775807L;
        this.D0 = false;
        this.C0 = false;
        this.f9932l0 = false;
        this.f9933m0 = false;
        this.f9942t0 = false;
        this.f9943u0 = false;
        this.B.clear();
        this.F0 = -9223372036854775807L;
        this.G0 = -9223372036854775807L;
        this.O0 = -9223372036854775807L;
        g gVar = this.f9935o0;
        if (gVar != null) {
            gVar.c();
        }
        this.A0 = 0;
        this.B0 = 0;
        this.f9952z0 = this.f9950y0 ? 1 : 0;
    }

    protected void h1() {
        g1();
        this.L0 = null;
        this.f9935o0 = null;
        this.f9921a0 = null;
        this.f9923c0 = null;
        this.W = null;
        this.X = null;
        this.Y = false;
        this.E0 = false;
        this.Z = -1.0f;
        this.f9924d0 = 0;
        this.f9925e0 = false;
        this.f9926f0 = false;
        this.f9927g0 = false;
        this.f9928h0 = false;
        this.f9929i0 = false;
        this.f9930j0 = false;
        this.f9931k0 = false;
        this.f9934n0 = false;
        this.f9950y0 = false;
        this.f9952z0 = 0;
        this.O = false;
    }

    protected MediaCodecDecoderException k0(Throwable th, k kVar) {
        return new MediaCodecDecoderException(th, kVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void m1() {
        this.K0 = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void n1(ExoPlaybackException exoPlaybackException) {
        this.L0 = exoPlaybackException;
    }

    protected boolean q1(k kVar) {
        return true;
    }

    protected boolean r1() {
        return false;
    }

    @Override // com.google.android.exoplayer2.c2
    public void s(float f5, float f8) {
        this.Q = f5;
        this.R = f8;
        v1(this.W);
    }

    protected boolean s1(w0 w0Var) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean t0() {
        boolean u02 = u0();
        if (u02) {
            O0();
        }
        return u02;
    }

    protected abstract int t1(l lVar, w0 w0Var);

    protected boolean u0() {
        if (this.T == null) {
            return false;
        }
        int i8 = this.B0;
        if (i8 == 3 || this.f9926f0 || ((this.f9927g0 && !this.E0) || (this.f9928h0 && this.D0))) {
            e1();
            return true;
        }
        if (i8 == 2) {
            int i9 = l0.f8063a;
            b6.a.f(i9 >= 23);
            if (i9 >= 23) {
                try {
                    w1();
                } catch (ExoPlaybackException e8) {
                    b6.p.j("MediaCodecRenderer", "Failed to update the DRM session, releasing the codec instead.", e8);
                    e1();
                    return true;
                }
            }
        }
        s0();
        return false;
    }

    @Override // com.google.android.exoplayer2.f, i4.f0
    public final int v() {
        return 8;
    }

    @Override // com.google.android.exoplayer2.c2
    public void w(long j8, long j9) {
        boolean z4 = false;
        if (this.K0) {
            this.K0 = false;
            Z0();
        }
        ExoPlaybackException exoPlaybackException = this.L0;
        if (exoPlaybackException != null) {
            this.L0 = null;
            throw exoPlaybackException;
        }
        try {
            if (this.I0) {
                f1();
            } else if (this.F != null || c1(2)) {
                O0();
                if (this.f9944v0) {
                    i0.a("bypassRender");
                    while (Z(j8, j9)) {
                    }
                } else if (this.T == null) {
                    this.M0.f21588d += X(j8);
                    c1(1);
                    this.M0.c();
                } else {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    i0.a("drainAndFeed");
                    while (p0(j8, j9) && p1(elapsedRealtime)) {
                    }
                    while (r0() && p1(elapsedRealtime)) {
                    }
                }
                i0.c();
                this.M0.c();
            }
        } catch (IllegalStateException e8) {
            if (!L0(e8)) {
                throw e8;
            }
            Q0(e8);
            if (l0.f8063a >= 21 && N0(e8)) {
                z4 = true;
            }
            if (z4) {
                e1();
            }
            throw H(k0(e8, x0()), this.F, z4, 4003);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final j w0() {
        return this.T;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final k x0() {
        return this.f9923c0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void x1(long j8) {
        boolean z4;
        w0 j9 = this.N0.f9962d.j(j8);
        if (j9 == null && this.P0 && this.X != null) {
            j9 = this.N0.f9962d.i();
        }
        if (j9 != null) {
            this.G = j9;
            z4 = true;
        } else {
            z4 = false;
        }
        if (z4 || (this.Y && this.G != null)) {
            U0(this.G, this.X);
            this.Y = false;
            this.P0 = false;
        }
    }

    protected boolean y0() {
        return false;
    }

    protected abstract float z0(float f5, w0 w0Var, w0[] w0VarArr);
}
