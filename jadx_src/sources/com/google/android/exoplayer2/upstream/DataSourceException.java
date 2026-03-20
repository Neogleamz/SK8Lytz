package com.google.android.exoplayer2.upstream;

import java.io.IOException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class DataSourceException extends IOException {

    /* renamed from: a  reason: collision with root package name */
    public final int f10894a;

    public DataSourceException(int i8) {
        this.f10894a = i8;
    }

    public DataSourceException(String str, int i8) {
        super(str);
        this.f10894a = i8;
    }

    public DataSourceException(String str, Throwable th, int i8) {
        super(str, th);
        this.f10894a = i8;
    }

    public DataSourceException(Throwable th, int i8) {
        super(th);
        this.f10894a = i8;
    }

    /* JADX WARN: Code restructure failed: missing block: B:0:?, code lost:
        r2 = r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean a(java.io.IOException r2) {
        /*
        L0:
            if (r2 == 0) goto L16
            boolean r0 = r2 instanceof com.google.android.exoplayer2.upstream.DataSourceException
            if (r0 == 0) goto L11
            r0 = r2
            com.google.android.exoplayer2.upstream.DataSourceException r0 = (com.google.android.exoplayer2.upstream.DataSourceException) r0
            int r0 = r0.f10894a
            r1 = 2008(0x7d8, float:2.814E-42)
            if (r0 != r1) goto L11
            r2 = 1
            return r2
        L11:
            java.lang.Throwable r2 = r2.getCause()
            goto L0
        L16:
            r2 = 0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.upstream.DataSourceException.a(java.io.IOException):boolean");
    }
}
