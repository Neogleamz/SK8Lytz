package w5;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;
import android.util.Base64;
import android.util.Pair;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import p5.b;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: a  reason: collision with root package name */
    public final String f23623a;

    /* renamed from: b  reason: collision with root package name */
    public final String f23624b;

    /* renamed from: c  reason: collision with root package name */
    public final boolean f23625c;

    /* renamed from: d  reason: collision with root package name */
    public final long f23626d;

    /* renamed from: e  reason: collision with root package name */
    public final long f23627e;

    /* renamed from: f  reason: collision with root package name */
    public final g f23628f;

    /* renamed from: g  reason: collision with root package name */
    private final String[] f23629g;

    /* renamed from: h  reason: collision with root package name */
    public final String f23630h;

    /* renamed from: i  reason: collision with root package name */
    public final String f23631i;

    /* renamed from: j  reason: collision with root package name */
    public final d f23632j;

    /* renamed from: k  reason: collision with root package name */
    private final HashMap<String, Integer> f23633k;

    /* renamed from: l  reason: collision with root package name */
    private final HashMap<String, Integer> f23634l;

    /* renamed from: m  reason: collision with root package name */
    private List<d> f23635m;

    private d(String str, String str2, long j8, long j9, g gVar, String[] strArr, String str3, String str4, d dVar) {
        this.f23623a = str;
        this.f23624b = str2;
        this.f23631i = str4;
        this.f23628f = gVar;
        this.f23629g = strArr;
        this.f23625c = str2 != null;
        this.f23626d = j8;
        this.f23627e = j9;
        this.f23630h = (String) b6.a.e(str3);
        this.f23632j = dVar;
        this.f23633k = new HashMap<>();
        this.f23634l = new HashMap<>();
    }

    private void b(Map<String, g> map, b.C0196b c0196b, int i8, int i9, int i10) {
        g f5 = f.f(this.f23628f, this.f23629g, map);
        SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) c0196b.e();
        if (spannableStringBuilder == null) {
            spannableStringBuilder = new SpannableStringBuilder();
            c0196b.o(spannableStringBuilder);
        }
        SpannableStringBuilder spannableStringBuilder2 = spannableStringBuilder;
        if (f5 != null) {
            f.a(spannableStringBuilder2, i8, i9, f5, this.f23632j, map, i10);
            if ("p".equals(this.f23623a)) {
                if (f5.k() != Float.MAX_VALUE) {
                    c0196b.m((f5.k() * (-90.0f)) / 100.0f);
                }
                if (f5.m() != null) {
                    c0196b.p(f5.m());
                }
                if (f5.h() != null) {
                    c0196b.j(f5.h());
                }
            }
        }
    }

    public static d c(String str, long j8, long j9, g gVar, String[] strArr, String str2, String str3, d dVar) {
        return new d(str, null, j8, j9, gVar, strArr, str2, str3, dVar);
    }

    public static d d(String str) {
        return new d(null, f.b(str), -9223372036854775807L, -9223372036854775807L, null, null, BuildConfig.FLAVOR, null, null);
    }

    private static void e(SpannableStringBuilder spannableStringBuilder) {
        a[] aVarArr;
        for (a aVar : (a[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), a.class)) {
            spannableStringBuilder.replace(spannableStringBuilder.getSpanStart(aVar), spannableStringBuilder.getSpanEnd(aVar), BuildConfig.FLAVOR);
        }
        for (int i8 = 0; i8 < spannableStringBuilder.length(); i8++) {
            if (spannableStringBuilder.charAt(i8) == ' ') {
                int i9 = i8 + 1;
                int i10 = i9;
                while (i10 < spannableStringBuilder.length() && spannableStringBuilder.charAt(i10) == ' ') {
                    i10++;
                }
                int i11 = i10 - i9;
                if (i11 > 0) {
                    spannableStringBuilder.delete(i8, i11 + i8);
                }
            }
        }
        if (spannableStringBuilder.length() > 0 && spannableStringBuilder.charAt(0) == ' ') {
            spannableStringBuilder.delete(0, 1);
        }
        for (int i12 = 0; i12 < spannableStringBuilder.length() - 1; i12++) {
            if (spannableStringBuilder.charAt(i12) == '\n') {
                int i13 = i12 + 1;
                if (spannableStringBuilder.charAt(i13) == ' ') {
                    spannableStringBuilder.delete(i13, i12 + 2);
                }
            }
        }
        if (spannableStringBuilder.length() > 0 && spannableStringBuilder.charAt(spannableStringBuilder.length() - 1) == ' ') {
            spannableStringBuilder.delete(spannableStringBuilder.length() - 1, spannableStringBuilder.length());
        }
        for (int i14 = 0; i14 < spannableStringBuilder.length() - 1; i14++) {
            if (spannableStringBuilder.charAt(i14) == ' ') {
                int i15 = i14 + 1;
                if (spannableStringBuilder.charAt(i15) == '\n') {
                    spannableStringBuilder.delete(i14, i15);
                }
            }
        }
        if (spannableStringBuilder.length() <= 0 || spannableStringBuilder.charAt(spannableStringBuilder.length() - 1) != '\n') {
            return;
        }
        spannableStringBuilder.delete(spannableStringBuilder.length() - 1, spannableStringBuilder.length());
    }

    private void i(TreeSet<Long> treeSet, boolean z4) {
        boolean equals = "p".equals(this.f23623a);
        boolean equals2 = "div".equals(this.f23623a);
        if (z4 || equals || (equals2 && this.f23631i != null)) {
            long j8 = this.f23626d;
            if (j8 != -9223372036854775807L) {
                treeSet.add(Long.valueOf(j8));
            }
            long j9 = this.f23627e;
            if (j9 != -9223372036854775807L) {
                treeSet.add(Long.valueOf(j9));
            }
        }
        if (this.f23635m == null) {
            return;
        }
        for (int i8 = 0; i8 < this.f23635m.size(); i8++) {
            this.f23635m.get(i8).i(treeSet, z4 || equals);
        }
    }

    private static SpannableStringBuilder k(String str, Map<String, b.C0196b> map) {
        if (!map.containsKey(str)) {
            b.C0196b c0196b = new b.C0196b();
            c0196b.o(new SpannableStringBuilder());
            map.put(str, c0196b);
        }
        return (SpannableStringBuilder) b6.a.e(map.get(str).e());
    }

    private void n(long j8, String str, List<Pair<String, String>> list) {
        if (!BuildConfig.FLAVOR.equals(this.f23630h)) {
            str = this.f23630h;
        }
        if (m(j8) && "div".equals(this.f23623a) && this.f23631i != null) {
            list.add(new Pair<>(str, this.f23631i));
            return;
        }
        for (int i8 = 0; i8 < g(); i8++) {
            f(i8).n(j8, str, list);
        }
    }

    private void o(long j8, Map<String, g> map, Map<String, e> map2, String str, Map<String, b.C0196b> map3) {
        int i8;
        if (m(j8)) {
            String str2 = BuildConfig.FLAVOR.equals(this.f23630h) ? str : this.f23630h;
            Iterator<Map.Entry<String, Integer>> it = this.f23634l.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry<String, Integer> next = it.next();
                String key = next.getKey();
                int intValue = this.f23633k.containsKey(key) ? this.f23633k.get(key).intValue() : 0;
                int intValue2 = next.getValue().intValue();
                if (intValue != intValue2) {
                    b(map, (b.C0196b) b6.a.e(map3.get(key)), intValue, intValue2, ((e) b6.a.e(map2.get(str2))).f23645j);
                }
            }
            while (i8 < g()) {
                f(i8).o(j8, map, map2, str2, map3);
                i8++;
            }
        }
    }

    private void p(long j8, boolean z4, String str, Map<String, b.C0196b> map) {
        this.f23633k.clear();
        this.f23634l.clear();
        if ("metadata".equals(this.f23623a)) {
            return;
        }
        if (!BuildConfig.FLAVOR.equals(this.f23630h)) {
            str = this.f23630h;
        }
        if (this.f23625c && z4) {
            k(str, map).append((CharSequence) b6.a.e(this.f23624b));
        } else if ("br".equals(this.f23623a) && z4) {
            k(str, map).append('\n');
        } else if (m(j8)) {
            for (Map.Entry<String, b.C0196b> entry : map.entrySet()) {
                this.f23633k.put(entry.getKey(), Integer.valueOf(((CharSequence) b6.a.e(entry.getValue().e())).length()));
            }
            boolean equals = "p".equals(this.f23623a);
            for (int i8 = 0; i8 < g(); i8++) {
                f(i8).p(j8, z4 || equals, str, map);
            }
            if (equals) {
                f.c(k(str, map));
            }
            for (Map.Entry<String, b.C0196b> entry2 : map.entrySet()) {
                this.f23634l.put(entry2.getKey(), Integer.valueOf(((CharSequence) b6.a.e(entry2.getValue().e())).length()));
            }
        }
    }

    public void a(d dVar) {
        if (this.f23635m == null) {
            this.f23635m = new ArrayList();
        }
        this.f23635m.add(dVar);
    }

    public d f(int i8) {
        List<d> list = this.f23635m;
        if (list != null) {
            return list.get(i8);
        }
        throw new IndexOutOfBoundsException();
    }

    public int g() {
        List<d> list = this.f23635m;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public List<p5.b> h(long j8, Map<String, g> map, Map<String, e> map2, Map<String, String> map3) {
        List<Pair<String, String>> arrayList = new ArrayList<>();
        n(j8, this.f23630h, arrayList);
        TreeMap treeMap = new TreeMap();
        p(j8, false, this.f23630h, treeMap);
        o(j8, map, map2, this.f23630h, treeMap);
        ArrayList arrayList2 = new ArrayList();
        for (Pair<String, String> pair : arrayList) {
            String str = map3.get(pair.second);
            if (str != null) {
                byte[] decode = Base64.decode(str, 0);
                Bitmap decodeByteArray = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                e eVar = (e) b6.a.e(map2.get(pair.first));
                arrayList2.add(new b.C0196b().f(decodeByteArray).k(eVar.f23637b).l(0).h(eVar.f23638c, 0).i(eVar.f23640e).n(eVar.f23641f).g(eVar.f23642g).r(eVar.f23645j).a());
            }
        }
        for (Map.Entry entry : treeMap.entrySet()) {
            e eVar2 = (e) b6.a.e(map2.get(entry.getKey()));
            b.C0196b c0196b = (b.C0196b) entry.getValue();
            e((SpannableStringBuilder) b6.a.e(c0196b.e()));
            c0196b.h(eVar2.f23638c, eVar2.f23639d);
            c0196b.i(eVar2.f23640e);
            c0196b.k(eVar2.f23637b);
            c0196b.n(eVar2.f23641f);
            c0196b.q(eVar2.f23644i, eVar2.f23643h);
            c0196b.r(eVar2.f23645j);
            arrayList2.add(c0196b.a());
        }
        return arrayList2;
    }

    public long[] j() {
        TreeSet<Long> treeSet = new TreeSet<>();
        int i8 = 0;
        i(treeSet, false);
        long[] jArr = new long[treeSet.size()];
        Iterator<Long> it = treeSet.iterator();
        while (it.hasNext()) {
            jArr[i8] = it.next().longValue();
            i8++;
        }
        return jArr;
    }

    public String[] l() {
        return this.f23629g;
    }

    public boolean m(long j8) {
        long j9 = this.f23626d;
        return (j9 == -9223372036854775807L && this.f23627e == -9223372036854775807L) || (j9 <= j8 && this.f23627e == -9223372036854775807L) || ((j9 == -9223372036854775807L && j8 < this.f23627e) || (j9 <= j8 && j8 < this.f23627e));
    }
}
