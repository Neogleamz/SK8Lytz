package u5;

import android.graphics.Color;
import android.graphics.PointF;
import android.text.TextUtils;
import b6.l0;
import b6.p;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.common.primitives.g;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: a  reason: collision with root package name */
    public final String f23038a;

    /* renamed from: b  reason: collision with root package name */
    public final int f23039b;

    /* renamed from: c  reason: collision with root package name */
    public final Integer f23040c;

    /* renamed from: d  reason: collision with root package name */
    public final Integer f23041d;

    /* renamed from: e  reason: collision with root package name */
    public final float f23042e;

    /* renamed from: f  reason: collision with root package name */
    public final boolean f23043f;

    /* renamed from: g  reason: collision with root package name */
    public final boolean f23044g;

    /* renamed from: h  reason: collision with root package name */
    public final boolean f23045h;

    /* renamed from: i  reason: collision with root package name */
    public final boolean f23046i;

    /* renamed from: j  reason: collision with root package name */
    public final int f23047j;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final int f23048a;

        /* renamed from: b  reason: collision with root package name */
        public final int f23049b;

        /* renamed from: c  reason: collision with root package name */
        public final int f23050c;

        /* renamed from: d  reason: collision with root package name */
        public final int f23051d;

        /* renamed from: e  reason: collision with root package name */
        public final int f23052e;

        /* renamed from: f  reason: collision with root package name */
        public final int f23053f;

        /* renamed from: g  reason: collision with root package name */
        public final int f23054g;

        /* renamed from: h  reason: collision with root package name */
        public final int f23055h;

        /* renamed from: i  reason: collision with root package name */
        public final int f23056i;

        /* renamed from: j  reason: collision with root package name */
        public final int f23057j;

        /* renamed from: k  reason: collision with root package name */
        public final int f23058k;

        private a(int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18) {
            this.f23048a = i8;
            this.f23049b = i9;
            this.f23050c = i10;
            this.f23051d = i11;
            this.f23052e = i12;
            this.f23053f = i13;
            this.f23054g = i14;
            this.f23055h = i15;
            this.f23056i = i16;
            this.f23057j = i17;
            this.f23058k = i18;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        public static a a(String str) {
            char c9;
            String[] split = TextUtils.split(str.substring(7), ",");
            int i8 = -1;
            int i9 = -1;
            int i10 = -1;
            int i11 = -1;
            int i12 = -1;
            int i13 = -1;
            int i14 = -1;
            int i15 = -1;
            int i16 = -1;
            int i17 = -1;
            for (int i18 = 0; i18 < split.length; i18++) {
                String e8 = com.google.common.base.c.e(split[i18].trim());
                e8.hashCode();
                switch (e8.hashCode()) {
                    case -1178781136:
                        if (e8.equals("italic")) {
                            c9 = 0;
                            break;
                        }
                        c9 = 65535;
                        break;
                    case -1026963764:
                        if (e8.equals("underline")) {
                            c9 = 1;
                            break;
                        }
                        c9 = 65535;
                        break;
                    case -192095652:
                        if (e8.equals("strikeout")) {
                            c9 = 2;
                            break;
                        }
                        c9 = 65535;
                        break;
                    case -70925746:
                        if (e8.equals("primarycolour")) {
                            c9 = 3;
                            break;
                        }
                        c9 = 65535;
                        break;
                    case 3029637:
                        if (e8.equals("bold")) {
                            c9 = 4;
                            break;
                        }
                        c9 = 65535;
                        break;
                    case 3373707:
                        if (e8.equals("name")) {
                            c9 = 5;
                            break;
                        }
                        c9 = 65535;
                        break;
                    case 366554320:
                        if (e8.equals("fontsize")) {
                            c9 = 6;
                            break;
                        }
                        c9 = 65535;
                        break;
                    case 767321349:
                        if (e8.equals("borderstyle")) {
                            c9 = 7;
                            break;
                        }
                        c9 = 65535;
                        break;
                    case 1767875043:
                        if (e8.equals("alignment")) {
                            c9 = '\b';
                            break;
                        }
                        c9 = 65535;
                        break;
                    case 1988365454:
                        if (e8.equals("outlinecolour")) {
                            c9 = '\t';
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
                        i14 = i18;
                        break;
                    case 1:
                        i15 = i18;
                        break;
                    case 2:
                        i16 = i18;
                        break;
                    case 3:
                        i10 = i18;
                        break;
                    case 4:
                        i13 = i18;
                        break;
                    case 5:
                        i8 = i18;
                        break;
                    case 6:
                        i12 = i18;
                        break;
                    case 7:
                        i17 = i18;
                        break;
                    case '\b':
                        i9 = i18;
                        break;
                    case '\t':
                        i11 = i18;
                        break;
                }
            }
            if (i8 != -1) {
                return new a(i8, i9, i10, i11, i12, i13, i14, i15, i16, i17, split.length);
            }
            return null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b {

        /* renamed from: c  reason: collision with root package name */
        private static final Pattern f23059c = Pattern.compile("\\{([^}]*)\\}");

        /* renamed from: d  reason: collision with root package name */
        private static final Pattern f23060d = Pattern.compile(l0.C("\\\\pos\\((%1$s),(%1$s)\\)", "\\s*\\d+(?:\\.\\d+)?\\s*"));

        /* renamed from: e  reason: collision with root package name */
        private static final Pattern f23061e = Pattern.compile(l0.C("\\\\move\\(%1$s,%1$s,(%1$s),(%1$s)(?:,%1$s,%1$s)?\\)", "\\s*\\d+(?:\\.\\d+)?\\s*"));

        /* renamed from: f  reason: collision with root package name */
        private static final Pattern f23062f = Pattern.compile("\\\\an(\\d+)");

        /* renamed from: a  reason: collision with root package name */
        public final int f23063a;

        /* renamed from: b  reason: collision with root package name */
        public final PointF f23064b;

        private b(int i8, PointF pointF) {
            this.f23063a = i8;
            this.f23064b = pointF;
        }

        private static int a(String str) {
            Matcher matcher = f23062f.matcher(str);
            if (matcher.find()) {
                return c.e((String) b6.a.e(matcher.group(1)));
            }
            return -1;
        }

        public static b b(String str) {
            Matcher matcher = f23059c.matcher(str);
            PointF pointF = null;
            int i8 = -1;
            while (matcher.find()) {
                String str2 = (String) b6.a.e(matcher.group(1));
                try {
                    PointF c9 = c(str2);
                    if (c9 != null) {
                        pointF = c9;
                    }
                } catch (RuntimeException unused) {
                }
                try {
                    int a9 = a(str2);
                    if (a9 != -1) {
                        i8 = a9;
                    }
                } catch (RuntimeException unused2) {
                }
            }
            return new b(i8, pointF);
        }

        private static PointF c(String str) {
            String group;
            String group2;
            Matcher matcher = f23060d.matcher(str);
            Matcher matcher2 = f23061e.matcher(str);
            boolean find = matcher.find();
            boolean find2 = matcher2.find();
            if (find) {
                if (find2) {
                    p.f("SsaStyle.Overrides", "Override has both \\pos(x,y) and \\move(x1,y1,x2,y2); using \\pos values. override='" + str + "'");
                }
                group = matcher.group(1);
                group2 = matcher.group(2);
            } else if (!find2) {
                return null;
            } else {
                group = matcher2.group(1);
                group2 = matcher2.group(2);
            }
            return new PointF(Float.parseFloat(((String) b6.a.e(group)).trim()), Float.parseFloat(((String) b6.a.e(group2)).trim()));
        }

        public static String d(String str) {
            return f23059c.matcher(str).replaceAll(BuildConfig.FLAVOR);
        }
    }

    private c(String str, int i8, Integer num, Integer num2, float f5, boolean z4, boolean z8, boolean z9, boolean z10, int i9) {
        this.f23038a = str;
        this.f23039b = i8;
        this.f23040c = num;
        this.f23041d = num2;
        this.f23042e = f5;
        this.f23043f = z4;
        this.f23044g = z8;
        this.f23045h = z9;
        this.f23046i = z10;
        this.f23047j = i9;
    }

    public static c b(String str, a aVar) {
        b6.a.a(str.startsWith("Style:"));
        String[] split = TextUtils.split(str.substring(6), ",");
        int length = split.length;
        int i8 = aVar.f23058k;
        if (length != i8) {
            p.i("SsaStyle", l0.C("Skipping malformed 'Style:' line (expected %s values, found %s): '%s'", Integer.valueOf(i8), Integer.valueOf(split.length), str));
            return null;
        }
        try {
            String trim = split[aVar.f23048a].trim();
            int i9 = aVar.f23049b;
            int e8 = i9 != -1 ? e(split[i9].trim()) : -1;
            int i10 = aVar.f23050c;
            Integer h8 = i10 != -1 ? h(split[i10].trim()) : null;
            int i11 = aVar.f23051d;
            Integer h9 = i11 != -1 ? h(split[i11].trim()) : null;
            int i12 = aVar.f23052e;
            float i13 = i12 != -1 ? i(split[i12].trim()) : -3.4028235E38f;
            int i14 = aVar.f23053f;
            boolean z4 = i14 != -1 && f(split[i14].trim());
            int i15 = aVar.f23054g;
            boolean z8 = i15 != -1 && f(split[i15].trim());
            int i16 = aVar.f23055h;
            boolean z9 = i16 != -1 && f(split[i16].trim());
            int i17 = aVar.f23056i;
            boolean z10 = i17 != -1 && f(split[i17].trim());
            int i18 = aVar.f23057j;
            return new c(trim, e8, h8, h9, i13, z4, z8, z9, z10, i18 != -1 ? g(split[i18].trim()) : -1);
        } catch (RuntimeException e9) {
            p.j("SsaStyle", "Skipping malformed 'Style:' line: '" + str + "'", e9);
            return null;
        }
    }

    private static boolean c(int i8) {
        switch (i8) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return true;
            default:
                return false;
        }
    }

    private static boolean d(int i8) {
        return i8 == 1 || i8 == 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int e(String str) {
        try {
            int parseInt = Integer.parseInt(str.trim());
            if (c(parseInt)) {
                return parseInt;
            }
        } catch (NumberFormatException unused) {
        }
        p.i("SsaStyle", "Ignoring unknown alignment: " + str);
        return -1;
    }

    private static boolean f(String str) {
        try {
            int parseInt = Integer.parseInt(str);
            return parseInt == 1 || parseInt == -1;
        } catch (NumberFormatException e8) {
            p.j("SsaStyle", "Failed to parse boolean value: '" + str + "'", e8);
            return false;
        }
    }

    private static int g(String str) {
        try {
            int parseInt = Integer.parseInt(str.trim());
            if (d(parseInt)) {
                return parseInt;
            }
        } catch (NumberFormatException unused) {
        }
        p.i("SsaStyle", "Ignoring unknown BorderStyle: " + str);
        return -1;
    }

    public static Integer h(String str) {
        try {
            long parseLong = str.startsWith("&H") ? Long.parseLong(str.substring(2), 16) : Long.parseLong(str);
            b6.a.a(parseLong <= 4294967295L);
            return Integer.valueOf(Color.argb(g.d(((parseLong >> 24) & 255) ^ 255), g.d(parseLong & 255), g.d((parseLong >> 8) & 255), g.d((parseLong >> 16) & 255)));
        } catch (IllegalArgumentException e8) {
            p.j("SsaStyle", "Failed to parse color expression: '" + str + "'", e8);
            return null;
        }
    }

    private static float i(String str) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e8) {
            p.j("SsaStyle", "Failed to parse font size: '" + str + "'", e8);
            return -3.4028235E38f;
        }
    }
}
