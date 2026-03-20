package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Iterator;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Iterator it) {
        Objects.requireNonNull(it);
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }
}
