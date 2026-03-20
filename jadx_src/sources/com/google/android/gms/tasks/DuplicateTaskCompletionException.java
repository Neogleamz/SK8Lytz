package com.google.android.gms.tasks;

import j7.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class DuplicateTaskCompletionException extends IllegalStateException {
    private DuplicateTaskCompletionException(String str, Throwable th) {
        super(str, th);
    }

    public static IllegalStateException a(j<?> jVar) {
        if (jVar.o()) {
            Exception k8 = jVar.k();
            return new DuplicateTaskCompletionException("Complete with: ".concat(k8 != null ? "failure" : jVar.p() ? "result ".concat(String.valueOf(jVar.l())) : jVar.n() ? "cancellation" : "unknown issue"), k8);
        }
        return new IllegalStateException("DuplicateTaskCompletionException can only be created from completed Task.");
    }
}
