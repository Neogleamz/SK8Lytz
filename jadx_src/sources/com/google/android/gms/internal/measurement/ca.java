package com.google.android.gms.internal.measurement;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ca {

    /* renamed from: a  reason: collision with root package name */
    private Map<String, Callable<? extends m>> f12115a = new HashMap();

    public final r a(String str) {
        if (this.f12115a.containsKey(str)) {
            try {
                return this.f12115a.get(str).call();
            } catch (Exception unused) {
                throw new IllegalStateException("Failed to create API implementation: " + str);
            }
        }
        return r.f12463r;
    }

    public final void b(String str, Callable<? extends m> callable) {
        this.f12115a.put(str, callable);
    }
}
