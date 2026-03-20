package com.google.common.collect;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.z2;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w2<R, C, V> extends j2<R, C, V> {

    /* renamed from: g  reason: collision with root package name */
    static final ImmutableTable<Object, Object, Object> f19501g = new w2(ImmutableList.E(), ImmutableSet.H(), ImmutableSet.H());

    /* renamed from: c  reason: collision with root package name */
    private final ImmutableMap<R, ImmutableMap<C, V>> f19502c;

    /* renamed from: d  reason: collision with root package name */
    private final ImmutableMap<C, ImmutableMap<R, V>> f19503d;

    /* renamed from: e  reason: collision with root package name */
    private final int[] f19504e;

    /* renamed from: f  reason: collision with root package name */
    private final int[] f19505f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public w2(ImmutableList<z2.a<R, C, V>> immutableList, ImmutableSet<R> immutableSet, ImmutableSet<C> immutableSet2) {
        ImmutableMap g8 = m1.g(immutableSet);
        LinkedHashMap m8 = m1.m();
        d3<R> it = immutableSet.iterator();
        while (it.hasNext()) {
            m8.put(it.next(), new LinkedHashMap());
        }
        LinkedHashMap m9 = m1.m();
        d3<C> it2 = immutableSet2.iterator();
        while (it2.hasNext()) {
            m9.put(it2.next(), new LinkedHashMap());
        }
        int[] iArr = new int[immutableList.size()];
        int[] iArr2 = new int[immutableList.size()];
        for (int i8 = 0; i8 < immutableList.size(); i8++) {
            z2.a<R, C, V> aVar = immutableList.get(i8);
            R b9 = aVar.b();
            C a9 = aVar.a();
            V value = aVar.getValue();
            Integer num = (Integer) g8.get(b9);
            Objects.requireNonNull(num);
            iArr[i8] = num.intValue();
            Map map = (Map) m8.get(b9);
            Objects.requireNonNull(map);
            Map map2 = map;
            iArr2[i8] = map2.size();
            y(b9, a9, map2.put(a9, value), value);
            Map map3 = (Map) m9.get(a9);
            Objects.requireNonNull(map3);
            map3.put(b9, value);
        }
        this.f19504e = iArr;
        this.f19505f = iArr2;
        ImmutableMap.b bVar = new ImmutableMap.b(m8.size());
        for (Map.Entry entry : m8.entrySet()) {
            bVar.g(entry.getKey(), ImmutableMap.c((Map) entry.getValue()));
        }
        this.f19502c = bVar.d();
        ImmutableMap.b bVar2 = new ImmutableMap.b(m9.size());
        for (Map.Entry entry2 : m9.entrySet()) {
            bVar2.g(entry2.getKey(), ImmutableMap.c((Map) entry2.getValue()));
        }
        this.f19503d = bVar2.d();
    }

    @Override // com.google.common.collect.j2
    z2.a<R, C, V> A(int i8) {
        Map.Entry<R, ImmutableMap<C, V>> entry = this.f19502c.entrySet().e().get(this.f19504e[i8]);
        Map.Entry<C, V> entry2 = entry.getValue().entrySet().e().get(this.f19505f[i8]);
        return ImmutableTable.m(entry.getKey(), entry2.getKey(), entry2.getValue());
    }

    @Override // com.google.common.collect.j2
    V B(int i8) {
        int i9 = this.f19504e[i8];
        return this.f19502c.values().e().get(i9).values().e().get(this.f19505f[i8]);
    }

    @Override // com.google.common.collect.ImmutableTable
    public ImmutableMap<C, Map<R, V>> p() {
        return ImmutableMap.c(this.f19503d);
    }

    @Override // com.google.common.collect.ImmutableTable
    ImmutableTable.a r() {
        ImmutableMap g8 = m1.g(o());
        int[] iArr = new int[a().size()];
        d3<z2.a<R, C, V>> it = a().iterator();
        int i8 = 0;
        while (it.hasNext()) {
            Integer num = (Integer) g8.get(it.next().a());
            Objects.requireNonNull(num);
            iArr[i8] = num.intValue();
            i8++;
        }
        return ImmutableTable.a.a(this, this.f19504e, iArr);
    }

    @Override // com.google.common.collect.z2
    public int size() {
        return this.f19504e.length;
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.z2
    /* renamed from: w */
    public ImmutableMap<R, Map<C, V>> b() {
        return ImmutableMap.c(this.f19502c);
    }
}
