package com.google.android.gms.internal.mlkit_vision_common;

import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class lc extends zzs {

    /* renamed from: c  reason: collision with root package name */
    private final transient zzr f15667c;

    /* renamed from: d  reason: collision with root package name */
    private final transient Object[] f15668d;

    /* renamed from: e  reason: collision with root package name */
    private final transient int f15669e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public lc(zzr zzrVar, Object[] objArr, int i8, int i9) {
        this.f15667c = zzrVar;
        this.f15668d = objArr;
        this.f15669e = i9;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        if (obj instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (value != null && value.equals(this.f15667c.get(key))) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl
    public final int e(Object[] objArr, int i8) {
        return n().e(objArr, 0);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzs, com.google.android.gms.internal.mlkit_vision_common.zzl
    public final c i() {
        return n().listIterator(0);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzs, com.google.android.gms.internal.mlkit_vision_common.zzl, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final /* synthetic */ Iterator iterator() {
        return n().listIterator(0);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzs
    final zzp p() {
        return new kc(this);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.f15669e;
    }
}
