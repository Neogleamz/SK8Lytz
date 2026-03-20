package com.google.android.exoplayer2.source.hls.playlist;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import b6.j0;
import b6.l0;
import b6.p;
import b6.t;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.hls.HlsTrackMetadataEntry;
import com.google.android.exoplayer2.source.hls.playlist.d;
import com.google.android.exoplayer2.source.hls.playlist.e;
import com.google.android.exoplayer2.upstream.d;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import v4.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class HlsPlaylistParser implements d.a<n5.d> {

    /* renamed from: a  reason: collision with root package name */
    private final e f10508a;

    /* renamed from: b  reason: collision with root package name */
    private final d f10509b;

    /* renamed from: c  reason: collision with root package name */
    private static final Pattern f10485c = Pattern.compile("AVERAGE-BANDWIDTH=(\\d+)\\b");

    /* renamed from: d  reason: collision with root package name */
    private static final Pattern f10486d = Pattern.compile("VIDEO=\"(.+?)\"");

    /* renamed from: e  reason: collision with root package name */
    private static final Pattern f10487e = Pattern.compile("AUDIO=\"(.+?)\"");

    /* renamed from: f  reason: collision with root package name */
    private static final Pattern f10488f = Pattern.compile("SUBTITLES=\"(.+?)\"");

    /* renamed from: g  reason: collision with root package name */
    private static final Pattern f10489g = Pattern.compile("CLOSED-CAPTIONS=\"(.+?)\"");

    /* renamed from: h  reason: collision with root package name */
    private static final Pattern f10490h = Pattern.compile("[^-]BANDWIDTH=(\\d+)\\b");

    /* renamed from: i  reason: collision with root package name */
    private static final Pattern f10491i = Pattern.compile("CHANNELS=\"(.+?)\"");

    /* renamed from: j  reason: collision with root package name */
    private static final Pattern f10492j = Pattern.compile("CODECS=\"(.+?)\"");

    /* renamed from: k  reason: collision with root package name */
    private static final Pattern f10493k = Pattern.compile("RESOLUTION=(\\d+x\\d+)");

    /* renamed from: l  reason: collision with root package name */
    private static final Pattern f10494l = Pattern.compile("FRAME-RATE=([\\d\\.]+)\\b");

    /* renamed from: m  reason: collision with root package name */
    private static final Pattern f10495m = Pattern.compile("#EXT-X-TARGETDURATION:(\\d+)\\b");

    /* renamed from: n  reason: collision with root package name */
    private static final Pattern f10496n = Pattern.compile("DURATION=([\\d\\.]+)\\b");

    /* renamed from: o  reason: collision with root package name */
    private static final Pattern f10497o = Pattern.compile("PART-TARGET=([\\d\\.]+)\\b");

    /* renamed from: p  reason: collision with root package name */
    private static final Pattern f10498p = Pattern.compile("#EXT-X-VERSION:(\\d+)\\b");
    private static final Pattern q = Pattern.compile("#EXT-X-PLAYLIST-TYPE:(.+)\\b");

    /* renamed from: r  reason: collision with root package name */
    private static final Pattern f10499r = Pattern.compile("CAN-SKIP-UNTIL=([\\d\\.]+)\\b");

    /* renamed from: s  reason: collision with root package name */
    private static final Pattern f10500s = c("CAN-SKIP-DATERANGES");

    /* renamed from: t  reason: collision with root package name */
    private static final Pattern f10501t = Pattern.compile("SKIPPED-SEGMENTS=(\\d+)\\b");

    /* renamed from: u  reason: collision with root package name */
    private static final Pattern f10502u = Pattern.compile("[:|,]HOLD-BACK=([\\d\\.]+)\\b");

    /* renamed from: v  reason: collision with root package name */
    private static final Pattern f10503v = Pattern.compile("PART-HOLD-BACK=([\\d\\.]+)\\b");

    /* renamed from: w  reason: collision with root package name */
    private static final Pattern f10504w = c("CAN-BLOCK-RELOAD");

    /* renamed from: x  reason: collision with root package name */
    private static final Pattern f10505x = Pattern.compile("#EXT-X-MEDIA-SEQUENCE:(\\d+)\\b");

    /* renamed from: y  reason: collision with root package name */
    private static final Pattern f10506y = Pattern.compile("#EXTINF:([\\d\\.]+)\\b");

    /* renamed from: z  reason: collision with root package name */
    private static final Pattern f10507z = Pattern.compile("#EXTINF:[\\d\\.]+\\b,(.+)");
    private static final Pattern A = Pattern.compile("LAST-MSN=(\\d+)\\b");
    private static final Pattern B = Pattern.compile("LAST-PART=(\\d+)\\b");
    private static final Pattern C = Pattern.compile("TIME-OFFSET=(-?[\\d\\.]+)\\b");
    private static final Pattern D = Pattern.compile("#EXT-X-BYTERANGE:(\\d+(?:@\\d+)?)\\b");
    private static final Pattern E = Pattern.compile("BYTERANGE=\"(\\d+(?:@\\d+)?)\\b\"");
    private static final Pattern F = Pattern.compile("BYTERANGE-START=(\\d+)\\b");
    private static final Pattern G = Pattern.compile("BYTERANGE-LENGTH=(\\d+)\\b");
    private static final Pattern H = Pattern.compile("METHOD=(NONE|AES-128|SAMPLE-AES|SAMPLE-AES-CENC|SAMPLE-AES-CTR)\\s*(?:,|$)");
    private static final Pattern I = Pattern.compile("KEYFORMAT=\"(.+?)\"");
    private static final Pattern J = Pattern.compile("KEYFORMATVERSIONS=\"(.+?)\"");
    private static final Pattern K = Pattern.compile("URI=\"(.+?)\"");
    private static final Pattern L = Pattern.compile("IV=([^,.*]+)");
    private static final Pattern M = Pattern.compile("TYPE=(AUDIO|VIDEO|SUBTITLES|CLOSED-CAPTIONS)");
    private static final Pattern N = Pattern.compile("TYPE=(PART|MAP)");
    private static final Pattern O = Pattern.compile("LANGUAGE=\"(.+?)\"");
    private static final Pattern P = Pattern.compile("NAME=\"(.+?)\"");
    private static final Pattern Q = Pattern.compile("GROUP-ID=\"(.+?)\"");
    private static final Pattern R = Pattern.compile("CHARACTERISTICS=\"(.+?)\"");
    private static final Pattern S = Pattern.compile("INSTREAM-ID=\"((?:CC|SERVICE)\\d+)\"");
    private static final Pattern T = c("AUTOSELECT");
    private static final Pattern U = c("DEFAULT");
    private static final Pattern V = c("FORCED");
    private static final Pattern W = c("INDEPENDENT");
    private static final Pattern X = c("GAP");
    private static final Pattern Y = c("PRECISE");
    private static final Pattern Z = Pattern.compile("VALUE=\"(.+?)\"");

    /* renamed from: a0  reason: collision with root package name */
    private static final Pattern f10483a0 = Pattern.compile("IMPORT=\"(.+?)\"");

    /* renamed from: b0  reason: collision with root package name */
    private static final Pattern f10484b0 = Pattern.compile("\\{\\$([a-zA-Z0-9\\-_]+)\\}");

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class DeltaUpdateException extends IOException {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private final BufferedReader f10510a;

        /* renamed from: b  reason: collision with root package name */
        private final Queue<String> f10511b;

        /* renamed from: c  reason: collision with root package name */
        private String f10512c;

        public a(Queue<String> queue, BufferedReader bufferedReader) {
            this.f10511b = queue;
            this.f10510a = bufferedReader;
        }

        public boolean a() {
            String trim;
            if (this.f10512c != null) {
                return true;
            }
            if (!this.f10511b.isEmpty()) {
                this.f10512c = (String) b6.a.e(this.f10511b.poll());
                return true;
            }
            do {
                String readLine = this.f10510a.readLine();
                this.f10512c = readLine;
                if (readLine == null) {
                    return false;
                }
                trim = readLine.trim();
                this.f10512c = trim;
            } while (trim.isEmpty());
            return true;
        }

        public String b() {
            if (a()) {
                String str = this.f10512c;
                this.f10512c = null;
                return str;
            }
            throw new NoSuchElementException();
        }
    }

    public HlsPlaylistParser() {
        this(e.f10585n, null);
    }

    public HlsPlaylistParser(e eVar, d dVar) {
        this.f10508a = eVar;
        this.f10509b = dVar;
    }

    private static long A(String str, Pattern pattern) {
        return new BigDecimal(z(str, pattern, Collections.emptyMap())).multiply(new BigDecimal(1000000L)).longValue();
    }

    private static String B(String str, Map<String, String> map) {
        Matcher matcher = f10484b0.matcher(str);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group(1);
            if (map.containsKey(group)) {
                matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(map.get(group)));
            }
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    private static int C(BufferedReader bufferedReader, boolean z4, int i8) {
        while (i8 != -1 && Character.isWhitespace(i8) && (z4 || !l0.v0(i8))) {
            i8 = bufferedReader.read();
        }
        return i8;
    }

    private static boolean b(BufferedReader bufferedReader) {
        int read = bufferedReader.read();
        if (read == 239) {
            if (bufferedReader.read() != 187 || bufferedReader.read() != 191) {
                return false;
            }
            read = bufferedReader.read();
        }
        int C2 = C(bufferedReader, true, read);
        for (int i8 = 0; i8 < 7; i8++) {
            if (C2 != "#EXTM3U".charAt(i8)) {
                return false;
            }
            C2 = bufferedReader.read();
        }
        return l0.v0(C(bufferedReader, false, C2));
    }

    private static Pattern c(String str) {
        return Pattern.compile(str + "=(NO|YES)");
    }

    private static DrmInitData d(String str, DrmInitData.SchemeData[] schemeDataArr) {
        DrmInitData.SchemeData[] schemeDataArr2 = new DrmInitData.SchemeData[schemeDataArr.length];
        for (int i8 = 0; i8 < schemeDataArr.length; i8++) {
            schemeDataArr2[i8] = schemeDataArr[i8].b(null);
        }
        return new DrmInitData(str, schemeDataArr2);
    }

    private static String e(long j8, String str, String str2) {
        if (str == null) {
            return null;
        }
        return str2 != null ? str2 : Long.toHexString(j8);
    }

    private static e.b f(ArrayList<e.b> arrayList, String str) {
        for (int i8 = 0; i8 < arrayList.size(); i8++) {
            e.b bVar = arrayList.get(i8);
            if (str.equals(bVar.f10603d)) {
                return bVar;
            }
        }
        return null;
    }

    private static e.b g(ArrayList<e.b> arrayList, String str) {
        for (int i8 = 0; i8 < arrayList.size(); i8++) {
            e.b bVar = arrayList.get(i8);
            if (str.equals(bVar.f10604e)) {
                return bVar;
            }
        }
        return null;
    }

    private static e.b h(ArrayList<e.b> arrayList, String str) {
        for (int i8 = 0; i8 < arrayList.size(); i8++) {
            e.b bVar = arrayList.get(i8);
            if (str.equals(bVar.f10602c)) {
                return bVar;
            }
        }
        return null;
    }

    private static double j(String str, Pattern pattern) {
        return Double.parseDouble(z(str, pattern, Collections.emptyMap()));
    }

    private static DrmInitData.SchemeData k(String str, String str2, Map<String, String> map) {
        String u8 = u(str, J, "1", map);
        if ("urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed".equals(str2)) {
            String z4 = z(str, K, map);
            return new DrmInitData.SchemeData(i4.b.f20468d, "video/mp4", Base64.decode(z4.substring(z4.indexOf(44)), 0));
        } else if ("com.widevine".equals(str2)) {
            return new DrmInitData.SchemeData(i4.b.f20468d, "hls", l0.m0(str));
        } else {
            if ("com.microsoft.playready".equals(str2) && "1".equals(u8)) {
                String z8 = z(str, K, map);
                byte[] decode = Base64.decode(z8.substring(z8.indexOf(44)), 0);
                UUID uuid = i4.b.f20469e;
                return new DrmInitData.SchemeData(uuid, "video/mp4", l.a(uuid, decode));
            }
            return null;
        }
    }

    private static String l(String str) {
        return ("SAMPLE-AES-CENC".equals(str) || "SAMPLE-AES-CTR".equals(str)) ? "cenc" : "cbcs";
    }

    private static int m(String str, Pattern pattern) {
        return Integer.parseInt(z(str, pattern, Collections.emptyMap()));
    }

    private static long n(String str, Pattern pattern) {
        return Long.parseLong(z(str, pattern, Collections.emptyMap()));
    }

    /* JADX WARN: Code restructure failed: missing block: B:66:0x01d9, code lost:
        if (r12 != null) goto L237;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static com.google.android.exoplayer2.source.hls.playlist.d o(com.google.android.exoplayer2.source.hls.playlist.e r94, com.google.android.exoplayer2.source.hls.playlist.d r95, com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser.a r96, java.lang.String r97) {
        /*
            Method dump skipped, instructions count: 1879
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser.o(com.google.android.exoplayer2.source.hls.playlist.e, com.google.android.exoplayer2.source.hls.playlist.d, com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser$a, java.lang.String):com.google.android.exoplayer2.source.hls.playlist.d");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v6, types: [java.util.List] */
    private static e p(a aVar, String str) {
        char c9;
        w0 w0Var;
        ArrayList arrayList;
        ArrayList arrayList2;
        String str2;
        ArrayList arrayList3;
        int parseInt;
        String str3;
        String str4;
        boolean z4;
        int i8;
        ArrayList arrayList4;
        ArrayList arrayList5;
        ArrayList arrayList6;
        ArrayList arrayList7;
        int i9;
        int i10;
        ArrayList arrayList8;
        ArrayList arrayList9;
        ArrayList arrayList10;
        String B2;
        HashMap hashMap;
        int i11;
        String str5 = str;
        HashMap hashMap2 = new HashMap();
        HashMap hashMap3 = new HashMap();
        ArrayList arrayList11 = new ArrayList();
        ArrayList arrayList12 = new ArrayList();
        ArrayList arrayList13 = new ArrayList();
        ArrayList arrayList14 = new ArrayList();
        ArrayList arrayList15 = new ArrayList();
        ArrayList arrayList16 = new ArrayList();
        ArrayList arrayList17 = new ArrayList();
        ArrayList arrayList18 = new ArrayList();
        boolean z8 = false;
        boolean z9 = false;
        while (true) {
            String str6 = "application/x-mpegURL";
            if (!aVar.a()) {
                HashMap hashMap4 = hashMap2;
                ArrayList arrayList19 = arrayList16;
                ArrayList arrayList20 = arrayList12;
                ArrayList arrayList21 = arrayList13;
                ArrayList arrayList22 = arrayList14;
                ArrayList arrayList23 = arrayList15;
                ArrayList arrayList24 = arrayList18;
                boolean z10 = z8;
                ArrayList arrayList25 = arrayList17;
                ArrayList arrayList26 = new ArrayList();
                HashSet hashSet = new HashSet();
                for (int i12 = 0; i12 < arrayList11.size(); i12++) {
                    e.b bVar = (e.b) arrayList11.get(i12);
                    if (hashSet.add(bVar.f10600a)) {
                        b6.a.f(bVar.f10601b.f11205k == null);
                        arrayList26.add(bVar.a(bVar.f10601b.b().Z(new Metadata(new HlsTrackMetadataEntry(null, null, (List) b6.a.e((ArrayList) hashMap4.get(bVar.f10600a))))).G()));
                    }
                }
                Uri uri = null;
                ArrayList arrayList27 = null;
                w0 w0Var2 = null;
                int i13 = 0;
                while (i13 < arrayList19.size()) {
                    ArrayList arrayList28 = arrayList19;
                    String str7 = (String) arrayList28.get(i13);
                    String z11 = z(str7, Q, hashMap3);
                    String z12 = z(str7, P, hashMap3);
                    w0.b X2 = new w0.b().U(z11 + ":" + z12).W(z12).M(str6).i0(x(str7)).e0(w(str7, hashMap3)).X(v(str7, O, hashMap3));
                    String v8 = v(str7, K, hashMap3);
                    Uri e8 = v8 == null ? uri : j0.e(str, v8);
                    arrayList19 = arrayList28;
                    String str8 = str6;
                    Metadata metadata = new Metadata(new HlsTrackMetadataEntry(z11, z12, Collections.emptyList()));
                    String z13 = z(str7, M, hashMap3);
                    z13.hashCode();
                    switch (z13.hashCode()) {
                        case -959297733:
                            if (z13.equals("SUBTITLES")) {
                                c9 = 0;
                                break;
                            }
                            c9 = 65535;
                            break;
                        case -333210994:
                            if (z13.equals("CLOSED-CAPTIONS")) {
                                c9 = 1;
                                break;
                            }
                            c9 = 65535;
                            break;
                        case 62628790:
                            if (z13.equals("AUDIO")) {
                                c9 = 2;
                                break;
                            }
                            c9 = 65535;
                            break;
                        case 81665115:
                            if (z13.equals("VIDEO")) {
                                c9 = 3;
                                break;
                            }
                            c9 = 65535;
                            break;
                        default:
                            c9 = 65535;
                            break;
                    }
                    switch (c9) {
                        case 0:
                            w0Var = w0Var2;
                            arrayList = arrayList21;
                            arrayList2 = arrayList20;
                            e.b g8 = g(arrayList11, z11);
                            if (g8 != null) {
                                String L2 = l0.L(g8.f10601b.f11204j, 3);
                                X2.K(L2);
                                str2 = t.g(L2);
                            } else {
                                str2 = null;
                            }
                            if (str2 == null) {
                                str2 = "text/vtt";
                            }
                            X2.g0(str2).Z(metadata);
                            if (e8 != null) {
                                e.a aVar2 = new e.a(e8, X2.G(), z11, z12);
                                arrayList3 = arrayList22;
                                arrayList3.add(aVar2);
                                break;
                            } else {
                                arrayList3 = arrayList22;
                                p.i("HlsPlaylistParser", "EXT-X-MEDIA tag with missing mandatory URI attribute: skipping");
                                break;
                            }
                        case 1:
                            w0Var = w0Var2;
                            arrayList = arrayList21;
                            arrayList2 = arrayList20;
                            String z14 = z(str7, S, hashMap3);
                            if (z14.startsWith("CC")) {
                                parseInt = Integer.parseInt(z14.substring(2));
                                str3 = "application/cea-608";
                            } else {
                                parseInt = Integer.parseInt(z14.substring(7));
                                str3 = "application/cea-708";
                            }
                            if (arrayList27 == null) {
                                arrayList27 = new ArrayList();
                            }
                            X2.g0(str3).H(parseInt);
                            arrayList27.add(X2.G());
                            arrayList3 = arrayList22;
                            break;
                        case 2:
                            arrayList2 = arrayList20;
                            e.b f5 = f(arrayList11, z11);
                            if (f5 != null) {
                                w0Var = w0Var2;
                                String L3 = l0.L(f5.f10601b.f11204j, 1);
                                X2.K(L3);
                                str4 = t.g(L3);
                            } else {
                                w0Var = w0Var2;
                                str4 = null;
                            }
                            String v9 = v(str7, f10491i, hashMap3);
                            if (v9 != null) {
                                X2.J(Integer.parseInt(l0.S0(v9, "/")[0]));
                                if ("audio/eac3".equals(str4) && v9.endsWith("/JOC")) {
                                    X2.K("ec+3");
                                    str4 = "audio/eac3-joc";
                                }
                            }
                            X2.g0(str4);
                            if (e8 != null) {
                                X2.Z(metadata);
                                arrayList = arrayList21;
                                arrayList.add(new e.a(e8, X2.G(), z11, z12));
                            } else {
                                arrayList = arrayList21;
                                if (f5 != null) {
                                    w0Var = X2.G();
                                }
                            }
                            arrayList3 = arrayList22;
                            break;
                        case 3:
                            e.b h8 = h(arrayList11, z11);
                            if (h8 != null) {
                                w0 w0Var3 = h8.f10601b;
                                String L4 = l0.L(w0Var3.f11204j, 2);
                                X2.K(L4).g0(t.g(L4)).n0(w0Var3.f11211w).S(w0Var3.f11212x).R(w0Var3.f11213y);
                            }
                            if (e8 != null) {
                                X2.Z(metadata);
                                arrayList2 = arrayList20;
                                arrayList2.add(new e.a(e8, X2.G(), z11, z12));
                                w0Var = w0Var2;
                                arrayList3 = arrayList22;
                                arrayList = arrayList21;
                                break;
                            }
                        default:
                            w0Var = w0Var2;
                            arrayList3 = arrayList22;
                            arrayList = arrayList21;
                            arrayList2 = arrayList20;
                            break;
                    }
                    i13++;
                    arrayList22 = arrayList3;
                    arrayList21 = arrayList;
                    arrayList20 = arrayList2;
                    str6 = str8;
                    w0Var2 = w0Var;
                    uri = null;
                }
                return new e(str, arrayList24, arrayList26, arrayList20, arrayList21, arrayList22, arrayList23, w0Var2, z9 ? Collections.emptyList() : arrayList27, z10, hashMap3, arrayList25);
            }
            String b9 = aVar.b();
            if (b9.startsWith("#EXT")) {
                arrayList18.add(b9);
            }
            boolean startsWith = b9.startsWith("#EXT-X-I-FRAME-STREAM-INF");
            boolean z15 = z8;
            if (b9.startsWith("#EXT-X-DEFINE")) {
                hashMap3.put(z(b9, P, hashMap3), z(b9, Z, hashMap3));
            } else {
                if (b9.equals("#EXT-X-INDEPENDENT-SEGMENTS")) {
                    hashMap = hashMap2;
                    arrayList10 = arrayList16;
                    arrayList9 = arrayList12;
                    arrayList8 = arrayList13;
                    arrayList7 = arrayList14;
                    arrayList5 = arrayList15;
                    arrayList6 = arrayList18;
                    arrayList4 = arrayList17;
                    z8 = true;
                } else if (b9.startsWith("#EXT-X-MEDIA")) {
                    arrayList16.add(b9);
                } else if (b9.startsWith("#EXT-X-SESSION-KEY")) {
                    DrmInitData.SchemeData k8 = k(b9, u(b9, I, "identity", hashMap3), hashMap3);
                    if (k8 != null) {
                        arrayList17.add(new DrmInitData(l(z(b9, H, hashMap3)), k8));
                    }
                } else if (b9.startsWith("#EXT-X-STREAM-INF") || startsWith) {
                    boolean contains = z9 | b9.contains("CLOSED-CAPTIONS=NONE");
                    if (startsWith) {
                        i8 = 16384;
                        z4 = contains;
                    } else {
                        z4 = contains;
                        i8 = 0;
                    }
                    int m8 = m(b9, f10490h);
                    arrayList4 = arrayList17;
                    arrayList5 = arrayList15;
                    int s8 = s(b9, f10485c, -1);
                    String v10 = v(b9, f10492j, hashMap3);
                    arrayList6 = arrayList18;
                    String v11 = v(b9, f10493k, hashMap3);
                    arrayList7 = arrayList14;
                    if (v11 != null) {
                        String[] R0 = l0.R0(v11, "x");
                        int parseInt2 = Integer.parseInt(R0[0]);
                        int parseInt3 = Integer.parseInt(R0[1]);
                        if (parseInt2 <= 0 || parseInt3 <= 0) {
                            parseInt3 = -1;
                            i11 = -1;
                        } else {
                            i11 = parseInt2;
                        }
                        i10 = parseInt3;
                        i9 = i11;
                    } else {
                        i9 = -1;
                        i10 = -1;
                    }
                    arrayList8 = arrayList13;
                    String v12 = v(b9, f10494l, hashMap3);
                    arrayList9 = arrayList12;
                    float parseFloat = v12 != null ? Float.parseFloat(v12) : -1.0f;
                    String v13 = v(b9, f10486d, hashMap3);
                    arrayList10 = arrayList16;
                    String v14 = v(b9, f10487e, hashMap3);
                    HashMap hashMap5 = hashMap2;
                    String v15 = v(b9, f10488f, hashMap3);
                    String v16 = v(b9, f10489g, hashMap3);
                    if (startsWith) {
                        B2 = z(b9, K, hashMap3);
                    } else if (!aVar.a()) {
                        throw ParserException.c("#EXT-X-STREAM-INF must be followed by another line", null);
                    } else {
                        B2 = B(aVar.b(), hashMap3);
                    }
                    Uri e9 = j0.e(str5, B2);
                    arrayList11.add(new e.b(e9, new w0.b().T(arrayList11.size()).M("application/x-mpegURL").K(v10).I(s8).b0(m8).n0(i9).S(i10).R(parseFloat).e0(i8).G(), v13, v14, v15, v16));
                    hashMap = hashMap5;
                    ArrayList arrayList29 = (ArrayList) hashMap.get(e9);
                    if (arrayList29 == null) {
                        arrayList29 = new ArrayList();
                        hashMap.put(e9, arrayList29);
                    }
                    arrayList29.add(new HlsTrackMetadataEntry.VariantInfo(s8, m8, v13, v14, v15, v16));
                    z8 = z15;
                    z9 = z4;
                }
                hashMap2 = hashMap;
                arrayList17 = arrayList4;
                arrayList15 = arrayList5;
                arrayList18 = arrayList6;
                arrayList14 = arrayList7;
                arrayList13 = arrayList8;
                arrayList12 = arrayList9;
                arrayList16 = arrayList10;
                str5 = str;
            }
            hashMap = hashMap2;
            arrayList10 = arrayList16;
            arrayList9 = arrayList12;
            arrayList8 = arrayList13;
            arrayList7 = arrayList14;
            arrayList5 = arrayList15;
            arrayList6 = arrayList18;
            arrayList4 = arrayList17;
            z8 = z15;
            hashMap2 = hashMap;
            arrayList17 = arrayList4;
            arrayList15 = arrayList5;
            arrayList18 = arrayList6;
            arrayList14 = arrayList7;
            arrayList13 = arrayList8;
            arrayList12 = arrayList9;
            arrayList16 = arrayList10;
            str5 = str;
        }
    }

    private static boolean q(String str, Pattern pattern, boolean z4) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? "YES".equals(matcher.group(1)) : z4;
    }

    private static double r(String str, Pattern pattern, double d8) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Double.parseDouble((String) b6.a.e(matcher.group(1))) : d8;
    }

    private static int s(String str, Pattern pattern, int i8) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Integer.parseInt((String) b6.a.e(matcher.group(1))) : i8;
    }

    private static long t(String str, Pattern pattern, long j8) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Long.parseLong((String) b6.a.e(matcher.group(1))) : j8;
    }

    private static String u(String str, Pattern pattern, String str2, Map<String, String> map) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            str2 = (String) b6.a.e(matcher.group(1));
        }
        return (map.isEmpty() || str2 == null) ? str2 : B(str2, map);
    }

    private static String v(String str, Pattern pattern, Map<String, String> map) {
        return u(str, pattern, null, map);
    }

    private static int w(String str, Map<String, String> map) {
        String v8 = v(str, R, map);
        if (TextUtils.isEmpty(v8)) {
            return 0;
        }
        String[] R0 = l0.R0(v8, ",");
        int i8 = l0.s(R0, "public.accessibility.describes-video") ? RecognitionOptions.UPC_A : 0;
        if (l0.s(R0, "public.accessibility.transcribes-spoken-dialog")) {
            i8 |= RecognitionOptions.AZTEC;
        }
        if (l0.s(R0, "public.accessibility.describes-music-and-sound")) {
            i8 |= RecognitionOptions.UPC_E;
        }
        return l0.s(R0, "public.easy-to-read") ? i8 | 8192 : i8;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [int] */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v7 */
    private static int x(String str) {
        boolean q8 = q(str, U, false);
        ?? r02 = q8;
        if (q(str, V, false)) {
            r02 = (q8 ? 1 : 0) | true;
        }
        return q(str, T, false) ? r02 | 4 : r02;
    }

    private static d.f y(String str) {
        double r4 = r(str, f10499r, -9.223372036854776E18d);
        long j8 = r4 == -9.223372036854776E18d ? -9223372036854775807L : (long) (r4 * 1000000.0d);
        boolean q8 = q(str, f10500s, false);
        double r8 = r(str, f10502u, -9.223372036854776E18d);
        long j9 = r8 == -9.223372036854776E18d ? -9223372036854775807L : (long) (r8 * 1000000.0d);
        double r9 = r(str, f10503v, -9.223372036854776E18d);
        return new d.f(j8, q8, j9, r9 != -9.223372036854776E18d ? (long) (r9 * 1000000.0d) : -9223372036854775807L, q(str, f10504w, false));
    }

    private static String z(String str, Pattern pattern, Map<String, String> map) {
        String v8 = v(str, pattern, map);
        if (v8 != null) {
            return v8;
        }
        throw ParserException.c("Couldn't match " + pattern.pattern() + " in " + str, null);
    }

    @Override // com.google.android.exoplayer2.upstream.d.a
    /* renamed from: i */
    public n5.d a(Uri uri, InputStream inputStream) {
        String trim;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ArrayDeque arrayDeque = new ArrayDeque();
        try {
            if (b(bufferedReader)) {
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        l0.n(bufferedReader);
                        throw ParserException.c("Failed to parse the playlist, could not identify any tags.", null);
                    }
                    trim = readLine.trim();
                    if (!trim.isEmpty()) {
                        if (!trim.startsWith("#EXT-X-STREAM-INF")) {
                            if (trim.startsWith("#EXT-X-TARGETDURATION") || trim.startsWith("#EXT-X-MEDIA-SEQUENCE") || trim.startsWith("#EXTINF") || trim.startsWith("#EXT-X-KEY") || trim.startsWith("#EXT-X-BYTERANGE") || trim.equals("#EXT-X-DISCONTINUITY") || trim.equals("#EXT-X-DISCONTINUITY-SEQUENCE") || trim.equals("#EXT-X-ENDLIST")) {
                                break;
                            }
                            arrayDeque.add(trim);
                        } else {
                            arrayDeque.add(trim);
                            return p(new a(arrayDeque, bufferedReader), uri.toString());
                        }
                    }
                }
                arrayDeque.add(trim);
                return o(this.f10508a, this.f10509b, new a(arrayDeque, bufferedReader), uri.toString());
            }
            throw ParserException.c("Input does not start with the #EXTM3U header.", null);
        } finally {
            l0.n(bufferedReader);
        }
    }
}
