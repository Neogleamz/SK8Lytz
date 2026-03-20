package b6;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t {

    /* renamed from: a  reason: collision with root package name */
    private static final ArrayList<a> f8102a = new ArrayList<>();

    /* renamed from: b  reason: collision with root package name */
    private static final Pattern f8103b = Pattern.compile("^mp4a\\.([a-zA-Z0-9]{2})(?:\\.([0-9]{1,2}))?$");

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final String f8104a;

        /* renamed from: b  reason: collision with root package name */
        public final String f8105b;

        /* renamed from: c  reason: collision with root package name */
        public final int f8106c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final int f8107a;

        /* renamed from: b  reason: collision with root package name */
        public final int f8108b;

        public b(int i8, int i9) {
            this.f8107a = i8;
            this.f8108b = i9;
        }

        public int a() {
            int i8 = this.f8108b;
            if (i8 != 2) {
                if (i8 != 5) {
                    if (i8 != 29) {
                        if (i8 != 42) {
                            if (i8 != 22) {
                                return i8 != 23 ? 0 : 15;
                            }
                            return 1073741824;
                        }
                        return 16;
                    }
                    return 12;
                }
                return 11;
            }
            return 10;
        }
    }

    public static boolean a(String str, String str2) {
        b i8;
        int a9;
        if (str == null) {
            return false;
        }
        char c9 = 65535;
        switch (str.hashCode()) {
            case -2123537834:
                if (str.equals("audio/eac3-joc")) {
                    c9 = 0;
                    break;
                }
                break;
            case -432837260:
                if (str.equals("audio/mpeg-L1")) {
                    c9 = 1;
                    break;
                }
                break;
            case -432837259:
                if (str.equals("audio/mpeg-L2")) {
                    c9 = 2;
                    break;
                }
                break;
            case -53558318:
                if (str.equals("audio/mp4a-latm")) {
                    c9 = 3;
                    break;
                }
                break;
            case 187078296:
                if (str.equals("audio/ac3")) {
                    c9 = 4;
                    break;
                }
                break;
            case 187094639:
                if (str.equals("audio/raw")) {
                    c9 = 5;
                    break;
                }
                break;
            case 1504578661:
                if (str.equals("audio/eac3")) {
                    c9 = 6;
                    break;
                }
                break;
            case 1504619009:
                if (str.equals("audio/flac")) {
                    c9 = 7;
                    break;
                }
                break;
            case 1504831518:
                if (str.equals("audio/mpeg")) {
                    c9 = '\b';
                    break;
                }
                break;
            case 1903231877:
                if (str.equals("audio/g711-alaw")) {
                    c9 = '\t';
                    break;
                }
                break;
            case 1903589369:
                if (str.equals("audio/g711-mlaw")) {
                    c9 = '\n';
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
            case 1:
            case 2:
            case 4:
            case 5:
            case 6:
            case 7:
            case '\b':
            case '\t':
            case '\n':
                return true;
            case 3:
                return (str2 == null || (i8 = i(str2)) == null || (a9 = i8.a()) == 0 || a9 == 16) ? false : true;
            default:
                return false;
        }
    }

    public static boolean b(String str, String str2) {
        return d(str, str2) != null;
    }

    public static String c(String str) {
        if (str == null) {
            return null;
        }
        for (String str2 : l0.T0(str)) {
            String g8 = g(str2);
            if (g8 != null && o(g8)) {
                return g8;
            }
        }
        return null;
    }

    public static String d(String str, String str2) {
        if (str == null || str2 == null) {
            return null;
        }
        String[] T0 = l0.T0(str);
        StringBuilder sb = new StringBuilder();
        for (String str3 : T0) {
            if (str2.equals(g(str3))) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(str3);
            }
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }

    private static String e(String str) {
        int size = f8102a.size();
        for (int i8 = 0; i8 < size; i8++) {
            a aVar = f8102a.get(i8);
            if (str.startsWith(aVar.f8105b)) {
                return aVar.f8104a;
            }
        }
        return null;
    }

    public static int f(String str, String str2) {
        b i8;
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -2123537834:
                if (str.equals("audio/eac3-joc")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1095064472:
                if (str.equals("audio/vnd.dts")) {
                    c9 = 1;
                    break;
                }
                break;
            case -53558318:
                if (str.equals("audio/mp4a-latm")) {
                    c9 = 2;
                    break;
                }
                break;
            case 187078296:
                if (str.equals("audio/ac3")) {
                    c9 = 3;
                    break;
                }
                break;
            case 187078297:
                if (str.equals("audio/ac4")) {
                    c9 = 4;
                    break;
                }
                break;
            case 1504578661:
                if (str.equals("audio/eac3")) {
                    c9 = 5;
                    break;
                }
                break;
            case 1504831518:
                if (str.equals("audio/mpeg")) {
                    c9 = 6;
                    break;
                }
                break;
            case 1504891608:
                if (str.equals("audio/opus")) {
                    c9 = 7;
                    break;
                }
                break;
            case 1505942594:
                if (str.equals("audio/vnd.dts.hd")) {
                    c9 = '\b';
                    break;
                }
                break;
            case 1556697186:
                if (str.equals("audio/true-hd")) {
                    c9 = '\t';
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return 18;
            case 1:
                return 7;
            case 2:
                if (str2 == null || (i8 = i(str2)) == null) {
                    return 0;
                }
                return i8.a();
            case 3:
                return 5;
            case 4:
                return 17;
            case 5:
                return 6;
            case 6:
                return 9;
            case 7:
                return 20;
            case '\b':
                return 8;
            case '\t':
                return 14;
            default:
                return 0;
        }
    }

    public static String g(String str) {
        b i8;
        String str2 = null;
        if (str == null) {
            return null;
        }
        String e8 = com.google.common.base.c.e(str.trim());
        if (e8.startsWith("avc1") || e8.startsWith("avc3")) {
            return "video/avc";
        }
        if (e8.startsWith("hev1") || e8.startsWith("hvc1")) {
            return "video/hevc";
        }
        if (e8.startsWith("dvav") || e8.startsWith("dva1") || e8.startsWith("dvhe") || e8.startsWith("dvh1")) {
            return "video/dolby-vision";
        }
        if (e8.startsWith("av01")) {
            return "video/av01";
        }
        if (e8.startsWith("vp9") || e8.startsWith("vp09")) {
            return "video/x-vnd.on2.vp9";
        }
        if (e8.startsWith("vp8") || e8.startsWith("vp08")) {
            return "video/x-vnd.on2.vp8";
        }
        if (!e8.startsWith("mp4a")) {
            return e8.startsWith("mha1") ? "audio/mha1" : e8.startsWith("mhm1") ? "audio/mhm1" : (e8.startsWith("ac-3") || e8.startsWith("dac3")) ? "audio/ac3" : (e8.startsWith("ec-3") || e8.startsWith("dec3")) ? "audio/eac3" : e8.startsWith("ec+3") ? "audio/eac3-joc" : (e8.startsWith("ac-4") || e8.startsWith("dac4")) ? "audio/ac4" : e8.startsWith("dtsc") ? "audio/vnd.dts" : e8.startsWith("dtse") ? "audio/vnd.dts.hd;profile=lbr" : (e8.startsWith("dtsh") || e8.startsWith("dtsl")) ? "audio/vnd.dts.hd" : e8.startsWith("dtsx") ? "audio/vnd.dts.uhd;profile=p2" : e8.startsWith("opus") ? "audio/opus" : e8.startsWith("vorbis") ? "audio/vorbis" : e8.startsWith("flac") ? "audio/flac" : e8.startsWith("stpp") ? "application/ttml+xml" : e8.startsWith("wvtt") ? "text/vtt" : e8.contains("cea708") ? "application/cea-708" : (e8.contains("eia608") || e8.contains("cea608")) ? "application/cea-608" : e(e8);
        }
        if (e8.startsWith("mp4a.") && (i8 = i(e8)) != null) {
            str2 = h(i8.f8107a);
        }
        return str2 == null ? "audio/mp4a-latm" : str2;
    }

    public static String h(int i8) {
        if (i8 != 32) {
            if (i8 != 33) {
                if (i8 != 35) {
                    if (i8 != 64) {
                        if (i8 != 163) {
                            if (i8 != 177) {
                                if (i8 != 165) {
                                    if (i8 != 166) {
                                        switch (i8) {
                                            case 96:
                                            case 97:
                                            case 98:
                                            case 99:
                                            case 100:
                                            case 101:
                                                return "video/mpeg2";
                                            case 102:
                                            case 103:
                                            case 104:
                                                return "audio/mp4a-latm";
                                            case 105:
                                            case 107:
                                                return "audio/mpeg";
                                            case 106:
                                                return "video/mpeg";
                                            default:
                                                switch (i8) {
                                                    case 169:
                                                    case 172:
                                                        return "audio/vnd.dts";
                                                    case 170:
                                                    case 171:
                                                        return "audio/vnd.dts.hd";
                                                    case 173:
                                                        return "audio/opus";
                                                    case 174:
                                                        return "audio/ac4";
                                                    default:
                                                        return null;
                                                }
                                        }
                                    }
                                    return "audio/eac3";
                                }
                                return "audio/ac3";
                            }
                            return "video/x-vnd.on2.vp9";
                        }
                        return "video/wvc1";
                    }
                    return "audio/mp4a-latm";
                }
                return "video/hevc";
            }
            return "video/avc";
        }
        return "video/mp4v-es";
    }

    static b i(String str) {
        Matcher matcher = f8103b.matcher(str);
        if (matcher.matches()) {
            String str2 = (String) b6.a.e(matcher.group(1));
            String group = matcher.group(2);
            try {
                return new b(Integer.parseInt(str2, 16), group != null ? Integer.parseInt(group) : 0);
            } catch (NumberFormatException unused) {
                return null;
            }
        }
        return null;
    }

    private static String j(String str) {
        int indexOf;
        if (str == null || (indexOf = str.indexOf(47)) == -1) {
            return null;
        }
        return str.substring(0, indexOf);
    }

    public static int k(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        if (o(str)) {
            return 1;
        }
        if (s(str)) {
            return 2;
        }
        if (r(str)) {
            return 3;
        }
        if (p(str)) {
            return 4;
        }
        if ("application/id3".equals(str) || "application/x-emsg".equals(str) || "application/x-scte35".equals(str)) {
            return 5;
        }
        if ("application/x-camera-motion".equals(str)) {
            return 6;
        }
        return l(str);
    }

    private static int l(String str) {
        int size = f8102a.size();
        for (int i8 = 0; i8 < size; i8++) {
            a aVar = f8102a.get(i8);
            if (str.equals(aVar.f8104a)) {
                return aVar.f8106c;
            }
        }
        return -1;
    }

    public static int m(String str) {
        return k(g(str));
    }

    public static String n(String str) {
        if (str == null) {
            return null;
        }
        for (String str2 : l0.T0(str)) {
            String g8 = g(str2);
            if (g8 != null && s(g8)) {
                return g8;
            }
        }
        return null;
    }

    public static boolean o(String str) {
        return "audio".equals(j(str));
    }

    public static boolean p(String str) {
        return "image".equals(j(str));
    }

    public static boolean q(String str) {
        if (str == null) {
            return false;
        }
        return str.startsWith("video/webm") || str.startsWith("audio/webm") || str.startsWith("application/webm") || str.startsWith("video/x-matroska") || str.startsWith("audio/x-matroska") || str.startsWith("application/x-matroska");
    }

    public static boolean r(String str) {
        return "text".equals(j(str)) || "application/cea-608".equals(str) || "application/cea-708".equals(str) || "application/x-mp4-cea-608".equals(str) || "application/x-subrip".equals(str) || "application/ttml+xml".equals(str) || "application/x-quicktime-tx3g".equals(str) || "application/x-mp4-vtt".equals(str) || "application/x-rawcc".equals(str) || "application/vobsub".equals(str) || "application/pgs".equals(str) || "application/dvbsubs".equals(str);
    }

    public static boolean s(String str) {
        return "video".equals(j(str));
    }

    public static String t(String str) {
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1007807498:
                if (str.equals("audio/x-flac")) {
                    c9 = 0;
                    break;
                }
                break;
            case -586683234:
                if (str.equals("audio/x-wav")) {
                    c9 = 1;
                    break;
                }
                break;
            case 187090231:
                if (str.equals("audio/mp3")) {
                    c9 = 2;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return "audio/flac";
            case 1:
                return "audio/wav";
            case 2:
                return "audio/mpeg";
            default:
                return str;
        }
    }
}
