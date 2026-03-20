package com.google.common.base;

import java.io.Serializable;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a<T> implements r<T>, Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        final r<T> f18852a;

        /* renamed from: b  reason: collision with root package name */
        volatile transient boolean f18853b;

        /* renamed from: c  reason: collision with root package name */
        transient T f18854c;

        a(r<T> rVar) {
            this.f18852a = (r) l.n(rVar);
        }

        @Override // com.google.common.base.r
        public T get() {
            if (!this.f18853b) {
                synchronized (this) {
                    if (!this.f18853b) {
                        T t8 = this.f18852a.get();
                        this.f18854c = t8;
                        this.f18853b = true;
                        return t8;
                    }
                }
            }
            return (T) j.a(this.f18854c);
        }

        public String toString() {
            Object obj;
            if (this.f18853b) {
                String valueOf = String.valueOf(this.f18854c);
                StringBuilder sb = new StringBuilder(valueOf.length() + 25);
                sb.append("<supplier that returned ");
                sb.append(valueOf);
                sb.append(">");
                obj = sb.toString();
            } else {
                obj = this.f18852a;
            }
            String valueOf2 = String.valueOf(obj);
            StringBuilder sb2 = new StringBuilder(valueOf2.length() + 19);
            sb2.append("Suppliers.memoize(");
            sb2.append(valueOf2);
            sb2.append(")");
            return sb2.toString();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b<T> implements r<T> {

        /* renamed from: a  reason: collision with root package name */
        volatile r<T> f18855a;

        /* renamed from: b  reason: collision with root package name */
        volatile boolean f18856b;

        /* renamed from: c  reason: collision with root package name */
        T f18857c;

        b(r<T> rVar) {
            this.f18855a = (r) l.n(rVar);
        }

        @Override // com.google.common.base.r
        public T get() {
            if (!this.f18856b) {
                synchronized (this) {
                    if (!this.f18856b) {
                        r<T> rVar = this.f18855a;
                        Objects.requireNonNull(rVar);
                        T t8 = rVar.get();
                        this.f18857c = t8;
                        this.f18856b = true;
                        this.f18855a = null;
                        return t8;
                    }
                }
            }
            return (T) j.a(this.f18857c);
        }

        public String toString() {
            Object obj = this.f18855a;
            if (obj == null) {
                String valueOf = String.valueOf(this.f18857c);
                StringBuilder sb = new StringBuilder(valueOf.length() + 25);
                sb.append("<supplier that returned ");
                sb.append(valueOf);
                sb.append(">");
                obj = sb.toString();
            }
            String valueOf2 = String.valueOf(obj);
            StringBuilder sb2 = new StringBuilder(valueOf2.length() + 19);
            sb2.append("Suppliers.memoize(");
            sb2.append(valueOf2);
            sb2.append(")");
            return sb2.toString();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c<T> implements r<T>, Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        final T f18858a;

        c(T t8) {
            this.f18858a = t8;
        }

        public boolean equals(Object obj) {
            if (obj instanceof c) {
                return k.a(this.f18858a, ((c) obj).f18858a);
            }
            return false;
        }

        @Override // com.google.common.base.r
        public T get() {
            return this.f18858a;
        }

        public int hashCode() {
            return k.b(this.f18858a);
        }

        public String toString() {
            String valueOf = String.valueOf(this.f18858a);
            StringBuilder sb = new StringBuilder(valueOf.length() + 22);
            sb.append("Suppliers.ofInstance(");
            sb.append(valueOf);
            sb.append(")");
            return sb.toString();
        }
    }

    public static <T> r<T> a(r<T> rVar) {
        return ((rVar instanceof b) || (rVar instanceof a)) ? rVar : rVar instanceof Serializable ? new a(rVar) : new b(rVar);
    }

    public static <T> r<T> b(T t8) {
        return new c(t8);
    }
}
