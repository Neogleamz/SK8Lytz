package k5;

import android.os.SystemClock;
import android.util.Pair;
import b6.l0;
import com.google.common.collect.f1;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    private final Map<String, Long> f21028a;

    /* renamed from: b  reason: collision with root package name */
    private final Map<Integer, Long> f21029b;

    /* renamed from: c  reason: collision with root package name */
    private final Map<List<Pair<String, Integer>>, l5.b> f21030c;

    /* renamed from: d  reason: collision with root package name */
    private final Random f21031d;

    public b() {
        this(new Random());
    }

    b(Random random) {
        this.f21030c = new HashMap();
        this.f21031d = random;
        this.f21028a = new HashMap();
        this.f21029b = new HashMap();
    }

    private static <T> void b(T t8, long j8, Map<T, Long> map) {
        if (map.containsKey(t8)) {
            j8 = Math.max(j8, ((Long) l0.j(map.get(t8))).longValue());
        }
        map.put(t8, Long.valueOf(j8));
    }

    private List<l5.b> c(List<l5.b> list) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        h(elapsedRealtime, this.f21028a);
        h(elapsedRealtime, this.f21029b);
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < list.size(); i8++) {
            l5.b bVar = list.get(i8);
            if (!this.f21028a.containsKey(bVar.f21631b) && !this.f21029b.containsKey(Integer.valueOf(bVar.f21632c))) {
                arrayList.add(bVar);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int d(l5.b bVar, l5.b bVar2) {
        int compare = Integer.compare(bVar.f21632c, bVar2.f21632c);
        return compare != 0 ? compare : bVar.f21631b.compareTo(bVar2.f21631b);
    }

    public static int f(List<l5.b> list) {
        HashSet hashSet = new HashSet();
        for (int i8 = 0; i8 < list.size(); i8++) {
            hashSet.add(Integer.valueOf(list.get(i8).f21632c));
        }
        return hashSet.size();
    }

    private static <T> void h(long j8, Map<T, Long> map) {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<T, Long> entry : map.entrySet()) {
            if (entry.getValue().longValue() <= j8) {
                arrayList.add(entry.getKey());
            }
        }
        for (int i8 = 0; i8 < arrayList.size(); i8++) {
            map.remove(arrayList.get(i8));
        }
    }

    private l5.b k(List<l5.b> list) {
        int i8 = 0;
        for (int i9 = 0; i9 < list.size(); i9++) {
            i8 += list.get(i9).f21633d;
        }
        int nextInt = this.f21031d.nextInt(i8);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            l5.b bVar = list.get(i11);
            i10 += bVar.f21633d;
            if (nextInt < i10) {
                return bVar;
            }
        }
        return (l5.b) f1.f(list);
    }

    public void e(l5.b bVar, long j8) {
        long elapsedRealtime = SystemClock.elapsedRealtime() + j8;
        b(bVar.f21631b, elapsedRealtime, this.f21028a);
        int i8 = bVar.f21632c;
        if (i8 != Integer.MIN_VALUE) {
            b(Integer.valueOf(i8), elapsedRealtime, this.f21029b);
        }
    }

    public int g(List<l5.b> list) {
        HashSet hashSet = new HashSet();
        List<l5.b> c9 = c(list);
        for (int i8 = 0; i8 < c9.size(); i8++) {
            hashSet.add(Integer.valueOf(c9.get(i8).f21632c));
        }
        return hashSet.size();
    }

    public void i() {
        this.f21028a.clear();
        this.f21029b.clear();
        this.f21030c.clear();
    }

    public l5.b j(List<l5.b> list) {
        Object obj;
        List<l5.b> c9 = c(list);
        if (c9.size() >= 2) {
            Collections.sort(c9, a.a);
            ArrayList arrayList = new ArrayList();
            int i8 = c9.get(0).f21632c;
            int i9 = 0;
            while (true) {
                if (i9 >= c9.size()) {
                    break;
                }
                l5.b bVar = c9.get(i9);
                if (i8 == bVar.f21632c) {
                    arrayList.add(new Pair(bVar.f21631b, Integer.valueOf(bVar.f21633d)));
                    i9++;
                } else if (arrayList.size() == 1) {
                    obj = c9.get(0);
                }
            }
            l5.b bVar2 = this.f21030c.get(arrayList);
            if (bVar2 == null) {
                l5.b k8 = k(c9.subList(0, arrayList.size()));
                this.f21030c.put(arrayList, k8);
                return k8;
            }
            return bVar2;
        }
        obj = f1.e(c9, null);
        return (l5.b) obj;
    }
}
