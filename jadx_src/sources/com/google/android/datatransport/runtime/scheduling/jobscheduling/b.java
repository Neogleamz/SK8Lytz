package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig;
import java.util.Objects;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b extends SchedulerConfig.b {

    /* renamed from: a  reason: collision with root package name */
    private final long f9120a;

    /* renamed from: b  reason: collision with root package name */
    private final long f9121b;

    /* renamed from: c  reason: collision with root package name */
    private final Set<SchedulerConfig.Flag> f9122c;

    /* renamed from: com.google.android.datatransport.runtime.scheduling.jobscheduling.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class C0103b extends SchedulerConfig.b.a {

        /* renamed from: a  reason: collision with root package name */
        private Long f9123a;

        /* renamed from: b  reason: collision with root package name */
        private Long f9124b;

        /* renamed from: c  reason: collision with root package name */
        private Set<SchedulerConfig.Flag> f9125c;

        @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.b.a
        public SchedulerConfig.b a() {
            Long l8 = this.f9123a;
            String str = BuildConfig.FLAVOR;
            if (l8 == null) {
                str = BuildConfig.FLAVOR + " delta";
            }
            if (this.f9124b == null) {
                str = str + " maxAllowedDelay";
            }
            if (this.f9125c == null) {
                str = str + " flags";
            }
            if (str.isEmpty()) {
                return new b(this.f9123a.longValue(), this.f9124b.longValue(), this.f9125c);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.b.a
        public SchedulerConfig.b.a b(long j8) {
            this.f9123a = Long.valueOf(j8);
            return this;
        }

        @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.b.a
        public SchedulerConfig.b.a c(Set<SchedulerConfig.Flag> set) {
            Objects.requireNonNull(set, "Null flags");
            this.f9125c = set;
            return this;
        }

        @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.b.a
        public SchedulerConfig.b.a d(long j8) {
            this.f9124b = Long.valueOf(j8);
            return this;
        }
    }

    private b(long j8, long j9, Set<SchedulerConfig.Flag> set) {
        this.f9120a = j8;
        this.f9121b = j9;
        this.f9122c = set;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.b
    long b() {
        return this.f9120a;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.b
    Set<SchedulerConfig.Flag> c() {
        return this.f9122c;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.b
    long d() {
        return this.f9121b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof SchedulerConfig.b) {
            SchedulerConfig.b bVar = (SchedulerConfig.b) obj;
            return this.f9120a == bVar.b() && this.f9121b == bVar.d() && this.f9122c.equals(bVar.c());
        }
        return false;
    }

    public int hashCode() {
        long j8 = this.f9120a;
        long j9 = this.f9121b;
        return ((((((int) (j8 ^ (j8 >>> 32))) ^ 1000003) * 1000003) ^ ((int) ((j9 >>> 32) ^ j9))) * 1000003) ^ this.f9122c.hashCode();
    }

    public String toString() {
        return "ConfigValue{delta=" + this.f9120a + ", maxAllowedDelay=" + this.f9121b + ", flags=" + this.f9122c + "}";
    }
}
