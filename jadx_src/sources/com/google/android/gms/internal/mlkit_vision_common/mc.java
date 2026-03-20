package com.google.android.gms.internal.mlkit_vision_common;

import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class mc extends zzs {

    /* renamed from: c  reason: collision with root package name */
    private final transient zzr f15697c;

    /* renamed from: d  reason: collision with root package name */
    private final transient zzp f15698d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public mc(zzr zzrVar, zzp zzpVar) {
        this.f15697c = zzrVar;
        this.f15698d = zzpVar;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        return this.f15697c.get(obj) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl
    public final int e(Object[] objArr, int i8) {
        return this.f15698d.e(objArr, 0);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzs, com.google.android.gms.internal.mlkit_vision_common.zzl
    public final c i() {
        return this.f15698d.listIterator(0);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzs, com.google.android.gms.internal.mlkit_vision_common.zzl, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final /* synthetic */ Iterator iterator() {
        return this.f15698d.listIterator(0);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.f15697c.size();
    }
}
