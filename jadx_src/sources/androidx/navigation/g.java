package androidx.navigation;

import android.net.Uri;
import android.os.Bundle;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g {

    /* renamed from: j  reason: collision with root package name */
    private static final Pattern f6367j = Pattern.compile("^[a-zA-Z]+[+\\w\\-.]*:");

    /* renamed from: a  reason: collision with root package name */
    private final ArrayList<String> f6368a = new ArrayList<>();

    /* renamed from: b  reason: collision with root package name */
    private final Map<String, c> f6369b = new HashMap();

    /* renamed from: c  reason: collision with root package name */
    private Pattern f6370c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f6371d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f6372e;

    /* renamed from: f  reason: collision with root package name */
    private final String f6373f;

    /* renamed from: g  reason: collision with root package name */
    private final String f6374g;

    /* renamed from: h  reason: collision with root package name */
    private Pattern f6375h;

    /* renamed from: i  reason: collision with root package name */
    private final String f6376i;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private String f6377a;

        /* renamed from: b  reason: collision with root package name */
        private String f6378b;

        /* renamed from: c  reason: collision with root package name */
        private String f6379c;

        public g a() {
            return new g(this.f6377a, this.f6378b, this.f6379c);
        }

        public a b(String str) {
            if (str.isEmpty()) {
                throw new IllegalArgumentException("The NavDeepLink cannot have an empty action.");
            }
            this.f6378b = str;
            return this;
        }

        public a c(String str) {
            this.f6379c = str;
            return this;
        }

        public a d(String str) {
            this.f6377a = str;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class b implements Comparable<b> {

        /* renamed from: a  reason: collision with root package name */
        String f6380a;

        /* renamed from: b  reason: collision with root package name */
        String f6381b;

        b(String str) {
            String[] split = str.split("/", -1);
            this.f6380a = split[0];
            this.f6381b = split[1];
        }

        @Override // java.lang.Comparable
        /* renamed from: c */
        public int compareTo(b bVar) {
            int i8 = this.f6380a.equals(bVar.f6380a) ? 2 : 0;
            return this.f6381b.equals(bVar.f6381b) ? i8 + 1 : i8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c {

        /* renamed from: a  reason: collision with root package name */
        private String f6382a;

        /* renamed from: b  reason: collision with root package name */
        private ArrayList<String> f6383b = new ArrayList<>();

        c() {
        }

        void a(String str) {
            this.f6383b.add(str);
        }

        String b(int i8) {
            return this.f6383b.get(i8);
        }

        String c() {
            return this.f6382a;
        }

        void d(String str) {
            this.f6382a = str;
        }

        public int e() {
            return this.f6383b.size();
        }
    }

    g(String str, String str2, String str3) {
        this.f6370c = null;
        int i8 = 0;
        this.f6371d = false;
        this.f6372e = false;
        this.f6375h = null;
        this.f6373f = str;
        this.f6374g = str2;
        this.f6376i = str3;
        if (str != null) {
            Uri parse = Uri.parse(str);
            this.f6372e = parse.getQuery() != null;
            StringBuilder sb = new StringBuilder("^");
            if (!f6367j.matcher(str).find()) {
                sb.append("http[s]?://");
            }
            Pattern compile = Pattern.compile("\\{(.+?)\\}");
            if (this.f6372e) {
                Matcher matcher = Pattern.compile("(\\?)").matcher(str);
                if (matcher.find()) {
                    a(str.substring(0, matcher.start()), sb, compile);
                }
                this.f6371d = false;
                for (String str4 : parse.getQueryParameterNames()) {
                    StringBuilder sb2 = new StringBuilder();
                    String queryParameter = parse.getQueryParameter(str4);
                    Matcher matcher2 = compile.matcher(queryParameter);
                    c cVar = new c();
                    int i9 = i8;
                    while (matcher2.find()) {
                        cVar.a(matcher2.group(1));
                        sb2.append(Pattern.quote(queryParameter.substring(i9, matcher2.start())));
                        sb2.append("(.+?)?");
                        i9 = matcher2.end();
                    }
                    if (i9 < queryParameter.length()) {
                        sb2.append(Pattern.quote(queryParameter.substring(i9)));
                    }
                    cVar.d(sb2.toString().replace(".*", "\\E.*\\Q"));
                    this.f6369b.put(str4, cVar);
                    i8 = 0;
                }
            } else {
                this.f6371d = a(str, sb, compile);
            }
            this.f6370c = Pattern.compile(sb.toString().replace(".*", "\\E.*\\Q"), 2);
        }
        if (str3 != null) {
            if (!Pattern.compile("^[\\s\\S]+/[\\s\\S]+$").matcher(str3).matches()) {
                throw new IllegalArgumentException("The given mimeType " + str3 + " does not match to required \"type/subtype\" format");
            }
            b bVar = new b(str3);
            this.f6375h = Pattern.compile(("^(" + bVar.f6380a + "|[*]+)/(" + bVar.f6381b + "|[*]+)$").replace("*|[*]", "[\\s\\S]"));
        }
    }

    private boolean a(String str, StringBuilder sb, Pattern pattern) {
        Matcher matcher = pattern.matcher(str);
        boolean z4 = !str.contains(".*");
        int i8 = 0;
        while (matcher.find()) {
            this.f6368a.add(matcher.group(1));
            sb.append(Pattern.quote(str.substring(i8, matcher.start())));
            sb.append("(.+?)");
            i8 = matcher.end();
            z4 = false;
        }
        if (i8 < str.length()) {
            sb.append(Pattern.quote(str.substring(i8)));
        }
        sb.append("($|(\\?(.)*))");
        return z4;
    }

    private boolean f(Bundle bundle, String str, String str2, d dVar) {
        if (dVar == null) {
            bundle.putString(str, str2);
            return false;
        }
        try {
            dVar.a().g(bundle, str, str2);
            return false;
        } catch (IllegalArgumentException unused) {
            return true;
        }
    }

    public String b() {
        return this.f6374g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle c(Uri uri, Map<String, d> map) {
        Matcher matcher;
        Matcher matcher2 = this.f6370c.matcher(uri.toString());
        if (matcher2.matches()) {
            Bundle bundle = new Bundle();
            int size = this.f6368a.size();
            int i8 = 0;
            while (i8 < size) {
                String str = this.f6368a.get(i8);
                i8++;
                if (f(bundle, str, Uri.decode(matcher2.group(i8)), map.get(str))) {
                    return null;
                }
            }
            if (this.f6372e) {
                for (String str2 : this.f6369b.keySet()) {
                    c cVar = this.f6369b.get(str2);
                    String queryParameter = uri.getQueryParameter(str2);
                    if (queryParameter != null) {
                        matcher = Pattern.compile(cVar.c()).matcher(queryParameter);
                        if (!matcher.matches()) {
                            return null;
                        }
                    } else {
                        matcher = null;
                    }
                    for (int i9 = 0; i9 < cVar.e(); i9++) {
                        String decode = matcher != null ? Uri.decode(matcher.group(i9 + 1)) : null;
                        String b9 = cVar.b(i9);
                        d dVar = map.get(b9);
                        if (decode != null && !decode.replaceAll("[{}]", BuildConfig.FLAVOR).equals(b9) && f(bundle, b9, decode, dVar)) {
                            return null;
                        }
                    }
                }
            }
            return bundle;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int d(String str) {
        if (this.f6376i == null || !this.f6375h.matcher(str).matches()) {
            return -1;
        }
        return new b(this.f6376i).compareTo(new b(str));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean e() {
        return this.f6371d;
    }
}
