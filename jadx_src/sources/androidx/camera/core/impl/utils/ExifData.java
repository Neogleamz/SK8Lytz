package androidx.camera.core.impl.utils;

import android.os.Build;
import android.util.Pair;
import androidx.camera.core.impl.CameraCaptureMetaData$FlashState;
import androidx.camera.core.l1;
import androidx.camera.core.p1;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ExifData {

    /* renamed from: c  reason: collision with root package name */
    static final String[] f2592c = {BuildConfig.FLAVOR, "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE", "IFD"};

    /* renamed from: d  reason: collision with root package name */
    private static final i[] f2593d;

    /* renamed from: e  reason: collision with root package name */
    private static final i[] f2594e;

    /* renamed from: f  reason: collision with root package name */
    private static final i[] f2595f;

    /* renamed from: g  reason: collision with root package name */
    static final i[] f2596g;

    /* renamed from: h  reason: collision with root package name */
    private static final i[] f2597h;

    /* renamed from: i  reason: collision with root package name */
    static final i[][] f2598i;

    /* renamed from: j  reason: collision with root package name */
    static final HashSet<String> f2599j;

    /* renamed from: a  reason: collision with root package name */
    private final List<Map<String, g>> f2600a;

    /* renamed from: b  reason: collision with root package name */
    private final ByteOrder f2601b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum WhiteBalanceMode {
        AUTO,
        MANUAL
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f2605a;

        /* renamed from: b  reason: collision with root package name */
        static final /* synthetic */ int[] f2606b;

        static {
            int[] iArr = new int[WhiteBalanceMode.values().length];
            f2606b = iArr;
            try {
                iArr[WhiteBalanceMode.AUTO.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f2606b[WhiteBalanceMode.MANUAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            int[] iArr2 = new int[CameraCaptureMetaData$FlashState.values().length];
            f2605a = iArr2;
            try {
                iArr2[CameraCaptureMetaData$FlashState.READY.ordinal()] = 1;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f2605a[CameraCaptureMetaData$FlashState.NONE.ordinal()] = 2;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f2605a[CameraCaptureMetaData$FlashState.FIRED.ordinal()] = 3;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: c  reason: collision with root package name */
        private static final Pattern f2607c = Pattern.compile("^(\\d{2}):(\\d{2}):(\\d{2})$");

        /* renamed from: d  reason: collision with root package name */
        private static final Pattern f2608d = Pattern.compile("^(\\d{4}):(\\d{2}):(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2})$");

        /* renamed from: e  reason: collision with root package name */
        private static final Pattern f2609e = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2})$");

        /* renamed from: f  reason: collision with root package name */
        static final List<HashMap<String, i>> f2610f = Collections.list(new a());

        /* renamed from: a  reason: collision with root package name */
        final List<Map<String, g>> f2611a = Collections.list(new C0018b());

        /* renamed from: b  reason: collision with root package name */
        private final ByteOrder f2612b;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Enumeration<HashMap<String, i>> {

            /* renamed from: a  reason: collision with root package name */
            int f2613a = 0;

            a() {
            }

            @Override // java.util.Enumeration
            /* renamed from: a */
            public HashMap<String, i> nextElement() {
                i[] iVarArr;
                HashMap<String, i> hashMap = new HashMap<>();
                for (i iVar : ExifData.f2598i[this.f2613a]) {
                    hashMap.put(iVar.f2651b, iVar);
                }
                this.f2613a++;
                return hashMap;
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.f2613a < ExifData.f2598i.length;
            }
        }

        /* renamed from: androidx.camera.core.impl.utils.ExifData$b$b  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class C0018b implements Enumeration<Map<String, g>> {

            /* renamed from: a  reason: collision with root package name */
            int f2614a = 0;

            C0018b() {
            }

            @Override // java.util.Enumeration
            /* renamed from: a */
            public Map<String, g> nextElement() {
                this.f2614a++;
                return new HashMap();
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.f2614a < ExifData.f2598i.length;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class c implements Enumeration<Map<String, g>> {

            /* renamed from: a  reason: collision with root package name */
            final Enumeration<Map<String, g>> f2616a;

            c() {
                this.f2616a = Collections.enumeration(b.this.f2611a);
            }

            @Override // java.util.Enumeration
            /* renamed from: a */
            public Map<String, g> nextElement() {
                return new HashMap(this.f2616a.nextElement());
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.f2616a.hasMoreElements();
            }
        }

        b(ByteOrder byteOrder) {
            this.f2612b = byteOrder;
        }

        private static Pair<Integer, Integer> b(String str) {
            if (str.contains(",")) {
                String[] split = str.split(",", -1);
                Pair<Integer, Integer> b9 = b(split[0]);
                if (((Integer) b9.first).intValue() == 2) {
                    return b9;
                }
                for (int i8 = 1; i8 < split.length; i8++) {
                    Pair<Integer, Integer> b10 = b(split[i8]);
                    int intValue = (((Integer) b10.first).equals(b9.first) || ((Integer) b10.second).equals(b9.first)) ? ((Integer) b9.first).intValue() : -1;
                    int intValue2 = (((Integer) b9.second).intValue() == -1 || !(((Integer) b10.first).equals(b9.second) || ((Integer) b10.second).equals(b9.second))) ? -1 : ((Integer) b9.second).intValue();
                    if (intValue == -1 && intValue2 == -1) {
                        return new Pair<>(2, -1);
                    }
                    if (intValue == -1) {
                        b9 = new Pair<>(Integer.valueOf(intValue2), -1);
                    } else if (intValue2 == -1) {
                        b9 = new Pair<>(Integer.valueOf(intValue), -1);
                    }
                }
                return b9;
            } else if (!str.contains("/")) {
                try {
                    try {
                        long parseLong = Long.parseLong(str);
                        int i9 = (parseLong > 0L ? 1 : (parseLong == 0L ? 0 : -1));
                        return (i9 < 0 || parseLong > 65535) ? i9 < 0 ? new Pair<>(9, -1) : new Pair<>(4, -1) : new Pair<>(3, 4);
                    } catch (NumberFormatException unused) {
                        return new Pair<>(2, -1);
                    }
                } catch (NumberFormatException unused2) {
                    Double.parseDouble(str);
                    return new Pair<>(12, -1);
                }
            } else {
                String[] split2 = str.split("/", -1);
                if (split2.length == 2) {
                    try {
                        long parseDouble = (long) Double.parseDouble(split2[0]);
                        long parseDouble2 = (long) Double.parseDouble(split2[1]);
                        if (parseDouble >= 0 && parseDouble2 >= 0) {
                            if (parseDouble <= 2147483647L && parseDouble2 <= 2147483647L) {
                                return new Pair<>(10, 5);
                            }
                            return new Pair<>(5, -1);
                        }
                        return new Pair<>(10, -1);
                    } catch (NumberFormatException unused3) {
                    }
                }
                return new Pair<>(2, -1);
            }
        }

        private void d(String str, String str2, List<Map<String, g>> list) {
            for (Map<String, g> map : list) {
                if (map.containsKey(str)) {
                    return;
                }
            }
            e(str, str2, list);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x018a, code lost:
            if (r7 != r0) goto L97;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void e(java.lang.String r18, java.lang.String r19, java.util.List<java.util.Map<java.lang.String, androidx.camera.core.impl.utils.g>> r20) {
            /*
                Method dump skipped, instructions count: 758
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.camera.core.impl.utils.ExifData.b.e(java.lang.String, java.lang.String, java.util.List):void");
        }

        public ExifData a() {
            ArrayList list = Collections.list(new c());
            if (!list.get(1).isEmpty()) {
                d("ExposureProgram", String.valueOf(0), list);
                d("ExifVersion", "0230", list);
                d("ComponentsConfiguration", "1,2,3,0", list);
                d("MeteringMode", String.valueOf(0), list);
                d("LightSource", String.valueOf(0), list);
                d("FlashpixVersion", "0100", list);
                d("FocalPlaneResolutionUnit", String.valueOf(2), list);
                d("FileSource", String.valueOf(3), list);
                d("SceneType", String.valueOf(1), list);
                d("CustomRendered", String.valueOf(0), list);
                d("SceneCaptureType", String.valueOf(0), list);
                d("Contrast", String.valueOf(0), list);
                d("Saturation", String.valueOf(0), list);
                d("Sharpness", String.valueOf(0), list);
            }
            if (!list.get(2).isEmpty()) {
                d("GPSVersionID", "2300", list);
                d("GPSSpeedRef", "K", list);
                d("GPSTrackRef", "T", list);
                d("GPSImgDirectionRef", "T", list);
                d("GPSDestBearingRef", "T", list);
                d("GPSDestDistanceRef", "K", list);
            }
            return new ExifData(this.f2612b, list);
        }

        public b c(String str, String str2) {
            e(str, str2, this.f2611a);
            return this;
        }

        public b f(long j8) {
            return c("ExposureTime", String.valueOf(j8 / TimeUnit.SECONDS.toNanos(1L)));
        }

        public b g(CameraCaptureMetaData$FlashState cameraCaptureMetaData$FlashState) {
            int i8;
            if (cameraCaptureMetaData$FlashState == CameraCaptureMetaData$FlashState.UNKNOWN) {
                return this;
            }
            int i9 = a.f2605a[cameraCaptureMetaData$FlashState.ordinal()];
            if (i9 == 1) {
                i8 = 0;
            } else if (i9 == 2) {
                i8 = 32;
            } else if (i9 != 3) {
                p1.k("ExifData", "Unknown flash state: " + cameraCaptureMetaData$FlashState);
                return this;
            } else {
                i8 = 1;
            }
            if ((i8 & 1) == 1) {
                c("LightSource", String.valueOf(4));
            }
            return c("Flash", String.valueOf(i8));
        }

        public b h(float f5) {
            return c("FocalLength", new j(f5 * 1000.0f, 1000L).toString());
        }

        public b i(int i8) {
            return c("ImageLength", String.valueOf(i8));
        }

        public b j(int i8) {
            return c("ImageWidth", String.valueOf(i8));
        }

        public b k(int i8) {
            return c("SensitivityType", String.valueOf(3)).c("PhotographicSensitivity", String.valueOf(Math.min(65535, i8)));
        }

        public b l(float f5) {
            return c("FNumber", String.valueOf(f5));
        }

        public b m(int i8) {
            int i9;
            if (i8 == 0) {
                i9 = 1;
            } else if (i8 == 90) {
                i9 = 6;
            } else if (i8 == 180) {
                i9 = 3;
            } else if (i8 != 270) {
                p1.k("ExifData", "Unexpected orientation value: " + i8 + ". Must be one of 0, 90, 180, 270.");
                i9 = 0;
            } else {
                i9 = 8;
            }
            return c("Orientation", String.valueOf(i9));
        }

        public b n(WhiteBalanceMode whiteBalanceMode) {
            int i8 = a.f2606b[whiteBalanceMode.ordinal()];
            return c("WhiteBalance", i8 != 1 ? i8 != 2 ? null : String.valueOf(1) : String.valueOf(0));
        }
    }

    static {
        i[] iVarArr = {new i("ImageWidth", RecognitionOptions.QR_CODE, 3, 4), new i("ImageLength", 257, 3, 4), new i("Make", 271, 2), new i("Model", 272, 2), new i("Orientation", 274, 3), new i("XResolution", 282, 5), new i("YResolution", 283, 5), new i("ResolutionUnit", 296, 3), new i("Software", 305, 2), new i("DateTime", 306, 2), new i("YCbCrPositioning", 531, 3), new i("SubIFDPointer", 330, 4), new i("ExifIFDPointer", 34665, 4), new i("GPSInfoIFDPointer", 34853, 4)};
        f2593d = iVarArr;
        i[] iVarArr2 = {new i("ExposureTime", 33434, 5), new i("FNumber", 33437, 5), new i("ExposureProgram", 34850, 3), new i("PhotographicSensitivity", 34855, 3), new i("SensitivityType", 34864, 3), new i("ExifVersion", 36864, 2), new i("DateTimeOriginal", 36867, 2), new i("DateTimeDigitized", 36868, 2), new i("ComponentsConfiguration", 37121, 7), new i("ShutterSpeedValue", 37377, 10), new i("ApertureValue", 37378, 5), new i("BrightnessValue", 37379, 10), new i("ExposureBiasValue", 37380, 10), new i("MaxApertureValue", 37381, 5), new i("MeteringMode", 37383, 3), new i("LightSource", 37384, 3), new i("Flash", 37385, 3), new i("FocalLength", 37386, 5), new i("SubSecTime", 37520, 2), new i("SubSecTimeOriginal", 37521, 2), new i("SubSecTimeDigitized", 37522, 2), new i("FlashpixVersion", 40960, 7), new i("ColorSpace", 40961, 3), new i("PixelXDimension", 40962, 3, 4), new i("PixelYDimension", 40963, 3, 4), new i("InteroperabilityIFDPointer", 40965, 4), new i("FocalPlaneResolutionUnit", 41488, 3), new i("SensingMethod", 41495, 3), new i("FileSource", 41728, 7), new i("SceneType", 41729, 7), new i("CustomRendered", 41985, 3), new i("ExposureMode", 41986, 3), new i("WhiteBalance", 41987, 3), new i("SceneCaptureType", 41990, 3), new i("Contrast", 41992, 3), new i("Saturation", 41993, 3), new i("Sharpness", 41994, 3)};
        f2594e = iVarArr2;
        i[] iVarArr3 = {new i("GPSVersionID", 0, 1), new i("GPSLatitudeRef", 1, 2), new i("GPSLatitude", 2, 5, 10), new i("GPSLongitudeRef", 3, 2), new i("GPSLongitude", 4, 5, 10), new i("GPSAltitudeRef", 5, 1), new i("GPSAltitude", 6, 5), new i("GPSTimeStamp", 7, 5), new i("GPSSpeedRef", 12, 2), new i("GPSTrackRef", 14, 2), new i("GPSImgDirectionRef", 16, 2), new i("GPSDestBearingRef", 23, 2), new i("GPSDestDistanceRef", 25, 2)};
        f2595f = iVarArr3;
        f2596g = new i[]{new i("SubIFDPointer", 330, 4), new i("ExifIFDPointer", 34665, 4), new i("GPSInfoIFDPointer", 34853, 4), new i("InteroperabilityIFDPointer", 40965, 4)};
        i[] iVarArr4 = {new i("InteroperabilityIndex", 1, 2)};
        f2597h = iVarArr4;
        f2598i = new i[][]{iVarArr, iVarArr2, iVarArr3, iVarArr4};
        f2599j = new HashSet<>(Arrays.asList("FNumber", "ExposureTime", "GPSTimeStamp"));
    }

    ExifData(ByteOrder byteOrder, List<Map<String, g>> list) {
        androidx.core.util.h.k(list.size() == f2598i.length, "Malformed attributes list. Number of IFDs mismatch.");
        this.f2601b = byteOrder;
        this.f2600a = list;
    }

    public static b a() {
        return new b(ByteOrder.BIG_ENDIAN).c("Orientation", String.valueOf(1)).c("XResolution", "72/1").c("YResolution", "72/1").c("ResolutionUnit", String.valueOf(2)).c("YCbCrPositioning", String.valueOf(1)).c("Make", Build.MANUFACTURER).c("Model", Build.MODEL);
    }

    public static ExifData b(l1 l1Var, int i8) {
        b a9 = a();
        l1Var.e1().c(a9);
        a9.m(i8);
        return a9.j(l1Var.getWidth()).i(l1Var.getHeight()).a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<String, g> c(int i8) {
        int length = f2598i.length;
        androidx.core.util.h.d(i8, 0, length, "Invalid IFD index: " + i8 + ". Index should be between [0, EXIF_TAGS.length] ");
        return this.f2600a.get(i8);
    }

    public ByteOrder d() {
        return this.f2601b;
    }
}
