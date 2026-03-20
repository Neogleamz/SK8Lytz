package com.google.android.gms.internal.measurement;

import android.content.Context;
import com.google.android.gms.internal.measurement.k6;
import com.google.common.base.Optional;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class n6<T> {

    /* renamed from: h  reason: collision with root package name */
    private static volatile y6 f12368h;

    /* renamed from: a  reason: collision with root package name */
    private final v6 f12372a;

    /* renamed from: b  reason: collision with root package name */
    private final String f12373b;

    /* renamed from: c  reason: collision with root package name */
    private final T f12374c;

    /* renamed from: d  reason: collision with root package name */
    private volatile int f12375d;

    /* renamed from: e  reason: collision with root package name */
    private volatile T f12376e;

    /* renamed from: f  reason: collision with root package name */
    private final boolean f12377f;

    /* renamed from: g  reason: collision with root package name */
    private static final Object f12367g = new Object();

    /* renamed from: i  reason: collision with root package name */
    private static final AtomicReference<Collection<n6<?>>> f12369i = new AtomicReference<>();

    /* renamed from: j  reason: collision with root package name */
    private static c7 f12370j = new c7(new b7() { // from class: com.google.android.gms.internal.measurement.s6
        @Override // com.google.android.gms.internal.measurement.b7
        public final boolean zza() {
            return n6.n();
        }
    });

    /* renamed from: k  reason: collision with root package name */
    private static final AtomicInteger f12371k = new AtomicInteger();

    private n6(v6 v6Var, String str, T t8, boolean z4) {
        this.f12375d = -1;
        String str2 = v6Var.f12574a;
        if (str2 == null && v6Var.f12575b == null) {
            throw new IllegalArgumentException("Must pass a valid SharedPreferences file name or ContentProvider URI");
        }
        if (str2 != null && v6Var.f12575b != null) {
            throw new IllegalArgumentException("Must pass one of SharedPreferences file name or ContentProvider URI");
        }
        this.f12372a = v6Var;
        this.f12373b = str;
        this.f12374c = t8;
        this.f12377f = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ n6 a(v6 v6Var, String str, Boolean bool, boolean z4) {
        return new u6(v6Var, str, bool, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ n6 b(v6 v6Var, String str, Double d8, boolean z4) {
        return new t6(v6Var, str, d8, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ n6 c(v6 v6Var, String str, Long l8, boolean z4) {
        return new r6(v6Var, str, l8, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ n6 d(v6 v6Var, String str, String str2, boolean z4) {
        return new w6(v6Var, str, str2, true);
    }

    private final T g(y6 y6Var) {
        com.google.common.base.g<Context, Boolean> gVar;
        v6 v6Var = this.f12372a;
        if (!v6Var.f12578e && ((gVar = v6Var.f12582i) == null || gVar.apply(y6Var.a()).booleanValue())) {
            f6 a9 = f6.a(y6Var.a());
            v6 v6Var2 = this.f12372a;
            Object h8 = a9.h(v6Var2.f12578e ? null : i(v6Var2.f12576c));
            if (h8 != null) {
                return h(h8);
            }
        }
        return null;
    }

    private final String i(String str) {
        if (str == null || !str.isEmpty()) {
            String str2 = this.f12373b;
            return str + str2;
        }
        return this.f12373b;
    }

    private final T j(y6 y6Var) {
        Object h8;
        e6 a9 = this.f12372a.f12575b != null ? m6.b(y6Var.a(), this.f12372a.f12575b) ? this.f12372a.f12581h ? x5.a(y6Var.a().getContentResolver(), o6.a(o6.b(y6Var.a(), this.f12372a.f12575b.getLastPathSegment())), new Runnable() { // from class: com.google.android.gms.internal.measurement.q6
            @Override // java.lang.Runnable
            public final void run() {
                n6.m();
            }
        }) : x5.a(y6Var.a().getContentResolver(), this.f12372a.f12575b, new Runnable() { // from class: com.google.android.gms.internal.measurement.q6
            @Override // java.lang.Runnable
            public final void run() {
                n6.m();
            }
        }) : null : a7.b(y6Var.a(), this.f12372a.f12574a, new Runnable() { // from class: com.google.android.gms.internal.measurement.q6
            @Override // java.lang.Runnable
            public final void run() {
                n6.m();
            }
        });
        if (a9 == null || (h8 = a9.h(k())) == null) {
            return null;
        }
        return h(h8);
    }

    public static void l(final Context context) {
        if (f12368h != null || context == null) {
            return;
        }
        Object obj = f12367g;
        synchronized (obj) {
            if (f12368h == null) {
                synchronized (obj) {
                    y6 y6Var = f12368h;
                    Context applicationContext = context.getApplicationContext();
                    if (applicationContext != null) {
                        context = applicationContext;
                    }
                    if (y6Var == null || y6Var.a() != context) {
                        if (y6Var != null) {
                            x5.d();
                            a7.c();
                            f6.b();
                        }
                        f12368h = new y5(context, com.google.common.base.s.a(new com.google.common.base.r() { // from class: com.google.android.gms.internal.measurement.p6
                            @Override // com.google.common.base.r
                            public final Object get() {
                                Optional a9;
                                a9 = k6.a.a(context);
                                return a9;
                            }
                        }));
                        f12371k.incrementAndGet();
                    }
                }
            }
        }
    }

    public static void m() {
        f12371k.incrementAndGet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean n() {
        return true;
    }

    public final T f() {
        T j8;
        if (!this.f12377f) {
            com.google.common.base.l.t(f12370j.a(this.f12373b), "Attempt to access PhenotypeFlag not via codegen. All new PhenotypeFlags must be accessed through codegen APIs. If you believe you are seeing this error by mistake, you can add your flag to the exemption list located at //java/com/google/android/libraries/phenotype/client/lockdown/flags.textproto. Send the addition CL to ph-reviews@. See go/phenotype-android-codegen for information about generated code. See go/ph-lockdown for more information about this error.");
        }
        int i8 = f12371k.get();
        if (this.f12375d < i8) {
            synchronized (this) {
                if (this.f12375d < i8) {
                    y6 y6Var = f12368h;
                    Optional<l6> a9 = Optional.a();
                    String str = null;
                    if (y6Var != null) {
                        a9 = y6Var.b().get();
                        if (a9.c()) {
                            v6 v6Var = this.f12372a;
                            str = a9.b().a(v6Var.f12575b, v6Var.f12574a, v6Var.f12577d, this.f12373b);
                        }
                    }
                    com.google.common.base.l.t(y6Var != null, "Must call PhenotypeFlagInitializer.maybeInit() first");
                    if (!this.f12372a.f12579f ? (j8 = j(y6Var)) == null && (j8 = g(y6Var)) == null : (j8 = g(y6Var)) == null && (j8 = j(y6Var)) == null) {
                        j8 = this.f12374c;
                    }
                    if (a9.c()) {
                        j8 = str == null ? this.f12374c : h(str);
                    }
                    this.f12376e = j8;
                    this.f12375d = i8;
                }
            }
        }
        return this.f12376e;
    }

    abstract T h(Object obj);

    public final String k() {
        return i(this.f12372a.f12577d);
    }
}
