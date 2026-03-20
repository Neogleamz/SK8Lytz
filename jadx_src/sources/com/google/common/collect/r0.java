package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class r0<E> extends j0<E> implements Set<E> {
    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object obj) {
        return obj == this || i().equals(obj);
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        return i().hashCode();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.j0
    /* renamed from: v */
    public abstract Set<E> i();

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean x(Collection<?> collection) {
        return p2.i(this, (Collection) com.google.common.base.l.n(collection));
    }
}
