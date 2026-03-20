package com.google.android.gms.internal.measurement;

import java.util.NoSuchElementException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q7 extends r7 {

    /* renamed from: a  reason: collision with root package name */
    private int f12454a = 0;

    /* renamed from: b  reason: collision with root package name */
    private final int f12455b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ zzij f12456c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q7(zzij zzijVar) {
        this.f12456c = zzijVar;
        this.f12455b = zzijVar.v();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f12454a < this.f12455b;
    }

    @Override // com.google.android.gms.internal.measurement.w7
    public final byte zza() {
        int i8 = this.f12454a;
        if (i8 < this.f12455b) {
            this.f12454a = i8 + 1;
            return this.f12456c.u(i8);
        }
        throw new NoSuchElementException();
    }
}
