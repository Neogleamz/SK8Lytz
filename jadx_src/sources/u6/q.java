package u6;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Process;
import android.os.WorkSource;
import android.util.Log;
import com.daimajia.numberprogressbar.BuildConfig;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class q {

    /* renamed from: a  reason: collision with root package name */
    private static final int f23080a = Process.myUid();

    /* renamed from: b  reason: collision with root package name */
    private static final Method f23081b;

    /* renamed from: c  reason: collision with root package name */
    private static final Method f23082c;

    /* renamed from: d  reason: collision with root package name */
    private static final Method f23083d;

    /* renamed from: e  reason: collision with root package name */
    private static final Method f23084e;

    /* renamed from: f  reason: collision with root package name */
    private static final Method f23085f;

    /* renamed from: g  reason: collision with root package name */
    private static final Method f23086g;

    /* renamed from: h  reason: collision with root package name */
    private static final Method f23087h;

    /* renamed from: i  reason: collision with root package name */
    private static final Method f23088i;

    /* renamed from: j  reason: collision with root package name */
    private static Boolean f23089j;

    /* JADX WARN: Can't wrap try/catch for region: R(26:1|2|3|4|(22:54|55|7|8|9|10|11|12|13|(13:46|47|16|(10:41|42|19|(7:36|37|22|(6:28|29|30|31|25|26)|24|25|26)|21|22|(0)|24|25|26)|18|19|(0)|21|22|(0)|24|25|26)|15|16|(0)|18|19|(0)|21|22|(0)|24|25|26)|6|7|8|9|10|11|12|13|(0)|15|16|(0)|18|19|(0)|21|22|(0)|24|25|26) */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0041, code lost:
        r1 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0053, code lost:
        r1 = null;
     */
    /* JADX WARN: Removed duplicated region for block: B:50:0x005c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0090 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00b4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0076 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    static {
        /*
            java.lang.Class<java.lang.String> r0 = java.lang.String.class
            java.lang.String r1 = "add"
            int r2 = android.os.Process.myUid()
            u6.q.f23080a = r2
            r2 = 1
            r3 = 0
            r4 = 0
            java.lang.Class<android.os.WorkSource> r5 = android.os.WorkSource.class
            java.lang.Class[] r6 = new java.lang.Class[r2]     // Catch: java.lang.Exception -> L1a
            java.lang.Class r7 = java.lang.Integer.TYPE     // Catch: java.lang.Exception -> L1a
            r6[r3] = r7     // Catch: java.lang.Exception -> L1a
            java.lang.reflect.Method r5 = r5.getMethod(r1, r6)     // Catch: java.lang.Exception -> L1a
            goto L1b
        L1a:
            r5 = r4
        L1b:
            u6.q.f23081b = r5
            boolean r5 = u6.m.c()
            r6 = 2
            if (r5 == 0) goto L33
            java.lang.Class<android.os.WorkSource> r5 = android.os.WorkSource.class
            java.lang.Class[] r7 = new java.lang.Class[r6]     // Catch: java.lang.Exception -> L33
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch: java.lang.Exception -> L33
            r7[r3] = r8     // Catch: java.lang.Exception -> L33
            r7[r2] = r0     // Catch: java.lang.Exception -> L33
            java.lang.reflect.Method r1 = r5.getMethod(r1, r7)     // Catch: java.lang.Exception -> L33
            goto L34
        L33:
            r1 = r4
        L34:
            u6.q.f23082c = r1
            java.lang.Class<android.os.WorkSource> r1 = android.os.WorkSource.class
            java.lang.String r5 = "size"
            java.lang.Class[] r7 = new java.lang.Class[r3]     // Catch: java.lang.Exception -> L41
            java.lang.reflect.Method r1 = r1.getMethod(r5, r7)     // Catch: java.lang.Exception -> L41
            goto L42
        L41:
            r1 = r4
        L42:
            u6.q.f23083d = r1
            java.lang.Class<android.os.WorkSource> r1 = android.os.WorkSource.class
            java.lang.String r5 = "get"
            java.lang.Class[] r7 = new java.lang.Class[r2]     // Catch: java.lang.Exception -> L53
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch: java.lang.Exception -> L53
            r7[r3] = r8     // Catch: java.lang.Exception -> L53
            java.lang.reflect.Method r1 = r1.getMethod(r5, r7)     // Catch: java.lang.Exception -> L53
            goto L54
        L53:
            r1 = r4
        L54:
            u6.q.f23084e = r1
            boolean r1 = u6.m.c()
            if (r1 == 0) goto L6b
            java.lang.Class<android.os.WorkSource> r1 = android.os.WorkSource.class
            java.lang.String r5 = "getName"
            java.lang.Class[] r7 = new java.lang.Class[r2]     // Catch: java.lang.Exception -> L6b
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch: java.lang.Exception -> L6b
            r7[r3] = r8     // Catch: java.lang.Exception -> L6b
            java.lang.reflect.Method r1 = r1.getMethod(r5, r7)     // Catch: java.lang.Exception -> L6b
            goto L6c
        L6b:
            r1 = r4
        L6c:
            u6.q.f23085f = r1
            boolean r1 = u6.m.i()
            java.lang.String r5 = "WorkSourceUtil"
            if (r1 == 0) goto L87
            java.lang.Class<android.os.WorkSource> r1 = android.os.WorkSource.class
            java.lang.String r7 = "createWorkChain"
            java.lang.Class[] r8 = new java.lang.Class[r3]     // Catch: java.lang.Exception -> L81
            java.lang.reflect.Method r1 = r1.getMethod(r7, r8)     // Catch: java.lang.Exception -> L81
            goto L88
        L81:
            r1 = move-exception
            java.lang.String r7 = "Missing WorkChain API createWorkChain"
            android.util.Log.w(r5, r7, r1)
        L87:
            r1 = r4
        L88:
            u6.q.f23086g = r1
            boolean r1 = u6.m.i()
            if (r1 == 0) goto Lab
            java.lang.String r1 = "android.os.WorkSource$WorkChain"
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch: java.lang.Exception -> La5
            java.lang.String r7 = "addNode"
            java.lang.Class[] r6 = new java.lang.Class[r6]     // Catch: java.lang.Exception -> La5
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch: java.lang.Exception -> La5
            r6[r3] = r8     // Catch: java.lang.Exception -> La5
            r6[r2] = r0     // Catch: java.lang.Exception -> La5
            java.lang.reflect.Method r0 = r1.getMethod(r7, r6)     // Catch: java.lang.Exception -> La5
            goto Lac
        La5:
            r0 = move-exception
            java.lang.String r1 = "Missing WorkChain class"
            android.util.Log.w(r5, r1, r0)
        Lab:
            r0 = r4
        Lac:
            u6.q.f23087h = r0
            boolean r0 = u6.m.i()
            if (r0 == 0) goto Lc2
            java.lang.Class<android.os.WorkSource> r0 = android.os.WorkSource.class
            java.lang.String r1 = "isEmpty"
            java.lang.Class[] r3 = new java.lang.Class[r3]     // Catch: java.lang.Exception -> Lc2
            java.lang.reflect.Method r0 = r0.getMethod(r1, r3)     // Catch: java.lang.Exception -> Lc2
            r0.setAccessible(r2)     // Catch: java.lang.Exception -> Lc3
            goto Lc3
        Lc2:
            r0 = r4
        Lc3:
            u6.q.f23088i = r0
            u6.q.f23089j = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: u6.q.<clinit>():void");
    }

    public static void a(WorkSource workSource, int i8, String str) {
        Method method = f23082c;
        if (method != null) {
            if (str == null) {
                str = BuildConfig.FLAVOR;
            }
            try {
                method.invoke(workSource, Integer.valueOf(i8), str);
                return;
            } catch (Exception e8) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e8);
                return;
            }
        }
        Method method2 = f23081b;
        if (method2 != null) {
            try {
                method2.invoke(workSource, Integer.valueOf(i8));
            } catch (Exception e9) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e9);
            }
        }
    }

    public static WorkSource b(Context context, String str) {
        String str2;
        ApplicationInfo c9;
        if (context == null || context.getPackageManager() == null || str == null) {
            return null;
        }
        try {
            c9 = w6.c.a(context).c(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            str2 = "Could not find package: ";
        }
        if (c9 == null) {
            str2 = "Could not get applicationInfo from package: ";
            Log.e("WorkSourceUtil", str2.concat(str));
            return null;
        }
        int i8 = c9.uid;
        WorkSource workSource = new WorkSource();
        a(workSource, i8, str);
        return workSource;
    }

    public static synchronized boolean c(Context context) {
        synchronized (q.class) {
            Boolean bool = f23089j;
            if (bool != null) {
                return bool.booleanValue();
            }
            if (context == null) {
                return false;
            }
            Boolean valueOf = Boolean.valueOf(androidx.core.content.a.a(context, "android.permission.UPDATE_DEVICE_STATS") == 0);
            f23089j = valueOf;
            return valueOf.booleanValue();
        }
    }
}
