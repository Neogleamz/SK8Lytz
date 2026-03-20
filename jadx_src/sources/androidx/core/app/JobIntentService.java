package androidx.core.app;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobServiceEngine;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import java.util.ArrayList;
import java.util.HashMap;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class JobIntentService extends Service {

    /* renamed from: h  reason: collision with root package name */
    static final Object f4414h = new Object();

    /* renamed from: j  reason: collision with root package name */
    static final HashMap<ComponentName, h> f4415j = new HashMap<>();

    /* renamed from: a  reason: collision with root package name */
    b f4416a;

    /* renamed from: b  reason: collision with root package name */
    h f4417b;

    /* renamed from: c  reason: collision with root package name */
    a f4418c;

    /* renamed from: d  reason: collision with root package name */
    boolean f4419d = false;

    /* renamed from: e  reason: collision with root package name */
    boolean f4420e = false;

    /* renamed from: f  reason: collision with root package name */
    boolean f4421f = false;

    /* renamed from: g  reason: collision with root package name */
    final ArrayList<d> f4422g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class a extends AsyncTask<Void, Void, Void> {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: a */
        public Void doInBackground(Void... voidArr) {
            while (true) {
                e a9 = JobIntentService.this.a();
                if (a9 == null) {
                    return null;
                }
                JobIntentService.this.e(a9.getIntent());
                a9.a();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: b */
        public void onCancelled(Void r12) {
            JobIntentService.this.g();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: c */
        public void onPostExecute(Void r12) {
            JobIntentService.this.g();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        IBinder a();

        e b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c extends h {

        /* renamed from: d  reason: collision with root package name */
        private final Context f4424d;

        /* renamed from: e  reason: collision with root package name */
        private final PowerManager.WakeLock f4425e;

        /* renamed from: f  reason: collision with root package name */
        private final PowerManager.WakeLock f4426f;

        /* renamed from: g  reason: collision with root package name */
        boolean f4427g;

        /* renamed from: h  reason: collision with root package name */
        boolean f4428h;

        c(Context context, ComponentName componentName) {
            super(componentName);
            this.f4424d = context.getApplicationContext();
            PowerManager powerManager = (PowerManager) context.getSystemService("power");
            PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(1, componentName.getClassName() + ":launch");
            this.f4425e = newWakeLock;
            newWakeLock.setReferenceCounted(false);
            PowerManager.WakeLock newWakeLock2 = powerManager.newWakeLock(1, componentName.getClassName() + ":run");
            this.f4426f = newWakeLock2;
            newWakeLock2.setReferenceCounted(false);
        }

        @Override // androidx.core.app.JobIntentService.h
        public void b() {
            synchronized (this) {
                if (this.f4428h) {
                    if (this.f4427g) {
                        this.f4425e.acquire(60000L);
                    }
                    this.f4428h = false;
                    this.f4426f.release();
                }
            }
        }

        @Override // androidx.core.app.JobIntentService.h
        public void c() {
            synchronized (this) {
                if (!this.f4428h) {
                    this.f4428h = true;
                    this.f4426f.acquire(600000L);
                    this.f4425e.release();
                }
            }
        }

        @Override // androidx.core.app.JobIntentService.h
        public void d() {
            synchronized (this) {
                this.f4427g = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class d implements e {

        /* renamed from: a  reason: collision with root package name */
        final Intent f4429a;

        /* renamed from: b  reason: collision with root package name */
        final int f4430b;

        d(Intent intent, int i8) {
            this.f4429a = intent;
            this.f4430b = i8;
        }

        @Override // androidx.core.app.JobIntentService.e
        public void a() {
            JobIntentService.this.stopSelf(this.f4430b);
        }

        @Override // androidx.core.app.JobIntentService.e
        public Intent getIntent() {
            return this.f4429a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface e {
        void a();

        Intent getIntent();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class f extends JobServiceEngine implements b {

        /* renamed from: a  reason: collision with root package name */
        final JobIntentService f4432a;

        /* renamed from: b  reason: collision with root package name */
        final Object f4433b;

        /* renamed from: c  reason: collision with root package name */
        JobParameters f4434c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        final class a implements e {

            /* renamed from: a  reason: collision with root package name */
            final JobWorkItem f4435a;

            a(JobWorkItem jobWorkItem) {
                this.f4435a = jobWorkItem;
            }

            @Override // androidx.core.app.JobIntentService.e
            public void a() {
                synchronized (f.this.f4433b) {
                    JobParameters jobParameters = f.this.f4434c;
                    if (jobParameters != null) {
                        jobParameters.completeWork(this.f4435a);
                    }
                }
            }

            @Override // androidx.core.app.JobIntentService.e
            public Intent getIntent() {
                return this.f4435a.getIntent();
            }
        }

        f(JobIntentService jobIntentService) {
            super(jobIntentService);
            this.f4433b = new Object();
            this.f4432a = jobIntentService;
        }

        @Override // androidx.core.app.JobIntentService.b
        public IBinder a() {
            return getBinder();
        }

        @Override // androidx.core.app.JobIntentService.b
        public e b() {
            synchronized (this.f4433b) {
                JobParameters jobParameters = this.f4434c;
                if (jobParameters == null) {
                    return null;
                }
                JobWorkItem dequeueWork = jobParameters.dequeueWork();
                if (dequeueWork != null) {
                    dequeueWork.getIntent().setExtrasClassLoader(this.f4432a.getClassLoader());
                    return new a(dequeueWork);
                }
                return null;
            }
        }

        @Override // android.app.job.JobServiceEngine
        public boolean onStartJob(JobParameters jobParameters) {
            this.f4434c = jobParameters;
            this.f4432a.c(false);
            return true;
        }

        @Override // android.app.job.JobServiceEngine
        public boolean onStopJob(JobParameters jobParameters) {
            boolean b9 = this.f4432a.b();
            synchronized (this.f4433b) {
                this.f4434c = null;
            }
            return b9;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g extends h {

        /* renamed from: d  reason: collision with root package name */
        private final JobInfo f4437d;

        /* renamed from: e  reason: collision with root package name */
        private final JobScheduler f4438e;

        g(Context context, ComponentName componentName, int i8) {
            super(componentName);
            a(i8);
            this.f4437d = new JobInfo.Builder(i8, this.f4439a).setOverrideDeadline(0L).build();
            this.f4438e = (JobScheduler) context.getApplicationContext().getSystemService("jobscheduler");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class h {

        /* renamed from: a  reason: collision with root package name */
        final ComponentName f4439a;

        /* renamed from: b  reason: collision with root package name */
        boolean f4440b;

        /* renamed from: c  reason: collision with root package name */
        int f4441c;

        h(ComponentName componentName) {
            this.f4439a = componentName;
        }

        void a(int i8) {
            if (!this.f4440b) {
                this.f4440b = true;
                this.f4441c = i8;
            } else if (this.f4441c == i8) {
            } else {
                throw new IllegalArgumentException("Given job ID " + i8 + " is different than previous " + this.f4441c);
            }
        }

        public void b() {
        }

        public void c() {
        }

        public void d() {
        }
    }

    public JobIntentService() {
        this.f4422g = Build.VERSION.SDK_INT >= 26 ? null : new ArrayList<>();
    }

    static h d(Context context, ComponentName componentName, boolean z4, int i8) {
        h cVar;
        HashMap<ComponentName, h> hashMap = f4415j;
        h hVar = hashMap.get(componentName);
        if (hVar == null) {
            if (Build.VERSION.SDK_INT < 26) {
                cVar = new c(context, componentName);
            } else if (!z4) {
                throw new IllegalArgumentException("Can't be here without a job id");
            } else {
                cVar = new g(context, componentName, i8);
            }
            h hVar2 = cVar;
            hashMap.put(componentName, hVar2);
            return hVar2;
        }
        return hVar;
    }

    e a() {
        b bVar = this.f4416a;
        if (bVar != null) {
            return bVar.b();
        }
        synchronized (this.f4422g) {
            if (this.f4422g.size() > 0) {
                return this.f4422g.remove(0);
            }
            return null;
        }
    }

    boolean b() {
        a aVar = this.f4418c;
        if (aVar != null) {
            aVar.cancel(this.f4419d);
        }
        this.f4420e = true;
        return f();
    }

    void c(boolean z4) {
        if (this.f4418c == null) {
            this.f4418c = new a();
            h hVar = this.f4417b;
            if (hVar != null && z4) {
                hVar.c();
            }
            this.f4418c.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    protected abstract void e(Intent intent);

    public boolean f() {
        return true;
    }

    void g() {
        ArrayList<d> arrayList = this.f4422g;
        if (arrayList != null) {
            synchronized (arrayList) {
                this.f4418c = null;
                ArrayList<d> arrayList2 = this.f4422g;
                if (arrayList2 != null && arrayList2.size() > 0) {
                    c(false);
                } else if (!this.f4421f) {
                    this.f4417b.b();
                }
            }
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        b bVar = this.f4416a;
        if (bVar != null) {
            return bVar.a();
        }
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            this.f4416a = new f(this);
            this.f4417b = null;
            return;
        }
        this.f4416a = null;
        this.f4417b = d(this, new ComponentName(this, getClass()), false, 0);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        ArrayList<d> arrayList = this.f4422g;
        if (arrayList != null) {
            synchronized (arrayList) {
                this.f4421f = true;
                this.f4417b.b();
            }
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i8, int i9) {
        if (this.f4422g != null) {
            this.f4417b.d();
            synchronized (this.f4422g) {
                ArrayList<d> arrayList = this.f4422g;
                if (intent == null) {
                    intent = new Intent();
                }
                arrayList.add(new d(intent, i9));
                c(true);
            }
            return 3;
        }
        return 2;
    }
}
