package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class zzcy extends zzcq implements Set {

    /* renamed from: b  reason: collision with root package name */
    private transient zzcv f14315b;

    @Override // java.util.Collection, java.util.Set
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return z1.b(this, obj);
    }

    @Override // java.util.Collection, java.util.Set
    public final int hashCode() {
        return z1.a(this);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcq, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    /* renamed from: i */
    public abstract b2 iterator();

    public final zzcv n() {
        zzcv zzcvVar = this.f14315b;
        if (zzcvVar == null) {
            zzcv p8 = p();
            this.f14315b = p8;
            return p8;
        }
        return zzcvVar;
    }

    zzcv p() {
        Object[] array = toArray();
        int i8 = zzcv.f14311c;
        return zzcv.p(array, array.length);
    }
}
