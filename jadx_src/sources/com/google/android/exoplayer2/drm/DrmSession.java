package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.h;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface DrmSession {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class DrmSessionException extends IOException {

        /* renamed from: a  reason: collision with root package name */
        public final int f9601a;

        public DrmSessionException(Throwable th, int i8) {
            super(th);
            this.f9601a = i8;
        }
    }

    static void c(DrmSession drmSession, DrmSession drmSession2) {
        if (drmSession == drmSession2) {
            return;
        }
        if (drmSession2 != null) {
            drmSession2.a(null);
        }
        if (drmSession != null) {
            drmSession.b(null);
        }
    }

    void a(h.a aVar);

    void b(h.a aVar);

    UUID d();

    default boolean e() {
        return false;
    }

    Map<String, String> f();

    boolean g(String str);

    int getState();

    DrmSessionException h();

    l4.b i();
}
