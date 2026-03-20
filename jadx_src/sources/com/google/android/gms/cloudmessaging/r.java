package com.google.android.gms.cloudmessaging;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r {

    /* renamed from: e */
    private static r f11513e;

    /* renamed from: a */
    private final Context f11514a;

    /* renamed from: b */
    private final ScheduledExecutorService f11515b;

    /* renamed from: c */
    private m f11516c = new m(this, null);

    /* renamed from: d */
    private int f11517d = 1;

    r(Context context, ScheduledExecutorService scheduledExecutorService) {
        this.f11515b = scheduledExecutorService;
        this.f11514a = context.getApplicationContext();
    }

    public static /* bridge */ /* synthetic */ Context a(r rVar) {
        return rVar.f11514a;
    }

    public static synchronized r b(Context context) {
        r rVar;
        synchronized (r.class) {
            if (f11513e == null) {
                b7.e.a();
                f11513e = new r(context, Executors.unconfigurableScheduledExecutorService(Executors.newScheduledThreadPool(1, new v6.b("MessengerIpcClient"))));
            }
            rVar = f11513e;
        }
        return rVar;
    }

    public static /* bridge */ /* synthetic */ ScheduledExecutorService e(r rVar) {
        return rVar.f11515b;
    }

    private final synchronized int f() {
        int i8;
        i8 = this.f11517d;
        this.f11517d = i8 + 1;
        return i8;
    }

    private final synchronized j7.j g(p pVar) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            Log.d("MessengerIpcClient", "Queueing ".concat(pVar.toString()));
        }
        if (!this.f11516c.g(pVar)) {
            m mVar = new m(this, null);
            this.f11516c = mVar;
            mVar.g(pVar);
        }
        return pVar.f11510b.a();
    }

    public final j7.j c(int i8, Bundle bundle) {
        return g(new o(f(), i8, bundle));
    }

    public final j7.j d(int i8, Bundle bundle) {
        return g(new q(f(), 1, bundle));
    }
}
