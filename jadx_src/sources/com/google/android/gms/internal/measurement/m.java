package com.google.android.gms.internal.measurement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class m implements l, r {

    /* renamed from: a  reason: collision with root package name */
    protected final String f12328a;

    /* renamed from: b  reason: collision with root package name */
    protected final Map<String, r> f12329b = new HashMap();

    public m(String str) {
        this.f12328a = str;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public r a() {
        return this;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Boolean b() {
        return Boolean.TRUE;
    }

    public abstract r c(g6 g6Var, List<r> list);

    @Override // com.google.android.gms.internal.measurement.r
    public final Double d() {
        return Double.valueOf(Double.NaN);
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final String e() {
        return this.f12328a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof m) {
            m mVar = (m) obj;
            String str = this.f12328a;
            if (str != null) {
                return str.equals(mVar.f12328a);
            }
            return false;
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Iterator<r> f() {
        return o.b(this.f12329b);
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final r g(String str, g6 g6Var, List<r> list) {
        return "toString".equals(str) ? new t(this.f12328a) : o.a(this, new t(str), g6Var, list);
    }

    @Override // com.google.android.gms.internal.measurement.l
    public final r h(String str) {
        return this.f12329b.containsKey(str) ? this.f12329b.get(str) : r.f12463r;
    }

    public int hashCode() {
        String str = this.f12328a;
        if (str != null) {
            return str.hashCode();
        }
        return 0;
    }

    public final String i() {
        return this.f12328a;
    }

    @Override // com.google.android.gms.internal.measurement.l
    public final boolean k(String str) {
        return this.f12329b.containsKey(str);
    }

    @Override // com.google.android.gms.internal.measurement.l
    public final void n(String str, r rVar) {
        if (rVar == null) {
            this.f12329b.remove(str);
        } else {
            this.f12329b.put(str, rVar);
        }
    }
}
