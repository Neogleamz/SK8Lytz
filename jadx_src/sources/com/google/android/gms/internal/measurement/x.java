package com.google.android.gms.internal.measurement;

import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x extends z {
    public x() {
        this.f12724a.add(zzbv.BITWISE_AND);
        this.f12724a.add(zzbv.BITWISE_LEFT_SHIFT);
        this.f12724a.add(zzbv.BITWISE_NOT);
        this.f12724a.add(zzbv.BITWISE_OR);
        this.f12724a.add(zzbv.BITWISE_RIGHT_SHIFT);
        this.f12724a.add(zzbv.BITWISE_UNSIGNED_RIGHT_SHIFT);
        this.f12724a.add(zzbv.BITWISE_XOR);
    }

    @Override // com.google.android.gms.internal.measurement.z
    public final r b(String str, g6 g6Var, List<r> list) {
        switch (a0.f12054a[e5.c(str).ordinal()]) {
            case 1:
                e5.f(zzbv.BITWISE_AND, 2, list);
                return new j(Double.valueOf(e5.i(g6Var.b(list.get(0)).d().doubleValue()) & e5.i(g6Var.b(list.get(1)).d().doubleValue())));
            case 2:
                e5.f(zzbv.BITWISE_LEFT_SHIFT, 2, list);
                return new j(Double.valueOf(e5.i(g6Var.b(list.get(0)).d().doubleValue()) << ((int) (e5.m(g6Var.b(list.get(1)).d().doubleValue()) & 31))));
            case 3:
                e5.f(zzbv.BITWISE_NOT, 1, list);
                return new j(Double.valueOf(~e5.i(g6Var.b(list.get(0)).d().doubleValue())));
            case 4:
                e5.f(zzbv.BITWISE_OR, 2, list);
                return new j(Double.valueOf(e5.i(g6Var.b(list.get(0)).d().doubleValue()) | e5.i(g6Var.b(list.get(1)).d().doubleValue())));
            case 5:
                e5.f(zzbv.BITWISE_RIGHT_SHIFT, 2, list);
                return new j(Double.valueOf(e5.i(g6Var.b(list.get(0)).d().doubleValue()) >> ((int) (e5.m(g6Var.b(list.get(1)).d().doubleValue()) & 31))));
            case 6:
                e5.f(zzbv.BITWISE_UNSIGNED_RIGHT_SHIFT, 2, list);
                return new j(Double.valueOf(e5.m(g6Var.b(list.get(0)).d().doubleValue()) >>> ((int) (e5.m(g6Var.b(list.get(1)).d().doubleValue()) & 31))));
            case 7:
                e5.f(zzbv.BITWISE_XOR, 2, list);
                return new j(Double.valueOf(e5.i(g6Var.b(list.get(0)).d().doubleValue()) ^ e5.i(g6Var.b(list.get(1)).d().doubleValue())));
            default:
                return super.a(str);
        }
    }
}
