package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import java.util.Iterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b0 implements Iterator<String> {

    /* renamed from: a  reason: collision with root package name */
    private Iterator<String> f16327a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ zzba f16328b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b0(zzba zzbaVar) {
        Bundle bundle;
        this.f16328b = zzbaVar;
        bundle = zzbaVar.f17262a;
        this.f16327a = bundle.keySet().iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f16327a.hasNext();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ String next() {
        return this.f16327a.next();
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException("Remove not supported");
    }
}
