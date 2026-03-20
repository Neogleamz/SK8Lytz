package com.google.common.collect;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.z2;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d0<R, C, V> extends j2<R, C, V> {

    /* renamed from: c  reason: collision with root package name */
    private final ImmutableMap<R, Integer> f19190c;

    /* renamed from: d  reason: collision with root package name */
    private final ImmutableMap<C, Integer> f19191d;

    /* renamed from: e  reason: collision with root package name */
    private final ImmutableMap<R, ImmutableMap<C, V>> f19192e;

    /* renamed from: f  reason: collision with root package name */
    private final ImmutableMap<C, ImmutableMap<R, V>> f19193f;

    /* renamed from: g  reason: collision with root package name */
    private final int[] f19194g;

    /* renamed from: h  reason: collision with root package name */
    private final int[] f19195h;

    /* renamed from: j  reason: collision with root package name */
    private final V[][] f19196j;

    /* renamed from: k  reason: collision with root package name */
    private final int[] f19197k;

    /* renamed from: l  reason: collision with root package name */
    private final int[] f19198l;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class b extends d<R, V> {

        /* renamed from: f  reason: collision with root package name */
        private final int f19199f;

        b(int i8) {
            super(d0.this.f19195h[i8]);
            this.f19199f = i8;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableMap
        public boolean k() {
            return true;
        }

        @Override // com.google.common.collect.d0.d
        V s(int i8) {
            return (V) d0.this.f19196j[i8][this.f19199f];
        }

        @Override // com.google.common.collect.d0.d
        ImmutableMap<R, Integer> u() {
            return d0.this.f19190c;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private final class c extends d<C, ImmutableMap<R, V>> {
        private c() {
            super(d0.this.f19195h.length);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableMap
        public boolean k() {
            return false;
        }

        @Override // com.google.common.collect.d0.d
        ImmutableMap<C, Integer> u() {
            return d0.this.f19191d;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.d0.d
        /* renamed from: v */
        public ImmutableMap<R, V> s(int i8) {
            return new b(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class d<K, V> extends ImmutableMap.c<K, V> {

        /* renamed from: e  reason: collision with root package name */
        private final int f19202e;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends com.google.common.collect.c<Map.Entry<K, V>> {

            /* renamed from: c  reason: collision with root package name */
            private int f19203c = -1;

            /* renamed from: d  reason: collision with root package name */
            private final int f19204d;

            a() {
                this.f19204d = d.this.u().size();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.c
            /* renamed from: d */
            public Map.Entry<K, V> a() {
                int i8 = this.f19203c;
                while (true) {
                    this.f19203c = i8 + 1;
                    int i9 = this.f19203c;
                    if (i9 >= this.f19204d) {
                        return b();
                    }
                    Object s8 = d.this.s(i9);
                    if (s8 != null) {
                        return m1.f(d.this.r(this.f19203c), s8);
                    }
                    i8 = this.f19203c;
                }
            }
        }

        d(int i8) {
            this.f19202e = i8;
        }

        private boolean t() {
            return this.f19202e == u().size();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableMap.c, com.google.common.collect.ImmutableMap
        public ImmutableSet<K> f() {
            return t() ? u().keySet() : super.f();
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public V get(Object obj) {
            Integer num = u().get(obj);
            if (num == null) {
                return null;
            }
            return s(num.intValue());
        }

        @Override // com.google.common.collect.ImmutableMap.c
        d3<Map.Entry<K, V>> q() {
            return new a();
        }

        K r(int i8) {
            return u().keySet().e().get(i8);
        }

        abstract V s(int i8);

        @Override // java.util.Map
        public int size() {
            return this.f19202e;
        }

        abstract ImmutableMap<K, Integer> u();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class e extends d<C, V> {

        /* renamed from: f  reason: collision with root package name */
        private final int f19206f;

        e(int i8) {
            super(d0.this.f19194g[i8]);
            this.f19206f = i8;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableMap
        public boolean k() {
            return true;
        }

        @Override // com.google.common.collect.d0.d
        V s(int i8) {
            return (V) d0.this.f19196j[this.f19206f][i8];
        }

        @Override // com.google.common.collect.d0.d
        ImmutableMap<C, Integer> u() {
            return d0.this.f19191d;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private final class f extends d<R, ImmutableMap<C, V>> {
        private f() {
            super(d0.this.f19194g.length);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableMap
        public boolean k() {
            return false;
        }

        @Override // com.google.common.collect.d0.d
        ImmutableMap<R, Integer> u() {
            return d0.this.f19190c;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.d0.d
        /* renamed from: v */
        public ImmutableMap<C, V> s(int i8) {
            return new e(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d0(ImmutableList<z2.a<R, C, V>> immutableList, ImmutableSet<R> immutableSet, ImmutableSet<C> immutableSet2) {
        this.f19196j = (V[][]) ((Object[][]) Array.newInstance(Object.class, immutableSet.size(), immutableSet2.size()));
        ImmutableMap<R, Integer> g8 = m1.g(immutableSet);
        this.f19190c = g8;
        ImmutableMap<C, Integer> g9 = m1.g(immutableSet2);
        this.f19191d = g9;
        this.f19194g = new int[g8.size()];
        this.f19195h = new int[g9.size()];
        int[] iArr = new int[immutableList.size()];
        int[] iArr2 = new int[immutableList.size()];
        for (int i8 = 0; i8 < immutableList.size(); i8++) {
            z2.a<R, C, V> aVar = immutableList.get(i8);
            R b9 = aVar.b();
            C a9 = aVar.a();
            Integer num = this.f19190c.get(b9);
            Objects.requireNonNull(num);
            int intValue = num.intValue();
            Integer num2 = this.f19191d.get(a9);
            Objects.requireNonNull(num2);
            int intValue2 = num2.intValue();
            y(b9, a9, this.f19196j[intValue][intValue2], aVar.getValue());
            this.f19196j[intValue][intValue2] = aVar.getValue();
            int[] iArr3 = this.f19194g;
            iArr3[intValue] = iArr3[intValue] + 1;
            int[] iArr4 = this.f19195h;
            iArr4[intValue2] = iArr4[intValue2] + 1;
            iArr[i8] = intValue;
            iArr2[i8] = intValue2;
        }
        this.f19197k = iArr;
        this.f19198l = iArr2;
        this.f19192e = new f();
        this.f19193f = new c();
    }

    @Override // com.google.common.collect.j2
    z2.a<R, C, V> A(int i8) {
        int i9 = this.f19197k[i8];
        int i10 = this.f19198l[i8];
        R r4 = v().e().get(i9);
        C c9 = o().e().get(i10);
        V v8 = this.f19196j[i9][i10];
        Objects.requireNonNull(v8);
        return ImmutableTable.m(r4, c9, v8);
    }

    @Override // com.google.common.collect.j2
    V B(int i8) {
        V v8 = this.f19196j[this.f19197k[i8]][this.f19198l[i8]];
        Objects.requireNonNull(v8);
        return v8;
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.o
    public V h(Object obj, Object obj2) {
        Integer num = this.f19190c.get(obj);
        Integer num2 = this.f19191d.get(obj2);
        if (num == null || num2 == null) {
            return null;
        }
        return this.f19196j[num.intValue()][num2.intValue()];
    }

    @Override // com.google.common.collect.ImmutableTable
    public ImmutableMap<C, Map<R, V>> p() {
        return ImmutableMap.c(this.f19193f);
    }

    @Override // com.google.common.collect.ImmutableTable
    ImmutableTable.a r() {
        return ImmutableTable.a.a(this, this.f19197k, this.f19198l);
    }

    @Override // com.google.common.collect.z2
    public int size() {
        return this.f19197k.length;
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.z2
    /* renamed from: w */
    public ImmutableMap<R, Map<C, V>> b() {
        return ImmutableMap.c(this.f19192e);
    }
}
