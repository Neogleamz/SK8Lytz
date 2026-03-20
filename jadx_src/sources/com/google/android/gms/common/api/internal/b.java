package com.google.android.gms.common.api.internal;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.c;
import com.google.android.gms.common.internal.MethodInvocation;
import com.google.android.gms.common.internal.RootTelemetryConfiguration;
import com.google.android.gms.common.internal.TelemetryData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b implements Handler.Callback {
    private static b A;

    /* renamed from: x  reason: collision with root package name */
    public static final Status f11602x = new Status(4, "Sign-out occurred while this API call was in progress.");

    /* renamed from: y  reason: collision with root package name */
    private static final Status f11603y = new Status(4, "The user must be signed in to make this API call.");

    /* renamed from: z  reason: collision with root package name */
    private static final Object f11604z = new Object();

    /* renamed from: e  reason: collision with root package name */
    private TelemetryData f11609e;

    /* renamed from: f  reason: collision with root package name */
    private n6.n f11610f;

    /* renamed from: g  reason: collision with root package name */
    private final Context f11611g;

    /* renamed from: h  reason: collision with root package name */
    private final com.google.android.gms.common.a f11612h;

    /* renamed from: j  reason: collision with root package name */
    private final n6.y f11613j;

    /* renamed from: t  reason: collision with root package name */
    private final Handler f11619t;

    /* renamed from: w  reason: collision with root package name */
    private volatile boolean f11620w;

    /* renamed from: a  reason: collision with root package name */
    private long f11605a = 5000;

    /* renamed from: b  reason: collision with root package name */
    private long f11606b = 120000;

    /* renamed from: c  reason: collision with root package name */
    private long f11607c = 10000;

    /* renamed from: d  reason: collision with root package name */
    private boolean f11608d = false;

    /* renamed from: k  reason: collision with root package name */
    private final AtomicInteger f11614k = new AtomicInteger(1);

    /* renamed from: l  reason: collision with root package name */
    private final AtomicInteger f11615l = new AtomicInteger(0);

    /* renamed from: m  reason: collision with root package name */
    private final Map f11616m = new ConcurrentHashMap(5, 0.75f, 1);

    /* renamed from: n  reason: collision with root package name */
    private k f11617n = null;

    /* renamed from: p  reason: collision with root package name */
    private final Set f11618p = new k0.b();
    private final Set q = new k0.b();

    private b(Context context, Looper looper, com.google.android.gms.common.a aVar) {
        this.f11620w = true;
        this.f11611g = context;
        a7.k kVar = new a7.k(looper, this);
        this.f11619t = kVar;
        this.f11612h = aVar;
        this.f11613j = new n6.y(aVar);
        if (u6.h.a(context)) {
            this.f11620w = false;
        }
        kVar.sendMessage(kVar.obtainMessage(6));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Status h(l6.b bVar, ConnectionResult connectionResult) {
        String b9 = bVar.b();
        String valueOf = String.valueOf(connectionResult);
        return new Status(connectionResult, "API: " + b9 + " is not available on this device. Connection failed with: " + valueOf);
    }

    private final r i(com.google.android.gms.common.api.b bVar) {
        l6.b i8 = bVar.i();
        r rVar = (r) this.f11616m.get(i8);
        if (rVar == null) {
            rVar = new r(this, bVar);
            this.f11616m.put(i8, rVar);
        }
        if (rVar.P()) {
            this.q.add(i8);
        }
        rVar.E();
        return rVar;
    }

    private final n6.n j() {
        if (this.f11610f == null) {
            this.f11610f = n6.m.a(this.f11611g);
        }
        return this.f11610f;
    }

    private final void k() {
        TelemetryData telemetryData = this.f11609e;
        if (telemetryData != null) {
            if (telemetryData.t() > 0 || f()) {
                j().a(telemetryData);
            }
            this.f11609e = null;
        }
    }

    private final void l(j7.k kVar, int i8, com.google.android.gms.common.api.b bVar) {
        w b9;
        if (i8 == 0 || (b9 = w.b(this, i8, bVar.i())) == null) {
            return;
        }
        j7.j a9 = kVar.a();
        final Handler handler = this.f11619t;
        handler.getClass();
        a9.c(new Executor() { // from class: l6.m
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                handler.post(runnable);
            }
        }, b9);
    }

    public static b x(Context context) {
        b bVar;
        synchronized (f11604z) {
            if (A == null) {
                A = new b(context.getApplicationContext(), com.google.android.gms.common.internal.d.c().getLooper(), com.google.android.gms.common.a.m());
            }
            bVar = A;
        }
        return bVar;
    }

    public final j7.j A(com.google.android.gms.common.api.b bVar, c.a aVar, int i8) {
        j7.k kVar = new j7.k();
        l(kVar, i8, bVar);
        f0 f0Var = new f0(aVar, kVar);
        Handler handler = this.f11619t;
        handler.sendMessage(handler.obtainMessage(13, new l6.t(f0Var, this.f11615l.get(), bVar)));
        return kVar.a();
    }

    public final void F(com.google.android.gms.common.api.b bVar, int i8, g gVar, j7.k kVar, l6.j jVar) {
        l(kVar, gVar.d(), bVar);
        e0 e0Var = new e0(i8, gVar, kVar, jVar);
        Handler handler = this.f11619t;
        handler.sendMessage(handler.obtainMessage(4, new l6.t(e0Var, this.f11615l.get(), bVar)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void G(MethodInvocation methodInvocation, int i8, long j8, int i9) {
        Handler handler = this.f11619t;
        handler.sendMessage(handler.obtainMessage(18, new x(methodInvocation, i8, j8, i9)));
    }

    public final void H(ConnectionResult connectionResult, int i8) {
        if (g(connectionResult, i8)) {
            return;
        }
        Handler handler = this.f11619t;
        handler.sendMessage(handler.obtainMessage(5, i8, 0, connectionResult));
    }

    public final void a() {
        Handler handler = this.f11619t;
        handler.sendMessage(handler.obtainMessage(3));
    }

    public final void b(com.google.android.gms.common.api.b bVar) {
        Handler handler = this.f11619t;
        handler.sendMessage(handler.obtainMessage(7, bVar));
    }

    public final void c(k kVar) {
        synchronized (f11604z) {
            if (this.f11617n != kVar) {
                this.f11617n = kVar;
                this.f11618p.clear();
            }
            this.f11618p.addAll(kVar.t());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void d(k kVar) {
        synchronized (f11604z) {
            if (this.f11617n == kVar) {
                this.f11617n = null;
                this.f11618p.clear();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean f() {
        if (this.f11608d) {
            return false;
        }
        RootTelemetryConfiguration a9 = n6.k.b().a();
        if (a9 == null || a9.Z()) {
            int a10 = this.f11613j.a(this.f11611g, 203400000);
            return a10 == -1 || a10 == 0;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean g(ConnectionResult connectionResult, int i8) {
        return this.f11612h.w(this.f11611g, connectionResult, i8);
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        r rVar;
        j7.k b9;
        Boolean valueOf;
        l6.b bVar;
        l6.b bVar2;
        l6.b bVar3;
        l6.b bVar4;
        int i8 = message.what;
        switch (i8) {
            case 1:
                this.f11607c = true == ((Boolean) message.obj).booleanValue() ? 10000L : 300000L;
                this.f11619t.removeMessages(12);
                for (l6.b bVar5 : this.f11616m.keySet()) {
                    Handler handler = this.f11619t;
                    handler.sendMessageDelayed(handler.obtainMessage(12, bVar5), this.f11607c);
                }
                break;
            case 2:
                l6.d0 d0Var = (l6.d0) message.obj;
                Iterator it = d0Var.a().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    } else {
                        l6.b bVar6 = (l6.b) it.next();
                        r rVar2 = (r) this.f11616m.get(bVar6);
                        if (rVar2 == null) {
                            d0Var.b(bVar6, new ConnectionResult(13), null);
                            break;
                        } else if (rVar2.O()) {
                            d0Var.b(bVar6, ConnectionResult.f11523e, rVar2.v().f());
                        } else {
                            ConnectionResult t8 = rVar2.t();
                            if (t8 != null) {
                                d0Var.b(bVar6, t8, null);
                            } else {
                                rVar2.J(d0Var);
                                rVar2.E();
                            }
                        }
                    }
                }
            case 3:
                for (r rVar3 : this.f11616m.values()) {
                    rVar3.D();
                    rVar3.E();
                }
                break;
            case 4:
            case 8:
            case 13:
                l6.t tVar = (l6.t) message.obj;
                r rVar4 = (r) this.f11616m.get(tVar.f21778c.i());
                if (rVar4 == null) {
                    rVar4 = i(tVar.f21778c);
                }
                if (!rVar4.P() || this.f11615l.get() == tVar.f21777b) {
                    rVar4.F(tVar.f21776a);
                    break;
                } else {
                    tVar.f21776a.a(f11602x);
                    rVar4.L();
                    break;
                }
                break;
            case 5:
                int i9 = message.arg1;
                ConnectionResult connectionResult = (ConnectionResult) message.obj;
                Iterator it2 = this.f11616m.values().iterator();
                while (true) {
                    if (it2.hasNext()) {
                        r rVar5 = (r) it2.next();
                        rVar = rVar5.r() == i9 ? rVar5 : null;
                    }
                }
                if (rVar != null) {
                    if (connectionResult.t() == 13) {
                        String e8 = this.f11612h.e(connectionResult.t());
                        String u8 = connectionResult.u();
                        r.y(rVar, new Status(17, "Error resolution was canceled by the user, original error message: " + e8 + ": " + u8));
                        break;
                    } else {
                        r.y(rVar, h(r.w(rVar), connectionResult));
                        break;
                    }
                } else {
                    Log.wtf("GoogleApiManager", "Could not find API instance " + i9 + " while trying to fail enqueued calls.", new Exception());
                    break;
                }
            case 6:
                if (this.f11611g.getApplicationContext() instanceof Application) {
                    a.c((Application) this.f11611g.getApplicationContext());
                    a.b().a(new m(this));
                    if (!a.b().e(true)) {
                        this.f11607c = 300000L;
                        break;
                    }
                }
                break;
            case 7:
                i((com.google.android.gms.common.api.b) message.obj);
                break;
            case 9:
                if (this.f11616m.containsKey(message.obj)) {
                    ((r) this.f11616m.get(message.obj)).K();
                    break;
                }
                break;
            case 10:
                for (l6.b bVar7 : this.q) {
                    r rVar6 = (r) this.f11616m.remove(bVar7);
                    if (rVar6 != null) {
                        rVar6.L();
                    }
                }
                this.q.clear();
                break;
            case 11:
                if (this.f11616m.containsKey(message.obj)) {
                    ((r) this.f11616m.get(message.obj)).M();
                    break;
                }
                break;
            case 12:
                if (this.f11616m.containsKey(message.obj)) {
                    ((r) this.f11616m.get(message.obj)).a();
                    break;
                }
                break;
            case 14:
                l lVar = (l) message.obj;
                l6.b a9 = lVar.a();
                if (this.f11616m.containsKey(a9)) {
                    boolean N = r.N((r) this.f11616m.get(a9), false);
                    b9 = lVar.b();
                    valueOf = Boolean.valueOf(N);
                } else {
                    b9 = lVar.b();
                    valueOf = Boolean.FALSE;
                }
                b9.c(valueOf);
                break;
            case 15:
                s sVar = (s) message.obj;
                Map map = this.f11616m;
                bVar = sVar.f11693a;
                if (map.containsKey(bVar)) {
                    Map map2 = this.f11616m;
                    bVar2 = sVar.f11693a;
                    r.B((r) map2.get(bVar2), sVar);
                    break;
                }
                break;
            case 16:
                s sVar2 = (s) message.obj;
                Map map3 = this.f11616m;
                bVar3 = sVar2.f11693a;
                if (map3.containsKey(bVar3)) {
                    Map map4 = this.f11616m;
                    bVar4 = sVar2.f11693a;
                    r.C((r) map4.get(bVar4), sVar2);
                    break;
                }
                break;
            case 17:
                k();
                break;
            case 18:
                x xVar = (x) message.obj;
                if (xVar.f11712c == 0) {
                    j().a(new TelemetryData(xVar.f11711b, Arrays.asList(xVar.f11710a)));
                    break;
                } else {
                    TelemetryData telemetryData = this.f11609e;
                    if (telemetryData != null) {
                        List u9 = telemetryData.u();
                        if (telemetryData.t() != xVar.f11711b || (u9 != null && u9.size() >= xVar.f11713d)) {
                            this.f11619t.removeMessages(17);
                            k();
                        } else {
                            this.f11609e.Z(xVar.f11710a);
                        }
                    }
                    if (this.f11609e == null) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(xVar.f11710a);
                        this.f11609e = new TelemetryData(xVar.f11711b, arrayList);
                        Handler handler2 = this.f11619t;
                        handler2.sendMessageDelayed(handler2.obtainMessage(17), xVar.f11712c);
                        break;
                    }
                }
                break;
            case 19:
                this.f11608d = false;
                break;
            default:
                Log.w("GoogleApiManager", "Unknown message id: " + i8);
                return false;
        }
        return true;
    }

    public final int m() {
        return this.f11614k.getAndIncrement();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final r w(l6.b bVar) {
        return (r) this.f11616m.get(bVar);
    }

    public final j7.j z(com.google.android.gms.common.api.b bVar, e eVar, h hVar, Runnable runnable) {
        j7.k kVar = new j7.k();
        l(kVar, eVar.e(), bVar);
        d0 d0Var = new d0(new l6.u(eVar, hVar, runnable), kVar);
        Handler handler = this.f11619t;
        handler.sendMessage(handler.obtainMessage(8, new l6.t(d0Var, this.f11615l.get(), bVar)));
        return kVar.a();
    }
}
