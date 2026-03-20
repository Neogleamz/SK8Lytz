package com.google.android.datatransport.runtime.backends;

import com.google.android.datatransport.runtime.backends.BackendResponse;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends BackendResponse {

    /* renamed from: a  reason: collision with root package name */
    private final BackendResponse.Status f9096a;

    /* renamed from: b  reason: collision with root package name */
    private final long f9097b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(BackendResponse.Status status, long j8) {
        Objects.requireNonNull(status, "Null status");
        this.f9096a = status;
        this.f9097b = j8;
    }

    @Override // com.google.android.datatransport.runtime.backends.BackendResponse
    public long b() {
        return this.f9097b;
    }

    @Override // com.google.android.datatransport.runtime.backends.BackendResponse
    public BackendResponse.Status c() {
        return this.f9096a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof BackendResponse) {
            BackendResponse backendResponse = (BackendResponse) obj;
            return this.f9096a.equals(backendResponse.c()) && this.f9097b == backendResponse.b();
        }
        return false;
    }

    public int hashCode() {
        long j8 = this.f9097b;
        return ((this.f9096a.hashCode() ^ 1000003) * 1000003) ^ ((int) (j8 ^ (j8 >>> 32)));
    }

    public String toString() {
        return "BackendResponse{status=" + this.f9096a + ", nextRequestWaitMillis=" + this.f9097b + "}";
    }
}
