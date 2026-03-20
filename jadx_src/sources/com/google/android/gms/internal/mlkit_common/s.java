package com.google.android.gms.internal.mlkit_common;

import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class s extends zzav {

    /* renamed from: c  reason: collision with root package name */
    private final transient zzau f13010c;

    /* renamed from: d  reason: collision with root package name */
    private final transient zzar f13011d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s(zzau zzauVar, zzar zzarVar) {
        this.f13010c = zzauVar;
        this.f13011d = zzarVar;
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzan, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        return this.f13010c.get(obj) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_common.zzan
    public final int e(Object[] objArr, int i8) {
        return this.f13011d.e(objArr, 0);
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzav, com.google.android.gms.internal.mlkit_common.zzan
    public final w i() {
        return this.f13011d.listIterator(0);
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzav, com.google.android.gms.internal.mlkit_common.zzan, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final /* synthetic */ Iterator iterator() {
        return this.f13011d.listIterator(0);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.f13010c.size();
    }
}
