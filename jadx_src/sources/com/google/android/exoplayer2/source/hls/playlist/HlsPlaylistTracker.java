package com.google.android.exoplayer2.source.hls.playlist;

import android.net.Uri;
import com.google.android.exoplayer2.source.l;
import com.google.android.exoplayer2.upstream.c;
import java.io.IOException;
import m5.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface HlsPlaylistTracker {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class PlaylistResetException extends IOException {

        /* renamed from: a  reason: collision with root package name */
        public final Uri f10513a;

        public PlaylistResetException(Uri uri) {
            this.f10513a = uri;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class PlaylistStuckException extends IOException {

        /* renamed from: a  reason: collision with root package name */
        public final Uri f10514a;

        public PlaylistStuckException(Uri uri) {
            this.f10514a = uri;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        HlsPlaylistTracker a(g gVar, com.google.android.exoplayer2.upstream.c cVar, n5.e eVar);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a();

        boolean e(Uri uri, c.C0117c c0117c, boolean z4);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        void d(d dVar);
    }

    boolean a(Uri uri);

    void b(b bVar);

    void c(Uri uri);

    long d();

    boolean e();

    e f();

    boolean g(Uri uri, long j8);

    void h(Uri uri, l.a aVar, c cVar);

    void i();

    void l(Uri uri);

    void m(b bVar);

    d n(Uri uri, boolean z4);

    void stop();
}
