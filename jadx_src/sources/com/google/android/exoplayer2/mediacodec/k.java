package com.google.android.exoplayer2.mediacodec;

import android.graphics.Point;
import android.media.MediaCodecInfo;
import android.util.Pair;
import b6.l0;
import b6.t;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k {

    /* renamed from: a  reason: collision with root package name */
    public final String f10030a;

    /* renamed from: b  reason: collision with root package name */
    public final String f10031b;

    /* renamed from: c  reason: collision with root package name */
    public final String f10032c;

    /* renamed from: d  reason: collision with root package name */
    public final MediaCodecInfo.CodecCapabilities f10033d;

    /* renamed from: e  reason: collision with root package name */
    public final boolean f10034e;

    /* renamed from: f  reason: collision with root package name */
    public final boolean f10035f;

    /* renamed from: g  reason: collision with root package name */
    public final boolean f10036g;

    /* renamed from: h  reason: collision with root package name */
    public final boolean f10037h;

    /* renamed from: i  reason: collision with root package name */
    public final boolean f10038i;

    /* renamed from: j  reason: collision with root package name */
    public final boolean f10039j;

    /* renamed from: k  reason: collision with root package name */
    private final boolean f10040k;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        public static int a(MediaCodecInfo.VideoCapabilities videoCapabilities, int i8, int i9, double d8) {
            List<MediaCodecInfo.VideoCapabilities.PerformancePoint> supportedPerformancePoints = videoCapabilities.getSupportedPerformancePoints();
            if (supportedPerformancePoints == null || supportedPerformancePoints.isEmpty() || k.a()) {
                return 0;
            }
            MediaCodecInfo.VideoCapabilities.PerformancePoint performancePoint = new MediaCodecInfo.VideoCapabilities.PerformancePoint(i8, i9, (int) d8);
            for (int i10 = 0; i10 < supportedPerformancePoints.size(); i10++) {
                if (supportedPerformancePoints.get(i10).covers(performancePoint)) {
                    return 2;
                }
            }
            return 1;
        }
    }

    k(String str, String str2, String str3, MediaCodecInfo.CodecCapabilities codecCapabilities, boolean z4, boolean z8, boolean z9, boolean z10, boolean z11, boolean z12) {
        this.f10030a = (String) b6.a.e(str);
        this.f10031b = str2;
        this.f10032c = str3;
        this.f10033d = codecCapabilities;
        this.f10037h = z4;
        this.f10038i = z8;
        this.f10039j = z9;
        this.f10034e = z10;
        this.f10035f = z11;
        this.f10036g = z12;
        this.f10040k = t.s(str2);
    }

    private static boolean A(String str) {
        return l0.f8066d.startsWith("SM-T230") && "OMX.MARVELL.VIDEO.HW.CODA7542DECODER".equals(str);
    }

    private static boolean B(String str) {
        if (l0.f8063a <= 22) {
            String str2 = l0.f8066d;
            if (("ODROID-XU3".equals(str2) || "Nexus 10".equals(str2)) && ("OMX.Exynos.AVC.Decoder".equals(str) || "OMX.Exynos.AVC.Decoder.secure".equals(str))) {
                return true;
            }
        }
        return false;
    }

    private static boolean C() {
        String str = l0.f8064b;
        if (!str.equals("sabrina") && !str.equals("boreal")) {
            String str2 = l0.f8066d;
            if (!str2.startsWith("Lenovo TB-X605") && !str2.startsWith("Lenovo TB-X606") && !str2.startsWith("Lenovo TB-X616")) {
                return false;
            }
        }
        return true;
    }

    private static boolean D(String str, int i8) {
        if ("video/hevc".equals(str) && 2 == i8) {
            String str2 = l0.f8064b;
            if ("sailfish".equals(str2) || "marlin".equals(str2)) {
                return true;
            }
        }
        return false;
    }

    private static boolean E(String str) {
        return ("OMX.MTK.VIDEO.DECODER.HEVC".equals(str) && "mcv5a".equals(l0.f8064b)) ? false : true;
    }

    public static k F(String str, String str2, String str3, MediaCodecInfo.CodecCapabilities codecCapabilities, boolean z4, boolean z8, boolean z9, boolean z10, boolean z11) {
        return new k(str, str2, str3, codecCapabilities, z4, z8, z9, (z10 || codecCapabilities == null || !i(codecCapabilities) || B(str)) ? false : true, codecCapabilities != null && u(codecCapabilities), z11 || (codecCapabilities != null && s(codecCapabilities)));
    }

    static /* synthetic */ boolean a() {
        return C();
    }

    private static int b(String str, String str2, int i8) {
        if (i8 > 1 || ((l0.f8063a >= 26 && i8 > 0) || "audio/mpeg".equals(str2) || "audio/3gpp".equals(str2) || "audio/amr-wb".equals(str2) || "audio/mp4a-latm".equals(str2) || "audio/vorbis".equals(str2) || "audio/opus".equals(str2) || "audio/raw".equals(str2) || "audio/flac".equals(str2) || "audio/g711-alaw".equals(str2) || "audio/g711-mlaw".equals(str2) || "audio/gsm".equals(str2))) {
            return i8;
        }
        int i9 = "audio/ac3".equals(str2) ? 6 : "audio/eac3".equals(str2) ? 16 : 30;
        b6.p.i("MediaCodecInfo", "AssumedMaxChannelAdjustment: " + str + ", [" + i8 + " to " + i9 + "]");
        return i9;
    }

    private static Point d(MediaCodecInfo.VideoCapabilities videoCapabilities, int i8, int i9) {
        int widthAlignment = videoCapabilities.getWidthAlignment();
        int heightAlignment = videoCapabilities.getHeightAlignment();
        return new Point(l0.l(i8, widthAlignment) * widthAlignment, l0.l(i9, heightAlignment) * heightAlignment);
    }

    private static boolean e(MediaCodecInfo.VideoCapabilities videoCapabilities, int i8, int i9, double d8) {
        Point d9 = d(videoCapabilities, i8, i9);
        int i10 = d9.x;
        int i11 = d9.y;
        return (d8 == -1.0d || d8 < 1.0d) ? videoCapabilities.isSizeSupported(i10, i11) : videoCapabilities.areSizeAndRateSupported(i10, i11, Math.floor(d8));
    }

    private static MediaCodecInfo.CodecProfileLevel[] g(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        MediaCodecInfo.VideoCapabilities videoCapabilities;
        int intValue = (codecCapabilities == null || (videoCapabilities = codecCapabilities.getVideoCapabilities()) == null) ? 0 : videoCapabilities.getBitrateRange().getUpper().intValue();
        int i8 = intValue >= 180000000 ? RecognitionOptions.UPC_E : intValue >= 120000000 ? RecognitionOptions.UPC_A : intValue >= 60000000 ? RecognitionOptions.QR_CODE : intValue >= 30000000 ? RecognitionOptions.ITF : intValue >= 18000000 ? 64 : intValue >= 12000000 ? 32 : intValue >= 7200000 ? 16 : intValue >= 3600000 ? 8 : intValue >= 1800000 ? 4 : intValue >= 800000 ? 2 : 1;
        MediaCodecInfo.CodecProfileLevel codecProfileLevel = new MediaCodecInfo.CodecProfileLevel();
        codecProfileLevel.profile = 1;
        codecProfileLevel.level = i8;
        return new MediaCodecInfo.CodecProfileLevel[]{codecProfileLevel};
    }

    private static boolean i(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        return l0.f8063a >= 19 && j(codecCapabilities);
    }

    private static boolean j(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        return codecCapabilities.isFeatureSupported("adaptive-playback");
    }

    private boolean m(w0 w0Var, boolean z4) {
        Pair<Integer, Integer> q = MediaCodecUtil.q(w0Var);
        if (q == null) {
            return true;
        }
        int intValue = ((Integer) q.first).intValue();
        int intValue2 = ((Integer) q.second).intValue();
        if ("video/dolby-vision".equals(w0Var.f11207m)) {
            if ("video/avc".equals(this.f10031b)) {
                intValue = 8;
            } else {
                intValue = "video/hevc".equals(this.f10031b) ? 2 : 2;
            }
            intValue2 = 0;
        }
        if (this.f10040k || intValue == 42) {
            MediaCodecInfo.CodecProfileLevel[] h8 = h();
            if (l0.f8063a <= 23 && "video/x-vnd.on2.vp9".equals(this.f10031b) && h8.length == 0) {
                h8 = g(this.f10033d);
            }
            for (MediaCodecInfo.CodecProfileLevel codecProfileLevel : h8) {
                if (codecProfileLevel.profile == intValue && ((codecProfileLevel.level >= intValue2 || !z4) && !D(this.f10031b, intValue))) {
                    return true;
                }
            }
            y("codec.profileLevel, " + w0Var.f11204j + ", " + this.f10032c);
            return false;
        }
        return true;
    }

    private boolean q(w0 w0Var) {
        return this.f10031b.equals(w0Var.f11207m) || this.f10031b.equals(MediaCodecUtil.m(w0Var));
    }

    private static boolean s(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        return l0.f8063a >= 21 && t(codecCapabilities);
    }

    private static boolean t(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        return codecCapabilities.isFeatureSupported("secure-playback");
    }

    private static boolean u(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        return l0.f8063a >= 21 && v(codecCapabilities);
    }

    private static boolean v(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        return codecCapabilities.isFeatureSupported("tunneled-playback");
    }

    private void x(String str) {
        b6.p.b("MediaCodecInfo", "AssumedSupport [" + str + "] [" + this.f10030a + ", " + this.f10031b + "] [" + l0.f8067e + "]");
    }

    private void y(String str) {
        b6.p.b("MediaCodecInfo", "NoSupport [" + str + "] [" + this.f10030a + ", " + this.f10031b + "] [" + l0.f8067e + "]");
    }

    private static boolean z(String str) {
        return "audio/opus".equals(str);
    }

    public Point c(int i8, int i9) {
        MediaCodecInfo.VideoCapabilities videoCapabilities;
        MediaCodecInfo.CodecCapabilities codecCapabilities = this.f10033d;
        if (codecCapabilities == null || (videoCapabilities = codecCapabilities.getVideoCapabilities()) == null) {
            return null;
        }
        return d(videoCapabilities, i8, i9);
    }

    public l4.g f(w0 w0Var, w0 w0Var2) {
        int i8 = !l0.c(w0Var.f11207m, w0Var2.f11207m) ? 8 : 0;
        if (this.f10040k) {
            if (w0Var.f11214z != w0Var2.f11214z) {
                i8 |= RecognitionOptions.UPC_E;
            }
            if (!this.f10034e && (w0Var.f11211w != w0Var2.f11211w || w0Var.f11212x != w0Var2.f11212x)) {
                i8 |= RecognitionOptions.UPC_A;
            }
            if (!l0.c(w0Var.E, w0Var2.E)) {
                i8 |= RecognitionOptions.PDF417;
            }
            if (A(this.f10030a) && !w0Var.g(w0Var2)) {
                i8 |= 2;
            }
            if (i8 == 0) {
                return new l4.g(this.f10030a, w0Var, w0Var2, w0Var.g(w0Var2) ? 3 : 2, 0);
            }
        } else {
            if (w0Var.F != w0Var2.F) {
                i8 |= RecognitionOptions.AZTEC;
            }
            if (w0Var.G != w0Var2.G) {
                i8 |= 8192;
            }
            if (w0Var.H != w0Var2.H) {
                i8 |= 16384;
            }
            if (i8 == 0 && "audio/mp4a-latm".equals(this.f10031b)) {
                Pair<Integer, Integer> q = MediaCodecUtil.q(w0Var);
                Pair<Integer, Integer> q8 = MediaCodecUtil.q(w0Var2);
                if (q != null && q8 != null) {
                    int intValue = ((Integer) q.first).intValue();
                    int intValue2 = ((Integer) q8.first).intValue();
                    if (intValue == 42 && intValue2 == 42) {
                        return new l4.g(this.f10030a, w0Var, w0Var2, 3, 0);
                    }
                }
            }
            if (!w0Var.g(w0Var2)) {
                i8 |= 32;
            }
            if (z(this.f10031b)) {
                i8 |= 2;
            }
            if (i8 == 0) {
                return new l4.g(this.f10030a, w0Var, w0Var2, 1, 0);
            }
        }
        return new l4.g(this.f10030a, w0Var, w0Var2, 0, i8);
    }

    public MediaCodecInfo.CodecProfileLevel[] h() {
        MediaCodecInfo.CodecProfileLevel[] codecProfileLevelArr;
        MediaCodecInfo.CodecCapabilities codecCapabilities = this.f10033d;
        return (codecCapabilities == null || (codecProfileLevelArr = codecCapabilities.profileLevels) == null) ? new MediaCodecInfo.CodecProfileLevel[0] : codecProfileLevelArr;
    }

    public boolean k(int i8) {
        String str;
        MediaCodecInfo.CodecCapabilities codecCapabilities = this.f10033d;
        if (codecCapabilities == null) {
            str = "channelCount.caps";
        } else {
            MediaCodecInfo.AudioCapabilities audioCapabilities = codecCapabilities.getAudioCapabilities();
            if (audioCapabilities == null) {
                str = "channelCount.aCaps";
            } else if (b(this.f10030a, this.f10031b, audioCapabilities.getMaxInputChannelCount()) >= i8) {
                return true;
            } else {
                str = "channelCount.support, " + i8;
            }
        }
        y(str);
        return false;
    }

    public boolean l(int i8) {
        String str;
        MediaCodecInfo.CodecCapabilities codecCapabilities = this.f10033d;
        if (codecCapabilities == null) {
            str = "sampleRate.caps";
        } else {
            MediaCodecInfo.AudioCapabilities audioCapabilities = codecCapabilities.getAudioCapabilities();
            if (audioCapabilities == null) {
                str = "sampleRate.aCaps";
            } else if (audioCapabilities.isSampleRateSupported(i8)) {
                return true;
            } else {
                str = "sampleRate.support, " + i8;
            }
        }
        y(str);
        return false;
    }

    public boolean n(w0 w0Var) {
        return q(w0Var) && m(w0Var, false);
    }

    public boolean o(w0 w0Var) {
        int i8;
        if (q(w0Var) && m(w0Var, true)) {
            if (!this.f10040k) {
                if (l0.f8063a >= 21) {
                    int i9 = w0Var.G;
                    if (i9 != -1 && !l(i9)) {
                        return false;
                    }
                    int i10 = w0Var.F;
                    if (i10 != -1 && !k(i10)) {
                        return false;
                    }
                }
                return true;
            }
            int i11 = w0Var.f11211w;
            if (i11 <= 0 || (i8 = w0Var.f11212x) <= 0) {
                return true;
            }
            if (l0.f8063a >= 21) {
                return w(i11, i8, w0Var.f11213y);
            }
            boolean z4 = i11 * i8 <= MediaCodecUtil.N();
            if (!z4) {
                y("legacyFrameSize, " + w0Var.f11211w + "x" + w0Var.f11212x);
            }
            return z4;
        }
        return false;
    }

    public boolean p() {
        if (l0.f8063a >= 29 && "video/x-vnd.on2.vp9".equals(this.f10031b)) {
            for (MediaCodecInfo.CodecProfileLevel codecProfileLevel : h()) {
                if (codecProfileLevel.profile == 16384) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean r(w0 w0Var) {
        if (this.f10040k) {
            return this.f10034e;
        }
        Pair<Integer, Integer> q = MediaCodecUtil.q(w0Var);
        return q != null && ((Integer) q.first).intValue() == 42;
    }

    public String toString() {
        return this.f10030a;
    }

    public boolean w(int i8, int i9, double d8) {
        StringBuilder sb;
        String str;
        String sb2;
        MediaCodecInfo.CodecCapabilities codecCapabilities = this.f10033d;
        if (codecCapabilities == null) {
            sb2 = "sizeAndRate.caps";
        } else {
            MediaCodecInfo.VideoCapabilities videoCapabilities = codecCapabilities.getVideoCapabilities();
            if (videoCapabilities != null) {
                if (l0.f8063a >= 29) {
                    int a9 = a.a(videoCapabilities, i8, i9, d8);
                    if (a9 == 2) {
                        return true;
                    }
                    if (a9 == 1) {
                        sb = new StringBuilder();
                        str = "sizeAndRate.cover, ";
                        sb.append(str);
                        sb.append(i8);
                        sb.append("x");
                        sb.append(i9);
                        sb.append("@");
                        sb.append(d8);
                        sb2 = sb.toString();
                    }
                }
                if (!e(videoCapabilities, i8, i9, d8)) {
                    if (i8 < i9 && E(this.f10030a) && e(videoCapabilities, i9, i8, d8)) {
                        x("sizeAndRate.rotated, " + i8 + "x" + i9 + "@" + d8);
                    } else {
                        sb = new StringBuilder();
                        str = "sizeAndRate.support, ";
                        sb.append(str);
                        sb.append(i8);
                        sb.append("x");
                        sb.append(i9);
                        sb.append("@");
                        sb.append(d8);
                        sb2 = sb.toString();
                    }
                }
                return true;
            }
            sb2 = "sizeAndRate.vCaps";
        }
        y(sb2);
        return false;
    }
}
