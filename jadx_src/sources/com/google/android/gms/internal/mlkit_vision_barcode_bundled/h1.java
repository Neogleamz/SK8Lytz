package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.Comparator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h1 implements Comparator {
    @Override // java.util.Comparator
    public final /* synthetic */ int compare(Object obj, Object obj2) {
        zzdb zzdbVar = (zzdb) obj;
        zzdb zzdbVar2 = (zzdb) obj2;
        k1 it = zzdbVar.iterator();
        k1 it2 = zzdbVar2.iterator();
        while (it.hasNext() && it2.hasNext()) {
            int compareTo = Integer.valueOf(it.zza() & 255).compareTo(Integer.valueOf(it2.zza() & 255));
            if (compareTo != 0) {
                return compareTo;
            }
        }
        return Integer.valueOf(zzdbVar.i()).compareTo(Integer.valueOf(zzdbVar2.i()));
    }
}
