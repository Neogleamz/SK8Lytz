package h5;

import android.net.Uri;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h {

    /* renamed from: h  reason: collision with root package name */
    private static final AtomicLong f20271h = new AtomicLong();

    /* renamed from: a  reason: collision with root package name */
    public final long f20272a;

    /* renamed from: b  reason: collision with root package name */
    public final com.google.android.exoplayer2.upstream.a f20273b;

    /* renamed from: c  reason: collision with root package name */
    public final Uri f20274c;

    /* renamed from: d  reason: collision with root package name */
    public final Map<String, List<String>> f20275d;

    /* renamed from: e  reason: collision with root package name */
    public final long f20276e;

    /* renamed from: f  reason: collision with root package name */
    public final long f20277f;

    /* renamed from: g  reason: collision with root package name */
    public final long f20278g;

    public h(long j8, com.google.android.exoplayer2.upstream.a aVar, long j9) {
        this(j8, aVar, aVar.f10942a, Collections.emptyMap(), j9, 0L, 0L);
    }

    public h(long j8, com.google.android.exoplayer2.upstream.a aVar, Uri uri, Map<String, List<String>> map, long j9, long j10, long j11) {
        this.f20272a = j8;
        this.f20273b = aVar;
        this.f20274c = uri;
        this.f20275d = map;
        this.f20276e = j9;
        this.f20277f = j10;
        this.f20278g = j11;
    }

    public static long a() {
        return f20271h.getAndIncrement();
    }
}
