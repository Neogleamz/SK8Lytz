package com.google.android.gms.internal.measurement;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x5 implements e6 {

    /* renamed from: h  reason: collision with root package name */
    private static final Map<Uri, x5> f12650h = new k0.a();

    /* renamed from: i  reason: collision with root package name */
    private static final String[] f12651i = {"key", "value"};

    /* renamed from: a  reason: collision with root package name */
    private final ContentResolver f12652a;

    /* renamed from: b  reason: collision with root package name */
    private final Uri f12653b;

    /* renamed from: c  reason: collision with root package name */
    private final Runnable f12654c;

    /* renamed from: d  reason: collision with root package name */
    private final ContentObserver f12655d;

    /* renamed from: e  reason: collision with root package name */
    private final Object f12656e;

    /* renamed from: f  reason: collision with root package name */
    private volatile Map<String, String> f12657f;

    /* renamed from: g  reason: collision with root package name */
    private final List<c6> f12658g;

    private x5(ContentResolver contentResolver, Uri uri, Runnable runnable) {
        z5 z5Var = new z5(this, null);
        this.f12655d = z5Var;
        this.f12656e = new Object();
        this.f12658g = new ArrayList();
        com.google.common.base.l.n(contentResolver);
        com.google.common.base.l.n(uri);
        this.f12652a = contentResolver;
        this.f12653b = uri;
        this.f12654c = runnable;
        contentResolver.registerContentObserver(uri, false, z5Var);
    }

    public static x5 a(ContentResolver contentResolver, Uri uri, Runnable runnable) {
        x5 x5Var;
        synchronized (x5.class) {
            Map<Uri, x5> map = f12650h;
            x5Var = map.get(uri);
            if (x5Var == null) {
                try {
                    x5 x5Var2 = new x5(contentResolver, uri, runnable);
                    try {
                        map.put(uri, x5Var2);
                    } catch (SecurityException unused) {
                    }
                    x5Var = x5Var2;
                } catch (SecurityException unused2) {
                }
            }
        }
        return x5Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void d() {
        synchronized (x5.class) {
            for (x5 x5Var : f12650h.values()) {
                x5Var.f12652a.unregisterContentObserver(x5Var.f12655d);
            }
            f12650h.clear();
        }
    }

    private final Map<String, String> f() {
        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            try {
                return (Map) d6.a(new h6() { // from class: com.google.android.gms.internal.measurement.a6
                    @Override // com.google.android.gms.internal.measurement.h6
                    public final Object zza() {
                        return x5.this.c();
                    }
                });
            } catch (SQLiteException | IllegalStateException | SecurityException unused) {
                Log.e("ConfigurationContentLdr", "PhenotypeFlag unable to load ContentProvider, using default values");
                StrictMode.setThreadPolicy(allowThreadDiskReads);
                return null;
            }
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }

    public final Map<String, String> b() {
        Map<String, String> map = this.f12657f;
        if (map == null) {
            synchronized (this.f12656e) {
                map = this.f12657f;
                if (map == null) {
                    map = f();
                    this.f12657f = map;
                }
            }
        }
        return map != null ? map : Collections.emptyMap();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ Map c() {
        Cursor query = this.f12652a.query(this.f12653b, f12651i, null, null, null);
        if (query == null) {
            return Collections.emptyMap();
        }
        try {
            int count = query.getCount();
            if (count == 0) {
                return Collections.emptyMap();
            }
            Map aVar = count <= 256 ? new k0.a(count) : new HashMap(count, 1.0f);
            while (query.moveToNext()) {
                aVar.put(query.getString(0), query.getString(1));
            }
            return aVar;
        } finally {
            query.close();
        }
    }

    public final void e() {
        synchronized (this.f12656e) {
            this.f12657f = null;
            this.f12654c.run();
        }
        synchronized (this) {
            for (c6 c6Var : this.f12658g) {
                c6Var.zza();
            }
        }
    }

    @Override // com.google.android.gms.internal.measurement.e6
    public final /* synthetic */ Object h(String str) {
        return b().get(str);
    }
}
