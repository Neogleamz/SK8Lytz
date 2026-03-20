package com.google.android.exoplayer2.mediacodec;

import android.annotation.SuppressLint;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.text.TextUtils;
import android.util.Pair;
import b6.l0;
import b6.t;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@SuppressLint({"InlinedApi"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MediaCodecUtil {

    /* renamed from: a  reason: collision with root package name */
    private static final Pattern f9963a = Pattern.compile("^\\D?(\\d+)$");

    /* renamed from: b  reason: collision with root package name */
    private static final HashMap<b, List<k>> f9964b = new HashMap<>();

    /* renamed from: c  reason: collision with root package name */
    private static int f9965c = -1;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class DecoderQueryException extends Exception {
        private DecoderQueryException(Throwable th) {
            super("Failed to query underlying media codecs", th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final String f9966a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f9967b;

        /* renamed from: c  reason: collision with root package name */
        public final boolean f9968c;

        public b(String str, boolean z4, boolean z8) {
            this.f9966a = str;
            this.f9967b = z4;
            this.f9968c = z8;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || obj.getClass() != b.class) {
                return false;
            }
            b bVar = (b) obj;
            return TextUtils.equals(this.f9966a, bVar.f9966a) && this.f9967b == bVar.f9967b && this.f9968c == bVar.f9968c;
        }

        public int hashCode() {
            return ((((this.f9966a.hashCode() + 31) * 31) + (this.f9967b ? 1231 : 1237)) * 31) + (this.f9968c ? 1231 : 1237);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        MediaCodecInfo a(int i8);

        boolean b(String str, String str2, MediaCodecInfo.CodecCapabilities codecCapabilities);

        boolean c(String str, String str2, MediaCodecInfo.CodecCapabilities codecCapabilities);

        int d();

        boolean e();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d implements c {
        private d() {
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.c
        public MediaCodecInfo a(int i8) {
            return MediaCodecList.getCodecInfoAt(i8);
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.c
        public boolean b(String str, String str2, MediaCodecInfo.CodecCapabilities codecCapabilities) {
            return "secure-playback".equals(str) && "video/avc".equals(str2);
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.c
        public boolean c(String str, String str2, MediaCodecInfo.CodecCapabilities codecCapabilities) {
            return false;
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.c
        public int d() {
            return MediaCodecList.getCodecCount();
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.c
        public boolean e() {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e implements c {

        /* renamed from: a  reason: collision with root package name */
        private final int f9969a;

        /* renamed from: b  reason: collision with root package name */
        private MediaCodecInfo[] f9970b;

        public e(boolean z4, boolean z8) {
            this.f9969a = (z4 || z8) ? 1 : 0;
        }

        private void f() {
            if (this.f9970b == null) {
                this.f9970b = new MediaCodecList(this.f9969a).getCodecInfos();
            }
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.c
        public MediaCodecInfo a(int i8) {
            f();
            return this.f9970b[i8];
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.c
        public boolean b(String str, String str2, MediaCodecInfo.CodecCapabilities codecCapabilities) {
            return codecCapabilities.isFeatureSupported(str);
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.c
        public boolean c(String str, String str2, MediaCodecInfo.CodecCapabilities codecCapabilities) {
            return codecCapabilities.isFeatureRequired(str);
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.c
        public int d() {
            f();
            return this.f9970b.length;
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.c
        public boolean e() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f<T> {
        int a(T t8);
    }

    private static boolean A(MediaCodecInfo mediaCodecInfo) {
        return l0.f8063a >= 29 && B(mediaCodecInfo);
    }

    private static boolean B(MediaCodecInfo mediaCodecInfo) {
        return mediaCodecInfo.isAlias();
    }

    private static boolean C(MediaCodecInfo mediaCodecInfo, String str, boolean z4, String str2) {
        if (mediaCodecInfo.isEncoder() || (!z4 && str.endsWith(".secure"))) {
            return false;
        }
        int i8 = l0.f8063a;
        if (i8 >= 21 || !("CIPAACDecoder".equals(str) || "CIPMP3Decoder".equals(str) || "CIPVorbisDecoder".equals(str) || "CIPAMRNBDecoder".equals(str) || "AACDecoder".equals(str) || "MP3Decoder".equals(str))) {
            if (i8 < 18 && "OMX.MTK.AUDIO.DECODER.AAC".equals(str)) {
                String str3 = l0.f8064b;
                if ("a70".equals(str3) || ("Xiaomi".equals(l0.f8065c) && str3.startsWith("HM"))) {
                    return false;
                }
            }
            if (i8 == 16 && "OMX.qcom.audio.decoder.mp3".equals(str)) {
                String str4 = l0.f8064b;
                if ("dlxu".equals(str4) || "protou".equals(str4) || "ville".equals(str4) || "villeplus".equals(str4) || "villec2".equals(str4) || str4.startsWith("gee") || "C6602".equals(str4) || "C6603".equals(str4) || "C6606".equals(str4) || "C6616".equals(str4) || "L36h".equals(str4) || "SO-02E".equals(str4)) {
                    return false;
                }
            }
            if (i8 == 16 && "OMX.qcom.audio.decoder.aac".equals(str)) {
                String str5 = l0.f8064b;
                if ("C1504".equals(str5) || "C1505".equals(str5) || "C1604".equals(str5) || "C1605".equals(str5)) {
                    return false;
                }
            }
            if (i8 < 24 && (("OMX.SEC.aac.dec".equals(str) || "OMX.Exynos.AAC.Decoder".equals(str)) && "samsung".equals(l0.f8065c))) {
                String str6 = l0.f8064b;
                if (str6.startsWith("zeroflte") || str6.startsWith("zerolte") || str6.startsWith("zenlte") || "SC-05G".equals(str6) || "marinelteatt".equals(str6) || "404SC".equals(str6) || "SC-04G".equals(str6) || "SCV31".equals(str6)) {
                    return false;
                }
            }
            if (i8 <= 19 && "OMX.SEC.vp8.dec".equals(str) && "samsung".equals(l0.f8065c)) {
                String str7 = l0.f8064b;
                if (str7.startsWith("d2") || str7.startsWith("serrano") || str7.startsWith("jflte") || str7.startsWith("santos") || str7.startsWith("t0")) {
                    return false;
                }
            }
            if (i8 <= 19 && l0.f8064b.startsWith("jflte") && "OMX.qcom.video.decoder.vp8".equals(str)) {
                return false;
            }
            return (i8 <= 23 && "audio/eac3-joc".equals(str2) && "OMX.MTK.AUDIO.DECODER.DSPAC3".equals(str)) ? false : true;
        }
        return false;
    }

    private static boolean D(MediaCodecInfo mediaCodecInfo, String str) {
        return l0.f8063a >= 29 ? E(mediaCodecInfo) : !F(mediaCodecInfo, str);
    }

    private static boolean E(MediaCodecInfo mediaCodecInfo) {
        return mediaCodecInfo.isHardwareAccelerated();
    }

    private static boolean F(MediaCodecInfo mediaCodecInfo, String str) {
        if (l0.f8063a >= 29) {
            return G(mediaCodecInfo);
        }
        if (t.o(str)) {
            return true;
        }
        String e8 = com.google.common.base.c.e(mediaCodecInfo.getName());
        if (e8.startsWith("arc.")) {
            return false;
        }
        if (e8.startsWith("omx.google.") || e8.startsWith("omx.ffmpeg.")) {
            return true;
        }
        if ((e8.startsWith("omx.sec.") && e8.contains(".sw.")) || e8.equals("omx.qcom.video.decoder.hevcswvdec") || e8.startsWith("c2.android.") || e8.startsWith("c2.google.")) {
            return true;
        }
        return (e8.startsWith("omx.") || e8.startsWith("c2.")) ? false : true;
    }

    private static boolean G(MediaCodecInfo mediaCodecInfo) {
        return mediaCodecInfo.isSoftwareOnly();
    }

    private static boolean H(MediaCodecInfo mediaCodecInfo) {
        if (l0.f8063a >= 29) {
            return I(mediaCodecInfo);
        }
        String e8 = com.google.common.base.c.e(mediaCodecInfo.getName());
        return (e8.startsWith("omx.google.") || e8.startsWith("c2.android.") || e8.startsWith("c2.google.")) ? false : true;
    }

    private static boolean I(MediaCodecInfo mediaCodecInfo) {
        return mediaCodecInfo.isVendor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int J(k kVar) {
        String str = kVar.f10030a;
        if (str.startsWith("OMX.google") || str.startsWith("c2.android")) {
            return 1;
        }
        return (l0.f8063a >= 26 || !str.equals("OMX.MTK.AUDIO.DECODER.RAW")) ? 0 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int K(k kVar) {
        return kVar.f10030a.startsWith("OMX.google") ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int L(w0 w0Var, k kVar) {
        return kVar.n(w0Var) ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int M(f fVar, Object obj, Object obj2) {
        return fVar.a(obj2) - fVar.a(obj);
    }

    public static int N() {
        if (f9965c == -1) {
            int i8 = 0;
            k r4 = r("video/avc", false, false);
            if (r4 != null) {
                MediaCodecInfo.CodecProfileLevel[] h8 = r4.h();
                int length = h8.length;
                int i9 = 0;
                while (i8 < length) {
                    i9 = Math.max(h(h8[i8].level), i9);
                    i8++;
                }
                i8 = Math.max(i9, l0.f8063a >= 21 ? 345600 : 172800);
            }
            f9965c = i8;
        }
        return f9965c;
    }

    private static int O(int i8) {
        int i9 = 17;
        if (i8 != 17) {
            i9 = 20;
            if (i8 != 20) {
                i9 = 23;
                if (i8 != 23) {
                    i9 = 29;
                    if (i8 != 29) {
                        i9 = 39;
                        if (i8 != 39) {
                            i9 = 42;
                            if (i8 != 42) {
                                switch (i8) {
                                    case 1:
                                        return 1;
                                    case 2:
                                        return 2;
                                    case 3:
                                        return 3;
                                    case 4:
                                        return 4;
                                    case 5:
                                        return 5;
                                    case 6:
                                        return 6;
                                    default:
                                        return -1;
                                }
                            }
                        }
                    }
                }
            }
        }
        return i9;
    }

    private static <T> void P(List<T> list, final f<T> fVar) {
        Collections.sort(list, new Comparator() { // from class: com.google.android.exoplayer2.mediacodec.p
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int M;
                M = MediaCodecUtil.M(MediaCodecUtil.f.this, obj, obj2);
                return M;
            }
        });
    }

    private static int Q(int i8) {
        if (i8 != 10) {
            if (i8 != 11) {
                if (i8 != 20) {
                    if (i8 != 21) {
                        if (i8 != 30) {
                            if (i8 != 31) {
                                if (i8 != 40) {
                                    if (i8 != 41) {
                                        if (i8 != 50) {
                                            if (i8 != 51) {
                                                switch (i8) {
                                                    case 60:
                                                        return RecognitionOptions.PDF417;
                                                    case 61:
                                                        return RecognitionOptions.AZTEC;
                                                    case 62:
                                                        return 8192;
                                                    default:
                                                        return -1;
                                                }
                                            }
                                            return RecognitionOptions.UPC_A;
                                        }
                                        return RecognitionOptions.QR_CODE;
                                    }
                                    return RecognitionOptions.ITF;
                                }
                                return 64;
                            }
                            return 32;
                        }
                        return 16;
                    }
                    return 8;
                }
                return 4;
            }
            return 2;
        }
        return 1;
    }

    private static int R(int i8) {
        if (i8 != 0) {
            if (i8 != 1) {
                if (i8 != 2) {
                    return i8 != 3 ? -1 : 8;
                }
                return 4;
            }
            return 2;
        }
        return 1;
    }

    private static void e(String str, List<k> list) {
        if ("audio/raw".equals(str)) {
            if (l0.f8063a < 26 && l0.f8064b.equals("R9") && list.size() == 1 && list.get(0).f10030a.equals("OMX.MTK.AUDIO.DECODER.RAW")) {
                list.add(k.F("OMX.google.raw.decoder", "audio/raw", "audio/raw", null, false, true, false, false, false));
            }
            P(list, new f() { // from class: com.google.android.exoplayer2.mediacodec.n
                @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.f
                public final int a(Object obj) {
                    int J;
                    J = MediaCodecUtil.J((k) obj);
                    return J;
                }
            });
        }
        int i8 = l0.f8063a;
        if (i8 < 21 && list.size() > 1) {
            String str2 = list.get(0).f10030a;
            if ("OMX.SEC.mp3.dec".equals(str2) || "OMX.SEC.MP3.Decoder".equals(str2) || "OMX.brcm.audio.mp3.decoder".equals(str2)) {
                P(list, new f() { // from class: com.google.android.exoplayer2.mediacodec.o
                    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.f
                    public final int a(Object obj) {
                        int K;
                        K = MediaCodecUtil.K((k) obj);
                        return K;
                    }
                });
            }
        }
        if (i8 >= 32 || list.size() <= 1 || !"OMX.qti.audio.decoder.flac".equals(list.get(0).f10030a)) {
            return;
        }
        list.add(list.remove(0));
    }

    private static int f(int i8) {
        switch (i8) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 8;
            case 4:
                return 16;
            case 5:
                return 32;
            case 6:
                return 64;
            case 7:
                return RecognitionOptions.ITF;
            case 8:
                return RecognitionOptions.QR_CODE;
            case 9:
                return RecognitionOptions.UPC_A;
            case 10:
                return RecognitionOptions.UPC_E;
            case 11:
                return RecognitionOptions.PDF417;
            case 12:
                return RecognitionOptions.AZTEC;
            case 13:
                return 8192;
            case 14:
                return 16384;
            case 15:
                return RecognitionOptions.TEZ_CODE;
            case 16:
                return 65536;
            case 17:
                return 131072;
            case 18:
                return 262144;
            case 19:
                return 524288;
            case 20:
                return 1048576;
            case 21:
                return 2097152;
            case 22:
                return 4194304;
            case 23:
                return 8388608;
            default:
                return -1;
        }
    }

    private static int g(int i8) {
        switch (i8) {
            case 10:
                return 1;
            case 11:
                return 4;
            case 12:
                return 8;
            case 13:
                return 16;
            default:
                switch (i8) {
                    case 20:
                        return 32;
                    case 21:
                        return 64;
                    case 22:
                        return RecognitionOptions.ITF;
                    default:
                        switch (i8) {
                            case 30:
                                return RecognitionOptions.QR_CODE;
                            case 31:
                                return RecognitionOptions.UPC_A;
                            case 32:
                                return RecognitionOptions.UPC_E;
                            default:
                                switch (i8) {
                                    case 40:
                                        return RecognitionOptions.PDF417;
                                    case 41:
                                        return RecognitionOptions.AZTEC;
                                    case 42:
                                        return 8192;
                                    default:
                                        switch (i8) {
                                            case 50:
                                                return 16384;
                                            case 51:
                                                return RecognitionOptions.TEZ_CODE;
                                            case 52:
                                                return 65536;
                                            default:
                                                return -1;
                                        }
                                }
                        }
                }
        }
    }

    private static int h(int i8) {
        if (i8 == 1 || i8 == 2) {
            return 25344;
        }
        switch (i8) {
            case 8:
            case 16:
            case 32:
                return 101376;
            case 64:
                return 202752;
            case RecognitionOptions.ITF /* 128 */:
            case RecognitionOptions.QR_CODE /* 256 */:
                return 414720;
            case RecognitionOptions.UPC_A /* 512 */:
                return 921600;
            case RecognitionOptions.UPC_E /* 1024 */:
                return 1310720;
            case RecognitionOptions.PDF417 /* 2048 */:
            case RecognitionOptions.AZTEC /* 4096 */:
                return 2097152;
            case 8192:
                return 2228224;
            case 16384:
                return 5652480;
            case RecognitionOptions.TEZ_CODE /* 32768 */:
            case 65536:
                return 9437184;
            case 131072:
            case 262144:
            case 524288:
                return 35651584;
            default:
                return -1;
        }
    }

    private static int i(int i8) {
        if (i8 != 66) {
            if (i8 != 77) {
                if (i8 != 88) {
                    if (i8 != 100) {
                        if (i8 != 110) {
                            if (i8 != 122) {
                                return i8 != 244 ? -1 : 64;
                            }
                            return 32;
                        }
                        return 16;
                    }
                    return 8;
                }
                return 4;
            }
            return 2;
        }
        return 1;
    }

    private static Integer j(String str) {
        int i8;
        if (str == null) {
            return null;
        }
        char c9 = 65535;
        switch (str.hashCode()) {
            case 1537:
                if (str.equals("01")) {
                    c9 = 0;
                    break;
                }
                break;
            case 1538:
                if (str.equals("02")) {
                    c9 = 1;
                    break;
                }
                break;
            case 1539:
                if (str.equals("03")) {
                    c9 = 2;
                    break;
                }
                break;
            case 1540:
                if (str.equals("04")) {
                    c9 = 3;
                    break;
                }
                break;
            case 1541:
                if (str.equals("05")) {
                    c9 = 4;
                    break;
                }
                break;
            case 1542:
                if (str.equals("06")) {
                    c9 = 5;
                    break;
                }
                break;
            case 1543:
                if (str.equals("07")) {
                    c9 = 6;
                    break;
                }
                break;
            case 1544:
                if (str.equals("08")) {
                    c9 = 7;
                    break;
                }
                break;
            case 1545:
                if (str.equals("09")) {
                    c9 = '\b';
                    break;
                }
                break;
            case 1567:
                if (str.equals("10")) {
                    c9 = '\t';
                    break;
                }
                break;
            case 1568:
                if (str.equals("11")) {
                    c9 = '\n';
                    break;
                }
                break;
            case 1569:
                if (str.equals("12")) {
                    c9 = 11;
                    break;
                }
                break;
            case 1570:
                if (str.equals("13")) {
                    c9 = '\f';
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 8;
            case 4:
                i8 = 16;
                break;
            case 5:
                i8 = 32;
                break;
            case 6:
                i8 = 64;
                break;
            case 7:
                i8 = RecognitionOptions.ITF;
                break;
            case '\b':
                i8 = RecognitionOptions.QR_CODE;
                break;
            case '\t':
                i8 = RecognitionOptions.UPC_A;
                break;
            case '\n':
                i8 = RecognitionOptions.UPC_E;
                break;
            case 11:
                i8 = RecognitionOptions.PDF417;
                break;
            case '\f':
                i8 = RecognitionOptions.AZTEC;
                break;
            default:
                return null;
        }
        return Integer.valueOf(i8);
    }

    private static Integer k(String str) {
        int i8;
        if (str == null) {
            return null;
        }
        char c9 = 65535;
        switch (str.hashCode()) {
            case 1536:
                if (str.equals("00")) {
                    c9 = 0;
                    break;
                }
                break;
            case 1537:
                if (str.equals("01")) {
                    c9 = 1;
                    break;
                }
                break;
            case 1538:
                if (str.equals("02")) {
                    c9 = 2;
                    break;
                }
                break;
            case 1539:
                if (str.equals("03")) {
                    c9 = 3;
                    break;
                }
                break;
            case 1540:
                if (str.equals("04")) {
                    c9 = 4;
                    break;
                }
                break;
            case 1541:
                if (str.equals("05")) {
                    c9 = 5;
                    break;
                }
                break;
            case 1542:
                if (str.equals("06")) {
                    c9 = 6;
                    break;
                }
                break;
            case 1543:
                if (str.equals("07")) {
                    c9 = 7;
                    break;
                }
                break;
            case 1544:
                if (str.equals("08")) {
                    c9 = '\b';
                    break;
                }
                break;
            case 1545:
                if (str.equals("09")) {
                    c9 = '\t';
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 8;
            case 4:
                i8 = 16;
                break;
            case 5:
                i8 = 32;
                break;
            case 6:
                i8 = 64;
                break;
            case 7:
                i8 = RecognitionOptions.ITF;
                break;
            case '\b':
                i8 = RecognitionOptions.QR_CODE;
                break;
            case '\t':
                i8 = RecognitionOptions.UPC_A;
                break;
            default:
                return null;
        }
        return Integer.valueOf(i8);
    }

    private static Pair<Integer, Integer> l(String str, String[] strArr) {
        int O;
        if (strArr.length != 3) {
            b6.p.i("MediaCodecUtil", "Ignoring malformed MP4A codec string: " + str);
            return null;
        }
        try {
            if ("audio/mp4a-latm".equals(t.h(Integer.parseInt(strArr[1], 16))) && (O = O(Integer.parseInt(strArr[2]))) != -1) {
                return new Pair<>(Integer.valueOf(O), 0);
            }
        } catch (NumberFormatException unused) {
            b6.p.i("MediaCodecUtil", "Ignoring malformed MP4A codec string: " + str);
        }
        return null;
    }

    public static String m(w0 w0Var) {
        Pair<Integer, Integer> q;
        if ("audio/eac3-joc".equals(w0Var.f11207m)) {
            return "audio/eac3";
        }
        if (!"video/dolby-vision".equals(w0Var.f11207m) || (q = q(w0Var)) == null) {
            return null;
        }
        int intValue = ((Integer) q.first).intValue();
        if (intValue == 16 || intValue == 256) {
            return "video/hevc";
        }
        if (intValue == 512) {
            return "video/avc";
        }
        return null;
    }

    private static Pair<Integer, Integer> n(String str, String[] strArr, c6.c cVar) {
        StringBuilder sb;
        int parseInt;
        int parseInt2;
        int parseInt3;
        StringBuilder sb2;
        int i8;
        String sb3;
        if (strArr.length >= 4) {
            try {
                parseInt = Integer.parseInt(strArr[1]);
                parseInt2 = Integer.parseInt(strArr[2].substring(0, 2));
                parseInt3 = Integer.parseInt(strArr[3]);
            } catch (NumberFormatException unused) {
                sb = new StringBuilder();
            }
            if (parseInt != 0) {
                sb2 = new StringBuilder();
                sb2.append("Unknown AV1 profile: ");
                sb2.append(parseInt);
            } else if (parseInt3 != 8 && parseInt3 != 10) {
                sb = new StringBuilder();
                sb.append("Unknown AV1 bit depth: ");
                sb.append(parseInt3);
                sb3 = sb.toString();
                b6.p.i("MediaCodecUtil", sb3);
                return null;
            } else {
                int i9 = parseInt3 != 8 ? (cVar == null || !(cVar.f8341d != null || (i8 = cVar.f8340c) == 7 || i8 == 6)) ? 2 : RecognitionOptions.AZTEC : 1;
                int f5 = f(parseInt2);
                if (f5 != -1) {
                    return new Pair<>(Integer.valueOf(i9), Integer.valueOf(f5));
                }
                sb2 = new StringBuilder();
                sb2.append("Unknown AV1 level: ");
                sb2.append(parseInt2);
            }
            sb3 = sb2.toString();
            b6.p.i("MediaCodecUtil", sb3);
            return null;
        }
        sb = new StringBuilder();
        sb.append("Ignoring malformed AV1 codec string: ");
        sb.append(str);
        sb3 = sb.toString();
        b6.p.i("MediaCodecUtil", sb3);
        return null;
    }

    private static Pair<Integer, Integer> o(String str, String[] strArr) {
        StringBuilder sb;
        int parseInt;
        int i8;
        int i9;
        String str2;
        if (strArr.length >= 2) {
            try {
                if (strArr[1].length() == 6) {
                    i8 = Integer.parseInt(strArr[1].substring(0, 2), 16);
                    parseInt = Integer.parseInt(strArr[1].substring(4), 16);
                } else if (strArr.length < 3) {
                    b6.p.i("MediaCodecUtil", "Ignoring malformed AVC codec string: " + str);
                    return null;
                } else {
                    int parseInt2 = Integer.parseInt(strArr[1]);
                    parseInt = Integer.parseInt(strArr[2]);
                    i8 = parseInt2;
                }
                i9 = i(i8);
            } catch (NumberFormatException unused) {
                sb = new StringBuilder();
            }
            if (i9 == -1) {
                str2 = "Unknown AVC profile: " + i8;
                b6.p.i("MediaCodecUtil", str2);
                return null;
            }
            int g8 = g(parseInt);
            if (g8 == -1) {
                sb = new StringBuilder();
                sb.append("Unknown AVC level: ");
                sb.append(parseInt);
                str2 = sb.toString();
                b6.p.i("MediaCodecUtil", str2);
                return null;
            }
            return new Pair<>(Integer.valueOf(i9), Integer.valueOf(g8));
        }
        sb = new StringBuilder();
        sb.append("Ignoring malformed AVC codec string: ");
        sb.append(str);
        str2 = sb.toString();
        b6.p.i("MediaCodecUtil", str2);
        return null;
    }

    private static String p(MediaCodecInfo mediaCodecInfo, String str, String str2) {
        String[] supportedTypes;
        for (String str3 : mediaCodecInfo.getSupportedTypes()) {
            if (str3.equalsIgnoreCase(str2)) {
                return str3;
            }
        }
        if (str2.equals("video/dolby-vision")) {
            if ("OMX.MS.HEVCDV.Decoder".equals(str)) {
                return "video/hevcdv";
            }
            if ("OMX.RTK.video.decoder".equals(str) || "OMX.realtek.video.decoder.tunneled".equals(str)) {
                return "video/dv_hevc";
            }
            return null;
        } else if (str2.equals("audio/alac") && "OMX.lge.alac.decoder".equals(str)) {
            return "audio/x-lg-alac";
        } else {
            if (str2.equals("audio/flac") && "OMX.lge.flac.decoder".equals(str)) {
                return "audio/x-lg-flac";
            }
            if (str2.equals("audio/ac3") && "OMX.lge.ac3.decoder".equals(str)) {
                return "audio/lg-ac3";
            }
            return null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0075, code lost:
        if (r3.equals("av01") == false) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static android.util.Pair<java.lang.Integer, java.lang.Integer> q(com.google.android.exoplayer2.w0 r6) {
        /*
            java.lang.String r0 = r6.f11204j
            r1 = 0
            if (r0 != 0) goto L6
            return r1
        L6:
            java.lang.String r2 = "\\."
            java.lang.String[] r0 = r0.split(r2)
            java.lang.String r2 = r6.f11207m
            java.lang.String r3 = "video/dolby-vision"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L1d
            java.lang.String r6 = r6.f11204j
            android.util.Pair r6 = w(r6, r0)
            return r6
        L1d:
            r2 = 0
            r3 = r0[r2]
            r3.hashCode()
            r4 = -1
            int r5 = r3.hashCode()
            switch(r5) {
                case 3004662: goto L6f;
                case 3006243: goto L64;
                case 3006244: goto L59;
                case 3199032: goto L4e;
                case 3214780: goto L43;
                case 3356560: goto L38;
                case 3624515: goto L2d;
                default: goto L2b;
            }
        L2b:
            r2 = r4
            goto L78
        L2d:
            java.lang.String r2 = "vp09"
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto L36
            goto L2b
        L36:
            r2 = 6
            goto L78
        L38:
            java.lang.String r2 = "mp4a"
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto L41
            goto L2b
        L41:
            r2 = 5
            goto L78
        L43:
            java.lang.String r2 = "hvc1"
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto L4c
            goto L2b
        L4c:
            r2 = 4
            goto L78
        L4e:
            java.lang.String r2 = "hev1"
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto L57
            goto L2b
        L57:
            r2 = 3
            goto L78
        L59:
            java.lang.String r2 = "avc2"
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto L62
            goto L2b
        L62:
            r2 = 2
            goto L78
        L64:
            java.lang.String r2 = "avc1"
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto L6d
            goto L2b
        L6d:
            r2 = 1
            goto L78
        L6f:
            java.lang.String r5 = "av01"
            boolean r3 = r3.equals(r5)
            if (r3 != 0) goto L78
            goto L2b
        L78:
            switch(r2) {
                case 0: goto L9a;
                case 1: goto L93;
                case 2: goto L93;
                case 3: goto L8a;
                case 4: goto L8a;
                case 5: goto L83;
                case 6: goto L7c;
                default: goto L7b;
            }
        L7b:
            return r1
        L7c:
            java.lang.String r6 = r6.f11204j
            android.util.Pair r6 = y(r6, r0)
            return r6
        L83:
            java.lang.String r6 = r6.f11204j
            android.util.Pair r6 = l(r6, r0)
            return r6
        L8a:
            java.lang.String r1 = r6.f11204j
            c6.c r6 = r6.E
            android.util.Pair r6 = x(r1, r0, r6)
            return r6
        L93:
            java.lang.String r6 = r6.f11204j
            android.util.Pair r6 = o(r6, r0)
            return r6
        L9a:
            java.lang.String r1 = r6.f11204j
            c6.c r6 = r6.E
            android.util.Pair r6 = n(r1, r0, r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.q(com.google.android.exoplayer2.w0):android.util.Pair");
    }

    public static k r(String str, boolean z4, boolean z8) {
        List<k> s8 = s(str, z4, z8);
        if (s8.isEmpty()) {
            return null;
        }
        return s8.get(0);
    }

    public static synchronized List<k> s(String str, boolean z4, boolean z8) {
        synchronized (MediaCodecUtil.class) {
            b bVar = new b(str, z4, z8);
            HashMap<b, List<k>> hashMap = f9964b;
            List<k> list = hashMap.get(bVar);
            if (list != null) {
                return list;
            }
            int i8 = l0.f8063a;
            ArrayList<k> t8 = t(bVar, i8 >= 21 ? new e(z4, z8) : new d());
            if (z4 && t8.isEmpty() && 21 <= i8 && i8 <= 23) {
                t8 = t(bVar, new d());
                if (!t8.isEmpty()) {
                    b6.p.i("MediaCodecUtil", "MediaCodecList API didn't list secure decoder for: " + str + ". Assuming: " + t8.get(0).f10030a);
                }
            }
            e(str, t8);
            ImmutableList x8 = ImmutableList.x(t8);
            hashMap.put(bVar, x8);
            return x8;
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(7:28|(4:(2:72|73)|53|(9:56|57|58|59|60|61|62|64|65)|9)|32|33|34|36|9) */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x007f, code lost:
        if (r1.f9967b == false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00a4, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00a5, code lost:
        r1 = r11;
     */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0101 A[Catch: Exception -> 0x014f, TRY_ENTER, TryCatch #1 {Exception -> 0x014f, blocks: (B:3:0x0008, B:5:0x001b, B:60:0x0120, B:8:0x002d, B:11:0x0038, B:54:0x00f9, B:57:0x0101, B:59:0x0107, B:61:0x012a, B:62:0x014d), top: B:69:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x012a A[ADDED_TO_REGION, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.util.ArrayList<com.google.android.exoplayer2.mediacodec.k> t(com.google.android.exoplayer2.mediacodec.MediaCodecUtil.b r24, com.google.android.exoplayer2.mediacodec.MediaCodecUtil.c r25) {
        /*
            Method dump skipped, instructions count: 343
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.t(com.google.android.exoplayer2.mediacodec.MediaCodecUtil$b, com.google.android.exoplayer2.mediacodec.MediaCodecUtil$c):java.util.ArrayList");
    }

    public static List<k> u(List<k> list, final w0 w0Var) {
        ArrayList arrayList = new ArrayList(list);
        P(arrayList, new f() { // from class: com.google.android.exoplayer2.mediacodec.m
            @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.f
            public final int a(Object obj) {
                int L;
                L = MediaCodecUtil.L(w0.this, (k) obj);
                return L;
            }
        });
        return arrayList;
    }

    public static k v() {
        return r("audio/raw", false, false);
    }

    private static Pair<Integer, Integer> w(String str, String[] strArr) {
        StringBuilder sb;
        String str2;
        if (strArr.length < 3) {
            sb = new StringBuilder();
        } else {
            Matcher matcher = f9963a.matcher(strArr[1]);
            if (matcher.matches()) {
                str = matcher.group(1);
                Integer k8 = k(str);
                if (k8 == null) {
                    sb = new StringBuilder();
                    str2 = "Unknown Dolby Vision profile string: ";
                } else {
                    str = strArr[2];
                    Integer j8 = j(str);
                    if (j8 != null) {
                        return new Pair<>(k8, j8);
                    }
                    sb = new StringBuilder();
                    str2 = "Unknown Dolby Vision level string: ";
                }
                sb.append(str2);
                sb.append(str);
                b6.p.i("MediaCodecUtil", sb.toString());
                return null;
            }
            sb = new StringBuilder();
        }
        sb.append("Ignoring malformed Dolby Vision codec string: ");
        sb.append(str);
        b6.p.i("MediaCodecUtil", sb.toString());
        return null;
    }

    private static Pair<Integer, Integer> x(String str, String[] strArr, c6.c cVar) {
        StringBuilder sb;
        String str2;
        if (strArr.length < 4) {
            sb = new StringBuilder();
        } else {
            int i8 = 1;
            Matcher matcher = f9963a.matcher(strArr[1]);
            if (matcher.matches()) {
                str = matcher.group(1);
                if (!"1".equals(str)) {
                    if (!"2".equals(str)) {
                        sb = new StringBuilder();
                        str2 = "Unknown HEVC profile string: ";
                        sb.append(str2);
                        sb.append(str);
                        b6.p.i("MediaCodecUtil", sb.toString());
                        return null;
                    }
                    i8 = (cVar == null || cVar.f8340c != 6) ? 2 : RecognitionOptions.AZTEC;
                }
                str = strArr[3];
                Integer z4 = z(str);
                if (z4 == null) {
                    sb = new StringBuilder();
                    str2 = "Unknown HEVC level string: ";
                    sb.append(str2);
                    sb.append(str);
                    b6.p.i("MediaCodecUtil", sb.toString());
                    return null;
                }
                return new Pair<>(Integer.valueOf(i8), z4);
            }
            sb = new StringBuilder();
        }
        sb.append("Ignoring malformed HEVC codec string: ");
        sb.append(str);
        b6.p.i("MediaCodecUtil", sb.toString());
        return null;
    }

    private static Pair<Integer, Integer> y(String str, String[] strArr) {
        StringBuilder sb;
        int parseInt;
        int parseInt2;
        int R;
        String str2;
        if (strArr.length >= 3) {
            try {
                parseInt = Integer.parseInt(strArr[1]);
                parseInt2 = Integer.parseInt(strArr[2]);
                R = R(parseInt);
            } catch (NumberFormatException unused) {
                sb = new StringBuilder();
            }
            if (R == -1) {
                str2 = "Unknown VP9 profile: " + parseInt;
                b6.p.i("MediaCodecUtil", str2);
                return null;
            }
            int Q = Q(parseInt2);
            if (Q == -1) {
                sb = new StringBuilder();
                sb.append("Unknown VP9 level: ");
                sb.append(parseInt2);
                str2 = sb.toString();
                b6.p.i("MediaCodecUtil", str2);
                return null;
            }
            return new Pair<>(Integer.valueOf(R), Integer.valueOf(Q));
        }
        sb = new StringBuilder();
        sb.append("Ignoring malformed VP9 codec string: ");
        sb.append(str);
        str2 = sb.toString();
        b6.p.i("MediaCodecUtil", str2);
        return null;
    }

    private static Integer z(String str) {
        int i8;
        if (str == null) {
            return null;
        }
        char c9 = 65535;
        switch (str.hashCode()) {
            case 70821:
                if (str.equals("H30")) {
                    c9 = 0;
                    break;
                }
                break;
            case 70914:
                if (str.equals("H60")) {
                    c9 = 1;
                    break;
                }
                break;
            case 70917:
                if (str.equals("H63")) {
                    c9 = 2;
                    break;
                }
                break;
            case 71007:
                if (str.equals("H90")) {
                    c9 = 3;
                    break;
                }
                break;
            case 71010:
                if (str.equals("H93")) {
                    c9 = 4;
                    break;
                }
                break;
            case 74665:
                if (str.equals("L30")) {
                    c9 = 5;
                    break;
                }
                break;
            case 74758:
                if (str.equals("L60")) {
                    c9 = 6;
                    break;
                }
                break;
            case 74761:
                if (str.equals("L63")) {
                    c9 = 7;
                    break;
                }
                break;
            case 74851:
                if (str.equals("L90")) {
                    c9 = '\b';
                    break;
                }
                break;
            case 74854:
                if (str.equals("L93")) {
                    c9 = '\t';
                    break;
                }
                break;
            case 2193639:
                if (str.equals("H120")) {
                    c9 = '\n';
                    break;
                }
                break;
            case 2193642:
                if (str.equals("H123")) {
                    c9 = 11;
                    break;
                }
                break;
            case 2193732:
                if (str.equals("H150")) {
                    c9 = '\f';
                    break;
                }
                break;
            case 2193735:
                if (str.equals("H153")) {
                    c9 = '\r';
                    break;
                }
                break;
            case 2193738:
                if (str.equals("H156")) {
                    c9 = 14;
                    break;
                }
                break;
            case 2193825:
                if (str.equals("H180")) {
                    c9 = 15;
                    break;
                }
                break;
            case 2193828:
                if (str.equals("H183")) {
                    c9 = 16;
                    break;
                }
                break;
            case 2193831:
                if (str.equals("H186")) {
                    c9 = 17;
                    break;
                }
                break;
            case 2312803:
                if (str.equals("L120")) {
                    c9 = 18;
                    break;
                }
                break;
            case 2312806:
                if (str.equals("L123")) {
                    c9 = 19;
                    break;
                }
                break;
            case 2312896:
                if (str.equals("L150")) {
                    c9 = 20;
                    break;
                }
                break;
            case 2312899:
                if (str.equals("L153")) {
                    c9 = 21;
                    break;
                }
                break;
            case 2312902:
                if (str.equals("L156")) {
                    c9 = 22;
                    break;
                }
                break;
            case 2312989:
                if (str.equals("L180")) {
                    c9 = 23;
                    break;
                }
                break;
            case 2312992:
                if (str.equals("L183")) {
                    c9 = 24;
                    break;
                }
                break;
            case 2312995:
                if (str.equals("L186")) {
                    c9 = 25;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return 2;
            case 1:
                return 8;
            case 2:
                i8 = 32;
                break;
            case 3:
                i8 = RecognitionOptions.ITF;
                break;
            case 4:
                i8 = RecognitionOptions.UPC_A;
                break;
            case 5:
                return 1;
            case 6:
                return 4;
            case 7:
                return 16;
            case '\b':
                i8 = 64;
                break;
            case '\t':
                i8 = RecognitionOptions.QR_CODE;
                break;
            case '\n':
                i8 = RecognitionOptions.PDF417;
                break;
            case 11:
                i8 = 8192;
                break;
            case '\f':
                i8 = RecognitionOptions.TEZ_CODE;
                break;
            case '\r':
                i8 = 131072;
                break;
            case 14:
                i8 = 524288;
                break;
            case 15:
                i8 = 2097152;
                break;
            case 16:
                i8 = 8388608;
                break;
            case 17:
                i8 = 33554432;
                break;
            case 18:
                i8 = RecognitionOptions.UPC_E;
                break;
            case 19:
                i8 = RecognitionOptions.AZTEC;
                break;
            case 20:
                i8 = 16384;
                break;
            case 21:
                i8 = 65536;
                break;
            case 22:
                i8 = 262144;
                break;
            case 23:
                i8 = 1048576;
                break;
            case 24:
                i8 = 4194304;
                break;
            case 25:
                i8 = 16777216;
                break;
            default:
                return null;
        }
        return Integer.valueOf(i8);
    }
}
