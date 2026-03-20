package d4;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.util.Base64;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.JobInfoSchedulerService;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig;
import e4.d;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.zip.Adler32;
import w3.o;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c implements v {

    /* renamed from: a  reason: collision with root package name */
    private final Context f19708a;

    /* renamed from: b  reason: collision with root package name */
    private final d f19709b;

    /* renamed from: c  reason: collision with root package name */
    private final SchedulerConfig f19710c;

    public c(Context context, d dVar, SchedulerConfig schedulerConfig) {
        this.f19708a = context;
        this.f19709b = dVar;
        this.f19710c = schedulerConfig;
    }

    private boolean d(JobScheduler jobScheduler, int i8, int i9) {
        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
            int i10 = jobInfo.getExtras().getInt("attemptNumber");
            if (jobInfo.getId() == i8) {
                return i10 >= i9;
            }
        }
        return false;
    }

    @Override // d4.v
    public void a(o oVar, int i8) {
        b(oVar, i8, false);
    }

    @Override // d4.v
    public void b(o oVar, int i8, boolean z4) {
        ComponentName componentName = new ComponentName(this.f19708a, JobInfoSchedulerService.class);
        JobScheduler jobScheduler = (JobScheduler) this.f19708a.getSystemService("jobscheduler");
        int c9 = c(oVar);
        if (!z4 && d(jobScheduler, c9, i8)) {
            a4.a.b("JobInfoScheduler", "Upload for context %s is already scheduled. Returning...", oVar);
            return;
        }
        long T = this.f19709b.T(oVar);
        JobInfo.Builder c10 = this.f19710c.c(new JobInfo.Builder(c9, componentName), oVar.d(), T, i8);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putInt("attemptNumber", i8);
        persistableBundle.putString("backendName", oVar.b());
        persistableBundle.putInt("priority", h4.a.a(oVar.d()));
        if (oVar.c() != null) {
            persistableBundle.putString("extras", Base64.encodeToString(oVar.c(), 0));
        }
        c10.setExtras(persistableBundle);
        a4.a.c("JobInfoScheduler", "Scheduling upload for context %s with jobId=%d in %dms(Backend next call timestamp %d). Attempt %d", oVar, Integer.valueOf(c9), Long.valueOf(this.f19710c.g(oVar.d(), T, i8)), Long.valueOf(T), Integer.valueOf(i8));
        jobScheduler.schedule(c10.build());
    }

    int c(o oVar) {
        Adler32 adler32 = new Adler32();
        adler32.update(this.f19708a.getPackageName().getBytes(Charset.forName("UTF-8")));
        adler32.update(oVar.b().getBytes(Charset.forName("UTF-8")));
        adler32.update(ByteBuffer.allocate(4).putInt(h4.a.a(oVar.d())).array());
        if (oVar.c() != null) {
            adler32.update(oVar.c());
        }
        return (int) adler32.getValue();
    }
}
