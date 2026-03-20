package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k2 extends j2 implements y3 {
    /* JADX INFO: Access modifiers changed from: protected */
    public k2(l2 l2Var) {
        super(l2Var);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.j2
    public final void r() {
        super.r();
        if (((l2) this.f14798b).zza != g2.d()) {
            l2 l2Var = (l2) this.f14798b;
            l2Var.zza = l2Var.zza.clone();
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.j2, com.google.android.gms.internal.mlkit_vision_barcode_bundled.w3
    /* renamed from: s */
    public final l2 u() {
        p2 u8;
        if (((l2) this.f14798b).E()) {
            ((l2) this.f14798b).zza.g();
            u8 = super.u();
        } else {
            u8 = this.f14798b;
        }
        return (l2) u8;
    }
}
