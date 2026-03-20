package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g8 {
    public static r a(zzgb$zzd zzgb_zzd) {
        if (zzgb_zzd == null) {
            return r.f12463r;
        }
        int i8 = h7.f12226a[zzgb_zzd.I().ordinal()];
        if (i8 == 1) {
            return zzgb_zzd.Q() ? new t(zzgb_zzd.L()) : r.M;
        } else if (i8 == 2) {
            return zzgb_zzd.P() ? new j(Double.valueOf(zzgb_zzd.H())) : new j(null);
        } else if (i8 == 3) {
            return zzgb_zzd.O() ? new h(Boolean.valueOf(zzgb_zzd.N())) : new h(null);
        } else if (i8 != 4) {
            if (i8 != 5) {
                String valueOf = String.valueOf(zzgb_zzd);
                throw new IllegalStateException("Invalid entity: " + valueOf);
            }
            throw new IllegalArgumentException("Unknown type found. Cannot convert entity");
        } else {
            List<zzgb$zzd> M = zzgb_zzd.M();
            ArrayList arrayList = new ArrayList();
            for (zzgb$zzd zzgb_zzd2 : M) {
                arrayList.add(a(zzgb_zzd2));
            }
            return new u(zzgb_zzd.K(), arrayList);
        }
    }

    public static r b(Object obj) {
        if (obj == null) {
            return r.f12464s;
        }
        if (obj instanceof String) {
            return new t((String) obj);
        }
        if (obj instanceof Double) {
            return new j((Double) obj);
        }
        if (obj instanceof Long) {
            return new j(Double.valueOf(((Long) obj).doubleValue()));
        }
        if (obj instanceof Integer) {
            return new j(Double.valueOf(((Integer) obj).doubleValue()));
        }
        if (obj instanceof Boolean) {
            return new h((Boolean) obj);
        }
        if (!(obj instanceof Map)) {
            if (obj instanceof List) {
                g gVar = new g();
                for (Object obj2 : (List) obj) {
                    gVar.t(b(obj2));
                }
                return gVar;
            }
            throw new IllegalArgumentException("Invalid value type");
        }
        q qVar = new q();
        Map map = (Map) obj;
        for (Object obj3 : map.keySet()) {
            r b9 = b(map.get(obj3));
            if (obj3 != null) {
                if (!(obj3 instanceof String)) {
                    obj3 = obj3.toString();
                }
                qVar.n((String) obj3, b9);
            }
        }
        return qVar;
    }
}
