package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class u1 extends zzcy {

    /* renamed from: c  reason: collision with root package name */
    private final transient zzcx f14056c;

    /* renamed from: d  reason: collision with root package name */
    private final transient Object[] f14057d;

    /* renamed from: e  reason: collision with root package name */
    private final transient int f14058e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public u1(zzcx zzcxVar, Object[] objArr, int i8, int i9) {
        this.f14056c = zzcxVar;
        this.f14057d = objArr;
        this.f14058e = i9;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcq, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        if (obj instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (value != null && value.equals(this.f14056c.get(key))) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcq
    public final int e(Object[] objArr, int i8) {
        return n().e(objArr, i8);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcy, com.google.android.gms.internal.mlkit_vision_barcode.zzcq
    public final b2 i() {
        return n().listIterator(0);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcy, com.google.android.gms.internal.mlkit_vision_barcode.zzcq, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final /* synthetic */ Iterator iterator() {
        return n().listIterator(0);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcy
    final zzcv p() {
        return new t1(this);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.f14058e;
    }
}
