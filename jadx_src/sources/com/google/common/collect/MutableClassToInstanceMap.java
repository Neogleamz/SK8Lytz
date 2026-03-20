package com.google.common.collect;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MutableClassToInstanceMap<B> extends m0<Class<? extends B>, B> implements Serializable {

    /* renamed from: a  reason: collision with root package name */
    private final Map<Class<? extends B>, B> f19109a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends n0<Class<? extends B>, B> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Map.Entry f19110a;

        a(Map.Entry entry) {
            this.f19110a = entry;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.p0
        /* renamed from: i */
        public Map.Entry<Class<? extends B>, B> h() {
            return this.f19110a;
        }

        @Override // com.google.common.collect.n0, java.util.Map.Entry
        public B setValue(B b9) {
            return (B) super.setValue(MutableClassToInstanceMap.t(getKey(), b9));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends r0<Map.Entry<Class<? extends B>, B>> {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends b3<Map.Entry<Class<? extends B>, B>, Map.Entry<Class<? extends B>, B>> {
            a(b bVar, Iterator it) {
                super(it);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.collect.b3
            /* renamed from: b */
            public Map.Entry<Class<? extends B>, B> a(Map.Entry<Class<? extends B>, B> entry) {
                return MutableClassToInstanceMap.u(entry);
            }
        }

        b() {
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<Class<? extends B>, B>> iterator() {
            return new a(this, i().iterator());
        }

        @Override // com.google.common.collect.j0, java.util.Collection
        public Object[] toArray() {
            return q();
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] tArr) {
            return (T[]) t(tArr);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.r0, com.google.common.collect.j0
        /* renamed from: v */
        public Set<Map.Entry<Class<? extends B>, B>> i() {
            return MutableClassToInstanceMap.this.h().entrySet();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class c<B> implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private final Map<Class<? extends B>, B> f19112a;

        c(Map<Class<? extends B>, B> map) {
            this.f19112a = map;
        }

        Object readResolve() {
            return MutableClassToInstanceMap.v(this.f19112a);
        }
    }

    private MutableClassToInstanceMap(Map<Class<? extends B>, B> map) {
        this.f19109a = (Map) com.google.common.base.l.n(map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <B, T extends B> T t(Class<T> cls, B b9) {
        return (T) com.google.common.primitives.j.b(cls).cast(b9);
    }

    static <B> Map.Entry<Class<? extends B>, B> u(Map.Entry<Class<? extends B>, B> entry) {
        return new a(entry);
    }

    public static <B> MutableClassToInstanceMap<B> v(Map<Class<? extends B>, B> map) {
        return new MutableClassToInstanceMap<>(map);
    }

    private Object writeReplace() {
        return new c(h());
    }

    @Override // com.google.common.collect.m0, java.util.Map
    public Set<Map.Entry<Class<? extends B>, B>> entrySet() {
        return new b();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.m0, com.google.common.collect.p0
    /* renamed from: i */
    public Map<Class<? extends B>, B> h() {
        return this.f19109a;
    }

    @Override // com.google.common.collect.m0, java.util.Map
    public void putAll(Map<? extends Class<? extends B>, ? extends B> map) {
        LinkedHashMap linkedHashMap = new LinkedHashMap(map);
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            t((Class) entry.getKey(), entry.getValue());
        }
        super.putAll(linkedHashMap);
    }

    @Override // com.google.common.collect.m0, java.util.Map
    /* renamed from: x */
    public B put(Class<? extends B> cls, B b9) {
        return (B) super.put(cls, t(cls, b9));
    }
}
