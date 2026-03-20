package com.google.common.collect;

import com.daimajia.numberprogressbar.BuildConfig;
import java.io.Serializable;
import java.lang.Comparable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class c0<C extends Comparable> implements Comparable<c0<C>>, Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    final C f19187a;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends c0<Comparable<?>> {

        /* renamed from: b  reason: collision with root package name */
        private static final a f19188b = new a();
        private static final long serialVersionUID = 0;

        private a() {
            super(BuildConfig.FLAVOR);
        }

        private Object readResolve() {
            return f19188b;
        }

        @Override // com.google.common.collect.c0, java.lang.Comparable
        /* renamed from: h */
        public int compareTo(c0<Comparable<?>> c0Var) {
            return c0Var == this ? 0 : 1;
        }

        @Override // com.google.common.collect.c0
        public int hashCode() {
            return System.identityHashCode(this);
        }

        @Override // com.google.common.collect.c0
        void i(StringBuilder sb) {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.c0
        void j(StringBuilder sb) {
            sb.append("+∞)");
        }

        @Override // com.google.common.collect.c0
        boolean k(Comparable<?> comparable) {
            return false;
        }

        public String toString() {
            return "+∞";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends c0<Comparable<?>> {

        /* renamed from: b  reason: collision with root package name */
        private static final b f19189b = new b();
        private static final long serialVersionUID = 0;

        private b() {
            super(BuildConfig.FLAVOR);
        }

        private Object readResolve() {
            return f19189b;
        }

        @Override // com.google.common.collect.c0, java.lang.Comparable
        /* renamed from: h */
        public int compareTo(c0<Comparable<?>> c0Var) {
            return c0Var == this ? 0 : -1;
        }

        @Override // com.google.common.collect.c0
        public int hashCode() {
            return System.identityHashCode(this);
        }

        @Override // com.google.common.collect.c0
        void i(StringBuilder sb) {
            sb.append("(-∞");
        }

        @Override // com.google.common.collect.c0
        void j(StringBuilder sb) {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.c0
        boolean k(Comparable<?> comparable) {
            return true;
        }

        public String toString() {
            return "-∞";
        }
    }

    c0(C c9) {
        this.f19187a = c9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <C extends Comparable> c0<C> c() {
        return a.f19188b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <C extends Comparable> c0<C> f() {
        return b.f19189b;
    }

    public boolean equals(Object obj) {
        if (obj instanceof c0) {
            try {
                return compareTo((c0) obj) == 0;
            } catch (ClassCastException unused) {
                return false;
            }
        }
        return false;
    }

    @Override // java.lang.Comparable
    /* renamed from: h */
    public int compareTo(c0<C> c0Var) {
        if (c0Var == f()) {
            return 1;
        }
        if (c0Var == c()) {
            return -1;
        }
        int c9 = Range.c(this.f19187a, c0Var.f19187a);
        return c9 != 0 ? c9 : com.google.common.primitives.a.a(false, false);
    }

    public abstract int hashCode();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void i(StringBuilder sb);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void j(StringBuilder sb);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean k(C c9);
}
