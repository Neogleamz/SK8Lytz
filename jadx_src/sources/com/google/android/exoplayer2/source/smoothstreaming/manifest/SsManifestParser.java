package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import b6.l0;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.a;
import com.google.android.exoplayer2.upstream.d;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import v4.l;
import v4.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SsManifestParser implements d.a<com.google.android.exoplayer2.source.smoothstreaming.manifest.a> {

    /* renamed from: a  reason: collision with root package name */
    private final XmlPullParserFactory f10733a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class MissingFieldException extends ParserException {
        public MissingFieldException(String str) {
            super("Missing required field: " + str, null, true, 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {

        /* renamed from: a  reason: collision with root package name */
        private final String f10734a;

        /* renamed from: b  reason: collision with root package name */
        private final String f10735b;

        /* renamed from: c  reason: collision with root package name */
        private final a f10736c;

        /* renamed from: d  reason: collision with root package name */
        private final List<Pair<String, Object>> f10737d = new LinkedList();

        public a(a aVar, String str, String str2) {
            this.f10736c = aVar;
            this.f10734a = str;
            this.f10735b = str2;
        }

        private a e(a aVar, String str, String str2) {
            if ("QualityLevel".equals(str)) {
                return new c(aVar, str2);
            }
            if ("Protection".equals(str)) {
                return new b(aVar, str2);
            }
            if ("StreamIndex".equals(str)) {
                return new e(aVar, str2);
            }
            return null;
        }

        protected void a(Object obj) {
        }

        protected abstract Object b();

        protected final Object c(String str) {
            for (int i8 = 0; i8 < this.f10737d.size(); i8++) {
                Pair<String, Object> pair = this.f10737d.get(i8);
                if (((String) pair.first).equals(str)) {
                    return pair.second;
                }
            }
            a aVar = this.f10736c;
            if (aVar == null) {
                return null;
            }
            return aVar.c(str);
        }

        protected boolean d(String str) {
            return false;
        }

        public final Object f(XmlPullParser xmlPullParser) {
            boolean z4 = false;
            int i8 = 0;
            while (true) {
                int eventType = xmlPullParser.getEventType();
                if (eventType == 1) {
                    return null;
                }
                if (eventType == 2) {
                    String name = xmlPullParser.getName();
                    if (this.f10735b.equals(name)) {
                        n(xmlPullParser);
                        z4 = true;
                    } else if (z4) {
                        if (i8 > 0) {
                            i8++;
                        } else if (d(name)) {
                            n(xmlPullParser);
                        } else {
                            a e8 = e(this, name, this.f10734a);
                            if (e8 == null) {
                                i8 = 1;
                            } else {
                                a(e8.f(xmlPullParser));
                            }
                        }
                    }
                } else if (eventType != 3) {
                    if (eventType == 4 && z4 && i8 == 0) {
                        o(xmlPullParser);
                    }
                } else if (!z4) {
                    continue;
                } else if (i8 > 0) {
                    i8--;
                } else {
                    String name2 = xmlPullParser.getName();
                    h(xmlPullParser);
                    if (!d(name2)) {
                        return b();
                    }
                }
                xmlPullParser.next();
            }
        }

        protected final boolean g(XmlPullParser xmlPullParser, String str, boolean z4) {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            return attributeValue != null ? Boolean.parseBoolean(attributeValue) : z4;
        }

        protected void h(XmlPullParser xmlPullParser) {
        }

        protected final int i(XmlPullParser xmlPullParser, String str, int i8) {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue != null) {
                try {
                    return Integer.parseInt(attributeValue);
                } catch (NumberFormatException e8) {
                    throw ParserException.c(null, e8);
                }
            }
            return i8;
        }

        protected final long j(XmlPullParser xmlPullParser, String str, long j8) {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue != null) {
                try {
                    return Long.parseLong(attributeValue);
                } catch (NumberFormatException e8) {
                    throw ParserException.c(null, e8);
                }
            }
            return j8;
        }

        protected final int k(XmlPullParser xmlPullParser, String str) {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue != null) {
                try {
                    return Integer.parseInt(attributeValue);
                } catch (NumberFormatException e8) {
                    throw ParserException.c(null, e8);
                }
            }
            throw new MissingFieldException(str);
        }

        protected final long l(XmlPullParser xmlPullParser, String str) {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue != null) {
                try {
                    return Long.parseLong(attributeValue);
                } catch (NumberFormatException e8) {
                    throw ParserException.c(null, e8);
                }
            }
            throw new MissingFieldException(str);
        }

        protected final String m(XmlPullParser xmlPullParser, String str) {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue != null) {
                return attributeValue;
            }
            throw new MissingFieldException(str);
        }

        protected abstract void n(XmlPullParser xmlPullParser);

        protected void o(XmlPullParser xmlPullParser) {
        }

        protected final void p(String str, Object obj) {
            this.f10737d.add(Pair.create(str, obj));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends a {

        /* renamed from: e  reason: collision with root package name */
        private boolean f10738e;

        /* renamed from: f  reason: collision with root package name */
        private UUID f10739f;

        /* renamed from: g  reason: collision with root package name */
        private byte[] f10740g;

        public b(a aVar, String str) {
            super(aVar, str, "Protection");
        }

        private static p[] q(byte[] bArr) {
            return new p[]{new p(true, null, 8, r(bArr), 0, 0, null)};
        }

        private static byte[] r(byte[] bArr) {
            StringBuilder sb = new StringBuilder();
            for (int i8 = 0; i8 < bArr.length; i8 += 2) {
                sb.append((char) bArr[i8]);
            }
            String sb2 = sb.toString();
            byte[] decode = Base64.decode(sb2.substring(sb2.indexOf("<KID>") + 5, sb2.indexOf("</KID>")), 0);
            t(decode, 0, 3);
            t(decode, 1, 2);
            t(decode, 4, 5);
            t(decode, 6, 7);
            return decode;
        }

        private static String s(String str) {
            return (str.charAt(0) == '{' && str.charAt(str.length() - 1) == '}') ? str.substring(1, str.length() - 1) : str;
        }

        private static void t(byte[] bArr, int i8, int i9) {
            byte b9 = bArr[i8];
            bArr[i8] = bArr[i9];
            bArr[i9] = b9;
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public Object b() {
            UUID uuid = this.f10739f;
            return new a.C0115a(uuid, l.a(uuid, this.f10740g), q(this.f10740g));
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public boolean d(String str) {
            return "ProtectionHeader".equals(str);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public void h(XmlPullParser xmlPullParser) {
            if ("ProtectionHeader".equals(xmlPullParser.getName())) {
                this.f10738e = false;
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public void n(XmlPullParser xmlPullParser) {
            if ("ProtectionHeader".equals(xmlPullParser.getName())) {
                this.f10738e = true;
                this.f10739f = UUID.fromString(s(xmlPullParser.getAttributeValue(null, "SystemID")));
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public void o(XmlPullParser xmlPullParser) {
            if (this.f10738e) {
                this.f10740g = Base64.decode(xmlPullParser.getText(), 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c extends a {

        /* renamed from: e  reason: collision with root package name */
        private w0 f10741e;

        public c(a aVar, String str) {
            super(aVar, str, "QualityLevel");
        }

        private static List<byte[]> q(String str) {
            ArrayList arrayList = new ArrayList();
            if (!TextUtils.isEmpty(str)) {
                byte[] J = l0.J(str);
                byte[][] i8 = b6.e.i(J);
                if (i8 == null) {
                    arrayList.add(J);
                } else {
                    Collections.addAll(arrayList, i8);
                }
            }
            return arrayList;
        }

        private static String r(String str) {
            if (str.equalsIgnoreCase("H264") || str.equalsIgnoreCase("X264") || str.equalsIgnoreCase("AVC1") || str.equalsIgnoreCase("DAVC")) {
                return "video/avc";
            }
            if (str.equalsIgnoreCase("AAC") || str.equalsIgnoreCase("AACL") || str.equalsIgnoreCase("AACH") || str.equalsIgnoreCase("AACP")) {
                return "audio/mp4a-latm";
            }
            if (str.equalsIgnoreCase("TTML") || str.equalsIgnoreCase("DFXP")) {
                return "application/ttml+xml";
            }
            if (str.equalsIgnoreCase("ac-3") || str.equalsIgnoreCase("dac3")) {
                return "audio/ac3";
            }
            if (str.equalsIgnoreCase("ec-3") || str.equalsIgnoreCase("dec3")) {
                return "audio/eac3";
            }
            if (str.equalsIgnoreCase("dtsc")) {
                return "audio/vnd.dts";
            }
            if (str.equalsIgnoreCase("dtsh") || str.equalsIgnoreCase("dtsl")) {
                return "audio/vnd.dts.hd";
            }
            if (str.equalsIgnoreCase("dtse")) {
                return "audio/vnd.dts.hd;profile=lbr";
            }
            if (str.equalsIgnoreCase("opus")) {
                return "audio/opus";
            }
            return null;
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public Object b() {
            return this.f10741e;
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public void n(XmlPullParser xmlPullParser) {
            w0.b bVar = new w0.b();
            String r4 = r(m(xmlPullParser, "FourCC"));
            int intValue = ((Integer) c("Type")).intValue();
            if (intValue == 2) {
                bVar.M("video/mp4").n0(k(xmlPullParser, "MaxWidth")).S(k(xmlPullParser, "MaxHeight")).V(q(xmlPullParser.getAttributeValue(null, "CodecPrivateData")));
            } else if (intValue == 1) {
                if (r4 == null) {
                    r4 = "audio/mp4a-latm";
                }
                int k8 = k(xmlPullParser, "Channels");
                int k9 = k(xmlPullParser, "SamplingRate");
                List<byte[]> q = q(xmlPullParser.getAttributeValue(null, "CodecPrivateData"));
                if (q.isEmpty() && "audio/mp4a-latm".equals(r4)) {
                    q = Collections.singletonList(k4.a.a(k9, k8));
                }
                bVar.M("audio/mp4").J(k8).h0(k9).V(q);
            } else if (intValue == 3) {
                int i8 = 0;
                String str = (String) c("Subtype");
                if (str != null) {
                    if (str.equals("CAPT")) {
                        i8 = 64;
                    } else if (str.equals("DESC")) {
                        i8 = RecognitionOptions.UPC_E;
                    }
                }
                bVar.M("application/mp4").e0(i8);
            } else {
                bVar.M("application/mp4");
            }
            this.f10741e = bVar.U(xmlPullParser.getAttributeValue(null, "Index")).W((String) c("Name")).g0(r4).I(k(xmlPullParser, "Bitrate")).X((String) c("Language")).G();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d extends a {

        /* renamed from: e  reason: collision with root package name */
        private final List<a.b> f10742e;

        /* renamed from: f  reason: collision with root package name */
        private int f10743f;

        /* renamed from: g  reason: collision with root package name */
        private int f10744g;

        /* renamed from: h  reason: collision with root package name */
        private long f10745h;

        /* renamed from: i  reason: collision with root package name */
        private long f10746i;

        /* renamed from: j  reason: collision with root package name */
        private long f10747j;

        /* renamed from: k  reason: collision with root package name */
        private int f10748k;

        /* renamed from: l  reason: collision with root package name */
        private boolean f10749l;

        /* renamed from: m  reason: collision with root package name */
        private a.C0115a f10750m;

        public d(a aVar, String str) {
            super(aVar, str, "SmoothStreamingMedia");
            this.f10748k = -1;
            this.f10750m = null;
            this.f10742e = new LinkedList();
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public void a(Object obj) {
            if (obj instanceof a.b) {
                this.f10742e.add((a.b) obj);
            } else if (obj instanceof a.C0115a) {
                b6.a.f(this.f10750m == null);
                this.f10750m = (a.C0115a) obj;
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public Object b() {
            int size = this.f10742e.size();
            a.b[] bVarArr = new a.b[size];
            this.f10742e.toArray(bVarArr);
            if (this.f10750m != null) {
                a.C0115a c0115a = this.f10750m;
                DrmInitData drmInitData = new DrmInitData(new DrmInitData.SchemeData(c0115a.f10772a, "video/mp4", c0115a.f10773b));
                for (int i8 = 0; i8 < size; i8++) {
                    a.b bVar = bVarArr[i8];
                    int i9 = bVar.f10775a;
                    if (i9 == 2 || i9 == 1) {
                        w0[] w0VarArr = bVar.f10784j;
                        for (int i10 = 0; i10 < w0VarArr.length; i10++) {
                            w0VarArr[i10] = w0VarArr[i10].b().O(drmInitData).G();
                        }
                    }
                }
            }
            return new com.google.android.exoplayer2.source.smoothstreaming.manifest.a(this.f10743f, this.f10744g, this.f10745h, this.f10746i, this.f10747j, this.f10748k, this.f10749l, this.f10750m, bVarArr);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public void n(XmlPullParser xmlPullParser) {
            this.f10743f = k(xmlPullParser, "MajorVersion");
            this.f10744g = k(xmlPullParser, "MinorVersion");
            this.f10745h = j(xmlPullParser, "TimeScale", 10000000L);
            this.f10746i = l(xmlPullParser, "Duration");
            this.f10747j = j(xmlPullParser, "DVRWindowLength", 0L);
            this.f10748k = i(xmlPullParser, "LookaheadCount", -1);
            this.f10749l = g(xmlPullParser, "IsLive", false);
            p("TimeScale", Long.valueOf(this.f10745h));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e extends a {

        /* renamed from: e  reason: collision with root package name */
        private final String f10751e;

        /* renamed from: f  reason: collision with root package name */
        private final List<w0> f10752f;

        /* renamed from: g  reason: collision with root package name */
        private int f10753g;

        /* renamed from: h  reason: collision with root package name */
        private String f10754h;

        /* renamed from: i  reason: collision with root package name */
        private long f10755i;

        /* renamed from: j  reason: collision with root package name */
        private String f10756j;

        /* renamed from: k  reason: collision with root package name */
        private String f10757k;

        /* renamed from: l  reason: collision with root package name */
        private int f10758l;

        /* renamed from: m  reason: collision with root package name */
        private int f10759m;

        /* renamed from: n  reason: collision with root package name */
        private int f10760n;

        /* renamed from: o  reason: collision with root package name */
        private int f10761o;

        /* renamed from: p  reason: collision with root package name */
        private String f10762p;
        private ArrayList<Long> q;

        /* renamed from: r  reason: collision with root package name */
        private long f10763r;

        public e(a aVar, String str) {
            super(aVar, str, "StreamIndex");
            this.f10751e = str;
            this.f10752f = new LinkedList();
        }

        private void q(XmlPullParser xmlPullParser) {
            int s8 = s(xmlPullParser);
            this.f10753g = s8;
            p("Type", Integer.valueOf(s8));
            this.f10754h = this.f10753g == 3 ? m(xmlPullParser, "Subtype") : xmlPullParser.getAttributeValue(null, "Subtype");
            p("Subtype", this.f10754h);
            String attributeValue = xmlPullParser.getAttributeValue(null, "Name");
            this.f10756j = attributeValue;
            p("Name", attributeValue);
            this.f10757k = m(xmlPullParser, "Url");
            this.f10758l = i(xmlPullParser, "MaxWidth", -1);
            this.f10759m = i(xmlPullParser, "MaxHeight", -1);
            this.f10760n = i(xmlPullParser, "DisplayWidth", -1);
            this.f10761o = i(xmlPullParser, "DisplayHeight", -1);
            String attributeValue2 = xmlPullParser.getAttributeValue(null, "Language");
            this.f10762p = attributeValue2;
            p("Language", attributeValue2);
            long i8 = i(xmlPullParser, "TimeScale", -1);
            this.f10755i = i8;
            if (i8 == -1) {
                this.f10755i = ((Long) c("TimeScale")).longValue();
            }
            this.q = new ArrayList<>();
        }

        private void r(XmlPullParser xmlPullParser) {
            int size = this.q.size();
            long j8 = j(xmlPullParser, "t", -9223372036854775807L);
            int i8 = 1;
            if (j8 == -9223372036854775807L) {
                if (size == 0) {
                    j8 = 0;
                } else if (this.f10763r == -1) {
                    throw ParserException.c("Unable to infer start time", null);
                } else {
                    j8 = this.f10763r + this.q.get(size - 1).longValue();
                }
            }
            this.q.add(Long.valueOf(j8));
            this.f10763r = j(xmlPullParser, "d", -9223372036854775807L);
            long j9 = j(xmlPullParser, "r", 1L);
            if (j9 > 1 && this.f10763r == -9223372036854775807L) {
                throw ParserException.c("Repeated chunk with unspecified duration", null);
            }
            while (true) {
                long j10 = i8;
                if (j10 >= j9) {
                    return;
                }
                this.q.add(Long.valueOf((this.f10763r * j10) + j8));
                i8++;
            }
        }

        private int s(XmlPullParser xmlPullParser) {
            String attributeValue = xmlPullParser.getAttributeValue(null, "Type");
            if (attributeValue != null) {
                if ("audio".equalsIgnoreCase(attributeValue)) {
                    return 1;
                }
                if ("video".equalsIgnoreCase(attributeValue)) {
                    return 2;
                }
                if ("text".equalsIgnoreCase(attributeValue)) {
                    return 3;
                }
                throw ParserException.c("Invalid key value[" + attributeValue + "]", null);
            }
            throw new MissingFieldException("Type");
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public void a(Object obj) {
            if (obj instanceof w0) {
                this.f10752f.add((w0) obj);
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public Object b() {
            w0[] w0VarArr = new w0[this.f10752f.size()];
            this.f10752f.toArray(w0VarArr);
            return new a.b(this.f10751e, this.f10757k, this.f10753g, this.f10754h, this.f10755i, this.f10756j, this.f10758l, this.f10759m, this.f10760n, this.f10761o, this.f10762p, w0VarArr, this.q, this.f10763r);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public boolean d(String str) {
            return "c".equals(str);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.a
        public void n(XmlPullParser xmlPullParser) {
            if ("c".equals(xmlPullParser.getName())) {
                r(xmlPullParser);
            } else {
                q(xmlPullParser);
            }
        }
    }

    public SsManifestParser() {
        try {
            this.f10733a = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e8) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e8);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.d.a
    /* renamed from: b */
    public com.google.android.exoplayer2.source.smoothstreaming.manifest.a a(Uri uri, InputStream inputStream) {
        try {
            XmlPullParser newPullParser = this.f10733a.newPullParser();
            newPullParser.setInput(inputStream, null);
            return (com.google.android.exoplayer2.source.smoothstreaming.manifest.a) new d(null, uri.toString()).f(newPullParser);
        } catch (XmlPullParserException e8) {
            throw ParserException.c(null, e8);
        }
    }
}
