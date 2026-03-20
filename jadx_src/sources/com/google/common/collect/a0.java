package com.google.common.collect;

import java.io.Serializable;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a0<T> extends y1<T> implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    final Comparator<T> f19167a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a0(Comparator<T> comparator) {
        this.f19167a = (Comparator) com.google.common.base.l.n(comparator);
    }

    @Override // com.google.common.collect.y1, java.util.Comparator
    public int compare(T t8, T t9) {
        return this.f19167a.compare(t8, t9);
    }

    @Override // java.util.Comparator
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof a0) {
            return this.f19167a.equals(((a0) obj).f19167a);
        }
        return false;
    }

    public int hashCode() {
        return this.f19167a.hashCode();
    }

    public String toString() {
        return this.f19167a.toString();
    }
}
