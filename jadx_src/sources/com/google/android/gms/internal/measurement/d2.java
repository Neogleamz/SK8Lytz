package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d2 extends g2 {

    /* renamed from: a  reason: collision with root package name */
    private final AtomicReference<Bundle> f12134a = new AtomicReference<>();

    /* renamed from: b  reason: collision with root package name */
    private boolean f12135b;

    /* JADX WARN: Code restructure failed: missing block: B:4:0x0003, code lost:
        r4 = r4.get("r");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static <T> T f(android.os.Bundle r4, java.lang.Class<T> r5) {
        /*
            r0 = 0
            if (r4 == 0) goto L43
            java.lang.String r1 = "r"
            java.lang.Object r4 = r4.get(r1)
            if (r4 == 0) goto L43
            java.lang.Object r4 = r5.cast(r4)     // Catch: java.lang.ClassCastException -> L10
            return r4
        L10:
            r0 = move-exception
            java.lang.String r5 = r5.getCanonicalName()
            java.lang.Class r4 = r4.getClass()
            java.lang.String r4 = r4.getCanonicalName()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unexpected object type. Expected, Received"
            r1.append(r2)
            java.lang.String r2 = ": %s, %s"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            r2[r3] = r5
            r5 = 1
            r2[r5] = r4
            java.lang.String r4 = java.lang.String.format(r1, r2)
            java.lang.String r5 = "AM"
            android.util.Log.w(r5, r4, r0)
            throw r0
        L43:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.d2.f(android.os.Bundle, java.lang.Class):java.lang.Object");
    }

    @Override // com.google.android.gms.internal.measurement.h2
    public final void c(Bundle bundle) {
        synchronized (this.f12134a) {
            this.f12134a.set(bundle);
            this.f12135b = true;
            this.f12134a.notify();
        }
    }

    public final Bundle e(long j8) {
        Bundle bundle;
        synchronized (this.f12134a) {
            if (!this.f12135b) {
                try {
                    this.f12134a.wait(j8);
                } catch (InterruptedException unused) {
                    return null;
                }
            }
            bundle = this.f12134a.get();
        }
        return bundle;
    }

    public final Long g(long j8) {
        return (Long) f(e(j8), Long.class);
    }

    public final String k(long j8) {
        return (String) f(e(j8), String.class);
    }
}
