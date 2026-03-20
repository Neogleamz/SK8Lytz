package com.google.common.collect;

import com.google.common.collect.z2;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a3 {

    /* renamed from: a  reason: collision with root package name */
    private static final com.google.common.base.g<? extends Map<?, ?>, ? extends Map<?, ?>> f19168a = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements com.google.common.base.g<Map<Object, Object>, Map<Object, Object>> {
        a() {
        }

        @Override // com.google.common.base.g
        /* renamed from: a */
        public Map<Object, Object> apply(Map<Object, Object> map) {
            return Collections.unmodifiableMap(map);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static abstract class b<R, C, V> implements z2.a<R, C, V> {
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof z2.a) {
                z2.a aVar = (z2.a) obj;
                return com.google.common.base.k.a(b(), aVar.b()) && com.google.common.base.k.a(a(), aVar.a()) && com.google.common.base.k.a(getValue(), aVar.getValue());
            }
            return false;
        }

        public int hashCode() {
            return com.google.common.base.k.b(b(), a(), getValue());
        }

        public String toString() {
            String valueOf = String.valueOf(b());
            String valueOf2 = String.valueOf(a());
            String valueOf3 = String.valueOf(getValue());
            StringBuilder sb = new StringBuilder(valueOf.length() + 4 + valueOf2.length() + valueOf3.length());
            sb.append("(");
            sb.append(valueOf);
            sb.append(",");
            sb.append(valueOf2);
            sb.append(")=");
            sb.append(valueOf3);
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class c<R, C, V> extends b<R, C, V> implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private final R f19169a;

        /* renamed from: b  reason: collision with root package name */
        private final C f19170b;

        /* renamed from: c  reason: collision with root package name */
        private final V f19171c;

        c(R r4, C c9, V v8) {
            this.f19169a = r4;
            this.f19170b = c9;
            this.f19171c = v8;
        }

        @Override // com.google.common.collect.z2.a
        public C a() {
            return this.f19170b;
        }

        @Override // com.google.common.collect.z2.a
        public R b() {
            return this.f19169a;
        }

        @Override // com.google.common.collect.z2.a
        public V getValue() {
            return this.f19171c;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(z2<?, ?, ?> z2Var, Object obj) {
        if (obj == z2Var) {
            return true;
        }
        if (obj instanceof z2) {
            return z2Var.a().equals(((z2) obj).a());
        }
        return false;
    }

    public static <R, C, V> z2.a<R, C, V> b(R r4, C c9, V v8) {
        return new c(r4, c9, v8);
    }
}
