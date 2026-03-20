package com.google.android.datatransport.cct.internal;

import java.util.List;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d extends i {

    /* renamed from: a  reason: collision with root package name */
    private final List<k> f9054a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(List<k> list) {
        Objects.requireNonNull(list, "Null logRequests");
        this.f9054a = list;
    }

    @Override // com.google.android.datatransport.cct.internal.i
    public List<k> c() {
        return this.f9054a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof i) {
            return this.f9054a.equals(((i) obj).c());
        }
        return false;
    }

    public int hashCode() {
        return this.f9054a.hashCode() ^ 1000003;
    }

    public String toString() {
        return "BatchedLogRequest{logRequests=" + this.f9054a + "}";
    }
}
