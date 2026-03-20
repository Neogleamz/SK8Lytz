package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g implements l, r, Iterable<r> {

    /* renamed from: a  reason: collision with root package name */
    private final SortedMap<Integer, r> f12189a;

    /* renamed from: b  reason: collision with root package name */
    private final Map<String, r> f12190b;

    public g() {
        this.f12189a = new TreeMap();
        this.f12190b = new TreeMap();
    }

    public g(List<r> list) {
        this();
        if (list != null) {
            for (int i8 = 0; i8 < list.size(); i8++) {
                y(i8, list.get(i8));
            }
        }
    }

    public g(r... rVarArr) {
        this(Arrays.asList(rVarArr));
    }

    public final boolean A(int i8) {
        if (i8 < 0 || i8 > this.f12189a.lastKey().intValue()) {
            throw new IndexOutOfBoundsException("Out of bounds index: " + i8);
        }
        return this.f12189a.containsKey(Integer.valueOf(i8));
    }

    public final Iterator<Integer> C() {
        return this.f12189a.keySet().iterator();
    }

    public final List<r> D() {
        ArrayList arrayList = new ArrayList(u());
        for (int i8 = 0; i8 < u(); i8++) {
            arrayList.add(p(i8));
        }
        return arrayList;
    }

    public final void E() {
        this.f12189a.clear();
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final r a() {
        SortedMap<Integer, r> sortedMap;
        Integer key;
        r a9;
        g gVar = new g();
        for (Map.Entry<Integer, r> entry : this.f12189a.entrySet()) {
            if (entry.getValue() instanceof l) {
                sortedMap = gVar.f12189a;
                key = entry.getKey();
                a9 = entry.getValue();
            } else {
                sortedMap = gVar.f12189a;
                key = entry.getKey();
                a9 = entry.getValue().a();
            }
            sortedMap.put(key, a9);
        }
        return gVar;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Boolean b() {
        return Boolean.TRUE;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Double d() {
        return this.f12189a.size() == 1 ? p(0).d() : this.f12189a.size() <= 0 ? Double.valueOf(0.0d) : Double.valueOf(Double.NaN);
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final String e() {
        return toString();
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof g) {
            g gVar = (g) obj;
            if (u() != gVar.u()) {
                return false;
            }
            if (this.f12189a.isEmpty()) {
                return gVar.f12189a.isEmpty();
            }
            for (int intValue = this.f12189a.firstKey().intValue(); intValue <= this.f12189a.lastKey().intValue(); intValue++) {
                if (!p(intValue).equals(gVar.p(intValue))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Iterator<r> f() {
        return new f(this, this.f12189a.keySet().iterator(), this.f12190b.keySet().iterator());
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final r g(String str, g6 g6Var, List<r> list) {
        return "concat".equals(str) || "every".equals(str) || "filter".equals(str) || "forEach".equals(str) || "indexOf".equals(str) || "join".equals(str) || "lastIndexOf".equals(str) || "map".equals(str) || "pop".equals(str) || "push".equals(str) || "reduce".equals(str) || "reduceRight".equals(str) || "reverse".equals(str) || "shift".equals(str) || "slice".equals(str) || "some".equals(str) || "sort".equals(str) || "splice".equals(str) || "toString".equals(str) || "unshift".equals(str) ? g0.d(str, this, g6Var, list) : o.a(this, new t(str), g6Var, list);
    }

    @Override // com.google.android.gms.internal.measurement.l
    public final r h(String str) {
        r rVar;
        return "length".equals(str) ? new j(Double.valueOf(u())) : (!k(str) || (rVar = this.f12190b.get(str)) == null) ? r.f12463r : rVar;
    }

    public final int hashCode() {
        return this.f12189a.hashCode() * 31;
    }

    public final int i() {
        return this.f12189a.size();
    }

    @Override // java.lang.Iterable
    public final Iterator<r> iterator() {
        return new i(this);
    }

    @Override // com.google.android.gms.internal.measurement.l
    public final boolean k(String str) {
        return "length".equals(str) || this.f12190b.containsKey(str);
    }

    @Override // com.google.android.gms.internal.measurement.l
    public final void n(String str, r rVar) {
        if (rVar == null) {
            this.f12190b.remove(str);
        } else {
            this.f12190b.put(str, rVar);
        }
    }

    public final r p(int i8) {
        r rVar;
        if (i8 < u()) {
            return (!A(i8) || (rVar = this.f12189a.get(Integer.valueOf(i8))) == null) ? r.f12463r : rVar;
        }
        throw new IndexOutOfBoundsException("Attempting to get element outside of current array");
    }

    public final void q(int i8, r rVar) {
        if (i8 < 0) {
            throw new IllegalArgumentException("Invalid value index: " + i8);
        } else if (i8 >= u()) {
            y(i8, rVar);
        } else {
            for (int intValue = this.f12189a.lastKey().intValue(); intValue >= i8; intValue--) {
                r rVar2 = this.f12189a.get(Integer.valueOf(intValue));
                if (rVar2 != null) {
                    y(intValue + 1, rVar2);
                    this.f12189a.remove(Integer.valueOf(intValue));
                }
            }
            y(i8, rVar);
        }
    }

    public final void t(r rVar) {
        y(u(), rVar);
    }

    public final String toString() {
        return v(",");
    }

    public final int u() {
        if (this.f12189a.isEmpty()) {
            return 0;
        }
        return this.f12189a.lastKey().intValue() + 1;
    }

    public final String v(String str) {
        if (str == null) {
            str = BuildConfig.FLAVOR;
        }
        StringBuilder sb = new StringBuilder();
        if (!this.f12189a.isEmpty()) {
            for (int i8 = 0; i8 < u(); i8++) {
                r p8 = p(i8);
                sb.append(str);
                if (!(p8 instanceof y) && !(p8 instanceof p)) {
                    sb.append(p8.e());
                }
            }
            sb.delete(0, str.length());
        }
        return sb.toString();
    }

    public final void x(int i8) {
        int intValue = this.f12189a.lastKey().intValue();
        if (i8 > intValue || i8 < 0) {
            return;
        }
        this.f12189a.remove(Integer.valueOf(i8));
        if (i8 == intValue) {
            int i9 = i8 - 1;
            if (this.f12189a.containsKey(Integer.valueOf(i9)) || i9 < 0) {
                return;
            }
            this.f12189a.put(Integer.valueOf(i9), r.f12463r);
            return;
        }
        while (true) {
            i8++;
            if (i8 > this.f12189a.lastKey().intValue()) {
                return;
            }
            r rVar = this.f12189a.get(Integer.valueOf(i8));
            if (rVar != null) {
                this.f12189a.put(Integer.valueOf(i8 - 1), rVar);
                this.f12189a.remove(Integer.valueOf(i8));
            }
        }
    }

    public final void y(int i8, r rVar) {
        if (i8 > 32468) {
            throw new IllegalStateException("Array too large");
        }
        if (i8 < 0) {
            throw new IndexOutOfBoundsException("Out of bounds index: " + i8);
        } else if (rVar == null) {
            this.f12189a.remove(Integer.valueOf(i8));
        } else {
            this.f12189a.put(Integer.valueOf(i8), rVar);
        }
    }
}
