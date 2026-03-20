package com.google.android.exoplayer2.audio;

import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.PlaybackParams;
import android.media.metrics.LogSessionId;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Pair;
import b6.l0;
import b6.p;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.audio.d;
import com.google.android.exoplayer2.audio.g;
import com.google.android.exoplayer2.k;
import com.google.android.exoplayer2.w0;
import com.google.android.exoplayer2.x1;
import com.google.android.libraries.barhopper.RecognitionOptions;
import j4.t1;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import k4.q;
import k4.r;
import k4.s;
import k4.t;
import k4.u;
import k4.v;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class DefaultAudioSink implements AudioSink {

    /* renamed from: e0  reason: collision with root package name */
    public static boolean f9246e0 = false;

    /* renamed from: f0  reason: collision with root package name */
    private static final Object f9247f0 = new Object();

    /* renamed from: g0  reason: collision with root package name */
    private static ExecutorService f9248g0;

    /* renamed from: h0  reason: collision with root package name */
    private static int f9249h0;
    private ByteBuffer A;
    private int B;
    private long C;
    private long D;
    private long E;
    private long F;
    private int G;
    private boolean H;
    private boolean I;
    private long J;
    private float K;
    private AudioProcessor[] L;
    private ByteBuffer[] M;
    private ByteBuffer N;
    private int O;
    private ByteBuffer P;
    private byte[] Q;
    private int R;
    private int S;
    private boolean T;
    private boolean U;
    private boolean V;
    private boolean W;
    private int X;
    private q Y;
    private d Z;

    /* renamed from: a  reason: collision with root package name */
    private final k4.e f9250a;

    /* renamed from: a0  reason: collision with root package name */
    private boolean f9251a0;

    /* renamed from: b  reason: collision with root package name */
    private final k4.f f9252b;

    /* renamed from: b0  reason: collision with root package name */
    private long f9253b0;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f9254c;

    /* renamed from: c0  reason: collision with root package name */
    private boolean f9255c0;

    /* renamed from: d  reason: collision with root package name */
    private final com.google.android.exoplayer2.audio.f f9256d;

    /* renamed from: d0  reason: collision with root package name */
    private boolean f9257d0;

    /* renamed from: e  reason: collision with root package name */
    private final n f9258e;

    /* renamed from: f  reason: collision with root package name */
    private final AudioProcessor[] f9259f;

    /* renamed from: g  reason: collision with root package name */
    private final AudioProcessor[] f9260g;

    /* renamed from: h  reason: collision with root package name */
    private final b6.g f9261h;

    /* renamed from: i  reason: collision with root package name */
    private final com.google.android.exoplayer2.audio.d f9262i;

    /* renamed from: j  reason: collision with root package name */
    private final ArrayDeque<i> f9263j;

    /* renamed from: k  reason: collision with root package name */
    private final boolean f9264k;

    /* renamed from: l  reason: collision with root package name */
    private final int f9265l;

    /* renamed from: m  reason: collision with root package name */
    private l f9266m;

    /* renamed from: n  reason: collision with root package name */
    private final j<AudioSink.InitializationException> f9267n;

    /* renamed from: o  reason: collision with root package name */
    private final j<AudioSink.WriteException> f9268o;

    /* renamed from: p  reason: collision with root package name */
    private final e f9269p;
    private final k.a q;

    /* renamed from: r  reason: collision with root package name */
    private t1 f9270r;

    /* renamed from: s  reason: collision with root package name */
    private AudioSink.a f9271s;

    /* renamed from: t  reason: collision with root package name */
    private g f9272t;

    /* renamed from: u  reason: collision with root package name */
    private g f9273u;

    /* renamed from: v  reason: collision with root package name */
    private AudioTrack f9274v;

    /* renamed from: w  reason: collision with root package name */
    private com.google.android.exoplayer2.audio.a f9275w;

    /* renamed from: x  reason: collision with root package name */
    private i f9276x;

    /* renamed from: y  reason: collision with root package name */
    private i f9277y;

    /* renamed from: z  reason: collision with root package name */
    private x1 f9278z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class InvalidAudioTrackTimestampException extends RuntimeException {
        private InvalidAudioTrackTimestampException(String str) {
            super(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {
        public static void a(AudioTrack audioTrack, d dVar) {
            audioTrack.setPreferredDevice(dVar == null ? null : dVar.f9279a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {
        public static void a(AudioTrack audioTrack, t1 t1Var) {
            LogSessionId a9 = t1Var.a();
            if (a9.equals(LogSessionId.LOG_SESSION_ID_NONE)) {
                return;
            }
            audioTrack.setLogSessionId(a9);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        public final AudioDeviceInfo f9279a;

        public d(AudioDeviceInfo audioDeviceInfo) {
            this.f9279a = audioDeviceInfo;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface e {

        /* renamed from: a  reason: collision with root package name */
        public static final e f9280a = new g.a().g();

        int a(int i8, int i9, int i10, int i11, int i12, int i13, double d8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class f {

        /* renamed from: b  reason: collision with root package name */
        private k4.f f9282b;

        /* renamed from: c  reason: collision with root package name */
        private boolean f9283c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f9284d;

        /* renamed from: g  reason: collision with root package name */
        k.a f9287g;

        /* renamed from: a  reason: collision with root package name */
        private k4.e f9281a = k4.e.f21003c;

        /* renamed from: e  reason: collision with root package name */
        private int f9285e = 0;

        /* renamed from: f  reason: collision with root package name */
        e f9286f = e.f9280a;

        public DefaultAudioSink f() {
            if (this.f9282b == null) {
                this.f9282b = new h(new AudioProcessor[0]);
            }
            return new DefaultAudioSink(this);
        }

        public f g(k4.e eVar) {
            b6.a.e(eVar);
            this.f9281a = eVar;
            return this;
        }

        public f h(boolean z4) {
            this.f9284d = z4;
            return this;
        }

        public f i(boolean z4) {
            this.f9283c = z4;
            return this;
        }

        public f j(int i8) {
            this.f9285e = i8;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g {

        /* renamed from: a  reason: collision with root package name */
        public final w0 f9288a;

        /* renamed from: b  reason: collision with root package name */
        public final int f9289b;

        /* renamed from: c  reason: collision with root package name */
        public final int f9290c;

        /* renamed from: d  reason: collision with root package name */
        public final int f9291d;

        /* renamed from: e  reason: collision with root package name */
        public final int f9292e;

        /* renamed from: f  reason: collision with root package name */
        public final int f9293f;

        /* renamed from: g  reason: collision with root package name */
        public final int f9294g;

        /* renamed from: h  reason: collision with root package name */
        public final int f9295h;

        /* renamed from: i  reason: collision with root package name */
        public final AudioProcessor[] f9296i;

        public g(w0 w0Var, int i8, int i9, int i10, int i11, int i12, int i13, int i14, AudioProcessor[] audioProcessorArr) {
            this.f9288a = w0Var;
            this.f9289b = i8;
            this.f9290c = i9;
            this.f9291d = i10;
            this.f9292e = i11;
            this.f9293f = i12;
            this.f9294g = i13;
            this.f9295h = i14;
            this.f9296i = audioProcessorArr;
        }

        private AudioTrack d(boolean z4, com.google.android.exoplayer2.audio.a aVar, int i8) {
            int i9 = l0.f8063a;
            return i9 >= 29 ? f(z4, aVar, i8) : i9 >= 21 ? e(z4, aVar, i8) : g(aVar, i8);
        }

        private AudioTrack e(boolean z4, com.google.android.exoplayer2.audio.a aVar, int i8) {
            return new AudioTrack(i(aVar, z4), DefaultAudioSink.M(this.f9292e, this.f9293f, this.f9294g), this.f9295h, 1, i8);
        }

        private AudioTrack f(boolean z4, com.google.android.exoplayer2.audio.a aVar, int i8) {
            return new AudioTrack.Builder().setAudioAttributes(i(aVar, z4)).setAudioFormat(DefaultAudioSink.M(this.f9292e, this.f9293f, this.f9294g)).setTransferMode(1).setBufferSizeInBytes(this.f9295h).setSessionId(i8).setOffloadedPlayback(this.f9290c == 1).build();
        }

        private AudioTrack g(com.google.android.exoplayer2.audio.a aVar, int i8) {
            int f02 = l0.f0(aVar.f9322c);
            int i9 = this.f9292e;
            int i10 = this.f9293f;
            int i11 = this.f9294g;
            int i12 = this.f9295h;
            return i8 == 0 ? new AudioTrack(f02, i9, i10, i11, i12, 1) : new AudioTrack(f02, i9, i10, i11, i12, 1, i8);
        }

        private static AudioAttributes i(com.google.android.exoplayer2.audio.a aVar, boolean z4) {
            return z4 ? j() : aVar.b().f9326a;
        }

        private static AudioAttributes j() {
            return new AudioAttributes.Builder().setContentType(3).setFlags(16).setUsage(1).build();
        }

        public AudioTrack a(boolean z4, com.google.android.exoplayer2.audio.a aVar, int i8) {
            try {
                AudioTrack d8 = d(z4, aVar, i8);
                int state = d8.getState();
                if (state == 1) {
                    return d8;
                }
                try {
                    d8.release();
                } catch (Exception unused) {
                }
                throw new AudioSink.InitializationException(state, this.f9292e, this.f9293f, this.f9295h, this.f9288a, l(), null);
            } catch (IllegalArgumentException | UnsupportedOperationException e8) {
                throw new AudioSink.InitializationException(0, this.f9292e, this.f9293f, this.f9295h, this.f9288a, l(), e8);
            }
        }

        public boolean b(g gVar) {
            return gVar.f9290c == this.f9290c && gVar.f9294g == this.f9294g && gVar.f9292e == this.f9292e && gVar.f9293f == this.f9293f && gVar.f9291d == this.f9291d;
        }

        public g c(int i8) {
            return new g(this.f9288a, this.f9289b, this.f9290c, this.f9291d, this.f9292e, this.f9293f, this.f9294g, i8, this.f9296i);
        }

        public long h(long j8) {
            return (j8 * 1000000) / this.f9292e;
        }

        public long k(long j8) {
            return (j8 * 1000000) / this.f9288a.G;
        }

        public boolean l() {
            return this.f9290c == 1;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h implements k4.f {

        /* renamed from: a  reason: collision with root package name */
        private final AudioProcessor[] f9297a;

        /* renamed from: b  reason: collision with root package name */
        private final com.google.android.exoplayer2.audio.k f9298b;

        /* renamed from: c  reason: collision with root package name */
        private final m f9299c;

        public h(AudioProcessor... audioProcessorArr) {
            this(audioProcessorArr, new com.google.android.exoplayer2.audio.k(), new m());
        }

        public h(AudioProcessor[] audioProcessorArr, com.google.android.exoplayer2.audio.k kVar, m mVar) {
            AudioProcessor[] audioProcessorArr2 = new AudioProcessor[audioProcessorArr.length + 2];
            this.f9297a = audioProcessorArr2;
            System.arraycopy(audioProcessorArr, 0, audioProcessorArr2, 0, audioProcessorArr.length);
            this.f9298b = kVar;
            this.f9299c = mVar;
            audioProcessorArr2[audioProcessorArr.length] = kVar;
            audioProcessorArr2[audioProcessorArr.length + 1] = mVar;
        }

        @Override // k4.f
        public x1 a(x1 x1Var) {
            this.f9299c.i(x1Var.f11268a);
            this.f9299c.h(x1Var.f11269b);
            return x1Var;
        }

        @Override // k4.f
        public long b(long j8) {
            return this.f9299c.a(j8);
        }

        @Override // k4.f
        public long c() {
            return this.f9298b.p();
        }

        @Override // k4.f
        public boolean d(boolean z4) {
            this.f9298b.v(z4);
            return z4;
        }

        @Override // k4.f
        public AudioProcessor[] e() {
            return this.f9297a;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class i {

        /* renamed from: a  reason: collision with root package name */
        public final x1 f9300a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f9301b;

        /* renamed from: c  reason: collision with root package name */
        public final long f9302c;

        /* renamed from: d  reason: collision with root package name */
        public final long f9303d;

        private i(x1 x1Var, boolean z4, long j8, long j9) {
            this.f9300a = x1Var;
            this.f9301b = z4;
            this.f9302c = j8;
            this.f9303d = j9;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class j<T extends Exception> {

        /* renamed from: a  reason: collision with root package name */
        private final long f9304a;

        /* renamed from: b  reason: collision with root package name */
        private T f9305b;

        /* renamed from: c  reason: collision with root package name */
        private long f9306c;

        public j(long j8) {
            this.f9304a = j8;
        }

        public void a() {
            this.f9305b = null;
        }

        public void b(T t8) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (this.f9305b == null) {
                this.f9305b = t8;
                this.f9306c = this.f9304a + elapsedRealtime;
            }
            if (elapsedRealtime >= this.f9306c) {
                T t9 = this.f9305b;
                if (t9 != t8) {
                    t9.addSuppressed(t8);
                }
                T t10 = this.f9305b;
                a();
                throw t10;
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private final class k implements d.a {
        private k() {
        }

        @Override // com.google.android.exoplayer2.audio.d.a
        public void a(int i8, long j8) {
            if (DefaultAudioSink.this.f9271s != null) {
                DefaultAudioSink.this.f9271s.e(i8, j8, SystemClock.elapsedRealtime() - DefaultAudioSink.this.f9253b0);
            }
        }

        @Override // com.google.android.exoplayer2.audio.d.a
        public void b(long j8) {
            p.i("DefaultAudioSink", "Ignoring impossibly large audio latency: " + j8);
        }

        @Override // com.google.android.exoplayer2.audio.d.a
        public void c(long j8) {
            if (DefaultAudioSink.this.f9271s != null) {
                DefaultAudioSink.this.f9271s.c(j8);
            }
        }

        @Override // com.google.android.exoplayer2.audio.d.a
        public void d(long j8, long j9, long j10, long j11) {
            String str = "Spurious audio timestamp (frame position mismatch): " + j8 + ", " + j9 + ", " + j10 + ", " + j11 + ", " + DefaultAudioSink.this.T() + ", " + DefaultAudioSink.this.U();
            if (DefaultAudioSink.f9246e0) {
                throw new InvalidAudioTrackTimestampException(str);
            }
            p.i("DefaultAudioSink", str);
        }

        @Override // com.google.android.exoplayer2.audio.d.a
        public void e(long j8, long j9, long j10, long j11) {
            String str = "Spurious audio timestamp (system clock mismatch): " + j8 + ", " + j9 + ", " + j10 + ", " + j11 + ", " + DefaultAudioSink.this.T() + ", " + DefaultAudioSink.this.U();
            if (DefaultAudioSink.f9246e0) {
                throw new InvalidAudioTrackTimestampException(str);
            }
            p.i("DefaultAudioSink", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class l {

        /* renamed from: a  reason: collision with root package name */
        private final Handler f9308a = new Handler(Looper.myLooper());

        /* renamed from: b  reason: collision with root package name */
        private final AudioTrack.StreamEventCallback f9309b;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends AudioTrack.StreamEventCallback {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ DefaultAudioSink f9311a;

            a(DefaultAudioSink defaultAudioSink) {
                this.f9311a = defaultAudioSink;
            }

            @Override // android.media.AudioTrack.StreamEventCallback
            public void onDataRequest(AudioTrack audioTrack, int i8) {
                if (audioTrack.equals(DefaultAudioSink.this.f9274v) && DefaultAudioSink.this.f9271s != null && DefaultAudioSink.this.V) {
                    DefaultAudioSink.this.f9271s.g();
                }
            }

            @Override // android.media.AudioTrack.StreamEventCallback
            public void onTearDown(AudioTrack audioTrack) {
                if (audioTrack.equals(DefaultAudioSink.this.f9274v) && DefaultAudioSink.this.f9271s != null && DefaultAudioSink.this.V) {
                    DefaultAudioSink.this.f9271s.g();
                }
            }
        }

        public l() {
            this.f9309b = new a(DefaultAudioSink.this);
        }

        public void a(AudioTrack audioTrack) {
            Handler handler = this.f9308a;
            Objects.requireNonNull(handler);
            audioTrack.registerStreamEventCallback(new s(handler), this.f9309b);
        }

        public void b(AudioTrack audioTrack) {
            audioTrack.unregisterStreamEventCallback(this.f9309b);
            this.f9308a.removeCallbacksAndMessages(null);
        }
    }

    private DefaultAudioSink(f fVar) {
        this.f9250a = fVar.f9281a;
        k4.f fVar2 = fVar.f9282b;
        this.f9252b = fVar2;
        int i8 = l0.f8063a;
        this.f9254c = i8 >= 21 && fVar.f9283c;
        this.f9264k = i8 >= 23 && fVar.f9284d;
        this.f9265l = i8 >= 29 ? fVar.f9285e : 0;
        this.f9269p = fVar.f9286f;
        b6.g gVar = new b6.g(b6.d.f8029a);
        this.f9261h = gVar;
        gVar.e();
        this.f9262i = new com.google.android.exoplayer2.audio.d(new k());
        com.google.android.exoplayer2.audio.f fVar3 = new com.google.android.exoplayer2.audio.f();
        this.f9256d = fVar3;
        n nVar = new n();
        this.f9258e = nVar;
        ArrayList arrayList = new ArrayList();
        Collections.addAll(arrayList, new com.google.android.exoplayer2.audio.j(), fVar3, nVar);
        Collections.addAll(arrayList, fVar2.e());
        this.f9259f = (AudioProcessor[]) arrayList.toArray(new AudioProcessor[0]);
        this.f9260g = new AudioProcessor[]{new com.google.android.exoplayer2.audio.h()};
        this.K = 1.0f;
        this.f9275w = com.google.android.exoplayer2.audio.a.f9313g;
        this.X = 0;
        this.Y = new q(0, 0.0f);
        x1 x1Var = x1.f11264d;
        this.f9277y = new i(x1Var, false, 0L, 0L);
        this.f9278z = x1Var;
        this.S = -1;
        this.L = new AudioProcessor[0];
        this.M = new ByteBuffer[0];
        this.f9263j = new ArrayDeque<>();
        this.f9267n = new j<>(100L);
        this.f9268o = new j<>(100L);
        this.q = fVar.f9287g;
    }

    private void F(long j8) {
        x1 a9 = m0() ? this.f9252b.a(N()) : x1.f11264d;
        boolean d8 = m0() ? this.f9252b.d(S()) : false;
        this.f9263j.add(new i(a9, d8, Math.max(0L, j8), this.f9273u.h(U())));
        l0();
        AudioSink.a aVar = this.f9271s;
        if (aVar != null) {
            aVar.a(d8);
        }
    }

    private long G(long j8) {
        while (!this.f9263j.isEmpty() && j8 >= this.f9263j.getFirst().f9303d) {
            this.f9277y = this.f9263j.remove();
        }
        i iVar = this.f9277y;
        long j9 = j8 - iVar.f9303d;
        if (iVar.f9300a.equals(x1.f11264d)) {
            return this.f9277y.f9302c + j9;
        }
        if (this.f9263j.isEmpty()) {
            return this.f9277y.f9302c + this.f9252b.b(j9);
        }
        i first = this.f9263j.getFirst();
        return first.f9302c - l0.Z(first.f9303d - j8, this.f9277y.f9300a.f11268a);
    }

    private long H(long j8) {
        return j8 + this.f9273u.h(this.f9252b.c());
    }

    private AudioTrack I(g gVar) {
        try {
            AudioTrack a9 = gVar.a(this.f9251a0, this.f9275w, this.X);
            k.a aVar = this.q;
            if (aVar != null) {
                aVar.H(Y(a9));
            }
            return a9;
        } catch (AudioSink.InitializationException e8) {
            AudioSink.a aVar2 = this.f9271s;
            if (aVar2 != null) {
                aVar2.b(e8);
            }
            throw e8;
        }
    }

    private AudioTrack J() {
        try {
            return I((g) b6.a.e(this.f9273u));
        } catch (AudioSink.InitializationException e8) {
            g gVar = this.f9273u;
            if (gVar.f9295h > 1000000) {
                g c9 = gVar.c(1000000);
                try {
                    AudioTrack I = I(c9);
                    this.f9273u = c9;
                    return I;
                } catch (AudioSink.InitializationException e9) {
                    e8.addSuppressed(e9);
                    a0();
                    throw e8;
                }
            }
            a0();
            throw e8;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0018  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:15:0x0029 -> B:5:0x0009). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean K() {
        /*
            r9 = this;
            int r0 = r9.S
            r1 = -1
            r2 = 1
            r3 = 0
            if (r0 != r1) goto Lb
            r9.S = r3
        L9:
            r0 = r2
            goto Lc
        Lb:
            r0 = r3
        Lc:
            int r4 = r9.S
            com.google.android.exoplayer2.audio.AudioProcessor[] r5 = r9.L
            int r6 = r5.length
            r7 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
            if (r4 >= r6) goto L2f
            r4 = r5[r4]
            if (r0 == 0) goto L1f
            r4.g()
        L1f:
            r9.c0(r7)
            boolean r0 = r4.b()
            if (r0 != 0) goto L29
            return r3
        L29:
            int r0 = r9.S
            int r0 = r0 + r2
            r9.S = r0
            goto L9
        L2f:
            java.nio.ByteBuffer r0 = r9.P
            if (r0 == 0) goto L3b
            r9.p0(r0, r7)
            java.nio.ByteBuffer r0 = r9.P
            if (r0 == 0) goto L3b
            return r3
        L3b:
            r9.S = r1
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.audio.DefaultAudioSink.K():boolean");
    }

    private void L() {
        int i8 = 0;
        while (true) {
            AudioProcessor[] audioProcessorArr = this.L;
            if (i8 >= audioProcessorArr.length) {
                return;
            }
            AudioProcessor audioProcessor = audioProcessorArr[i8];
            audioProcessor.flush();
            this.M[i8] = audioProcessor.d();
            i8++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AudioFormat M(int i8, int i9, int i10) {
        return new AudioFormat.Builder().setSampleRate(i8).setChannelMask(i9).setEncoding(i10).build();
    }

    private x1 N() {
        return Q().f9300a;
    }

    private static int O(int i8, int i9, int i10) {
        int minBufferSize = AudioTrack.getMinBufferSize(i8, i9, i10);
        b6.a.f(minBufferSize != -2);
        return minBufferSize;
    }

    private static int P(int i8, ByteBuffer byteBuffer) {
        switch (i8) {
            case 5:
            case 6:
            case 18:
                return k4.b.e(byteBuffer);
            case 7:
            case 8:
                return t.e(byteBuffer);
            case 9:
                int m8 = u.m(l0.I(byteBuffer, byteBuffer.position()));
                if (m8 != -1) {
                    return m8;
                }
                throw new IllegalArgumentException();
            case 10:
                return RecognitionOptions.UPC_E;
            case 11:
            case 12:
                return RecognitionOptions.PDF417;
            case 13:
            case 19:
            default:
                throw new IllegalStateException("Unexpected audio encoding: " + i8);
            case 14:
                int b9 = k4.b.b(byteBuffer);
                if (b9 == -1) {
                    return 0;
                }
                return k4.b.i(byteBuffer, b9) * 16;
            case 15:
                return RecognitionOptions.UPC_A;
            case 16:
                return RecognitionOptions.UPC_E;
            case 17:
                return k4.c.c(byteBuffer);
            case 20:
                return v.g(byteBuffer);
        }
    }

    private i Q() {
        i iVar = this.f9276x;
        return iVar != null ? iVar : !this.f9263j.isEmpty() ? this.f9263j.getLast() : this.f9277y;
    }

    @SuppressLint({"InlinedApi"})
    private int R(AudioFormat audioFormat, AudioAttributes audioAttributes) {
        int i8 = l0.f8063a;
        if (i8 >= 31) {
            return AudioManager.getPlaybackOffloadSupport(audioFormat, audioAttributes);
        }
        if (AudioManager.isOffloadedPlaybackSupported(audioFormat, audioAttributes)) {
            return (i8 == 30 && l0.f8066d.startsWith("Pixel")) ? 2 : 1;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long T() {
        g gVar = this.f9273u;
        return gVar.f9290c == 0 ? this.C / gVar.f9289b : this.D;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long U() {
        g gVar = this.f9273u;
        return gVar.f9290c == 0 ? this.E / gVar.f9291d : this.F;
    }

    private boolean V() {
        t1 t1Var;
        if (this.f9261h.d()) {
            AudioTrack J = J();
            this.f9274v = J;
            if (Y(J)) {
                d0(this.f9274v);
                if (this.f9265l != 3) {
                    AudioTrack audioTrack = this.f9274v;
                    w0 w0Var = this.f9273u.f9288a;
                    audioTrack.setOffloadDelayPadding(w0Var.K, w0Var.L);
                }
            }
            int i8 = l0.f8063a;
            if (i8 >= 31 && (t1Var = this.f9270r) != null) {
                c.a(this.f9274v, t1Var);
            }
            this.X = this.f9274v.getAudioSessionId();
            com.google.android.exoplayer2.audio.d dVar = this.f9262i;
            AudioTrack audioTrack2 = this.f9274v;
            g gVar = this.f9273u;
            dVar.s(audioTrack2, gVar.f9290c == 2, gVar.f9294g, gVar.f9291d, gVar.f9295h);
            i0();
            int i9 = this.Y.f21009a;
            if (i9 != 0) {
                this.f9274v.attachAuxEffect(i9);
                this.f9274v.setAuxEffectSendLevel(this.Y.f21010b);
            }
            d dVar2 = this.Z;
            if (dVar2 != null && i8 >= 23) {
                b.a(this.f9274v, dVar2);
            }
            this.I = true;
            return true;
        }
        return false;
    }

    private static boolean W(int i8) {
        return (l0.f8063a >= 24 && i8 == -6) || i8 == -32;
    }

    private boolean X() {
        return this.f9274v != null;
    }

    private static boolean Y(AudioTrack audioTrack) {
        return l0.f8063a >= 29 && audioTrack.isOffloadedPlayback();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void Z(AudioTrack audioTrack, b6.g gVar) {
        try {
            audioTrack.flush();
            audioTrack.release();
            gVar.e();
            synchronized (f9247f0) {
                int i8 = f9249h0 - 1;
                f9249h0 = i8;
                if (i8 == 0) {
                    f9248g0.shutdown();
                    f9248g0 = null;
                }
            }
        } catch (Throwable th) {
            gVar.e();
            synchronized (f9247f0) {
                int i9 = f9249h0 - 1;
                f9249h0 = i9;
                if (i9 == 0) {
                    f9248g0.shutdown();
                    f9248g0 = null;
                }
                throw th;
            }
        }
    }

    private void a0() {
        if (this.f9273u.l()) {
            this.f9255c0 = true;
        }
    }

    private void b0() {
        if (this.U) {
            return;
        }
        this.U = true;
        this.f9262i.g(U());
        this.f9274v.stop();
        this.B = 0;
    }

    private void c0(long j8) {
        ByteBuffer byteBuffer;
        int length = this.L.length;
        int i8 = length;
        while (i8 >= 0) {
            if (i8 > 0) {
                byteBuffer = this.M[i8 - 1];
            } else {
                byteBuffer = this.N;
                if (byteBuffer == null) {
                    byteBuffer = AudioProcessor.f9231a;
                }
            }
            if (i8 == length) {
                p0(byteBuffer, j8);
            } else {
                AudioProcessor audioProcessor = this.L[i8];
                if (i8 > this.S) {
                    audioProcessor.e(byteBuffer);
                }
                ByteBuffer d8 = audioProcessor.d();
                this.M[i8] = d8;
                if (d8.hasRemaining()) {
                    i8++;
                }
            }
            if (byteBuffer.hasRemaining()) {
                return;
            }
            i8--;
        }
    }

    private void d0(AudioTrack audioTrack) {
        if (this.f9266m == null) {
            this.f9266m = new l();
        }
        this.f9266m.a(audioTrack);
    }

    private static void e0(AudioTrack audioTrack, b6.g gVar) {
        gVar.c();
        synchronized (f9247f0) {
            if (f9248g0 == null) {
                f9248g0 = l0.D0("ExoPlayer:AudioTrackReleaseThread");
            }
            f9249h0++;
            f9248g0.execute(new r(audioTrack, gVar));
        }
    }

    private void f0() {
        this.C = 0L;
        this.D = 0L;
        this.E = 0L;
        this.F = 0L;
        this.f9257d0 = false;
        this.G = 0;
        this.f9277y = new i(N(), S(), 0L, 0L);
        this.J = 0L;
        this.f9276x = null;
        this.f9263j.clear();
        this.N = null;
        this.O = 0;
        this.P = null;
        this.U = false;
        this.T = false;
        this.S = -1;
        this.A = null;
        this.B = 0;
        this.f9258e.n();
        L();
    }

    private void g0(x1 x1Var, boolean z4) {
        i Q = Q();
        if (x1Var.equals(Q.f9300a) && z4 == Q.f9301b) {
            return;
        }
        i iVar = new i(x1Var, z4, -9223372036854775807L, -9223372036854775807L);
        if (X()) {
            this.f9276x = iVar;
        } else {
            this.f9277y = iVar;
        }
    }

    private void h0(x1 x1Var) {
        if (X()) {
            try {
                this.f9274v.setPlaybackParams(new PlaybackParams().allowDefaults().setSpeed(x1Var.f11268a).setPitch(x1Var.f11269b).setAudioFallbackMode(2));
            } catch (IllegalArgumentException e8) {
                p.j("DefaultAudioSink", "Failed to set playback params", e8);
            }
            x1Var = new x1(this.f9274v.getPlaybackParams().getSpeed(), this.f9274v.getPlaybackParams().getPitch());
            this.f9262i.t(x1Var.f11268a);
        }
        this.f9278z = x1Var;
    }

    private void i0() {
        if (X()) {
            if (l0.f8063a >= 21) {
                j0(this.f9274v, this.K);
            } else {
                k0(this.f9274v, this.K);
            }
        }
    }

    private static void j0(AudioTrack audioTrack, float f5) {
        audioTrack.setVolume(f5);
    }

    private static void k0(AudioTrack audioTrack, float f5) {
        audioTrack.setStereoVolume(f5, f5);
    }

    private void l0() {
        AudioProcessor[] audioProcessorArr = this.f9273u.f9296i;
        ArrayList arrayList = new ArrayList();
        for (AudioProcessor audioProcessor : audioProcessorArr) {
            if (audioProcessor.c()) {
                arrayList.add(audioProcessor);
            } else {
                audioProcessor.flush();
            }
        }
        int size = arrayList.size();
        this.L = (AudioProcessor[]) arrayList.toArray(new AudioProcessor[size]);
        this.M = new ByteBuffer[size];
        L();
    }

    private boolean m0() {
        return (this.f9251a0 || !"audio/raw".equals(this.f9273u.f9288a.f11207m) || n0(this.f9273u.f9288a.H)) ? false : true;
    }

    private boolean n0(int i8) {
        return this.f9254c && l0.t0(i8);
    }

    private boolean o0(w0 w0Var, com.google.android.exoplayer2.audio.a aVar) {
        int f5;
        int G;
        int R;
        if (l0.f8063a < 29 || this.f9265l == 0 || (f5 = b6.t.f((String) b6.a.e(w0Var.f11207m), w0Var.f11204j)) == 0 || (G = l0.G(w0Var.F)) == 0 || (R = R(M(w0Var.G, G, f5), aVar.b().f9326a)) == 0) {
            return false;
        }
        if (R == 1) {
            return ((w0Var.K != 0 || w0Var.L != 0) && (this.f9265l == 1)) ? false : true;
        } else if (R == 2) {
            return true;
        } else {
            throw new IllegalStateException();
        }
    }

    private void p0(ByteBuffer byteBuffer, long j8) {
        int q02;
        AudioSink.a aVar;
        if (byteBuffer.hasRemaining()) {
            ByteBuffer byteBuffer2 = this.P;
            if (byteBuffer2 != null) {
                b6.a.a(byteBuffer2 == byteBuffer);
            } else {
                this.P = byteBuffer;
                if (l0.f8063a < 21) {
                    int remaining = byteBuffer.remaining();
                    byte[] bArr = this.Q;
                    if (bArr == null || bArr.length < remaining) {
                        this.Q = new byte[remaining];
                    }
                    int position = byteBuffer.position();
                    byteBuffer.get(this.Q, 0, remaining);
                    byteBuffer.position(position);
                    this.R = 0;
                }
            }
            int remaining2 = byteBuffer.remaining();
            if (l0.f8063a < 21) {
                int c9 = this.f9262i.c(this.E);
                if (c9 > 0) {
                    q02 = this.f9274v.write(this.Q, this.R, Math.min(remaining2, c9));
                    if (q02 > 0) {
                        this.R += q02;
                        byteBuffer.position(byteBuffer.position() + q02);
                    }
                } else {
                    q02 = 0;
                }
            } else if (this.f9251a0) {
                b6.a.f(j8 != -9223372036854775807L);
                q02 = r0(this.f9274v, byteBuffer, remaining2, j8);
            } else {
                q02 = q0(this.f9274v, byteBuffer, remaining2);
            }
            this.f9253b0 = SystemClock.elapsedRealtime();
            if (q02 < 0) {
                if (!W(q02) || this.F <= 0) {
                    r2 = false;
                }
                AudioSink.WriteException writeException = new AudioSink.WriteException(q02, this.f9273u.f9288a, r2);
                AudioSink.a aVar2 = this.f9271s;
                if (aVar2 != null) {
                    aVar2.b(writeException);
                }
                if (writeException.f9244b) {
                    throw writeException;
                }
                this.f9268o.b(writeException);
                return;
            }
            this.f9268o.a();
            if (Y(this.f9274v)) {
                if (this.F > 0) {
                    this.f9257d0 = false;
                }
                if (this.V && (aVar = this.f9271s) != null && q02 < remaining2 && !this.f9257d0) {
                    aVar.d();
                }
            }
            int i8 = this.f9273u.f9290c;
            if (i8 == 0) {
                this.E += q02;
            }
            if (q02 == remaining2) {
                if (i8 != 0) {
                    b6.a.f(byteBuffer == this.N);
                    this.F += this.G * this.O;
                }
                this.P = null;
            }
        }
    }

    private static int q0(AudioTrack audioTrack, ByteBuffer byteBuffer, int i8) {
        return audioTrack.write(byteBuffer, i8, 1);
    }

    private int r0(AudioTrack audioTrack, ByteBuffer byteBuffer, int i8, long j8) {
        if (l0.f8063a >= 26) {
            return audioTrack.write(byteBuffer, i8, 1, j8 * 1000);
        }
        if (this.A == null) {
            ByteBuffer allocate = ByteBuffer.allocate(16);
            this.A = allocate;
            allocate.order(ByteOrder.BIG_ENDIAN);
            this.A.putInt(1431633921);
        }
        if (this.B == 0) {
            this.A.putInt(4, i8);
            this.A.putLong(8, j8 * 1000);
            this.A.position(0);
            this.B = i8;
        }
        int remaining = this.A.remaining();
        if (remaining > 0) {
            int write = audioTrack.write(this.A, remaining, 1);
            if (write < 0) {
                this.B = 0;
                return write;
            } else if (write < remaining) {
                return 0;
            }
        }
        int q02 = q0(audioTrack, byteBuffer, i8);
        if (q02 < 0) {
            this.B = 0;
            return q02;
        }
        this.B -= q02;
        return q02;
    }

    public boolean S() {
        return Q().f9301b;
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public boolean a(w0 w0Var) {
        return u(w0Var) != 0;
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public boolean b() {
        return !X() || (this.T && !i());
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public x1 c() {
        return this.f9264k ? this.f9278z : N();
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void d(x1 x1Var) {
        x1 x1Var2 = new x1(l0.p(x1Var.f11268a, 0.1f, 8.0f), l0.p(x1Var.f11269b, 0.1f, 8.0f));
        if (!this.f9264k || l0.f8063a < 23) {
            g0(x1Var2, S());
        } else {
            h0(x1Var2);
        }
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void e(float f5) {
        if (this.K != f5) {
            this.K = f5;
            i0();
        }
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void f(boolean z4) {
        g0(N(), z4);
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void flush() {
        if (X()) {
            f0();
            if (this.f9262i.i()) {
                this.f9274v.pause();
            }
            if (Y(this.f9274v)) {
                ((l) b6.a.e(this.f9266m)).b(this.f9274v);
            }
            if (l0.f8063a < 21 && !this.W) {
                this.X = 0;
            }
            g gVar = this.f9272t;
            if (gVar != null) {
                this.f9273u = gVar;
                this.f9272t = null;
            }
            this.f9262i.q();
            e0(this.f9274v, this.f9261h);
            this.f9274v = null;
        }
        this.f9268o.a();
        this.f9267n.a();
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void g(AudioDeviceInfo audioDeviceInfo) {
        d dVar = audioDeviceInfo == null ? null : new d(audioDeviceInfo);
        this.Z = dVar;
        AudioTrack audioTrack = this.f9274v;
        if (audioTrack != null) {
            b.a(audioTrack, dVar);
        }
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void h() {
        if (!this.T && X() && K()) {
            b0();
            this.T = true;
        }
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public boolean i() {
        return X() && this.f9262i.h(U());
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void j(int i8) {
        if (this.X != i8) {
            this.X = i8;
            this.W = i8 != 0;
            flush();
        }
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void k(q qVar) {
        if (this.Y.equals(qVar)) {
            return;
        }
        int i8 = qVar.f21009a;
        float f5 = qVar.f21010b;
        AudioTrack audioTrack = this.f9274v;
        if (audioTrack != null) {
            if (this.Y.f21009a != i8) {
                audioTrack.attachAuxEffect(i8);
            }
            if (i8 != 0) {
                this.f9274v.setAuxEffectSendLevel(f5);
            }
        }
        this.Y = qVar;
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public long l(boolean z4) {
        if (!X() || this.I) {
            return Long.MIN_VALUE;
        }
        return H(G(Math.min(this.f9262i.d(z4), this.f9273u.h(U()))));
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void m() {
        if (this.f9251a0) {
            this.f9251a0 = false;
            flush();
        }
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void n(com.google.android.exoplayer2.audio.a aVar) {
        if (this.f9275w.equals(aVar)) {
            return;
        }
        this.f9275w = aVar;
        if (this.f9251a0) {
            return;
        }
        flush();
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void p() {
        this.H = true;
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void pause() {
        this.V = false;
        if (X() && this.f9262i.p()) {
            this.f9274v.pause();
        }
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void play() {
        this.V = true;
        if (X()) {
            this.f9262i.u();
            this.f9274v.play();
        }
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void q(t1 t1Var) {
        this.f9270r = t1Var;
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void r() {
        b6.a.f(l0.f8063a >= 21);
        b6.a.f(this.W);
        if (this.f9251a0) {
            return;
        }
        this.f9251a0 = true;
        flush();
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void reset() {
        flush();
        for (AudioProcessor audioProcessor : this.f9259f) {
            audioProcessor.reset();
        }
        for (AudioProcessor audioProcessor2 : this.f9260g) {
            audioProcessor2.reset();
        }
        this.V = false;
        this.f9255c0 = false;
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public boolean s(ByteBuffer byteBuffer, long j8, int i8) {
        ByteBuffer byteBuffer2 = this.N;
        b6.a.a(byteBuffer2 == null || byteBuffer == byteBuffer2);
        if (this.f9272t != null) {
            if (!K()) {
                return false;
            }
            if (this.f9272t.b(this.f9273u)) {
                this.f9273u = this.f9272t;
                this.f9272t = null;
                if (Y(this.f9274v) && this.f9265l != 3) {
                    if (this.f9274v.getPlayState() == 3) {
                        this.f9274v.setOffloadEndOfStream();
                    }
                    AudioTrack audioTrack = this.f9274v;
                    w0 w0Var = this.f9273u.f9288a;
                    audioTrack.setOffloadDelayPadding(w0Var.K, w0Var.L);
                    this.f9257d0 = true;
                }
            } else {
                b0();
                if (i()) {
                    return false;
                }
                flush();
            }
            F(j8);
        }
        if (!X()) {
            try {
                if (!V()) {
                    return false;
                }
            } catch (AudioSink.InitializationException e8) {
                if (e8.f9239b) {
                    throw e8;
                }
                this.f9267n.b(e8);
                return false;
            }
        }
        this.f9267n.a();
        if (this.I) {
            this.J = Math.max(0L, j8);
            this.H = false;
            this.I = false;
            if (this.f9264k && l0.f8063a >= 23) {
                h0(this.f9278z);
            }
            F(j8);
            if (this.V) {
                play();
            }
        }
        if (this.f9262i.k(U())) {
            if (this.N == null) {
                b6.a.a(byteBuffer.order() == ByteOrder.LITTLE_ENDIAN);
                if (!byteBuffer.hasRemaining()) {
                    return true;
                }
                g gVar = this.f9273u;
                if (gVar.f9290c != 0 && this.G == 0) {
                    int P = P(gVar.f9294g, byteBuffer);
                    this.G = P;
                    if (P == 0) {
                        return true;
                    }
                }
                if (this.f9276x != null) {
                    if (!K()) {
                        return false;
                    }
                    F(j8);
                    this.f9276x = null;
                }
                long k8 = this.J + this.f9273u.k(T() - this.f9258e.m());
                if (!this.H && Math.abs(k8 - j8) > 200000) {
                    AudioSink.a aVar = this.f9271s;
                    if (aVar != null) {
                        aVar.b(new AudioSink.UnexpectedDiscontinuityException(j8, k8));
                    }
                    this.H = true;
                }
                if (this.H) {
                    if (!K()) {
                        return false;
                    }
                    long j9 = j8 - k8;
                    this.J += j9;
                    this.H = false;
                    F(j8);
                    AudioSink.a aVar2 = this.f9271s;
                    if (aVar2 != null && j9 != 0) {
                        aVar2.f();
                    }
                }
                if (this.f9273u.f9290c == 0) {
                    this.C += byteBuffer.remaining();
                } else {
                    this.D += this.G * i8;
                }
                this.N = byteBuffer;
                this.O = i8;
            }
            c0(j8);
            if (!this.N.hasRemaining()) {
                this.N = null;
                this.O = 0;
                return true;
            } else if (this.f9262i.j(U())) {
                p.i("DefaultAudioSink", "Resetting stalled audio track");
                flush();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void t(AudioSink.a aVar) {
        this.f9271s = aVar;
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public int u(w0 w0Var) {
        if (!"audio/raw".equals(w0Var.f11207m)) {
            return ((this.f9255c0 || !o0(w0Var, this.f9275w)) && !this.f9250a.h(w0Var)) ? 0 : 2;
        } else if (l0.u0(w0Var.H)) {
            int i8 = w0Var.H;
            return (i8 == 2 || (this.f9254c && i8 == 4)) ? 2 : 1;
        } else {
            p.i("DefaultAudioSink", "Invalid PCM encoding: " + w0Var.H);
            return 0;
        }
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void v(w0 w0Var, int i8, int[] iArr) {
        AudioProcessor[] audioProcessorArr;
        int i9;
        int i10;
        int i11;
        int i12;
        int intValue;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int a9;
        int[] iArr2;
        if ("audio/raw".equals(w0Var.f11207m)) {
            b6.a.a(l0.u0(w0Var.H));
            i9 = l0.d0(w0Var.H, w0Var.F);
            AudioProcessor[] audioProcessorArr2 = n0(w0Var.H) ? this.f9260g : this.f9259f;
            this.f9258e.o(w0Var.K, w0Var.L);
            if (l0.f8063a < 21 && w0Var.F == 8 && iArr == null) {
                iArr2 = new int[6];
                for (int i18 = 0; i18 < 6; i18++) {
                    iArr2[i18] = i18;
                }
            } else {
                iArr2 = iArr;
            }
            this.f9256d.m(iArr2);
            AudioProcessor.a aVar = new AudioProcessor.a(w0Var.G, w0Var.F, w0Var.H);
            for (AudioProcessor audioProcessor : audioProcessorArr2) {
                try {
                    AudioProcessor.a f5 = audioProcessor.f(aVar);
                    if (audioProcessor.c()) {
                        aVar = f5;
                    }
                } catch (AudioProcessor.UnhandledAudioFormatException e8) {
                    throw new AudioSink.ConfigurationException(e8, w0Var);
                }
            }
            int i19 = aVar.f9235c;
            int i20 = aVar.f9233a;
            int G = l0.G(aVar.f9234b);
            i12 = 0;
            audioProcessorArr = audioProcessorArr2;
            i10 = l0.d0(i19, aVar.f9234b);
            i13 = i19;
            i11 = i20;
            intValue = G;
        } else {
            AudioProcessor[] audioProcessorArr3 = new AudioProcessor[0];
            int i21 = w0Var.G;
            if (o0(w0Var, this.f9275w)) {
                audioProcessorArr = audioProcessorArr3;
                i9 = -1;
                i10 = -1;
                i12 = 1;
                i11 = i21;
                i13 = b6.t.f((String) b6.a.e(w0Var.f11207m), w0Var.f11204j);
                intValue = l0.G(w0Var.F);
            } else {
                Pair<Integer, Integer> f8 = this.f9250a.f(w0Var);
                if (f8 == null) {
                    throw new AudioSink.ConfigurationException("Unable to configure passthrough for: " + w0Var, w0Var);
                }
                int intValue2 = ((Integer) f8.first).intValue();
                audioProcessorArr = audioProcessorArr3;
                i9 = -1;
                i10 = -1;
                i11 = i21;
                i12 = 2;
                intValue = ((Integer) f8.second).intValue();
                i13 = intValue2;
            }
        }
        if (i13 == 0) {
            throw new AudioSink.ConfigurationException("Invalid output encoding (mode=" + i12 + ") for: " + w0Var, w0Var);
        } else if (intValue == 0) {
            throw new AudioSink.ConfigurationException("Invalid output channel config (mode=" + i12 + ") for: " + w0Var, w0Var);
        } else {
            if (i8 != 0) {
                a9 = i8;
                i14 = i13;
                i15 = intValue;
                i16 = i10;
                i17 = i11;
            } else {
                i14 = i13;
                i15 = intValue;
                i16 = i10;
                i17 = i11;
                a9 = this.f9269p.a(O(i11, intValue, i13), i13, i12, i10 != -1 ? i10 : 1, i11, w0Var.f11203h, this.f9264k ? 8.0d : 1.0d);
            }
            this.f9255c0 = false;
            g gVar = new g(w0Var, i9, i12, i16, i17, i15, i14, a9, audioProcessorArr);
            if (X()) {
                this.f9272t = gVar;
            } else {
                this.f9273u = gVar;
            }
        }
    }

    @Override // com.google.android.exoplayer2.audio.AudioSink
    public void w() {
        if (l0.f8063a < 25) {
            flush();
            return;
        }
        this.f9268o.a();
        this.f9267n.a();
        if (X()) {
            f0();
            if (this.f9262i.i()) {
                this.f9274v.pause();
            }
            this.f9274v.flush();
            this.f9262i.q();
            com.google.android.exoplayer2.audio.d dVar = this.f9262i;
            AudioTrack audioTrack = this.f9274v;
            g gVar = this.f9273u;
            dVar.s(audioTrack, gVar.f9290c == 2, gVar.f9294g, gVar.f9291d, gVar.f9295h);
            this.I = true;
        }
    }
}
