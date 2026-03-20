package com.google.android.exoplayer2.audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Handler;
import b6.l0;
import b6.p;
import b6.r;
import b6.s;
import b6.t;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.audio.b;
import com.google.android.exoplayer2.c2;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.mediacodec.j;
import com.google.android.exoplayer2.w0;
import com.google.android.exoplayer2.x1;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.common.collect.ImmutableList;
import i4.f0;
import java.nio.ByteBuffer;
import java.util.List;
import k4.q;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i extends MediaCodecRenderer implements r {
    private final Context R0;
    private final b.a S0;
    private final AudioSink T0;
    private int U0;
    private boolean V0;
    private w0 W0;
    private w0 X0;
    private long Y0;
    private boolean Z0;

    /* renamed from: a1  reason: collision with root package name */
    private boolean f9392a1;

    /* renamed from: b1  reason: collision with root package name */
    private boolean f9393b1;

    /* renamed from: c1  reason: collision with root package name */
    private boolean f9394c1;

    /* renamed from: d1  reason: collision with root package name */
    private c2.a f9395d1;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b {
        public static void a(AudioSink audioSink, Object obj) {
            audioSink.g((AudioDeviceInfo) obj);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private final class c implements AudioSink.a {
        private c() {
        }

        @Override // com.google.android.exoplayer2.audio.AudioSink.a
        public void a(boolean z4) {
            i.this.S0.C(z4);
        }

        @Override // com.google.android.exoplayer2.audio.AudioSink.a
        public void b(Exception exc) {
            p.d("MediaCodecAudioRenderer", "Audio sink error", exc);
            i.this.S0.l(exc);
        }

        @Override // com.google.android.exoplayer2.audio.AudioSink.a
        public void c(long j8) {
            i.this.S0.B(j8);
        }

        @Override // com.google.android.exoplayer2.audio.AudioSink.a
        public void d() {
            if (i.this.f9395d1 != null) {
                i.this.f9395d1.a();
            }
        }

        @Override // com.google.android.exoplayer2.audio.AudioSink.a
        public void e(int i8, long j8, long j9) {
            i.this.S0.D(i8, j8, j9);
        }

        @Override // com.google.android.exoplayer2.audio.AudioSink.a
        public void f() {
            i.this.G1();
        }

        @Override // com.google.android.exoplayer2.audio.AudioSink.a
        public void g() {
            if (i.this.f9395d1 != null) {
                i.this.f9395d1.b();
            }
        }
    }

    public i(Context context, j.b bVar, com.google.android.exoplayer2.mediacodec.l lVar, boolean z4, Handler handler, com.google.android.exoplayer2.audio.b bVar2, AudioSink audioSink) {
        super(1, bVar, lVar, z4, 44100.0f);
        this.R0 = context.getApplicationContext();
        this.T0 = audioSink;
        this.S0 = new b.a(handler, bVar2);
        audioSink.t(new c());
    }

    private static boolean A1(String str) {
        if (l0.f8063a < 24 && "OMX.SEC.aac.dec".equals(str) && "samsung".equals(l0.f8065c)) {
            String str2 = l0.f8064b;
            if (str2.startsWith("zeroflte") || str2.startsWith("herolte") || str2.startsWith("heroqlte")) {
                return true;
            }
        }
        return false;
    }

    private static boolean B1() {
        if (l0.f8063a == 23) {
            String str = l0.f8066d;
            if ("ZTE B2017G".equals(str) || "AXON 7 mini".equals(str)) {
                return true;
            }
        }
        return false;
    }

    private int C1(com.google.android.exoplayer2.mediacodec.k kVar, w0 w0Var) {
        int i8;
        if (!"OMX.google.raw.decoder".equals(kVar.f10030a) || (i8 = l0.f8063a) >= 24 || (i8 == 23 && l0.x0(this.R0))) {
            return w0Var.f11208n;
        }
        return -1;
    }

    private static List<com.google.android.exoplayer2.mediacodec.k> E1(com.google.android.exoplayer2.mediacodec.l lVar, w0 w0Var, boolean z4, AudioSink audioSink) {
        com.google.android.exoplayer2.mediacodec.k v8;
        String str = w0Var.f11207m;
        if (str == null) {
            return ImmutableList.E();
        }
        if (!audioSink.a(w0Var) || (v8 = MediaCodecUtil.v()) == null) {
            List<com.google.android.exoplayer2.mediacodec.k> a9 = lVar.a(str, z4, false);
            String m8 = MediaCodecUtil.m(w0Var);
            return m8 == null ? ImmutableList.x(a9) : ImmutableList.u().j(a9).j(lVar.a(m8, z4, false)).k();
        }
        return ImmutableList.F(v8);
    }

    private void H1() {
        long l8 = this.T0.l(b());
        if (l8 != Long.MIN_VALUE) {
            if (!this.f9392a1) {
                l8 = Math.max(this.Y0, l8);
            }
            this.Y0 = l8;
            this.f9392a1 = false;
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected List<com.google.android.exoplayer2.mediacodec.k> B0(com.google.android.exoplayer2.mediacodec.l lVar, w0 w0Var, boolean z4) {
        return MediaCodecUtil.u(E1(lVar, w0Var, z4, this.T0), w0Var);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected j.a D0(com.google.android.exoplayer2.mediacodec.k kVar, w0 w0Var, MediaCrypto mediaCrypto, float f5) {
        this.U0 = D1(kVar, w0Var, M());
        this.V0 = A1(kVar.f10030a);
        MediaFormat F1 = F1(w0Var, kVar.f10032c, this.U0, f5);
        this.X0 = "audio/raw".equals(kVar.f10031b) && !"audio/raw".equals(w0Var.f11207m) ? w0Var : null;
        return j.a.a(kVar, F1, w0Var, mediaCrypto);
    }

    protected int D1(com.google.android.exoplayer2.mediacodec.k kVar, w0 w0Var, w0[] w0VarArr) {
        int C1 = C1(kVar, w0Var);
        if (w0VarArr.length == 1) {
            return C1;
        }
        for (w0 w0Var2 : w0VarArr) {
            if (kVar.f(w0Var, w0Var2).f21602d != 0) {
                C1 = Math.max(C1, C1(kVar, w0Var2));
            }
        }
        return C1;
    }

    @Override // com.google.android.exoplayer2.f, com.google.android.exoplayer2.c2
    public r E() {
        return this;
    }

    @SuppressLint({"InlinedApi"})
    protected MediaFormat F1(w0 w0Var, String str, int i8, float f5) {
        MediaFormat mediaFormat = new MediaFormat();
        mediaFormat.setString("mime", str);
        mediaFormat.setInteger("channel-count", w0Var.F);
        mediaFormat.setInteger("sample-rate", w0Var.G);
        s.e(mediaFormat, w0Var.f11209p);
        s.d(mediaFormat, "max-input-size", i8);
        int i9 = l0.f8063a;
        if (i9 >= 23) {
            mediaFormat.setInteger("priority", 0);
            if (f5 != -1.0f && !B1()) {
                mediaFormat.setFloat("operating-rate", f5);
            }
        }
        if (i9 <= 28 && "audio/ac4".equals(w0Var.f11207m)) {
            mediaFormat.setInteger("ac4-is-sync", 1);
        }
        if (i9 >= 24 && this.T0.u(l0.c0(4, w0Var.F, w0Var.G)) == 2) {
            mediaFormat.setInteger("pcm-encoding", 4);
        }
        if (i9 >= 32) {
            mediaFormat.setInteger("max-output-channel-count", 99);
        }
        return mediaFormat;
    }

    protected void G1() {
        this.f9392a1 = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.f
    public void O() {
        this.f9393b1 = true;
        this.W0 = null;
        try {
            this.T0.flush();
            try {
                super.O();
            } finally {
            }
        } catch (Throwable th) {
            try {
                super.O();
                throw th;
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.f
    public void P(boolean z4, boolean z8) {
        super.P(z4, z8);
        this.S0.p(this.M0);
        if (I().f20501a) {
            this.T0.r();
        } else {
            this.T0.m();
        }
        this.T0.q(L());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.f
    public void Q(long j8, boolean z4) {
        super.Q(j8, z4);
        if (this.f9394c1) {
            this.T0.w();
        } else {
            this.T0.flush();
        }
        this.Y0 = j8;
        this.Z0 = true;
        this.f9392a1 = true;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void Q0(Exception exc) {
        p.d("MediaCodecAudioRenderer", "Audio codec error", exc);
        this.S0.k(exc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.f
    public void R() {
        try {
            super.R();
        } finally {
            if (this.f9393b1) {
                this.f9393b1 = false;
                this.T0.reset();
            }
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void R0(String str, j.a aVar, long j8, long j9) {
        this.S0.m(str, j8, j9);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.f
    public void S() {
        super.S();
        this.T0.play();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void S0(String str) {
        this.S0.n(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.f
    public void T() {
        H1();
        this.T0.pause();
        super.T();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    public l4.g T0(i4.s sVar) {
        this.W0 = (w0) b6.a.e(sVar.f20512b);
        l4.g T0 = super.T0(sVar);
        this.S0.q(this.W0, T0);
        return T0;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void U0(w0 w0Var, MediaFormat mediaFormat) {
        int i8;
        w0 w0Var2 = this.X0;
        int[] iArr = null;
        if (w0Var2 != null) {
            w0Var = w0Var2;
        } else if (w0() != null) {
            w0 G = new w0.b().g0("audio/raw").a0("audio/raw".equals(w0Var.f11207m) ? w0Var.H : (l0.f8063a < 24 || !mediaFormat.containsKey("pcm-encoding")) ? mediaFormat.containsKey("v-bits-per-sample") ? l0.b0(mediaFormat.getInteger("v-bits-per-sample")) : 2 : mediaFormat.getInteger("pcm-encoding")).P(w0Var.K).Q(w0Var.L).J(mediaFormat.getInteger("channel-count")).h0(mediaFormat.getInteger("sample-rate")).G();
            if (this.V0 && G.F == 6 && (i8 = w0Var.F) < 6) {
                iArr = new int[i8];
                for (int i9 = 0; i9 < w0Var.F; i9++) {
                    iArr[i9] = i9;
                }
            }
            w0Var = G;
        }
        try {
            this.T0.v(w0Var, 0, iArr);
        } catch (AudioSink.ConfigurationException e8) {
            throw G(e8, e8.f9237a, 5001);
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void V0(long j8) {
        this.T0.o(j8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    public void X0() {
        super.X0();
        this.T0.p();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void Y0(DecoderInputBuffer decoderInputBuffer) {
        if (!this.Z0 || decoderInputBuffer.s()) {
            return;
        }
        if (Math.abs(decoderInputBuffer.f9514e - this.Y0) > 500000) {
            this.Y0 = decoderInputBuffer.f9514e;
        }
        this.Z0 = false;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected l4.g a0(com.google.android.exoplayer2.mediacodec.k kVar, w0 w0Var, w0 w0Var2) {
        l4.g f5 = kVar.f(w0Var, w0Var2);
        int i8 = f5.f21603e;
        if (C1(kVar, w0Var2) > this.U0) {
            i8 |= 64;
        }
        int i9 = i8;
        return new l4.g(kVar.f10030a, w0Var, w0Var2, i9 != 0 ? 0 : f5.f21602d, i9);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected boolean a1(long j8, long j9, com.google.android.exoplayer2.mediacodec.j jVar, ByteBuffer byteBuffer, int i8, int i9, int i10, long j10, boolean z4, boolean z8, w0 w0Var) {
        b6.a.e(byteBuffer);
        if (this.X0 != null && (i9 & 2) != 0) {
            ((com.google.android.exoplayer2.mediacodec.j) b6.a.e(jVar)).h(i8, false);
            return true;
        } else if (z4) {
            if (jVar != null) {
                jVar.h(i8, false);
            }
            this.M0.f21590f += i10;
            this.T0.p();
            return true;
        } else {
            try {
                if (this.T0.s(byteBuffer, j10, i10)) {
                    if (jVar != null) {
                        jVar.h(i8, false);
                    }
                    this.M0.f21589e += i10;
                    return true;
                }
                return false;
            } catch (AudioSink.InitializationException e8) {
                throw H(e8, this.W0, e8.f9239b, 5001);
            } catch (AudioSink.WriteException e9) {
                throw H(e9, w0Var, e9.f9244b, 5002);
            }
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.c2
    public boolean b() {
        return super.b() && this.T0.b();
    }

    @Override // b6.r
    public x1 c() {
        return this.T0.c();
    }

    @Override // b6.r
    public void d(x1 x1Var) {
        this.T0.d(x1Var);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.c2
    public boolean e() {
        return this.T0.i() || super.e();
    }

    @Override // b6.r
    public long f() {
        if (getState() == 2) {
            H1();
        }
        return this.Y0;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void f1() {
        try {
            this.T0.h();
        } catch (AudioSink.WriteException e8) {
            throw H(e8, e8.f9245c, e8.f9244b, 5002);
        }
    }

    @Override // com.google.android.exoplayer2.c2, i4.f0
    public String getName() {
        return "MediaCodecAudioRenderer";
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected boolean s1(w0 w0Var) {
        return this.T0.a(w0Var);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected int t1(com.google.android.exoplayer2.mediacodec.l lVar, w0 w0Var) {
        boolean z4;
        if (t.o(w0Var.f11207m)) {
            int i8 = l0.f8063a >= 21 ? 32 : 0;
            boolean z8 = true;
            boolean z9 = w0Var.R != 0;
            boolean u12 = MediaCodecRenderer.u1(w0Var);
            int i9 = 8;
            if (u12 && this.T0.a(w0Var) && (!z9 || MediaCodecUtil.v() != null)) {
                return f0.r(4, 8, i8);
            }
            if ((!"audio/raw".equals(w0Var.f11207m) || this.T0.a(w0Var)) && this.T0.a(l0.c0(2, w0Var.F, w0Var.G))) {
                List<com.google.android.exoplayer2.mediacodec.k> E1 = E1(lVar, w0Var, false, this.T0);
                if (E1.isEmpty()) {
                    return f0.u(1);
                }
                if (u12) {
                    com.google.android.exoplayer2.mediacodec.k kVar = E1.get(0);
                    boolean o5 = kVar.o(w0Var);
                    if (!o5) {
                        for (int i10 = 1; i10 < E1.size(); i10++) {
                            com.google.android.exoplayer2.mediacodec.k kVar2 = E1.get(i10);
                            if (kVar2.o(w0Var)) {
                                z4 = false;
                                kVar = kVar2;
                                break;
                            }
                        }
                    }
                    z4 = true;
                    z8 = o5;
                    int i11 = z8 ? 4 : 3;
                    if (z8 && kVar.r(w0Var)) {
                        i9 = 16;
                    }
                    return f0.n(i11, i9, i8, kVar.f10037h ? 64 : 0, z4 ? RecognitionOptions.ITF : 0);
                }
                return f0.u(2);
            }
            return f0.u(1);
        }
        return f0.u(0);
    }

    @Override // com.google.android.exoplayer2.f, com.google.android.exoplayer2.z1.b
    public void x(int i8, Object obj) {
        if (i8 == 2) {
            this.T0.e(((Float) obj).floatValue());
        } else if (i8 == 3) {
            this.T0.n((com.google.android.exoplayer2.audio.a) obj);
        } else if (i8 == 6) {
            this.T0.k((q) obj);
        } else {
            switch (i8) {
                case 9:
                    this.T0.f(((Boolean) obj).booleanValue());
                    return;
                case 10:
                    this.T0.j(((Integer) obj).intValue());
                    return;
                case 11:
                    this.f9395d1 = (c2.a) obj;
                    return;
                case 12:
                    if (l0.f8063a >= 23) {
                        b.a(this.T0, obj);
                        return;
                    }
                    return;
                default:
                    super.x(i8, obj);
                    return;
            }
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected float z0(float f5, w0 w0Var, w0[] w0VarArr) {
        int i8 = -1;
        for (w0 w0Var2 : w0VarArr) {
            int i9 = w0Var2.G;
            if (i9 != -1) {
                i8 = Math.max(i8, i9);
            }
        }
        if (i8 == -1) {
            return -1.0f;
        }
        return f5 * i8;
    }
}
