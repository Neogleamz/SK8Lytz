package androidx.core.os;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class k implements l {

    /* renamed from: c  reason: collision with root package name */
    private static final Locale[] f4776c = new Locale[0];

    /* renamed from: d  reason: collision with root package name */
    private static final Locale f4777d = new Locale("en", "XA");

    /* renamed from: e  reason: collision with root package name */
    private static final Locale f4778e = new Locale("ar", "XB");

    /* renamed from: f  reason: collision with root package name */
    private static final Locale f4779f = j.b("en-Latn");

    /* renamed from: a  reason: collision with root package name */
    private final Locale[] f4780a;

    /* renamed from: b  reason: collision with root package name */
    private final String f4781b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k(Locale... localeArr) {
        String sb;
        if (localeArr.length == 0) {
            this.f4780a = f4776c;
            sb = BuildConfig.FLAVOR;
        } else {
            ArrayList arrayList = new ArrayList();
            HashSet hashSet = new HashSet();
            StringBuilder sb2 = new StringBuilder();
            for (int i8 = 0; i8 < localeArr.length; i8++) {
                Locale locale = localeArr[i8];
                if (locale == null) {
                    throw new NullPointerException("list[" + i8 + "] is null");
                }
                if (!hashSet.contains(locale)) {
                    Locale locale2 = (Locale) locale.clone();
                    arrayList.add(locale2);
                    c(sb2, locale2);
                    if (i8 < localeArr.length - 1) {
                        sb2.append(',');
                    }
                    hashSet.add(locale2);
                }
            }
            this.f4780a = (Locale[]) arrayList.toArray(new Locale[0]);
            sb = sb2.toString();
        }
        this.f4781b = sb;
    }

    static void c(StringBuilder sb, Locale locale) {
        sb.append(locale.getLanguage());
        String country = locale.getCountry();
        if (country == null || country.isEmpty()) {
            return;
        }
        sb.append('-');
        sb.append(locale.getCountry());
    }

    @Override // androidx.core.os.l
    public String a() {
        return this.f4781b;
    }

    @Override // androidx.core.os.l
    public Object b() {
        return null;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof k)) {
            return false;
        }
        Locale[] localeArr = ((k) obj).f4780a;
        if (this.f4780a.length != localeArr.length) {
            return false;
        }
        int i8 = 0;
        while (true) {
            Locale[] localeArr2 = this.f4780a;
            if (i8 >= localeArr2.length) {
                return true;
            }
            if (!localeArr2[i8].equals(localeArr[i8])) {
                return false;
            }
            i8++;
        }
    }

    @Override // androidx.core.os.l
    public Locale get(int i8) {
        if (i8 >= 0) {
            Locale[] localeArr = this.f4780a;
            if (i8 < localeArr.length) {
                return localeArr[i8];
            }
        }
        return null;
    }

    public int hashCode() {
        int i8 = 1;
        for (Locale locale : this.f4780a) {
            i8 = (i8 * 31) + locale.hashCode();
        }
        return i8;
    }

    @Override // androidx.core.os.l
    public boolean isEmpty() {
        return this.f4780a.length == 0;
    }

    @Override // androidx.core.os.l
    public int size() {
        return this.f4780a.length;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int i8 = 0;
        while (true) {
            Locale[] localeArr = this.f4780a;
            if (i8 >= localeArr.length) {
                sb.append("]");
                return sb.toString();
            }
            sb.append(localeArr[i8]);
            if (i8 < this.f4780a.length - 1) {
                sb.append(',');
            }
            i8++;
        }
    }
}
