package com.google.android.gms.common;

import android.util.Log;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class w {

    /* renamed from: e  reason: collision with root package name */
    private static final w f11994e = new w(true, 3, 1, null, null);

    /* renamed from: a  reason: collision with root package name */
    final boolean f11995a;

    /* renamed from: b  reason: collision with root package name */
    final String f11996b;

    /* renamed from: c  reason: collision with root package name */
    final Throwable f11997c;

    /* renamed from: d  reason: collision with root package name */
    final int f11998d;

    private w(boolean z4, int i8, int i9, String str, Throwable th) {
        this.f11995a = z4;
        this.f11998d = i8;
        this.f11996b = str;
        this.f11997c = th;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public static w b() {
        return f11994e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static w c(String str) {
        return new w(false, 1, 5, str, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static w d(String str, Throwable th) {
        return new w(false, 1, 5, str, th);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static w f(int i8) {
        return new w(true, i8, 1, null, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static w g(int i8, int i9, String str, Throwable th) {
        return new w(false, i8, i9, str, th);
    }

    String a() {
        return this.f11996b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void e() {
        if (this.f11995a || !Log.isLoggable("GoogleCertificatesRslt", 3)) {
            return;
        }
        if (this.f11997c != null) {
            Log.d("GoogleCertificatesRslt", a(), this.f11997c);
        } else {
            Log.d("GoogleCertificatesRslt", a());
        }
    }
}
