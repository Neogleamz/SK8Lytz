package com.google.android.gms.internal.measurement;

import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s0 extends z {
    @Override // com.google.android.gms.internal.measurement.z
    public final r b(String str, g6 g6Var, List<r> list) {
        if (str == null || str.isEmpty() || !g6Var.g(str)) {
            throw new IllegalArgumentException(String.format("Command not found: %s", str));
        }
        r c9 = g6Var.c(str);
        if (c9 instanceof m) {
            return ((m) c9).c(g6Var, list);
        }
        throw new IllegalArgumentException(String.format("Function %s is not defined", str));
    }
}
