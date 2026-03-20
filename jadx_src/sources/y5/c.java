package y5;

import android.text.TextUtils;
import b6.l0;
import b6.p;
import b6.z;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c {

    /* renamed from: c  reason: collision with root package name */
    private static final Pattern f24424c = Pattern.compile("\\[voice=\"([^\"]*)\"\\]");

    /* renamed from: d  reason: collision with root package name */
    private static final Pattern f24425d = Pattern.compile("^((?:[0-9]*\\.)?[0-9]+)(px|em|%)$");

    /* renamed from: a  reason: collision with root package name */
    private final z f24426a = new z();

    /* renamed from: b  reason: collision with root package name */
    private final StringBuilder f24427b = new StringBuilder();

    private void a(d dVar, String str) {
        if (BuildConfig.FLAVOR.equals(str)) {
            return;
        }
        int indexOf = str.indexOf(91);
        if (indexOf != -1) {
            Matcher matcher = f24424c.matcher(str.substring(indexOf));
            if (matcher.matches()) {
                dVar.z((String) b6.a.e(matcher.group(1)));
            }
            str = str.substring(0, indexOf);
        }
        String[] R0 = l0.R0(str, "\\.");
        String str2 = R0[0];
        int indexOf2 = str2.indexOf(35);
        if (indexOf2 != -1) {
            dVar.y(str2.substring(0, indexOf2));
            dVar.x(str2.substring(indexOf2 + 1));
        } else {
            dVar.y(str2);
        }
        if (R0.length > 1) {
            dVar.w((String[]) l0.I0(R0, 1, R0.length));
        }
    }

    private static boolean b(z zVar) {
        int f5 = zVar.f();
        int g8 = zVar.g();
        byte[] e8 = zVar.e();
        if (f5 + 2 > g8) {
            return false;
        }
        int i8 = f5 + 1;
        if (e8[f5] != 47) {
            return false;
        }
        int i9 = i8 + 1;
        if (e8[i8] != 42) {
            return false;
        }
        while (true) {
            int i10 = i9 + 1;
            if (i10 >= g8) {
                zVar.V(g8 - zVar.f());
                return true;
            } else if (((char) e8[i9]) == '*' && ((char) e8[i10]) == '/') {
                i9 = i10 + 1;
                g8 = i9;
            } else {
                i9 = i10;
            }
        }
    }

    private static boolean c(z zVar) {
        char k8 = k(zVar, zVar.f());
        if (k8 == '\t' || k8 == '\n' || k8 == '\f' || k8 == '\r' || k8 == ' ') {
            zVar.V(1);
            return true;
        }
        return false;
    }

    private static void e(String str, d dVar) {
        Matcher matcher = f24425d.matcher(com.google.common.base.c.e(str));
        if (!matcher.matches()) {
            p.i("WebvttCssParser", "Invalid font-size: '" + str + "'.");
            return;
        }
        int i8 = 2;
        String str2 = (String) b6.a.e(matcher.group(2));
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
                i8 = 3;
            case 1:
                dVar.t(i8);
                break;
            case 2:
                dVar.t(1);
                break;
            default:
                throw new IllegalStateException();
        }
        dVar.s(Float.parseFloat((String) b6.a.e(matcher.group(1))));
    }

    private static String f(z zVar, StringBuilder sb) {
        boolean z4 = false;
        sb.setLength(0);
        int f5 = zVar.f();
        int g8 = zVar.g();
        while (f5 < g8 && !z4) {
            char c9 = (char) zVar.e()[f5];
            if ((c9 < 'A' || c9 > 'Z') && ((c9 < 'a' || c9 > 'z') && !((c9 >= '0' && c9 <= '9') || c9 == '#' || c9 == '-' || c9 == '.' || c9 == '_'))) {
                z4 = true;
            } else {
                f5++;
                sb.append(c9);
            }
        }
        zVar.V(f5 - zVar.f());
        return sb.toString();
    }

    static String g(z zVar, StringBuilder sb) {
        n(zVar);
        if (zVar.a() == 0) {
            return null;
        }
        String f5 = f(zVar, sb);
        if (BuildConfig.FLAVOR.equals(f5)) {
            return BuildConfig.FLAVOR + ((char) zVar.H());
        }
        return f5;
    }

    private static String h(z zVar, StringBuilder sb) {
        StringBuilder sb2 = new StringBuilder();
        boolean z4 = false;
        while (!z4) {
            int f5 = zVar.f();
            String g8 = g(zVar, sb);
            if (g8 == null) {
                return null;
            }
            if ("}".equals(g8) || ";".equals(g8)) {
                zVar.U(f5);
                z4 = true;
            } else {
                sb2.append(g8);
            }
        }
        return sb2.toString();
    }

    private static String i(z zVar, StringBuilder sb) {
        n(zVar);
        if (zVar.a() >= 5 && "::cue".equals(zVar.E(5))) {
            int f5 = zVar.f();
            String g8 = g(zVar, sb);
            if (g8 == null) {
                return null;
            }
            if ("{".equals(g8)) {
                zVar.U(f5);
                return BuildConfig.FLAVOR;
            }
            String l8 = "(".equals(g8) ? l(zVar) : null;
            if (")".equals(g(zVar, sb))) {
                return l8;
            }
            return null;
        }
        return null;
    }

    private static void j(z zVar, d dVar, StringBuilder sb) {
        n(zVar);
        String f5 = f(zVar, sb);
        if (!BuildConfig.FLAVOR.equals(f5) && ":".equals(g(zVar, sb))) {
            n(zVar);
            String h8 = h(zVar, sb);
            if (h8 == null || BuildConfig.FLAVOR.equals(h8)) {
                return;
            }
            int f8 = zVar.f();
            String g8 = g(zVar, sb);
            if (!";".equals(g8)) {
                if (!"}".equals(g8)) {
                    return;
                }
                zVar.U(f8);
            }
            if ("color".equals(f5)) {
                dVar.q(b6.f.b(h8));
            } else if ("background-color".equals(f5)) {
                dVar.n(b6.f.b(h8));
            } else {
                boolean z4 = true;
                if ("ruby-position".equals(f5)) {
                    if ("over".equals(h8)) {
                        dVar.v(1);
                    } else if ("under".equals(h8)) {
                        dVar.v(2);
                    }
                } else if ("text-combine-upright".equals(f5)) {
                    if (!"all".equals(h8) && !h8.startsWith("digits")) {
                        z4 = false;
                    }
                    dVar.p(z4);
                } else if ("text-decoration".equals(f5)) {
                    if ("underline".equals(h8)) {
                        dVar.A(true);
                    }
                } else if ("font-family".equals(f5)) {
                    dVar.r(h8);
                } else if ("font-weight".equals(f5)) {
                    if ("bold".equals(h8)) {
                        dVar.o(true);
                    }
                } else if ("font-style".equals(f5)) {
                    if ("italic".equals(h8)) {
                        dVar.u(true);
                    }
                } else if ("font-size".equals(f5)) {
                    e(h8, dVar);
                }
            }
        }
    }

    private static char k(z zVar, int i8) {
        return (char) zVar.e()[i8];
    }

    private static String l(z zVar) {
        int f5 = zVar.f();
        int g8 = zVar.g();
        boolean z4 = false;
        while (f5 < g8 && !z4) {
            int i8 = f5 + 1;
            z4 = ((char) zVar.e()[f5]) == ')';
            f5 = i8;
        }
        return zVar.E((f5 - 1) - zVar.f()).trim();
    }

    static void m(z zVar) {
        do {
        } while (!TextUtils.isEmpty(zVar.s()));
    }

    static void n(z zVar) {
        while (true) {
            for (boolean z4 = true; zVar.a() > 0 && z4; z4 = false) {
                if (!c(zVar) && !b(zVar)) {
                }
            }
            return;
        }
    }

    public List<d> d(z zVar) {
        this.f24427b.setLength(0);
        int f5 = zVar.f();
        m(zVar);
        this.f24426a.S(zVar.e(), zVar.f());
        this.f24426a.U(f5);
        ArrayList arrayList = new ArrayList();
        while (true) {
            String i8 = i(this.f24426a, this.f24427b);
            if (i8 == null || !"{".equals(g(this.f24426a, this.f24427b))) {
                return arrayList;
            }
            d dVar = new d();
            a(dVar, i8);
            String str = null;
            boolean z4 = false;
            while (!z4) {
                int f8 = this.f24426a.f();
                String g8 = g(this.f24426a, this.f24427b);
                boolean z8 = g8 == null || "}".equals(g8);
                if (!z8) {
                    this.f24426a.U(f8);
                    j(this.f24426a, dVar, this.f24427b);
                }
                str = g8;
                z4 = z8;
            }
            if ("}".equals(str)) {
                arrayList.add(dVar);
            }
        }
    }
}
