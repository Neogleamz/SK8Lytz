package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.p4;
import com.google.android.gms.internal.measurement.td;
import com.google.android.gms.internal.measurement.w4;
import com.google.android.gms.internal.measurement.x4;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class yb {

    /* renamed from: a  reason: collision with root package name */
    private String f17198a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f17199b;

    /* renamed from: c  reason: collision with root package name */
    private com.google.android.gms.internal.measurement.w4 f17200c;

    /* renamed from: d  reason: collision with root package name */
    private BitSet f17201d;

    /* renamed from: e  reason: collision with root package name */
    private BitSet f17202e;

    /* renamed from: f  reason: collision with root package name */
    private Map<Integer, Long> f17203f;

    /* renamed from: g  reason: collision with root package name */
    private Map<Integer, List<Long>> f17204g;

    /* renamed from: h  reason: collision with root package name */
    private final /* synthetic */ wb f17205h;

    private yb(wb wbVar, String str) {
        this.f17205h = wbVar;
        this.f17198a = str;
        this.f17199b = true;
        this.f17201d = new BitSet();
        this.f17202e = new BitSet();
        this.f17203f = new k0.a();
        this.f17204g = new k0.a();
    }

    private yb(wb wbVar, String str, com.google.android.gms.internal.measurement.w4 w4Var, BitSet bitSet, BitSet bitSet2, Map<Integer, Long> map, Map<Integer, Long> map2) {
        this.f17205h = wbVar;
        this.f17198a = str;
        this.f17201d = bitSet;
        this.f17202e = bitSet2;
        this.f17203f = map;
        this.f17204g = new k0.a();
        if (map2 != null) {
            for (Integer num : map2.keySet()) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(map2.get(num));
                this.f17204g.put(num, arrayList);
            }
        }
        this.f17199b = false;
        this.f17200c = w4Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ BitSet b(yb ybVar) {
        return ybVar.f17201d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v10, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r1v8, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r1v9, types: [java.lang.Iterable] */
    public final com.google.android.gms.internal.measurement.p4 a(int i8) {
        ArrayList arrayList;
        ?? arrayList2;
        p4.a N = com.google.android.gms.internal.measurement.p4.N();
        N.x(i8);
        N.A(this.f17199b);
        com.google.android.gms.internal.measurement.w4 w4Var = this.f17200c;
        if (w4Var != null) {
            N.z(w4Var);
        }
        w4.a E = com.google.android.gms.internal.measurement.w4.W().A(nb.M(this.f17201d)).E(nb.M(this.f17202e));
        if (this.f17203f == null) {
            arrayList = null;
        } else {
            arrayList = new ArrayList(this.f17203f.size());
            for (Integer num : this.f17203f.keySet()) {
                int intValue = num.intValue();
                Long l8 = this.f17203f.get(Integer.valueOf(intValue));
                if (l8 != null) {
                    arrayList.add((com.google.android.gms.internal.measurement.q4) ((com.google.android.gms.internal.measurement.x8) com.google.android.gms.internal.measurement.q4.M().x(intValue).y(l8.longValue()).n()));
                }
            }
        }
        if (arrayList != null) {
            E.y(arrayList);
        }
        if (this.f17204g == null) {
            arrayList2 = Collections.emptyList();
        } else {
            arrayList2 = new ArrayList(this.f17204g.size());
            for (Integer num2 : this.f17204g.keySet()) {
                x4.a x8 = com.google.android.gms.internal.measurement.x4.N().x(num2.intValue());
                List<Long> list = this.f17204g.get(num2);
                if (list != null) {
                    Collections.sort(list);
                    x8.y(list);
                }
                arrayList2.add((com.google.android.gms.internal.measurement.x4) ((com.google.android.gms.internal.measurement.x8) x8.n()));
            }
        }
        E.C(arrayList2);
        N.y(E);
        return (com.google.android.gms.internal.measurement.p4) ((com.google.android.gms.internal.measurement.x8) N.n());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void c(c cVar) {
        int a9 = cVar.a();
        Boolean bool = cVar.f16356c;
        if (bool != null) {
            this.f17202e.set(a9, bool.booleanValue());
        }
        Boolean bool2 = cVar.f16357d;
        if (bool2 != null) {
            this.f17201d.set(a9, bool2.booleanValue());
        }
        if (cVar.f16358e != null) {
            Long l8 = this.f17203f.get(Integer.valueOf(a9));
            long longValue = cVar.f16358e.longValue() / 1000;
            if (l8 == null || longValue > l8.longValue()) {
                this.f17203f.put(Integer.valueOf(a9), Long.valueOf(longValue));
            }
        }
        if (cVar.f16359f != null) {
            List<Long> list = this.f17204g.get(Integer.valueOf(a9));
            if (list == null) {
                list = new ArrayList<>();
                this.f17204g.put(Integer.valueOf(a9), list);
            }
            if (cVar.j()) {
                list.clear();
            }
            if (td.a() && this.f17205h.a().D(this.f17198a, c0.f16391k0) && cVar.i()) {
                list.clear();
            }
            if (!td.a() || !this.f17205h.a().D(this.f17198a, c0.f16391k0)) {
                list.add(Long.valueOf(cVar.f16359f.longValue() / 1000));
                return;
            }
            long longValue2 = cVar.f16359f.longValue() / 1000;
            if (list.contains(Long.valueOf(longValue2))) {
                return;
            }
            list.add(Long.valueOf(longValue2));
        }
    }
}
