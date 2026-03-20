package androidx.camera.core.impl.utils;

import android.location.Location;
import androidx.camera.core.l1;
import androidx.camera.core.p1;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {

    /* renamed from: c  reason: collision with root package name */
    private static final String f2626c = "f";

    /* renamed from: d  reason: collision with root package name */
    private static final ThreadLocal<SimpleDateFormat> f2627d = new a();

    /* renamed from: e  reason: collision with root package name */
    private static final ThreadLocal<SimpleDateFormat> f2628e = new b();

    /* renamed from: f  reason: collision with root package name */
    private static final ThreadLocal<SimpleDateFormat> f2629f = new c();

    /* renamed from: g  reason: collision with root package name */
    private static final List<String> f2630g = i();

    /* renamed from: h  reason: collision with root package name */
    private static final List<String> f2631h = Arrays.asList("ImageWidth", "ImageLength", "PixelXDimension", "PixelYDimension", "Compression", "JPEGInterchangeFormat", "JPEGInterchangeFormatLength", "ThumbnailImageLength", "ThumbnailImageWidth", "ThumbnailOrientation");

    /* renamed from: a  reason: collision with root package name */
    private final androidx.exifinterface.media.a f2632a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f2633b = false;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends ThreadLocal<SimpleDateFormat> {
        a() {
        }

        @Override // java.lang.ThreadLocal
        /* renamed from: a */
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy:MM:dd", Locale.US);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends ThreadLocal<SimpleDateFormat> {
        b() {
        }

        @Override // java.lang.ThreadLocal
        /* renamed from: a */
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss", Locale.US);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends ThreadLocal<SimpleDateFormat> {
        c() {
        }

        @Override // java.lang.ThreadLocal
        /* renamed from: a */
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a {

            /* renamed from: a  reason: collision with root package name */
            final double f2634a;

            a(double d8) {
                this.f2634a = d8;
            }

            double a() {
                return this.f2634a / 2.23694d;
            }
        }

        static a a(double d8) {
            return new a(d8 * 0.621371d);
        }

        static a b(double d8) {
            return new a(d8 * 1.15078d);
        }

        static a c(double d8) {
            return new a(d8);
        }
    }

    private f(androidx.exifinterface.media.a aVar) {
        this.f2632a = aVar;
    }

    private static Date a(String str) {
        return f2627d.get().parse(str);
    }

    private static Date b(String str) {
        return f2629f.get().parse(str);
    }

    private static Date c(String str) {
        return f2628e.get().parse(str);
    }

    public static f e(File file) {
        return f(file.toString());
    }

    public static f f(String str) {
        return new f(new androidx.exifinterface.media.a(str));
    }

    public static f g(l1 l1Var) {
        ByteBuffer b9 = l1Var.A()[0].b();
        b9.rewind();
        byte[] bArr = new byte[b9.capacity()];
        b9.get(bArr);
        return h(new ByteArrayInputStream(bArr));
    }

    public static f h(InputStream inputStream) {
        return new f(new androidx.exifinterface.media.a(inputStream));
    }

    public static List<String> i() {
        return Arrays.asList("ImageWidth", "ImageLength", "BitsPerSample", "Compression", "PhotometricInterpretation", "Orientation", "SamplesPerPixel", "PlanarConfiguration", "YCbCrSubSampling", "YCbCrPositioning", "XResolution", "YResolution", "ResolutionUnit", "StripOffsets", "RowsPerStrip", "StripByteCounts", "JPEGInterchangeFormat", "JPEGInterchangeFormatLength", "TransferFunction", "WhitePoint", "PrimaryChromaticities", "YCbCrCoefficients", "ReferenceBlackWhite", "DateTime", "ImageDescription", "Make", "Model", "Software", "Artist", "Copyright", "ExifVersion", "FlashpixVersion", "ColorSpace", "Gamma", "PixelXDimension", "PixelYDimension", "ComponentsConfiguration", "CompressedBitsPerPixel", "MakerNote", "UserComment", "RelatedSoundFile", "DateTimeOriginal", "DateTimeDigitized", "OffsetTime", "OffsetTimeOriginal", "OffsetTimeDigitized", "SubSecTime", "SubSecTimeOriginal", "SubSecTimeDigitized", "ExposureTime", "FNumber", "ExposureProgram", "SpectralSensitivity", "PhotographicSensitivity", "OECF", "SensitivityType", "StandardOutputSensitivity", "RecommendedExposureIndex", "ISOSpeed", "ISOSpeedLatitudeyyy", "ISOSpeedLatitudezzz", "ShutterSpeedValue", "ApertureValue", "BrightnessValue", "ExposureBiasValue", "MaxApertureValue", "SubjectDistance", "MeteringMode", "LightSource", "Flash", "SubjectArea", "FocalLength", "FlashEnergy", "SpatialFrequencyResponse", "FocalPlaneXResolution", "FocalPlaneYResolution", "FocalPlaneResolutionUnit", "SubjectLocation", "ExposureIndex", "SensingMethod", "FileSource", "SceneType", "CFAPattern", "CustomRendered", "ExposureMode", "WhiteBalance", "DigitalZoomRatio", "FocalLengthIn35mmFilm", "SceneCaptureType", "GainControl", "Contrast", "Saturation", "Sharpness", "DeviceSettingDescription", "SubjectDistanceRange", "ImageUniqueID", "CameraOwnerName", "BodySerialNumber", "LensSpecification", "LensMake", "LensModel", "LensSerialNumber", "GPSVersionID", "GPSLatitudeRef", "GPSLatitude", "GPSLongitudeRef", "GPSLongitude", "GPSAltitudeRef", "GPSAltitude", "GPSTimeStamp", "GPSSatellites", "GPSStatus", "GPSMeasureMode", "GPSDOP", "GPSSpeedRef", "GPSSpeed", "GPSTrackRef", "GPSTrack", "GPSImgDirectionRef", "GPSImgDirection", "GPSMapDatum", "GPSDestLatitudeRef", "GPSDestLatitude", "GPSDestLongitudeRef", "GPSDestLongitude", "GPSDestBearingRef", "GPSDestBearing", "GPSDestDistanceRef", "GPSDestDistance", "GPSProcessingMethod", "GPSAreaInformation", "GPSDateStamp", "GPSDifferential", "GPSHPositioningError", "InteroperabilityIndex", "ThumbnailImageLength", "ThumbnailImageWidth", "ThumbnailOrientation", "DNGVersion", "DefaultCropSize", "ThumbnailImage", "PreviewImageStart", "PreviewImageLength", "AspectFrame", "SensorBottomBorder", "SensorLeftBorder", "SensorRightBorder", "SensorTopBorder", "ISO", "JpgFromRaw", "Xmp", "NewSubfileType", "SubfileType");
    }

    private long s(String str) {
        if (str == null) {
            return -1L;
        }
        try {
            return b(str).getTime();
        } catch (ParseException unused) {
            return -1L;
        }
    }

    private long t(String str, String str2) {
        if (str == null && str2 == null) {
            return -1L;
        }
        if (str2 == null) {
            try {
                return a(str).getTime();
            } catch (ParseException unused) {
                return -1L;
            }
        } else if (str == null) {
            try {
                return c(str2).getTime();
            } catch (ParseException unused2) {
                return -1L;
            }
        } else {
            return s(str + " " + str2);
        }
    }

    public void d(f fVar) {
        ArrayList<String> arrayList = new ArrayList(f2630g);
        arrayList.removeAll(f2631h);
        for (String str : arrayList) {
            String f5 = this.f2632a.f(str);
            String f8 = fVar.f2632a.f(str);
            if (f5 != null && !f5.equals(f8)) {
                fVar.f2632a.a0(str, f5);
            }
        }
    }

    public String j() {
        return this.f2632a.f("ImageDescription");
    }

    public int k() {
        return this.f2632a.h("ImageLength", 0);
    }

    public Location l() {
        String f5 = this.f2632a.f("GPSProcessingMethod");
        double[] l8 = this.f2632a.l();
        double e8 = this.f2632a.e(0.0d);
        double g8 = this.f2632a.g("GPSSpeed", 0.0d);
        String f8 = this.f2632a.f("GPSSpeedRef");
        if (f8 == null) {
            f8 = "K";
        }
        long t8 = t(this.f2632a.f("GPSDateStamp"), this.f2632a.f("GPSTimeStamp"));
        if (l8 == null) {
            return null;
        }
        if (f5 == null) {
            f5 = f2626c;
        }
        Location location = new Location(f5);
        location.setLatitude(l8[0]);
        location.setLongitude(l8[1]);
        if (e8 != 0.0d) {
            location.setAltitude(e8);
        }
        if (g8 != 0.0d) {
            char c9 = 65535;
            int hashCode = f8.hashCode();
            if (hashCode != 75) {
                if (hashCode != 77) {
                    if (hashCode == 78 && f8.equals("N")) {
                        c9 = 1;
                    }
                } else if (f8.equals("M")) {
                    c9 = 0;
                }
            } else if (f8.equals("K")) {
                c9 = 2;
            }
            location.setSpeed((float) (c9 != 0 ? c9 != 1 ? d.a(g8) : d.b(g8) : d.c(g8)).a());
        }
        if (t8 != -1) {
            location.setTime(t8);
        }
        return location;
    }

    public int m() {
        return this.f2632a.h("Orientation", 0);
    }

    public int n() {
        switch (m()) {
            case 3:
            case 4:
                return 180;
            case 5:
                return 270;
            case 6:
            case 7:
                return 90;
            case 8:
                return 270;
            default:
                return 0;
        }
    }

    public long o() {
        long s8 = s(this.f2632a.f("DateTimeOriginal"));
        if (s8 == -1) {
            return -1L;
        }
        String f5 = this.f2632a.f("SubSecTimeOriginal");
        if (f5 != null) {
            try {
                long parseLong = Long.parseLong(f5);
                while (parseLong > 1000) {
                    parseLong /= 10;
                }
                return s8 + parseLong;
            } catch (NumberFormatException unused) {
                return s8;
            }
        }
        return s8;
    }

    public int p() {
        return this.f2632a.h("ImageWidth", 0);
    }

    public boolean q() {
        return m() == 2;
    }

    public boolean r() {
        int m8 = m();
        return m8 == 4 || m8 == 5 || m8 == 7;
    }

    public String toString() {
        return String.format(Locale.ENGLISH, "Exif{width=%s, height=%s, rotation=%d, isFlippedVertically=%s, isFlippedHorizontally=%s, location=%s, timestamp=%s, description=%s}", Integer.valueOf(p()), Integer.valueOf(k()), Integer.valueOf(n()), Boolean.valueOf(r()), Boolean.valueOf(q()), l(), Long.valueOf(o()), j());
    }

    public void u(int i8) {
        androidx.exifinterface.media.a aVar;
        String valueOf;
        if (i8 % 90 != 0) {
            p1.k(f2626c, String.format(Locale.US, "Can only rotate in right angles (eg. 0, 90, 180, 270). %d is unsupported.", Integer.valueOf(i8)));
            aVar = this.f2632a;
            valueOf = String.valueOf(0);
        } else {
            int i9 = i8 % 360;
            int m8 = m();
            while (i9 < 0) {
                i9 += 90;
                switch (m8) {
                    case 2:
                        m8 = 5;
                        break;
                    case 3:
                    case 8:
                        m8 = 6;
                        break;
                    case 4:
                        m8 = 7;
                        break;
                    case 5:
                        m8 = 4;
                        break;
                    case 6:
                        m8 = 1;
                        break;
                    case 7:
                        m8 = 2;
                        break;
                    default:
                        m8 = 8;
                        break;
                }
            }
            while (i9 > 0) {
                i9 -= 90;
                switch (m8) {
                    case 2:
                        m8 = 7;
                        break;
                    case 3:
                        m8 = 8;
                        break;
                    case 4:
                        m8 = 5;
                        break;
                    case 5:
                        m8 = 2;
                        break;
                    case 6:
                        m8 = 3;
                        break;
                    case 7:
                        m8 = 4;
                        break;
                    case 8:
                        m8 = 1;
                        break;
                    default:
                        m8 = 6;
                        break;
                }
            }
            aVar = this.f2632a;
            valueOf = String.valueOf(m8);
        }
        aVar.a0("Orientation", valueOf);
    }
}
