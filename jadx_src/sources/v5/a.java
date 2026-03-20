package v5;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import b6.p;
import b6.q;
import b6.z;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.common.base.e;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p5.b;
import p5.g;
import p5.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends g {
    private static final Pattern q = Pattern.compile("\\s*((?:(\\d+):)?(\\d+):(\\d+)(?:,(\\d+))?)\\s*-->\\s*((?:(\\d+):)?(\\d+):(\\d+)(?:,(\\d+))?)\\s*");

    /* renamed from: r  reason: collision with root package name */
    private static final Pattern f23340r = Pattern.compile("\\{\\\\.*?\\}");

    /* renamed from: o  reason: collision with root package name */
    private final StringBuilder f23341o;

    /* renamed from: p  reason: collision with root package name */
    private final ArrayList<String> f23342p;

    public a() {
        super("SubripDecoder");
        this.f23341o = new StringBuilder();
        this.f23342p = new ArrayList<>();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private p5.b B(Spanned spanned, String str) {
        char c9;
        char c10;
        b.C0196b o5 = new b.C0196b().o(spanned);
        if (str == null) {
            return o5.a();
        }
        switch (str.hashCode()) {
            case -685620710:
                if (str.equals("{\\an1}")) {
                    c9 = 0;
                    break;
                }
                c9 = 65535;
                break;
            case -685620679:
                if (str.equals("{\\an2}")) {
                    c9 = 6;
                    break;
                }
                c9 = 65535;
                break;
            case -685620648:
                if (str.equals("{\\an3}")) {
                    c9 = 3;
                    break;
                }
                c9 = 65535;
                break;
            case -685620617:
                if (str.equals("{\\an4}")) {
                    c9 = 1;
                    break;
                }
                c9 = 65535;
                break;
            case -685620586:
                if (str.equals("{\\an5}")) {
                    c9 = 7;
                    break;
                }
                c9 = 65535;
                break;
            case -685620555:
                if (str.equals("{\\an6}")) {
                    c9 = 4;
                    break;
                }
                c9 = 65535;
                break;
            case -685620524:
                if (str.equals("{\\an7}")) {
                    c9 = 2;
                    break;
                }
                c9 = 65535;
                break;
            case -685620493:
                if (str.equals("{\\an8}")) {
                    c9 = '\b';
                    break;
                }
                c9 = 65535;
                break;
            case -685620462:
                if (str.equals("{\\an9}")) {
                    c9 = 5;
                    break;
                }
                c9 = 65535;
                break;
            default:
                c9 = 65535;
                break;
        }
        if (c9 == 0 || c9 == 1 || c9 == 2) {
            o5.l(0);
        } else if (c9 == 3 || c9 == 4 || c9 == 5) {
            o5.l(2);
        } else {
            o5.l(1);
        }
        switch (str.hashCode()) {
            case -685620710:
                if (str.equals("{\\an1}")) {
                    c10 = 0;
                    break;
                }
                c10 = 65535;
                break;
            case -685620679:
                if (str.equals("{\\an2}")) {
                    c10 = 1;
                    break;
                }
                c10 = 65535;
                break;
            case -685620648:
                if (str.equals("{\\an3}")) {
                    c10 = 2;
                    break;
                }
                c10 = 65535;
                break;
            case -685620617:
                if (str.equals("{\\an4}")) {
                    c10 = 6;
                    break;
                }
                c10 = 65535;
                break;
            case -685620586:
                if (str.equals("{\\an5}")) {
                    c10 = 7;
                    break;
                }
                c10 = 65535;
                break;
            case -685620555:
                if (str.equals("{\\an6}")) {
                    c10 = '\b';
                    break;
                }
                c10 = 65535;
                break;
            case -685620524:
                if (str.equals("{\\an7}")) {
                    c10 = 3;
                    break;
                }
                c10 = 65535;
                break;
            case -685620493:
                if (str.equals("{\\an8}")) {
                    c10 = 4;
                    break;
                }
                c10 = 65535;
                break;
            case -685620462:
                if (str.equals("{\\an9}")) {
                    c10 = 5;
                    break;
                }
                c10 = 65535;
                break;
            default:
                c10 = 65535;
                break;
        }
        if (c10 == 0 || c10 == 1 || c10 == 2) {
            o5.i(2);
        } else if (c10 == 3 || c10 == 4 || c10 == 5) {
            o5.i(0);
        } else {
            o5.i(1);
        }
        return o5.k(D(o5.d())).h(D(o5.c()), 0).a();
    }

    private Charset C(z zVar) {
        Charset P = zVar.P();
        return P != null ? P : e.f18817c;
    }

    static float D(int i8) {
        if (i8 != 0) {
            if (i8 != 1) {
                if (i8 == 2) {
                    return 0.92f;
                }
                throw new IllegalArgumentException();
            }
            return 0.5f;
        }
        return 0.08f;
    }

    private static long E(Matcher matcher, int i8) {
        String group = matcher.group(i8 + 1);
        long parseLong = (group != null ? Long.parseLong(group) * 60 * 60 * 1000 : 0L) + (Long.parseLong((String) b6.a.e(matcher.group(i8 + 2))) * 60 * 1000) + (Long.parseLong((String) b6.a.e(matcher.group(i8 + 3))) * 1000);
        String group2 = matcher.group(i8 + 4);
        if (group2 != null) {
            parseLong += Long.parseLong(group2);
        }
        return parseLong * 1000;
    }

    private String F(String str, ArrayList<String> arrayList) {
        String trim = str.trim();
        StringBuilder sb = new StringBuilder(trim);
        Matcher matcher = f23340r.matcher(trim);
        int i8 = 0;
        while (matcher.find()) {
            String group = matcher.group();
            arrayList.add(group);
            int start = matcher.start() - i8;
            int length = group.length();
            sb.replace(start, start + length, BuildConfig.FLAVOR);
            i8 += length;
        }
        return sb.toString();
    }

    @Override // p5.g
    protected h A(byte[] bArr, int i8, boolean z4) {
        StringBuilder sb;
        String str;
        ArrayList arrayList = new ArrayList();
        q qVar = new q();
        z zVar = new z(bArr, i8);
        Charset C = C(zVar);
        while (true) {
            String t8 = zVar.t(C);
            int i9 = 0;
            if (t8 == null) {
                break;
            } else if (t8.length() != 0) {
                try {
                    Integer.parseInt(t8);
                    t8 = zVar.t(C);
                } catch (NumberFormatException unused) {
                    sb = new StringBuilder();
                    str = "Skipping invalid index: ";
                }
                if (t8 == null) {
                    p.i("SubripDecoder", "Unexpected end");
                    break;
                }
                Matcher matcher = q.matcher(t8);
                if (matcher.matches()) {
                    qVar.a(E(matcher, 1));
                    qVar.a(E(matcher, 6));
                    this.f23341o.setLength(0);
                    this.f23342p.clear();
                    while (true) {
                        String t9 = zVar.t(C);
                        if (TextUtils.isEmpty(t9)) {
                            break;
                        }
                        if (this.f23341o.length() > 0) {
                            this.f23341o.append("<br>");
                        }
                        this.f23341o.append(F(t9, this.f23342p));
                    }
                    Spanned fromHtml = Html.fromHtml(this.f23341o.toString());
                    String str2 = null;
                    while (true) {
                        if (i9 >= this.f23342p.size()) {
                            break;
                        }
                        String str3 = this.f23342p.get(i9);
                        if (str3.matches("\\{\\\\an[1-9]\\}")) {
                            str2 = str3;
                            break;
                        }
                        i9++;
                    }
                    arrayList.add(B(fromHtml, str2));
                    arrayList.add(p5.b.f22371x);
                } else {
                    sb = new StringBuilder();
                    str = "Skipping invalid timing: ";
                    sb.append(str);
                    sb.append(t8);
                    p.i("SubripDecoder", sb.toString());
                }
            }
        }
        return new b((p5.b[]) arrayList.toArray(new p5.b[0]), qVar.d());
    }
}
