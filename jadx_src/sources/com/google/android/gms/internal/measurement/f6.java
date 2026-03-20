package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.database.ContentObserver;
import android.util.Log;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f6 implements e6 {

    /* renamed from: c  reason: collision with root package name */
    private static f6 f12179c;

    /* renamed from: a  reason: collision with root package name */
    private final Context f12180a;

    /* renamed from: b  reason: collision with root package name */
    private final ContentObserver f12181b;

    private f6() {
        this.f12180a = null;
        this.f12181b = null;
    }

    private f6(Context context) {
        this.f12180a = context;
        i6 i6Var = new i6(this, null);
        this.f12181b = i6Var;
        context.getContentResolver().registerContentObserver(m5.f12338a, true, i6Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static f6 a(Context context) {
        f6 f6Var;
        synchronized (f6.class) {
            if (f12179c == null) {
                f12179c = androidx.core.content.b.b(context, "com.google.android.providers.gsf.permission.READ_GSERVICES") == 0 ? new f6(context) : new f6();
            }
            f6Var = f12179c;
        }
        return f6Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void b() {
        Context context;
        synchronized (f6.class) {
            f6 f6Var = f12179c;
            if (f6Var != null && (context = f6Var.f12180a) != null && f6Var.f12181b != null) {
                context.getContentResolver().unregisterContentObserver(f12179c.f12181b);
            }
            f12179c = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // com.google.android.gms.internal.measurement.e6
    /* renamed from: d */
    public final String h(final String str) {
        Context context = this.f12180a;
        if (context != null && !w5.b(context)) {
            try {
                return (String) d6.a(new h6() { // from class: com.google.android.gms.internal.measurement.j6
                    @Override // com.google.android.gms.internal.measurement.h6
                    public final Object zza() {
                        return f6.this.c(str);
                    }
                });
            } catch (IllegalStateException | NullPointerException | SecurityException e8) {
                Log.e("GservicesLoader", "Unable to read GServices for: " + str, e8);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ String c(String str) {
        return n5.a(this.f12180a.getContentResolver(), str, null);
    }
}
