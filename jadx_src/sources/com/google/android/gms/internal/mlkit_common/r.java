package com.google.android.gms.internal.mlkit_common;

import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class r extends zzav {

    /* renamed from: c  reason: collision with root package name */
    private final transient zzau f13007c;

    /* renamed from: d  reason: collision with root package name */
    private final transient Object[] f13008d;

    /* renamed from: e  reason: collision with root package name */
    private final transient int f13009e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public r(zzau zzauVar, Object[] objArr, int i8, int i9) {
        this.f13007c = zzauVar;
        this.f13008d = objArr;
        this.f13009e = i9;
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzan, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        if (obj instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (value != null && value.equals(this.f13007c.get(key))) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_common.zzan
    public final int e(Object[] objArr, int i8) {
        return n().e(objArr, 0);
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzav, com.google.android.gms.internal.mlkit_common.zzan
    public final w i() {
        return n().listIterator(0);
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzav, com.google.android.gms.internal.mlkit_common.zzan, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final /* synthetic */ Iterator iterator() {
        return n().listIterator(0);
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzav
    final zzar p() {
        return new q(this);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.f13009e;
    }
}
