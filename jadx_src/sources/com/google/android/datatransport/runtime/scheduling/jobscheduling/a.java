package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.google.android.datatransport.Priority;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig;
import java.util.Map;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a extends SchedulerConfig {

    /* renamed from: a  reason: collision with root package name */
    private final g4.a f9118a;

    /* renamed from: b  reason: collision with root package name */
    private final Map<Priority, SchedulerConfig.b> f9119b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(g4.a aVar, Map<Priority, SchedulerConfig.b> map) {
        Objects.requireNonNull(aVar, "Null clock");
        this.f9118a = aVar;
        Objects.requireNonNull(map, "Null values");
        this.f9119b = map;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig
    g4.a e() {
        return this.f9118a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof SchedulerConfig) {
            SchedulerConfig schedulerConfig = (SchedulerConfig) obj;
            return this.f9118a.equals(schedulerConfig.e()) && this.f9119b.equals(schedulerConfig.h());
        }
        return false;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig
    Map<Priority, SchedulerConfig.b> h() {
        return this.f9119b;
    }

    public int hashCode() {
        return ((this.f9118a.hashCode() ^ 1000003) * 1000003) ^ this.f9119b.hashCode();
    }

    public String toString() {
        return "SchedulerConfig{clock=" + this.f9118a + ", values=" + this.f9119b + "}";
    }
}
