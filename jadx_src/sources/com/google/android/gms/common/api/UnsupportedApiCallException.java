package com.google.android.gms.common.api;

import com.google.android.gms.common.Feature;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class UnsupportedApiCallException extends UnsupportedOperationException {

    /* renamed from: a  reason: collision with root package name */
    private final Feature f11558a;

    public UnsupportedApiCallException(Feature feature) {
        this.f11558a = feature;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return "Missing ".concat(String.valueOf(this.f11558a));
    }
}
