package com.google.common.collect;

import java.io.Serializable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l2<T> extends y1<T> implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    final y1<? super T> f19396a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l2(y1<? super T> y1Var) {
        this.f19396a = (y1) com.google.common.base.l.n(y1Var);
    }

    @Override // com.google.common.collect.y1, java.util.Comparator
    public int compare(T t8, T t9) {
        return this.f19396a.compare(t9, t8);
    }

    @Override // java.util.Comparator
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof l2) {
            return this.f19396a.equals(((l2) obj).f19396a);
        }
        return false;
    }

    @Override // com.google.common.collect.y1
    public <S extends T> y1<S> f() {
        return (y1<? super T>) this.f19396a;
    }

    public int hashCode() {
        return -this.f19396a.hashCode();
    }

    public String toString() {
        String valueOf = String.valueOf(this.f19396a);
        StringBuilder sb = new StringBuilder(valueOf.length() + 10);
        sb.append(valueOf);
        sb.append(".reverse()");
        return sb.toString();
    }
}
