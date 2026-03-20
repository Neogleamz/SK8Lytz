package com.google.common.collect;

import java.io.Serializable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s<F, T> extends y1<F> implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    final com.google.common.base.g<F, ? extends T> f19435a;

    /* renamed from: b  reason: collision with root package name */
    final y1<T> f19436b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s(com.google.common.base.g<F, ? extends T> gVar, y1<T> y1Var) {
        this.f19435a = (com.google.common.base.g) com.google.common.base.l.n(gVar);
        this.f19436b = (y1) com.google.common.base.l.n(y1Var);
    }

    @Override // com.google.common.collect.y1, java.util.Comparator
    public int compare(F f5, F f8) {
        return this.f19436b.compare(this.f19435a.apply(f5), this.f19435a.apply(f8));
    }

    @Override // java.util.Comparator
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof s) {
            s sVar = (s) obj;
            return this.f19435a.equals(sVar.f19435a) && this.f19436b.equals(sVar.f19436b);
        }
        return false;
    }

    public int hashCode() {
        return com.google.common.base.k.b(this.f19435a, this.f19436b);
    }

    public String toString() {
        String valueOf = String.valueOf(this.f19436b);
        String valueOf2 = String.valueOf(this.f19435a);
        StringBuilder sb = new StringBuilder(valueOf.length() + 13 + valueOf2.length());
        sb.append(valueOf);
        sb.append(".onResultOf(");
        sb.append(valueOf2);
        sb.append(")");
        return sb.toString();
    }
}
