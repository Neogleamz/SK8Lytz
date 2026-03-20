package com.google.common.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.z2;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ImmutableTable<R, C, V> extends o<R, C, V> implements Serializable {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class a implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private final Object[] f19045a;

        /* renamed from: b  reason: collision with root package name */
        private final Object[] f19046b;

        /* renamed from: c  reason: collision with root package name */
        private final Object[] f19047c;

        /* renamed from: d  reason: collision with root package name */
        private final int[] f19048d;

        /* renamed from: e  reason: collision with root package name */
        private final int[] f19049e;

        private a(Object[] objArr, Object[] objArr2, Object[] objArr3, int[] iArr, int[] iArr2) {
            this.f19045a = objArr;
            this.f19046b = objArr2;
            this.f19047c = objArr3;
            this.f19048d = iArr;
            this.f19049e = iArr2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static a a(ImmutableTable<?, ?, ?> immutableTable, int[] iArr, int[] iArr2) {
            return new a(immutableTable.v().toArray(), immutableTable.o().toArray(), immutableTable.x().toArray(), iArr, iArr2);
        }

        Object readResolve() {
            Object[] objArr = this.f19047c;
            if (objArr.length == 0) {
                return ImmutableTable.t();
            }
            int i8 = 0;
            if (objArr.length == 1) {
                return ImmutableTable.u(this.f19045a[0], this.f19046b[0], objArr[0]);
            }
            ImmutableList.a aVar = new ImmutableList.a(objArr.length);
            while (true) {
                Object[] objArr2 = this.f19047c;
                if (i8 >= objArr2.length) {
                    return j2.z(aVar.k(), ImmutableSet.E(this.f19045a), ImmutableSet.E(this.f19046b));
                }
                aVar.a(ImmutableTable.m(this.f19045a[this.f19048d[i8]], this.f19046b[this.f19049e[i8]], objArr2[i8]));
                i8++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <R, C, V> z2.a<R, C, V> m(R r4, C c9, V v8) {
        return a3.b(com.google.common.base.l.o(r4, "rowKey"), com.google.common.base.l.o(c9, "columnKey"), com.google.common.base.l.o(v8, "value"));
    }

    public static <R, C, V> ImmutableTable<R, C, V> t() {
        return (ImmutableTable<R, C, V>) w2.f19501g;
    }

    public static <R, C, V> ImmutableTable<R, C, V> u(R r4, C c9, V v8) {
        return new r2(r4, c9, v8);
    }

    @Override // com.google.common.collect.o
    @Deprecated
    public final void d() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.o
    public boolean e(Object obj) {
        return x().contains(obj);
    }

    @Override // com.google.common.collect.o
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.google.common.collect.o
    public /* bridge */ /* synthetic */ Object h(Object obj, Object obj2) {
        return super.h(obj, obj2);
    }

    @Override // com.google.common.collect.o
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.o
    public /* bridge */ /* synthetic */ boolean i() {
        return super.i();
    }

    @Override // com.google.common.collect.o
    final Iterator<V> k() {
        throw new AssertionError("should never be called");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.o
    /* renamed from: l */
    public final d3<z2.a<R, C, V>> c() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.o, com.google.common.collect.z2
    /* renamed from: n */
    public ImmutableSet<z2.a<R, C, V>> a() {
        return (ImmutableSet) super.a();
    }

    public ImmutableSet<C> o() {
        return p().keySet();
    }

    public abstract ImmutableMap<C, Map<R, V>> p();

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.o
    /* renamed from: q */
    public abstract ImmutableSet<z2.a<R, C, V>> f();

    abstract a r();

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.o
    /* renamed from: s */
    public abstract ImmutableCollection<V> g();

    @Override // com.google.common.collect.o
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public ImmutableSet<R> v() {
        return b().keySet();
    }

    @Override // com.google.common.collect.z2
    /* renamed from: w */
    public abstract ImmutableMap<R, Map<C, V>> b();

    final Object writeReplace() {
        return r();
    }

    public ImmutableCollection<V> x() {
        return (ImmutableCollection) super.j();
    }
}
