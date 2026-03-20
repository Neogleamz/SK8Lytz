package androidx.navigation;

import android.os.Bundle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: a  reason: collision with root package name */
    private final o f6307a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f6308b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f6309c;

    /* renamed from: d  reason: collision with root package name */
    private final Object f6310d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private o<?> f6311a;

        /* renamed from: c  reason: collision with root package name */
        private Object f6313c;

        /* renamed from: b  reason: collision with root package name */
        private boolean f6312b = false;

        /* renamed from: d  reason: collision with root package name */
        private boolean f6314d = false;

        public d a() {
            if (this.f6311a == null) {
                this.f6311a = o.e(this.f6313c);
            }
            return new d(this.f6311a, this.f6312b, this.f6313c, this.f6314d);
        }

        public a b(Object obj) {
            this.f6313c = obj;
            this.f6314d = true;
            return this;
        }

        public a c(boolean z4) {
            this.f6312b = z4;
            return this;
        }

        public a d(o<?> oVar) {
            this.f6311a = oVar;
            return this;
        }
    }

    d(o<?> oVar, boolean z4, Object obj, boolean z8) {
        if (!oVar.f() && z4) {
            throw new IllegalArgumentException(oVar.c() + " does not allow nullable values");
        } else if (!z4 && z8 && obj == null) {
            throw new IllegalArgumentException("Argument with type " + oVar.c() + " has null value but is not nullable.");
        } else {
            this.f6307a = oVar;
            this.f6308b = z4;
            this.f6310d = obj;
            this.f6309c = z8;
        }
    }

    public o<?> a() {
        return this.f6307a;
    }

    public boolean b() {
        return this.f6309c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(String str, Bundle bundle) {
        if (this.f6309c) {
            this.f6307a.i(bundle, str, this.f6310d);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d(String str, Bundle bundle) {
        if (!this.f6308b && bundle.containsKey(str) && bundle.get(str) == null) {
            return false;
        }
        try {
            this.f6307a.b(bundle, str);
            return true;
        } catch (ClassCastException unused) {
            return false;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || d.class != obj.getClass()) {
            return false;
        }
        d dVar = (d) obj;
        if (this.f6308b == dVar.f6308b && this.f6309c == dVar.f6309c && this.f6307a.equals(dVar.f6307a)) {
            Object obj2 = this.f6310d;
            Object obj3 = dVar.f6310d;
            return obj2 != null ? obj2.equals(obj3) : obj3 == null;
        }
        return false;
    }

    public int hashCode() {
        int hashCode = ((((this.f6307a.hashCode() * 31) + (this.f6308b ? 1 : 0)) * 31) + (this.f6309c ? 1 : 0)) * 31;
        Object obj = this.f6310d;
        return hashCode + (obj != null ? obj.hashCode() : 0);
    }
}
