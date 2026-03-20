package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.google.android.gms.internal.mlkit_vision_barcode_bundled.x0;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.y0;
import java.io.IOException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class y0<MessageType extends y0<MessageType, BuilderType>, BuilderType extends x0<MessageType, BuilderType>> implements x3 {
    protected int zzb = 0;

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.x3
    public final zzdb c() {
        try {
            int b9 = b();
            zzdb zzdbVar = zzdb.f14977b;
            byte[] bArr = new byte[b9];
            w1 a9 = w1.a(bArr, 0, b9);
            d(a9);
            a9.b();
            return new m1(bArr);
        } catch (IOException e8) {
            String name = getClass().getName();
            throw new RuntimeException("Serializing " + name + " to a ByteString threw an IOException (should never happen).", e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int g(r4 r4Var) {
        throw null;
    }
}
