package com.google.common.collect;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.z2;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class r2<R, C, V> extends ImmutableTable<R, C, V> {

    /* renamed from: c  reason: collision with root package name */
    final R f19432c;

    /* renamed from: d  reason: collision with root package name */
    final C f19433d;

    /* renamed from: e  reason: collision with root package name */
    final V f19434e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public r2(R r4, C c9, V v8) {
        this.f19432c = (R) com.google.common.base.l.n(r4);
        this.f19433d = (C) com.google.common.base.l.n(c9);
        this.f19434e = (V) com.google.common.base.l.n(v8);
    }

    @Override // com.google.common.collect.ImmutableTable
    public ImmutableMap<C, Map<R, V>> p() {
        return ImmutableMap.o(this.f19433d, ImmutableMap.o(this.f19432c, this.f19434e));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.o
    /* renamed from: q */
    public ImmutableSet<z2.a<R, C, V>> f() {
        return ImmutableSet.I(ImmutableTable.m(this.f19432c, this.f19433d, this.f19434e));
    }

    @Override // com.google.common.collect.ImmutableTable
    ImmutableTable.a r() {
        return ImmutableTable.a.a(this, new int[]{0}, new int[]{0});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.o
    /* renamed from: s */
    public ImmutableCollection<V> g() {
        return ImmutableSet.I(this.f19434e);
    }

    @Override // com.google.common.collect.z2
    public int size() {
        return 1;
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.z2
    /* renamed from: w */
    public ImmutableMap<R, Map<C, V>> b() {
        return ImmutableMap.o(this.f19432c, ImmutableMap.o(this.f19433d, this.f19434e));
    }
}
