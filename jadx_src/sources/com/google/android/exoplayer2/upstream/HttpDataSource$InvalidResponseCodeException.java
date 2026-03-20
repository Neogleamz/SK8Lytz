package com.google.android.exoplayer2.upstream;

import java.io.IOException;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class HttpDataSource$InvalidResponseCodeException extends HttpDataSource$HttpDataSourceException {

    /* renamed from: d  reason: collision with root package name */
    public final int f10902d;

    /* renamed from: e  reason: collision with root package name */
    public final String f10903e;

    /* renamed from: f  reason: collision with root package name */
    public final Map<String, List<String>> f10904f;

    /* renamed from: g  reason: collision with root package name */
    public final byte[] f10905g;

    public HttpDataSource$InvalidResponseCodeException(int i8, String str, IOException iOException, Map<String, List<String>> map, a aVar, byte[] bArr) {
        super("Response code: " + i8, iOException, aVar, 2004, 1);
        this.f10902d = i8;
        this.f10903e = str;
        this.f10904f = map;
        this.f10905g = bArr;
    }
}
