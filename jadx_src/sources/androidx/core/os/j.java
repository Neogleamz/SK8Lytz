package androidx.core.os;

import android.os.Build;
import android.os.LocaleList;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j {

    /* renamed from: b  reason: collision with root package name */
    private static final j f4774b = a(new Locale[0]);

    /* renamed from: a  reason: collision with root package name */
    private final l f4775a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static Locale a(String str) {
            return Locale.forLanguageTag(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {
        static LocaleList a(Locale... localeArr) {
            return new LocaleList(localeArr);
        }

        static LocaleList b() {
            return LocaleList.getAdjustedDefault();
        }

        static LocaleList c() {
            return LocaleList.getDefault();
        }
    }

    private j(l lVar) {
        this.f4775a = lVar;
    }

    public static j a(Locale... localeArr) {
        return Build.VERSION.SDK_INT >= 24 ? i(b.a(localeArr)) : new j(new k(localeArr));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Locale b(String str) {
        if (str.contains("-")) {
            String[] split = str.split("-", -1);
            if (split.length > 2) {
                return new Locale(split[0], split[1], split[2]);
            }
            if (split.length > 1) {
                return new Locale(split[0], split[1]);
            }
            if (split.length == 1) {
                return new Locale(split[0]);
            }
        } else if (!str.contains("_")) {
            return new Locale(str);
        } else {
            String[] split2 = str.split("_", -1);
            if (split2.length > 2) {
                return new Locale(split2[0], split2[1], split2[2]);
            }
            if (split2.length > 1) {
                return new Locale(split2[0], split2[1]);
            }
            if (split2.length == 1) {
                return new Locale(split2[0]);
            }
        }
        throw new IllegalArgumentException("Can not parse language tag: [" + str + "]");
    }

    public static j c(String str) {
        if (str == null || str.isEmpty()) {
            return e();
        }
        String[] split = str.split(",", -1);
        int length = split.length;
        Locale[] localeArr = new Locale[length];
        for (int i8 = 0; i8 < length; i8++) {
            localeArr[i8] = Build.VERSION.SDK_INT >= 21 ? a.a(split[i8]) : b(split[i8]);
        }
        return a(localeArr);
    }

    public static j e() {
        return f4774b;
    }

    public static j i(LocaleList localeList) {
        return new j(new m(localeList));
    }

    public Locale d(int i8) {
        return this.f4775a.get(i8);
    }

    public boolean equals(Object obj) {
        return (obj instanceof j) && this.f4775a.equals(((j) obj).f4775a);
    }

    public boolean f() {
        return this.f4775a.isEmpty();
    }

    public int g() {
        return this.f4775a.size();
    }

    public String h() {
        return this.f4775a.a();
    }

    public int hashCode() {
        return this.f4775a.hashCode();
    }

    public String toString() {
        return this.f4775a.toString();
    }
}
