package com.google.android.gms.internal.measurement;

import java.util.Collections;
import java.util.TreeMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    private final TreeMap<Integer, s> f12083a = new TreeMap<>();

    /* renamed from: b  reason: collision with root package name */
    private final TreeMap<Integer, s> f12084b = new TreeMap<>();

    private static int a(g6 g6Var, s sVar, r rVar) {
        r c9 = sVar.c(g6Var, Collections.singletonList(rVar));
        if (c9 instanceof j) {
            return e5.i(c9.d().doubleValue());
        }
        return -1;
    }

    public final void b(g6 g6Var, d dVar) {
        zb zbVar = new zb(dVar);
        for (Integer num : this.f12083a.keySet()) {
            e eVar = (e) dVar.d().clone();
            int a9 = a(g6Var, this.f12083a.get(num), zbVar);
            if (a9 == 2 || a9 == -1) {
                dVar.e(eVar);
            }
        }
        for (Integer num2 : this.f12084b.keySet()) {
            a(g6Var, this.f12084b.get(num2), zbVar);
        }
    }

    public final void c(String str, int i8, s sVar, String str2) {
        TreeMap<Integer, s> treeMap;
        if ("create".equals(str2)) {
            treeMap = this.f12084b;
        } else if (!"edit".equals(str2)) {
            throw new IllegalStateException("Unknown callback type: " + str2);
        } else {
            treeMap = this.f12083a;
        }
        if (treeMap.containsKey(Integer.valueOf(i8))) {
            i8 = treeMap.lastKey().intValue() + 1;
        }
        treeMap.put(Integer.valueOf(i8), sVar);
    }
}
