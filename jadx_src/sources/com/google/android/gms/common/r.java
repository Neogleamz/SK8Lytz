package com.google.android.gms.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;
import com.google.android.gms.dynamite.DynamiteModule;
import java.security.MessageDigest;
import java.util.concurrent.Callable;
import n6.d0;
import n6.e0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r {

    /* renamed from: e  reason: collision with root package name */
    private static volatile e0 f11938e;

    /* renamed from: g  reason: collision with root package name */
    private static Context f11940g;

    /* renamed from: a  reason: collision with root package name */
    static final p f11934a = new j(n.f("0\u0082\u0005È0\u0082\u0003° \u0003\u0002\u0001\u0002\u0002\u0014\u0010\u008ae\bsù/\u008eQí"));

    /* renamed from: b  reason: collision with root package name */
    static final p f11935b = new k(n.f("0\u0082\u0006\u00040\u0082\u0003ì \u0003\u0002\u0001\u0002\u0002\u0014\u0003£²\u00ad×árÊkì"));

    /* renamed from: c  reason: collision with root package name */
    static final p f11936c = new l(n.f("0\u0082\u0004C0\u0082\u0003+ \u0003\u0002\u0001\u0002\u0002\t\u0000Âà\u0087FdJ0\u008d0"));

    /* renamed from: d  reason: collision with root package name */
    static final p f11937d = new m(n.f("0\u0082\u0004¨0\u0082\u0003\u0090 \u0003\u0002\u0001\u0002\u0002\t\u0000Õ\u0085¸l}ÓNõ0"));

    /* renamed from: f  reason: collision with root package name */
    private static final Object f11939f = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static w a(String str, n nVar, boolean z4, boolean z8) {
        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            return f(str, nVar, z4, z8);
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static w b(String str, boolean z4, boolean z8, boolean z9) {
        return g(str, z4, false, false, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ String c(boolean z4, String str, n nVar) {
        String str2 = true != (!z4 && f(str, nVar, true, false).f11995a) ? "not allowed" : "debug cert rejected";
        MessageDigest b9 = u6.a.b("SHA-256");
        n6.j.l(b9);
        return String.format("%s: pkg=%s, sha256=%s, atk=%s, ver=%s", str2, str, u6.i.a(b9.digest(nVar.g())), Boolean.valueOf(z4), "12451000.false");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void d(Context context) {
        synchronized (r.class) {
            if (f11940g != null) {
                Log.w("GoogleCertificates", "GoogleCertificates has been initialized already");
            } else if (context != null) {
                f11940g = context.getApplicationContext();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean e() {
        boolean z4;
        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            try {
                h();
                z4 = f11938e.j();
            } finally {
                StrictMode.setThreadPolicy(allowThreadDiskReads);
            }
        } catch (RemoteException | DynamiteModule.LoadingException e8) {
            Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e8);
            z4 = false;
        }
        return z4;
    }

    private static w f(final String str, final n nVar, final boolean z4, boolean z8) {
        try {
            h();
            n6.j.l(f11940g);
            try {
                return f11938e.T0(new zzs(str, nVar, z4, z8), x6.b.g(f11940g.getPackageManager())) ? w.b() : new v(new Callable() { // from class: com.google.android.gms.common.i
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        return r.c(z4, str, nVar);
                    }
                }, null);
            } catch (RemoteException e8) {
                Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e8);
                return w.d("module call", e8);
            }
        } catch (DynamiteModule.LoadingException e9) {
            Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e9);
            return w.d("module init: ".concat(String.valueOf(e9.getMessage())), e9);
        }
    }

    /* JADX WARN: Type inference failed for: r6v0, types: [x6.a, android.os.IBinder] */
    private static w g(String str, boolean z4, boolean z8, boolean z9, boolean z10) {
        String concat;
        w d8;
        zzo zzoVar;
        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            n6.j.l(f11940g);
            try {
                h();
                zzoVar = new zzo(str, z4, false, x6.b.g(f11940g), false, true);
            } catch (DynamiteModule.LoadingException e8) {
                e = e8;
                Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e);
                concat = "module init: ".concat(String.valueOf(e.getMessage()));
            }
            try {
                zzq q02 = z10 ? f11938e.q0(zzoVar) : f11938e.R0(zzoVar);
                if (q02.u()) {
                    d8 = w.f(q02.Z());
                } else {
                    String t8 = q02.t();
                    PackageManager.NameNotFoundException nameNotFoundException = q02.D0() == 4 ? new PackageManager.NameNotFoundException() : null;
                    if (t8 == null) {
                        t8 = "error checking package certificate";
                    }
                    d8 = w.g(q02.Z(), q02.D0(), t8, nameNotFoundException);
                }
            } catch (RemoteException e9) {
                e = e9;
                Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e);
                concat = "module call";
                d8 = w.d(concat, e);
                return d8;
            }
            return d8;
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }

    private static void h() {
        if (f11938e != null) {
            return;
        }
        n6.j.l(f11940g);
        synchronized (f11939f) {
            if (f11938e == null) {
                f11938e = d0.e(DynamiteModule.d(f11940g, DynamiteModule.f12019f, "com.google.android.gms.googlecertificates").c("com.google.android.gms.common.GoogleCertificatesImpl"));
            }
        }
    }
}
