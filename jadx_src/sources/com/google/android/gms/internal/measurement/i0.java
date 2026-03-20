package com.google.android.gms.internal.measurement;

import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i0 extends z {
    /* JADX INFO: Access modifiers changed from: protected */
    public i0() {
        this.f12724a.add(zzbv.AND);
        this.f12724a.add(zzbv.NOT);
        this.f12724a.add(zzbv.OR);
    }

    @Override // com.google.android.gms.internal.measurement.z
    public final r b(String str, g6 g6Var, List<r> list) {
        int i8 = l0.f12291a[e5.c(str).ordinal()];
        if (i8 == 1) {
            e5.f(zzbv.AND, 2, list);
            r b9 = g6Var.b(list.get(0));
            if (!b9.b().booleanValue()) {
                return b9;
            }
        } else if (i8 == 2) {
            e5.f(zzbv.NOT, 1, list);
            return new h(Boolean.valueOf(!g6Var.b(list.get(0)).b().booleanValue()));
        } else if (i8 != 3) {
            return super.a(str);
        } else {
            e5.f(zzbv.OR, 2, list);
            r b10 = g6Var.b(list.get(0));
            if (b10.b().booleanValue()) {
                return b10;
            }
        }
        return g6Var.b(list.get(1));
    }
}
