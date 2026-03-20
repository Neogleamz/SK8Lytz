package y5;

import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import b6.l0;
import b6.p;
import b6.z;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p5.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {

    /* renamed from: a  reason: collision with root package name */
    public static final Pattern f24447a = Pattern.compile("^(\\S+)\\s+-->\\s+(\\S+)(.*)?$");

    /* renamed from: b  reason: collision with root package name */
    private static final Pattern f24448b = Pattern.compile("(\\S+?):(\\S+)");

    /* renamed from: c  reason: collision with root package name */
    private static final Map<String, Integer> f24449c;

    /* renamed from: d  reason: collision with root package name */
    private static final Map<String, Integer> f24450d;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: c  reason: collision with root package name */
        private static final Comparator<b> f24451c = g.a;

        /* renamed from: a  reason: collision with root package name */
        private final c f24452a;

        /* renamed from: b  reason: collision with root package name */
        private final int f24453b;

        private b(c cVar, int i8) {
            this.f24452a = cVar;
            this.f24453b = i8;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int e(b bVar, b bVar2) {
            return Integer.compare(bVar.f24452a.f24455b, bVar2.f24452a.f24455b);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        public final String f24454a;

        /* renamed from: b  reason: collision with root package name */
        public final int f24455b;

        /* renamed from: c  reason: collision with root package name */
        public final String f24456c;

        /* renamed from: d  reason: collision with root package name */
        public final Set<String> f24457d;

        private c(String str, int i8, String str2, Set<String> set) {
            this.f24455b = i8;
            this.f24454a = str;
            this.f24456c = str2;
            this.f24457d = set;
        }

        public static c a(String str, int i8) {
            String str2;
            String trim = str.trim();
            b6.a.a(!trim.isEmpty());
            int indexOf = trim.indexOf(" ");
            if (indexOf == -1) {
                str2 = BuildConfig.FLAVOR;
            } else {
                String trim2 = trim.substring(indexOf).trim();
                trim = trim.substring(0, indexOf);
                str2 = trim2;
            }
            String[] R0 = l0.R0(trim, "\\.");
            String str3 = R0[0];
            HashSet hashSet = new HashSet();
            for (int i9 = 1; i9 < R0.length; i9++) {
                hashSet.add(R0[i9]);
            }
            return new c(str3, i8, str2, hashSet);
        }

        public static c b() {
            return new c(BuildConfig.FLAVOR, 0, BuildConfig.FLAVOR, Collections.emptySet());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d implements Comparable<d> {

        /* renamed from: a  reason: collision with root package name */
        public final int f24458a;

        /* renamed from: b  reason: collision with root package name */
        public final y5.d f24459b;

        public d(int i8, y5.d dVar) {
            this.f24458a = i8;
            this.f24459b = dVar;
        }

        @Override // java.lang.Comparable
        /* renamed from: c */
        public int compareTo(d dVar) {
            return Integer.compare(this.f24458a, dVar.f24458a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: c  reason: collision with root package name */
        public CharSequence f24462c;

        /* renamed from: a  reason: collision with root package name */
        public long f24460a = 0;

        /* renamed from: b  reason: collision with root package name */
        public long f24461b = 0;

        /* renamed from: d  reason: collision with root package name */
        public int f24463d = 2;

        /* renamed from: e  reason: collision with root package name */
        public float f24464e = -3.4028235E38f;

        /* renamed from: f  reason: collision with root package name */
        public int f24465f = 1;

        /* renamed from: g  reason: collision with root package name */
        public int f24466g = 0;

        /* renamed from: h  reason: collision with root package name */
        public float f24467h = -3.4028235E38f;

        /* renamed from: i  reason: collision with root package name */
        public int f24468i = Integer.MIN_VALUE;

        /* renamed from: j  reason: collision with root package name */
        public float f24469j = 1.0f;

        /* renamed from: k  reason: collision with root package name */
        public int f24470k = Integer.MIN_VALUE;

        private static float b(float f5, int i8) {
            int i9 = (f5 > (-3.4028235E38f) ? 1 : (f5 == (-3.4028235E38f) ? 0 : -1));
            if (i9 == 0 || i8 != 0 || (f5 >= 0.0f && f5 <= 1.0f)) {
                return i9 != 0 ? f5 : i8 == 0 ? 1.0f : -3.4028235E38f;
            }
            return 1.0f;
        }

        private static Layout.Alignment c(int i8) {
            if (i8 != 1) {
                if (i8 != 2) {
                    if (i8 != 3) {
                        if (i8 != 4) {
                            if (i8 != 5) {
                                p.i("WebvttCueParser", "Unknown textAlignment: " + i8);
                                return null;
                            }
                        }
                    }
                    return Layout.Alignment.ALIGN_OPPOSITE;
                }
                return Layout.Alignment.ALIGN_CENTER;
            }
            return Layout.Alignment.ALIGN_NORMAL;
        }

        private static float d(int i8, float f5) {
            if (i8 != 0) {
                if (i8 == 1) {
                    return f5 <= 0.5f ? f5 * 2.0f : (1.0f - f5) * 2.0f;
                } else if (i8 == 2) {
                    return f5;
                } else {
                    throw new IllegalStateException(String.valueOf(i8));
                }
            }
            return 1.0f - f5;
        }

        private static float e(int i8) {
            if (i8 != 4) {
                return i8 != 5 ? 0.5f : 1.0f;
            }
            return 0.0f;
        }

        private static int f(int i8) {
            if (i8 != 1) {
                if (i8 != 3) {
                    if (i8 != 4) {
                        return i8 != 5 ? 1 : 2;
                    }
                    return 0;
                }
                return 2;
            }
            return 0;
        }

        public y5.e a() {
            return new y5.e(g().a(), this.f24460a, this.f24461b);
        }

        public b.C0196b g() {
            float f5 = this.f24467h;
            if (f5 == -3.4028235E38f) {
                f5 = e(this.f24463d);
            }
            int i8 = this.f24468i;
            if (i8 == Integer.MIN_VALUE) {
                i8 = f(this.f24463d);
            }
            b.C0196b r4 = new b.C0196b().p(c(this.f24463d)).h(b(this.f24464e, this.f24465f), this.f24465f).i(this.f24466g).k(f5).l(i8).n(Math.min(this.f24469j, d(i8, f5))).r(this.f24470k);
            CharSequence charSequence = this.f24462c;
            if (charSequence != null) {
                r4.o(charSequence);
            }
            return r4;
        }
    }

    static {
        HashMap hashMap = new HashMap();
        hashMap.put("white", Integer.valueOf(Color.rgb(255, 255, 255)));
        hashMap.put("lime", Integer.valueOf(Color.rgb(0, 255, 0)));
        hashMap.put("cyan", Integer.valueOf(Color.rgb(0, 255, 255)));
        hashMap.put("red", Integer.valueOf(Color.rgb(255, 0, 0)));
        hashMap.put("yellow", Integer.valueOf(Color.rgb(255, 255, 0)));
        hashMap.put("magenta", Integer.valueOf(Color.rgb(255, 0, 255)));
        hashMap.put("blue", Integer.valueOf(Color.rgb(0, 0, 255)));
        hashMap.put("black", Integer.valueOf(Color.rgb(0, 0, 0)));
        f24449c = Collections.unmodifiableMap(hashMap);
        HashMap hashMap2 = new HashMap();
        hashMap2.put("bg_white", Integer.valueOf(Color.rgb(255, 255, 255)));
        hashMap2.put("bg_lime", Integer.valueOf(Color.rgb(0, 255, 0)));
        hashMap2.put("bg_cyan", Integer.valueOf(Color.rgb(0, 255, 255)));
        hashMap2.put("bg_red", Integer.valueOf(Color.rgb(255, 0, 0)));
        hashMap2.put("bg_yellow", Integer.valueOf(Color.rgb(255, 255, 0)));
        hashMap2.put("bg_magenta", Integer.valueOf(Color.rgb(255, 0, 255)));
        hashMap2.put("bg_blue", Integer.valueOf(Color.rgb(0, 0, 255)));
        hashMap2.put("bg_black", Integer.valueOf(Color.rgb(0, 0, 0)));
        f24450d = Collections.unmodifiableMap(hashMap2);
    }

    private static void a(SpannableStringBuilder spannableStringBuilder, Set<String> set, int i8, int i9) {
        Object foregroundColorSpan;
        for (String str : set) {
            Map<String, Integer> map = f24449c;
            if (map.containsKey(str)) {
                foregroundColorSpan = new ForegroundColorSpan(map.get(str).intValue());
            } else {
                Map<String, Integer> map2 = f24450d;
                if (map2.containsKey(str)) {
                    foregroundColorSpan = new BackgroundColorSpan(map2.get(str).intValue());
                }
            }
            spannableStringBuilder.setSpan(foregroundColorSpan, i8, i9, 33);
        }
    }

    private static void b(String str, SpannableStringBuilder spannableStringBuilder) {
        char c9;
        str.hashCode();
        char c10 = 65535;
        switch (str.hashCode()) {
            case 3309:
                if (str.equals("gt")) {
                    c10 = 0;
                    break;
                }
                break;
            case 3464:
                if (str.equals("lt")) {
                    c10 = 1;
                    break;
                }
                break;
            case 96708:
                if (str.equals("amp")) {
                    c10 = 2;
                    break;
                }
                break;
            case 3374865:
                if (str.equals("nbsp")) {
                    c10 = 3;
                    break;
                }
                break;
        }
        switch (c10) {
            case 0:
                c9 = '>';
                break;
            case 1:
                c9 = '<';
                break;
            case 2:
                c9 = '&';
                break;
            case 3:
                c9 = ' ';
                break;
            default:
                p.i("WebvttCueParser", "ignoring unsupported entity: '&" + str + ";'");
                return;
        }
        spannableStringBuilder.append(c9);
    }

    private static void c(SpannableStringBuilder spannableStringBuilder, String str, c cVar, List<b> list, List<y5.d> list2) {
        int i8 = i(list2, str, cVar);
        ArrayList arrayList = new ArrayList(list.size());
        arrayList.addAll(list);
        Collections.sort(arrayList, b.f24451c);
        int i9 = cVar.f24455b;
        int i10 = 0;
        for (int i11 = 0; i11 < arrayList.size(); i11++) {
            if ("rt".equals(((b) arrayList.get(i11)).f24452a.f24454a)) {
                b bVar = (b) arrayList.get(i11);
                int g8 = g(i(list2, str, bVar.f24452a), i8, 1);
                int i12 = bVar.f24452a.f24455b - i10;
                int i13 = bVar.f24453b - i10;
                CharSequence subSequence = spannableStringBuilder.subSequence(i12, i13);
                spannableStringBuilder.delete(i12, i13);
                spannableStringBuilder.setSpan(new t5.b(subSequence.toString(), g8), i9, i12, 33);
                i10 += subSequence.length();
                i9 = i12;
            }
        }
    }

    private static void d(String str, c cVar, List<b> list, SpannableStringBuilder spannableStringBuilder, List<y5.d> list2) {
        Object styleSpan;
        int i8 = cVar.f24455b;
        int length = spannableStringBuilder.length();
        String str2 = cVar.f24454a;
        str2.hashCode();
        char c9 = 65535;
        switch (str2.hashCode()) {
            case 0:
                if (str2.equals(BuildConfig.FLAVOR)) {
                    c9 = 0;
                    break;
                }
                break;
            case 98:
                if (str2.equals("b")) {
                    c9 = 1;
                    break;
                }
                break;
            case 99:
                if (str2.equals("c")) {
                    c9 = 2;
                    break;
                }
                break;
            case 105:
                if (str2.equals("i")) {
                    c9 = 3;
                    break;
                }
                break;
            case 117:
                if (str2.equals("u")) {
                    c9 = 4;
                    break;
                }
                break;
            case 118:
                if (str2.equals("v")) {
                    c9 = 5;
                    break;
                }
                break;
            case 3314158:
                if (str2.equals("lang")) {
                    c9 = 6;
                    break;
                }
                break;
            case 3511770:
                if (str2.equals("ruby")) {
                    c9 = 7;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
            case 5:
            case 6:
                break;
            case 1:
                styleSpan = new StyleSpan(1);
                spannableStringBuilder.setSpan(styleSpan, i8, length, 33);
                break;
            case 2:
                a(spannableStringBuilder, cVar.f24457d, i8, length);
                break;
            case 3:
                styleSpan = new StyleSpan(2);
                spannableStringBuilder.setSpan(styleSpan, i8, length, 33);
                break;
            case 4:
                styleSpan = new UnderlineSpan();
                spannableStringBuilder.setSpan(styleSpan, i8, length, 33);
                break;
            case 7:
                c(spannableStringBuilder, str, cVar, list, list2);
                break;
            default:
                return;
        }
        List<d> h8 = h(list2, str, cVar);
        for (int i9 = 0; i9 < h8.size(); i9++) {
            e(spannableStringBuilder, h8.get(i9).f24459b, i8, length);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:37:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void e(android.text.SpannableStringBuilder r4, y5.d r5, int r6, int r7) {
        /*
            if (r5 != 0) goto L3
            return
        L3:
            int r0 = r5.i()
            r1 = -1
            r2 = 33
            if (r0 == r1) goto L18
            android.text.style.StyleSpan r0 = new android.text.style.StyleSpan
            int r1 = r5.i()
            r0.<init>(r1)
            t5.c.a(r4, r0, r6, r7, r2)
        L18:
            boolean r0 = r5.l()
            if (r0 == 0) goto L26
            android.text.style.StrikethroughSpan r0 = new android.text.style.StrikethroughSpan
            r0.<init>()
            r4.setSpan(r0, r6, r7, r2)
        L26:
            boolean r0 = r5.m()
            if (r0 == 0) goto L34
            android.text.style.UnderlineSpan r0 = new android.text.style.UnderlineSpan
            r0.<init>()
            r4.setSpan(r0, r6, r7, r2)
        L34:
            boolean r0 = r5.k()
            if (r0 == 0) goto L46
            android.text.style.ForegroundColorSpan r0 = new android.text.style.ForegroundColorSpan
            int r1 = r5.c()
            r0.<init>(r1)
            t5.c.a(r4, r0, r6, r7, r2)
        L46:
            boolean r0 = r5.j()
            if (r0 == 0) goto L58
            android.text.style.BackgroundColorSpan r0 = new android.text.style.BackgroundColorSpan
            int r1 = r5.a()
            r0.<init>(r1)
            t5.c.a(r4, r0, r6, r7, r2)
        L58:
            java.lang.String r0 = r5.d()
            if (r0 == 0) goto L6a
            android.text.style.TypefaceSpan r0 = new android.text.style.TypefaceSpan
            java.lang.String r1 = r5.d()
            r0.<init>(r1)
            t5.c.a(r4, r0, r6, r7, r2)
        L6a:
            int r0 = r5.f()
            r1 = 1
            if (r0 == r1) goto L8f
            r1 = 2
            if (r0 == r1) goto L85
            r1 = 3
            if (r0 == r1) goto L78
            goto L9c
        L78:
            android.text.style.RelativeSizeSpan r0 = new android.text.style.RelativeSizeSpan
            float r1 = r5.e()
            r3 = 1120403456(0x42c80000, float:100.0)
            float r1 = r1 / r3
            r0.<init>(r1)
            goto L99
        L85:
            android.text.style.RelativeSizeSpan r0 = new android.text.style.RelativeSizeSpan
            float r1 = r5.e()
            r0.<init>(r1)
            goto L99
        L8f:
            android.text.style.AbsoluteSizeSpan r0 = new android.text.style.AbsoluteSizeSpan
            float r3 = r5.e()
            int r3 = (int) r3
            r0.<init>(r3, r1)
        L99:
            t5.c.a(r4, r0, r6, r7, r2)
        L9c:
            boolean r5 = r5.b()
            if (r5 == 0) goto Laa
            t5.a r5 = new t5.a
            r5.<init>()
            r4.setSpan(r5, r6, r7, r2)
        Laa:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: y5.f.e(android.text.SpannableStringBuilder, y5.d, int, int):void");
    }

    private static int f(String str, int i8) {
        int indexOf = str.indexOf(62, i8);
        return indexOf == -1 ? str.length() : indexOf + 1;
    }

    private static int g(int i8, int i9, int i10) {
        if (i8 != -1) {
            return i8;
        }
        if (i9 != -1) {
            return i9;
        }
        if (i10 != -1) {
            return i10;
        }
        throw new IllegalArgumentException();
    }

    private static List<d> h(List<y5.d> list, String str, c cVar) {
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < list.size(); i8++) {
            y5.d dVar = list.get(i8);
            int h8 = dVar.h(str, cVar.f24454a, cVar.f24457d, cVar.f24456c);
            if (h8 > 0) {
                arrayList.add(new d(h8, dVar));
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private static int i(List<y5.d> list, String str, c cVar) {
        List<d> h8 = h(list, str, cVar);
        for (int i8 = 0; i8 < h8.size(); i8++) {
            y5.d dVar = h8.get(i8).f24459b;
            if (dVar.g() != -1) {
                return dVar.g();
            }
        }
        return -1;
    }

    private static String j(String str) {
        String trim = str.trim();
        b6.a.a(!trim.isEmpty());
        return l0.S0(trim, "[ \\.]")[0];
    }

    private static boolean k(String str) {
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case 98:
                if (str.equals("b")) {
                    c9 = 0;
                    break;
                }
                break;
            case 99:
                if (str.equals("c")) {
                    c9 = 1;
                    break;
                }
                break;
            case 105:
                if (str.equals("i")) {
                    c9 = 2;
                    break;
                }
                break;
            case 117:
                if (str.equals("u")) {
                    c9 = 3;
                    break;
                }
                break;
            case 118:
                if (str.equals("v")) {
                    c9 = 4;
                    break;
                }
                break;
            case 3650:
                if (str.equals("rt")) {
                    c9 = 5;
                    break;
                }
                break;
            case 3314158:
                if (str.equals("lang")) {
                    c9 = 6;
                    break;
                }
                break;
            case 3511770:
                if (str.equals("ruby")) {
                    c9 = 7;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                return true;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static p5.b l(CharSequence charSequence) {
        e eVar = new e();
        eVar.f24462c = charSequence;
        return eVar.g().a();
    }

    public static y5.e m(z zVar, List<y5.d> list) {
        String s8 = zVar.s();
        if (s8 == null) {
            return null;
        }
        Pattern pattern = f24447a;
        Matcher matcher = pattern.matcher(s8);
        if (matcher.matches()) {
            return n(null, matcher, zVar, list);
        }
        String s9 = zVar.s();
        if (s9 == null) {
            return null;
        }
        Matcher matcher2 = pattern.matcher(s9);
        if (matcher2.matches()) {
            return n(s8.trim(), matcher2, zVar, list);
        }
        return null;
    }

    private static y5.e n(String str, Matcher matcher, z zVar, List<y5.d> list) {
        e eVar = new e();
        try {
            eVar.f24460a = i.d((String) b6.a.e(matcher.group(1)));
            eVar.f24461b = i.d((String) b6.a.e(matcher.group(2)));
            p((String) b6.a.e(matcher.group(3)), eVar);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String s8 = zVar.s();
                if (TextUtils.isEmpty(s8)) {
                    eVar.f24462c = q(str, sb.toString(), list);
                    return eVar.a();
                }
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(s8.trim());
            }
        } catch (NumberFormatException unused) {
            p.i("WebvttCueParser", "Skipping cue with bad header: " + matcher.group());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static b.C0196b o(String str) {
        e eVar = new e();
        p(str, eVar);
        return eVar.g();
    }

    private static void p(String str, e eVar) {
        Matcher matcher = f24448b.matcher(str);
        while (matcher.find()) {
            String str2 = (String) b6.a.e(matcher.group(1));
            String str3 = (String) b6.a.e(matcher.group(2));
            try {
                if ("line".equals(str2)) {
                    s(str3, eVar);
                } else if ("align".equals(str2)) {
                    eVar.f24463d = v(str3);
                } else if ("position".equals(str2)) {
                    u(str3, eVar);
                } else if ("size".equals(str2)) {
                    eVar.f24469j = i.c(str3);
                } else if ("vertical".equals(str2)) {
                    eVar.f24470k = w(str3);
                } else {
                    p.i("WebvttCueParser", "Unknown cue setting " + str2 + ":" + str3);
                }
            } catch (NumberFormatException unused) {
                p.i("WebvttCueParser", "Skipping bad cue setting: " + matcher.group());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SpannedString q(String str, String str2, List<y5.d> list) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        ArrayDeque arrayDeque = new ArrayDeque();
        ArrayList arrayList = new ArrayList();
        int i8 = 0;
        while (i8 < str2.length()) {
            char charAt = str2.charAt(i8);
            if (charAt == '&') {
                i8++;
                int indexOf = str2.indexOf(59, i8);
                int indexOf2 = str2.indexOf(32, i8);
                if (indexOf == -1) {
                    indexOf = indexOf2;
                } else if (indexOf2 != -1) {
                    indexOf = Math.min(indexOf, indexOf2);
                }
                if (indexOf != -1) {
                    b(str2.substring(i8, indexOf), spannableStringBuilder);
                    if (indexOf == indexOf2) {
                        spannableStringBuilder.append((CharSequence) " ");
                    }
                    i8 = indexOf + 1;
                } else {
                    spannableStringBuilder.append(charAt);
                }
            } else if (charAt != '<') {
                spannableStringBuilder.append(charAt);
                i8++;
            } else {
                int i9 = i8 + 1;
                if (i9 < str2.length()) {
                    boolean z4 = str2.charAt(i9) == '/';
                    i9 = f(str2, i9);
                    int i10 = i9 - 2;
                    boolean z8 = str2.charAt(i10) == '/';
                    int i11 = i8 + (z4 ? 2 : 1);
                    if (!z8) {
                        i10 = i9 - 1;
                    }
                    String substring = str2.substring(i11, i10);
                    if (!substring.trim().isEmpty()) {
                        String j8 = j(substring);
                        if (k(j8)) {
                            if (z4) {
                                while (!arrayDeque.isEmpty()) {
                                    c cVar = (c) arrayDeque.pop();
                                    d(str, cVar, arrayList, spannableStringBuilder, list);
                                    if (arrayDeque.isEmpty()) {
                                        arrayList.clear();
                                    } else {
                                        arrayList.add(new b(cVar, spannableStringBuilder.length()));
                                    }
                                    if (cVar.f24454a.equals(j8)) {
                                        break;
                                    }
                                }
                            } else if (!z8) {
                                arrayDeque.push(c.a(substring, spannableStringBuilder.length()));
                            }
                        }
                    }
                }
                i8 = i9;
            }
        }
        while (!arrayDeque.isEmpty()) {
            d(str, (c) arrayDeque.pop(), arrayList, spannableStringBuilder, list);
        }
        d(str, c.b(), Collections.emptyList(), spannableStringBuilder, list);
        return SpannedString.valueOf(spannableStringBuilder);
    }

    private static int r(String str) {
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1364013995:
                if (str.equals("center")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1074341483:
                if (str.equals("middle")) {
                    c9 = 1;
                    break;
                }
                break;
            case 100571:
                if (str.equals("end")) {
                    c9 = 2;
                    break;
                }
                break;
            case 109757538:
                if (str.equals("start")) {
                    c9 = 3;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 0;
            default:
                p.i("WebvttCueParser", "Invalid anchor value: " + str);
                return Integer.MIN_VALUE;
        }
    }

    private static void s(String str, e eVar) {
        int indexOf = str.indexOf(44);
        if (indexOf != -1) {
            eVar.f24466g = r(str.substring(indexOf + 1));
            str = str.substring(0, indexOf);
        }
        if (str.endsWith("%")) {
            eVar.f24464e = i.c(str);
            eVar.f24465f = 0;
            return;
        }
        eVar.f24464e = Integer.parseInt(str);
        eVar.f24465f = 1;
    }

    private static int t(String str) {
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1842484672:
                if (str.equals("line-left")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1364013995:
                if (str.equals("center")) {
                    c9 = 1;
                    break;
                }
                break;
            case -1276788989:
                if (str.equals("line-right")) {
                    c9 = 2;
                    break;
                }
                break;
            case -1074341483:
                if (str.equals("middle")) {
                    c9 = 3;
                    break;
                }
                break;
            case 100571:
                if (str.equals("end")) {
                    c9 = 4;
                    break;
                }
                break;
            case 109757538:
                if (str.equals("start")) {
                    c9 = 5;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
            case 5:
                return 0;
            case 1:
            case 3:
                return 1;
            case 2:
            case 4:
                return 2;
            default:
                p.i("WebvttCueParser", "Invalid anchor value: " + str);
                return Integer.MIN_VALUE;
        }
    }

    private static void u(String str, e eVar) {
        int indexOf = str.indexOf(44);
        if (indexOf != -1) {
            eVar.f24468i = t(str.substring(indexOf + 1));
            str = str.substring(0, indexOf);
        }
        eVar.f24467h = i.c(str);
    }

    private static int v(String str) {
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1364013995:
                if (str.equals("center")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1074341483:
                if (str.equals("middle")) {
                    c9 = 1;
                    break;
                }
                break;
            case 100571:
                if (str.equals("end")) {
                    c9 = 2;
                    break;
                }
                break;
            case 3317767:
                if (str.equals("left")) {
                    c9 = 3;
                    break;
                }
                break;
            case 108511772:
                if (str.equals("right")) {
                    c9 = 4;
                    break;
                }
                break;
            case 109757538:
                if (str.equals("start")) {
                    c9 = 5;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 1;
            default:
                p.i("WebvttCueParser", "Invalid alignment value: " + str);
                return 2;
        }
    }

    private static int w(String str) {
        str.hashCode();
        if (str.equals("lr")) {
            return 2;
        }
        if (str.equals("rl")) {
            return 1;
        }
        p.i("WebvttCueParser", "Invalid 'vertical' value: " + str);
        return Integer.MIN_VALUE;
    }
}
