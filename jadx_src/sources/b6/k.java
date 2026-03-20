package b6;

import android.util.SparseBooleanArray;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k {

    /* renamed from: a  reason: collision with root package name */
    private final SparseBooleanArray f8059a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private final SparseBooleanArray f8060a = new SparseBooleanArray();

        /* renamed from: b  reason: collision with root package name */
        private boolean f8061b;

        public b a(int i8) {
            b6.a.f(!this.f8061b);
            this.f8060a.append(i8, true);
            return this;
        }

        public b b(k kVar) {
            for (int i8 = 0; i8 < kVar.c(); i8++) {
                a(kVar.b(i8));
            }
            return this;
        }

        public b c(int... iArr) {
            for (int i8 : iArr) {
                a(i8);
            }
            return this;
        }

        public b d(int i8, boolean z4) {
            return z4 ? a(i8) : this;
        }

        public k e() {
            b6.a.f(!this.f8061b);
            this.f8061b = true;
            return new k(this.f8060a);
        }
    }

    private k(SparseBooleanArray sparseBooleanArray) {
        this.f8059a = sparseBooleanArray;
    }

    public boolean a(int i8) {
        return this.f8059a.get(i8);
    }

    public int b(int i8) {
        b6.a.c(i8, 0, c());
        return this.f8059a.keyAt(i8);
    }

    public int c() {
        return this.f8059a.size();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof k) {
            k kVar = (k) obj;
            if (l0.f8063a < 24) {
                if (c() != kVar.c()) {
                    return false;
                }
                for (int i8 = 0; i8 < c(); i8++) {
                    if (b(i8) != kVar.b(i8)) {
                        return false;
                    }
                }
                return true;
            }
            return this.f8059a.equals(kVar.f8059a);
        }
        return false;
    }

    public int hashCode() {
        if (l0.f8063a < 24) {
            int c9 = c();
            for (int i8 = 0; i8 < c(); i8++) {
                c9 = (c9 * 31) + b(i8);
            }
            return c9;
        }
        return this.f8059a.hashCode();
    }
}
