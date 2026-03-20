package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.x8;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l8 {

    /* renamed from: b  reason: collision with root package name */
    private static volatile l8 f12296b;

    /* renamed from: c  reason: collision with root package name */
    static final l8 f12297c = new l8(true);

    /* renamed from: a  reason: collision with root package name */
    private final Map<a, x8.d<?, ?>> f12298a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final Object f12299a;

        /* renamed from: b  reason: collision with root package name */
        private final int f12300b;

        a(Object obj, int i8) {
            this.f12299a = obj;
            this.f12300b = i8;
        }

        public final boolean equals(Object obj) {
            if (obj instanceof a) {
                a aVar = (a) obj;
                return this.f12299a == aVar.f12299a && this.f12300b == aVar.f12300b;
            }
            return false;
        }

        public final int hashCode() {
            return (System.identityHashCode(this.f12299a) * 65535) + this.f12300b;
        }
    }

    l8() {
        this.f12298a = new HashMap();
    }

    private l8(boolean z4) {
        this.f12298a = Collections.emptyMap();
    }

    public static l8 a() {
        l8 l8Var = f12296b;
        if (l8Var != null) {
            return l8Var;
        }
        synchronized (l8.class) {
            l8 l8Var2 = f12296b;
            if (l8Var2 != null) {
                return l8Var2;
            }
            l8 b9 = w8.b(l8.class);
            f12296b = b9;
            return b9;
        }
    }

    public final <ContainingType extends ia> x8.d<ContainingType, ?> b(ContainingType containingtype, int i8) {
        return (x8.d<ContainingType, ?>) this.f12298a.get(new a(containingtype, i8));
    }
}
