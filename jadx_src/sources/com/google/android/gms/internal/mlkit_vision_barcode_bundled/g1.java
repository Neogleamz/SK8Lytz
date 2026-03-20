package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.NoSuchElementException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g1 extends i1 {

    /* renamed from: a  reason: collision with root package name */
    private int f14762a = 0;

    /* renamed from: b  reason: collision with root package name */
    private final int f14763b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ zzdb f14764c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g1(zzdb zzdbVar) {
        this.f14764c = zzdbVar;
        this.f14763b = zzdbVar.i();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f14762a < this.f14763b;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.k1
    public final byte zza() {
        int i8 = this.f14762a;
        if (i8 < this.f14763b) {
            this.f14762a = i8 + 1;
            return this.f14764c.g(i8);
        }
        throw new NoSuchElementException();
    }
}
