package com.google.common.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b<T> implements m<T>, Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private final List<? extends m<? super T>> f18832a;

        private b(List<? extends m<? super T>> list) {
            this.f18832a = list;
        }

        @Override // com.google.common.base.m
        public boolean apply(T t8) {
            for (int i8 = 0; i8 < this.f18832a.size(); i8++) {
                if (!this.f18832a.get(i8).apply(t8)) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.base.m
        public boolean equals(Object obj) {
            if (obj instanceof b) {
                return this.f18832a.equals(((b) obj).f18832a);
            }
            return false;
        }

        public int hashCode() {
            return this.f18832a.hashCode() + 306654252;
        }

        public String toString() {
            return n.g("and", this.f18832a);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c implements m<Object>, Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private final Object f18833a;

        private c(Object obj) {
            this.f18833a = obj;
        }

        <T> m<T> a() {
            return this;
        }

        @Override // com.google.common.base.m
        public boolean apply(Object obj) {
            return this.f18833a.equals(obj);
        }

        @Override // com.google.common.base.m
        public boolean equals(Object obj) {
            if (obj instanceof c) {
                return this.f18833a.equals(((c) obj).f18833a);
            }
            return false;
        }

        public int hashCode() {
            return this.f18833a.hashCode();
        }

        public String toString() {
            String valueOf = String.valueOf(this.f18833a);
            StringBuilder sb = new StringBuilder(valueOf.length() + 20);
            sb.append("Predicates.equalTo(");
            sb.append(valueOf);
            sb.append(")");
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class d<T> implements m<T>, Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        final m<T> f18834a;

        d(m<T> mVar) {
            this.f18834a = (m) l.n(mVar);
        }

        @Override // com.google.common.base.m
        public boolean apply(T t8) {
            return !this.f18834a.apply(t8);
        }

        @Override // com.google.common.base.m
        public boolean equals(Object obj) {
            if (obj instanceof d) {
                return this.f18834a.equals(((d) obj).f18834a);
            }
            return false;
        }

        public int hashCode() {
            return ~this.f18834a.hashCode();
        }

        public String toString() {
            String valueOf = String.valueOf(this.f18834a);
            StringBuilder sb = new StringBuilder(valueOf.length() + 16);
            sb.append("Predicates.not(");
            sb.append(valueOf);
            sb.append(")");
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class e implements m<Object> {

        /* renamed from: a  reason: collision with root package name */
        public static final e f18835a = new a("ALWAYS_TRUE", 0);

        /* renamed from: b  reason: collision with root package name */
        public static final e f18836b = new b("ALWAYS_FALSE", 1);

        /* renamed from: c  reason: collision with root package name */
        public static final e f18837c = new c("IS_NULL", 2);

        /* renamed from: d  reason: collision with root package name */
        public static final e f18838d = new d("NOT_NULL", 3);

        /* renamed from: e  reason: collision with root package name */
        private static final /* synthetic */ e[] f18839e = c();

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum a extends e {
            a(String str, int i8) {
                super(str, i8);
            }

            @Override // com.google.common.base.m
            public boolean apply(Object obj) {
                return true;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "Predicates.alwaysTrue()";
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum b extends e {
            b(String str, int i8) {
                super(str, i8);
            }

            @Override // com.google.common.base.m
            public boolean apply(Object obj) {
                return false;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "Predicates.alwaysFalse()";
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum c extends e {
            c(String str, int i8) {
                super(str, i8);
            }

            @Override // com.google.common.base.m
            public boolean apply(Object obj) {
                return obj == null;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "Predicates.isNull()";
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum d extends e {
            d(String str, int i8) {
                super(str, i8);
            }

            @Override // com.google.common.base.m
            public boolean apply(Object obj) {
                return obj != null;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "Predicates.notNull()";
            }
        }

        private e(String str, int i8) {
        }

        private static /* synthetic */ e[] c() {
            return new e[]{f18835a, f18836b, f18837c, f18838d};
        }

        public static e valueOf(String str) {
            return (e) Enum.valueOf(e.class, str);
        }

        public static e[] values() {
            return (e[]) f18839e.clone();
        }

        <T> m<T> f() {
            return this;
        }
    }

    public static <T> m<T> b(m<? super T> mVar, m<? super T> mVar2) {
        return new b(c((m) l.n(mVar), (m) l.n(mVar2)));
    }

    private static <T> List<m<? super T>> c(m<? super T> mVar, m<? super T> mVar2) {
        return Arrays.asList(mVar, mVar2);
    }

    public static <T> m<T> d(T t8) {
        return t8 == null ? e() : new c(t8).a();
    }

    public static <T> m<T> e() {
        return e.f18837c.f();
    }

    public static <T> m<T> f(m<T> mVar) {
        return new d(mVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String g(String str, Iterable<?> iterable) {
        StringBuilder sb = new StringBuilder("Predicates.");
        sb.append(str);
        sb.append('(');
        boolean z4 = true;
        for (Object obj : iterable) {
            if (!z4) {
                sb.append(',');
            }
            sb.append(obj);
            z4 = false;
        }
        sb.append(')');
        return sb.toString();
    }
}
