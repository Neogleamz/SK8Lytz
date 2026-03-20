package d4;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.AlarmManagerSchedulerBroadcastReceiver;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig;
import e4.d;
import w3.o;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements v {

    /* renamed from: a  reason: collision with root package name */
    private final Context f19703a;

    /* renamed from: b  reason: collision with root package name */
    private final d f19704b;

    /* renamed from: c  reason: collision with root package name */
    private AlarmManager f19705c;

    /* renamed from: d  reason: collision with root package name */
    private final SchedulerConfig f19706d;

    /* renamed from: e  reason: collision with root package name */
    private final g4.a f19707e;

    a(Context context, d dVar, AlarmManager alarmManager, g4.a aVar, SchedulerConfig schedulerConfig) {
        this.f19703a = context;
        this.f19704b = dVar;
        this.f19705c = alarmManager;
        this.f19707e = aVar;
        this.f19706d = schedulerConfig;
    }

    public a(Context context, d dVar, g4.a aVar, SchedulerConfig schedulerConfig) {
        this(context, dVar, (AlarmManager) context.getSystemService("alarm"), aVar, schedulerConfig);
    }

    @Override // d4.v
    public void a(o oVar, int i8) {
        b(oVar, i8, false);
    }

    @Override // d4.v
    public void b(o oVar, int i8, boolean z4) {
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("backendName", oVar.b());
        builder.appendQueryParameter("priority", String.valueOf(h4.a.a(oVar.d())));
        if (oVar.c() != null) {
            builder.appendQueryParameter("extras", Base64.encodeToString(oVar.c(), 0));
        }
        Intent intent = new Intent(this.f19703a, AlarmManagerSchedulerBroadcastReceiver.class);
        intent.setData(builder.build());
        intent.putExtra("attemptNumber", i8);
        if (!z4 && c(intent)) {
            a4.a.b("AlarmManagerScheduler", "Upload for context %s is already scheduled. Returning...", oVar);
            return;
        }
        long T = this.f19704b.T(oVar);
        long g8 = this.f19706d.g(oVar.d(), T, i8);
        a4.a.c("AlarmManagerScheduler", "Scheduling upload for context %s in %dms(Backend next call timestamp %d). Attempt %d", oVar, Long.valueOf(g8), Long.valueOf(T), Integer.valueOf(i8));
        this.f19705c.set(3, this.f19707e.a() + g8, PendingIntent.getBroadcast(this.f19703a, 0, intent, Build.VERSION.SDK_INT >= 23 ? 67108864 : 0));
    }

    boolean c(Intent intent) {
        return PendingIntent.getBroadcast(this.f19703a, 0, intent, Build.VERSION.SDK_INT >= 23 ? 603979776 : 536870912) != null;
    }
}
