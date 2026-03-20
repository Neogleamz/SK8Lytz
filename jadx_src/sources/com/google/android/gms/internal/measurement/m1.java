package com.google.android.gms.internal.measurement;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Build;
import android.os.UserHandle;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
@TargetApi(24)
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m1 {

    /* renamed from: b  reason: collision with root package name */
    private static final Method f12332b = e();

    /* renamed from: c  reason: collision with root package name */
    private static final Method f12333c = d();

    /* renamed from: a  reason: collision with root package name */
    private final JobScheduler f12334a;

    private m1(JobScheduler jobScheduler) {
        this.f12334a = jobScheduler;
    }

    private static int a() {
        Method method = f12333c;
        if (method != null) {
            try {
                Integer num = (Integer) method.invoke(UserHandle.class, new Object[0]);
                if (num != null) {
                    return num.intValue();
                }
                return 0;
            } catch (IllegalAccessException | InvocationTargetException e8) {
                if (Log.isLoggable("JobSchedulerCompat", 6)) {
                    Log.e("JobSchedulerCompat", "myUserId invocation illegal", e8);
                }
            }
        }
        return 0;
    }

    private final int b(JobInfo jobInfo, String str, int i8, String str2) {
        Method method = f12332b;
        if (method != null) {
            try {
                Integer num = (Integer) method.invoke(this.f12334a, jobInfo, str, Integer.valueOf(i8), str2);
                if (num != null) {
                    return num.intValue();
                }
                return 0;
            } catch (IllegalAccessException | InvocationTargetException e8) {
                Log.e(str2, "error calling scheduleAsPackage", e8);
            }
        }
        return this.f12334a.schedule(jobInfo);
    }

    public static int c(Context context, JobInfo jobInfo, String str, String str2) {
        JobScheduler jobScheduler = (JobScheduler) com.google.common.base.l.n((JobScheduler) context.getSystemService("jobscheduler"));
        return (f12332b == null || context.checkSelfPermission("android.permission.UPDATE_DEVICE_STATS") != 0) ? jobScheduler.schedule(jobInfo) : new m1(jobScheduler).b(jobInfo, str, a(), str2);
    }

    private static Method d() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                return UserHandle.class.getDeclaredMethod("myUserId", new Class[0]);
            } catch (NoSuchMethodException unused) {
                if (Log.isLoggable("JobSchedulerCompat", 6)) {
                    Log.e("JobSchedulerCompat", "No myUserId method available");
                    return null;
                }
                return null;
            }
        }
        return null;
    }

    private static Method e() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                return JobScheduler.class.getDeclaredMethod("scheduleAsPackage", JobInfo.class, String.class, Integer.TYPE, String.class);
            } catch (NoSuchMethodException unused) {
                if (Log.isLoggable("JobSchedulerCompat", 6)) {
                    Log.e("JobSchedulerCompat", "No scheduleAsPackage method available, falling back to schedule");
                    return null;
                }
                return null;
            }
        }
        return null;
    }
}
