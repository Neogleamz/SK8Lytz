package c4;

import android.content.Context;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig;
import d4.v;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i implements y3.b<v> {

    /* renamed from: a  reason: collision with root package name */
    private final bj.a<Context> f8319a;

    /* renamed from: b  reason: collision with root package name */
    private final bj.a<e4.d> f8320b;

    /* renamed from: c  reason: collision with root package name */
    private final bj.a<SchedulerConfig> f8321c;

    /* renamed from: d  reason: collision with root package name */
    private final bj.a<g4.a> f8322d;

    public i(bj.a<Context> aVar, bj.a<e4.d> aVar2, bj.a<SchedulerConfig> aVar3, bj.a<g4.a> aVar4) {
        this.f8319a = aVar;
        this.f8320b = aVar2;
        this.f8321c = aVar3;
        this.f8322d = aVar4;
    }

    public static i a(bj.a<Context> aVar, bj.a<e4.d> aVar2, bj.a<SchedulerConfig> aVar3, bj.a<g4.a> aVar4) {
        return new i(aVar, aVar2, aVar3, aVar4);
    }

    public static v c(Context context, e4.d dVar, SchedulerConfig schedulerConfig, g4.a aVar) {
        return (v) y3.d.c(h.a(context, dVar, schedulerConfig, aVar), "Cannot return null from a non-@Nullable @Provides method");
    }

    /* renamed from: b */
    public v get() {
        return c((Context) this.f8319a.get(), (e4.d) this.f8320b.get(), (SchedulerConfig) this.f8321c.get(), (g4.a) this.f8322d.get());
    }
}
