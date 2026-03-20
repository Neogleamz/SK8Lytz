package com.google.common.collect;

import com.google.common.collect.ImmutableMap;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f2<K, V> extends ImmutableMap<K, V> {

    /* renamed from: h  reason: collision with root package name */
    static final ImmutableMap<Object, Object> f19272h = new f2(null, new Object[0], 0);
    private static final long serialVersionUID = 0;

    /* renamed from: e  reason: collision with root package name */
    private final transient Object f19273e;

    /* renamed from: f  reason: collision with root package name */
    final transient Object[] f19274f;

    /* renamed from: g  reason: collision with root package name */
    private final transient int f19275g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a<K, V> extends ImmutableSet<Map.Entry<K, V>> {

        /* renamed from: c  reason: collision with root package name */
        private final transient ImmutableMap<K, V> f19276c;

        /* renamed from: d  reason: collision with root package name */
        private final transient Object[] f19277d;

        /* renamed from: e  reason: collision with root package name */
        private final transient int f19278e;

        /* renamed from: f  reason: collision with root package name */
        private final transient int f19279f;

        /* renamed from: com.google.common.collect.f2$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class C0152a extends ImmutableList<Map.Entry<K, V>> {
            C0152a() {
            }

            @Override // java.util.List
            /* renamed from: Q */
            public Map.Entry<K, V> get(int i8) {
                com.google.common.base.l.l(i8, a.this.f19279f);
                int i9 = i8 * 2;
                Object obj = a.this.f19277d[a.this.f19278e + i9];
                Objects.requireNonNull(obj);
                Object obj2 = a.this.f19277d[i9 + (a.this.f19278e ^ 1)];
                Objects.requireNonNull(obj2);
                return new AbstractMap.SimpleImmutableEntry(obj, obj2);
            }

            @Override // com.google.common.collect.ImmutableCollection
            public boolean n() {
                return true;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return a.this.f19279f;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(ImmutableMap<K, V> immutableMap, Object[] objArr, int i8, int i9) {
            this.f19276c = immutableMap;
            this.f19277d = objArr;
            this.f19278e = i8;
            this.f19279f = i9;
        }

        @Override // com.google.common.collect.ImmutableSet
        ImmutableList<Map.Entry<K, V>> F() {
            return new C0152a();
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                Object value = entry.getValue();
                return value != null && value.equals(this.f19276c.get(key));
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public int g(Object[] objArr, int i8) {
            return e().g(objArr, i8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean n() {
            return true;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        /* renamed from: p */
        public d3<Map.Entry<K, V>> iterator() {
            return e().iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f19279f;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b<K> extends ImmutableSet<K> {

        /* renamed from: c  reason: collision with root package name */
        private final transient ImmutableMap<K, ?> f19281c;

        /* renamed from: d  reason: collision with root package name */
        private final transient ImmutableList<K> f19282d;

        /* JADX INFO: Access modifiers changed from: package-private */
        public b(ImmutableMap<K, ?> immutableMap, ImmutableList<K> immutableList) {
            this.f19281c = immutableMap;
            this.f19282d = immutableList;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return this.f19281c.get(obj) != null;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        public ImmutableList<K> e() {
            return this.f19282d;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public int g(Object[] objArr, int i8) {
            return e().g(objArr, i8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean n() {
            return true;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        /* renamed from: p */
        public d3<K> iterator() {
            return e().iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f19281c.size();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class c extends ImmutableList<Object> {

        /* renamed from: c  reason: collision with root package name */
        private final transient Object[] f19283c;

        /* renamed from: d  reason: collision with root package name */
        private final transient int f19284d;

        /* renamed from: e  reason: collision with root package name */
        private final transient int f19285e;

        /* JADX INFO: Access modifiers changed from: package-private */
        public c(Object[] objArr, int i8, int i9) {
            this.f19283c = objArr;
            this.f19284d = i8;
            this.f19285e = i9;
        }

        @Override // java.util.List
        public Object get(int i8) {
            com.google.common.base.l.l(i8, this.f19285e);
            Object obj = this.f19283c[(i8 * 2) + this.f19284d];
            Objects.requireNonNull(obj);
            return obj;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean n() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.f19285e;
        }
    }

    private f2(Object obj, Object[] objArr, int i8) {
        this.f19273e = obj;
        this.f19274f = objArr;
        this.f19275g = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> f2<K, V> q(int i8, Object[] objArr) {
        return r(i8, objArr, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> f2<K, V> r(int i8, Object[] objArr, ImmutableMap.b<K, V> bVar) {
        if (i8 == 0) {
            return (f2) f19272h;
        }
        if (i8 == 1) {
            Object obj = objArr[0];
            Objects.requireNonNull(obj);
            Object obj2 = objArr[1];
            Objects.requireNonNull(obj2);
            t.a(obj, obj2);
            return new f2<>(null, objArr, 1);
        }
        com.google.common.base.l.p(i8, objArr.length >> 1);
        Object s8 = s(objArr, i8, ImmutableSet.v(i8), 0);
        if (s8 instanceof Object[]) {
            Object[] objArr2 = (Object[]) s8;
            ImmutableMap.b.a aVar = (ImmutableMap.b.a) objArr2[2];
            if (bVar == null) {
                throw aVar.a();
            }
            bVar.f18970e = aVar;
            Object obj3 = objArr2[0];
            int intValue = ((Integer) objArr2[1]).intValue();
            objArr = Arrays.copyOf(objArr, intValue * 2);
            s8 = obj3;
            i8 = intValue;
        }
        return new f2<>(s8, objArr, i8);
    }

    private static Object s(Object[] objArr, int i8, int i9, int i10) {
        ImmutableMap.b.a aVar = null;
        if (i8 == 1) {
            Object obj = objArr[i10];
            Objects.requireNonNull(obj);
            Object obj2 = objArr[i10 ^ 1];
            Objects.requireNonNull(obj2);
            t.a(obj, obj2);
            return null;
        }
        int i11 = i9 - 1;
        int i12 = -1;
        if (i9 <= 128) {
            byte[] bArr = new byte[i9];
            Arrays.fill(bArr, (byte) -1);
            int i13 = 0;
            for (int i14 = 0; i14 < i8; i14++) {
                int i15 = (i14 * 2) + i10;
                int i16 = (i13 * 2) + i10;
                Object obj3 = objArr[i15];
                Objects.requireNonNull(obj3);
                Object obj4 = objArr[i15 ^ 1];
                Objects.requireNonNull(obj4);
                t.a(obj3, obj4);
                int c9 = v0.c(obj3.hashCode());
                while (true) {
                    int i17 = c9 & i11;
                    int i18 = bArr[i17] & 255;
                    if (i18 == 255) {
                        bArr[i17] = (byte) i16;
                        if (i13 < i14) {
                            objArr[i16] = obj3;
                            objArr[i16 ^ 1] = obj4;
                        }
                        i13++;
                    } else if (obj3.equals(objArr[i18])) {
                        int i19 = i18 ^ 1;
                        Object obj5 = objArr[i19];
                        Objects.requireNonNull(obj5);
                        aVar = new ImmutableMap.b.a(obj3, obj4, obj5);
                        objArr[i19] = obj4;
                        break;
                    } else {
                        c9 = i17 + 1;
                    }
                }
            }
            return i13 == i8 ? bArr : new Object[]{bArr, Integer.valueOf(i13), aVar};
        } else if (i9 <= 32768) {
            short[] sArr = new short[i9];
            Arrays.fill(sArr, (short) -1);
            int i20 = 0;
            for (int i21 = 0; i21 < i8; i21++) {
                int i22 = (i21 * 2) + i10;
                int i23 = (i20 * 2) + i10;
                Object obj6 = objArr[i22];
                Objects.requireNonNull(obj6);
                Object obj7 = objArr[i22 ^ 1];
                Objects.requireNonNull(obj7);
                t.a(obj6, obj7);
                int c10 = v0.c(obj6.hashCode());
                while (true) {
                    int i24 = c10 & i11;
                    int i25 = sArr[i24] & 65535;
                    if (i25 == 65535) {
                        sArr[i24] = (short) i23;
                        if (i20 < i21) {
                            objArr[i23] = obj6;
                            objArr[i23 ^ 1] = obj7;
                        }
                        i20++;
                    } else if (obj6.equals(objArr[i25])) {
                        int i26 = i25 ^ 1;
                        Object obj8 = objArr[i26];
                        Objects.requireNonNull(obj8);
                        aVar = new ImmutableMap.b.a(obj6, obj7, obj8);
                        objArr[i26] = obj7;
                        break;
                    } else {
                        c10 = i24 + 1;
                    }
                }
            }
            return i20 == i8 ? sArr : new Object[]{sArr, Integer.valueOf(i20), aVar};
        } else {
            int[] iArr = new int[i9];
            Arrays.fill(iArr, -1);
            int i27 = 0;
            int i28 = 0;
            while (i27 < i8) {
                int i29 = (i27 * 2) + i10;
                int i30 = (i28 * 2) + i10;
                Object obj9 = objArr[i29];
                Objects.requireNonNull(obj9);
                Object obj10 = objArr[i29 ^ 1];
                Objects.requireNonNull(obj10);
                t.a(obj9, obj10);
                int c11 = v0.c(obj9.hashCode());
                while (true) {
                    int i31 = c11 & i11;
                    int i32 = iArr[i31];
                    if (i32 == i12) {
                        iArr[i31] = i30;
                        if (i28 < i27) {
                            objArr[i30] = obj9;
                            objArr[i30 ^ 1] = obj10;
                        }
                        i28++;
                    } else if (obj9.equals(objArr[i32])) {
                        int i33 = i32 ^ 1;
                        Object obj11 = objArr[i33];
                        Objects.requireNonNull(obj11);
                        aVar = new ImmutableMap.b.a(obj9, obj10, obj11);
                        objArr[i33] = obj10;
                        break;
                    } else {
                        c11 = i31 + 1;
                        i12 = -1;
                    }
                }
                i27++;
                i12 = -1;
            }
            return i28 == i8 ? iArr : new Object[]{iArr, Integer.valueOf(i28), aVar};
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object t(Object[] objArr, int i8, int i9, int i10) {
        Object s8 = s(objArr, i8, i9, i10);
        if (s8 instanceof Object[]) {
            throw ((ImmutableMap.b.a) ((Object[]) s8)[2]).a();
        }
        return s8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object u(Object obj, Object[] objArr, int i8, int i9, Object obj2) {
        if (obj2 == null) {
            return null;
        }
        if (i8 == 1) {
            Object obj3 = objArr[i9];
            Objects.requireNonNull(obj3);
            if (obj3.equals(obj2)) {
                Object obj4 = objArr[i9 ^ 1];
                Objects.requireNonNull(obj4);
                return obj4;
            }
            return null;
        } else if (obj == null) {
            return null;
        } else {
            if (obj instanceof byte[]) {
                byte[] bArr = (byte[]) obj;
                int length = bArr.length - 1;
                int c9 = v0.c(obj2.hashCode());
                while (true) {
                    int i10 = c9 & length;
                    int i11 = bArr[i10] & 255;
                    if (i11 == 255) {
                        return null;
                    }
                    if (obj2.equals(objArr[i11])) {
                        return objArr[i11 ^ 1];
                    }
                    c9 = i10 + 1;
                }
            } else if (obj instanceof short[]) {
                short[] sArr = (short[]) obj;
                int length2 = sArr.length - 1;
                int c10 = v0.c(obj2.hashCode());
                while (true) {
                    int i12 = c10 & length2;
                    int i13 = sArr[i12] & 65535;
                    if (i13 == 65535) {
                        return null;
                    }
                    if (obj2.equals(objArr[i13])) {
                        return objArr[i13 ^ 1];
                    }
                    c10 = i12 + 1;
                }
            } else {
                int[] iArr = (int[]) obj;
                int length3 = iArr.length - 1;
                int c11 = v0.c(obj2.hashCode());
                while (true) {
                    int i14 = c11 & length3;
                    int i15 = iArr[i14];
                    if (i15 == -1) {
                        return null;
                    }
                    if (obj2.equals(objArr[i15])) {
                        return objArr[i15 ^ 1];
                    }
                    c11 = i14 + 1;
                }
            }
        }
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<Map.Entry<K, V>> d() {
        return new a(this, this.f19274f, 0, this.f19275g);
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<K> f() {
        return new b(this, new c(this.f19274f, 0, this.f19275g));
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public V get(Object obj) {
        V v8 = (V) u(this.f19273e, this.f19274f, this.f19275g, 0, obj);
        if (v8 == null) {
            return null;
        }
        return v8;
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableCollection<V> h() {
        return new c(this.f19274f, 1, this.f19275g);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableMap
    public boolean k() {
        return false;
    }

    @Override // java.util.Map
    public int size() {
        return this.f19275g;
    }
}
