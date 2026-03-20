package com.google.android.gms.internal.measurement;

import java.util.Iterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class db extends lb {

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ ya f12144b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    private db(ya yaVar) {
        super(yaVar);
        this.f12144b = yaVar;
    }

    @Override // com.google.android.gms.internal.measurement.lb, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator iterator() {
        return new bb(this.f12144b);
    }
}
