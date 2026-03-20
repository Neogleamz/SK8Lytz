package s4;

import b6.m0;
import b6.p;
import com.google.android.exoplayer2.ParserException;
import com.google.common.collect.ImmutableList;
import java.io.StringReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import s4.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e {

    /* renamed from: a  reason: collision with root package name */
    private static final String[] f22794a = {"Camera:MotionPhoto", "GCamera:MotionPhoto", "Camera:MicroVideo", "GCamera:MicroVideo"};

    /* renamed from: b  reason: collision with root package name */
    private static final String[] f22795b = {"Camera:MotionPhotoPresentationTimestampUs", "GCamera:MotionPhotoPresentationTimestampUs", "Camera:MicroVideoPresentationTimestampUs", "GCamera:MicroVideoPresentationTimestampUs"};

    /* renamed from: c  reason: collision with root package name */
    private static final String[] f22796c = {"Camera:MicroVideoOffset", "GCamera:MicroVideoOffset"};

    public static b a(String str) {
        try {
            return b(str);
        } catch (ParserException | NumberFormatException | XmlPullParserException unused) {
            p.i("MotionPhotoXmpParser", "Ignoring unexpected XMP metadata");
            return null;
        }
    }

    private static b b(String str) {
        String str2;
        String str3;
        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        newPullParser.setInput(new StringReader(str));
        newPullParser.next();
        if (m0.f(newPullParser, "x:xmpmeta")) {
            long j8 = -9223372036854775807L;
            ImmutableList<b.a> E = ImmutableList.E();
            do {
                newPullParser.next();
                if (!m0.f(newPullParser, "rdf:Description")) {
                    if (m0.f(newPullParser, "Container:Directory")) {
                        str2 = "Container";
                        str3 = "Item";
                    } else if (m0.f(newPullParser, "GContainer:Directory")) {
                        str2 = "GContainer";
                        str3 = "GContainerItem";
                    }
                    E = f(newPullParser, str2, str3);
                } else if (!d(newPullParser)) {
                    return null;
                } else {
                    j8 = e(newPullParser);
                    E = c(newPullParser);
                }
            } while (!m0.d(newPullParser, "x:xmpmeta"));
            if (E.isEmpty()) {
                return null;
            }
            return new b(j8, E);
        }
        throw ParserException.a("Couldn't find xmp metadata", null);
    }

    private static ImmutableList<b.a> c(XmlPullParser xmlPullParser) {
        for (String str : f22796c) {
            String a9 = m0.a(xmlPullParser, str);
            if (a9 != null) {
                return ImmutableList.G(new b.a("image/jpeg", "Primary", 0L, 0L), new b.a("video/mp4", "MotionPhoto", Long.parseLong(a9), 0L));
            }
        }
        return ImmutableList.E();
    }

    private static boolean d(XmlPullParser xmlPullParser) {
        for (String str : f22794a) {
            String a9 = m0.a(xmlPullParser, str);
            if (a9 != null) {
                return Integer.parseInt(a9) == 1;
            }
        }
        return false;
    }

    private static long e(XmlPullParser xmlPullParser) {
        for (String str : f22795b) {
            String a9 = m0.a(xmlPullParser, str);
            if (a9 != null) {
                long parseLong = Long.parseLong(a9);
                if (parseLong == -1) {
                    return -9223372036854775807L;
                }
                return parseLong;
            }
        }
        return -9223372036854775807L;
    }

    private static ImmutableList<b.a> f(XmlPullParser xmlPullParser, String str, String str2) {
        ImmutableList.a u8 = ImmutableList.u();
        String str3 = str + ":Item";
        String str4 = str + ":Directory";
        do {
            xmlPullParser.next();
            if (m0.f(xmlPullParser, str3)) {
                String a9 = m0.a(xmlPullParser, str2 + ":Mime");
                String a10 = m0.a(xmlPullParser, str2 + ":Semantic");
                String a11 = m0.a(xmlPullParser, str2 + ":Length");
                String a12 = m0.a(xmlPullParser, str2 + ":Padding");
                if (a9 == null || a10 == null) {
                    return ImmutableList.E();
                }
                u8.a(new b.a(a9, a10, a11 != null ? Long.parseLong(a11) : 0L, a12 != null ? Long.parseLong(a12) : 0L));
            }
        } while (!m0.d(xmlPullParser, str4));
        return u8.k();
    }
}
