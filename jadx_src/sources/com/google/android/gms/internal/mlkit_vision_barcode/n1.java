package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class n1 extends AbstractMap {

    /* renamed from: a  reason: collision with root package name */
    private transient Set f13790a;

    /* renamed from: b  reason: collision with root package name */
    private transient Collection f13791b;

    abstract Set a();

    @Override // java.util.AbstractMap, java.util.Map
    public final Set entrySet() {
        Set set = this.f13790a;
        if (set == null) {
            Set a9 = a();
            this.f13790a = a9;
            return a9;
        }
        return set;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Collection values() {
        Collection collection = this.f13791b;
        if (collection == null) {
            m1 m1Var = new m1(this);
            this.f13791b = m1Var;
            return m1Var;
        }
        return collection;
    }
}
