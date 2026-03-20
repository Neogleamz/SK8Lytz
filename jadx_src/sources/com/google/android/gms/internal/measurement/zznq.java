package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public enum zznq {
    INT(0),
    LONG(0L),
    FLOAT(Float.valueOf(0.0f)),
    DOUBLE(Double.valueOf(0.0d)),
    BOOLEAN(Boolean.FALSE),
    STRING(BuildConfig.FLAVOR),
    BYTE_STRING(zzij.f12852b),
    ENUM(null),
    MESSAGE(null);
    

    /* renamed from: a  reason: collision with root package name */
    private final Object f12954a;

    zznq(Object obj) {
        this.f12954a = obj;
    }
}
