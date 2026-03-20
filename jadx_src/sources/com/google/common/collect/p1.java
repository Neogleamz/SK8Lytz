package com.google.common.collect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class p1<K0, V0> {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends d<K0> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Comparator f19416a;

        a(Comparator comparator) {
            this.f19416a = comparator;
        }

        @Override // com.google.common.collect.p1.d
        <K extends K0, V> Map<K, Collection<V>> c() {
            return new TreeMap(this.f19416a);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b<V> implements com.google.common.base.r<List<V>>, Serializable {

        /* renamed from: a  reason: collision with root package name */
        private final int f19417a;

        b(int i8) {
            this.f19417a = t.b(i8, "expectedValuesPerKey");
        }

        @Override // com.google.common.base.r
        /* renamed from: a */
        public List<V> get() {
            return new ArrayList(this.f19417a);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class c<K0, V0> extends p1<K0, V0> {
        c() {
            super(null);
        }

        public abstract <K extends K0, V extends V0> i1<K, V> c();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class d<K0> {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a extends c<K0, Object> {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ int f19418a;

            a(int i8) {
                this.f19418a = i8;
            }

            @Override // com.google.common.collect.p1.c
            public <K extends K0, V> i1<K, V> c() {
                return q1.b(d.this.c(), new b(this.f19418a));
            }
        }

        d() {
        }

        public c<K0, Object> a() {
            return b(2);
        }

        public c<K0, Object> b(int i8) {
            t.b(i8, "expectedValuesPerKey");
            return new a(i8);
        }

        abstract <K extends K0, V> Map<K, Collection<V>> c();
    }

    private p1() {
    }

    /* synthetic */ p1(o1 o1Var) {
        this();
    }

    public static d<Comparable> a() {
        return b(y1.c());
    }

    public static <K0> d<K0> b(Comparator<K0> comparator) {
        com.google.common.base.l.n(comparator);
        return new a(comparator);
    }
}
