package com.google.android.gms.internal.mlkit_common;

import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class zzav extends zzan implements Set {

    /* renamed from: b  reason: collision with root package name */
    private transient zzar f13035b;

    @Override // java.util.Collection, java.util.Set
    public final boolean equals(Object obj) {
        if (obj == this || obj == this) {
            return true;
        }
        if (obj instanceof Set) {
            Set set = (Set) obj;
            try {
                if (size() == set.size()) {
                    if (containsAll(set)) {
                        return true;
                    }
                }
            } catch (ClassCastException | NullPointerException unused) {
            }
        }
        return false;
    }

    @Override // java.util.Collection, java.util.Set
    public final int hashCode() {
        return v.a(this);
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzan, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    /* renamed from: i */
    public abstract w iterator();

    public final zzar n() {
        zzar zzarVar = this.f13035b;
        if (zzarVar == null) {
            zzar p8 = p();
            this.f13035b = p8;
            return p8;
        }
        return zzarVar;
    }

    zzar p() {
        Object[] array = toArray();
        int i8 = zzar.f13031c;
        return zzar.p(array, array.length);
    }
}
