package com.google.android.exoplayer2.drm;

import android.media.MediaDrmException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.m;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k implements m {
    @Override // com.google.android.exoplayer2.drm.m
    public Map<String, String> a(byte[] bArr) {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer2.drm.m
    public m.d b() {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer2.drm.m
    public l4.b c(byte[] bArr) {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer2.drm.m
    public byte[] d() {
        throw new MediaDrmException("Attempting to open a session using a dummy ExoMediaDrm.");
    }

    @Override // com.google.android.exoplayer2.drm.m
    public boolean e(byte[] bArr, String str) {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer2.drm.m
    public void f(byte[] bArr, byte[] bArr2) {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer2.drm.m
    public void g(byte[] bArr) {
    }

    @Override // com.google.android.exoplayer2.drm.m
    public void h(m.b bVar) {
    }

    @Override // com.google.android.exoplayer2.drm.m
    public byte[] i(byte[] bArr, byte[] bArr2) {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer2.drm.m
    public void j(byte[] bArr) {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer2.drm.m
    public m.a k(byte[] bArr, List<DrmInitData.SchemeData> list, int i8, HashMap<String, String> hashMap) {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer2.drm.m
    public int l() {
        return 1;
    }

    @Override // com.google.android.exoplayer2.drm.m
    public void release() {
    }
}
