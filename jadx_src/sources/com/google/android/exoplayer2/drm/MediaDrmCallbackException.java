package com.google.android.exoplayer2.drm;

import android.net.Uri;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MediaDrmCallbackException extends IOException {

    /* renamed from: a  reason: collision with root package name */
    public final com.google.android.exoplayer2.upstream.a f9602a;

    /* renamed from: b  reason: collision with root package name */
    public final Uri f9603b;

    /* renamed from: c  reason: collision with root package name */
    public final Map<String, List<String>> f9604c;

    /* renamed from: d  reason: collision with root package name */
    public final long f9605d;

    public MediaDrmCallbackException(com.google.android.exoplayer2.upstream.a aVar, Uri uri, Map<String, List<String>> map, long j8, Throwable th) {
        super(th);
        this.f9602a = aVar;
        this.f9603b = uri;
        this.f9604c = map;
        this.f9605d = j8;
    }
}
