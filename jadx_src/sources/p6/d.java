package p6;

import a7.f;
import android.content.Context;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.api.b;
import com.google.android.gms.common.api.internal.g;
import com.google.android.gms.common.internal.TelemetryData;
import j7.j;
import j7.k;
import l6.i;
import n6.n;
import n6.o;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d extends com.google.android.gms.common.api.b implements n {

    /* renamed from: k  reason: collision with root package name */
    private static final a.g f22433k;

    /* renamed from: l  reason: collision with root package name */
    private static final a.AbstractC0121a f22434l;

    /* renamed from: m  reason: collision with root package name */
    private static final com.google.android.gms.common.api.a f22435m;

    /* renamed from: n  reason: collision with root package name */
    public static final /* synthetic */ int f22436n = 0;

    static {
        a.g gVar = new a.g();
        f22433k = gVar;
        c cVar = new c();
        f22434l = cVar;
        f22435m = new com.google.android.gms.common.api.a("ClientTelemetry.API", cVar, gVar);
    }

    public d(Context context, o oVar) {
        super(context, f22435m, oVar, b.a.f11573c);
    }

    @Override // n6.n
    public final j<Void> a(final TelemetryData telemetryData) {
        g.a a9 = g.a();
        a9.d(f.f196a);
        a9.c(false);
        a9.b(new i() { // from class: p6.b
            @Override // l6.i
            public final void accept(Object obj, Object obj2) {
                TelemetryData telemetryData2 = TelemetryData.this;
                int i8 = d.f22436n;
                ((a) ((e) obj).B()).k(telemetryData2);
                ((k) obj2).c(null);
            }
        });
        return e(a9.a());
    }
}
