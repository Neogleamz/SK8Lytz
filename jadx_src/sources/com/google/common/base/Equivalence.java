package com.google.common.base;

import java.io.Serializable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class Equivalence<T> {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class Wrapper<T> implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private final Equivalence<? super T> f18778a;

        /* renamed from: b  reason: collision with root package name */
        private final T f18779b;

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Wrapper) {
                Wrapper wrapper = (Wrapper) obj;
                if (this.f18778a.equals(wrapper.f18778a)) {
                    return this.f18778a.d((T) this.f18779b, (T) wrapper.f18779b);
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            return this.f18778a.e((T) this.f18779b);
        }

        public String toString() {
            String valueOf = String.valueOf(this.f18778a);
            String valueOf2 = String.valueOf(this.f18779b);
            StringBuilder sb = new StringBuilder(valueOf.length() + 7 + valueOf2.length());
            sb.append(valueOf);
            sb.append(".wrap(");
            sb.append(valueOf2);
            sb.append(")");
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class a extends Equivalence<Object> implements Serializable {

        /* renamed from: a  reason: collision with root package name */
        static final a f18780a = new a();
        private static final long serialVersionUID = 1;

        a() {
        }

        private Object readResolve() {
            return f18780a;
        }

        @Override // com.google.common.base.Equivalence
        protected boolean a(Object obj, Object obj2) {
            return obj.equals(obj2);
        }

        @Override // com.google.common.base.Equivalence
        protected int b(Object obj) {
            return obj.hashCode();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b extends Equivalence<Object> implements Serializable {

        /* renamed from: a  reason: collision with root package name */
        static final b f18781a = new b();
        private static final long serialVersionUID = 1;

        b() {
        }

        private Object readResolve() {
            return f18781a;
        }

        @Override // com.google.common.base.Equivalence
        protected boolean a(Object obj, Object obj2) {
            return false;
        }

        @Override // com.google.common.base.Equivalence
        protected int b(Object obj) {
            return System.identityHashCode(obj);
        }
    }

    protected Equivalence() {
    }

    public static Equivalence<Object> c() {
        return a.f18780a;
    }

    public static Equivalence<Object> f() {
        return b.f18781a;
    }

    protected abstract boolean a(T t8, T t9);

    protected abstract int b(T t8);

    public final boolean d(T t8, T t9) {
        if (t8 == t9) {
            return true;
        }
        if (t8 == null || t9 == null) {
            return false;
        }
        return a(t8, t9);
    }

    public final int e(T t8) {
        if (t8 == null) {
            return 0;
        }
        return b(t8);
    }
}
