package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class k4 extends i1 {

    /* renamed from: a  reason: collision with root package name */
    final o4 f14799a;

    /* renamed from: b  reason: collision with root package name */
    k1 f14800b = a();

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ q4 f14801c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k4(q4 q4Var) {
        this.f14801c = q4Var;
        this.f14799a = new o4(q4Var, null);
    }

    private final k1 a() {
        o4 o4Var = this.f14799a;
        if (o4Var.hasNext()) {
            return o4Var.next().iterator();
        }
        return null;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f14800b != null;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.k1
    public final byte zza() {
        k1 k1Var = this.f14800b;
        if (k1Var != null) {
            byte zza = k1Var.zza();
            if (!this.f14800b.hasNext()) {
                this.f14800b = a();
            }
            return zza;
        }
        throw new NoSuchElementException();
    }
}
