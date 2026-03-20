package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final /* synthetic */ class o {
    public static r a(l lVar, r rVar, g6 g6Var, List<r> list) {
        if (lVar.k(rVar.e())) {
            r h8 = lVar.h(rVar.e());
            if (h8 instanceof m) {
                return ((m) h8).c(g6Var, list);
            }
            throw new IllegalArgumentException(String.format("%s is not a function", rVar.e()));
        } else if ("hasOwnProperty".equals(rVar.e())) {
            e5.g("hasOwnProperty", 1, list);
            return lVar.k(g6Var.b(list.get(0)).e()) ? r.I : r.J;
        } else {
            throw new IllegalArgumentException(String.format("Object has no function %s", rVar.e()));
        }
    }

    public static Iterator<r> b(Map<String, r> map) {
        return new n(map.keySet().iterator());
    }
}
