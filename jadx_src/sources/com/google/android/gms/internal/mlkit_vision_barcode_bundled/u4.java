package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u4 extends e5 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public u4(int i8) {
        super(i8, null);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.e5
    public final void a() {
        if (!l()) {
            for (int i8 = 0; i8 < b(); i8++) {
                ((f2) i(i8).getKey()).g();
            }
            for (Map.Entry entry : c()) {
                ((f2) entry.getKey()).g();
            }
        }
        super.a();
    }
}
