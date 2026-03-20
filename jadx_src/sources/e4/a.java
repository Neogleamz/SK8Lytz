package e4;

import com.daimajia.numberprogressbar.BuildConfig;
import e4.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a extends e {

    /* renamed from: b  reason: collision with root package name */
    private final long f19752b;

    /* renamed from: c  reason: collision with root package name */
    private final int f19753c;

    /* renamed from: d  reason: collision with root package name */
    private final int f19754d;

    /* renamed from: e  reason: collision with root package name */
    private final long f19755e;

    /* renamed from: f  reason: collision with root package name */
    private final int f19756f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b extends e.a {

        /* renamed from: a  reason: collision with root package name */
        private Long f19757a;

        /* renamed from: b  reason: collision with root package name */
        private Integer f19758b;

        /* renamed from: c  reason: collision with root package name */
        private Integer f19759c;

        /* renamed from: d  reason: collision with root package name */
        private Long f19760d;

        /* renamed from: e  reason: collision with root package name */
        private Integer f19761e;

        @Override // e4.e.a
        e a() {
            Long l8 = this.f19757a;
            String str = BuildConfig.FLAVOR;
            if (l8 == null) {
                str = BuildConfig.FLAVOR + " maxStorageSizeInBytes";
            }
            if (this.f19758b == null) {
                str = str + " loadBatchSize";
            }
            if (this.f19759c == null) {
                str = str + " criticalSectionEnterTimeoutMs";
            }
            if (this.f19760d == null) {
                str = str + " eventCleanUpAge";
            }
            if (this.f19761e == null) {
                str = str + " maxBlobByteSizePerRow";
            }
            if (str.isEmpty()) {
                return new a(this.f19757a.longValue(), this.f19758b.intValue(), this.f19759c.intValue(), this.f19760d.longValue(), this.f19761e.intValue());
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override // e4.e.a
        e.a b(int i8) {
            this.f19759c = Integer.valueOf(i8);
            return this;
        }

        @Override // e4.e.a
        e.a c(long j8) {
            this.f19760d = Long.valueOf(j8);
            return this;
        }

        @Override // e4.e.a
        e.a d(int i8) {
            this.f19758b = Integer.valueOf(i8);
            return this;
        }

        @Override // e4.e.a
        e.a e(int i8) {
            this.f19761e = Integer.valueOf(i8);
            return this;
        }

        @Override // e4.e.a
        e.a f(long j8) {
            this.f19757a = Long.valueOf(j8);
            return this;
        }
    }

    private a(long j8, int i8, int i9, long j9, int i10) {
        this.f19752b = j8;
        this.f19753c = i8;
        this.f19754d = i9;
        this.f19755e = j9;
        this.f19756f = i10;
    }

    @Override // e4.e
    int b() {
        return this.f19754d;
    }

    @Override // e4.e
    long c() {
        return this.f19755e;
    }

    @Override // e4.e
    int d() {
        return this.f19753c;
    }

    @Override // e4.e
    int e() {
        return this.f19756f;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof e) {
            e eVar = (e) obj;
            return this.f19752b == eVar.f() && this.f19753c == eVar.d() && this.f19754d == eVar.b() && this.f19755e == eVar.c() && this.f19756f == eVar.e();
        }
        return false;
    }

    @Override // e4.e
    long f() {
        return this.f19752b;
    }

    public int hashCode() {
        long j8 = this.f19752b;
        long j9 = this.f19755e;
        return ((((((((((int) (j8 ^ (j8 >>> 32))) ^ 1000003) * 1000003) ^ this.f19753c) * 1000003) ^ this.f19754d) * 1000003) ^ ((int) ((j9 >>> 32) ^ j9))) * 1000003) ^ this.f19756f;
    }

    public String toString() {
        return "EventStoreConfig{maxStorageSizeInBytes=" + this.f19752b + ", loadBatchSize=" + this.f19753c + ", criticalSectionEnterTimeoutMs=" + this.f19754d + ", eventCleanUpAge=" + this.f19755e + ", maxBlobByteSizePerRow=" + this.f19756f + "}";
    }
}
