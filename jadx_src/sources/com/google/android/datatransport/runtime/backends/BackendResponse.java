package com.google.android.datatransport.runtime.backends;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class BackendResponse {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum Status {
        OK,
        TRANSIENT_ERROR,
        FATAL_ERROR,
        INVALID_PAYLOAD
    }

    public static BackendResponse a() {
        return new a(Status.FATAL_ERROR, -1L);
    }

    public static BackendResponse d() {
        return new a(Status.INVALID_PAYLOAD, -1L);
    }

    public static BackendResponse e(long j8) {
        return new a(Status.OK, j8);
    }

    public static BackendResponse f() {
        return new a(Status.TRANSIENT_ERROR, -1L);
    }

    public abstract long b();

    public abstract Status c();
}
