package c4;

import android.content.Context;
import android.os.Build;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig;
import d4.v;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class h {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static v a(Context context, e4.d dVar, SchedulerConfig schedulerConfig, g4.a aVar) {
        return Build.VERSION.SDK_INT >= 21 ? new d4.c(context, dVar, schedulerConfig) : new d4.a(context, dVar, aVar, schedulerConfig);
    }
}
