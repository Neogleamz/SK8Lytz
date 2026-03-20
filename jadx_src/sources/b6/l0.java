package b6;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import com.example.seedpoint.R;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.w0;
import com.google.android.exoplayer2.y1;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l0 {

    /* renamed from: a  reason: collision with root package name */
    public static final int f8063a;

    /* renamed from: b  reason: collision with root package name */
    public static final String f8064b;

    /* renamed from: c  reason: collision with root package name */
    public static final String f8065c;

    /* renamed from: d  reason: collision with root package name */
    public static final String f8066d;

    /* renamed from: e  reason: collision with root package name */
    public static final String f8067e;

    /* renamed from: f  reason: collision with root package name */
    public static final byte[] f8068f;

    /* renamed from: g  reason: collision with root package name */
    private static final Pattern f8069g;

    /* renamed from: h  reason: collision with root package name */
    private static final Pattern f8070h;

    /* renamed from: i  reason: collision with root package name */
    private static final Pattern f8071i;

    /* renamed from: j  reason: collision with root package name */
    private static final Pattern f8072j;

    /* renamed from: k  reason: collision with root package name */
    private static HashMap<String, String> f8073k;

    /* renamed from: l  reason: collision with root package name */
    private static final String[] f8074l;

    /* renamed from: m  reason: collision with root package name */
    private static final String[] f8075m;

    /* renamed from: n  reason: collision with root package name */
    private static final int[] f8076n;

    /* renamed from: o  reason: collision with root package name */
    private static final int[] f8077o;

    static {
        int i8 = Build.VERSION.SDK_INT;
        f8063a = i8;
        String str = Build.DEVICE;
        f8064b = str;
        String str2 = Build.MANUFACTURER;
        f8065c = str2;
        String str3 = Build.MODEL;
        f8066d = str3;
        f8067e = str + ", " + str3 + ", " + str2 + ", " + i8;
        f8068f = new byte[0];
        f8069g = Pattern.compile("(\\d\\d\\d\\d)\\-(\\d\\d)\\-(\\d\\d)[Tt](\\d\\d):(\\d\\d):(\\d\\d)([\\.,](\\d+))?([Zz]|((\\+|\\-)(\\d?\\d):?(\\d\\d)))?");
        f8070h = Pattern.compile("^(-)?P(([0-9]*)Y)?(([0-9]*)M)?(([0-9]*)D)?(T(([0-9]*)H)?(([0-9]*)M)?(([0-9.]*)S)?)?$");
        f8071i = Pattern.compile("%([A-Fa-f0-9]{2})");
        f8072j = Pattern.compile("(?:.*\\.)?isml?(?:/(manifest(.*))?)?", 2);
        f8074l = new String[]{"alb", "sq", "arm", "hy", "baq", "eu", "bur", "my", "tib", "bo", "chi", "zh", "cze", "cs", "dut", "nl", "ger", "de", "gre", "el", "fre", "fr", "geo", "ka", "ice", "is", "mac", "mk", "mao", "mi", "may", "ms", "per", "fa", "rum", "ro", "scc", "hbs-srp", "slo", "sk", "wel", "cy", "id", "ms-ind", "iw", "he", "heb", "he", "ji", "yi", "arb", "ar-arb", "in", "ms-ind", "ind", "ms-ind", "nb", "no-nob", "nob", "no-nob", "nn", "no-nno", "nno", "no-nno", "tw", "ak-twi", "twi", "ak-twi", "bs", "hbs-bos", "bos", "hbs-bos", "hr", "hbs-hrv", "hrv", "hbs-hrv", "sr", "hbs-srp", "srp", "hbs-srp", "cmn", "zh-cmn", "hak", "zh-hak", "nan", "zh-nan", "hsn", "zh-hsn"};
        f8075m = new String[]{"i-lux", "lb", "i-hak", "zh-hak", "i-navajo", "nv", "no-bok", "no-nob", "no-nyn", "no-nno", "zh-guoyu", "zh-cmn", "zh-hakka", "zh-hak", "zh-min-nan", "zh-nan", "zh-xiang", "zh-hsn"};
        f8076n = new int[]{0, 79764919, 159529838, 222504665, 319059676, 398814059, 445009330, 507990021, 638119352, 583659535, 797628118, 726387553, 890018660, 835552979, 1015980042, 944750013, 1276238704, 1221641927, 1167319070, 1095957929, 1595256236, 1540665371, 1452775106, 1381403509, 1780037320, 1859660671, 1671105958, 1733955601, 2031960084, 2111593891, 1889500026, 1952343757, -1742489888, -1662866601, -1851683442, -1788833735, -1960329156, -1880695413, -2103051438, -2040207643, -1104454824, -1159051537, -1213636554, -1284997759, -1389417084, -1444007885, -1532160278, -1603531939, -734892656, -789352409, -575645954, -646886583, -952755380, -1007220997, -827056094, -898286187, -231047128, -151282273, -71779514, -8804623, -515967244, -436212925, -390279782, -327299027, 881225847, 809987520, 1023691545, 969234094, 662832811, 591600412, 771767749, 717299826, 311336399, 374308984, 453813921, 533576470, 25881363, 88864420, 134795389, 214552010, 2023205639, 2086057648, 1897238633, 1976864222, 1804852699, 1867694188, 1645340341, 1724971778, 1587496639, 1516133128, 1461550545, 1406951526, 1302016099, 1230646740, 1142491917, 1087903418, -1398421865, -1469785312, -1524105735, -1578704818, -1079922613, -1151291908, -1239184603, -1293773166, -1968362705, -1905510760, -2094067647, -2014441994, -1716953613, -1654112188, -1876203875, -1796572374, -525066777, -462094256, -382327159, -302564546, -206542021, -143559028, -97365931, -17609246, -960696225, -1031934488, -817968335, -872425850, -709327229, -780559564, -600130067, -654598054, 1762451694, 1842216281, 1619975040, 1682949687, 2047383090, 2127137669, 1938468188, 2001449195, 1325665622, 1271206113, 1183200824, 1111960463, 1543535498, 1489069629, 1434599652, 1363369299, 622672798, 568075817, 748617968, 677256519, 907627842, 853037301, 1067152940, 995781531, 51762726, 131386257, 177728840, 240578815, 269590778, 349224269, 429104020, 491947555, -248556018, -168932423, -122852000, -60002089, -500490030, -420856475, -341238852, -278395381, -685261898, -739858943, -559578920, -630940305, -1004286614, -1058877219, -845023740, -916395085, -1119974018, -1174433591, -1262701040, -1333941337, -1371866206, -1426332139, -1481064244, -1552294533, -1690935098, -1611170447, -1833673816, -1770699233, -2009983462, -1930228819, -2119160460, -2056179517, 1569362073, 1498123566, 1409854455, 1355396672, 1317987909, 1246755826, 1192025387, 1137557660, 2072149281, 2135122070, 1912620623, 1992383480, 1753615357, 1816598090, 1627664531, 1707420964, 295390185, 358241886, 404320391, 483945776, 43990325, 106832002, 186451547, 266083308, 932423249, 861060070, 1041341759, 986742920, 613929101, 542559546, 756411363, 701822548, -978770311, -1050133554, -869589737, -924188512, -693284699, -764654318, -550540341, -605129092, -475935807, -413084042, -366743377, -287118056, -257573603, -194731862, -114850189, -35218492, -1984365303, -1921392450, -2143631769, -2063868976, -1698919467, -1635936670, -1824608069, -1744851700, -1347415887, -1418654458, -1506661409, -1561119128, -1129027987, -1200260134, -1254728445, -1309196108};
        f8077o = new int[]{0, 7, 14, 9, 28, 27, 18, 21, 56, 63, 54, 49, 36, 35, 42, 45, R.styleable.AppCompatTheme_toolbarNavigationButtonStyle, 119, 126, 121, 108, 107, 98, 101, 72, 79, 70, 65, 84, 83, 90, 93, 224, 231, 238, 233, 252, 251, 242, 245, 216, 223, 214, 209, 196, 195, 202, 205, 144, 151, 158, 153, 140, 139, 130, 133, 168, 175, 166, 161, 180, 179, 186, 189, 199, 192, 201, 206, 219, 220, 213, 210, 255, 248, 241, 246, 227, 228, 237, 234, 183, 176, 185, 190, 171, 172, 165, 162, 143, 136, 129, 134, 147, 148, 157, 154, 39, 32, 41, 46, 59, 60, 53, 50, 31, 24, 17, 22, 3, 4, 13, 10, 87, 80, 89, 94, 75, 76, 69, 66, R.styleable.AppCompatTheme_textColorSearchUrl, 104, 97, 102, R.styleable.AppCompatTheme_tooltipFrameBackground, 116, 125, 122, 137, 142, 135, RecognitionOptions.ITF, 149, 146, 155, 156, 177, 182, 191, 184, 173, 170, 163, 164, 249, 254, 247, 240, 229, 226, 235, 236, 193, 198, 207, 200, 221, 218, 211, 212, 105, R.styleable.AppCompatTheme_textColorAlertDialogListItem, 103, 96, 117, R.styleable.AppCompatTheme_tooltipForegroundColor, 123, 124, 81, 86, 95, 88, 77, 74, 67, 68, 25, 30, 23, 16, 5, 2, 11, 12, 33, 38, 47, 40, 61, 58, 51, 52, 78, 73, 64, 71, 82, 85, 92, 91, 118, R.styleable.AppCompatTheme_toolbarStyle, 120, 127, 106, R.styleable.AppCompatTheme_textAppearanceSmallPopupMenu, 100, 99, 62, 57, 48, 55, 34, 37, 44, 43, 6, 1, 8, 15, 26, 29, 20, 19, 174, 169, 160, 167, 178, 181, 188, 187, 150, 145, 152, 159, 138, 141, 132, 131, 222, 217, 208, 215, 194, 197, 204, 203, 230, 225, 232, 239, 250, 253, 244, 243};
    }

    private static HashMap<String, String> A() {
        String[] iSOLanguages = Locale.getISOLanguages();
        HashMap<String, String> hashMap = new HashMap<>(iSOLanguages.length + f8074l.length);
        int i8 = 0;
        for (String str : iSOLanguages) {
            try {
                String iSO3Language = new Locale(str).getISO3Language();
                if (!TextUtils.isEmpty(iSO3Language)) {
                    hashMap.put(iSO3Language, str);
                }
            } catch (MissingResourceException unused) {
            }
        }
        while (true) {
            String[] strArr = f8074l;
            if (i8 >= strArr.length) {
                return hashMap;
            }
            hashMap.put(strArr[i8], strArr[i8 + 1]);
            i8 += 2;
        }
    }

    private static String A0(String str) {
        int i8 = 0;
        while (true) {
            String[] strArr = f8075m;
            if (i8 >= strArr.length) {
                return str;
            }
            if (str.startsWith(strArr[i8])) {
                return strArr[i8 + 1] + str.substring(strArr[i8].length());
            }
            i8 += 2;
        }
    }

    public static Uri B(Uri uri) {
        String path = uri.getPath();
        if (path == null) {
            return uri;
        }
        Matcher matcher = f8072j.matcher(path);
        return (matcher.matches() && matcher.group(1) == null) ? Uri.withAppendedPath(uri, "Manifest") : uri;
    }

    public static <T> void B0(List<T> list, int i8, int i9, int i10) {
        ArrayDeque arrayDeque = new ArrayDeque();
        for (int i11 = (i9 - i8) - 1; i11 >= 0; i11--) {
            arrayDeque.addFirst(list.remove(i8 + i11));
        }
        list.addAll(Math.min(i10, list.size()), arrayDeque);
    }

    public static String C(String str, Object... objArr) {
        return String.format(Locale.US, str, objArr);
    }

    public static long C0(long j8) {
        return (j8 == -9223372036854775807L || j8 == Long.MIN_VALUE) ? j8 : j8 * 1000;
    }

    public static String D(byte[] bArr) {
        return new String(bArr, com.google.common.base.e.f18817c);
    }

    public static ExecutorService D0(final String str) {
        return Executors.newSingleThreadExecutor(new ThreadFactory() { // from class: b6.k0
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                Thread y02;
                y02 = l0.y0(str, runnable);
                return y02;
            }
        });
    }

    public static String E(byte[] bArr, int i8, int i9) {
        return new String(bArr, i8, i9, com.google.common.base.e.f18817c);
    }

    public static String E0(String str) {
        if (str == null) {
            return null;
        }
        String replace = str.replace('_', '-');
        if (!replace.isEmpty() && !replace.equals("und")) {
            str = replace;
        }
        String e8 = com.google.common.base.c.e(str);
        String str2 = S0(e8, "-")[0];
        if (f8073k == null) {
            f8073k = A();
        }
        String str3 = f8073k.get(str2);
        if (str3 != null) {
            e8 = str3 + e8.substring(str2.length());
            str2 = str3;
        }
        return ("no".equals(str2) || "i".equals(str2) || "zh".equals(str2)) ? A0(e8) : e8;
    }

    public static int F(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        if (audioManager == null) {
            return -1;
        }
        return audioManager.generateAudioSessionId();
    }

    public static <T> T[] F0(T[] tArr, T t8) {
        Object[] copyOf = Arrays.copyOf(tArr, tArr.length + 1);
        copyOf[tArr.length] = t8;
        return (T[]) k(copyOf);
    }

    @SuppressLint({"InlinedApi"})
    public static int G(int i8) {
        if (i8 != 12) {
            switch (i8) {
                case 1:
                    return 4;
                case 2:
                    return 12;
                case 3:
                    return 28;
                case 4:
                    return 204;
                case 5:
                    return 220;
                case 6:
                    return 252;
                case 7:
                    return 1276;
                case 8:
                    return 6396;
                default:
                    return 0;
            }
        }
        return 743676;
    }

    public static <T> T[] G0(T[] tArr, T[] tArr2) {
        T[] tArr3 = (T[]) Arrays.copyOf(tArr, tArr.length + tArr2.length);
        System.arraycopy(tArr2, 0, tArr3, tArr.length, tArr2.length);
        return tArr3;
    }

    public static y1.b H(y1 y1Var, y1.b bVar) {
        boolean h8 = y1Var.h();
        boolean x8 = y1Var.x();
        boolean p8 = y1Var.p();
        boolean D = y1Var.D();
        boolean O = y1Var.O();
        boolean H = y1Var.H();
        boolean u8 = y1Var.K().u();
        boolean z4 = false;
        y1.b.a d8 = new y1.b.a().b(bVar).d(4, !h8).d(5, x8 && !h8).d(6, p8 && !h8).d(7, !u8 && (p8 || !O || x8) && !h8).d(8, D && !h8).d(9, !u8 && (D || (O && H)) && !h8).d(10, !h8).d(11, x8 && !h8);
        if (x8 && !h8) {
            z4 = true;
        }
        return d8.d(12, z4).e();
    }

    public static <T> T[] H0(T[] tArr, int i8) {
        a.a(i8 <= tArr.length);
        return (T[]) Arrays.copyOf(tArr, i8);
    }

    public static int I(ByteBuffer byteBuffer, int i8) {
        int i9 = byteBuffer.getInt(i8);
        return byteBuffer.order() == ByteOrder.BIG_ENDIAN ? i9 : Integer.reverseBytes(i9);
    }

    public static <T> T[] I0(T[] tArr, int i8, int i9) {
        a.a(i8 >= 0);
        a.a(i9 <= tArr.length);
        return (T[]) Arrays.copyOfRange(tArr, i8, i9);
    }

    public static byte[] J(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        for (int i8 = 0; i8 < length; i8++) {
            int i9 = i8 * 2;
            bArr[i8] = (byte) ((Character.digit(str.charAt(i9), 16) << 4) + Character.digit(str.charAt(i9 + 1), 16));
        }
        return bArr;
    }

    public static long J0(String str) {
        Matcher matcher = f8069g.matcher(str);
        if (!matcher.matches()) {
            throw ParserException.a("Invalid date/time format: " + str, null);
        }
        int i8 = 0;
        if (matcher.group(9) != null && !matcher.group(9).equalsIgnoreCase("Z")) {
            i8 = (Integer.parseInt(matcher.group(12)) * 60) + Integer.parseInt(matcher.group(13));
            if ("-".equals(matcher.group(11))) {
                i8 *= -1;
            }
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        gregorianCalendar.clear();
        gregorianCalendar.set(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) - 1, Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)));
        if (!TextUtils.isEmpty(matcher.group(8))) {
            gregorianCalendar.set(14, new BigDecimal("0." + matcher.group(8)).movePointRight(3).intValue());
        }
        long timeInMillis = gregorianCalendar.getTimeInMillis();
        return i8 != 0 ? timeInMillis - (i8 * 60000) : timeInMillis;
    }

    public static int K(String str, int i8) {
        int i9 = 0;
        for (String str2 : T0(str)) {
            if (i8 == t.m(str2)) {
                i9++;
            }
        }
        return i9;
    }

    public static long K0(String str) {
        Matcher matcher = f8070h.matcher(str);
        if (matcher.matches()) {
            boolean isEmpty = true ^ TextUtils.isEmpty(matcher.group(1));
            String group = matcher.group(3);
            double parseDouble = group != null ? Double.parseDouble(group) * 3.1556908E7d : 0.0d;
            String group2 = matcher.group(5);
            double parseDouble2 = parseDouble + (group2 != null ? Double.parseDouble(group2) * 2629739.0d : 0.0d);
            String group3 = matcher.group(7);
            double parseDouble3 = parseDouble2 + (group3 != null ? Double.parseDouble(group3) * 86400.0d : 0.0d);
            String group4 = matcher.group(10);
            double parseDouble4 = parseDouble3 + (group4 != null ? Double.parseDouble(group4) * 3600.0d : 0.0d);
            String group5 = matcher.group(12);
            double parseDouble5 = parseDouble4 + (group5 != null ? Double.parseDouble(group5) * 60.0d : 0.0d);
            String group6 = matcher.group(14);
            long parseDouble6 = (long) ((parseDouble5 + (group6 != null ? Double.parseDouble(group6) : 0.0d)) * 1000.0d);
            return isEmpty ? -parseDouble6 : parseDouble6;
        }
        return (long) (Double.parseDouble(str) * 3600.0d * 1000.0d);
    }

    public static String L(String str, int i8) {
        String[] T0 = T0(str);
        if (T0.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String str2 : T0) {
            if (i8 == t.m(str2)) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(str2);
            }
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }

    public static boolean L0(Handler handler, Runnable runnable) {
        if (handler.getLooper().getThread().isAlive()) {
            if (handler.getLooper() == Looper.myLooper()) {
                runnable.run();
                return true;
            }
            return handler.post(runnable);
        }
        return false;
    }

    public static String M(Object[] objArr) {
        StringBuilder sb = new StringBuilder();
        for (int i8 = 0; i8 < objArr.length; i8++) {
            sb.append(objArr[i8].getClass().getSimpleName());
            if (i8 < objArr.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static boolean M0(Parcel parcel) {
        return parcel.readInt() != 0;
    }

    public static String N(Context context) {
        TelephonyManager telephonyManager;
        if (context != null && (telephonyManager = (TelephonyManager) context.getSystemService("phone")) != null) {
            String networkCountryIso = telephonyManager.getNetworkCountryIso();
            if (!TextUtils.isEmpty(networkCountryIso)) {
                return com.google.common.base.c.f(networkCountryIso);
            }
        }
        return com.google.common.base.c.f(Locale.getDefault().getCountry());
    }

    public static <T> void N0(List<T> list, int i8, int i9) {
        if (i8 < 0 || i9 > list.size() || i8 > i9) {
            throw new IllegalArgumentException();
        }
        if (i8 != i9) {
            list.subList(i8, i9).clear();
        }
    }

    public static Point O(Context context) {
        DisplayManager displayManager;
        Display display = (f8063a < 17 || (displayManager = (DisplayManager) context.getSystemService("display")) == null) ? null : displayManager.getDisplay(0);
        if (display == null) {
            display = ((WindowManager) a.e((WindowManager) context.getSystemService("window"))).getDefaultDisplay();
        }
        return P(context, display);
    }

    public static long O0(long j8, long j9, long j10) {
        int i8 = (j10 > j9 ? 1 : (j10 == j9 ? 0 : -1));
        if (i8 < 0 || j10 % j9 != 0) {
            if (i8 >= 0 || j9 % j10 != 0) {
                return (long) (j8 * (j9 / j10));
            }
            return j8 * (j9 / j10);
        }
        return j8 / (j10 / j9);
    }

    public static Point P(Context context, Display display) {
        if (display.getDisplayId() == 0 && x0(context)) {
            String j02 = j0(f8063a < 28 ? "sys.display-size" : "vendor.display-size");
            if (!TextUtils.isEmpty(j02)) {
                try {
                    String[] R0 = R0(j02.trim(), "x");
                    if (R0.length == 2) {
                        int parseInt = Integer.parseInt(R0[0]);
                        int parseInt2 = Integer.parseInt(R0[1]);
                        if (parseInt > 0 && parseInt2 > 0) {
                            return new Point(parseInt, parseInt2);
                        }
                    }
                } catch (NumberFormatException unused) {
                }
                p.c("Util", "Invalid display size: " + j02);
            }
            if ("Sony".equals(f8065c) && f8066d.startsWith("BRAVIA") && context.getPackageManager().hasSystemFeature("com.sony.dtv.hardware.panel.qfhd")) {
                return new Point(3840, 2160);
            }
        }
        Point point = new Point();
        int i8 = f8063a;
        if (i8 >= 23) {
            T(display, point);
        } else if (i8 >= 17) {
            S(display, point);
        } else {
            R(display, point);
        }
        return point;
    }

    public static long[] P0(List<Long> list, long j8, long j9) {
        int size = list.size();
        long[] jArr = new long[size];
        int i8 = (j9 > j8 ? 1 : (j9 == j8 ? 0 : -1));
        int i9 = 0;
        if (i8 >= 0 && j9 % j8 == 0) {
            long j10 = j9 / j8;
            while (i9 < size) {
                jArr[i9] = list.get(i9).longValue() / j10;
                i9++;
            }
        } else if (i8 >= 0 || j8 % j9 != 0) {
            double d8 = j8 / j9;
            while (i9 < size) {
                jArr[i9] = (long) (list.get(i9).longValue() * d8);
                i9++;
            }
        } else {
            long j11 = j8 / j9;
            while (i9 < size) {
                jArr[i9] = list.get(i9).longValue() * j11;
                i9++;
            }
        }
        return jArr;
    }

    public static Looper Q() {
        Looper myLooper = Looper.myLooper();
        return myLooper != null ? myLooper : Looper.getMainLooper();
    }

    public static void Q0(long[] jArr, long j8, long j9) {
        int i8 = (j9 > j8 ? 1 : (j9 == j8 ? 0 : -1));
        int i9 = 0;
        if (i8 >= 0 && j9 % j8 == 0) {
            long j10 = j9 / j8;
            while (i9 < jArr.length) {
                jArr[i9] = jArr[i9] / j10;
                i9++;
            }
        } else if (i8 >= 0 || j8 % j9 != 0) {
            double d8 = j8 / j9;
            while (i9 < jArr.length) {
                jArr[i9] = (long) (jArr[i9] * d8);
                i9++;
            }
        } else {
            long j11 = j8 / j9;
            while (i9 < jArr.length) {
                jArr[i9] = jArr[i9] * j11;
                i9++;
            }
        }
    }

    private static void R(Display display, Point point) {
        display.getSize(point);
    }

    public static String[] R0(String str, String str2) {
        return str.split(str2, -1);
    }

    private static void S(Display display, Point point) {
        display.getRealSize(point);
    }

    public static String[] S0(String str, String str2) {
        return str.split(str2, 2);
    }

    private static void T(Display display, Point point) {
        Display.Mode mode = display.getMode();
        point.x = mode.getPhysicalWidth();
        point.y = mode.getPhysicalHeight();
    }

    public static String[] T0(String str) {
        return TextUtils.isEmpty(str) ? new String[0] : R0(str.trim(), "(\\s*,\\s*)");
    }

    public static int U(int i8) {
        if (i8 == 2 || i8 == 4) {
            return 6005;
        }
        if (i8 != 10) {
            if (i8 != 7) {
                if (i8 != 8) {
                    switch (i8) {
                        case 15:
                            return 6003;
                        case 16:
                        case 18:
                            return 6005;
                        case 17:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                            return 6004;
                        default:
                            switch (i8) {
                                case 24:
                                case 25:
                                case 26:
                                case 27:
                                case 28:
                                    return 6002;
                                default:
                                    return 6006;
                            }
                    }
                }
                return 6003;
            }
            return 6005;
        }
        return 6004;
    }

    public static ComponentName U0(Context context, Intent intent) {
        return f8063a >= 26 ? context.startForegroundService(intent) : context.startService(intent);
    }

    public static int V(String str) {
        String[] R0;
        int length;
        if (str != null && (length = (R0 = R0(str, "_")).length) >= 2) {
            String str2 = R0[length - 1];
            boolean z4 = length >= 3 && "neg".equals(R0[length - 2]);
            try {
                int parseInt = Integer.parseInt((String) a.e(str2));
                return z4 ? -parseInt : parseInt;
            } catch (NumberFormatException unused) {
                return 0;
            }
        }
        return 0;
    }

    public static long V0(long j8, long j9, long j10) {
        long j11 = j8 - j9;
        return ((j8 ^ j11) & (j9 ^ j8)) < 0 ? j10 : j11;
    }

    public static String W(int i8) {
        if (i8 != 0) {
            if (i8 != 1) {
                if (i8 != 2) {
                    if (i8 != 3) {
                        if (i8 == 4) {
                            return "YES";
                        }
                        throw new IllegalStateException();
                    }
                    return "NO_EXCEEDS_CAPABILITIES";
                }
                return "NO_UNSUPPORTED_DRM";
            }
            return "NO_UNSUPPORTED_TYPE";
        }
        return "NO";
    }

    public static byte[] W0(InputStream inputStream) {
        byte[] bArr = new byte[RecognitionOptions.AZTEC];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public static String X(Locale locale) {
        return f8063a >= 21 ? Y(locale) : locale.toString();
    }

    public static long X0(int i8, int i9) {
        return Y0(i9) | (Y0(i8) << 32);
    }

    private static String Y(Locale locale) {
        return locale.toLanguageTag();
    }

    public static long Y0(int i8) {
        return i8 & 4294967295L;
    }

    public static long Z(long j8, float f5) {
        return f5 == 1.0f ? j8 : Math.round(j8 * f5);
    }

    public static CharSequence Z0(CharSequence charSequence, int i8) {
        return charSequence.length() <= i8 ? charSequence : charSequence.subSequence(0, i8);
    }

    public static long a0(long j8) {
        return j8 == -9223372036854775807L ? System.currentTimeMillis() : j8 + SystemClock.elapsedRealtime();
    }

    public static long a1(long j8) {
        return (j8 == -9223372036854775807L || j8 == Long.MIN_VALUE) ? j8 : j8 / 1000;
    }

    public static long b(long j8, long j9, long j10) {
        long j11 = j8 + j9;
        return ((j8 ^ j11) & (j9 ^ j11)) < 0 ? j10 : j11;
    }

    public static int b0(int i8) {
        if (i8 != 8) {
            if (i8 != 16) {
                if (i8 != 24) {
                    return i8 != 32 ? 0 : 805306368;
                }
                return 536870912;
            }
            return 2;
        }
        return 3;
    }

    public static void b1(Parcel parcel, boolean z4) {
        parcel.writeInt(z4 ? 1 : 0);
    }

    public static boolean c(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    public static w0 c0(int i8, int i9, int i10) {
        return new w0.b().g0("audio/raw").J(i9).h0(i10).a0(i8).G();
    }

    public static <T extends Comparable<? super T>> int d(List<? extends Comparable<? super T>> list, T t8, boolean z4, boolean z8) {
        int i8;
        int binarySearch = Collections.binarySearch(list, t8);
        if (binarySearch < 0) {
            i8 = ~binarySearch;
        } else {
            int size = list.size();
            do {
                binarySearch++;
                if (binarySearch >= size) {
                    break;
                }
            } while (list.get(binarySearch).compareTo(t8) == 0);
            i8 = z4 ? binarySearch - 1 : binarySearch;
        }
        return z8 ? Math.min(list.size() - 1, i8) : i8;
    }

    public static int d0(int i8, int i9) {
        if (i8 != 2) {
            if (i8 != 3) {
                if (i8 != 4) {
                    if (i8 != 268435456) {
                        if (i8 == 536870912) {
                            return i9 * 3;
                        }
                        if (i8 != 805306368) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
                return i9 * 4;
            }
            return i9;
        }
        return i9 * 2;
    }

    public static int e(long[] jArr, long j8, boolean z4, boolean z8) {
        int i8;
        int binarySearch = Arrays.binarySearch(jArr, j8);
        if (binarySearch < 0) {
            i8 = ~binarySearch;
        } else {
            do {
                binarySearch++;
                if (binarySearch >= jArr.length) {
                    break;
                }
            } while (jArr[binarySearch] == j8);
            i8 = z4 ? binarySearch - 1 : binarySearch;
        }
        return z8 ? Math.min(jArr.length - 1, i8) : i8;
    }

    public static long e0(long j8, float f5) {
        return f5 == 1.0f ? j8 : Math.round(j8 / f5);
    }

    public static int f(q qVar, long j8, boolean z4, boolean z8) {
        int i8;
        int c9 = qVar.c() - 1;
        int i9 = 0;
        while (i9 <= c9) {
            int i10 = (i9 + c9) >>> 1;
            if (qVar.b(i10) < j8) {
                i9 = i10 + 1;
            } else {
                c9 = i10 - 1;
            }
        }
        if (z4 && (i8 = c9 + 1) < qVar.c() && qVar.b(i8) == j8) {
            return i8;
        }
        if (z8 && c9 == -1) {
            return 0;
        }
        return c9;
    }

    public static int f0(int i8) {
        if (i8 != 13) {
            switch (i8) {
                case 2:
                    return 0;
                case 3:
                    return 8;
                case 4:
                    return 4;
                case 5:
                case 7:
                case 8:
                case 9:
                case 10:
                    return 5;
                case 6:
                    return 2;
                default:
                    return 3;
            }
        }
        return 1;
    }

    public static <T extends Comparable<? super T>> int g(List<? extends Comparable<? super T>> list, T t8, boolean z4, boolean z8) {
        int i8;
        int binarySearch = Collections.binarySearch(list, t8);
        if (binarySearch < 0) {
            i8 = -(binarySearch + 2);
        } else {
            do {
                binarySearch--;
                if (binarySearch < 0) {
                    break;
                }
            } while (list.get(binarySearch).compareTo(t8) == 0);
            i8 = z4 ? binarySearch + 1 : binarySearch;
        }
        return z8 ? Math.max(0, i8) : i8;
    }

    public static String[] g0() {
        String[] h02 = h0();
        for (int i8 = 0; i8 < h02.length; i8++) {
            h02[i8] = E0(h02[i8]);
        }
        return h02;
    }

    public static int h(int[] iArr, int i8, boolean z4, boolean z8) {
        int i9;
        int binarySearch = Arrays.binarySearch(iArr, i8);
        if (binarySearch < 0) {
            i9 = -(binarySearch + 2);
        } else {
            do {
                binarySearch--;
                if (binarySearch < 0) {
                    break;
                }
            } while (iArr[binarySearch] == i8);
            i9 = z4 ? binarySearch + 1 : binarySearch;
        }
        return z8 ? Math.max(0, i9) : i9;
    }

    private static String[] h0() {
        Configuration configuration = Resources.getSystem().getConfiguration();
        return f8063a >= 24 ? i0(configuration) : new String[]{X(configuration.locale)};
    }

    public static int i(long[] jArr, long j8, boolean z4, boolean z8) {
        int i8;
        int binarySearch = Arrays.binarySearch(jArr, j8);
        if (binarySearch < 0) {
            i8 = -(binarySearch + 2);
        } else {
            do {
                binarySearch--;
                if (binarySearch < 0) {
                    break;
                }
            } while (jArr[binarySearch] == j8);
            i8 = z4 ? binarySearch + 1 : binarySearch;
        }
        return z8 ? Math.max(0, i8) : i8;
    }

    private static String[] i0(Configuration configuration) {
        return R0(configuration.getLocales().toLanguageTags(), ",");
    }

    public static <T> T j(T t8) {
        return t8;
    }

    private static String j0(String str) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", String.class).invoke(cls, str);
        } catch (Exception e8) {
            p.d("Util", "Failed to read system property " + str, e8);
            return null;
        }
    }

    public static <T> T[] k(T[] tArr) {
        return tArr;
    }

    public static String k0(int i8) {
        switch (i8) {
            case -2:
                return "none";
            case -1:
                return "unknown";
            case 0:
                return "default";
            case 1:
                return "audio";
            case 2:
                return "video";
            case 3:
                return "text";
            case 4:
                return "image";
            case 5:
                return "metadata";
            case 6:
                return "camera motion";
            default:
                if (i8 >= 10000) {
                    return "custom (" + i8 + ")";
                }
                return "?";
        }
    }

    public static int l(int i8, int i9) {
        return ((i8 + i9) - 1) / i9;
    }

    public static String l0(Context context, String str) {
        String str2;
        try {
            str2 = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException unused) {
            str2 = "?";
        }
        return str + "/" + str2 + " (Linux;Android " + Build.VERSION.RELEASE + ") ExoPlayerLib/2.18.7";
    }

    public static long m(long j8, long j9) {
        return ((j8 + j9) - 1) / j9;
    }

    public static byte[] m0(String str) {
        return str.getBytes(com.google.common.base.e.f18817c);
    }

    public static void n(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }

    public static int n0(Uri uri) {
        int o02;
        String scheme = uri.getScheme();
        if (scheme == null || !com.google.common.base.c.a("rtsp", scheme)) {
            String lastPathSegment = uri.getLastPathSegment();
            if (lastPathSegment == null) {
                return 4;
            }
            int lastIndexOf = lastPathSegment.lastIndexOf(46);
            if (lastIndexOf < 0 || (o02 = o0(lastPathSegment.substring(lastIndexOf + 1))) == 4) {
                Matcher matcher = f8072j.matcher((CharSequence) a.e(uri.getPath()));
                if (matcher.matches()) {
                    String group = matcher.group(2);
                    if (group != null) {
                        if (group.contains("format=mpd-time-csf")) {
                            return 0;
                        }
                        if (group.contains("format=m3u8-aapl")) {
                            return 2;
                        }
                    }
                    return 1;
                }
                return 4;
            }
            return o02;
        }
        return 3;
    }

    public static int o(long j8, long j9) {
        int i8 = (j8 > j9 ? 1 : (j8 == j9 ? 0 : -1));
        if (i8 < 0) {
            return -1;
        }
        return i8 == 0 ? 0 : 1;
    }

    public static int o0(String str) {
        String e8 = com.google.common.base.c.e(str);
        e8.hashCode();
        char c9 = 65535;
        switch (e8.hashCode()) {
            case 104579:
                if (e8.equals("ism")) {
                    c9 = 0;
                    break;
                }
                break;
            case 108321:
                if (e8.equals("mpd")) {
                    c9 = 1;
                    break;
                }
                break;
            case 3242057:
                if (e8.equals("isml")) {
                    c9 = 2;
                    break;
                }
                break;
            case 3299913:
                if (e8.equals("m3u8")) {
                    c9 = 3;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
            case 2:
                return 1;
            case 1:
                return 0;
            case 3:
                return 2;
            default:
                return 4;
        }
    }

    public static float p(float f5, float f8, float f9) {
        return Math.max(f8, Math.min(f5, f9));
    }

    public static int p0(Uri uri, String str) {
        if (str == null) {
            return n0(uri);
        }
        char c9 = 65535;
        switch (str.hashCode()) {
            case -979127466:
                if (str.equals("application/x-mpegURL")) {
                    c9 = 0;
                    break;
                }
                break;
            case -156749520:
                if (str.equals("application/vnd.ms-sstr+xml")) {
                    c9 = 1;
                    break;
                }
                break;
            case 64194685:
                if (str.equals("application/dash+xml")) {
                    c9 = 2;
                    break;
                }
                break;
            case 1154777587:
                if (str.equals("application/x-rtsp")) {
                    c9 = 3;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return 2;
            case 1:
                return 1;
            case 2:
                return 0;
            case 3:
                return 3;
            default:
                return 4;
        }
    }

    public static int q(int i8, int i9, int i10) {
        return Math.max(i9, Math.min(i8, i10));
    }

    public static boolean q0(z zVar, z zVar2, Inflater inflater) {
        if (zVar.a() <= 0) {
            return false;
        }
        if (zVar2.b() < zVar.a()) {
            zVar2.c(zVar.a() * 2);
        }
        if (inflater == null) {
            inflater = new Inflater();
        }
        inflater.setInput(zVar.e(), zVar.f(), zVar.a());
        int i8 = 0;
        while (true) {
            try {
                i8 += inflater.inflate(zVar2.e(), i8, zVar2.b() - i8);
                if (!inflater.finished()) {
                    if (inflater.needsDictionary() || inflater.needsInput()) {
                        break;
                    } else if (i8 == zVar2.b()) {
                        zVar2.c(zVar2.b() * 2);
                    }
                } else {
                    zVar2.T(i8);
                    return true;
                }
            } catch (DataFormatException unused) {
                return false;
            } finally {
                inflater.reset();
            }
        }
        return false;
    }

    public static long r(long j8, long j9, long j10) {
        return Math.max(j9, Math.min(j8, j10));
    }

    public static String r0(int i8) {
        return Integer.toString(i8, 36);
    }

    public static boolean s(Object[] objArr, Object obj) {
        for (Object obj2 : objArr) {
            if (c(obj2, obj)) {
                return true;
            }
        }
        return false;
    }

    public static boolean s0(Context context) {
        return f8063a >= 23 && context.getPackageManager().hasSystemFeature("android.hardware.type.automotive");
    }

    public static int t(byte[] bArr, int i8, int i9, int i10) {
        while (i8 < i9) {
            i10 = f8076n[((i10 >>> 24) ^ (bArr[i8] & 255)) & 255] ^ (i10 << 8);
            i8++;
        }
        return i10;
    }

    public static boolean t0(int i8) {
        return i8 == 536870912 || i8 == 805306368 || i8 == 4;
    }

    public static int u(byte[] bArr, int i8, int i9, int i10) {
        while (i8 < i9) {
            i10 = f8077o[i10 ^ (bArr[i8] & 255)];
            i8++;
        }
        return i10;
    }

    public static boolean u0(int i8) {
        return i8 == 3 || i8 == 2 || i8 == 268435456 || i8 == 536870912 || i8 == 805306368 || i8 == 4;
    }

    public static Handler v(Looper looper, Handler.Callback callback) {
        return new Handler(looper, callback);
    }

    public static boolean v0(int i8) {
        return i8 == 10 || i8 == 13;
    }

    public static Handler w() {
        return x(null);
    }

    public static boolean w0(Uri uri) {
        String scheme = uri.getScheme();
        return TextUtils.isEmpty(scheme) || "file".equals(scheme);
    }

    public static Handler x(Handler.Callback callback) {
        return v((Looper) a.h(Looper.myLooper()), callback);
    }

    public static boolean x0(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getApplicationContext().getSystemService("uimode");
        return uiModeManager != null && uiModeManager.getCurrentModeType() == 4;
    }

    public static Handler y() {
        return z(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Thread y0(String str, Runnable runnable) {
        return new Thread(runnable, str);
    }

    public static Handler z(Handler.Callback callback) {
        return v(Q(), callback);
    }

    public static int z0(int[] iArr, int i8) {
        for (int i9 = 0; i9 < iArr.length; i9++) {
            if (iArr[i9] == i8) {
                return i9;
            }
        }
        return -1;
    }
}
