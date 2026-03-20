package com.google.android.gms.internal.measurement;

import java.util.Comparator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class s7 implements Comparator<zzij> {
    @Override // java.util.Comparator
    public final /* synthetic */ int compare(zzij zzijVar, zzij zzijVar2) {
        zzij zzijVar3 = zzijVar;
        zzij zzijVar4 = zzijVar2;
        w7 w7Var = (w7) zzijVar3.iterator();
        w7 w7Var2 = (w7) zzijVar4.iterator();
        while (w7Var.hasNext() && w7Var2.hasNext()) {
            int compareTo = Integer.valueOf(zzij.h(w7Var.zza())).compareTo(Integer.valueOf(zzij.h(w7Var2.zza())));
            if (compareTo != 0) {
                return compareTo;
            }
        }
        return Integer.valueOf(zzijVar3.v()).compareTo(Integer.valueOf(zzijVar4.v()));
    }
}
