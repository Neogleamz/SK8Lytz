package com.google.android.exoplayer2.upstream;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class HttpDataSource$HttpDataSourceException extends DataSourceException {

    /* renamed from: b  reason: collision with root package name */
    public final a f10899b;

    /* renamed from: c  reason: collision with root package name */
    public final int f10900c;

    public HttpDataSource$HttpDataSourceException(a aVar, int i8, int i9) {
        super(b(i8, i9));
        this.f10899b = aVar;
        this.f10900c = i9;
    }

    public HttpDataSource$HttpDataSourceException(IOException iOException, a aVar, int i8, int i9) {
        super(iOException, b(i8, i9));
        this.f10899b = aVar;
        this.f10900c = i9;
    }

    public HttpDataSource$HttpDataSourceException(String str, a aVar, int i8, int i9) {
        super(str, b(i8, i9));
        this.f10899b = aVar;
        this.f10900c = i9;
    }

    public HttpDataSource$HttpDataSourceException(String str, IOException iOException, a aVar, int i8, int i9) {
        super(str, iOException, b(i8, i9));
        this.f10899b = aVar;
        this.f10900c = i9;
    }

    private static int b(int i8, int i9) {
        if (i8 == 2000 && i9 == 1) {
            return 2001;
        }
        return i8;
    }

    public static HttpDataSource$HttpDataSourceException c(final IOException iOException, final a aVar, int i8) {
        String message = iOException.getMessage();
        int i9 = iOException instanceof SocketTimeoutException ? 2002 : iOException instanceof InterruptedIOException ? 1004 : (message == null || !com.google.common.base.c.e(message).matches("cleartext.*not permitted.*")) ? 2001 : 2007;
        return i9 == 2007 ? new HttpDataSource$HttpDataSourceException(iOException, aVar) { // from class: com.google.android.exoplayer2.upstream.HttpDataSource$CleartextNotPermittedException
        } : new HttpDataSource$HttpDataSourceException(iOException, aVar, i9, i8);
    }
}
