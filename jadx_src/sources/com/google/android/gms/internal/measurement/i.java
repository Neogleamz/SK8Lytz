package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.NoSuchElementException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i implements Iterator<r> {

    /* renamed from: a  reason: collision with root package name */
    private int f12236a = 0;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ g f12237b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(g gVar) {
        this.f12237b = gVar;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f12236a < this.f12237b.u();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ r next() {
        if (this.f12236a < this.f12237b.u()) {
            g gVar = this.f12237b;
            int i8 = this.f12236a;
            this.f12236a = i8 + 1;
            return gVar.p(i8);
        }
        int i9 = this.f12236a;
        throw new NoSuchElementException("Out of bounds index: " + i9);
    }
}
