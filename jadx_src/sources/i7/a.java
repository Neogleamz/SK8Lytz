package i7;

import android.content.Context;
import android.os.PowerManager;
import android.os.WorkSource;
import android.text.TextUtils;
import android.util.Log;
import c7.h;
import com.google.android.gms.internal.stats.zzi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import n6.j;
import u6.g;
import u6.o;
import u6.q;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: r  reason: collision with root package name */
    private static final long f20548r = TimeUnit.DAYS.toMillis(366);

    /* renamed from: s  reason: collision with root package name */
    private static volatile ScheduledExecutorService f20549s = null;

    /* renamed from: t  reason: collision with root package name */
    private static final Object f20550t = new Object();

    /* renamed from: u  reason: collision with root package name */
    private static volatile e f20551u = new c();

    /* renamed from: a  reason: collision with root package name */
    private final Object f20552a;

    /* renamed from: b  reason: collision with root package name */
    private final PowerManager.WakeLock f20553b;

    /* renamed from: c  reason: collision with root package name */
    private int f20554c;

    /* renamed from: d  reason: collision with root package name */
    private Future<?> f20555d;

    /* renamed from: e  reason: collision with root package name */
    private long f20556e;

    /* renamed from: f  reason: collision with root package name */
    private final Set<f> f20557f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f20558g;

    /* renamed from: h  reason: collision with root package name */
    private int f20559h;

    /* renamed from: i  reason: collision with root package name */
    c7.b f20560i;

    /* renamed from: j  reason: collision with root package name */
    private u6.d f20561j;

    /* renamed from: k  reason: collision with root package name */
    private WorkSource f20562k;

    /* renamed from: l  reason: collision with root package name */
    private final String f20563l;

    /* renamed from: m  reason: collision with root package name */
    private final String f20564m;

    /* renamed from: n  reason: collision with root package name */
    private final Context f20565n;

    /* renamed from: o  reason: collision with root package name */
    private final Map<String, d> f20566o;

    /* renamed from: p  reason: collision with root package name */
    private AtomicInteger f20567p;
    private final ScheduledExecutorService q;

    public a(Context context, int i8, String str) {
        String packageName = context.getPackageName();
        this.f20552a = new Object();
        this.f20554c = 0;
        this.f20557f = new HashSet();
        this.f20558g = true;
        this.f20561j = g.d();
        this.f20566o = new HashMap();
        this.f20567p = new AtomicInteger(0);
        j.m(context, "WakeLock: context must not be null");
        j.g(str, "WakeLock: wakeLockName must not be empty");
        this.f20565n = context.getApplicationContext();
        this.f20564m = str;
        this.f20560i = null;
        if ("com.google.android.gms".equals(context.getPackageName())) {
            this.f20563l = str;
        } else {
            String valueOf = String.valueOf(str);
            this.f20563l = valueOf.length() != 0 ? "*gcore*:".concat(valueOf) : new String("*gcore*:");
        }
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        if (powerManager == null) {
            StringBuilder sb = new StringBuilder(29);
            sb.append((CharSequence) "expected a non-null reference", 0, 29);
            throw new zzi(sb.toString());
        }
        PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(i8, str);
        this.f20553b = newWakeLock;
        if (q.c(context)) {
            WorkSource b9 = q.b(context, o.a(packageName) ? context.getPackageName() : packageName);
            this.f20562k = b9;
            if (b9 != null) {
                i(newWakeLock, b9);
            }
        }
        ScheduledExecutorService scheduledExecutorService = f20549s;
        if (scheduledExecutorService == null) {
            synchronized (f20550t) {
                scheduledExecutorService = f20549s;
                if (scheduledExecutorService == null) {
                    h.a();
                    scheduledExecutorService = Executors.unconfigurableScheduledExecutorService(Executors.newScheduledThreadPool(1));
                    f20549s = scheduledExecutorService;
                }
            }
        }
        this.q = scheduledExecutorService;
    }

    public static /* synthetic */ void e(a aVar) {
        synchronized (aVar.f20552a) {
            if (aVar.b()) {
                Log.e("WakeLock", String.valueOf(aVar.f20563l).concat(" ** IS FORCE-RELEASED ON TIMEOUT **"));
                aVar.g();
                if (aVar.b()) {
                    aVar.f20554c = 1;
                    aVar.h(0);
                }
            }
        }
    }

    private final String f(String str) {
        if (this.f20558g) {
            TextUtils.isEmpty(null);
        }
        return null;
    }

    private final void g() {
        if (this.f20557f.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList(this.f20557f);
        this.f20557f.clear();
        if (arrayList.size() <= 0) {
            return;
        }
        f fVar = (f) arrayList.get(0);
        throw null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x005d, code lost:
        if (r5.f20560i != null) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x005f, code lost:
        r5.f20560i = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0084, code lost:
        if (r5.f20560i != null) goto L31;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void h(int r6) {
        /*
            r5 = this;
            java.lang.Object r6 = r5.f20552a
            monitor-enter(r6)
            boolean r0 = r5.b()     // Catch: java.lang.Throwable -> La2
            if (r0 != 0) goto Lb
            monitor-exit(r6)     // Catch: java.lang.Throwable -> La2
            return
        Lb:
            boolean r0 = r5.f20558g     // Catch: java.lang.Throwable -> La2
            r1 = 0
            if (r0 == 0) goto L1b
            int r0 = r5.f20554c     // Catch: java.lang.Throwable -> La2
            int r0 = r0 + (-1)
            r5.f20554c = r0     // Catch: java.lang.Throwable -> La2
            if (r0 > 0) goto L19
            goto L1d
        L19:
            monitor-exit(r6)     // Catch: java.lang.Throwable -> La2
            return
        L1b:
            r5.f20554c = r1     // Catch: java.lang.Throwable -> La2
        L1d:
            r5.g()     // Catch: java.lang.Throwable -> La2
            java.util.Map<java.lang.String, i7.d> r0 = r5.f20566o     // Catch: java.lang.Throwable -> La2
            java.util.Collection r0 = r0.values()     // Catch: java.lang.Throwable -> La2
            java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Throwable -> La2
        L2a:
            boolean r2 = r0.hasNext()     // Catch: java.lang.Throwable -> La2
            if (r2 == 0) goto L39
            java.lang.Object r2 = r0.next()     // Catch: java.lang.Throwable -> La2
            i7.d r2 = (i7.d) r2     // Catch: java.lang.Throwable -> La2
            r2.f20569a = r1     // Catch: java.lang.Throwable -> La2
            goto L2a
        L39:
            java.util.Map<java.lang.String, i7.d> r0 = r5.f20566o     // Catch: java.lang.Throwable -> La2
            r0.clear()     // Catch: java.lang.Throwable -> La2
            java.util.concurrent.Future<?> r0 = r5.f20555d     // Catch: java.lang.Throwable -> La2
            r2 = 0
            if (r0 == 0) goto L4c
            r0.cancel(r1)     // Catch: java.lang.Throwable -> La2
            r5.f20555d = r2     // Catch: java.lang.Throwable -> La2
            r3 = 0
            r5.f20556e = r3     // Catch: java.lang.Throwable -> La2
        L4c:
            r5.f20559h = r1     // Catch: java.lang.Throwable -> La2
            android.os.PowerManager$WakeLock r0 = r5.f20553b     // Catch: java.lang.Throwable -> La2
            boolean r0 = r0.isHeld()     // Catch: java.lang.Throwable -> La2
            if (r0 == 0) goto L8f
            android.os.PowerManager$WakeLock r0 = r5.f20553b     // Catch: java.lang.Throwable -> L62 java.lang.RuntimeException -> L64
            r0.release()     // Catch: java.lang.Throwable -> L62 java.lang.RuntimeException -> L64
            c7.b r0 = r5.f20560i     // Catch: java.lang.Throwable -> La2
            if (r0 == 0) goto La0
        L5f:
            r5.f20560i = r2     // Catch: java.lang.Throwable -> La2
            goto La0
        L62:
            r0 = move-exception
            goto L88
        L64:
            r0 = move-exception
            java.lang.Class r1 = r0.getClass()     // Catch: java.lang.Throwable -> L62
            java.lang.Class<java.lang.RuntimeException> r3 = java.lang.RuntimeException.class
            boolean r1 = r1.equals(r3)     // Catch: java.lang.Throwable -> L62
            if (r1 == 0) goto L87
            java.lang.String r1 = "WakeLock"
            java.lang.String r3 = r5.f20563l     // Catch: java.lang.Throwable -> L62
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch: java.lang.Throwable -> L62
            java.lang.String r4 = " failed to release!"
            java.lang.String r3 = r3.concat(r4)     // Catch: java.lang.Throwable -> L62
            android.util.Log.e(r1, r3, r0)     // Catch: java.lang.Throwable -> L62
            c7.b r0 = r5.f20560i     // Catch: java.lang.Throwable -> La2
            if (r0 == 0) goto La0
            goto L5f
        L87:
            throw r0     // Catch: java.lang.Throwable -> L62
        L88:
            c7.b r1 = r5.f20560i     // Catch: java.lang.Throwable -> La2
            if (r1 == 0) goto L8e
            r5.f20560i = r2     // Catch: java.lang.Throwable -> La2
        L8e:
            throw r0     // Catch: java.lang.Throwable -> La2
        L8f:
            java.lang.String r0 = "WakeLock"
            java.lang.String r1 = r5.f20563l     // Catch: java.lang.Throwable -> La2
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch: java.lang.Throwable -> La2
            java.lang.String r2 = " should be held!"
            java.lang.String r1 = r1.concat(r2)     // Catch: java.lang.Throwable -> La2
            android.util.Log.e(r0, r1)     // Catch: java.lang.Throwable -> La2
        La0:
            monitor-exit(r6)     // Catch: java.lang.Throwable -> La2
            return
        La2:
            r0 = move-exception
            monitor-exit(r6)     // Catch: java.lang.Throwable -> La2
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: i7.a.h(int):void");
    }

    private static void i(PowerManager.WakeLock wakeLock, WorkSource workSource) {
        try {
            wakeLock.setWorkSource(workSource);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e8) {
            Log.wtf("WakeLock", e8.toString());
        }
    }

    public void a(long j8) {
        this.f20567p.incrementAndGet();
        long max = Math.max(Math.min(Long.MAX_VALUE, f20548r), 1L);
        if (j8 > 0) {
            max = Math.min(j8, max);
        }
        synchronized (this.f20552a) {
            if (!b()) {
                this.f20560i = c7.b.a(false, null);
                this.f20553b.acquire();
                this.f20561j.b();
            }
            this.f20554c++;
            this.f20559h++;
            f(null);
            d dVar = this.f20566o.get(null);
            if (dVar == null) {
                dVar = new d(null);
                this.f20566o.put(null, dVar);
            }
            dVar.f20569a++;
            long b9 = this.f20561j.b();
            long j9 = Long.MAX_VALUE - b9 > max ? b9 + max : Long.MAX_VALUE;
            if (j9 > this.f20556e) {
                this.f20556e = j9;
                Future<?> future = this.f20555d;
                if (future != null) {
                    future.cancel(false);
                }
                this.f20555d = this.q.schedule(new Runnable() { // from class: i7.b
                    @Override // java.lang.Runnable
                    public final void run() {
                        a.e(a.this);
                    }
                }, max, TimeUnit.MILLISECONDS);
            }
        }
    }

    public boolean b() {
        boolean z4;
        synchronized (this.f20552a) {
            z4 = this.f20554c > 0;
        }
        return z4;
    }

    public void c() {
        if (this.f20567p.decrementAndGet() < 0) {
            Log.e("WakeLock", String.valueOf(this.f20563l).concat(" release without a matched acquire!"));
        }
        synchronized (this.f20552a) {
            f(null);
            if (this.f20566o.containsKey(null)) {
                d dVar = this.f20566o.get(null);
                if (dVar != null) {
                    int i8 = dVar.f20569a - 1;
                    dVar.f20569a = i8;
                    if (i8 == 0) {
                        this.f20566o.remove(null);
                    }
                }
            } else {
                Log.w("WakeLock", String.valueOf(this.f20563l).concat(" counter does not exist"));
            }
            h(0);
        }
    }

    public void d(boolean z4) {
        synchronized (this.f20552a) {
            this.f20558g = z4;
        }
    }
}
