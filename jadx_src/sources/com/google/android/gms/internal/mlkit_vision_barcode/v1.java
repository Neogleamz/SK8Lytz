package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class v1 extends zzcy {

    /* renamed from: c  reason: collision with root package name */
    private final transient zzcx f14094c;

    /* renamed from: d  reason: collision with root package name */
    private final transient zzcv f14095d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v1(zzcx zzcxVar, zzcv zzcvVar) {
        this.f14094c = zzcxVar;
        this.f14095d = zzcvVar;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcq, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        return this.f14094c.get(obj) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcq
    public final int e(Object[] objArr, int i8) {
        return this.f14095d.e(objArr, i8);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcy, com.google.android.gms.internal.mlkit_vision_barcode.zzcq
    public final b2 i() {
        return this.f14095d.listIterator(0);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcy, com.google.android.gms.internal.mlkit_vision_barcode.zzcq, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final /* synthetic */ Iterator iterator() {
        return this.f14095d.listIterator(0);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.f14094c.size();
    }
}
