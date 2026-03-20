package com.google.common.collect;

import com.google.common.collect.f2;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d2<K, V> extends ImmutableBiMap<K, V> {

    /* renamed from: k  reason: collision with root package name */
    static final d2<Object, Object> f19209k = new d2<>();

    /* renamed from: e  reason: collision with root package name */
    private final transient Object f19210e;

    /* renamed from: f  reason: collision with root package name */
    final transient Object[] f19211f;

    /* renamed from: g  reason: collision with root package name */
    private final transient int f19212g;

    /* renamed from: h  reason: collision with root package name */
    private final transient int f19213h;

    /* renamed from: j  reason: collision with root package name */
    private final transient d2<V, K> f19214j;

    /* JADX WARN: Multi-variable type inference failed */
    private d2() {
        this.f19210e = null;
        this.f19211f = new Object[0];
        this.f19212g = 0;
        this.f19213h = 0;
        this.f19214j = this;
    }

    private d2(Object obj, Object[] objArr, int i8, d2<V, K> d2Var) {
        this.f19210e = obj;
        this.f19211f = objArr;
        this.f19212g = 1;
        this.f19213h = i8;
        this.f19214j = d2Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d2(Object[] objArr, int i8) {
        this.f19211f = objArr;
        this.f19213h = i8;
        this.f19212g = 0;
        int v8 = i8 >= 2 ? ImmutableSet.v(i8) : 0;
        this.f19210e = f2.t(objArr, i8, v8, 0);
        this.f19214j = new d2<>(f2.t(objArr, i8, v8, 1), objArr, i8, this);
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<Map.Entry<K, V>> d() {
        return new f2.a(this, this.f19211f, this.f19212g, this.f19213h);
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<K> f() {
        return new f2.b(this, new f2.c(this.f19211f, this.f19212g, this.f19213h));
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public V get(Object obj) {
        V v8 = (V) f2.u(this.f19210e, this.f19211f, this.f19213h, this.f19212g, obj);
        if (v8 == null) {
            return null;
        }
        return v8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableMap
    public boolean k() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableBiMap, com.google.common.collect.r
    /* renamed from: r */
    public ImmutableBiMap<V, K> g() {
        return this.f19214j;
    }

    @Override // java.util.Map
    public int size() {
        return this.f19213h;
    }
}
