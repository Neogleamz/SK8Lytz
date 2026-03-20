package l5;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Xml;
import b6.j0;
import b6.l0;
import b6.m0;
import b6.p;
import b6.t;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.upstream.d;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.j1;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import l5.k;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d extends DefaultHandler implements d.a<c> {

    /* renamed from: b  reason: collision with root package name */
    private static final Pattern f21647b = Pattern.compile("(\\d+)(?:/(\\d+))?");

    /* renamed from: c  reason: collision with root package name */
    private static final Pattern f21648c = Pattern.compile("CC([1-4])=.*");

    /* renamed from: d  reason: collision with root package name */
    private static final Pattern f21649d = Pattern.compile("([1-9]|[1-5][0-9]|6[0-3])=.*");

    /* renamed from: e  reason: collision with root package name */
    private static final int[] f21650e = {-1, 1, 2, 3, 4, 5, 6, 8, 2, 3, 4, 7, 8, 24, 8, 12, 10, 12, 14, 12, 14};

    /* renamed from: a  reason: collision with root package name */
    private final XmlPullParserFactory f21651a;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final w0 f21652a;

        /* renamed from: b  reason: collision with root package name */
        public final ImmutableList<b> f21653b;

        /* renamed from: c  reason: collision with root package name */
        public final k f21654c;

        /* renamed from: d  reason: collision with root package name */
        public final String f21655d;

        /* renamed from: e  reason: collision with root package name */
        public final ArrayList<DrmInitData.SchemeData> f21656e;

        /* renamed from: f  reason: collision with root package name */
        public final ArrayList<e> f21657f;

        /* renamed from: g  reason: collision with root package name */
        public final long f21658g;

        /* renamed from: h  reason: collision with root package name */
        public final List<e> f21659h;

        /* renamed from: i  reason: collision with root package name */
        public final List<e> f21660i;

        public a(w0 w0Var, List<b> list, k kVar, String str, ArrayList<DrmInitData.SchemeData> arrayList, ArrayList<e> arrayList2, List<e> list2, List<e> list3, long j8) {
            this.f21652a = w0Var;
            this.f21653b = ImmutableList.x(list);
            this.f21654c = kVar;
            this.f21655d = str;
            this.f21656e = arrayList;
            this.f21657f = arrayList2;
            this.f21659h = list2;
            this.f21660i = list3;
            this.f21658g = j8;
        }
    }

    public d() {
        try {
            this.f21651a = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e8) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e8);
        }
    }

    protected static int D(List<e> list) {
        String str;
        for (int i8 = 0; i8 < list.size(); i8++) {
            e eVar = list.get(i8);
            if ("urn:scte:dash:cc:cea-608:2015".equals(eVar.f21661a) && (str = eVar.f21662b) != null) {
                Matcher matcher = f21648c.matcher(str);
                if (matcher.matches()) {
                    return Integer.parseInt(matcher.group(1));
                }
                p.i("MpdParser", "Unable to parse CEA-608 channel number from: " + eVar.f21662b);
            }
        }
        return -1;
    }

    protected static int E(List<e> list) {
        String str;
        for (int i8 = 0; i8 < list.size(); i8++) {
            e eVar = list.get(i8);
            if ("urn:scte:dash:cc:cea-708:2015".equals(eVar.f21661a) && (str = eVar.f21662b) != null) {
                Matcher matcher = f21649d.matcher(str);
                if (matcher.matches()) {
                    return Integer.parseInt(matcher.group(1));
                }
                p.i("MpdParser", "Unable to parse CEA-708 service block number from: " + eVar.f21662b);
            }
        }
        return -1;
    }

    protected static long H(XmlPullParser xmlPullParser, String str, long j8) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? j8 : l0.J0(attributeValue);
    }

    protected static e I(XmlPullParser xmlPullParser, String str) {
        String r02 = r0(xmlPullParser, "schemeIdUri", BuildConfig.FLAVOR);
        String r03 = r0(xmlPullParser, "value", null);
        String r04 = r0(xmlPullParser, "id", null);
        do {
            xmlPullParser.next();
        } while (!m0.d(xmlPullParser, str));
        return new e(r02, r03, r04);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected static int J(XmlPullParser xmlPullParser) {
        char c9;
        String attributeValue = xmlPullParser.getAttributeValue(null, "value");
        if (attributeValue == null) {
            return -1;
        }
        String e8 = com.google.common.base.c.e(attributeValue);
        e8.hashCode();
        switch (e8.hashCode()) {
            case 1596796:
                if (e8.equals("4000")) {
                    c9 = 0;
                    break;
                }
                c9 = 65535;
                break;
            case 2937391:
                if (e8.equals("a000")) {
                    c9 = 1;
                    break;
                }
                c9 = 65535;
                break;
            case 3094035:
                if (e8.equals("f801")) {
                    c9 = 2;
                    break;
                }
                c9 = 65535;
                break;
            case 3133436:
                if (e8.equals("fa01")) {
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
                return 1;
            case 1:
                return 2;
            case 2:
                return 6;
            case 3:
                return 8;
            default:
                return -1;
        }
    }

    protected static int K(XmlPullParser xmlPullParser) {
        int U = U(xmlPullParser, "value", -1);
        if (U <= 0 || U >= 33) {
            return -1;
        }
        return U;
    }

    protected static int L(XmlPullParser xmlPullParser) {
        int bitCount;
        String attributeValue = xmlPullParser.getAttributeValue(null, "value");
        if (attributeValue == null || (bitCount = Integer.bitCount(Integer.parseInt(attributeValue, 16))) == 0) {
            return -1;
        }
        return bitCount;
    }

    protected static long M(XmlPullParser xmlPullParser, String str, long j8) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? j8 : l0.K0(attributeValue);
    }

    protected static String N(List<e> list) {
        for (int i8 = 0; i8 < list.size(); i8++) {
            e eVar = list.get(i8);
            String str = eVar.f21661a;
            if ("tag:dolby.com,2018:dash:EC3_ExtensionType:2018".equals(str) && "JOC".equals(eVar.f21662b)) {
                return "audio/eac3-joc";
            }
            if ("tag:dolby.com,2014:dash:DolbyDigitalPlusExtensionType:2014".equals(str) && "ec+3".equals(eVar.f21662b)) {
                return "audio/eac3-joc";
            }
        }
        return "audio/eac3";
    }

    protected static float R(XmlPullParser xmlPullParser, String str, float f5) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? f5 : Float.parseFloat(attributeValue);
    }

    protected static float S(XmlPullParser xmlPullParser, float f5) {
        String group;
        String attributeValue = xmlPullParser.getAttributeValue(null, "frameRate");
        if (attributeValue != null) {
            Matcher matcher = f21647b.matcher(attributeValue);
            if (matcher.matches()) {
                int parseInt = Integer.parseInt(matcher.group(1));
                float f8 = parseInt;
                return !TextUtils.isEmpty(matcher.group(2)) ? f8 / Integer.parseInt(group) : f8;
            }
            return f5;
        }
        return f5;
    }

    protected static int U(XmlPullParser xmlPullParser, String str, int i8) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? i8 : Integer.parseInt(attributeValue);
    }

    protected static long W(List<e> list) {
        for (int i8 = 0; i8 < list.size(); i8++) {
            e eVar = list.get(i8);
            if (com.google.common.base.c.a("http://dashif.org/guidelines/last-segment-number", eVar.f21661a)) {
                return Long.parseLong(eVar.f21662b);
            }
        }
        return -1L;
    }

    protected static long X(XmlPullParser xmlPullParser, String str, long j8) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? j8 : Long.parseLong(attributeValue);
    }

    protected static int Z(XmlPullParser xmlPullParser) {
        int U = U(xmlPullParser, "value", -1);
        if (U >= 0) {
            int[] iArr = f21650e;
            if (U < iArr.length) {
                return iArr[U];
            }
            return -1;
        }
        return -1;
    }

    private long b(List<k.d> list, long j8, long j9, int i8, long j10) {
        int m8 = i8 >= 0 ? i8 + 1 : (int) l0.m(j10 - j8, j9);
        for (int i9 = 0; i9 < m8; i9++) {
            list.add(m(j8, j9));
            j8 += j9;
        }
        return j8;
    }

    private static int p(int i8, int i9) {
        if (i8 == -1) {
            return i9;
        }
        if (i9 == -1) {
            return i8;
        }
        b6.a.f(i8 == i9);
        return i8;
    }

    private static String q(String str, String str2) {
        if (str == null) {
            return str2;
        }
        if (str2 == null) {
            return str;
        }
        b6.a.f(str.equals(str2));
        return str;
    }

    private static void r(ArrayList<DrmInitData.SchemeData> arrayList) {
        String str;
        int i8 = 0;
        while (true) {
            if (i8 >= arrayList.size()) {
                str = null;
                break;
            }
            DrmInitData.SchemeData schemeData = arrayList.get(i8);
            if (i4.b.f20467c.equals(schemeData.f9597b) && (str = schemeData.f9598c) != null) {
                arrayList.remove(i8);
                break;
            }
            i8++;
        }
        if (str == null) {
            return;
        }
        for (int i9 = 0; i9 < arrayList.size(); i9++) {
            DrmInitData.SchemeData schemeData2 = arrayList.get(i9);
            if (i4.b.f20466b.equals(schemeData2.f9597b) && schemeData2.f9598c == null) {
                arrayList.set(i9, new DrmInitData.SchemeData(i4.b.f20467c, str, schemeData2.f9599d, schemeData2.f9600e));
            }
        }
    }

    protected static String r0(XmlPullParser xmlPullParser, String str, String str2) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? str2 : attributeValue;
    }

    private static void s(ArrayList<DrmInitData.SchemeData> arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            DrmInitData.SchemeData schemeData = arrayList.get(size);
            if (!schemeData.c()) {
                int i8 = 0;
                while (true) {
                    if (i8 >= arrayList.size()) {
                        break;
                    } else if (arrayList.get(i8).a(schemeData)) {
                        arrayList.remove(size);
                        break;
                    } else {
                        i8++;
                    }
                }
            }
        }
    }

    protected static String s0(XmlPullParser xmlPullParser, String str) {
        String str2 = BuildConfig.FLAVOR;
        do {
            xmlPullParser.next();
            if (xmlPullParser.getEventType() == 4) {
                str2 = xmlPullParser.getText();
            } else {
                w(xmlPullParser);
            }
        } while (!m0.d(xmlPullParser, str));
        return str2;
    }

    private static long t(long j8, long j9) {
        if (j9 != -9223372036854775807L) {
            j8 = j9;
        }
        if (j8 == Long.MAX_VALUE) {
            return -9223372036854775807L;
        }
        return j8;
    }

    private static String u(String str, String str2) {
        if (t.o(str)) {
            return t.c(str2);
        }
        if (t.s(str)) {
            return t.n(str2);
        }
        if (t.r(str) || t.p(str)) {
            return str;
        }
        if ("application/mp4".equals(str)) {
            String g8 = t.g(str2);
            return "text/vtt".equals(g8) ? "application/x-mp4-vtt" : g8;
        }
        return null;
    }

    private boolean v(String[] strArr) {
        for (String str : strArr) {
            if (str.startsWith("urn:dvb:dash:profile:dvb-dash:")) {
                return true;
            }
        }
        return false;
    }

    public static void w(XmlPullParser xmlPullParser) {
        if (m0.e(xmlPullParser)) {
            int i8 = 1;
            while (i8 != 0) {
                xmlPullParser.next();
                if (m0.e(xmlPullParser)) {
                    i8++;
                } else if (m0.c(xmlPullParser)) {
                    i8--;
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected int A(XmlPullParser xmlPullParser) {
        char c9;
        String r02 = r0(xmlPullParser, "schemeIdUri", null);
        r02.hashCode();
        int i8 = -1;
        switch (r02.hashCode()) {
            case -2128649360:
                if (r02.equals("urn:dts:dash:audio_channel_configuration:2012")) {
                    c9 = 0;
                    break;
                }
                c9 = 65535;
                break;
            case -1352850286:
                if (r02.equals("urn:mpeg:dash:23003:3:audio_channel_configuration:2011")) {
                    c9 = 1;
                    break;
                }
                c9 = 65535;
                break;
            case -1138141449:
                if (r02.equals("tag:dolby.com,2014:dash:audio_channel_configuration:2011")) {
                    c9 = 2;
                    break;
                }
                c9 = 65535;
                break;
            case -986633423:
                if (r02.equals("urn:mpeg:mpegB:cicp:ChannelConfiguration")) {
                    c9 = 3;
                    break;
                }
                c9 = 65535;
                break;
            case -79006963:
                if (r02.equals("tag:dts.com,2014:dash:audio_channel_configuration:2012")) {
                    c9 = 4;
                    break;
                }
                c9 = 65535;
                break;
            case 312179081:
                if (r02.equals("tag:dts.com,2018:uhd:audio_channel_configuration")) {
                    c9 = 5;
                    break;
                }
                c9 = 65535;
                break;
            case 2036691300:
                if (r02.equals("urn:dolby:dash:audio_channel_configuration:2011")) {
                    c9 = 6;
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
            case 4:
                i8 = K(xmlPullParser);
                break;
            case 1:
                i8 = U(xmlPullParser, "value", -1);
                break;
            case 2:
            case 6:
                i8 = J(xmlPullParser);
                break;
            case 3:
                i8 = Z(xmlPullParser);
                break;
            case 5:
                i8 = L(xmlPullParser);
                break;
        }
        do {
            xmlPullParser.next();
        } while (!m0.d(xmlPullParser, "AudioChannelConfiguration"));
        return i8;
    }

    protected long B(XmlPullParser xmlPullParser, long j8) {
        String attributeValue = xmlPullParser.getAttributeValue(null, "availabilityTimeOffset");
        if (attributeValue == null) {
            return j8;
        }
        if ("INF".equals(attributeValue)) {
            return Long.MAX_VALUE;
        }
        return Float.parseFloat(attributeValue) * 1000000.0f;
    }

    protected List<b> C(XmlPullParser xmlPullParser, List<b> list, boolean z4) {
        String attributeValue = xmlPullParser.getAttributeValue(null, "dvb:priority");
        int parseInt = attributeValue != null ? Integer.parseInt(attributeValue) : z4 ? 1 : Integer.MIN_VALUE;
        String attributeValue2 = xmlPullParser.getAttributeValue(null, "dvb:weight");
        int parseInt2 = attributeValue2 != null ? Integer.parseInt(attributeValue2) : 1;
        String attributeValue3 = xmlPullParser.getAttributeValue(null, "serviceLocation");
        String s02 = s0(xmlPullParser, "BaseURL");
        if (j0.b(s02)) {
            if (attributeValue3 == null) {
                attributeValue3 = s02;
            }
            return j1.j(new b(s02, attributeValue3, parseInt, parseInt2));
        }
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < list.size(); i8++) {
            b bVar = list.get(i8);
            String d8 = j0.d(bVar.f21630a, s02);
            String str = attributeValue3 == null ? d8 : attributeValue3;
            if (z4) {
                parseInt = bVar.f21632c;
                parseInt2 = bVar.f21633d;
                str = bVar.f21631b;
            }
            arrayList.add(new b(d8, str, parseInt, parseInt2));
        }
        return arrayList;
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0119  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected android.util.Pair<java.lang.String, com.google.android.exoplayer2.drm.DrmInitData.SchemeData> F(org.xmlpull.v1.XmlPullParser r11) {
        /*
            Method dump skipped, instructions count: 324
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: l5.d.F(org.xmlpull.v1.XmlPullParser):android.util.Pair");
    }

    protected int G(XmlPullParser xmlPullParser) {
        String attributeValue = xmlPullParser.getAttributeValue(null, "contentType");
        if (TextUtils.isEmpty(attributeValue)) {
            return -1;
        }
        if ("audio".equals(attributeValue)) {
            return 1;
        }
        if ("video".equals(attributeValue)) {
            return 2;
        }
        if ("text".equals(attributeValue)) {
            return 3;
        }
        return "image".equals(attributeValue) ? 4 : -1;
    }

    protected Pair<Long, EventMessage> O(XmlPullParser xmlPullParser, String str, String str2, long j8, long j9, ByteArrayOutputStream byteArrayOutputStream) {
        long X = X(xmlPullParser, "id", 0L);
        long X2 = X(xmlPullParser, "duration", -9223372036854775807L);
        long X3 = X(xmlPullParser, "presentationTime", 0L);
        long O0 = l0.O0(X2, 1000L, j8);
        long O02 = l0.O0(X3 - j9, 1000000L, j8);
        String r02 = r0(xmlPullParser, "messageData", null);
        byte[] P = P(xmlPullParser, byteArrayOutputStream);
        Long valueOf = Long.valueOf(O02);
        if (r02 != null) {
            P = l0.m0(r02);
        }
        return Pair.create(valueOf, d(str, str2, X, O0, P));
    }

    protected byte[] P(XmlPullParser xmlPullParser, ByteArrayOutputStream byteArrayOutputStream) {
        byteArrayOutputStream.reset();
        XmlSerializer newSerializer = Xml.newSerializer();
        newSerializer.setOutput(byteArrayOutputStream, com.google.common.base.e.f18817c.name());
        while (true) {
            xmlPullParser.nextToken();
            if (m0.d(xmlPullParser, "Event")) {
                newSerializer.flush();
                return byteArrayOutputStream.toByteArray();
            }
            switch (xmlPullParser.getEventType()) {
                case 0:
                    newSerializer.startDocument(null, Boolean.FALSE);
                    break;
                case 1:
                    newSerializer.endDocument();
                    break;
                case 2:
                    newSerializer.startTag(xmlPullParser.getNamespace(), xmlPullParser.getName());
                    for (int i8 = 0; i8 < xmlPullParser.getAttributeCount(); i8++) {
                        newSerializer.attribute(xmlPullParser.getAttributeNamespace(i8), xmlPullParser.getAttributeName(i8), xmlPullParser.getAttributeValue(i8));
                    }
                    break;
                case 3:
                    newSerializer.endTag(xmlPullParser.getNamespace(), xmlPullParser.getName());
                    break;
                case 4:
                    newSerializer.text(xmlPullParser.getText());
                    break;
                case 5:
                    newSerializer.cdsect(xmlPullParser.getText());
                    break;
                case 6:
                    newSerializer.entityRef(xmlPullParser.getText());
                    break;
                case 7:
                    newSerializer.ignorableWhitespace(xmlPullParser.getText());
                    break;
                case 8:
                    newSerializer.processingInstruction(xmlPullParser.getText());
                    break;
                case 9:
                    newSerializer.comment(xmlPullParser.getText());
                    break;
                case 10:
                    newSerializer.docdecl(xmlPullParser.getText());
                    break;
            }
        }
    }

    protected f Q(XmlPullParser xmlPullParser) {
        ByteArrayOutputStream byteArrayOutputStream;
        long j8;
        ArrayList arrayList;
        String r02 = r0(xmlPullParser, "schemeIdUri", BuildConfig.FLAVOR);
        String r03 = r0(xmlPullParser, "value", BuildConfig.FLAVOR);
        long X = X(xmlPullParser, "timescale", 1L);
        long X2 = X(xmlPullParser, "presentationTimeOffset", 0L);
        ArrayList arrayList2 = new ArrayList();
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream(RecognitionOptions.UPC_A);
        while (true) {
            xmlPullParser.next();
            if (m0.f(xmlPullParser, "Event")) {
                byteArrayOutputStream = byteArrayOutputStream2;
                long j9 = X2;
                j8 = X2;
                arrayList = arrayList2;
                arrayList.add(O(xmlPullParser, r02, r03, X, j9, byteArrayOutputStream));
            } else {
                byteArrayOutputStream = byteArrayOutputStream2;
                j8 = X2;
                arrayList = arrayList2;
                w(xmlPullParser);
            }
            if (m0.d(xmlPullParser, "EventStream")) {
                break;
            }
            arrayList2 = arrayList;
            byteArrayOutputStream2 = byteArrayOutputStream;
            X2 = j8;
        }
        long[] jArr = new long[arrayList.size()];
        EventMessage[] eventMessageArr = new EventMessage[arrayList.size()];
        for (int i8 = 0; i8 < arrayList.size(); i8++) {
            Pair pair = (Pair) arrayList.get(i8);
            jArr[i8] = ((Long) pair.first).longValue();
            eventMessageArr[i8] = (EventMessage) pair.second;
        }
        return e(r02, r03, X, jArr, eventMessageArr);
    }

    protected i T(XmlPullParser xmlPullParser) {
        return d0(xmlPullParser, "sourceURL", "range");
    }

    protected String V(XmlPullParser xmlPullParser) {
        return s0(xmlPullParser, "Label");
    }

    /* JADX WARN: Removed duplicated region for block: B:78:0x01c0  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01e7 A[LOOP:0: B:25:0x00a4->B:82:0x01e7, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01a3 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected l5.c Y(org.xmlpull.v1.XmlPullParser r47, android.net.Uri r48) {
        /*
            Method dump skipped, instructions count: 501
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: l5.d.Y(org.xmlpull.v1.XmlPullParser, android.net.Uri):l5.c");
    }

    protected Pair<g, Long> a0(XmlPullParser xmlPullParser, List<b> list, long j8, long j9, long j10, long j11, boolean z4) {
        long j12;
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        Object obj;
        long j13;
        k l02;
        d dVar = this;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        Object obj2 = null;
        String attributeValue = xmlPullParser2.getAttributeValue(null, "id");
        long M = M(xmlPullParser2, "start", j8);
        long j14 = -9223372036854775807L;
        long j15 = j10 != -9223372036854775807L ? j10 + M : -9223372036854775807L;
        long M2 = M(xmlPullParser2, "duration", -9223372036854775807L);
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        long j16 = j9;
        boolean z8 = false;
        long j17 = -9223372036854775807L;
        k.e eVar = null;
        e eVar2 = null;
        while (true) {
            xmlPullParser.next();
            if (m0.f(xmlPullParser2, "BaseURL")) {
                if (!z8) {
                    j16 = dVar.B(xmlPullParser2, j16);
                    z8 = true;
                }
                arrayList6.addAll(dVar.C(xmlPullParser2, list, z4));
                arrayList3 = arrayList5;
                arrayList = arrayList6;
                j13 = j14;
                obj = obj2;
                arrayList2 = arrayList4;
            } else {
                if (m0.f(xmlPullParser2, "AdaptationSet")) {
                    j12 = j16;
                    arrayList = arrayList6;
                    arrayList2 = arrayList4;
                    arrayList2.add(y(xmlPullParser, !arrayList6.isEmpty() ? arrayList6 : list, eVar, M2, j16, j17, j15, j11, z4));
                    xmlPullParser2 = xmlPullParser;
                    arrayList3 = arrayList5;
                } else {
                    j12 = j16;
                    ArrayList arrayList7 = arrayList5;
                    arrayList = arrayList6;
                    arrayList2 = arrayList4;
                    xmlPullParser2 = xmlPullParser;
                    if (m0.f(xmlPullParser2, "EventStream")) {
                        arrayList7.add(Q(xmlPullParser));
                        arrayList3 = arrayList7;
                    } else {
                        arrayList3 = arrayList7;
                        if (m0.f(xmlPullParser2, "SegmentBase")) {
                            eVar = j0(xmlPullParser2, null);
                            obj = null;
                            j16 = j12;
                            j13 = -9223372036854775807L;
                        } else {
                            if (m0.f(xmlPullParser2, "SegmentList")) {
                                long B = B(xmlPullParser2, -9223372036854775807L);
                                obj = null;
                                l02 = k0(xmlPullParser, null, j15, M2, j12, B, j11);
                                j17 = B;
                                j16 = j12;
                                j13 = -9223372036854775807L;
                            } else {
                                obj = null;
                                if (m0.f(xmlPullParser2, "SegmentTemplate")) {
                                    long B2 = B(xmlPullParser2, -9223372036854775807L);
                                    j13 = -9223372036854775807L;
                                    l02 = l0(xmlPullParser, null, ImmutableList.E(), j15, M2, j12, B2, j11);
                                    j17 = B2;
                                    j16 = j12;
                                } else {
                                    j13 = -9223372036854775807L;
                                    if (m0.f(xmlPullParser2, "AssetIdentifier")) {
                                        eVar2 = I(xmlPullParser2, "AssetIdentifier");
                                    } else {
                                        w(xmlPullParser);
                                    }
                                    j16 = j12;
                                }
                            }
                            eVar = l02;
                        }
                    }
                }
                obj = null;
                j13 = -9223372036854775807L;
                j16 = j12;
            }
            if (m0.d(xmlPullParser2, "Period")) {
                return Pair.create(h(attributeValue, M, arrayList2, arrayList3, eVar2), Long.valueOf(M2));
            }
            arrayList4 = arrayList2;
            arrayList6 = arrayList;
            obj2 = obj;
            arrayList5 = arrayList3;
            j14 = j13;
            dVar = this;
        }
    }

    protected String[] b0(XmlPullParser xmlPullParser, String str, String[] strArr) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? strArr : attributeValue.split(",");
    }

    protected l5.a c(int i8, int i9, List<j> list, List<e> list2, List<e> list3, List<e> list4) {
        return new l5.a(i8, i9, list, list2, list3, list4);
    }

    protected h c0(XmlPullParser xmlPullParser) {
        String str = null;
        String r02 = r0(xmlPullParser, "moreInformationURL", null);
        String r03 = r0(xmlPullParser, "lang", null);
        String str2 = null;
        String str3 = null;
        while (true) {
            xmlPullParser.next();
            if (m0.f(xmlPullParser, "Title")) {
                str = xmlPullParser.nextText();
            } else if (m0.f(xmlPullParser, "Source")) {
                str2 = xmlPullParser.nextText();
            } else if (m0.f(xmlPullParser, "Copyright")) {
                str3 = xmlPullParser.nextText();
            } else {
                w(xmlPullParser);
            }
            String str4 = str3;
            if (m0.d(xmlPullParser, "ProgramInformation")) {
                return new h(str, str2, str4, r02, r03);
            }
            str3 = str4;
        }
    }

    protected EventMessage d(String str, String str2, long j8, long j9, byte[] bArr) {
        return new EventMessage(str, str2, j9, j8, bArr);
    }

    protected i d0(XmlPullParser xmlPullParser, String str, String str2) {
        long j8;
        long j9;
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        String attributeValue2 = xmlPullParser.getAttributeValue(null, str2);
        if (attributeValue2 != null) {
            String[] split = attributeValue2.split("-");
            j8 = Long.parseLong(split[0]);
            if (split.length == 2) {
                j9 = (Long.parseLong(split[1]) - j8) + 1;
                return i(attributeValue, j8, j9);
            }
        } else {
            j8 = 0;
        }
        j9 = -1;
        return i(attributeValue, j8, j9);
    }

    protected f e(String str, String str2, long j8, long[] jArr, EventMessage[] eventMessageArr) {
        return new f(str, str2, j8, jArr, eventMessageArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x01ee A[LOOP:0: B:3:0x006a->B:57:0x01ee, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0198 A[EDGE_INSN: B:58:0x0198->B:47:0x0198 ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected l5.d.a e0(org.xmlpull.v1.XmlPullParser r36, java.util.List<l5.b> r37, java.lang.String r38, java.lang.String r39, int r40, int r41, float r42, int r43, int r44, java.lang.String r45, java.util.List<l5.e> r46, java.util.List<l5.e> r47, java.util.List<l5.e> r48, java.util.List<l5.e> r49, l5.k r50, long r51, long r53, long r55, long r57, long r59, boolean r61) {
        /*
            Method dump skipped, instructions count: 509
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: l5.d.e0(org.xmlpull.v1.XmlPullParser, java.util.List, java.lang.String, java.lang.String, int, int, float, int, int, java.lang.String, java.util.List, java.util.List, java.util.List, java.util.List, l5.k, long, long, long, long, long, boolean):l5.d$a");
    }

    protected w0 f(String str, String str2, int i8, int i9, float f5, int i10, int i11, int i12, String str3, List<e> list, List<e> list2, String str4, List<e> list3, List<e> list4) {
        String str5 = str4;
        String u8 = u(str2, str5);
        if ("audio/eac3".equals(u8)) {
            u8 = N(list4);
            if ("audio/eac3-joc".equals(u8)) {
                str5 = "ec+3";
            }
        }
        int p02 = p0(list);
        int i02 = i0(list) | f0(list2) | h0(list3) | h0(list4);
        Pair<Integer, Integer> t02 = t0(list3);
        w0.b X = new w0.b().U(str).M(str2).g0(u8).K(str5).b0(i12).i0(p02).e0(i02).X(str3);
        int i13 = -1;
        w0.b m02 = X.l0(t02 != null ? ((Integer) t02.first).intValue() : -1).m0(t02 != null ? ((Integer) t02.second).intValue() : -1);
        if (t.s(u8)) {
            m02.n0(i8).S(i9).R(f5);
        } else if (t.o(u8)) {
            m02.J(i10).h0(i11);
        } else if (t.r(u8)) {
            if ("application/cea-608".equals(u8)) {
                i13 = D(list2);
            } else if ("application/cea-708".equals(u8)) {
                i13 = E(list2);
            }
            m02.H(i13);
        } else if (t.p(u8)) {
            m02.n0(i8).S(i9);
        }
        return m02.G();
    }

    protected int f0(List<e> list) {
        int u02;
        int i8 = 0;
        for (int i9 = 0; i9 < list.size(); i9++) {
            e eVar = list.get(i9);
            if (com.google.common.base.c.a("urn:mpeg:dash:role:2011", eVar.f21661a)) {
                u02 = g0(eVar.f21662b);
            } else if (com.google.common.base.c.a("urn:tva:metadata:cs:AudioPurposeCS:2007", eVar.f21661a)) {
                u02 = u0(eVar.f21662b);
            }
            i8 |= u02;
        }
        return i8;
    }

    protected c g(long j8, long j9, long j10, boolean z4, long j11, long j12, long j13, long j14, h hVar, o oVar, l lVar, Uri uri, List<g> list) {
        return new c(j8, j9, j10, z4, j11, j12, j13, j14, hVar, oVar, lVar, uri, list);
    }

    protected int g0(String str) {
        if (str == null) {
            return 0;
        }
        char c9 = 65535;
        switch (str.hashCode()) {
            case -2060497896:
                if (str.equals("subtitle")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1724546052:
                if (str.equals("description")) {
                    c9 = 1;
                    break;
                }
                break;
            case -1580883024:
                if (str.equals("enhanced-audio-intelligibility")) {
                    c9 = 2;
                    break;
                }
                break;
            case -1574842690:
                if (str.equals("forced_subtitle")) {
                    c9 = 3;
                    break;
                }
                break;
            case -1408024454:
                if (str.equals("alternate")) {
                    c9 = 4;
                    break;
                }
                break;
            case -1396432756:
                if (str.equals("forced-subtitle")) {
                    c9 = 5;
                    break;
                }
                break;
            case 99825:
                if (str.equals("dub")) {
                    c9 = 6;
                    break;
                }
                break;
            case 3343801:
                if (str.equals("main")) {
                    c9 = 7;
                    break;
                }
                break;
            case 3530173:
                if (str.equals("sign")) {
                    c9 = '\b';
                    break;
                }
                break;
            case 552573414:
                if (str.equals("caption")) {
                    c9 = '\t';
                    break;
                }
                break;
            case 899152809:
                if (str.equals("commentary")) {
                    c9 = '\n';
                    break;
                }
                break;
            case 1629013393:
                if (str.equals("emergency")) {
                    c9 = 11;
                    break;
                }
                break;
            case 1855372047:
                if (str.equals("supplementary")) {
                    c9 = '\f';
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
            case 3:
            case 5:
                return RecognitionOptions.ITF;
            case 1:
                return RecognitionOptions.UPC_A;
            case 2:
                return RecognitionOptions.PDF417;
            case 4:
                return 2;
            case 6:
                return 16;
            case 7:
                return 1;
            case '\b':
                return RecognitionOptions.QR_CODE;
            case '\t':
                return 64;
            case '\n':
                return 8;
            case 11:
                return 32;
            case '\f':
                return 4;
            default:
                return 0;
        }
    }

    protected g h(String str, long j8, List<l5.a> list, List<f> list2, e eVar) {
        return new g(str, j8, list, list2, eVar);
    }

    protected int h0(List<e> list) {
        int i8 = 0;
        for (int i9 = 0; i9 < list.size(); i9++) {
            if (com.google.common.base.c.a("http://dashif.org/guidelines/trickmode", list.get(i9).f21661a)) {
                i8 |= 16384;
            }
        }
        return i8;
    }

    protected i i(String str, long j8, long j9) {
        return new i(str, j8, j9);
    }

    protected int i0(List<e> list) {
        int i8 = 0;
        for (int i9 = 0; i9 < list.size(); i9++) {
            e eVar = list.get(i9);
            if (com.google.common.base.c.a("urn:mpeg:dash:role:2011", eVar.f21661a)) {
                i8 |= g0(eVar.f21662b);
            }
        }
        return i8;
    }

    protected j j(a aVar, String str, String str2, ArrayList<DrmInitData.SchemeData> arrayList, ArrayList<e> arrayList2) {
        w0.b b9 = aVar.f21652a.b();
        if (str != null) {
            b9.W(str);
        }
        String str3 = aVar.f21655d;
        if (str3 != null) {
            str2 = str3;
        }
        ArrayList<DrmInitData.SchemeData> arrayList3 = aVar.f21656e;
        arrayList3.addAll(arrayList);
        if (!arrayList3.isEmpty()) {
            r(arrayList3);
            s(arrayList3);
            b9.O(new DrmInitData(str2, arrayList3));
        }
        ArrayList<e> arrayList4 = aVar.f21657f;
        arrayList4.addAll(arrayList2);
        return j.o(aVar.f21658g, b9.G(), aVar.f21653b, aVar.f21654c, arrayList4, aVar.f21659h, aVar.f21660i, null);
    }

    protected k.e j0(XmlPullParser xmlPullParser, k.e eVar) {
        long j8;
        long j9;
        long X = X(xmlPullParser, "timescale", eVar != null ? eVar.f21698b : 1L);
        long X2 = X(xmlPullParser, "presentationTimeOffset", eVar != null ? eVar.f21699c : 0L);
        long j10 = eVar != null ? eVar.f21712d : 0L;
        long j11 = eVar != null ? eVar.f21713e : 0L;
        String attributeValue = xmlPullParser.getAttributeValue(null, "indexRange");
        if (attributeValue != null) {
            String[] split = attributeValue.split("-");
            long parseLong = Long.parseLong(split[0]);
            j8 = (Long.parseLong(split[1]) - parseLong) + 1;
            j9 = parseLong;
        } else {
            j8 = j11;
            j9 = j10;
        }
        i iVar = eVar != null ? eVar.f21697a : null;
        do {
            xmlPullParser.next();
            if (m0.f(xmlPullParser, "Initialization")) {
                iVar = T(xmlPullParser);
            } else {
                w(xmlPullParser);
            }
        } while (!m0.d(xmlPullParser, "SegmentBase"));
        return n(iVar, X, X2, j9, j8);
    }

    protected k.b k(i iVar, long j8, long j9, long j10, long j11, List<k.d> list, long j12, List<i> list2, long j13, long j14) {
        return new k.b(iVar, j8, j9, j10, j11, list, j12, list2, l0.C0(j13), l0.C0(j14));
    }

    protected k.b k0(XmlPullParser xmlPullParser, k.b bVar, long j8, long j9, long j10, long j11, long j12) {
        long X = X(xmlPullParser, "timescale", bVar != null ? bVar.f21698b : 1L);
        long X2 = X(xmlPullParser, "presentationTimeOffset", bVar != null ? bVar.f21699c : 0L);
        long X3 = X(xmlPullParser, "duration", bVar != null ? bVar.f21701e : -9223372036854775807L);
        long X4 = X(xmlPullParser, "startNumber", bVar != null ? bVar.f21700d : 1L);
        long t8 = t(j10, j11);
        List<k.d> list = null;
        List<i> list2 = null;
        i iVar = null;
        do {
            xmlPullParser.next();
            if (m0.f(xmlPullParser, "Initialization")) {
                iVar = T(xmlPullParser);
            } else if (m0.f(xmlPullParser, "SegmentTimeline")) {
                list = m0(xmlPullParser, X, j9);
            } else if (m0.f(xmlPullParser, "SegmentURL")) {
                if (list2 == null) {
                    list2 = new ArrayList<>();
                }
                list2.add(n0(xmlPullParser));
            } else {
                w(xmlPullParser);
            }
        } while (!m0.d(xmlPullParser, "SegmentList"));
        if (bVar != null) {
            if (iVar == null) {
                iVar = bVar.f21697a;
            }
            if (list == null) {
                list = bVar.f21702f;
            }
            if (list2 == null) {
                list2 = bVar.f21706j;
            }
        }
        return k(iVar, X, X2, X4, X3, list, t8, list2, j12, j8);
    }

    protected k.c l(i iVar, long j8, long j9, long j10, long j11, long j12, List<k.d> list, long j13, n nVar, n nVar2, long j14, long j15) {
        return new k.c(iVar, j8, j9, j10, j11, j12, list, j13, nVar, nVar2, l0.C0(j14), l0.C0(j15));
    }

    protected k.c l0(XmlPullParser xmlPullParser, k.c cVar, List<e> list, long j8, long j9, long j10, long j11, long j12) {
        long X = X(xmlPullParser, "timescale", cVar != null ? cVar.f21698b : 1L);
        long X2 = X(xmlPullParser, "presentationTimeOffset", cVar != null ? cVar.f21699c : 0L);
        long X3 = X(xmlPullParser, "duration", cVar != null ? cVar.f21701e : -9223372036854775807L);
        long X4 = X(xmlPullParser, "startNumber", cVar != null ? cVar.f21700d : 1L);
        long W = W(list);
        long t8 = t(j10, j11);
        List<k.d> list2 = null;
        n v02 = v0(xmlPullParser, "media", cVar != null ? cVar.f21708k : null);
        n v03 = v0(xmlPullParser, "initialization", cVar != null ? cVar.f21707j : null);
        i iVar = null;
        while (true) {
            xmlPullParser.next();
            if (m0.f(xmlPullParser, "Initialization")) {
                iVar = T(xmlPullParser);
            } else if (m0.f(xmlPullParser, "SegmentTimeline")) {
                list2 = m0(xmlPullParser, X, j9);
            } else {
                w(xmlPullParser);
            }
            if (m0.d(xmlPullParser, "SegmentTemplate")) {
                break;
            }
        }
        if (cVar != null) {
            if (iVar == null) {
                iVar = cVar.f21697a;
            }
            if (list2 == null) {
                list2 = cVar.f21702f;
            }
        }
        return l(iVar, X, X2, X4, W, X3, list2, t8, v03, v02, j12, j8);
    }

    protected k.d m(long j8, long j9) {
        return new k.d(j8, j9);
    }

    protected List<k.d> m0(XmlPullParser xmlPullParser, long j8, long j9) {
        ArrayList arrayList = new ArrayList();
        long j10 = 0;
        boolean z4 = false;
        int i8 = 0;
        long j11 = -9223372036854775807L;
        do {
            xmlPullParser.next();
            if (m0.f(xmlPullParser, "S")) {
                long X = X(xmlPullParser, "t", -9223372036854775807L);
                if (z4) {
                    j10 = b(arrayList, j10, j11, i8, X);
                }
                if (X == -9223372036854775807L) {
                    X = j10;
                }
                j11 = X(xmlPullParser, "d", -9223372036854775807L);
                i8 = U(xmlPullParser, "r", 0);
                z4 = true;
                j10 = X;
            } else {
                w(xmlPullParser);
            }
        } while (!m0.d(xmlPullParser, "SegmentTimeline"));
        if (z4) {
            b(arrayList, j10, j11, i8, l0.O0(j9, j8, 1000L));
        }
        return arrayList;
    }

    protected k.e n(i iVar, long j8, long j9, long j10, long j11) {
        return new k.e(iVar, j8, j9, j10, j11);
    }

    protected i n0(XmlPullParser xmlPullParser) {
        return d0(xmlPullParser, "media", "mediaRange");
    }

    protected o o(String str, String str2) {
        return new o(str, str2);
    }

    protected int o0(String str) {
        if (str == null) {
            return 0;
        }
        return (str.equals("forced_subtitle") || str.equals("forced-subtitle")) ? 2 : 0;
    }

    protected int p0(List<e> list) {
        int i8 = 0;
        for (int i9 = 0; i9 < list.size(); i9++) {
            e eVar = list.get(i9);
            if (com.google.common.base.c.a("urn:mpeg:dash:role:2011", eVar.f21661a)) {
                i8 |= o0(eVar.f21662b);
            }
        }
        return i8;
    }

    protected l q0(XmlPullParser xmlPullParser) {
        float f5 = -3.4028235E38f;
        float f8 = -3.4028235E38f;
        long j8 = -9223372036854775807L;
        long j9 = -9223372036854775807L;
        long j10 = -9223372036854775807L;
        while (true) {
            xmlPullParser.next();
            if (m0.f(xmlPullParser, "Latency")) {
                j8 = X(xmlPullParser, "target", -9223372036854775807L);
                j9 = X(xmlPullParser, "min", -9223372036854775807L);
                j10 = X(xmlPullParser, "max", -9223372036854775807L);
            } else if (m0.f(xmlPullParser, "PlaybackRate")) {
                f5 = R(xmlPullParser, "min", -3.4028235E38f);
                f8 = R(xmlPullParser, "max", -3.4028235E38f);
            }
            long j11 = j8;
            long j12 = j9;
            long j13 = j10;
            float f9 = f5;
            float f10 = f8;
            if (m0.d(xmlPullParser, "ServiceDescription")) {
                return new l(j11, j12, j13, f9, f10);
            }
            j8 = j11;
            j9 = j12;
            j10 = j13;
            f5 = f9;
            f8 = f10;
        }
    }

    protected Pair<Integer, Integer> t0(List<e> list) {
        String str;
        for (int i8 = 0; i8 < list.size(); i8++) {
            e eVar = list.get(i8);
            if ((com.google.common.base.c.a("http://dashif.org/thumbnail_tile", eVar.f21661a) || com.google.common.base.c.a("http://dashif.org/guidelines/thumbnail_tile", eVar.f21661a)) && (str = eVar.f21662b) != null) {
                String[] R0 = l0.R0(str, "x");
                if (R0.length == 2) {
                    try {
                        return Pair.create(Integer.valueOf(Integer.parseInt(R0[0])), Integer.valueOf(Integer.parseInt(R0[1])));
                    } catch (NumberFormatException unused) {
                        continue;
                    }
                }
            }
        }
        return null;
    }

    protected int u0(String str) {
        if (str == null) {
            return 0;
        }
        char c9 = 65535;
        switch (str.hashCode()) {
            case 49:
                if (str.equals("1")) {
                    c9 = 0;
                    break;
                }
                break;
            case 50:
                if (str.equals("2")) {
                    c9 = 1;
                    break;
                }
                break;
            case 51:
                if (str.equals("3")) {
                    c9 = 2;
                    break;
                }
                break;
            case 52:
                if (str.equals("4")) {
                    c9 = 3;
                    break;
                }
                break;
            case 54:
                if (str.equals("6")) {
                    c9 = 4;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return RecognitionOptions.UPC_A;
            case 1:
                return RecognitionOptions.PDF417;
            case 2:
                return 4;
            case 3:
                return 8;
            case 4:
                return 1;
            default:
                return 0;
        }
    }

    protected n v0(XmlPullParser xmlPullParser, String str, n nVar) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue != null ? n.b(attributeValue) : nVar;
    }

    protected o w0(XmlPullParser xmlPullParser) {
        return o(xmlPullParser.getAttributeValue(null, "schemeIdUri"), xmlPullParser.getAttributeValue(null, "value"));
    }

    @Override // com.google.android.exoplayer2.upstream.d.a
    /* renamed from: x */
    public c a(Uri uri, InputStream inputStream) {
        try {
            XmlPullParser newPullParser = this.f21651a.newPullParser();
            newPullParser.setInput(inputStream, null);
            if (newPullParser.next() == 2 && "MPD".equals(newPullParser.getName())) {
                return Y(newPullParser, uri);
            }
            throw ParserException.c("inputStream does not contain a valid media presentation description", null);
        } catch (XmlPullParserException e8) {
            throw ParserException.c(null, e8);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:71:0x030e A[LOOP:0: B:3:0x007c->B:71:0x030e, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x02ce A[EDGE_INSN: B:72:0x02ce->B:65:0x02ce ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected l5.a y(org.xmlpull.v1.XmlPullParser r55, java.util.List<l5.b> r56, l5.k r57, long r58, long r60, long r62, long r64, long r66, boolean r68) {
        /*
            Method dump skipped, instructions count: 810
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: l5.d.y(org.xmlpull.v1.XmlPullParser, java.util.List, l5.k, long, long, long, long, long, boolean):l5.a");
    }

    protected void z(XmlPullParser xmlPullParser) {
        w(xmlPullParser);
    }
}
