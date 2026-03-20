package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y implements r {
    @Override // com.google.android.gms.internal.measurement.r
    public final r a() {
        return r.f12463r;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Boolean b() {
        return Boolean.FALSE;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Double d() {
        return Double.valueOf(Double.NaN);
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final String e() {
        return "undefined";
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof y;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Iterator<r> f() {
        return null;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final r g(String str, g6 g6Var, List<r> list) {
        throw new IllegalStateException(String.format("Undefined has no function %s", str));
    }
}
