package com.google.android.gms.internal.measurement;

import java.util.Collections;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class cb extends ya {
    /* JADX INFO: Access modifiers changed from: package-private */
    public cb(int i8) {
        super(i8);
    }

    @Override // com.google.android.gms.internal.measurement.ya
    public final void o() {
        if (!q()) {
            for (int i8 = 0; i8 < a(); i8++) {
                Map.Entry j8 = j(i8);
                if (((q8) j8.getKey()).d()) {
                    j8.setValue(Collections.unmodifiableList((List) j8.getValue()));
                }
            }
            for (Map.Entry entry : i()) {
                if (((q8) entry.getKey()).d()) {
                    entry.setValue(Collections.unmodifiableList((List) entry.getValue()));
                }
            }
        }
        super.o();
    }
}
