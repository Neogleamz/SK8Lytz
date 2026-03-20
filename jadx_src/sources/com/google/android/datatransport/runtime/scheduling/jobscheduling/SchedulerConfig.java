package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import android.app.job.JobInfo;
import com.google.android.datatransport.Priority;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.b;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class SchedulerConfig {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum Flag {
        NETWORK_UNMETERED,
        DEVICE_IDLE,
        DEVICE_CHARGING
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private g4.a f9116a;

        /* renamed from: b  reason: collision with root package name */
        private Map<Priority, b> f9117b = new HashMap();

        public a a(Priority priority, b bVar) {
            this.f9117b.put(priority, bVar);
            return this;
        }

        public SchedulerConfig b() {
            Objects.requireNonNull(this.f9116a, "missing required property: clock");
            if (this.f9117b.keySet().size() >= Priority.values().length) {
                Map<Priority, b> map = this.f9117b;
                this.f9117b = new HashMap();
                return SchedulerConfig.d(this.f9116a, map);
            }
            throw new IllegalStateException("Not all priorities have been configured");
        }

        public a c(g4.a aVar) {
            this.f9116a = aVar;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class b {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static abstract class a {
            public abstract b a();

            public abstract a b(long j8);

            public abstract a c(Set<Flag> set);

            public abstract a d(long j8);
        }

        public static a a() {
            return new b.C0103b().c(Collections.emptySet());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract long b();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract Set<Flag> c();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract long d();
    }

    private long a(int i8, long j8) {
        int i9;
        return (long) (Math.pow(3.0d, i8 - 1) * j8 * Math.max(1.0d, Math.log(10000.0d) / Math.log((j8 > 1 ? j8 : 2L) * i9)));
    }

    public static a b() {
        return new a();
    }

    static SchedulerConfig d(g4.a aVar, Map<Priority, b> map) {
        return new com.google.android.datatransport.runtime.scheduling.jobscheduling.a(aVar, map);
    }

    public static SchedulerConfig f(g4.a aVar) {
        return b().a(Priority.DEFAULT, b.a().b(30000L).d(86400000L).a()).a(Priority.HIGHEST, b.a().b(1000L).d(86400000L).a()).a(Priority.VERY_LOW, b.a().b(86400000L).d(86400000L).c(i(Flag.DEVICE_IDLE)).a()).c(aVar).b();
    }

    private static <T> Set<T> i(T... tArr) {
        return Collections.unmodifiableSet(new HashSet(Arrays.asList(tArr)));
    }

    private void j(JobInfo.Builder builder, Set<Flag> set) {
        if (set.contains(Flag.NETWORK_UNMETERED)) {
            builder.setRequiredNetworkType(2);
        } else {
            builder.setRequiredNetworkType(1);
        }
        if (set.contains(Flag.DEVICE_CHARGING)) {
            builder.setRequiresCharging(true);
        }
        if (set.contains(Flag.DEVICE_IDLE)) {
            builder.setRequiresDeviceIdle(true);
        }
    }

    public JobInfo.Builder c(JobInfo.Builder builder, Priority priority, long j8, int i8) {
        builder.setMinimumLatency(g(priority, j8, i8));
        j(builder, h().get(priority).c());
        return builder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract g4.a e();

    public long g(Priority priority, long j8, int i8) {
        long a9 = j8 - e().a();
        b bVar = h().get(priority);
        return Math.min(Math.max(a(i8, bVar.b()), a9), bVar.d());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Map<Priority, b> h();
}
