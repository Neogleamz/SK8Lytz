package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d2 extends c2 {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.c2
    public final int a(Map.Entry entry) {
        return ((n2) entry.getKey()).f14816a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.c2
    public final g2 b(Object obj) {
        return ((l2) obj).zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.c2
    public final g2 c(Object obj) {
        return ((l2) obj).I();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.c2
    public final Object d(b2 b2Var, x3 x3Var, int i8) {
        return b2Var.b(x3Var, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.c2
    public final void e(Object obj) {
        ((l2) obj).zza.g();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.c2
    public final void f(y5 y5Var, Map.Entry entry) {
        n2 n2Var = (n2) entry.getKey();
        zzho zzhoVar = zzho.f15035b;
        switch (n2Var.f14817b.ordinal()) {
            case 0:
                y5Var.v(n2Var.f14816a, ((Double) entry.getValue()).doubleValue());
                return;
            case 1:
                y5Var.y(n2Var.f14816a, ((Float) entry.getValue()).floatValue());
                return;
            case 2:
                y5Var.m(n2Var.f14816a, ((Long) entry.getValue()).longValue());
                return;
            case 3:
                y5Var.I(n2Var.f14816a, ((Long) entry.getValue()).longValue());
                return;
            case 4:
                y5Var.t(n2Var.f14816a, ((Integer) entry.getValue()).intValue());
                return;
            case 5:
                y5Var.F(n2Var.f14816a, ((Long) entry.getValue()).longValue());
                return;
            case 6:
                y5Var.k(n2Var.f14816a, ((Integer) entry.getValue()).intValue());
                return;
            case 7:
                y5Var.l(n2Var.f14816a, ((Boolean) entry.getValue()).booleanValue());
                return;
            case 8:
                y5Var.g(n2Var.f14816a, (String) entry.getValue());
                return;
            case 9:
                y5Var.s(n2Var.f14816a, entry.getValue(), g4.a().b(entry.getValue().getClass()));
                return;
            case 10:
                y5Var.G(n2Var.f14816a, entry.getValue(), g4.a().b(entry.getValue().getClass()));
                return;
            case 11:
                y5Var.h(n2Var.f14816a, (zzdb) entry.getValue());
                return;
            case 12:
                y5Var.i(n2Var.f14816a, ((Integer) entry.getValue()).intValue());
                return;
            case 13:
                y5Var.t(n2Var.f14816a, ((Integer) entry.getValue()).intValue());
                return;
            case 14:
                y5Var.B(n2Var.f14816a, ((Integer) entry.getValue()).intValue());
                return;
            case 15:
                y5Var.q(n2Var.f14816a, ((Long) entry.getValue()).longValue());
                return;
            case 16:
                y5Var.E(n2Var.f14816a, ((Integer) entry.getValue()).intValue());
                return;
            case 17:
                y5Var.w(n2Var.f14816a, ((Long) entry.getValue()).longValue());
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.c2
    public final boolean g(x3 x3Var) {
        return x3Var instanceof l2;
    }
}
