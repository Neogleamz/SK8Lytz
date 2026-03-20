package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class s3 {
    public static final int a(int i8, Object obj, Object obj2) {
        zzfi zzfiVar = (zzfi) obj;
        r3 r3Var = (r3) obj2;
        if (zzfiVar.isEmpty()) {
            return 0;
        }
        Iterator it = zzfiVar.entrySet().iterator();
        if (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            entry.getKey();
            entry.getValue();
            throw null;
        }
        return 0;
    }

    public static final Object b(Object obj, Object obj2) {
        zzfi zzfiVar = (zzfi) obj;
        zzfi zzfiVar2 = (zzfi) obj2;
        if (!zzfiVar2.isEmpty()) {
            if (!zzfiVar.j()) {
                zzfiVar = zzfiVar.c();
            }
            zzfiVar.f(zzfiVar2);
        }
        return zzfiVar;
    }
}
