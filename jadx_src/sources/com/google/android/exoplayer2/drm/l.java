package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.h;
import java.util.Map;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l implements DrmSession {

    /* renamed from: a  reason: collision with root package name */
    private final DrmSession.DrmSessionException f9626a;

    public l(DrmSession.DrmSessionException drmSessionException) {
        this.f9626a = (DrmSession.DrmSessionException) b6.a.e(drmSessionException);
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public void a(h.a aVar) {
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public void b(h.a aVar) {
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public final UUID d() {
        return i4.b.f20465a;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public boolean e() {
        return false;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public Map<String, String> f() {
        return null;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public boolean g(String str) {
        return false;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public int getState() {
        return 1;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public DrmSession.DrmSessionException h() {
        return this.f9626a;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public l4.b i() {
        return null;
    }
}
