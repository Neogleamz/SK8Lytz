package com.google.android.gms.common.api;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ApiException extends Exception {
    @Deprecated

    /* renamed from: a  reason: collision with root package name */
    protected final Status f11541a;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ApiException(com.google.android.gms.common.api.Status r4) {
        /*
            r3 = this;
            int r0 = r4.u()
            java.lang.String r1 = r4.Z()
            if (r1 == 0) goto Lf
            java.lang.String r1 = r4.Z()
            goto L11
        Lf:
            java.lang.String r1 = ""
        L11:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r0)
            java.lang.String r0 = ": "
            r2.append(r0)
            r2.append(r1)
            java.lang.String r0 = r2.toString()
            r3.<init>(r0)
            r3.f11541a = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.ApiException.<init>(com.google.android.gms.common.api.Status):void");
    }

    public Status a() {
        return this.f11541a;
    }
}
