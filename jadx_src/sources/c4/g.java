package c4;

import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g implements y3.b<SchedulerConfig> {

    /* renamed from: a  reason: collision with root package name */
    private final bj.a<g4.a> f8318a;

    public g(bj.a<g4.a> aVar) {
        this.f8318a = aVar;
    }

    public static SchedulerConfig a(g4.a aVar) {
        return (SchedulerConfig) y3.d.c(f.a(aVar), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static g b(bj.a<g4.a> aVar) {
        return new g(aVar);
    }

    /* renamed from: c */
    public SchedulerConfig get() {
        return a((g4.a) this.f8318a.get());
    }
}
