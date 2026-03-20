package w5;

import android.text.Layout;
import b6.l0;
import b6.m0;
import b6.p;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c extends p5.g {

    /* renamed from: p  reason: collision with root package name */
    private static final Pattern f23607p = Pattern.compile("^([0-9][0-9]+):([0-9][0-9]):([0-9][0-9])(?:(\\.[0-9]+)|:([0-9][0-9])(?:\\.([0-9]+))?)?$");
    private static final Pattern q = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)(h|m|s|ms|f|t)$");

    /* renamed from: r  reason: collision with root package name */
    private static final Pattern f23608r = Pattern.compile("^(([0-9]*.)?[0-9]+)(px|em|%)$");

    /* renamed from: s  reason: collision with root package name */
    static final Pattern f23609s = Pattern.compile("^([-+]?\\d+\\.?\\d*?)%$");

    /* renamed from: t  reason: collision with root package name */
    static final Pattern f23610t = Pattern.compile("^(\\d+\\.?\\d*?)% (\\d+\\.?\\d*?)%$");

    /* renamed from: u  reason: collision with root package name */
    private static final Pattern f23611u = Pattern.compile("^(\\d+\\.?\\d*?)px (\\d+\\.?\\d*?)px$");

    /* renamed from: v  reason: collision with root package name */
    private static final Pattern f23612v = Pattern.compile("^(\\d+) (\\d+)$");

    /* renamed from: w  reason: collision with root package name */
    private static final b f23613w = new b(30.0f, 1, 1);

    /* renamed from: x  reason: collision with root package name */
    private static final a f23614x = new a(32, 15);

    /* renamed from: o  reason: collision with root package name */
    private final XmlPullParserFactory f23615o;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        final int f23616a;

        /* renamed from: b  reason: collision with root package name */
        final int f23617b;

        a(int i8, int i9) {
            this.f23616a = i8;
            this.f23617b = i9;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        final float f23618a;

        /* renamed from: b  reason: collision with root package name */
        final int f23619b;

        /* renamed from: c  reason: collision with root package name */
        final int f23620c;

        b(float f5, int i8, int i9) {
            this.f23618a = f5;
            this.f23619b = i8;
            this.f23620c = i9;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: w5.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0225c {

        /* renamed from: a  reason: collision with root package name */
        final int f23621a;

        /* renamed from: b  reason: collision with root package name */
        final int f23622b;

        C0225c(int i8, int i9) {
            this.f23621a = i8;
            this.f23622b = i9;
        }
    }

    public c() {
        super("TtmlDecoder");
        try {
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            this.f23615o = newInstance;
            newInstance.setNamespaceAware(true);
        } catch (XmlPullParserException e8) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e8);
        }
    }

    private static g B(g gVar) {
        return gVar == null ? new g() : gVar;
    }

    private static boolean C(String str) {
        return str.equals("tt") || str.equals("head") || str.equals("body") || str.equals("div") || str.equals("p") || str.equals("span") || str.equals("br") || str.equals("style") || str.equals("styling") || str.equals("layout") || str.equals("region") || str.equals("metadata") || str.equals("image") || str.equals("data") || str.equals("information");
    }

    private static Layout.Alignment D(String str) {
        String e8 = com.google.common.base.c.e(str);
        e8.hashCode();
        char c9 = 65535;
        switch (e8.hashCode()) {
            case -1364013995:
                if (e8.equals("center")) {
                    c9 = 0;
                    break;
                }
                break;
            case 100571:
                if (e8.equals("end")) {
                    c9 = 1;
                    break;
                }
                break;
            case 3317767:
                if (e8.equals("left")) {
                    c9 = 2;
                    break;
                }
                break;
            case 108511772:
                if (e8.equals("right")) {
                    c9 = 3;
                    break;
                }
                break;
            case 109757538:
                if (e8.equals("start")) {
                    c9 = 4;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return Layout.Alignment.ALIGN_CENTER;
            case 1:
            case 3:
                return Layout.Alignment.ALIGN_OPPOSITE;
            case 2:
            case 4:
                return Layout.Alignment.ALIGN_NORMAL;
            default:
                return null;
        }
    }

    private static a E(XmlPullParser xmlPullParser, a aVar) {
        StringBuilder sb;
        String attributeValue = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "cellResolution");
        if (attributeValue == null) {
            return aVar;
        }
        Matcher matcher = f23612v.matcher(attributeValue);
        if (matcher.matches()) {
            try {
                int parseInt = Integer.parseInt((String) b6.a.e(matcher.group(1)));
                int parseInt2 = Integer.parseInt((String) b6.a.e(matcher.group(2)));
                if (parseInt == 0 || parseInt2 == 0) {
                    throw new SubtitleDecoderException("Invalid cell resolution " + parseInt + " " + parseInt2);
                }
                return new a(parseInt, parseInt2);
            } catch (NumberFormatException unused) {
                sb = new StringBuilder();
            }
        } else {
            sb = new StringBuilder();
        }
        sb.append("Ignoring malformed cell resolution: ");
        sb.append(attributeValue);
        p.i("TtmlDecoder", sb.toString());
        return aVar;
    }

    private static void F(String str, g gVar) {
        Matcher matcher;
        String[] R0 = l0.R0(str, "\\s+");
        if (R0.length == 1) {
            matcher = f23608r.matcher(str);
        } else if (R0.length != 2) {
            throw new SubtitleDecoderException("Invalid number of entries for fontSize: " + R0.length + ".");
        } else {
            matcher = f23608r.matcher(R0[1]);
            p.i("TtmlDecoder", "Multiple values in fontSize attribute. Picking the second value for vertical font size and ignoring the first.");
        }
        if (!matcher.matches()) {
            throw new SubtitleDecoderException("Invalid expression for fontSize: '" + str + "'.");
        }
        String str2 = (String) b6.a.e(matcher.group(3));
        str2.hashCode();
        char c9 = 65535;
        switch (str2.hashCode()) {
            case 37:
                if (str2.equals("%")) {
                    c9 = 0;
                    break;
                }
                break;
            case 3240:
                if (str2.equals("em")) {
                    c9 = 1;
                    break;
                }
                break;
            case 3592:
                if (str2.equals("px")) {
                    c9 = 2;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                gVar.z(3);
                break;
            case 1:
                gVar.z(2);
                break;
            case 2:
                gVar.z(1);
                break;
            default:
                throw new SubtitleDecoderException("Invalid unit for fontSize: '" + str2 + "'.");
        }
        gVar.y(Float.parseFloat((String) b6.a.e(matcher.group(1))));
    }

    private static b G(XmlPullParser xmlPullParser) {
        String attributeValue = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "frameRate");
        int parseInt = attributeValue != null ? Integer.parseInt(attributeValue) : 30;
        float f5 = 1.0f;
        String attributeValue2 = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "frameRateMultiplier");
        if (attributeValue2 != null) {
            String[] R0 = l0.R0(attributeValue2, " ");
            if (R0.length != 2) {
                throw new SubtitleDecoderException("frameRateMultiplier doesn't have 2 parts");
            }
            f5 = Integer.parseInt(R0[0]) / Integer.parseInt(R0[1]);
        }
        b bVar = f23613w;
        int i8 = bVar.f23619b;
        String attributeValue3 = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "subFrameRate");
        if (attributeValue3 != null) {
            i8 = Integer.parseInt(attributeValue3);
        }
        int i9 = bVar.f23620c;
        String attributeValue4 = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "tickRate");
        if (attributeValue4 != null) {
            i9 = Integer.parseInt(attributeValue4);
        }
        return new b(parseInt * f5, i8, i9);
    }

    private static Map<String, g> H(XmlPullParser xmlPullParser, Map<String, g> map, a aVar, C0225c c0225c, Map<String, e> map2, Map<String, String> map3) {
        do {
            xmlPullParser.next();
            if (m0.f(xmlPullParser, "style")) {
                String a9 = m0.a(xmlPullParser, "style");
                g M = M(xmlPullParser, new g());
                if (a9 != null) {
                    for (String str : N(a9)) {
                        M.a(map.get(str));
                    }
                }
                String g8 = M.g();
                if (g8 != null) {
                    map.put(g8, M);
                }
            } else if (m0.f(xmlPullParser, "region")) {
                e K = K(xmlPullParser, aVar, c0225c);
                if (K != null) {
                    map2.put(K.f23636a, K);
                }
            } else if (m0.f(xmlPullParser, "metadata")) {
                I(xmlPullParser, map3);
            }
        } while (!m0.d(xmlPullParser, "head"));
        return map;
    }

    private static void I(XmlPullParser xmlPullParser, Map<String, String> map) {
        String a9;
        do {
            xmlPullParser.next();
            if (m0.f(xmlPullParser, "image") && (a9 = m0.a(xmlPullParser, "id")) != null) {
                map.put(a9, xmlPullParser.nextText());
            }
        } while (!m0.d(xmlPullParser, "metadata"));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static d J(XmlPullParser xmlPullParser, d dVar, Map<String, e> map, b bVar) {
        long j8;
        long j9;
        char c9;
        int attributeCount = xmlPullParser.getAttributeCount();
        g M = M(xmlPullParser, null);
        String str = null;
        String str2 = BuildConfig.FLAVOR;
        long j10 = -9223372036854775807L;
        long j11 = -9223372036854775807L;
        long j12 = -9223372036854775807L;
        String[] strArr = null;
        for (int i8 = 0; i8 < attributeCount; i8++) {
            String attributeName = xmlPullParser.getAttributeName(i8);
            String attributeValue = xmlPullParser.getAttributeValue(i8);
            attributeName.hashCode();
            switch (attributeName.hashCode()) {
                case -934795532:
                    if (attributeName.equals("region")) {
                        c9 = 0;
                        break;
                    }
                    c9 = 65535;
                    break;
                case 99841:
                    if (attributeName.equals("dur")) {
                        c9 = 1;
                        break;
                    }
                    c9 = 65535;
                    break;
                case 100571:
                    if (attributeName.equals("end")) {
                        c9 = 2;
                        break;
                    }
                    c9 = 65535;
                    break;
                case 93616297:
                    if (attributeName.equals("begin")) {
                        c9 = 3;
                        break;
                    }
                    c9 = 65535;
                    break;
                case 109780401:
                    if (attributeName.equals("style")) {
                        c9 = 4;
                        break;
                    }
                    c9 = 65535;
                    break;
                case 1292595405:
                    if (attributeName.equals("backgroundImage")) {
                        c9 = 5;
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
                    if (map.containsKey(attributeValue)) {
                        str2 = attributeValue;
                        continue;
                    }
                case 1:
                    j12 = O(attributeValue, bVar);
                    break;
                case 2:
                    j11 = O(attributeValue, bVar);
                    break;
                case 3:
                    j10 = O(attributeValue, bVar);
                    break;
                case 4:
                    String[] N = N(attributeValue);
                    if (N.length > 0) {
                        strArr = N;
                        break;
                    }
                    break;
                case 5:
                    if (attributeValue.startsWith("#")) {
                        str = attributeValue.substring(1);
                        break;
                    }
                    break;
            }
        }
        if (dVar != null) {
            long j13 = dVar.f23626d;
            j8 = -9223372036854775807L;
            if (j13 != -9223372036854775807L) {
                if (j10 != -9223372036854775807L) {
                    j10 += j13;
                }
                if (j11 != -9223372036854775807L) {
                    j11 += j13;
                }
            }
        } else {
            j8 = -9223372036854775807L;
        }
        long j14 = j10;
        if (j11 == j8) {
            if (j12 != j8) {
                j9 = j14 + j12;
            } else if (dVar != null) {
                long j15 = dVar.f23627e;
                if (j15 != j8) {
                    j9 = j15;
                }
            }
            return d.c(xmlPullParser.getName(), j14, j9, M, strArr, str2, str, dVar);
        }
        j9 = j11;
        return d.c(xmlPullParser.getName(), j14, j9, M, strArr, str2, str, dVar);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0184, code lost:
        if (r0.equals("tb") == false) goto L42;
     */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0157  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static w5.e K(org.xmlpull.v1.XmlPullParser r17, w5.c.a r18, w5.c.C0225c r19) {
        /*
            Method dump skipped, instructions count: 478
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: w5.c.K(org.xmlpull.v1.XmlPullParser, w5.c$a, w5.c$c):w5.e");
    }

    private static float L(String str) {
        Matcher matcher = f23609s.matcher(str);
        if (!matcher.matches()) {
            p.i("TtmlDecoder", "Invalid value for shear: " + str);
            return Float.MAX_VALUE;
        }
        try {
            return Math.min(100.0f, Math.max(-100.0f, Float.parseFloat((String) b6.a.e(matcher.group(1)))));
        } catch (NumberFormatException e8) {
            p.j("TtmlDecoder", "Failed to parse shear: " + str, e8);
            return Float.MAX_VALUE;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x01c4, code lost:
        if (r3.equals("text") == false) goto L49;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static w5.g M(org.xmlpull.v1.XmlPullParser r12, w5.g r13) {
        /*
            Method dump skipped, instructions count: 900
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: w5.c.M(org.xmlpull.v1.XmlPullParser, w5.g):w5.g");
    }

    private static String[] N(String str) {
        String trim = str.trim();
        return trim.isEmpty() ? new String[0] : l0.R0(trim, "\\s+");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00bc, code lost:
        if (r13.equals("ms") == false) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static long O(java.lang.String r13, w5.c.b r14) {
        /*
            Method dump skipped, instructions count: 326
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: w5.c.O(java.lang.String, w5.c$b):long");
    }

    private static C0225c P(XmlPullParser xmlPullParser) {
        StringBuilder sb;
        String str;
        String a9 = m0.a(xmlPullParser, "extent");
        if (a9 == null) {
            return null;
        }
        Matcher matcher = f23611u.matcher(a9);
        if (matcher.matches()) {
            try {
                return new C0225c(Integer.parseInt((String) b6.a.e(matcher.group(1))), Integer.parseInt((String) b6.a.e(matcher.group(2))));
            } catch (NumberFormatException unused) {
                sb = new StringBuilder();
                str = "Ignoring malformed tts extent: ";
            }
        } else {
            sb = new StringBuilder();
            str = "Ignoring non-pixel tts extent: ";
        }
        sb.append(str);
        sb.append(a9);
        p.i("TtmlDecoder", sb.toString());
        return null;
    }

    @Override // p5.g
    protected p5.h A(byte[] bArr, int i8, boolean z4) {
        b bVar;
        try {
            XmlPullParser newPullParser = this.f23615o.newPullParser();
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            HashMap hashMap3 = new HashMap();
            hashMap2.put(BuildConfig.FLAVOR, new e(BuildConfig.FLAVOR));
            C0225c c0225c = null;
            newPullParser.setInput(new ByteArrayInputStream(bArr, 0, i8), null);
            ArrayDeque arrayDeque = new ArrayDeque();
            b bVar2 = f23613w;
            a aVar = f23614x;
            int i9 = 0;
            h hVar = null;
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.getEventType()) {
                d dVar = (d) arrayDeque.peek();
                if (i9 == 0) {
                    String name = newPullParser.getName();
                    if (eventType == 2) {
                        if ("tt".equals(name)) {
                            bVar2 = G(newPullParser);
                            aVar = E(newPullParser, f23614x);
                            c0225c = P(newPullParser);
                        }
                        C0225c c0225c2 = c0225c;
                        b bVar3 = bVar2;
                        a aVar2 = aVar;
                        if (C(name)) {
                            if ("head".equals(name)) {
                                bVar = bVar3;
                                H(newPullParser, hashMap, aVar2, c0225c2, hashMap2, hashMap3);
                            } else {
                                bVar = bVar3;
                                try {
                                    d J = J(newPullParser, dVar, hashMap2, bVar);
                                    arrayDeque.push(J);
                                    if (dVar != null) {
                                        dVar.a(J);
                                    }
                                } catch (SubtitleDecoderException e8) {
                                    p.j("TtmlDecoder", "Suppressing parser error", e8);
                                    i9++;
                                }
                            }
                            bVar2 = bVar;
                        } else {
                            p.f("TtmlDecoder", "Ignoring unsupported tag: " + newPullParser.getName());
                            i9++;
                            bVar2 = bVar3;
                        }
                        c0225c = c0225c2;
                        aVar = aVar2;
                    } else if (eventType == 4) {
                        ((d) b6.a.e(dVar)).a(d.d(newPullParser.getText()));
                    } else if (eventType == 3) {
                        if (newPullParser.getName().equals("tt")) {
                            hVar = new h((d) b6.a.e((d) arrayDeque.peek()), hashMap, hashMap2, hashMap3);
                        }
                        arrayDeque.pop();
                    }
                } else if (eventType == 2) {
                    i9++;
                } else if (eventType == 3) {
                    i9--;
                }
                newPullParser.next();
            }
            if (hVar != null) {
                return hVar;
            }
            throw new SubtitleDecoderException("No TTML subtitles found");
        } catch (IOException e9) {
            throw new IllegalStateException("Unexpected error when reading input.", e9);
        } catch (XmlPullParserException e10) {
            throw new SubtitleDecoderException("Unable to decode source", e10);
        }
    }
}
