package com.google.android.gms.internal.mlkit_vision_common;

import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class zzs extends zzl implements Set {

    /* renamed from: b  reason: collision with root package name */
    private transient zzp f16279b;

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
        return b.a(this);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    /* renamed from: i */
    public abstract c iterator();

    public final zzp n() {
        zzp zzpVar = this.f16279b;
        if (zzpVar == null) {
            zzp p8 = p();
            this.f16279b = p8;
            return p8;
        }
        return zzpVar;
    }

    zzp p() {
        return zzp.p(toArray());
    }
}
