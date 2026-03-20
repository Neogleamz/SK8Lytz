package com.google.common.collect;

import java.util.Comparator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b0 {

    /* renamed from: a  reason: collision with root package name */
    private static final b0 f19174a = new a();

    /* renamed from: b  reason: collision with root package name */
    private static final b0 f19175b = new b(-1);

    /* renamed from: c  reason: collision with root package name */
    private static final b0 f19176c = new b(1);

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends b0 {
        a() {
            super(null);
        }

        @Override // com.google.common.collect.b0
        public b0 d(int i8, int i9) {
            return l(com.google.common.primitives.g.e(i8, i9));
        }

        @Override // com.google.common.collect.b0
        public b0 e(long j8, long j9) {
            return l(com.google.common.primitives.i.a(j8, j9));
        }

        @Override // com.google.common.collect.b0
        public b0 f(Comparable<?> comparable, Comparable<?> comparable2) {
            return l(comparable.compareTo(comparable2));
        }

        @Override // com.google.common.collect.b0
        public <T> b0 g(T t8, T t9, Comparator<T> comparator) {
            return l(comparator.compare(t8, t9));
        }

        @Override // com.google.common.collect.b0
        public b0 h(boolean z4, boolean z8) {
            return l(com.google.common.primitives.a.a(z4, z8));
        }

        @Override // com.google.common.collect.b0
        public b0 i(boolean z4, boolean z8) {
            return l(com.google.common.primitives.a.a(z8, z4));
        }

        @Override // com.google.common.collect.b0
        public int j() {
            return 0;
        }

        b0 l(int i8) {
            return i8 < 0 ? b0.f19175b : i8 > 0 ? b0.f19176c : b0.f19174a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b extends b0 {

        /* renamed from: d  reason: collision with root package name */
        final int f19177d;

        b(int i8) {
            super(null);
            this.f19177d = i8;
        }

        @Override // com.google.common.collect.b0
        public b0 d(int i8, int i9) {
            return this;
        }

        @Override // com.google.common.collect.b0
        public b0 e(long j8, long j9) {
            return this;
        }

        @Override // com.google.common.collect.b0
        public b0 f(Comparable<?> comparable, Comparable<?> comparable2) {
            return this;
        }

        @Override // com.google.common.collect.b0
        public <T> b0 g(T t8, T t9, Comparator<T> comparator) {
            return this;
        }

        @Override // com.google.common.collect.b0
        public b0 h(boolean z4, boolean z8) {
            return this;
        }

        @Override // com.google.common.collect.b0
        public b0 i(boolean z4, boolean z8) {
            return this;
        }

        @Override // com.google.common.collect.b0
        public int j() {
            return this.f19177d;
        }
    }

    private b0() {
    }

    /* synthetic */ b0(a aVar) {
        this();
    }

    public static b0 k() {
        return f19174a;
    }

    public abstract b0 d(int i8, int i9);

    public abstract b0 e(long j8, long j9);

    public abstract b0 f(Comparable<?> comparable, Comparable<?> comparable2);

    public abstract <T> b0 g(T t8, T t9, Comparator<T> comparator);

    public abstract b0 h(boolean z4, boolean z8);

    public abstract b0 i(boolean z4, boolean z8);

    public abstract int j();
}
