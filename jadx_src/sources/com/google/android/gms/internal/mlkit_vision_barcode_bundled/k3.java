package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class k3 extends m3 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ k3(i3 i3Var) {
        super(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.m3
    public final void a(Object obj, long j8) {
        ((x2) s5.k(obj, j8)).zzb();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.m3
    public final void b(Object obj, Object obj2, long j8) {
        x2 x2Var = (x2) s5.k(obj, j8);
        x2 x2Var2 = (x2) s5.k(obj2, j8);
        int size = x2Var.size();
        int size2 = x2Var2.size();
        if (size > 0 && size2 > 0) {
            if (!x2Var.a()) {
                x2Var = x2Var.z(size2 + size);
            }
            x2Var.addAll(x2Var2);
        }
        if (size > 0) {
            x2Var2 = x2Var;
        }
        s5.x(obj, j8, x2Var2);
    }
}
