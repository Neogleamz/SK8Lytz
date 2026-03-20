package com.google.android.exoplayer2.mediacodec;

import b6.l0;
import b6.t;
import com.google.android.exoplayer2.mediacodec.b;
import com.google.android.exoplayer2.mediacodec.j;
import com.google.android.exoplayer2.mediacodec.q;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h implements j.b {

    /* renamed from: a  reason: collision with root package name */
    private int f10017a = 0;

    /* renamed from: b  reason: collision with root package name */
    private boolean f10018b;

    @Override // com.google.android.exoplayer2.mediacodec.j.b
    public j a(j.a aVar) {
        int i8;
        int i9 = l0.f8063a;
        if (i9 < 23 || ((i8 = this.f10017a) != 1 && (i8 != 0 || i9 < 31))) {
            return new q.b().a(aVar);
        }
        int k8 = t.k(aVar.f10026c.f11207m);
        b6.p.f("DMCodecAdapterFactory", "Creating an asynchronous MediaCodec adapter for track type " + l0.k0(k8));
        return new b.C0107b(k8, this.f10018b).a(aVar);
    }
}
