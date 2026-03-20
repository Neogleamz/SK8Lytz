package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Scope;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import n6.k0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b<T extends IInterface> {
    private final a A;
    private final InterfaceC0125b B;
    private final int C;
    private final String E;
    private volatile String F;
    private ConnectionResult G;
    private boolean H;
    private volatile zzk K;
    protected AtomicInteger L;

    /* renamed from: a  reason: collision with root package name */
    private int f11821a;

    /* renamed from: b  reason: collision with root package name */
    private long f11822b;

    /* renamed from: c  reason: collision with root package name */
    private long f11823c;

    /* renamed from: d  reason: collision with root package name */
    private int f11824d;

    /* renamed from: e  reason: collision with root package name */
    private long f11825e;

    /* renamed from: f  reason: collision with root package name */
    private volatile String f11826f;

    /* renamed from: g  reason: collision with root package name */
    b0 f11827g;

    /* renamed from: h  reason: collision with root package name */
    private final Context f11828h;

    /* renamed from: j  reason: collision with root package name */
    private final Looper f11829j;

    /* renamed from: k  reason: collision with root package name */
    private final com.google.android.gms.common.internal.d f11830k;

    /* renamed from: l  reason: collision with root package name */
    private final com.google.android.gms.common.b f11831l;

    /* renamed from: m  reason: collision with root package name */
    final Handler f11832m;

    /* renamed from: n  reason: collision with root package name */
    private final Object f11833n;

    /* renamed from: p  reason: collision with root package name */
    private final Object f11834p;
    private n6.g q;

    /* renamed from: t  reason: collision with root package name */
    protected c f11835t;

    /* renamed from: w  reason: collision with root package name */
    private IInterface f11836w;

    /* renamed from: x  reason: collision with root package name */
    private final ArrayList f11837x;

    /* renamed from: y  reason: collision with root package name */
    private s f11838y;

    /* renamed from: z  reason: collision with root package name */
    private int f11839z;
    private static final Feature[] P = new Feature[0];
    public static final String[] O = {"service_esmobile", "service_googleme"};

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void d(int i8);

        void f(Bundle bundle);
    }

    /* renamed from: com.google.android.gms.common.internal.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface InterfaceC0125b {
        void e(ConnectionResult connectionResult);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        void a(ConnectionResult connectionResult);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    protected class d implements c {
        public d() {
        }

        @Override // com.google.android.gms.common.internal.b.c
        public final void a(ConnectionResult connectionResult) {
            if (connectionResult.E0()) {
                b bVar = b.this;
                bVar.b(null, bVar.A());
            } else if (b.this.B != null) {
                b.this.B.e(connectionResult);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface e {
        void a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public b(android.content.Context r10, android.os.Looper r11, int r12, com.google.android.gms.common.internal.b.a r13, com.google.android.gms.common.internal.b.InterfaceC0125b r14, java.lang.String r15) {
        /*
            r9 = this;
            com.google.android.gms.common.internal.d r3 = com.google.android.gms.common.internal.d.b(r10)
            com.google.android.gms.common.b r4 = com.google.android.gms.common.b.f()
            n6.j.l(r13)
            n6.j.l(r14)
            r0 = r9
            r1 = r10
            r2 = r11
            r5 = r12
            r6 = r13
            r7 = r14
            r8 = r15
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.b.<init>(android.content.Context, android.os.Looper, int, com.google.android.gms.common.internal.b$a, com.google.android.gms.common.internal.b$b, java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public b(Context context, Looper looper, com.google.android.gms.common.internal.d dVar, com.google.android.gms.common.b bVar, int i8, a aVar, InterfaceC0125b interfaceC0125b, String str) {
        this.f11826f = null;
        this.f11833n = new Object();
        this.f11834p = new Object();
        this.f11837x = new ArrayList();
        this.f11839z = 1;
        this.G = null;
        this.H = false;
        this.K = null;
        this.L = new AtomicInteger(0);
        n6.j.m(context, "Context must not be null");
        this.f11828h = context;
        n6.j.m(looper, "Looper must not be null");
        this.f11829j = looper;
        n6.j.m(dVar, "Supervisor must not be null");
        this.f11830k = dVar;
        n6.j.m(bVar, "API availability must not be null");
        this.f11831l = bVar;
        this.f11832m = new p(this, looper);
        this.C = i8;
        this.A = aVar;
        this.B = interfaceC0125b;
        this.E = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ void a0(b bVar, zzk zzkVar) {
        bVar.K = zzkVar;
        if (bVar.Q()) {
            ConnectionTelemetryConfiguration connectionTelemetryConfiguration = zzkVar.f11892d;
            n6.k.b().c(connectionTelemetryConfiguration == null ? null : connectionTelemetryConfiguration.I0());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ void b0(b bVar, int i8) {
        int i9;
        int i10;
        synchronized (bVar.f11833n) {
            i9 = bVar.f11839z;
        }
        if (i9 == 3) {
            bVar.H = true;
            i10 = 5;
        } else {
            i10 = 4;
        }
        Handler handler = bVar.f11832m;
        handler.sendMessage(handler.obtainMessage(i10, bVar.L.get(), 16));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ boolean e0(b bVar, int i8, int i9, IInterface iInterface) {
        synchronized (bVar.f11833n) {
            if (bVar.f11839z != i8) {
                return false;
            }
            bVar.g0(i9, iInterface);
            return true;
        }
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException: Cannot read field "wordsInUse" because "set" is null
        	at java.base/java.util.BitSet.or(BitSet.java:943)
        	at jadx.core.utils.BlockUtils.getPathCross(BlockUtils.java:732)
        	at jadx.core.utils.BlockUtils.getPathCross(BlockUtils.java:811)
        	at jadx.core.dex.visitors.regions.IfMakerHelper.restructureIf(IfMakerHelper.java:88)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:706)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:155)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:94)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:730)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:155)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:94)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    static /* bridge */ /* synthetic */ boolean f0(com.google.android.gms.common.internal.b r2) {
        /*
            boolean r0 = r2.H
            r1 = 0
            if (r0 == 0) goto L6
            goto L24
        L6:
            java.lang.String r0 = r2.C()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L11
            goto L24
        L11:
            java.lang.String r0 = r2.z()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L1c
            goto L24
        L1c:
            java.lang.String r2 = r2.C()     // Catch: java.lang.ClassNotFoundException -> L24
            java.lang.Class.forName(r2)     // Catch: java.lang.ClassNotFoundException -> L24
            r1 = 1
        L24:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.b.f0(com.google.android.gms.common.internal.b):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public final void g0(int i8, IInterface iInterface) {
        b0 b0Var;
        n6.j.a((i8 == 4) == (iInterface != 0));
        synchronized (this.f11833n) {
            this.f11839z = i8;
            this.f11836w = iInterface;
            if (i8 == 1) {
                s sVar = this.f11838y;
                if (sVar != null) {
                    com.google.android.gms.common.internal.d dVar = this.f11830k;
                    String b9 = this.f11827g.b();
                    n6.j.l(b9);
                    dVar.e(b9, this.f11827g.a(), 4225, sVar, V(), this.f11827g.c());
                    this.f11838y = null;
                }
            } else if (i8 == 2 || i8 == 3) {
                s sVar2 = this.f11838y;
                if (sVar2 != null && (b0Var = this.f11827g) != null) {
                    String b10 = b0Var.b();
                    String a9 = b0Var.a();
                    Log.e("GmsClient", "Calling connect() while still connected, missing disconnect() for " + b10 + " on " + a9);
                    com.google.android.gms.common.internal.d dVar2 = this.f11830k;
                    String b11 = this.f11827g.b();
                    n6.j.l(b11);
                    dVar2.e(b11, this.f11827g.a(), 4225, sVar2, V(), this.f11827g.c());
                    this.L.incrementAndGet();
                }
                s sVar3 = new s(this, this.L.get());
                this.f11838y = sVar3;
                b0 b0Var2 = (this.f11839z != 3 || z() == null) ? new b0(E(), D(), false, 4225, G()) : new b0(w().getPackageName(), z(), true, 4225, false);
                this.f11827g = b0Var2;
                if (b0Var2.c() && j() < 17895000) {
                    throw new IllegalStateException("Internal Error, the minimum apk version of this BaseGmsClient is too low to support dynamic lookup. Start service action: ".concat(String.valueOf(this.f11827g.b())));
                }
                com.google.android.gms.common.internal.d dVar3 = this.f11830k;
                String b12 = this.f11827g.b();
                n6.j.l(b12);
                if (!dVar3.f(new k0(b12, this.f11827g.a(), 4225, this.f11827g.c()), sVar3, V(), u())) {
                    String b13 = this.f11827g.b();
                    String a10 = this.f11827g.a();
                    Log.w("GmsClient", "unable to connect to service: " + b13 + " on " + a10);
                    c0(16, null, this.L.get());
                }
            } else if (i8 == 4) {
                n6.j.l(iInterface);
                I(iInterface);
            }
        }
    }

    protected Set<Scope> A() {
        return Collections.emptySet();
    }

    public final T B() {
        T t8;
        synchronized (this.f11833n) {
            if (this.f11839z == 5) {
                throw new DeadObjectException();
            }
            p();
            t8 = (T) this.f11836w;
            n6.j.m(t8, "Client is connected but service is null");
        }
        return t8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract String C();

    protected abstract String D();

    protected String E() {
        return "com.google.android.gms";
    }

    public ConnectionTelemetryConfiguration F() {
        zzk zzkVar = this.K;
        if (zzkVar == null) {
            return null;
        }
        return zzkVar.f11892d;
    }

    protected boolean G() {
        return j() >= 211700000;
    }

    public boolean H() {
        return this.K != null;
    }

    protected void I(T t8) {
        this.f11823c = System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void J(ConnectionResult connectionResult) {
        this.f11824d = connectionResult.t();
        this.f11825e = System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void K(int i8) {
        this.f11821a = i8;
        this.f11822b = System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void L(int i8, IBinder iBinder, Bundle bundle, int i9) {
        this.f11832m.sendMessage(this.f11832m.obtainMessage(1, i9, -1, new t(this, i8, iBinder, bundle)));
    }

    public boolean M() {
        return false;
    }

    public void N(String str) {
        this.F = str;
    }

    public void O(int i8) {
        this.f11832m.sendMessage(this.f11832m.obtainMessage(6, this.L.get(), i8));
    }

    protected void P(c cVar, int i8, PendingIntent pendingIntent) {
        n6.j.m(cVar, "Connection progress callbacks cannot be null.");
        this.f11835t = cVar;
        this.f11832m.sendMessage(this.f11832m.obtainMessage(3, this.L.get(), i8, pendingIntent));
    }

    public boolean Q() {
        return false;
    }

    protected final String V() {
        String str = this.E;
        return str == null ? this.f11828h.getClass().getName() : str;
    }

    public void b(com.google.android.gms.common.internal.e eVar, Set<Scope> set) {
        Bundle y8 = y();
        String str = this.F;
        int i8 = com.google.android.gms.common.b.f11718a;
        Scope[] scopeArr = GetServiceRequest.q;
        Bundle bundle = new Bundle();
        int i9 = this.C;
        Feature[] featureArr = GetServiceRequest.f11782t;
        GetServiceRequest getServiceRequest = new GetServiceRequest(6, i9, i8, null, null, scopeArr, bundle, null, featureArr, featureArr, true, 0, false, str);
        getServiceRequest.f11786d = this.f11828h.getPackageName();
        getServiceRequest.f11789g = y8;
        if (set != null) {
            getServiceRequest.f11788f = (Scope[]) set.toArray(new Scope[0]);
        }
        if (m()) {
            Account s8 = s();
            if (s8 == null) {
                s8 = new Account("<<default account>>", "com.google");
            }
            getServiceRequest.f11790h = s8;
            if (eVar != null) {
                getServiceRequest.f11787e = eVar.asBinder();
            }
        } else if (M()) {
            getServiceRequest.f11790h = s();
        }
        getServiceRequest.f11791j = P;
        getServiceRequest.f11792k = t();
        if (Q()) {
            getServiceRequest.f11795n = true;
        }
        try {
            synchronized (this.f11834p) {
                n6.g gVar = this.q;
                if (gVar != null) {
                    gVar.k1(new r(this, this.L.get()), getServiceRequest);
                } else {
                    Log.w("GmsClient", "mServiceBroker is null, client disconnected");
                }
            }
        } catch (DeadObjectException e8) {
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", e8);
            O(3);
        } catch (RemoteException e9) {
            e = e9;
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", e);
            L(8, null, null, this.L.get());
        } catch (SecurityException e10) {
            throw e10;
        } catch (RuntimeException e11) {
            e = e11;
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", e);
            L(8, null, null, this.L.get());
        }
    }

    public void c(String str) {
        this.f11826f = str;
        disconnect();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void c0(int i8, Bundle bundle, int i9) {
        this.f11832m.sendMessage(this.f11832m.obtainMessage(7, i9, -1, new u(this, i8, null)));
    }

    public void disconnect() {
        this.L.incrementAndGet();
        synchronized (this.f11837x) {
            int size = this.f11837x.size();
            for (int i8 = 0; i8 < size; i8++) {
                ((q) this.f11837x.get(i8)).d();
            }
            this.f11837x.clear();
        }
        synchronized (this.f11834p) {
            this.q = null;
        }
        g0(1, null);
    }

    public boolean e() {
        boolean z4;
        synchronized (this.f11833n) {
            int i8 = this.f11839z;
            z4 = true;
            if (i8 != 2 && i8 != 3) {
                z4 = false;
            }
        }
        return z4;
    }

    public String f() {
        b0 b0Var;
        if (!isConnected() || (b0Var = this.f11827g) == null) {
            throw new RuntimeException("Failed to connect when checking package");
        }
        return b0Var.a();
    }

    public void g(c cVar) {
        n6.j.m(cVar, "Connection progress callbacks cannot be null.");
        this.f11835t = cVar;
        g0(2, null);
    }

    public void h(e eVar) {
        eVar.a();
    }

    public boolean i() {
        return true;
    }

    public boolean isConnected() {
        boolean z4;
        synchronized (this.f11833n) {
            z4 = this.f11839z == 4;
        }
        return z4;
    }

    public int j() {
        return com.google.android.gms.common.b.f11718a;
    }

    public final Feature[] k() {
        zzk zzkVar = this.K;
        if (zzkVar == null) {
            return null;
        }
        return zzkVar.f11890b;
    }

    public String l() {
        return this.f11826f;
    }

    public boolean m() {
        return false;
    }

    public void o() {
        int h8 = this.f11831l.h(this.f11828h, j());
        if (h8 == 0) {
            g(new d());
            return;
        }
        g0(1, null);
        P(new d(), h8, null);
    }

    protected final void p() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract T q(IBinder iBinder);

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean r() {
        return false;
    }

    public Account s() {
        return null;
    }

    public Feature[] t() {
        return P;
    }

    protected Executor u() {
        return null;
    }

    public Bundle v() {
        return null;
    }

    public final Context w() {
        return this.f11828h;
    }

    public int x() {
        return this.C;
    }

    protected Bundle y() {
        return new Bundle();
    }

    protected String z() {
        return null;
    }
}
