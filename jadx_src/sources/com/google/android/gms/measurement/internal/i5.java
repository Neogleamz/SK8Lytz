package com.google.android.gms.measurement.internal;

import android.text.TextUtils;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class i5 {

    /* renamed from: a  reason: collision with root package name */
    private final zzip f16674a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i5(zzip zzipVar) {
        this.f16674a = zzipVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static i5 a(String str) {
        return new i5((TextUtils.isEmpty(str) || str.length() > 1) ? zzip.UNINITIALIZED : zziq.c(str.charAt(0)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzip b() {
        return this.f16674a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String c() {
        return String.valueOf(zziq.a(this.f16674a));
    }
}
