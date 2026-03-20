package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a7 implements e6 {

    /* renamed from: g  reason: collision with root package name */
    private static final Map<String, a7> f12060g = new k0.a();

    /* renamed from: a  reason: collision with root package name */
    private final SharedPreferences f12061a;

    /* renamed from: b  reason: collision with root package name */
    private final Runnable f12062b;

    /* renamed from: c  reason: collision with root package name */
    private final SharedPreferences.OnSharedPreferenceChangeListener f12063c;

    /* renamed from: d  reason: collision with root package name */
    private final Object f12064d;

    /* renamed from: e  reason: collision with root package name */
    private volatile Map<String, ?> f12065e;

    /* renamed from: f  reason: collision with root package name */
    private final List<c6> f12066f;

    private a7(SharedPreferences sharedPreferences, Runnable runnable) {
        SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: com.google.android.gms.internal.measurement.z6
            @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
            public final void onSharedPreferenceChanged(SharedPreferences sharedPreferences2, String str) {
                a7.this.d(sharedPreferences2, str);
            }
        };
        this.f12063c = onSharedPreferenceChangeListener;
        this.f12064d = new Object();
        this.f12066f = new ArrayList();
        this.f12061a = sharedPreferences;
        this.f12062b = runnable;
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    private static SharedPreferences a(Context context, String str) {
        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            if (str.startsWith("direct_boot:")) {
                if (w5.a()) {
                    context = context.createDeviceProtectedStorageContext();
                }
                return context.getSharedPreferences(str.substring(12), 0);
            }
            return context.getSharedPreferences(str, 0);
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static a7 b(Context context, String str, Runnable runnable) {
        a7 a7Var;
        if ((!w5.a() || str.startsWith("direct_boot:")) ? true : w5.c(context)) {
            synchronized (a7.class) {
                Map<String, a7> map = f12060g;
                a7Var = map.get(str);
                if (a7Var == null) {
                    a7Var = new a7(a(context, str), runnable);
                    map.put(str, a7Var);
                }
            }
            return a7Var;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void c() {
        synchronized (a7.class) {
            for (a7 a7Var : f12060g.values()) {
                a7Var.f12061a.unregisterOnSharedPreferenceChangeListener(a7Var.f12063c);
            }
            f12060g.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void d(SharedPreferences sharedPreferences, String str) {
        synchronized (this.f12064d) {
            this.f12065e = null;
            this.f12062b.run();
        }
        synchronized (this) {
            for (c6 c6Var : this.f12066f) {
                c6Var.zza();
            }
        }
    }

    @Override // com.google.android.gms.internal.measurement.e6
    public final Object h(String str) {
        Map<String, ?> map = this.f12065e;
        if (map == null) {
            synchronized (this.f12064d) {
                map = this.f12065e;
                if (map == null) {
                    StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
                    Map<String, ?> all = this.f12061a.getAll();
                    this.f12065e = all;
                    StrictMode.setThreadPolicy(allowThreadDiskReads);
                    map = all;
                }
            }
        }
        if (map != null) {
            return map.get(str);
        }
        return null;
    }
}
