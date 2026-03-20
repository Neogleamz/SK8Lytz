package com.google.android.gms.internal.measurement;

import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q0 extends z {
    /* JADX INFO: Access modifiers changed from: protected */
    public q0() {
        this.f12724a.add(zzbv.ADD);
        this.f12724a.add(zzbv.DIVIDE);
        this.f12724a.add(zzbv.MODULUS);
        this.f12724a.add(zzbv.MULTIPLY);
        this.f12724a.add(zzbv.NEGATE);
        this.f12724a.add(zzbv.POST_DECREMENT);
        this.f12724a.add(zzbv.POST_INCREMENT);
        this.f12724a.add(zzbv.PRE_DECREMENT);
        this.f12724a.add(zzbv.PRE_INCREMENT);
        this.f12724a.add(zzbv.SUBTRACT);
    }

    @Override // com.google.android.gms.internal.measurement.z
    public final r b(String str, g6 g6Var, List<r> list) {
        switch (t0.f12515a[e5.c(str).ordinal()]) {
            case 1:
                e5.f(zzbv.ADD, 2, list);
                r b9 = g6Var.b(list.get(0));
                r b10 = g6Var.b(list.get(1));
                if ((b9 instanceof l) || (b9 instanceof t) || (b10 instanceof l) || (b10 instanceof t)) {
                    String e8 = b9.e();
                    String e9 = b10.e();
                    return new t(e8 + e9);
                }
                return new j(Double.valueOf(b9.d().doubleValue() + b10.d().doubleValue()));
            case 2:
                e5.f(zzbv.DIVIDE, 2, list);
                return new j(Double.valueOf(g6Var.b(list.get(0)).d().doubleValue() / g6Var.b(list.get(1)).d().doubleValue()));
            case 3:
                e5.f(zzbv.MODULUS, 2, list);
                return new j(Double.valueOf(g6Var.b(list.get(0)).d().doubleValue() % g6Var.b(list.get(1)).d().doubleValue()));
            case 4:
                e5.f(zzbv.MULTIPLY, 2, list);
                return new j(Double.valueOf(g6Var.b(list.get(0)).d().doubleValue() * g6Var.b(list.get(1)).d().doubleValue()));
            case 5:
                e5.f(zzbv.NEGATE, 1, list);
                return new j(Double.valueOf(g6Var.b(list.get(0)).d().doubleValue() * (-1.0d)));
            case 6:
            case 7:
                e5.g(str, 2, list);
                r b11 = g6Var.b(list.get(0));
                g6Var.b(list.get(1));
                return b11;
            case 8:
            case 9:
                e5.g(str, 1, list);
                return g6Var.b(list.get(0));
            case 10:
                e5.f(zzbv.SUBTRACT, 2, list);
                return new j(Double.valueOf(g6Var.b(list.get(0)).d().doubleValue() + new j(Double.valueOf(g6Var.b(list.get(1)).d().doubleValue() * (-1.0d))).d().doubleValue()));
            default:
                return super.a(str);
        }
    }
}
