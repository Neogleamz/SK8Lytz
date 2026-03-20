package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class q implements l, r {

    /* renamed from: a  reason: collision with root package name */
    private final Map<String, r> f12447a = new HashMap();

    @Override // com.google.android.gms.internal.measurement.r
    public final r a() {
        Map<String, r> map;
        String key;
        r a9;
        q qVar = new q();
        for (Map.Entry<String, r> entry : this.f12447a.entrySet()) {
            if (entry.getValue() instanceof l) {
                map = qVar.f12447a;
                key = entry.getKey();
                a9 = entry.getValue();
            } else {
                map = qVar.f12447a;
                key = entry.getKey();
                a9 = entry.getValue().a();
            }
            map.put(key, a9);
        }
        return qVar;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Boolean b() {
        return Boolean.TRUE;
    }

    public final List<String> c() {
        return new ArrayList(this.f12447a.keySet());
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Double d() {
        return Double.valueOf(Double.NaN);
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final String e() {
        return "[object Object]";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof q) {
            return this.f12447a.equals(((q) obj).f12447a);
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Iterator<r> f() {
        return o.b(this.f12447a);
    }

    @Override // com.google.android.gms.internal.measurement.r
    public r g(String str, g6 g6Var, List<r> list) {
        return "toString".equals(str) ? new t(toString()) : o.a(this, new t(str), g6Var, list);
    }

    @Override // com.google.android.gms.internal.measurement.l
    public final r h(String str) {
        return this.f12447a.containsKey(str) ? this.f12447a.get(str) : r.f12463r;
    }

    public int hashCode() {
        return this.f12447a.hashCode();
    }

    @Override // com.google.android.gms.internal.measurement.l
    public final boolean k(String str) {
        return this.f12447a.containsKey(str);
    }

    @Override // com.google.android.gms.internal.measurement.l
    public final void n(String str, r rVar) {
        if (rVar == null) {
            this.f12447a.remove(str);
        } else {
            this.f12447a.put(str, rVar);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if (!this.f12447a.isEmpty()) {
            for (String str : this.f12447a.keySet()) {
                sb.append(String.format("%s: %s,", str, this.f12447a.get(str)));
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        sb.append("}");
        return sb.toString();
    }
}
