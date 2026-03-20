package com.google.android.gms.measurement.internal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.gms.common.util.DynamiteApi;
import com.google.android.gms.internal.measurement.wf;
import com.google.android.gms.internal.measurement.zzdq;
import java.util.Map;
@DynamiteApi
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AppMeasurementDynamiteService extends com.google.android.gms.internal.measurement.f2 {

    /* renamed from: a  reason: collision with root package name */
    f6 f16288a = null;

    /* renamed from: b  reason: collision with root package name */
    private final Map<Integer, f7.r> f16289b = new k0.a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements f7.p {

        /* renamed from: a  reason: collision with root package name */
        private com.google.android.gms.internal.measurement.i2 f16290a;

        a(com.google.android.gms.internal.measurement.i2 i2Var) {
            this.f16290a = i2Var;
        }

        @Override // f7.p
        public final void a(String str, String str2, Bundle bundle, long j8) {
            try {
                this.f16290a.g1(str, str2, bundle, j8);
            } catch (RemoteException e8) {
                f6 f6Var = AppMeasurementDynamiteService.this.f16288a;
                if (f6Var != null) {
                    f6Var.i().J().b("Event interceptor threw exception", e8);
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements f7.r {

        /* renamed from: a  reason: collision with root package name */
        private com.google.android.gms.internal.measurement.i2 f16292a;

        b(com.google.android.gms.internal.measurement.i2 i2Var) {
            this.f16292a = i2Var;
        }

        @Override // f7.r
        public final void a(String str, String str2, Bundle bundle, long j8) {
            try {
                this.f16292a.g1(str, str2, bundle, j8);
            } catch (RemoteException e8) {
                f6 f6Var = AppMeasurementDynamiteService.this.f16288a;
                if (f6Var != null) {
                    f6Var.i().J().b("Event listener threw exception", e8);
                }
            }
        }
    }

    private final void e() {
        if (this.f16288a == null) {
            throw new IllegalStateException("Attempting to perform action before initialize.");
        }
    }

    private final void f(com.google.android.gms.internal.measurement.h2 h2Var, String str) {
        e();
        this.f16288a.J().Q(h2Var, str);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void beginAdUnitExposure(String str, long j8) {
        e();
        this.f16288a.w().x(str, j8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void clearConditionalUserProperty(String str, String str2, Bundle bundle) {
        e();
        this.f16288a.F().W(str, str2, bundle);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void clearMeasurementEnabled(long j8) {
        e();
        this.f16288a.F().Q(null);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void endAdUnitExposure(String str, long j8) {
        e();
        this.f16288a.w().B(str, j8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void generateEventId(com.google.android.gms.internal.measurement.h2 h2Var) {
        e();
        long P0 = this.f16288a.J().P0();
        e();
        this.f16288a.J().O(h2Var, P0);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void getAppInstanceId(com.google.android.gms.internal.measurement.h2 h2Var) {
        e();
        this.f16288a.l().B(new t5(this, h2Var));
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void getCachedAppInstanceId(com.google.android.gms.internal.measurement.h2 h2Var) {
        e();
        f(h2Var, this.f16288a.F().h0());
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void getConditionalUserProperties(String str, String str2, com.google.android.gms.internal.measurement.h2 h2Var) {
        e();
        this.f16288a.l().B(new f8(this, h2Var, str, str2));
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void getCurrentScreenClass(com.google.android.gms.internal.measurement.h2 h2Var) {
        e();
        f(h2Var, this.f16288a.F().i0());
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void getCurrentScreenName(com.google.android.gms.internal.measurement.h2 h2Var) {
        e();
        f(h2Var, this.f16288a.F().j0());
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void getGmpAppId(com.google.android.gms.internal.measurement.h2 h2Var) {
        e();
        f(h2Var, this.f16288a.F().k0());
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void getMaxUserProperties(String str, com.google.android.gms.internal.measurement.h2 h2Var) {
        e();
        this.f16288a.F();
        n6.j.f(str);
        e();
        this.f16288a.J().N(h2Var, 25);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void getSessionId(com.google.android.gms.internal.measurement.h2 h2Var) {
        e();
        h7 F = this.f16288a.F();
        F.l().B(new g8(F, h2Var));
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void getTestFlag(com.google.android.gms.internal.measurement.h2 h2Var, int i8) {
        e();
        if (i8 == 0) {
            this.f16288a.J().Q(h2Var, this.f16288a.F().l0());
        } else if (i8 == 1) {
            this.f16288a.J().O(h2Var, this.f16288a.F().g0().longValue());
        } else if (i8 != 2) {
            if (i8 == 3) {
                this.f16288a.J().N(h2Var, this.f16288a.F().f0().intValue());
            } else if (i8 != 4) {
            } else {
                this.f16288a.J().S(h2Var, this.f16288a.F().d0().booleanValue());
            }
        } else {
            sb J = this.f16288a.J();
            double doubleValue = this.f16288a.F().e0().doubleValue();
            Bundle bundle = new Bundle();
            bundle.putDouble("r", doubleValue);
            try {
                h2Var.c(bundle);
            } catch (RemoteException e8) {
                J.f16485a.i().J().b("Error returning double value to wrapper", e8);
            }
        }
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void getUserProperties(String str, String str2, boolean z4, com.google.android.gms.internal.measurement.h2 h2Var) {
        e();
        this.f16288a.l().B(new r6(this, h2Var, str, str2, z4));
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void initForTests(Map map) {
        e();
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void initialize(x6.a aVar, zzdq zzdqVar, long j8) {
        f6 f6Var = this.f16288a;
        if (f6Var == null) {
            this.f16288a = f6.a((Context) n6.j.l((Context) x6.b.f(aVar)), zzdqVar, Long.valueOf(j8));
        } else {
            f6Var.i().J().a("Attempting to initialize multiple times");
        }
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void isDataCollectionEnabled(com.google.android.gms.internal.measurement.h2 h2Var) {
        e();
        this.f16288a.l().B(new ea(this, h2Var));
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void logEvent(String str, String str2, Bundle bundle, boolean z4, boolean z8, long j8) {
        e();
        this.f16288a.F().Y(str, str2, bundle, z4, z8, j8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void logEventAndBundle(String str, String str2, Bundle bundle, com.google.android.gms.internal.measurement.h2 h2Var, long j8) {
        e();
        n6.j.f(str2);
        (bundle != null ? new Bundle(bundle) : new Bundle()).putString("_o", "app");
        this.f16288a.l().B(new j7(this, h2Var, new zzbf(str2, new zzba(bundle), "app", j8), str));
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void logHealthData(int i8, String str, x6.a aVar, x6.a aVar2, x6.a aVar3) {
        e();
        this.f16288a.i().x(i8, true, false, str, aVar == null ? null : x6.b.f(aVar), aVar2 == null ? null : x6.b.f(aVar2), aVar3 != null ? x6.b.f(aVar3) : null);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void onActivityCreated(x6.a aVar, Bundle bundle, long j8) {
        e();
        r8 r8Var = this.f16288a.F().f16625c;
        if (r8Var != null) {
            this.f16288a.F().n0();
            r8Var.onActivityCreated((Activity) x6.b.f(aVar), bundle);
        }
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void onActivityDestroyed(x6.a aVar, long j8) {
        e();
        r8 r8Var = this.f16288a.F().f16625c;
        if (r8Var != null) {
            this.f16288a.F().n0();
            r8Var.onActivityDestroyed((Activity) x6.b.f(aVar));
        }
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void onActivityPaused(x6.a aVar, long j8) {
        e();
        r8 r8Var = this.f16288a.F().f16625c;
        if (r8Var != null) {
            this.f16288a.F().n0();
            r8Var.onActivityPaused((Activity) x6.b.f(aVar));
        }
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void onActivityResumed(x6.a aVar, long j8) {
        e();
        r8 r8Var = this.f16288a.F().f16625c;
        if (r8Var != null) {
            this.f16288a.F().n0();
            r8Var.onActivityResumed((Activity) x6.b.f(aVar));
        }
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void onActivitySaveInstanceState(x6.a aVar, com.google.android.gms.internal.measurement.h2 h2Var, long j8) {
        e();
        r8 r8Var = this.f16288a.F().f16625c;
        Bundle bundle = new Bundle();
        if (r8Var != null) {
            this.f16288a.F().n0();
            r8Var.onActivitySaveInstanceState((Activity) x6.b.f(aVar), bundle);
        }
        try {
            h2Var.c(bundle);
        } catch (RemoteException e8) {
            this.f16288a.i().J().b("Error returning bundle value to wrapper", e8);
        }
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void onActivityStarted(x6.a aVar, long j8) {
        e();
        r8 r8Var = this.f16288a.F().f16625c;
        if (r8Var != null) {
            this.f16288a.F().n0();
            r8Var.onActivityStarted((Activity) x6.b.f(aVar));
        }
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void onActivityStopped(x6.a aVar, long j8) {
        e();
        r8 r8Var = this.f16288a.F().f16625c;
        if (r8Var != null) {
            this.f16288a.F().n0();
            r8Var.onActivityStopped((Activity) x6.b.f(aVar));
        }
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void performAction(Bundle bundle, com.google.android.gms.internal.measurement.h2 h2Var, long j8) {
        e();
        h2Var.c(null);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void registerOnMeasurementEventListener(com.google.android.gms.internal.measurement.i2 i2Var) {
        f7.r rVar;
        e();
        synchronized (this.f16289b) {
            rVar = this.f16289b.get(Integer.valueOf(i2Var.zza()));
            if (rVar == null) {
                rVar = new b(i2Var);
                this.f16289b.put(Integer.valueOf(i2Var.zza()), rVar);
            }
        }
        this.f16288a.F().P(rVar);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void resetAnalyticsData(long j8) {
        e();
        h7 F = this.f16288a.F();
        F.S(null);
        F.l().B(new c8(F, j8));
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setConditionalUserProperty(Bundle bundle, long j8) {
        e();
        if (bundle == null) {
            this.f16288a.i().E().a("Conditional user property must not be null");
        } else {
            this.f16288a.F().G(bundle, j8);
        }
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setConsent(final Bundle bundle, final long j8) {
        e();
        final h7 F = this.f16288a.F();
        F.l().E(new Runnable() { // from class: com.google.android.gms.measurement.internal.m7
            @Override // java.lang.Runnable
            public final void run() {
                h7 h7Var = h7.this;
                Bundle bundle2 = bundle;
                long j9 = j8;
                if (TextUtils.isEmpty(h7Var.n().E())) {
                    h7Var.F(bundle2, 0, j9);
                } else {
                    h7Var.i().K().a("Using developer consent only; google app id found");
                }
            }
        });
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setConsentThirdParty(Bundle bundle, long j8) {
        e();
        this.f16288a.F().F(bundle, -20, j8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setCurrentScreen(x6.a aVar, String str, String str2, long j8) {
        e();
        this.f16288a.G().F((Activity) x6.b.f(aVar), str, str2);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setDataCollectionEnabled(boolean z4) {
        e();
        h7 F = this.f16288a.F();
        F.t();
        F.l().B(new s7(F, z4));
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setDefaultEventParameters(Bundle bundle) {
        e();
        final h7 F = this.f16288a.F();
        final Bundle bundle2 = bundle == null ? null : new Bundle(bundle);
        F.l().B(new Runnable() { // from class: com.google.android.gms.measurement.internal.n7
            @Override // java.lang.Runnable
            public final void run() {
                h7.this.E(bundle2);
            }
        });
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setEventInterceptor(com.google.android.gms.internal.measurement.i2 i2Var) {
        e();
        a aVar = new a(i2Var);
        if (this.f16288a.l().H()) {
            this.f16288a.F().O(aVar);
        } else {
            this.f16288a.l().B(new e9(this, aVar));
        }
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setInstanceIdProvider(com.google.android.gms.internal.measurement.m2 m2Var) {
        e();
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setMeasurementEnabled(boolean z4, long j8) {
        e();
        this.f16288a.F().Q(Boolean.valueOf(z4));
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setMinimumSessionDuration(long j8) {
        e();
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setSessionTimeoutDuration(long j8) {
        e();
        h7 F = this.f16288a.F();
        F.l().B(new u7(F, j8));
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setSgtmDebugInfo(Intent intent) {
        e();
        h7 F = this.f16288a.F();
        if (wf.a() && F.a().D(null, c0.f16417x0)) {
            Uri data = intent.getData();
            if (data == null) {
                F.i().H().a("Activity intent has no data. Preview Mode was not enabled.");
                return;
            }
            String queryParameter = data.getQueryParameter("sgtm_debug_enable");
            if (queryParameter == null || !queryParameter.equals("1")) {
                F.i().H().a("Preview Mode was not enabled.");
                F.a().I(null);
                return;
            }
            String queryParameter2 = data.getQueryParameter("sgtm_preview_key");
            if (TextUtils.isEmpty(queryParameter2)) {
                return;
            }
            F.i().H().b("Preview Mode was enabled. Using the sgtmPreviewKey: ", queryParameter2);
            F.a().I(queryParameter2);
        }
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setUserId(final String str, long j8) {
        e();
        final h7 F = this.f16288a.F();
        if (str != null && TextUtils.isEmpty(str)) {
            F.f16485a.i().J().a("User ID must be non-empty or null");
            return;
        }
        F.l().B(new Runnable() { // from class: com.google.android.gms.measurement.internal.p7
            @Override // java.lang.Runnable
            public final void run() {
                h7 h7Var = h7.this;
                if (h7Var.n().I(str)) {
                    h7Var.n().G();
                }
            }
        });
        F.b0(null, "_id", str, true, j8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void setUserProperty(String str, String str2, x6.a aVar, boolean z4, long j8) {
        e();
        this.f16288a.F().b0(str, str2, x6.b.f(aVar), z4, j8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public void unregisterOnMeasurementEventListener(com.google.android.gms.internal.measurement.i2 i2Var) {
        f7.r remove;
        e();
        synchronized (this.f16289b) {
            remove = this.f16289b.remove(Integer.valueOf(i2Var.zza()));
        }
        if (remove == null) {
            remove = new b(i2Var);
        }
        this.f16288a.F().x0(remove);
    }
}
