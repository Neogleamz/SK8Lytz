package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class ea implements ba {
    @Override // com.google.android.gms.internal.measurement.ba
    public final z9<?, ?> a(Object obj) {
        aa aaVar = (aa) obj;
        throw new NoSuchMethodError();
    }

    @Override // com.google.android.gms.internal.measurement.ba
    public final Object b(Object obj) {
        return zzla.c().f();
    }

    @Override // com.google.android.gms.internal.measurement.ba
    public final boolean c(Object obj) {
        return !((zzla) obj).k();
    }

    @Override // com.google.android.gms.internal.measurement.ba
    public final Map<?, ?> d(Object obj) {
        return (zzla) obj;
    }

    @Override // com.google.android.gms.internal.measurement.ba
    public final Map<?, ?> e(Object obj) {
        return (zzla) obj;
    }

    @Override // com.google.android.gms.internal.measurement.ba
    public final Object f(Object obj) {
        ((zzla) obj).j();
        return obj;
    }

    @Override // com.google.android.gms.internal.measurement.ba
    public final Object g(Object obj, Object obj2) {
        zzla zzlaVar = (zzla) obj;
        zzla zzlaVar2 = (zzla) obj2;
        if (!zzlaVar2.isEmpty()) {
            if (!zzlaVar.k()) {
                zzlaVar = zzlaVar.f();
            }
            zzlaVar.d(zzlaVar2);
        }
        return zzlaVar;
    }

    @Override // com.google.android.gms.internal.measurement.ba
    public final int h(int i8, Object obj, Object obj2) {
        zzla zzlaVar = (zzla) obj;
        aa aaVar = (aa) obj2;
        if (zzlaVar.isEmpty()) {
            return 0;
        }
        Iterator it = zzlaVar.entrySet().iterator();
        if (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            entry.getKey();
            entry.getValue();
            throw new NoSuchMethodError();
        }
        return 0;
    }
}
