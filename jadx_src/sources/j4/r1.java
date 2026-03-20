package j4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.DeniedByServerException;
import android.media.MediaCodec;
import android.media.MediaDrm;
import android.media.MediaDrmResetException;
import android.media.NotProvisionedException;
import android.media.metrics.LogSessionId;
import android.media.metrics.MediaMetricsManager;
import android.media.metrics.NetworkEvent;
import android.media.metrics.PlaybackErrorEvent;
import android.media.metrics.PlaybackMetrics;
import android.media.metrics.PlaybackSession;
import android.media.metrics.PlaybackStateEvent;
import android.media.metrics.TrackChangeEvent;
import android.os.SystemClock;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.util.Pair;
import b6.l0;
import b6.w;
import c6.x;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.i2;
import com.google.android.exoplayer2.mediacodec.MediaCodecDecoderException;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource$HttpDataSourceException;
import com.google.android.exoplayer2.upstream.HttpDataSource$InvalidContentTypeException;
import com.google.android.exoplayer2.upstream.HttpDataSource$InvalidResponseCodeException;
import com.google.android.exoplayer2.upstream.UdpDataSource;
import com.google.android.exoplayer2.w0;
import com.google.android.exoplayer2.y1;
import com.google.android.exoplayer2.z0;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.d3;
import h5.h;
import h5.i;
import j4.b;
import j4.s1;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.UUID;
import l4.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r1 implements j4.b, s1.a {
    private boolean A;

    /* renamed from: a  reason: collision with root package name */
    private final Context f20680a;

    /* renamed from: b  reason: collision with root package name */
    private final s1 f20681b;

    /* renamed from: c  reason: collision with root package name */
    private final PlaybackSession f20682c;

    /* renamed from: i  reason: collision with root package name */
    private String f20688i;

    /* renamed from: j  reason: collision with root package name */
    private PlaybackMetrics.Builder f20689j;

    /* renamed from: k  reason: collision with root package name */
    private int f20690k;

    /* renamed from: n  reason: collision with root package name */
    private PlaybackException f20693n;

    /* renamed from: o  reason: collision with root package name */
    private b f20694o;

    /* renamed from: p  reason: collision with root package name */
    private b f20695p;
    private b q;

    /* renamed from: r  reason: collision with root package name */
    private w0 f20696r;

    /* renamed from: s  reason: collision with root package name */
    private w0 f20697s;

    /* renamed from: t  reason: collision with root package name */
    private w0 f20698t;

    /* renamed from: u  reason: collision with root package name */
    private boolean f20699u;

    /* renamed from: v  reason: collision with root package name */
    private int f20700v;

    /* renamed from: w  reason: collision with root package name */
    private boolean f20701w;

    /* renamed from: x  reason: collision with root package name */
    private int f20702x;

    /* renamed from: y  reason: collision with root package name */
    private int f20703y;

    /* renamed from: z  reason: collision with root package name */
    private int f20704z;

    /* renamed from: e  reason: collision with root package name */
    private final h2.d f20684e = new h2.d();

    /* renamed from: f  reason: collision with root package name */
    private final h2.b f20685f = new h2.b();

    /* renamed from: h  reason: collision with root package name */
    private final HashMap<String, Long> f20687h = new HashMap<>();

    /* renamed from: g  reason: collision with root package name */
    private final HashMap<String, Long> f20686g = new HashMap<>();

    /* renamed from: d  reason: collision with root package name */
    private final long f20683d = SystemClock.elapsedRealtime();

    /* renamed from: l  reason: collision with root package name */
    private int f20691l = 0;

    /* renamed from: m  reason: collision with root package name */
    private int f20692m = 0;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final int f20705a;

        /* renamed from: b  reason: collision with root package name */
        public final int f20706b;

        public a(int i8, int i9) {
            this.f20705a = i8;
            this.f20706b = i9;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final w0 f20707a;

        /* renamed from: b  reason: collision with root package name */
        public final int f20708b;

        /* renamed from: c  reason: collision with root package name */
        public final String f20709c;

        public b(w0 w0Var, int i8, String str) {
            this.f20707a = w0Var;
            this.f20708b = i8;
            this.f20709c = str;
        }
    }

    private r1(Context context, PlaybackSession playbackSession) {
        this.f20680a = context.getApplicationContext();
        this.f20682c = playbackSession;
        q1 q1Var = new q1();
        this.f20681b = q1Var;
        q1Var.e(this);
    }

    private boolean A0(b bVar) {
        return bVar != null && bVar.f20709c.equals(this.f20681b.a());
    }

    public static r1 B0(Context context) {
        MediaMetricsManager mediaMetricsManager = (MediaMetricsManager) context.getSystemService("media_metrics");
        if (mediaMetricsManager == null) {
            return null;
        }
        return new r1(context, mediaMetricsManager.createPlaybackSession());
    }

    private void C0() {
        PlaybackMetrics.Builder builder = this.f20689j;
        if (builder != null && this.A) {
            builder.setAudioUnderrunCount(this.f20704z);
            this.f20689j.setVideoFramesDropped(this.f20702x);
            this.f20689j.setVideoFramesPlayed(this.f20703y);
            Long l8 = this.f20686g.get(this.f20688i);
            this.f20689j.setNetworkTransferDurationMillis(l8 == null ? 0L : l8.longValue());
            Long l9 = this.f20687h.get(this.f20688i);
            this.f20689j.setNetworkBytesRead(l9 == null ? 0L : l9.longValue());
            this.f20689j.setStreamSource((l9 == null || l9.longValue() <= 0) ? 0 : 1);
            this.f20682c.reportPlaybackMetrics(this.f20689j.build());
        }
        this.f20689j = null;
        this.f20688i = null;
        this.f20704z = 0;
        this.f20702x = 0;
        this.f20703y = 0;
        this.f20696r = null;
        this.f20697s = null;
        this.f20698t = null;
        this.A = false;
    }

    @SuppressLint({"SwitchIntDef"})
    private static int D0(int i8) {
        switch (l0.U(i8)) {
            case 6002:
                return 24;
            case 6003:
                return 28;
            case 6004:
                return 25;
            case 6005:
                return 26;
            default:
                return 27;
        }
    }

    private static DrmInitData E0(ImmutableList<i2.a> immutableList) {
        DrmInitData drmInitData;
        d3<i2.a> it = immutableList.iterator();
        while (it.hasNext()) {
            i2.a next = it.next();
            for (int i8 = 0; i8 < next.f9805a; i8++) {
                if (next.f(i8) && (drmInitData = next.c(i8).q) != null) {
                    return drmInitData;
                }
            }
        }
        return null;
    }

    private static int F0(DrmInitData drmInitData) {
        for (int i8 = 0; i8 < drmInitData.f9595d; i8++) {
            UUID uuid = drmInitData.e(i8).f9597b;
            if (uuid.equals(i4.b.f20468d)) {
                return 3;
            }
            if (uuid.equals(i4.b.f20469e)) {
                return 2;
            }
            if (uuid.equals(i4.b.f20467c)) {
                return 6;
            }
        }
        return 1;
    }

    private static a G0(PlaybackException playbackException, Context context, boolean z4) {
        int i8;
        boolean z8;
        if (playbackException.f9149a == 1001) {
            return new a(20, 0);
        }
        if (playbackException instanceof ExoPlaybackException) {
            ExoPlaybackException exoPlaybackException = (ExoPlaybackException) playbackException;
            z8 = exoPlaybackException.f9131j == 1;
            i8 = exoPlaybackException.f9135n;
        } else {
            i8 = 0;
            z8 = false;
        }
        Throwable th = (Throwable) b6.a.e(playbackException.getCause());
        if (!(th instanceof IOException)) {
            if (z8 && (i8 == 0 || i8 == 1)) {
                return new a(35, 0);
            }
            if (z8 && i8 == 3) {
                return new a(15, 0);
            }
            if (z8 && i8 == 2) {
                return new a(23, 0);
            }
            if (th instanceof MediaCodecRenderer.DecoderInitializationException) {
                return new a(13, l0.V(((MediaCodecRenderer.DecoderInitializationException) th).f9956d));
            }
            if (th instanceof MediaCodecDecoderException) {
                return new a(14, l0.V(((MediaCodecDecoderException) th).f9920b));
            }
            if (th instanceof OutOfMemoryError) {
                return new a(14, 0);
            }
            if (th instanceof AudioSink.InitializationException) {
                return new a(17, ((AudioSink.InitializationException) th).f9238a);
            }
            if (th instanceof AudioSink.WriteException) {
                return new a(18, ((AudioSink.WriteException) th).f9243a);
            }
            if (l0.f8063a < 16 || !(th instanceof MediaCodec.CryptoException)) {
                return new a(22, 0);
            }
            int errorCode = ((MediaCodec.CryptoException) th).getErrorCode();
            return new a(D0(errorCode), errorCode);
        } else if (th instanceof HttpDataSource$InvalidResponseCodeException) {
            return new a(5, ((HttpDataSource$InvalidResponseCodeException) th).f10902d);
        } else {
            if ((th instanceof HttpDataSource$InvalidContentTypeException) || (th instanceof ParserException)) {
                return new a(z4 ? 10 : 11, 0);
            } else if ((th instanceof HttpDataSource$HttpDataSourceException) || (th instanceof UdpDataSource.UdpDataSourceException)) {
                if (w.d(context).f() == 1) {
                    return new a(3, 0);
                }
                Throwable cause = th.getCause();
                return cause instanceof UnknownHostException ? new a(6, 0) : cause instanceof SocketTimeoutException ? new a(7, 0) : ((th instanceof HttpDataSource$HttpDataSourceException) && ((HttpDataSource$HttpDataSourceException) th).f10900c == 1) ? new a(4, 0) : new a(8, 0);
            } else if (playbackException.f9149a == 1002) {
                return new a(21, 0);
            } else {
                if (!(th instanceof DrmSession.DrmSessionException)) {
                    if ((th instanceof FileDataSource.FileDataSourceException) && (th.getCause() instanceof FileNotFoundException)) {
                        Throwable cause2 = ((Throwable) b6.a.e(th.getCause())).getCause();
                        return (l0.f8063a >= 21 && (cause2 instanceof ErrnoException) && ((ErrnoException) cause2).errno == OsConstants.EACCES) ? new a(32, 0) : new a(31, 0);
                    }
                    return new a(9, 0);
                }
                Throwable th2 = (Throwable) b6.a.e(th.getCause());
                int i9 = l0.f8063a;
                if (i9 < 21 || !(th2 instanceof MediaDrm.MediaDrmStateException)) {
                    return (i9 < 23 || !(th2 instanceof MediaDrmResetException)) ? (i9 < 18 || !(th2 instanceof NotProvisionedException)) ? (i9 < 18 || !(th2 instanceof DeniedByServerException)) ? th2 instanceof UnsupportedDrmException ? new a(23, 0) : th2 instanceof DefaultDrmSessionManager.MissingSchemeDataException ? new a(28, 0) : new a(30, 0) : new a(29, 0) : new a(24, 0) : new a(27, 0);
                }
                int V = l0.V(((MediaDrm.MediaDrmStateException) th2).getDiagnosticInfo());
                return new a(D0(V), V);
            }
        }
    }

    private static Pair<String, String> H0(String str) {
        String[] R0 = l0.R0(str, "-");
        return Pair.create(R0[0], R0.length >= 2 ? R0[1] : null);
    }

    private static int J0(Context context) {
        switch (w.d(context).f()) {
            case 0:
                return 0;
            case 1:
                return 9;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
            case 8:
            default:
                return 1;
            case 7:
                return 3;
            case 9:
                return 8;
            case 10:
                return 7;
        }
    }

    private static int K0(z0 z0Var) {
        z0.h hVar = z0Var.f11304b;
        if (hVar == null) {
            return 0;
        }
        int p02 = l0.p0(hVar.f11378a, hVar.f11379b);
        if (p02 != 0) {
            if (p02 != 1) {
                return p02 != 2 ? 1 : 4;
            }
            return 5;
        }
        return 3;
    }

    private static int L0(int i8) {
        if (i8 != 1) {
            if (i8 != 2) {
                return i8 != 3 ? 1 : 4;
            }
            return 3;
        }
        return 2;
    }

    private void M0(b.C0177b c0177b) {
        for (int i8 = 0; i8 < c0177b.d(); i8++) {
            int b9 = c0177b.b(i8);
            b.a c9 = c0177b.c(b9);
            if (b9 == 0) {
                this.f20681b.d(c9);
            } else if (b9 == 11) {
                this.f20681b.g(c9, this.f20690k);
            } else {
                this.f20681b.c(c9);
            }
        }
    }

    private void N0(long j8) {
        int J0 = J0(this.f20680a);
        if (J0 != this.f20692m) {
            this.f20692m = J0;
            this.f20682c.reportNetworkEvent(new NetworkEvent.Builder().setNetworkType(J0).setTimeSinceCreatedMillis(j8 - this.f20683d).build());
        }
    }

    private void O0(long j8) {
        PlaybackException playbackException = this.f20693n;
        if (playbackException == null) {
            return;
        }
        a G0 = G0(playbackException, this.f20680a, this.f20700v == 4);
        this.f20682c.reportPlaybackErrorEvent(new PlaybackErrorEvent.Builder().setTimeSinceCreatedMillis(j8 - this.f20683d).setErrorCode(G0.f20705a).setSubErrorCode(G0.f20706b).setException(playbackException).build());
        this.A = true;
        this.f20693n = null;
    }

    private void P0(y1 y1Var, b.C0177b c0177b, long j8) {
        if (y1Var.z() != 2) {
            this.f20699u = false;
        }
        if (y1Var.s() == null) {
            this.f20701w = false;
        } else if (c0177b.a(10)) {
            this.f20701w = true;
        }
        int X0 = X0(y1Var);
        if (this.f20691l != X0) {
            this.f20691l = X0;
            this.A = true;
            this.f20682c.reportPlaybackStateEvent(new PlaybackStateEvent.Builder().setState(this.f20691l).setTimeSinceCreatedMillis(j8 - this.f20683d).build());
        }
    }

    private void Q0(y1 y1Var, b.C0177b c0177b, long j8) {
        if (c0177b.a(2)) {
            i2 B = y1Var.B();
            boolean c9 = B.c(2);
            boolean c10 = B.c(1);
            boolean c11 = B.c(3);
            if (c9 || c10 || c11) {
                if (!c9) {
                    V0(j8, null, 0);
                }
                if (!c10) {
                    R0(j8, null, 0);
                }
                if (!c11) {
                    T0(j8, null, 0);
                }
            }
        }
        if (A0(this.f20694o)) {
            b bVar = this.f20694o;
            w0 w0Var = bVar.f20707a;
            if (w0Var.f11212x != -1) {
                V0(j8, w0Var, bVar.f20708b);
                this.f20694o = null;
            }
        }
        if (A0(this.f20695p)) {
            b bVar2 = this.f20695p;
            R0(j8, bVar2.f20707a, bVar2.f20708b);
            this.f20695p = null;
        }
        if (A0(this.q)) {
            b bVar3 = this.q;
            T0(j8, bVar3.f20707a, bVar3.f20708b);
            this.q = null;
        }
    }

    private void R0(long j8, w0 w0Var, int i8) {
        if (l0.c(this.f20697s, w0Var)) {
            return;
        }
        if (this.f20697s == null && i8 == 0) {
            i8 = 1;
        }
        this.f20697s = w0Var;
        W0(0, j8, w0Var, i8);
    }

    private void S0(y1 y1Var, b.C0177b c0177b) {
        DrmInitData E0;
        if (c0177b.a(0)) {
            b.a c9 = c0177b.c(0);
            if (this.f20689j != null) {
                U0(c9.f20638b, c9.f20640d);
            }
        }
        if (c0177b.a(2) && this.f20689j != null && (E0 = E0(y1Var.B().b())) != null) {
            ((PlaybackMetrics.Builder) l0.j(this.f20689j)).setDrmType(F0(E0));
        }
        if (c0177b.a(1011)) {
            this.f20704z++;
        }
    }

    private void T0(long j8, w0 w0Var, int i8) {
        if (l0.c(this.f20698t, w0Var)) {
            return;
        }
        if (this.f20698t == null && i8 == 0) {
            i8 = 1;
        }
        this.f20698t = w0Var;
        W0(2, j8, w0Var, i8);
    }

    private void U0(h2 h2Var, k.b bVar) {
        int f5;
        PlaybackMetrics.Builder builder = this.f20689j;
        if (bVar == null || (f5 = h2Var.f(bVar.f20286a)) == -1) {
            return;
        }
        h2Var.j(f5, this.f20685f);
        h2Var.r(this.f20685f.f9758c, this.f20684e);
        builder.setStreamType(K0(this.f20684e.f9772c));
        h2.d dVar = this.f20684e;
        if (dVar.f9783p != -9223372036854775807L && !dVar.f9781m && !dVar.f9778j && !dVar.h()) {
            builder.setMediaDurationMillis(this.f20684e.f());
        }
        builder.setPlaybackType(this.f20684e.h() ? 2 : 1);
        this.A = true;
    }

    private void V0(long j8, w0 w0Var, int i8) {
        if (l0.c(this.f20696r, w0Var)) {
            return;
        }
        if (this.f20696r == null && i8 == 0) {
            i8 = 1;
        }
        this.f20696r = w0Var;
        W0(1, j8, w0Var, i8);
    }

    private void W0(int i8, long j8, w0 w0Var, int i9) {
        TrackChangeEvent.Builder timeSinceCreatedMillis = new TrackChangeEvent.Builder(i8).setTimeSinceCreatedMillis(j8 - this.f20683d);
        if (w0Var != null) {
            timeSinceCreatedMillis.setTrackState(1);
            timeSinceCreatedMillis.setTrackChangeReason(L0(i9));
            String str = w0Var.f11206l;
            if (str != null) {
                timeSinceCreatedMillis.setContainerMimeType(str);
            }
            String str2 = w0Var.f11207m;
            if (str2 != null) {
                timeSinceCreatedMillis.setSampleMimeType(str2);
            }
            String str3 = w0Var.f11204j;
            if (str3 != null) {
                timeSinceCreatedMillis.setCodecName(str3);
            }
            int i10 = w0Var.f11203h;
            if (i10 != -1) {
                timeSinceCreatedMillis.setBitrate(i10);
            }
            int i11 = w0Var.f11211w;
            if (i11 != -1) {
                timeSinceCreatedMillis.setWidth(i11);
            }
            int i12 = w0Var.f11212x;
            if (i12 != -1) {
                timeSinceCreatedMillis.setHeight(i12);
            }
            int i13 = w0Var.F;
            if (i13 != -1) {
                timeSinceCreatedMillis.setChannelCount(i13);
            }
            int i14 = w0Var.G;
            if (i14 != -1) {
                timeSinceCreatedMillis.setAudioSampleRate(i14);
            }
            String str4 = w0Var.f11198c;
            if (str4 != null) {
                Pair<String, String> H0 = H0(str4);
                timeSinceCreatedMillis.setLanguage((String) H0.first);
                Object obj = H0.second;
                if (obj != null) {
                    timeSinceCreatedMillis.setLanguageRegion((String) obj);
                }
            }
            float f5 = w0Var.f11213y;
            if (f5 != -1.0f) {
                timeSinceCreatedMillis.setVideoFrameRate(f5);
            }
        } else {
            timeSinceCreatedMillis.setTrackState(0);
        }
        this.A = true;
        this.f20682c.reportTrackChangeEvent(timeSinceCreatedMillis.build());
    }

    private int X0(y1 y1Var) {
        int z4 = y1Var.z();
        if (this.f20699u) {
            return 5;
        }
        if (this.f20701w) {
            return 13;
        }
        if (z4 == 4) {
            return 11;
        }
        if (z4 == 2) {
            int i8 = this.f20691l;
            if (i8 == 0 || i8 == 2) {
                return 2;
            }
            if (y1Var.k()) {
                return y1Var.I() != 0 ? 10 : 6;
            }
            return 7;
        } else if (z4 == 3) {
            if (y1Var.k()) {
                return y1Var.I() != 0 ? 9 : 3;
            }
            return 4;
        } else if (z4 != 1 || this.f20691l == 0) {
            return this.f20691l;
        } else {
            return 12;
        }
    }

    public LogSessionId I0() {
        return this.f20682c.getSessionId();
    }

    @Override // j4.b
    public void P(b.a aVar, y1.e eVar, y1.e eVar2, int i8) {
        if (i8 == 1) {
            this.f20699u = true;
        }
        this.f20690k = i8;
    }

    @Override // j4.s1.a
    public void T(b.a aVar, String str) {
    }

    @Override // j4.b
    public void V(b.a aVar, e eVar) {
        this.f20702x += eVar.f21591g;
        this.f20703y += eVar.f21589e;
    }

    @Override // j4.s1.a
    public void X(b.a aVar, String str, boolean z4) {
        k.b bVar = aVar.f20640d;
        if ((bVar == null || !bVar.b()) && str.equals(this.f20688i)) {
            C0();
        }
        this.f20686g.remove(str);
        this.f20687h.remove(str);
    }

    @Override // j4.b
    public void d0(y1 y1Var, b.C0177b c0177b) {
        if (c0177b.d() == 0) {
            return;
        }
        M0(c0177b);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        S0(y1Var, c0177b);
        O0(elapsedRealtime);
        Q0(y1Var, c0177b, elapsedRealtime);
        N0(elapsedRealtime);
        P0(y1Var, c0177b, elapsedRealtime);
        if (c0177b.a(1028)) {
            this.f20681b.b(c0177b.c(1028));
        }
    }

    @Override // j4.b
    public void g(b.a aVar, h hVar, i iVar, IOException iOException, boolean z4) {
        this.f20700v = iVar.f20279a;
    }

    @Override // j4.b
    public void g0(b.a aVar, i iVar) {
        if (aVar.f20640d == null) {
            return;
        }
        b bVar = new b((w0) b6.a.e(iVar.f20281c), iVar.f20282d, this.f20681b.f(aVar.f20638b, (k.b) b6.a.e(aVar.f20640d)));
        int i8 = iVar.f20280b;
        if (i8 != 0) {
            if (i8 == 1) {
                this.f20695p = bVar;
                return;
            } else if (i8 != 2) {
                if (i8 != 3) {
                    return;
                }
                this.q = bVar;
                return;
            }
        }
        this.f20694o = bVar;
    }

    @Override // j4.s1.a
    public void i(b.a aVar, String str) {
        k.b bVar = aVar.f20640d;
        if (bVar == null || !bVar.b()) {
            C0();
            this.f20688i = str;
            this.f20689j = new PlaybackMetrics.Builder().setPlayerName("ExoPlayerLib").setPlayerVersion("2.18.7");
            U0(aVar.f20638b, aVar.f20640d);
        }
    }

    @Override // j4.b
    public void o(b.a aVar, int i8, long j8, long j9) {
        k.b bVar = aVar.f20640d;
        if (bVar != null) {
            String f5 = this.f20681b.f(aVar.f20638b, (k.b) b6.a.e(bVar));
            Long l8 = this.f20687h.get(f5);
            Long l9 = this.f20686g.get(f5);
            this.f20687h.put(f5, Long.valueOf((l8 == null ? 0L : l8.longValue()) + j8));
            this.f20686g.put(f5, Long.valueOf((l9 != null ? l9.longValue() : 0L) + i8));
        }
    }

    @Override // j4.s1.a
    public void q(b.a aVar, String str, String str2) {
    }

    @Override // j4.b
    public void q0(b.a aVar, PlaybackException playbackException) {
        this.f20693n = playbackException;
    }

    @Override // j4.b
    public void t0(b.a aVar, x xVar) {
        b bVar = this.f20694o;
        if (bVar != null) {
            w0 w0Var = bVar.f20707a;
            if (w0Var.f11212x == -1) {
                this.f20694o = new b(w0Var.b().n0(xVar.f8461a).S(xVar.f8462b).G(), bVar.f20708b, bVar.f20709c);
            }
        }
    }
}
