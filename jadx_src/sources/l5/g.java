package l5;

import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g {

    /* renamed from: a  reason: collision with root package name */
    public final String f21669a;

    /* renamed from: b  reason: collision with root package name */
    public final long f21670b;

    /* renamed from: c  reason: collision with root package name */
    public final List<a> f21671c;

    /* renamed from: d  reason: collision with root package name */
    public final List<f> f21672d;

    /* renamed from: e  reason: collision with root package name */
    public final e f21673e;

    public g(String str, long j8, List<a> list, List<f> list2) {
        this(str, j8, list, list2, null);
    }

    public g(String str, long j8, List<a> list, List<f> list2, e eVar) {
        this.f21669a = str;
        this.f21670b = j8;
        this.f21671c = Collections.unmodifiableList(list);
        this.f21672d = Collections.unmodifiableList(list2);
        this.f21673e = eVar;
    }

    public int a(int i8) {
        int size = this.f21671c.size();
        for (int i9 = 0; i9 < size; i9++) {
            if (this.f21671c.get(i9).f21625b == i8) {
                return i9;
            }
        }
        return -1;
    }
}
